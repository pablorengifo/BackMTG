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
import pe.com.pacifico.kuntur.business.MaCanalService;
import pe.com.pacifico.kuntur.expose.request.MaCanalRequest;
import pe.com.pacifico.kuntur.expose.response.MaCanalResponse;
import pe.com.pacifico.kuntur.model.MaCanal;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <b>Class</b>: MaCanalController <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 16, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://distribuciongastosdev.pacificotest.com.pe"})
@RequestMapping("/params/channels")
@Slf4j
@RequiredArgsConstructor
public class MaCanalController {

  private final MaCanalService maCanalService;

  /**
   * This method is used to get all canalResponse.
   * @return all canales.
   */
  @GetMapping("{repartoTipo}")
  @ApiOperation(value = "MaCanal value", notes = "Endpoint example canales", response = MaCanal.class, code = 200)
  public Flux<MaCanalResponse> getCanales(@PathVariable("repartoTipo") @Valid int repartoTipo) {
    System.out.println("llegaCanal ");
    return maCanalService.getCanales(repartoTipo).log();
  }

  /**
   * This method is used to get only one canalResponse.
   * @param codigo This is the first parameter to method.
   * @return one canal.
   */
  /*
  @GetMapping("{repartoTipo}/{codigo}")
  @ApiOperation(value = "MaCanal value", notes = "Endpoint example canales", response = MaCanal.class, code = 200)
  public Mono<MaCanalResponse> getCanalByCodCanal(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                        @PathVariable("codigo") @Valid String codigo) {
    return maCanalService.getCanal(repartoTipo, codigo);
  }
*/
  /**
   * This method is used to get only one canalToBeObfuscated.
   * @param request This is the first parameter to method.
   * @return one canal.
   */
  @PostMapping
  @ApiOperation(value = "MaCanal value", notes = "Endpoint example canales", response = MaCanal.class, code = 200)
  public Mono<MaCanalResponse> registerCuenta(
      @RequestBody @Valid MaCanalRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return maCanalService.registerCanal(request).flatMap(this::buildResponse);
  }

  /**
   * updateCanal.
   * @param: request
   * @return
   */
  @PutMapping
  @ApiOperation(value = "MaCanal value", notes = "Endpoint example canal", response = MaCanal.class, code = 200)
  public Mono<MaCanalResponse> updateCanal(
      @RequestBody @Valid MaCanalRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return maCanalService.updateCanal(request).flatMap(this::buildResponse);
  }

  /**
   * deleteParametria.
   * @param: request
   * @return
   */
  @DeleteMapping("{repartoTipo}/{codigo}")
  @ApiOperation(value = "MaCanal value", notes = "Endpoint example canal", response = MaCanal.class, code = 200)
  public boolean deleteCuenta(@PathVariable("repartoTipo") @Valid int repartoTipo,
                              @PathVariable("codigo") @Valid String codigo)
  {
    boolean isRemoved = maCanalService.deleteCanal(repartoTipo, codigo);
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

    return maCanalService.fileRead(repartoTipo, requestFileRead);
  }

  private Mono<MaCanalResponse> buildResponse(MaCanal canal) {
    MaCanalResponse maCanalResponse = new MaCanalResponse();
    try {
      maCanalResponse.setCodCanal(canal.getCodCanal());
      maCanalResponse.setRepartoTipo(canal.getRepartoTipo());
      maCanalResponse.setNombre(canal.getNombre());
      maCanalResponse.setEstaActivo(canal.isEstaActivo());
      maCanalResponse.setFechaCreacion(canal.getFechaCreacion());
      maCanalResponse.setFechaActualizacion(canal.getFechaActualizacion());
    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(maCanalResponse).log();
  }
}
