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
 * <b>Class</b>: CuentaContableResponse <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     March 31, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ApiModel(description = "CuentaContableResponse model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class CuentaContableResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull
  @ApiModelProperty(example = "40.1.1.1.1")
  private String codCuentaContable;

  @ApiModelProperty(example = "ABC")
  private String nombre;

  @ApiModelProperty(example = "true")
  private boolean estaActivo;

  @ApiModelProperty(example = "2021-03-31")
  private Date fechaCreacion;

  @ApiModelProperty(example = "2021-03-31")
  private Date fechaActualizacion;

  @ApiModelProperty(example = "AA")
  private String niif17Atribuible;

  @ApiModelProperty(example = "AA")
  private String niif17Clase;

  @ApiModelProperty(example = "AA")
  private String niif17Tipo;

  @ApiModelProperty(example = "1")
  private int repartoTipo;

  @ApiModelProperty(example = "true")
  private boolean tipoGasto;
}
