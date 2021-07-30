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
import pe.com.pacifico.kuntur.expose.request.MovCentroRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovCentro;
import pe.com.pacifico.kuntur.model.MovCentro;
import pe.com.pacifico.kuntur.repository.MovCentroJpaRepository;

/**
 * <b>Class</b>: MovCentroJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 12, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MovCentroJdbcRepository implements MovCentroJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  private static final String queryCoverage = "SELECT CodCentro, CodCentroOrigen, CodCuentaContableOrigen, CodEntidadOrigen, "
      + "CodCentroOrigen, FechaActualizacion, FechaCreacion, GrupoGasto, Iteracion, Periodo, RepartoTipo, Saldo "
      + "FROM MOV_CENTRO";
  private static final String queryCoverageOption = queryCoverage + " where CodCentro = ? ";

  @Override
  public List<MovCentro> findAll(int repartoTipo, int periodo) {
    try {
      log.info("Resultado MovCentro");
      String queryAllWithNombre = "SELECT mo.CodCentro, ma.Nombre,ma.Tipo, mo.CodCentroOrigen, mo.CodCuentaContableOrigen, "
          + "mo.CodEntidadOrigen,"
          + " mo.CodPartidaOrigen, mo.FechaActualizacion, mo.FechaCreacion, mo.GrupoGasto, mo.Iteracion, mo.Periodo, mo.RepartoTipo,"
          + " mo.Saldo FROM MOV_CENTRO mo"
          + " JOIN MA_CENTRO ma ON mo.CodCentro = ma.CodCentro"
          + " AND mo.RepartoTipo = ma.RepartoTipo"
          + " where mo.RepartoTipo = ? AND mo.Periodo = ?";
      //String conRepTipo = " and mo.RepartoTipo = " + repartoTipo;
      return jdbcTemplate.query(queryAllWithNombre, BeanPropertyRowMapper.newInstance(MovCentro.class), repartoTipo, periodo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovCentro", e);
    }
    return new ArrayList<>();
  }

  @Override
  public MovCentro findByCodCentro(String codigo) {
    try {
      return
          jdbcTemplate.queryForObject(queryCoverageOption, new Object[]{codigo}, BeanPropertyRowMapper.newInstance(MovCentro.class));
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en mov centros ", e);
      return null;
    }
  }

  @Override
  public int save(MovCentroRequest centro) {
    int result = 0;
    try {
      result = jdbcTemplate.update("insert into MOV_CENTRO (CodCentro, CodCentroOrigen, CodCuentaContableOrigen, CodEntidadOrigen,"
              + " CodPartidaOrigen, FechaCreacion, FechaActualizacion, GrupoGasto, Iteracion, Periodo, RepartoTipo, Saldo) "
              + " values (?,?,?,?,?,?,?,?,?,?,?,?)", centro.getCodCentro(), centro.getCodCentroOrigen(),
          centro.getCodCuentaContableOrigen(), centro.getCodEntidadOrigen(), centro.getCodPartidaOrigen(),
          centro.getFechaCreacion(), centro.getFechaActualizacion(), centro.getGrupoGasto(), -2,
          centro.getPeriodo(), centro.getRepartoTipo(), centro.getSaldo());
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
  public int update(MovCentroRequest centro) {
    System.out.println(centro);
    return jdbcTemplate.update(
        new StringBuilder()
            .append(" update MOV_CENTRO ")
            .append(" set CodCentroOrigen = ? , CodCuentaContableOrigen = ? , CodEntidadOrigen = ? , CodCentroOrigen = ? ,")
            .append(" FechaCreacion = ? , FechaActualizacion = ? , GrupoGasto = ? , Iteracion = ? , Periodo = ? ,")
            .append(" RepartoTipo = ? , Saldo = ?")
            .append(" where CodCentro = ?").toString(), centro.getCodCentroOrigen(),
        centro.getCodCuentaContableOrigen(), centro.getCodEntidadOrigen(), centro.getCodCentroOrigen(),
        centro.getFechaCreacion(), centro.getFechaActualizacion(), centro.getGrupoGasto(), -2,
        centro.getPeriodo(), centro.getRepartoTipo(), centro.getSaldo(), centro.getCodCentro());

  }

  @Override
  public boolean delete(int repartoTipo, int periodo, String codigo) {
    try {
      String deleteQuery = "delete from MOV_CENTRO where periodo = ? and codCentro = ? and RepartoTipo=?";
      jdbcTemplate.update(deleteQuery, periodo, codigo,repartoTipo);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public List<String> findAllCodCentro(int repartoTipo) {
    try {
      log.info("Resultado MaCentro");
      String query = "SELECT CodCentro FROM MA_CENTRO WHERE RepartoTipo = ?";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaCentro", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo, int periodo) {
    List<String> errores = new ArrayList<>();
    int numErrores = 0;
    log.info("CANTIDAD FILAS EXCEL (CON HEADERS): " + excel.size());
    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().toLowerCase().equals("codigo"))
    {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      List<MovCentro> lstCarga = new ArrayList<>();
      List<String> lstCodigos = findAllCodCentro(repartoTipo);
      MovCentro movCentro;
      boolean codigoExiste = false;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoExiste = false;
        movCentro = new MovCentro();
        numFila++;

        String codCentroActual = fila.get(0).getValue();

        String codCentroBD = lstCodigos.stream().filter(item -> codCentroActual.equals(item)).findAny().orElse(null);

        //VALIDA QUE CODIGO SI EXISTA EN BD
        if (codCentroBD != null) {
          codigoExiste = true;
        }

        if (codigoExiste) {
          movCentro.setCodCentro(codCentroActual);
          lstCarga.add(movCentro);
          //lstCodigos.removeIf(x -> x.equals(codCentroActual));
        } else {
          errores.add("FILA: " + numFila + " con codigo: " + codCentroActual
              + " presenta un codigo que no existe en Catalogo");
          numErrores++;
        }
      }
      if (lstCarga.size() > 0) {
        String deleteQuery = "DELETE FROM MOV_CENTRO WHERE RepartoTipo = ? AND Periodo = ?";
        jdbcTemplate.update(deleteQuery, repartoTipo, periodo);
      }

      String sql = "INSERT INTO MOV_CENTRO"
          + " (codCentro, fechaCreacion, fechaActualizacion, periodo, repartoTipo, saldo, Iteracion) "
          + "VALUES " + "(?,?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstCarga.get(i).getCodCentro());//CodCentro
            ps.setString(2, fecha);//FechaCreacion
            ps.setString(3, fecha);//FechaActualizacion
            ps.setInt(4,periodo);//Periodo
            ps.setInt(5, repartoTipo);//RepartoTipo
            ps.setDouble(6,0);//Saldo
            ps.setInt(7,-2);//Iteracion
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
