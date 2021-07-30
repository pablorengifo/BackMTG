package pe.com.pacifico.kuntur.business.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.response.ExactusResponse;
import pe.com.pacifico.kuntur.model.Exactus;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.ExactusJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExactusServiceImplTest {
  @Mock
  private ExactusJpaRepository jpaRepository;

  @InjectMocks
  private ExactusServiceImpl service;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  Exactus exactus = new Exactus();
  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(jpaRepository.findAll(repartoTipo,periodo))
        .thenReturn(Arrays.asList(exactus,exactus));

    //Then
    Flux<ExactusResponse> flux= service.getExactus(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(2).verifyComplete();
  }


  @Test
  public void shouldReadAFile()
  {
    //When

    //Then
    Assertions.assertNull(service.fileRead(repartoTipo,requestFileRead,periodo));
  }

}
