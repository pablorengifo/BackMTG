package pe.com.pacifico.kuntur.business.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.ProcesoService;
import pe.com.pacifico.kuntur.expose.ProcesoController;
import pe.com.pacifico.kuntur.model.*;
import pe.com.pacifico.kuntur.repository.ProcesoJpaRepoository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProcesosServiceImplTest {

    //Given
    int repartoTipo=1;
    int periodo = 202104;
    String userEmail = "tester@pps.com.pe";
    String userNames = "Dummy";
    int fase = 3;
    int estado = 1;
    EjecucionProceso ejecucionProceso = new EjecucionProceso(new Date().getTime(), userEmail, userNames);

    @Mock
    private ProcesoJpaRepoository jpaRepository;

    @InjectMocks
    private ProcesoServiceImpl service;

    @Test
    public void shouldEjecutarFase1() {
        //When
        when(service.ejecutarFase1(repartoTipo, periodo, userEmail, userNames))
                .thenReturn(100);
        //Then
        int response = service.ejecutarFase1(repartoTipo, periodo, userEmail, userNames);
        assertEquals(100, response);
    }

    @Test
    public void shouldValidarFase1_TodoOK(){
        //When
        when(jpaRepository.verificarFasesEnEjecucion(repartoTipo, periodo))
                .thenReturn(0);
        when(jpaRepository.verificarDetalleGasto(repartoTipo, periodo))
                .thenReturn(true);
        //Then
        assertNull(service.validarFase1(repartoTipo, periodo));
    }

    @Test
    public void shouldValidarFase1_Error1(){
        //When
        when(jpaRepository.verificarFasesEnEjecucion(repartoTipo, periodo))
                .thenReturn(1);
        //Then
        assertEquals(service.validarFase1(repartoTipo, periodo).size(), 1);
    }

    @Test
    public void shouldValidarFase1_Error2(){
        //When
        when(jpaRepository.verificarFasesEnEjecucion(repartoTipo, periodo))
                .thenReturn(0);
        when(jpaRepository.verificarDetalleGasto(repartoTipo, periodo))
                .thenReturn(false);
        //Then
        assertEquals(service.validarFase1(repartoTipo, periodo).size(), 1);
    }

    @Test
    public void shouldValidarFase1_Error3(){
        //When
        when(jpaRepository.verificarFasesEnEjecucion(repartoTipo, periodo))
                .thenReturn(0);
        when(jpaRepository.verificarDetalleGasto(repartoTipo, periodo))
                .thenReturn(true);
        when(jpaRepository.obtenerDriverFase1ConError(repartoTipo,periodo))
                .thenReturn(null);
        //Then
        assertEquals(service.validarFase1(repartoTipo, periodo).size(), 1);
    }

    @Test
    public void shouldValidarFase1_Error4(){
        List<String> lstSinDriver = new ArrayList(Arrays.asList("1","2","3"));
        //When
        when(jpaRepository.verificarFasesEnEjecucion(repartoTipo, periodo))
                .thenReturn(0);
        when(jpaRepository.verificarDetalleGasto(repartoTipo, periodo))
                .thenReturn(true);
        when(jpaRepository.obtenerDriverFase1ConError(repartoTipo,periodo))
                .thenReturn(lstSinDriver);
        //Then
        assertEquals(4, service.validarFase1(repartoTipo, periodo).size());
    }

    @Test
    public void shouldEjecutarFase2(){
        //When
        when(service.ejecutarFase2(repartoTipo, periodo, userEmail, userNames))
                .thenReturn(200);
        //Then
        int response = service.ejecutarFase2(repartoTipo, periodo, userEmail, userNames);
        assertEquals(200, response);
    }

    @Test
    public void validarFase2(){
        //When
        when(jpaRepository.verificarFasesEnEjecucion(repartoTipo, periodo)).thenReturn(0);
        when(jpaRepository.obtenerDriverFase2ConError(repartoTipo, periodo)).thenReturn(new ArrayList<>());
        //Then
        assertNull(service.validarFase2(repartoTipo, periodo));
    }

    @Test
    public void validarFase2_Error1(){
        //When
        when(jpaRepository.verificarFasesEnEjecucion(repartoTipo, periodo)).thenReturn(1);
        //Then
        assertEquals(service.validarFase2(repartoTipo, periodo).size(), 1);
    }

    @Test
    public void validarFase2_Error2(){
        //When
        when(jpaRepository.verificarFasesEnEjecucion(repartoTipo, periodo)).thenReturn(0);
        when(jpaRepository.obtenerDriverFase2ConError(repartoTipo, periodo)).thenReturn(null);
        //Then
        assertEquals(service.validarFase2(repartoTipo, periodo).size(), 1);
    }

    @Test
    public void shouldValidarFase2_Error3(){
        List<String> lstSinDriver = new ArrayList(Arrays.asList("1","2","3"));
        //When
        when(jpaRepository.verificarFasesEnEjecucion(repartoTipo, periodo))
                .thenReturn(0);
        when(jpaRepository.obtenerDriverFase2ConError(repartoTipo,periodo))
                .thenReturn(lstSinDriver);
        //Then
        assertEquals(4, service.validarFase2(repartoTipo, periodo).size());
    }

    @Test
    public void shouldEjecutarFase3(){
        //When
        when(service.ejecutarFase3(repartoTipo, periodo, userEmail, userNames))
                .thenReturn(200);
        //Then
        int response = service.ejecutarFase3(repartoTipo, periodo, userEmail, userNames);
        assertEquals(200, response);
    }

    @Test
    public void shouldValidarFase3(){
        //When
        when(jpaRepository.verificarFasesEnEjecucion(repartoTipo, periodo)).thenReturn(0);
        when(jpaRepository.obtenerDriverFase3ConError(repartoTipo, periodo)).thenReturn(new ArrayList<>());
        //Then
        assertNull(service.validarFase3(repartoTipo, periodo));
    }

    @Test
    public void validarFase3_Error1(){
        //When
        when(jpaRepository.verificarFasesEnEjecucion(repartoTipo, periodo)).thenReturn(1);
        //Then
        assertEquals(service.validarFase3(repartoTipo, periodo).size(), 1);
    }

    @Test
    public void validarFase3_Error2(){
        //When
        when(jpaRepository.verificarFasesEnEjecucion(repartoTipo, periodo)).thenReturn(0);
        when(jpaRepository.obtenerDriverFase3ConError(repartoTipo, periodo)).thenReturn(null);
        //Then
        assertEquals(service.validarFase3(repartoTipo, periodo).size(), 1);
    }

    @Test
    public void shouldValidarFase3_Error3(){
        List<String> lstSinDriver = new ArrayList(Arrays.asList("1","2","3"));
        //When
        when(jpaRepository.verificarFasesEnEjecucion(repartoTipo, periodo))
                .thenReturn(0);
        when(jpaRepository.obtenerDriverFase3ConError(repartoTipo,periodo))
                .thenReturn(lstSinDriver);
        //Then
        assertEquals(4, service.validarFase3(repartoTipo, periodo).size());
    }

    @Test
    public void shouldFasesCompletadas(){
        // When
        when(jpaRepository.fasesCompletadas(repartoTipo, periodo)).thenReturn(1);
        // Then
        int response = service.fasesCompletadas(repartoTipo, periodo);
        assertEquals(1, response);
    }

    @Test
    public void shouldHayFaseEnEjecucion(){
        // When
        when(jpaRepository.verificarFasesEnEjecucion(repartoTipo, periodo)).thenReturn(0);
        // Then
        int response = service.hayFaseEnEjecucion(repartoTipo, periodo);
        assertEquals(0,response);
    }

    @Test
    public void shouldObtenerEstadoCierreProceso(){
        // When
        when(jpaRepository.procesoCerrado(repartoTipo, periodo)).thenReturn(1);
        // Then
        int response = service.obtenerEstadoCierreProceso(repartoTipo, periodo);
        assertEquals(1, response);
    }

    @Test
    public void shouldEjecutarCierreProceso(){
        //When
        /*when(jpaRepository.verificarProcesosCompletos(repartoTipo,periodo))
                .thenReturn(true);*/
        when(jpaRepository.obtenerEstadoProceso(repartoTipo, periodo)).thenReturn(2);
        /*when(jpaRepository.generarReporteBolsasOficinas(repartoTipo, periodo)).thenReturn(1);
        when(jpaRepository.generarReporteCascada(repartoTipo, periodo)).thenReturn(1);
        when(jpaRepository.generarReporteObjetos(repartoTipo, periodo)).thenReturn(1);
        doNothing().when(jpaRepository).borrarDistribucionesFase1();
        doNothing().when(jpaRepository).borrarDistribucionesFase2();
        doNothing().when(jpaRepository).borrarDistribucionesFase3();*/
        when(jpaRepository.updateCierreProceso(repartoTipo, periodo, estado)).thenReturn(2);
        //Then
        int response = service.ejecutarCierreProceso(repartoTipo,periodo);
        assertEquals(200, response);
    }

    /*
    @Test
    public void shouldEjecutarCierreProceso_Ruta2(){
        //When
        when(jpaRepository.verificarProcesosCompletos(repartoTipo,periodo))
                .thenReturn(true);
        when(jpaRepository.obtenerEstadoProceso(repartoTipo, periodo)).thenReturn(-1);
        when(jpaRepository.generarReporteBolsasOficinas(repartoTipo, periodo)).thenReturn(0);
        //Then
        int response = service.ejecutarCierreProceso(repartoTipo,periodo);
        assertEquals(200, response);
    }

    @Test
    public void shouldEjecutarCierreProceso_Error1(){
        //When
        when(jpaRepository.verificarProcesosCompletos(repartoTipo,periodo))
                .thenReturn(false);
        //Then
        int response = service.ejecutarCierreProceso(repartoTipo,periodo);
        assertEquals(200, response);
    }

    @Test
    public void shouldEjecutarCierreProceso_Error2(){
        //When
        when(jpaRepository.verificarProcesosCompletos(repartoTipo,periodo))
                .thenReturn(true);
        when(jpaRepository.obtenerEstadoProceso(repartoTipo, periodo)).thenReturn(-2);
        //Then
        int response = service.ejecutarCierreProceso(repartoTipo,periodo);
        assertEquals(200, response);
    }

    @Test
    public void shouldEjecutarCierreProceso_Error3(){
        //When
        when(jpaRepository.verificarProcesosCompletos(repartoTipo,periodo))
                .thenReturn(true);
        when(jpaRepository.obtenerEstadoProceso(repartoTipo, periodo)).thenReturn(1);
        //Then
        int response = service.ejecutarCierreProceso(repartoTipo,periodo);
        assertEquals(200, response);
    }

    @Test
    public void shouldEjecutarCierreProceso_Error4(){
        //When
        when(jpaRepository.verificarProcesosCompletos(repartoTipo,periodo))
                .thenReturn(true);
        when(jpaRepository.obtenerEstadoProceso(repartoTipo, periodo)).thenReturn(2);
        when(jpaRepository.generarReporteBolsasOficinas(repartoTipo, periodo)).thenReturn(1);
        when(jpaRepository.generarReporteCascada(repartoTipo, periodo)).thenReturn(0);
        //Then
        int response = service.ejecutarCierreProceso(repartoTipo,periodo);
        assertEquals(200, response);
    }

    @Test
    public void shouldEjecutarCierreProceso_Error5(){
        //When
        when(jpaRepository.verificarProcesosCompletos(repartoTipo,periodo))
                .thenReturn(true);
        when(jpaRepository.obtenerEstadoProceso(repartoTipo, periodo)).thenReturn(2);
        when(jpaRepository.generarReporteBolsasOficinas(repartoTipo, periodo)).thenReturn(1);
        when(jpaRepository.generarReporteCascada(repartoTipo, periodo)).thenReturn(1);
        when(jpaRepository.generarReporteObjetos(repartoTipo, periodo)).thenReturn(0);
        //Then
        int response = service.ejecutarCierreProceso(repartoTipo,periodo);
        assertEquals(200, response);
    }*/

    @Test
    public void shouldValidarCierreProceso_Route1() {
        // When
        when(jpaRepository.verificarProcesosCompletos(repartoTipo, periodo)).thenReturn(false);
        // Then
        assertNotNull(service.validarCierreProceso(repartoTipo, periodo));
    }

    @Test
    public void shouldValidarCierreProceso_Route2() {
        // When
        when(jpaRepository.verificarProcesosCompletos(repartoTipo, periodo)).thenReturn(true);
        when(jpaRepository.obtenerEstadoProceso(repartoTipo, periodo)).thenReturn(-2);
        // Then
        assertNotNull(service.validarCierreProceso(repartoTipo, periodo));
    }

    @Test
    public void shouldValidarCierreProceso_Route3() {
        // When
        when(jpaRepository.verificarProcesosCompletos(repartoTipo, periodo)).thenReturn(true);
        when(jpaRepository.obtenerEstadoProceso(repartoTipo, periodo)).thenReturn(1);
        // Then
        assertNotNull(service.validarCierreProceso(repartoTipo, periodo));
    }

    @Test
    public void shouldValidarCierreProceso_Route4() {
        // When
        when(jpaRepository.verificarProcesosCompletos(repartoTipo, periodo)).thenReturn(true);
        when(jpaRepository.obtenerEstadoProceso(repartoTipo, periodo)).thenReturn(-1);
        // Then
        assertNull(service.validarCierreProceso(repartoTipo, periodo));
    }

    @Test
    public void shouldTerminarProcesosIndefinidos() {
        // When
        when(jpaRepository.terminarProcesosIndefinidos()).thenReturn(1);
        // Then
        assertEquals(1, service.terminarProcesosIndeterminados());
    }

    @Test
    public void shouldGetAsignacionesFase3() {
        // When
        List<AsignacionCecoBolsa> list = new ArrayList();
        list.add(AsignacionCecoBolsa.builder()
          .codCentro("asignacionCecoBolsa.getCodCentro()")
          .codCuentaContable("asignacionCecoBolsa.getCodCuentaContable()")
          .codDriver("asignacionCecoBolsa.getCodDriver()")
          .codPartida("asignacionCecoBolsa.getCodPartida()")
          .nombreCentro("asignacionCecoBolsa.getNombreCentro()")
          .nombreCuentaContable("asignacionCecoBolsa.getNombreCuentaContable()")
          .nombreDriver("asignacionCecoBolsa.getNombreDriver()")
          .nombrePartida("asignacionCecoBolsa.getNombrePartida()")
          .build());
        when(jpaRepository.getAsignacionesFase1Val3(repartoTipo, periodo)).thenReturn(list);
        // Then
        assertNotNull(service.getAsignacionesFase1Val3(repartoTipo, periodo));
    }

    @Test
    public void shouldEjecucionTotal() {
        //When
        when(service.ejecucionTotal(repartoTipo, periodo, userEmail, userNames))
                .thenReturn(100);
        //Then
        int response = service.ejecucionTotal(repartoTipo, periodo, userEmail, userNames);
        assertEquals(100, response);
    }

    @Test
    public void obtenerFechaEjecucion() {
        // When
        when(jpaRepository.obtenerFechaEjecucion(repartoTipo, periodo, fase)).thenReturn(ejecucionProceso);
        // Then
        EjecucionProceso response = service.obtenerFechaEjecucion(repartoTipo, periodo, fase);
        Assertions.assertEquals(ejecucionProceso,
                response);
    }

}
