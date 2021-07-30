package pe.com.pacifico.kuntur.business;

import pe.com.pacifico.kuntur.model.Ejecuciones;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <b>Class</b>: EjecucionesService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Pacifico Seguros - La Chakra <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 23, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface EjecucionesService {

  Flux<Ejecuciones> getEjecuciones();

  Mono<Ejecuciones> getEjecuciones(Long id);

}
