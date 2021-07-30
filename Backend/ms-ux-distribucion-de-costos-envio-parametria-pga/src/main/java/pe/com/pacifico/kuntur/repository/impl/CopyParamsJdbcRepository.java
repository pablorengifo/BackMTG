package pe.com.pacifico.kuntur.repository.impl;

import java.util.Calendar;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pe.com.pacifico.kuntur.repository.CopyParamsJpaRepository;

/**
 * <b>Class</b>: CopyParamsJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Management Solutions <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 14, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class CopyParamsJdbcRepository implements CopyParamsJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public boolean copyMovCuentaContable(int repartoTipo, int periodoOri, int periodoDest) {
    try {
      String queryDelete = "DELETE FROM MOV_CUENTA_CONTABLE\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryDelete, periodoDest, repartoTipo);

      Date fecha = Calendar.getInstance().getTime();

      String queryInsert = "INSERT INTO MOV_CUENTA_CONTABLE (CodCuentaContable, RepartoTipo, Periodo, \n"
          + "FechaCreacion, FechaActualizacion, Saldo)\n"
          + "SELECT CodCuentaContable, ?, ?, \n"
          + "?, ?, 0\n"
          + "FROM MOV_CUENTA_CONTABLE\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryInsert, repartoTipo, periodoDest, fecha, fecha, periodoOri, repartoTipo);
      return true;
    } catch (Exception e) {
      log.warn(e.toString());
      return false;
    }
  }

  @Override
  public boolean copyMovPartida(int repartoTipo, int periodoOri, int periodoDest) {
    try {
      String queryDelete = "DELETE FROM MOV_PARTIDA\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryDelete, periodoDest, repartoTipo);

      Date fecha = Calendar.getInstance().getTime();

      String queryInsert = "INSERT INTO MOV_PARTIDA (CodPartida, RepartoTipo, Periodo, \n"
          + "FechaCreacion, FechaActualizacion, Saldo)\n"
          + "SELECT CodPartida, ?, ?, \n"
          + "?, ?, 0\n"
          + "FROM MOV_PARTIDA\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryInsert, repartoTipo, periodoDest, fecha, fecha, periodoOri, repartoTipo);
      return true;
    } catch (Exception e) {
      log.warn(e.toString());
      return false;
    }
  }

  @Override
  public boolean copyMovCentro(int repartoTipo, int periodoOri, int periodoDest) {
    try {
      String queryDelete = "DELETE FROM MOV_CENTRO\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryDelete, periodoDest, repartoTipo);

      Date fecha = Calendar.getInstance().getTime();

      String queryInsert = "INSERT INTO MOV_CENTRO (CodCentro, RepartoTipo, Periodo, \n"
          + "FechaCreacion, FechaActualizacion, Iteracion, Saldo)\n"
          + "SELECT CodCentro, ?, ?, \n"
          + "?, ?, -2, 0\n"
          + "FROM MOV_CENTRO\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryInsert, repartoTipo, periodoDest, fecha, fecha, periodoOri, repartoTipo);
      return true;
    } catch (Exception e) {
      log.warn(e.toString());
      return false;
    }
  }

  @Override
  public boolean copyMovProducto(int repartoTipo, int periodoOri, int periodoDest) {
    try {
      String queryDelete = "DELETE FROM MOV_PRODUCTO\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryDelete, periodoDest, repartoTipo);

      Date fecha = Calendar.getInstance().getTime();

      String queryInsert = "INSERT INTO MOV_PRODUCTO (CodProducto, RepartoTipo, Periodo, \n"
          + "FechaCreacion, FechaActualizacion, CodLinea)\n"
          + "SELECT CodProducto, ?, ?, \n"
          + "?, ?, CodLinea\n"
          + "FROM MOV_PRODUCTO\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryInsert, repartoTipo, periodoDest, fecha, fecha, periodoOri, repartoTipo);
      return true;
    } catch (Exception e) {
      log.warn(e.toString());
      return false;
    }
  }

  @Override
  public boolean copyMovSubcanal(int repartoTipo, int periodoOri, int periodoDest) {
    try {
      String queryDelete = "DELETE FROM MOV_SUBCANAL\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryDelete, periodoDest, repartoTipo);

      Date fecha = Calendar.getInstance().getTime();

      String queryInsert = "INSERT INTO MOV_SUBCANAL (CodSubcanal, RepartoTipo, Periodo, \n"
          + "FechaCreacion, FechaActualizacion, CodCanal)\n"
          + "SELECT CodSubcanal, ?, ?, \n"
          + "?, ?, CodCanal\n"
          + "FROM MOV_SUBCANAL\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryInsert, repartoTipo, periodoDest, fecha, fecha, periodoOri, repartoTipo);
      return true;
    } catch (Exception e) {
      log.warn(e.toString());
      return false;
    }
  }

  @Override
  public boolean copyMovDriverCentro(int repartoTipo, int periodoOri, int periodoDest) {
    try {
      String queryDelete = "DELETE FROM MOV_DRIVER_CENTRO\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryDelete, periodoDest, repartoTipo);

      Date fecha = Calendar.getInstance().getTime();

      String queryInsert = "INSERT INTO MOV_DRIVER_CENTRO (CodDriverCentro, RepartoTipo, Periodo, \n"
          + "CodCentroDestino, FechaCreacion, FechaActualizacion, Porcentaje)\n"
          + "SELECT CodDriverCentro, ?, ?, CodCentroDestino,\n"
          + "?, ?, Porcentaje\n"
          + "FROM MOV_DRIVER_CENTRO\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryInsert, repartoTipo, periodoDest, fecha, fecha, periodoOri, repartoTipo);
      return true;
    } catch (Exception e) {
      log.warn(e.toString());
      return false;
    }
  }

  @Override
  public boolean copyMovDriverObjeto(int repartoTipo, int periodoOri, int periodoDest) {
    try {
      String queryDelete = "DELETE FROM MOV_DRIVER_OBJETO\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryDelete, periodoDest, repartoTipo);

      Date fecha = Calendar.getInstance().getTime();

      String queryInsert = "INSERT INTO MOV_DRIVER_OBJETO (CodDriverObjeto, RepartoTipo, Periodo, \n"
          + "CodProducto, CodSubcanal, FechaCreacion, FechaActualizacion, Porcentaje)\n"
          + "SELECT CodDriverObjeto, ?, ?, CodProducto, CodSubcanal,\n"
          + "?, ?, Porcentaje\n"
          + "FROM MOV_DRIVER_OBJETO\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryInsert, repartoTipo, periodoDest, fecha, fecha, periodoOri, repartoTipo);
      return true;
    } catch (Exception e) {
      log.warn(e.toString());
      return false;
    }
  }

  @Override
  public boolean copyAsignacionCecoBolsa(int repartoTipo, int periodoOri, int periodoDest) {
    try {
      String queryDelete = "DELETE FROM CUENTA_PARTIDA_CENTRO_DRIVER_CENTRO\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryDelete, periodoDest, repartoTipo);

      Date fecha = Calendar.getInstance().getTime();

      String queryInsert = "INSERT INTO CUENTA_PARTIDA_CENTRO_DRIVER_CENTRO (CodCuentaContable, CodPartida, CodCentro, \n"
          + "RepartoTipo, Periodo, CodDriver, FechaCreacion, FechaActualizacion)\n"
          + "SELECT CodCuentaContable, CodPartida, CodCentro, ?, ?, CodDriver,\n"
          + "?, ?\n"
          + "FROM CUENTA_PARTIDA_CENTRO_DRIVER_CENTRO\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryInsert, repartoTipo, periodoDest, fecha, fecha, periodoOri, repartoTipo);
      return true;
    } catch (Exception e) {
      log.warn(e.toString());
      return false;
    }
  }

  @Override
  public boolean copyAsignacionDriverCascada(int repartoTipo, int periodoOri, int periodoDest) {
    try {
      String queryDelete = "DELETE FROM CENTRO_DRIVER\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryDelete, periodoDest, repartoTipo);

      Date fecha = Calendar.getInstance().getTime();

      String queryInsert = "INSERT INTO CENTRO_DRIVER (CodCentro, RepartoTipo, Periodo, \n"
          + "CodDriver, FechaCreacion, FechaActualizacion)\n"
          + "SELECT CodCentro, ?, ?, CodDriver,\n"
          + "?, ?\n"
          + "FROM CENTRO_DRIVER\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryInsert, repartoTipo, periodoDest, fecha, fecha, periodoOri, repartoTipo);
      return true;
    } catch (Exception e) {
      log.warn(e.toString());
      return false;
    }
  }

  @Override
  public boolean copyAsignacionDriverObjeto(int repartoTipo, int periodoOri, int periodoDest) {
    try {
      String queryDelete = "DELETE FROM CENTRO_DRIVER_OBJETO\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryDelete, periodoDest, repartoTipo);

      Date fecha = Calendar.getInstance().getTime();

      String queryInsert = "INSERT INTO CENTRO_DRIVER_OBJETO (CodCentro, RepartoTipo, Periodo, \n"
          + "GrupoGasto, CodDriver, FechaCreacion, FechaActualizacion)\n"
          + "SELECT CodCentro, ?, ?, GrupoGasto, CodDriver,\n"
          + "?, ?\n"
          + "FROM CENTRO_DRIVER_OBJETO\n"
          + "WHERE Periodo = ?\n"
          + "AND RepartoTipo = ?";
      jdbcTemplate.update(queryInsert, repartoTipo, periodoDest, fecha, fecha, periodoOri, repartoTipo);
      return true;
    } catch (Exception e) {
      log.warn(e.toString());
      return false;
    }
  }
}
