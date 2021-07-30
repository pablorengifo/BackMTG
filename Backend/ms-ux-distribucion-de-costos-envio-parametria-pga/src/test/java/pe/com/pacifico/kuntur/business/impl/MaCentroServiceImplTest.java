package pe.com.pacifico.kuntur.business.impl;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.request.MaCentroRequest;
import pe.com.pacifico.kuntur.expose.response.MaCentroResponse;
import pe.com.pacifico.kuntur.model.MaCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MaCentroJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaCentroServiceImplTest {

  @Mock
  private MaCentroJpaRepository jpaRepository;

  @InjectMocks
  private MaCentroServiceImpl service;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaCentro centro = new MaCentro();
  MaCentro centro1 = new MaCentro();
  MaCentro centro2 = new MaCentro();
  MaCentro centro3 = new MaCentro();
  MaCentroRequest request1 = new MaCentroRequest();
  MaCentroRequest request2 = new MaCentroRequest();
  String codigo="any";

  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(jpaRepository.findAll(repartoTipo))
        .thenReturn(Arrays.asList(centro1,centro2,centro3));

    //Then
    Flux<MaCentroResponse> flux= service.getCentros(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetAllAccountsNotInMov()
  {
    //When
    when(jpaRepository.findAllNotInMovCentro(repartoTipo,periodo))
        .thenReturn(Arrays.asList(centro1,centro2));

    //Then
    Flux<MaCentroResponse> flux= service.getCentrosNotInMov(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(2).verifyComplete();
  }

  @Test
  public void shouldGetAnAccount()
  {
    //When
    when(jpaRepository.findByCodCentro(repartoTipo,codigo))
        .thenReturn(centro);

    //Then
    service.getCentro(repartoTipo,codigo).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldRegisterAnAccount()
  {
    //When
    when(jpaRepository.save(request1)).thenReturn(1);
    //Then
    service.registerCentro(request1).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldUpdateAnAccount()
  {
    //given
    request2.setRepartoTipo(0);
    request2.setCodCentro("");
    //When
    when(jpaRepository.findByCodCentro(anyInt(),anyString())).thenReturn(centro);
    //Then
    service.updateCentro(request2).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(jpaRepository.delete(repartoTipo,codigo)).thenReturn(true);

    //Then
    assertThat(service.deleteCentro(repartoTipo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When

    //Then
    Assertions.assertNotNull(service.fileRead(repartoTipo,requestFileRead));
  }
}
