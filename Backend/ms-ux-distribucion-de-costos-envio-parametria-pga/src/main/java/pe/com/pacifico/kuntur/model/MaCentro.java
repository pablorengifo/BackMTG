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
 * <b>Class</b>: MaCentro <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 12, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */

@Entity(name = "MA_CENTRO")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class MaCentro implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(length = 8, name = "CodCentro")
  private String codCentro;

  @Column(length = 100, name = "Nombre")
  private String nombre;

  @Column(length = 8, name = "CodCentroPadre")
  private String codCentroPadre;

  @Column(name = "EstaActivo")
  private boolean estaActivo;

  @Column(name = "FechaCreacion")
  private Date fechaCreacion;

  @Column(name = "FechaActualizacion")
  private Date fechaActualizacion;

  @Column(length = 100, name = "GrupoCeco")
  private String grupoCeco;

  @Column(length = 2, name = "Niif17Atribuible")
  private String niif17Atribuible;

  @Column(length = 2, name = "Niif17Clase")
  private String niif17Clase;

  @Column(length = 2, name = "Niif17Tipo")
  private String niif17Tipo;

  @Column(name = "Nivel")
  private int nivel;

  @Column(name = "RepartoTipo")
  private int repartoTipo;

  @Column(length = 8, name = "Tipo")
  private String tipo;

  @Column(length = 100, name = "TipoCeco")
  private String tipoCeco;

  @Column(name = "TipoGasto")
  private boolean tipoGasto;
}
