package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MaCentroRequest;
import pe.com.pacifico.kuntur.expose.response.MaCentroResponse;
import pe.com.pacifico.kuntur.model.MaCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MaCentroService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 12, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface MaCentroService {

  Flux<MaCentroResponse> getCentros(int repartoTipo);

  Flux<MaCentroResponse> getCentrosNotInMov(int repartoTipo,int periodo);

  Mono<MaCentroResponse> getCentro(int repartoTipo,String codigo);

  Mono<MaCentro> registerCentro(MaCentroRequest request);

  Mono<MaCentro> updateCentro(MaCentroRequest request);

  boolean deleteCentro(int repartoTipo, String codigo);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead);
}
