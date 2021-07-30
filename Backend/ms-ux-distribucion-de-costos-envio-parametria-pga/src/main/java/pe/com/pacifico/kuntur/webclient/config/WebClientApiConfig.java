package pe.com.pacifico.kuntur.webclient.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * <b>Class</b>: WebClientApiConfig <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Pacifico Seguros - La Chakra <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     March 23, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Slf4j
@Configuration
public class WebClientApiConfig {

  @Value("${api.support.base.params.url:http://localhost:8090/params}")
  private String apiSupportBaseParamsUrl;


  /**
   * This method is used to get parametriaWebClient.
   * @return WebClient.
   */
  @Bean
  public WebClient paramsWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder
        .baseUrl(apiSupportBaseParamsUrl)
        .build();
  }

}
