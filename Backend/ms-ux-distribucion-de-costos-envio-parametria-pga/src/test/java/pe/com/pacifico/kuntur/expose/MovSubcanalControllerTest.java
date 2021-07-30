package pe.com.pacifico.kuntur.expose;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.MovSubcanalService;
import pe.com.pacifico.kuntur.expose.request.MovSubcanalRequest;
import pe.com.pacifico.kuntur.expose.response.MovCuentaContableResponse;
import pe.com.pacifico.kuntur.expose.response.MovSubcanalResponse;
import pe.com.pacifico.kuntur.model.MovCuentaContable;
import pe.com.pacifico.kuntur.model.MovSubcanal;
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
public class MovSubcanalControllerTest {

  @Mock
  private MovSubcanalService service;

  @InjectMocks
  private MovSubcanalController controller;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MovSubcanalResponse response1 = new MovSubcanalResponse();
  MovSubcanalResponse response2 = new MovSubcanalResponse();
  MovSubcanalResponse response3 = new MovSubcanalResponse();
  MovSubcanalRequest request = new MovSubcanalRequest();
  String codigo="any";

  @Test
  public void shouldGetAllAcounts()
  {
    //When
    when(service.getSubcanales(repartoTipo,periodo))
        .thenReturn(Flux.just(response1, response2, response3));

    //Then
    Flux<MovSubcanalResponse> flux= controller.getSubcanales(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }


  @Test
  public void shouldRegisterAnAccount()
  {
    //When
    when(service.registerSubcanal(request)).thenReturn(1);

    //Then
    assertThat(controller.registerSubcanal(request), Is.is(1));
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(service.deleteSubcanal(repartoTipo,periodo,codigo)).thenReturn(true);

    //Then
    assertThat(controller.deleteSubcanal(repartoTipo,periodo,codigo), Is.is(true));
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
    MovSubcanal m = new MovSubcanal();
    MovSubcanalResponse a = new MovSubcanalResponse();
    // When
    lenient().when(service.getMovSubcanalResponseFromMovSubcanal(any(MovSubcanal.class))).thenReturn(a);
    // Then
    Assertions.assertNotNull(controller.buildResponse(m));
  }
}
