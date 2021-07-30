package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.AsignacionLineaNiifRequest;
import pe.com.pacifico.kuntur.expose.response.AsignacionLineaNiifResponse;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;

/**
 * <b>Class</b>: AsignacionLineaNiifService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 22, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface AsignacionLineaNiifService {

  Flux<AsignacionLineaNiifResponse> getAsignaciones(int repartoTipo, int periodo);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo);

  boolean deleteAsignacion(int repartoTipo, int periodo, String codigo);

  boolean addAsignacionNiif(AsignacionLineaNiifRequest request);
}
