package pe.com.pacifico.kuntur.business.impl;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.request.MaLineaRequest;
import pe.com.pacifico.kuntur.expose.response.MaLineaResponse;
import pe.com.pacifico.kuntur.model.MaLinea;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MaLineaJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaLineaServiceImplTest {

  @Mock
  private MaLineaJpaRepository jpaRepository;

  @InjectMocks
  private MaLineaServiceImpl service;

  //Given
  int repartoTipo=1;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaLinea linea = new MaLinea();
  MaLinea linea1 = new MaLinea();
  MaLinea linea2 = new MaLinea();
  MaLinea linea3 = new MaLinea();
  MaLineaRequest request = new MaLineaRequest();
  String codigo="any";

  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(jpaRepository.findAll(repartoTipo))
        .thenReturn(Arrays.asList(linea1,linea2,linea3));

    //Then
    Flux<MaLineaResponse> flux= service.getLineas(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }



  @Test
  public void shouldGetAnAccount()
  {
    //When
    when(jpaRepository.findByCodLinea(repartoTipo,codigo))
        .thenReturn(linea);

    //Then
    service.getLinea(repartoTipo,codigo).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldRegisterAnAccount()
  {
    //When

    //Then
    service.registerLinea(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldUpdateAnAccount()
  {
    //given
    request.setRepartoTipo(0);
    request.setCodLinea("");
    //When
    when(jpaRepository.findByCodLinea(anyInt(),anyString())).thenReturn(linea);
    //Then
    service.updateLinea(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(jpaRepository.delete(repartoTipo,codigo)).thenReturn(true);

    //Then
    assertThat(service.deleteLinea(repartoTipo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When

    //Then
    Assertions.assertNotNull(service.fileRead(repartoTipo,requestFileRead));
  }
}
