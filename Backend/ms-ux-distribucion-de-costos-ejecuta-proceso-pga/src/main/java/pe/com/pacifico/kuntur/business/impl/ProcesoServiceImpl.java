package pe.com.pacifico.kuntur.business.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.com.pacifico.kuntur.business.ProcesoService;
import pe.com.pacifico.kuntur.business.threads.HiloCierreProceso;
import pe.com.pacifico.kuntur.business.threads.HiloEjecucionTotal;
import pe.com.pacifico.kuntur.business.threads.HiloFase2;
import pe.com.pacifico.kuntur.business.threads.HiloFase3;
import pe.com.pacifico.kuntur.expose.response.AsignacionCecoBolsaResponse;
import pe.com.pacifico.kuntur.model.AsignacionCecoBolsa;
import pe.com.pacifico.kuntur.model.EjecucionProceso;
import pe.com.pacifico.kuntur.repository.ProcesoJpaRepoository;
import reactor.core.publisher.Flux;


/**
 * <b>Class</b>: ProcesoServiceImpl <br/>
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

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcesoServiceImpl implements ProcesoService {

  private final ProcesoJpaRepoository procesoJpaRepoository;

  @Override
  public int ejecutarFase1(int repartoTipo, int periodo, String userEmail, String userNames) {

    log.info("INICIO EJECUCION FASE 1.");

    int fase = 1;

    //Limpiar tablas relacionadas
    procesoJpaRepoository.borrarAsignaciones(periodo, fase, repartoTipo);
    procesoJpaRepoository.borrarDistribucionesFase1();
    procesoJpaRepoository.borrarDistribucionesFase2();
    procesoJpaRepoository.borrarDistribucionesFase3();
    procesoJpaRepoository.cleanCierreProceso(repartoTipo, periodo);

    // LO QUE VIENE A SER DistribuirFase1Task.java

    procesoJpaRepoository.insertarEjecucionIni(periodo,fase,repartoTipo, userEmail, userNames);

    procesoJpaRepoository.iniciarFase1(periodo,repartoTipo);

    procesoJpaRepoository.insertarDistribucionBolsas(periodo,repartoTipo);

    procesoJpaRepoository.insertarEjecucionFin(periodo,fase,repartoTipo);

    log.info("FIN EJECUCION FASE 1.");

    //se debería validar que se subio all exitosamente
    return 100;
  }

  @Override
  public List<String>  validarFase1(int repartoTipo, int periodo) {

    List<String> error = new ArrayList<>();
    //PASO 1: Validar que no se esté ejecutando ninguna fase en un hilo aparte
    log.info("INICIO VALIDACION 1.");
    boolean ejecutandoFase;
    ejecutandoFase = procesoJpaRepoository.verificarFasesEnEjecucion(repartoTipo, periodo) > 0;
    if (ejecutandoFase) {
      log.error("VALIDACION 1 ERROR: Alguna fase se esta ejecutando actualmente.");
      error.add("ERROR: Alguna fase se esta ejecutando actualmente.");
      return error;
    }
    log.info("VALIDACION 1 CORRECTO.");

    //PASO 2: Validar que el input (DETALLE GASTO) esté cargado
    log.info("VALIDACION PASO 2.");
    //Consulta si COUNT de detalle gasto del periodo > 0
    boolean detalleGastoCargado = procesoJpaRepoository.verificarDetalleGasto(repartoTipo, periodo);
    if (!detalleGastoCargado) {
      log.error("VALIDACION 2 ERROR: Falta cargar el Detalle Gasto del periodo correspondiente.");
      error.add("ERROR: Falta cargar el Detalle Gasto del periodo correspondiente.");
      return error;
    }
    log.info("VALIDACION 2 CORRECTO.");


    //PASO 3: Obtener registros de DETALLE GASTO que no tengan driver y los muestra
    log.info("VALIDACION PASO 3.");

    List<String> lstSinDriver = procesoJpaRepoository.obtenerDriverFase1ConError(repartoTipo, periodo);

    if (lstSinDriver == null) {
      log.error("VALIDACION PASO 3 ERROR: No se pudo consultar las Asignaciones de la Fase 1.");
      error.add("ERROR: No se pudo consultar las Asignaciones de la Fase 1.");
      return error;
    }

    if (lstSinDriver.size() > 0) {
      log.error("VALIDACION PASO 3 ERROR: " + lstSinDriver.size() + " registro(s) del DETALLE GASTO no tienen Asignaciones en Fase 1:");
      error.add("ERROR: " + lstSinDriver.size() + " registro(s) del DETALLE GASTO no tienen Asignaciones en Fase 1:");
      for (String fila : lstSinDriver) {
        log.info("- " + fila);
        error.add("- " + fila);
      }
      return error;
    }

    log.info("VALIDACION 3 CORRECTO.");
    return null;
  }

  @Override
  public int ejecutarFase2(int repartoTipo, int periodo, String userEmail, String userNames) {
    int fase = 2;
    procesoJpaRepoository.borrarAsignaciones(periodo, fase, repartoTipo);
    // Limpiar tablas relacionadas
    procesoJpaRepoository.borrarDistribucionesFase2();
    // Primer registro de ejecuciones
    procesoJpaRepoository.insertarEjecucionIni(periodo, fase, repartoTipo, userEmail, userNames);
    // Iniciar ejecución del hilo
    HiloFase2.begin(procesoJpaRepoository, repartoTipo, periodo);

    return 200;
  }

  @Override
  public List<String> validarFase2(int repartoTipo, int periodo) {
    List<String> error = new ArrayList<>();
    //PASO 1: Validar que no se esté ejecutando ninguna fase en un hilo aparte
    log.info("INICIO VALIDACION 1.");
    boolean ejecutandoFase;
    ejecutandoFase = procesoJpaRepoository.verificarFasesEnEjecucion(repartoTipo, periodo) > 0;
    if (ejecutandoFase) {
      log.error("VALIDACION 1 ERROR: Alguna fase se esta ejecutando actualmente.");
      error.add("ERROR: Alguna fase se esta ejecutando actualmente.");
      return error;
    }
    log.info("VALIDACION 1 CORRECTO.");

    //PASO: Validar que las fases anteriores se hayan ejecutado
    //SE REALIZA POR FRONT

    //PASO 2: Mostrar los centros que tengan driver erróneo
    log.info("VALIDACION PASO 2.");

    List<String> lstSinDriver = procesoJpaRepoository.obtenerDriverFase2ConError(repartoTipo, periodo);

    if (lstSinDriver == null) {
      log.error("VALIDACION PASO 2 ERROR: No se pudo consultar las Asignaciones de la Fase 2.");
      error.add("ERROR: No se pudo consultar las Asignaciones de la Fase 2.");
      return error;
    }

    if (lstSinDriver.size() > 0) {
      log.error("VALIDACION PASO 2 ERROR: " + lstSinDriver.size()
              + " registro(s) relacionados a Asignaciones Fase 2 presentan errores:");
      error.add("ERROR: " + lstSinDriver.size() + " registro(s) relacionados a Asignaciones Fase 2 presentan errores:");
      for (String fila : lstSinDriver) {
        log.info("- " + fila);
        error.add("- " + fila);
      }
      return error;
    }

    log.info("VALIDACION 2 CORRECTO.");

    //PASO: Validar que no se haya ejecutado la fase 2 previamente, de lo contrario confirmar antes de ejecutar.
    //SE REALIZA POR FRONT

    return null;
  }

  @Override
  public int ejecutarFase3(int repartoTipo, int periodo, String userEmail, String userNames) {
    int fase = 3;
    //Limpiar tablas relacionadas
    procesoJpaRepoository.borrarAsignaciones(periodo, fase, repartoTipo);
    procesoJpaRepoository.borrarDistribucionesFase3();

    //Primer registro de ejecuciones
    procesoJpaRepoository.insertarEjecucionIni(periodo, fase, repartoTipo, userEmail, userNames);

    //iniciar el hilo
    HiloFase3.begin(procesoJpaRepoository,repartoTipo,periodo);

    return 200;
  }

  @Override
  public List<String> validarFase3(int repartoTipo, int periodo) {
    List<String> error = new ArrayList<>();
    //PASO 1: Validar que no se esté ejecutando ninguna fase en un hilo aparte
    log.info("INICIO VALIDACION 1.");
    boolean ejecutandoFase;
    ejecutandoFase = procesoJpaRepoository.verificarFasesEnEjecucion(repartoTipo, periodo) > 0;
    if (ejecutandoFase) {
      log.error("VALIDACION 1 ERROR: Alguna fase se esta ejecutando actualmente.");
      error.add("ERROR: Alguna fase se esta ejecutando actualmente.");
      return error;
    }
    log.info("VALIDACION 1 CORRECTO.");

    //PASO: Validar que las fases anteriores se hayan ejecutado
    //SE REALIZA POR FRONT

    //PASO 2: Mostrar los centros que tengan driver erróneo
    List<String> lstSinDriver = procesoJpaRepoository.obtenerDriverFase3ConError(repartoTipo, periodo);

    if (lstSinDriver == null) {
      log.error("VALIDACION PASO 2 ERROR: No se pudo consultar las Asignaciones de la Fase 3.");
      error.add("ERROR: No se pudo consultar las Asignaciones de la Fase 3.");
      return error;
    }

    if (lstSinDriver.size() > 0) {
      log.error("VALIDACION PASO 2 ERROR: " + lstSinDriver.size()
              + " registro(s) relacionados a Asignaciones Fase 3 presentan errores:");
      error.add("ERROR: " + lstSinDriver.size() + " registro(s) relacionados a Asignaciones Fase 3 presentan errores:");
      for (String fila : lstSinDriver) {
        log.info("- " + fila);
        error.add("- " + fila);
      }
      return error;
    }

    log.info("VALIDACION 2 CORRECTO.");
    //PASO: Validar que no se haya ejecutado la fase 3 previamente, de lo contrario confirmar antes de ejecutar.
    //SE REALIZA POR FRONT

    return null;
  }

  @Override
  public int fasesCompletadas(int repartoTipo, int periodo) {
    return procesoJpaRepoository.fasesCompletadas(repartoTipo,periodo);
  }

  @Override
  public int hayFaseEnEjecucion(int repartoTipo, int periodo) {
    return procesoJpaRepoository.verificarFasesEnEjecucion(repartoTipo,periodo);
  }

  @Override
  public int obtenerEstadoCierreProceso(int repartoTipo, int periodo) {
    return procesoJpaRepoository.procesoCerrado(repartoTipo, periodo);
  }

  @Override
  public List<String> validarCierreProceso(int repartoTipo, int periodo) {
    List<String> error = new ArrayList<>();
    log.info("CIERRE PROCESO - INICIO VALIDACION 1.");

    boolean procesosCompletos = procesoJpaRepoository.verificarProcesosCompletos(repartoTipo, periodo);

    if (!procesosCompletos) {
      log.error("CIERRE PROCESO - VALIDACION 1 ERROR: Falta ejecutar alguna fase del periodo.");
      error.add("ERROR: Falta ejecutar alguna fase del periodo.");
      return error;
    }

    log.info("CIERRE PROCESO - VALIDACION 1 CORRECTA.");

    int estadoProceso = procesoJpaRepoository.obtenerEstadoProceso(repartoTipo, periodo);
    /*
     * DE BD:
     *  2 = Completo
     *  1 = En Ejecucion
     *  0 = Hubo Error
     * DE QUERY:
     *  -1 = No existe registro -> Debe crearlo.
     *  -2 = Error al consultar la tabla.
     */

    log.info("CIERRE PROCESO - INICIO VALIDACION 2.");

    if (estadoProceso == -2) {
      log.info("CIERRE PROCESO - VALIDACION 2 ERROR: No se pudo consultar MOV_CIERRE_PROCESO.");
      error.add("ERROR: No se pudo consultar MOV_CIERRE_PROCESO.");
      return error;
    }

    if (estadoProceso == 1) {
      log.info("CIERRE PROCESO - VALIDACION 2 ERROR: El proceso se encuentra actualmente en ejecución.");
      error.add("ERROR: El proceso se encuentra actualmente en ejecución.");
      return error;
    }

    log.info("CIERRE PROCESO - VALIDACION 2 CORRECTA.");

    log.info("CIERRE PROCESO - INICIO VALIDACION 3.");

    List<String> errores = procesoJpaRepoository.verificarLineasNiifAsignadas(repartoTipo,periodo);

    if (errores == null)
    {
      error.add("No se pudo consultar LINEA_NIIF");
      return error;
    }
    if (errores.size() > 0) {
      errores.add(0,"No se han asignado porcentaje a las siguientes lineas niif: ");
      return errores;
    }
    log.info("CIERRE PROCESO - VALIDACION 3 CORRECTA.");
    return null;
  }

  @Override
  public int ejecutarCierreProceso(int repartoTipo, int periodo) {

    int estadoProceso = procesoJpaRepoository.obtenerEstadoProceso(repartoTipo, periodo);

    //Actualizar Estado Cierre Proceso
    if (estadoProceso == -1) {
      procesoJpaRepoository.insertarCierreProceso(repartoTipo, periodo, 1);
    } else {
      procesoJpaRepoository.updateCierreProceso(repartoTipo, periodo, 1);
    }

    //Iniciar el hilo
    HiloCierreProceso.begin(procesoJpaRepoository, repartoTipo, periodo);

    return 200;
  }

  @Override
  public int ejecucionTotal(int repartoTipo, int periodo, String userEmail, String userNames) {
    int fase = 2;

    final int val = ejecutarFase1(repartoTipo, periodo, userEmail, userNames);

    procesoJpaRepoository.borrarAsignaciones(periodo, fase, repartoTipo);
    // Limpiar tablas relacionadas
    procesoJpaRepoository.borrarDistribucionesFase2();
    // Primer registro de ejecuciones
    procesoJpaRepoository.insertarEjecucionIni(periodo, fase, repartoTipo, userEmail, userNames);
    // Iniciar ejecución del hilo
    //HiloFase2.begin(procesoJpaRepoository, repartoTipo, periodo);
    System.out.println("**** -> Val : " + val);
    HiloEjecucionTotal.begin(procesoJpaRepoository, repartoTipo, periodo, userEmail, userNames);

    return val;
  }

  @Override
  public EjecucionProceso obtenerFechaEjecucion(int repartoTipo, int periodo, int fase) {
    return procesoJpaRepoository.obtenerFechaEjecucion(repartoTipo,periodo,fase);
    /*Date fecha = procesoJpaRepoository.obtenerFechaEjecucion(repartoTipo,periodo,fase);
    long respuesta;
    if (fecha == null)
    {
      respuesta = -1; // Se ha tenido un error en el registro de la fecha
    } else
    {
      respuesta = fecha.getTime();
    }
    return respuesta;*/
  }

  @Override
  public int terminarProcesosIndeterminados() {
    return procesoJpaRepoository.terminarProcesosIndefinidos();
  }

  @Override
  public Flux<AsignacionCecoBolsaResponse> getAsignacionesFase1Val3(int repartoTipo, int periodo) {
    return Flux.fromIterable(procesoJpaRepoository.getAsignacionesFase1Val3(repartoTipo, periodo))
            .map(this::asignacionCecoBolsaResponse);
  }

  private AsignacionCecoBolsaResponse asignacionCecoBolsaResponse(AsignacionCecoBolsa asignacionCecoBolsa)
  {
    return AsignacionCecoBolsaResponse.builder()
            .codCentro(asignacionCecoBolsa.getCodCentro())
            .codCuentaContable(asignacionCecoBolsa.getCodCuentaContable())
            .codDriver(asignacionCecoBolsa.getCodDriver())
            .codPartida(asignacionCecoBolsa.getCodPartida())
            .nombreCentro(asignacionCecoBolsa.getNombreCentro())
            .nombreCuentaContable(asignacionCecoBolsa.getNombreCuentaContable())
            .nombreDriver(asignacionCecoBolsa.getNombreDriver())
            .nombrePartida(asignacionCecoBolsa.getNombrePartida())
            .build();
  }
}
