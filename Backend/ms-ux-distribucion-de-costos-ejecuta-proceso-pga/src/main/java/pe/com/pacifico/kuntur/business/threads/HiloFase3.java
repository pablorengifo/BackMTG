package pe.com.pacifico.kuntur.business.threads;

import java.util.List;
import pe.com.pacifico.kuntur.repository.ProcesoJpaRepoository;

/**
 * <b>Class</b>: HiloFase3 <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 07, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */


public class HiloFase3 extends Thread {

  private static ProcesoJpaRepoository procesoJpaRepoository;
  public static int progreso;
  private static int repartoTipo;
  private static int periodo;

  /**
   * Ejecucion del hilo.
   */
  public void run() {
    //obtener la lista de objetos
    List<String> codigosObjetos = procesoJpaRepoository.listarCodigosObjetos(repartoTipo,periodo);
    int total = codigosObjetos.size();
    progreso = 0;
    procesoJpaRepoository.iniciarFase3(periodo, repartoTipo);
    //añadir registro por objeto
    for (int i = 1; i <= total;i++)
    {
      procesoJpaRepoository.insertarDistribucionesDeObjetosDeCosto(repartoTipo,periodo,codigosObjetos.get(i - 1));
      progreso = i * 100 / total;
    }
    //terminar con el proceso
    procesoJpaRepoository.insertarEjecucionFin(periodo,3,repartoTipo);
  }

  /**
   * Inicio del hilo.
   */
  public static void begin(ProcesoJpaRepoository repository, int rt, int p) {
    procesoJpaRepoository = repository;
    repartoTipo = rt;
    periodo = p;
    Thread hilo = new HiloFase3();
    hilo.start();
  }
}
