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
 * <b>Class</b>: Reporte7_Agile1CentrosResponse <br/>
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
@ApiModel(description = "Reporte 7a model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Reporte7Agile1CentrosResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(example = "1")
  private int iteracion;
  @ApiModelProperty(example = "ABCDE")
  private String codCentroDestino;
  @ApiModelProperty(example = "ABCDE")
  private String centroDestinoNombre;
  @ApiModelProperty(example = "ABCDE")
  private String centroDestinoGrupo;
  @ApiModelProperty(example = "1")
  private int centroDestinoNivel;
  @ApiModelProperty(example = "ABCDE")
  private String centroDestinoTipo;
  @ApiModelProperty(example = "ABCDE")
  private String codCentroOrigen;
  @ApiModelProperty(example = "ABCDE")
  private String centroOrigenNombre;
  @ApiModelProperty(example = "ABCDE")
  private String centroOrigenGrupo;
  @ApiModelProperty(example = "1")
  private int centroOrigenNivel;
  @ApiModelProperty(example = "ABCDE")
  private String centroOrigenTipo;
  @ApiModelProperty(example = "ABCDE")
  private String codCentroPadreOrigen;
  @ApiModelProperty(example = "ABCDE")
  private String centroPadreOrigenNombre;
  @ApiModelProperty(example = "ABCDE")
  private String codCentroPadreDestino;
  @ApiModelProperty(example = "ABCDE")
  private String centroPadreDestinoNombre;
  @ApiModelProperty(example = "ABCDE")
  private String tipo;
  @ApiModelProperty(example = "ABCDE")
  private String tipoGasto;
  @ApiModelProperty(example = "1")
  private double monto;

}
