package pe.com.pacifico.kuntur.expose;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.swagger.annotations.ApiOperation;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pe.com.pacifico.kuntur.business.ReportsService;
import pe.com.pacifico.kuntur.business.threads.ReportesHilo;
import pe.com.pacifico.kuntur.expose.response.FiltroLineaResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte1BolsasOficinasResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte2CascadaResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte3ObjetosResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte4CascadaCentrosResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte5LineaCanalResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte6Control1Response;
import pe.com.pacifico.kuntur.expose.response.Reporte7Agile1CentrosResponse;
import pe.com.pacifico.kuntur.expose.response.Reporte8NiifResponse;
import pe.com.pacifico.kuntur.model.Reporte1BolsasOficinas;
import pe.com.pacifico.kuntur.model.Reporte2Cascada;
import pe.com.pacifico.kuntur.model.Reporte3Objetos;
import pe.com.pacifico.kuntur.model.Reporte4CascadaCentros;
import pe.com.pacifico.kuntur.model.Reporte5LineaCanal;
import pe.com.pacifico.kuntur.model.Reporte6Control1;
import pe.com.pacifico.kuntur.model.Reporte7Agile1Centros;
import pe.com.pacifico.kuntur.model.Reporte8Niff;
import reactor.core.publisher.Flux;

