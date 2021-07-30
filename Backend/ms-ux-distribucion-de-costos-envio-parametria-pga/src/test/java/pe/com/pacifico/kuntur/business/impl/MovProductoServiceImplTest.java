package pe.com.pacifico.kuntur.business.impl;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.request.MovProductoRequest;
import pe.com.pacifico.kuntur.expose.response.MovProductoResponse;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MovProductoJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovProductoServiceImplTest {

  @Mock
  private MovProductoJpaRepository jpaRepository;

  @InjectMocks
  private MovProductoServiceImpl service;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MovProductoResponse producto1 = new MovProductoResponse();
  MovProductoResponse producto2 = new MovProductoResponse();
  MovProductoResponse producto3 = new MovProductoResponse();
  MovProductoRequest request = new MovProductoRequest();
  String codigo="any";

  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(jpaRepository.findAll(repartoTipo,periodo))
        .thenReturn(Arrays.asList(producto1,producto2,producto3));

    //Then
    Flux<MovProductoResponse> flux= service.getProductos(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldRegisterAnAccount()
  {
    //When
    when(jpaRepository.save(any(MovProductoRequest.class))).thenReturn(1);
    //Then
    assertThat(service.registerProducto(request), Is.is(1));
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(jpaRepository.delete(repartoTipo,periodo,codigo)).thenReturn(true);

    //Then
    assertThat(service.deleteProducto(repartoTipo,periodo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When

    //Then
    Assertions.assertNotNull(service.fileRead(repartoTipo,requestFileRead,periodo ));
  }
}
