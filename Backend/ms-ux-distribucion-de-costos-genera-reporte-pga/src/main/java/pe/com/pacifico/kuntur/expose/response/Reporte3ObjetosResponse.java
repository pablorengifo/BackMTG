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
 * <b>Class</b>: Reporte3ObjetosResponse <br/>
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
@ApiModel(description = "Reporte3 model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Reporte3ObjetosResponse implements Serializable {

  @ApiModelProperty(example = "ABCDE")
  private String asignacion;
  @ApiModelProperty(example = "ABCDE")
  private String canalNombre;
  @ApiModelProperty(example = "ABCDE")
  private String ccDestinoNiif17Atribuible;
  @ApiModelProperty(example = "ABCDE")
  private String ccInicialNiif17Atribuible;
  @ApiModelProperty(example = "1")
  private int centroDestinoNivel;
  @ApiModelProperty(example = "ABCDE")
  private String centroDestinoNombre;
  @ApiModelProperty(example = "ABCDE")
  private String centroDestinoTipo;
  @ApiModelProperty(example = "ABCDE")
  private String centroInicialGrupo;
  @ApiModelProperty(example = "1")
  private int centroInicialNivel;
  @ApiModelProperty(example = "ABCDE")
  private String centroInicialNombre;
  @ApiModelProperty(example = "ABCDE")
  private String centroInicialTipo;
  @ApiModelProperty(example = "ABCDE")
  private String codCanal;
  @ApiModelProperty(example = "ABCDE")
  private String codCentroDestino;
  @ApiModelProperty(example = "ABCDE")
  private String codCentroInicial;
  @ApiModelProperty(example = "ABCDE")
  private String codCuentaContableInicial;
  @ApiModelProperty(example = "ABCDE")
  private String codDriver;
  @ApiModelProperty(example = "ABCDE")
  private String codLinea;
  @ApiModelProperty(example = "ABCDE")
  private String codPartidaInicial;
  @ApiModelProperty(example = "ABCDE")
  private String codProducto;
  @ApiModelProperty(example = "ABCDE")
  private String codSubcanal;
  @ApiModelProperty(example = "ABCDE")
  private String ctaContableNiif17Atribuible;
  @ApiModelProperty(example = "ABCDE")
  private String cuentaContableInicialNombre;
  @ApiModelProperty(example = "ABCDE")
  private String driverNombre;
  @ApiModelProperty(example = "ABCDE")
  private String grupoGasto;
  @ApiModelProperty(example = "ABCDE")
  private String lineaNombre;
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
  @ApiModelProperty(example = "ABCDE")
  private String productoNombre;
  @ApiModelProperty(example = "ABCDE")
  private String resultadoNiif17Atribuible;
  @ApiModelProperty(example = "ABCDE")
  private String resultadoNiif17Tipo;
  @ApiModelProperty(example = "ABCDE")
  private String subcanalNombre;
  @ApiModelProperty(example = "ABCDE")
  private String tipo;
  @ApiModelProperty(example = "ABCDE")
  private String tipoGasto;
  @ApiModelProperty(example = "ABCDE")
  private String tipoNegocio;
  @ApiModelProperty(example = "ABCDE")
  private String asiento;
  @ApiModelProperty(example = "ABCDE")
  private String tipoReferenciaDocumento;
  @ApiModelProperty(example = "ABCDE")
  private String documentoContabilizado;
  @ApiModelProperty(example = "ABCDE")
  private String referencia;
  @ApiModelProperty(example = "ABCDE")
  private String fechaContable;
  @ApiModelProperty(example = "ABCDE")
  private String origenNegocio;
  @ApiModelProperty(example = "ABCDE")
  private String moneda;
  @ApiModelProperty(example = "ABCDE")
  private double montoMonedaOrigen;
  @ApiModelProperty(example = "ABCDE")
  private String tipoMovimiento;

}
