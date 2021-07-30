package pe.com.pacifico.kuntur.expose;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.MovDriverObjetoService;
import pe.com.pacifico.kuntur.expose.response.MaDriverResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverObjetoResponse;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovDriverObjetoControllerTest {

  @Mock
  private MovDriverObjetoService service;

  @InjectMocks
  private MovDriverObjetoController controller;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaDriverResponse response1 = new MaDriverResponse();
  MaDriverResponse response2 = new MaDriverResponse();
  MaDriverResponse response3 = new MaDriverResponse();
  MovDriverObjetoResponse response4 = new MovDriverObjetoResponse();
  MovDriverObjetoResponse response5 = new MovDriverObjetoResponse();
  MovDriverObjetoResponse response6 = new MovDriverObjetoResponse();
  String codigo="any";

  @Test
  public void shouldGetAllItems()
  {
    //When
    when(service.getMaDriverObjeto(repartoTipo,periodo))
        .thenReturn(Flux.just(response1, response2, response3));

    //Then
    Flux<MaDriverResponse> flux= controller.getDriverObjetos(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetAllMovItems()
  {
    //When
    when(service.getObjetos(repartoTipo,periodo,codigo))
        .thenReturn(Flux.just(response4, response5, response6));

    //Then
    Flux<MovDriverObjetoResponse> flux= controller.getMovDriverObjetos(repartoTipo,periodo,codigo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }
  @Test
  public void shouldDeleteAnItem()
  {
    //When
    when(service.deleteMovDriverObjeto(repartoTipo,periodo,codigo)).thenReturn(true);

    //Then
    assertThat(controller.deleteDriverObjeto(repartoTipo,periodo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When
    when(service.fileRead(repartoTipo,periodo,requestFileRead)).thenReturn(new ArrayList<>());
    //Then
    Assertions.assertNotNull(controller.fileRead(requestFileRead,repartoTipo,periodo));
  }

  @Test
  public void isSubiendoArchivo() {
    // Then
    Assertions.assertFalse(controller.subiendoArchivo());
  }

  @Test
  public void shouldObtenerErroresEnSubidaDeArchivo() {
    // Then
    Assertions.assertNotNull(controller.obtenerErroresEnSubidaDeArchivo());
  }


}
