package pe.com.pacifico.kuntur.business.auth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Base64;

/**
 * <b>Class</b>: JwtDecoder <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     Julio 21, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public class JwtDecoder {

  /**
   * This method is used to decode.
   *
   * @param bearer This is the first parameter to method.
   * @return user.
   */
  public String decodeJwt(String bearer) {
    String user = null;
    String token = bearer.replaceAll("Bearer ", "");

    String[] chunks = token.split("\\.");
    Base64.Decoder decoder = Base64.getDecoder();

    //String header = new String(decoder.decode(chunks[0]));
    String payload = new String(decoder.decode(chunks[1]));

    JsonObject jsonObject = (JsonObject) JsonParser.parseString(payload);
    String usuarioOnPremise = jsonObject.get("onpremisesuserprincipalname").getAsString();
    user = usuarioOnPremise.split("@")[0];

    return user;
  }

}
