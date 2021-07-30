package pe.com.pacifico.kuntur.business.auth;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <b>Class</b>: SegcenService <br/>
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
@Service
@Slf4j
@RequiredArgsConstructor
public class SegcenService {

  private final String codigoModuloProcesos = "000006";

  private final String codigoEdicion = "A00002";

  /**
   * This method is used to checkAuth.
   * @return boolean.
   */
  public boolean checkAuth(SegcenRequest segcenRequest,String route, String bearer) {
    // Excepción para metodo POST que es solo obtener fecha/usuario que ejecutó.
    if (route.contains("date")) {
      return true;
    }

    String request = segcenRequest.obtainAuthRequest(bearer);

    if (request == null) {
      return false;
    } else {
      if (request.equals("")) { return false; }
    }

    JsonObject jsonObject = (JsonObject) JsonParser.parseString(request);
    JsonObject objectResponse = jsonObject.getAsJsonObject("objectResponse");
    JsonArray acciones = objectResponse.getAsJsonArray("Acciones");
    JsonObject[] arrAcciones = new Gson().fromJson(acciones, JsonObject[].class);

    String code = codigoModuloProcesos;

    for (JsonObject accion : arrAcciones) {
      String codigoObjeto = accion.get("CodigoObjeto").getAsString();
      String codigoAccion = accion.get("CodigoAccion").getAsString();

      if (codigoAccion.equals(codigoEdicion) && codigoObjeto.equals(code)) {
        return true;
      }
    }
    return false;
  }

}
