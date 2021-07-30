package pe.com.pacifico.kuntur.business.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <b>Class</b>: SegcenConfig <br/>
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
@Component
public class SegcenConfig implements WebMvcConfigurer {

  public SegcenConfig() {
    super();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new SegcenInterceptor(new SegcenService())).addPathPatterns("/**");
  }

  @Bean
  public ServerCodecConfigurer serverCodecConfigurer() {
    return ServerCodecConfigurer.create();
  }

}
