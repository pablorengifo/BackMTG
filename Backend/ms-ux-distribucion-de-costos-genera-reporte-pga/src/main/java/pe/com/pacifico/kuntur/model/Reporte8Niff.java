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
 * <b>Class</b>: Reporte8_NIIF <br/>
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
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Reporte8Niff implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "CodCuentaContableInicial")
  private String codCuentaContableInicial;
  @Column(name = "CuentaContableInicialNombre")
  private String cuentaContableInicialNombre;
  @Id
  @Column(name = "CodLinea")
  private String codLinea;
  @Column(name = "LineaNombre")
  private String lineaNombre;
  @Id
  @Column(name = "CodProducto")
  private String codProducto;
  @Column(name = "ProductoNombre")
  private String productoNombre;
  @Id
  @Column(name = "CodCanal")
  private String codCanal;
  @Column(name = "CanalNombre")
  private String canalNombre;
  @Id
  @Column(name = "CodSubcanal")
  private String codSubcanal;
  @Column(name = "SubcanalNombre")
  private String subcanalNombre;
  @Column(name = "ResultadoNiif17Atribuible")
  private String resultadoNiif17Atribuible;
  @Column(name = "ResultadoNiif17Tipo")
  private String resultadoNiif17Tipo;
  @Column(name = "Niif17Clase")
  private String niif17Clase;
  @Column(name = "Monto")
  private double monto;

}
/*
private String codCuentaContableInicial;
    private String cuentaContableInicialNombre;
    private String codLinea;
    private String lineaNombre;
    private String codProducto;
    private String productoNombre;
    private String codCanal;
    private String canalNombre;
    private String codSubcanal;
    private String subcanalNombre;
    private String resultadoNiif17Atribuible;
    private String resultadoNiif17Tipo;
    private String niif17Clase;
    private double monto;
 */
