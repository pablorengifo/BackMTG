package pe.com.pacifico.kuntur.webclient.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <b>Class</b>: WebClientErrorCodes <br/>
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
public enum WebClientErrorCodes {
  WEBCLIENT_ERROR("001", "WEBCLIENT_ERROR","ERROR EN CONSUMO API EXTERNA: %s");

  private String code;
  private String title;
  private String description;
}
