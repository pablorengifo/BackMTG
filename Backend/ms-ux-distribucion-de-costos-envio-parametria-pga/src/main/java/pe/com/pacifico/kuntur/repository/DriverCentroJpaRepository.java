package pe.com.pacifico.kuntur.repository;

import java.util.List;

import pe.com.pacifico.kuntur.expose.request.DriverCentroRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaDriver;
import pe.com.pacifico.kuntur.model.MovDriverCentro;

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
public interface DriverCentroJpaRepository {

  List<MaDriver> findAll(int repartoTipo, int periodo);

  List<MovDriverCentro> findAllMov(int repartoTipo, int periodo, String codDriverCentro);

  List<MovDriverCentro> findAllMov(int repartoTipo, int periodo);

  int save(DriverCentroRequest driverCentroRequest);

  int update(DriverCentroRequest driverCentroRequest);

  boolean delete(int repartoTipo, int periodo, String codigo);

  List<String> saveExcelToBdMa(List<List<CellData>> excel, int repartoTipo, int periodo);

  List<String> saveExcelToBdMov(List<List<CellData>> excel, int repartoTipo, int periodo);

  MovDriverCentro findByCodDriver(String codigo);

  List<String> findAllCodDriver(int repartoTipo);


}
