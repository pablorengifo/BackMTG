package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MovCuentaContableRequest;
import pe.com.pacifico.kuntur.expose.response.MovCuentaContableResponse;
import pe.com.pacifico.kuntur.model.MovCuentaContable;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MovCuentaContableService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     March 31, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface MovCuentaContableService {

  Flux<MovCuentaContableResponse> getCuentas(int repartoTipo, int periodo);

  MovCuentaContableResponse getMovCuentaContableResponseFromMovCuentaContable(MovCuentaContable cuenta);

  Mono<MovCuentaContable> registerCuenta(MovCuentaContableRequest request);

  boolean deleteCuenta(int repartoTipo, int periodo, String codigo);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo);
}
