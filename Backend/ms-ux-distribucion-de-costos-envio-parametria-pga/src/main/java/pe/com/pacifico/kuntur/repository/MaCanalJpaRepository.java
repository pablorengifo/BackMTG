package pe.com.pacifico.kuntur.repository;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MaCanalRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaCanal;

/**
 * <b>Class</b>: MaCanalJpaRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 16, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface MaCanalJpaRepository {

  List<MaCanal> findAll(int repartoTipo);

  MaCanal findByCodCanal(int repartoTipo, String codigo);

  int save(MaCanalRequest partida);

  int update(MaCanalRequest partida);

  boolean delete(int repartoTipo, String codigo);

  List<String> findAllCodCanal(int repartoTipo);

  List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo);

}
