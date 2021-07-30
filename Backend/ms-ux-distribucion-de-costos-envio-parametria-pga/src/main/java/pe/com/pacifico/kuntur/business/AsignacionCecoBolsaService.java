package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.response.AsignacionCecoBolsaResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverCentroResponse;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;

/**
 * <b>Class</b>: AsignacionCecoBolsaService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 30, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface AsignacionCecoBolsaService {

  Flux<AsignacionCecoBolsaResponse> getAsignaciones(int repartoTipo, int periodo);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo);

  Flux<MovDriverCentroResponse> getMovDriversCentro(int repartoTipo, int periodo, String codDriverCentro);
}
