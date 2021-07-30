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
import pe.com.pacifico.kuntur.business.MovDriverObjetoService;
import pe.com.pacifico.kuntur.business.threads.CargarDriverObjetoHilo;
import pe.com.pacifico.kuntur.expose.response.MaDriverResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverObjetoResponse;
import pe.com.pacifico.kuntur.model.MaDriver;
import pe.com.pacifico.kuntur.model.MovDriverObjeto;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;



/**
 * <b>Class</b>: MovDriverObjetoController <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 28, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://distribuciongastosdev.pacificotest.com.pe"})
@RequestMapping("/params/cost-objects-drivers")
@Slf4j
@RequiredArgsConstructor
public class MovDriverObjetoController {

  private final MovDriverObjetoService movDriverObjetoService;

  @GetMapping("{repartoTipo}/{periodo}")
  @ApiOperation(value = "MaDriver value", notes = "Endpoint example Driver", response = MaDriver.class, code = 200)
  public Flux<MaDriverResponse> getDriverObjetos(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                 @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llegaDriverObjeto ");
    return movDriverObjetoService.getMaDriverObjeto(repartoTipo , periodo).log();
  }

  @GetMapping("{repartoTipo}/{periodo}/{codDriver}")
  @ApiOperation(value = "MovDriverObjeto value", notes = "Endpoint example Driver", response = MovDriverObjeto.class, code = 200)
  public Flux<MovDriverObjetoResponse> getMovDriverObjetos(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                           @PathVariable("periodo") @Valid int periodo,
                                                           @PathVariable("codDriver") @Valid String codDriver) {
    System.out.println("llegaMovDriverObjeto ");
    return movDriverObjetoService.getObjetos(repartoTipo , periodo, codDriver).log();
  }

  @GetMapping("period/{repartoTipo}/{periodo}")
  @ApiOperation(value = "MovDriverObjeto value", notes = "Endpoint example Driver", response = MovDriverObjeto.class, code = 200)
  public Flux<MovDriverObjetoResponse> getAllMovDriverObjetos(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                           @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llegaMovDriverObjeto ");
    return movDriverObjetoService.getObjetos(repartoTipo , periodo).log();
  }

  /**
   * deleteDriverObjeto.
   * @param: request
   * @return
   */
  @DeleteMapping("{repartoTipo}/{periodo}/{codigo}")
  @ApiOperation(value = "MovDriverObjeto value", notes = "Endpoint example MovDriverObjeto", response = boolean.class, code = 200)
  public boolean deleteDriverObjeto(
      @PathVariable("repartoTipo") @Valid int repartoTipo,
      @PathVariable("periodo") @Valid int periodo,
      @PathVariable("codigo") @Valid String codigo
  ) {

    boolean isRemoved = movDriverObjetoService.deleteMovDriverObjeto(repartoTipo, periodo,codigo);
    return isRemoved;
  }

  /**
   * uploadExcel.
   * @param: repartoTipo & periodo
   * @return ok
   */
  @PostMapping(value = "/upload/{repartoTipo}/{periodo}", produces = "application/stream+json;charset=UTF-8")
  @ApiOperation(value = "Leer contenido archivo excel de la hoja 1 y 2",
      notes = "Leer contenido archivo excel de la hoja 1 y 2",
      response = ResponseFileRead.class, code = 200)
  public List<String> fileRead(@RequestBody RequestFileRead requestFileRead,
                               @PathVariable("repartoTipo") @Valid int repartoTipo,
                               @PathVariable("periodo") @Valid int periodo) {
    log.info("Inicio de lectura archivo");
    log.info("" + requestFileRead);

    return movDriverObjetoService.fileRead(repartoTipo, periodo, requestFileRead);
  }

  @GetMapping("keepAlive")
  @ApiOperation(value = "prueba value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public boolean subiendoArchivo() {
    System.out.println("*******controller Evaluando si se esta subiendo archivo");
    return CargarDriverObjetoHilo.enEjecucion || !CargarDriverObjetoHilo.terminoEjecucion;
  }

  /**
   * get errores.
   * @return
   */
  @GetMapping("errores")
  @ApiOperation(value = "prueba value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public List<String> obtenerErroresEnSubidaDeArchivo() {
    System.out.println("*******controller Evaluando si se esta subiendo archivo");
    List<String> errores = CargarDriverObjetoHilo.errores;
    CargarDriverObjetoHilo.limpiarHilo();
    return errores;
  }
}
