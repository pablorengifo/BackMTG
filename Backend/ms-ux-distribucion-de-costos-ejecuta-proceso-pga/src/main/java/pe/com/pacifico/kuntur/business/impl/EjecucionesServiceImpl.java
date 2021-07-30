package pe.com.pacifico.kuntur.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.com.pacifico.kuntur.business.EjecucionesService;
import pe.com.pacifico.kuntur.business.exception.BusinessErrorCodes;
import pe.com.pacifico.kuntur.business.exception.BusinessException;
import pe.com.pacifico.kuntur.model.Ejecuciones;
import pe.com.pacifico.kuntur.repository.EjecucionesJpaRepository;
import pe.com.pacifico.kuntur.webclient.EjecucionesWebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <b>Class</b>: EjecucionesServiceImpl <br/>
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
@Service
@Slf4j
@RequiredArgsConstructor
public class EjecucionesServiceImpl implements EjecucionesService {

  private final EjecucionesWebClient ejecucionesWebClient;

  private final EjecucionesJpaRepository ejecucionesJpaRepository;

  /**
   * This method is used to get all ejecuciones.
   * @return all ejecuciones.
   */
  public Flux<Ejecuciones> getEjecuciones() {
    log.error("An exception occurred! {}", BusinessErrorCodes.BUSINESS_ERROR.getDescription());
    return Flux.error(BusinessException.createException(
                      HttpStatus.INTERNAL_SERVER_ERROR,
                      BusinessErrorCodes.BUSINESS_ERROR));
  }

  /**
   * This method is used to get only one ejecuciones.
   * @param id This is the first parameter to method.
   * @return one ejecuciones.
   */
  public Mono<Ejecuciones> getEjecuciones(Long id) {
    return ejecucionesWebClient.getEjecucionesResponse(id)
        .map(t -> Ejecuciones.builder()
            .id(t.getIdController())
            .data(t.getData())
            .cardNumber(t.getCardNumber())
            .documentNumber(t.getDocumentNumber())
            .build());
  }
}
