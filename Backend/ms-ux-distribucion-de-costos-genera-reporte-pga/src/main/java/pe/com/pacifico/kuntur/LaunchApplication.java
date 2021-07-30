package pe.com.pacifico.kuntur;

import com.pacifico.kuntur.core.launcher.ApplicationLauncher;
import com.pacifico.kuntur.core.starter.web.runner.StarterWebApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <b>Class</b>: LaunchApplication <br/>
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
@SpringBootApplication
@ApplicationLauncher
public class LaunchApplication extends StarterWebApplication {

  public static void main(String[] args) {
    SpringApplication.run(LaunchApplication.class, args);
  }

}
