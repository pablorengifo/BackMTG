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
import lombok.ToString;

/**
 * <b>Class</b>: CuentaContable <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     March 31, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */

@Entity(name = "MA_CUENTA_CONTABLE")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class CuentaContable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(length = 15, name = "CodCuentaContable")
  private String codCuentaContable;

  @Column(length = 100, name = "Nombre")
  private String nombre;

  @Column(name = "EstaActivo")
  private boolean estaActivo;

  @Column(name = "FechaCreacion")
  private Date fechaCreacion;

  @Column(name = "FechaActualizacion")
  private Date fechaActualizacion;

  @Column(length = 2, name = "Niif17Atribuible")
  private String niif17Atribuible;

  @Column(length = 2, name = "Niif17Clase")
  private String niif17Clase;

  @Column(length = 2, name = "Niif17Tipo")
  private String niif17Tipo;

  @Column(name = "RepartoTipo")
  private int repartoTipo;

  @Column(name = "TipoGasto")
  private boolean tipoGasto;
}
