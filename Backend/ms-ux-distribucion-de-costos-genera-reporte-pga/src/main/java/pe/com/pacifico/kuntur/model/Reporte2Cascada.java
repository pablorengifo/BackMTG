package pe.com.pacifico.kuntur.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class</b>: Reporte2Cascada <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     June 3, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Reporte2Cascada implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "CentroDestinoNivel")
  private int centroDestinoNivel;
  @Column(name = "CentroDestinoNombre")
  private String centroDestinoNombre;
  @Column(name = "CentroDestinoTipo")
  private String centroDestinoTipo;
  @Column(name = "CentroInicialNivel")
  private int centroInicialNivel;
  @Column(name = "CentroInicialNombre")
  private String centroInicialNombre;
  @Column(name = "CentroInicialTipo")
  private String centroInicialTipo;
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
  @Column(name = "CodCentroInicial")
  private String codCentroInicial;
  @Id
  @Column(name = "CodCentroOrigen")
  private String codCentroOrigen;
  @Id
  @Column(name = "CodCuentaContableInicial")
  private String codCuentaContableInicial;

  @Id
  @Column(name = "CodDocumentoClienteInicial")
  private String codDocumentoClienteInicial;
  @Column(name = "ClienteInicial")
  private String clienteInicial;
  @Column(name = "TipoDocumentoClienteInicial")
  private String tipoDocumentoClienteInicial;


  @Id
  @Column(name = "CodDriver")
  private String codDriver;
  @Column(name = "CodPartidaInicial")
  private String codPartidaInicial;
  @Column(name = "CuentaContableInicialNombre")
  private String cuentaContableInicialNombre;
  @Column(name = "DriverNombre")
  private String driverNombre;
  @Column(name = "Iteracion")
  private int iteracion;
  @Column(name = "Monto")
  private double monto;
  @Column(name = "PartidaInicialNombre")
  private String partidaInicialNombre;
  @Column(name = "Periodo")
  private int periodo;

}
/*
private int centroDestinoNivel;
    private String centroDestinoNombre;
    private String centroDestinoTipo;
    private int centroInicialNivel;
    private String centroInicialNombre;
    private String centroInicialTIpo;
    private int centroOrigenNivel;
    private String centroOrigenNombre;
    private String centroOrigenTipo;

    private String codCentroDestino;
    private String codCentroInicial;
    private String codCentroOrigen;
    private String codCuentaContableInicial;

    private String codDriver;
    private String codPartidaInicial;
    private String cuentaContableInicialNombre;
    private String driverNombre;
    private int iteracion;
    private double monto;
    private String partidaInicialNombre;
    private int periodo;
 */
