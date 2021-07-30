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
import pe.com.pacifico.kuntur.business.MaProductoService;
import pe.com.pacifico.kuntur.expose.request.MaProductoRequest;
import pe.com.pacifico.kuntur.expose.response.MaProductoResponse;
import pe.com.pacifico.kuntur.model.MaProducto;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.model.ResponseFileRead;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;




/**
 * <b>Class</b>: MaProductoController <br/>
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
@RequestMapping("/params/products")
@Slf4j
@RequiredArgsConstructor
public class MaProductoController {
  private final MaProductoService maProductoService;

  /**
   * This method is used to get all ProductoResponse.
   * @return all productos.
   */
  @GetMapping("{repartoTipo}")
  @ApiOperation(value = "MaProducto value", notes = "Endpoint example productos", response = MaProducto.class, code = 200)
  public Flux<MaProductoResponse> getProductos(@PathVariable("repartoTipo") @Valid int repartoTipo) {
    System.out.println("llegaProducto ");
    return maProductoService.getProductos(repartoTipo).log();
  }

  /**
   * This method is used to get all productoResponse not in Mov.
   * @return all productos not in Mov.
   */
  @GetMapping("period/{repartoTipo}/{periodo}")
  @ApiOperation(value = "MaProducto value", notes = "Endpoint example productos", response = MaProducto.class, code = 200)
  public Flux<MaProductoResponse> getProductosNotInMov(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                 @PathVariable("periodo") @Valid int periodo)
  {
    System.out.println("llega MaProductoNotInMov ");
    return maProductoService.getProductosNotInMovProducto(repartoTipo, periodo).log();
  }

  /**
   * This method is used to get only one productoResponse.
   * @param codigo This is the first parameter to method.
   * @return one producto.
   */
  @GetMapping("{repartoTipo}/{codigo}")
  @ApiOperation(value = "MaProducto value", notes = "Endpoint example productos", response = MaProducto.class, code = 200)
  public Mono<MaProductoResponse> getProductoByCodProducto(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                  @PathVariable("codigo") @Valid String codigo) {
    return maProductoService.getProducto(repartoTipo, codigo);
  }

  /**
   * This method is used to create a new Producto in the DB.
   * @param request This is the first parameter to method.
   * @return one producto.
   */
  @PostMapping
  @ApiOperation(value = "MaProducto value", notes = "Endpoint example productos", response = MaProducto.class, code = 200)
  public Mono<MaProductoResponse> registerProducto(
      @RequestBody @Valid MaProductoRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return maProductoService.registerProducto(request).flatMap(this::buildResponse);
  }

  /**
   * updateProducto.
   * @param: request
   * @return
   */
  @PutMapping
  @ApiOperation(value = "MaProducto value", notes = "Endpoint example producto", response = MaProducto.class, code = 200)
  public Mono<MaProductoResponse> updateProducto(
      @RequestBody @Valid MaProductoRequest request) {
    System.out.println("*******controller");
    System.out.println(request);
    return maProductoService.updateProducto(request).flatMap(this::buildResponse);
  }

  /**
   * deleteParametria.
   * @param: request
   * @return
   */
  @DeleteMapping("{repartoTipo}/{codigo}")
  @ApiOperation(value = "MaProducto value", notes = "Endpoint example producto", response = MaProducto.class, code = 200)
  public boolean deleteProducto(@PathVariable("repartoTipo") @Valid int repartoTipo,
                              @PathVariable("codigo") @Valid String codigo)
  {
    boolean isRemoved = maProductoService.deleteProducto(repartoTipo, codigo);
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

    return maProductoService.fileRead(repartoTipo, requestFileRead);
  }

  private Mono<MaProductoResponse> buildResponse(MaProducto producto) {
    MaProductoResponse maProductoResponse = new MaProductoResponse();
    try {
      maProductoResponse.setCodProducto(producto.getCodProducto());
      maProductoResponse.setNombre(producto.getNombre());
      maProductoResponse.setFechaCreacion(producto.getFechaCreacion());
      maProductoResponse.setFechaActualizacion(producto.getFechaActualizacion());
      maProductoResponse.setRepartoTipo(producto.getRepartoTipo());
      maProductoResponse.setEstaActivo(producto.isEstaActivo());
    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(maProductoResponse).log();
  }
}
