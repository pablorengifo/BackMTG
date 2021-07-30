package pe.com.pacifico.kuntur.business.impl;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.request.MovCentroRequest;
import pe.com.pacifico.kuntur.expose.response.MovCentroResponse;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MovCentroJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovCentroServiceImplTest {

  @Mock
  private MovCentroJpaRepository jpaRepository;

  @InjectMocks
  private MovCentroServiceImpl service;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MovCentro centro1 = new MovCentro();
  MovCentro centro2 = new MovCentro();
  MovCentro centro3 = new MovCentro();
  MovCentroRequest request = new MovCentroRequest();
  String codigo="any";

  @Value("${date-format-allowed.data-format}")
  private String dateFormatAllowed;

  @Value("${date-format-allowed.data-format-string}")
  private String dateFormatStringAllowed;

  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(jpaRepository.findAll(repartoTipo,periodo))
        .thenReturn(Arrays.asList(centro1,centro2,centro3));

    //Then
    Flux<MovCentroResponse> flux= service.getCentros(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldRegisterAnAccount()
  {
    //When
    when(jpaRepository.save(any(MovCentroRequest.class))).thenReturn(1);
    //Then
    assertThat(service.registerCentro(request), Is.is(1));
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(jpaRepository.delete(repartoTipo,periodo,codigo)).thenReturn(true);

    //Then
    assertThat(service.deleteCentro(repartoTipo,periodo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFileOK() throws IOException, ValidateException {
    // Given
    List<String> list = new ArrayList<>();
    SXSSFWorkbook wb = new SXSSFWorkbook();
    String filePath = Constant.TMP_PATH + File.separator + "clearName";
    wb.createSheet();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      wb.write(bos);
    } finally {
      bos.close();
    }
    byte[] bytes = bos.toByteArray();
    File file = new File(filePath);
    FileCopyUtils.copy(bytes, file);
    RequestFileRead requestFileRead = new RequestFileRead(java.util.Base64.getEncoder().encodeToString(bytes));
    List<List<CellData>> listExcelBody = ExcelUtil.getRowList(filePath, dateFormatAllowed, dateFormatStringAllowed,0);
    //When
    when(jpaRepository.saveExcelToBD(listExcelBody, repartoTipo, periodo)).thenReturn(list);
    //Then
    //Assertions.assertNotNull(service.fileRead(repartoTipo,requestFileRead,periodo ));
    Assertions.assertEquals(list, service.fileRead(repartoTipo, requestFileRead, periodo));
    FileUtil.removeTempFile("clearName");
  }

  @Test
  public void shouldReadAFileError1() {
    //When
    RequestFileRead requestFileRead = new RequestFileRead(null);
    //Then
    Assertions.assertNotNull(service.fileRead(repartoTipo,requestFileRead,periodo ));
  }

  @Test
  public void shouldReadAFileError2() {
    //When

    //Then
    Assertions.assertNotNull(service.fileRead(repartoTipo,requestFileRead,periodo ));
  }

  @Test
  public void shouldGetCentro() {
    // When
    when(jpaRepository.findByCodCentro(codigo)).thenReturn(centro1);
    // Then
    Mono<MovCentroResponse> flux= service.getCentro(codigo);
    Assertions.assertNotNull(flux);
  }

  @Test
  public void shouldUpdateCentroError() {
    MovCentroRequest request = new MovCentroRequest();
    request.setCodCentro(codigo);
    // When
    when(jpaRepository.findByCodCentro(codigo)).thenReturn(null);
    // Then
    Assertions.assertNotNull(service.updateCentro(request));
  }

  @Test
  public void shouldUpdateCentroOK() {
    MovCentroRequest request = new MovCentroRequest();
    request.setCodCentro(codigo);
    // When
    when(jpaRepository.findByCodCentro(codigo)).thenReturn(centro1);
    when(jpaRepository.update(request)).thenReturn(1);
    // Then
    Assertions.assertNotNull(service.updateCentro(request));
  }
}
