package pe.com.pacifico.kuntur.expose;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.MaCanalService;
import pe.com.pacifico.kuntur.expose.request.MaCanalRequest;
import pe.com.pacifico.kuntur.expose.response.MaCanalResponse;
import pe.com.pacifico.kuntur.model.MaCanal;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaCanalControllerTest {

  @Mock
  private MaCanalService service;

  @InjectMocks
  private MaCanalController controller;

  //Given
  int repartoTipo=1;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaCanal linea = new MaCanal();
  MaCanalResponse response1 = new MaCanalResponse();
  MaCanalResponse response2 = new MaCanalResponse();
  MaCanalResponse response3 = new MaCanalResponse();
  MaCanalRequest request = new MaCanalRequest();
  String codigo="any";

  @Test
  public void shouldGetAllItems()
  {
    //When
    when(service.getCanales(repartoTipo))
        .thenReturn(Flux.just(response1, response2, response3));

    //Then
    Flux<MaCanalResponse> flux= controller.getCanales(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }


  @Test
  public void shouldRegisterAnItem()
  {
    //When
    when(service.registerCanal(request)).thenReturn(Mono.just(linea));

    //Then
    controller.registerCuenta(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldUpdateAnItem()
  {
    //When
    when(service.updateCanal(request)).thenReturn(Mono.just(linea));
    //Then
    controller.updateCanal(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnItem()
  {
    //When
    when(service.deleteCanal(repartoTipo,codigo)).thenReturn(true);

    //Then
    assertThat(controller.deleteCuenta(repartoTipo,codigo), Is.is(true));
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
