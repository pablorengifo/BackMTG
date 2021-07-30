package pe.com.pacifico.kuntur.business.impl;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.request.CuentaContableRequest;
import pe.com.pacifico.kuntur.expose.response.CuentaContableResponse;
import pe.com.pacifico.kuntur.model.CuentaContable;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.CuentaContableJpaRepository;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


/**
 * <b>Class</b>: CuentaContableServiceImpl <br/>
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
public class CuentaContableServiceImplTest {

  @Mock
  private  CuentaContableJpaRepository cuentaContableJpaRepository;

  @InjectMocks
  private  CuentaContableServiceImpl cuentaContableService;


  //Given
  int repartoTipo=1;
  Date date = new Date();
  CuentaContable cuentaContable1 = new CuentaContable("46.1.2.01.00.00","reparacion",true,
      date,date,"EX","EX","EX",repartoTipo,true);
  CuentaContable cuentaContable2 = new CuentaContable("46.1.2.03.00.00","eventuales",true,
      date,date,"EX","EX","EX",repartoTipo,true);
  CuentaContable cuentaContable3 = new CuentaContable("46.1.2.12.00.00","mantenimiento",true,
      date,date,"EX","EX","EX",repartoTipo,true);
  CuentaContableRequest request1 = new CuentaContableRequest("46.1.2.15.00.00","ejemplo",true,
      date,date,"EX","EX","EX",1,true);
  CuentaContableRequest request2 = new CuentaContableRequest("46.1.2.01.00.00","ejemplo",true,
      date,date,"EX","EX2","EX",1,true);
  int periodo = 202104;
  String codigo="46.1.2.01.00.00";
  RequestFileRead requestFileRead = new RequestFileRead("prueba");

  @Test
  public void shouldGetAllAccounts()
  {
    //When
    when(cuentaContableJpaRepository.findAll(repartoTipo))
        .thenReturn(Arrays.asList(cuentaContable1,cuentaContable2,cuentaContable3));

    //Then
    Flux<CuentaContableResponse> flux= cuentaContableService.getCuentas(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetAllAccountsNotInMov()
  {
    //When
    when(cuentaContableJpaRepository.findAllNotInMovCuentaContable(repartoTipo,periodo))
        .thenReturn(Arrays.asList(cuentaContable1,cuentaContable2));

    //Then
    Flux<CuentaContableResponse> flux= cuentaContableService.getCuentasNotInMov(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(2).verifyComplete();
  }

  @Test
  public void shouldGetAnAccount()
  {
    //When
    when(cuentaContableJpaRepository.findByCodCuentaContable(repartoTipo,codigo))
        .thenReturn(cuentaContable1);

    //Then
    cuentaContableService.getCuenta(repartoTipo,codigo).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldRegisterAnAccount()
  {
    //When
    when(cuentaContableJpaRepository.save(request1)).thenReturn(1);
    //Then
    cuentaContableService.registerCuenta(request1).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldUpdateAnAccount()
  {
    //When
    when(cuentaContableJpaRepository.findByCodCuentaContable(repartoTipo,codigo)).thenReturn(cuentaContable1);
    //Then
    cuentaContableService.updateCuenta(request2).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAnAccount()
  {
    //When
    when(cuentaContableJpaRepository.delete(repartoTipo,codigo)).thenReturn(true);

    //Then
    assertThat(cuentaContableService.deleteCuenta(repartoTipo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadAFile()
  {
    //Then
    Assertions.assertNotNull(cuentaContableService.fileRead(repartoTipo,requestFileRead));
  }

  @Test
  public void shouldGetAllNiifAccounts()
  {
    //When
    when(cuentaContableJpaRepository.findAllNiif(repartoTipo))
        .thenReturn(Arrays.asList(cuentaContable1,cuentaContable2,cuentaContable3));

    //Then
    Flux<CuentaContableResponse> flux= cuentaContableService.getCuentasNiif(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldGetAllNotNiifAccounts()
  {
    //When
    when(cuentaContableJpaRepository.findAllNotNiifCuentaContable(repartoTipo))
        .thenReturn(Arrays.asList(cuentaContable1,cuentaContable2,cuentaContable3));

    //Then
    Flux<CuentaContableResponse> flux= cuentaContableService.getCuentasNotNiif(repartoTipo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldAddANiifAccount()
  {
    //When
    when(cuentaContableJpaRepository.registrarNiif(request1)).thenReturn(1);
    //Then
    cuentaContableService.agregarCuentaNiif(request1).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldRemoveANiifAccount()
  {
    //When
    when(cuentaContableJpaRepository.quitarNiif(repartoTipo,codigo)).thenReturn(true);

    //Then
    assertThat(cuentaContableService.removeCuentaNiif(repartoTipo,codigo), Is.is(true));
  }

  @Test
  public void shouldReadANiifFile(){
    //when
    when(cuentaContableJpaRepository.registerNiifByExcel(any(List.class),anyInt()))
        .thenReturn(new ArrayList());
    try(MockedStatic<ExcelUtil> utilities = Mockito.mockStatic(ExcelUtil.class)) {
      utilities.when(()->ExcelUtil.getRowList(anyString(),any(),any(),anyInt()))
          .thenReturn(new ArrayList());
      //Then
      Assertions.assertNotNull(cuentaContableService.fileReadNiif(repartoTipo,requestFileRead));
    }
  }

  @Test
  public void shouldNotReadANiifFile()
  {
    //Then
    Assertions.assertNotNull(cuentaContableService.fileReadNiif(repartoTipo,requestFileRead));
  }

}
