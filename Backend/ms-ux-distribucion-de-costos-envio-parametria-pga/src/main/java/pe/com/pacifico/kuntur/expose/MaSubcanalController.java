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
import pe.com.pacifico.kuntur.business.MaSubcanalService;
import pe.com.pacifico.kuntur.expose.request.MaSubcanalRequest;
import pe.com.pacifico.kuntur.expose.response.MaSubcanalResponse;
import pe.com.pacifico.kuntur.model.MaSubcanal;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MaSubcanalController <br/>
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
@RequestMapping("/params/subchannels")
@Slf4j
@RequiredArgsConstructor
public class MaSubcanalController {

  private final MaSubcanalService maSubcanalService;

  /**
   * This method is used to get all subcanalResponse.
   * @return all subcanales.
   */
  @GetMapping("{repartoTipo}")
  @ApiOperation(value = "MaSubcanal value", notes = "Endpoint example subcanales", response = MaSubcanal.class, code = 200)
  public Flux<MaSubcanalResponse> getSubcanales(@PathVariable("repartoTipo") @Valid int repartoTipo) {
    System.out.println("llegaSubcanal ");
    return maSubcanalService.getSubcanales(repartoTipo).log();
  }

  /**
   * This method is used to get all subcanalResponse not in Mov.
   * @return all subcanales not in Mov.
   */
  @GetMapping("period/{repartoTipo}/{periodo}")
  @ApiOperation(value = "MaSubcanal value", notes = "Endpoint example subcanales", response = MaSubcanal.class, code = 200)
  public Flux<MaSubcanalResponse> getSubcanalesNotInMov(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                     @PathVariable("periodo") @Valid int periodo)
  {
    System.out.println("llegaMaSubcanalNotInMov ");
    return maSubcanalService.getSubcanalesNotInMov(repartoTipo, periodo).log();
  }

  /**
   * This method is used to get only one subcanalResponse.
   * @param codigo This is the first parameter to method.
   * @return one subcanal.
   */
  @GetMapping("{repartoTipo}/{codigo}")
  @ApiOperation(value = "MaSubcanal value", notes = "Endpoint example subcanales", response = MaSubcanal.class, code = 200)
  public Mono<MaSubcanalResponse> getSubcanalByCodSubcanal(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                        @PathVariable("codigo") @Valid String codigo) {
    return maSubcanalService.getSubcanal(repartoTipo, codigo);
  }

  /**
   * This method is used to get only one subcanalToBeObfuscated.
   * @param request This is the first parameter to method.
   * @return one subcanal.
   */
  @PostMapping
  @ApiOperation(value = "MaSubcanal value", notes = "Endpoint example subcanales", response = MaSubcanal.class, code = 200)
  public Mono<MaSubcanalResponse> registerSubcanal(
      @RequestBody @Valid MaSubcanalRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return maSubcanalService.registerSubcanal(request).flatMap(this::buildResponse);
  }

  /**
   * updateSubcanal.
   * @param: request
   * @return
   */
  @PutMapping
  @ApiOperation(value = "MaSubcanal value", notes = "Endpoint example subcanal", response = MaSubcanal.class, code = 200)
  public Mono<MaSubcanalResponse> updateSubcanal(
      @RequestBody @Valid MaSubcanalRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return maSubcanalService.updateSubcanal(request).flatMap(this::buildResponse);
  }

  /**
   * deleteSubcanal.
   * @param: request
   * @return
   */
  @DeleteMapping("{repartoTipo}/{codigo}")
  @ApiOperation(value = "MaSubcanal value", notes = "Endpoint example subcanal", response = MaSubcanal.class, code = 200)
  public boolean deleteSubcanal(@PathVariable("repartoTipo") @Valid int repartoTipo,
                              @PathVariable("codigo") @Valid String codigo)
  {
    boolean isRemoved = maSubcanalService.deleteSubcanal(repartoTipo, codigo);
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

    return maSubcanalService.fileRead(repartoTipo, requestFileRead);
  }

  private Mono<MaSubcanalResponse> buildResponse(MaSubcanal subcanal) {
    MaSubcanalResponse maSubcanalResponse = new MaSubcanalResponse();
    try {
      maSubcanalResponse.setCodSubcanal(subcanal.getCodSubcanal());
      maSubcanalResponse.setRepartoTipo(subcanal.getRepartoTipo());
      maSubcanalResponse.setNombre(subcanal.getNombre());
      maSubcanalResponse.setEstaActivo(subcanal.isEstaActivo());
      maSubcanalResponse.setFechaCreacion(subcanal.getFechaCreacion());
      maSubcanalResponse.setFechaActualizacion(subcanal.getFechaActualizacion());

    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(maSubcanalResponse).log();
  }
}
