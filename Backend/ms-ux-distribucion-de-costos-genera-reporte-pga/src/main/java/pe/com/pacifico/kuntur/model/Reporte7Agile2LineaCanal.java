package pe.com.pacifico.kuntur.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class</b>: Reporte7_Agile2LineaCanal <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 10, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Reporte7Agile2LineaCanal implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "CodCentroInicial")
  private String codCentroInicial;
  @Column(name = "CodCentroPadreInicial")
  private String codCentroPadreInicial;
  @Column(name = "NombreCentroPadreInicial")
  private String nombreCentroPadreInicial;
  @Column(name = "TipoGasto")
  private int tipoGasto;
  @Id
  @Column(name = "CodLinea")
  private String codLinea;
  @Column(name = "LineaNombre")
  private String lineaNombre;
  @Id
  @Column(name = "CodCanal")
  private String codCanal;
  @Column(name = "CanalNombre")
  private String canalNombre;
  @Column(name = "Monto")
  private double monto;

}
/* private String codCentroInicial;
    private String codCentroPadreInicial;
    private String nombreCentroPadreInicial;
    private int tipoGasto;
    private String codLinea;
    private String lineaNombre;
    private String codCanal;
    private String canalNombre;
    private double monto;*/
