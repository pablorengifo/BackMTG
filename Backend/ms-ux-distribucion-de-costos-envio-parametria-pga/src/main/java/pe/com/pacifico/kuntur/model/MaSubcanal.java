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
 * <b>Class</b>: MaSubcanal <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 16, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */

@Entity(name = "MA_SUBCANAL")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class MaSubcanal implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(length = 6, name = "CodSubcanal")
  private String codSubcanal;

  @Column(name = "RepartoTipo")
  private int repartoTipo;

  @Column(length = 100, name = "Nombre")
  private String nombre;

  @Column(name = "EstaActivo")
  private boolean estaActivo;

  @Column(name = "FechaCreacion")
  private Date fechaCreacion;

  @Column(name = "FechaActualizacion")
  private Date fechaActualizacion;

}
