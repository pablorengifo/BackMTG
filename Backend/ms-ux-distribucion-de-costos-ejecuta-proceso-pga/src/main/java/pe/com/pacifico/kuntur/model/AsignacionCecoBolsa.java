package pe.com.pacifico.kuntur.model;

import java.io.Serializable;
import java.util.Date;
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
 * <b>Class</b>: AsignacionCecoBolsa <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 30, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */

@Entity(name = "CUENTA_PARTIDA_CENTRO_DRIVER_CENTRO")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class AsignacionCecoBolsa implements Serializable {
  @Id
  @Column(length = 15, name = "CodCuentaContable")
  public String codCuentaContable;

  private String nombreCuentaContable;

  @Id
  @Column(length = 6, name = "codPartida")
  public String codPartida;

  private String nombrePartida;

  @Id
  @Column(length = 8, name = "codCentro")
  public String codCentro;

  private String nombreCentro;

  @Id
  @Column(length = 15, name = "CodDriver")
  private String codDriver;

  private String nombreDriver;

  @Column(name = "FechaCreacion")
  private Date fechaCreacion;

  @Column(name = "FechaActualizacion")
  private Date fechaActualizacion;

  @Column(name = "Periodo")
  private int periodo;

  @Column(name = "RepartoTipo")
  private int repartoTipo;
}
