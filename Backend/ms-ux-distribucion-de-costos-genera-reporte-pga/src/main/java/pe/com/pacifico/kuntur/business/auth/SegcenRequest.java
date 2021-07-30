package pe.com.pacifico.kuntur.business.auth;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.concurrent.TimeUnit;

/**
 * <b>Class</b>: SegcenRequest <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 08, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public class SegcenRequest {

  private static String suscriptionKey = "3a9979c5f1b04dc79db8c54e35ed9aa3";
  private static String appName = "MOTDTG";

  /**
   * This method is used to obtainAuthRequest.
   * @return answer.
   */
  public String obtainAuthRequest(String bearer) {
    String answer = "";
    OkHttpClient client = new OkHttpClient();
    client.setConnectTimeout(30, TimeUnit.SECONDS);
    client.setReadTimeout(30, TimeUnit.SECONDS);
    client.setWriteTimeout(30, TimeUnit.SECONDS);
    Request request = new Request.Builder()
        .header("Authorization", bearer)
        .header("Ocp-Apim-Subscription-Key", suscriptionKey)
        .header("nombreAplicacion", appName)
        .url("https://apimgmt-pacificodesa.azure-api.net/ux-consulta-permisos-mdg/segcen/v1/api/aplicaciones/1/0/0/1")
        .build();
    try {
      Response response = client.newCall(request).execute();
      String body = response.body().string();
      System.out.println("Body is: " + body);
      answer = body;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return answer;
  }

}
