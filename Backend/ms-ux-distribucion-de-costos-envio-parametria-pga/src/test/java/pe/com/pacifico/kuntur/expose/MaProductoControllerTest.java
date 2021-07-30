package pe.com.pacifico.kuntur.expose;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.MaProductoService;
import pe.com.pacifico.kuntur.expose.request.MaProductoRequest;
import pe.com.pacifico.kuntur.expose.response.MaProductoResponse;
import pe.com.pacifico.kuntur.model.MaProducto;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaProductoControllerTest {

  @Mock
  private MaProductoService service;

  @InjectMocks
  private MaProductoController controller;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaProducto producto = new MaProducto();
  MaProductoResponse response1 = new MaProductoResponse();
  MaProductoResponse response2 = new MaProductoResponse();
  MaProductoResponse response3 = new MaProductoResponse();
  MaProductoRequest request = new MaProductoRequest();
  String codigo="any";

  @Test
  public void shouldGetAllItems()
  {
    //When
    when(service.getProductos(repartoTipo))
        .thenReturn(Flux.just(response1, response2, response3));

    //Then
    Flux<MaProductoResponse> flux= controller.getProductos(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetAllItemsNotInMov()
  {
    //When
    when(service.getProductosNotInMovProducto(repartoTipo,periodo))
        .thenReturn(Flux.just(response1, response2));

    //Then
    Flux<MaProductoResponse> flux= controller.getProductosNotInMov(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(2).verifyComplete();
  }

  @Test
  public void shouldGetAnItem()
  {
    //When
    when(service.getProducto(repartoTipo,codigo))
        .thenReturn(Mono.just(response1));

    //Then
    controller.getProductoByCodProducto(repartoTipo,codigo).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldRegisterAnItem()
  {
    //When
    when(service.registerProducto(request)).thenReturn(Mono.just(producto));

    //Then
    controller.registerProducto(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldUpdateAnItem()
  {
    //When
    when(service.updateProducto(request)).thenReturn(Mono.just(producto));
    //Then
    controller.updateProducto(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnItem()
  {
    //When
    when(service.deleteProducto(repartoTipo,codigo)).thenReturn(true);

    //Then
    assertThat(controller.deleteProducto(repartoTipo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When
    when(service.fileRead(repartoTipo,requestFileRead)).thenReturn(new ArrayList<>());
    //Then
    Assertions.assertNotNull(controller.fileRead(repartoTipo,requestFileRead));
  }


}
