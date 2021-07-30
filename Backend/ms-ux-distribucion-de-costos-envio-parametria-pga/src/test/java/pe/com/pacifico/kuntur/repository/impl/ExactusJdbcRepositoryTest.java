package pe.com.pacifico.kuntur.repository.impl;

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
import pe.com.pacifico.kuntur.model.DetalleGasto;
import pe.com.pacifico.kuntur.model.Exactus;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExactusJdbcRepositoryTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private ExactusJdbcRepository jdbcRepository;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  Exactus exactus = new Exactus();

  @Test
  void shouldfindAllExactus()
  {
    //when
    doReturn(Arrays.asList(exactus,exactus))
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt(),anyInt(),anyInt());

    //then
    assertEquals(2,jdbcRepository.findAll(repartoTipo,periodo).size());
    assertEquals(exactus, jdbcRepository.findAll(repartoTipo,periodo).get(0));
  }

  @Test
  void shouldNotFindAllExactus()
  {
    //when
    doThrow(EmptyResultDataAccessException.class)
        .when(jdbcTemplate).query(anyString(),
        any(BeanPropertyRowMapper.class),anyInt(),anyInt(),anyInt(),anyInt());

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
      when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyInt()))
          .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertEquals(0,jdbcRepository.findAllCodCentro(repartoTipo,periodo).size());
  }

  @Test
  public void shouldFindAllPartidaCodes()
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
  public void shouldNotFindAllPartidaCodes()
  {
    //given
    repartoTipo=2;

      when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyInt()))
          .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertEquals(0,jdbcRepository.findAllCodPartida(repartoTipo,periodo).size());
  }

  @Test
  public void shouldFindAllCuentaContable()
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
  public void shouldNotFindAllCuentaContable()
  {
    //given
    repartoTipo=2;

    //when
    when(jdbcTemplate.queryForList(anyString(), any(), anyInt(),anyInt()))
          .thenThrow(EmptyResultDataAccessException.class);

    //then
    assertEquals(0,jdbcRepository.findAllCodCuentaContable(repartoTipo,periodo).size());
  }

  @Test
  public void shouldSaveExcel()
  {
    //given
    List<List<CellData>> excel= llenarExcelBien();

    //when
    when(jdbcRepository.findAllCodCuentaContable(1,periodo))
        .thenReturn(Arrays.asList("codigo.cod","1.1"));
    when(jdbcRepository.findAllCodPartida(1,periodo))
        .thenReturn(Arrays.asList("codigo.cod","FI00"));
    when(jdbcRepository.findAllCodCentro(1,periodo))
        .thenReturn(Arrays.asList("codigo.cod","60.60"));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).thenReturn(null);

    //then
    assertEquals(7,jdbcRepository.saveExcelToBd(excel, repartoTipo, periodo).size());
  }

  @Test
  public void shouldNotSaveExcelByErrorInDB()
  {
    //given
    List<List<CellData>> excel= llenarExcelBien();

    //when
    when(jdbcRepository.findAllCodCuentaContable(1,periodo))
        .thenReturn(Arrays.asList("codigo.cod","1.1"));
    when(jdbcRepository.findAllCodPartida(1,periodo))
        .thenReturn(Arrays.asList("codigo.cod","FI00"));
    when(jdbcRepository.findAllCodCentro(1,periodo))
        .thenReturn(Arrays.asList("codigo.cod","60.60"));
    when(jdbcTemplate.batchUpdate(anyString(), any(BatchPreparedStatementSetter.class))).
        thenThrow(NullPointerException.class);

    //then
    assertEquals(8,jdbcRepository.saveExcelToBd(excel,repartoTipo, periodo).size());
  }

  @Test
  public void shouldNotSaveExcel()
  {
    //Given
    List<List<CellData>> excel = llenarExcelMal();

    //then
    assertNotNull(jdbcRepository.saveExcelToBd(excel,repartoTipo, periodo));
  }

  private  List<List<CellData>> llenarExcelBien()
  {

    List<List<CellData>> excel = new ArrayList<>();
    List<CellData> fila1 = new ArrayList<>();
    List<CellData> fila2 = new ArrayList<>();
    List<CellData> fila3 = new ArrayList<>();
    List<CellData> fila4 = new ArrayList<>();

    for (int i = 0 ; i<=20 ; i++)
    {
      fila1.add(new CellData());
      fila2.add(new CellData());
      fila3.add(new CellData());
      fila4.add(new CellData());
    }

    fila1.get(0).setValue("CUENTA CONTABLE");
    fila1.get(1).setValue("");
    fila1.get(2).setValue("ASIENTO");
    fila1.get(3).setValue("TIPO DE DOCUMENTO");
    fila1.get(4).setValue("DOCUMENTO");
    fila1.get(5).setValue("REFERENCIA");
    fila1.get(6).setValue("DÉBITO LOCAL");
    fila1.get(7).setValue("DÉBITO DOLAR");
    fila1.get(8).setValue("CRÉDITO LOCAL");
    fila1.get(9).setValue("CRÉDITO DOLAR");
    fila1.get(10).setValue("CENTRO COSTO");
    fila1.get(11).setValue("");
    fila1.get(12).setValue("TIPO DE ASIENTO");
    fila1.get(13).setValue("FECHA");
    fila1.get(14).setValue("NIT");
    fila1.get(15).setValue("RAZÓN SOCIAL");
    fila1.get(16).setValue("");
    fila1.get(17).setValue("");
    fila1.get(18).setValue("");
    fila1.get(19).setValue("");
    fila1.get(20).setValue("Partida");

    fila2.get(0).setValue("codigo.cod");
    fila2.get(1).setValue("");
    fila2.get(2).setValue("ASIENTO");
    fila2.get(3).setValue("TIPO DE DOCUMENTO");
    fila2.get(4).setValue("DOCUMENTO");
    fila2.get(5).setValue("REFERENCIA");
    fila2.get(6).setValue("");
    fila2.get(7).setValue("");
    fila2.get(8).setValue("");
    fila2.get(9).setValue("");
    fila2.get(10).setValue("codigo.cod");
    fila2.get(11).setValue("");
    fila2.get(12).setValue("TIPO DE ASIENTO");
    fila2.get(13).setValue("12/11/1997");
    fila2.get(14).setValue("NIT");
    fila2.get(15).setValue("RAZÓN SOCIAL");
    fila2.get(16).setValue("");
    fila2.get(17).setValue("");
    fila2.get(18).setValue("");
    fila2.get(19).setValue("");
    fila2.get(20).setValue("codigo.cod");

    fila3.get(0).setValue("");
    fila3.get(1).setValue("");
    fila3.get(2).setValue("ASIENTO");
    fila3.get(3).setValue("TIPO DE DOCUMENTO");
    fila3.get(4).setValue("DOCUMENTO");
    fila3.get(5).setValue("REFERENCIA");
    fila3.get(6).setValue("");
    fila3.get(7).setValue("");
    fila3.get(8).setValue("");
    fila3.get(9).setValue("");
    fila3.get(10).setValue("");
    fila3.get(11).setValue("");
    fila3.get(12).setValue("TIPO DE ASIENTO");
    fila3.get(13).setValue("12/11/1997");
    fila3.get(14).setValue("NIT");
    fila3.get(15).setValue("RAZÓN SOCIAL");
    fila3.get(16).setValue("");
    fila3.get(17).setValue("");
    fila3.get(18).setValue("");
    fila3.get(19).setValue("");
    fila3.get(20).setValue("");

    fila4.get(0).setValue("1.1");
    fila4.get(1).setValue("");
    fila4.get(2).setValue("ASIENTO");
    fila4.get(3).setValue("TIPO DE DOCUMENTO");
    fila4.get(4).setValue("DOCUMENTO");
    fila4.get(5).setValue("REFERENCIA");
    fila4.get(6).setValue("a");
    fila4.get(7).setValue("a");
    fila4.get(8).setValue("a");
    fila4.get(9).setValue("a");
    fila4.get(10).setValue("60.60");
    fila4.get(11).setValue("");
    fila4.get(12).setValue("TIPO DE ASIENTO");
    fila4.get(13).setValue("fecha");
    fila4.get(14).setValue("12345678");
    fila4.get(15).setValue("RAZÓN SOCIAL");
    fila4.get(16).setValue("");
    fila4.get(17).setValue("");
    fila4.get(18).setValue("");
    fila4.get(19).setValue("");
    fila4.get(20).setValue("");

    excel.add(fila1);
    excel.add(fila2);
    excel.add(fila3);
    excel.add(fila4);

    return excel;
  }

  private  List<List<CellData>> llenarExcelMal()
  {
    List<List<CellData>> excel = new ArrayList<>();
    List<CellData> fila1 = new ArrayList<>();

    for (int i = 0 ; i<=20 ; i++)
    {
      fila1.add(new CellData());
    }

    fila1.get(0).setValue("");
    fila1.get(1).setValue("");
    fila1.get(2).setValue("");
    fila1.get(3).setValue("");
    fila1.get(4).setValue("");
    fila1.get(5).setValue("");
    fila1.get(6).setValue("");
    fila1.get(7).setValue("");
    fila1.get(8).setValue("");
    fila1.get(9).setValue("");
    fila1.get(10).setValue("");
    fila1.get(11).setValue("");
    fila1.get(12).setValue("");
    fila1.get(13).setValue("");
    fila1.get(14).setValue("");
    fila1.get(15).setValue("");
    fila1.get(16).setValue("");
    fila1.get(17).setValue("");
    fila1.get(18).setValue("");
    fila1.get(19).setValue("");
    fila1.get(20).setValue("");

    excel.add(fila1);

    return excel;
  }

}
