package pe.com.pacifico.kuntur.business.auth;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * <b>Class</b>: SegcenInterceptor <br/>
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
@Slf4j
@RequiredArgsConstructor
@Component
public class SegcenInterceptor implements HandlerInterceptor {

  @Value("${api-validation.mode:server}")
  private String validationMode;

  @Override
  public boolean preHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler) {
    // your code
    System.out.println("Methods: " + request.getMethod());
    if (request.getMethod().equalsIgnoreCase("options")
        || request.getMethod().equalsIgnoreCase("get")) {
      System.out.println("Get or options, ignoring validation.");
      return true;
    } else {
      String token = "";
      System.out.println("Getting token from header 'token'");
      final String authorizationHeaderValue = request.getHeader("Token");
      System.out.println("porque falls te odio!!");
      System.out.println(authorizationHeaderValue);
      if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
        System.out.println("Token OK");
        token = authorizationHeaderValue;
      } else {
        System.out.println("Token error.");
        return false;
      }
      String route = request.getServletPath();

      SegcenService service = new SegcenService();
      boolean answer;
      if (validationMode.equals("server")) {
        answer = service.checkAuthWithSvc(route, token);
      } else { // Do localhost with api management
        answer = service.checkAuthWithApiManagement(route, token);
      }

      if (!answer) {
        try {
          response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      return answer;
    }
  }
}


