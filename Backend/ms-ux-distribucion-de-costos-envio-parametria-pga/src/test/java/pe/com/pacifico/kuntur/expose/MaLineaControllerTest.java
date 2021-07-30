package pe.com.pacifico.kuntur.expose;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.MaLineaService;
import pe.com.pacifico.kuntur.expose.request.MaLineaRequest;
import pe.com.pacifico.kuntur.expose.response.MaLineaResponse;
import pe.com.pacifico.kuntur.model.MaLinea;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaLineaControllerTest {

  @Mock
  private MaLineaService service;

  @InjectMocks
  private MaLineaController controller;

  //Given
  int repartoTipo=1;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaLinea linea = new MaLinea();
  MaLineaResponse response1 = new MaLineaResponse();
  MaLineaResponse response2 = new MaLineaResponse();
  MaLineaResponse response3 = new MaLineaResponse();
  MaLineaRequest request = new MaLineaRequest();
  String codigo="any";

  @Test
  public void shouldGetAllItems()
  {
    //When
    when(service.getLineas(repartoTipo))
        .thenReturn(Flux.just(response1, response2, response3));

    //Then
    Flux<MaLineaResponse> flux= controller.getLineas(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }


  @Test
  public void shouldRegisterAnItem()
  {
    //When
    when(service.registerLinea(request)).thenReturn(Mono.just(linea));

    //Then
    controller.registerCuenta(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldUpdateAnItem()
  {
    //When
    when(service.updateLinea(request)).thenReturn(Mono.just(linea));
    //Then
    controller.updateLinea(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnItem()
  {
    //When
    when(service.deleteLinea(repartoTipo,codigo)).thenReturn(true);

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
