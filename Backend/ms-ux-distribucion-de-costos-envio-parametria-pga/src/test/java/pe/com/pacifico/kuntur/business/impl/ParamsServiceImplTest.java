package pe.com.pacifico.kuntur.business.impl;

import pe.com.pacifico.kuntur.business.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.expose.response.ParamsResponse;
import pe.com.pacifico.kuntur.model.Params;
import pe.com.pacifico.kuntur.webclient.impl.ParamsWebClientImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * <b>Class</b>: ParamsServiceImplTest <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Pacifico Seguros - La Chakra <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 26, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
public class ParamsServiceImplTest {

  @Mock
  private ParamsWebClientImpl paramsWebClient;

  @InjectMocks
  private ParamsServiceImpl paramsService;

  @Test
  void shouldReturnGetErrorParams() {
    Flux<Params> items = paramsService.getParams();
    StepVerifier.create(items)
        .expectErrorMatches(throwable -> throwable instanceof BusinessException)
        .verify();
  }

  @Test
  void shouldReturnGetOkParams() {
    when(paramsWebClient.getParamsResponse(eq(1L)))
        .thenReturn(Mono.just(ParamsResponse.builder().idController(1L).build()));

    Mono<Params> item = paramsService.getParams(1L);
    StepVerifier.create(item).expectComplete();
  }

}
