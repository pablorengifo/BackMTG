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
 * <b>Class</b>: MovUoaPaaResponse <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 09, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ApiModel(description = "MovUoaPaaResponse model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class MovUoaPaaResponse implements Serializable {

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String codProducto;

  @ApiModelProperty(example = "ABCDE")
  private String nombreProducto;

  @NotNull
  @ApiModelProperty(example = "U_ABCDE")
  private String unidadCuenta;

  @NotNull
  @ApiModelProperty(example = "10.2")
  private double porcentaje;

  @ApiModelProperty(example = "2021-03-31")
  private Date fechaCreacion;

  @ApiModelProperty(example = "2021-03-31")
  private Date fechaActualizacion;

  @ApiModelProperty(example = "123")
  private int periodo;

  @ApiModelProperty(example = "ABC")
  private int repartoTipo;
}
