package pe.com.pacifico.kuntur.business.impl;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverCascadaRequest;
import pe.com.pacifico.kuntur.expose.response.AsignacionDriverCascadaResponse;
import pe.com.pacifico.kuntur.model.AsignacionDriverCascada;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.AsignacionDriverCascadaJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AsignacionDriverCascadaServiceImplTest {

  @Mock
  private AsignacionDriverCascadaJpaRepository jpaRepository;

  @InjectMocks
  private AsignacionDriverCascadaServiceImpl service;

  //Given
  int repartoTipo=1;
  int periodo = 202105;

  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  AsignacionDriverCascada asignacion = new AsignacionDriverCascada();
  AsignacionDriverCascada asignacion1 = new AsignacionDriverCascada();
  AsignacionDriverCascada asignacion2 = new AsignacionDriverCascada();
  AsignacionDriverCascada asignacion3 = new AsignacionDriverCascada();
  AsignacionDriverCascadaRequest request = new AsignacionDriverCascadaRequest();
  String codigo="any";

  @Test
  public void shouldGetAsignaciones(){
    //When
    when(jpaRepository.findAll(repartoTipo, periodo))
        .thenReturn(Arrays.asList(asignacion1,asignacion2,asignacion3));

    //Then
    Flux<AsignacionDriverCascadaResponse> flux= service.getAsignaciones(repartoTipo, periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldReadAFile(){
    //When
    //Then
    Assertions.assertNotNull(service.fileRead(repartoTipo,requestFileRead, periodo));
  }

  @Test
  public void shouldDeleteAsignacion(){
    //When
    when(jpaRepository.delete(repartoTipo, periodo, codigo)).thenReturn(true);

    //Then
    assertThat(service.deleteAsignacion(repartoTipo,periodo, codigo), Is.is(true));
  }

  @Test
  public void shouldRegisterDriverCascada(){
    //When
    //Then
    service.registerDriverCascada(request).subscribe(Assertions::assertNotNull);
  }
}
