package pe.com.pacifico.kuntur.business.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.response.DetalleGastoResponse;
import pe.com.pacifico.kuntur.model.DetalleGasto;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.DetalleGastoJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DetalleGastoServiceImplTest {

  @Mock
  private DetalleGastoJpaRepository jpaRepository;

  @InjectMocks
  private DetalleGastoServiceImpl service;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  DetalleGasto detalleGasto1 = new DetalleGasto();
  DetalleGasto detalleGasto2 = new DetalleGasto();
  DetalleGasto detalleGasto3 = new DetalleGasto();
  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(jpaRepository.findAll(repartoTipo,periodo))
        .thenReturn(Arrays.asList(detalleGasto1,detalleGasto2,detalleGasto3));

    //Then
    Flux<DetalleGastoResponse> flux= service.getDetallesGasto(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGenerarDetalleGasto()
  {
    //Then
    Assertions.assertTrue(service.generarDetalleGasto(repartoTipo,periodo));
  }

  @Test
  public void shouldReadAFile()
  {
    //When

    //Then
    Assertions.assertNull(service.fileRead(repartoTipo,requestFileRead,periodo));
  }


}
