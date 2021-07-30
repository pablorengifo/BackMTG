package pe.com.pacifico.kuntur.business.threads;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import pe.com.pacifico.kuntur.business.ReportsService;
import pe.com.pacifico.kuntur.business.impl.ExcelServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportesHiloTest {

  @Mock
  private ReportesHilo mockReportesHilo = mock(ReportesHilo.class);

  @Mock
  private ReportsService reportsService = mock(ReportsService.class);

  @InjectMocks
  private ReportesHilo injectedReportesHilo;

  private int periodo = 202001;
  private int repartoTipo_R = 1;
  List<String> dimensiones = new ArrayList();
  List<String> canales = new ArrayList();
  List<String> lineas = new ArrayList();

  private SXSSFWorkbook mockWorkbook = new SXSSFWorkbook();
  private Resource r = new ExcelServiceImpl().crearResource(mockWorkbook);

  @Test
  public void shouldBegin(){
    // When
    lenient().doNothing().when(mockReportesHilo).start();
    // Then
    ReportesHilo.begin(0, repartoTipo_R, periodo, reportsService);
  }

  @Test
  public void shouldBegin6(){
    // When
    lenient().doNothing().when(mockReportesHilo).start();
    // Then
    ReportesHilo.begin(0, repartoTipo_R, periodo, reportsService, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }

  @Test
  public void obtainReporte1() {
    // When
    ReportesHilo.lastKey = String.format("1~%d~%d",repartoTipo_R, periodo);
    when(reportsService.generateReporte1(repartoTipo_R,periodo)).thenReturn(r);
    // Then
    injectedReportesHilo.run();
    verify(reportsService, times(1)).generateReporte1(repartoTipo_R, periodo);
  }

  @Test
  public void obtainReporte4() {
    // When
    ReportesHilo.lastKey = String.format("4~%d~%d",repartoTipo_R, periodo);
    when(reportsService.generateReporte4(repartoTipo_R,periodo)).thenReturn(r);
    // Then
    injectedReportesHilo.run();
    verify(reportsService, times(1)).generateReporte4(repartoTipo_R, periodo);
  }

  @Test
  public void obtainReporte5() {
    // When
    ReportesHilo.lastKey = String.format("5~%d~%d",repartoTipo_R, periodo);
    ReportesHilo.dimensiones = dimensiones;
    ReportesHilo.lineas = lineas;
    ReportesHilo.canales = canales;
    when(reportsService.generateReporte5(repartoTipo_R,periodo, dimensiones, lineas, canales)).thenReturn(r);
    // Then
    injectedReportesHilo.run();
    verify(reportsService, times(1)).generateReporte5(repartoTipo_R, periodo, dimensiones, lineas, canales);
  }

  @Test
  public void obtainReporte6() {
    // When
    ReportesHilo.lastKey = String.format("6~%d~%d",repartoTipo_R, periodo);
    when(reportsService.generateReporte6(repartoTipo_R,periodo)).thenReturn(r);
    // Then
    injectedReportesHilo.run();
    verify(reportsService, times(1)).generateReporte6(repartoTipo_R, periodo);
  }

  @Test
  public void obtainReporte7() {
    // When
    ReportesHilo.lastKey = String.format("7~%d~%d",repartoTipo_R, periodo);
    when(reportsService.generateReporte7(repartoTipo_R,periodo)).thenReturn(r);
    // Then
    injectedReportesHilo.run();
    verify(reportsService, times(1)).generateReporte7(repartoTipo_R, periodo);
  }

  @Test
  public void obtainReporte8() {
    // When
    ReportesHilo.lastKey = String.format("8~%d~%d",repartoTipo_R, periodo);
    when(reportsService.generateReporte8(repartoTipo_R,periodo)).thenReturn(r);
    // Then
    injectedReportesHilo.run();
    verify(reportsService, times(1)).generateReporte8(repartoTipo_R, periodo);
  }

}
