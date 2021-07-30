package pe.com.pacifico.kuntur.business.impl;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.request.MaPartidaRequest;
import pe.com.pacifico.kuntur.expose.response.MaPartidaResponse;
import pe.com.pacifico.kuntur.model.MaPartida;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MaPartidaJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MaPartidaServiceImplTest {

  @Mock
  private MaPartidaJpaRepository maPartidaJpaRepository;

  @InjectMocks
  private MaPartidaServiceImpl maPartidaService;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaPartida partida = new MaPartida();
  MaPartida partida1 = new MaPartida();
  MaPartida partida2 = new MaPartida();
  MaPartida partida3 = new MaPartida();
  MaPartidaRequest request1 = new MaPartidaRequest();
  MaPartidaRequest request2 = new MaPartidaRequest();
  String codigo="any";

  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(maPartidaJpaRepository.findAll(repartoTipo))
        .thenReturn(Arrays.asList(partida1,partida2,partida3));

    //Then
    Flux<MaPartidaResponse> flux= maPartidaService.getPartidas(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetAllAccountsNotInMov()
  {
    //When
    when(maPartidaJpaRepository.findAllNotInMovPartida(repartoTipo,periodo))
        .thenReturn(Arrays.asList(partida1,partida2));

    //Then
    Flux<MaPartidaResponse> flux= maPartidaService.getPartidasNotInMov(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(2).verifyComplete();
  }

  @Test
  public void shouldGetAnAccount()
  {
    //When
    when(maPartidaJpaRepository.findByCodPartida(repartoTipo,codigo))
        .thenReturn(partida);

    //Then
    maPartidaService.getPartida(repartoTipo,codigo).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldRegisterAnAccount()
  {
    //When

    //Then
    maPartidaService.registerPartida(request1).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldUpdateAnAccount()
  {
    //given
    request2.setRepartoTipo(0);
    request2.setCodPartida("");
    //When
    when(maPartidaJpaRepository.findByCodPartida(anyInt(),anyString())).thenReturn(partida);
    //Then
    maPartidaService.updatePartida(request2).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(maPartidaJpaRepository.delete(repartoTipo,codigo)).thenReturn(true);

    //Then
    assertThat(maPartidaService.deletePartida(repartoTipo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When

    //Then
    Assertions.assertNotNull(maPartidaService.fileRead(repartoTipo,requestFileRead));
  }
}
