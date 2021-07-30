package pe.com.pacifico.kuntur.repository;

import java.util.List;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaDriver;
import pe.com.pacifico.kuntur.model.MovDriverObjeto;


/**
 * <b>Class</b>: MovDriverObjetoJpaRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 28, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface MovDriverObjetoJpaRepository {

  List<MaDriver> findAll(int repartoTipo, int periodo);

  List<MovDriverObjeto> findAllByPeriod(int repartoTipo, int periodo, String codDriver);

  List<MovDriverObjeto> findAllByPeriod(int repartoTipo, int periodo);

  boolean delete(int repartoTipo,int periodo, String codigo);

  List<String> saveExcelToBD_MaDriver(List<List<CellData>> excel, int repartoTipo, int periodo);

  List<String> saveExcelToBD_MovDriverObjeto(List<List<CellData>> excel, int repartoTipo, int periodo);
}
