package pe.com.pacifico.kuntur.expose;

import io.swagger.annotations.ApiOperation;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.com.pacifico.kuntur.business.AsignacionDriverCascadaService;
import pe.com.pacifico.kuntur.business.AsignacionDriverObjetoService;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverCascadaRequest;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverObjetoRequest;
import pe.com.pacifico.kuntur.expose.response.AsignacionDriverCascadaResponse;
import pe.com.pacifico.kuntur.expose.response.AsignacionDriverObjetoResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverCentroResponse;
import pe.com.pacifico.kuntur.model.AsignacionDriverCascada;
import pe.com.pacifico.kuntur.model.AsignacionDriverObjeto;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
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
public class AsignacionDriverObjetoControllerTest {

  @Mock
  private AsignacionDriverObjetoService service;

  @InjectMocks
  private AsignacionDriverObjetoController controller;

  //Given
  int repartoTipo=1;
  int periodo = 202105;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  String codigo ="any";
  String grupoGasto = "any";

  AsignacionDriverObjetoResponse response1 = new AsignacionDriverObjetoResponse();
  AsignacionDriverObjetoResponse response2 = new AsignacionDriverObjetoResponse();
  AsignacionDriverObjetoResponse response3 = new AsignacionDriverObjetoResponse();

  AsignacionDriverObjetoRequest request1 = new AsignacionDriverObjetoRequest();
  AsignacionDriverObjeto asignacion = new AsignacionDriverObjeto();

  @Test
  public void shouldGetAsignaciones() {
    //When
    when(service.getAsignaciones(repartoTipo,periodo))
        .thenReturn(Flux.just(response1, response2, response3));

    //Then
    Flux<AsignacionDriverObjetoResponse> flux= controller.getAsignaciones(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldRegisterAsignacion() {
    //When
    when(service.registerAsignacion(any(AsignacionDriverObjetoRequest.class))).thenReturn(Mono.just(asignacion));

    //Then
    controller.registerAsignacion(request1).subscribe(response -> {
      Assertions.assertNotNull(response);
    });
  }

  @Test
  public void shouldDeleteAsignacion() {
    //When
    when(service.deleteAsignacion(repartoTipo, periodo, codigo, grupoGasto)).thenReturn(true);
    //Then
    assertThat(controller.deleteAsignacion(repartoTipo, periodo, codigo, grupoGasto), Is.is(true));
  }

  @Test
  public void shouldFileRead() {
    //When
    when(service.fileRead(repartoTipo,requestFileRead,periodo)).thenReturn(new ArrayList<>());
    //Then
    Assertions.assertNotNull(controller.fileRead(requestFileRead,repartoTipo,periodo));
  }

  @Test
  public void shouldBuildResponse() {
    AsignacionDriverObjeto a = new AsignacionDriverObjeto();
    Mono<AsignacionDriverObjeto> monoA = Mono.just(a);
    // When
    lenient().when(service.registerAsignacion(any(AsignacionDriverObjetoRequest.class))).thenReturn(monoA);
    // Then
    Assertions.assertNotNull(controller.buildResponse(a));
  }
}
