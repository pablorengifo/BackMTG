package pe.com.pacifico.kuntur.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;



/**
 * <b>Class</b>: MovCentro <br/>
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

@Entity(name = "MOV_CENTRO")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class MovCentro implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(length = 8, name = "CodCentro")
  private String codCentro;

  private String nombre;
  private String tipo;

  @Column(length = 8, name = "CodCentroOrigen")
  private String codCentroOrigen;

  @Column(length = 15, name = "CodCuentaContableOrigen")
  private String codCuentaContableOrigen;

  @Column(length = 10, name = "CodEntidadOrigen")
  private String codEntidadOrigen;

  @Column(length = 6, name = "CodPartidaOrigen")
  private String codPartidaOrigen;

  @Column(name = "FechaCreacion")
  private Date fechaCreacion;

  @Column(name = "FechaActualizacion")
  private Date fechaActualizacion;

  @Column(length = 6, name = "GrupoGasto")
  private String grupoGasto;

  @Column(name = "Iteracion")
  private int iteracion;

  //@Id
  @Column(name = "Periodo")
  private int periodo;

  @Column(name = "RepartoTipo")
  private int repartoTipo;

  @Column(name = "Saldo", precision = 35, scale = 8)
  private float saldo;

  public String getCodCentro() {
    return this.codCentro;
  }

  public String getNombre() {
    return this.nombre;
  }

  public String getTipo() {
    return this.tipo;
  }

  public String getCodCentroOrigen() {
    return this.codCentroOrigen;
  }

  public String getCodCuentaContableOrigen() {
    return this.codCuentaContableOrigen;
  }

  public String getCodEntidadOrigen() {
    return this.codEntidadOrigen;
  }

  public String getCodPartidaOrigen() {
    return this.codPartidaOrigen;
  }

  public Date getFechaCreacion() {
    return this.fechaCreacion;
  }

  public Date getFechaActualizacion() {
    return this.fechaActualizacion;
  }

  public String getGrupoGasto() {
    return this.grupoGasto;
  }

  public int getIteracion() {
    return this.iteracion;
  }

  public int getPeriodo() {
    return this.periodo;
  }

  public int getRepartoTipo() {
    return this.repartoTipo;
  }

  public float getSaldo() {
    return this.saldo;
  }

  public void setCodCentro(String codCentro) {
    this.codCentro = codCentro;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public void setCodCentroOrigen(String codCentroOrigen) {
    this.codCentroOrigen = codCentroOrigen;
  }

  public void setCodCuentaContableOrigen(String codCuentaContableOrigen) {
    this.codCuentaContableOrigen = codCuentaContableOrigen;
  }

  public void setCodEntidadOrigen(String codEntidadOrigen) {
    this.codEntidadOrigen = codEntidadOrigen;
  }

  public void setCodPartidaOrigen(String codPartidaOrigen) {
    this.codPartidaOrigen = codPartidaOrigen;
  }

  public void setFechaCreacion(Date fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public void setFechaActualizacion(Date fechaActualizacion) {
    this.fechaActualizacion = fechaActualizacion;
  }

  public void setGrupoGasto(String grupoGasto) {
    this.grupoGasto = grupoGasto;
  }

  public void setIteracion(int iteracion) {
    this.iteracion = iteracion;
  }

  public void setPeriodo(int periodo) {
    this.periodo = periodo;
  }

  public void setRepartoTipo(int repartoTipo) {
    this.repartoTipo = repartoTipo;
  }

  public void setSaldo(float saldo) {
    this.saldo = saldo;
  }
}
