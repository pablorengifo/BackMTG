package pe.com.pacifico.kuntur.expose.request;

import com.pacifico.kuntur.core.data.input.validation.RegularExpression;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class</b>: EjecucionesToBeObfuscated <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Pacifico Seguros - La Chakra <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 23, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class EjecucionesToBeObfuscated implements Serializable {
  private static final long serialVersionUID = 1L;

  @NotNull
  @NotBlank(message = "Input must not be empty")
  @ApiModelProperty(example = "1")
  @Pattern(regexp = RegularExpression.ONLY_NUMBERS, message = "Input should be only numbers")
  private String id;

  @NotNull
  @NotBlank(message = "Input must not be empty")
  @ApiModelProperty(example = "DATA-TEST")
  @Pattern(regexp = RegularExpression.NOT_ALPHABETIC_AND_DASH, message = "Input must not include special characters.")
  private String data;

  @NotNull
  @NotBlank(message = "Input must not be empty")
  @ApiModelProperty(example = "123456789012345")
  @Pattern(regexp = RegularExpression.ONLY_NUMBERS, message = "Input should be only numbers")
  private String cardNumber;

  @NotNull
  @NotBlank(message = "Input must not be empty")
  @ApiModelProperty(example = "5367")
  @Pattern(regexp = RegularExpression.ONLY_NUMBERS, message = "Input should be only numbers")
  private String documentNumber;

  @NotNull
  @NotBlank(message = "Input must not be empty")
  @Pattern(regexp = RegularExpression.EMAIL_PATTERN, message = "Input doesn't match with e-mail pattern")
  @ApiModelProperty(example = "joe_doe@email-server.com")
  private String email;

  @NotNull
  @NotBlank(message = "Input must not be empty")
  @ApiModelProperty(example = "0011-0117-0200-7898")
  @Pattern(regexp = RegularExpression.NOT_SPECIAL_CHARACTERS, message = "Input must not include special characters.")
  private String cta;

  @NotNull
  @NotBlank(message = "Input must not be empty")
  @Pattern(regexp = RegularExpression.ONLY_NUMBERS, message = "Input should be only numbers")
  @ApiModelProperty(example = "94567831")
  private String phoneNumber;

}