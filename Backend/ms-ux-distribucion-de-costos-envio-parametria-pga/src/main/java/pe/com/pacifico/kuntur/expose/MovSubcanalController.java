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
import pe.com.pacifico.kuntur.business.MovSubcanalService;
import pe.com.pacifico.kuntur.expose.request.MovSubcanalRequest;
import pe.com.pacifico.kuntur.expose.response.MovSubcanalResponse;
import pe.com.pacifico.kuntur.model.MovSubcanal;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;




/**
 * <b>Class</b>: MovSubcanalController <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 16, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://distribuciongastosdev.pacificotest.com.pe"})
@RequestMapping("/params/subchannels-period")
@Slf4j
@RequiredArgsConstructor
public class MovSubcanalController {

  private final MovSubcanalService movSubcanalService;

  /**
   * This method is used to get all subcanalResponse.
   * @return all subcanales.
   */
  @GetMapping("{repartoTipo}/{periodo}")
  @ApiOperation(value = "MovSubcanal value", notes = "Endpoint example subcanales", response = MovSubcanal.class, code = 200)
  public Flux<MovSubcanalResponse> getSubcanales(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                              @PathVariable("periodo") @Valid int periodo)
  {
    System.out.println("llegaSubcanalPeriodo ");
    return movSubcanalService.getSubcanales(repartoTipo, periodo).log();

  }

  /**
   * This method is used to get only one subcanalToBeObfuscated.
   * @param request This is the first parameter to method.
   * @return one subcanal.
   */
  @PostMapping
  @ApiOperation(value = "MovSubcanal value", notes = "Endpoint example subcanales", response = int.class, code = 200)
  public int registerSubcanal(
      @RequestBody @Valid MovSubcanalRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return movSubcanalService.registerSubcanal(request);
  }

  /**
   * deleteSubcanal.
   * @param: request
   * @return
   */
  @DeleteMapping("{repartoTipo}/{periodo}/{codigo}")
  @ApiOperation(value = "MovSubcanal value", notes = "Endpoint example subcanal", response = MovSubcanal.class, code = 200)
  public boolean deleteSubcanal(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                @PathVariable("periodo") @Valid int periodo,
                                                @PathVariable("codigo") @Valid String codigo)
  {
    boolean isRemoved = movSubcanalService.deleteSubcanal(repartoTipo, periodo, codigo);
    /*if (!isRemoved) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }*/
    System.out.println("deleteTest: " + periodo + " " + codigo + " " + isRemoved);
    return isRemoved;
    //return new Mono<SubcanalResponse>(id, HttpStatus.OK);
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

    return movSubcanalService.fileRead(repartoTipo, requestFileRead, periodo);
  }

  /**
   * This method converts a MovSubcanal into a response.
   * @param subcanal This is the first parameter to method.
   * @return one response
   */
  public Mono<MovSubcanalResponse> buildResponse(MovSubcanal subcanal) {
    MovSubcanalResponse subcanalResponse = new MovSubcanalResponse();
    try {
      subcanalResponse.setCodSubcanal(subcanal.getCodSubcanal());
      subcanalResponse.setNombre(subcanal.getNombre());
      subcanalResponse.setPeriodo(subcanal.getPeriodo());
      subcanalResponse.setRepartoTipo(subcanal.getRepartoTipo());
      subcanalResponse.setFechaCreacion(subcanal.getFechaCreacion());
      subcanalResponse.setFechaActualizacion(subcanal.getFechaActualizacion());
      subcanalResponse.setCodCanal(subcanal.getCodCanal());
      subcanalResponse.setNombreCanal(subcanal.getNombreCanal());
    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(subcanalResponse).log();
  }
}
