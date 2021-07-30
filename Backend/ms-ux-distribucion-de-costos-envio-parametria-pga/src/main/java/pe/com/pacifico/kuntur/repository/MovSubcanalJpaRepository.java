package pe.com.pacifico.kuntur.repository;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MovSubcanalRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovSubcanal;

/**
 * <b>Class</b>: MovSubcanalJpaRepository <br/>
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
public interface MovSubcanalJpaRepository {

  List<MovSubcanal> findAll(int repartoTipo, int periodo);

  int save(MovSubcanalRequest subcanal);

  boolean delete(int repartoTipo, int periodo, String codigo);

  List<String> findAllCodCanal(int repartoTipo);

  List<String> findAllCodSubcanal(int repartoTipo);

  List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo, int periodo);

}
