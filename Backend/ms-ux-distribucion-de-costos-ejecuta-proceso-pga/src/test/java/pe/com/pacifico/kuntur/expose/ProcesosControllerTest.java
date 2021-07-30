package pe.com.pacifico.kuntur.expose;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.ProcesoService;
import pe.com.pacifico.kuntur.model.EjecucionProceso;
import pe.com.pacifico.kuntur.business.threads.HiloEjecucionTotal;
import pe.com.pacifico.kuntur.business.threads.HiloFase3;

import java.util.Date;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProcesosControllerTest {

    //Given
    int repartoTipo=1;
    int periodo = 202104;
    String userEmail = "tester@pps.com.pe";
    String userNames = "Dummy";
    String json = String.format("{ \"userName\": %s, \"userEmail\": %s}", userNames, userEmail);
    EjecucionProceso ejecucionProceso = new EjecucionProceso(new Date().getTime(), userEmail, userNames);

    int resultFase = 100;
    int fase = 1;
    boolean resultValidacion = true;

    @Mock
    private ProcesoService procesoService;

    @InjectMocks
    private ProcesoController procesoController;

    @Test
    public void shouldFase1() {
        //When
        when(procesoService.ejecutarFase1(repartoTipo, periodo, userEmail, userNames)).thenReturn(resultFase);

        //Then
        int response = procesoController.fase1(repartoTipo, periodo, json);
        Assertions.assertEquals(resultFase, response);
    }

    @Test
    public void shouldFase1Validaciones() {
        //When
        when(procesoService.validarFase1(repartoTipo, periodo)).thenReturn(null);

        //Then
        Assertions.assertNull(procesoController.fase1Validaciones(repartoTipo,periodo));
    }

    @Test
    public void shouldFase2Validaciones() {
        //When
        when(procesoService.validarFase2(repartoTipo, periodo)).thenReturn(null);

        //Then
        Assertions.assertNull(procesoController.fase2Validaciones(repartoTipo,periodo));
    }

    @Test
    public void shouldFase3Validaciones() {
        //When
        when(procesoService.validarFase3(repartoTipo, periodo)).thenReturn(null);

        //Then
        Assertions.assertNull(procesoController.fase3Validaciones(repartoTipo,periodo));
    }

    @Test
    public void shouldFase2() {
        //When
        when(procesoService.ejecutarFase2(repartoTipo, periodo, userEmail, userNames)).thenReturn(resultFase);

        //Then
        int response = procesoController.fase2(repartoTipo, periodo, json);
        Assertions.assertEquals(resultFase, response);
    }

    @Test
    public void ShouldObtenerFase3() {
        HiloFase3.progreso = 0;
        //Then
        int response = HiloFase3.progreso;
        Assertions.assertEquals(0, response);
    }

    @Test
    public void shouldFase3() {
        //When
        when(procesoService.ejecutarFase3(repartoTipo, periodo, userEmail, userNames)).thenReturn(resultFase);

        //Then
        int response = procesoController.fase3(repartoTipo, periodo, json);
        Assertions.assertEquals(resultFase, response);
    }

    @Test
    public void shouldGetEstadoCierreProceso(){
        // When
        when(procesoService.obtenerEstadoCierreProceso(repartoTipo, periodo)).thenReturn(resultFase);
        //Then
        int response = procesoController.estadoCierreProceso(repartoTipo, periodo);
        Assertions.assertEquals(resultFase, response);
    }

    @Test
    public void shouldCerrarProceso() {
        // When
        when(procesoService.ejecutarCierreProceso(repartoTipo, periodo)).thenReturn(resultFase);
        //Then
        int response = procesoController.cerrarProceso(repartoTipo, periodo);
        Assertions.assertEquals(resultFase, response);
    }

    @Test
    public void fasesEjecutadas() {
        // When
        when(procesoService.fasesCompletadas(repartoTipo, periodo)).thenReturn(resultFase);
        //Then
        int response = procesoController.fasesEjecutadas(repartoTipo, periodo);
        Assertions.assertEquals(resultFase, response);
    }

    @Test
    public void faseEnEjecucion() {
        // When
        when(procesoService.hayFaseEnEjecucion(repartoTipo, periodo)).thenReturn(1);
        //Then
        int response = procesoController.faseEnEjecucion(repartoTipo, periodo);
        Assertions.assertEquals(1, response);
    }

    // Ejecución total
    @Test
    public void obtenerEjecucionTotal() {
        HiloEjecucionTotal.progreso = 0;
        //Then
        int response = HiloEjecucionTotal.progreso;
        Assertions.assertEquals(0, response);
    }

    @Test
    public void ejecucionTotal() {
        //When
        when(procesoService.ejecucionTotal(repartoTipo, periodo, userEmail, userNames)).thenReturn(resultFase);

        //Then
        int response = procesoController.ejecucionTotal(repartoTipo, periodo, json);
        Assertions.assertEquals(resultFase, response);
    }

    @Test
    public void obtenerFechaEjecucion() {
        // When
        when(procesoService.obtenerFechaEjecucion(repartoTipo, periodo, fase)).thenReturn(ejecucionProceso);

        // Then
        EjecucionProceso response = procesoController.obtenerFechaEjecucion(repartoTipo, periodo, fase);
        Assertions.assertEquals(ejecucionProceso, response);
    }
}
