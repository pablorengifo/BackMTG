package pe.com.pacifico.kuntur.business.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <b>Class</b>: BusinessErrorCodes <br/>
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
@Getter
@AllArgsConstructor
public enum BusinessErrorCodes {
  BUSINESS_ERROR("001", "BUSINESS_ERROR","ERROR DE NEGOCIO: %s");

  private String code;
  private String title;
  private String description;
}
