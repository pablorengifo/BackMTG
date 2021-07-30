package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.response.MovUoaBbaVfaResponse;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;


/**
 * <b>Class</b>: MovUoaBbaVfaService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 14, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface MovUoaBbaVfaService {

  Flux<MovUoaBbaVfaResponse> getAllMovUoaBbaVfas(int repartoTipo, int periodo);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo);

}
