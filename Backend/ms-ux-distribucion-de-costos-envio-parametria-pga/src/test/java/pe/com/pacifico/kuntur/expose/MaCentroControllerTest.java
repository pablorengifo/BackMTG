package pe.com.pacifico.kuntur.expose;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.MaCentroService;
import pe.com.pacifico.kuntur.expose.request.MaCentroRequest;
import pe.com.pacifico.kuntur.expose.response.MaCentroResponse;
import pe.com.pacifico.kuntur.model.MaCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaCentroControllerTest {
  @Mock
  private MaCentroService maCentroService;

  @InjectMocks
  private MaCentroController controller;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaCentro centro = new MaCentro();
  MaCentroResponse response1 = new MaCentroResponse();
  MaCentroResponse response2 = new MaCentroResponse();
  MaCentroResponse response3 = new MaCentroResponse();
  MaCentroRequest request1 = new MaCentroRequest();
  MaCentroRequest request2 = new MaCentroRequest();
  String codigo="any";

  @Test
  public void shouldGetAllItems()
  {
    /*//When
    when(maCentroService.getCentros(repartoTipo))
        .thenReturn(Flux.just(response1, response2, response3));

    //Then
    Flux<MaCentroResponse> flux= controller.getCentros(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();*/
  }

  @Test
  public void shouldGetAllItemsNotInMov()
  {
    //When
    when(maCentroService.getCentrosNotInMov(repartoTipo,periodo))
        .thenReturn(Flux.just(response1, response2));

    //Then
    Flux<MaCentroResponse> flux= controller.getCentrosNotInMov(periodo,repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(2).verifyComplete();
  }

  @Test
  public void shouldGetAnItem()
  {
    //When
    when(maCentroService.getCentro(repartoTipo,codigo))
        .thenReturn(Mono.just(response1));

    //Then
    controller.getCentroByCodCentro(codigo,repartoTipo).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldRegisterAnItem()
  {
    //When
    when(maCentroService.registerCentro(request1)).thenReturn(Mono.just(centro));

    //Then
    controller.registerCentro(request1).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldUpdateAnItem()
  {
    //When
    when(maCentroService.updateCentro(request2)).thenReturn(Mono.just(centro));
    //Then
    controller.updateCentro(request2).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnItem()
  {
    //When
    when(maCentroService.deleteCentro(repartoTipo,codigo)).thenReturn(true);

    //Then
    assertThat(controller.deleteCentro(codigo,repartoTipo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When
    when(maCentroService.fileRead(repartoTipo,requestFileRead)).thenReturn(new ArrayList<>());
    //Then
    Assertions.assertNotNull(controller.fileRead(repartoTipo,requestFileRead));
  }
}
