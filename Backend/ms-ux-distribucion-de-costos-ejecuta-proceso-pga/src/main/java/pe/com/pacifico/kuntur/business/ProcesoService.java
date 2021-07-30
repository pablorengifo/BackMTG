package pe.com.pacifico.kuntur.business;

import java.util.List;

import pe.com.pacifico.kuntur.expose.response.AsignacionCecoBolsaResponse;
import pe.com.pacifico.kuntur.model.EjecucionProceso;
import reactor.core.publisher.Flux;

/**
 * <b>Class</b>: ProcesoService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 06, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface ProcesoService {

  int ejecutarFase1(int repartoTipo,int periodo, String userEmail, String userNames);

  List<String> validarFase1(int repartoTipo, int periodo);

  int ejecutarFase2(int repartoTipo,int periodo, String userEmail, String userNames);

  List<String>  validarFase2(int repartoTipo,int periodo);

  int ejecutarFase3(int repartoTipo,int periodo, String userEmail, String userNames);

  List<String> validarFase3(int repartoTipo,int periodo);

  int fasesCompletadas(int repartoTipo,int periodo);

  int hayFaseEnEjecucion(int repartoTipo,int periodo);

  int obtenerEstadoCierreProceso(int repartoTipo, int periodo);

  List<String> validarCierreProceso(int repartoTipo,int periodo);

  int ejecutarCierreProceso(int repartoTipo, int periodo);

  int ejecucionTotal(int repartoTipo, int periodo, String userEmail, String userNames);

  EjecucionProceso obtenerFechaEjecucion(int repartoTipo, int periodo, int fase);

  int terminarProcesosIndeterminados();

  Flux<AsignacionCecoBolsaResponse> getAsignacionesFase1Val3(int repartoTipo, int periodo);

}
