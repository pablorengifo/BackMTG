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
 * <b>Class</b>: MovUoaBbaVfaRequest <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 14, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ApiModel(description = "MovUoaBbaVfaRequest model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class MovUoaBbaVfaRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull
  @ApiModelProperty(value = "", example = "")
  private String idPoliza;

  @ApiModelProperty(value = "", example = "")
  private String numeroPoliza;

  @ApiModelProperty(value = "", example = "")
  private String codigoUoAPacifico;

  @NotNull
  @ApiModelProperty(value = "", example = "")
  private String descripcionUoA;

  @ApiModelProperty(value = "", example = "")
  private String centroCosto;

  @ApiModelProperty(value = "", example = "")
  private String lineaNegocio;

  @NotNull
  @ApiModelProperty(value = "", example = "")
  private String productoPpto;

  @ApiModelProperty(value = "", example = "")
  private String canalDistribucion;

  @NotNull
  @ApiModelProperty(value = "", example = "")
  private String subCanal;

  @ApiModelProperty(value = "", example = "")
  private Date fechaCreacion;

  @ApiModelProperty(value = "", example = "")
  private Date fechaActualizacion;

  @NotNull
  @ApiModelProperty(value = "", example = "")
  private int periodo;

  @NotNull
  @ApiModelProperty(value = "", example = "")
  private int repartoTipo;

}
