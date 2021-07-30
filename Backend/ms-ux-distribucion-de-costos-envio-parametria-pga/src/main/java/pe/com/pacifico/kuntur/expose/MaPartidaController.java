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
import pe.com.pacifico.kuntur.business.MaPartidaService;
import pe.com.pacifico.kuntur.expose.request.MaPartidaRequest;
import pe.com.pacifico.kuntur.expose.response.MaPartidaResponse;
import pe.com.pacifico.kuntur.model.MaPartida;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MaPartidaController <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 7, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://distribuciongastosdev.pacificotest.com.pe"})
@RequestMapping("/params/accounting-items")
@Slf4j
@RequiredArgsConstructor
public class MaPartidaController {

  private final MaPartidaService maPartidaService;

  /**
   * This method is used to get all partidaResponse.
   * @return all partidas.
   */
  @GetMapping("{repartoTipo}")
  @ApiOperation(value = "MaPartida value", notes = "Endpoint example partidas", response = MaPartida.class, code = 200)
  public Flux<MaPartidaResponse> getPartidas(@PathVariable("repartoTipo") @Valid int repartoTipo) {
    System.out.println("llegaPartida ");
    return maPartidaService.getPartidas(repartoTipo).log();
  }

  /**
   * This method is used to get all partidaResponse not in Mov.
   * @return all partidas not in Mov.
   */
  @GetMapping("period/{repartoTipo}/{periodo}")
  @ApiOperation(value = "MaPartida value", notes = "Endpoint example partidas", response = MaPartida.class, code = 200)
  public Flux<MaPartidaResponse> getPartidasNotInMov(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                     @PathVariable("periodo") @Valid int periodo)
  {
    System.out.println("llegaMaPartidaNotInMov ");
    return maPartidaService.getPartidasNotInMov(repartoTipo, periodo).log();
  }

  /**
   * This method is used to get only one partidaResponse.
   * @param codigo This is the first parameter to method.
   * @return one partida.
   */
  @GetMapping("{repartoTipo}/{codigo}")
  @ApiOperation(value = "MaPartida value", notes = "Endpoint example partidas", response = MaPartida.class, code = 200)
  public Mono<MaPartidaResponse> getPartidaByCodPartida(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                        @PathVariable("codigo") @Valid String codigo) {
    return maPartidaService.getPartida(repartoTipo, codigo);
  }

  /**
   * This method is used to get only one partidaToBeObfuscated.
   * @param request This is the first parameter to method.
   * @return one partida.
   */
  @PostMapping
  @ApiOperation(value = "MaPartida value", notes = "Endpoint example partidas", response = MaPartida.class, code = 200)
  public Mono<MaPartidaResponse> registerCuenta(
      @RequestBody @Valid MaPartidaRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return maPartidaService.registerPartida(request).flatMap(this::buildResponse);
  }

  /**
   * updatePartida.
   * @param: request
   * @return
   */
  @PutMapping
  @ApiOperation(value = "MaPartida value", notes = "Endpoint example partida", response = MaPartida.class, code = 200)
  public Mono<MaPartidaResponse> updatePartida(
      @RequestBody @Valid MaPartidaRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return maPartidaService.updatePartida(request).flatMap(this::buildResponse);
  }

  /**
   * deleteParametria.
   * @param: request
   * @return
   */
  @DeleteMapping("{repartoTipo}/{codigo}")
  @ApiOperation(value = "MaPartida value", notes = "Endpoint example partida", response = MaPartida.class, code = 200)
  public boolean deleteCuenta(@PathVariable("repartoTipo") @Valid int repartoTipo,
                              @PathVariable("codigo") @Valid String codigo)
  {
    boolean isRemoved = maPartidaService.deletePartida(repartoTipo, codigo);
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

    return maPartidaService.fileRead(repartoTipo, requestFileRead);
  }

  /**
   * This method converts a MaPartida into a response.
   * @param partida This is the first parameter to method.
   * @return one response
   */
  public Mono<MaPartidaResponse> buildResponse(MaPartida partida) {
    MaPartidaResponse maPartidaResponse = new MaPartidaResponse();
    try {
      maPartidaResponse.setCodPartida(partida.getCodPartida());
      maPartidaResponse.setNombre(partida.getNombre());
      maPartidaResponse.setFechaCreacion(partida.getFechaCreacion());
      maPartidaResponse.setFechaActualizacion(partida.getFechaActualizacion());
      maPartidaResponse.setGrupoGasto(partida.getGrupoGasto());
      maPartidaResponse.setRepartoTipo(partida.getRepartoTipo());
      maPartidaResponse.setTipoGasto(partida.isTipoGasto());
    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(maPartidaResponse).log();
  }
}
