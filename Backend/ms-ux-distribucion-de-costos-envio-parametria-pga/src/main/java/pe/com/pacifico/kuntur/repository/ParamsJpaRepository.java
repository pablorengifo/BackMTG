package pe.com.pacifico.kuntur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.pacifico.kuntur.model.Params;

/**
 * <b>Class</b>: ParamsJpaRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Pacifico Seguros - La Chakra <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 26, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface ParamsJpaRepository extends JpaRepository<Params, Long>  {

}
