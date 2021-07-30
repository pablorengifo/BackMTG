package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MovUoaPaaRequest;
import pe.com.pacifico.kuntur.expose.response.MovUoaPaaResponse;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;


/**
 * <b>Class</b>: MovUoaPaaService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 12, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface MovUoaPaaService {

  Flux<MovUoaPaaResponse> getAllMovUoaPaaList(int repartoTipo, int periodo);

  Flux<MovUoaPaaResponse> getMovUoaPaaList(int repartoTipo, int periodo, String codProducto);

  boolean deleteMovUoaPaaList(int repartoTipo, int periodo, String codProducto);

  int registerMovUoaPaaList(List<MovUoaPaaRequest> movUoaPaaRequestList);

  int updateMovUoaPaaList(List<MovUoaPaaRequest> movUoaPaaRequestList);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo);

}
