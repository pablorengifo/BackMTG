package pe.com.pacifico.kuntur.business.auth;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.impl.httpclient4.HttpTransportPropertiesImpl;
import org.springframework.stereotype.Service;
import org.tempuri.SegCenServicioStub;

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

  private final String codigoModuloParametrizacion = "000003";
  private final String codigoModuloAprovisionamiento = "000004";
  private final String codigoModuloAsignaciones = "000005";

  private final String codigoEdicion = "A00002";

  /*@Autowired
  private SegCenServicioInjI segCenService;
  */
  /**
   * This method is used to checkAuth.
   *
   * @return boolean.
   */
  public boolean checkAuthWithApiManagement(String route, String bearer) {
    String request = new SegcenRequest().obtainAuthRequest(bearer);

    if (request == null) {
      return false;
    } else {
      if (request.equals("")) {
        return false;
      }
    }

    JsonObject jsonObject = (JsonObject) JsonParser.parseString(request);
    JsonObject objectResponse = jsonObject.getAsJsonObject("objectResponse");
    JsonArray acciones = objectResponse.getAsJsonArray("Acciones");
    JsonObject[] arrAcciones = new Gson().fromJson(acciones, JsonObject[].class);

    String code = codigoModuloParametrizacion;

    if (route.toLowerCase().contains("assignments")) {
      // Asignaciones
      code = codigoModuloAsignaciones;
    } else if (route.toLowerCase().contains("drivers") || route.toLowerCase().contains("detail")
        || route.toLowerCase().contains("equivalence")) {
      // Aprovisionamiento
      code = codigoModuloAprovisionamiento;
    }

    for (JsonObject accion : arrAcciones) {
      String codigoObjeto = accion.get("CodigoObjeto").getAsString();
      String codigoAccion = accion.get("CodigoAccion").getAsString();

      if (codigoAccion.equals(codigoEdicion) && codigoObjeto.equals(code)) {
        return true;
      }
    }
    return false;
  }

  /**
   * This method is used to checkAuth.
   *
   * @return boolean.
   */
  public boolean checkAuthWithSvc(String route, String bearer) {
    String userOnPremise = new JwtDecoder().decodeJwt(bearer);

    String strPCodigoAplicacion = "MOTDTG";
    int intPMayor = 1;
    int intPMinor = 0;
    int intPVersion = 0;
    String strpip = "";
    String strPHostName = "";

    try {
      SegCenServicioStub segcen = new SegCenServicioStub();
      Options options = segcen._getServiceClient().getOptions();
      HttpTransportPropertiesImpl.Authenticator basicAuth = new HttpTransportPropertiesImpl.Authenticator();
      basicAuth.setUsername("SERMDGDTPDES");
      basicAuth.setPassword("x7UkZa56$BL");
      options.setProperty(HTTPConstants.AUTHENTICATE, basicAuth);

      SegCenServicioStub.ObtenerRolesUsuarioAplicacion asker = new SegCenServicioStub.ObtenerRolesUsuarioAplicacion();
      asker.setStrIdUsuario(userOnPremise);
      asker.setInt_pMayor(intPMayor);
      asker.setInt_pMinor(intPMinor);
      asker.setInt_pVersion(intPVersion);
      asker.setStr_pCodigoAplicacion(strPCodigoAplicacion);
      SegCenServicioStub.ObtenerRolesUsuarioAplicacionResponse response = segcen.obtenerRolesUsuarioAplicacion(asker);
      System.out.println("Reading response: ");
      for(int x = 0; x < response.getObtenerRolesUsuarioAplicacionResult().getRolBE().length; x++){
        System.out.println(">>>" + response.getObtenerRolesUsuarioAplicacionResult().getRolBE()[x].getCodigoRol() + " - " +
            response.getObtenerRolesUsuarioAplicacionResult().getRolBE()[x].getNombreRol());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }


    //ISegCenServicios iSegCenServicios = new
    /*String response = segCenService.("SERMDGDTPDES", "x7UkZa56$BL",
        strPCodigoAplicacion, intPMayor, intPMinor, intPVersion, strpip, strPHostName);
    if (response.equals("")) {
      log.info(String.format("Respuesta del SegCen: '%s'.", response));
    } else {
      log.error(String.format("Respuesta del SegCen: '%s'.", response));
    }*/
    //ArrayOfRolBE roles = segCenService.obtenerRolesUsuario(strPCodigoAplicacion, intPMayor, intPMayor, intPVersion, userOnPremise);
    //log.info("Role test: ");
    //log.info(roles.toString());
    //log.info(roles.getRolBE().toString());
    /*for (RolBE rol : roles.getRolBE()) {
      log.info("*** " + rol.getIdRol() + " | " + rol.getCodigoRol() + "|" + rol.getNombreRol());
    }*/

    return false;
  }

}
