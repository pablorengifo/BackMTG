package pe.com.pacifico.kuntur.expose.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class</b>: Reporte4_CascadaCentrosResponse <br/>
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
@ApiModel(description = "Reporte4 model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Reporte4CascadaCentrosResponse {

  private static final long serialVersionUID = 1L;

  @NotNull
  @ApiModelProperty(example = "202912")
  private int periodo;

  @NotNull
  @ApiModelProperty(example = "1")
  private int iteracion;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String codCentroDestino;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String centroDestinoNombre;

  @NotNull
  @ApiModelProperty(example = "1")
  private int centroDestinoNivel;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String centroDestinoTipo;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String codCentroOrigen;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String centroOrigenNombre;

  @NotNull
  @ApiModelProperty(example = "1")
  private int centroOrigenNivel;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String centroOrigenTipo;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String tipo;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private double monto;

}
