package pe.com.pacifico.kuntur.expose;

import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.pacifico.kuntur.business.MovUoaBbaVfaService;
import pe.com.pacifico.kuntur.business.threads.CargarMovUoaBbaVfaHilo;
import pe.com.pacifico.kuntur.expose.response.MovUoaBbaVfaResponse;
import pe.com.pacifico.kuntur.model.MovUoaBbaVfa;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;

/**
 * <b>Class</b>: MovUoaBbaVfaController <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 14, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://distribuciongastosdev.pacificotest.com.pe"})
@RequestMapping("/params/uoa-bba-vfa")
@Slf4j
@RequiredArgsConstructor
public class MovUoaBbaVfaController {
  private final MovUoaBbaVfaService movUoaBbaVfaService;

  /**
   * This method is used to get all movUoaBbaVfas.
   * @return all movUoaBbaVfas.
   */
  @GetMapping("{repartoTipo}/{periodo}")
  @ApiOperation(value = "MovUoaBbaVfa value", notes = "Endpoint example MovUoaBbaVfa", response = MovUoaBbaVfa.class, code = 200)
  public Flux<MovUoaBbaVfaResponse> getAllMovUoaPaas(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llegaMovUoaBbaVfa ");
    return movUoaBbaVfaService.getAllMovUoaBbaVfas(repartoTipo,periodo).log();
  }

  /**
   * uploadExcel.
   * @param: repartoTipo & periodo
   * @return ok
   */
  @PostMapping(value = "/upload/{repartoTipo}/{periodo}", produces = "application/stream+json;charset=UTF-8")
  @ApiOperation(value = "Leer contenido archivo excel de la hoja 1",
      notes = "Leer contenido archivo excel de la hoja 1",
      response = ResponseFileRead.class, code = 200)
  public List<String> fileRead(@RequestBody RequestFileRead requestFileRead,
                               @PathVariable("repartoTipo") @Valid int repartoTipo,
                               @PathVariable("periodo") @Valid int periodo) {
    log.info("Inicio de lectura archivo MovUoaBbaVfa");
    log.info("" + requestFileRead);

    return movUoaBbaVfaService.fileRead(repartoTipo, requestFileRead, periodo);
  }

  @GetMapping("keepAlive")
  @ApiOperation(value = "prueba value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public boolean subiendoArchivo() {
    System.out.println("*******controller Evaluando si se esta subiendo archivo");
    return CargarMovUoaBbaVfaHilo.enEjecucion || !CargarMovUoaBbaVfaHilo.terminoEjecucion;
  }

  /**
   * get errores.
   * @return
   */
  @GetMapping("errores")
  @ApiOperation(value = "prueba value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public List<String> obtenerErroresEnSubidaDeArchivo() {
    System.out.println("*******controller Evaluando si se esta subiendo archivo");
    List<String> errores = CargarMovUoaBbaVfaHilo.errores;
    CargarMovUoaBbaVfaHilo.limpiarHilo();
    return errores;
  }

}
