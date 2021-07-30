package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.CuentaContableRequest;
import pe.com.pacifico.kuntur.expose.response.CuentaContableResponse;
import pe.com.pacifico.kuntur.model.CuentaContable;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * <b>Class</b>: CuentaContableService <br/>
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
public interface CuentaContableService {

  Flux<CuentaContableResponse> getCuentas(int repartoTipo);

  Flux<CuentaContableResponse> getCuentasNotInMov(int repartoTipo, int periodo);

  Mono<CuentaContableResponse> getCuenta(int repartoTipo, String codigo);

  Mono<CuentaContable> registerCuenta(CuentaContableRequest request);

  Mono<CuentaContable> updateCuenta(CuentaContableRequest request);

  boolean deleteCuenta(int repartoTipo, String codigo);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead);

  Flux<CuentaContableResponse> getCuentasNiif(int repartoTipo);

  Flux<CuentaContableResponse> getCuentasNotNiif(int repartoTipo);

  Mono<CuentaContable> agregarCuentaNiif(CuentaContableRequest request);

  boolean removeCuentaNiif(int repartoTipo, String codigo);

  List<String> fileReadNiif(int repartoTipo, RequestFileRead requestFileRead);

}
