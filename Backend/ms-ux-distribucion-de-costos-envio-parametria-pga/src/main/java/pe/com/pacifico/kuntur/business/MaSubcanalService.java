package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MaSubcanalRequest;
import pe.com.pacifico.kuntur.expose.response.MaSubcanalResponse;
import pe.com.pacifico.kuntur.model.MaSubcanal;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MaSubcanalService <br/>
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
public interface MaSubcanalService {

  Flux<MaSubcanalResponse> getSubcanales(int repartoTipo);

  Flux<MaSubcanalResponse> getSubcanalesNotInMov(int repartoTipo, int periodo);

  Mono<MaSubcanalResponse> getSubcanal(int repartoTipo, String codigo);

  Mono<MaSubcanal> registerSubcanal(MaSubcanalRequest request);

  Mono<MaSubcanal> updateSubcanal(MaSubcanalRequest request);

  boolean deleteSubcanal(int repartoTipo, String codigo);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead);
}
