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
 * <b>Class</b>: MovUoaPaa <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 09, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */

@Entity(name = "MOV_UOA_PAA")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class MovUoaPaa implements Serializable {

  @Id
  @Column(length = 10, name = "CodProducto")
  private String codProducto;

  private String nombreProducto;

  @Id
  @Column(length = 50, name = "UnidadCuenta")
  private String unidadCuenta;

  @Column(name = "Porcentaje")
  private double porcentaje;

  @Column(name = "FechaCreacion")
  private Date fechaCreacion;

  @Column(name = "FechaActualizacion")
  private Date fechaActualizacion;

  @Id
  @Column(name = "Periodo")
  private int periodo;

  @Id
  @Column(name = "RepartoTipo")
  private int repartoTipo;

}
