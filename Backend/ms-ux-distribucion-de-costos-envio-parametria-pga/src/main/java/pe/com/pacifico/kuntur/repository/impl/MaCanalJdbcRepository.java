package pe.com.pacifico.kuntur.repository.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pe.com.pacifico.kuntur.expose.request.MaCanalRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaCanal;
import pe.com.pacifico.kuntur.model.MovSubcanal;
import pe.com.pacifico.kuntur.repository.MaCanalJpaRepository;

/**
 * <b>Class</b>: MaCanalJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Pablo Rengifo <br/>
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
public class MaCanalJdbcRepository implements MaCanalJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  private static final String querySelect = "SELECT CodCanal, RepartoTipo, Nombre, EstaActivo, FechaCreacion, "
      + "FechaActualizacion FROM MA_CANAL WHERE RepartoTipo = ?";
  private static final String querySelectOption = querySelect + " AND codCanal = ? ";

  @Override
  public List<MaCanal> findAll(int repartoTipo) {
    try {
      log.info("Resultado MaCanal");
      return jdbcTemplate.query(querySelect, BeanPropertyRowMapper.newInstance(MaCanal.class), repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaCanal", e);
    }
    return new ArrayList<>();
  }

  @Override
  public MaCanal findByCodCanal(int repartoTipo, String codigo) {
    try {
      return
          jdbcTemplate.queryForObject(querySelectOption, new Object[]{repartoTipo,codigo},
              BeanPropertyRowMapper.newInstance(MaCanal.class));
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en canales ", e);
      return null;
    }
  }

  @Override
  public int save(MaCanalRequest canal) {
    Pattern str = Pattern.compile("[A-Z]{3}");
    if (!str.matcher(canal.getCodCanal()).matches() && !canal.getCodCanal().equals("SIN CANAL")) {
      return 0;
    }

    return jdbcTemplate.update("insert into MA_CANAL (CodCanal, RepartoTipo, Nombre, EstaActivo, FechaCreacion, FechaActualizacion) "
            + " values (?,?,?,?,?,?)", canal.getCodCanal(), canal.getRepartoTipo(), canal.getNombre(), canal.isEstaActivo(),
        canal.getFechaCreacion(),  canal.getFechaActualizacion());
  }

  @Override
  public int update(MaCanalRequest canal) {
    System.out.println(canal);
    return jdbcTemplate.update(
        new StringBuilder()
            .append(" update MA_CANAL")
            .append(" set Nombre = ? , EstaActivo = ? , FechaCreacion = ? , FechaActualizacion = ?")
            .append(" where RepartoTipo = ? AND CodCanal = ?").toString(), canal.getNombre(), canal.isEstaActivo(),
        canal.getFechaCreacion(), canal.getFechaActualizacion(), canal.getRepartoTipo(), canal.getCodCanal());
  }

  @Override
  public boolean delete(int repartoTipo, String codigo) {
    try {
      String queryExistsInMovSubCanal = "SELECT top 1 CodCanal FROM MOV_SUBCANAL"
          + " WHERE RepartoTipo = ? AND CodCanal = ? ";
      MovSubcanal ms = jdbcTemplate.queryForObject(queryExistsInMovSubCanal, new Object[]{repartoTipo,codigo},
          BeanPropertyRowMapper.newInstance(MovSubcanal.class));
      log.info("NO ES NULL, NO SE ELIMINA");
      return false;
    } catch (EmptyResultDataAccessException ex) {
      log.info("ES VACIO, SE ELIMINA");
      String deleteQuery = "delete from MA_CANAL where CodCanal = ? AND RepartoTipo = ?" ;
      jdbcTemplate.update(deleteQuery, codigo, repartoTipo);
      return true;
    } catch (Exception e) {
      log.info("ERROR");
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
  public List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo) {
    List<String> errores = new ArrayList<>();
    log.info("CANTIDAD FILAS EXCEL (CON HEADERS): " + excel.size());
    int numErrores = 0;
    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().toLowerCase().equals("codigo")
        && excel.get(0).get(1).getValue().toLowerCase().equals("nombre"))
    {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      Pattern str = Pattern.compile("[A-Z]{3}");
      List<MaCanal> lstCarga = new ArrayList<>();
      List<MaCanal> lstUpdate = new ArrayList<>();
      List<String> lstCodigos = findAllCodCanal(repartoTipo);
      MaCanal canal;
      boolean codigoExiste = false;
      boolean patronDiferente = false;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoExiste = false;
        patronDiferente = false;
        canal = new MaCanal();
        numFila++;

        String codCanalActual = fila.get(0).getValue();

        // Valida patrón

        if (!str.matcher(codCanalActual).matches() && !codCanalActual.equals("SIN CANAL")) {
          patronDiferente = true;
        }

        String codCanalBD = lstCodigos.stream().filter(item -> codCanalActual.equals(item)).findAny().orElse(null);

        //VALIDA SI CODIGO YA EXISTE EN BD
        if (codCanalBD != null) {
          codigoExiste = true;
        }

        if (!patronDiferente) {
          canal.setCodCanal(codCanalActual);
          canal.setNombre(fila.get(1).getValue());
          if (codigoExiste)
          {
            lstUpdate.add(canal);
          } else
          {
            lstCarga.add(canal);
          }
          lstCodigos.removeIf(x -> x.equals(codCanalActual));
        } else {
          numErrores++;
          if (patronDiferente) {
            errores.add("fila: " + numFila + " con codigo: " + codCanalActual + " no presenta un patron de codigo valido");
          } else {
            errores.add("fila: " + numFila + " con codigo: " + codCanalActual
                + " presenta un codigo ya existente en el sistema");
          }
        }
      }

      //Primero se inserta
      String sql = "INSERT INTO MA_CANAL"
          + " (CodCanal, RepartoTipo, Nombre, EstaActivo, FechaCreacion, FechaActualizacion) "
          + "VALUES " + "(?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstCarga.get(i).getCodCanal());//CodCanal
            ps.setInt(2, repartoTipo);//RepartoTipo
            ps.setString(3, lstCarga.get(i).getNombre());//Nombre
            ps.setInt(4, 1);//EstaActivo
            ps.setString(5, fecha);//FechaCreacion
            ps.setString(6, fecha);//FechaActualizacion
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

      //Luego actualiza
      sql =  "update MA_CANAL\n"
           + "set Nombre=?, FechaActualizacion=?\n"
           + "where RepartoTipo=? and CodCanal=?";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstUpdate.get(i).getNombre());//Nombre
            ps.setString(2, fecha);//FechaActualizacion
            ps.setInt(3, repartoTipo);//RepartoTipo
            ps.setString(4, lstUpdate.get(i).getCodCanal());//CodCanal
          }

          @Override
          public int getBatchSize() {
            return lstUpdate.size();
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
