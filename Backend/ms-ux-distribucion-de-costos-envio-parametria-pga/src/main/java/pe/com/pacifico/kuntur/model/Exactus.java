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
 * <b>Class</b>: Detalle Gasto <br/>
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
@Entity(name = "EXACTUS")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Exactus implements Serializable {

  @Id
  @Column(length = 15, name = "CodCuentaContable")
  private String codCuentaContable;

  private String nombreCuentaContable;

  @Id
  @Column(length = 6, name = "codPartida")
  private String codPartida;

  private String nombrePartida;

  @Id
  @Column(length = 8, name = "codCentro")
  private String codCentro;

  private String nombreCentro;

  @Column(name = "TipoDocumentoCliente")
  private String tipoDocumentoCliente;

  @Column(name = "CodDocumentoCliente")
  private String codDocumentoCliente;

  @Column(name = "Cliente")
  private String cliente;

  @Column(name = "Monto")
  private double monto;

  @Id
  @Column(name = "RepartoTipo")
  private int repartoTipo;

  @Id
  @Column(name = "Periodo")
  private int periodo;

  @Column(name = "Asiento")
  private String asiento;

  @Column(name = "TipoReferenciaDocumento")
  private String tipoReferenciaDocumento;

  @Column(name = "DocumentoContabilizado")
  private String documentoContabilizado;

  @Column(name = "Referencia")
  private String referencia;

  @Column(name = "FechaContable")
  private Date fechaContable;

  @Column(name = "OrigenNegocio")
  private String origenNegocio;

  @Column(name = "Moneda")
  private String moneda;

  @Column(name = "MontoMonedaOrigen")
  private double montoMonedaOrigen;

  @Column(name = "TipoMovimiento")
  private String tipoMovimiento;

  @Column(name = "FechaCreacion")
  private Date fechaCreacion;

}
