package pe.com.pacifico.kuntur.business;

import java.util.List;

import org.springframework.core.io.Resource;
import pe.com.pacifico.kuntur.expose.response.FiltroLineaResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte1BolsasOficinasResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte2CascadaResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte3ObjetosResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte4CascadaCentrosResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte5LineaCanalResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte6Control1Response;
import pe.com.pacifico.kuntur.expose.response.Reporte7Agile1CentrosResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte8NiifResponse;
import pe.com.pacifico.kuntur.model.Reports;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <b>Class</b>: ReportsService <br/>
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
public interface ReportsService {

  Flux<Reports> getReports();

  Mono<Reports> getReports(Long id);

  boolean getReporte(int reporte, int repartoTipo, int periodo);

  boolean getReporte5(int repartoTipo, int periodo, List<String> dimensiones, List<String> lineas, List<String> canales);

  Resource generateReporte1(int repartoTipo, int periodo);

  Flux<Reporte1BolsasOficinasResponse> getReporte1Limited(int repartoTipo, int periodo);

  Flux<Reporte2CascadaResponse> getReporte2(int repartoTipo, int periodo);

  Flux<Reporte3ObjetosResponse> getReporte3(int repartoTipo, int periodo);

  Resource generateReporte4(int repartoTipo, int periodo);

  Flux<Reporte4CascadaCentrosResponse> getReporte4Limited(int repartoTipo, int periodo);

  Resource generateReporte5(int repartoTipo, int periodo, List<String> dimensiones,
                       List<String> lineas, List<String> canales);

  Flux<Reporte5LineaCanalResponse> getReporte5Limited(int repartoTipo, int periodo, List<String> dimensiones,
                                                      List<String> lineas, List<String> canales);

  Resource generateReporte6(int repartoTipo, int periodo);

  Flux<Reporte6Control1Response> getReporte6Limited(int repartoTipo, int periodo);

  Resource generateReporte7(int repartoTipo, int periodo);

  Flux<Reporte7Agile1CentrosResponse> getReporte7Limited(int repartoTipo, int periodo);

  Resource generateReporte8(int repartoTipo, int periodo);

  Flux<Reporte8NiifResponse> getReporte8Limited(int repartoTipo, int periodo);

  Flux<FiltroLineaResponse> getFiltroCodLinea(int repartoTipo, int periodo);

}
