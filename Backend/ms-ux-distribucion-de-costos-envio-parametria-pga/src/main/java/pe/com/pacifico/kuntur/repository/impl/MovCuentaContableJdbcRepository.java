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
import pe.com.pacifico.kuntur.expose.request.MovCuentaContableRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovCuentaContable;
import pe.com.pacifico.kuntur.repository.MovCuentaContableJpaRepository;

/**
 * <b>Class</b>: MovCuentaContableJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Pablo Rengifo <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     Mar 31, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MovCuentaContableJdbcRepository implements MovCuentaContableJpaRepository {

  private final JdbcTemplate jdbcTemplate;


  @Override
  public List<MovCuentaContable> findAll(int repartoTipo, int periodo) {
    try {
      log.info("Resultado MovCuentaContable");
      String queryAllWithNombre = "SELECT mo.codCuentaContable, ma.nombre, mo.fechaCreacion, mo.fechaActualizacion,"
          + " mo.periodo, mo.repartoTipo, mo.saldo from MOV_CUENTA_CONTABLE mo"
          + " join MA_CUENTA_CONTABLE ma on mo.codCuentaContable = ma.codCuentaContable"
          + " AND mo.repartoTipo = ma.repartoTipo"
          + " where mo.repartoTipo = ? AND mo.periodo = ?";
      return jdbcTemplate.query(queryAllWithNombre, BeanPropertyRowMapper.newInstance(MovCuentaContable.class), repartoTipo, periodo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovCuentaContable", e);
    }
    return new ArrayList<>();
  }

  @Override
  public int save(MovCuentaContableRequest cuenta) {
    return  jdbcTemplate.update("insert into MOV_CUENTA_CONTABLE (codCuentaContable, fechaCreacion, fechaActualizacion, "
            + "periodo, repartoTipo, saldo) "
            + " values (?,?,?,?,?,?)", cuenta.getCodCuentaContable(), cuenta.getFechaCreacion(), cuenta.getFechaActualizacion(),
        cuenta.getPeriodo(), cuenta.getRepartoTipo(), cuenta.getSaldo());

  }

  @Override
  public boolean delete(int repartoTipo, int periodo, String codigo) {
    try {
      String deleteQuery = "delete from MOV_CUENTA_CONTABLE where repartoTipo = ? AND periodo = ? AND codCuentaContable = ?";
      jdbcTemplate.update(deleteQuery,repartoTipo, periodo, codigo);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public List<String> findAllCodCuentaContable(int repartoTipo) {
    try {
      log.info("Resultado CuentaContable");
      String query = "SELECT CodCuentaContable FROM MA_CUENTA_CONTABLE WHERE RepartoTipo = ?";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en CuentaContable", e);
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
      List<MovCuentaContable> lstCarga = new ArrayList<>();
      List<String> lstCodigos = findAllCodCuentaContable(repartoTipo);
      MovCuentaContable movCuenta;
      boolean codigoExiste;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoExiste = false;
        movCuenta = new MovCuentaContable();
        numFila++;

        String codCuentaActual = fila.get(0).getValue();

        String codCuentaBD = lstCodigos.stream().filter(codCuentaActual::equals).findAny().orElse(null);

        //VALIDA QUE CODIGO SI EXISTA EN BD
        if (codCuentaBD != null) {
          codigoExiste = true;
        }

        if (codigoExiste) {
          movCuenta.setCodCuentaContable(codCuentaActual);
          lstCarga.add(movCuenta);
          //lstCodigos.removeIf(x -> x.equals(codCuentaActual));
        } else {
          errores.add("FILA: " + numFila + " con codigo: " + codCuentaActual
              + " presenta un codigo que no existe en Catalogo");
          numErrores++;
        }
      }
      if (lstCarga.size() > 0) {
        String deleteQuery = "DELETE FROM MOV_CUENTA_CONTABLE WHERE RepartoTipo = ? AND Periodo = ?";
        jdbcTemplate.update(deleteQuery, repartoTipo, periodo);
      }

      String sql = "INSERT INTO MOV_CUENTA_CONTABLE"
          + " (codCuentaContable, fechaCreacion, fechaActualizacion, periodo, repartoTipo, saldo) "
          + "VALUES " + "(?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstCarga.get(i).getCodCuentaContable());//CodCuentaContable
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
