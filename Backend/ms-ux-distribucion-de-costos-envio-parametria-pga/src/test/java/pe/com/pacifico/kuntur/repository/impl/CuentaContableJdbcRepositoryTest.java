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
import pe.com.pacifico.kuntur.expose.request.CuentaContableRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.CuentaContable;
import pe.com.pacifico.kuntur.model.MovCuentaContable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


/**
 * <b>Class</b>: CuentaContableJdbcRepositoryTest <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 28, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class CuentaContableJdbcRepositoryTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private CuentaContableJdbcRepository cuentaContableJdbcRepository;

  //Given
  Date date = new Date();
  int repartoTipo=1;
  int periodo=202104;
  CuentaContable cuentaContable1 = new CuentaContable("46.1.2.01.00.00","reparacion",true,
      date,date,"EX","EX","EX",repartoTipo,true);
  CuentaContable cuentaContable2 = new CuentaContable("46.1.2.03.00.00","eventuales",true,
      date,date,"EX","EX","EX",repartoTipo,true);
  CuentaContable cuentaContable3 = new CuentaContable("46.1.2.12.00.00","mantenimiento",true,
      date,date,"EX","EX","EX",repartoTipo,true);
  CuentaContableRequest cuenta = new CuentaContableRequest("46.1.2.15.00.00","ejemplo",true,
      date,date,"EX","EX","EX",1,true);
  String codigo="46.1.2.01.00.00";

  @Test
  void shouldfindAllAccounts()
  {
    //when
    doReturn(Arrays.asList(cuentaContable1,cuentaContable2,cuentaContable3))
        .when(jdbcTemplate).query(ArgumentMatchers.anyString(),
        any(BeanPropertyRowMapper.class),anyInt());

    //then
    assertEquals(3,cuentaContableJdbcRepository.findAll(repartoTipo).size());
    assertEquals(cuentaContable1, cuentaContableJdbcRepository.findAll(repartoTipo).get(0));
  }

  @Test
  void shouldNotFindAllAccounts()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(ArgumentMatchers.anyString(),
        any(BeanPropertyRowMapper.class),anyInt());

    //then
    assertEquals(0,cuentaContableJdbcRepository.findAll(repartoTipo).size());
  }

  @Test
  void shouldfindAllAccountsNotInMov()
  {
    //when
    doReturn(Arrays.asList(cuentaContable1,cuentaContable2))
        .when(jdbcTemplate).query(ArgumentMatchers.anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt(),anyInt());

    //then
    assertEquals(2,cuentaContableJdbcRepository.findAllNotInMovCuentaContable(repartoTipo,periodo).size());
    assertEquals(cuentaContable1, cuentaContableJdbcRepository.findAllNotInMovCuentaContable(repartoTipo,periodo).get(0));
  }

  @Test
  void shouldNotFindAllAccountsInMov()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(ArgumentMatchers.anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt(),anyInt());

    //then
    assertEquals(0,cuentaContableJdbcRepository.findAllNotInMovCuentaContable(repartoTipo,periodo).size());
  }

  @Test
  void shouldfindAnAccount()
  {
    //when
    when(jdbcTemplate.queryForObject(anyString(),any(),
        any(BeanPropertyRowMapper.class)))
        .thenReturn(cuentaContable1);

    //then
    assertThat(cuentaContableJdbcRepository.findByCodCuentaContable(repartoTipo,codigo),notNullValue());
    assertThat(cuentaContableJdbcRepository.findByCodCuentaContable(repartoTipo,codigo),equalTo(cuentaContable1));
  }

  @Test
  void shouldNotFindAnAccount()
  {
    //when
    when(jdbcTemplate.queryForObject(anyString(),any(),
        any(BeanPropertyRowMapper.class)))
        .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertThat(cuentaContableJdbcRepository.findByCodCuentaContable(repartoTipo,codigo),nullValue());
  }

  @Test
  void shouldSaveAnAccount()
  {
    //when
    when(jdbcTemplate.update(anyString(),
        anyString(),anyString(),anyBoolean(),any(Date.class),any(Date.class),anyString(),anyString(),
        anyString(),anyInt(),anyBoolean(),anyInt()))
        .thenReturn(1);

    //then
    assertEquals(cuentaContableJdbcRepository.save(cuenta),1);
  }

  @Test
  void shouldNotSaveAnAccount()
  {
    //given
    cuenta.setCodCuentaContable("hola");

    //then
    assertEquals(cuentaContableJdbcRepository.save(cuenta),0);
  }


  @Test
  void shouldUpdateAnAccount()
  {
    //when
    when(jdbcTemplate.update(anyString(),anyString(), anyBoolean(),
        any(Date.class), anyString(),anyString(),
        anyString(), anyBoolean(), anyInt(), anyString()))
        .thenReturn(1);

    //then
    assertEquals(cuentaContableJdbcRepository.update(cuenta),1);
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
    assertTrue(cuentaContableJdbcRepository.delete(repartoTipo, codigo));
  }

  @Test
  @DisplayName("debería eliminar una cuenta contable") //Para descripcion en el reporte
  void shouldNotDeleteAnAccountBecauseItExistInMov()
  {
    //when
    when(jdbcTemplate.queryForObject(anyString(), any(),
        any(BeanPropertyRowMapper.class)))
        .thenReturn(new MovCuentaContable());

    //then
    assertFalse(cuentaContableJdbcRepository.delete(repartoTipo, codigo));
  }

  @Test
  @DisplayName("debería eliminar una cuenta contable") //Para descripcion en el reporte
  void shouldDeleteAnAccountByAnError()
  {
    //when
    when(jdbcTemplate.queryForObject(anyString(), any(),
        any(BeanPropertyRowMapper.class)))
        .thenThrow(NullPointerException.class);

    //then
    assertFalse(cuentaContableJdbcRepository.delete(repartoTipo, codigo));
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
        .thenReturn(Arrays.asList(""));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).thenReturn(null);

    //then
    assertEquals(2,cuentaContableJdbcRepository.saveExcelToBD(excel,repartoTipo).size());
  }

  @Test
  public void shouldNotSaveExcelRealByErrorInDB()
  {
    //given
    List<List<CellData>> excel= llenarExcelBien();

    //when
    when(cuentaContableJdbcRepository.findAllCodCuentaContable(repartoTipo))
        .thenReturn(Arrays.asList(""));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class)))
        .thenThrow(NullPointerException.class);

    //then
    assertEquals(4,cuentaContableJdbcRepository.saveExcelToBD(excel,repartoTipo).size());
  }

  @Test
  public void shouldNotSaveExcelRealByWrongHeaders()
  {
    //Given
    List<List<CellData>> excel = llenarExcelMal();

    //then
    assertNotNull(cuentaContableJdbcRepository.saveExcelToBD(excel,repartoTipo));
  }

  @Test
  void shouldfindAllNiifAccounts()
  {
    //when
    doReturn(Arrays.asList(cuentaContable1,cuentaContable2,cuentaContable3))
        .when(jdbcTemplate).query(ArgumentMatchers.anyString(),
        any(BeanPropertyRowMapper.class),anyInt());

    //then
    assertEquals(3,cuentaContableJdbcRepository.findAllNiif(repartoTipo).size());
    assertEquals(cuentaContable1, cuentaContableJdbcRepository.findAllNiif(repartoTipo).get(0));
  }

  @Test
  void shouldNotFindAllNiifAccounts()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(ArgumentMatchers.anyString(),
        any(BeanPropertyRowMapper.class),anyInt());

    //then
    assertEquals(0,cuentaContableJdbcRepository.findAllNiif(repartoTipo).size());
  }

  @Test
  void shouldfindAllnotNiifAccounts()
  {
    //when
    doReturn(Arrays.asList(cuentaContable1,cuentaContable2,cuentaContable3))
        .when(jdbcTemplate).query(ArgumentMatchers.anyString(),
        any(BeanPropertyRowMapper.class),anyInt());

    //then
    assertEquals(3,cuentaContableJdbcRepository.findAllNotNiifCuentaContable(repartoTipo).size());
    assertEquals(cuentaContable1, cuentaContableJdbcRepository.findAllNotNiifCuentaContable(repartoTipo).get(0));
  }

  @Test
  void shouldNotFindAllnotNiifAccounts()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(ArgumentMatchers.anyString(),
        any(BeanPropertyRowMapper.class),anyInt());

    //then
    assertEquals(0,cuentaContableJdbcRepository.findAllNotNiifCuentaContable(repartoTipo).size());
  }

  @Test
  @DisplayName("debería eliminar una cuenta contable") //Para descripcion en el reporte
  void shouldRegisterAnNiifAccount()
  {
    //when
    when(jdbcTemplate.update(anyString(), anyString(), anyInt()))
        .thenReturn(1);

    //then
    assertEquals(1, cuentaContableJdbcRepository.registrarNiif(cuenta));
  }

  @Test
  @DisplayName("debería eliminar una cuenta contable") //Para descripcion en el reporte
  void shouldRemoveAnNiifAccount()
  {
    //when
    when(jdbcTemplate.update(anyString(), anyString(), anyInt()))
        .thenReturn(1);

    //then
    assertTrue(cuentaContableJdbcRepository.quitarNiif(repartoTipo, codigo));
  }

  @Test
  public void shouldSaveNiifExcel()
  {
    //given
    List<List<CellData>> excel= llenarExcelNiifBien();
    ArrayList<String> resultado = new ArrayList<>();
    resultado.add("codigo");

    //when
    when(cuentaContableJdbcRepository.findAllCodCuentaContable(repartoTipo))
        .thenReturn(resultado);
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).thenReturn(null);

    //then
    assertEquals(2,cuentaContableJdbcRepository.registerNiifByExcel(excel,repartoTipo).size());
  }

  @Test
  public void shouldNotSaveNiifExcelByErrorInDB()
  {
    //given
    List<List<CellData>> excel= llenarExcelNiifBien();
    ArrayList<String> resultado = new ArrayList<>();
    resultado.add("codigo");

    //when
    when(cuentaContableJdbcRepository.findAllCodCuentaContable(repartoTipo))
        .thenReturn(resultado);
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class)))
        .thenThrow(NullPointerException.class);

    //then
    assertEquals(3,cuentaContableJdbcRepository.registerNiifByExcel(excel,repartoTipo).size());
  }

  @Test
  public void shouldNotSaveNiifExcel()
  {
    //given
    List<List<CellData>> excel= llenarExcelMal();

    //then
    assertEquals(2,cuentaContableJdbcRepository.registerNiifByExcel(excel,repartoTipo).size());
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

  private List<List<CellData>> llenarExcelNiifBien()
  {
    List<List<CellData>> excel = new ArrayList<>();
    List<CellData> fila1 = new ArrayList<>();
    List<CellData> fila2 = new ArrayList<>();
    List<CellData> fila3 = new ArrayList<>();

    for (int i = 0 ; i<=1 ; i++)
    {
      fila1.add(new CellData());
      fila2.add(new CellData());
      fila3.add(new CellData());
    }

    fila1.get(0).setValue("codigo");
    fila2.get(0).setValue("codigo");
    fila3.get(0).setValue("");

    excel.add(fila1);
    excel.add(fila2);
    excel.add(fila3);

    return excel;
  }

}
