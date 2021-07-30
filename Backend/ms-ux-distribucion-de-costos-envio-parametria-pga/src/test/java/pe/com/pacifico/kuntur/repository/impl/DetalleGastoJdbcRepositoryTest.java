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
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.DetalleGasto;
import pe.com.pacifico.kuntur.model.RequestFileRead;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DetalleGastoJdbcRepositoryTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private DetalleGastoJdbcRepository jdbcRepository;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  DetalleGasto detalleGasto1 = new DetalleGasto();
  DetalleGasto detalleGasto2 = new DetalleGasto();
  DetalleGasto detalleGasto3 = new DetalleGasto();

  @Test
  void shouldfindAllAccounts()
  {
    //when
    doReturn(Arrays.asList(detalleGasto1,detalleGasto2,detalleGasto3))
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyString(),anyInt(),anyInt(),anyInt());

    //then
    assertEquals(3,jdbcRepository.findAll(repartoTipo,periodo).size());
    assertEquals(detalleGasto1, jdbcRepository.findAll(repartoTipo,periodo).get(0));
  }

  @Test
  void shouldNotFindAllAccounts()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyString(),anyInt(),anyInt(),anyInt());

    //then
    assertEquals(0,jdbcRepository.findAll(2,periodo).size());
  }

  @Test
  public void shouldFindAllAcountCodes()
  {
    //when
    if(repartoTipo==1)
    {
      when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
          .thenReturn(Arrays.asList("Codigo1","Codigo2"));
    }
    else
    {
      when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyInt()))
          .thenReturn(Arrays.asList("Codigo1","Codigo2"));
    }

    //then
    assertThat(jdbcRepository.findAllCodCentro(repartoTipo,periodo),notNullValue());
    assertThat(jdbcRepository.findAllCodCentro(repartoTipo,periodo).get(0),equalToIgnoringCase("Codigo1"));
  }

  @Test
  public void shouldNotFindAllAcountCodes()
  {
    //given
    repartoTipo=2;

    //when
    if(repartoTipo==1)
    {
      when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
          .thenThrow(EmptyResultDataAccessException.class);
    }
    else
    {
      when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyInt()))
          .thenThrow(EmptyResultDataAccessException.class);
    }

    //then
    assertEquals(0,jdbcRepository.findAllCodCentro(repartoTipo,periodo).size());
  }

  @Test
  public void shouldFindAllItemCodes()
  {
    //when
    if(repartoTipo==1)
    {
      when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
          .thenReturn(Arrays.asList("Codigo1","Codigo2"));
    }
    else
    {
      when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyInt()))
          .thenReturn(Arrays.asList("Codigo1","Codigo2"));
    }

    //then
    assertThat(jdbcRepository.findAllCodPartida(repartoTipo,periodo),notNullValue());
    assertThat(jdbcRepository.findAllCodPartida(repartoTipo,periodo).get(0),equalToIgnoringCase("Codigo1"));
  }

  @Test
  public void shouldNotFindAllItemCodes()
  {
    //given
    repartoTipo=2;

    //when
    if(repartoTipo==1)
    {
      when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
          .thenThrow(EmptyResultDataAccessException.class);
    }
    else
    {
      when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyInt()))
          .thenThrow(EmptyResultDataAccessException.class);
    }

    //then
    assertEquals(0,jdbcRepository.findAllCodPartida(repartoTipo,periodo).size());
  }

  @Test
  public void shouldFindAllCenterCodes()
  {
    //when
    if(repartoTipo==1)
    {
      when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
          .thenReturn(Arrays.asList("Codigo1","Codigo2"));
    }
    else
    {
      when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyInt()))
          .thenReturn(Arrays.asList("Codigo1","Codigo2"));
    }

    //then
    assertThat(jdbcRepository.findAllCodCuentaContable(repartoTipo,periodo),notNullValue());
    assertThat(jdbcRepository.findAllCodCuentaContable(repartoTipo,periodo).get(0),equalToIgnoringCase("Codigo1"));
  }

  @Test
  public void shouldNotFindAllCenterCodes()
  {
    //given
    repartoTipo=2;

    //when
    if(repartoTipo==1)
    {
      when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
          .thenThrow(EmptyResultDataAccessException.class);
    }
    else
    {
      when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyInt()))
          .thenThrow(EmptyResultDataAccessException.class);
    }

    //then
    assertEquals(0,jdbcRepository.findAllCodCuentaContable(repartoTipo,periodo).size());
  }

  @Test
  public void shouldSaveExcelReal()
  {
    //given
    List<List<CellData>> excel= llenarExcelBien();

    //when
    when(jdbcRepository.findAllCodCuentaContable(1,periodo))
        .thenReturn(Arrays.asList("codigo cuenta contable"));
    when(jdbcRepository.findAllCodPartida(1,periodo))
        .thenReturn(Arrays.asList("codigo partida"));
    when(jdbcRepository.findAllCodCentro(1,periodo))
        .thenReturn(Arrays.asList("codigo centro"));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).thenReturn(null);

    //then
    assertEquals(4,jdbcRepository.saveExcelToBdReal(excel,periodo).size());
  }

  @Test
  public void shouldNotSaveExcelRealByWrongData()
  {
    //given
    List<List<CellData>> excel= llenarExcelBien();
    excel.get(1).get(6).setValue("no numero");
    //when
    when(jdbcRepository.findAllCodCuentaContable(1,periodo))
        .thenReturn(Arrays.asList("codigo cuenta contable"));
    when(jdbcRepository.findAllCodPartida(1,periodo))
        .thenReturn(Arrays.asList("codigo partida"));
    when(jdbcRepository.findAllCodCentro(1,periodo))
        .thenReturn(Arrays.asList("codigo centro"));

    //when

    //then
    assertEquals(5,jdbcRepository.saveExcelToBdReal(excel,periodo).size());
  }

  @Test
  public void shouldNotSaveExcelRealByErrorInDB()
  {
    //given
    List<List<CellData>> excel= llenarExcelBien();

    //when
    when(jdbcRepository.findAllCodCuentaContable(1,periodo))
        .thenReturn(Arrays.asList("codigo cuenta contable"));
    when(jdbcRepository.findAllCodPartida(1,periodo))
        .thenReturn(Arrays.asList("codigo partida"));
    when(jdbcRepository.findAllCodCentro(1,periodo))
        .thenReturn(Arrays.asList("codigo centro"));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).
        thenThrow(NullPointerException.class);

    //then
    assertEquals(5,jdbcRepository.saveExcelToBdReal(excel,periodo).size());
  }

  @Test
  public void shouldNotSaveExcelReal()
  {
    //Given
    List<List<CellData>> excel = llenarExcelMal();

    //then
    assertNotNull(jdbcRepository.saveExcelToBdReal(excel,periodo));
  }

  @Test
  public void shouldSaveExcelBudget()
  {
    //given
    List<List<CellData>> excel= llenarExcelBienBudget();

    //when
    when(jdbcRepository.findAllCodCuentaContable(2,periodo))
        .thenReturn(Arrays.asList("codigo cuenta contable"));
    when(jdbcRepository.findAllCodPartida(2,periodo))
        .thenReturn(Arrays.asList("codigo partida"));
    when(jdbcRepository.findAllCodCentro(2,periodo))
        .thenReturn(Arrays.asList("codigo centro"));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).thenReturn(null);

    //then
    assertEquals(4,jdbcRepository.saveExcelToBdPresupuesto(excel,periodo).size());
  }

  @Test
  public void shouldNotSaveExcelBudgetByWrongData()
  {
    //given
    List<List<CellData>> excel= llenarExcelBienBudget();
    excel.get(1).get(9).setValue("no numero");
    //when
    when(jdbcRepository.findAllCodCuentaContable(2,periodo))
        .thenReturn(Arrays.asList("codigo cuenta contable"));
    when(jdbcRepository.findAllCodPartida(2,periodo))
        .thenReturn(Arrays.asList("codigo partida"));
    when(jdbcRepository.findAllCodCentro(2,periodo))
        .thenReturn(Arrays.asList("codigo centro"));
    //then
    assertEquals(5,jdbcRepository.saveExcelToBdPresupuesto(excel,periodo).size());
  }

  @Test
  public void shouldNotSaveExcelBudgetByErrorInDB()
  {
    //given
    List<List<CellData>> excel= llenarExcelBienBudget();

    //when
    when(jdbcRepository.findAllCodCuentaContable(2,periodo))
        .thenReturn(Arrays.asList("codigo cuenta contable"));
    when(jdbcRepository.findAllCodPartida(2,periodo))
        .thenReturn(Arrays.asList("codigo partida"));
    when(jdbcRepository.findAllCodCentro(2,periodo))
        .thenReturn(Arrays.asList("codigo centro"));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class)))
        .thenThrow(NullPointerException.class);

    //then
    assertEquals(16,jdbcRepository.saveExcelToBdPresupuesto(excel,periodo).size());
  }

  @Test
  public void shouldNotSaveExcelBudget()
  {
    //Given
    List<List<CellData>> excel = llenarExcelMal();

    //then
    assertNotNull(jdbcRepository.saveExcelToBdPresupuesto(excel,periodo));
  }


  private  List<List<CellData>> llenarExcelBien()
  {
    List<List<CellData>> excel = new ArrayList<>();
    List<CellData> fila1 = new ArrayList<>();
    List<CellData> fila2 = new ArrayList<>();
    List<CellData> fila3 = new ArrayList<>();

    CellData a1 = new CellData();
    a1.setValue("codigo cuenta contable");
    fila1.add(a1);
    fila2.add(a1);

    CellData b1 = new CellData();
    b1.setValue("");
    fila1.add(b1);
    fila2.add(b1);

    CellData c1 = new CellData();
    c1.setValue("codigo partida");
    fila1.add(c1);
    fila2.add(c1);

    CellData d1 = new CellData();
    d1.setValue("");
    fila1.add(d1);
    fila2.add(d1);

    CellData e1 = new CellData();
    e1.setValue("codigo centro");
    fila1.add(e1);
    fila2.add(e1);

    CellData f1 = new CellData();
    f1.setValue("");
    fila1.add(f1);
    fila2.add(f1);

    CellData g1 = new CellData();
    g1.setValue("monto");
    fila1.add(g1);

    CellData g2 = new CellData();
    g2.setValue("100");
    fila2.add(g2);

    CellData a3 = new CellData();
    a3.setValue("");
    fila3.add(a3);

    CellData b3 = new CellData();
    b3.setValue("");
    fila3.add(b3);

    CellData c3 = new CellData();
    c3.setValue("");
    fila3.add(c3);

    CellData d3 = new CellData();
    d3.setValue("");
    fila3.add(d3);

    CellData e3 = new CellData();
    e3.setValue("");
    fila3.add(e3);

    CellData f3 = new CellData();
    f3.setValue("");
    fila3.add(f3);

    CellData g3 = new CellData();
    g3.setValue("10");
    fila3.add(g3);

    excel.add(fila1);
    excel.add(fila2);
    excel.add(fila3);

    return excel;
  }

  private  List<List<CellData>> llenarExcelBienBudget()
  {
    List<List<CellData>> excel = new ArrayList<>();
    List<CellData> fila1 = new ArrayList<>();
    List<CellData> fila2 = new ArrayList<>();
    List<CellData> fila3 = new ArrayList<>();

    CellData a1 = new CellData();
    a1.setValue("codigo cuenta contable");
    fila1.add(a1);
    fila2.add(a1);

    CellData b1 = new CellData();
    b1.setValue("");
    fila1.add(b1);
    fila2.add(b1);

    CellData c1 = new CellData();
    c1.setValue("codigo partida");
    fila1.add(c1);
    fila2.add(c1);

    CellData d1 = new CellData();
    d1.setValue("");
    fila1.add(d1);
    fila2.add(d1);

    CellData e1 = new CellData();
    e1.setValue("codigo centro");
    fila1.add(e1);
    fila2.add(e1);

    CellData f1 = new CellData();
    f1.setValue("");
    fila1.add(f1);
    fila2.add(f1);

    CellData g1 = new CellData();
    g1.setValue("monto 1");
    fila1.add(g1);

    CellData h1 = new CellData();
    h1.setValue("monto 2");
    fila1.add(h1);

    CellData i1 = new CellData();
    i1.setValue("monto 3");
    fila1.add(i1);

    CellData j1 = new CellData();
    j1.setValue("monto 4");
    fila1.add(j1);

    CellData k1 = new CellData();
    k1.setValue("monto 5");
    fila1.add(k1);

    CellData l1 = new CellData();
    l1.setValue("monto 6");
    fila1.add(l1);

    CellData m1 = new CellData();
    m1.setValue("monto 7");
    fila1.add(m1);

    CellData n1 = new CellData();
    n1.setValue("monto 8");
    fila1.add(n1);

    CellData o1 = new CellData();
    o1.setValue("monto 9");
    fila1.add(o1);

    CellData p1 = new CellData();
    p1.setValue("monto 10");
    fila1.add(p1);

    CellData q1 = new CellData();
    q1.setValue("monto 11");
    fila1.add(q1);

    CellData r1 = new CellData();
    r1.setValue("monto 12");
    fila1.add(r1);

    CellData g2 = new CellData();
    g2.setValue("100");
    fila2.add(g2);

    CellData h2 = new CellData();
    h2.setValue("100");
    fila2.add(h2);

    CellData i2 = new CellData();
    i2.setValue("100");
    fila2.add(i2);

    CellData j2 = new CellData();
    j2.setValue("100");
    fila2.add(j2);

    CellData k2 = new CellData();
    k2.setValue("100");
    fila2.add(k2);

    CellData l2 = new CellData();
    l2.setValue("100");
    fila2.add(l2);

    CellData m2 = new CellData();
    m2.setValue("100");
    fila2.add(m2);

    CellData n2 = new CellData();
    n2.setValue("100");
    fila2.add(n2);

    CellData o2 = new CellData();
    o2.setValue("100");
    fila2.add(o2);

    CellData p2 = new CellData();
    p2.setValue("100");
    fila2.add(p2);

    CellData q2 = new CellData();
    q2.setValue("100");
    fila2.add(q2);

    CellData r2 = new CellData();
    r2.setValue("100");
    fila2.add(r2);

    CellData a3 = new CellData();
    a3.setValue("");
    fila3.add(a3);

    CellData b3 = new CellData();
    b3.setValue("");
    fila3.add(b3);

    CellData c3 = new CellData();
    c3.setValue("");
    fila3.add(c3);

    CellData d3 = new CellData();
    d3.setValue("");
    fila3.add(d3);

    CellData e3 = new CellData();
    e3.setValue("");
    fila3.add(e3);

    CellData f3 = new CellData();
    f3.setValue("");
    fila3.add(f3);

    CellData g3 = new CellData();
    g3.setValue("10");
    fila3.add(g3);

    CellData h3 = new CellData();
    h3.setValue("10");
    fila3.add(h3);

    CellData i3 = new CellData();
    i3.setValue("10");
    fila3.add(i3);

    CellData j3 = new CellData();
    j3.setValue("10");
    fila3.add(j3);

    CellData k3 = new CellData();
    k3.setValue("10");
    fila3.add(k3);

    CellData l3 = new CellData();
    l3.setValue("10");
    fila3.add(l3);

    CellData m3 = new CellData();
    m3.setValue("10");
    fila3.add(m3);

    CellData n3 = new CellData();
    n3.setValue("10");
    fila3.add(n3);

    CellData o3 = new CellData();
    o3.setValue("10");
    fila3.add(o3);

    CellData p3 = new CellData();
    p3.setValue("10");
    fila3.add(p3);

    CellData q3 = new CellData();
    q3.setValue("10");
    fila3.add(q3);

    CellData r3 = new CellData();
    r3.setValue("10");
    fila3.add(r3);

    excel.add(fila1);
    excel.add(fila2);
    excel.add(fila3);

    return excel;
  }

  private  List<List<CellData>> llenarExcelMal()
  {
    List<List<CellData>> excel = new ArrayList<>();
    List<CellData> fila1 = new ArrayList<>();

    CellData a3 = new CellData();
    a3.setValue("");
    fila1.add(a3);

    CellData b3 = new CellData();
    b3.setValue("");
    fila1.add(b3);

    CellData c3 = new CellData();
    c3.setValue("");
    fila1.add(c3);

    CellData d3 = new CellData();
    d3.setValue("");
    fila1.add(d3);

    CellData e3 = new CellData();
    e3.setValue("");
    fila1.add(e3);

    CellData f3 = new CellData();
    f3.setValue("");
    fila1.add(f3);

    CellData g3 = new CellData();
    g3.setValue("");
    fila1.add(g3);

    CellData h3 = new CellData();
    h3.setValue("10");
    fila1.add(h3);

    CellData i3 = new CellData();
    i3.setValue("10");
    fila1.add(i3);

    CellData j3 = new CellData();
    j3.setValue("10");
    fila1.add(j3);

    CellData k3 = new CellData();
    k3.setValue("10");
    fila1.add(k3);

    CellData l3 = new CellData();
    l3.setValue("10");
    fila1.add(l3);

    CellData m3 = new CellData();
    m3.setValue("10");
    fila1.add(m3);

    CellData n3 = new CellData();
    n3.setValue("10");
    fila1.add(n3);

    CellData o3 = new CellData();
    o3.setValue("10");
    fila1.add(o3);

    CellData p3 = new CellData();
    p3.setValue("10");
    fila1.add(p3);

    CellData q3 = new CellData();
    q3.setValue("10");
    fila1.add(q3);

    CellData r3 = new CellData();
    r3.setValue("10");
    fila1.add(r3);

    excel.add(fila1);
    excel.add(fila1);

    return excel;
  }

}
