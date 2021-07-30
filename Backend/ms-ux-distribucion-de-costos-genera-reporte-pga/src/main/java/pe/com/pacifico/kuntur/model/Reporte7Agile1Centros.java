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
 * <b>Class</b>: Reporte7_Agile1Centros <br/>
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
public class Reporte7Agile1Centros implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "Iteracion")
  private int iteracion;

  @Id
  @Column(name = "CodCentroDestino")
  private String codCentroDestino;
  @Column(name = "CentroDestinoNombre")
  private String centroDestinoNombre;
  @Column(name = "CentroDestinoGrupo")
  private String centroDestinoGrupo;
  @Column(name = "CentroDestinoNivel")
  private int centroDestinoNivel;
  @Column(name = "CentroDestinoTipo")
  private String centroDestinoTipo;

  @Id
  @Column(name = "CodCentroOrigen")
  private String codCentroOrigen;
  @Column(name = "CentroOrigenNombre")
  private String centroOrigenNombre;
  @Column(name = "CentroOrigenGrupo")
  private String centroOrigenGrupo;
  @Column(name = "CentroOrigenNivel")
  private int centroOrigenNivel;
  @Column(name = "CentroOrigenTipo")
  private String centroOrigenTipo;

  @Column(name = "CodCentroPadreOrigen")
  private String codCentroPadreOrigen;
  @Column(name = "CentroPadreOrigenNombre")
  private String centroPadreOrigenNombre;
  @Column(name = "CodCentroPadreDestino")
  private String codCentroPadreDestino;
  @Column(name = "CentroPadreDestinoNombre")
  private String centroPadreDestinoNombre;

  @Column(name = "Tipo")
  private String tipo;
  @Column(name = "TipoGasto")
  private String tipoGasto;
  @Column(name = "Monto")
  private double monto;

}
/*private int iteracion;

    private String codCentroDestino;
    private String centroDestinoNombre;
    private String centroDestinoGrupo;
    private int centroDestinoNivel;
    private String centroDestinoTipo;

    private String codCentroOrigen;
    private String centroOrigenNombre;
    private String centroOrigenGrupo;
    private int centroOrigenNivel;
    private String centroOrigenTipo;

    private String codCentroPadreOrigen;
    private String centroPadreOrigenNombre;
    private String codCentroPadreDestino;
    private String centroPadreDestinoNombre;

    private String tipo;
    private String tipoGasto;
    private double monto;*/
