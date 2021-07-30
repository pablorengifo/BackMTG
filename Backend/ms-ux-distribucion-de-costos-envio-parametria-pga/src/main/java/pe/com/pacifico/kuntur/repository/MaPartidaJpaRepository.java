package pe.com.pacifico.kuntur.repository;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MaPartidaRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaPartida;


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
public interface MaPartidaJpaRepository {

  List<MaPartida> findAll(int repartoTipo);

  List<MaPartida> findAllNotInMovPartida(int repartoTipo, int periodo);

  MaPartida findByCodPartida(int repartoTipo, String codigo);

  int save(MaPartidaRequest partida);

  int update(MaPartidaRequest partida);

  boolean delete(int repartoTipo, String codigo);

  List<String> findAllCodPartida(int repartoTipo);

  List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo);

}
