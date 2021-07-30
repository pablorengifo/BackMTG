package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MovSubcanalRequest;
import pe.com.pacifico.kuntur.expose.response.MovSubcanalResponse;
import pe.com.pacifico.kuntur.model.MovSubcanal;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;

/**
 * <b>Class</b>: MovSubcanalService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 16, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface MovSubcanalService {

  Flux<MovSubcanalResponse> getSubcanales(int repartoTipo, int periodo);

  MovSubcanalResponse getMovSubcanalResponseFromMovSubcanal(MovSubcanal subcanal);

  int registerSubcanal(MovSubcanalRequest request);

  boolean deleteSubcanal(int repartoTipo, int periodo, String codigo);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo);
}
