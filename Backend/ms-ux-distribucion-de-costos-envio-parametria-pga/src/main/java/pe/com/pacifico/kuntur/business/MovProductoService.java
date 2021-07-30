package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MovProductoRequest;
import pe.com.pacifico.kuntur.expose.response.MovProductoResponse;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;



/**
 * <b>Class</b>: MovProductoService <br/>
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
public interface MovProductoService {

  Flux<MovProductoResponse> getProductos(int repartoTipo, int periodo);

  MovProductoResponse getMovProductoResponseFromMovProducto(MovProductoResponse producto);

  int registerProducto(MovProductoRequest request);

  boolean deleteProducto(int repartoTipo, int periodo, String codigo);

  List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo);

}
