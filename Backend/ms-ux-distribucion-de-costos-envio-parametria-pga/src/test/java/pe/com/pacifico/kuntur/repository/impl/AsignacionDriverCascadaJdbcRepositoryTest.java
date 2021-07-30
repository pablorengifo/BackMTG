package pe.com.pacifico.kuntur.repository.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverCascadaRequest;
import pe.com.pacifico.kuntur.expose.request.MaCentroRequest;
import pe.com.pacifico.kuntur.model.AsignacionDriverCascada;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovDriverCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AsignacionDriverCascadaJdbcRepositoryTest {
  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private AsignacionDriverCascadaJdbcRepository jdbcRepository;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  String codDriver = "any";
  String codDriverCentro = "any";

  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  AsignacionDriverCascada acb1 = new AsignacionDriverCascada();
  AsignacionDriverCascada acb2 = new AsignacionDriverCascada();
  AsignacionDriverCascada acb3 = new AsignacionDriverCascada();
  MovDriverCentro mdc1 = new MovDriverCentro();
  MovDriverCentro mdc2 = new MovDriverCentro();
  MovDriverCentro mdc3 = new MovDriverCentro();
  AsignacionDriverCascadaRequest request1 = new AsignacionDriverCascadaRequest();

  @Test
  public void shouldFindAll() {
    //when
    doReturn(Arrays.asList(acb1,acb2,acb3))
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyString(),anyInt());

    //then
    assertEquals(3,jdbcRepository.findAll(repartoTipo,periodo).size());
    assertEquals(acb1, jdbcRepository.findAll(repartoTipo,periodo).get(0));
  }

  @Test
  void shoulNotFindAll()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyString(),anyInt());

    //then
    assertEquals(0,jdbcRepository.findAll(2,periodo).size());
  }


  @Test
  public void shouldSaveExcelReal()
  {
    //given
    List<List<CellData>> excel= llenarExcelBien();
    String deleteQuery = "DELETE FROM CUENTA_PARTIDA_CENTRO_DRIVER_CENTRO WHERE RepartoTipo=? and periodo =?";

    //when
    when(jdbcTemplate.update(anyString(), anyInt(), anyInt())).thenReturn(1);
    when(jdbcTemplate.queryForList(anyString(), eq(String.class), anyString(), anyInt()))
        .thenReturn(Arrays.asList("CODIGO DRIVER"));
    when(jdbcTemplate.queryForList(anyString(), eq(String.class), anyInt(), anyString()))
        .thenReturn(Arrays.asList("CODIGO CENTRO"));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).thenReturn(null);

    //then
    assertEquals(2,jdbcRepository.saveExcelToBd(excel,periodo,repartoTipo).size());
  }

  @Test
  public void shouldNotSaveExcelRealByErrorInDB()
  {
    //given
    List<List<CellData>> excel= llenarExcelBien();
    String deleteQuery = "DELETE FROM CUENTA_PARTIDA_CENTRO_DRIVER_CENTRO WHERE RepartoTipo=? and periodo =?";

    //when
    when(jdbcTemplate.update(anyString(), anyInt(), anyInt())).thenReturn(1);
    when(jdbcTemplate.queryForList(anyString(), eq(String.class), anyString(), anyInt()))
        .thenReturn(Arrays.asList("CODIGO DRIVER"));
    when(jdbcTemplate.queryForList(anyString(), eq(String.class), anyInt(), anyString()))
        .thenReturn(Arrays.asList("CODIGO CENTRO"));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class)))
        .thenThrow(NullPointerException.class);

    //then
    assertEquals(3,jdbcRepository.saveExcelToBd(excel,periodo,repartoTipo).size());
  }

  @Test
  public void shouldNotSaveExcelRealByWrongHeaders()
  {
    //Given
    List<List<CellData>> excel = llenarExcelMal();

    //then
    assertNotNull(jdbcRepository.saveExcelToBd(excel,periodo,repartoTipo));
  }

  @Test
  public void shouldReportByCodDriver() {
    doReturn(Arrays.asList(mdc1,mdc2,mdc3))
        .when(jdbcTemplate).query(ArgumentMatchers.anyString(),
        any(BeanPropertyRowMapper.class),anyString(),anyInt(),anyInt());

    //then
    assertEquals(3,jdbcRepository.reportByCodDriver(repartoTipo,periodo, codDriver).size());
    assertEquals(mdc1, jdbcRepository.reportByCodDriver(repartoTipo,periodo, codDriver).get(0));
  }
  @Test
  void shouldNotReportByCodDriver()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(ArgumentMatchers.anyString(),
        any(BeanPropertyRowMapper.class),anyString(),anyInt(),anyInt());

    //then
    assertEquals(0,jdbcRepository.reportByCodDriver(repartoTipo,periodo, codDriver).size());
  }

  @Test
  public void shouldSave() {
    //when
    when(jdbcTemplate.update(anyString(),
        any(),any(),any(),any(),any(),any()))
        .thenReturn(1);

    //then
    assertEquals(jdbcRepository.save(request1),1);
  }

  @Test
  public void shouldDelete() {
    //when
    when(jdbcTemplate.update(anyString(),
        anyInt(),anyString(),anyInt()))
        .thenReturn(1);

    //then
    assertEquals(jdbcRepository.delete(repartoTipo,periodo,codDriverCentro),true);
  }

  @Test
  public void shouldNotDelete() {
    //when
    when(jdbcTemplate.update(anyString(),
        anyInt(),anyString(),anyInt()))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertEquals(jdbcRepository.delete(repartoTipo,periodo,codDriverCentro),false);
  }

  private  List<List<CellData>> llenarExcelBien()
  {
    List<List<CellData>> excel = new ArrayList<>();
    List<CellData> fila1 = new ArrayList<>();
    List<CellData> fila3 = new ArrayList<>();

    CellData a1 = new CellData();
    a1.setValue("CODIGO CENTRO");
    fila1.add(a1);

    CellData b1 = new CellData();
    b1.setValue("NOMBRE CENTRO");
    fila1.add(b1);

    CellData c1 = new CellData();
    c1.setValue("CODIGO DRIVER");
    fila1.add(c1);

    CellData d1 = new CellData();
    d1.setValue("NOMBRE DRIVER");
    fila1.add(d1);

    CellData a3 = new CellData();
    a3.setValue("");
    fila3.add(a3);
    fila3.add(new CellData());

    CellData c3 = new CellData();
    c3.setValue("");
    fila3.add(c3);
    fila3.add(new CellData());

    CellData e3 = new CellData();
    e3.setValue("");
    fila3.add(e3);
    fila3.add(new CellData());

    CellData g3 = new CellData();
    g3.setValue("");
    fila3.add(g3);

    excel.add(fila1);
    excel.add(fila1);
    excel.add(fila3);

    return excel;
  }

  private  List<List<CellData>> llenarExcelMal()
  {
    List<List<CellData>> excel = new ArrayList<>();
    List<CellData> fila1 = new ArrayList<>();

    CellData a1 = new CellData();
    a1.setValue("");
    fila1.add(a1);
    fila1.add(new CellData());

    CellData c1 = new CellData();
    c1.setValue("");
    fila1.add(c1);
    fila1.add(new CellData());

    CellData e1 = new CellData();
    e1.setValue("");
    fila1.add(e1);
    fila1.add(new CellData());

    CellData g1 = new CellData();
    g1.setValue("");
    fila1.add(g1);

    excel.add(fila1);
    excel.add(fila1);

    return excel;
  }
}
