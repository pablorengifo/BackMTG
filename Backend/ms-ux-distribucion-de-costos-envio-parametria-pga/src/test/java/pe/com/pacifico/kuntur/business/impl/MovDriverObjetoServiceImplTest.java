package pe.com.pacifico.kuntur.business.impl;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.response.MaDriverResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverObjetoResponse;
import pe.com.pacifico.kuntur.model.MaDriver;
import pe.com.pacifico.kuntur.model.MovDriverObjeto;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MovDriverObjetoJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovDriverObjetoServiceImplTest {

  @Mock
  private MovDriverObjetoJpaRepository jpaRepository;

  @InjectMocks
  private MovDriverObjetoServiceImpl service;

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
  public void shouldGetAllAccounts()
  {
    //When
    when(jpaRepository.findAll(repartoTipo,periodo))
        .thenReturn(Arrays.asList(driver1,driver2,driver3));

    //Then
    Flux<MaDriverResponse> flux= service.getMaDriverObjeto(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetAllMovAccounts()
  {
    //When
    when(jpaRepository.findAllByPeriod(repartoTipo,periodo,codigo))
        .thenReturn(Arrays.asList(driver4,driver5,driver6));

    //Then
    Flux<MovDriverObjetoResponse> flux= service.getObjetos(repartoTipo,periodo,codigo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(jpaRepository.delete(repartoTipo,periodo,codigo)).thenReturn(true);

    //Then
    assertThat(service.deleteMovDriverObjeto(repartoTipo,periodo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When

    //Then
    Assertions.assertNull(service.fileRead(repartoTipo,periodo,requestFileRead));
  }


}
