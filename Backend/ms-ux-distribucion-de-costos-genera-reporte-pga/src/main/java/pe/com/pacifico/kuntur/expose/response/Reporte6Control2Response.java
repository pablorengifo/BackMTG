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
 * <b>Class</b>: Reporte6_Control2Response <br/>
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
@ApiModel(description = "Reporte6b model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Reporte6Control2Response implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(example = "ABCDE")
  private String codCuentaContable;
  @ApiModelProperty(example = "ABCDE")
  private String cuentaContableNombre;
  @ApiModelProperty(example = "ABCDE")
  private String codPartida;
  @ApiModelProperty(example = "ABCDE")
  private String partidaNombre;

  @ApiModelProperty(example = "1")
  private double montoTablaInput;
  @ApiModelProperty(example = "1")
  private double montoTablaObjetos;
  @ApiModelProperty(example = "1")
  private double cntCentrosTablaInput;
  @ApiModelProperty(example = "1")
  private double cntCentrosTablaObjetos;
}
