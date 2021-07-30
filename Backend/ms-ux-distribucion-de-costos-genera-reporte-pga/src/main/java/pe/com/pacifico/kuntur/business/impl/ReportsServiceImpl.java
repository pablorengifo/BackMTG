package pe.com.pacifico.kuntur.business.impl;

import java.math.BigDecimal;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.List;

import java.util.Map;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.com.pacifico.kuntur.business.ExcelService;
import pe.com.pacifico.kuntur.business.ReportsService;
import pe.com.pacifico.kuntur.business.exception.BusinessErrorCodes;
import pe.com.pacifico.kuntur.business.exception.BusinessException;
import pe.com.pacifico.kuntur.business.threads.ReportesHilo;
import pe.com.pacifico.kuntur.expose.response.FiltroLineaResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte1BolsasOficinasResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte2CascadaResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte3ObjetosResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte4CascadaCentrosResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte5LineaCanalResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte6Control1Response;
import pe.com.pacifico.kuntur.expose.response.Reporte7Agile1CentrosResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte8NiifResponse;
import pe.com.pacifico.kuntur.model.FiltroLinea;
import pe.com.pacifico.kuntur.model.Reporte1BolsasOficinas;
import pe.com.pacifico.kuntur.model.Reporte2Cascada;
import pe.com.pacifico.kuntur.model.Reporte3Objetos;

import pe.com.pacifico.kuntur.model.Reporte4CascadaCentros;
import pe.com.pacifico.kuntur.model.Reporte5LineaCanal;
import pe.com.pacifico.kuntur.model.Reporte6Control1;
import pe.com.pacifico.kuntur.model.Reporte7Agile1Centros;
import pe.com.pacifico.kuntur.model.Reporte8Niff;
import pe.com.pacifico.kuntur.model.Reports;
import pe.com.pacifico.kuntur.repository.ReportsJpaRepository;
import pe.com.pacifico.kuntur.webclient.ReportsWebClient;
import reactor.core.publisher.Flux;

import reactor.core.publisher.Mono;

