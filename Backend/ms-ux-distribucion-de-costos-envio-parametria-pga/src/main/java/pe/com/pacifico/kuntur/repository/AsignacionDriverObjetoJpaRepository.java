package pe.com.pacifico.kuntur.repository;

import java.util.List;

import pe.com.pacifico.kuntur.expose.request.AsignacionDriverObjetoRequest;
import pe.com.pacifico.kuntur.model.AsignacionDriverObjeto;
import pe.com.pacifico.kuntur.model.CellData;


/**
 * <b>Class</b>: AsignacionDriverObjetoJpaRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 04, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface AsignacionDriverObjetoJpaRepository {

  List<AsignacionDriverObjeto> findAll(int repartoTipo, int periodo);

  //List<MovDriverObjeto> findAllMov(int repartoTipo, int periodo, String codDriverObjeto);

  int save(AsignacionDriverObjetoRequest asignacion);

  boolean delete(int repartoTipo,int periodo, String codigo, String grupoGasto);

  List<String> saveExcelToBd(List<List<CellData>> excel, int periodo, int repartoTipo);

  //List<String> findAllCodCentro(int repartoTipo, int periodo);

  //List<String> findAllGrupoGasto();

  //List<String> findAllCodDriver(int repartoTipo, int periodo);

}
