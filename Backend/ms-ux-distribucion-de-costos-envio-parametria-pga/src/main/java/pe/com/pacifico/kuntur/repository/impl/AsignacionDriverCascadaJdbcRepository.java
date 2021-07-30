package pe.com.pacifico.kuntur.repository.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverCascadaRequest;
import pe.com.pacifico.kuntur.model.AsignacionDriverCascada;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovDriverCentro;
import pe.com.pacifico.kuntur.repository.AsignacionDriverCascadaJpaRepository;

/**
 * <b>Class</b>: AsignacionDriverCascadaJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 30, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class AsignacionDriverCascadaJdbcRepository implements AsignacionDriverCascadaJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<AsignacionDriverCascada> findAll(int repartoTipo, int periodo) {
    String periodoStr = repartoTipo == 1 ? String.valueOf(periodo) : periodo / 100 + "%" ;
    try {
      String querySelect = /*"  " +
          "  SELECT AC.CodCentro, MC.Nombre as NombreCentro, AC.CodDriver, \n" +
          "  MA.Nombre as NombreDriver, AC.FechaActualizacion, AC.FechaCreacion, AC.Periodo, AC.RepartoTipo\n" +
          "  FROM CENTRO_DRIVER AC\n" +
          "  JOIN MA_DRIVER MA ON AC.CODDRIVER = MA.CODDRIVER\n" +
          "  JOIN MA_CENTRO MC ON AC.CODCENTRO = MC.CODCENTRO\n" +
          "  WHERE AC.RepartoTipo = ? AND AC.PERIODO = ?;";*/
          "SELECT \n"
          + "\tB.CodCentro,\n"
          + "    B.NOMBRE as NombreCentro,\n"
          + "    COALESCE(C.codDriver,'Sin driver asignado') CodDriver,\n"
          + "    COALESCE(D.NOMBRE,'Sin driver asignado') NombreDriver,"
          + "    A.repartoTipo, A.periodo\n"
          + "FROM MOV_CENTRO A\n"
          + "\tJOIN MA_CENTRO B ON A.CodCentro = B.CodCentro AND A.RepartoTipo = B.RepartoTipo\n"
          + "\tLEFT JOIN CENTRO_DRIVER C ON A.RepartoTipo = C.RepartoTipo AND C.CodCentro = A.CodCentro AND C.Periodo = A.Periodo\n"
          + "\tLEFT JOIN MA_DRIVER D ON C.CodDriver = D.CodDriver\n"
          + "WHERE A.PERIODO like ? AND A.RepartoTipo = ?\n"
          + "AND B.TIPO IN ('STAFF','SOPORTE')\n"
          + "ORDER BY A.CodCentro";

      return jdbcTemplate.query(querySelect, BeanPropertyRowMapper.newInstance(AsignacionDriverCascada.class),
          periodoStr, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en Asignacion Driver Cascada ", e);
    }
    return new ArrayList<>();
  }

  private List<String> findAllCodDriver(int repartoTipo, int periodo) {
    String periodoStr = repartoTipo == 1 ? String.valueOf(periodo) : periodo / 100 + "%" ;
    try {
      log.info("Resultado DriverCentro");
      //String query = "select CodDriver from MA_DRIVER where RepartoTipo=? ";
      String query = "SELECT DISTINCT A.CodDriverCentro\n"
          + "FROM MOV_DRIVER_CENTRO A\n"
          + "JOIN MA_DRIVER B ON B.CodDriver=A.CodDriverCentro\n"
          + "WHERE A.PERIODO like ? AND B.CodDriverTipo='CECO' AND A.RepartoTipo=?\n"
          + "ORDER BY A.CodDriverCentro";
      return jdbcTemplate.queryForList(query, String.class, periodoStr, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MOV_DRIVER_CENTRO", e);
    }
    return new ArrayList<>();
  }

  private List<String> findAllMovCentro(int repartoTipo, int periodo) {
    String periodoStr = repartoTipo == 1 ? String.valueOf(periodo) : periodo / 100 + "%" ;
    try {
      log.info("Resultado DriverCentro");
      String query = "select CodCentro from MOV_CENTRO where RepartoTipo=? AND periodo like ?";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo, periodoStr);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaCentro", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String>  saveExcelToBd(List<List<CellData>> excel, int periodo, int repartoTipo) {
    List<String> errores = new ArrayList<>();
    int numErrores = 0;
    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("CODIGO CENTRO")
        && excel.get(0).get(1).getValue().equalsIgnoreCase("NOMBRE CENTRO")
        && excel.get(0).get(2).getValue().equalsIgnoreCase("CODIGO DRIVER")
        && excel.get(0).get(3).getValue().equalsIgnoreCase("NOMBRE DRIVER")) {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      List<String> lstCodDriver = findAllCodDriver(repartoTipo, periodo);
      //List<String> lstCodCentro = findAllCodCentro(repartoTipo);
      List<String> lstMovCentro = findAllMovCentro(repartoTipo, periodo);

      //System.out.println("Sizes: " + lstCodDriver.size() + " " + lstCodCentro.size() + " " + lstMovCentro.size());

      List<AsignacionDriverCascada> lstCarga = new ArrayList();

      boolean codigoExiste;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoExiste = false;
        numFila++;

        String codDriverActual = fila.get(2).getValue();
        String codCentroActual = fila.get(0).getValue();

        String codDriverBD = lstCodDriver.stream().filter(item -> codDriverActual.equals(item)).findAny().orElse(null);
        //String codCentroBD = lstCodCentro.stream().filter(item -> codCentroActual.equals(item)).findAny().orElse(null);
        String movCentroBD = lstMovCentro.stream().filter(item -> codCentroActual.equals(item)).findAny().orElse(null);

        if (codDriverBD != null && movCentroBD != null) {
          codigoExiste = true;
        }

        if (codigoExiste) {
          AsignacionDriverCascada driverCascada = new AsignacionDriverCascada();
          driverCascada.setCodDriver(codDriverActual);
          driverCascada.setCodCentro(codCentroActual);

          lstCarga.add(driverCascada);
        } else {
          numErrores++;
          String error = "FILA: " + numFila + " - ERROR: ";
          if (movCentroBD == null) {
            error = error + "CODIGO CENTRO '" + codCentroActual + "' no existe en Periodo. ";
          }
          if (codDriverBD == null) {
            error = error + "CODIGO DRIVER '" + codDriverActual + "' no existe en Periodo. ";
          }
          errores.add(error);
        }
      }

      System.out.println("Lst carga size: " + lstCarga.size());

      if (lstCarga.size() > 0) {
        // Limpiar lo viejo
        String deleteQuery = "DELETE FROM CENTRO_DRIVER WHERE RepartoTipo = ? AND Periodo = ?";
        try {
          jdbcTemplate.update(deleteQuery, repartoTipo, periodo);
        } catch (Exception e) {
          log.error(e.toString());
        }

        // Subir lo nuevo
        String sql = "insert into CENTRO_DRIVER \n"
            + "(CodCentro, CodDriver, FechaActualizacion, "
            + " FechaCreacion, Periodo, RepartoTipo)\n"
            + "values (?,?,?,?,?,?)";

        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);

        try {
          jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setString(1, lstCarga.get(i).getCodCentro());//getCodCentro
              ps.setString(2, lstCarga.get(i).getCodDriver());//getCodDriver
              ps.setString(3, fecha);//Periodo
              ps.setString(4, fecha);//CodProducto
              ps.setInt(5, periodo);//CodSubcanal
              ps.setInt(6, repartoTipo);//Porcentaje
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

  @Override
  public List<MovDriverCentro> reportByCodDriver(int repartoTipo, int periodo, String codDriver) {
    try {
      String querySelect = "  "
          + "  select dc.codDriverCentro, dc.repartoTipo, dc.periodo, dc.CodCentroDestino, mc.Nombre as nombreCentro, dc.Porcentaje"
          + "  from mov_driver_centro dc\n"
          + "  JOIN MA_CENTRO mc on dc.CodCentroDestino = mc.CodCentro\n"
          + "  where dc.CodDriverCentro = ? and dc.RepartoTipo = ? and dc.Periodo = ?";

      return jdbcTemplate.query(querySelect, BeanPropertyRowMapper.newInstance(MovDriverCentro.class),
          codDriver, repartoTipo, periodo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en reportByCodDriver Driver Cascada ", e);
    }
    return new ArrayList<>();
  }

  @Override
  public boolean delete(int repartoTipo, int periodo, String codDriverCentro) {
    try {
      String deleteQuery = "delete from CENTRO_DRIVER where periodo = ? and codCentro = ? and RepartoTipo=?";
      jdbcTemplate.update(deleteQuery, periodo, codDriverCentro, repartoTipo);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public int save(AsignacionDriverCascadaRequest req) {
    System.out.println("Save: ");
    System.out.println("Request: " + req.toString());

    Date date = new Date();
    String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);

    return jdbcTemplate.update("insert into CENTRO_DRIVER \n"
            + "(CodCentro, CodDriver, FechaActualizacion, "
            + " FechaCreacion, Periodo, RepartoTipo)\n"
            + "values (?,?,?,?,?,?)", req.getCodCentro(), req.getCodDriver(), fecha, fecha,
        req.getPeriodo(), req.getRepartoTipo());
  }
}
