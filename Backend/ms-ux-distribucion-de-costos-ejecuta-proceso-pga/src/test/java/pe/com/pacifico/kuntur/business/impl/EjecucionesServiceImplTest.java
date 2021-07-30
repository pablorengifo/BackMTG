package pe.com.pacifico.kuntur.business.impl;

import pe.com.pacifico.kuntur.business.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.response.EjecucionesResponse;
import pe.com.pacifico.kuntur.model.Ejecuciones;
import pe.com.pacifico.kuntur.webclient.impl.EjecucionesWebClientImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * <b>Class</b>: EjecucionesServiceImplTest <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Pacifico Seguros - La Chakra <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 23, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
public class EjecucionesServiceImplTest {

  @Mock
  private EjecucionesWebClientImpl ejecucionesWebClient;

  @InjectMocks
  private EjecucionesServiceImpl ejecucionesService;

  @Test
  void shouldReturnGetErrorEjecuciones() {
    Flux<Ejecuciones> items = ejecucionesService.getEjecuciones();
    StepVerifier.create(items)
        .expectErrorMatches(throwable -> throwable instanceof BusinessException)
        .verify();
  }

  @Test
  void shouldReturnGetOkEjecuciones() {
    when(ejecucionesWebClient.getEjecucionesResponse(eq(1L)))
        .thenReturn(Mono.just(EjecucionesResponse.builder().idController(1L).build()));

    Mono<Ejecuciones> item = ejecucionesService.getEjecuciones(1L);
    StepVerifier.create(item).expectComplete();
  }

}
