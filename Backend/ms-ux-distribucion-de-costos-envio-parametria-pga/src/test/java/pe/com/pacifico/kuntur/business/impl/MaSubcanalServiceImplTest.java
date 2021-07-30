package pe.com.pacifico.kuntur.business.impl;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.request.MaSubcanalRequest;
import pe.com.pacifico.kuntur.expose.response.MaSubcanalResponse;
import pe.com.pacifico.kuntur.model.MaSubcanal;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MaSubcanalJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaSubcanalServiceImplTest {

  @Mock
  private MaSubcanalJpaRepository jpaRepository;

  @InjectMocks
  private MaSubcanalServiceImpl service;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaSubcanal subcanal = new MaSubcanal();
  MaSubcanal subcanal1 = new MaSubcanal();
  MaSubcanal subcanal2 = new MaSubcanal();
  MaSubcanal subcanal3 = new MaSubcanal();
  MaSubcanalRequest request = new MaSubcanalRequest();
  String codigo="any";

  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(jpaRepository.findAll(repartoTipo))
        .thenReturn(Arrays.asList(subcanal1,subcanal2,subcanal3));

    //Then
    Flux<MaSubcanalResponse> flux= service.getSubcanales(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetAllAccountsNotInMov()
  {
    //When
    when(jpaRepository.findAllNotInMovSubcanal(repartoTipo,periodo))
        .thenReturn(Arrays.asList(subcanal1,subcanal2));

    //Then
    Flux<MaSubcanalResponse> flux= service.getSubcanalesNotInMov(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(2).verifyComplete();
  }

  @Test
  public void shouldGetAnAccount()
  {
    //When
    when(jpaRepository.findByCodSubcanal(repartoTipo,codigo))
        .thenReturn(subcanal);

    //Then
    service.getSubcanal(repartoTipo,codigo).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldRegisterAnAccount()
  {
    //When

    //Then
    service.registerSubcanal(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldUpdateAnAccount()
  {
    //given
    request.setRepartoTipo(0);
    request.setCodSubcanal("");
    //When
    when(jpaRepository.findByCodSubcanal(anyInt(),anyString())).thenReturn(subcanal);
    //Then
    service.updateSubcanal(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(jpaRepository.delete(repartoTipo,codigo)).thenReturn(true);

    //Then
    assertThat(service.deleteSubcanal(repartoTipo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When

    //Then
    Assertions.assertNotNull(service.fileRead(repartoTipo,requestFileRead));
  }
}
