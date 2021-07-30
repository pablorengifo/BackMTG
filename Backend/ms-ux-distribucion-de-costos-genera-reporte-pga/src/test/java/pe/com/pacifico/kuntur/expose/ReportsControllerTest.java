package pe.com.pacifico.kuntur.expose;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import pe.com.pacifico.kuntur.business.ReportsService;
import pe.com.pacifico.kuntur.business.threads.ReportesHilo;
import pe.com.pacifico.kuntur.expose.response.FiltroLineaResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte1BolsasOficinasResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte2CascadaResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte3ObjetosResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte5LineaCanalResponse;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * <b>Class</b>: ReportsControllerTest <br/>
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
class ReportsControllerTest {
  @Mock
  private ReportsService reportsService;

  @InjectMocks
  private ReportsController reportsController;

  // given
  private int periodo = 202005;
  private int repartoTipo = 1;

  String json = "{ \"dimensiones\": [], \"canales\": [], \"lineas\": []}";
  List<String> dimensiones = new ArrayList();
  List<String> canales = new ArrayList();
  List<String> lineas = new ArrayList();

  private Reporte1BolsasOficinasResponse response1_1 = new Reporte1BolsasOficinasResponse();
  private Reporte1BolsasOficinasResponse response1_2 = new Reporte1BolsasOficinasResponse();
  private Reporte1BolsasOficinasResponse response1_3 = new Reporte1BolsasOficinasResponse();

  private Reporte2CascadaResponse response2_1 = new Reporte2CascadaResponse();
  private Reporte2CascadaResponse response2_2 = new Reporte2CascadaResponse();
  private Reporte2CascadaResponse response2_3 = new Reporte2CascadaResponse();

  private Reporte3ObjetosResponse response3_1 = new Reporte3ObjetosResponse();
  private Reporte3ObjetosResponse response3_2 = new Reporte3ObjetosResponse();
  private Reporte3ObjetosResponse response3_3 = new Reporte3ObjetosResponse();

  private FiltroLineaResponse fl_1 = new FiltroLineaResponse();
  private FiltroLineaResponse fl_2 = new FiltroLineaResponse();
  private FiltroLineaResponse fl_3 = new FiltroLineaResponse();

  private SXSSFWorkbook mockWorkbook = new SXSSFWorkbook();
  private ByteArrayResource mockBAR = new ByteArrayResource(new ByteArrayOutputStream().toByteArray());