/**
 * <b>Class</b>: ReportsServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Pacifico Seguros - La Chakra <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 23, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ReportsServiceImpl implements ReportsService {

  @Autowired
  private final ExcelService excelService;

  private final ReportsWebClient reportsWebClient;
  private final ReportsJpaRepository reportsJpaRepository;


  /**
   * This method is used to get all reports.
   *
   * @return all reports.
   */
  public Flux<Reports> getReports() {
    log.error("An exception occurred! {}", BusinessErrorCodes.BUSINESS_ERROR.getDescription());
    return Flux.error(BusinessException.createException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            BusinessErrorCodes.BUSINESS_ERROR));
  }

  /**
   * This method is used to get only one reports.
   *
   * @param id This is the first parameter to method.
   * @return one reports.
   */
  public Mono<Reports> getReports(Long id) {
    return reportsWebClient.getReportsResponse(id)
            .map(t -> Reports.builder()
                    .id(t.getIdController())
                    .data(t.getData())
                    .cardNumber(t.getCardNumber())
                    .documentNumber(t.getDocumentNumber())
                    .build());
  }

  /**
   * This method is used to get only one reports.
   *
   * @return Report as Response Entity.
   */
  @Override
  public boolean getReporte(int reporte, int repartoTipo, int periodo) {
    ReportesHilo.begin(reporte, repartoTipo, periodo, this);
    return true;
  }

  /**
   * This method is used to get only one reports.
   *
   * @return Report as Response Entity.
   */
  @Override
  public boolean getReporte5(int repartoTipo, int periodo, List<String> dimensiones,
                             List<String> lineas, List<String> canales) {
    ReportesHilo.dimensiones = dimensiones;
    ReportesHilo.lineas = lineas;
    ReportesHilo.canales = canales;

    ReportesHilo.begin(5, repartoTipo, periodo, this);
    return true;
  }

  /**
   * This method is used to get only one reports.
   *
   * @return Report as Response Entity.
   */
  public Resource generateReporte1(int repartoTipo, int periodo) {
    try {
      System.out.println("Llega servicio reporte 1");
      SXSSFWorkbook wb = excelService.crearLibro();
      CellStyle cabeceraEstilo = excelService.cabeceraEstilo(wb);
      CellStyle bordeEstilo = excelService.bordeEstilo(wb);
      CellStyle fechaEstilo = excelService.fechaEstilo(wb);
      SXSSFSheet sh = wb.createSheet("Reporte 1");

      List<Map<String, Object>> reporte1 = reportsJpaRepository.obtenerReporte1BolsasOficinas(repartoTipo, periodo);
      System.out.println("Despues de jdbc");
      List<Integer> widths = Arrays.asList(2000, 3800, 12000, 2000, 8000, 3000, 4000, 2000, 3000, 3000, 7000, 3000,
              4000, 4000, 4000,7000, 4000, 4000, 6000, 3000);

      if (reporte1 == null || reporte1.size() == 0) {
        reporte1 = reportsJpaRepository.obtenerReporte1Vacio();
        excelService.crearCabecera(sh, 0, reporte1, widths, cabeceraEstilo);
        return excelService.crearResource(wb);
      }
      System.out.println("Checkpoint 1");
      int rowNum = 0;
      excelService.crearCabecera(sh, rowNum++, reporte1, widths, cabeceraEstilo);
      System.out.println("Checkpoint 2");
      for (Map<String, Object> fila : reporte1) {
        int colNum = 0;
        Row row = sh.createRow(rowNum++);
        try {
          excelService.createCell(row, colNum++, ((Number) fila.get("Periodo")).intValue(), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CodCuentaContableOrigen"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CuentaContableOrigenNombre"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CodPartidaOrigen"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("PartidaOrigenNombre"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CodCentroOrigen"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CentroOrigenNombre"), bordeEstilo);
          excelService.createCell(row, colNum++, ((Number) fila.get("CentroOrigenNivel")).intValue(), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CentroOrigenTipo"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CodCentroDestino"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CentroDestinoNombre"), bordeEstilo);
          excelService.createCell(row, colNum++, ((Number) fila.get("CentroDestinoNivel")).intValue(), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CentroDestinoTipo"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("TipoDocumentoClienteOrigen"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CodDocumentoClienteOrigen"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("ClienteOrigen"), bordeEstilo);
          excelService.createCell(row, colNum++, ((BigDecimal) fila.get("Monto")).doubleValue(), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CodDriver"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("DriverNombre"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("Asignacion"), bordeEstilo);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      System.out.println("Checkpoint 3");
      return excelService.crearResource(wb);
    } catch (Exception e) {
      System.out.println("Falló");
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Flux<Reporte1BolsasOficinasResponse> getReporte1Limited(int repartoTipo, int periodo) {
    System.out.println("Llega servicio reporte 1");
    return Flux.fromIterable(reportsJpaRepository.obtenerReporte1BolsasOficinasLimited(repartoTipo, periodo))
            .map(this::asignarReporte1);
  }

  private Reporte1BolsasOficinasResponse asignarReporte1(Reporte1BolsasOficinas r) {
    return Reporte1BolsasOficinasResponse.builder()
            .asignacion(r.getAsignacion())
            .centroDestinoNivel(r.getCentroDestinoNivel())
            .centroDestinoNombre(r.getCentroDestinoNombre())
            .centroDestinoTipo(r.getCentroDestinoTipo())
            .centroOrigenNivel(r.getCentroOrigenNivel())
            .centroOrigenNombre(r.getCentroOrigenNombre())
            .centroOrigenTipo(r.getCentroOrigenTipo())
            .codCentroDestino(r.getCodCentroDestino())
            .codCentroOrigen(r.getCodCentroOrigen())
            .codCuentaContableOrigen(r.getCodCuentaContableOrigen())
            .codDriver(r.getCodDriver())
            .codPartidaOrigen(r.getCodPartidaOrigen())
            .cuentaContableOrigenNombre(r.getCuentaContableOrigenNombre())
            .driverNombre(r.getDriverNombre())
            .monto(r.getMonto())
            .partidaOrigenNombre(r.getPartidaOrigenNombre())
            .periodo(r.getPeriodo())
            .clienteOrigen(r.getClienteOrigen())
            .codDocumentoClienteOrigen(r.getCodDocumentoClienteOrigen())
            .tipoDocumentoClienteOrigen(r.getTipoDocumentoClienteOrigen())
            .build();
  }

  @Override
  public Flux<Reporte2CascadaResponse> getReporte2(int repartoTipo, int periodo) {
    return Flux.fromIterable(reportsJpaRepository.obtenerReporte2Cascada(repartoTipo, periodo))
            .map(this::asignarReporte2);
  }

  private Reporte2CascadaResponse asignarReporte2(Reporte2Cascada r) {
    return Reporte2CascadaResponse.builder()
            .centroDestinoNivel(r.getCentroDestinoNivel())
            .centroDestinoNombre(r.getCentroDestinoNombre())
            .centroDestinoTipo(r.getCentroDestinoTipo())
            .centroInicialNivel(r.getCentroInicialNivel())
            .centroInicialNombre(r.getCentroInicialNombre())
            .centroInicialTIpo(r.getCentroInicialTipo())
            .centroOrigenNivel(r.getCentroOrigenNivel())
            .centroOrigenNombre(r.getCentroOrigenNombre())
            .centroOrigenTipo(r.getCentroOrigenTipo())
            .codCentroDestino(r.getCodCentroDestino())
            .codCentroInicial(r.getCodCentroInicial())
            .codCentroOrigen(r.getCodCentroOrigen())
            .codCuentaContableInicial(r.getCodCuentaContableInicial())
            .codDriver(r.getCodDriver())
            .codPartidaInicial(r.getCodPartidaInicial())
            .cuentaContableInicialNombre(r.getCuentaContableInicialNombre())
            .driverNombre(r.getDriverNombre())
            .iteracion(r.getIteracion())
            .monto(r.getMonto())
            .partidaInicialNombre(r.getPartidaInicialNombre())
            .periodo(r.getPeriodo())
            .tipoDocumentoClienteInicial(r.getTipoDocumentoClienteInicial())
            .codDocumentoClienteInicial(r.getCodDocumentoClienteInicial())
            .clienteInicial(r.getClienteInicial())
            .build();
  }

  @Override
  public Flux<Reporte3ObjetosResponse> getReporte3(int repartoTipo, int periodo) {
    return Flux.fromIterable(reportsJpaRepository.obtenerReporte3Objetos(repartoTipo, periodo))
            .map(this::asignarReporte3);
  }

  private Reporte3ObjetosResponse asignarReporte3(Reporte3Objetos r) {
    return Reporte3ObjetosResponse.builder()
            .asignacion(r.getAsignacion())
            .canalNombre(r.getCanalNombre())
            .ccDestinoNiif17Atribuible(r.getCcDestinoNiif17Atribuible())
            .ccInicialNiif17Atribuible(r.getCcInicialNiif17Atribuible())
            .centroDestinoNivel(r.getCentroDestinoNivel())
            .centroDestinoNombre(r.getCentroDestinoNombre())
            .centroDestinoTipo(r.getCentroDestinoTipo())
            .centroInicialGrupo(r.getCentroInicialGrupo())
            .centroInicialNivel(r.getCentroInicialNivel())
            .centroInicialNombre(r.getCentroInicialNombre())
            .centroInicialTipo(r.getCentroInicialTipo())
            .codCanal(r.getCodCanal())
            .codCentroDestino(r.getCodCentroDestino())
            .codCentroInicial(r.getCodCentroInicial())
            .codCuentaContableInicial(r.getCodCuentaContableInicial())
            .codDriver(r.getCodDriver())
            .codLinea(r.getCodLinea())
            .codPartidaInicial(r.getCodPartidaInicial())
            .codProducto(r.getCodProducto())
            .codSubcanal(r.getCodSubcanal())
            .ctaContableNiif17Atribuible(r.getCtaContableNiif17Atribuible())
            .cuentaContableInicialNombre(r.getCuentaContableInicialNombre())
            .driverNombre(r.getDriverNombre())
            .grupoGasto(r.getGrupoGasto())
            .lineaNombre(r.getLineaNombre())
            .monto(r.getMonto())
            .partidaInicialNombre(r.getPartidaInicialNombre())
            .periodo(r.getPeriodo())
            .productoNombre(r.getProductoNombre())
            .resultadoNiif17Atribuible(r.getResultadoNiif17Atribuible())
            .resultadoNiif17Tipo(r.getResultadoNiif17Tipo())
            .subcanalNombre(r.getSubcanalNombre())
            .tipo(r.getTipo())
            .tipoGasto(r.getTipoGasto())
            .tipoNegocio(r.getTipoNegocio())
            .tipoDocumentoClienteInicial(r.getTipoDocumentoClienteInicial())
            .codDocumentoClienteInicial(r.getCodDocumentoClienteInicial())
            .clienteInicial(r.getClienteInicial())
            .asiento(r.getAsiento())
            .tipoReferenciaDocumento(r.getTipoReferenciaDocumento())
            .documentoContabilizado(r.getDocumentoContabilizado())
            .referencia(r.getReferencia())
            .fechaContable(r.getFechaContable())
            .origenNegocio(r.getOrigenNegocio())
            .moneda(r.getMoneda())
            .montoMonedaOrigen(r.getMontoMonedaOrigen())
            .tipoMovimiento(r.getTipoMovimiento())
            .build();
  }

  @Override
  public Resource generateReporte4(int repartoTipo, int periodo) {
    System.out.println("Llega servicio reporte 1");
    SXSSFWorkbook wb = excelService.crearLibro();
    CellStyle cabeceraEstilo = excelService.cabeceraEstilo(wb);
    CellStyle bordeEstilo = excelService.bordeEstilo(wb);
    CellStyle fechaEstilo = excelService.fechaEstilo(wb);
    SXSSFSheet sh = wb.createSheet("Reporte 4");

    List<Map<String, Object>> reporte1 = reportsJpaRepository.obtenerReporte4CascadaCentros(repartoTipo, periodo);
    List<Integer> widths = Arrays.asList(2000, 3800, 12000, 2000, 8000, 3000, 4000, 2000, 3000, 3000, 7000, 3000);

    if (reporte1 == null || reporte1.size() == 0) {
      reporte1 = reportsJpaRepository.obtenerReporte4Vacio();
      excelService.crearCabecera(sh, 0, reporte1, widths, cabeceraEstilo);
      return excelService.crearResource(wb);
    }
    int rowNum = 0;
    excelService.crearCabecera(sh, rowNum++, reporte1, widths, cabeceraEstilo);
    ;
    for (Map<String, Object> fila : reporte1) {
      int colNum = 0;
      Row row = sh.createRow(rowNum++);
      try {
        excelService.createCell(row, colNum++, ((Number) fila.get("Periodo")).intValue(), bordeEstilo);
        excelService.createCell(row, colNum++, ((Number) fila.get("ITERACION")).intValue(), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CodCentroDestino"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CentroDestinoNombre"), bordeEstilo);
        excelService.createCell(row, colNum++, ((Number) fila.get("CentroDestinoNivel")).intValue(), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CentroDestinoTipo"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CodCentroOrigen"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CentroOrigenNombre"), bordeEstilo);
        excelService.createCell(row, colNum++, ((Number) fila.get("CentroOrigenNivel")).intValue(), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CentroOrigenTipo"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("TIPO"), bordeEstilo);
        excelService.createCell(row, colNum++, ((BigDecimal) fila.get("MONTO")).doubleValue(), bordeEstilo);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return excelService.crearResource(wb);
  }

  @Override
  public Flux<Reporte4CascadaCentrosResponse> getReporte4Limited(int repartoTipo, int periodo) {
    System.out.println("Llega servicio reporte 1");
    return Flux.fromIterable(reportsJpaRepository.obtenerReporte4CascadaCentrosLimited(repartoTipo, periodo))
            .map(this::asignarReporte4);
  }

  private Reporte4CascadaCentrosResponse asignarReporte4(Reporte4CascadaCentros r) {
    return Reporte4CascadaCentrosResponse.builder()
            .periodo(r.getPeriodo())
            .iteracion(r.getIteracion())
            .centroDestinoNivel(r.getCentroDestinoNivel())
            .centroDestinoNombre(r.getCentroDestinoNombre())
            .centroDestinoTipo(r.getCentroDestinoTipo())
            .centroOrigenNivel(r.getCentroOrigenNivel())
            .centroOrigenNombre(r.getCentroOrigenNombre())
            .centroOrigenTipo(r.getCentroOrigenTipo())
            .codCentroDestino(r.getCodCentroDestino())
            .codCentroOrigen(r.getCodCentroOrigen())
            .monto(r.getMonto())
            .tipo(r.getTipo())
            .build();
  }

  @Override
  public Resource generateReporte5(int repartoTipo, int periodo, List<String> dimensiones,
                                    List<String> lineas, List<String> canales) {
    boolean valido = reportsJpaRepository.validarRepo5(repartoTipo,periodo,lineas,canales);
    if (!valido)
    {
      return null;
    }
    System.out.println("1");
    SXSSFWorkbook wb = excelService.crearLibro();
    CellStyle cabeceraEstilo = excelService.cabeceraEstilo(wb);
    CellStyle bordeEstilo = excelService.bordeEstilo(wb);
    CellStyle fechaEstilo = excelService.fechaEstilo(wb);
    SXSSFSheet sh = wb.createSheet("Reporte 5");
    System.out.println("2");
    List<Integer> widths = Arrays.asList(
            //2000, 3800,
            //4000, 8000,
            //1000, 3000,
            4000, 10000, 1000, 3000, 4000, 5000);
    try {
      if (dimensiones.contains("CENTRO INICIAL")) {
        List<Integer> old = widths;
        widths = new ArrayList<>(Arrays.asList(4000, 8000));
        widths.addAll(old);
      }
      if (dimensiones.contains("PARTIDA INICIAL")) {
        List<Integer> old = widths;
        widths = new ArrayList<>(Arrays.asList(4000, 8000));
        widths.addAll(old);
      }
      if (dimensiones.contains("CUENTA CONTABLE INICIAL")) {
        List<Integer> old = widths;
        widths = new ArrayList<>(Arrays.asList(4000, 8000));
        widths.addAll(old);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    List<Integer> old = widths;
    widths = new ArrayList<>(Arrays.asList(1000));
    widths.addAll(old);

    System.out.println("3");
    System.out.println("Widths below: ");
    System.out.println(widths.toString());

    List<Map<String, Object>> reporte5 = reportsJpaRepository.obtenerReporte5LineaCanal(repartoTipo, periodo,
            dimensiones, lineas, canales);

    if (reporte5 == null || reporte5.size() == 0) {
      reporte5 = reportsJpaRepository.obtenerReporte5Vacio(dimensiones, lineas, canales);
      excelService.crearCabecera(sh, 0, reporte5, widths, cabeceraEstilo);
      return excelService.crearResource(wb);
    }
    int rowNum = 0;
    excelService.crearCabecera(sh, rowNum++, reporte5, widths, cabeceraEstilo);
    try {
      for (Map<String, Object> fila : reporte5) {
        int colNum = 0;
        Row row = sh.createRow(rowNum++);

        excelService.createCell(row, colNum++, periodo, bordeEstilo);
        if (dimensiones.contains("CUENTA CONTABLE INICIAL")) {
          excelService.createCell(row, colNum++, (String) fila.get("CodCuentaContableInicial"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CuentaContableInicialNombre"), bordeEstilo);
        }
        if (dimensiones.contains("PARTIDA INICIAL")) {
          excelService.createCell(row, colNum++, (String) fila.get("CodPartidaInicial"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("PartidaInicialNombre"), bordeEstilo);
        }
        if (dimensiones.contains("CENTRO INICIAL")) {
          excelService.createCell(row, colNum++, (String) fila.get("CodCentroInicial"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CentroInicialNombre"), bordeEstilo);
        }
        String tipoGasto;
        if ((Boolean) fila.get("TipoGasto")) {
          tipoGasto = "DIRECTO";
        } else {
          tipoGasto = "INDIRECTO";
        }
        excelService.createCell(row, colNum++, tipoGasto, bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CodLinea"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("LineaNombre"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CodCanal"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CanalNombre"), bordeEstilo);
        excelService.createCell(row, colNum++, ((BigDecimal) fila.get("Monto")).doubleValue(), bordeEstilo);

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return excelService.crearResource(wb);
  }

  @Override
  public Flux<Reporte5LineaCanalResponse> getReporte5Limited(int repartoTipo, int periodo, List<String> dimensiones,
                                                             List<String> lineas, List<String> canales) {
    System.out.println("Llega servicio reporte 5");
    boolean valido = reportsJpaRepository.validarRepo5(repartoTipo,periodo,lineas,canales);
    if (!valido)
    {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                  "Combinacion de linea/canal no tiene resultados");
    }
    return Flux.fromIterable(reportsJpaRepository.obtenerReporte5LineaCanalLimited(repartoTipo, periodo, dimensiones,
             lineas,  canales))
            .map(this::asignarReporte5);
  }

  private Reporte5LineaCanalResponse asignarReporte5(Reporte5LineaCanal r) {
    return Reporte5LineaCanalResponse.builder()
          .canalNombre(r.getCanalNombre())
          .centroInicialNombre(r.getCentroInicialNombre())
          .codCanal(r.getCodCanal())
          .codCentroInicial(r.getCodCentroInicial())
          .codCuentaContableInicial(r.getCodCuentaContableInicial())
          .codLinea(r.getCodLinea())
          .codPartidaInicial(r.getCodPartidaInicial())
          .cuentaContableInicialNombre(r.getCuentaContableInicialNombre())
          .lineaNombre(r.getLineaNombre())
          .monto(r.getMonto())
          .partidaInicialNombre(r.getPartidaInicialNombre())
          .tipoGasto(r.getTipoGasto())
          .build();
  }

  /**
   * This method is used to get only one reports.
   *
   * @param repartoTipo This indicates the type.
   * @param periodo This indicates the period.
   * @return Report 6 as Resource.
   */
  @Override
  public Resource generateReporte6(int repartoTipo, int periodo) {
    SXSSFWorkbook wb = excelService.crearLibro();
    CellStyle cabeceraEstilo = excelService.cabeceraEstilo(wb);
    CellStyle bordeEstilo = excelService.bordeEstilo(wb);
    CellStyle fechaEstilo = excelService.fechaEstilo(wb);
    CellStyle percentStyle = wb.createCellStyle();
    CellStyle headerCellStyle = cabeceraEstilo(wb);
    percentStyle.setDataFormat(wb.createDataFormat().getFormat("0.000%"));

    SXSSFSheet sh1 = wb.createSheet("Control 1");
    SXSSFSheet sh2 = wb.createSheet("Control 2");

    List<Map<String, Object>> reporte6A = reportsJpaRepository.obtenerReporte6A(repartoTipo, periodo);
    List<Map<String, Object>> reporte6B = reportsJpaRepository.obtenerReporte6B(repartoTipo, periodo);

    List<Integer> widths1 = Arrays.asList(5000, 4000, 11000, 3000, 4000, 4000, 4000, 4000);
    List<Integer> widths2 = Arrays.asList(5000, 4000, 11000, 3000, 4000, 4000, 3500, 3500, 4000, 4000);

    // 6A
    if (reporte6A == null || reporte6A.size() == 0) {
      reporte6A = reportsJpaRepository.obtenerReporte6AVacio();
      excelService.crearCabecera(sh1, 0, reporte6A, widths1, cabeceraEstilo);
    } else {
      int rowNum = 0;

      Row row;
      row = sh1.createRow(rowNum++);
      row.createCell(1).setCellValue("MONTO TOTAL");
      row.getCell(1).setCellStyle(headerCellStyle);
      row = sh1.createRow(rowNum++);
      row.createCell(0).setCellValue("TABLA INPUT");
      row.getCell(0).setCellStyle(headerCellStyle);
      row.createCell(3).setCellValue("DIFERENCIA");
      row.getCell(3).setCellStyle(headerCellStyle);
      row = sh1.createRow(rowNum++);
      row.createCell(0).setCellValue("TABLA BOLSAS");
      row.getCell(0).setCellStyle(headerCellStyle);
      row.createCell(3).setCellValue("VARIACION");
      row.getCell(3).setCellStyle(headerCellStyle);
      row = sh1.createRow(rowNum++);
      row.createCell(0).setCellValue("TABLA CASCADA FINAL");
      row.getCell(0).setCellStyle(headerCellStyle);
      row = sh1.createRow(rowNum++);
      row.createCell(0).setCellValue("TABLA OBJETOS");
      row.getCell(0).setCellStyle(headerCellStyle);
      sh1.createRow(rowNum++);

      excelService.crearCabecera(sh1, rowNum++, reporte6A, widths1, cabeceraEstilo);
      for (Map<String, Object> fila : reporte6A) {
        int colNum = 0;
        row = sh1.createRow(rowNum++);
        try {
          excelService.createCell(row, colNum++, periodo, bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CodCentro"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CentroNombre"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CentroTipo"), bordeEstilo);
          excelService.createCell(row, colNum++, ((BigDecimal) fila.get("MontoTablaInput")).doubleValue(), bordeEstilo);
          excelService.createCell(row, colNum++, ((BigDecimal) fila.get("MontoTablaBolsas")).doubleValue(), bordeEstilo);
          excelService.createCell(row, colNum++, ((BigDecimal) fila.get("MontoTablaCascadaF")).doubleValue(), bordeEstilo);
          excelService.createCell(row, colNum++, ((BigDecimal) fila.get("MontoTablaObjetos")).doubleValue(), bordeEstilo);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      row = sh1.getRow(1);
      row.createCell(1).setCellFormula(String.format("SUM(E8:E%d)", rowNum));
      row.createCell(4).setCellFormula("B5-B2");

      row = sh1.getRow(2);
      row.createCell(1).setCellFormula(String.format("SUM(F8:F%d)", rowNum));
      row.createCell(4).setCellFormula("E2/B2");
      row.getCell(4).setCellStyle(percentStyle);

      row = sh1.getRow(3);
      row.createCell(1).setCellFormula(String.format("SUM(G8:G%d)", rowNum));

      row = sh1.getRow(4);
      row.createCell(1).setCellFormula(String.format("SUM(H8:H%d)", rowNum));
    }
    // 6B
    if (reporte6B == null || reporte6B.size() == 0) {
      reporte6B = reportsJpaRepository.obtenerReporte6BVacio();
      excelService.crearCabecera(sh2, 0, reporte6B, widths2, cabeceraEstilo);
    } else {
      int rowNum = 0;

      excelService.crearCabecera(sh2, rowNum++, reporte6B, new ArrayList(Arrays.asList("VARIACIÓN MONTO")), widths2, cabeceraEstilo);
      for (Map<String, Object> fila : reporte6B) {
        int colNum = 0;
        Row row = sh2.createRow(rowNum++);
        try {
          excelService.createCell(row, colNum++, periodo, bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CodCuentaContable"), bordeEstilo); //A
          excelService.createCell(row, colNum++, (String) fila.get("CuentaContableNombre"), bordeEstilo); // B

          excelService.createCell(row, colNum++, (String) fila.get("CodPartida"), bordeEstilo); // C
          excelService.createCell(row, colNum++, (String) fila.get("PartidaNombre"), bordeEstilo); // D

          excelService.createCell(row, colNum++, ((BigDecimal) fila.get("MontoTablaInput")).doubleValue(), bordeEstilo); // E
          excelService.createCell(row, colNum++, ((BigDecimal) fila.get("MontoTablaObjetos")).doubleValue(), bordeEstilo); // F

          excelService.createCell(row, colNum++, ((Number) fila.get("CntCentrosTablaInput")).intValue(), bordeEstilo); // G
          excelService.createCell(row, colNum++, ((Number) fila.get("CntCentrosTablaObjetos")).intValue(), bordeEstilo); // H
          row.createCell(colNum).setCellFormula(String.format("(G%d-F%d)/F%d", rowNum, rowNum, rowNum));
          row.getCell(colNum).setCellStyle(percentStyle);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return excelService.crearResource(wb);
  }

  @Override
  public Flux<Reporte6Control1Response> getReporte6Limited(int repartoTipo, int periodo) {
    System.out.println("Llega servicio reporte 6");
    return Flux.fromIterable(reportsJpaRepository.obtenerReporte6ALimited(repartoTipo, periodo))
            .map(this::asignarReporte6);
  }

  private Reporte6Control1Response asignarReporte6(Reporte6Control1 r) {
    return Reporte6Control1Response.builder()
            .centroNombre(r.getCentroNombre())
            .centroTipo(r.getCentroTipo())
            .codCentro(r.getCodCentro())
            .montoTablaBolsas(r.getMontoTablaBolsas())
            .montoTablaCascadaF(r.getMontoTablaCascadaF())
            .montoTablaInput(r.getMontoTablaInput())
            .montoTablaObjetos(r.getMontoTablaObjetos())
            .build();
  }

  /**
   * This method is used to get only one reports.
   *
   * @param repartoTipo This indicates the type.
   * @param periodo This indicates the period.
   * @return Report 7 as Resource.
   */
  public Resource generateReporte7(int repartoTipo, int periodo) {
    SXSSFWorkbook wb = excelService.crearLibro();
    CellStyle cabeceraEstilo = excelService.cabeceraEstilo(wb);
    CellStyle bordeEstilo = excelService.bordeEstilo(wb);
    CellStyle fechaEstilo = excelService.fechaEstilo(wb);

    SXSSFSheet sh1 = wb.createSheet("Agile 1");
    SXSSFSheet sh2 = wb.createSheet("Agile 2");

    //reporte7Atemp se crea para respetar el checkstyle
    List<Map<String, Object>> reporte7Atemp = reportsJpaRepository.obtenerReporte7A(repartoTipo, periodo);

    List<Map<String, Object>> reporte7B = reportsJpaRepository.obtenerReporte7B(repartoTipo, periodo);


    List<Integer> widths1 = Arrays.asList(2349, 2767, 6721, 18372, 10163, 6163, 5907, 6488,
            18372, 10163, 6163, 5907, 6488, 18372, 2512, 3186, 3023);
    List<Integer> widths2 = Arrays.asList(1000, 3000, 3000, 3000, 3000, 3000,
            3000, 3000, 3000, 3000, 3000);
    List<Map<String, Object>> reporte7A = reporte7Atemp;
    List<Integer> exclusion = Arrays.asList(14,15);
    // 7A
    if (reporte7A == null || reporte7A.size() == 0) {
      reporte7A = reportsJpaRepository.obtenerReporte7AVacio();
      excelService.crearCabecera(sh1, 0, reporte7A, widths1, cabeceraEstilo, exclusion);
    } else {
      int rowNum = 0;
      excelService.crearCabecera(sh1, rowNum++, reporte7A, widths1, cabeceraEstilo, exclusion);
      ;
      for (Map<String, Object> fila : reporte7A) {
        int colNum = 0;
        Row row = sh1.createRow(rowNum++);
        try {
          excelService.createCell(row, colNum++, periodo, bordeEstilo);
          excelService.createCell(row, colNum++, ((Number) fila.get("Iteracion")).intValue(), bordeEstilo);

          excelService.createCell(row, colNum++, (String) fila.get("CodCentroDestino"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CentroDestinoNombre"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CentroDestinoGrupo"), bordeEstilo);
          excelService.createCell(row, colNum++, ((Number) fila.get("CentroDestinoNivel")).intValue(), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CentroDestinoTipo"), bordeEstilo);

          excelService.createCell(row, colNum++, (String) fila.get("CodCentroOrigen"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CentroOrigenNombre"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CentroOrigenGrupo"), bordeEstilo);
          excelService.createCell(row, colNum++, ((Number) fila.get("CentroOrigenNivel")).intValue(), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CentroOrigenTipo"), bordeEstilo);

          excelService.createCell(row, colNum++, (String) fila.get("CodCentroPadreOrigen"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CentroPadreOrigenNombre"), bordeEstilo);
          //excelService.createCell(row, colNum++, (String) fila.get("CodCentroPadreDestino"), bordeEstilo);
          //excelService.createCell(row, colNum++, (String) fila.get("CentroPadreDestinoNombre"), bordeEstilo);

          excelService.createCell(row, colNum++, (String) fila.get("Tipo"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("TipoGasto"), bordeEstilo);

          excelService.createCell(row, colNum++, ((BigDecimal) fila.get("Monto")).doubleValue(), bordeEstilo);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    // 7B
    if (reporte7B == null || reporte7B.size() == 0) {
      reporte7B = reportsJpaRepository.obtenerReporte7BVacio();
      excelService.crearCabecera(sh2, 0, reporte7B, widths2, cabeceraEstilo);
    } else {
      int rowNum = 0;
      excelService.crearCabecera(sh2, rowNum++, reporte7B, widths2, cabeceraEstilo);
      ;
      for (Map<String, Object> fila : reporte7B) {
        int colNum = 0;
        Row row = sh2.createRow(rowNum++);
        try {
          excelService.createCell(row, colNum++, periodo, bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CodCentroInicial"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CentroInicialNombre"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CodCentroPadreInicial"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("NombreCentroPadreInicial"), bordeEstilo);
          excelService.createCell(row, colNum++, ((Boolean) fila.get("TipoGasto")).toString(), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CodLinea"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("LineaNombre"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CodCanal"), bordeEstilo);
          excelService.createCell(row, colNum++, (String) fila.get("CanalNombre"), bordeEstilo);
          excelService.createCell(row, colNum++, ((BigDecimal) fila.get("Monto")).doubleValue(), bordeEstilo);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    return excelService.crearResource(wb);
  }

  @Override
  public Flux<Reporte7Agile1CentrosResponse> getReporte7Limited(int repartoTipo, int periodo) {
    System.out.println("Llega servicio reporte 7");
    return Flux.fromIterable(reportsJpaRepository.obtenerReporte7ALimited(repartoTipo, periodo))
            .map(this::asignarReporte7);
  }

  private Reporte7Agile1CentrosResponse asignarReporte7(Reporte7Agile1Centros r) {
    return Reporte7Agile1CentrosResponse.builder()
            .centroDestinoGrupo(r.getCentroDestinoGrupo())
            .centroDestinoNivel(r.getCentroDestinoNivel())
            .centroDestinoNombre(r.getCentroDestinoNombre())
            .centroDestinoTipo(r.getCentroDestinoTipo())
            .centroOrigenNivel(r.getCentroOrigenNivel())
            .centroOrigenGrupo(r.getCentroOrigenGrupo())
            .centroOrigenNombre(r.getCentroOrigenNombre())
            .centroOrigenTipo(r.getCentroOrigenTipo())
            .codCentroDestino(r.getCodCentroDestino())
            .codCentroOrigen(r.getCodCentroOrigen())
            .codCentroPadreOrigen(r.getCodCentroPadreOrigen())
            .codCentroPadreDestino(r.getCodCentroPadreDestino())
            .centroPadreDestinoNombre(r.getCentroPadreDestinoNombre())
            .centroPadreOrigenNombre(r.getCentroPadreOrigenNombre())
            .iteracion(r.getIteracion())
            .monto(r.getMonto())
            .tipo(r.getTipo())
            .tipoGasto(r.getTipoGasto())
            .build();
  }

  /**
   * This method is used to get only one reports.
   *
   * @param repartoTipo This indicates the type.
   * @param periodo This indicates the period.
   * @return Report 8 as Resource.
   */
  @Override
  public Resource generateReporte8(int repartoTipo, int periodo) {
    SXSSFWorkbook wb = excelService.crearLibro();
    CellStyle cabeceraEstilo = excelService.cabeceraEstilo(wb);
    CellStyle bordeEstilo = excelService.bordeEstilo(wb);
    SXSSFSheet sh = wb.createSheet("Reporte 8");

    List<Map<String, Object>> reporte8 = reportsJpaRepository.obtenerReporte8(repartoTipo, periodo);
    List<Integer> widths = Arrays.asList(1000, 4000, 4000, 4000, 4000, 4000,
            4000, 4000, 4000, 4000, 4000,
            4000, 4000, 4000, 4000, 4000);

    if (reporte8 == null || reporte8.size() == 0) {
      reporte8 = reportsJpaRepository.obtenerReporte8Vacio();
      excelService.crearCabecera(sh, 0, reporte8, widths, cabeceraEstilo);
      return excelService.crearResource(wb);
    }
    int rowNum = 0;
    excelService.crearCabecera(sh, rowNum++, reporte8, widths, cabeceraEstilo);
    ;
    for (Map<String, Object> fila : reporte8) {
      int colNum = 0;
      Row row = sh.createRow(rowNum++);
      try {
        excelService.createCell(row, colNum++, periodo, bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CodCuentaContableInicial"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CuentaContableInicialNombre"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CodLinea"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("LineaNombre"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CodProducto"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("ProductoNombre"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CodCanal"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CanalNombre"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("CodSubcanal"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("SubcanalNombre"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("ResultadoNiif17Atribuible"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("ResultadoNiif17Tipo"), bordeEstilo);
        excelService.createCell(row, colNum++, (String) fila.get("Niif17Clase"), bordeEstilo);
        excelService.createCell(row, colNum++, ((BigDecimal) fila.get("Monto")).doubleValue(), bordeEstilo);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return excelService.crearResource(wb);
  }

  @Override
  public Flux<Reporte8NiifResponse> getReporte8Limited(int repartoTipo, int periodo) {
    System.out.println("Llega servicio reporte 8");
    return Flux.fromIterable(reportsJpaRepository.obtenerReporte8Limited(repartoTipo, periodo))
            .map(this::asignarReporte8);
  }

  private Reporte8NiifResponse asignarReporte8(Reporte8Niff r) {
    return Reporte8NiifResponse.builder()
            .canalNombre(r.getCanalNombre())
            .codCanal(r.getCodCanal())
            .codCuentaContableInicial(r.getCodCuentaContableInicial())
            .codLinea(r.getCodLinea())
            .codProducto(r.getCodProducto())
            .codSubcanal(r.getCodSubcanal())
            .cuentaContableInicialNombre(r.getCuentaContableInicialNombre())
            .lineaNombre(r.getLineaNombre())
            .monto(r.getMonto())
            .niif17Clase(r.getNiif17Clase())
            .productoNombre(r.getProductoNombre())
            .resultadoNiif17Atribuible(r.getResultadoNiif17Atribuible())
            .resultadoNiif17Tipo(r.getResultadoNiif17Tipo())
            .subcanalNombre(r.getSubcanalNombre())
            .build();
  }

  @Override
  public Flux<FiltroLineaResponse> getFiltroCodLinea(int repartoTipo, int periodo) {
    return Flux.fromIterable(reportsJpaRepository.obtenerFiltroCodLineas(repartoTipo, periodo)).map(this::asignarFiltro);
  }

  private FiltroLineaResponse asignarFiltro(FiltroLinea f) {
    return FiltroLineaResponse.builder().codLinea(f.getCodLinea()).build();
  }

  private CellStyle cabeceraEstilo(SXSSFWorkbook wb) {
    // Create a Font for styling header cells
    Font headerFont = wb.createFont();
    headerFont.setBold(true);
    headerFont.setFontHeightInPoints((short) 12);
    headerFont.setColor(IndexedColors.WHITE.getIndex());

    // Create a CellStyle with the font
    CellStyle headerCellStyle = wb.createCellStyle();
    headerCellStyle.setFont(headerFont);
    headerCellStyle.setFillBackgroundColor(IndexedColors.DARK_BLUE.getIndex());
    headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return headerCellStyle;
  }

}
