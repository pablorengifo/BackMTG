package pe.com.pacifico.kuntur.business.threads;

import java.util.ArrayList;
import java.util.List;
import pe.com.pacifico.kuntur.repository.ProcesoJpaRepoository;

/**
 * <b>Class</b>: HiloCierreProceso <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     Jun 24, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public class HiloCierreProceso extends Thread {

  public static int progreso;
  public static ProcesoJpaRepoository procesoJpaRepository;
  public static int repartoTipo;
  public static int periodo;
  public static Thread hilo = null;
  public static boolean enEjecucion = false;
  public static boolean terminoEjecucion = false;
  public static List<String> errores;

  /**
   * Inicio del hilo.
   */
  public static void begin(ProcesoJpaRepoository repository, int rt, int p) {
    procesoJpaRepository = repository;
    repartoTipo = rt;
    periodo = p;
    progreso = 0;
    Thread hilo = new HiloCierreProceso();
    enEjecucion = true;
    hilo.start();
  }

  /**
   * Ejecucion del hilo.
   */
  public void run() {
    List<String> errores = new ArrayList<>();

    System.out.println("~~~~ -> Progreso : " + progreso);

    int sinError;

    //GENERAR REPORTE BOLSAS OFICINAS
    System.out.println("CIERRE PROCESO - INICIO GENERAR REPORTE BOLSAS OFICINAS.");
    sinError = procesoJpaRepository.generarReporteBolsasOficinas(repartoTipo, periodo);

    if (sinError != 1) {
      System.out.println("CIERRE PROCESO - ERROR AL GENERAR REPORTE BOLSAS OFICINAS.");
      procesoJpaRepository.updateCierreProceso(repartoTipo, periodo, 0);
      errores.add("ERROR AL GENERAR REPORTE BOLSAS OFICINAS.");
      errorHilo(errores);
      return;
    }

    System.out.println("CIERRE PROCESO - FIN GENERAR REPORTE BOLSAS OFICINAS.");
    progreso = 1;

    //GENERAR REPORTE CASCADA
    System.out.println("CIERRE PROCESO - INICIO GENERAR REPORTE CASCADA.");
    sinError = procesoJpaRepository.generarReporteCascada(repartoTipo, periodo);

    if (sinError != 1) {
      System.out.println("CIERRE PROCESO - ERROR AL GENERAR REPORTE CASCADA.");
      procesoJpaRepository.updateCierreProceso(repartoTipo, periodo, 0);
      errores.add("ERROR AL GENERAR REPORTE CASCADA.");
      errorHilo(errores);
      return;
    }

    System.out.println("CIERRE PROCESO - FIN GENERAR REPORTE CASCADA.");
    progreso = 2;

    //GENERAR REPORTE OBJETOS
    System.out.println("CIERRE PROCESO - INICIO GENERAR REPORTE OBJETOS.");
    sinError = procesoJpaRepository.generarReporteObjetos(repartoTipo, periodo);

    if (sinError != 1) {
      System.out.println("CIERRE PROCESO - ERROR AL GENERAR REPORTE OBJETOS.");
      procesoJpaRepository.updateCierreProceso(repartoTipo, periodo, 0);
      errores.add("ERROR AL GENERAR REPORTE OBJETOS.");
      errorHilo(errores);
      return;
    }

    System.out.println("CIERRE PROCESO - FIN GENERAR REPORTE OBJETOS.");

    System.out.println("CIERRE PROCESO - INICIO BORRAR DISTRIBUCIONES.");
    procesoJpaRepository.borrarDistribucionesFase1();
    procesoJpaRepository.borrarDistribucionesFase2();
    procesoJpaRepository.borrarDistribucionesFase3();
    System.out.println("CIERRE PROCESO - FIN BORRAR DISTRIBUCIONES.");

    procesoJpaRepository.updateCierreProceso(repartoTipo, periodo, 2);

    System.out.println("CIERRE PROCESO - TERMINADO CORRECTAMENTE.");
    progreso = 3;

    HiloCierreProceso.enEjecucion = false;
    HiloCierreProceso.errores = null;
    HiloCierreProceso.terminoEjecucion = true;
  }

  /**
   * Error en el hilo.
   */
  public static void errorHilo(List<String> errores) {
    HiloCierreProceso.progreso = -1;
    HiloCierreProceso.enEjecucion = false;
    HiloCierreProceso.errores = errores;
    HiloCierreProceso.terminoEjecucion = true;
  }

  /**
   * Limpiar el hilo.
   */
  public static void limpiarHilo() {
    hilo = null;
    enEjecucion = false;
    terminoEjecucion = false;
    errores = null;
  }
}
