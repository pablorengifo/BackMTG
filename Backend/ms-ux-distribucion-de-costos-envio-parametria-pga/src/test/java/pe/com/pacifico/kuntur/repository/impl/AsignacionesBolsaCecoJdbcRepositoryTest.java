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
import pe.com.pacifico.kuntur.model.AsignacionCecoBolsa;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovDriverCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AsignacionesBolsaCecoJdbcRepositoryTest {
  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private AsignacionCecoBolsaJdbcRepository jdbcRepository;

  //Given
  int repartoTipo=1;
  int repartoTipo2=2;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  AsignacionCecoBolsa acb1 = new AsignacionCecoBolsa();
  AsignacionCecoBolsa acb2 = new AsignacionCecoBolsa();
  AsignacionCecoBolsa acb3 = new AsignacionCecoBolsa();
  MovDriverCentro driver4 = new MovDriverCentro();
  MovDriverCentro driver5 = new MovDriverCentro();
  MovDriverCentro driver6 = new MovDriverCentro();
  String codigo="any";

  @Test
  void shouldfindAllAccounts()
  {
    //when
    doReturn(Arrays.asList(acb1,acb2,acb3))
        .when(jdbcTemplate).query(ArgumentMatchers.anyString(),
        any(BeanPropertyRowMapper.class),anyString(),anyInt(),anyInt(),anyInt(),anyInt(),anyInt());

    //then
    assertEquals(3,jdbcRepository.findAll(repartoTipo,periodo).size());
    assertEquals(acb1, jdbcRepository.findAll(repartoTipo,periodo).get(0));
  }

  @Test
  void shoulNotFindAllAccounts()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(ArgumentMatchers.anyString(),
        any(BeanPropertyRowMapper.class),anyString(),anyInt(),anyInt(),anyInt(),anyInt(),anyInt());

    //then
    assertEquals(0,jdbcRepository.findAll(repartoTipo2,periodo).size());
  }

  @Test
  public void shouldFindAllCenterCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyInt()))
        .thenReturn(Arrays.asList("Codigo1","Codigo2"));

    //then
    assertThat(jdbcRepository.findAllCodCentro(repartoTipo,periodo),notNullValue());
    assertThat(jdbcRepository.findAllCodCentro(repartoTipo,periodo).get(0),equalToIgnoringCase("Codigo1"));
  }

  @Test
  public void shouldNotFindAllCenterCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyString()))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertEquals(0,jdbcRepository.findAllCodCentro(repartoTipo2,periodo).size());
  }

  @Test
  public void shouldFindAllItemCodes()
  {
    //when

    when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyInt()))
        .thenReturn(Arrays.asList("Codigo1","Codigo2"));
    //then
    assertThat(jdbcRepository.findAllCodPartida(repartoTipo,periodo),notNullValue());
    assertThat(jdbcRepository.findAllCodPartida(repartoTipo,periodo).get(0),equalToIgnoringCase("Codigo1"));
  }

  @Test
  public void shouldNotFindAllItemCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyString()))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertEquals(0,jdbcRepository.findAllCodPartida(repartoTipo2,periodo).size());
  }

  @Test
  public void shouldFindAllAcountsCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyInt()))
        .thenReturn(Arrays.asList("Codigo1","Codigo2"));

    //then
    assertThat(jdbcRepository.findAllCodCuentaContable(repartoTipo,periodo),notNullValue());
    assertThat(jdbcRepository.findAllCodCuentaContable(repartoTipo,periodo).get(0),equalToIgnoringCase("Codigo1"));
  }

  @Test
  public void shouldNotFindAllAcountsCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyString()))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertEquals(0,jdbcRepository.findAllCodCuentaContable(repartoTipo2,periodo).size());
  }

  @Test
  public void shouldFindAllDriverCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyInt()))
        .thenReturn(Arrays.asList("Codigo1","Codigo2"));

    //then
    assertThat(jdbcRepository.findAllCodDriver(repartoTipo,periodo),notNullValue());
    assertThat(jdbcRepository.findAllCodDriver(repartoTipo,periodo).get(0),equalToIgnoringCase("Codigo1"));
  }

  @Test
  public void shouldNotFindAllDriverCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyString()))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertEquals(0,jdbcRepository.findAllCodDriver(repartoTipo2,periodo).size());
  }

  @Test
  public void shouldSaveExcelReal()
  {
    //given
    List<List<CellData>> excel= llenarExcelBien();
    String deleteQuery = "DELETE FROM CUENTA_PARTIDA_CENTRO_DRIVER_CENTRO WHERE RepartoTipo=? and periodo =?";

    //when
    when(jdbcRepository.findAllCodCuentaContable(repartoTipo,periodo))
        .thenReturn(Collections.singletonList("CODIGO CUENTA CONTABLE"));
    when(jdbcRepository.findAllCodPartida(repartoTipo,periodo))
        .thenReturn(Collections.singletonList("CODIGO PARTIDA"));
    when(jdbcRepository.findAllCodCentro(repartoTipo,periodo))
        .thenReturn(Collections.singletonList("CODIGO CENTRO"));
    when(jdbcRepository.findAllCodDriver(repartoTipo,periodo))
        .thenReturn(Collections.singletonList("CODIGO DRIVER"));
    when(jdbcTemplate.update(anyString(), anyInt(), anyInt())).thenReturn(1);
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
    when(jdbcRepository.findAllCodCuentaContable(repartoTipo,periodo))
        .thenReturn(Collections.singletonList("CODIGO CUENTA CONTABLE"));
    when(jdbcRepository.findAllCodPartida(repartoTipo,periodo))
        .thenReturn(Collections.singletonList("CODIGO PARTIDA"));
    when(jdbcRepository.findAllCodCentro(repartoTipo,periodo))
        .thenReturn(Collections.singletonList("CODIGO CENTRO"));
    when(jdbcRepository.findAllCodDriver(repartoTipo,periodo))
        .thenReturn(Collections.singletonList("CODIGO DRIVER"));
    when(jdbcTemplate.update(anyString(), anyInt(), anyInt())).thenReturn(1);
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
    assertEquals(5,jdbcRepository.saveExcelToBd(excel,periodo,repartoTipo).size());
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


  private  List<List<CellData>> llenarExcelBien()
  {
    List<List<CellData>> excel = new ArrayList<>();
    List<CellData> fila1 = new ArrayList<>();
    List<CellData> fila3 = new ArrayList<>();

    CellData a1 = new CellData();
    a1.setValue("CODIGO CUENTA CONTABLE");
    fila1.add(a1);
    fila1.add(new CellData());

    CellData c1 = new CellData();
    c1.setValue("CODIGO PARTIDA");
    fila1.add(c1);
    fila1.add(new CellData());

    CellData e1 = new CellData();
    e1.setValue("CODIGO CENTRO");
    fila1.add(e1);
    fila1.add(new CellData());

    CellData g1 = new CellData();
    g1.setValue("CODIGO DRIVER");
    fila1.add(g1);

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
