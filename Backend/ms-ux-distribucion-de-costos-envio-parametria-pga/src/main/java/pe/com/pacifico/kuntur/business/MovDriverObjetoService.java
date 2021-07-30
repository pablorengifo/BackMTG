package pe.com.pacifico.kuntur.business;

import java.util.List;
import pe.com.pacifico.kuntur.expose.response.MaDriverResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverObjetoResponse;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import reactor.core.publisher.Flux;



/**
 * <b>Class</b>: MovDriverObjetoService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 28, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface MovDriverObjetoService {

  Flux<MaDriverResponse> getMaDriverObjeto(int repartoTipo, int periodo);

  Flux<MovDriverObjetoResponse> getObjetos(int repartoTipo, int periodo, String codDriver);

  Flux<MovDriverObjetoResponse> getObjetos(int repartoTipo, int periodo);

  boolean deleteMovDriverObjeto(int repartoTipo, int periodo, String codigo);

  List<String> fileRead(int repartoTipo, int periodo, RequestFileRead requestFileRead);

  /*void saveExcelToBDMa(List<List<CellData>> excel, int repartoTipo, int periodo);

  void saveExcelToBDMov(List<List<CellData>> excel, int repartoTipo, int periodo);*/

}
