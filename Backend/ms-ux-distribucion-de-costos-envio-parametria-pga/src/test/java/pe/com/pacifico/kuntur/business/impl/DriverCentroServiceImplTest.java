package pe.com.pacifico.kuntur.business.impl;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.request.DriverCentroRequest;
import pe.com.pacifico.kuntur.expose.response.MaDriverResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverCentroResponse;
import pe.com.pacifico.kuntur.model.MaDriver;
import pe.com.pacifico.kuntur.model.MovDriverCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.DriverCentroJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DriverCentroServiceImplTest {

  @Mock
  private DriverCentroJpaRepository jpaRepository;

  @InjectMocks
  private DriverCentroServiceImpl service;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaDriver driver1 = new MaDriver();
  MaDriver driver2 = new MaDriver();
  MaDriver driver3 = new MaDriver();
  MovDriverCentro driver4 = new MovDriverCentro();
  MovDriverCentro driver5 = new MovDriverCentro();
  MovDriverCentro driver6 = new MovDriverCentro();
  DriverCentroRequest request = new DriverCentroRequest();
  String codigo="any";

  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(jpaRepository.findAll(repartoTipo,periodo))
        .thenReturn(Arrays.asList(driver1,driver2,driver3));

    //Then
    Flux<MaDriverResponse> flux= service.getMaDriverCentro(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
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


  @Test
  public void shouldRegisterAnAccount()
  {
    //When
    when(jpaRepository.save(request)).thenReturn(1);

    //Then
    assertThat(service.registerDriverCentro(request),Is.is(1));
  }

  @Test
  public void shouldUpdateAnAccount()
  {
    //given
    request.setRepartoTipo(0);
    request.setCodDriver("");
    //When
    when(jpaRepository.findByCodDriver(anyString())).thenReturn(driver4);
    //Then
    service.updateDriverCentro(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(jpaRepository.delete(repartoTipo,periodo,codigo)).thenReturn(true);

    //Then
    assertThat(service.deleteMovDriverCentro(repartoTipo,periodo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When

    //Then
    Assertions.assertNull(service.fileRead(repartoTipo,requestFileRead,periodo));
  }
}
