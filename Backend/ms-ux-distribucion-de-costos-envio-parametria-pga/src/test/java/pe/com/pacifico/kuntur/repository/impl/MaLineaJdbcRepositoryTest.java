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
import pe.com.pacifico.kuntur.expose.request.MaCanalRequest;
import pe.com.pacifico.kuntur.expose.request.MaLineaRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaLinea;
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
public class MaLineaJdbcRepositoryTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private  MaLineaJdbcRepository jdbcRepository;

  //Given
  int repartoTipo=1;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaLinea linea = new MaLinea();
  MaLinea linea1 = new MaLinea();
  MaLinea linea2 = new MaLinea();
  MaLinea linea3 = new MaLinea();
  MaLineaRequest request = new MaLineaRequest();
  String codigo="ANY";

  @Test
  void shouldfindAllAccounts()
  {
    //when
    doReturn(Arrays.asList(linea1,linea2,linea3))
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt());

    //then
    assertEquals(3,jdbcRepository.findAll(repartoTipo).size());
    assertEquals(linea1, jdbcRepository.findAll(repartoTipo).get(0));
  }

  @Test
  void shouldNotfindAllAccounts()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt());

    //then
    assertEquals(0,jdbcRepository.findAll(repartoTipo).size());
  }

  @Test
  void shouldfindAnAccount()
  {
    //when
    when(jdbcTemplate.queryForObject(anyString(),any(),
        any(BeanPropertyRowMapper.class)))
        .thenReturn(linea);

    //then
    assertThat(jdbcRepository.findByCodLinea(repartoTipo,codigo),notNullValue());
    assertThat(jdbcRepository.findByCodLinea(repartoTipo,codigo),equalTo(linea));
  }

  @Test
  void shouldNotfindAnAccount()
  {
    //when
    when(jdbcTemplate.queryForObject(anyString(),any(),
        any(BeanPropertyRowMapper.class)))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertNull(jdbcRepository.findByCodLinea(repartoTipo,codigo));
  }

  @Test
  void shouldSaveAnAccount()
  {
    //when
    when(jdbcTemplate.update(anyString(),
        any(),any(),any(),any(),any(),any()))
        .thenReturn(1);

    //then
    request = MaLineaRequest.builder().codLinea("ABC").build();
    System.out.println("LINEA: " + request);
    assertEquals(jdbcRepository.save(request),1);
  }

  @Test
  void shouldNotSaveAnAccount()
  {
    //then
    request = MaLineaRequest.builder().codLinea("").build();
    assertEquals(jdbcRepository.save(request),0);
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
  @DisplayName("debería eliminar una Linea") //Para descripcion en el reporte
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
  @DisplayName("debería eliminar una Linea") //Para descripcion en el reporte
  void shouldNotDeleteAnAccountBecauseItExistsInMov()
  {
    //when
    when(jdbcTemplate.queryForObject(anyString(), any(),
        any(BeanPropertyRowMapper.class)))
        .thenReturn(null);

    //then
    assertFalse(jdbcRepository.delete(repartoTipo, codigo));
  }

  @Test
  @DisplayName("debería eliminar una Linea") //Para descripcion en el reporte
  void shouldNotDeleteAnAccountByException()
  {
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
    assertThat(jdbcRepository.findAllCodLinea(repartoTipo),notNullValue());
    assertThat(jdbcRepository.findAllCodLinea(repartoTipo).get(0),equalToIgnoringCase("Codigo1"));
  }

  @Test
  public void shouldNotFindAllAcountCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertEquals(0,jdbcRepository.findAllCodLinea(repartoTipo).size());
  }

  @Test
  public void shouldSaveExcel()
  {
    //given
    List<List<CellData>> excel= llenarExcelBien();

    //when
    when(jdbcRepository.findAllCodLinea(repartoTipo))
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

    excel.add(fila1);
    excel.add(fila1);

    return excel;
  }
}
