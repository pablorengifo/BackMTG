package pe.com.pacifico.kuntur.business.impl;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import pe.com.pacifico.kuntur.business.ExcelService;
import pe.com.pacifico.kuntur.business.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.response.*;
import pe.com.pacifico.kuntur.model.Reporte1BolsasOficinas;
import pe.com.pacifico.kuntur.model.Reporte2Cascada;
import pe.com.pacifico.kuntur.model.Reporte3Objetos;
import pe.com.pacifico.kuntur.model.Reports;
import pe.com.pacifico.kuntur.repository.ReportsJpaRepository;
import pe.com.pacifico.kuntur.webclient.impl.ReportsWebClientImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <b>Class</b>: ReportsServiceImplTest <br/>
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
@ExtendWith(MockitoExtension.class)
public class ReportsServiceImplTest {

  // given
  private int periodo = 202005;
  private int repartoTipo = 1;

  String json = "{ \"dimensiones\": [], \"canales\": [], \"lineas\": []}";
  List<String> dimensiones = new ArrayList();
  List<String> canales = new ArrayList();
  List<String> lineas = new ArrayList();

  private Reporte1BolsasOficinas response1_1 = new Reporte1BolsasOficinas();
  private Reporte1BolsasOficinas response1_2 = new Reporte1BolsasOficinas();
  private Reporte1BolsasOficinas response1_3 = new Reporte1BolsasOficinas();

  private Reporte2Cascada response2_1 = new Reporte2Cascada();
  private Reporte2Cascada response2_2 = new Reporte2Cascada();
  private Reporte2Cascada response2_3 = new Reporte2Cascada();

  private Reporte3Objetos response3_1 = new Reporte3Objetos();
  private Reporte3Objetos response3_2 = new Reporte3Objetos();
  private Reporte3Objetos response3_3 = new Reporte3Objetos();

  private SXSSFWorkbook mockWorkbook = new SXSSFWorkbook();
  private ByteArrayResource mockBAR = new ByteArrayResource(new ByteArrayOutputStream().toByteArray());

  @Mock
  private ReportsWebClientImpl reportsWebClient;

  @InjectMocks
  private ReportsServiceImpl reportsService;

  @Mock
  private ExcelService excelService = mock(ExcelService.class);
  @Mock
  private ReportsJpaRepository jpaRepository;


  @InjectMocks
  private ReportsServiceImpl service;

  @Test
  void shouldReturnGetErrorReports() {
    Flux<Reports> items = reportsService.getReports();
    StepVerifier.create(items)
        .expectErrorMatches(throwable -> throwable instanceof BusinessException)
        .verify();
  }

  @Test
  void shouldReturnGetOkReports() {
    when(reportsWebClient.getReportsResponse(eq(1L)))
        .thenReturn(Mono.just(ReportsResponse.builder().idController(1L).build()));

    Mono<Reports> item = reportsService.getReports(1L);
    StepVerifier.create(item).expectComplete();
  }

  @Test
  public void shouldGetReporte1Vacio(){
    //When
    when(excelService.crearLibro()).thenReturn(mockWorkbook);
    when(excelService.crearResource(mockWorkbook)).thenReturn(mockBAR);
    when(jpaRepository.obtenerReporte1BolsasOficinas(repartoTipo, periodo))
            .thenReturn(new ArrayList());

    //Then
    Resource r= service.generateReporte1(repartoTipo, periodo);
    assertNotNull(r);
  }

  @Test
  public void shouldGetReporte1(){
    List<Map<String, Object>> data = new ArrayList();
    Map<String, Object> dataMap = new HashMap<>();
    // Fill Dummy Data:
    dataMap.put("Periodo",0);
    dataMap.put("CodCuentaContableOrigen","");
    dataMap.put("CuentaContableOrigenNombre","");
    dataMap.put("CodPartidaOrigen","");
    dataMap.put("PartidaOrigenNombre","");
    dataMap.put("CentroOrigenNombre","");
    dataMap.put("CentroOrigenNivel",0);
    dataMap.put("CentroOrigenTipo","");
    dataMap.put("CodCentroDestino","");
    dataMap.put("CentroDestinoNombre","");
    dataMap.put("CentroDestinoNivel",0);
    dataMap.put("CentroDestinoTipo","");
    dataMap.put("Monto", new BigDecimal("0.0"));
    dataMap.put("CodDriver","");
    dataMap.put("DriverNombre","");
    dataMap.put("Asignacion","");

    data.add(dataMap);

    //When
    when(excelService.crearLibro()).thenReturn(mockWorkbook);
    when(excelService.crearResource(mockWorkbook)).thenReturn(mockBAR);
    when(jpaRepository.obtenerReporte1BolsasOficinas(repartoTipo, periodo))
      .thenReturn(data);

    //Then
    Resource r= service.generateReporte1(repartoTipo, periodo);
    assertNotNull(r);
  }

