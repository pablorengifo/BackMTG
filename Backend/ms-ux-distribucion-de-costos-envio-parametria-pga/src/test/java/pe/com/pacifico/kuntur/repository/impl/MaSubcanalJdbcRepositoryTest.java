package pe.com.pacifico.kuntur.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pe.com.pacifico.kuntur.expose.request.MaProductoRequest;
import pe.com.pacifico.kuntur.expose.request.MaSubcanalRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaSubcanal;
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
public class MaSubcanalJdbcRepositoryTest {
  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private  MaSubcanalJdbcRepository jdbcRepository;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaSubcanal subcanal = new MaSubcanal();
  MaSubcanal subcanal1 = new MaSubcanal();
  MaSubcanal subcanal2 = new MaSubcanal();
  MaSubcanal subcanal3 = new MaSubcanal();
  MaSubcanalRequest request = new MaSubcanalRequest();
  String codigo="ABC001";

  @Test
  void shouldfindAllAccounts()
  {
    //when
    doReturn(Arrays.asList(subcanal1,subcanal2,subcanal3))
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt());

    //then
    assertEquals(3,jdbcRepository.findAll(repartoTipo).size());
    assertEquals(subcanal1, jdbcRepository.findAll(repartoTipo).get(0));
  }

  @Test
  void shouldNotfindAllAccounts() {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class), anyInt());

    //then
    assertEquals(0, jdbcRepository.findAll(repartoTipo).size());
  }

  @Test
  void shouldfindAllAccountsNotInMov()
  {
    //when
    doReturn(Arrays.asList(subcanal1,subcanal2))
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt(),anyInt());

    //then
    assertEquals(2,jdbcRepository.findAllNotInMovSubcanal(repartoTipo,periodo).size());
    assertEquals(subcanal1, jdbcRepository.findAllNotInMovSubcanal(repartoTipo,periodo).get(0));
  }

  @Test
  void shouldNotfindAllAccountsNotInMov() {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class), anyInt(), anyInt(), anyInt());

    //then
    assertEquals(0, jdbcRepository.findAllNotInMovSubcanal(repartoTipo, periodo).size());
  }

  @Test
  void shouldfindAnAccount()
  {
    //when
    when(jdbcTemplate.queryForObject(anyString(),any(),
        any(BeanPropertyRowMapper.class)))
        .thenReturn(subcanal);

    //then
    assertThat(jdbcRepository.findByCodSubcanal(repartoTipo,codigo),notNullValue());
    assertThat(jdbcRepository.findByCodSubcanal(repartoTipo,codigo),equalTo(subcanal));
  }

  @Test
  void shouldNotfindAnAccount() {
    //when
    when(jdbcTemplate.queryForObject(anyString(), any(),
        any(BeanPropertyRowMapper.class)))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertNull(jdbcRepository.findByCodSubcanal(repartoTipo, codigo));
  }

  @Test
  void shouldSaveAnAccount()
  {
    //when
    when(jdbcTemplate.update(anyString(),
        any(),any(),any(),any(),any(),any()))
        .thenReturn(1);

    //then
    request = MaSubcanalRequest.builder().codSubcanal("ABC001").build();
    assertEquals(jdbcRepository.save(request),1);
  }

  @Test
  void shouldNotSaveAnAccount() {
    //then
    request = MaSubcanalRequest.builder().codSubcanal("").build();
    assertEquals(jdbcRepository.save(request), 0);
  }

  @Test
  void shouldUpdateAnAccount()
  {
    //when
    when(jdbcTemplate.update(anyString(),
        any(),any(),any(),any(),any(),any()))
        .thenReturn(1);
    //then
    assertEquals(jdbcRepository.update(request),1);
  }

  @Test
  @DisplayName("debería eliminar una cuenta contable") //Para descripcion en el reporte
  void shouldDeleteAnAccount()
  {
    //when
    when(jdbcTemplate.queryForObject(anyString(), any(),
        any(BeanPropertyRowMapper.class)))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertTrue(jdbcRepository.delete(repartoTipo, codigo));
  }

  @Test
  void shouldNotDeleteAnAccountBecauseItExistsInMov() {
    //when
    when(jdbcTemplate.queryForObject(anyString(), any(),
        any(BeanPropertyRowMapper.class)))
        .thenReturn(null);

    //then
    assertFalse(jdbcRepository.delete(repartoTipo, codigo));
  }

  @Test
  void shouldNotDeleteAnAccountByException() {
    //when
    when(jdbcTemplate.queryForObject(anyString(), any(),
        any(BeanPropertyRowMapper.class)))
        .thenThrow(NullPointerException.class);

    //then
    assertFalse(jdbcRepository.delete(repartoTipo, codigo));
  }

  @Test
  public void shouldFindAllAcountCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
        .thenReturn(Arrays.asList("Codigo1","Codigo2"));

    //then
    assertThat(jdbcRepository.findAllCodSubcanal(repartoTipo),notNullValue());
    assertThat(jdbcRepository.findAllCodSubcanal(repartoTipo).get(0),equalToIgnoringCase("Codigo1"));
  }

  @Test
  public void shouldNotFindAllAcountCodes() {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertEquals(0, jdbcRepository.findAllCodSubcanal(repartoTipo).size());
  }

  @Test
  public void shouldSaveExcel()
  {
    //given
    List<List<CellData>> excel= llenarExcelBien();

    //when
    when(jdbcRepository.findAllCodSubcanal(repartoTipo))
        .thenReturn(Arrays.asList(""));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).thenReturn(null);

    //then
    assertEquals(2,jdbcRepository.saveExcelToBD(excel,repartoTipo).size());
  }

  @Test
  public void shouldNotSaveExcelRealByWrongHeaders()
  {
    //Given
    List<List<CellData>> excel = llenarExcelMal();

    //then
    assertNotNull(jdbcRepository.saveExcelToBD(excel,repartoTipo));
  }

  private List<List<CellData>> llenarExcelBien() {
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

    CellData a2 = new CellData();
    a2.setValue(codigo);
    fila2.add(a2);

    CellData b2 = new CellData();
    b2.setValue("nombre");
    fila2.add(b2);

    CellData a3 = new CellData();
    a3.setValue("");
    fila3.add(a3);

    CellData b3 = new CellData();
    b3.setValue("");
    fila3.add(b3);

    excel.add(fila1);
    excel.add(fila2);
    excel.add(fila3);

    return excel;
  }

  private List<List<CellData>> llenarExcelMal() {
    List<List<CellData>> excel = new ArrayList<>();
    List<CellData> fila1 = new ArrayList<>();

    CellData a3 = new CellData();
    a3.setValue("");
    fila1.add(a3);

    CellData b3 = new CellData();
    b3.setValue("");
    fila1.add(b3);

    excel.add(fila1);
    excel.add(fila1);

    return excel;
  }
}

