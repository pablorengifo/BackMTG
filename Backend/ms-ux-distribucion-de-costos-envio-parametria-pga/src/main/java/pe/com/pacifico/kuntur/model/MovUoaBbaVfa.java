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
 * <b>Class</b>: MovUoaBbaVfa <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 14, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */

@Entity(name = "MOV_UOA_BBA_VFA")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class MovUoaBbaVfa implements Serializable {

  @Id
  @Column(length = 50, name = "IdPoliza")
  private String idPoliza;

  @Column(length = 50, name = "NumeroPoliza")
  private String numeroPoliza;

  @Column(length = 50, name = "CodigoUoAPacifico")
  private String codigoUoAPacifico;

  @Id
  @Column(length = 50, name = "DescripcionUoA")
  private String descripcionUoA;

  @Column(length = 25, name = "CentroCosto")
  private String centroCosto;

  @Column(length = 2, name = "LineaNegocio")
  private String lineaNegocio;

  @Id
  @Column(length = 50, name = "ProductoPPTO")
  private String productoPpto;

  @Column(length = 50, name = "CanalDistribucion")
  private String canalDistribucion;

  @Id
  @Column(length = 50, name = "SubCanal")
  private String subCanal;

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
