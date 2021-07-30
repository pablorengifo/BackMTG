package pe.com.pacifico.kuntur.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.com.pacifico.kuntur.business.ParamsService;
import pe.com.pacifico.kuntur.business.exception.BusinessErrorCodes;
import pe.com.pacifico.kuntur.business.exception.BusinessException;
import pe.com.pacifico.kuntur.model.Params;
import pe.com.pacifico.kuntur.repository.ParamsJpaRepository;
import pe.com.pacifico.kuntur.webclient.ParamsWebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <b>Class</b>: ParamsServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Pacifico Seguros - La Chakra <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 26, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ParamsServiceImpl implements ParamsService {

  private final ParamsWebClient paramsWebClient;

  private final ParamsJpaRepository paramsJpaRepository;

  /**
   * This method is used to get all params.
   * @return all params.
   */
  public Flux<Params> getParams() {
    log.error("An exception occurred! {}", BusinessErrorCodes.BUSINESS_ERROR.getDescription());
    return Flux.error(BusinessException.createException(
                      HttpStatus.INTERNAL_SERVER_ERROR,
                      BusinessErrorCodes.BUSINESS_ERROR));
  }

  /**
   * This method is used to get only one params.
   * @param id This is the first parameter to method.
   * @return one params.
   */
  public Mono<Params> getParams(Long id) {
    return paramsWebClient.getParamsResponse(id)
        .map(t -> Params.builder()
            .id(t.getIdController())
            .data(t.getData())
            .cardNumber(t.getCardNumber())
            .documentNumber(t.getDocumentNumber())
            .build());
  }
}
