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
 * <b>Class</b>: AsignacionDriverCascadaResponse <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 30, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ApiModel(description = "AsignacionDriverCascadaResponse model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class AsignacionDriverCascadaResponse implements Serializable {

  @NotNull
  @ApiModelProperty(example = "12345")
  private String codCentro;

  @NotNull
  @ApiModelProperty(example = "12345")
  private String codDriver;

  @ApiModelProperty(example = "12345")
  private String grupoGasto;

  @ApiModelProperty(example = "2021-03-31")
  private Date fechaCreacion;

  @ApiModelProperty(example = "2021-03-31")
  private Date fechaActualizacion;

  @ApiModelProperty(example = "1")
  private int repartoTipo;

  @ApiModelProperty(example = "202101")
  private int periodo;

  @ApiModelProperty(example = "ABCDE")
  private String nombreCentro;

  @ApiModelProperty(example = "ABCDE")
  private String nombreDriver;

}

