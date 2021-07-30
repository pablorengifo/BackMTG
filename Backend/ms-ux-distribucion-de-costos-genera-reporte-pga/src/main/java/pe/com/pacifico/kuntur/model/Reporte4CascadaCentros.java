package pe.com.pacifico.kuntur.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class</b>: Reporte4_CascadaCentros <br/>
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
@Entity(name = "REPORTE_CASCADA_R_HST")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Reporte4CascadaCentros implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "Periodo")
  private int periodo;
  @Id
  @Column(name = "ITERACION")
  private int iteracion;
  @Id
  @Column(name = "CodCentroDestino")
  private String codCentroDestino;
  @Column(name = "CentroDestinoNombre")
  private String centroDestinoNombre;
  @Id
  @Column(name = "centroDestinoNivel")
  private int centroDestinoNivel;
  @Id
  @Column(name = "CentroDestinoTipo")
  private String centroDestinoTipo;
  @Id
  @Column(name = "CodCentroOrigen")
  private String codCentroOrigen;
  @Column(name = "CentroOrigenNombre")
  private String centroOrigenNombre;
  @Id
  @Column(name = "CentroOrigenNivel")
  private int centroOrigenNivel;
  @Id
  @Column(name = "CentroOrigenTipo")
  private String centroOrigenTipo;
  @Id
  @Column(name = "TIPO")
  private String tipo;
  @Column(name = "MONTO")
  private double monto;
}
