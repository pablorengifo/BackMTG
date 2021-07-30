package pe.com.pacifico.kuntur.repository;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MaSubcanalRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaSubcanal;




/**
 * <b>Class</b>: MaSubcanalJpaRepository <br/>
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
public interface MaSubcanalJpaRepository {

  List<MaSubcanal> findAll(int repartoTipo);

  List<MaSubcanal> findAllNotInMovSubcanal(int repartoTipo, int periodo);

  MaSubcanal findByCodSubcanal(int repartoTipo, String codigo);

  int save(MaSubcanalRequest subcanal);

  int update(MaSubcanalRequest subcanal);

  boolean delete(int repartoTipo, String codigo);

  List<String> findAllCodSubcanal(int repartoTipo);

  List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo);

}
