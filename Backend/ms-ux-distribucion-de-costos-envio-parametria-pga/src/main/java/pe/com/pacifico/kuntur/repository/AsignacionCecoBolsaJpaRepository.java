package pe.com.pacifico.kuntur.repository;

import java.util.List;
import pe.com.pacifico.kuntur.model.AsignacionCecoBolsa;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovDriverCentro;

/**
 * <b>Class</b>: AsignacionCecoBolsaJpaRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 30, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface AsignacionCecoBolsaJpaRepository {

  List<AsignacionCecoBolsa> findAll(int repartoTipo, int periodo);

  List<String> saveExcelToBd(List<List<CellData>> excel, int periodo, int repartoTipo);

  List<MovDriverCentro> findAllMov(int repartoTipo, int periodo, String codDriverCentro);

  List<String> findAllCodCuentaContable(int repartoTipo, int periodo);

  List<String> findAllCodPartida(int repartoTipo, int periodo);

  List<String> findAllCodCentro(int repartoTipo, int periodo);

  List<String> findAllCodDriver(int repartoTipo, int periodo);
}
