package pe.com.pacifico.kuntur.repository;

import java.util.List;

import pe.com.pacifico.kuntur.model.AsignacionCecoBolsa;
import pe.com.pacifico.kuntur.model.EjecucionProceso;

/**
 * <b>Class</b>: ProcesoJpaRepoository <br/>
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
public interface ProcesoJpaRepoository {

  /*=======Generales=======*/

  Integer fasesCompletadas(int repartoTipo, int periodo);

  int procesoCerrado(int repartoTipo, int periodo);

  int verificarFasesEnEjecucion(int repartoTipo, int periodo);

  int insertarEjecucionIni(int periodo, int fase, int repartoTipo, String userEmail, String userNames);

  int insertarEjecucionFin(int periodo, int fase, int repartoTipo);

  int terminarProcesosIndefinidos();

  /*=======FASE 1=======*/

  boolean verificarDetalleGasto(int repartoTipo, int periodo);

  List<String> obtenerDriverFase1ConError(int repartoTipo, int periodo);

  int borrarAsignaciones(int periodo, int fase, int repartoTipo);

  void borrarDistribucionesFase1();

  void borrarDistribucionesFase2();

  void borrarDistribucionesFase3();

  int iniciarFase1(int periodo, int repartoTipo);

  int insertarDistribucionBolsas(int periodo, int repartoTipo);

  /*====================*/
  /*========Fase2===========*/
  List<String> obtenerDriverFase2ConError(int repartoTipo, int periodo);

  int maxNivelCascada(int periodo, int repartoTipo);

  int iniciarFase2(int periodo, int repartoTipo);
  /*====================*/

  /*========Fase3===========*/
  List<String> obtenerDriverFase3ConError(int repartoTipo, int periodo);

  List<String> listarCodigosObjetos(int repartoTipo, int periodo);

  int iniciarFase3(int periodo, int repartoTipo);

  int insertarDistribucionesDeObjetosDeCosto(int repartoTipo, int periodo,String codigo);

  int insertarDistribucionCascadaPorNivel(int iteracion, int periodo, int repartoTipo, double precision);

  int calcularFase2F(int periodo, int repartoTipo);

  /*=======CIERRE=======*/
  int obtenerEstadoProceso(int repartoTipo, int periodo);

  boolean verificarProcesosCompletos(int repartoTipo, int periodo);

  List<String> verificarLineasNiifAsignadas(int repartoTipo, int periodo);

  boolean existeInformacionReporteTabla(int repartoTipo, int periodo, int nroReporte);

  int insertarGeneracionReporte(int repartoTipo, int periodo, int nroReporte);

  int generarReporteBolsasOficinas(int repartoTipo, int periodo);

  int generarReporteCascada(int repartoTipo, int periodo);

  int actualizarTipoNegocio(int periodo, String tabla, int rowsUpdateTotal);

  int actualizarAsignacion(int repartoTipo, int periodo, String tabla, int rowsUpdateTotal);

  int actualizarNiif17Atribuible(int periodo, String tabla, int rowsUpdateTotal);

  int generarReporteObjetos(int repartoTipo, int periodo);

  int insertarCierreProceso(int repartoTipo,int periodo, int estado);

  int updateCierreProceso(int repartoTipo,int periodo, int estado);

  EjecucionProceso obtenerFechaEjecucion(int repartoTipo, int periodo, int fase);

  int cleanCierreProceso(int repartoTipo,int periodo);

  List<AsignacionCecoBolsa> getAsignacionesFase1Val3(int repartoTipo, int periodo);
}
