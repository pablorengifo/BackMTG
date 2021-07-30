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
import pe.com.pacifico.kuntur.business.AsignacionDriverObjetoService;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverObjetoRequest;
import pe.com.pacifico.kuntur.expose.response.AsignacionDriverObjetoResponse;
import pe.com.pacifico.kuntur.model.AsignacionDriverObjeto;
import pe.com.pacifico.kuntur.model.MovCuentaContable;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: AsignacionDriverObjetoController <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 04, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://distribuciongastosdev.pacificotest.com.pe"})
@RequestMapping("/params/object-drivers-assignments")
@Slf4j
@RequiredArgsConstructor
public class AsignacionDriverObjetoController {
  private final AsignacionDriverObjetoService asignacionDriverObjetoService;

  /**
   * This method is used to get all DetalleGasto.
   *
   * @return all canales.
   */
  @GetMapping("{repartoTipo}/{periodo}")
  @ApiOperation(value = "AsignacionDriverObjeto value", notes = "Endpoint example AsignacionDriverObjeto",
      response = AsignacionDriverObjeto.class, code = 200)
  public Flux<AsignacionDriverObjetoResponse> getAsignaciones(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                              @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llegaAsignacionDriverObjeto ");
    return asignacionDriverObjetoService.getAsignaciones(repartoTipo, periodo).log();
  }

  /**
   * registerAsignacion.
   *
   * @param request .
   * @return int.
   */
  @PostMapping
  @ApiOperation(value = "AsignacionDriverObjeto value", notes = "Endpoint example subcanales", response = int.class, code = 200)
  public Mono<AsignacionDriverObjetoResponse> registerAsignacion(
      @RequestBody @Valid AsignacionDriverObjetoRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return asignacionDriverObjetoService.registerAsignacion(request).flatMap(this::buildResponse);
  }

  /**
   * This method converts a AsignacionDriverObjeto into a response.
   * @param asignacionDriverObjeto This is the first parameter to method.
   * @return one response
   */
  public Mono<AsignacionDriverObjetoResponse> buildResponse(AsignacionDriverObjeto asignacionDriverObjeto) {
    AsignacionDriverObjetoResponse asignacionDriverObjetoResponse = new AsignacionDriverObjetoResponse();
    try {
      asignacionDriverObjetoResponse.setCodCentro(asignacionDriverObjeto.getCodCentro());
      asignacionDriverObjetoResponse.setNombreCentro(asignacionDriverObjeto.getNombreCentro());
      asignacionDriverObjetoResponse.setRepartoTipo(asignacionDriverObjeto.getRepartoTipo());
      asignacionDriverObjetoResponse.setPeriodo(asignacionDriverObjeto.getPeriodo());
      asignacionDriverObjetoResponse.setGrupoGasto(asignacionDriverObjeto.getGrupoGasto());
      asignacionDriverObjetoResponse.setCodDriver(asignacionDriverObjeto.getCodDriver());
      asignacionDriverObjetoResponse.setNombreDriver(asignacionDriverObjeto.getNombreDriver());
      asignacionDriverObjetoResponse.setFechaCreacion(asignacionDriverObjeto.getFechaCreacion());
      asignacionDriverObjetoResponse.setFechaActualizacion(asignacionDriverObjeto.getFechaActualizacion());
    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(asignacionDriverObjetoResponse).log();
  }

  /**
   * deleteAsignacion.
   * @return a.
   * @param: request.
   */
  @DeleteMapping("{repartoTipo}/{periodo}/{codigo}/{grupoGasto}")
  @ApiOperation(value = "MovCuentaContable value", notes = "Endpoint example cuenta", response = MovCuentaContable.class, code = 200)
  public Boolean deleteAsignacion(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                  @PathVariable("periodo") @Valid int periodo,
                                  @PathVariable("codigo") @Valid String codigo,
                                  @PathVariable("grupoGasto") @Valid String grupoGasto) {
    boolean isRemoved = asignacionDriverObjetoService.deleteAsignacion(repartoTipo, periodo, codigo, grupoGasto);
    /*if (!isRemoved) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }*/
    System.out.println("deleteTest: " + periodo + " " + codigo + " " + isRemoved);
    return isRemoved;
  }

  /**
   * uploadExcel.
   *
   * @return ok
   * @param: repartoTipo & periodo
   */
  @PostMapping(value = "/upload/{repartoTipo}/{periodo}", produces = "application/stream+json;charset=UTF-8")
  @ApiOperation(value = "Leer contenido archivo excel de la hoja 1",
      notes = "Leer contenido archivo excel de la hoja 1",
      response = ResponseFileRead.class, code = 200)
  public List<String> fileRead(@RequestBody RequestFileRead requestFileRead,
                               @PathVariable("repartoTipo") @Valid int repartoTipo,
                               @PathVariable("periodo") @Valid int periodo) {
    log.info("Inicio de lectura archivo Asignacion Driver Objeto");
    log.info("" + requestFileRead);

    return asignacionDriverObjetoService.fileRead(repartoTipo, requestFileRead, periodo);
  }

  /**
   * This method is used to get all centroResponse.
   * @return all centros.
   */
  /*
  @GetMapping("{repartoTipo}/{periodo}/{codDriverCentro}")
  @ApiOperation(value = "MovDriverCentro value", notes = "Endpoint example Driver", response = MovCentro.class, code = 200)
  public Flux<MovDriverCentroResponse> getMovDriversByMaDriver(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                               @PathVariable("periodo") @Valid int periodo,
                                                               @PathVariable("codDriverCentro") @Valid String codDriverCentro) {
    System.out.println("llegaDriverCentro ");
    return asignacionCecoBolsaService.getMovDriversCentro(repartoTipo , periodo,codDriverCentro).log();
  }*/
}
