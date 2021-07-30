package pe.com.pacifico.kuntur.repository;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.CuentaContableRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.CuentaContable;



/**
 * <b>Class</b>: CuentaContableJpaRepository <br/>
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
public interface CuentaContableJpaRepository {

  List<CuentaContable> findAll(int repartoTipo);

  List<CuentaContable> findAllNotInMovCuentaContable(int repartoTipo, int periodo);

  CuentaContable findByCodCuentaContable(int repartoTipo, String codigo);

  int save(CuentaContableRequest cuenta);

  int update(CuentaContableRequest cuenta);

  boolean delete(int repartoTipo, String codigo);

  List<String> findAllCodCuentaContable(int repartoTipo);

  List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo);

  List<CuentaContable> findAllNiif(int repartoTipo);

  List<CuentaContable> findAllNotNiifCuentaContable(int repartoTipo);

  int registrarNiif(CuentaContableRequest cuenta);

  boolean quitarNiif(int repartoTipo, String codigo);

  List<String> registerNiifByExcel(List<List<CellData>> excel, int repartoTipo);


}
