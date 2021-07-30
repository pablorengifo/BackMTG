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
 * <b>Class</b>: Reporte6_Control2 <br/>
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
public class Reporte6Control2 implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "CodCuentaContable")
  private String codCuentaContable;
  @Column(name = "CuentaContableNombre")
  private String cuentaContableNombre;
  @Id
  @Column(name = "CodPartida")
  private String codPartida;
  @Column(name = "PartidaNombre")
  private String partidaNombre;
  @Column(name = "MontoTablaInput")
  private double montoTablaInput;
  @Column(name = "MontoTablaObjetos")
  private double montoTablaObjetos;
  @Column(name = "CntCentrosTablaInput")
  private double cntCentrosTablaInput;
  @Column(name = "CntCentrosTablaObjetos")
  private double cntCentrosTablaObjetos;
}
/*
private String codCuentaContable;
    private String cuentaContableNombre;
    private String codPartida;
    private String partidaNombre;
    private double montoTablaInput;
    private double montoTablaObjetos;
    private double cntCentrosTablaInput;
    private double cntCentrosTablaObjetos;
 */
