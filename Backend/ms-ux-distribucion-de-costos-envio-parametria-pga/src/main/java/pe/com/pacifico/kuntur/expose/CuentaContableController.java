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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.pacifico.kuntur.business.CuentaContableService;
import pe.com.pacifico.kuntur.expose.request.CuentaContableRequest;
import pe.com.pacifico.kuntur.expose.response.CuentaContableResponse;
import pe.com.pacifico.kuntur.model.CuentaContable;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;




/**
 * <b>Class</b>: CuentaContableController <br/>
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
@RequestMapping("/params/accounts")
@Slf4j
@RequiredArgsConstructor
public class CuentaContableController {

  private final CuentaContableService cuentaContableService;

  /*
  @GetMapping(value = "/stream/{idcontrolador}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  @ApiOperation(value = "Integer Numbers example", notes = "Ejemplo Endpoint", response = Flux.class, code = 200)
  public Flux<Integer> streamInteger(@PathVariable("idcontrolador") @Valid Integer controllerId) {
    log.info("id - controlador : {}", controllerId);
    return Flux.just(1,2,3,4);
  }
  */

  /**
   * This method is used to get all cuentaContableResponse.
   * @return all cuentas.
   */
  @GetMapping("{repartoTipo}")
  @ApiOperation(value = "CuentaContable value", notes = "Endpoint example cuentas", response = CuentaContable.class, code = 200)
  public Flux<CuentaContableResponse> getCuentas(@PathVariable("repartoTipo") @Valid int repartoTipo) {
    System.out.println("llegaCuenta ");
    return cuentaContableService.getCuentas(repartoTipo).log();
  }

  /**
   * This method is used to get all cuentaContableResponse not in Mov.
   * @return all cuentas not in Mov.
   */
  @GetMapping("period/{repartoTipo}/{periodo}")
  @ApiOperation(value = "CuentaContable value", notes = "Endpoint example cuentas", response = CuentaContable.class, code = 200)
  public Flux<CuentaContableResponse> getCuentasNotInMov(@PathVariable("repartoTipo") @Valid int repartoTipo,
      @PathVariable("periodo") @Valid int periodo)
  {
    System.out.println("llegaCuentaNotInMov ");
    return cuentaContableService.getCuentasNotInMov(repartoTipo, periodo).log();
  }

  /**
   * This method is used to get only one parametriaResponse.
   * @param codigo This is the first parameter to method.
   * @return one cuenta.
   */
  @GetMapping("{repartoTipo}/{codigo}")
  @ApiOperation(value = "CuentaContable value", notes = "Endpoint example cuentas", response = CuentaContable.class, code = 200)
  public Mono<CuentaContableResponse> getCuentaContableByCodCuentaContable(@PathVariable("repartoTipo") @Valid int repartoTipo,
      @PathVariable("codigo") @Valid String codigo) {
    return cuentaContableService.getCuenta(repartoTipo, codigo);
  }

  /**
   * This method is used to get only one parametriaToBeObfuscated.
   * @param request This is the first parameter to method.
   * @return one parametria.
   */
  @PostMapping
  @ApiOperation(value = "CuentaContable value", notes = "Endpoint example cuentas", response = CuentaContable.class, code = 200)
  public Mono<CuentaContableResponse> registerCuenta(
      @RequestBody @Valid CuentaContableRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return  cuentaContableService.registerCuenta(request).flatMap(this::buildResponse);
  }

  /**
   * updateParametria.
   * @param: request
   * @return
   */
  @PutMapping
  @ApiOperation(value = "CuentaContable value", notes = "Endpoint example cuenta", response = CuentaContable.class, code = 200)
  public Mono<CuentaContableResponse> updateParametria(
      @RequestBody @Valid CuentaContableRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return  cuentaContableService.updateCuenta(request).flatMap(this::buildResponse);
  }

  /**
   * deleteParametria.
   * @param: request
   * @return
   */
  @DeleteMapping("{repartoTipo}/{codigo}")
  @ApiOperation(value = "CuentaContable value", notes = "Endpoint example cuenta", response = CuentaContable.class, code = 200)
  public boolean deleteCuenta(@PathVariable("repartoTipo") @Valid int repartoTipo,
                              @PathVariable("codigo") @Valid String codigo) {

    boolean isRemoved = cuentaContableService.deleteCuenta(repartoTipo, codigo);
    /*if (!isRemoved) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }*/
    System.out.println("deleteTest: " + codigo + " " + isRemoved);
    return isRemoved;
    //return new Mono<ParametriaResponse>(id, HttpStatus.OK);
  }

  /**
   * uploadExcel.
   * @param: request
   * @return
   */
  @PostMapping(value = "/upload/{repartoTipo}", produces = "application/stream+json;charset=UTF-8")
  @ApiOperation(value = "Leer contenido archivo excel de la hoja 1",
      notes = "Leer contenido archivo excel de la hoja 1",
      response = ResponseFileRead.class, code = 200)
  public List<String> fileRead(@PathVariable("repartoTipo") @Valid int repartoTipo,
                       @RequestBody RequestFileRead requestFileRead) {
    log.info("Inicio de lectura archivo");
    log.info("" + requestFileRead);

    return cuentaContableService.fileRead(repartoTipo, requestFileRead);
  }
  //Aqui empieza lo niif

  /**
   * This method is used to get all cuentaContableResponse.
   * @return all cuentas.
   */
  @GetMapping("niif/{repartoTipo}")
  @ApiOperation(value = "CuentaContable value", notes = "Endpoint example cuentas", response = CuentaContable.class, code = 200)
  public Flux<CuentaContableResponse> getCuentasNiif(@PathVariable("repartoTipo") @Valid int repartoTipo) {
    System.out.println("llegaCuenta niif ");
    return cuentaContableService.getCuentasNiif(repartoTipo).log();
  }

  /**
   * This method is used to get all cuentaContableResponse.
   * @return all cuentas.
   */
  @GetMapping("not-niif/{repartoTipo}")
  @ApiOperation(value = "CuentaContable value", notes = "Endpoint example cuentas", response = CuentaContable.class, code = 200)
  public Flux<CuentaContableResponse> getCuentasNoNiif(@PathVariable("repartoTipo") @Valid int repartoTipo) {
    System.out.println("llegaCuenta no niif ");
    return cuentaContableService.getCuentasNotNiif(repartoTipo).log();
  }

  /**
   * This method is used to get all cuentaContableResponse.
   * @return all cuentas.
   */
  @PostMapping("niif")
  @ApiOperation(value = "CuentaContable value", notes = "Endpoint example cuentas", response = CuentaContable.class, code = 200)
  public Mono<CuentaContable> agregarCuentaNiif(@RequestBody @Valid CuentaContableRequest request) {
    System.out.println("llegaCuenta agregar niif");
    return cuentaContableService.agregarCuentaNiif(request).log();
  }
  /**
   * deleteParametria.
   * @param: request
   * @return
   */

  @DeleteMapping("niif/{repartoTipo}/{codigo}")
  @ApiOperation(value = "CuentaContable value", notes = "Endpoint example cuenta", response = CuentaContable.class, code = 200)
  public boolean removeCuentaNiif(@PathVariable("repartoTipo") @Valid int repartoTipo,
                              @PathVariable("codigo") @Valid String codigo) {
    System.out.println("llegaCuenta quitar niif");
    boolean isRemoved = cuentaContableService.removeCuentaNiif(repartoTipo, codigo);
    System.out.println("deleteTest: " + codigo + " " + isRemoved);
    return isRemoved;
  }

  /**
   * uploadExcel.
   * @param: request
   * @return
   */
  @PostMapping(value = "niif/upload/{repartoTipo}", produces = "application/stream+json;charset=UTF-8")
  @ApiOperation(value = "Leer contenido archivo excel de la hoja 1",
      notes = "Leer contenido archivo excel de la hoja 1",
      response = ResponseFileRead.class, code = 200)
  public List<String> fileReadNiif(@PathVariable("repartoTipo") @Valid int repartoTipo,
                               @RequestBody RequestFileRead requestFileRead) {
    log.info("Inicio de lectura archivo");
    log.info("" + requestFileRead);

    return cuentaContableService.fileReadNiif(repartoTipo, requestFileRead);
  }

  private Mono<CuentaContableResponse> buildResponse(CuentaContable cuenta) {
    CuentaContableResponse cuentaContableResponse = new CuentaContableResponse();
    try {
      cuentaContableResponse.setCodCuentaContable(cuenta.getCodCuentaContable());
      cuentaContableResponse.setNombre(cuenta.getNombre());
      cuentaContableResponse.setEstaActivo(cuenta.isEstaActivo());
      cuentaContableResponse.setFechaCreacion(cuenta.getFechaCreacion());
      cuentaContableResponse.setFechaActualizacion(cuenta.getFechaActualizacion());
      cuentaContableResponse.setNiif17Atribuible(cuenta.getNiif17Atribuible());
      cuentaContableResponse.setNiif17Clase(cuenta.getNiif17Clase());
      cuentaContableResponse.setNiif17Tipo(cuenta.getNiif17Tipo());
      cuentaContableResponse.setRepartoTipo(cuenta.getRepartoTipo());
      cuentaContableResponse.setTipoGasto(cuenta.isTipoGasto());
    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(cuentaContableResponse).log();
  }
}
