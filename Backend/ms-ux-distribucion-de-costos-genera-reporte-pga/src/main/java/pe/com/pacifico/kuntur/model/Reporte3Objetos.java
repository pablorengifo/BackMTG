package pe.com.pacifico.kuntur.model;

import java.io.Serializable;
import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class</b>: Reporte3Objetos <br/>
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
public class Reporte3Objetos implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "asignacion")
  private String asignacion;
  @Column(name = "CanalNombre")
  private String canalNombre;
  @Column(name = "CcDestinoNiif17Atribuible")
  private String ccDestinoNiif17Atribuible;
  @Column(name = "CcInicialNiif17Atribuible")
  private String ccInicialNiif17Atribuible;
  @Column(name = "CentroDestinoNivel")
  private int centroDestinoNivel;
  @Column(name = "CentroDestinoNombre")
  private String centroDestinoNombre;
  @Column(name = "CentroDestinoTipo")
  private String centroDestinoTipo;
  @Column(name = "CentroInicialGrupo")
  private String centroInicialGrupo;
  @Column(name = "CentroInicialNivel")
  private int centroInicialNivel;
  @Column(name = "CentroInicialNombre")
  private String centroInicialNombre;
  @Column(name = "CentroInicialTipo")
  private String centroInicialTipo;
  @Column(name = "CodCanal")
  private String codCanal;
  @Column(name = "CodCentroDestino")
  private String codCentroDestino;
  @Column(name = "CodCentroInicial")
  private String codCentroInicial;
  @Column(name = "CodCuentaContableInicial")
  private String codCuentaContableInicial;
  @Column(name = "CodDriver")
  private String codDriver;
  @Column(name = "CodLinea")
  private String codLinea;
  @Column(name = "CodPartidaInicial")
  private String codPartidaInicial;
  @Column(name = "CodProducto")
  private String codProducto;
  @Column(name = "CodSubcanal")
  private String codSubcanal;
  @Column(name = "CtaContableNiif17Atribuible")
  private String ctaContableNiif17Atribuible;
  @Column(name = "CuentaContableInicialNombre")
  private String cuentaContableInicialNombre;
  @Column(name = "DriverNombre")
  private String driverNombre;
  @Column(name = "GrupoGasto")
  private String grupoGasto;
  @Column(name = "LineaNombre")
  private String lineaNombre;
  @Column(name = "Monto")
  private double monto;
  @Column(name = "PartidaInicialNombre")
  private String partidaInicialNombre;
  @Column(name = "Periodo")
  private int periodo;
  @Column(name = "ProductoNombre")
  private String productoNombre;
  @Column(name = "ResultadoNiif17Atribuible")
  private String resultadoNiif17Atribuible;
  @Column(name = "ResultadoNiif17Tipo")
  private String resultadoNiif17Tipo;
  @Column(name = "SubcanalNombre")
  private String subcanalNombre;
  @Column(name = "Tipo")
  private String tipo;
  @Column(name = "TipoGasto")
  private String tipoGasto;
  @Column(name = "TipoNegocio")
  private String tipoNegocio;
  @Column(name = "CodDocumentoClienteInicial")
  private String codDocumentoClienteInicial;
  @Column(name = "ClienteInicial")
  private String clienteInicial;
  @Column(name = "TipoDocumentoClienteInicial")
  private String tipoDocumentoClienteInicial;
  @Column(name = "Asiento")
  private String asiento;
  @Column(name = "TipoReferenciaDocumento")
  private String tipoReferenciaDocumento;
  @Column(name = "DocumentoContabilizado")
  private String documentoContabilizado;
  @Column(name = "Referencia")
  private String referencia;
  @Column(name = "FechaContable")
  private String fechaContable;
  @Column(name = "OrigenNegocio")
  private String origenNegocio;
  @Column(name = "Moneda")
  private String moneda;
  @Column(name = "MontoMonedaOrigen")
  private double montoMonedaOrigen;
  @Column(name = "TipoMovimiento")
  private String tipoMovimiento;
}

/*
    private String asignacion;
    private String canalNombre;
    private String ccDestinoNiif17Atribuible;
    private String ccInicialNiif17Atribuible;
    private int centroDestinoNivel;
    private String centroDestinoNombre;
    private String centroDestinoTipo;
    private String centroInicialGrupo;
    private int centroInicialNivel;
    private String centroInicialNombre;
    private String centroInicialTipo;
    private String codCanal;
    private String codCentroDestino;
    private String codCentroInicial;
    private String codCuentaContableInicial;
    private String codDriver;
    private String codLinea;
    private String codPartidaInicial;
    private String codProducto;
    private String codSubcanal;
    private String ctaContableNiif17Atribuible;
    private String cuentaContableInicialNombre;
    private String driverNombre;
    private String grupoGasto;
    private String lineaNombre;
    private double monto;
    private String partidaInicialNombre;
    private int periodo;
    private String productoNombre;
    private String resultadoNiif17Atribuible;
    private String resultadoNiif17Tipo;
    private String subcanalNombre;
    private String tipo;
    private String tipoGasto;
    private String tipoNegocio;
 */
