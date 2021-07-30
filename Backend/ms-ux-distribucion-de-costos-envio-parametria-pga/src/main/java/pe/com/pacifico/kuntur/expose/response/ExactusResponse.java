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
 * <b>Class</b>: DetalleGastoResponse <br/>
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
@ApiModel(description = "DetalleGastoRealResponse model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class ExactusResponse implements Serializable {
  private static final long serialVersionUID = 1L;

  @NotNull
  @ApiModelProperty(example = "40.1.1.1.1")
  private String codCuentaContable;

  @ApiModelProperty(example = "ABCDE")
  private String nombreCuentaContable;

  @NotNull
  @ApiModelProperty(example = "AA11")
  private String codPartida;

  @ApiModelProperty(example = "ABCDE")
  private String nombrePartida;

  @NotNull
  @ApiModelProperty(example = "60.99.99")
  private String codCentro;

  @ApiModelProperty(example = "ABCDE")
  private String nombreCentro;

  @ApiModelProperty(example = "ABCDE")
  private String tipoDocumentoCliente;

  @ApiModelProperty(example = "ABCDE")
  private String codDocumentoCliente;

  @ApiModelProperty(example = "ABCDE")
  private String cliente;

  @ApiModelProperty(example = "10.2")
  private double monto;

  @NotNull
  @ApiModelProperty(example = "202101")
  private int repartoTipo;

  @NotNull
  @ApiModelProperty(example = "202101")
  private int periodo;

  @ApiModelProperty(example = "ABCDE")
  private String asiento;

  @ApiModelProperty(example = "ABCDE")
  private String tipoReferenciaDocumento;

  @ApiModelProperty(example = "ABCDE")
  private String documentoContabilizado;

  @ApiModelProperty(example = "ABCDE")
  private String referencia;

  @ApiModelProperty(example = "2021-03-31")
  private Date fechaContable;

  @ApiModelProperty(example = "ABCDE")
  private String origenNegocio;

  @ApiModelProperty(example = "ABCDE")
  private String moneda;

  @ApiModelProperty(example = "10.2")
  private double montoMonedaOrigen;

  @ApiModelProperty(example = "ABCDE")
  private String tipoMovimiento;

  @ApiModelProperty(example = "2021-03-31")
  private Date fechaCreacion;







}
