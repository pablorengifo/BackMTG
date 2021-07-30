package pe.com.pacifico.kuntur.repository;

import java.util.List;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.DetalleGasto;

/**
 * <b>Class</b>: DetalleGastoServiceJpaRepository <br/>
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
public interface DetalleGastoJpaRepository {

  List<DetalleGasto> findAll(int repartoTipo,int periodo);

  List<String> saveExcelToBdReal(List<List<CellData>> excel, int periodo);

  List<String> generarDetalleGasto(int repartoTipo,int periodo);

  List<String> saveExcelToBdPresupuesto(List<List<CellData>> excel, int periodo);

  List<String> findAllCodCuentaContable(int repartoTipo, int periodo);

  List<String> findAllCodPartida(int repartoTipo, int periodo);

  List<String> findAllCodCentro(int repartoTipo, int periodo);
}
