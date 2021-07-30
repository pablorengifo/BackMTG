package pe.com.pacifico.kuntur.repository.impl;

import org.junit.jupiter.api.DisplayName;
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
import pe.com.pacifico.kuntur.expose.request.MovCuentaContableRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovCuentaContable;
import pe.com.pacifico.kuntur.model.RequestFileRead;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovCuentaContableJdbcRepositoryTest {

  @Mock
  private  JdbcTemplate jdbcTemplate;

  @InjectMocks
  private MovCuentaContableJdbcRepository cuentaContableJdbcRepository;

  //Given
  int repartoTipo=1;
  Date date = new Date();
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MovCuentaContable cuentaContable1 = new MovCuentaContable("46.1.2.01.00.00","reparacion",
      date,date,periodo,repartoTipo,12);
  MovCuentaContable cuentaContable2 = new MovCuentaContable("46.1.2.01.00.00","reparacion",
      date,date,periodo,repartoTipo,12);
  MovCuentaContable cuentaContable3 = new MovCuentaContable("46.1.2.01.00.00","reparacion",
      date,date,periodo,repartoTipo,12);
  MovCuentaContableRequest request1 = new MovCuentaContableRequest("46.1.2.01.00.00",
      date,date,periodo,repartoTipo,12);
  String codigo="46.1.2.01.00.00";

  @Test
  void shouldfindAllAccounts()
  {
    //when
    doReturn(Arrays.asList(cuentaContable1,cuentaContable2,cuentaContable3))
        .when(jdbcTemplate).query(ArgumentMatchers.anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt());

    //then
    assertEquals(3,cuentaContableJdbcRepository.findAll(repartoTipo,periodo).size());
    assertEquals(cuentaContable1, cuentaContableJdbcRepository.findAll(repartoTipo,periodo).get(0));
  }

  @Test
  void shouldNotfindAllAccounts()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt());

    //then
    assertEquals(0,cuentaContableJdbcRepository.findAll(repartoTipo,periodo).size());
  }

  @Test
  void shouldSaveAnAccount()
  {
    //when
    when(jdbcTemplate.update(anyString(),
        anyString(),any(Date.class),any(Date.class),anyInt(),anyInt(),anyFloat()))
        .thenReturn(1);
    //then
    assertEquals(cuentaContableJdbcRepository.save(request1),1);
  }

  @Test
  @DisplayName("debería eliminar una cuenta contable") //Para descripcion en el reporte
  void shouldDeleteAnAccount()
  {
    //when
    when(jdbcTemplate.update(anyString(),
        anyInt(),anyInt(),anyString()))
        .thenReturn(1);

    //then
    assertTrue(cuentaContableJdbcRepository.delete(repartoTipo,periodo, codigo));
  }

  @Test
  void shouldNotDeleteAnAccountByException()
  {
    //when
    when(jdbcTemplate.update(anyString(),
        anyInt(),anyString(),anyInt()))
        .thenThrow(NullPointerException.class);

    //then
    assertFalse(cuentaContableJdbcRepository.delete(repartoTipo,periodo, codigo));
  }

  @Test
  public void shouldFindAllAcountCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
        .thenReturn(Arrays.asList("Codigo1","Codigo2"));

    //then
    assertThat(cuentaContableJdbcRepository.findAllCodCuentaContable(repartoTipo),notNullValue());
    assertThat(cuentaContableJdbcRepository.findAllCodCuentaContable(repartoTipo).get(0),equalToIgnoringCase("Codigo1"));
  }

  @Test
  public void shouldNotFindAllAcountCodes()
  {
    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt()))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertEquals(0,cuentaContableJdbcRepository.findAllCodCuentaContable(repartoTipo).size());
  }

  @Test
  public void shouldSaveExcel()
  {
    //given
    List<List<CellData>> excel= llenarExcelBien();

    //when
    when(cuentaContableJdbcRepository.findAllCodCuentaContable(repartoTipo))
        .thenReturn(Arrays.asList(codigo));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).thenReturn(null);

    //then
    assertEquals(2,cuentaContableJdbcRepository.saveExcelToBD(excel,repartoTipo,periodo).size());
  }

  @Test
  public void shouldNotSaveExcelRealByWrongHeaders()
  {
    //Given
    List<List<CellData>> excel = llenarExcelMal();

    //then
    assertNotNull(cuentaContableJdbcRepository.saveExcelToBD(excel,repartoTipo,periodo));
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
    c1.setValue("tipo gasto");
    fila1.add(c1);

    CellData d1 = new CellData();
    d1.setValue("niif17 atribuible");
    fila1.add(d1);

    CellData e1 = new CellData();
    e1.setValue("niif17 tipo");
    fila1.add(e1);

    CellData f1 = new CellData();
    f1.setValue("niif17 clase");
    fila1.add(f1);

    CellData a2 = new CellData();
    a2.setValue(codigo);
    fila2.add(a2);

    CellData b2 = new CellData();
    b2.setValue("nombre");
    fila2.add(b2);

    CellData c2 = new CellData();
    c2.setValue("1");
    fila2.add(c2);

    CellData d2 = new CellData();
    d2.setValue("SI");
    fila2.add(d2);

    CellData e2 = new CellData();
    e2.setValue("GM");
    fila2.add(e2);

    CellData f2 = new CellData();
    f2.setValue("FI");
    fila2.add(f2);

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

    excel.add(fila1);
    excel.add(fila1);

    return excel;
  }
}
