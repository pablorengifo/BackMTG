package pe.com.pacifico.kuntur.model;

import java.io.Serializable;
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
 * <b>Class</b>: Reporte1_BolsasOficinas <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 10, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Entity(name = "REPORTE_BOLSA_OFICINA_R_HST")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Reporte1BolsasOficinas implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "Asignacion")
  private String asignacion;

  @Column(name = "CentroDestinoNivel")
  private int centroDestinoNivel;

  @Column(name = "CentroDestinoNombre")
  private String centroDestinoNombre;

  @Column(name = "CentroDestinoTipo")
  private String centroDestinoTipo;

  @Column(name = "CentroOrigenNivel")
  private int centroOrigenNivel;

  @Column(name = "CentroOrigenNombre")
  private String centroOrigenNombre;

  @Column(name = "CentroOrigenTipo")
  private String centroOrigenTipo;

  @Id
  @Column(name = "CodCentroDestino")
  private String codCentroDestino;

  @Id
  @Column(name = "CodCentroOrigen")
  private String codCentroOrigen;

  @Id
  @Column(name = "CodCuentaContableOrigen")
  private String codCuentaContableOrigen;

  @Id
  @Column(name = "CodDriver")
  private String codDriver;

  @Id
  @Column(name = "CodPartidaOrigen")
  private String codPartidaOrigen;

  @Column(name = "CuentaContableOrigenNombre")
  private String cuentaContableOrigenNombre;

  @Column(name = "DriverNombre")
  private String driverNombre;

  @Column(name = "Monto")
  private double monto;

  @Column(name = "PartidaOrigenNombre")
  private String partidaOrigenNombre;

  @Column(name = "Periodo")
  private int periodo;

  @Id
  @Column(name = "CodDocumentoClienteOrigen")
  private String codDocumentoClienteOrigen;

  @Column(name = "ClienteOrigen")
  private String clienteOrigen;

  @Column(name = "TipoDocumentoClienteOrigen")
  private String tipoDocumentoClienteOrigen;

}
