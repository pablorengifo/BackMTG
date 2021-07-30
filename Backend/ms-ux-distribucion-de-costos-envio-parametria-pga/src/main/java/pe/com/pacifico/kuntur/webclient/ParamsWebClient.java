package pe.com.pacifico.kuntur.webclient;

import pe.com.pacifico.kuntur.expose.response.ParamsResponse;
import reactor.core.publisher.Mono;

/**
 * <b>Class</b>: ParamsWebClient <br/>
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
public interface ParamsWebClient {

  Mono<ParamsResponse> getParamsResponse(Long idControlador);

}
