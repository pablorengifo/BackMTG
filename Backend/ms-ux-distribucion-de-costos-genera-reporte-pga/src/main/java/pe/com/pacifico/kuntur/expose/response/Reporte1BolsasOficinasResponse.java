package pe.com.pacifico.kuntur.expose.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class</b>: Reporte1_BolsasOficinasResponse <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 10, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ApiModel(description = "Reporte1 model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Reporte1BolsasOficinasResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  /*ORDERNADO SEGUN REPORTE:
    'PERIODO', 'CUENTA_CONTABLE_ORIGEN_CODIGO', 'CUENTA_CONTABLE_ORIGEN_NOMBRE',
    'PARTIDA_ORIGEN_CODIGO', 'PARTIDA_ORIGEN_NOMBRE', 'CENTRO_ORIGEN_CODIGO',
    'CENTRO_ORIGEN_NOMBRE', 'CENTRO_ORIGEN_NIVEL', 'CENTRO_ORIGEN_TIPO', 'CENTRO_DESTINO_CODIGO',
    'CENTRO_DESTINO_NOMBRE', 'CENTRO_DESTINO_NIVEL', 'CENTRO_DESTINO_TIPO', 'MONTO', 'DRIVER_CODIGO',
    'DRIVER_NOMBRE', 'ASIGNACION'*/

  @NotNull
  @ApiModelProperty(example = "202001", position = 1)
  private int periodo;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String codCuentaContableOrigen;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String cuentaContableOrigenNombre;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String codPartidaOrigen;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String partidaOrigenNombre;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String codCentroOrigen;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String centroOrigenNombre;

  @NotNull
  @ApiModelProperty(example = "1")
  private int centroOrigenNivel;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String centroOrigenTipo;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String codCentroDestino;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String centroDestinoNombre;

  @NotNull
  @ApiModelProperty(example = "1")
  private int centroDestinoNivel;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String centroDestinoTipo;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String codDocumentoClienteOrigen;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String clienteOrigen;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String tipoDocumentoClienteOrigen;

  @NotNull
  @ApiModelProperty(example = "10")
  private double monto;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String codDriver;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String driverNombre;

  @NotNull
  @ApiModelProperty(example = "ABCDE")
  private String asignacion;


}
