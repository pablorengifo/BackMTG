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
 * <b>Class</b>: MaProductoRequest <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 7, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ApiModel(description = "MaProductoRequest model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class MaProductoRequest implements Serializable {
  private static final long serialVersionUID = 1L;

  @NotNull
  @ApiModelProperty(value = "", example = "")
  private String codProducto;

  @ApiModelProperty(value = "", example = "")
  private String nombre;

  @ApiModelProperty(value = "", example = "")
  private Date fechaCreacion;

  @ApiModelProperty(value = "", example = "")
  private Date fechaActualizacion;

  @ApiModelProperty(value = "", example = "")
  private int repartoTipo;

  @ApiModelProperty(value = "", example = "")
  private boolean estaActivo;

}
