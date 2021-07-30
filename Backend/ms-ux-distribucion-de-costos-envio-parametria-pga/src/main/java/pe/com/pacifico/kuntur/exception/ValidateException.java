package pe.com.pacifico.kuntur.exception;

import com.pacifico.kuntur.core.exception.KunturFrameworkException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <b>Class</b>: ValidateException <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Pacifico Seguros - La Chakra <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     March 23, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ValidateException extends KunturFrameworkException {
  @Getter
  private String code;

  @Getter
  private HttpStatus httpStatus;

  /**
   * Full constructor EcommerceLifeException().
   *
   * @param: message
   * @param: cause
   * @param: code
   * @param: httpStatus
   */
  public ValidateException(String message, Throwable cause, String code, HttpStatus httpStatus) {
    super(message, cause);
    this.code = code;
    this.httpStatus = httpStatus;
  }
}
