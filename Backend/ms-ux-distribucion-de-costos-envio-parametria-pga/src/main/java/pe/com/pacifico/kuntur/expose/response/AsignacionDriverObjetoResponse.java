package pe.com.pacifico.kuntur.expose.response;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class</b>: AsignacionDriverObjetoResponse <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 04, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@ApiModel(description = "AsignacionDriverObjetoResponse model")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class AsignacionDriverObjetoResponse implements Serializable {

  @Id
  @Column(length = 8, name = "CodCentro")
  public String codCentro;

  private String nombreCentro;

  @Id
  @Column(name = "RepartoTipo")
  private int repartoTipo;

  @Id
  @Column(name = "Periodo")
  private int periodo;

  @Id
  @Column(length = 2, name = "GrupoGasto")
  private String grupoGasto;

  @Column(length = 10, name = "CodDriver")
  private String codDriver;

  private String nombreDriver;

  @Column(name = "FechaCreacion")
  private Date fechaCreacion;

  @Column(name = "FechaActualizacion")
  private Date fechaActualizacion;

}
