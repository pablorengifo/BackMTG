package pe.com.pacifico.kuntur.expose;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.MovCentroService;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverCascadaRequest;
import pe.com.pacifico.kuntur.expose.request.MovCentroRequest;
import pe.com.pacifico.kuntur.expose.response.MovCentroResponse;
import pe.com.pacifico.kuntur.model.AsignacionDriverCascada;
import pe.com.pacifico.kuntur.model.MaProducto;
import pe.com.pacifico.kuntur.model.MovCentro;
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
public class MovCentroControllerTest {
  @Mock
  private MovCentroService service;

  @InjectMocks
  private MovCentroController controller;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MovCentroResponse response1 = new MovCentroResponse();
  MovCentroResponse response2 = new MovCentroResponse();
  MovCentroResponse response3 = new MovCentroResponse();
  MovCentroRequest request = new MovCentroRequest();
  String codigo="any";
  MovCentro centro = new MovCentro();

  @Test
  public void shouldGetAllAcounts()
  {
    //When
    when(service.getCentros(repartoTipo,periodo))
        .thenReturn(Flux.just(response1, response2, response3));

    //Then
    Flux<MovCentroResponse> flux= controller.getCentros(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }


  @Test
  public void shouldRegisterAnAccount()
  {
    //When
    when(service.registerCentro(request)).thenReturn(1);

    //Then
    assertThat(controller.registerCentro(request), Is.is(1));
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(service.deleteCentro(repartoTipo,periodo,codigo)).thenReturn(true);

    //Then
    assertThat(controller.deleteCentro(repartoTipo,periodo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When
    when(service.fileRead(repartoTipo,requestFileRead,periodo)).thenReturn(new ArrayList<>());
    //Then
    Assertions.assertNotNull(controller.fileRead(requestFileRead,repartoTipo,periodo));
  }

  @Test
  public void shouldUpdateAnItem()
  {
    //When
    when(service.updateCentro(request)).thenReturn(Mono.just(centro));
    //Then
    controller.updateCentro(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldBuildResponse() {
    MovCentro m = new MovCentro();
    MovCentroResponse a = new MovCentroResponse();
    // When
    lenient().when(service.getMovCentroResponseFromMovCentro(any(MovCentro.class))).thenReturn(a);
    // Then
    Assertions.assertNotNull(controller.buildResponse(m));
  }
}
