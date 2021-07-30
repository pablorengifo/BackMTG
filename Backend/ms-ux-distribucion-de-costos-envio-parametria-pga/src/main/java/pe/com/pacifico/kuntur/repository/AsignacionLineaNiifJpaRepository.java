package pe.com.pacifico.kuntur.repository;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.AsignacionLineaNiifRequest;
import pe.com.pacifico.kuntur.model.AsignacionLineaNiif;
import pe.com.pacifico.kuntur.model.CellData;

/**
 * <b>Class</b>: AsignacionLineaNiifJpaRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 22, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface AsignacionLineaNiifJpaRepository {

  List<AsignacionLineaNiif> findAll(int repartoTipo, int periodo);

  List<String> saveExcelToBd(List<List<CellData>> excel, int periodo, int repartoTipo);

  boolean delete(int repartoTipo,int periodo, String codLinea);

  int save(AsignacionLineaNiifRequest asignacionLineaNiifRequest);

}
