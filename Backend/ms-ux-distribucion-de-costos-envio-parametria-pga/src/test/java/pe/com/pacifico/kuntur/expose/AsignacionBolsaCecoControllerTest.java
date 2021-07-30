package pe.com.pacifico.kuntur.expose;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.AsignacionCecoBolsaService;
import pe.com.pacifico.kuntur.business.DetalleGastoService;
import pe.com.pacifico.kuntur.expose.response.AsignacionCecoBolsaResponse;
import pe.com.pacifico.kuntur.expose.response.DetalleGastoResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverCentroResponse;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AsignacionBolsaCecoControllerTest {
  @Mock
  private AsignacionCecoBolsaService service;

  @InjectMocks
  private AsignacionCecoBolsaController controller;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  AsignacionCecoBolsaResponse response1 = new AsignacionCecoBolsaResponse();
  AsignacionCecoBolsaResponse response2 = new AsignacionCecoBolsaResponse();
  AsignacionCecoBolsaResponse response3 = new AsignacionCecoBolsaResponse();
  MovDriverCentroResponse response4 = new MovDriverCentroResponse();
  MovDriverCentroResponse response5 = new MovDriverCentroResponse();
  MovDriverCentroResponse response6 = new MovDriverCentroResponse();
  String codigo ="any";

  @Test
  public void shouldGetAllItems()
  {
    //When
    when(service.getAsignaciones(repartoTipo,periodo))
        .thenReturn(Flux.just(response1, response2, response3));

    //Then
    Flux<AsignacionCecoBolsaResponse> flux= controller.getAsignaciones(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
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
  public void shouldGetAllMovItems()
  {
    //When
    when(service.getMovDriversCentro(repartoTipo,periodo,codigo))
        .thenReturn(Flux.just(response4, response5, response6));

    //Then
    Flux<MovDriverCentroResponse> flux= controller.getMovDriversByMaDriver(repartoTipo,periodo,codigo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

}
