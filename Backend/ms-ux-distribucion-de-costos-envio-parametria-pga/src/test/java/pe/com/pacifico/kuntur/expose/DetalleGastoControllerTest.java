package pe.com.pacifico.kuntur.expose;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.DetalleGastoService;
import pe.com.pacifico.kuntur.business.threads.CargarDetalleGastoHilo;
import pe.com.pacifico.kuntur.business.threads.CargarExactusHilo;
import pe.com.pacifico.kuntur.expose.response.DetalleGastoResponse;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DetalleGastoControllerTest {
  @Mock
  private DetalleGastoService service;

  @InjectMocks
  private DetalleGastoController controller;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  DetalleGastoResponse response1 = new DetalleGastoResponse();
  DetalleGastoResponse response2 = new DetalleGastoResponse();
  DetalleGastoResponse response3 = new DetalleGastoResponse();

  @Test
  public void shouldGetAllItems()
  {
    //When
    when(service.getDetallesGasto(repartoTipo,periodo))
        .thenReturn(Flux.just(response1, response2, response3));

    //Then
    Flux<DetalleGastoResponse> flux= controller.getDetalles(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldReadAFile()
  {
    //When
    when(service.fileRead(repartoTipo,requestFileRead,periodo)).thenReturn(new ArrayList<>());
    //Then
    Assertions.assertNotNull(controller.fileRead(requestFileRead,repartoTipo,periodo));
  }

  @Test
  public void shouldGenereDetail()
  {
    //When
    when(service.generarDetalleGasto(repartoTipo,periodo)).thenReturn(true);
    //Then
    Assertions.assertTrue(controller.generar(repartoTipo,periodo));
  }

  @Test
  public void isSubiendoArchivo() {

    //given
    CargarExactusHilo.enEjecucion=false;
    CargarExactusHilo.terminoEjecucion=true;
    // Then
    Assertions.assertFalse(controller.subiendoArchivo());
  }

  @Test
  public void shouldObtenerErroresEnSubidaDeArchivo() {
    //given
    CargarDetalleGastoHilo.errores=null;
    // Then
    Assertions.assertNull(controller.obtenerErroresEnSubidaDeArchivo());
  }

}
