package pe.com.pacifico.kuntur.business.impl;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.request.MaProductoRequest;
import pe.com.pacifico.kuntur.expose.response.MaProductoResponse;
import pe.com.pacifico.kuntur.model.MaProducto;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MaProductoJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaProductoServiceImplTest {

  @Mock
  private MaProductoJpaRepository jpaRepository;

  @InjectMocks
  private MaProductoServiceImpl service;

  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MaProducto producto = new MaProducto();
  MaProducto producto1 = new MaProducto();
  MaProducto producto2 = new MaProducto();
  MaProducto producto3 = new MaProducto();
  MaProductoRequest request = new MaProductoRequest();
  String codigo="any";

  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(jpaRepository.findAll(repartoTipo))
        .thenReturn(Arrays.asList(producto1,producto2,producto3));

    //Then
    Flux<MaProductoResponse> flux= service.getProductos(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetAllAccountsNotInMov()
  {
    //When
    when(jpaRepository.findAllNotInMovProducto(repartoTipo,periodo))
        .thenReturn(Arrays.asList(producto1,producto2));

    //Then
    Flux<MaProductoResponse> flux= service.getProductosNotInMovProducto(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(2).verifyComplete();
  }

  @Test
  public void shouldGetAnAccount()
  {
    //When
    when(jpaRepository.findByCodProducto(repartoTipo,codigo))
        .thenReturn(producto);

    //Then
    service.getProducto(repartoTipo,codigo).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldRegisterAnAccount()
  {
    //When

    //Then
    service.registerProducto(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldUpdateAnAccount()
  {
    //given
    request.setRepartoTipo(0);
    request.setCodProducto("");
    //When
    when(jpaRepository.findByCodProducto(anyInt(),anyString())).thenReturn(producto);
    //Then
    service.updateProducto(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(jpaRepository.delete(repartoTipo,codigo)).thenReturn(true);

    //Then
    assertThat(service.deleteProducto(repartoTipo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When

    //Then
    Assertions.assertNotNull(service.fileRead(repartoTipo,requestFileRead));
  }

}
