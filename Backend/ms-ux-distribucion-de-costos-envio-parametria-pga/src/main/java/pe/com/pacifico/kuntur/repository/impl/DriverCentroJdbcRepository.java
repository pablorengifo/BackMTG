package pe.com.pacifico.kuntur.repository.impl;

import static java.lang.Math.round;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pe.com.pacifico.kuntur.expose.request.DriverAsignacionesRequest;
import pe.com.pacifico.kuntur.expose.request.DriverCentroRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaDriver;
import pe.com.pacifico.kuntur.model.MovDriverCentro;
import pe.com.pacifico.kuntur.repository.DriverCentroJpaRepository;

/**
 * <b>Class</b>: DriverCentroJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 23, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class DriverCentroJdbcRepository implements DriverCentroJpaRepository {
  private final JdbcTemplate jdbcTemplate;


  @Override
  public List<MaDriver> findAll(int repartoTipo, int periodo) {
    try {
      String query = "select distinct (CodDriverCentro) as codDriver , d.Nombre as nombre\n"
          + "from MOV_DRIVER_CENTRO dc\n"
          + "inner join MA_DRIVER d on dc.CodDriverCentro=d.CodDriver\n"
          + "where dc.periodo = ? and dc.RepartoTipo=? and d.CodDriverTipo='CECO' ";
      return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(MaDriver.class), periodo, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaDriver", e);
    }
    return new ArrayList<>();
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

      return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(MovDriverCentro.class), periodo, repartoTipo, codDriverCentro);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovCentro", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<MovDriverCentro> findAllMov(int repartoTipo, int periodo) {
    try {
      String query = "select dc.codDriverCentro, dc.codCentroDestino,c.Nombre as 'nombreCentro',dc.Porcentaje \n"
          + "from MOV_DRIVER_CENTRO dc\n"
          + "inner join MA_DRIVER d on dc.CodDriverCentro=d.CodDriver\n"
          + "and dc.RepartoTipo = d.RepartoTipo\n"
          + "inner join MA_CENTRO c on dc.CodCentroDestino=c.CodCentro\n"
          + "and c.RepartoTipo = dc.RepartoTipo\n"
          + "where dc.periodo = ? and dc.RepartoTipo=?";

      return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(MovDriverCentro.class), periodo, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovCentro", e);
    }
    return new ArrayList<>();
  }

  @Override
  public int save(DriverCentroRequest driverCentroRequest) {
    int result = 0;
    try {
      String query = "insert into MA_DRIVER(CodDriver,CodDriverTipo,FechaActualizacion,FechaCreacion, Nombre,RepartoTipo)\n"
          + "values (?,'CECO',?,?,?,?);";
      result = jdbcTemplate.update(query, driverCentroRequest.getCodDriver(), driverCentroRequest.getFechaActualizacion(),
          driverCentroRequest.getFechaCreacion(), driverCentroRequest.getNombre(), driverCentroRequest.getRepartoTipo());
      for (DriverAsignacionesRequest dar : driverCentroRequest.getMovDrivers()) {
        String queryMov = "insert into MOV_DRIVER_CENTRO "
            + "(CodDriverCentro,CodCentroDestino,FechaActualizacion,FechaCreacion,Periodo,Porcentaje,RepartoTipo)\n"
            + "values (?,?,?,?,?,?,?)";
        result = jdbcTemplate.update(queryMov, driverCentroRequest.getCodDriver(), dar.getCodCentroDestino(),
            driverCentroRequest.getFechaActualizacion(), driverCentroRequest.getFechaCreacion(),
            driverCentroRequest.getPeriodo(), dar.getPorcentaje(), driverCentroRequest.getRepartoTipo());
      }
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
  public int update(DriverCentroRequest request) {


    int result = 0;
    try {
      //primero se actualiza el nombre del ma driver
      String updateQuery = "update MA_DRIVER\n"
          + "set Nombre=?, FechaActualizacion=?\n"
          + "where CodDriver=?";
      result = jdbcTemplate.update(updateQuery, request.getNombre(), request.getFechaActualizacion(), request.getCodDriver());

      //luego se actualizan los centros de costo
      delete(request.getRepartoTipo(), request.getPeriodo(), request.getCodDriver());
      for (DriverAsignacionesRequest dar : request.getMovDrivers()) {
        String queryMov = "insert into MOV_DRIVER_CENTRO "
            + "(CodDriverCentro,CodCentroDestino,FechaActualizacion,FechaCreacion,Periodo,Porcentaje,RepartoTipo)\n"
            + "values (?,?,?,?,?,?,?)";
        result = jdbcTemplate.update(queryMov, request.getCodDriver(), dar.getCodCentroDestino(),
            request.getFechaActualizacion(), request.getFechaCreacion(),
            request.getPeriodo(), dar.getPorcentaje(), request.getRepartoTipo());
      }
    } catch (DuplicateKeyException e) {
      result = -1;
      log.warn(e.toString());
    } catch (Exception e) {
      log.warn(e.toString());
      result = -2;
    }
    return result;
  }

  @Override
  public boolean delete(int repartoTipo, int periodo, String codigo) {
    try {
      String deleteQuery = "delete from MOV_DRIVER_CENTRO\n"
          + "where CodDriverCentro=? and Periodo=? and RepartoTipo=?";
      log.info(codigo + " con " + periodo + " con " + repartoTipo);
      jdbcTemplate.update(deleteQuery, codigo, periodo, repartoTipo);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public List<String> saveExcelToBdMa(List<List<CellData>> excel, int repartoTipo, int periodo) {
    List<String> errores = new ArrayList<>();
    log.info("CANTIDAD FILAS del sheet 1(CON HEADERS): " + excel.size());
    int numErrores = 0;
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

        //VALIDA QUE CODIGO SI EXISTA EN BD
        if (codDriverBD != null) {
          codigoExiste = true;
        }

        //Si no esxiste es nuevo y por ende se agrega, sino se actualiza
        if (fila.get(3).getValue().equalsIgnoreCase("si")
            && fila.get(2).getValue().equalsIgnoreCase("CENTROS DE COSTOS")) {
          maDriver.setCodDriver(codDriverActual);
          maDriver.setNombre(fila.get(1).getValue());
          if (!codigoExiste) {
            lstCarga.add(maDriver);
          } else {
            lstUpdate.add(maDriver);
          }
        } else {
          numErrores++;
          errores.add("FILA: " + numFila + " con codigo de driver " + codDriverActual
              + " no es un driver del tipo CENTROS DE COSTOS o no esta activo");
        }
      }

      //priemro se crean las nuevas
      String sql = "insert into MA_DRIVER \n"
          + "(CodDriver,CodDriverTipo, FechaActualizacion, FechaCreacion, Nombre,RepartoTipo)\n"
          + "values (?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        System.out.println("Ejecutando inserts MA: ");
        if (lstCarga.size() > 0) {
          jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setString(1, lstCarga.get(i).getCodDriver());//CodCentro
              ps.setString(2, "CECO");//CodCentro
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
        log.info("SE Insertaron " + lstCarga.size() + " DE " + excel.size() + " FILAS.");
      } catch (Exception e) {
        log.error(e.toString());
        numErrores += lstCarga.size();
        errores.add("Se tiene un error al registrar los nuevos drivers en el excel. "
            + "Verifique que los datos sean correctos");
      }

      //Luego se actualizan las existentes

      sql = "update MA_DRIVER\n"
          + "set  Nombre=?, FechaActualizacion=?\n"
          + "where CodDriver=?";
      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        System.out.println("Ejecutando update...");
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
        }
        log.info("SE Actualizaron " + lstUpdate.size() + " DE " + excel.size() + " FILAS.");
      } catch (Exception e) {
        log.error(e.toString());
        numErrores += lstUpdate.size();
        errores.add("Se tiene un error al actualizar los nuevos drivers en el excel. "
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

  @Override
  public List<String> saveExcelToBdMov(List<List<CellData>> excel, int repartoTipo, int periodo) {
    List<String> errores = new ArrayList<>();
    log.info("CANTIDAD FILAS del sheet 1(CON HEADERS): " + excel.size());
    int numErrores = 0;
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("Modelo de costos")) {
      for (int x = 4; x >= 0; x--) {
        excel.remove(x);
      }
    }

    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("codigo")
        && excel.get(0).get(2).getValue().equalsIgnoreCase("codigo centro")
        && excel.get(0).get(4).getValue().equalsIgnoreCase("porcentaje")) {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      List<MovDriverCentro> lstCarga = new ArrayList<>();

      List<String> lstCodigosDriver = findAllCodDriver(repartoTipo);
      List<String> lstCodigosCentro = findAllCodCentro(repartoTipo);

      List<String> lstCodigosAgregar = new ArrayList<>();
      MovDriverCentro movDriverCentro;
      boolean codigoExiste;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoExiste = false;
        movDriverCentro = new MovDriverCentro();
        numFila++;

        String codCentroActual = fila.get(2).getValue();
        String codDriverActual = fila.get(0).getValue();

        String codCentroBD = lstCodigosCentro.stream().filter(item -> codCentroActual.equals(item)).findAny().orElse(null);
        String codDriverBD = lstCodigosDriver.stream().filter(item -> codDriverActual.equals(item)).findAny().orElse(null);
        String nuevo = lstCodigosAgregar.stream().filter(item -> codDriverActual.equals(item)).findAny().orElse(null);

        //VALIDA QUE AMBOS CODIGOS SI EXISTAN EN BD
        if (codCentroBD != null && codDriverBD != null) {
          codigoExiste = true;

          //Esto identifica si el codigo es nuevo
          if (nuevo == null) {
            lstCodigosAgregar.add(codDriverActual);
          }
        }

        if (codigoExiste) {
          movDriverCentro.setCodDriverCentro(codDriverActual);
          movDriverCentro.setCodCentroDestino(fila.get(2).getValue());
          double porcentaje = Double.parseDouble(fila.get(4).getValue());
          movDriverCentro.setPorcentaje(porcentaje);
          lstCarga.add(movDriverCentro);
        } else {
          numErrores++;
          if (codCentroBD == null) {
            errores.add("FILA: " + numFila + " con codigo de centro " + codCentroActual + " no existe en el periodo");
          }
          if (codDriverBD == null) {
            errores.add("FILA: " + numFila + " con codigo de driver " + codDriverActual + " no existe en el periodo");
          }
        }
      }

      //Se valida que el porcentaje sea 100 en todos los drivers a subir
      for (String codNuevo : lstCodigosAgregar) {
        double porcentajeAcomulado = 0;
        int filas = 0;
        for (MovDriverCentro i : lstCarga) {
          if (i.getCodDriverCentro().equals(codNuevo)) {
            porcentajeAcomulado += i.getPorcentaje();
            filas ++;
          }
        }

        //Si no suma 100 se borra de la lista
        if (round(porcentajeAcomulado) != 100) {
          lstCarga.removeIf(i -> i.getCodDriverCentro().equals(codNuevo));
          numErrores += filas;
          errores.add("Dirver con codigo " + codNuevo
              + " - ERROR: Porcentaje no suma 100, suma: " + round(porcentajeAcomulado * 1000) / 1000);
        }
      }

      //Se limpian los antiguos drivers
      Set<String> set = new HashSet<>(lstCarga.size());
      List<MovDriverCentro> lstDelete =
          lstCarga.stream().filter(p -> set.add(p.getCodDriverCentro())).collect(Collectors.toList());

      if (lstDelete.size() > 0) {
        String deleteQuery = "DELETE FROM MOV_DRIVER_CENTRO \n"
            + "WHERE RepartoTipo = ? AND Periodo = ? and CodDriverCentro=?";
        try {
          jdbcTemplate.batchUpdate(deleteQuery, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setString(3, lstDelete.get(i).getCodDriverCentro());//CodDriver
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

      //se agregan los drivers
      String sql = "insert into MOV_DRIVER_CENTRO \n"
          + "(CodDriverCentro,CodCentroDestino, FechaActualizacion, FechaCreacion, Periodo,Porcentaje,RepartoTipo)\n"
          + "values (?,?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        System.out.println("Ejecutando inserts...");
        if (lstCarga.size() > 0) {
          jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setString(1, lstCarga.get(i).getCodDriverCentro());//CodDriver
              ps.setString(2, lstCarga.get(i).getCodCentroDestino());//CodCentro
              ps.setString(3, fecha);//FechaCreacion
              ps.setString(4, fecha);//FechaActualizacion
              ps.setInt(5, periodo);//Periodo
              ps.setDouble(6, lstCarga.get(i).getPorcentaje());//Porcentaje
              ps.setInt(7, repartoTipo);//Reparto Tipo
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
        numErrores = excel.size();
        errores.add("Se tiene un error al registrar el excel. Verifique que los datos sean correctos");
      }
    } else {
      errores.add("Se tiene un error de formato de cabeceras");
      return errores;
    }
    if (errores.size() > 0) {
      errores.add(0,numErrores + " filas de " + excel.size()
          + " de Mov Driver Centros no se subieron por los siguientes errores: ");
      for (String fila : errores) {
        log.info(fila);
      }
      return errores;
    } else { return null; }
  }

  @Override
  public MovDriverCentro findByCodDriver(String codigo) {
    try {
      String query = "select top 1 CodCentroDestino,FechaActualizacion,FechaCreacion,Periodo,Porcentaje,RepartoTipo \n"
          + "from MOV_DRIVER_CENTRO \n"
          + "where CodDriverCentro=?";
      return
          jdbcTemplate.queryForObject(query, new Object[]{codigo}, BeanPropertyRowMapper.newInstance(MovDriverCentro.class));
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en mov driver ", e);
      return null;
    }
  }

  @Override
  public List<String> findAllCodDriver(int repartoTipo) {
    try {
      log.info("Resultado DriverCentro");
      String query = "select CodDriver from MA_DRIVER where RepartoTipo=? and codDriverTipo = 'CECO'";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaDriverCentro", e);
    }
    return new ArrayList<>();
  }

  /**
   * This method is used to get all codCentros.
   *
   * @return all codCentros.
   */
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


}
