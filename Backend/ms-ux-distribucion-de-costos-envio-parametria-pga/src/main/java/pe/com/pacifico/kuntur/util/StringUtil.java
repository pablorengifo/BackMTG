package pe.com.pacifico.kuntur.util;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * <b>Class</b>: StringUtil <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Pacifico Seguros - La Chakra <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     March 23, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface StringUtil {
  /**
   * remove Diacritics.
   *
   * @param s the name text
   * @return the string clean
   */
  static String removeDiacritics(String s) {
    s = Normalizer.normalize(s, Normalizer.Form.NFD);
    s = StringUtils.stripAccents(s);
    s = s.replaceAll("\\W", "");
    return s;
  }

  static String convertCurrentDateToText() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    return dateFormat.format(new Date());
  }
}
