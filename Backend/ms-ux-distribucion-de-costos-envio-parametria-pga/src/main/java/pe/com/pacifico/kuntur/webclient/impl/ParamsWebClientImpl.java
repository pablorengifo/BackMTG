package pe.com.pacifico.kuntur.webclient.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.pacifico.kuntur.expose.response.ParamsResponse;
import pe.com.pacifico.kuntur.webclient.ParamsWebClient;
import reactor.core.publisher.Mono;

/**
 * <b>Class</b>: ParamsWebClientImpl <br/>
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
@RequiredArgsConstructor
@Repository
@Slf4j
public class ParamsWebClientImpl implements ParamsWebClient {

  private final WebClient paramsWebClient;

  private final ReactiveCircuitBreakerFactory cbFactory;

  @Override
  public Mono<ParamsResponse> getParamsResponse(Long idControlador) {
    return
        paramsWebClient
          .get()
          .uri("/resources/{idcontrolador}", idControlador)
          .headers(httpHeaders ->
            httpHeaders.setContentType(MediaType.APPLICATION_JSON)
          )
          .retrieve()
          .bodyToMono(ParamsResponse.class)
          .transformDeferred(tradeParamsModelFlux ->
            cbFactory.create("slow").run(tradeParamsModelFlux, this::fallBackGetParamsResilience4j)
          )
          .log();
  }

  private Mono<ParamsResponse> fallBackGetParamsResilience4j(Throwable t) {
    log.error("Inside circuit breaker fallBackGetParamsResilience4j, cause - {}", t.getLocalizedMessage());
    return Mono.just(ParamsResponse.builder()
      .idController(1L)
      .data("1234567890")
      .cardNumber("0123456789012345")
      .documentNumber("47589463")
      .build());
  }

}