/**
 * <b>Class</b>: ReportsController <br/>
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
@RestController
@RequestMapping("/reports")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin
public class ReportsController {

  private final ReportsService reportsService;

  // **** Reportes ****
  /**
   * This method is used to get the report state.
   *
   * @return all reports.
   */

  @GetMapping("estadoReporte/{reporte}/{repartoTipo}/{periodo}")
  @ApiOperation(value = "prueba value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public boolean obtenerEstadoReporte(@PathVariable("reporte") @Valid int reporte,
                          @PathVariable("repartoTipo") @Valid int repartoTipo,
                          @PathVariable("periodo") @Valid int periodo) {
    System.out.println("Estado reporte " + reporte + " is");
    System.out.println(ReportesHilo.hasGenerationFinished(reporte, repartoTipo, periodo));
    return ReportesHilo.hasGenerationFinished(reporte, repartoTipo, periodo);
  }

  /**
   * This method is used to get the report data.
   *
   * @return all reports.
   */
  @GetMapping("obtenerReporte/{reporte}/{repartoTipo}/{periodo}")
  @ApiOperation(value = "prueba value", notes = "Endpoint example canales", response = Integer.class, code = 200)
  public ResponseEntity<?> obtenerReporte(@PathVariable("reporte") @Valid int reporte,
                                @PathVariable("repartoTipo") @Valid int repartoTipo,
                                @PathVariable("periodo") @Valid int periodo) {
    try
    {
      return ReportesHilo.returnData(reporte, repartoTipo, periodo);
    } catch (Exception e)
    {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
              "Combinacion de linea/canal no tiene resultados");
    }
  }

  // Reporte #1
  /**
   * This method is used to get report1.
   *
   * @return all reports.
   */
  @GetMapping("reporte1/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Reprte1 value", notes = "Endpoint example AsignacionCecoBolsa",
          response = Reporte1BolsasOficinas.class, code = 200)
  public boolean getReporte1(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                       @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llega reporte 1 ");
    return reportsService.getReporte(1, repartoTipo, periodo);
  }

  @GetMapping("reporte1L/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Reprte1 value", notes = "Endpoint example AsignacionCecoBolsa",
          response = Reporte1BolsasOficinas.class, code = 200)
  public Flux<Reporte1BolsasOficinasResponse> getReporte1L(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                           @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llega reporte 1 ");
    return reportsService.getReporte1Limited(repartoTipo, periodo).log();
  }

  // Reporte #2
  @GetMapping("reporte2/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Reprte2 value", notes = "Endpoint example",
          response = Reporte2Cascada.class, code = 200)
  public Flux<Reporte2CascadaResponse> getReporte2(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                   @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llega reporte 2 ");
    return reportsService.getReporte2(repartoTipo, periodo).log();
  }

  // Reporte #3
  @GetMapping("reporte3/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Reporte 3 value", notes = "Endpoint example",
          response = Reporte3Objetos.class, code = 200)
  public Flux<Reporte3ObjetosResponse> getReporte3(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                   @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llega reporte 3 ");
    return reportsService.getReporte3(repartoTipo, periodo).log();
  }

  // Reporte #4
  /**
   * This method is used to get report4.
   *
   * @return all reports.
   */
  @GetMapping("reporte4/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Reporte 4 value", notes = "Endpoint example AsignacionCecoBolsa",
          response = Reporte4CascadaCentros.class, code = 200)
  public boolean getReporte4(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                       @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llega reporte 4 ");
    return reportsService.getReporte(4, repartoTipo, periodo);
  }

  @GetMapping("reporte4L/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Reprte4 value", notes = "Endpoint example AsignacionCecoBolsa",
          response = Reporte1BolsasOficinas.class, code = 200)
  public Flux<Reporte4CascadaCentrosResponse> getReporte4L(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                           @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llega reporte 4L ");
    return reportsService.getReporte4Limited(repartoTipo, periodo).log();
  }

  // Reporte #5

  /**
   * Reporte 5.
   *
   * @return one reports.
   */
  @PostMapping("reporte5/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Reporte 5 value", notes = "Endpoint example AsignacionCecoBolsa",
          response = Reporte5LineaCanal.class, code = 200)
  public boolean getReporte5(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                       @PathVariable("periodo") @Valid int periodo,
                                       @RequestBody String json) {
    JsonObject jsonObject = (JsonObject) JsonParser.parseString(json);
    System.out.println(json);

    String[] arrDimensiones = new Gson().fromJson(jsonObject.getAsJsonArray("dimensiones"), String[].class);
    String[] arrCanales = new Gson().fromJson(jsonObject.getAsJsonArray("canales"), String[].class);
    String[] arrLineas = new Gson().fromJson(jsonObject.getAsJsonArray("lineas"), String[].class);

    List<String> dimensiones = Arrays.asList(arrDimensiones);
    List<String> canales = Arrays.asList(arrCanales);
    List<String> lineas = Arrays.asList(arrLineas);

    return reportsService.getReporte5(repartoTipo, periodo, dimensiones, lineas, canales);
  }

  /**
   * Ver reporte 5.
   *
   * @return one reports.
   */
  @PostMapping("reporte5L/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Reprte5 value", notes = "Endpoint example AsignacionCecoBolsa",
          response = Reporte1BolsasOficinas.class, code = 200)
  public Flux<Reporte5LineaCanalResponse> getReporte5L(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                       @PathVariable("periodo") @Valid int periodo,
                                                       @RequestBody String json) {
    JsonObject jsonObject = (JsonObject) JsonParser.parseString(json);
    System.out.println(json);

    String[] arrDimensiones = new Gson().fromJson(jsonObject.getAsJsonArray("dimensiones"), String[].class);
    String[] arrCanales = new Gson().fromJson(jsonObject.getAsJsonArray("canales"), String[].class);
    String[] arrLineas = new Gson().fromJson(jsonObject.getAsJsonArray("lineas"), String[].class);

    List<String> dimensiones = Arrays.asList(arrDimensiones);
    List<String> canales = Arrays.asList(arrCanales);
    List<String> lineas = Arrays.asList(arrLineas);
    System.out.println("llega reporte 5L ");
    return reportsService.getReporte5Limited(repartoTipo, periodo, dimensiones, lineas, canales).log();
  }

  // Reporte #6
  /**
   * This method is used to get report6.
   *
   * @return all reports.
   */
  @GetMapping("reporte6/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Reporte 6 value", notes = "Endpoint example Reporte 6 Parte 1",
          response = Reporte6Control1.class, code = 200)
  public boolean getReporte6(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                       @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llega reporte 6 ");
    return reportsService.getReporte(6, repartoTipo, periodo);
  }

  @GetMapping("reporte6L/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Reprte6 value", notes = "Endpoint example AsignacionCecoBolsa",
          response = Reporte1BolsasOficinas.class, code = 200)
  public Flux<Reporte6Control1Response> getReporte6L(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                     @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llega reporte 6L ");
    return reportsService.getReporte6Limited(repartoTipo, periodo).log();
  }


  // Reporte #7
  /**
   * This method is used to get report7.
   *
   * @return all reports.
   */
  @GetMapping("reporte7/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Reporte 7A value", notes = "Endpoint example Reporte 7",
          response = Reporte7Agile1Centros.class, code = 200)
  public boolean getReporte7(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                       @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llega reporte 7 ");
    return reportsService.getReporte(7, repartoTipo, periodo);
  }

  @GetMapping("reporte7L/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Reprte7 value", notes = "Endpoint example AsignacionCecoBolsa",
          response = Reporte1BolsasOficinas.class, code = 200)
  public Flux<Reporte7Agile1CentrosResponse> getReporte7L(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                          @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llega reporte 7L ");
    return reportsService.getReporte7Limited(repartoTipo, periodo).log();
  }

  // Reporte #8
  /**
   * This method is used to get report8.
   *
   * @return all reports.
   */
  @GetMapping("reporte8/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Reporte 8 value", notes = "Endpoint example Reporte 8",
          response = Reporte8Niff.class, code = 200)
  public boolean getReporte8(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                       @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llega reporte 8 ");
    return reportsService.getReporte(8, repartoTipo, periodo);
  }

  @GetMapping("reporte8L/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Reprte7 value", notes = "Endpoint example AsignacionCecoBolsa",
          response = Reporte1BolsasOficinas.class, code = 200)
  public Flux<Reporte8NiifResponse> getReporte8L(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                 @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llega reporte 8L ");
    return reportsService.getReporte8Limited(repartoTipo, periodo).log();
  }

  /**
   * This method is used to get filters.
   *
   * @return all reports.
   */
  // Filtros
  @GetMapping("filtroCodLinea/{repartoTipo}/{periodo}")
  @ApiOperation(value = "Filtro cod linea value", notes = "Endpoint example Filtro cod linea",
          response = String.class, code = 200)
  public Flux<FiltroLineaResponse> getFiltroCodLinea(@PathVariable("repartoTipo") @Valid int repartoTipo,
                                                     @PathVariable("periodo") @Valid int periodo) {
    System.out.println("llega reporte 4 ");
    return reportsService.getFiltroCodLinea(repartoTipo, periodo).log();
  }
}
