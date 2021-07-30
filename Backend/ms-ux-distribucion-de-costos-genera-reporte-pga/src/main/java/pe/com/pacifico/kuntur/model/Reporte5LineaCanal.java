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
 * <b>Class</b>: Reporte5_LineaCanal <br/>
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
@Entity(name = "REPORTE_OBJETO_R_HST")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Reporte5LineaCanal implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "CodCuentaContableInicial")
  private String codCuentaContableInicial;
  @Column(name = "CuentaContableInicialNombre")
  private String cuentaContableInicialNombre;

  @Id
  @Column(name = "CodPartidaInicial")
  private String codPartidaInicial;
  @Column(name = "PartidaInicialNombre")
  private String partidaInicialNombre;

  @Id
  @Column(name = "CodCentroInicial")
  private String codCentroInicial;
  @Column(name = "CentroInicialNombre")
  private String centroInicialNombre;

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