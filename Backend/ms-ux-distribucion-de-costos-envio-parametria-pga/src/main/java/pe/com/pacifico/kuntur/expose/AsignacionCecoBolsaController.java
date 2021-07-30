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
import pe.com.pacifico.kuntur.business.AsignacionCecoBolsaService;
import pe.com.pacifico.kuntur.expose.response.AsignacionCecoBolsaResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverCentroResponse;
import pe.com.pacifico.kuntur.model.AsignacionCecoBolsa;
import pe.com.pacifico.kuntur.model.MovCentro;
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
@RequestMapping("/params/bolsa-drivers-assignments")
@Slf4j
@RequiredArgsConstructor
public class AsignacionCecoBolsaController {
  private final AsignacionCecoBolsaService asignacionCecoBolsaService;

  /**
   * This method is used to get all DetalleGasto.
   * @return all canales.
   */
  @GetMapping("{repartoTipo}/{periodo}")
  @ApiOperation(value = "AsignacionCecoBolsa value", notes = "Endpoint example AsignacionCecoBolsa",
      response = AsignacionCecoBolsa.class, code = 200)
  public Flux<AsignacionCecoBolsaResponse> getAsignaciones(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                      @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llegaDetalleGasto ");
    return asignacionCecoBolsaService.getAsignaciones(repartoTipo,periodo).log();
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

    return asignacionCecoBolsaService.fileRead(repartoTipo, requestFileRead, periodo);
  }

  /**
   * This method is used to get all centroResponse.
   * @return all centros.
   */
  @GetMapping("{repartoTipo}/{periodo}/{codDriverCentro}")
  @ApiOperation(value = "MovDriverCentro value", notes = "Endpoint example Driver", response = MovCentro.class, code = 200)
  public Flux<MovDriverCentroResponse> getMovDriversByMaDriver(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                               @PathVariable("periodo") @Valid int periodo,
                                                               @PathVariable("codDriverCentro") @Valid String codDriverCentro) {
    System.out.println("llegaDriverCentro ");
    return asignacionCecoBolsaService.getMovDriversCentro(repartoTipo , periodo,codDriverCentro).log();
  }
}
