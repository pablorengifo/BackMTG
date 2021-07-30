package pe.com.pacifico.kuntur.business.impl;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.request.MovCuentaContableRequest;
import pe.com.pacifico.kuntur.expose.response.MovCuentaContableResponse;
import pe.com.pacifico.kuntur.model.MovCuentaContable;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MovCuentaContableJpaRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;


/**
 * <b>Class</b>: MovCuentaContableServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     March 31, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
public class MovCuentaContableServiceImplTest{

  @Mock
  private  MovCuentaContableJpaRepository movCuentaContableJpaRepository;

  @InjectMocks
  private MovCuentaContableServiceImpl movCuentaContableService;

  int repartoTipo=1;
  Date date = new Date();
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  MovCuentaContable cuentaContable1 = new MovCuentaContable("46.1.2.01.00.00","reparacion",
      date,date,periodo,repartoTipo,12);
  MovCuentaContable cuentaContable2 = new MovCuentaContable("46.1.2.01.00.00","reparacion",
      date,date,periodo,repartoTipo,12);
  MovCuentaContable cuentaContable3 = new MovCuentaContable("46.1.2.01.00.00","reparacion",
      date,date,periodo,repartoTipo,12);
  MovCuentaContableRequest request1 = new MovCuentaContableRequest("46.1.2.01.00.00",
      date,date,periodo,repartoTipo,12);
  String codigo="46.1.2.01.00.00";

  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(movCuentaContableJpaRepository.findAll(repartoTipo,periodo))
        .thenReturn(Arrays.asList(cuentaContable1,cuentaContable2,cuentaContable3));

    //Then
    Flux<MovCuentaContableResponse> flux= movCuentaContableService.getCuentas(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldRegisterAnAccount()
  {
    //When

    //Then
    movCuentaContableService.registerCuenta(request1).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(movCuentaContableJpaRepository.delete(repartoTipo,periodo,codigo)).thenReturn(true);

    //Then
    assertThat(movCuentaContableService.deleteCuenta(repartoTipo,periodo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //When

    //Then
    Assertions.assertNotNull(movCuentaContableService.fileRead(repartoTipo,requestFileRead,periodo ));
  }



}
