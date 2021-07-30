package pe.com.pacifico.kuntur.repository;

import java.util.List;
import java.util.Map;
import pe.com.pacifico.kuntur.model.FiltroLinea;
import pe.com.pacifico.kuntur.model.Reporte1BolsasOficinas;
import pe.com.pacifico.kuntur.model.Reporte2Cascada;
import pe.com.pacifico.kuntur.model.Reporte3Objetos;
import pe.com.pacifico.kuntur.model.Reporte4CascadaCentros;
import pe.com.pacifico.kuntur.model.Reporte5LineaCanal;
import pe.com.pacifico.kuntur.model.Reporte6Control1;
import pe.com.pacifico.kuntur.model.Reporte7Agile1Centros;
import pe.com.pacifico.kuntur.model.Reporte8Niff;

/**
 * <b>Class</b>: ReportsJpaRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Pacifico Seguros - La Chakra <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 23, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface ReportsJpaRepository {

  List<Map<String, Object>> obtenerReporte1BolsasOficinas(int repartoTipo, int periodo);

  List<Map<String, Object>> obtenerReporte1Vacio();

  List<Reporte1BolsasOficinas> obtenerReporte1BolsasOficinasLimited(int repartoTipo, int periodo);

  List<Reporte2Cascada> obtenerReporte2Cascada(int repartoTipo, int periodo);

  List<Reporte3Objetos> obtenerReporte3Objetos(int repartoTipo, int periodo);

  List<Map<String, Object>> obtenerReporte4CascadaCentros(int repartoTipo, int periodo);

  List<Reporte4CascadaCentros> obtenerReporte4CascadaCentrosLimited(int repartoTipo, int periodo);

  List<Map<String, Object>> obtenerReporte4Vacio();

  List<Map<String, Object>> obtenerReporte5LineaCanal(int repartoTipo, int periodo, List<String> dimensiones,
                                                      List<String> lineas, List<String> canales);

  List<Reporte5LineaCanal> obtenerReporte5LineaCanalLimited(int repartoTipo, int periodo, List<String> dimensiones,
                                                            List<String> lineas, List<String> canales);

  List<Map<String, Object>> obtenerReporte5Vacio(List<String> lstFiltrosDimension, List<String> lstFiltrosLinea,
                                                 List<String> lstFiltrosCanal);

  List<Map<String, Object>> obtenerReporte6A(int repartoTipo, int periodo);

  List<Reporte6Control1> obtenerReporte6ALimited(int repartoTipo, int periodo);

  List<Map<String, Object>> obtenerReporte6B(int repartoTipo, int periodo);

  List<Map<String, Object>> obtenerReporte6AVacio();

  List<Map<String, Object>> obtenerReporte6BVacio();

  List<Map<String, Object>> obtenerReporte7A(int repartoTipo, int periodo);

  List<Reporte7Agile1Centros> obtenerReporte7ALimited(int repartoTipo, int periodo);

  List<Map<String, Object>> obtenerReporte7B(int repartoTipo, int periodo);

  List<Map<String, Object>> obtenerReporte7AVacio();

  List<Map<String, Object>> obtenerReporte7BVacio();

  List<Map<String, Object>> obtenerReporte8(int repartoTipo, int periodo);

  List<Reporte8Niff> obtenerReporte8Limited(int repartoTipo, int periodo);

  List<Map<String, Object>> obtenerReporte8Vacio();

  List<FiltroLinea> obtenerFiltroCodLineas(int repartoTipo, int periodo);

  boolean validarRepo5(int repartoTipo, int periodo,
                       List<String> lineas, List<String> canales);
}
