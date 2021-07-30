package pe.com.pacifico.kuntur.expose;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.CuentaContableService;
import pe.com.pacifico.kuntur.expose.request.CuentaContableRequest;
import pe.com.pacifico.kuntur.expose.response.CuentaContableResponse;
import pe.com.pacifico.kuntur.model.CuentaContable;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CuentaContableControllerTest {

  @Mock
  private CuentaContableService cuentaContableService;

  @InjectMocks
  private CuentaContableController cuentaContableController;

  //Given
  int repartoTipo=1;
  Date date = new Date();
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  CuentaContable cuentaContable = new CuentaContable("46.1.2.01.00.00","reparacion",true,
      date,date,"EX","EX","EX",repartoTipo,true);
  CuentaContableResponse cuentaContable1 = new CuentaContableResponse("46.1.2.01.00.00","reparacion",true,
      date,date,"EX","EX","EX",repartoTipo,true);
  CuentaContableResponse cuentaContable2 = new CuentaContableResponse("46.1.2.03.00.00","eventuales",true,
      date,date,"EX","EX","EX",repartoTipo,true);
  CuentaContableResponse cuentaContable3 = new CuentaContableResponse("46.1.2.12.00.00","mantenimiento",true,
      date,date,"EX","EX","EX",repartoTipo,true);
  CuentaContableRequest request1 = new CuentaContableRequest("46.1.2.15.00.00","ejemplo",true,
      date,date,"EX","EX","EX",1,true);
  CuentaContableRequest request2 = new CuentaContableRequest("46.1.2.01.00.00","ejemplo",true,
      date,date,"EX","EX2","EX",1,true);
  String codigo="46.1.2.01.00.00";


  @Test
  public void shouldGetAllAcounts()
  {
    //When
    when(cuentaContableService.getCuentas(repartoTipo))
        .thenReturn(Flux.just(cuentaContable1, cuentaContable2, cuentaContable3));

    //Then
    Flux<CuentaContableResponse> flux= cuentaContableController.getCuentas(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetAllAccountsNotInMov()
  {
    //When
    when(cuentaContableService.getCuentasNotInMov(repartoTipo,periodo))
        .thenReturn(Flux.just(cuentaContable1, cuentaContable2));

    //Then
    Flux<CuentaContableResponse> flux= cuentaContableController.getCuentasNotInMov(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(2).verifyComplete();
  }

  @Test
  public void shouldGetAnAccount()
  {
    //When
    when(cuentaContableService.getCuenta(repartoTipo,codigo))
        .thenReturn(Mono.just(cuentaContable1));

    //Then
    cuentaContableController.getCuentaContableByCodCuentaContable(repartoTipo,codigo).subscribe(response -> {
      Assertions.assertNotNull(response);
    });
  }

  @Test
  public void shouldRegisterAnAccount()
  {
    //When
    when(cuentaContableService.registerCuenta(request1)).thenReturn(Mono.just(cuentaContable));

    //Then
    cuentaContableService.registerCuenta(request1).subscribe(response -> {
      Assertions.assertNotNull(response);
    });
  }

  @Test
  public void shouldUpdateAnAccount()
  {
    //When
    when(cuentaContableService.updateCuenta(request2)).thenReturn(Mono.just(cuentaContable));
    //Then
    cuentaContableController.updateParametria(request2).subscribe(response -> {
      Assertions.assertNotNull(response);
    });
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(cuentaContableService.deleteCuenta(repartoTipo,codigo)).thenReturn(true);

    //Then
    assertThat(cuentaContableController.deleteCuenta(repartoTipo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When
    when(cuentaContableService.fileRead(repartoTipo,requestFileRead)).thenReturn(new ArrayList<>());
    //Then
    Assertions.assertNotNull(cuentaContableController.fileRead(repartoTipo,requestFileRead));
  }

  @Test
  public void  shouldListAllCuentasNiif()
  {
    //when
    when(cuentaContableService.getCuentasNiif(anyInt()))
        .thenReturn(Flux.just(cuentaContable1, cuentaContable2, cuentaContable3));
    //Then
    Flux<CuentaContableResponse> flux= cuentaContableController.getCuentasNiif(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void  shouldListAllCuentasNotNiif()
  {
    //when
    when(cuentaContableService.getCuentasNotNiif(anyInt()))
        .thenReturn(Flux.just(cuentaContable1, cuentaContable2, cuentaContable3));
    //Then
    Flux<CuentaContableResponse> flux= cuentaContableController.getCuentasNoNiif(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldRegisterAnNiifCuenta()
  {
    //When
    when(cuentaContableService.agregarCuentaNiif(request1)).thenReturn(Mono.just(cuentaContable));

    //Then
    cuentaContableService.agregarCuentaNiif(request1).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldRemoveAnNiifCuenta()
  {
    //When
    when(cuentaContableService.removeCuentaNiif(repartoTipo,codigo)).thenReturn(true);

    //Then
    assertThat(cuentaContableController.removeCuentaNiif(repartoTipo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadANiifFile()
  {
    //When
    when(cuentaContableService.fileReadNiif(repartoTipo,requestFileRead)).thenReturn(new ArrayList<>());
    //Then
    Assertions.assertNotNull(cuentaContableController.fileReadNiif(repartoTipo,requestFileRead));
  }


}
