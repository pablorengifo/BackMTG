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
import pe.com.pacifico.kuntur.business.MaCentroService;
import pe.com.pacifico.kuntur.expose.request.MaCentroRequest;
import pe.com.pacifico.kuntur.expose.response.MaCentroResponse;
import pe.com.pacifico.kuntur.model.MaCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * <b>Class</b>: MaCentroController <br/>
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
@RequestMapping("/params/cost-centers")
@Slf4j
@RequiredArgsConstructor
public class MaCentroController {

  private final MaCentroService maCentroService;

  /**
   * This method is used to get all centroResponse.
   * @return all centros.
   */
  @GetMapping("{repartoTipo}")
  @ApiOperation(value = "MaCentro value", notes = "Endpoint example centros", response = MaCentro.class, code = 200)
  public Flux<MaCentroResponse> getCentros(@PathVariable("repartoTipo") @Valid int repartoTipo) {
    System.out.println("llegaCentro ");
    return maCentroService.getCentros(repartoTipo).log();
  }

  /**
   * This method is used to get all centroResponse not in Mov.
   * @return all centros not in Mov.
   */
  @GetMapping("period/{repartoTipo}/{periodo}")
  @ApiOperation(value = "MaCentro value", notes = "Endpoint example centros", response = MaCentro.class, code = 200)
  public Flux<MaCentroResponse> getCentrosNotInMov(@PathVariable("periodo") @Valid int periodo,
                                                    @PathVariable("repartoTipo") @Valid int repartoTipo)
  {
    System.out.println("llegaCentroNotInMov ");
    return maCentroService.getCentrosNotInMov(repartoTipo,periodo).log();
  }

  /**
   * This method is used to get only one centroResponse.
   * @param codigo This is the first parameter to method.
   * @return one centro.
   */
  @GetMapping("{repartoTipo}/{codigo}")
  @ApiOperation(value = "MaCentro value", notes = "Endpoint example centros", response = MaCentro.class, code = 200)
  public Mono<MaCentroResponse> getCentroByCodCentro(@PathVariable("codigo") @Valid String codigo,
                                                     @PathVariable("repartoTipo") @Valid int repartoTipo) {
    return maCentroService.getCentro(repartoTipo,codigo);
  }

  /**
   * This method is used to get only one centroToBeObfuscated.
   * @param request This is the first parameter to method.
   * @return one centro.
   */
  @PostMapping
  @ApiOperation(value = "MaCentro value", notes = "Endpoint example centros", response = MaCentro.class, code = 200)
  public Mono<MaCentroResponse> registerCentro(
      @RequestBody @Valid MaCentroRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return maCentroService.registerCentro(request).flatMap(this::buildResponse);
  }

  /**
   * updateCentro.
   * @param: request
   * @return
   */
  @PutMapping
  @ApiOperation(value = "MaCentro value", notes = "Endpoint example centro", response = MaCentro.class, code = 200)
  public Mono<MaCentroResponse> updateCentro(
      @RequestBody @Valid MaCentroRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return maCentroService.updateCentro(request).flatMap(this::buildResponse);
  }

  /**
   * deleteParametria.
   * @param: request
   * @return
   */
  @DeleteMapping("{repartoTipo}/{codigo}")
  @ApiOperation(value = "MaCentro value", notes = "Endpoint example centro", response = MaCentro.class, code = 200)
  public boolean deleteCentro(@PathVariable("codigo") @Valid String codigo,
                              @PathVariable("repartoTipo") @Valid int repartoTipo) {

    boolean isRemoved = maCentroService.deleteCentro(repartoTipo,codigo);
    /* if (!isRemoved) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }*/
    System.out.println("deleteTest: " + codigo + " " + isRemoved);
    return isRemoved;
    //return new Mono<ParametriaResponse>(id, HttpStatus.OK);
  }

  private Mono<MaCentroResponse> buildResponse(MaCentro centro) {
    MaCentroResponse maCentroResponse = new MaCentroResponse();
    try {
      maCentroResponse.setCodCentro(centro.getCodCentro());
      maCentroResponse.setNombre(centro.getNombre());
      maCentroResponse.setCodCentroPadre(centro.getCodCentroPadre());
      maCentroResponse.setEstaActivo(centro.isEstaActivo());
      maCentroResponse.setFechaCreacion(centro.getFechaCreacion());
      maCentroResponse.setFechaActualizacion(centro.getFechaActualizacion());
      maCentroResponse.setGrupoCeco(centro.getGrupoCeco());
      maCentroResponse.setNiif17Atribuible(centro.getNiif17Atribuible());
      maCentroResponse.setNiif17Clase(centro.getNiif17Clase());
      maCentroResponse.setNiif17Tipo(centro.getNiif17Tipo());
      maCentroResponse.setNivel(centro.getNivel());
      maCentroResponse.setRepartoTipo(centro.getRepartoTipo());
      maCentroResponse.setTipo(centro.getTipo());
      maCentroResponse.setTipoCeco(centro.getTipoCeco());
      maCentroResponse.setTipoGasto(centro.isTipoGasto());

    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(maCentroResponse).log();
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

    return maCentroService.fileRead(repartoTipo, requestFileRead);
  }
}
