package pe.com.pacifico.kuntur.expose;

import com.pacifico.kuntur.core.errorhandling.ErrorResponse;
import com.pacifico.kuntur.core.errorhandling.HeadersConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.com.pacifico.kuntur.business.impl.EjecucionesServiceImpl;
import pe.com.pacifico.kuntur.expose.response.EjecucionesResponse;
import pe.com.pacifico.kuntur.repository.EjecucionesJpaRepository;
import pe.com.pacifico.kuntur.webclient.EjecucionesWebClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * <b>Class</b>: EjecucionesControllerTest <br/>
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
@AutoConfigureWebTestClient(timeout = "50000")
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EjecucionesController.class)
@Import(EjecucionesServiceImpl.class)
class EjecucionesControllerTest {

  private WebTestClient webTestClient;

  @MockBean
  private EjecucionesWebClient ejecucionesWebClient;

  @MockBean
  private EjecucionesJpaRepository ejecucionesJpaRepository;

  @Autowired
  public EjecucionesControllerTest(WebTestClient webTestClient) {
    this.webTestClient = webTestClient;
  }

  @BeforeEach
  void setUp() {
    EjecucionesResponse ejecucionesResponse = new EjecucionesResponse();
    ejecucionesResponse.setData("DEMO");
    ejecucionesResponse.setIdController(1L);

    when(ejecucionesWebClient.getEjecucionesResponse(any()))
        .thenReturn(Mono.just(ejecucionesResponse));
  }

  @Test
  void shouldReturnGetError500Ejecuciones() {
    webTestClient
        .get()
        .uri("/ejecuciones")
        .header(HeadersConstant.HeaderRequest.TRANSACTION_ID.getKey(), "trx_0001")
        .header(HeadersConstant.HeaderRequest.APPLICATION_NAME.getKey(), "app_001")
        .header(HeadersConstant.HeaderRequest.APPLICATION_USER.getKey(), "user_001")
        .header(HeadersConstant.HeaderRequest.BUSINESS_PROCESS.getKey(), "process_001")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().is5xxServerError()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(ErrorResponse.class);
  }

  @Test
  void shouldReturnGetOKEjecuciones() {
    webTestClient
        .get()
        .uri("/ejecuciones/{idcontrolador}", 1L)
        .header(HeadersConstant.HeaderRequest.TRANSACTION_ID.getKey(), "trx_0001")
        .header(HeadersConstant.HeaderRequest.APPLICATION_NAME.getKey(), "app_001")
        .header(HeadersConstant.HeaderRequest.APPLICATION_USER.getKey(), "user_001")
        .header(HeadersConstant.HeaderRequest.BUSINESS_PROCESS.getKey(), "process_001")
        .accept(MediaType.APPLICATION_STREAM_JSON)
        .exchange()
        .expectStatus().isOk();
  }

}
