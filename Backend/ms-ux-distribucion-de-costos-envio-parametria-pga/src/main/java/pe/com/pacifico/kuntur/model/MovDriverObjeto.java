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


/**
 * <b>Class</b>: MOV_DRIVER_OBJETO <br/>
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
@Entity(name = "MOV_DRIVER_OBJETO")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class MovDriverObjeto implements Serializable {

  @Id
  @Column(length = 10, name = "CodDriverObjeto")
  private String codDriverObjeto;

  @Column(name = "NombreDriver")
  private String nombreDriver;

  @Column(name = "RepartoTipo")
  private int repartoTipo;

  @Column(name = "Periodo")
  private int periodo;

  @Column(length = 10, name = "CodProducto")
  private String codProducto;

  @Column(length = 10, name = "CodSubcanal")
  private String codSubcanal;

  @Column(name = "FechaCreacion")
  private Date fechaCreacion;

  @Column(name = "FechaActualizacion")
  private Date fechaActualizacion;

  @Column(name = "Porcentaje")
  private double porcentaje;

  @Column(name = "NombreSubcanal")
  private String nombreSubcanal;

  @Column(name = "NombreProducto")
  private String nombreProducto;
}
