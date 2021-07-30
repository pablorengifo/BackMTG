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
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaDriver;
import pe.com.pacifico.kuntur.model.MovDriverObjeto;
import pe.com.pacifico.kuntur.model.RequestFileRead;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovDriverObjetoJdbcRepositoryTest {
  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private MovDriverObjetoJdbcRepository jdbcRepository;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaDriver driver1 = new MaDriver();
  MaDriver driver2 = new MaDriver();
  MaDriver driver3 = new MaDriver();
  MovDriverObjeto driver4 = new MovDriverObjeto();
  MovDriverObjeto driver5 = new MovDriverObjeto();
  MovDriverObjeto driver6 = new MovDriverObjeto();
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
  void shouldfindAllMovAccountsByCode()
  {
    //when
    doReturn(Arrays.asList(driver4,driver5,driver6))
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt(),anyString());

    //then
    assertEquals(3,jdbcRepository.findAllByPeriod(repartoTipo,periodo,codigo).size());
    assertEquals(driver4, jdbcRepository.findAllByPeriod(repartoTipo,periodo,codigo).get(0));
  }

  @Test
  void shouldNotfindAllMovAccountsByCode()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt(),anyString());

    //then
    assertEquals(0,jdbcRepository.findAllByPeriod(repartoTipo,periodo,codigo).size());
  }

  @Test
  void shouldNotFindAllMovAccounts()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt(),anyString());

    //then
    assertEquals(0,jdbcRepository.findAllByPeriod(repartoTipo,periodo,codigo).size());
  }
  @Test
  void shouldfindAllMovAccounts()
  {
    //when
    doReturn(Arrays.asList(driver4,driver5,driver6))
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt());

    //then
    assertEquals(3,jdbcRepository.findAllByPeriod(repartoTipo,periodo).size());
    assertEquals(driver4, jdbcRepository.findAllByPeriod(repartoTipo,periodo).get(0));
  }
  @Test
  void shoulNotFindAllInMov()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt());

    //then
    assertEquals(0,jdbcRepository.findAll(2,periodo).size());
  }

  @Test
  @DisplayName("debería eliminar una Linea") //Para descripcion en el reporte
  void shouldDeleteAnAccount()
  {
    //then
    assertTrue(jdbcRepository.delete(repartoTipo,periodo, codigo));
  }

  @Test
  public void shouldNotDelete() {
    //when
    when(jdbcTemplate.update(anyString(),
        anyInt(),anyString(),anyInt()))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertFalse(jdbcRepository.delete(repartoTipo, periodo, codigo));
  }

  @Test
  public void shouldSaveExcelMa()
  {
    //given
    List<List<CellData>> excel= llenarExcelMaBien();

    //When
    when(jdbcTemplate.queryForList(anyString(), eq(String.class), anyInt()))
        .thenReturn(Arrays.asList("CODIGO"));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).thenReturn(null);

    //Then
    assertNull(jdbcRepository.saveExcelToBD_MaDriver(excel,repartoTipo,periodo));
  }

  @Test
  public void shouldNotSaveExcelMaByWrongHeaders()
  {
    //Given
    List<List<CellData>> excel = llenarExcelMaMal();

    //then
    assertNotNull(jdbcRepository.saveExcelToBD_MaDriver(excel,periodo,repartoTipo));
    excel = llenarExcelMaMal();
    assertEquals(1,jdbcRepository.saveExcelToBD_MaDriver(excel,periodo,repartoTipo).size());
  }

  @Test
  public void shouldSaveExcelMov()
  {
    //given
    List<List<CellData>> excel= llenarExcelMovBien();

    //when
    when(jdbcTemplate.queryForList(anyString(), eq(String.class), anyInt()))
        .thenReturn(Arrays.asList("codigo","100","FI"));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).thenReturn(null);

    assertEquals(2,jdbcRepository.saveExcelToBD_MovDriverObjeto(excel,repartoTipo,periodo).size());
  }

  @Test
  public void shouldNotSaveExcelMovByWrongHeaders()
  {
    //Given
    List<List<CellData>> excel = llenarExcelMovMal();

    //then
    assertNotNull(jdbcRepository.saveExcelToBD_MovDriverObjeto(excel,periodo,repartoTipo));
    excel = llenarExcelMovMal();
    assertEquals(1,jdbcRepository.saveExcelToBD_MovDriverObjeto(excel,periodo,repartoTipo).size());
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
    c2.setValue("OBJETOS DE COSTOS");
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
    c3.setValue("OBJETOS DE COSTOS");
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

    CellData b1 = new CellData();
    b1.setValue("nombre");
    fila1.add(b1);

    CellData c1 = new CellData();
    c1.setValue("codigo linea");
    fila1.add(c1);

    CellData d1 = new CellData();
    d1.setValue("nombre linea");
    fila1.add(d1);

    CellData e1 = new CellData();
    e1.setValue("CODIGO PRODUCTO");
    fila1.add(e1);

    CellData f1 = new CellData();
    f1.setValue("NOMBRE PRODUCTO");
    fila1.add(f1);

    CellData g1 = new CellData();
    g1.setValue("CODIGO CANAL");
    fila1.add(g1);

    CellData h1 = new CellData();
    h1.setValue("NOMBRE CANAL");
    fila1.add(h1);

    CellData i1 = new CellData();
    i1.setValue("CODIGO SUBCANAL");
    fila1.add(i1);

    CellData j1 = new CellData();
    j1.setValue("NOMBRE SUBCANAL");
    fila1.add(j1);

    CellData k1 = new CellData();
    k1.setValue("PORCENTAJE");
    fila1.add(k1);

    CellData a2 = new CellData();
    a2.setValue("codigo");
    fila2.add(a2);

    CellData b2 = new CellData();
    b2.setValue("nombre");
    fila2.add(b2);

    CellData c2 = new CellData();
    c2.setValue("codigo centro");
    fila2.add(c2);

    CellData d2 = new CellData();
    d2.setValue("1");
    fila2.add(d2);

    CellData e2 = new CellData();
    e2.setValue("100");
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
    k2.setValue("100");
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
    k3.setValue("100");
    fila3.add(k3);

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

    CellData b1 = new CellData();
    b1.setValue("nombre");
    fila1.add(b1);

    CellData c1 = new CellData();
    c1.setValue("");
    fila1.add(c1);
    fila1.add(new CellData());

    CellData d1 = new CellData();
    d1.setValue("nivel");
    fila1.add(d1);

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

    excel.add(fila1);
    excel.add(fila1);
    excel.add(fila1);
    excel.add(fila1);
    excel.add(fila1);
    excel.add(fila1);

    return excel;
  }

}
