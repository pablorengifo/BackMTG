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
 * <b>Class</b>: Partida <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 7, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */

@Entity(name = "MOV_PARTIDA")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class MovPartida implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(length = 15, name = "CodPartida")
  private String codPartida;

  private String nombre;

  @Column(name = "FechaCreacion")
  private Date fechaCreacion;

  @Column(name = "FechaActualizacion")
  private Date fechaActualizacion;

  //@Id
  @Column(name = "Periodo")
  private int periodo;

  @Column(name = "RepartoTipo")
  private int repartoTipo;

  @Column(name = "Saldo", precision = 35, scale = 8)
  private float saldo;
}
