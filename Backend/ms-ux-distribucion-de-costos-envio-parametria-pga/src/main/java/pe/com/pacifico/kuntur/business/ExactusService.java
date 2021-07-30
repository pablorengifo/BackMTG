package pe.com.pacifico.kuntur.business;

import java.util.List;

import pe.com.pacifico.kuntur.expose.response.ExactusResponse;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;



/**
 * <b>Class</b>: pe.com.pacifico.kuntur.model.ExactusService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 21, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface ExactusService {

  Flux<ExactusResponse> getExactus(int repartoTipo, int periodo);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo);


}
