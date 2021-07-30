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
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverObjetoRequest;
import pe.com.pacifico.kuntur.model.AsignacionDriverObjeto;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.repository.AsignacionDriverObjetoJpaRepository;

/**
 * <b>Class</b>: AsignacionDriverObjetoJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 04, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class AsignacionDriverObjetoJdbcRepository implements AsignacionDriverObjetoJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<AsignacionDriverObjeto> findAll(int repartoTipo, int periodo) {
    try {
      String querySelect = "SELECT A.CodCentro,\n"
          + "B.Nombre as 'nombreCentro',\n"
          + "C.GrupoGasto,\n"
          + "COALESCE(D.CodDriver,'Sin driver asignado') as 'codDriver',\n"
          + "COALESCE(E.Nombre,'Sin driver asignado') as 'nombreDriver',\n"
          + "A.Periodo,\n"
          + "A.RepartoTipo\n"
          + "FROM MOV_CENTRO A\n"
          + "JOIN MA_CENTRO B ON A.CodCentro = B.CodCentro AND A.RepartoTipo = B.RepartoTipo\n"
          + "JOIN MA_GRUPO_GASTO C ON 1=1\n"
          + "LEFT JOIN CENTRO_DRIVER_OBJETO D ON D.RepartoTipo=A.RepartoTipo AND D.CodCentro=A.CodCentro AND D.Periodo = A.Periodo \n"
          + "AND D.GrupoGasto = C.GrupoGasto\n"
          + "LEFT JOIN MA_DRIVER E ON E.CodDriver=D.CodDriver\n"
          + "WHERE A.Periodo=? AND A.RepartoTipo=?\n"
          + "AND B.Tipo IN ('LINEA','CANAL','FICTICIO','PROYECTO','SALUD')\n"
          + "ORDER BY A.CodCentro, C.GrupoGasto";

      return jdbcTemplate.query(querySelect, BeanPropertyRowMapper.newInstance(AsignacionDriverObjeto.class),
          periodo, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en Asignacion Driver Objeto ", e);
    }
    return new ArrayList<>();
  }

  @Override
  public int save(AsignacionDriverObjetoRequest asignacion) {
    int result = 0;
    try {
      result = jdbcTemplate.update("insert into CENTRO_DRIVER_OBJETO (CodCentro, RepartoTipo, Periodo, GrupoGasto,"
              + " CodDriver, FechaCreacion, FechaActualizacion)"
              + " values (?,?,?,?,?,?,?)", asignacion.getCodCentro(), asignacion.getRepartoTipo(), asignacion.getPeriodo(),
          asignacion.getGrupoGasto(), asignacion.getCodDriver(), asignacion.getFechaCreacion(),
          asignacion.getFechaActualizacion());
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
  public boolean delete(int repartoTipo, int periodo, String codigo, String grupoGasto) {
    try {
      String deleteQuery = "delete from CENTRO_DRIVER_OBJETO where RepartoTipo = ? AND Periodo = ? "
          + "AND CodCentro = ? AND GrupoGasto = ?";
      jdbcTemplate.update(deleteQuery, repartoTipo, periodo, codigo, grupoGasto);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private List<String> findAllMovCentro(int repartoTipo, int periodo) {
    try {
      log.info("Resultado DriverCentro");
      String query = "select CodCentro from MOV_CENTRO where RepartoTipo=? AND Periodo = ?";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo, periodo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovCentro", e);
    }
    return new ArrayList<>();
  }

  private List<String> findAllGrupoGasto() {
    try {
      String query = "SELECT GrupoGasto FROM MA_GRUPO_GASTO";
      return jdbcTemplate.queryForList(query, String.class);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MA_GRUPO_GASTO", e);
    }
    return new ArrayList<>();
  }

  private List<String> findAllMovDriverObjeto(int repartoTipo, int periodo) {
    try {
      String query = "select distinct CodDriverObjeto\n"
          + "from MOV_DRIVER_OBJETO\n"
          + "where Periodo = ? and RepartoTipo=?";
      return jdbcTemplate.queryForList(query, String.class, periodo, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovDriverObjeto", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> saveExcelToBd(List<List<CellData>> excel, int periodo, int repartoTipo) {
    int numErrores = 0;
    List<String> errores = new ArrayList<>();
    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("CODIGO CENTRO")
        && excel.get(0).get(1).getValue().equalsIgnoreCase("NOMBRE CENTRO")
        && excel.get(0).get(2).getValue().equalsIgnoreCase("GRUPO GASTO")
        && excel.get(0).get(3).getValue().equalsIgnoreCase("CODIGO DRIVER")
        && excel.get(0).get(4).getValue().equalsIgnoreCase("NOMBRE DRIVER")) {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //
      List<String> lstCodDriver = findAllMovDriverObjeto(repartoTipo, periodo);
      List<String> lstMovCentro = findAllMovCentro(repartoTipo, periodo);
      List<String> lstGrupoGasto = findAllGrupoGasto();

      System.out.println("Sizes: " + lstCodDriver.size() + " " + lstMovCentro.size() + " " + lstGrupoGasto.size());

      List<AsignacionDriverObjeto> lstCarga = new ArrayList();

      boolean codigoExiste;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoExiste = false;
        numFila++;

        String codDriverActual = fila.get(3).getValue();
        String codCentroActual = fila.get(0).getValue();
        String grupoGastoActual = fila.get(2).getValue();

        String codDriverBD = lstCodDriver.stream().filter(item -> codDriverActual.equals(item)).findAny().orElse(null);
        String grupoGastoBD = lstGrupoGasto.stream().filter(item -> grupoGastoActual.equals(item)).findAny().orElse(null);
        String movCentroBD = lstMovCentro.stream().filter(item -> codCentroActual.equals(item)).findAny().orElse(null);

        if (codDriverBD != null && grupoGastoBD != null && movCentroBD != null) {
          codigoExiste = true;
        }

        if (codigoExiste) {
          AsignacionDriverObjeto asignacion = new AsignacionDriverObjeto();
          asignacion.setCodDriver(codDriverActual);
          asignacion.setCodCentro(codCentroActual);
          asignacion.setGrupoGasto(grupoGastoActual);

          lstCarga.add(asignacion);
        } else {
          numErrores++;
          String error = "FILA: " + numFila + " - ERROR: ";
          if (movCentroBD == null) {
            error = error + "CODIGO CENTRO '" + codCentroActual + "' no existe en Periodo. ";
          }
          if (grupoGastoBD == null) {
            error = error + "GRUPO GASTO '" + grupoGastoActual + "' no existe. ";
          }
          if (codDriverBD == null) {
            error = error + "CODIGO DRIVER '" + codDriverActual + "' no existe en Periodo. ";
          }
          errores.add(error);
        }
      }

      System.out.println("Lst carga size: " + lstCarga.size());

      if (lstCarga.size() > 0) {

        String deleteQuery = "DELETE FROM CENTRO_DRIVER_OBJETO \n"
            + "WHERE RepartoTipo = ? AND Periodo = ?";
        try {
          jdbcTemplate.update(deleteQuery, repartoTipo, periodo);
        } catch (Exception e) {
          log.error(e.toString());
        }

        String sql = "insert into CENTRO_DRIVER_OBJETO \n"
            + "(CodCentro, RepartoTipo, Periodo, GrupoGasto, CodDriver, FechaCreacion, FechaActualizacion)\n"
            + "values (?,?,?,?,?,?,?)";

        try {
          Date date = new Date();
          String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
          jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setString(1, lstCarga.get(i).getCodCentro());//CodCentro
              ps.setInt(2, repartoTipo);//RepartoTipo
              ps.setInt(3, periodo);//Periodo
              ps.setString(4, lstCarga.get(i).getGrupoGasto());//GrupoGasto
              ps.setString(5, lstCarga.get(i).getCodDriver());//CodDriver
              ps.setString(6, fecha);//FechaCreacion
              ps.setString(7, fecha);//FechaActualizacion
            }

            @Override
            public int getBatchSize() {
              return lstCarga.size();
            }
          });
          log.info("SE SUBIERON " + numErrores + " DE " + excel.size() + " FILAS.");
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

}
