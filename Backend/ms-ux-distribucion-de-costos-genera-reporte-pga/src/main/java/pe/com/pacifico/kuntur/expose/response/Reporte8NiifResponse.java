package pe.com.pacifico.kuntur.expose.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class</b>: Reporte8_NIIFResponse <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 10, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ApiModel(description = "Reporte 7b model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Reporte8NiifResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(example = "ABCDE")
  private String codCuentaContableInicial;
  @ApiModelProperty(example = "ABCDE")
  private String cuentaContableInicialNombre;
  @ApiModelProperty(example = "ABCDE")
  private String codLinea;
  @ApiModelProperty(example = "ABCDE")
  private String lineaNombre;
  @ApiModelProperty(example = "ABCDE")
  private String codProducto;
  @ApiModelProperty(example = "ABCDE")
  private String productoNombre;
  @ApiModelProperty(example = "ABCDE")
  private String codCanal;
  @ApiModelProperty(example = "ABCDE")
  private String canalNombre;
  @ApiModelProperty(example = "ABCDE")
  private String codSubcanal;
  @ApiModelProperty(example = "ABCDE")
  private String subcanalNombre;
  @ApiModelProperty(example = "ABCDE")
  private String resultadoNiif17Atribuible;
  @ApiModelProperty(example = "ABCDE")
  private String resultadoNiif17Tipo;
  @ApiModelProperty(example = "ABCDE")
  private String niif17Clase;
  @ApiModelProperty(example = "1")
  private double monto;

}
