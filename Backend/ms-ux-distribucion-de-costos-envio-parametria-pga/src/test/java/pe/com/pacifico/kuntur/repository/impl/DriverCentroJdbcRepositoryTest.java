package pe.com.pacifico.kuntur.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pe.com.pacifico.kuntur.expose.request.DriverAsignacionesRequest;
import pe.com.pacifico.kuntur.expose.request.DriverCentroRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaDriver;
import pe.com.pacifico.kuntur.model.MovDriverCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DriverCentroJdbcRepositoryTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private DriverCentroJdbcRepository jdbcRepository;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaDriver driver1 = new MaDriver();
  MaDriver driver2 = new MaDriver();
  MaDriver driver3 = new MaDriver();
  MovDriverCentro driver4 = new MovDriverCentro();
  MovDriverCentro driver5 = new MovDriverCentro();
  MovDriverCentro driver6 = new MovDriverCentro();
  DriverCentroRequest request = new DriverCentroRequest();
  String codigo="any";

  @Test
  void shouldfindAllAccounts()
  {
    //when
    doReturn(Arrays.asList(driver1,driver2,driver3))
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt());

    //then
    assertEquals(3,jdbcRepository.findAll(repartoTipo,periodo).size());
    assertEquals(driver1, jdbcRepository.findAll(repartoTipo,periodo).get(0));
  }

  @Test
  void shouldNotFindAllAccounts()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt());

    //then
    assertEquals(0,jdbcRepository.findAll(repartoTipo,periodo).size());
  }

  @Test
  void shouldfindAllMovAccounts()
  {
    //when
    doReturn(Arrays.asList(driver4,driver5,driver6))
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt(),anyString());

    //then
    assertEquals(3,jdbcRepository.findAllMov(repartoTipo,periodo,codigo).size());
    assertEquals(driver4, jdbcRepository.findAllMov(repartoTipo,periodo,codigo).get(0));
  }

  @Test
  void shouldNotFindAllMovAccounts()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt(),anyString());

    //then
    assertEquals(0,jdbcRepository.findAllMov(repartoTipo,periodo,codigo).size());
  }

  @Test
  void shouldListAllMovCentros()
  {
    //when
    doReturn(Arrays.asList(driver4,driver5,driver6))
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt());

    //then
    assertEquals(3,jdbcRepository.findAllMov(repartoTipo,periodo).size());
    assertEquals(driver4, jdbcRepository.findAllMov(repartoTipo,periodo).get(0));
  }

  @Test
  void shouldNotListAllMovCentros()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt());

    //then
    assertEquals(0,jdbcRepository.findAllMov(repartoTipo,periodo).size());
  }

  @Test
  void shouldSaveAnAccount()
  {
    //given
    DriverAsignacionesRequest driverAsignacionesRequest = new DriverAsignacionesRequest();
    driverAsignacionesRequest.setCodCentroDestino(codigo);
    driverAsignacionesRequest.setNombre("nombre");
    driverAsignacionesRequest.setPorcentaje(100);
    request.setMovDrivers(new ArrayList<>());
    request.getMovDrivers().add(driverAsignacionesRequest);

    //when
    when(jdbcTemplate.update(anyString(),
        any(),any(),any(),any(),any()))
        .thenReturn(1);
    when(jdbcTemplate.update(anyString(),
        any(),any(),any(),any(),any(),any(),any()))
        .thenReturn(1);
    //then
    assertEquals(jdbcRepository.save(request),1);
  }

  @Test
  void shouldNotSaveAnAccountByRepeatedKey()
  {
    //given
    request.setMovDrivers(new ArrayList<>());

    //when
    when(jdbcTemplate.update(anyString(),
        any(),any(),any(),any(),any()))
        .thenThrow(DuplicateKeyException.class);
    //then
    assertEquals(jdbcRepository.save(request),-1);
  }

  @Test
  void shouldNotSaveAnAccountByException()
  {
    //given
    request.setMovDrivers(new ArrayList<>());

    //when
    when(jdbcTemplate.update(anyString(),
        any(),any(),any(),any(),any()))
        .thenThrow(NullPointerException.class);
    //then
    assertEquals(jdbcRepository.save(request),-2);
  }

  @Test
  void shouldUpdateAnAccount()
  {
    //given
    DriverAsignacionesRequest driverAsignacionesRequest = new DriverAsignacionesRequest();
    driverAsignacionesRequest.setCodCentroDestino(codigo);
    driverAsignacionesRequest.setNombre("nombre");
    driverAsignacionesRequest.setPorcentaje(100);
    request.setMovDrivers(new ArrayList<>());
    request.getMovDrivers().add(driverAsignacionesRequest);

    //when
    doReturn(1).when(jdbcTemplate).update(anyString(),
        any(),any(),any());
    doReturn(1).when(jdbcTemplate).update(anyString(),
        any(),any(),any(),any(),any(),any(),any());

    //then
    assertEquals(jdbcRepository.update(request),1);
  }

  @Test
  void shouldNotUpdateAnAccountByRepeatedKey()
  {
    //given
    request.setMovDrivers(new ArrayList<>());

    //when
    when(jdbcTemplate.update(anyString(),
        any(),any(),any()))
        .thenThrow(DuplicateKeyException.class);
    //then
    assertEquals(jdbcRepository.update(request),-1);
  }

  @Test
  void shouldNotUpdateAnAccountByException()
  {
    //given
    request.setMovDrivers(new ArrayList<>());

    //when
    when(jdbcTemplate.update(anyString(),
        any(),any(),any(),any(),any()))
        .thenThrow(NullPointerException.class);
    //then
    assertEquals(jdbcRepository.update(request),-2);
  }

  @Test
  @DisplayName("debería eliminar una Linea") //Para descripcion en el reporte
  void shouldDeleteAnAccount()
  {
    //then
    assertTrue(jdbcRepository.delete(repartoTipo,periodo, codigo));
  }

  @Test
  void shouldNotDeleteAnAccount()
  {
    //when
    when(jdbcTemplate.update(anyString(), anyString(), anyInt(), anyInt()))
        .thenThrow(NullPointerException.class);
    //then
    assertFalse(jdbcRepository.delete(repartoTipo,periodo, codigo));
  }

  @Test
  public void shouldFindAllDriverCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
        .thenReturn(Arrays.asList("Codigo1","Codigo2"));

    //then
    assertThat(jdbcRepository.findAllCodDriver(repartoTipo),notNullValue());
    assertThat(jdbcRepository.findAllCodDriver(repartoTipo).get(0),equalToIgnoringCase("Codigo1"));
  }

  @Test
  public void shouldNotFindAllDriverCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertEquals(0,jdbcRepository.findAllCodDriver(repartoTipo).size());
  }

  @Test
  public void shouldFindAllCenterCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
        .thenReturn(Arrays.asList("Codigo1","Codigo2"));

    //then
    assertThat(jdbcRepository.findAllCodCentro(repartoTipo),notNullValue());
    assertThat(jdbcRepository.findAllCodCentro(repartoTipo).get(0),equalToIgnoringCase("Codigo1"));
  }

  @Test
  public void shouldNotFindAllCenterCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertEquals(0,jdbcRepository.findAllCodCentro(repartoTipo).size());
  }

  @Test
  void shouldfindAnAccount()
  {
    //when
    when(jdbcTemplate.queryForObject(anyString(),any(),
        any(BeanPropertyRowMapper.class)))
        .thenReturn(driver4);

    //then
    assertThat(jdbcRepository.findByCodDriver(codigo),notNullValue());
    assertThat(jdbcRepository.findByCodDriver(codigo),equalTo(driver4));
  }

  @Test
  void shouldNotfindAnAccount()
  {
    //when
    when(jdbcTemplate.queryForObject(anyString(),any(),
        any(BeanPropertyRowMapper.class)))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertNull(jdbcRepository.findByCodDriver(codigo));
  }

  @Test
  public void shouldSaveExcelMa()
  {
    //given
    List<List<CellData>> excel= llenarExcelMaBien();

    //When
    when(jdbcRepository.findAllCodDriver(repartoTipo))
        .thenReturn(Collections.singletonList("CODIGO"));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).thenReturn(null);

    //Then
   assertNull(jdbcRepository.saveExcelToBdMa(excel,repartoTipo,periodo));
  }

  @Test
  public void shouldNotSaveExcelRealByErrorInDB()
  {
    //given
    List<List<CellData>> excel= llenarExcelMaBien();

    //When
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class)))
        .thenThrow(NullPointerException.class);

    //Then
    assertEquals(2,jdbcRepository.saveExcelToBdMa(excel,periodo,repartoTipo).size());
  }


  @Test
  public void shouldNotSaveExcelMaByWrongHeaders()
  {
    //Given
    List<List<CellData>> excel = llenarExcelMaMal();

    //then
    assertNotNull(jdbcRepository.saveExcelToBdMa(excel,periodo,repartoTipo));
    excel = llenarExcelMaMal();
    assertEquals(1,jdbcRepository.saveExcelToBdMa(excel,periodo,repartoTipo).size());
  }

  @Test
  public void shouldSaveExcelMov()
  {
    //given
    List<List<CellData>> excel= llenarExcelMovBien();

    //when
    when(jdbcRepository.findAllCodDriver(repartoTipo))
        .thenReturn(Collections.singletonList("codigo"));
    when(jdbcRepository.findAllCodCentro(repartoTipo))
        .thenReturn(Collections.singletonList("codigo centro"));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).thenReturn(null);

    assertEquals(3,jdbcRepository.saveExcelToBdMov(excel,repartoTipo,periodo).size());
  }

  @Test
  public void shouldNotSaveExcelMovByWrongData()
  {
    //given
    List<List<CellData>> excel= llenarExcelMovBien();
    excel.get(1).get(4).setValue("1");
    //when
    when(jdbcRepository.findAllCodDriver(repartoTipo))
        .thenReturn(Collections.singletonList("codigo"));
    when(jdbcRepository.findAllCodCentro(repartoTipo))
        .thenReturn(Collections.singletonList("codigo centro"));

    assertEquals(4,jdbcRepository.saveExcelToBdMov(excel,repartoTipo,periodo).size());
  }

  @Test
  public void shouldNotSaveExcelMovByErrorInDB()
  {
    //given
    List<List<CellData>> excel= llenarExcelMovBien();

    //when
    when(jdbcRepository.findAllCodDriver(repartoTipo))
        .thenReturn(Collections.singletonList("codigo"));
    when(jdbcRepository.findAllCodCentro(repartoTipo))
        .thenReturn(Collections.singletonList("codigo centro"));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class)))
        .thenThrow(NullPointerException.class);

    assertEquals(4,jdbcRepository.saveExcelToBdMov(excel,repartoTipo,periodo).size());
  }

  @Test
  public void shouldNotSaveExcelMovByWrongHeaders()
  {
    //Given
    List<List<CellData>> excel = llenarExcelMovMal();

    //then
    assertNotNull(jdbcRepository.saveExcelToBdMov(excel,periodo,repartoTipo));
    excel = llenarExcelMovMal();
    assertEquals(1,jdbcRepository.saveExcelToBdMov(excel,periodo,repartoTipo).size());
  }

  private  List<List<CellData>> llenarExcelMaBien()
  {
    List<List<CellData>> excel = new ArrayList<>();
    List<CellData> fila1 = new ArrayList<>();
    List<CellData> fila2 = new ArrayList<>();
    List<CellData> fila3 = new ArrayList<>();

    CellData a1 = new CellData();
    a1.setValue("CODIGO");
    fila1.add(a1);

    CellData b1 = new CellData();
    b1.setValue("NOMBRE");
    fila1.add(b1);

    CellData c1 = new CellData();
    c1.setValue("TIPO");
    fila1.add(c1);

    CellData d1 = new CellData();
    d1.setValue("si");
    fila1.add(d1);

    CellData a2 = new CellData();
    a2.setValue("CODIGO");
    fila2.add(a2);

    CellData b2 = new CellData();
    b2.setValue("NOMBRE");
    fila2.add(b2);

    CellData c2 = new CellData();
    c2.setValue("CENTROS DE COSTOS");
    fila2.add(c2);

    CellData d2 = new CellData();
    d2.setValue("si");
    fila2.add(d2);

    CellData a3 = new CellData();
    a3.setValue("");
    fila3.add(a3);

    CellData b3 = new CellData();
    b3.setValue("");
    fila3.add(b3);

    CellData c3 = new CellData();
    c3.setValue("CENTROS DE COSTOS");
    fila3.add(c3);

    CellData d3 = new CellData();
    d3.setValue("si");
    fila3.add(d3);

    excel.add(fila1);
    excel.add(fila2);
    excel.add(fila3);

    return excel;
  }

  private  List<List<CellData>> llenarExcelMaMal()
  {
    List<List<CellData>> excel = new ArrayList<>();
    List<CellData> fila1 = new ArrayList<>();

    CellData a1 = new CellData();
    a1.setValue("Modelo de costos");
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
    excel.add(fila1);
    excel.add(fila1);
    excel.add(fila1);
    excel.add(fila1);

    return excel;
  }

  private  List<List<CellData>> llenarExcelMovBien()
  {
    List<List<CellData>> excel = new ArrayList<>();
    List<CellData> fila1 = new ArrayList<>();
    List<CellData> fila2 = new ArrayList<>();
    List<CellData> fila3 = new ArrayList<>();

    CellData a1 = new CellData();
    a1.setValue("codigo");
    fila1.add(a1);
    fila1.add(new CellData());

    CellData c1 = new CellData();
    c1.setValue("codigo centro");
    fila1.add(c1);
    fila1.add(new CellData());

    CellData e1 = new CellData();
    e1.setValue("porcentaje");
    fila1.add(e1);

    CellData a2 = new CellData();
    a2.setValue("codigo");
    fila2.add(a2);
    fila2.add(new CellData());

    CellData c2 = new CellData();
    c2.setValue("codigo centro");
    fila2.add(c2);
    fila2.add(new CellData());

    CellData e2 = new CellData();
    e2.setValue("100");
    fila2.add(e2);

    CellData a3 = new CellData();
    a3.setValue("");
    fila3.add(a3);
    fila2.add(new CellData());

    CellData b3 = new CellData();
    b3.setValue("");
    fila3.add(b3);
    fila2.add(new CellData());

    CellData c3 = new CellData();
    c3.setValue("");
    fila3.add(c3);

    excel.add(fila1);
    excel.add(fila2);
    excel.add(fila3);

    return excel;
  }

  private  List<List<CellData>> llenarExcelMovMal()
  {
    List<List<CellData>> excel = new ArrayList<>();
    List<CellData> fila1 = new ArrayList<>();

    CellData a1 = new CellData();
    a1.setValue("Modelo de costos");
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

    CellData f1 = new CellData();
    f1.setValue("");
    fila1.add(f1);

    excel.add(fila1);
    excel.add(fila1);
    excel.add(fila1);
    excel.add(fila1);
    excel.add(fila1);
    excel.add(fila1);

    return excel;
  }

}
