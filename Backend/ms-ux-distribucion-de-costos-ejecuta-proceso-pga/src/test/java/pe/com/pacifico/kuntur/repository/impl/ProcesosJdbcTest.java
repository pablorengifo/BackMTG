package pe.com.pacifico.kuntur.repository.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pe.com.pacifico.kuntur.model.EjecucionProceso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcesosJdbcTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ProcesoJdbcRepository procesoJdbcRepository;

    //Given
    int repartoTipo = 1;
    int periodo = 202104;
    String userEmail = "tester@pps.com.pe";
    String userNames = "Dummy";
    int fase = 0;
    int iteracion = 0;
    String codigo = "-";
    double precision = 0;
    int nroReporte = 0;
    int answer = 1;
    String tabla = "-";
    int estado = 1;

    int countFases = 3;
    int procesoCerrado = 2;

    @Test
    public void fasesCompletadas(){
        //when
        when(procesoJdbcRepository.fasesCompletadas(repartoTipo,periodo)).thenAnswer(invocation -> countFases);

        //Then
        Integer response = procesoJdbcRepository.fasesCompletadas(repartoTipo, periodo);
        System.out.println("Response: " + response);
        //Assertions.assertNotNull(response);
        assertEquals(3, response);
    }

    @Test
    public void fasesCompletadas_Error1(){
        //when
        when(jdbcTemplate.queryForObject(anyString(), any(Class.class), anyInt(), anyInt())).thenThrow(MockitoException.class);
        //Then
        Integer response = procesoJdbcRepository.fasesCompletadas(repartoTipo, periodo);
        System.out.println("Response: " + response);
        //Assertions.assertNotNull(response);
        assertEquals(0, response);
    }

    @Test
    public void procesoCerrado() {
        //When
        when(procesoJdbcRepository.procesoCerrado(repartoTipo,periodo)).thenAnswer(invocation -> procesoCerrado);
        //when(procesoJdbcRepository.procesoCerrado(repartoTipo,periodo)).thenReturn(procesoCerrado);
        //Then
        int response = procesoJdbcRepository.procesoCerrado(repartoTipo, periodo);
        assertEquals(2, response);
    }

    @Test
    public void verificarFasesEnEjecucion() {
        // When
        when(procesoJdbcRepository.verificarFasesEnEjecucion(repartoTipo,periodo)).thenAnswer(invocation -> countFases);
        //Then
        int response = procesoJdbcRepository.verificarFasesEnEjecucion(repartoTipo, periodo);
        assertEquals(3,response);
    }

    @Test
    public void insertarEjecucionIni() {
        // When
        when((jdbcTemplate).update(anyString(),
                any(),any(),any(),any(),any(),any())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.insertarEjecucionIni(periodo, fase, repartoTipo, userEmail, userNames);
        assertEquals(1, res);
    }

    @Test
    public void insertarEjecucionFin() {
        // When
        when((jdbcTemplate).update(anyString(),
                any(),any(),any(),any())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.insertarEjecucionFin(periodo, fase, repartoTipo);
        assertEquals(1, res);
    }

    @Test
    public void verificarDetalleGasto() {
        // When
        when(procesoJdbcRepository.verificarDetalleGasto(repartoTipo,periodo)).thenAnswer(invocation -> countFases);
        //Then
        boolean response = procesoJdbcRepository.verificarDetalleGasto(repartoTipo, periodo);
        assertTrue(response);
    }

    @Test
    public void obtenerDriverFase1ConError() {
        //when
        when(jdbcTemplate.queryForList(anyString(), any(), any(), any(), any(), any()))
                .thenReturn(Arrays.asList("Codigo1","Codigo2"));

        //then
        assertThat(procesoJdbcRepository.obtenerDriverFase1ConError(repartoTipo,periodo),notNullValue());
        assertThat(procesoJdbcRepository.obtenerDriverFase1ConError(repartoTipo,periodo).get(0),equalToIgnoringCase("Codigo1"));
    }

    @Test
    public void obtenerDriverFase1ConError_Error() {
        int repartoTipo = 2;
        //when
        when(jdbcTemplate.queryForList(anyString(), any(), any(), any(), any(), any()))
                .thenThrow(MockitoException.class);

        //then
        assertNull(procesoJdbcRepository.obtenerDriverFase1ConError(repartoTipo,periodo));
    }

    @Test
    public void borrarAsignaciones() {
        // When
        when((jdbcTemplate).update(anyString(),
                any(),any(),any())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.borrarAsignaciones(periodo, fase, repartoTipo);
        assertEquals(1, res);
    }

    @Test
    public void borrarAsignaciones_Error() {
        // When
        when((jdbcTemplate).update(anyString(),
                any(),any(),any())).thenThrow(MockitoException.class);
        // Then
        int res = procesoJdbcRepository.borrarAsignaciones(periodo, fase, repartoTipo);
        assertEquals(-1, res);
    }

    @Test
    public void borrarDistribucionesFase1() {
        // When
        doNothing().when(jdbcTemplate).execute(anyString());
        // Then
        procesoJdbcRepository.borrarDistribucionesFase1();
        verify(jdbcTemplate,times(3)).execute(anyString());
    }

    @Test
    public void borrarDistribucionesFase1_Error() {
        // When
        doThrow(MockitoException.class).when(jdbcTemplate).execute(anyString());
        // Then
        procesoJdbcRepository.borrarDistribucionesFase1();
        verify(jdbcTemplate,times(1)).execute(anyString());
    }

    @Test
    public void borrarDistribucionesFase2() {
        // When
        doNothing().when(jdbcTemplate).execute(anyString());
        // Then
        procesoJdbcRepository.borrarDistribucionesFase2();
        verify(jdbcTemplate,times(4)).execute(anyString());
    }

    @Test
    public void borrarDistribucionesFase2_Error() {
        // When
        doThrow(MockitoException.class).when(jdbcTemplate).execute(anyString());
        // Then
        procesoJdbcRepository.borrarDistribucionesFase2();
        verify(jdbcTemplate,times(1)).execute(anyString());
    }

    @Test
    public void borrarDistribucionesFase3() {
        // When
        doNothing().when(jdbcTemplate).execute(anyString());
        // Then
        procesoJdbcRepository.borrarDistribucionesFase3();
        verify(jdbcTemplate,times(3)).execute(anyString());
    }

    @Test
    public void borrarDistribucionesFase3_Error() {
        // When
        doThrow(MockitoException.class).when(jdbcTemplate).execute(anyString());
        // Then
        procesoJdbcRepository.borrarDistribucionesFase3();
        verify(jdbcTemplate,times(1)).execute(anyString());
    }

    @Test
    public void iniciarFase1() {
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt(),anyInt())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.iniciarFase1(periodo, repartoTipo);
        assertEquals(1, res);
    }

    @Test
    public void insertarDistribucionBolsas() {
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt(),anyInt())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.insertarDistribucionBolsas(periodo, repartoTipo);
        assertEquals(1, res);
    }


    @Test
    public void obtenerDriverFase2ConError() {
        // When
        when(jdbcTemplate.queryForList(anyString(),
                any(), any(), any(), any(), any(),
                any(), any(), any(), any()))
                .thenReturn(Arrays.asList("Codigo1","Codigo2"));

        // Then
        assertThat(procesoJdbcRepository.obtenerDriverFase2ConError(repartoTipo,periodo),notNullValue());
        assertThat(procesoJdbcRepository.obtenerDriverFase2ConError(repartoTipo,periodo).get(0),equalToIgnoringCase("Codigo1"));
    }

    @Test
    public void maxNivelCascada() {
        //when
        when(procesoJdbcRepository.maxNivelCascada(periodo,repartoTipo)).thenAnswer(invocation -> countFases);
        //then
        assertEquals(3,procesoJdbcRepository.maxNivelCascada(periodo, repartoTipo));

    }

    @Test
    public void iniciarFase2() {
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt())).thenReturn(1);
        when((jdbcTemplate).update(anyString(),
                anyInt(),anyInt())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.iniciarFase2(periodo, repartoTipo);
        assertEquals(1, res);
    }

    @Test
    public void iniciarFase2_Error() {
        int repartoTipo = 2;
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt())).thenReturn(1);
        when((jdbcTemplate).update(anyString(),
                anyInt(),anyInt())).thenThrow(MockitoException.class);
        // Then
        int res = procesoJdbcRepository.iniciarFase2(periodo, repartoTipo);
        assertEquals(-1, res);
    }

    @Test
    public void obtenerDriverFase3ConError() {
        // When
        when(jdbcTemplate.queryForList(anyString(),
                any(), anyInt(), anyInt()))
                .thenReturn(Arrays.asList("Codigo1","Codigo2"));

        // Then
        assertThat(procesoJdbcRepository.obtenerDriverFase3ConError(repartoTipo,periodo),notNullValue());
        assertThat(procesoJdbcRepository.obtenerDriverFase3ConError(repartoTipo,periodo).get(0),equalToIgnoringCase("Codigo1"));
    }

    @Test
    public void listarCodigosObjetos() {
        // When
        when(jdbcTemplate.queryForList(anyString(),
                any(), anyInt(), anyInt(), anyInt()))
                .thenReturn(Arrays.asList("Codigo1","Codigo2"));

        // Then
        assertThat(procesoJdbcRepository.listarCodigosObjetos(repartoTipo,periodo),notNullValue());
        assertThat(procesoJdbcRepository.listarCodigosObjetos(repartoTipo,periodo).get(0),equalToIgnoringCase("Codigo1"));
    }

    @Test
    public void listarCodigosObjetos_Error() {
        int repartoTipo = 2;
        // When
        when(jdbcTemplate.queryForList(anyString(),
                any(), anyInt(), anyInt(), anyInt()))
                .thenThrow(MockitoException.class);

        // Then
        assertEquals(0,(procesoJdbcRepository.listarCodigosObjetos(repartoTipo,periodo).size()));
    }

    @Test
    public void iniciarFase3() {
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt(),anyInt())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.iniciarFase3(periodo, repartoTipo);
        assertEquals(1, res);
    }

    @Test
    public void iniciarFase3_Error() {
        int repartoTipo = 2;
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt(),anyInt())).thenThrow(MockitoException.class);
        // Then
        int res = procesoJdbcRepository.iniciarFase3(periodo, repartoTipo);
        assertEquals(-1, res);
    }

    @Test
    public void insertarDistribucionesDeObjetosDeCosto() {
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt(),any())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.insertarDistribucionesDeObjetosDeCosto(repartoTipo, periodo, codigo);
        assertEquals(1, res);
    }

    @Test
    public void insertarDistribucionesDeObjetosDeCosto_Error() {
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt(),any())).thenThrow(MockitoException.class);
        // Then
        int res = procesoJdbcRepository.insertarDistribucionesDeObjetosDeCosto(repartoTipo, periodo, codigo);
        assertEquals(-1, res);
    }

    @Test
    public void insertarDistribucionCascadaPorNivel() {
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt(),anyInt(),anyInt(),any())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.insertarDistribucionCascadaPorNivel(iteracion, periodo, repartoTipo, precision);
        assertEquals(1, res);
    }

    @Test
    public void insertarDistribucionCascadaPorNivel_Error() {
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt(),anyInt(),anyInt(),any())).thenThrow(MockitoException.class);
        // Then
        int res = procesoJdbcRepository.insertarDistribucionCascadaPorNivel(iteracion, periodo, repartoTipo, precision);
        assertEquals(0, res);
    }

    @Test
    public void calcularFase2F() {
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.calcularFase2F(periodo, repartoTipo);
        assertEquals(1, res);
    }

    @Test
    public void calcularFase2F_error() {
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt())).thenThrow(MockitoException.class);
        // Then
        int res = procesoJdbcRepository.calcularFase2F(periodo, repartoTipo);
        assertEquals(0, res);
    }

    @Test
    public void obtenerEstadoProceso() {
        //when
        when(procesoJdbcRepository.obtenerEstadoProceso(repartoTipo, periodo)).thenAnswer(invocation -> countFases);
        //then
        assertEquals(3,procesoJdbcRepository.obtenerEstadoProceso(repartoTipo, periodo));
    }

    @Test
    public void verificarProcesosCompletos() {
        // When
        when(procesoJdbcRepository.verificarProcesosCompletos(repartoTipo,periodo)).thenAnswer(invocation -> countFases);
        //Then
        boolean response = procesoJdbcRepository.verificarProcesosCompletos(repartoTipo, periodo);
        assertTrue(response);
    }

    @Test
    public void existeInformacionReporteTabla() {
        // When
        when(procesoJdbcRepository.existeInformacionReporteTabla(repartoTipo,periodo, nroReporte)).thenAnswer(invocation -> countFases);
        //Then
        boolean response = procesoJdbcRepository.existeInformacionReporteTabla(repartoTipo, periodo, nroReporte);
        assertTrue(response);
    }

    @Test
    public void insertarGeneracionReporte() {
        // When
        lenient().when(jdbcTemplate.update(anyString(),
                any(),any(),any()))
                .thenReturn(1);
        lenient().when(jdbcTemplate.update(anyString(),
                any(),any(),any(),any()))
                .thenReturn(1);
        // Then
        assertEquals(answer,procesoJdbcRepository.insertarGeneracionReporte(repartoTipo, periodo, nroReporte));
    }

    @Test
    public void generarReporteBolsasOficinas(){
        // When
        lenient().when(jdbcTemplate.update(anyString(),
                anyInt()))
                .thenReturn(1);
        lenient().when(jdbcTemplate.update(anyString(),
                any(),any(),any(),any(), any()))
                .thenReturn(1);
        // Then
        assertEquals(answer,procesoJdbcRepository.generarReporteBolsasOficinas(repartoTipo, periodo));
    }

    @Test
    public void generarReporteCascada(){
        // When
        lenient().when(jdbcTemplate.update(anyString(),
                anyInt()))
                .thenReturn(1);
        lenient().when(jdbcTemplate.update(anyString(),
                any(),any(),any(),any(), any(),any(), any()))
                .thenReturn(1);
        // Then
        assertEquals(answer,procesoJdbcRepository.generarReporteCascada(repartoTipo, periodo));
    }

    @Test
    public void actualizarTipoNegocio(){
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.actualizarTipoNegocio(periodo, tabla, 1);
        assertEquals(1, res);
    }

    @Test
    public void actualizarAsignacion(){
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt(), anyInt(), anyInt())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.actualizarAsignacion(repartoTipo, periodo, tabla, 1);
        assertEquals(1, res);
    }

    @Test
    public void actualizarNiif17Atribuible(){
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.actualizarNiif17Atribuible(periodo, tabla, 1);
        assertEquals(1, res);
    }

    @Test
    public void generarReporteObjetos(){
        // When
        lenient().when(jdbcTemplate.queryForObject(any(),
                any(Class.class)))
                .thenReturn(1);
        lenient().when(jdbcTemplate.queryForObject(any(),
                any(Class.class),anyInt()))
                .thenReturn(1);
        lenient().when(jdbcTemplate.update(anyString(),
                anyInt()))
                .thenReturn(1);
        lenient().when(jdbcTemplate.update(anyString(),
                any(),any(),any(),any(), any(),any(), any(),
                any(),any(),any(),any(), any(),any(), any()))
                .thenReturn(1);
        // Then
        assertEquals(answer,procesoJdbcRepository.generarReporteObjetos(repartoTipo, periodo));
    }

    @Test
    public void insertarCierreProceso(){
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt(), anyInt(), anyInt())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.insertarCierreProceso(repartoTipo, periodo, estado);
        assertEquals(1, res);
    }

    @Test
    public void updateCierreProceso(){
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt(), anyInt(), anyInt())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.updateCierreProceso(repartoTipo, periodo, estado);
        assertEquals(1, res);
    }

    @Test
    public void obtenerFechaEjecucion() {
        EjecucionProceso ejecucionProceso = new EjecucionProceso(new Date().getTime(),
                "unit@test.com", "unit test");
        Object[] array = new Object[]{repartoTipo, periodo, fase};
        // When
        when((jdbcTemplate).queryForObject(anyString(), any(), any(RowMapper.class))).thenReturn(ejecucionProceso);
        // Then
        assertEquals(ejecucionProceso, procesoJdbcRepository.obtenerFechaEjecucion(repartoTipo, periodo, fase));
    }

    @Test
    public void cleanCierreProceso() {
        // When
        when((jdbcTemplate).update(anyString(),
                anyInt(), anyInt())).thenReturn(1);
        // Then
        int res = procesoJdbcRepository.cleanCierreProceso(repartoTipo, periodo);
        assertEquals(1, res);
    }

    @Test
    public void shouldGetAsiganacionesFase1Val3() {
        // When
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class),
                anyInt(), anyInt(), anyString(), anyInt(), anyInt(), anyInt(), anyString()))
                .thenReturn(new ArrayList(Collections.singleton("hola")));
        // Then
        assertEquals(1, procesoJdbcRepository.getAsignacionesFase1Val3(repartoTipo,periodo).size());
    }

    @Test
    public void shouldNotGetAsiganacionesFase1Val3() {
        // When
        when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class),
                anyInt(), anyInt(), anyString(), anyInt(), anyInt(), anyInt(), anyString()))
                .thenThrow(EmptyResultDataAccessException.class);
        // Then
        assertEquals(0, procesoJdbcRepository.getAsignacionesFase1Val3(repartoTipo,periodo).size());
    }

}
