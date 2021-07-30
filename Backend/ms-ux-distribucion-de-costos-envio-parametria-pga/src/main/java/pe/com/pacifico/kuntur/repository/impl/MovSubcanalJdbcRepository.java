package pe.com.pacifico.kuntur.repository.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pe.com.pacifico.kuntur.expose.request.MovSubcanalRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovSubcanal;
import pe.com.pacifico.kuntur.repository.MovSubcanalJpaRepository;

/**
 * <b>Class</b>: MovSubcanalJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 16, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MovSubcanalJdbcRepository implements MovSubcanalJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<MovSubcanal> findAll(int repartoTipo, int periodo) {
    try {
      log.info("Resultado MovSubcanal");
      String queryAllWithNombre = "SELECT mo.CodSubcanal, ma.Nombre, mo.Periodo, mo.RepartoTipo, mo.FechaCreacion,"
          + " mo.FechaActualizacion, mo.CodCanal, mc.Nombre as 'NombreCanal' FROM MOV_SUBCANAL mo"
          + " JOIN MA_SUBCANAL ma ON mo.CodSubcanal = ma.CodSubcanal"
          + " AND mo.RepartoTipo = ma.RepartoTipo"
          + " JOIN MA_CANAL mc ON mo.CodCanal = mc.CodCanal"
          + " AND mo.RepartoTipo = mc.RepartoTipo"
          + " WHERE mo.RepartoTipo = ? AND mo.Periodo = ?";
      return jdbcTemplate.query(queryAllWithNombre, BeanPropertyRowMapper.newInstance(MovSubcanal.class), repartoTipo, periodo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovSubcanal", e);
    }
    return new ArrayList<>();
  }

  @Override
  public int save(MovSubcanalRequest subcanal) {
    int result = 0;
    try {
      result = jdbcTemplate.update("insert into MOV_SUBCANAL (CodSubcanal, Periodo, RepartoTipo,"
              + " FechaCreacion, FechaActualizacion, CodCanal) "
              + " values (?,?,?,?,?,?)", subcanal.getCodSubcanal(), subcanal.getPeriodo(), subcanal.getRepartoTipo(),
          subcanal.getFechaCreacion(), subcanal.getFechaActualizacion(), subcanal.getCodCanal());
    } catch (DuplicateKeyException e) {
      result = -1;
      log.warn(e.toString());
    } catch (Exception e) {
      log.warn(e.toString());
      result = -2;
    }
    log.info("Result save = " + result);
    return result;
  }

  @Override
  public boolean delete(int repartoTipo, int periodo, String codigo) {
    try {
      String deleteQuery = "delete from MOV_SUBCANAL where RepartoTipo = ? AND Periodo = ? AND CodSubcanal = ?";
      jdbcTemplate.update(deleteQuery, repartoTipo, periodo, codigo);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public List<String> findAllCodCanal(int repartoTipo) {
    try {
      log.info("Resultado MaCanal");
      String query = "SELECT CodCanal FROM MA_CANAL WHERE RepartoTipo = ?";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaCanal", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> findAllCodSubcanal(int repartoTipo) {
    try {
      log.info("Resultado MaSubcanal");
      String query = "SELECT CodSubcanal FROM MA_SUBCANAL WHERE RepartoTipo = ?";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaSubcanal", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo, int periodo) {
    int numErrores = 0;
    List<String> errores = new ArrayList<>();
    log.info("CANTIDAD FILAS EXCEL (CON HEADERS): " + excel.size());
    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().toLowerCase().equals("codigo")
        && excel.get(0).get(3).getValue().toLowerCase().equals("codigo padre"))
    {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      List<MovSubcanal> lstCarga = new ArrayList<>();
      List<String> lstCodigos = findAllCodSubcanal(repartoTipo);
      List<String> lstCodigosCanal = findAllCodCanal(repartoTipo);
      MovSubcanal movSubcanal;
      boolean codigoExiste = false;
      boolean codigoCanalExiste = false;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoExiste = false;
        codigoCanalExiste = false;
        movSubcanal = new MovSubcanal();
        numFila++;

        String codSubcanalActual = fila.get(0).getValue();

        String codSubcanalBD = lstCodigos.stream().filter(item -> codSubcanalActual.equals(item)).findAny().orElse(null);

        String codCanalActual = fila.get(3).getValue();

        String codCanalBD = lstCodigosCanal.stream().filter(item -> codCanalActual.equals(item)).findAny().orElse(null);

        //VALIDA QUE CODIGO SI EXISTA EN BD
        if (codSubcanalBD != null) {
          codigoExiste = true;
        }

        //VALIDA QUE CODIGO CANAL SI EXISTA EN BD
        if (codCanalBD != null) {
          codigoCanalExiste = true;
        }

        if (codigoExiste && codigoCanalExiste) {
          movSubcanal.setCodSubcanal(codSubcanalActual);
          movSubcanal.setCodCanal(codCanalActual);
          lstCarga.add(movSubcanal);
          //lstCodigos.removeIf(x -> x.equals(codSubcanalActual));
        } else {
          numErrores++;
          if (!codigoExiste)
          {
            errores.add("FILA: " + numFila + " con codigo: " + codSubcanalActual
                + " presenta un codigo que no existe en Catalogo de subcanales");
          }
          if (!codigoCanalExiste)
          {
            errores.add("FILA: " + numFila + " con codigo: " + codSubcanalActual
                + " presenta un codigo de canal: " + codCanalActual + " que no existe en Catalogo de canales");
          }
        }
      }
      if (lstCarga.size() > 0) {
        String deleteQuery = "DELETE FROM MOV_SUBCANAL WHERE RepartoTipo = ? AND Periodo = ?";
        jdbcTemplate.update(deleteQuery, repartoTipo, periodo);
      }

      String sql = "INSERT INTO MOV_SUBCANAL"
          + " (CodSubcanal, Periodo, RepartoTipo, FechaCreacion, FechaActualizacion, CodCanal) "
          + "VALUES " + "(?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstCarga.get(i).getCodSubcanal());//CodSubcanal
            ps.setInt(2,periodo);//Periodo
            ps.setInt(3, repartoTipo);//RepartoTipo
            ps.setString(4, fecha);//FechaCreacion
            ps.setString(5, fecha);//FechaActualizacion
            ps.setString(6, lstCarga.get(i).getCodCanal());//CodCanal
          }

          @Override
          public int getBatchSize() {
            return lstCarga.size();
          }
        });
        log.info("SE SUBIERON " + lstCarga.size() + " DE " + excel.size() + " FILAS.");
      } catch (Exception e) {
        log.error(e.toString());
        numErrores = excel.size();
        errores.add("Se tiene un error al registrar el excel. Verifique que los datos sean correctos");
      }
    } else {
      errores.add("Se tiene un error de formato de cabeceras");
      return errores;
    }
    if (errores.size() > 0) {
      errores.add(0,numErrores + " filas de " + excel.size()
          + " no se subieron por los siguientes errores: ");
      for (String fila : errores) {
        log.info(fila);
      }
      return errores;
    } else { return null; }
  }

}
