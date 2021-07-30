package pe.com.pacifico.kuntur.expose;

import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.pacifico.kuntur.business.AsignacionDriverCascadaService;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverCascadaRequest;
import pe.com.pacifico.kuntur.expose.response.AsignacionDriverCascadaResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverCentroResponse;
import pe.com.pacifico.kuntur.model.AsignacionDriverCascada;
import pe.com.pacifico.kuntur.model.CuentaContable;
import pe.com.pacifico.kuntur.model.MovDriverCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <b>Class</b>: AsignacionDriverCascadaController <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 30, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://distribuciongastosdev.pacificotest.com.pe"})
@RequestMapping("/params/cascade-drivers-assignments")
@Slf4j
@RequiredArgsConstructor
public class AsignacionDriverCascadaController {

  private final AsignacionDriverCascadaService asignacionDriverCascadaService;

  @GetMapping("{repartoTipo}/{periodo}")
  @ApiOperation(value = "AsignacionDriverCascada value", notes = "Endpoint example AsignacionDriverCascada",
      response = AsignacionDriverCascada.class, code = 200)
  public Flux<AsignacionDriverCascadaResponse> getCanales(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                          @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llegaDetalleGasto ");
    return asignacionDriverCascadaService.getAsignaciones(repartoTipo, periodo).log();
  }

  /**
   * uploadExcel.
   * @param: repartoTipo & periodo
   * @return ok
   */
  @PostMapping(value = "/upload/{repartoTipo}/{periodo}", produces = "application/stream+json;charset=UTF-8")
  @ApiOperation(value = "Leer contenido archivo excel de la hoja 1",
      notes = "Leer contenido archivo excel de la hoja 1",
      response = ResponseFileRead.class, code = 200)
  public List<String> fileRead(@RequestBody RequestFileRead requestFileRead,
                               @PathVariable("repartoTipo") @Valid int repartoTipo,
                               @PathVariable("periodo") @Valid int periodo) {
    log.info("Inicio de lectura archivo Detalle gasto");
    log.info("" + requestFileRead);

    return asignacionDriverCascadaService.fileRead(repartoTipo, requestFileRead, periodo);
  }

  @GetMapping("{repartoTipo}/{periodo}/{codDriver}")
  @ApiOperation(value = "AsignacionDriverCascada value", notes = "Endpoint example AsignacionDriverCascada",
      response = MovDriverCentro.class, code = 200)
  public Flux<MovDriverCentroResponse> getMovDriversByCodDriver(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                                @PathVariable("periodo") @Valid int periodo,
                                                                @PathVariable("codDriver") @Valid String codDriver) {
    System.out.println("llega mov drivers by codDriver ");
    return asignacionDriverCascadaService.getDetalleMovCentro(repartoTipo, periodo, codDriver).log();
  }

  /**
   * delete.
   * @param: repartoTipo, periodo & codigo
   * @return ok
   */
  @DeleteMapping("{repartoTipo}/{periodo}/{codigo}")
  @ApiOperation(value = "CuentaContable value", notes = "Endpoint example cuenta", response = CuentaContable.class, code = 200)
  public boolean deleteCuenta(@PathVariable("repartoTipo") @Valid int repartoTipo,
                              @PathVariable("periodo") @Valid int periodo,
                              @PathVariable("codigo") @Valid String codigo) {

    boolean isRemoved = asignacionDriverCascadaService.deleteAsignacion(repartoTipo, periodo, codigo);
    System.out.println("deleteTest: " + codigo + " " + isRemoved);
    return isRemoved;
  }

  /**
   * save.
   * @param: asignacion
   * @return ok
   */
  @PostMapping
  @ApiOperation(value = "AsignacionDriverCascada value", notes = "Endpoint example canales",
      response = AsignacionDriverCascada.class, code = 200)
  public Mono<AsignacionDriverCascadaResponse> registerCuenta(
      @RequestBody @Valid AsignacionDriverCascadaRequest request) {
    System.out.println("******* Controller AsignacionDriverCascadaRequest");
    System.out.println(request.toString());
    return asignacionDriverCascadaService.registerDriverCascada(request).flatMap(this::buildResponse);
  }

  /**
   * This method converts a AsignacionDriverCascada into a response.
   * @param asignacionDriverCascada This is the first parameter to method.
   * @return one response
   */
  public Mono<AsignacionDriverCascadaResponse> buildResponse(AsignacionDriverCascada asignacionDriverCascada) {
    AsignacionDriverCascadaResponse asignacionDriverCascadaResponse = new AsignacionDriverCascadaResponse();
    try {
      asignacionDriverCascadaResponse.setCodDriver(asignacionDriverCascada.getCodDriver());
      asignacionDriverCascadaResponse.setNombreDriver(asignacionDriverCascada.getNombreDriver());
      asignacionDriverCascadaResponse.setFechaCreacion(asignacionDriverCascada.getFechaCreacion());
      asignacionDriverCascadaResponse.setFechaActualizacion(asignacionDriverCascada.getFechaActualizacion());
      asignacionDriverCascadaResponse.setRepartoTipo(asignacionDriverCascada.getRepartoTipo());
      asignacionDriverCascadaResponse.setNombreCentro(asignacionDriverCascada.getNombreCentro());
      asignacionDriverCascadaResponse.setPeriodo(asignacionDriverCascada.getPeriodo());
      asignacionDriverCascadaResponse.setCodCentro(asignacionDriverCascada.getCodCentro());
    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(asignacionDriverCascadaResponse).log();
  }
}
