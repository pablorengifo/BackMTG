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
import pe.com.pacifico.kuntur.business.DetalleGastoService;
import pe.com.pacifico.kuntur.business.threads.CargarDetalleGastoHilo;
import pe.com.pacifico.kuntur.expose.response.DetalleGastoResponse;
import pe.com.pacifico.kuntur.model.DetalleGasto;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;

/**
 * <b>Class</b>: DetalleGastoController <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     March 31, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://distribuciongastosdev.pacificotest.com.pe"})
@RequestMapping("/params/expense-detail")
@Slf4j
@RequiredArgsConstructor
public class DetalleGastoController {
  private final DetalleGastoService detalleGastoService;

  /**
   * This method is used to get all DetalleGasto.
   * @return all canales.
   */
  @GetMapping("{repartoTipo}/{periodo}")
  @ApiOperation(value = "DetalleGasto value", notes = "Endpoint example DetalleGastos", response = DetalleGasto.class, code = 200)
  public Flux<DetalleGastoResponse> getDetalles(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llegaDetalleGasto ");
    return detalleGastoService.getDetallesGasto(repartoTipo,periodo).log();
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
    log.info("Inicio de lectura archivo Detalle gasto");
    log.info("" + requestFileRead);

    return detalleGastoService.fileRead(repartoTipo, requestFileRead, periodo);
  }

  /**
   * Generar detalle.
   * @param: repartoTipo & periodo
   * @return ok
   */
  @PostMapping("/generar/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Empezar generacion del detalle de gasto",response = Boolean.class, code = 200)
  public boolean generar(@PathVariable("repartoTipo") @Valid int repartoTipo,
                               @PathVariable("periodo") @Valid int periodo) {
    log.info("Preparando para generar el detalle de gasto");
    return detalleGastoService.generarDetalleGasto(repartoTipo, periodo);
  }

  @GetMapping("keepAlive")
  @ApiOperation(value = "prueba value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public boolean subiendoArchivo() {
    System.out.println("*******controller Evaluando si se esta subiendo archivo");
    return CargarDetalleGastoHilo.enEjecucion && !CargarDetalleGastoHilo.terminoEjecucion;
  }

  /**
   * get errores.
   * @return
   */
  @GetMapping("errores")
  @ApiOperation(value = "prueba value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public List<String> obtenerErroresEnSubidaDeArchivo() {
    System.out.println("*******controller Evaluando si se esta subiendo archivo");
    List<String> errores = CargarDetalleGastoHilo.errores;
    CargarDetalleGastoHilo.limpiarHilo();
    return errores;
  }

}
