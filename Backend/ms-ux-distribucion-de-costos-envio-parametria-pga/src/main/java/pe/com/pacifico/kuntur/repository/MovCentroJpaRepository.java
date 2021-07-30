package pe.com.pacifico.kuntur.repository;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MovCentroRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovCentro;

/**
 * <b>Class</b>: MovCentroJpaRepository <br/>
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
public interface MovCentroJpaRepository {

  List<MovCentro> findAll(int repartoTipo,int periodo);

  MovCentro findByCodCentro(String codigo);

  int save(MovCentroRequest centro);

  int update(MovCentroRequest centro);

  boolean delete(int repartoTipo,int periodo, String codigo);

  List<String> findAllCodCentro(int repartoTipo);

  List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo, int periodo);

}
