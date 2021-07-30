package pe.com.pacifico.kuntur.expose;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.MaPartidaService;
import pe.com.pacifico.kuntur.expose.request.MaPartidaRequest;
import pe.com.pacifico.kuntur.expose.response.MaPartidaResponse;
import pe.com.pacifico.kuntur.expose.response.MovSubcanalResponse;
import pe.com.pacifico.kuntur.model.MaPartida;
import pe.com.pacifico.kuntur.model.MovSubcanal;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaPartidaControllerTest {
  @Mock
  private  MaPartidaService maPartidaService;

  @InjectMocks
  private MaPartidaController maPartidaController;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaPartida partida = new MaPartida();
  MaPartidaResponse partidaResponse1 = new MaPartidaResponse();
  MaPartidaResponse partidaResponse2 = new MaPartidaResponse();
  MaPartidaResponse partidaResponse3 = new MaPartidaResponse();
  MaPartidaRequest request1 = new MaPartidaRequest();
  MaPartidaRequest request2 = new MaPartidaRequest();
  String codigo="any";

  @Test
  public void shouldGetAllItems()
  {
    //When
    when(maPartidaService.getPartidas(repartoTipo))
        .thenReturn(Flux.just(partidaResponse1, partidaResponse2, partidaResponse3));

    //Then
    Flux<MaPartidaResponse> flux= maPartidaController.getPartidas(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetAllItemsNotInMov()
  {
    //When
    when(maPartidaService.getPartidasNotInMov(repartoTipo,periodo))
        .thenReturn(Flux.just(partidaResponse1, partidaResponse2));

    //Then
    Flux<MaPartidaResponse> flux= maPartidaController.getPartidasNotInMov(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(2).verifyComplete();
  }

  @Test
  public void shouldGetAnItem()
  {
    //When
    when(maPartidaService.getPartida(repartoTipo,codigo))
        .thenReturn(Mono.just(partidaResponse1));

    //Then
    maPartidaController.getPartidaByCodPartida(repartoTipo,codigo).subscribe(response -> Assertions.assertNotNull(response));
  }

  @Test
  public void shouldRegisterAnItem()
  {
    //When
    when(maPartidaService.registerPartida(request1)).thenReturn(Mono.just(partida));

    //Then
    maPartidaController.registerCuenta(request1).subscribe(response -> Assertions.assertNotNull(response));
  }

  @Test
  public void shouldUpdateAnItem()
  {
    //When
    when(maPartidaService.updatePartida(request2)).thenReturn(Mono.just(partida));
    //Then
    maPartidaController.updatePartida(request2).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnItem()
  {
    //When
    when(maPartidaService.deletePartida(repartoTipo,codigo)).thenReturn(true);

    //Then
    assertThat(maPartidaController.deleteCuenta(repartoTipo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When
    when(maPartidaService.fileRead(repartoTipo,requestFileRead)).thenReturn(new ArrayList<>());
    //Then
    Assertions.assertNotNull(maPartidaController.fileRead(repartoTipo,requestFileRead));
  }

  @Test
  public void shouldBuildResponse() {
    MaPartida m = new MaPartida();
    MaPartidaResponse a = new MaPartidaResponse();
    // When
    lenient().when(maPartidaService.getMaPartidaResponseFromMaPartida(any(MaPartida.class))).thenReturn(a);
    // Then
    Assertions.assertNotNull(maPartidaController.buildResponse(m));
  }

}
