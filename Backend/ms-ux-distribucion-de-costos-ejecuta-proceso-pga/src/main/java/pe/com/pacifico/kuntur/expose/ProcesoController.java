package pe.com.pacifico.kuntur.expose;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.swagger.annotations.ApiOperation;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.com.pacifico.kuntur.business.ProcesoService;
import pe.com.pacifico.kuntur.business.threads.HiloCierreProceso;
import pe.com.pacifico.kuntur.business.threads.HiloEjecucionTotal;
import pe.com.pacifico.kuntur.business.threads.HiloFase2;
import pe.com.pacifico.kuntur.business.threads.HiloFase3;
import pe.com.pacifico.kuntur.expose.response.AsignacionCecoBolsaResponse;
import pe.com.pacifico.kuntur.model.AsignacionCecoBolsa;
import pe.com.pacifico.kuntur.model.EjecucionProceso;
import reactor.core.publisher.Flux;


/**
 * <b>Class</b>: PorcesoController <br/>
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
@RestController
@CrossOrigin
@RequestMapping("/processes")
@Slf4j
@RequiredArgsConstructor
public class ProcesoController {

  private final ProcesoService procesoService;

  /**
   * This method is used to start the first process.
   * @param repartoTipo This is the first parameter to method
   * @param periodo This is the second parameter to method
   * @return the progress at 100%.
   */
  @PostMapping("fase1/{repartoTipo}/{periodo}")
  @ApiOperation(value = "MaCanal value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public int fase1(@PathVariable("repartoTipo") @Valid int repartoTipo,
                   @PathVariable("periodo") @Valid int periodo,
                   @RequestBody String json) {
    System.out.println("*******controller fase 1");
    JsonObject jsonObject = (JsonObject) JsonParser.parseString(json);
    String userEmails = jsonObject.get("userEmail").isJsonNull() ? "" : jsonObject.get("userEmail").getAsString();
    String userNames = jsonObject.get("userName").isJsonNull() ? "" : jsonObject.get("userName").getAsString();
    return procesoService.ejecutarFase1(repartoTipo,periodo, userEmails, userNames);
  }

  /**
   * This method is used to validate the parameters before doing the first proccess.
   * @param repartoTipo This is the first parameter to method
   * @param periodo This is the second parameter to method
   * @return true if all validations are ok or else it returns false.
   */
  @GetMapping("fase1/validacion/{repartoTipo}/{periodo}")
  @ApiOperation(value = "MaCanal value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public List<String> fase1Validaciones(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                        @PathVariable("periodo") @Valid int periodo) {
    System.out.println("*******controller fase 1");
    return procesoService.validarFase1(repartoTipo,periodo);
  }

  /**
   * This method is used to validate the parameters before doing the first proccess.
   * @param repartoTipo This is the first parameter to method
   * @param periodo This is the second parameter to method
   * @return true if all validations are ok or else it returns false.
   */
  @GetMapping("fase2/validacion/{repartoTipo}/{periodo}")
  @ApiOperation(value = "fase2 value", notes = "Endpoint example fase2", response = Integer.class, code = 200)
  public List<String> fase2Validaciones(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                        @PathVariable("periodo") @Valid int periodo) {
    System.out.println("*******controller fase 2");
    return procesoService.validarFase2(repartoTipo,periodo);
  }

  /**
   * This method is used to validate the parameters before doing the first proccess.
   * @param repartoTipo This is the first parameter to method
   * @param periodo This is the second parameter to method
   * @return true if all validations are ok or else it returns false.
   */
  @GetMapping("fase3/validacion/{repartoTipo}/{periodo}")
  @ApiOperation(value = "fase3 value", notes = "Endpoint example fase3", response = Integer.class, code = 200)
  public List<String> fase3Validaciones(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                   @PathVariable("periodo") @Valid int periodo) {
    System.out.println("*******controller fase 3");
    return procesoService.validarFase3(repartoTipo,periodo);
  }

  /**
   * This method is used to start the second process.
   * @param repartoTipo This is the first parameter to method
   * @param periodo This is the second parameter to method
   * @return a confirmarion code.
   */
  @PostMapping("fase2/{repartoTipo}/{periodo}")
  @ApiOperation(value = "MaCanal value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public int fase2(@PathVariable("repartoTipo") @Valid int repartoTipo,
                   @PathVariable("periodo") @Valid int periodo,
                   @RequestBody String json) {
    System.out.println("*******controller fase 2");
    JsonObject jsonObject = (JsonObject) JsonParser.parseString(json);
    String userEmails = jsonObject.get("userEmail").isJsonNull() ? "" : jsonObject.get("userEmail").getAsString();
    String userNames = jsonObject.get("userName").isJsonNull() ? "" : jsonObject.get("userName").getAsString();
    return procesoService.ejecutarFase2(repartoTipo,periodo, userEmails, userNames);
  }

  @GetMapping("fase2/obtener")
  @ApiOperation(value = "prueba value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public int obtenerFase2() {
    System.out.println("*******controller obteniendo Progreso Hilo fase 2: " + HiloFase2.progreso);
    return HiloFase2.progreso;
  }

  @GetMapping("fase3/obtener")
  @ApiOperation(value = "prueba value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public int obtenerFase3() {
    System.out.println("*******controller obteniendo Progreso Hilo fase 3: " + HiloFase3.progreso);
    return HiloFase3.progreso;
  }

  @GetMapping("cierre/obtener")
  @ApiOperation(value = "prueba value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public int obtenerCierreProceso() {
    System.out.println("*******controller obteniendo Progreso Hilo Cierre Proceso: " + HiloCierreProceso.progreso);
    return HiloCierreProceso.progreso;
  }

  /**
   * This method is used to start the third process.
   * @param repartoTipo This is the first parameter to method
   * @param periodo This is the second parameter to method
   * @return a confirmation code.
   */
  @PostMapping("fase3/{repartoTipo}/{periodo}")
  @ApiOperation(value = "fase3 value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public int fase3(@PathVariable("repartoTipo") @Valid int repartoTipo,
                   @PathVariable("periodo") @Valid int periodo,
                   @RequestBody String json) {
    System.out.println("*******controller fase 3");
    JsonObject jsonObject = (JsonObject) JsonParser.parseString(json);
    String userEmails = jsonObject.get("userEmail").isJsonNull() ? "" : jsonObject.get("userEmail").getAsString();
    String userNames = jsonObject.get("userName").isJsonNull() ? "" : jsonObject.get("userName").getAsString();
    return procesoService.ejecutarFase3(repartoTipo,periodo, userEmails, userNames);
  }

  @GetMapping("cierre/validacion/{repartoTipo}/{periodo}")
  @ApiOperation(value = "fase3 value", notes = "Endpoint example cierreProceso", response = Integer.class, code = 200)
  public List<String> cierreProcesoValidaciones(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                        @PathVariable("periodo") @Valid int periodo) {
    System.out.println("*******controller cierre proceso");
    return procesoService.validarCierreProceso(repartoTipo,periodo);
  }

  /**
   * This method is used obtain the status of the closure process.
   * @param repartoTipo This is the first parameter to method
   * @param periodo This is the second parameter to method
   * @return a confirmation code.
   */
  @GetMapping("cierre/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Cerrar Proceso value", notes = "Cerrar Proceso", response = Integer.class, code = 200)
  public int estadoCierreProceso(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                     @PathVariable("periodo") @Valid int periodo) {
    System.out.println("******* Controller GET cerrar proceso");
    return procesoService.obtenerEstadoCierreProceso(repartoTipo, periodo);
  }

  /**
   * This method is used to start the closure process.
   * @param repartoTipo This is the first parameter to method
   * @param periodo This is the second parameter to method
   * @return a confirmation code.
   */
  @PostMapping("cierre/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Cerrar Proceso value", notes = "Cerrar Proceso", response = Integer.class, code = 200)
  public int cerrarProceso(@PathVariable("repartoTipo") @Valid int repartoTipo,
                   @PathVariable("periodo") @Valid int periodo) {
    System.out.println("*******controller cerrarProceso");
    return procesoService.ejecutarCierreProceso(repartoTipo,periodo);
  }

  /**
   * This method is used to obtain the executed phases.
   * @param repartoTipo This is the first parameter to method
   * @param periodo This is the second parameter to method
   * @return a confirmation code.
   */
  @GetMapping("{repartoTipo}/{periodo}")
  @ApiOperation(value = "int", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public int fasesEjecutadas(@PathVariable("repartoTipo") @Valid int repartoTipo,
                             @PathVariable("periodo") @Valid int periodo) {
    System.out.println("*******controller fasesEjecutadas");
    return procesoService.fasesCompletadas(repartoTipo,periodo);
  }

  /**
   * This method is used to obtain the phase that is being executed.
   * @param repartoTipo This is the first parameter to method
   * @param periodo This is the second parameter to method
   * @return a confirmation code.
   */
  @GetMapping("executing/{repartoTipo}/{periodo}")
  @ApiOperation(value = "fases en ejecucion", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public int faseEnEjecucion(@PathVariable("repartoTipo") @Valid int repartoTipo,
                             @PathVariable("periodo") @Valid int periodo) {
    System.out.println("*******controller hay fase en ejecucion");
    return procesoService.hayFaseEnEjecucion(repartoTipo,periodo);
  }

  /**
   * This method is used to obtain the total execution status.
   * @return a confirmation code.
   */
  @GetMapping("ejecucionTotal/obtener")
  @ApiOperation(value = "prueba value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public int obtenerEjecucionTotal() {
    System.out.println("*******controller obteniendo Progreso Hilo fase T " + HiloEjecucionTotal.progreso);
    return HiloEjecucionTotal.progreso;
  }

  /**
   * This method is used to start the total execution process.
   * @param repartoTipo This is the first parameter to method
   * @param periodo This is the second parameter to method
   * @return a confirmation code.
   */
  @PostMapping("ejecucionTotal/{repartoTipo}/{periodo}")
  @ApiOperation(value = "MaCanal value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public int ejecucionTotal(@PathVariable("repartoTipo") @Valid int repartoTipo,
                            @PathVariable("periodo") @Valid int periodo,
                            @RequestBody String json) {
    System.out.println("*******controller fase total");
    JsonObject jsonObject = (JsonObject) JsonParser.parseString(json);
    String userEmails = jsonObject.get("userEmail").isJsonNull() ? "" : jsonObject.get("userEmail").getAsString();
    String userNames = jsonObject.get("userName").isJsonNull() ? "" : jsonObject.get("userName").getAsString();
    return procesoService.ejecucionTotal(repartoTipo,periodo, userEmails, userNames);
  }

  /**
   * This method is used to obtain the execution info for a phase.
   * @param repartoTipo This is the first parameter to method
   * @param periodo This is the second parameter to method
   * @param fase This is the third parameter to method
   * @return a confirmation code.
   */
  @PostMapping("date/{repartoTipo}/{periodo}/{fase}")
  @ApiOperation(value = "fases en ejecucion", notes = "Endpoint example canales", response = String.class, code = 200)
  public EjecucionProceso obtenerFechaEjecucion(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                @PathVariable("periodo") @Valid int periodo,
                                                @PathVariable("fase") @Valid int fase) {
    System.out.println("*******controller obtener fecha de ejecucion");
    return procesoService.obtenerFechaEjecucion(repartoTipo,periodo,fase);
  }

  /**
   * This method is used to force finish the unfinished processes.
   * @return a confirmation code.
   */
  @GetMapping("end")
  @ApiOperation(value = "fases en ejecucion", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public int terminarProcesosIndeterminados() {
    System.out.println("*******controller terminar procesos indeterminados");
    return procesoService.terminarProcesosIndeterminados();
  }

  /*@GetMapping("cierre/keepAlive")
  @ApiOperation(value = "prueba value", notes = "Endpoint example cierre", response = Integer.class, code = 200)
  public boolean cerrandoProceso() {
    System.out.println("*******controller Evaluando si se esta cerrando proceso");
    return HiloCierreProceso.enEjecucion || !HiloCierreProceso.terminoEjecucion;
  }*/

  /**
   * get errores.
   * @return
   */
  @GetMapping("cierre/errores")
  @ApiOperation(value = "prueba value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public List<String> obtenerErroresEnCierreProceso() {
    System.out.println("*******controller Obtener errores cierre proceso");
    List<String> errores = HiloCierreProceso.errores;
    HiloCierreProceso.limpiarHilo();
    return errores;
  }

  /**
   * This method is used to get Asignaciones Bolsa.
   * @return asignaciones bolsa.
   */
  @GetMapping("asignaciones/fase1val3/{repartoTipo}/{periodo}")
  @ApiOperation(value = "AsignacionCecoBolsa value", notes = "Endpoint example AsignacionCecoBolsa",
          response = AsignacionCecoBolsa.class, code = 200)
  public Flux<AsignacionCecoBolsaResponse> getAsignacionesFase1Val3(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                           @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llegaAsignacionesFase1Val3 ");
    return procesoService.getAsignacionesFase1Val3(repartoTipo,periodo).log();
  }

}
