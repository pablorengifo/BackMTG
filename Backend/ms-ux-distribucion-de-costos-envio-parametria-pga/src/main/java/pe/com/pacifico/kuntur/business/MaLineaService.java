package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MaLineaRequest;
import pe.com.pacifico.kuntur.expose.response.MaLineaResponse;
import pe.com.pacifico.kuntur.model.MaLinea;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MaLineaService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 7, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface MaLineaService {
  Flux<MaLineaResponse> getLineas(int repartoTipo);

  Mono<MaLineaResponse> getLinea(int repartoTipo, String codigo);

  Mono<MaLinea> registerLinea(MaLineaRequest request);

  Mono<MaLinea> updateLinea(MaLineaRequest request);

  boolean deleteLinea(int repartoTipo, String codigo);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead);
}
