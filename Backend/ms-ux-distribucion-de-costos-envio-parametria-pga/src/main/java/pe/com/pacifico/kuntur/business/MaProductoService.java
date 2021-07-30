package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MaProductoRequest;
import pe.com.pacifico.kuntur.expose.response.MaProductoResponse;
import pe.com.pacifico.kuntur.model.MaProducto;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MaProductoService <br/>
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
public interface MaProductoService {

  Flux<MaProductoResponse> getProductos(int repartoTipo);

  Flux<MaProductoResponse> getProductosNotInMovProducto(int repartoTipo, int periodo);

  Mono<MaProductoResponse> getProducto(int repartoTipo, String codigo);

  Mono<MaProducto> registerProducto(MaProductoRequest request);

  Mono<MaProducto> updateProducto(MaProductoRequest request);

  boolean deleteProducto(int repartoTipo, String codigo);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead);
}
