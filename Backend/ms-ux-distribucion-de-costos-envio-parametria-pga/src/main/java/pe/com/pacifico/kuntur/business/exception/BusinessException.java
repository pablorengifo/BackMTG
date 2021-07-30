package pe.com.pacifico.kuntur.business.exception;

import static com.pacifico.kuntur.core.errorhandling.ErrorCodeBuilder.getErrorCode;

import com.pacifico.kuntur.core.errorhandling.ErrorConstant;
import com.pacifico.kuntur.core.exception.KunturException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * <b>Class</b>: BusinessException <br/>
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
@Slf4j
public class BusinessException extends KunturException {

  private static final long serialVersionUID = 6088136638885458888L;

  @Builder
  public BusinessException(HttpStatus httpStatus, String id, String code, String type,
                           ErrorConstant.Category category, String description,
                           String externalDescription, String legacyCode, List<String> errors) {
    super(httpStatus, id, code, type, category, description, externalDescription, legacyCode, errors);
  }

  /**
   * createException.
   *
   * @param httpStatus         This is the first parameter to method.
   * @param businessErrorCodes This is the second parameter to method.
   * @return
   */
  public static BusinessException createException(HttpStatus httpStatus, BusinessErrorCodes businessErrorCodes) {
    return BusinessException.builder()
        .httpStatus(httpStatus == null ? HttpStatus.INTERNAL_SERVER_ERROR : httpStatus)
        .id(UUID.randomUUID().toString())
        .code(getErrorCode(
            ErrorConstant.Type.SYSTEM,
            ErrorConstant.Layer.SUPPORT,
            ErrorConstant.SystemComponent.SUPPORT,
            businessErrorCodes.getCode()))
        .type(ErrorConstant.Type.SYSTEM.getDescription())
        .description(businessErrorCodes.getDescription())
        .category(ErrorConstant.Category.ERROR)
        .errors(Collections.singletonList(businessErrorCodes.getTitle()))
        .build();
  }
}
