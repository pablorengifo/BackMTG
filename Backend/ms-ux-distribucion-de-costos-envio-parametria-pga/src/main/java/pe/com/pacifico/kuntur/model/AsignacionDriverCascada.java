package pe.com.pacifico.kuntur.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <b>Class</b>: AsignacionDriverCascada <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 30, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Entity(name = "ASIGNACION_CASCADA")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class AsignacionDriverCascada {
  @Id
  @Column(length = 8, name = "CodCentro")
  private String codCentro;

  @Column(length = 10, name = "CodDriver")
  private String codDriver;

  @Column(name = "FechaCreacion")
  private Date fechaCreacion;

  @Column(name = "FechaActualizacion")
  private Date fechaActualizacion;

  @Column(name = "RepartoTipo")
  private int repartoTipo;

  @Column(name = "Periodo")
  private int periodo;

  @Column(name = "NombreCentro")
  private String nombreCentro;

  @Column(name = "NombreDriver")
  private String nombreDriver;


}
