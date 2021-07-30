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
import pe.com.pacifico.kuntur.business.MovUoaPaaService;
import pe.com.pacifico.kuntur.expose.request.MovUoaPaaRequest;
import pe.com.pacifico.kuntur.expose.response.MovUoaPaaResponse;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;


/**
 * <b>Class</b>: MovUoaPaaController <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 12, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://distribuciongastosdev.pacificotest.com.pe"})
@RequestMapping("/params/uoa-paa")
@Slf4j
@RequiredArgsConstructor
public class MovUoaPaaController {

  private final MovUoaPaaService movUoaPaaService;

  /**
   * This method is used to get all MovUoaPaa.
   * @return all MovUoaPaa.
   */
  @GetMapping("{repartoTipo}/{periodo}")
  @ApiOperation(value = "MovUoaPaa value", notes = "Endpoint example Driver", response = MovUoaPaaResponse.class, code = 200)
  public Flux<MovUoaPaaResponse> getAllMovUoaPaaList(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                               @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llegaAllMovUoaPaa ");
    return movUoaPaaService.getAllMovUoaPaaList(repartoTipo , periodo).log();
  }

  /**
   * This method is used to get all MovUoaPaa by CodProducto.
   * @return all MovUoaPaa by CodProducto.
   */
  @GetMapping("{repartoTipo}/{periodo}/{codProducto}")
  @ApiOperation(value = "MovUoaPaa value", notes = "Endpoint example Driver", response = MovUoaPaaResponse.class, code = 200)
  public Flux<MovUoaPaaResponse> getMovUoaPaaListByCodProducto(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                               @PathVariable("periodo") @Valid int periodo,
                                                               @PathVariable("codProducto") @Valid String codProducto) {
    System.out.println("llegaMovUoaPaa ");
    return movUoaPaaService.getMovUoaPaaList(repartoTipo , periodo,codProducto).log();
  }

  /**
   * This method is used to save registerMovUoaPaaList.
   * @param request This is the first parameter to method.
   * @return int.
   */
  @PostMapping
  @ApiOperation(value = "MovUoaPaa value", notes = "Endpoint example MovUoaPaa", response = int.class, code = 200)
  public int registerMovUoaPaaList(@RequestBody @Valid List<MovUoaPaaRequest> request) {
    System.out.println("*******controller");
    System.out.println(request);
    return movUoaPaaService.registerMovUoaPaaList(request);
  }

  /**
   * updateMovUoaPaaList.
   * @param: request.
   * @return int
   */
  @PutMapping
  @ApiOperation(value = "MovUoaPaa value", notes = "Endpoint example driver", response = int.class, code = 200)
  public int updateMovUoaPaaList(@RequestBody @Valid List<MovUoaPaaRequest> request) {
    System.out.println("*******controller");
    System.out.println(request);
    return movUoaPaaService.updateMovUoaPaaList(request);
  }

  /**
   * deleteMovUoaPaaList.
   * @param: request
   * @return boolean
   */
  @DeleteMapping("{repartoTipo}/{periodo}/{codProducto}")
  @ApiOperation(value = "MovUoaPaa value", notes = "Endpoint example MovUoaPaa", response = boolean.class, code = 200)
  public boolean deleteMovUoaPaaList(
      @PathVariable("repartoTipo") @Valid int repartoTipo,
      @PathVariable("periodo") @Valid int periodo,
      @PathVariable("codProducto") @Valid String codProducto
  ) {
    boolean isRemoved = movUoaPaaService.deleteMovUoaPaaList(repartoTipo, periodo, codProducto);
    return isRemoved;
  }

  /**
   * uploadExcel.
   * @param: request
   * @return
   */
  @PostMapping(value = "/upload/{repartoTipo}/{periodo}", produces = "application/stream+json;charset=UTF-8")
  @ApiOperation(value = "Leer contenido archivo excel de la hoja 12",
      notes = "Leer contenido archivo excel de la hoja 1",
      response = ResponseFileRead.class, code = 200)
  public List<String> fileRead(@RequestBody RequestFileRead requestFileRead,
                               @PathVariable("repartoTipo") @Valid int repartoTipo,
                               @PathVariable("periodo") @Valid int periodo) {
    log.info("Inicio de lectura archivo");
    log.info("" + requestFileRead);

    return movUoaPaaService.fileRead(repartoTipo, requestFileRead, periodo);
  }

}
