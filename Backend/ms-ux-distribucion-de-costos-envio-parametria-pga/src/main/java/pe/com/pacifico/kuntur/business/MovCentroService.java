package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MovCentroRequest;
import pe.com.pacifico.kuntur.expose.response.MovCentroResponse;
import pe.com.pacifico.kuntur.model.MovCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MovCentroService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 12, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface MovCentroService {

  Flux<MovCentroResponse> getCentros(int repartoTipo,int periodo);

  MovCentroResponse getMovCentroResponseFromMovCentro(MovCentro centro);

  Mono<MovCentroResponse> getCentro(String codigo);

  int registerCentro(MovCentroRequest request);

  Mono<MovCentro> updateCentro(MovCentroRequest request);

  boolean deleteCentro(int repartoTipo,int periodo, String codigo);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo);
}
