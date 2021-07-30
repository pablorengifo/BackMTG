package pe.com.pacifico.kuntur.repository;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MovUoaPaaRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovUoaPaa;

/**
 * <b>Class</b>: MovUoaPaaJpaRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 09, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface MovUoaPaaJpaRepository {

  List<MovUoaPaa> findAllMov(int repartoTipo, int periodo);

  List<MovUoaPaa> findAllMovByCodProducto(int repartoTipo, int periodo, String codProducto);

  int save(List<MovUoaPaaRequest> movUoaPaaRequestList);

  int update(List<MovUoaPaaRequest> movUoaPaaRequestList);

  boolean delete(int repartoTipo, int periodo, String codigo);

  List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo, int periodo);

  List<String> findAllCodProducto(int repartoTipo, int periodo);

}
