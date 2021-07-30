package pe.com.pacifico.kuntur.expose;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.DriverCentroService;
import pe.com.pacifico.kuntur.expose.request.DriverCentroRequest;
import pe.com.pacifico.kuntur.expose.response.MaDriverResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverCentroResponse;
import pe.com.pacifico.kuntur.model.MovDriverCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DriverCentroControllerTest {

  @Mock
  private DriverCentroService service;

  @InjectMocks
  private DriverCentroController controller;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MovDriverCentro driverCentro = new MovDriverCentro();
  MaDriverResponse response1 = new MaDriverResponse();
  MaDriverResponse response2 = new MaDriverResponse();
  MaDriverResponse response3 = new MaDriverResponse();
  MovDriverCentroResponse response4 = new MovDriverCentroResponse();
  MovDriverCentroResponse response5 = new MovDriverCentroResponse();
  MovDriverCentroResponse response6 = new MovDriverCentroResponse();
  DriverCentroRequest request = new DriverCentroRequest();
  String codigo="any";

  @Test
  public void shouldGetAllItems()
  {
    //When
    when(service.getMaDriverCentro(repartoTipo,periodo))
        .thenReturn(Flux.just(response1, response2, response3));

    //Then
    Flux<MaDriverResponse> flux= controller.getDriverCentros(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetAllMovItems()
  {
    //When
    when(service.getMovDriversCentro(repartoTipo,periodo,codigo))
        .thenReturn(Flux.just(response4, response5, response6));

    //Then
    Flux<MovDriverCentroResponse> flux= controller.getMovDriversByMaDriver(repartoTipo,periodo,codigo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }


  @Test
  public void shouldRegisterAnItem()
  {
    //When
    when(service.registerDriverCentro(request)).thenReturn(1);

    //Then
    assertThat(controller.registerDriverCentro(request),Is.is(1));
  }

  @Test
  public void shouldUpdateAnItem()
  {
    //When
    when(service.updateDriverCentro(request)).thenReturn(Mono.just(driverCentro));
    //Then
    controller.updateDriverCentro(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnItem()
  {
    //When
    when(service.deleteMovDriverCentro(repartoTipo,periodo,codigo)).thenReturn(true);

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
}
