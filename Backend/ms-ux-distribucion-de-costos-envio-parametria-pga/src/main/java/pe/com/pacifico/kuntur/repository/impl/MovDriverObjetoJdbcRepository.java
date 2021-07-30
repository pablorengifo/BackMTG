package pe.com.pacifico.kuntur.repository.impl;

import static java.lang.Math.round;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaDriver;
import pe.com.pacifico.kuntur.model.MovDriverObjeto;
import pe.com.pacifico.kuntur.repository.MovDriverObjetoJpaRepository;


/**
 * <b>Class</b>: MovDriverObjetoJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 28, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MovDriverObjetoJdbcRepository implements MovDriverObjetoJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<MaDriver> findAll(int repartoTipo, int periodo) {
    try {
      String query = "select distinct do.CodDriverObjeto as CodDriver, d.Nombre as nombre\n"
          + "from MOV_DRIVER_OBJETO do\n"
          + "inner join MA_DRIVER d on do.CodDriverObjeto=d.CodDriver\n"
          + "where do.periodo = ? and do.RepartoTipo=? and d.CodDriverTipo='OBCO' ";
      return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(MaDriver.class), periodo, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaDriver", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<MovDriverObjeto> findAllByPeriod(int repartoTipo, int periodo, String codDriver) {

    String query = "SELECT mdo.CodDriverObjeto, mdo.RepartoTipo, mdo.Periodo,"
        + " mdo.CodProducto, mdo.CodSubcanal, mdo.FechaCreacion, "
        + " mdo.FechaActualizacion, mdo.Porcentaje, md.Nombre as NombreDriver, ms.Nombre as NombreSubCanal, mp.nombre as NombreProducto"
        + " FROM MOV_DRIVER_OBJETO mdo "
        + " JOIN MA_DRIVER md on mdo.CodDriverObjeto = md.CodDriver"
        + " AND mdo.RepartoTipo = md.RepartoTipo"
        + " JOIN MA_SUBCANAL ms on mdo.CodSubcanal = ms.CodSubcanal"
        + " AND mdo.RepartoTipo = ms.RepartoTipo"
        + " JOIN MA_PRODUCTO mp on mdo.CodProducto = mp.CodProducto"
        + " AND mdo.RepartoTipo = mp.RepartoTipo"
        + " WHERE mdo.repartoTipo = ? AND mdo.periodo = ? AND mdo.codDriverObjeto = ?";

    System.out.println("JDBC MovDriverObjeto - Executing find all by period: " + periodo + " and repartoTipo: " + repartoTipo);
    try {
      List<MovDriverObjeto> md = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(MovDriverObjeto.class),
          repartoTipo, periodo, codDriver);
      return md;
    } catch (EmptyResultDataAccessException e) { //
      System.out.println("Exception: " + e);
      log.warn("> Resultado vacío en MovDriverObjeto", e);
    }
    System.out.println("> Returning new array list...");
    return new ArrayList();
  }

  @Override
  public List<MovDriverObjeto> findAllByPeriod(int repartoTipo, int periodo) {
    String query = "SELECT mdo.CodDriverObjeto, mdo.RepartoTipo, mdo.Periodo,"
        + " mdo.CodProducto, mdo.CodSubcanal, mdo.FechaCreacion, "
        + " mdo.FechaActualizacion, mdo.Porcentaje, md.Nombre as NombreDriver, ms.Nombre as NombreSubCanal, mp.nombre as NombreProducto"
        + " FROM MOV_DRIVER_OBJETO mdo "
        + " JOIN MA_DRIVER md on mdo.CodDriverObjeto = md.CodDriver"
        + " AND mdo.RepartoTipo = md.RepartoTipo"
        + " JOIN MA_SUBCANAL ms on mdo.CodSubcanal = ms.CodSubcanal"
        + " AND mdo.RepartoTipo = ms.RepartoTipo"
        + " JOIN MA_PRODUCTO mp on mdo.CodProducto = mp.CodProducto"
        + " AND mdo.RepartoTipo = mp.RepartoTipo"
        + " WHERE mdo.repartoTipo = ? AND mdo.periodo = ?";

    System.out.println("JDBC MovDriverObjeto - Executing find all by period: " + periodo + " and repartoTipo: " + repartoTipo);
    try {
      List<MovDriverObjeto> md = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(MovDriverObjeto.class),
          repartoTipo, periodo);
      return md;
    } catch (EmptyResultDataAccessException e) { //
      System.out.println("Exception: " + e);
      log.warn("> Resultado vacío en MovDriverObjeto", e);
    }
    System.out.println("> Returning new array list...");
    return new ArrayList();
  }

  private List<String> findAllCodDriver(int repartoTipo) {
    try {
      log.info("Resultado DriverCentro");
      String query = "select CodDriver from MA_DRIVER where RepartoTipo=? ";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaDriverCentro", e);
    }
    return new ArrayList<>();
  }

  @Override
  public boolean delete(int repartoTipo, int periodo, String codigo) {
    try {
      String deleteQuery = "delete from MOV_DRIVER_OBJETO\n"
          + "where CodDriverObjeto=? and Periodo=? and RepartoTipo=?";
      log.info(codigo + " con " + periodo + " con " + repartoTipo);
      jdbcTemplate.update(deleteQuery, codigo, periodo, repartoTipo);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public List<String> saveExcelToBD_MaDriver(List<List<CellData>> excel, int repartoTipo, int periodo) {
    List<String> errores = new ArrayList<>();
    System.out.println("Guardando excel 1...");
    log.info("CANTIDAD FILAS del sheet 1(CON HEADERS): " + excel.size());
    int numErrores = 0;

    // Remover cabeceras iniciales:
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("Modelo de costos")) {
      for (int x = 3; x >= 0; x--) {
        excel.remove(x);
      }
    }

    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("codigo")
        && excel.get(0).get(1).getValue().equalsIgnoreCase("nombre")
        && excel.get(0).get(2).getValue().equalsIgnoreCase("tipo")) {

      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      List<MaDriver> lstCarga = new ArrayList<>();
      List<MaDriver> lstUpdate = new ArrayList<>();
      List<String> lstCodigos = findAllCodDriver(repartoTipo);
      MaDriver maDriver;
      boolean codigoExiste;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoExiste = false;
        maDriver = new MaDriver();
        numFila++;

        String codDriverActual = fila.get(0).getValue();

        String codDriverBD = lstCodigos.stream().filter(item -> codDriverActual.equals(item)).findAny().orElse(null);

        //VALIDA QUE LOS CODIGO SI EXISTA EN BD
        if (codDriverBD != null) {
          codigoExiste = true;
        }

        //Si no esxiste es nuevo y por ende se agrega, sino se actualiza
        if (fila.get(3).getValue().equalsIgnoreCase("si")
            && fila.get(2).getValue().equalsIgnoreCase("OBJETOS DE COSTOS")) {
          maDriver.setCodDriver(codDriverActual);
          maDriver.setNombre(fila.get(1).getValue());
          if (!codigoExiste) {
            //System.out.println("Agregando #" + numFila + " cod driver: " + codDriverActual);
            lstCarga.add(maDriver);
          } else {
            //System.out.println("Editando #" + numFila + " cod driver: " + codDriverActual);
            lstUpdate.add(maDriver);
          }
        } else {
          errores.add("FILA: " + numFila + " - CODIGO: " + codDriverActual
              + " No se permite cargar");
          numErrores++;
        }
      }

      //priemro se crean las nuevas
      String sql = "insert into MA_DRIVER \n"
          + "(CodDriver,CodDriverTipo, FechaActualizacion, FechaCreacion, Nombre, RepartoTipo)\n"
          + "values (?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        if (lstCarga.size() > 0) {
          jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setString(1, lstCarga.get(i).getCodDriver());//CodCentro
              ps.setString(2, "OBCO");//CodCentro
              ps.setString(3, fecha);//FechaCreacion
              ps.setString(4, fecha);//FechaActualizacion
              ps.setString(5, lstCarga.get(i).getNombre());//RepartoTipo
              ps.setInt(6, repartoTipo);//Saldo
            }

            @Override
            public int getBatchSize() {
              return lstCarga.size();
            }
          });
        }
        log.info("SE SUBIERON " + lstCarga.size() + " DE " + excel.size() + " FILAS.");
      } catch (Exception e) {
        log.error(e.toString());
        numErrores += lstCarga.size();
        errores.add("Se tiene un error al registrar los nuevos drivers del excel. "
            + "Verifique que los datos sean correctos");
      }

      //Luego se actualizan las existentes
      sql = "update MA_DRIVER\n"
          + "set  Nombre=?, FechaActualizacion=?\n"
          + "where CodDriver=?";
      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        if (lstUpdate.size() > 0) {
          jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setString(1, lstUpdate.get(i).getNombre());//nombre
              ps.setString(2, fecha);//FechaActualizacion
              ps.setString(3, lstUpdate.get(i).getCodDriver());//codDriver
            }

            @Override
            public int getBatchSize() {
              return lstCarga.size();
            }
          });
          log.info("SE SUBIERON " + lstCarga.size() + " DE " + excel.size() + " FILAS.");
        }
      } catch (Exception e) {
        log.error(e.toString());
        numErrores += lstCarga.size();
        errores.add("Se tiene un error al actualizar drivers del excel. "
            + "Verifique que los datos sean correctos");
      }
    } else {
      errores.add("Se tiene un error de formato de cabeceras");
      return errores;
    }
    if (errores.size() > 0) {
      errores.add(0,numErrores + " filas de " + excel.size()
          + " del catalogo de drivers no se subieron por los siguientes errores: ");
      for (String fila : errores) {
        log.info(fila);
      }
      return errores;
    } else { return null; }
  }

  private List<String> findAllCodProducto(int repartoTipo) {
    try {
      log.info("Resultado DriverCentro");
      String query = "select codProducto from MA_Producto where repartoTipo = ? ";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MAPRODUCTO", e);
    }
    return new ArrayList<>();
  }

  private List<String> findAllCodSubCanal(int repartoTipo) {
    try {
      log.info("Resultado DriverCentro");
      String query = "select codSubCanal from MA_Subcanal where repartoTipo = ? ";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MASUBCANAL", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> saveExcelToBD_MovDriverObjeto(List<List<CellData>> excel, int repartoTipo, int periodo) {
    List<String> errores = new ArrayList<>();
    int numError = 0;
    //System.out.println("Guardando excel 2...");
    log.info("CANTIDAD FILAS del sheet 2(CON HEADERS): " + excel.size());

    // Remover cabeceras iniciales:
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("Modelo de costos")) {
      for (int x = 4; x >= 0; x--) {
        excel.remove(x);
      }
    }

    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("codigo")
        && excel.get(0).get(1).getValue().equalsIgnoreCase("nombre")
        && excel.get(0).get(2).getValue().equalsIgnoreCase("CODIGO LINEA")
        && excel.get(0).get(3).getValue().equalsIgnoreCase("NOMBRE LINEA")
        && excel.get(0).get(4).getValue().equalsIgnoreCase("CODIGO PRODUCTO")
        && excel.get(0).get(5).getValue().equalsIgnoreCase("NOMBRE PRODUCTO")
        && excel.get(0).get(6).getValue().equalsIgnoreCase("CODIGO CANAL")
        && excel.get(0).get(7).getValue().equalsIgnoreCase("NOMBRE CANAL")
        && excel.get(0).get(8).getValue().equalsIgnoreCase("CODIGO SUBCANAL")
        && excel.get(0).get(9).getValue().equalsIgnoreCase("NOMBRE SUBCANAL")
        && excel.get(0).get(10).getValue().equalsIgnoreCase("PORCENTAJE")) {

      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      //List<MovDriverObjeto> lstCarga = new ArrayList<>();
      Map<String, Map<String, Double>> mapCarga = new HashMap();

      //List<String> lstCodigosCodMovDriverObj = findAllCodDriverObjeto(repartoTipo, periodo);
      List<String> lstCodigosDriver = findAllCodDriver(repartoTipo);
      List<String> lstCodigosSubcanal = findAllCodSubCanal(repartoTipo);
      List<String> lstCodigosProducto = findAllCodProducto(repartoTipo);

      MovDriverObjeto movDriverObjeto;
      boolean codigoDriverExiste;
      //boolean codigoMovDriverObjetoExiste;

      int numFila = 0;
      Date date = new Date();

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoDriverExiste = false;
        //codigoMovDriverObjetoExiste = false;
        movDriverObjeto = new MovDriverObjeto();
        numFila++;

        //String codMovDriverActual = fila.get(0).getValue() + "~" + fila.get(4).getValue() + "~" + fila.get(8).getValue();
        String codDriverActual = fila.get(0).getValue();
        String codigoSubcanal = fila.get(fila.size() - 3).getValue();
        String codigoProducto = fila.get(fila.size() - 7).getValue();

        //String codMovDriverObjBD = lstCodigosCodMovDriverObj.stream().filter(item -> codMovDriverActual.equals(item))
        // .findAny().orElse(null);
        String codDriverBD = lstCodigosDriver.stream().filter(item -> codDriverActual.equals(item)).findAny().orElse(null);
        String codSubcanalBD = lstCodigosSubcanal.stream().filter(item -> codigoSubcanal.equals(item)).findAny().orElse(null);
        String codProductoBD = lstCodigosProducto.stream().filter(item -> codigoProducto.equals(item)).findAny().orElse(null);

        //VALIDA QUE EL CODIGO DEL DRIVER ESTE EN LA DB
        if (codDriverBD != null && codSubcanalBD != null && codProductoBD != null) {
          codigoDriverExiste = true;
        } else {
          //System.out.println("Cod driver false for " + fila.get(0).getValue());
        }


        //String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);

        if (codigoDriverExiste) {
          try {
            /** .
             * keyOne = codDriverObjeto
             * keyTwo = codProducto~codSubcanal
             */

            double perc = Double.parseDouble(fila.get(fila.size() - 1).getValue());

            String keyOne = codDriverActual;
            String keyTwo = codigoProducto + "~" + codigoSubcanal;

            Map<String, Double> mapValue = mapCarga.getOrDefault(keyOne, new HashMap());
            double acumPerc = mapValue.getOrDefault(keyTwo, .0);

            mapValue.put(keyTwo, acumPerc + perc);
            mapCarga.put(keyOne, mapValue);

          } catch (Exception e) {
            e.printStackTrace();
          }
        } else {
          numError++;
          errores.add("FILA: " + numFila + " - CODIGO: " + codDriverActual
              + " - ERROR: No existe algun codigo. Resultados: codDriverBD: " + codDriverBD
              + " codSubcanal: " + codSubcanalBD + " codProducto: " + codProductoBD);
        }
      }
      //System.out.println("Ver que remover");
      /*for (String codNuevo : lstCodigosDriver) {
        double porcentajeAcomulado = 0;
        for (MovDriverObjeto i : lstCarga) {
          if (i.getCodDriverObjeto().equals(codNuevo)) {
            porcentajeAcomulado += i.getPorcentaje();
          }
        }
        // Si no suma 100 se borra
        if (round(porcentajeAcomulado) != 100) {
          lstCarga.removeIf(i -> i.getCodDriverObjeto().equals(codNuevo));
          lstFilasError.add("Driver con codigo" + codNuevo
              + " - ERROR: Porcentaje no suma 100, suma: " + round(porcentajeAcomulado));
          //System.out.println("Borrando " + codNuevo);
        }
      }*/
      List<String> keysToRemove = new ArrayList();
      for (String keyOne : mapCarga.keySet()) {
        double acumPerc = 0;
        int filas  = 0;
        for (String keyTwo : mapCarga.get(keyOne).keySet()) {
          acumPerc += mapCarga.get(keyOne).get(keyTwo);
          filas++;
        }
        if (round(acumPerc) != 100) {
          keysToRemove.add(keyOne);
          numError += filas;
          errores.add("Driver con codigo" + keyOne
              + " - ERROR: Porcentaje no suma 100, suma: " + round(acumPerc * 1000) / 1000);
          //System.out.println("Borrando " + codNuevo);
        }
      }
      // Remove
      for (String key : keysToRemove) {
        mapCarga.remove(key);
      }
      // Se crea la lista
      List<MovDriverObjeto> lstCarga = new ArrayList<>();
      // Se agregan los registros a la lista
      for (String keyOne : mapCarga.keySet()) {
        for (String keyTwo : mapCarga.get(keyOne).keySet()) {
          MovDriverObjeto objeto = new MovDriverObjeto();

          String[] splittedkeyTwo = keyTwo.split("~");
          String codigoProducto = splittedkeyTwo[0];
          String codigoSubcanal = splittedkeyTwo[1];

          objeto.setCodDriverObjeto(keyOne);
          objeto.setRepartoTipo(repartoTipo);
          objeto.setPeriodo(periodo);
          objeto.setCodProducto(codigoProducto);
          objeto.setCodSubcanal(codigoSubcanal);
          objeto.setFechaCreacion(date);
          objeto.setFechaActualizacion(date);
          objeto.setPorcentaje(mapCarga.get(keyOne).get(keyTwo));

          lstCarga.add(objeto);
        }
      }
      // Se limpia lo viejo
      Set<String> set = new HashSet<>(lstCarga.size());
      List<MovDriverObjeto> lstDelete =
          lstCarga.stream().filter(p -> set.add(p.getCodDriverObjeto())).collect(Collectors.toList());
      if (lstDelete.size() > 0) {
        String deleteQuery = "DELETE FROM MOV_DRIVER_OBJETO \n"
            + "WHERE RepartoTipo = ? AND Periodo = ? and CodDriverObjeto=?";
        try {
          jdbcTemplate.batchUpdate(deleteQuery, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setString(3, lstDelete.get(i).getCodDriverObjeto());//CodDriver
              ps.setInt(2, periodo);//Periodo
              ps.setInt(1, repartoTipo);//Reparto Tipo
            }

            @Override
            public int getBatchSize() {
              return lstDelete.size();
            }
          });
        } catch (Exception e) {
          log.error(e.toString());
        }
      }

      // Se agrega lo nuevo
      if (lstCarga.size() > 0) {
        String sql = "insert into MOV_DRIVER_OBJETO \n"
            + "(CodDriverObjeto, RepartoTipo, Periodo, "
            + " CodProducto, CodSubcanal, FechaCreacion, "
            + " FechaActualizacion, porcentaje)\n"
            + "values (?,?,?,?,?,?,?,?)";

        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);

        try {
          jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setString(1, lstCarga.get(i).getCodDriverObjeto());//CodDriverObjeto
              ps.setInt(2, lstCarga.get(i).getRepartoTipo());//RepartoTipo
              ps.setInt(3, lstCarga.get(i).getPeriodo());//Periodo
              ps.setString(4, lstCarga.get(i).getCodProducto());//CodProducto
              ps.setString(5, lstCarga.get(i).getCodSubcanal());//CodSubcanal
              ps.setString(6, fecha);//Porcentaje
              ps.setString(7, fecha);//Reparto Tipo
              ps.setDouble(8, lstCarga.get(i).getPorcentaje());//%
            }

            @Override
            public int getBatchSize() {
              return lstCarga.size();
            }
          });
          log.info("SE SUBIERON " + lstCarga.size() + " DE " + excel.size() + " FILAS.");
        } catch (Exception e) {
          log.error(e.toString());
          numError = excel.size();
          errores.add("Se tiene un error al registrar el excel. Verifique que los datos sean correctos");
        }
      }
    } else {
      errores.add("Se tiene un error de formato de cabeceras");
      return errores;
    }
    if (errores.size() > 0) {
      errores.add(0,numError + " filas de " + excel.size()
          + " de mov driver objeto no se subieron por los siguientes errores: ");
      for (String fila : errores) {
        log.info(fila);
      }
      return errores;
    } else { return null; }
  }

}
