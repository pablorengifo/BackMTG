package pe.com.pacifico.kuntur.repository;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverCascadaRequest;
import pe.com.pacifico.kuntur.model.AsignacionDriverCascada;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovDriverCentro;

/**
 * <b>Class</b>: AsignacionDriverCascadaJpaRepository <br/>
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
public interface AsignacionDriverCascadaJpaRepository {

  List<AsignacionDriverCascada> findAll(int repartoTipo, int periodo);

  List<String> saveExcelToBd(List<List<CellData>> excel, int periodo, int repartoTipo);

  List<MovDriverCentro> reportByCodDriver(int repartoTipo, int periodo, String codDriver);

  boolean delete(int repartoTipo,int periodo, String codDriverCentro);

  int save(AsignacionDriverCascadaRequest asignacionDriverCascadaRequest);
}
