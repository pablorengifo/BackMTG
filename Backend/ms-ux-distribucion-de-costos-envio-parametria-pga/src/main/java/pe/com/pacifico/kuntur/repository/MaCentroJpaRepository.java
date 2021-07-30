package pe.com.pacifico.kuntur.repository;

import java.util.List;
import pe.com.pacifico.kuntur.expose.request.MaCentroRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaCentro;

/**
 * <b>Class</b>: MaCentroJpaRepository <br/>
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
public interface MaCentroJpaRepository {

  List<MaCentro> findAll(int repartoTipo);

  List<MaCentro> findAllNotInMovCentro(int repartoTipo,int periodo);

  MaCentro findByCodCentro(int repartoTipo,String codigo);

  int save(MaCentroRequest centro);

  int update(MaCentroRequest centro);

  boolean delete(int repartoTipo,String codigo);

  List<String> findAllCodCentro(int repartoTipo);

  List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo);

}
