package pe.com.pacifico.kuntur.business.impl;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.request.MovSubcanalRequest;
import pe.com.pacifico.kuntur.expose.response.MovSubcanalResponse;
import pe.com.pacifico.kuntur.model.MovSubcanal;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MovSubcanalJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovSubcanalServiceImplTest {

  @Mock
  private MovSubcanalJpaRepository jpaRepository;

  @InjectMocks
  private MovSubcanalServiceImpl service;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MovSubcanal subcanal1 = new MovSubcanal();
  MovSubcanal subcanal2 = new MovSubcanal();
  MovSubcanal subcanal3 = new MovSubcanal();
  MovSubcanalRequest request = new MovSubcanalRequest();
  String codigo="any";

  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(jpaRepository.findAll(repartoTipo,periodo))
        .thenReturn(Arrays.asList(subcanal1,subcanal2,subcanal3));

    //Then
    Flux<MovSubcanalResponse> flux= service.getSubcanales(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldRegisterAnAccount()
  {
    //When
    when(jpaRepository.save(any(MovSubcanalRequest.class))).thenReturn(1);
    //Then
    assertThat(service.registerSubcanal(request), Is.is(1));
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(jpaRepository.delete(repartoTipo,periodo,codigo)).thenReturn(true);

    //Then
    assertThat(service.deleteSubcanal(repartoTipo,periodo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When

    //Then
    Assertions.assertNotNull(service.fileRead(repartoTipo,requestFileRead,periodo ));
  }

}
