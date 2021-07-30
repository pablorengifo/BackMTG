package pe.com.pacifico.kuntur.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



/**
 * <b>Class</b>: CellData <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     March 31, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CellData implements Serializable {

  private static final long serialVersionUID = 1L;
  private int row;
  private int column;
  private String value;
  private String format;

}

