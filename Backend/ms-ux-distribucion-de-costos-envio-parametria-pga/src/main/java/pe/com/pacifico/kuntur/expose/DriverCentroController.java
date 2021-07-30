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
import pe.com.pacifico.kuntur.business.DriverCentroService;
import pe.com.pacifico.kuntur.business.threads.CargarDriverCentroHilo;
import pe.com.pacifico.kuntur.expose.request.DriverCentroRequest;
import pe.com.pacifico.kuntur.expose.response.MaDriverResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverCentroResponse;
import pe.com.pacifico.kuntur.model.MaDriver;
import pe.com.pacifico.kuntur.model.MovDriverCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * <b>Class</b>: MovCentroController <br/>
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
@RequestMapping("/params/cost-centers-drivers")
@Slf4j
@RequiredArgsConstructor
public class DriverCentroController {

  private final DriverCentroService driverCentroService;

  /**
   * This method is used to get all centroResponse.
   * @return all centros.
   */
  @GetMapping("{repartoTipo}/{periodo}")
  @ApiOperation(value = "MaDriver value", notes = "Endpoint example Driver", response = MaDriver.class, code = 200)
  public Flux<MaDriverResponse> getDriverCentros(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                 @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llegaDriverCentro ");
    return driverCentroService.getMaDriverCentro(repartoTipo , periodo).log();
  }

  /**
   * This method is used to get all centroResponse.
   * @return all centros.
   */
  @GetMapping("period/{repartoTipo}/{periodo}")
  @ApiOperation(value = "MovDirver value", notes = "Endpoint example Driver", response = MovDriverCentro.class, code = 200)
  public Flux<MovDriverCentroResponse> getMovDriverCentros(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                    @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llegaDriverCentro ");
    return driverCentroService.getAllMovDriverCentro(repartoTipo , periodo).log();
  }

  /**
   * This method is used to get all centroResponse.
   * @return all centros.
   */
  @GetMapping("{repartoTipo}/{periodo}/{codDriverCentro}")
  @ApiOperation(value = "MovDriverCentro value", notes = "Endpoint example Driver", response = MovDriverCentroResponse.class, code = 200)
  public Flux<MovDriverCentroResponse> getMovDriversByMaDriver(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                               @PathVariable("periodo") @Valid int periodo,
                                                               @PathVariable("codDriverCentro") @Valid String codDriverCentro) {
    System.out.println("llegaDriverCentro ");
    return driverCentroService.getMovDriversCentro(repartoTipo , periodo,codDriverCentro).log();
  }


  /**
   * This method is used to get only one drivertoBeObfuscated.
   * @param request This is the first parameter to method.
   * @return one centro.
   */
  @PostMapping
  @ApiOperation(value = "MovDriverCentro value", notes = "Endpoint example Driver", response = int.class, code = 200)
  public int registerDriverCentro(
      @RequestBody @Valid DriverCentroRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return driverCentroService.registerDriverCentro(request);
  }

  /**
   * updateCentro.
   * @param: request
   * @return
   */
  @PutMapping
  @ApiOperation(value = "MovDriverCentro value", notes = "Endpoint example driver", response = MovDriverCentro.class, code = 200)
  public Mono<MovDriverCentroResponse> updateDriverCentro(
      @RequestBody @Valid DriverCentroRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return  driverCentroService.updateDriverCentro(request).flatMap(this::buildResponse);
  }

  /**
   * deleteDriverCentro.
   * @param: request
   * @return
   */
  @DeleteMapping("{repartoTipo}/{periodo}/{codigo}")
  @ApiOperation(value = "MovCentro value", notes = "Endpoint example centro", response = MovDriverCentro.class, code = 200)
  public boolean deleteCentro(
      @PathVariable("repartoTipo") @Valid int repartoTipo,
      @PathVariable("periodo") @Valid int periodo,
      @PathVariable("codigo") @Valid String codigo
  ) {

    boolean isRemoved = driverCentroService.deleteMovDriverCentro(repartoTipo, periodo,codigo);
    return isRemoved;
  }

  /**
   * uploadExcel.
   * @param: request
   * @return
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

    return driverCentroService.fileRead(repartoTipo, requestFileRead, periodo);
  }

  @GetMapping("keepAlive")
  @ApiOperation(value = "prueba value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public boolean subiendoArchivo() {
    System.out.println("*******controller Evaluando si se esta subiendo archivo");
    return CargarDriverCentroHilo.enEjecucion || !CargarDriverCentroHilo.terminoEjecucion;
  }

  /**
   * get errores.
   * @return
   */
  @GetMapping("errores")
  @ApiOperation(value = "prueba value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public List<String> obtenerErroresEnSubidaDeArchivo() {
    System.out.println("*******controller Evaluando si se esta subiendo archivo");
    List<String> errores = CargarDriverCentroHilo.errores;
    CargarDriverCentroHilo.limpiarHilo();
    return errores;
  }

  private Mono<MovDriverCentroResponse> buildResponse(MovDriverCentro driver) {
    MovDriverCentroResponse movDriverCentroResponse = new MovDriverCentroResponse();
    try {
      movDriverCentroResponse.setCodDriverCentro(driver.getCodDriverCentro());
      movDriverCentroResponse.setFechaActualizacion(driver.getFechaActualizacion());
      movDriverCentroResponse.setFechaCreacion(driver.getFechaCreacion());
      movDriverCentroResponse.setPeriodo(driver.getPeriodo());
      movDriverCentroResponse.setRepartoTipo(driver.getRepartoTipo());
    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(movDriverCentroResponse).log();
  }




}
