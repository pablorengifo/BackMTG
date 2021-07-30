package pe.com.pacifico.kuntur.repository;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MaProductoRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaProducto;



/**
 * <b>Class</b>: ProductoJpaRepository <br/>
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
public interface MaProductoJpaRepository {
  List<MaProducto> findAll(int repartoTipo);

  List<MaProducto> findAllNotInMovProducto(int repartoTipo, int periodo);

  MaProducto findByCodProducto(int repartoTipo, String codigo);

  int save(MaProductoRequest partida);

  int update(MaProductoRequest partida);

  boolean delete(int repartoTipo, String codigo);

  List<String> findAllCodProducto(int repartoTipo);

  List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo);
}
