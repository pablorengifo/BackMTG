package pe.com.pacifico.kuntur.business;

/**
 * <b>Class</b>: CopyParamsService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 14, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface CopyParamsService {

  boolean copyParams(int repartoTipo, int periodoOri, int periodoDest);

}
