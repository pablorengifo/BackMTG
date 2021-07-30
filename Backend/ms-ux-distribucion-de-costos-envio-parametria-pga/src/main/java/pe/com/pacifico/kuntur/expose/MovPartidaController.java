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
import pe.com.pacifico.kuntur.business.MovPartidaService;
import pe.com.pacifico.kuntur.expose.request.MovPartidaRequest;
import pe.com.pacifico.kuntur.expose.response.MovPartidaResponse;
import pe.com.pacifico.kuntur.model.MovPartida;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MovPartidaController <br/>
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
@RequestMapping("/params/accounting-items-period")
@Slf4j
@RequiredArgsConstructor
public class MovPartidaController {

  private final MovPartidaService movPartidaService;

  /**
   * This method is used to get all partidaResponse.
   * @return all partidas.
   */
  @GetMapping("{repartoTipo}/{periodo}")
  @ApiOperation(value = "MovPartida value", notes = "Endpoint example partidas", response = MovPartida.class, code = 200)
  public Flux<MovPartidaResponse> getPartidas(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                              @PathVariable("periodo") @Valid int periodo)
  {
    System.out.println("llegaPartidaPeriodo ");
    return movPartidaService.getPartidas(repartoTipo, periodo).log();

  }

  /**
   * This method is used to get only one partidaToBeObfuscated.
   * @param request This is the first parameter to method.
   * @return one partida.
   */
  @PostMapping
  @ApiOperation(value = "MovPartida value", notes = "Endpoint example partidas", response = int.class, code = 200)
  public int registerPartida(
      @RequestBody @Valid MovPartidaRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return movPartidaService.registerPartida(request);
  }

  /**
   * deletePartida.
   * @param: request
   * @return
   */
  @DeleteMapping("{repartoTipo}/{periodo}/{codigo}")
  @ApiOperation(value = "MovPartida value", notes = "Endpoint example partida", response = MovPartida.class, code = 200)
  public boolean deletePartida(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                @PathVariable("periodo") @Valid int periodo,
                                                @PathVariable("codigo") @Valid String codigo)
  {
    boolean isRemoved = movPartidaService.deletePartida(repartoTipo, periodo, codigo);
    /*if (!isRemoved) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }*/
    System.out.println("deleteTest: " + periodo + " " + codigo + " " + isRemoved);
    return isRemoved;
    //return new Mono<PartidaResponse>(id, HttpStatus.OK);
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

    return movPartidaService.fileRead(repartoTipo, requestFileRead, periodo);
  }

  /**
   * This method converts a MovPartida into a response.
   * @param partida This is the first parameter to method.
   * @return one response
   */
  public Mono<MovPartidaResponse> buildResponse(MovPartida partida) {
    MovPartidaResponse partidaResponse = new MovPartidaResponse();
    try {
      partidaResponse.setCodPartida(partida.getCodPartida());
      partidaResponse.setFechaCreacion(partida.getFechaCreacion());
      partidaResponse.setFechaActualizacion(partida.getFechaActualizacion());
      partidaResponse.setPeriodo(partida.getPeriodo());
      partidaResponse.setRepartoTipo(partida.getRepartoTipo());
      partidaResponse.setSaldo(partida.getSaldo());
    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(partidaResponse).log();
  }
}
