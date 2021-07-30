package pe.com.pacifico.kuntur.business.auth;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SegcenInterceptor implements HandlerInterceptor {

  private final SegcenService service;

  @Override
  public boolean preHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler) {
    // your code
    System.out.println("Methods: " + request.getMethod());
    if (request.getMethod().equalsIgnoreCase("options")
            || request.getMethod().equalsIgnoreCase("get")) {
      return true;
    } else {
      String token = "";
      final String authorizationHeaderValue = request.getHeader("Authorization");
      if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
        token = authorizationHeaderValue;
      } else {
        return false;
      }
      String route = request.getServletPath();
      System.out.println("Mapping is: ....................");
      System.out.println(route);
      System.out.println("Token is: ....................");
      System.out.println(token);
      System.out.println("End of token .................");
      //SegcenRequest.generateAuthRequest(token);
      //SegcenService service = new SegcenService();
      boolean answer = service.checkAuth(new SegcenRequest(), route, token);
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


