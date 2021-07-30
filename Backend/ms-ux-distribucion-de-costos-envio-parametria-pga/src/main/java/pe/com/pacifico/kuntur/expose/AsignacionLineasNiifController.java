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
import pe.com.pacifico.kuntur.business.AsignacionLineaNiifService;
import pe.com.pacifico.kuntur.expose.request.AsignacionLineaNiifRequest;
import pe.com.pacifico.kuntur.expose.response.AsignacionLineaNiifResponse;
import pe.com.pacifico.kuntur.model.AsignacionLineaNiif;
import pe.com.pacifico.kuntur.model.CuentaContable;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;

/**
 * <b>Class</b>: AsignacionLineaNiifController <br/>
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
@RequestMapping("/params/niif-drivers-assignments")
@Slf4j
@RequiredArgsConstructor
public class AsignacionLineasNiifController {
  private final AsignacionLineaNiifService asignacionLineaNiifService;

  @GetMapping("{repartoTipo}/{periodo}")
  @ApiOperation(value = "AsignacionLineaNiif value", notes = "Endpoint example AsignacionLineaNiif",
      response = AsignacionLineaNiif.class, code = 200)
  public Flux<AsignacionLineaNiifResponse> getAsignaciones(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                      @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llegaDetalleGasto ");
    return asignacionLineaNiifService.getAsignaciones(repartoTipo, periodo).log();
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
    log.info("Inicio de lectura archivo Linea Niif");
    log.info("" + requestFileRead);

    return asignacionLineaNiifService.fileRead(repartoTipo, requestFileRead, periodo);
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

    boolean isRemoved = asignacionLineaNiifService.deleteAsignacion(repartoTipo, periodo, codigo);
    System.out.println("Linea: " + codigo + " " + isRemoved);
    return isRemoved;
  }

  /**
   * save.
   * @param: asignacion
   * @return ok
   */
  @PostMapping
  @ApiOperation(value = "AsignacionLineaNiif value", notes = "Endpoint example canales",
      response = AsignacionLineaNiif.class, code = 200)
  public boolean registerCuenta(
      @RequestBody @Valid AsignacionLineaNiifRequest request) {
    System.out.println("******* Controller AsignacionLineaNiifRequest");
    System.out.println(request.toString());
    return asignacionLineaNiifService.addAsignacionNiif(request);
  }

}
