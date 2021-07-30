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
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 23, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Slf4j
@Configuration
public class WebClientApiConfig {

  @Value("${api.support.base.reports.url:http://localhost:8090/reports}")
  private String apiSupportBaseReportsUrl;

  /**
   * This method is used to get reportsWebClient.
   * @return WebClient.
   */
  @Bean
  public WebClient reportsWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder
        .baseUrl(apiSupportBaseReportsUrl)
        .build();
  }

}
