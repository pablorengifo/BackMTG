package pe.com.pacifico.kuntur.business.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.response.AsignacionCecoBolsaResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverCentroResponse;
import pe.com.pacifico.kuntur.model.AsignacionCecoBolsa;
import pe.com.pacifico.kuntur.model.MovDriverCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.AsignacionCecoBolsaJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AsignacionBolsaCecoServiceImplTest {
  @Mock
  private AsignacionCecoBolsaJpaRepository jpaRepository;

  @InjectMocks
  private AsignacionCecoBolsaServiceImpl service;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  AsignacionCecoBolsa acb1 = new AsignacionCecoBolsa();
  AsignacionCecoBolsa acb2 = new AsignacionCecoBolsa();
  AsignacionCecoBolsa acb3 = new AsignacionCecoBolsa();
  MovDriverCentro driver4 = new MovDriverCentro();
  MovDriverCentro driver5 = new MovDriverCentro();
  MovDriverCentro driver6 = new MovDriverCentro();
  String codigo="any";

  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(jpaRepository.findAll(repartoTipo,periodo))
        .thenReturn(Arrays.asList(acb1,acb2,acb3));

    //Then
    Flux<AsignacionCecoBolsaResponse> flux= service.getAsignaciones(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldReadAFile()
  {
    //When

    //Then
    Assertions.assertNotNull(service.fileRead(repartoTipo,requestFileRead,periodo));
  }

  @Test
  public void shouldGetAllMovAccounts()
  {
    //When
    when(jpaRepository.findAllMov(repartoTipo,periodo,codigo))
        .thenReturn(Arrays.asList(driver4,driver5,driver6));

    //Then
    Flux<MovDriverCentroResponse> flux= service.getMovDriversCentro(repartoTipo,periodo,codigo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

}
