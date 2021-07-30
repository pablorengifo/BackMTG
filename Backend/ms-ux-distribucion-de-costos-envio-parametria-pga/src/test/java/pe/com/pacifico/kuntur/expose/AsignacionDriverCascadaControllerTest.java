package pe.com.pacifico.kuntur.expose;

import io.swagger.annotations.ApiOperation;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.*;
import pe.com.pacifico.kuntur.business.AsignacionDriverCascadaService;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverCascadaRequest;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverObjetoRequest;
import pe.com.pacifico.kuntur.expose.response.AsignacionCecoBolsaResponse;
import pe.com.pacifico.kuntur.expose.response.AsignacionDriverCascadaResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverCentroResponse;
import pe.com.pacifico.kuntur.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.validation.Valid;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AsignacionDriverCascadaControllerTest {
  @Mock
  private AsignacionDriverCascadaService service;

  @InjectMocks
  private AsignacionDriverCascadaController controller;

  //Given
  int repartoTipo=1;
  int periodo = 202105;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  String codigo ="any";

  AsignacionDriverCascadaResponse response1 = new AsignacionDriverCascadaResponse();
  AsignacionDriverCascadaResponse response2 = new AsignacionDriverCascadaResponse();
  AsignacionDriverCascadaResponse response3 = new AsignacionDriverCascadaResponse();
  MovDriverCentroResponse response4 = new MovDriverCentroResponse();
  MovDriverCentroResponse response5 = new MovDriverCentroResponse();
  MovDriverCentroResponse response6 = new MovDriverCentroResponse();
  AsignacionDriverCascadaRequest request1 = new AsignacionDriverCascadaRequest();
  AsignacionDriverCascada asignacion = new AsignacionDriverCascada();

  @Test
  public void shouldGetCanales() {
    //When
    when(service.getAsignaciones(repartoTipo,periodo))
        .thenReturn(Flux.just(response1, response2, response3));

    //Then
    Flux<AsignacionDriverCascadaResponse> flux= controller.getCanales(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldFileRead() {
    //When
    when(service.fileRead(repartoTipo,requestFileRead,periodo)).thenReturn(new ArrayList<>());
    //Then
    Assertions.assertNotNull(controller.fileRead(requestFileRead,repartoTipo,periodo));
  }

  @Test
  public void shouldgetMovDriversByCodDriver() {
    //When
    when(service.getDetalleMovCentro(repartoTipo,periodo,codigo))
        .thenReturn(Flux.just(response4, response5, response6));

    //Then
    Flux<MovDriverCentroResponse> flux= controller.getMovDriversByCodDriver(repartoTipo,periodo,codigo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldDeleteCuenta() {
    //When
    when(service.deleteAsignacion(repartoTipo, periodo, codigo)).thenReturn(true);
    //Then
    assertThat(controller.deleteCuenta(repartoTipo, periodo, codigo), Is.is(true));
  }

  @Test
  public void shouldRegisterCuenta() {
    //When
    when(service.registerDriverCascada(request1)).thenReturn(Mono.just(asignacion));

    //Then
    controller.registerCuenta(request1).subscribe(response -> {
      Assertions.assertNotNull(response);
    });
  }

  @Test
  public void shouldBuildResponse() {
    AsignacionDriverCascada a = new AsignacionDriverCascada();
    Mono<AsignacionDriverCascada> monoA = Mono.just(a);
    // When
    lenient().when(service.registerDriverCascada(any(AsignacionDriverCascadaRequest.class))).thenReturn(monoA);
    // Then
    Assertions.assertNotNull(controller.buildResponse(a));
  }
}
