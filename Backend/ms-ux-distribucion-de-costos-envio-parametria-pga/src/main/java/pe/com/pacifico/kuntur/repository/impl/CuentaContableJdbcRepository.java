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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import pe.com.pacifico.kuntur.expose.request.CuentaContableRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.CuentaContable;
import pe.com.pacifico.kuntur.model.MovCuentaContable;
import pe.com.pacifico.kuntur.repository.CuentaContableJpaRepository;


/**
 * <b>Class</b>: CuentaContableJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
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
public class CuentaContableJdbcRepository implements CuentaContableJpaRepository {

  private final JdbcTemplate jdbcTemplate;
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private SimpleJdbcInsert simpleJdbcInsert;

  private static final String queryCoverage = "SELECT codCuentaContable, nombre, estaActivo, fechaCreacion, "
      + "fechaActualizacion, niif17Atribuible, niif17Clase, niif17Tipo, repartoTipo, tipoGasto "
      + "FROM MA_CUENTA_CONTABLE";
  private static final String querySelect = "SELECT codCuentaContable, nombre, estaActivo, fechaCreacion, "
      + "fechaActualizacion, niif17Atribuible, niif17Clase, niif17Tipo, repartoTipo, tipoGasto "
      + "FROM MA_CUENTA_CONTABLE WHERE repartoTipo = ?";
  private static final String queryCoverageOption = queryCoverage + " where codCuentaContable = ? ";
  private static final String querySelectOption = querySelect + " AND codCuentaContable = ? ";

  @Override
  public List<CuentaContable> findAll(int repartoTipo) {
    try {
      log.info("Resultado CuentaContable");
      return jdbcTemplate.query(querySelect, BeanPropertyRowMapper.newInstance(CuentaContable.class), repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en CuentaContable", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<CuentaContable> findAllNotInMovCuentaContable(int repartoTipo, int periodo) {
    try {
      log.info("Resultado CuentaContable");
      String qryNotInMov = "SELECT ma.codCuentaContable, ma.nombre, ma.estaActivo, ma.fechaCreacion, ma.fechaActualizacion,"
          + " ma.niif17Atribuible, ma.niif17Clase, ma.niif17Tipo, ma.repartoTipo, ma.tipoGasto"
          + " FROM MA_CUENTA_CONTABLE ma LEFT JOIN MOV_CUENTA_CONTABLE mo"
          + " ON ma.CodCuentaContable = mo.CodCuentaContable"
          + " AND mo.Periodo = ? AND mo.RepartoTipo = ?"
          + " WHERE mo.CodCuentaContable is NULL AND ma.repartoTipo = ?";
      return jdbcTemplate.query(qryNotInMov, BeanPropertyRowMapper.newInstance(CuentaContable.class),
          periodo, repartoTipo, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en CuentaContable", e);
    }
    return new ArrayList<>();
  }

  @Override
  public CuentaContable findByCodCuentaContable(int repartoTipo, String codigo) {
    try {
      return
          jdbcTemplate.queryForObject(querySelectOption, new Object[]{repartoTipo,codigo},
              BeanPropertyRowMapper.newInstance(CuentaContable.class));
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en cuentas contables ", e);
      return null;
    }
  }

  @Override
  public int save(CuentaContableRequest cuenta) {
    Pattern str = Pattern.compile("[0-9]{2}.[0-9]{1}.[0-9]{1}.[A-Z0-9]{2}.[A-Z0-9]{2}.[0-9]{2}");
    if (!str.matcher(cuenta.getCodCuentaContable()).matches()) {
      return 0;
    }
    return jdbcTemplate.update("insert into MA_CUENTA_CONTABLE (codCuentaContable, nombre, estaActivo, fechaCreacion, "
            + "fechaActualizacion, niif17Atribuible, niif17Clase, niif17Tipo, repartoTipo, tipoGasto,CuentaNiif) "
            + " values (?,?,?,?,?,?,?,?,?,?,?)", cuenta.getCodCuentaContable(), cuenta.getNombre(), cuenta.isEstaActivo(),
        cuenta.getFechaCreacion(), cuenta.getFechaActualizacion(), cuenta.getNiif17Atribuible(), cuenta.getNiif17Clase(),
        cuenta.getNiif17Tipo(), cuenta.getRepartoTipo(), cuenta.isTipoGasto(),0);
  }

  @Override
  public int update(CuentaContableRequest cuenta) {
    System.out.println(cuenta);
    return jdbcTemplate.update(
        new StringBuilder()
            .append(" update MA_CUENTA_CONTABLE ")
            .append(" set nombre = ? , estaActivo = ? , fechaActualizacion = ? , niif17Atribuible = ? , niif17Clase = ? "
                + ", niif17Tipo = ?, tipoGasto = ?")
            .append(" where repartoTipo = ? AND codCuentaContable = ?").toString(), cuenta.getNombre(), cuenta.isEstaActivo(),
        cuenta.getFechaActualizacion(), cuenta.getNiif17Atribuible(), cuenta.getNiif17Clase(),
        cuenta.getNiif17Tipo(), cuenta.isTipoGasto(), cuenta.getRepartoTipo(), cuenta.getCodCuentaContable());

  }

  @Override
  public boolean delete(int repartoTipo, String codigo) {
    try {
      String queryExistsInMov = "SELECT top 1 codCuentaContable FROM MOV_CUENTA_CONTABLE "
          + " WHERE repartoTipo = ? AND codCuentaContable = ? ";
      MovCuentaContable mc = jdbcTemplate.queryForObject(queryExistsInMov, new Object[]{repartoTipo,codigo},
          BeanPropertyRowMapper.newInstance(MovCuentaContable.class));
      log.info("NO ES NULL, NO SE ELIMINA");
      return false;
    } catch (EmptyResultDataAccessException ex) {
      log.info("ES VACIO, SE ELIMINA");
      String deleteQuery = "delete from MA_CUENTA_CONTABLE where codCuentaContable = ? AND repartoTipo = ?";
      jdbcTemplate.update(deleteQuery, codigo, repartoTipo);
      return true;
    } catch (Exception e) {
      log.info("ERROR");
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
  public List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo) {
    log.info("CANTIDAD FILAS EXCEL (CON HEADERS): " + excel.size());
    List<String> errores = new ArrayList<>();
    int numErrores = 0;
    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("codigo")
        && excel.get(0).get(1).getValue().equalsIgnoreCase("nombre")
        && excel.get(0).get(2).getValue().equalsIgnoreCase("tipo gasto")
        && excel.get(0).get(3).getValue().equalsIgnoreCase("niif17 atribuible")
        && excel.get(0).get(4).getValue().equalsIgnoreCase("niif17 tipo")
        && excel.get(0).get(5).getValue().equalsIgnoreCase("niif17 clase"))
    {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      List<CuentaContable> lstCarga = new ArrayList<>();
      List<CuentaContable> lstUpdate = new ArrayList<>();
      List<String> lstCodigos = findAllCodCuentaContable(repartoTipo);
      CuentaContable cuenta;
      Pattern str = Pattern.compile("[0-9]{2}.[0-9]{1}.[0-9]{1}.[A-Z0-9]{2}.[A-Z0-9]{2}.[0-9]{2}");
      boolean patronDiferente = false;
      boolean codigoExiste = false;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        patronDiferente = false;
        codigoExiste = false;
        cuenta = new CuentaContable();
        numFila++;

        String codCuentaActual = fila.get(0).getValue();

        //VALIDA PATRON
        if (!str.matcher(codCuentaActual).matches()) {
          patronDiferente = true;
        }

        String codCuentaBD = lstCodigos.stream().filter(item -> codCuentaActual.equals(item)).findAny().orElse(null);

        //VALIDA SI CODIGO YA EXISTE EN BD
        if (codCuentaBD != null) {
          codigoExiste = true;
        }

        if (!patronDiferente) {
          int error = 0;
          cuenta.setCodCuentaContable(codCuentaActual);
          cuenta.setNombre(fila.get(1).getValue());
          String tipo = fila.get(2).getValue().split("\\.")[0]; //para obtener el numero

          if (tipo.equalsIgnoreCase("1"))
          {
            cuenta.setTipoGasto(true);
          } else if (tipo.equalsIgnoreCase("0")) {
            cuenta.setTipoGasto(false);
          } else {
            errores.add("fila: " + numFila + " con codigo: " + codCuentaActual
                + " El Tipo gasto solo puede tomar valores de 1 o 0");
            error++;
          }

          if (fila.get(3).getValue().equalsIgnoreCase("SI"))
          {
            cuenta.setNiif17Atribuible("SI");
          } else if (fila.get(3).getValue().equalsIgnoreCase("NO")) {
            cuenta.setNiif17Atribuible("NO");
          } else {
            errores.add("fila: " + numFila + " con codigo: " + codCuentaActual
                + " El Niif17Atribuible solo puede tomar valores de SI o NO");
            error++;
          }

          if (fila.get(4).getValue().equalsIgnoreCase("GM"))
          {
            cuenta.setNiif17Tipo("GM");
          } else if (fila.get(4).getValue().equalsIgnoreCase("GA")) {
            cuenta.setNiif17Tipo("GA");
          } else {
            errores.add("fila: " + numFila + " con codigo: " + codCuentaActual
                + " El Niif17Tipo solo puede tomar valores de GM o GA");
            error++;
          }

          if (fila.get(5).getValue().equalsIgnoreCase("FI"))
          {
            cuenta.setNiif17Clase("FI");
          } else if (fila.get(5).getValue().equalsIgnoreCase("VA")) {
            cuenta.setNiif17Clase("VA");
          } else {
            errores.add("fila: " + numFila + " con codigo: " + codCuentaActual
                + " El Niif17Clase solo puede tomar valores de FI o NO");
            error++;
          }

          if (error > 0) { numErrores++; }
          if (error == 0) {
            if (codigoExiste)
            {
              lstUpdate.add(cuenta);
            } else
            {
              lstCarga.add(cuenta);
            }
          }
          lstCodigos.removeIf(x -> x.equals(codCuentaActual));
        } else {
          numErrores++;
          errores.add("fila: " + numFila + " con codigo: " + codCuentaActual + " no presenta un patron de codigo valido");
        }
      }

      String sql = "INSERT INTO MA_CUENTA_CONTABLE"
          + " (codCuentaContable, nombre, estaActivo, fechaCreacion, fechaActualizacion, tipoGasto, niif17Atribuible, "
          + "niif17Tipo, niif17Clase, repartoTipo, CuentaNiif) "
          + "VALUES " + "(?,?,?,?,?,?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstCarga.get(i).getCodCuentaContable());//CodCuentaContable
            ps.setString(2, lstCarga.get(i).getNombre());//Nombre
            ps.setInt(3, 1);//EstaActivo
            ps.setString(4, fecha);//FechaCreacion
            ps.setString(5, fecha);//FechaActualizacion
            ps.setInt(6, (lstCarga.get(i).isTipoGasto()) ? 1 : 0);//TipoGasto
            ps.setString(7, lstCarga.get(i).getNiif17Atribuible());//Niif17Atribuible
            ps.setString(8, lstCarga.get(i).getNiif17Tipo());//Niif17Tipo
            ps.setString(9, lstCarga.get(i).getNiif17Clase());//Niif17Clase
            ps.setInt(10, repartoTipo);//RepartoTipo
            ps.setInt(11, 0);//Cuenta niif
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
      //Luego se actualizan los centros
      sql = "update MA_CUENTA_CONTABLE \n"
          + "set Nombre=?, tipoGasto= ?,\n"
          + "Niif17Atribuible=?, Niif17Tipo=?, Niif17Clase = ?,\n"
          + "FechaActualizacion = ?\n"
          + "where codCuentaContable=? and RepartoTipo=?";
      try {
        log.info("llego aqui 4");
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstUpdate.get(i).getNombre());//Nombre
            ps.setInt(2, (lstUpdate.get(i).isTipoGasto()) ? 1 : 0);//TipoGasto
            ps.setString(3, lstUpdate.get(i).getNiif17Atribuible());//Niif17Atribuible
            ps.setString(4, lstUpdate.get(i).getNiif17Tipo());//Niif17Tipo
            ps.setString(5, lstUpdate.get(i).getNiif17Clase());//Niif17Clase
            ps.setString(6, fecha);//FechaActualizacion
            ps.setString(7, lstUpdate.get(i).getCodCuentaContable());//CodCuentaContable
            ps.setInt(8, repartoTipo);//RepartoTipo
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

      if (errores.size() > 0) {
        errores.add(0,numErrores + " filas de " + excel.size()
            + " no se subieron por los siguientes errores: ");
      }
    } else {
      errores.add("El archivo no se pudo subir por los siguientes errores de formato: ");
      if (!excel.get(0).get(0).getValue().equalsIgnoreCase("codigo"))
      { errores.add("La primera columna tiene un nombre erroneo. Debería llamarse codigo"); }
      if (!excel.get(0).get(1).getValue().equalsIgnoreCase("nombre"))
      { errores.add("La segunda columna tiene un nombre erroneo. Debería llamarse nombre"); }
      if (!excel.get(0).get(2).getValue().equalsIgnoreCase("tipo gasto"))
      { errores.add("La tercera columna tiene un nombre erroneo. Debería llamarse tipo gasto"); }
      if (!excel.get(0).get(3).getValue().equalsIgnoreCase("niif17 atribuible"))
      { errores.add("La cuarta columna tiene un nombre erroneo. Debería llamarse niif17 atribuible"); }
      if (!excel.get(0).get(4).getValue().equalsIgnoreCase("niif17 tipo"))
      { errores.add("La quinta columna tiene un nombre erroneo. Debería llamarse niif17 tipo"); }
      if (!excel.get(0).get(5).getValue().equalsIgnoreCase("niif17 clase"))
      { errores.add("La sexta columna tiene un nombre erroneo. Debería llamarse niif17 clase"); }
      return errores;
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
  public List<CuentaContable> findAllNiif(int repartoTipo) {
    String query = "select CodCuentaContable, Nombre from MA_CUENTA_CONTABLE\n"
        + "where CuentaNiif = 1 and RepartoTipo=?";
    try {
      log.info("Resultado CuentaContable");
      return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(CuentaContable.class), repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en CuentaContable", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<CuentaContable> findAllNotNiifCuentaContable(int repartoTipo) {
    String query = "select CodCuentaContable, Nombre from MA_CUENTA_CONTABLE\n"
        + "where CuentaNiif = 0 and RepartoTipo=?";
    try {
      log.info("Resultado CuentaContable");
      return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(CuentaContable.class), repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en CuentaContable", e);
    }
    return new ArrayList<>();
  }

  @Override
  public int registrarNiif(CuentaContableRequest cuenta) {
    String query = "update MA_CUENTA_CONTABLE\n"
        + "set CuentaNiif = 1\n"
        + "where CodCuentaContable= ? and RepartoTipo = ?";
    return jdbcTemplate.update(query, cuenta.getCodCuentaContable(), cuenta.getRepartoTipo());
  }

  @Override
  public boolean quitarNiif(int repartoTipo, String codigo) {
    String query = "update MA_CUENTA_CONTABLE\n"
        + "set CuentaNiif = 0\n"
        + "where CodCuentaContable= ? and RepartoTipo = ?";
    return jdbcTemplate.update(query, codigo, repartoTipo) > 0;
  }

  @Override
  public List<String> registerNiifByExcel(List<List<CellData>> excel, int repartoTipo) {
    log.info("CANTIDAD FILAS EXCEL (CON HEADERS): " + excel.size());
    List<String> errores = new ArrayList<>();
    int numErrores = 0;
    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("codigo"))
    {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      List<CuentaContable> lstCarga = new ArrayList<>();
      List<String> lstCodigos = findAllCodCuentaContable(repartoTipo);
      CuentaContable cuenta;
      boolean codigoExiste = false;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoExiste = false;
        cuenta = new CuentaContable();
        numFila++;

        String codCuentaActual = fila.get(0).getValue();
        String codCuentaBD = lstCodigos.stream().filter(codCuentaActual::equals).findAny().orElse(null);

        //VALIDA SI CODIGO YA EXISTE EN BD
        if (codCuentaBD != null) {
          codigoExiste = true;
        }

        if (codigoExiste) {
          cuenta.setCodCuentaContable(codCuentaActual);
          lstCarga.add(cuenta);
          lstCodigos.removeIf(x -> x.equals(codCuentaActual));
        } else {
          numErrores++;
          errores.add("fila: " + numFila + " con codigo: " + codCuentaActual + " no existe en el maestro de cuentas");
        }
      }

      //Se setea todoo a no niif
      if (lstCarga.size() > 0) {
        String deleteQuery = "update MA_CUENTA_CONTABLE\n"
            + "set CuentaNiif = 0\n"
            + "where RepartoTipo = ?";
        jdbcTemplate.update(deleteQuery, repartoTipo);
      }


      String sql = "update MA_CUENTA_CONTABLE\n"
          + "set CuentaNiif = 1\n"
          + "where CodCuentaContable= ? and RepartoTipo = ?";

      try {
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstCarga.get(i).getCodCuentaContable());//CodCuentaContable
            ps.setInt(2, repartoTipo);//Reparto Tipo
          }

          @Override
          public int getBatchSize() {
            return lstCarga.size();
          }
        });
        log.info("SE REGISTRARON " + lstCarga.size() + " DE " + excel.size() + " FILAS.");
      } catch (Exception e) {
        log.error(e.toString());
        numErrores = excel.size();
        errores.add("Se tiene un error al registrar el excel. Verifique que los datos sean correctos");
      }

      if (errores.size() > 0) {
        errores.add(0,numErrores + " filas de " + excel.size()
            + " no se subieron por los siguientes errores: ");
      }
    } else {
      errores.add("El archivo no se pudo subir por los siguientes errores de formato: ");
      if (!excel.get(0).get(0).getValue().equalsIgnoreCase("Numero de cuenta"))
      { errores.add("La primera columna tiene un nombre erroneo. Debería llamarse Numero de cuenta"); }
      return errores;
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
}
