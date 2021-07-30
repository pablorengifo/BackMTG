package pe.com.pacifico.kuntur.repository;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MovProductoRequest;
import pe.com.pacifico.kuntur.expose.response.MovProductoResponse;
import pe.com.pacifico.kuntur.model.CellData;



/**
 * <b>Class</b>: MovProductoJpaRepository <br/>
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
public interface MovProductoJpaRepository {

  List<MovProductoResponse> findAll(int repartoTipo, int periodo);

  int save(MovProductoRequest producto);

  boolean delete(int repartoTipo, int periodo, String codigo);

  List<String> findAllCodLinea(int repartoTipo);

  List<String> findAllCodProducto(int repartoTipo);

  List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo, int periodo);
}
