package pe.com.pacifico.kuntur.business.impl;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.request.MovPartidaRequest;
import pe.com.pacifico.kuntur.expose.response.MovCuentaContableResponse;
import pe.com.pacifico.kuntur.expose.response.MovPartidaResponse;
import pe.com.pacifico.kuntur.model.MovPartida;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MovPartidaJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovPartidaServiceImplTest {

  @Mock
  private MovPartidaJpaRepository jpaRepository;

  @InjectMocks
  private MovPartidaServiceImpl service;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MovPartida partida1 = new MovPartida();
  MovPartida partida2 = new MovPartida();
  MovPartida partida3 = new MovPartida();
  MovPartidaRequest request = new MovPartidaRequest();
  String codigo="any";

  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(jpaRepository.findAll(repartoTipo,periodo))
        .thenReturn(Arrays.asList(partida1,partida2,partida3));

    //Then
    Flux<MovPartidaResponse> flux= service.getPartidas(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldRegisterAnAccount()
  {
    //When
    when(jpaRepository.save(any(MovPartidaRequest.class))).thenReturn(1);
    //Then
    assertThat(service.registerPartida(request),Is.is(1));
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(jpaRepository.delete(repartoTipo,periodo,codigo)).thenReturn(true);

    //Then
    assertThat(service.deletePartida(repartoTipo,periodo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When

    //Then
    Assertions.assertNotNull(service.fileRead(repartoTipo,requestFileRead,periodo ));
  }
}
