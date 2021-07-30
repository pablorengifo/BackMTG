package pe.com.pacifico.kuntur.repository.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.DetalleGasto;
import pe.com.pacifico.kuntur.repository.DetalleGastoJpaRepository;

/**
 * <b>Class</b>: DetalleServicioJdbcRepository <br/>
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
public class DetalleGastoJdbcRepository implements DetalleGastoJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<DetalleGasto> findAll(int repartoTipo, int periodo) {
    try {
      String querySelect;
      String periodoStr;
      if (repartoTipo == 1) {
        periodoStr = String.valueOf(periodo);
        log.info("Resultado DetalleGasto real");
        querySelect = "select  dg.CodCuentaContable,cc.Nombre as 'NombreCuentaContable', dg.CodPartida,"
            + "pa.Nombre as 'NombrePartida',dg.CodCentro, ce.Nombre 'NombreCentro', "
            + "dg.TipoDocumentoCliente, dg.CodDocumentoCliente, dg.Cliente, dg.Periodo,dg.Monto "
            + "from CUENTA_PARTIDA_CENTRO_R dg "
            + "inner join  MA_CUENTA_CONTABLE cc on dg.CodCuentaContable=cc.CodCuentaContable "
            + "inner join  MA_PARTIDA pa on dg.CodPartida=pa.CodPartida "
            + "inner join  MA_CENTRO ce on dg.CodCentro=ce.CodCentro "
            + "where dg.Periodo=? and cc.RepartoTipo=? and pa.RepartoTipo=? and ce.RepartoTipo=?";
      } else {
        periodo = periodo / 100;
        periodoStr = String.valueOf(periodo) + '%';
        querySelect = "select  dg.CodCuentaContable,cc.Nombre as 'NombreCuentaContable', dg.CodPartida,pa.Nombre as \n"
            + "'NombrePartida',dg.CodCentro, ce.Nombre 'NombreCentro', "
            + "dg.TipoDocumentoCliente, dg.CodDocumentoCliente, dg.Cliente, sum(dg.Monto) as Monto\n"
            + "from CUENTA_PARTIDA_CENTRO_P dg \n"
            + "inner join  MA_CUENTA_CONTABLE cc on dg.CodCuentaContable=cc.CodCuentaContable \n"
            + "inner join  MA_PARTIDA pa on dg.CodPartida=pa.CodPartida \n"
            + "inner join  MA_CENTRO ce on dg.CodCentro=ce.CodCentro \n"
            + "where dg.Periodo like ? and cc.RepartoTipo=? and pa.RepartoTipo=? and ce.RepartoTipo=?\n"
            + "group by dg.CodCuentaContable,cc.Nombre,dg.CodPartida,pa.Nombre,dg.CodCentro,ce.Nombre,"
            + "dg.TipoDocumentoCliente, dg.CodDocumentoCliente, dg.Cliente";
      }

      return jdbcTemplate.query(querySelect, BeanPropertyRowMapper.newInstance(DetalleGasto.class),
          periodoStr, repartoTipo, repartoTipo, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en Detalle Costo ", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> findAllCodCuentaContable(int repartoTipo, int periodo) {
    try {
      log.info("Resultado CuentaContable");
      String query;
      if (repartoTipo == 1) {
        query = "SELECT CodCuentaContable FROM MOV_CUENTA_CONTABLE WHERE RepartoTipo = 1 and periodo = ?";
        return jdbcTemplate.queryForList(query, String.class, periodo);
      } else {
        query = "SELECT CodCuentaContable FROM MOV_CUENTA_CONTABLE "
            + "WHERE RepartoTipo = 2 and periodo>=? and Periodo< ?";
        return jdbcTemplate.queryForList(query, String.class, periodo, periodo + 100);
      }
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en CUENTA_PARTIDA_CENTRO", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> findAllCodPartida(int repartoTipo, int periodo) {
    try {
      log.info("Resultado CuentaContable");
      String query;
      if (repartoTipo == 1) {
        query = "SELECT CodPartida FROM MOV_PARTIDA WHERE RepartoTipo = 1 and periodo = ?";
        return jdbcTemplate.queryForList(query, String.class, periodo);
      } else {
        query = "SELECT CodPartida FROM MOV_PARTIDA "
            + "WHERE RepartoTipo = 2 and periodo>=? and Periodo< ?";
        return jdbcTemplate.queryForList(query, String.class, periodo, periodo + 100);
      }
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en CUENTA_PARTIDA_CENTRO", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> findAllCodCentro(int repartoTipo, int periodo) {
    try {
      String query;
      if (repartoTipo == 1) {
        query = "SELECT CodCentro FROM MOV_CENTRO WHERE RepartoTipo = 1 and periodo = ?";
        return jdbcTemplate.queryForList(query, String.class, periodo);
      } else {
        query = "SELECT CodCentro FROM MOV_CENTRO "
            + "WHERE RepartoTipo = 2 and periodo>=? and Periodo< ?";
        return jdbcTemplate.queryForList(query, String.class, periodo, periodo + 100);
      }
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en CUENTA_PARTIDA_CENTRO", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> generarDetalleGasto(int repartoTipo, int periodo) {
    List<String> errores = null;
    try
    {
      if (repartoTipo == 1)
      {
        String deleteQuery = "DELETE FROM CUENTA_PARTIDA_CENTRO_R WHERE  Periodo = ?";
        jdbcTemplate.update(deleteQuery, periodo);
        String sql = "insert into CUENTA_PARTIDA_CENTRO_R (CodCuentaContable,CodPartida,CodCentro, TipoDocumentoCliente, \n"
            + "CodDocumentoCliente, Cliente, Periodo, FechaCreacion, FechaActualizacion, Monto)\n"
            + "(select CodCuentaContable,CodPartida,CodCentro, TipoDocumentoCliente, CodDocumentoCliente, Cliente,\n"
            + "periodo, GETDATE() as 'FechaCreacion',GETDATE() as 'FechaActualizacion',SUM(Monto) as Monto from EXACTUS\n"
            + "where RepartoTipo=? and Periodo=?\n"
            + "group by CodCuentaContable,CodPartida,CodCentro, TipoDocumentoCliente, CodDocumentoCliente, Cliente, Periodo);";
        jdbcTemplate.update(sql,repartoTipo,periodo);
      } else
      {
        String deleteQuery = "DELETE FROM CUENTA_PARTIDA_CENTRO_P where periodo>=? and Periodo< ? ";
        jdbcTemplate.update(deleteQuery, periodo, periodo + 100);
        for (int i = 1 ; i <= 12 ; i++)
        {
          String sql = "insert into CUENTA_PARTIDA_CENTRO_P (CodCuentaContable,CodPartida,CodCentro, TipoDocumentoCliente, \n"
              + "CodDocumentoCliente, Cliente, Periodo, FechaCreacion, FechaActualizacion, Monto)\n"
              + "(select CodCuentaContable,CodPartida,CodCentro, TipoDocumentoCliente, CodDocumentoCliente, Cliente,\n"
              + "?, GETDATE() as 'FechaCreacion',GETDATE() as 'FechaActualizacion',SUM(Monto)/12 as Monto from EXACTUS\n"
              + "where RepartoTipo=? and Periodo=?\n"
              + "group by CodCuentaContable,CodPartida,CodCentro, TipoDocumentoCliente, CodDocumentoCliente, Cliente);";
          jdbcTemplate.update(sql,periodo + i,repartoTipo,periodo);
        }
      }
    } catch (Exception e)
    {
      errores = new ArrayList<>();
      errores.add("Se tiene un error al generar el detalle de gasto");
      errores.add(e.getMessage());
    }
    return errores;
  }

  @Override
  public List<String> saveExcelToBdReal(List<List<CellData>> excel, int periodo) {
    int numErrores = 0;
    List<String> errores = new ArrayList<>();
    log.info("CANTIDAD FILAS EXCEL (CON HEADERS): " + excel.size());
    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("codigo cuenta contable")
        && excel.get(0).get(2).getValue().equalsIgnoreCase("codigo partida")
        && excel.get(0).get(4).getValue().equalsIgnoreCase("codigo centro")
        && excel.get(0).get(6).getValue().equalsIgnoreCase("monto")) {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      System.out.println("1) Map carga");
      Map<String, Double> mapCarga = new HashMap();
      Map<String, Double> map01 = new HashMap();
      Map<String, Double> mapAcum = new HashMap();
      Map<String, Integer> numFilas = new HashMap();
      /*List<String> lstCodigosCuentaContable = findAllCodCuentaContable(1,periodo);
      List<String> lstCodigosPartias = findAllCodCuentaContable(1,periodo);
      List<String> lstCodigosCentros = findAllCodCuentaContable(1,periodo);*/
      List<String> lstCodigosCuentaContable = findAllCodCuentaContable(1, periodo);
      List<String> lstCodigosPartidas = findAllCodPartida(1, periodo);
      List<String> lstCodigosCentros = findAllCodCentro(1, periodo);
      //DetalleGasto detalleGasto;
      boolean combinacionExiste;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      System.out.println("2) Recorrido");
      for (List<CellData> fila : excel) {
        combinacionExiste = false;
        //detalleGasto = new DetalleGasto();
        numFila++;

        String codCuentaActual = fila.get(0).getValue();
        String codPartidaActual = fila.get(2).getValue();
        String codCentroActual = fila.get(4).getValue();

        String codCuentaBD = lstCodigosCuentaContable.stream().filter(codCuentaActual::equals).findAny().orElse(null);
        String codPartidaBD = lstCodigosPartidas.stream().filter(codPartidaActual::equals).findAny().orElse(null);
        String codCentroBD = lstCodigosCentros.stream().filter(codCentroActual::equals).findAny().orElse(null);

        String key = codCuentaActual + "~" + codPartidaActual + "~" + codCentroActual;
        numFilas.put(key, numFila);

        //VALIDA QUE los codigos SI EXISTAN EN BD
        if (codCuentaBD != null && codCentroBD != null && codPartidaBD != null) {
          combinacionExiste = true;
        }

        if (combinacionExiste) {
          try {
            double rowValue = Double.parseDouble(fila.get(6).getValue());
            double acumVal = rowValue + mapCarga.getOrDefault(key, 0d);
            mapCarga.put(key, acumVal);
            //lstCodigos.removeIf(x -> x.equals(codCuentaActual));
            // Agregar segunda validación: CodigoCuenta/CodigoPartida/Centro.01 vs CodigoCuenta/CodigoPartida/Centro.XX
            String last2 = codCentroActual.substring(codCentroActual.length() - 2);
            String key2 = codCuentaActual + "~" + codPartidaActual;
            if (last2.equals("01")) {
              map01.put(key2, map01.getOrDefault(key2, .0) + rowValue);
            } else {
              mapAcum.put(key2, mapAcum.getOrDefault(key2, .0) + rowValue);
            }
          } catch (Exception e)
          {
            e.printStackTrace();
            log.info(e.toString());
            errores.add("fila: " + numFilas.getOrDefault(key, numFila) + " con codigo de centro" + codCentroActual
                + " Presenta un monto que no es un numero");
            numErrores++;
          }
        } else {
          numErrores++;
          if (codCuentaBD == null) {
            errores.add("FILA: " + numFila + " con codigo de cuenta" + codCuentaActual + " no existe en el periodo");
          }
          if (codPartidaBD == null) {
            errores.add("FILA: " + numFila + " con codigo de partida" + codPartidaActual + " no existe en el periodo");
          }
          if (codCentroBD == null) {
            errores.add("FILA: " + numFila + " con codigo de centro" + codCentroActual + " no existe en el periodo");
          }
        }

      }
      System.out.println("Map carga: " + mapCarga.size());
      List<DetalleGasto> lstCarga = new ArrayList<>();
      for (String key : mapCarga.keySet()) {
        String[] splittedKey = key.split("~");
        DetalleGasto detalleGasto = new DetalleGasto();
        detalleGasto.setCodCuentaContable(splittedKey[0]);
        detalleGasto.setCodPartida(splittedKey[1]);
        detalleGasto.setCodCentro(splittedKey[2]);
        String key2 = splittedKey[0] + "~" + splittedKey[1];
        double monto = mapCarga.get(key);
        monto = Double.parseDouble(String.format("%.4f", monto));
        // Ejecutar validacion 2
        double montoAcum = Double.parseDouble(String.format("%.4f", mapAcum.getOrDefault(key2, .0)));
        double monto01 = Double.parseDouble(String.format("%.4f", map01.getOrDefault(key2, .0)));

        if (monto01 == montoAcum) {
          errores.add(String.format("En la fila %s para la combinación de cuenta contable (%s) y código de partida (%s)"
              + ", las sumas de los centros con .01 (%s) y del resto (%s) son iguales.", numFilas.getOrDefault(key, numFila),
              splittedKey[0], splittedKey[1], monto01, montoAcum));
          numErrores++;
        } else {
          detalleGasto.setMonto(monto);
          lstCarga.add(detalleGasto);
        }
      }
      System.out.println("List carga: " + lstCarga.size());
      //se usa para limpiar la tabla antes de agregar la nueva data
      if (lstCarga.size() > 0) {
        String deleteQuery = "DELETE FROM CUENTA_PARTIDA_CENTRO_R WHERE  Periodo = ?";
        jdbcTemplate.update(deleteQuery, periodo);

        String sql = "insert into CUENTA_PARTIDA_CENTRO_R "
            + "(CodCuentaContable, CodPartida,CodCentro,Periodo,FechaCreacion,FechaActualizacion,Monto) "
            + "values (?,?,?,?,?,?,?);";
        System.out.println("Insertando...");
        try {
          Date date = new Date();
          String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
          DecimalFormat df = new DecimalFormat("#.0000");
          jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setString(1, lstCarga.get(i).getCodCuentaContable());//CodCuentaContable
              ps.setString(2, lstCarga.get(i).getCodPartida()); //cod partida
              ps.setString(3, lstCarga.get(i).getCodCentro()); //cod centro
              ps.setInt(4, periodo);//Periodo
              ps.setString(5, fecha);//FechaCreacion
              ps.setString(6, fecha);//FechaActualizacion
              ps.setDouble(7, lstCarga.get(i).getMonto());//Monto
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
      errores.add("Error en el formato de las cabeceras. Garantice que no se tengan tildes en las cabeceras archivo");

      if (!excel.get(0).get(0).getValue().equalsIgnoreCase("codigo cuenta contable")) {
        errores.add("La primera fila debe llamarse codigo cuenta contable");
      }

      if (!excel.get(0).get(2).getValue().equalsIgnoreCase("codigo partida")) {
        errores.add("La primera fila debe llamarse codigo partida");
      }

      if (!excel.get(0).get(4).getValue().equalsIgnoreCase("codigo centro")) {
        errores.add("La primera fila debe llamarse codigo centro");
      }

      if (!excel.get(0).get(6).getValue().equalsIgnoreCase("monto")) {
        errores.add("La primera fila debe llamarse monto");
      }
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
  public List<String> saveExcelToBdPresupuesto(List<List<CellData>> excel, int periodo) {
    int numErrores = 0;
    List<String> errores = new ArrayList<>();
    log.info("CANTIDAD FILAS EXCEL (CON HEADERS): " + excel.size());
    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("codigo cuenta contable")
        && excel.get(0).get(2).getValue().equalsIgnoreCase("codigo partida")
        && excel.get(0).get(4).getValue().equalsIgnoreCase("codigo centro")
        && excel.get(0).get(6).getValue().equalsIgnoreCase("monto 1")
        && excel.get(0).get(7).getValue().equalsIgnoreCase("monto 2")
        && excel.get(0).get(8).getValue().equalsIgnoreCase("monto 3")
        && excel.get(0).get(9).getValue().equalsIgnoreCase("monto 4")
        && excel.get(0).get(10).getValue().equalsIgnoreCase("monto 5")
        && excel.get(0).get(11).getValue().equalsIgnoreCase("monto 6")
        && excel.get(0).get(12).getValue().equalsIgnoreCase("monto 7")
        && excel.get(0).get(13).getValue().equalsIgnoreCase("monto 8")
        && excel.get(0).get(14).getValue().equalsIgnoreCase("monto 9")
        && excel.get(0).get(15).getValue().equalsIgnoreCase("monto 10")
        && excel.get(0).get(16).getValue().equalsIgnoreCase("monto 11")
        && excel.get(0).get(17).getValue().equalsIgnoreCase("monto 12")
    ) {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);
      //PARAMETROS CARGA
      List<DetalleGasto> lstCarga = new ArrayList<>();
      List<List<Double>> montoAnos = new ArrayList<>();

      List<String> lstCodigosCuentaContable = findAllCodCuentaContable(2, periodo);
      List<String> lstCodigosPartidas = findAllCodPartida(2, periodo);
      List<String> lstCodigosCentros = findAllCodCentro(2, periodo);
      DetalleGasto detalleGasto;
      boolean combinacionExiste;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        combinacionExiste = false;
        detalleGasto = new DetalleGasto();
        numFila++;


        String codCuentaActual = fila.get(0).getValue();
        String codPartidaActual = fila.get(2).getValue();
        String codCentroActual = fila.get(4).getValue();

        String codCuentaBD = lstCodigosCuentaContable.stream().filter(codCuentaActual::equals).findAny().orElse(null);
        String codPartidaBD = lstCodigosPartidas.stream().filter(codPartidaActual::equals).findAny().orElse(null);
        String codCentroBD = lstCodigosCentros.stream().filter(codCentroActual::equals).findAny().orElse(null);

        //VALIDA QUE los codigos  SI EXISTAN EN BD
        if (codCuentaBD != null && codCentroBD != null && codPartidaBD != null) {
          combinacionExiste = true;
        }

        if (combinacionExiste) {
          detalleGasto.setCodCuentaContable(codCuentaActual);
          detalleGasto.setCodPartida(codPartidaActual);
          detalleGasto.setCodCentro(codCentroActual);

          try
          {
            List<Double> montoEnUnAno = new ArrayList<>();
            montoEnUnAno.add(Double.valueOf(fila.get(6).getValue()));
            montoEnUnAno.add(Double.valueOf(fila.get(7).getValue()));
            montoEnUnAno.add(Double.valueOf(fila.get(8).getValue()));
            montoEnUnAno.add(Double.valueOf(fila.get(9).getValue()));
            montoEnUnAno.add(Double.valueOf(fila.get(10).getValue()));
            montoEnUnAno.add(Double.valueOf(fila.get(11).getValue()));
            montoEnUnAno.add(Double.valueOf(fila.get(12).getValue()));
            montoEnUnAno.add(Double.valueOf(fila.get(13).getValue()));
            montoEnUnAno.add(Double.valueOf(fila.get(14).getValue()));
            montoEnUnAno.add(Double.valueOf(fila.get(15).getValue()));
            montoEnUnAno.add(Double.valueOf(fila.get(16).getValue()));
            montoEnUnAno.add(Double.valueOf(fila.get(17).getValue()));

            lstCarga.add(detalleGasto);
            montoAnos.add(montoEnUnAno);
          } catch (Exception e)
          {
            log.info(e.toString());
            errores.add("fila: " + numFila + " con codigo de centro" + codCentroActual
                + " Presenta algun monto que no es un numero");
            numErrores++;
          }
          //lstCodigos.removeIf(x -> x.equals(codCuentaActual));
        } else {
          numErrores++;
          if (codCuentaBD == null) {
            errores.add("FILA: " + numFila + "con codigo de cuenta" + codCuentaActual + " no existe en el periodo");
          }
          if (codPartidaBD == null) {
            errores.add("FILA: " + numFila + "con codigo de partida" + codPartidaActual + " no existe en el periodo");
          }
          if (codCentroBD == null) {
            errores.add("FILA: " + numFila + "con codigo de centro" + codCentroActual + " no existe en el periodo");
          }
        }
      }
      System.out.println("llego hasta el delete con:" + lstCarga.size() + "de carga size");
      if (lstCarga.size() > 0) {
        String deleteQuery = "DELETE FROM CUENTA_PARTIDA_CENTRO_P where periodo>=? and Periodo< ? ";
        jdbcTemplate.update(deleteQuery, periodo, periodo + 100);
        System.out.println("paso el delete");

        //Se itera por cada mes
        for (int i = 0; i < 12; i++) {
          String sql = "insert into CUENTA_PARTIDA_CENTRO_P "
              + "(CodCuentaContable, CodPartida,CodCentro,Periodo,FechaCreacion,FechaActualizacion,Monto) "
              + "values (?,?,?,?,?,?,?);";

          try {
            final int mesPasado = i;
            Date date = new Date();
            String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

              @Override
              public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, lstCarga.get(i).getCodCuentaContable());//CodCuentaContable
                ps.setString(2, lstCarga.get(i).getCodPartida()); //CodPartida
                ps.setString(3, lstCarga.get(i).getCodCentro()); //CodCentro
                ps.setInt(4, periodo + mesPasado + 1);//Periodo
                ps.setString(5, fecha);//FechaCreacion
                ps.setString(6, fecha);//FechaActualizacion
                ps.setDouble(7, montoAnos.get(i).get(mesPasado));//Saldo
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
