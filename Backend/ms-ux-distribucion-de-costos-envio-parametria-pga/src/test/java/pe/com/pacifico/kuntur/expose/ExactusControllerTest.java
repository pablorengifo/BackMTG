package pe.com.pacifico.kuntur.expose;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.business.ExactusService;
import pe.com.pacifico.kuntur.business.threads.CargarExactusHilo;
import pe.com.pacifico.kuntur.expose.response.ExactusResponse;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExactusControllerTest {

  @Mock
  private ExactusService service;

  @InjectMocks
  private ExactusController controller;

  //Given
  int repartoTipo=1;
  int periodo = 202104;
  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  ExactusResponse response1 = new ExactusResponse();

  @Test
  public void shouldGetAllItems()
  {
    //When
    when(service.getExactus(repartoTipo,periodo))
        .thenReturn(Flux.just(response1));

    //Then
    Flux<ExactusResponse> flux= controller.getExactus(repartoTipo,periodo);
    StepVerifier.create(flux.log()).expectNextCount(1).verifyComplete();
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
    CargarExactusHilo.errores=null;
    // Then
    Assertions.assertNull(controller.obtenerErroresEnSubidaDeArchivo());
  }
}
