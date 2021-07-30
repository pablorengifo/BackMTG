package pe.com.pacifico.kuntur.expose.response;

import com.pacifico.kuntur.core.crypto.annotation.Decrypted;
import com.pacifico.kuntur.core.crypto.annotation.Encrypted;
import com.pacifico.kuntur.core.obfuscator.annotation.Obfuscated;
import com.pacifico.kuntur.core.obfuscator.annotation.ObfuscatedType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class</b>: ParamsResponse <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Pacifico Seguros - La Chakra <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 26, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ApiModel(description = "ParamsResponse model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class ParamsResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull
  @ApiModelProperty(example = "1")
  private Long idController;

  @ApiModelProperty(example = "test-data")
  private String data;

  @ApiModelProperty(example = "************2345")
  @Obfuscated
  private String cardNumber;

  @ApiModelProperty(example = "****1377")
  @Obfuscated
  private String documentNumber;

  @ApiModelProperty(example = "*****8768")
  @Obfuscated
  private List<String> phoneNumbers;

  @Obfuscated(type = ObfuscatedType.CCC)
  private String ctaCte;

  @Obfuscated(type = ObfuscatedType.EMAIL)
  private String email;

  @Encrypted
  private String privateData;

  @Decrypted
  private String privateInfo;
}
