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
 * <b>Class</b>: Reporte6_Control1 <br/>
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
public class Reporte6Control1 implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "CodCentro")
  private String codCentro;
  @Id
  @Column(name = "CentroNombre")
  private String centroNombre;
  @Id
  @Column(name = "CentroTipo")
  private String centroTipo;
  @Column(name = "MontoTablaInput")
  private double montoTablaInput;
  @Column(name = "MontoTablaBolsas")
  private double montoTablaBolsas;
  @Column(name = "MontoTablaCascadaF")
  private double montoTablaCascadaF;
  @Column(name = "MontoTablaObjetos")
  private double montoTablaObjetos;
}
/*
* private String codCentro;
    private String centroNombre;
    private String centroTipo;
    private double montoTablaInput;
    private double montoTablaBolsas;
    private double montoTablaCascadaF;
    private double montoTablaObjetos;
* */
