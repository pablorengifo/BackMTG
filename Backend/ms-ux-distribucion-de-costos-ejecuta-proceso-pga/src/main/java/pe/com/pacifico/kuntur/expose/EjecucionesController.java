package pe.com.pacifico.kuntur.expose;

import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.Collections;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.pacifico.kuntur.business.EjecucionesService;
import pe.com.pacifico.kuntur.expose.request.EjecucionesToBeObfuscated;
import pe.com.pacifico.kuntur.expose.response.EjecucionesResponse;
import pe.com.pacifico.kuntur.model.Ejecuciones;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <b>Class</b>: EjecucionesController <br/>
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
@RequestMapping("/ejecuciones")
@Slf4j
@RequiredArgsConstructor
public class EjecucionesController {

  private final EjecucionesService ejecucionesService;

  @GetMapping(value = "/stream/{idcontrolador}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  @ApiOperation(value = "Integer Numbers example", notes = "Ejemplo Endpoint", response = Flux.class, code = 200)
  public Flux<Integer> streamInteger(@PathVariable("idcontrolador") @Valid Integer controllerId) {
    log.info("id - controlador : {}", controllerId);
    return Flux.just(1,2,3,4);
  }

  /**
   * This method is used to get all ejecucionesResponse.
   * @return all ejecuciones.
   */
  @GetMapping
  @ApiOperation(value = "Ejecuciones value", notes = "Endpoint example ejecuciones", response = Ejecuciones.class, code = 200)
  public Flux<EjecucionesResponse> getEjecuciones() {
    return ejecucionesService.getEjecuciones().flatMap(this::buildResponse);
  }

  /**
   * This method is used to get only one ejecucionesResponse.
   * @param controllerId This is the first parameter to method.
   * @return one ejecuciones.
   */
  @GetMapping("{idcontrolador}")
  @ApiOperation(value = "Ejecuciones value", notes = "Endpoint example ejecuciones", response = Ejecuciones.class, code = 200)
  public Mono<EjecucionesResponse> getEjecucionesByControllerId(@PathVariable("idcontrolador") @Valid Long controllerId) {
    return ejecucionesService.getEjecuciones(controllerId).flatMap(this::buildResponse);
  }

  /**
   * This method is used to get only one ejecucionesToBeObfuscated.
   * @param request This is the first parameter to method.
   * @return one ejecuciones.
   */
  @PostMapping
  @ApiOperation(value = "Ejecuciones value", notes = "Endpoint example ejecuciones", response = Ejecuciones.class, code = 200)
  public Mono<EjecucionesResponse> obfuscatedEjecuciones(@RequestBody @Valid EjecucionesToBeObfuscated request) {
    return Mono.just(EjecucionesResponse.builder()
        .idController(Long.parseLong(request.getId()))
        .ctaCte(request.getCta())
        .documentNumber(request.getDocumentNumber())
        .phoneNumbers(Collections.singletonList(request.getPhoneNumber()))
        .cardNumber(request.getCardNumber())
        .data(request.getData())
        .email(request.getEmail())
        .build());
  }

  private Mono<EjecucionesResponse> buildResponse(Ejecuciones ejecuciones) {
    EjecucionesResponse ejecucionesResponse = new EjecucionesResponse();
    try {
      ejecucionesResponse.setIdController(ejecuciones.getId());
      ejecucionesResponse.setData(ejecuciones.getData());
      ejecucionesResponse.setCardNumber(ejecuciones.getCardNumber());
      ejecucionesResponse.setDocumentNumber(ejecuciones.getDocumentNumber());
      ejecucionesResponse.setPhoneNumbers(Arrays.asList("945678340", "998747231"));
      ejecucionesResponse.setCtaCte("0011-0117-0200789865");
      ejecucionesResponse.setEmail("jonathan.wong@globant.com");
      ejecucionesResponse.setPrivateData("Pacifico-kuntur");
      ejecucionesResponse.setPrivateInfo(
            "MIAGCSqGSIb3DQEHA6CAMIACAQAxggFKMIIBRgIBADAuMBoxGDAWBgNVBAMTD3Bh"
            + "Y2lmaWNvLmNvbS5wZQIQPf3gEhnsTFGjhwApR5EDEDANBgkqhkiG9w0BAQEFAASC"
            + "AQBAmbyAFvtdIxEKlefysPL2BtLvy13tke6sFRb0bw9wV8bi5QPaeEw8VvG4zE79"
            + "FHN7U1ZUZ9KT/gb9RUZdbLIjGYZOwgzjef70XfhJtD7jBXbXA85zGaDX/SuSLrFC"
            + "2gmfO3+rbAhYI6LWOgWH3iW7ofHjkuwi3SWC5OPlV09+1JhCjt8K3t/ibq9X/Rsv"
            + "0/P4GE2J3U/M7RdXp9j848PVHg9eDAK2pjxIM5pcYKPF5o1zdFk/gSLgzeFkIxlR"
            + "4qwHs0WrcvU+oR4UDafVY194haE5JlQRo7pQkfEsPYeBWpA1waYsx8Nqrigt99D7"
            + "byQhdSlw5Bpbtvxj/b+5PU/UMIAGCSqGSIb3DQEHATAdBglghkgBZQMEAQIEEMDn"
            + "1dG+o2qE6B4Br5EuV7WggAQQgbpVPB/WQeS41D6ls1IlhQAAAAAAAAAAAAA=");
    } catch (Exception ex) {
      Mono.error(new Exception());
    }
    return Mono.just(ejecucionesResponse).log();
  }
}
