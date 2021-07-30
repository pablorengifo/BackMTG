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
 * <b>Class</b>: MOV_DRIVER_CENTRO <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 22, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */

@Entity(name = "MOV_DRIVER_CENTRO")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class MovDriverCentro implements Serializable {

  @Id
  @Column(length = 15, name = "CodDriverCentro")
  private String codDriverCentro;

  private String nombreDriver;

  @Id
  @Column(length = 8, name = "codCentroDestino")
  private String codCentroDestino;

  private String nombreCentro;

  @Column(name = "Porcentaje")
  private double porcentaje;

  @Column(name = "FechaCreacion")
  private Date fechaCreacion;

  @Column(name = "FechaActualizacion")
  private Date fechaActualizacion;

  @Column(name = "Periodo")
  private int periodo;

  @Column(name = "RepartoTipo")
  private int repartoTipo;

}
