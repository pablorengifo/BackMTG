package pe.com.pacifico.kuntur.business.threads;

import pe.com.pacifico.kuntur.repository.ProcesoJpaRepoository;

/**
 * <b>Class</b>: HiloFase2 <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 6, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public class HiloFase2 extends Thread {

  private static final int fase = 2;
  public static int progreso;
  private static ProcesoJpaRepoository procesoJpaRepository;
  private static int repartoTipo;
  private static int periodo;

  /**
   * Inicio del hilo.
   */
  public static void begin(ProcesoJpaRepoository repository, int rt, int p) {
    procesoJpaRepository = repository;
    repartoTipo = rt;
    periodo = p;
    Thread hilo = new HiloFase2();
    hilo.start();
  }

  /**
   * Ejecucion del hilo.
   */
  public void run() {
    progreso = 0;
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
      progreso = centroI++ * 100 / (maxNivel + 1);
      //updateProgress(++centroI, maxNivel+1);
    }
    // 5) Se calcula la fase 2F
    procesoJpaRepository.calcularFase2F(periodo, repartoTipo);
    progreso = (maxNivel + 1) * 100 / (maxNivel + 1);
    // 6) Se Ejecuta el fin de ejecucion
    procesoJpaRepository.insertarEjecucionFin(periodo, fase, repartoTipo);
  }
}
