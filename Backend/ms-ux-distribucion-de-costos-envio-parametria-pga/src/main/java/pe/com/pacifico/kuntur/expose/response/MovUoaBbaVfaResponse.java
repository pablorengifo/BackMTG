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
 * <b>Class</b>: MovUoaBbaVfaResponse <br/>
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
@ApiModel(description = "MovUoaBbaVfaResponse model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class MovUoaBbaVfaResponse implements Serializable {

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String idPoliza;

  @ApiModelProperty(example = "ABCDE")
  private String numeroPoliza;

  @ApiModelProperty(example = "ABCDE")
  private String codigoUoAPacifico;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String descripcionUoA;

  @ApiModelProperty(example = "ABCDE")
  private String centroCosto;

  @ApiModelProperty(example = "ABCDE")
  private String lineaNegocio;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String productoPpto;

  @ApiModelProperty(example = "ABCDE")
  private String canalDistribucion;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String subCanal;

  @ApiModelProperty(example = "2021-03-31")
  private Date fechaCreacion;

  @ApiModelProperty(example = "2021-03-31")
  private Date fechaActualizacion;

  @ApiModelProperty(example = "123")
  private int periodo;

  @ApiModelProperty(example = "ABC")
  private int repartoTipo;
}
