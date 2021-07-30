package pe.com.pacifico.kuntur.expose.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class</b>: MovCentroResponse <br/>
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
@ApiModel(description = "MovCentroResponse model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class MovCentroResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull
  @ApiModelProperty(example = "60.99.99")
  private String codCentro;

  @ApiModelProperty(example = "ABCDE")
  private String nombre;

  @ApiModelProperty(example = "ABCDE")
  private String tipo;

  @ApiModelProperty(example = "60.99.98")
  private String codCentroOrigen;

  @ApiModelProperty(example = "40.1.1.1.1")
  private String codCuentaContableOrigen;

  @ApiModelProperty(example = "ABCDE")
  private String codEntidadOrigen;

  @ApiModelProperty(example = "AA11")
  private String codPartidaOrigen;

  @ApiModelProperty(example = "2021-03-31")
  private Date fechaCreacion;

  @ApiModelProperty(example = "2021-03-31")
  private Date fechaActualizacion;

  @ApiModelProperty(example = "AA")
  private String grupoGasto;

  @ApiModelProperty(example = "10")
  private int iteracion;

  @NotNull
  @ApiModelProperty(example = "202101")
  private int periodo;

  @ApiModelProperty(example = "1")
  private int repartoTipo;

  @ApiModelProperty(example = "10.2")
  private float saldo;
}
