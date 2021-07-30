package pe.com.pacifico.kuntur.business.threads;

import java.util.List;
import pe.com.pacifico.kuntur.repository.ProcesoJpaRepoository;

/**
 * <b>Class</b>: HiloEjecucionTotal <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 6, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public class HiloEjecucionTotal extends Thread {

  public static int progreso;
  private static int fase = 0;
  private static ProcesoJpaRepoository procesoJpaRepository;
  private static int repartoTipo;
  private static int periodo;
  private static String userEmail;
  private static String userNames;

  /**
   * Inicio del hilo.
   */
  public static void begin(ProcesoJpaRepoository repository, int rt, int p, String ue, String un) {
    procesoJpaRepository = repository;
    repartoTipo = rt;
    periodo = p;
    userEmail = ue;
    userNames = un;
    Thread hilo = new HiloEjecucionTotal();
    hilo.start();
  }

  /**
   * Ejecucion del hilo.
   */
  public void run() {
    System.out.println("~~~~ -> Progreso : " + progreso);
    // Fase 2:
    fase = 2;
    progreso = 100;
    // 2) Obtener max nivel de cascada
    int maxNivel = procesoJpaRepository.maxNivelCascada(periodo, repartoTipo);
    // 2a) Se declara centroI = 0.
    int centroI = 0;
    // 2b) Se actualiza el proceso (0/total+1)
    //progreso = centroI * 100 / (maxNivel + 1);
    // 3) Se inicia la fase 2
    procesoJpaRepository.iniciarFase2(periodo, repartoTipo);
    // 4) Se ejecuta la iteracion, empezando por iter=1 hasta maxNivel.
    for (int iter = 1; iter <= maxNivel; ++iter) {
      procesoJpaRepository.insertarDistribucionCascadaPorNivel(iter, periodo, repartoTipo, 0.00001);
      progreso = 100 + (centroI++ * 100 / (maxNivel + 1));
      //updateProgress(++centroI, maxNivel+1);
      System.out.println("~~~~ -> Progreso : " + progreso);
    }
    // 5) Se calcula la fase 2F
    procesoJpaRepository.calcularFase2F(periodo, repartoTipo);
    progreso = 100 + ((maxNivel + 1) * 100 / (maxNivel + 1));
    // 6) Se Ejecuta el fin de ejecucion
    procesoJpaRepository.insertarEjecucionFin(periodo, fase, repartoTipo);
    // Fase 3:
    fase = 3;
    progreso = 200;
    System.out.println("~~~~ -> Progreso : " + progreso);
    //Limpiar tablas relacionadas
    procesoJpaRepository.borrarAsignaciones(periodo, fase, repartoTipo);
    procesoJpaRepository.borrarDistribucionesFase3();

    //Primer registro de ejecuciones
    procesoJpaRepository.insertarEjecucionIni(periodo, fase, repartoTipo, userEmail, userNames);

    //obtener la lista de objetos
    List<String> codigosObjetos = procesoJpaRepository.listarCodigosObjetos(repartoTipo, periodo);
    int total = codigosObjetos.size();
    procesoJpaRepository.iniciarFase3(periodo, repartoTipo);
    //añadir registro por objeto
    for (int i = 1; i <= total; i++) {
      procesoJpaRepository.insertarDistribucionesDeObjetosDeCosto(repartoTipo, periodo, codigosObjetos.get(i - 1));
      progreso = 200 + (i * 100 / total);
      System.out.println("~~~~ -> Progreso : " + progreso);
    }
    //terminar con el proceso
    procesoJpaRepository.insertarEjecucionFin(periodo, 3, repartoTipo);
    progreso = 300;
  }
}
