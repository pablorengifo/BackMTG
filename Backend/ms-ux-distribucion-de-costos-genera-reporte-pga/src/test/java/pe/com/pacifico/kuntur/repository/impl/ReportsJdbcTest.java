package pe.com.pacifico.kuntur.repository.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import pe.com.pacifico.kuntur.model.*;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportsJdbcTest {

    @Mock
    private NamedParameterJdbcTemplate plantilla;
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ReportsJdbcRepository jdbcRepository;

    // given
    private int periodo = 202005;
    private int repartoTipo = 1;

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

    private Reporte4CascadaCentros response4_1 = new Reporte4CascadaCentros();
    private Reporte4CascadaCentros response4_2 = new Reporte4CascadaCentros();
    private Reporte4CascadaCentros response4_3 = new Reporte4CascadaCentros();

    private Reporte5LineaCanal response5_1 = new Reporte5LineaCanal();
    private Reporte5LineaCanal response5_2 = new Reporte5LineaCanal();
    private Reporte5LineaCanal response5_3 = new Reporte5LineaCanal();

    private Reporte6Control1 response6A_1 = new Reporte6Control1();
    private Reporte6Control1 response6A_2 = new Reporte6Control1();
    private Reporte6Control1 response6A_3 = new Reporte6Control1();

    private Reporte6Control2 response6B_1 = new Reporte6Control2();
    private Reporte6Control2 response6B_2 = new Reporte6Control2();
    private Reporte6Control2 response6B_3 = new Reporte6Control2();

    private FiltroLinea filtroLinea_1 = new FiltroLinea();
    private FiltroLinea filtroLinea_2 = new FiltroLinea();
    private FiltroLinea filtroLinea_3 = new FiltroLinea();

    @Test
    public void obtenerReporte1BolsasOficinas() {
        //when
        lenient().doReturn(new ArrayList())
                .when(plantilla).queryForList(
                any(String.class),
                any(SqlParameterSource.class));
        //then
        assertNotNull(jdbcRepository.obtenerReporte1BolsasOficinas(repartoTipo, periodo));
    }

    @Test
    public void obtenerReporte1Vacio() {
        //when
        lenient().doReturn(new ArrayList())
          .when(plantilla).queryForList(
          any(String.class),
          any(SqlParameterSource.class));
        //then
        assertNotNull(jdbcRepository.obtenerReporte1Vacio());
    }

    @Test
    public void obtenerReporte1BolsasOficinasLimited() {
        //when
        doReturn(Arrays.asList(response1_1,response1_2,response1_3))
                .when(jdbcTemplate).query(anyString(),
                any(BeanPropertyRowMapper.class),anyInt());

        //then
        assertEquals(3,jdbcRepository.obtenerReporte1BolsasOficinasLimited(repartoTipo, periodo).size());
        assertEquals(response1_1, jdbcRepository.obtenerReporte1BolsasOficinasLimited(repartoTipo, periodo).get(0));
    }

    @Test
    public void obtenerReporte2Cascada() {
        //when
        doReturn(Arrays.asList(response2_1,response2_2,response2_3))
                .when(jdbcTemplate).query(anyString(),
                any(BeanPropertyRowMapper.class),anyInt());

        //then
        assertEquals(3,jdbcRepository.obtenerReporte2Cascada(repartoTipo, periodo).size());
        assertEquals(response2_1, jdbcRepository.obtenerReporte2Cascada(repartoTipo, periodo).get(0));
    }

    @Test
    public void obtenerReporte3Objetos() {
        //when
        doReturn(Arrays.asList(response3_1,response3_2,response3_3))
                .when(jdbcTemplate).query(anyString(),
                any(BeanPropertyRowMapper.class),anyInt());

        //then
        assertEquals(3,jdbcRepository.obtenerReporte3Objetos(repartoTipo, periodo).size());
        assertEquals(response3_1, jdbcRepository.obtenerReporte3Objetos(repartoTipo, periodo).get(0));
    }

    @Test
    public void obtenerReporte4Limited() {
        //when
        doReturn(Arrays.asList(response4_1,response4_2,response4_3))
          .when(jdbcTemplate).query(anyString(),
          any(BeanPropertyRowMapper.class));

        //then
        assertEquals(3,jdbcRepository.obtenerReporte4CascadaCentrosLimited(repartoTipo, periodo).size());
        assertEquals(response4_1, jdbcRepository.obtenerReporte4CascadaCentrosLimited(repartoTipo, periodo).get(0));
    }

    @Test
    public void obtenerReporte4CascadaCentros() {
        //when
        lenient().doReturn(new ArrayList())
                .when(plantilla).queryForList(
                anyString(),
                any(MapSqlParameterSource.class));
        //then
        assertNotNull(jdbcRepository.obtenerReporte4CascadaCentros(repartoTipo, periodo));
    }

    @Test
    public void obtenerReporte5LineaCanal() {
        //when
        lenient().doReturn(new ArrayList())
                .when(plantilla).queryForList(
                anyString(),
                any(MapSqlParameterSource.class));
        //then
        assertNotNull(jdbcRepository.obtenerReporte5LineaCanal(repartoTipo, periodo, dimensiones, lineas, canales));
    }

    @Test
    public void obtenerReporte5LineaCanalCompleto() {
        int repartoTipo = 2;
        List<String> dimensiones = new ArrayList(Arrays.asList("CUENTA CONTABLE INICIAL",
          "PARTIDA INICIAL", "CENTRO INICIAL"));
        List<String> lineas = new ArrayList(Arrays.asList("test"));
        List<String> canales = new ArrayList(Arrays.asList("test"));
        //when
        lenient().doReturn(new ArrayList())
          .when(plantilla).queryForList(
          anyString(),
          any(MapSqlParameterSource.class));
        //then
        assertNotNull(jdbcRepository.obtenerReporte5LineaCanal(repartoTipo, periodo, dimensiones, lineas, canales));
    }

    @Test
    public void obtenerReporte5Limited() {
        //when
        doReturn(Arrays.asList(response5_1,response5_2,response5_3))
          .when(jdbcTemplate).query(anyString(),
          any(BeanPropertyRowMapper.class));

        //then
        assertEquals(3,jdbcRepository.obtenerReporte5LineaCanalLimited(repartoTipo, periodo, dimensiones, lineas, canales).size());
        assertEquals(response5_1, jdbcRepository.obtenerReporte5LineaCanalLimited(repartoTipo, periodo, dimensiones, lineas, canales).get(0));
    }

    @Test
    public void obtenerReporte5LimitedCompleto() {
        int repartoTipo = 2;
        List<String> dimensiones = new ArrayList(Arrays.asList("CUENTA CONTABLE INICIAL",
          "PARTIDA INICIAL", "CENTRO INICIAL"));
        List<String> lineas = new ArrayList(Arrays.asList("test"));
        List<String> canales = new ArrayList(Arrays.asList("test"));
        //when
        doReturn(Arrays.asList(response5_1,response5_2,response5_3))
          .when(jdbcTemplate).query(anyString(),
          any(BeanPropertyRowMapper.class));
        //then
        assertEquals(3,jdbcRepository.obtenerReporte5LineaCanalLimited(repartoTipo, periodo, dimensiones, lineas, canales).size());
        assertEquals(response5_1, jdbcRepository.obtenerReporte5LineaCanalLimited(repartoTipo, periodo, dimensiones, lineas, canales).get(0));
    }

    @Test
    public void obtenerReporte5Vacio() {
        //when
        lenient().doReturn(new ArrayList())
          .when(plantilla).queryForList(
          any(String.class),
          any(SqlParameterSource.class));
        //then
        assertNotNull(jdbcRepository.obtenerReporte5Vacio(dimensiones, lineas, canales));
    }

    @Test
    public void obtenerReporte5VacioCompleto() {
        int repartoTipo = 2;
        List<String> dimensiones = new ArrayList(Arrays.asList("CUENTA CONTABLE INICIAL",
          "PARTIDA INICIAL", "CENTRO INICIAL"));
        List<String> lineas = new ArrayList(Arrays.asList("test"));
        List<String> canales = new ArrayList(Arrays.asList("test"));
        //when
        lenient().doReturn(new ArrayList())
          .when(plantilla).queryForList(
          any(String.class),
          any(SqlParameterSource.class));
        //then
        assertNotNull(jdbcRepository.obtenerReporte5Vacio(dimensiones, lineas, canales));
    }

    @Test
    public void obtenerReporte6A() {
        //when
        lenient().doReturn(new ArrayList())
                .when(plantilla).queryForList(
                anyString(),
                any(MapSqlParameterSource.class));
        //then
        assertNotNull(jdbcRepository.obtenerReporte6A(repartoTipo, periodo));
    }

    @Test
    public void obtenerReporte6A_RT2() {
        int repartoTipo = 2;
        //when
        lenient().doReturn(new ArrayList())
          .when(plantilla).queryForList(
          anyString(),
          any(MapSqlParameterSource.class));
        //then
        assertNotNull(jdbcRepository.obtenerReporte6A(repartoTipo, periodo));
    }

    @Test
    public void obtenerReporte6A_Limited() {
        //when
        doReturn(Arrays.asList(response6A_1,response6A_2,response6A_3))
          .when(jdbcTemplate).query(anyString(),
          any(BeanPropertyRowMapper.class));

        //then
        assertEquals(3,jdbcRepository.obtenerReporte6ALimited(repartoTipo, periodo).size());
        assertEquals(response6A_1, jdbcRepository.obtenerReporte6ALimited(repartoTipo, periodo).get(0));
    }

    @Test
    public void obtenerReporte6A_LimitedRT2() {
        int repartoTipo = 2;
        //when
        doReturn(Arrays.asList(response6A_1,response6A_2,response6A_3))
          .when(jdbcTemplate).query(anyString(),
          any(BeanPropertyRowMapper.class));

        //then
        assertEquals(3,jdbcRepository.obtenerReporte6ALimited(repartoTipo, periodo).size());
        assertEquals(response6A_1, jdbcRepository.obtenerReporte6ALimited(repartoTipo, periodo).get(0));
    }

    @Test
    public void obtenerReporte6B() {
        //when
        lenient().doReturn(new ArrayList())
                .when(plantilla).queryForList(
                anyString(),
                any(MapSqlParameterSource.class));
        //then
        assertNotNull(jdbcRepository.obtenerReporte6B(repartoTipo, periodo));
    }

    @Test
    public void obtenerReporte6B_RT2() {
        int repartoTipo = 2;
        //when
        lenient().doReturn(new ArrayList())
          .when(plantilla).queryForList(
          anyString(),
          any(MapSqlParameterSource.class));
        //then
        assertNotNull(jdbcRepository.obtenerReporte6B(repartoTipo, periodo));
    }

    @Test
    public void obtenerReporte7A() {
        //when
        lenient().doReturn(new ArrayList())
                .when(plantilla).queryForList(
                anyString(),
                any(MapSqlParameterSource.class));
        //then
        assertNotNull(jdbcRepository.obtenerReporte7A(repartoTipo, periodo));
    }

    @Test
    public void obtenerReporte7B() {
        //when
        lenient().doReturn(new ArrayList())
                .when(plantilla).queryForList(
                anyString(),
                any(MapSqlParameterSource.class));
        //then
        assertNotNull(jdbcRepository.obtenerReporte7B(repartoTipo, periodo));
    }

    @Test
    public void obtenerReporte8() {
        //when
        lenient().doReturn(new ArrayList())
                .when(plantilla).queryForList(
                anyString(),
                any(MapSqlParameterSource.class));
        //then
        assertNotNull(jdbcRepository.obtenerReporte8(repartoTipo, periodo));
    }

    @Test
    public void obtenerFiltroCodLineas() {
        //when
        doReturn(Arrays.asList(filtroLinea_1,filtroLinea_2,filtroLinea_3))
                .when(jdbcTemplate).query(anyString(),
                any(BeanPropertyRowMapper.class), any(), any());

        //then
        assertEquals(3,jdbcRepository.obtenerFiltroCodLineas(repartoTipo, periodo).size());
        assertEquals(filtroLinea_1, jdbcRepository.obtenerFiltroCodLineas(repartoTipo, periodo).get(0));
    }

    @Test
    public void validarRepo5() {
        // When
        lenient().when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class),
          anyInt())).thenReturn(0);
        // Then
        assertEquals(false, jdbcRepository.validarRepo5(repartoTipo, periodo, lineas, canales));
    }

    @Test
    public void validarRepo5Complete() {
        int repartoTipo = 2;
        List<String> dimensiones = new ArrayList(Arrays.asList("CUENTA CONTABLE INICIAL",
          "PARTIDA INICIAL", "CENTRO INICIAL"));
        List<String> lineas = new ArrayList(Arrays.asList("test"));
        List<String> canales = new ArrayList(Arrays.asList("test"));
        // When
        lenient().when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class),
          anyInt())).thenReturn(0);
        // Then
        assertEquals(false, jdbcRepository.validarRepo5(repartoTipo, periodo, lineas, canales));
    }
}