  @Test
  public void shouldGetReporte1Exception(){
    List<Map<String, Object>> data = new ArrayList();
    Map<String, Object> dataMap = new HashMap<>();
    // Fill Dummy Data:
    dataMap.put("Periodo",0);

    data.add(dataMap);

    //When
    when(excelService.crearLibro()).thenReturn(mockWorkbook);
    when(excelService.crearResource(mockWorkbook)).thenReturn(mockBAR);
    when(jpaRepository.obtenerReporte1BolsasOficinas(repartoTipo, periodo))
      .thenReturn(data);

    //Then
    Resource r= service.generateReporte1(repartoTipo, periodo);
    assertNotNull(r);
  }

  @Test
  public void shouldGetReporte1Limited(){
    //When
    when(jpaRepository.obtenerReporte1BolsasOficinasLimited(repartoTipo, periodo))
            .thenReturn(Arrays.asList(response1_1,response1_2,response1_3));

    //Then
    Flux<Reporte1BolsasOficinasResponse> flux= service.getReporte1Limited(repartoTipo, periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetReporte2(){
    //When
    when(jpaRepository.obtenerReporte2Cascada(repartoTipo, periodo))
            .thenReturn(Arrays.asList(response2_1,response2_2,response2_3));

    //Then
    Flux<Reporte2CascadaResponse> flux= service.getReporte2(repartoTipo, periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetReporte3(){
    //When
    when(jpaRepository.obtenerReporte3Objetos(repartoTipo, periodo))
            .thenReturn(Arrays.asList(response3_1,response3_2,response3_3));

    //Then
    Flux<Reporte3ObjetosResponse> flux= service.getReporte3(repartoTipo, periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetReporte4Vacio(){
    //When
    when(excelService.crearLibro()).thenReturn(mockWorkbook);
    when(excelService.crearResource(mockWorkbook)).thenReturn(mockBAR);
    when(jpaRepository.obtenerReporte4CascadaCentros(repartoTipo, periodo))
            .thenReturn(new ArrayList());

    //Then
    Resource r= service.generateReporte4(repartoTipo, periodo);
    assertNotNull(r);
  }

  @Test
  public void shouldGetReporte4(){
    List<Map<String, Object>> data = new ArrayList();
    Map<String, Object> dataMap = new HashMap<>();
    // Fill Dummy Data:
    dataMap.put("Periodo",0);
    dataMap.put("ITERACION",0);
    dataMap.put("CodCentroDestino","");
    dataMap.put("CentroDestinoNombre","");
    dataMap.put("CentroDestinoNivel",0);
    dataMap.put("CentroDestinoTipo","");
    dataMap.put("CodCentroOrigen","");
    dataMap.put("CentroOrigenNombre","");
    dataMap.put("CentroOrigenNivel",0);
    dataMap.put("CentroOrigenTipo","");
    dataMap.put("TIPO","");
    dataMap.put("MONTO", new BigDecimal("0.0"));

    data.add(dataMap);
    //When
    when(excelService.crearLibro()).thenReturn(mockWorkbook);
    when(excelService.crearResource(mockWorkbook)).thenReturn(mockBAR);
    when(jpaRepository.obtenerReporte4CascadaCentros(repartoTipo, periodo))
      .thenReturn(data);

    //Then
    Resource r= service.generateReporte4(repartoTipo, periodo);
    assertNotNull(r);
  }

  @Test
  public void shouldPostReporte5Vacio(){
    //When
    when(excelService.crearLibro()).thenReturn(mockWorkbook);
    when(excelService.crearResource(mockWorkbook)).thenReturn(mockBAR);
    when(jpaRepository.obtenerReporte5LineaCanal(repartoTipo, periodo, dimensiones, lineas, canales))
            .thenReturn(new ArrayList());
    when(jpaRepository.validarRepo5(repartoTipo,periodo,lineas,canales)).thenReturn(true);

    //Then
    Resource r= service.generateReporte5(repartoTipo, periodo, dimensiones, lineas, canales);
    assertNotNull(r);
  }

  @Test
  public void shouldPostReporte5(){
    dimensiones.add("CUENTA CONTABLE INICIAL");
    dimensiones.add("PARTIDA INICIAL");
    dimensiones.add("CENTRO INICIAL");

    List<Map<String, Object>> data = new ArrayList();
    Map<String, Object> dataMap = new HashMap<>();
    // Fill Dummy Data:
    dataMap.put("CodCuentaContableInicial","");
    dataMap.put("CuentaContableInicialNombre","");
    dataMap.put("CodPartidaInicial","");
    dataMap.put("PartidaInicialNombre","");
    dataMap.put("CodCentroInicial","");
    dataMap.put("CentroInicialNombre","");
    dataMap.put("TipoGasto", false);
    dataMap.put("CodLinea","");
    dataMap.put("LineaNombre","");
    dataMap.put("CodCanal","");
    dataMap.put("CanalNombre","");
    dataMap.put("Monto", new BigDecimal("0.0"));
    data.add(dataMap);

    //When
    when(excelService.crearLibro()).thenReturn(mockWorkbook);
    when(excelService.crearResource(mockWorkbook)).thenReturn(mockBAR);
    when(jpaRepository.obtenerReporte5LineaCanal(repartoTipo, periodo, dimensiones, lineas, canales))
      .thenReturn(data);
    when(jpaRepository.validarRepo5(repartoTipo,periodo,lineas,canales)).thenReturn(true);

    //Then
    Resource r= service.generateReporte5(repartoTipo, periodo, dimensiones, lineas, canales);
    assertNotNull(r);
  }

  @Test
  public void shouldGetReporte6(){
    // Fill dummy data
    //  A
    List<Map<String, Object>> dataA = new ArrayList();
    Map<String, Object> dataMapA = new HashMap<>();
    dataMapA.put("CodCentro","");
    dataMapA.put("CentroNombre","");
    dataMapA.put("CentroTipo","");
    dataMapA.put("MontoTablaInput", new BigDecimal("0.0"));
    dataMapA.put("MontoTablaBolsas", new BigDecimal("0.0"));
    dataMapA.put("MontoTablaCascadaF", new BigDecimal("0.0"));
    dataMapA.put("MontoTablaObjetos", new BigDecimal("0.0"));
    dataA.add(dataMapA);
    //  B
    List<Map<String, Object>> dataB = new ArrayList();
    Map<String, Object> dataMapB = new HashMap<>();
    dataMapB.put("CodCuentaContable","");
    dataMapB.put("CuentaContableNombre","");
    dataMapB.put("CodPartida","");
    dataMapB.put("PartidaNombre","");
    dataMapB.put("MontoTablaInput", new BigDecimal("0.0"));
    dataMapB.put("MontoTablaObjetos", new BigDecimal("0.0"));
    dataMapB.put("CntCentrosTablaInput",0);
    dataMapB.put("CntCentrosTablaObjetos",0);
    dataB.add(dataMapB);
    //When
    when(excelService.crearLibro()).thenReturn(mockWorkbook);
    when(excelService.crearResource(mockWorkbook)).thenReturn(mockBAR);
    when(jpaRepository.obtenerReporte6A(repartoTipo, periodo))
            .thenReturn(dataA);
    when(jpaRepository.obtenerReporte6B(repartoTipo, periodo))
            .thenReturn(dataB);

    //Then
    Resource r= service.generateReporte6(repartoTipo, periodo);
    assertNotNull(r);
  }

  @Test
  public void shouldGetReporte7(){
    // Fill dummy data
    //  A
    List<Map<String, Object>> dataA = new ArrayList();
    Map<String, Object> dataMapA = new HashMap<>();
    dataMapA.put("Iteracion",0);
    dataMapA.put("CodCentroDestino","");
    dataMapA.put("CentroDestinoNombre","");
    dataMapA.put("CentroDestinoGrupo","");
    dataMapA.put("CentroDestinoNivel", 0);
    dataMapA.put("CentroDestinoTipo","");
    dataMapA.put("CodCentroOrigen","");
    dataMapA.put("CentroOrigenNombre","");
    dataMapA.put("CentroOrigenGrupo","");
    dataMapA.put("CentroOrigenNivel", 0);
    dataMapA.put("CentroOrigenTipo","");
    dataMapA.put("CodCentroPadreOrigen","");
    dataMapA.put("CentroPadreOrigenNombre","");
    dataMapA.put("CodCentroPadreDestino","");
    dataMapA.put("CentroPadreDestinoNombre","");
    dataMapA.put("Tipo","");
    dataMapA.put("TipoGasto","");
    dataMapA.put("Monto", new BigDecimal("0.0"));
    dataA.add(dataMapA);
    //  B
    List<Map<String, Object>> dataB = new ArrayList();
    Map<String, Object> dataMapB = new HashMap<>();
    dataMapB.put("CodCentroInicial","");
    dataMapB.put("CentroInicialNombre","");
    dataMapB.put("CodCentroPadreInicial","");
    dataMapB.put("NombreCentroPadreInicial","");
    dataMapB.put("TipoGasto", false);
    dataMapB.put("CodLinea","");
    dataMapB.put("LineaNombre","");
    dataMapB.put("CodCanal","");
    dataMapB.put("CanalNombre","");
    dataMapB.put("Monto", new BigDecimal("0.0"));
    dataB.add(dataMapB);
    //When
    when(excelService.crearLibro()).thenReturn(mockWorkbook);
    when(excelService.crearResource(mockWorkbook)).thenReturn(mockBAR);
    when(jpaRepository.obtenerReporte7A(repartoTipo, periodo))
            .thenReturn(dataA);
    when(jpaRepository.obtenerReporte7B(repartoTipo, periodo))
            .thenReturn(dataB);

    //Then
    Resource r= service.generateReporte7(repartoTipo, periodo);
    assertNotNull(r);
  }

  @Test
  public void shouldGetReporte7Vacio(){
    //When
    when(excelService.crearLibro()).thenReturn(mockWorkbook);
    when(excelService.crearResource(mockWorkbook)).thenReturn(mockBAR);
    when(jpaRepository.obtenerReporte7A(repartoTipo, periodo))
      .thenReturn(new ArrayList());
    when(jpaRepository.obtenerReporte7B(repartoTipo, periodo))
      .thenReturn(new ArrayList());

    //Then
    Resource r= service.generateReporte7(repartoTipo, periodo);
    assertNotNull(r);
  }

  @Test
  public void shouldPostReporte8Vacio(){
    //When
    when(excelService.crearLibro()).thenReturn(mockWorkbook);
    when(excelService.crearResource(mockWorkbook)).thenReturn(mockBAR);
    when(jpaRepository.obtenerReporte8(repartoTipo, periodo))
            .thenReturn(new ArrayList());

    //Then
    Resource r= service.generateReporte8(repartoTipo, periodo);
    assertNotNull(r);
  }

  @Test
  public void shouldPostReporte8(){
    List<Map<String, Object>> data = new ArrayList();
    Map<String, Object> dataMap = new HashMap<>();
    // Fill Dummy Data:
    dataMap.put("CodCuentaContableInicial","");
    dataMap.put("CuentaContableInicialNombre","");
    dataMap.put("CodLinea","");
    dataMap.put("LineaNombre","");
    dataMap.put("CodProducto","");
    dataMap.put("ProductoNombre","");
    dataMap.put("CodCanal","");
    dataMap.put("CanalNombre","");
    dataMap.put("CodSubcanal","");
    dataMap.put("SubcanalNombre","");
    dataMap.put("ResultadoNiif17Atribuible","");
    dataMap.put("ResultadoNiif17Tipo","");
    dataMap.put("Niif17Clase","");
    dataMap.put("Monto", new BigDecimal("0.0"));
    data.add(dataMap);
    //When
    when(excelService.crearLibro()).thenReturn(mockWorkbook);
    when(excelService.crearResource(mockWorkbook)).thenReturn(mockBAR);
    when(jpaRepository.obtenerReporte8(repartoTipo, periodo))
      .thenReturn(data);

    //Then
    Resource r= service.generateReporte8(repartoTipo, periodo);
    assertNotNull(r);
  }
}
