package pe.com.pacifico.kuntur.expose;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.MovCuentaContableService;
import pe.com.pacifico.kuntur.expose.request.MovCuentaContableRequest;
import pe.com.pacifico.kuntur.expose.response.MovCuentaContableResponse;
import pe.com.pacifico.kuntur.expose.response.MovProductoResponse;
import pe.com.pacifico.kuntur.model.MovCuentaContable;
import pe.com.pacifico.kuntur.model.MovProducto;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovCuentaContableControllerTest {
  @Mock
  private MovCuentaContableService movCuentaContableService;

  @InjectMocks
  private MovCuentaContableController movCuentaContableController;

  //Given
  int repartoTipo=1;
  Date date = new Date();
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MovCuentaContable cuentaContable = new MovCuentaContable("46.1.2.01.00.00","reparacion",
      date,date,periodo,repartoTipo,12);
  MovCuentaContableResponse cuentaContable1 = new MovCuentaContableResponse("46.1.2.01.00.00","reparacion",
      date,date,periodo,repartoTipo,12);
  MovCuentaContableResponse cuentaContable2 = new MovCuentaContableResponse("46.1.2.01.00.00","reparacion",
      date,date,periodo,repartoTipo,12);
  MovCuentaContableResponse cuentaContable3 = new MovCuentaContableResponse("46.1.2.01.00.00","reparacion",
      date,date,periodo,repartoTipo,12);
  MovCuentaContableRequest request1 = new MovCuentaContableRequest("46.1.2.01.00.00",
      date,date,periodo,repartoTipo,12);
  String codigo="46.1.2.01.00.00";

  @Test
  public void shouldGetAllAcounts()
  {
    //When
    when(movCuentaContableService.getCuentas(repartoTipo,periodo))
        .thenReturn(Flux.just(cuentaContable1, cuentaContable2, cuentaContable3));

    //Then
    Flux<MovCuentaContableResponse> flux= movCuentaContableController.getCuentas(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }


  @Test
  public void shouldRegisterAnAccount()
  {
    //When
    when(movCuentaContableService.registerCuenta(request1)).thenReturn(Mono.just(cuentaContable));

    //Then
    movCuentaContableController.registerCuenta(request1).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(movCuentaContableService.deleteCuenta(repartoTipo,periodo,codigo)).thenReturn(true);

    //Then
    assertThat(movCuentaContableController.deleteCuenta(repartoTipo,periodo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When
    when(movCuentaContableService.fileRead(repartoTipo,requestFileRead,periodo)).thenReturn(new ArrayList<>());
    //Then
    Assertions.assertNotNull(movCuentaContableController.fileRead(requestFileRead,repartoTipo,periodo));
  }

  @Test
  public void shouldBuildResponse() {
    MovCuentaContable m = new MovCuentaContable();
    MovCuentaContableResponse a = new MovCuentaContableResponse();
    // When
    lenient().when(movCuentaContableService.getMovCuentaContableResponseFromMovCuentaContable(any(MovCuentaContable.class))).thenReturn(a);
    // Then
    Assertions.assertNotNull(movCuentaContableController.buildResponse(m));
  }
}
