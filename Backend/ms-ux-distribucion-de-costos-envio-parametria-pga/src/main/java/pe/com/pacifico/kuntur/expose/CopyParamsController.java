package pe.com.pacifico.kuntur.expose;

import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.pacifico.kuntur.business.CopyParamsService;


/**
 * <b>Class</b>: CopyParamsController <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 14, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://distribuciongastosdev.pacificotest.com.pe"})
@RequestMapping("/params/copy")
@Slf4j
@RequiredArgsConstructor
public class CopyParamsController {

  private final CopyParamsService copyParamsService;

  /**
   * This method is used to copy parameters from one period to another.
   * @return boolean.
   */
  @PostMapping("{repartoTipo}/{periodoOri}/{periodoDest}")
  @ApiOperation(value = "Boolean value", notes = "Copy Params", response = boolean.class, code = 200)
  public boolean copyParams(@PathVariable("repartoTipo") @Valid int repartoTipo,
                            @PathVariable("periodoOri") @Valid int periodoOri,
                            @PathVariable("periodoDest") @Valid int periodoDest) {
    System.out.println("llegaCopyParams ");
    return copyParamsService.copyParams(repartoTipo, periodoOri, periodoDest);
  }
}
