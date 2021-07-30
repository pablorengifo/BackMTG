package pe.com.pacifico.kuntur.expose.request;

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
 * <b>Class</b>: MovCentroRequest <br/>
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
@ApiModel(description = "MovCentroRequest model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class MovCentroRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull
  @ApiModelProperty(value = "", example = "")
  private String codCentro;

  @ApiModelProperty(value = "", example = "")
  private String nombre;

  @ApiModelProperty(value = "", example = "")
  private String tipo;

  @ApiModelProperty(value = "", example = "")
  private String codCentroOrigen;

  @ApiModelProperty(value = "", example = "")
  private String codCuentaContableOrigen;

  @ApiModelProperty(value = "", example = "")
  private String codEntidadOrigen;

  @ApiModelProperty(value = "", example = "")
  private String codPartidaOrigen;

  @ApiModelProperty(value = "", example = "")
  private Date fechaCreacion;

  @ApiModelProperty(value = "", example = "")
  private Date fechaActualizacion;

  @ApiModelProperty(value = "", example = "AA")
  private String grupoGasto;

  @ApiModelProperty(value = "", example = "")
  private int iteracion;

  @NotNull
  @ApiModelProperty(value = "", example = "")
  private int periodo;

  @ApiModelProperty(value = "", example = "")
  private int repartoTipo;

  @ApiModelProperty(value = "", example = "")
  private float saldo;

}
