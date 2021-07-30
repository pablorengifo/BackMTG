package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverObjetoRequest;
import pe.com.pacifico.kuntur.expose.response.AsignacionDriverObjetoResponse;
import pe.com.pacifico.kuntur.model.AsignacionDriverObjeto;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: AsignacionDriverObjetoService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 04, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface AsignacionDriverObjetoService {

  Flux<AsignacionDriverObjetoResponse> getAsignaciones(int repartoTipo, int periodo);

  Mono<AsignacionDriverObjeto> registerAsignacion(AsignacionDriverObjetoRequest request);

  boolean deleteAsignacion(int repartoTipo, int periodo, String codigo, String grupoGasto);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo);

  //Flux<AsignacionDriverObjetoResponse> getMovDriversCentro(int repartoTipo, int periodo, String codDriverCentro);
}
