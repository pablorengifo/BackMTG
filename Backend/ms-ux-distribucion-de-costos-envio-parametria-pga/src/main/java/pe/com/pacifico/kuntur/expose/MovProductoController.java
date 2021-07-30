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
import pe.com.pacifico.kuntur.business.MovProductoService;
import pe.com.pacifico.kuntur.expose.request.MovProductoRequest;
import pe.com.pacifico.kuntur.expose.response.MovProductoResponse;
import pe.com.pacifico.kuntur.model.MovProducto;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * <b>Class</b>: MovProductoController <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 19, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */

@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://distribuciongastosdev.pacificotest.com.pe"})
@RequestMapping("/params/products-period")
@Slf4j
@RequiredArgsConstructor
public class MovProductoController {

  private final MovProductoService movProductoService;

  @GetMapping("{repartoTipo}/{periodo}")
  @ApiOperation(value = "MovProducto value", notes = "Endpoint example productos", response = MovProducto.class, code = 200)
  public Flux<MovProductoResponse> getProductos(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llegalProductoPeriodo");
    return movProductoService.getProductos(repartoTipo, periodo).log();
  }

  /**
   * This method is used to get only one productoToBeObfuscated.
   *
   * @param request This is the first parameter to method.
   * @return one producto.
   */
  @PostMapping
  @ApiOperation(value = "MovProducto value", notes = "Endpoint example productos", response = int.class, code = 200)
  public int registerProducto(
      @RequestBody @Valid MovProductoRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return movProductoService.registerProducto(request);
  }

  /**
   * deleteProducto.
   * @param: request
   * @return
   */
  @DeleteMapping("{repartoTipo}/{periodo}/{codigo}")
  @ApiOperation(value = "MovProducto value", notes = "Endpoint example producto", response = MovProducto.class, code = 200)
  public boolean deleteProducto(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                  @PathVariable("periodo") @Valid int periodo,
                                                  @PathVariable("codigo") @Valid String codigo) {
    boolean isRemoved = movProductoService.deleteProducto(repartoTipo, periodo, codigo);
    System.out.println("deleteTest: " + periodo + " " + codigo + " " + isRemoved);
    return isRemoved;
    //return new Mono<ProductoResponse>(id, HttpStatus.OK);
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

    return movProductoService.fileRead(repartoTipo, requestFileRead, periodo);
  }

  /**
   * This method converts a MovProducto into a response.
   * @param producto This is the first parameter to method.
   * @return one response
   */
  public Mono<MovProductoResponse> buildResponse(MovProducto producto) {
    MovProductoResponse productoResponse = new MovProductoResponse();
    try {
      productoResponse.setCodProducto(producto.getCodProducto());
      productoResponse.setCodLinea(producto.getCodLinea());
      productoResponse.setPeriodo(producto.getPeriodo());
      productoResponse.setFechaActualizacion(producto.getFechaActualizacion());
      productoResponse.setFechaCreacion(producto.getFechaCreacion());
      productoResponse.setRepartoTipo(producto.getRepartoTipo());
      productoResponse.setNombre(producto.getNombre());
      productoResponse.setNombreLinea(producto.getNombreLinea());
    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(productoResponse).log();
  }
}
