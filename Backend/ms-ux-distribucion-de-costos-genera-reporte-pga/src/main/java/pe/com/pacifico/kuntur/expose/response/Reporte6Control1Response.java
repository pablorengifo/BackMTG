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
 * <b>Class</b>: Reporte6_Control1Response <br/>
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
@ApiModel(description = "Reporte6a model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Reporte6Control1Response implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(example = "ABCDE")
  private String codCentro;
  @ApiModelProperty(example = "ABCDE")
  private String centroNombre;
  @ApiModelProperty(example = "ABCDE")
  private String centroTipo;
  @ApiModelProperty(example = "ABCDE")
  private double montoTablaInput;
  @ApiModelProperty(example = "1")
  private double montoTablaBolsas;
  @ApiModelProperty(example = "1")
  private double montoTablaCascadaF;
  @ApiModelProperty(example = "1")
  private double montoTablaObjetos;
}
