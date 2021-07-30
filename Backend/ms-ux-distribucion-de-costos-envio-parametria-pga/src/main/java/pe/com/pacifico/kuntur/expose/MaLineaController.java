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
import pe.com.pacifico.kuntur.business.MaLineaService;
import pe.com.pacifico.kuntur.expose.request.MaLineaRequest;
import pe.com.pacifico.kuntur.expose.response.MaLineaResponse;
import pe.com.pacifico.kuntur.model.MaLinea;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * <b>Class</b>: MaLineaController <br/>
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
@RequestMapping("/params/lines")
@Slf4j
@RequiredArgsConstructor
public class MaLineaController {

  private final MaLineaService maLineaService;

  /**
   * This method is used to get all LineaResponse.
   * @return all lineas.
   */
  @GetMapping("{repartoTipo}")
  @ApiOperation(value = "MaLinea value", notes = "Endpoint example lineas", response = MaLinea.class, code = 200)
  public Flux<MaLineaResponse> getLineas(@PathVariable("repartoTipo") @Valid int repartoTipo) {
    System.out.println("llegaLinea ");
    return maLineaService.getLineas(repartoTipo).log();
  }


  /**
   * This method is used to get only one lineaResponse.
   * @param codigo This is the first parameter to method.
   * @return one linea.
   */
  /*
  @GetMapping("{repartoTipo}/{codigo}")
  @ApiOperation(value = "MaLinea value", notes = "Endpoint example lineas", response = MaLinea.class, code = 200)
  public Mono<MaLineaResponse> getLineaByCodLinea(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                        @PathVariable("codigo") @Valid String codigo) {
    return maLineaService.getLinea(repartoTipo, codigo);
  }*/

  /**
   * This method is used to create a new Linea in the DB.
   * @param request This is the first parameter to method.
   * @return one linea.
   */
  @PostMapping
  @ApiOperation(value = "MaLinea value", notes = "Endpoint example lineas", response = MaLinea.class, code = 200)
  public Mono<MaLineaResponse> registerCuenta(
      @RequestBody @Valid MaLineaRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return maLineaService.registerLinea(request).flatMap(this::buildResponse);
  }

  /**
   * updateLinea.
   * @param: request
   * @return
   */
  @PutMapping
  @ApiOperation(value = "MaLinea value", notes = "Endpoint example linea", response = MaLinea.class, code = 200)
  public Mono<MaLineaResponse> updateLinea(
      @RequestBody @Valid MaLineaRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return maLineaService.updateLinea(request).flatMap(this::buildResponse);
  }

  /**
   * deleteParametria.
   * @param: request
   * @return
   */
  @DeleteMapping("{repartoTipo}/{codigo}")
  @ApiOperation(value = "MaLinea value", notes = "Endpoint example linea", response = MaLinea.class, code = 200)
  public boolean deleteCuenta(@PathVariable("repartoTipo") @Valid int repartoTipo,
                              @PathVariable("codigo") @Valid String codigo)
  {
    boolean isRemoved = maLineaService.deleteLinea(repartoTipo, codigo);
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

    return maLineaService.fileRead(repartoTipo, requestFileRead);
  }

  private Mono<MaLineaResponse> buildResponse(MaLinea linea) {
    MaLineaResponse maLineaResponse = new MaLineaResponse();
    try {
      maLineaResponse.setCodLinea(linea.getCodLinea());
      maLineaResponse.setNombre(linea.getNombre());
      maLineaResponse.setFechaCreacion(linea.getFechaCreacion());
      maLineaResponse.setFechaActualizacion(linea.getFechaActualizacion());
      maLineaResponse.setRepartoTipo(linea.getRepartoTipo());
      maLineaResponse.setEstaActivo(linea.isEstaActivo());
    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(maLineaResponse).log();
  }
}
