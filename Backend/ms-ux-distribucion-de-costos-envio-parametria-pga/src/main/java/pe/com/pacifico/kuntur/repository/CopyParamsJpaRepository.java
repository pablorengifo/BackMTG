package pe.com.pacifico.kuntur.repository;

/**
 * <b>Class</b>: CopyParamsJpaRepository <br/>
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
public interface CopyParamsJpaRepository {

  boolean copyMovCuentaContable(int repartoTipo, int periodoOri, int periodoDest);

  boolean copyMovPartida(int repartoTipo, int periodoOri, int periodoDest);

  boolean copyMovCentro(int repartoTipo, int periodoOri, int periodoDest);

  boolean copyMovProducto(int repartoTipo, int periodoOri, int periodoDest);

  boolean copyMovSubcanal(int repartoTipo, int periodoOri, int periodoDest);

  boolean copyMovDriverCentro(int repartoTipo, int periodoOri, int periodoDest);

  boolean copyMovDriverObjeto(int repartoTipo, int periodoOri, int periodoDest);

  boolean copyAsignacionCecoBolsa(int repartoTipo, int periodoOri, int periodoDest);

  boolean copyAsignacionDriverCascada(int repartoTipo, int periodoOri, int periodoDest);

  boolean copyAsignacionDriverObjeto(int repartoTipo, int periodoOri, int periodoDest);

}
