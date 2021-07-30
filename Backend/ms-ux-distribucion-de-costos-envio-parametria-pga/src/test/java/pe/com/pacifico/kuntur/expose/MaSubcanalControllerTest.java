package pe.com.pacifico.kuntur.expose;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.MaSubcanalService;
import pe.com.pacifico.kuntur.expose.request.MaSubcanalRequest;
import pe.com.pacifico.kuntur.expose.response.MaSubcanalResponse;
import pe.com.pacifico.kuntur.model.MaSubcanal;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaSubcanalControllerTest {
  @Mock
  private MaSubcanalService service;

  @InjectMocks
  private MaSubcanalController controller;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaSubcanal producto = new MaSubcanal();
  MaSubcanalResponse response1 = new MaSubcanalResponse();
  MaSubcanalResponse response2 = new MaSubcanalResponse();
  MaSubcanalResponse response3 = new MaSubcanalResponse();
  MaSubcanalRequest request = new MaSubcanalRequest();
  String codigo="any";

  @Test
  public void shouldGetAllItems()
  {
    //When
    when(service.getSubcanales(repartoTipo))
        .thenReturn(Flux.just(response1, response2, response3));

    //Then
    Flux<MaSubcanalResponse> flux= controller.getSubcanales(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetAllItemsNotInMov()
  {
    //When
    when(service.getSubcanalesNotInMov(repartoTipo,periodo))
        .thenReturn(Flux.just(response1, response2));

    //Then
    Flux<MaSubcanalResponse> flux= controller.getSubcanalesNotInMov(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(2).verifyComplete();
  }

  @Test
  public void shouldGetAnItem()
  {
    //When
    when(service.getSubcanal(repartoTipo,codigo))
        .thenReturn(Mono.just(response1));

    //Then
    controller.getSubcanalByCodSubcanal(repartoTipo,codigo).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldRegisterAnItem()
  {
    //When
    when(service.registerSubcanal(request)).thenReturn(Mono.just(producto));

    //Then
    controller.registerSubcanal(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldUpdateAnItem()
  {
    //When
    when(service.updateSubcanal(request)).thenReturn(Mono.just(producto));
    //Then
    controller.updateSubcanal(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnItem()
  {
    //When
    when(service.deleteSubcanal(repartoTipo,codigo)).thenReturn(true);

    //Then
    assertThat(controller.deleteSubcanal(repartoTipo,codigo), Is.is(true));
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
