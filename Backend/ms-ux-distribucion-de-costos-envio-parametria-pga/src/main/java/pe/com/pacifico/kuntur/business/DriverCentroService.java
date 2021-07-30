package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.DriverCentroRequest;
import pe.com.pacifico.kuntur.expose.response.MaDriverResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverCentroResponse;
import pe.com.pacifico.kuntur.model.MovDriverCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: DriverCentroService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 23, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface DriverCentroService {

  Flux<MaDriverResponse> getMaDriverCentro(int repartoTipo, int periodo);

  Flux<MovDriverCentroResponse> getAllMovDriverCentro(int repartoTipo,int periodo);

  Flux<MovDriverCentroResponse> getMovDriversCentro(int repartoTipo, int periodo, String codDriverCentro);

  boolean deleteMovDriverCentro(int repartoTipo, int periodo, String codigo);

  int registerDriverCentro(DriverCentroRequest request);

  Mono<MovDriverCentro> updateDriverCentro(DriverCentroRequest request);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo);

}
