package pe.com.pacifico.kuntur.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pe.com.pacifico.kuntur.expose.request.MovCentroRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;

import java.util.ArrayList;
import java.util.Arrays;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovCentroJdbcRepositoryTest {
  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private MovCentroJdbcRepository jdbcRepository;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MovCentro centro1 = new MovCentro();
  MovCentro centro2 = new MovCentro();
  MovCentro centro3 = new MovCentro();
  MovCentroRequest request = new MovCentroRequest();
  String codigo="any";

  @Test
  void shouldfindAllAccounts()
  {
    //when
    doReturn(Arrays.asList(centro1,centro2,centro3))
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt());

    //then
    assertEquals(3,jdbcRepository.findAll(repartoTipo,periodo).size());
    assertEquals(centro1, jdbcRepository.findAll(repartoTipo,periodo).get(0));
  }

  @Test
  void shouldNotfindAllAccounts()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt());

    //then
    assertEquals(0,jdbcRepository.findAll(repartoTipo,periodo).size());
  }

  @Test
  void shouldfindAnAccount()
  {
    //when
    when(jdbcTemplate.queryForObject(anyString(),any(),
        any(BeanPropertyRowMapper.class)))
        .thenReturn(centro1);

    //then
    assertThat(jdbcRepository.findByCodCentro(codigo),notNullValue());
    assertThat(jdbcRepository.findByCodCentro(codigo),equalTo(centro1));
  }

  @Test
  void shouldNotfindAnAccount()
  {
    //when
    when(jdbcTemplate.queryForObject(anyString(),any(),
        any(BeanPropertyRowMapper.class)))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertNull(jdbcRepository.findByCodCentro(codigo));
  }

  @Test
  void shouldSaveAnAccount()
  {
    //when
    when(jdbcTemplate.update(anyString(),
        any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any()))
        .thenReturn(1);
    //then
    assertEquals(jdbcRepository.save(request),1);
  }

  @Test
  void shouldNotSaveAnAccountBecauseItExistsInMov()
  {
    //when
    when(jdbcTemplate.update(anyString(),
        any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any()))
        .thenThrow(DuplicateKeyException.class);

    //then
    assertEquals(jdbcRepository.save(request),-1);
  }

  @Test
  void shouldNotSaveAnAccountByException()
  {
    //when
    when(jdbcTemplate.update(anyString(),
        any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any()))
        .thenThrow(NullPointerException.class);

    //then
    assertEquals(jdbcRepository.save(request),-2);
  }

  @Test
  void shouldUpdateAnAccount()
  {
    //when
    when(jdbcTemplate.update(anyString(),
        any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any()))
        .thenReturn(1);
    //then
    assertEquals(jdbcRepository.update(request),1);
  }

  @Test
  @DisplayName("debería eliminar un Mov Centro") //Para descripcion en el reporte
  void shouldDeleteAnAccount()
  {
    //when
    when(jdbcTemplate.update(anyString(),
        anyInt(),anyString(),anyInt()))
        .thenReturn(1);

    //then
    assertTrue(jdbcRepository.delete(repartoTipo,periodo, codigo));
  }

  @Test
  void shouldNotDeleteAnAccountByException()
  {
    //when
    when(jdbcTemplate.update(anyString(),
        anyInt(),anyString(),anyInt()))
        .thenThrow(NullPointerException.class);

    //then
    assertFalse(jdbcRepository.delete(repartoTipo,periodo, codigo));
  }

  @Test
  public void shouldFindAllAcountCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
        .thenReturn(Arrays.asList("Codigo1","Codigo2"));

    //then
    assertThat(jdbcRepository.findAllCodCentro(repartoTipo),notNullValue());
    assertThat(jdbcRepository.findAllCodCentro(repartoTipo).get(0),equalToIgnoringCase("Codigo1"));
  }

  @Test
  public void shouldNotFindAllAcountCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertEquals(0,jdbcRepository.findAllCodCentro(repartoTipo).size());
  }

  @Test
  public void shouldSaveExcel()
  {
    //given
    List<List<CellData>> excel= llenarExcelBien();

    //when
    when(jdbcRepository.findAllCodCentro(repartoTipo))
        .thenReturn(Arrays.asList("codigo"));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).thenReturn(null);

    //then
    assertEquals(2,jdbcRepository.saveExcelToBD(excel,repartoTipo,periodo).size());
  }

  @Test
  public void shouldNotSaveExcelRealByWrongHeaders()
  {
    //Given
    List<List<CellData>> excel = llenarExcelMal();

    //then
    assertNotNull(jdbcRepository.saveExcelToBD(excel,repartoTipo,periodo));
  }

  private  List<List<CellData>> llenarExcelBien()
  {
    List<List<CellData>> excel = new ArrayList<>();
    List<CellData> fila1 = new ArrayList<>();
    List<CellData> fila2 = new ArrayList<>();
    List<CellData> fila3 = new ArrayList<>();

    CellData a1 = new CellData();
    a1.setValue("codigo");
    fila1.add(a1);

    CellData b1 = new CellData();
    b1.setValue("nombre");
    fila1.add(b1);

    CellData c1 = new CellData();
    c1.setValue("tipo");
    fila1.add(c1);

    CellData d1 = new CellData();
    d1.setValue("nivel");
    fila1.add(d1);

    CellData e1 = new CellData();
    e1.setValue("centro padre");
    fila1.add(e1);

    CellData f1 = new CellData();
    f1.setValue("tipo gasto");
    fila1.add(f1);

    CellData g1 = new CellData();
    g1.setValue("niif17 atribuible");
    fila1.add(g1);

    CellData h1 = new CellData();
    h1.setValue("niif17 tipo");
    fila1.add(h1);

    CellData i1 = new CellData();
    i1.setValue("niif17 clase");
    fila1.add(i1);

    CellData j1 = new CellData();
    j1.setValue("grupo ceco");
    fila1.add(j1);

    CellData k1 = new CellData();
    k1.setValue("tipo ceco");
    fila1.add(k1);

    CellData a2 = new CellData();
    a2.setValue("codigo");
    fila2.add(a2);

    CellData b2 = new CellData();
    b2.setValue("nombre");
    fila2.add(b2);

    CellData c2 = new CellData();
    c2.setValue("1");
    fila2.add(c2);

    CellData d2 = new CellData();
    d2.setValue("1");
    fila2.add(d2);

    CellData e2 = new CellData();
    e2.setValue("centro padre");
    fila2.add(e2);

    CellData f2 = new CellData();
    f2.setValue("1");
    fila2.add(f2);

    CellData g2 = new CellData();
    g2.setValue("SI");
    fila2.add(g2);

    CellData h2 = new CellData();
    h2.setValue("GM");
    fila2.add(h2);

    CellData i2 = new CellData();
    i2.setValue("FI");
    fila2.add(i2);

    CellData j2 = new CellData();
    j2.setValue("grupo ceco");
    fila2.add(j2);

    CellData k2 = new CellData();
    k2.setValue("tipo ceco");
    fila2.add(k2);

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
    g3.setValue("");
    fila3.add(g3);

    CellData h3 = new CellData();
    h3.setValue("");
    fila3.add(h3);

    CellData i3 = new CellData();
    i3.setValue("");
    fila3.add(i3);

    CellData j3 = new CellData();
    j3.setValue("");
    fila3.add(j3);

    CellData k3 = new CellData();
    k3.setValue("");
    fila3.add(k3);

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
    h3.setValue("");
    fila1.add(h3);

    CellData i3 = new CellData();
    i3.setValue("");
    fila1.add(i3);

    CellData j3 = new CellData();
    j3.setValue("");
    fila1.add(j3);

    CellData k3 = new CellData();
    k3.setValue("");
    fila1.add(k3);

    excel.add(fila1);
    excel.add(fila1);

    return excel;
  }
}
