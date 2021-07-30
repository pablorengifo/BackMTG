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
import pe.com.pacifico.kuntur.business.MovCuentaContableService;
import pe.com.pacifico.kuntur.expose.request.MovCuentaContableRequest;
import pe.com.pacifico.kuntur.expose.response.MovCuentaContableResponse;
import pe.com.pacifico.kuntur.model.MovCuentaContable;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MovCuentaContableController <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     March 31, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://distribuciongastosdev.pacificotest.com.pe"})
@RequestMapping("/params/accounts-period")
@Slf4j
@RequiredArgsConstructor
public class MovCuentaContableController {

  private final MovCuentaContableService movCuentaContableService;

  /**
   * This method is used to get all cuentaContableResponse.
   * @return all cuentas.
   */
  @GetMapping("{repartoTipo}/{periodo}")
  @ApiOperation(value = "MovCuentaContable value", notes = "Endpoint example cuentas", response = MovCuentaContable.class, code = 200)
  public Flux<MovCuentaContableResponse> getCuentas(@PathVariable("repartoTipo") @Valid int repartoTipo,
      @PathVariable("periodo") @Valid int periodo
  ) {
    System.out.println("llegaCuentaPeriodo ");
    return movCuentaContableService.getCuentas(repartoTipo, periodo).log();

  }

  /**
   * This method is used to get only one parametriaToBeObfuscated.
   * @param request This is the first parameter to method.
   * @return one parametria.
   */
  @PostMapping
  @ApiOperation(value = "MovCuentaContable value", notes = "Endpoint example cuentas", response = MovCuentaContable.class, code = 200)
  public Mono<MovCuentaContableResponse> registerCuenta(
      @RequestBody @Valid MovCuentaContableRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return  movCuentaContableService.registerCuenta(request).flatMap(this::buildResponse);
  }

  /**
   * deleteParametria.
   * @param: request
   * @return
   */
  @DeleteMapping("{repartoTipo}/{periodo}/{codigo}")
  @ApiOperation(value = "MovCuentaContable value", notes = "Endpoint example cuenta", response = MovCuentaContable.class, code = 200)
  public Boolean deleteCuenta(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                      @PathVariable("periodo") @Valid int periodo,
                                                      @PathVariable("codigo") @Valid String codigo)
  {
    boolean isRemoved = movCuentaContableService.deleteCuenta(repartoTipo, periodo, codigo);
    /*if (!isRemoved) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }*/
    System.out.println("deleteTest: " + periodo + " " + codigo + " " + isRemoved);
    return isRemoved;
    //return new Mono<ParametriaResponse>(id, HttpStatus.OK);
  }

  /**
   * uploadExcel.
   * @param: request
   * @return
   */
  @PostMapping(value = "/upload/{repartoTipo}/{periodo}", produces = "application/stream+json;charset=UTF-8")
  @ApiOperation(value = "Leer contenido archivo excel de la hoja 1",
      notes = "Leer contenido archivo excel de la hoja 1",
      response = ResponseFileRead.class, code = 200)
  public List<String> fileRead(@RequestBody RequestFileRead requestFileRead,
                               @PathVariable("repartoTipo") @Valid int repartoTipo,
                               @PathVariable("periodo") @Valid int periodo) {
    log.info("Inicio de lectura archivo");
    log.info("" + requestFileRead);

    return movCuentaContableService.fileRead(repartoTipo, requestFileRead, periodo);
  }

  /**
   * This method converts a MovCuentaContable into a response.
   * @param cuenta This is the first parameter to method.
   * @return one response
   */
  public Mono<MovCuentaContableResponse> buildResponse(MovCuentaContable cuenta) {
    MovCuentaContableResponse cuentaContableResponse = new MovCuentaContableResponse();
    try {
      cuentaContableResponse.setCodCuentaContable(cuenta.getCodCuentaContable());
      cuentaContableResponse.setFechaCreacion(cuenta.getFechaCreacion());
      cuentaContableResponse.setFechaActualizacion(cuenta.getFechaActualizacion());
      cuentaContableResponse.setPeriodo(cuenta.getPeriodo());
      cuentaContableResponse.setRepartoTipo(cuenta.getRepartoTipo());
      cuentaContableResponse.setSaldo(cuenta.getSaldo());
    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(cuentaContableResponse).log();
  }
}