  ResponseEntity<?> responseEntity = ResponseEntity.ok().contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"test\"")
          .body(null);

  @Test
  public void shouldGetReporte1Limited(){
    when(reportsService.getReporte1Limited(repartoTipo, periodo))
            .thenReturn(Flux.just(response1_1, response1_2, response1_3));

    //Then
    Flux<Reporte1BolsasOficinasResponse> flux= reportsController.getReporte1L(repartoTipo, periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetReporte2(){
    when(reportsService.getReporte2(repartoTipo, periodo))
            .thenReturn(Flux.just(response2_1, response2_2, response2_3));

    //Then
    Flux<Reporte2CascadaResponse> flux= reportsController.getReporte2(repartoTipo, periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetReporte3(){
    when(reportsService.getReporte3(repartoTipo, periodo))
            .thenReturn(Flux.just(response3_1, response3_2, response3_3));

    //Then
    Flux<Reporte3ObjetosResponse> flux= reportsController.getReporte3(repartoTipo, periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldObtenerEstadoReporte() {
    try (MockedStatic<ReportesHilo> theMock = Mockito.mockStatic(ReportesHilo.class)) {
      theMock.when(() -> ReportesHilo.hasGenerationFinished(anyInt(), anyInt(), anyInt())).thenReturn(false);
    }
    //Then
    assertFalse(reportsController.obtenerEstadoReporte(0, repartoTipo, periodo));
  }

  @Test
  public void shouldObtenerReporte1(){
    try (MockedStatic<ReportesHilo> theMock = Mockito.mockStatic(ReportesHilo.class)) {
      theMock.when( () -> ReportesHilo.returnData(anyInt(), anyInt(), anyInt()) ).thenReturn(responseEntity);
    }
    //Then
    ResponseEntity<?> rp = reportsController.obtenerReporte(1, repartoTipo, periodo);
    assertNotNull(rp);
  }

  @Test
  public void shouldObtenerReporte4(){
    try (MockedStatic<ReportesHilo> theMock = Mockito.mockStatic(ReportesHilo.class)) {
      theMock.when( () -> ReportesHilo.returnData(anyInt(), anyInt(), anyInt()) ).thenReturn(responseEntity);
    }
    //Then
    ResponseEntity<?> rp = reportsController.obtenerReporte(4, repartoTipo, periodo);
    assertNotNull(rp);
  }

  @Test
  public void shouldObtenerReporte5(){
    try (MockedStatic<ReportesHilo> theMock = Mockito.mockStatic(ReportesHilo.class)) {
      inicializarReporteHilo(5);
      theMock.when( () -> ReportesHilo.returnData(anyInt(), anyInt(), anyInt()) ).thenReturn(responseEntity);

    }
    //Then
    ResponseEntity<?> rp = reportsController.obtenerReporte(5, repartoTipo, periodo);
    assertNotNull(rp);
  }

  @Test
  public void shouldNotObtenerReporte5(){
    try (MockedStatic<ReportesHilo> theMock = Mockito.mockStatic(ReportesHilo.class)) {
      theMock.when( () -> ReportesHilo.returnData(anyInt(), anyInt(), anyInt()) ).thenReturn(responseEntity);
    }
    //Then
    assertThrows(Exception.class,() -> reportsController.obtenerReporte(5, repartoTipo, periodo));
  }

  @Test
  public void shouldObtenerReporte6(){
    try (MockedStatic<ReportesHilo> theMock = Mockito.mockStatic(ReportesHilo.class)) {
      theMock.when( () -> ReportesHilo.returnData(anyInt(), anyInt(), anyInt()) ).thenReturn(responseEntity);
    }
    //Then
    ResponseEntity<?> rp = reportsController.obtenerReporte(6, repartoTipo, periodo);
    assertNotNull(rp);
  }

  @Test
  public void shouldObtenerReporte7(){
    try (MockedStatic<ReportesHilo> theMock = Mockito.mockStatic(ReportesHilo.class)) {
      theMock.when( () -> ReportesHilo.returnData(anyInt(), anyInt(), anyInt()) ).thenReturn(responseEntity);
    }
    //Then
    ResponseEntity<?> rp = reportsController.obtenerReporte(7, repartoTipo, periodo);
    assertNotNull(rp);
  }

  @Test
  public void shouldObtenerReporte8(){
    try (MockedStatic<ReportesHilo> theMock = Mockito.mockStatic(ReportesHilo.class)) {
      theMock.when( () -> ReportesHilo.returnData(anyInt(), anyInt(), anyInt()) ).thenReturn(responseEntity);
    }
    //Then
    ResponseEntity<?> rp = reportsController.obtenerReporte(8, repartoTipo, periodo);
    assertNotNull(rp);
  }

  @Test
  public void shouldGetReporte1(){
    //When
    when(reportsService.getReporte(1, repartoTipo, periodo))
            .thenReturn(true);
    //Then
    assertTrue(reportsController.getReporte1(repartoTipo, periodo));
  }

  @Test
  public void shouldGetReporte4(){
    //When
    when(reportsService.getReporte(4, repartoTipo, periodo))
            .thenReturn(true);
    //Then
    assertTrue(reportsController.getReporte4(repartoTipo, periodo));
  }

  @Test
  public void shouldPostReporte5(){
    when(reportsService.getReporte5(repartoTipo, periodo, dimensiones, lineas, canales))
            .thenReturn(true);

    //Then
    assertTrue(reportsService.getReporte5(repartoTipo, periodo, dimensiones, lineas, canales));
  }

  @Test
  public void shouldGetReporte6(){
    //When
    when(reportsService.getReporte(6, repartoTipo, periodo))
            .thenReturn(true);
    //Then
    assertTrue(reportsController.getReporte6(repartoTipo, periodo));
  }

  @Test
  public void shouldGetReporte7(){
    //When
    when(reportsService.getReporte(7, repartoTipo, periodo))
            .thenReturn(true);
    //Then
    assertTrue(reportsController.getReporte7(repartoTipo, periodo));
  }

  @Test
  public void shouldGetReporte8(){
    //When
    when(reportsService.getReporte(8, repartoTipo, periodo))
            .thenReturn(true);
    //Then
    assertTrue(reportsController.getReporte8(repartoTipo, periodo));
  }

  @Test
  public void shouldGetFiltroCodLinea(){
    when(reportsService.getFiltroCodLinea(repartoTipo, periodo))
            .thenReturn(Flux.just(fl_1, fl_2, fl_3));
    //Then
    Flux<FiltroLineaResponse> flux= reportsController.getFiltroCodLinea(repartoTipo, periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  void inicializarReporteHilo(int reporte)
  {
    ReportesHilo.data.put(reporte + "~" + repartoTipo + "~" + periodo, new AbstractResource() {
      @Override
      public String getDescription() {
        return null;
      }

      @Override
      public InputStream getInputStream() throws IOException {
        return null;
      }
    });
  }

}
