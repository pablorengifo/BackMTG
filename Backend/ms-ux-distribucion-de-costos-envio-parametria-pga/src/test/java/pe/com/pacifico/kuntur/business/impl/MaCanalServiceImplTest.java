package pe.com.pacifico.kuntur.business.impl;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.request.MaCanalRequest;
import pe.com.pacifico.kuntur.expose.response.MaCanalResponse;
import pe.com.pacifico.kuntur.model.MaCanal;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MaCanalJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaCanalServiceImplTest {

  @Mock
  private MaCanalJpaRepository jpaRepository;

  @InjectMocks
  private MaCanalServiceImpl service;

  //Given
  int repartoTipo=1;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaCanal canal = new MaCanal();
  MaCanal canal1 = new MaCanal();
  MaCanal canal2 = new MaCanal();
  MaCanal canal3 = new MaCanal();
  MaCanalRequest request = new MaCanalRequest();
  String codigo="any";

  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(jpaRepository.findAll(repartoTipo))
        .thenReturn(Arrays.asList(canal1,canal2,canal3));

    //Then
    Flux<MaCanalResponse> flux= service.getCanales(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }



  @Test
  public void shouldGetAnAccount()
  {
    //When
    when(jpaRepository.findByCodCanal(repartoTipo,codigo))
        .thenReturn(canal);

    //Then
    service.getCanal(repartoTipo,codigo).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldRegisterAnAccount()
  {
    //When

    //Then
    service.registerCanal(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldUpdateAnAccount()
  {
    //given
    request.setRepartoTipo(0);
    request.setCodCanal("");
    //When
    when(jpaRepository.findByCodCanal(anyInt(),anyString())).thenReturn(canal);
    //Then
    service.updateCanal(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(jpaRepository.delete(repartoTipo,codigo)).thenReturn(true);

    //Then
    assertThat(service.deleteCanal(repartoTipo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When

    //Then
    Assertions.assertNotNull(service.fileRead(repartoTipo,requestFileRead));
  }
}
