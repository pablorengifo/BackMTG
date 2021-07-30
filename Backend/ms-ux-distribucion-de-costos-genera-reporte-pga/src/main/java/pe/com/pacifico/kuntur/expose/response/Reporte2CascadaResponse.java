package pe.com.pacifico.kuntur.expose.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class</b>: Reporte2CascadaResponse <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     June 3, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ApiModel(description = "Reporte2 model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Reporte2CascadaResponse implements Serializable {

  @ApiModelProperty(example = "1")
  private int centroDestinoNivel;
  @ApiModelProperty(example = "ABCDE")
  private String centroDestinoNombre;
  @ApiModelProperty(example = "ABCDE")
  private String centroDestinoTipo;
  @ApiModelProperty(example = "1")
  private int centroInicialNivel;
  @ApiModelProperty(example = "ABCDE")
  private String centroInicialNombre;
  @ApiModelProperty(example = "ABCDE")
  private String centroInicialTIpo;
  @ApiModelProperty(example = "1")
  private int centroOrigenNivel;
  @ApiModelProperty(example = "ABCDE")
  private String centroOrigenNombre;
  @ApiModelProperty(example = "ABCDE")
  private String centroOrigenTipo;

  @ApiModelProperty(example = "ABCDE")
  private String codCentroDestino;
  @ApiModelProperty(example = "ABCDE")
  private String codCentroInicial;
  @ApiModelProperty(example = "ABCDE")
  private String codCentroOrigen;
  @ApiModelProperty(example = "ABCDE")
  private String codCuentaContableInicial;

  @ApiModelProperty(example = "ABCDE")
  private String codDriver;
  @ApiModelProperty(example = "ABCDE")
  private String codPartidaInicial;
  @ApiModelProperty(example = "ABCDE")
  private String cuentaContableInicialNombre;
  @ApiModelProperty(example = "ABCDE")
  private String driverNombre;
  @ApiModelProperty(example = "1")
  private int iteracion;
  @ApiModelProperty(example = "ABCDE")
  private String codDocumentoClienteInicial;
  @ApiModelProperty(example = "ABCDE")
  private String clienteInicial;
  @ApiModelProperty(example = "ABCDE")
  private String tipoDocumentoClienteInicial;
  @ApiModelProperty(example = "1")
  private double monto;
  @ApiModelProperty(example = "ABCDE")
  private String partidaInicialNombre;
  @ApiModelProperty(example = "1")
  private int periodo;



}
