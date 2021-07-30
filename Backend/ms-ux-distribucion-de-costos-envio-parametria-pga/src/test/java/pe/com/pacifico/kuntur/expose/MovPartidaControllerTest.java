package pe.com.pacifico.kuntur.expose;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.MovPartidaService;
import pe.com.pacifico.kuntur.expose.request.MovPartidaRequest;
import pe.com.pacifico.kuntur.expose.response.MovPartidaResponse;
import pe.com.pacifico.kuntur.expose.response.MovProductoResponse;
import pe.com.pacifico.kuntur.model.MovPartida;
import pe.com.pacifico.kuntur.model.MovProducto;
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
public class MovPartidaControllerTest {
  @Mock
  private MovPartidaService movPartidaService;

  @InjectMocks
  private MovPartidaController controller;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MovPartidaResponse partidaResponse1 = new MovPartidaResponse();
  MovPartidaResponse partidaResponse2 = new MovPartidaResponse();
  MovPartidaResponse partidaResponse3 = new MovPartidaResponse();
  MovPartidaRequest request = new MovPartidaRequest();
  String codigo="any";

  @Test
  public void shouldGetAllAcounts()
  {
    //When
    when(movPartidaService.getPartidas(repartoTipo,periodo))
        .thenReturn(Flux.just(partidaResponse1, partidaResponse2, partidaResponse3));

    //Then
    Flux<MovPartidaResponse> flux= controller.getPartidas(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }


  @Test
  public void shouldRegisterAnAccount()
  {
    //When
    when(movPartidaService.registerPartida(request)).thenReturn(1);

    //Then
    assertThat(controller.registerPartida(request),Is.is(1));
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(movPartidaService.deletePartida(repartoTipo,periodo,codigo)).thenReturn(true);

    //Then
    assertThat(controller.deletePartida(repartoTipo,periodo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When
    when(movPartidaService.fileRead(repartoTipo,requestFileRead,periodo)).thenReturn(new ArrayList<>());
    //Then
    Assertions.assertNotNull(controller.fileRead(requestFileRead,repartoTipo,periodo));
  }

  @Test
  public void shouldBuildResponse() {
    MovPartida m = new MovPartida();
    MovPartidaResponse a = new MovPartidaResponse();
    // When
    lenient().when(movPartidaService.getMovPartidaResponseFromMovPartida(any(MovPartida.class))).thenReturn(a);
    // Then
    Assertions.assertNotNull(controller.buildResponse(m));
  }
}
