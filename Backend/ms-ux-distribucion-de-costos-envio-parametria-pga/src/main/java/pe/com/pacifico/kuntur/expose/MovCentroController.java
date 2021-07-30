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
import pe.com.pacifico.kuntur.business.MovCentroService;
import pe.com.pacifico.kuntur.expose.request.MovCentroRequest;
import pe.com.pacifico.kuntur.expose.response.MovCentroResponse;
import pe.com.pacifico.kuntur.model.MovCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MovCentroController <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 12, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://distribuciongastosdev.pacificotest.com.pe"})
@RequestMapping("/params/cost-centers-period")
@Slf4j
@RequiredArgsConstructor
public class MovCentroController {

  private final MovCentroService movCentroService;

  /**
   * This method is used to get all centroResponse.
   * @return all centros.
   */
  @GetMapping("{repartoTipo}/{periodo}")
  @ApiOperation(value = "MovCentro value", notes = "Endpoint example centros", response = MovCentro.class, code = 200)
  public Flux<MovCentroResponse> getCentros(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                            @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llegaCentroPeriodo ");
    return movCentroService.getCentros(repartoTipo , periodo).log();

  }

  /**
   * This method is used to get only one centroToBeObfuscated.
   * @param request This is the first parameter to method.
   * @return one centro.
   */
  @PostMapping
  @ApiOperation(value = "MovCentro value", notes = "Endpoint example centros", response = int.class, code = 200)
  public int registerCentro(
      @RequestBody @Valid MovCentroRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return movCentroService.registerCentro(request);
  }

  /**
   * updateCentro.
   * @param: request
   * @return
   */
  @PutMapping
  @ApiOperation(value = "MovCentro value", notes = "Endpoint example centro", response = MovCentro.class, code = 200)
  public Mono<MovCentroResponse> updateCentro(
      @RequestBody @Valid MovCentroRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return  movCentroService.updateCentro(request).flatMap(this::buildResponse);
  }

  /**
   * deleteCentro.
   * @param: request
   * @return
   */
  @DeleteMapping("{repartoTipo}/{periodo}/{codigo}")
  @ApiOperation(value = "MovCentro value", notes = "Endpoint example centro", response = MovCentro.class, code = 200)
  public boolean deleteCentro(
      @PathVariable("repartoTipo") @Valid int repartoTipo,
      @PathVariable("periodo") @Valid int periodo,
      @PathVariable("codigo") @Valid String codigo
  ) {

    boolean isRemoved = movCentroService.deleteCentro(repartoTipo, periodo,codigo);
    /*if (!isRemoved) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }*/
    System.out.println("deleteTest: " + periodo + " " + codigo + " " + isRemoved);
    return isRemoved;
    //return new Mono<CentroResponse>(id, HttpStatus.OK);
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

    return movCentroService.fileRead(repartoTipo, requestFileRead, periodo);
  }

  /**
   * This method converts a MovCentro into a response.
   * @param centro This is the first parameter to method.
   * @return one response
   */
  public Mono<MovCentroResponse> buildResponse(MovCentro centro) {
    MovCentroResponse centroResponse = new MovCentroResponse();
    try {
      centroResponse.setCodCentro(centro.getCodCentro());
      centroResponse.setNombre(centro.getNombre());
      centroResponse.setTipo(centro.getTipo());
      centroResponse.setCodCentroOrigen(centro.getCodCentroOrigen());
      centroResponse.setCodCuentaContableOrigen(centro.getCodCuentaContableOrigen());
      centroResponse.setCodEntidadOrigen(centro.getCodEntidadOrigen());
      centroResponse.setCodPartidaOrigen(centro.getCodPartidaOrigen());
      centroResponse.setFechaCreacion(centro.getFechaCreacion());
      centroResponse.setFechaActualizacion(centro.getFechaActualizacion());
      centroResponse.setGrupoGasto(centro.getGrupoGasto());
      centroResponse.setIteracion(centro.getIteracion());
      centroResponse.setPeriodo(centro.getPeriodo());
      centroResponse.setRepartoTipo(centro.getRepartoTipo());
      centroResponse.setSaldo(centro.getSaldo());
    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(centroResponse).log();
  }
}
