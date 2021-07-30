package pe.com.pacifico.kuntur.expose.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <b>Class</b>: MovDriverObjetoResponse <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 28, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ApiModel(description = "MovDriverObjetoResponse model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class MovDriverObjetoResponse {

  private static final long serialVersionUID = 1L;

  @NotNull
  @ApiModelProperty(example = "12345")
  private String codDriverObjeto;

  @ApiModelProperty(example = "ABCDE")
  private String nombreDriver;

  @ApiModelProperty(example = "1")
  private int repartoTipo;

  @ApiModelProperty(example = "202101")
  private int periodo;

  @ApiModelProperty(example = "AA11")
  private String codProducto;

  @ApiModelProperty(example = "ABCDE")
  private String nombreProducto;

  @ApiModelProperty(example = "AA11")
  private String codSubcanal;

  @ApiModelProperty(example = "ABCDE")
  private String nombreSubcanal;

  @ApiModelProperty(example = "2021-03-31")
  private Date fechaCreacion;

  @ApiModelProperty(example = "2021-03-31")
  private Date fechaActualizacion;

  @ApiModelProperty(example = "10.2")
  private double porcentaje;
}
