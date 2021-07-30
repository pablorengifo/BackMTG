package pe.com.pacifico.kuntur.webclient.exception;

import static com.pacifico.kuntur.core.errorhandling.ErrorCodeBuilder.getErrorCode;

import com.pacifico.kuntur.core.errorhandling.ErrorConstant;
import com.pacifico.kuntur.core.exception.KunturException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import org.springframework.http.HttpStatus;

/**
 * <b>Class</b>: WebClientException <br/>
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
public final class WebClientException extends KunturException {

  private static final long serialVersionUID = 6088136638885458888L;

  @Builder
  public WebClientException(HttpStatus httpStatus, String id, String code, String type,
                           ErrorConstant.Category category, String description,
                           String externalDescription, String legacyCode, List<String> errors) {
    super(httpStatus, id, code, type, category, description, externalDescription, legacyCode, errors);
  }

  /**
   * createException.
   * @param: httpStatus
   * @param: description
   * @param: error
   * @return
   */
  public static WebClientException createException(HttpStatus httpStatus, WebClientErrorCodes webClientErrorCodes) {
    return WebClientException.builder()
        .httpStatus(httpStatus == null ? HttpStatus.INTERNAL_SERVER_ERROR : httpStatus)
        .id(UUID.randomUUID().toString())
        .code(getErrorCode(
            ErrorConstant.Type.SYSTEM,
            ErrorConstant.Layer.SUPPORT,
            ErrorConstant.SystemComponent.SUPPORT,
            webClientErrorCodes.getCode()))
        .type(ErrorConstant.Type.SYSTEM.getDescription())
        .description(webClientErrorCodes.getDescription())
        .category(ErrorConstant.Category.ERROR)
        .errors(Collections.singletonList(webClientErrorCodes.getTitle()))
        .build();
  }
}
