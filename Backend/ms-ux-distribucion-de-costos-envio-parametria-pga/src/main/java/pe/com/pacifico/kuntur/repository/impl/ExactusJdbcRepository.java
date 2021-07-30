package pe.com.pacifico.kuntur.repository.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
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
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.Exactus;
import pe.com.pacifico.kuntur.repository.ExactusJpaRepository;



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
public class ExactusJdbcRepository implements ExactusJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<Exactus> findAll(int repartoTipo, int periodo) {
    try {
      String querySelect = "select ex.CodCuentaContable,cc.Nombre as 'NombreCuentaContable', ex.CodPartida,"
          + "pa.Nombre as 'NombrePartida',ex.CodCentro, ce.Nombre 'NombreCentro',ex.TipoDocumentoCliente,ex.CodDocumentoCliente,"
          + "ex.Cliente,ex.Monto,ex.RepartoTipo,ex.Periodo,ex.Asiento,ex.TipoReferenciaDocumento,ex.DocumentoContabilizado,"
          + "ex.Referencia, ex.FechaContable, ex.OrigenNegocio, ex.Moneda, ex.MontoMonedaOrigen, ex.TipoMovimiento "
          + "from EXACTUS ex "
          + "inner join  MA_CUENTA_CONTABLE cc on ex.CodCuentaContable=cc.CodCuentaContable "
          + "inner join  MA_PARTIDA pa on ex.CodPartida=pa.CodPartida "
          + "inner join  MA_CENTRO ce on ex.CodCentro=ce.CodCentro "
          + "where ex.Periodo=? and cc.RepartoTipo=? and pa.RepartoTipo=? and ce.RepartoTipo=?";

      return jdbcTemplate.query(querySelect, BeanPropertyRowMapper.newInstance(Exactus.class),
          periodo, repartoTipo, repartoTipo, repartoTipo);
    } catch (EmptyResultDataAccessException e)
    {
      log.warn("Resultado vacío en Detalle Costo ", e);
    }
    return  new ArrayList<>();
  }

  @Override
  public List<String> saveExcelToBd(List<List<CellData>> excel, int repartoTipo, int periodo) {
    int numErrores = 0;
    List<String> errores = new ArrayList<>();
    log.info("CANTIDAD FILAS EXCEL (CON HEADERS): " + excel.size());

    if (excel.get(0).get(0).getValue().equalsIgnoreCase("CUENTA CONTABLE")
        && excel.get(0).get(2).getValue().equalsIgnoreCase("ASIENTO")
        && excel.get(0).get(3).getValue().equalsIgnoreCase("TIPO DE DOCUMENTO")
        && excel.get(0).get(4).getValue().equalsIgnoreCase("DOCUMENTO")
        && excel.get(0).get(5).getValue().equalsIgnoreCase("REFERENCIA")
        && excel.get(0).get(6).getValue().equalsIgnoreCase("DÉBITO LOCAL")
        && excel.get(0).get(7).getValue().equalsIgnoreCase("DÉBITO DOLAR")
        && excel.get(0).get(8).getValue().equalsIgnoreCase("CRÉDITO LOCAL")
        && excel.get(0).get(9).getValue().equalsIgnoreCase("CRÉDITO DOLAR")
        && excel.get(0).get(10).getValue().equalsIgnoreCase("CENTRO COSTO")
        && excel.get(0).get(12).getValue().equalsIgnoreCase("TIPO DE ASIENTO")
        && excel.get(0).get(13).getValue().equalsIgnoreCase("FECHA")
        && excel.get(0).get(14).getValue().equalsIgnoreCase("NIT")
        && excel.get(0).get(15).getValue().equalsIgnoreCase("RAZÓN SOCIAL")
        && excel.get(0).get(20).getValue().equalsIgnoreCase("Partida")) {
      List<String> lstCodigosCuentaContable = findAllCodCuentaContable(repartoTipo, periodo);
      List<String> lstCodigosPartidas = findAllCodPartida(repartoTipo, periodo);
      List<String> lstCodigosCentros = findAllCodCentro(repartoTipo, periodo);
      boolean combinacionExiste;
      int numFila = 0;
      excel.remove(0);

      List<Exactus> listaExactus = new ArrayList<>();

      for (List<CellData> fila : excel) {
        combinacionExiste = false;
        numFila++;
        String codCuentaActual = fila.get(0).getValue();
        String codPartidaActual = fila.get(20).getValue().equalsIgnoreCase("")
            ? "FI00" : fila.get(20).getValue();
        String codCentroActual = fila.get(10).getValue();

        String codCuentaBD = lstCodigosCuentaContable.stream().filter(codCuentaActual::equals).findAny().orElse(null);
        String codPartidaBD = lstCodigosPartidas.stream().filter(codPartidaActual::equals).findAny().orElse(null);
        String codCentroBD = lstCodigosCentros.stream().filter(codCentroActual::equals).findAny().orElse(null);

        if (codCuentaBD != null && codCentroBD != null && codPartidaBD != null) {
          combinacionExiste = true;
        }

        if (combinacionExiste) {
          boolean error = false;
          Exactus exactus = new Exactus();

          exactus.setCodCuentaContable(codCuentaActual);
          exactus.setCodCentro(codCentroActual);

          /*
            Si la partida es vacia se le pone la partida FI00.
            Pero si el tipo de centro es bolsa no se agrega el registro.
           */
          if (codPartidaActual.equalsIgnoreCase("FI00"))
          {
            String tipoCentro = findTipoCentro(codCentroActual,repartoTipo);
            if (tipoCentro == null)
            {
              error = true;
              errores.add("La fila " + numFila + " presenta un codigo sin tipo en la base de datos");
            } else if (!tipoCentro.equalsIgnoreCase("BOLSA"))
            {
              exactus.setCodPartida(codPartidaActual);
            } else
            {
              error = true;
              errores.add("La fila " + numFila + " presenta una partida nula con un centro del tipo bolsa");
            }
          } else { exactus.setCodPartida(codPartidaActual); }



          //documento cliente
          String codDocumento = fila.get(14).getValue();
          try {
            Long.parseLong(codDocumento);
            if (codDocumento.length() <= 8) {
              exactus.setTipoDocumentoCliente("DNI");
            } else {
              exactus.setTipoDocumentoCliente("RUC");
            }

          } catch (NumberFormatException e) {
            exactus.setTipoDocumentoCliente("OTRO");
          }
          exactus.setCodDocumentoCliente(codDocumento);
          exactus.setCliente(fila.get(15).getValue());

          //generar monto
          try {
            double debito = Double.parseDouble(fila.get(6).getValue().equalsIgnoreCase("")
                ? "0" : fila.get(6).getValue());
            double credito = Double.parseDouble(fila.get(8).getValue().equalsIgnoreCase("")
                ? "0" : fila.get(8).getValue());
            exactus.setMonto(debito - credito);
          } catch (NumberFormatException e) {
            error = true;
            errores.add("El debito local o el credito local de la fila " + numFila + " no es un numero");
          }

          exactus.setRepartoTipo(repartoTipo);
          exactus.setPeriodo(periodo);
          exactus.setAsiento(fila.get(2).getValue());
          exactus.setTipoReferenciaDocumento(fila.get(3).getValue().equalsIgnoreCase("") ? "-" : fila.get(3).getValue());
          exactus.setDocumentoContabilizado(fila.get(4).getValue().equalsIgnoreCase("") ? "-" : fila.get(4).getValue());
          exactus.setReferencia(fila.get(5).getValue().equalsIgnoreCase("") ? "-" : fila.get(5).getValue());
          exactus.setOrigenNegocio(generarOrigenNegocio(codCentroActual));

          String moneda = generarMoneda(codCuentaActual);
          exactus.setMoneda(moneda);

          //fecha contable
          try {
            String fechaC = fila.get(13).getValue();
            Date fechaContable = new SimpleDateFormat("dd/MM/yyyy").parse(fechaC);
            exactus.setFechaContable(fechaContable);
          } catch (ParseException e) {
            e.printStackTrace();
            error = true;
            errores.add("La fecha contable no tiene formato de fecha en la fila:  " + numFila + ". "
                + "Garantice que tenga el formato de dd/MM/yyyy");
          }

          //monto moneda origen
          if (moneda.equalsIgnoreCase("soles") && !error) {
            exactus.setMontoMonedaOrigen(exactus.getMonto());
          } else {
            try {
              double debito = Double.parseDouble(fila.get(7).getValue().equalsIgnoreCase("")
                  ? "0" : fila.get(7).getValue());
              double credito = Double.parseDouble(fila.get(9).getValue().equalsIgnoreCase("")
                  ? "0" : fila.get(9).getValue());
              exactus.setMonto(debito - credito);
            } catch (NumberFormatException e) {
              error = true;
              errores.add("El debito dolar o el credito dolar de la fila " + numFila + " no es un numero");
            }
          }
          exactus.setTipoMovimiento(exactus.getMonto() > 0 ? "Debito" : "Credito");

          if (!error) {
            listaExactus.add(exactus);
          } else {
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

      if (listaExactus.size() > 0) {
        String deleteQuery = "DELETE FROM EXACTUS WHERE  Periodo = ? and RepartoTipo = ?";
        jdbcTemplate.update(deleteQuery, periodo, repartoTipo);

        String sql = "insert into EXACTUS "
            + "(CodCuentaContable, CodPartida,CodCentro,TipoDocumentoCliente,CodDocumentoCliente,Cliente,Monto,"
            + "RepartoTipo,Periodo,Asiento,TipoReferenciaDocumento,DocumentoContabilizado,Referencia,FechaContable,"
            + "OrigenNegocio,Moneda,MontoMonedaOrigen,TipoMovimiento,FechaCreacion) "
            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        System.out.println("Insertando...");
        try {
          Date date = new Date();
          String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
          jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setString(1, listaExactus.get(i).getCodCuentaContable());
              ps.setString(2, listaExactus.get(i).getCodPartida());
              ps.setString(3, listaExactus.get(i).getCodCentro());
              ps.setString(4, listaExactus.get(i).getTipoDocumentoCliente());
              ps.setString(5, listaExactus.get(i).getCodDocumentoCliente());
              ps.setString(6, listaExactus.get(i).getCliente());
              ps.setDouble(7, listaExactus.get(i).getMonto());
              ps.setInt(8, listaExactus.get(i).getRepartoTipo());
              ps.setInt(9, listaExactus.get(i).getPeriodo());
              ps.setString(10, listaExactus.get(i).getAsiento());
              ps.setString(11, listaExactus.get(i).getTipoReferenciaDocumento());
              ps.setString(12, listaExactus.get(i).getDocumentoContabilizado());
              ps.setString(13, listaExactus.get(i).getReferencia());
              ps.setString(14, new SimpleDateFormat("yyyy-MM-dd")
                  .format(listaExactus.get(i).getFechaContable()));
              ps.setString(15, listaExactus.get(i).getOrigenNegocio());
              ps.setString(16, listaExactus.get(i).getMoneda());
              ps.setDouble(17, listaExactus.get(i).getMontoMonedaOrigen());
              ps.setString(18, listaExactus.get(i).getTipoMovimiento());
              ps.setString(19, fecha);
            }

            @Override
            public int getBatchSize() {
              return listaExactus.size();
            }
          });
          log.info("SE SUBIERON " + listaExactus.size() + " DE " + excel.size() + " FILAS.");
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

  private String findTipoCentro(String codCentro, int repartoTipo)
  {
    try
    {
      String sql = "select Tipo from MA_CENTRO where CodCentro=? and RepartoTipo=?";
      return jdbcTemplate.queryForObject(sql,String.class,codCentro,repartoTipo);
    } catch (EmptyResultDataAccessException e)
    {
      return null;
    }
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

  private String generarOrigenNegocio(String codCentro) {

    String[] parts = codCentro.split("\\.");
    if (!parts[0].equalsIgnoreCase("60"))
    {
      return "Vida";
    } else
    {
      return "General";
    }
  }

  private String generarMoneda(String codCuentaContable) {

    String[] parts = codCuentaContable.split("\\.");
    if (parts[1].equalsIgnoreCase("1"))
    {
      return "soles";
    } else
    {
      return "dolares";
    }
  }
}
