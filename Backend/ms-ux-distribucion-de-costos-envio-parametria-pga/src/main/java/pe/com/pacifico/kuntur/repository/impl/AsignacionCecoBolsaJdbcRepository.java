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
import pe.com.pacifico.kuntur.model.AsignacionCecoBolsa;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovDriverCentro;
import pe.com.pacifico.kuntur.repository.AsignacionCecoBolsaJpaRepository;



/**
 * <b>Class</b>: AsignacionCecoBolsaJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
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
public class AsignacionCecoBolsaJdbcRepository implements AsignacionCecoBolsaJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<AsignacionCecoBolsa> findAll(int repartoTipo, int periodo) {
    String periodoStr = repartoTipo == 1 ? String.valueOf(periodo) : String.valueOf(periodo / 100) + "%" ;
    try {

      String querySelect = "select  cpcd.CodCuentaContable,cc.Nombre as 'NombreCuentaContable', cpcd.CodPartida,\n"
          + "pa.Nombre as 'NombrePartida',cpcd.CodCentro, ce.Nombre 'NombreCentro',cpcd.CodDriver,dr.Nombre as 'NombreDriver' \n"
          + "from CUENTA_PARTIDA_CENTRO_DRIVER_CENTRO cpcd \n"
          + "inner join  MA_CUENTA_CONTABLE cc on cpcd.CodCuentaContable=cc.CodCuentaContable \n"
          + "inner join  MA_PARTIDA pa on cpcd.CodPartida=pa.CodPartida \n"
          + "inner join  MA_CENTRO ce on cpcd.CodCentro=ce.CodCentro \n"
          + "inner join MA_DRIVER dr on cpcd.CodDriver=dr.CodDriver\n"
          + "where cpcd.Periodo like ? and cpcd.RepartoTipo=? and cc.RepartoTipo=? \n"
          + "and pa.RepartoTipo = ? and ce.RepartoTipo=? and dr.RepartoTipo=?;";

      return jdbcTemplate.query(querySelect, BeanPropertyRowMapper.newInstance(AsignacionCecoBolsa.class),
          periodoStr, repartoTipo, repartoTipo, repartoTipo,repartoTipo,repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en Asignacion Ceco bolsa ", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> saveExcelToBd(List<List<CellData>> excel, int periodo, int repartoTipo) {
    List<String> errores = new ArrayList<>();
    int numErrores = 0;
    log.info("CANTIDAD FILAS EXCEL (CON HEADERS): " + excel.size());
    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("CODIGO CUENTA CONTABLE")
        && excel.get(0).get(2).getValue().equalsIgnoreCase("CODIGO PARTIDA")
        && excel.get(0).get(4).getValue().equalsIgnoreCase("CODIGO CENTRO")
        && excel.get(0).get(6).getValue().equalsIgnoreCase("CODIGO DRIVER")) {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      List<AsignacionCecoBolsa> lstCarga = new ArrayList<>();

      List<String> lstCodigosCuentaContable = findAllCodCuentaContable(repartoTipo, periodo);
      List<String> lstCodigosPartidas = findAllCodPartida(repartoTipo, periodo);
      List<String> lstCodigosCentros = findAllCodCentro(repartoTipo, periodo);
      List<String> lstCodigosDriver = findAllCodDriver(repartoTipo, periodo);
      AsignacionCecoBolsa asignacionCecoBolsa;
      boolean combinacionPosible;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        combinacionPosible = false;
        asignacionCecoBolsa = new AsignacionCecoBolsa();
        numFila++;

        String codCuentaActual = fila.get(0).getValue();
        String codPartidaActual = fila.get(2).getValue();
        String codCentroActual = fila.get(4).getValue();
        String codDriverActual = fila.get(6).getValue();

        String codCuentaBD = lstCodigosCuentaContable.stream().filter(codCuentaActual::equals).findAny().orElse(null);
        String codPartidaBD = lstCodigosPartidas.stream().filter(codPartidaActual::equals).findAny().orElse(null);
        String codCentroBD = lstCodigosCentros.stream().filter(codCentroActual::equals).findAny().orElse(null);
        String codDriverBD = lstCodigosDriver.stream().filter(codDriverActual::equals).findAny().orElse(null);

        //VALIDA QUE los codigos  SI EXISTAN EN BD
        if (codCuentaBD != null && codCentroBD != null && codPartidaBD != null && codDriverBD != null) {
          combinacionPosible = true;
        }

        if (combinacionPosible) {
          asignacionCecoBolsa.setCodCuentaContable(codCuentaActual);
          asignacionCecoBolsa.setCodPartida(codPartidaActual);
          asignacionCecoBolsa.setCodCentro(codCentroActual);
          asignacionCecoBolsa.setCodDriver(codDriverActual);
          lstCarga.add(asignacionCecoBolsa);
          //lstCodigos.removeIf(x -> x.equals(codCuentaActual));
        } else {
          numErrores++;
          String error = "FILA: " + numFila + " presenta el siguiente error: ";
          if (codCuentaBD == null) { error = error + "CODIGO CUENTA CONTABLE '" + codCuentaActual + "' no existe en Periodo. "; }
          if (codPartidaBD == null) { error = error + "CODIGO PARTIDA '" + codPartidaActual + "' no existe en Periodo. "; }
          if (codCentroBD == null) { error = error + "CODIGO CENTRO '" + codCentroActual + "' no existe en Periodo. "; }
          if (codDriverBD == null) { error = error + "CODIGO DRIVER '" + codDriverActual + "' no existe en Periodo. "; }
          errores.add(error);
        }
      }

      //se usa para limpiar la tabla antes de agregar la nueva data
      if (lstCarga.size() > 0) {
        String deleteQuery = "DELETE FROM CUENTA_PARTIDA_CENTRO_DRIVER_CENTRO WHERE RepartoTipo=? and periodo =?";
        try {
          jdbcTemplate.update(deleteQuery, repartoTipo, periodo);
        } catch (Exception e) {
          log.error(e.toString());
        }

        String sql = "insert into CUENTA_PARTIDA_CENTRO_DRIVER_CENTRO\n"
            + "(CodCentro,CodCuentaContable,CodDriver,CodPartida,FechaActualizacion,FechaCreacion,Periodo,RepartoTipo)\n"
            + "values (?,?,?,?,?,?,?,?)";

        try {
          Date date = new Date();
          String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
          jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setString(1, lstCarga.get(i).getCodCentro());
              ps.setString(2, lstCarga.get(i).getCodCuentaContable());
              ps.setString(3, lstCarga.get(i).getCodDriver());
              ps.setString(4, lstCarga.get(i).getCodPartida());
              ps.setString(5, fecha);
              ps.setString(6, fecha);
              ps.setInt(7, periodo);
              ps.setDouble(8, repartoTipo);
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

        if (errores.size() > 0) {
          errores.add(0,numErrores + " filas de " + excel.size()
              + " no se subieron por los siguientes errores: ");
        }
      }
    } else {
      errores.add("El archivo no se pudo subir por los siguientes errores de formato: ");
      if (!excel.get(0).get(0).getValue().equalsIgnoreCase("codigo cuenta contable"))
      { errores.add("La primera fila debe llamarse codigo cuenta contable"); }
      if (!excel.get(0).get(2).getValue().equalsIgnoreCase("codigo partida"))
      { errores.add("La primera fila debe llamarse codigo partida"); }
      if (!excel.get(0).get(4).getValue().equalsIgnoreCase("codigo centro"))
      { errores.add("La primera fila debe llamarse codigo centro"); }
      if (!excel.get(0).get(6).getValue().equalsIgnoreCase("codigo driver"))
      { errores.add("La primera fila debe llamarse codigo driver"); }
    }
    if (errores.size() > 0)
    {
      for (String error : errores)
      {
        log.info(error);
      }
      return errores;
    } else {
      return null;
    }
  }

  @Override
  public List<MovDriverCentro> findAllMov(int repartoTipo, int periodo, String codDriverCentro) {
    try {
      String query = "select dc.codDriverCentro, dc.codCentroDestino,c.Nombre as 'nombreCentro',dc.Porcentaje \n"
          + "from MOV_DRIVER_CENTRO dc\n"
          + "inner join MA_DRIVER d on dc.CodDriverCentro=d.CodDriver\n"
          + "and dc.RepartoTipo = d.RepartoTipo\n"
          + "inner join MA_CENTRO c on dc.CodCentroDestino=c.CodCentro\n"
          + "and c.RepartoTipo = dc.RepartoTipo\n"
          + "where dc.periodo = ? and dc.RepartoTipo=? and d.CodDriver=?";

      return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(MovDriverCentro.class),periodo,repartoTipo,codDriverCentro);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovCentro", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> findAllCodCuentaContable(int repartoTipo, int periodo) {
    try {
      if (repartoTipo == 1)
      {
        String query = "SELECT CodCuentaContable FROM MOV_CUENTA_CONTABLE WHERE RepartoTipo = ? and periodo = ?";
        return jdbcTemplate.queryForList(query, String.class,repartoTipo, periodo);
      } else {
        String query = "SELECT CodCuentaContable FROM MOV_CUENTA_CONTABLE WHERE RepartoTipo = ? and periodo like ?";
        String periodoStr = String.valueOf(periodo / 100) + "%" ;
        return jdbcTemplate.queryForList(query, String.class,repartoTipo, periodoStr);
      }


    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en CUENTA_PARTIDA_CENTRO", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> findAllCodPartida(int repartoTipo, int periodo) {
    try {
      if (repartoTipo == 1)
      {
        String query = "SELECT CodPartida FROM MOV_PARTIDA WHERE RepartoTipo = ? and periodo = ?";
        return jdbcTemplate.queryForList(query, String.class,repartoTipo, periodo);
      } else {
        String query = "SELECT CodPartida FROM MOV_PARTIDA WHERE RepartoTipo = ? and periodo like ?";
        String periodoStr = String.valueOf(periodo / 100) + "%" ;
        return jdbcTemplate.queryForList(query, String.class,repartoTipo, periodoStr);
      }
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en CUENTA_PARTIDA_CENTRO", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> findAllCodCentro(int repartoTipo, int periodo) {
    try {
      if (repartoTipo == 1)
      {
        String query = "SELECT CodCentro FROM MOV_CENTRO WHERE RepartoTipo = ? and periodo = ?";
        return jdbcTemplate.queryForList(query, String.class,repartoTipo, periodo);
      } else {
        String query = "SELECT CodCentro FROM MOV_CENTRO WHERE RepartoTipo = ? and periodo like ?";
        String periodoStr = String.valueOf(periodo / 100) + "%" ;
        return jdbcTemplate.queryForList(query, String.class,repartoTipo, periodoStr);
      }
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en CUENTA_PARTIDA_CENTRO", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> findAllCodDriver(int repartoTipo, int periodo) {
    try {
      if (repartoTipo == 1)
      {
        String query = "SELECT distinct CodDriverCentro FROM MOV_DRIVER_CENTRO WHERE RepartoTipo = ? and periodo = ?";
        return jdbcTemplate.queryForList(query, String.class,repartoTipo, periodo);
      } else {
        String query = "SELECT distinct CodDriverCentro FROM MOV_DRIVER_CENTRO WHERE RepartoTipo = ? and periodo like ?";
        String periodoStr = String.valueOf(periodo / 100) + "%" ;
        return jdbcTemplate.queryForList(query, String.class,repartoTipo, periodoStr);
      }

    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en CUENTA_PARTIDA_CENTRO", e);
    }
    return new ArrayList<>();
  }
}
