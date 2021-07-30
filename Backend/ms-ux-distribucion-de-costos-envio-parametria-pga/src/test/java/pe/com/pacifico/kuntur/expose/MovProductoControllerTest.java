package pe.com.pacifico.kuntur.expose;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.MovProductoService;
import pe.com.pacifico.kuntur.expose.request.MovProductoRequest;
import pe.com.pacifico.kuntur.expose.response.MovCentroResponse;
import pe.com.pacifico.kuntur.expose.response.MovProductoResponse;
import pe.com.pacifico.kuntur.model.MovCentro;
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
public class MovProductoControllerTest {

  @Mock
  private MovProductoService service;

  @InjectMocks
  private MovProductoController controller;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MovProductoResponse response1 = new MovProductoResponse();
  MovProductoResponse response2 = new MovProductoResponse();
  MovProductoResponse response3 = new MovProductoResponse();
  MovProductoRequest request = new MovProductoRequest();
  String codigo="any";

  @Test
  public void shouldGetAllAcounts()
  {
    //When
    when(service.getProductos(repartoTipo,periodo))
        .thenReturn(Flux.just(response1, response2, response3));

    //Then
    Flux<MovProductoResponse> flux= controller.getProductos(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }


  @Test
  public void shouldRegisterAnAccount()
  {
    //When
    when(service.registerProducto(request)).thenReturn(1);

    //Then
    assertThat(controller.registerProducto(request), Is.is(1));
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(service.deleteProducto(repartoTipo,periodo,codigo)).thenReturn(true);

    //Then
    assertThat(controller.deleteProducto(repartoTipo,periodo,codigo), Is.is(true));
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
  public void shouldBuildResponse() {
    MovProducto m = new MovProducto();
    MovProductoResponse a = new MovProductoResponse();
    // When
    lenient().when(service.getMovProductoResponseFromMovProducto(any(MovProductoResponse.class))).thenReturn(a);
    // Then
    Assertions.assertNotNull(controller.buildResponse(m));
  }
}
