package pe.com.pacifico.kuntur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.pacifico.kuntur.model.Ejecuciones;

/**
 * <b>Class</b>: EjecucionesJpaRepository <br/>
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
public interface EjecucionesJpaRepository extends JpaRepository<Ejecuciones, Long>  {

}
