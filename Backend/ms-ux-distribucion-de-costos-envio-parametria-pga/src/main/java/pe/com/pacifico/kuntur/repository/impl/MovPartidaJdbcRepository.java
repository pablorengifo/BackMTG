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
import pe.com.pacifico.kuntur.expose.request.MovPartidaRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovPartida;
import pe.com.pacifico.kuntur.repository.MovPartidaJpaRepository;

/**
 * <b>Class</b>: MovPartidaJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Pablo Rengifo <br/>
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
public class MovPartidaJdbcRepository implements MovPartidaJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<MovPartida> findAll(int repartoTipo, int periodo) {
    try {
      log.info("Resultado MovPartida");
      String queryAllWithNombre = "SELECT mo.codPartida, ma.nombre, mo.fechaCreacion, mo.fechaActualizacion, mo.periodo,"
          + " mo.repartoTipo, mo.saldo from MOV_PARTIDA mo"
          + " join MA_PARTIDA ma on mo.codPartida = ma.codPartida"
          + " AND mo.repartoTipo = ma.repartoTipo"
          + " where mo.repartoTipo = ? AND mo.periodo = ?";
      return jdbcTemplate.query(queryAllWithNombre, BeanPropertyRowMapper.newInstance(MovPartida.class), repartoTipo, periodo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovPartida", e);
    }
    return new ArrayList<>();
  }

  @Override
  public int save(MovPartidaRequest partida) {
    int result;
    try {
      result = jdbcTemplate.update("insert into MOV_PARTIDA (codPartida, fechaCreacion, fechaActualizacion, "
              + "periodo, repartoTipo, saldo) "
              + " values (?,?,?,?,?,?)", partida.getCodPartida(), partida.getFechaCreacion(), partida.getFechaActualizacion(),
          partida.getPeriodo(), partida.getRepartoTipo(), partida.getSaldo());
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
      String deleteQuery = "delete from MOV_PARTIDA where repartoTipo = ? AND periodo = ? AND codPartida = ?";
      jdbcTemplate.update(deleteQuery, repartoTipo, periodo, codigo);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public List<String> findAllCodPartida(int repartoTipo) {
    try {
      log.info("Resultado MaPartida");
      String query = "SELECT CodPartida FROM MA_PARTIDA WHERE RepartoTipo = ?";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaPartida", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo, int periodo) {
    List<String> errores = new ArrayList<>();
    log.info("CANTIDAD FILAS EXCEL (CON HEADERS): " + excel.size());
    int numErrores = 0;
    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("codigo"))
    {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      List<MovPartida> lstCarga = new ArrayList<>();
      List<String> lstCodigos = findAllCodPartida(repartoTipo);
      MovPartida movPartida;
      boolean codigoExiste;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoExiste = false;
        movPartida = new MovPartida();
        numFila++;

        String codPartidaActual = fila.get(0).getValue();

        String codPartidaBD = lstCodigos.stream().filter(codPartidaActual::equals).findAny().orElse(null);

        //VALIDA QUE CODIGO SI EXISTA EN BD
        if (codPartidaBD != null) {
          codigoExiste = true;
        }

        if (codigoExiste) {
          movPartida.setCodPartida(codPartidaActual);
          lstCarga.add(movPartida);
          //lstCodigos.removeIf(x -> x.equals(codPartidaActual));
        } else {
          numErrores++;
          errores.add("FILA: " + numFila + " con codigo: " + codPartidaActual
              + " presenta un codigo que no existe en Catalogo");
        }
      }
      if (lstCarga.size() > 0) {
        String deleteQuery = "DELETE FROM MOV_PARTIDA WHERE RepartoTipo = ? AND Periodo = ?";
        jdbcTemplate.update(deleteQuery, repartoTipo, periodo);
      }

      String sql = "INSERT INTO MOV_PARTIDA"
          + " (codPartida, fechaCreacion, fechaActualizacion, periodo, repartoTipo, saldo) "
          + "VALUES " + "(?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstCarga.get(i).getCodPartida());//CodPartida
            ps.setString(2, fecha);//FechaCreacion
            ps.setString(3, fecha);//FechaActualizacion
            ps.setInt(4,periodo);//Periodo
            ps.setInt(5, repartoTipo);//RepartoTipo
            ps.setDouble(6,0);//Saldo
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
