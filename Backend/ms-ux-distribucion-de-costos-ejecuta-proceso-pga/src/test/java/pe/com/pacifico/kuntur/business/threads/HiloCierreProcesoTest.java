package pe.com.pacifico.kuntur.business.threads;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.repository.ProcesoJpaRepoository;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HiloCierreProcesoTest {

  @InjectMocks
  private HiloCierreProceso hiloCierreProceso;

  @Mock
  private ProcesoJpaRepoository procesoJpaRepository = mock(ProcesoJpaRepoository.class);

  private int periodo = 202001;
  private int repartoTipo_R = 1;

  @Test
  public void shouldBegin_Route1(){
    // When
    when(procesoJpaRepository.generarReporteBolsasOficinas(repartoTipo_R, periodo)).thenReturn(-1);
    hiloCierreProceso.begin(procesoJpaRepository, repartoTipo_R, periodo);
    // Then
    verify(procesoJpaRepository, times(1)).generarReporteBolsasOficinas(repartoTipo_R, periodo);
    HiloCierreProceso.limpiarHilo();
  }

  @Test
  public void shouldBegin_Route2(){
    // When
    when(procesoJpaRepository.generarReporteBolsasOficinas(repartoTipo_R, periodo)).thenReturn(1);
    when(procesoJpaRepository.generarReporteCascada(repartoTipo_R, periodo)).thenReturn(-1);
    HiloCierreProceso.procesoJpaRepository = procesoJpaRepository;
    HiloCierreProceso.repartoTipo = repartoTipo_R;
    HiloCierreProceso.periodo = periodo;
    hiloCierreProceso.run();
    // Then
    verify(procesoJpaRepository, times(1)).generarReporteCascada(repartoTipo_R, periodo);
    HiloCierreProceso.limpiarHilo();
  }

  @Test
  public void shouldBegin_Route3(){
    // When
    when(procesoJpaRepository.generarReporteBolsasOficinas(repartoTipo_R, periodo)).thenReturn(1);
    when(procesoJpaRepository.generarReporteCascada(repartoTipo_R, periodo)).thenReturn(1);
    when(procesoJpaRepository.generarReporteObjetos(repartoTipo_R, periodo)).thenReturn(-1);
    HiloCierreProceso.procesoJpaRepository = procesoJpaRepository;
    HiloCierreProceso.repartoTipo = repartoTipo_R;
    HiloCierreProceso.periodo = periodo;
    hiloCierreProceso.run();
    // Then
    verify(procesoJpaRepository, times(1)).generarReporteObjetos(repartoTipo_R, periodo);
    HiloCierreProceso.limpiarHilo();
  }

  @Test
  public void shouldBegin_Route4(){
    // When
    when(procesoJpaRepository.generarReporteBolsasOficinas(repartoTipo_R, periodo)).thenReturn(1);
    when(procesoJpaRepository.generarReporteCascada(repartoTipo_R, periodo)).thenReturn(1);
    when(procesoJpaRepository.generarReporteObjetos(repartoTipo_R, periodo)).thenReturn(1);
    doNothing().when(procesoJpaRepository).borrarDistribucionesFase1();
    doNothing().when(procesoJpaRepository).borrarDistribucionesFase2();
    doNothing().when(procesoJpaRepository).borrarDistribucionesFase3();
    HiloCierreProceso.procesoJpaRepository = procesoJpaRepository;
    HiloCierreProceso.repartoTipo = repartoTipo_R;
    HiloCierreProceso.periodo = periodo;
    hiloCierreProceso.run();
    // Then
    verify(procesoJpaRepository, times(1)).generarReporteObjetos(repartoTipo_R, periodo);
    HiloCierreProceso.limpiarHilo();
  }

}
