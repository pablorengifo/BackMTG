package pe.com.pacifico.kuntur.business.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.com.pacifico.kuntur.business.MovUoaBbaVfaService;
import pe.com.pacifico.kuntur.business.threads.CargarMovUoaBbaVfaHilo;
import pe.com.pacifico.kuntur.expose.response.MovUoaBbaVfaResponse;
import pe.com.pacifico.kuntur.model.MovUoaBbaVfa;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MovUoaBbaVfaJpaRepository;
import reactor.core.publisher.Flux;


/**
 * <b>Class</b>: MovUoaBbaVfaServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 14, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MovUoaBbaVfaServiceImpl implements MovUoaBbaVfaService {

  private final MovUoaBbaVfaJpaRepository movUoaBbaVfaJpaRepository;

  @Value("${date-format-allowed.data-format}")
  private String dateFormatAllowed;

  @Value("${date-format-allowed.data-format-string}")
  private String dateFormatStringAllowed;

  @Override
  public Flux<MovUoaBbaVfaResponse> getAllMovUoaBbaVfas(int repartoTipo, int periodo) {
    return Flux.fromIterable(movUoaBbaVfaJpaRepository.findAll(repartoTipo, periodo))
        .map(this::movUoaBbaVfaResponse);
  }

  @Override
  public List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo) {

    CargarMovUoaBbaVfaHilo.begin(repartoTipo,periodo,requestFileRead,movUoaBbaVfaJpaRepository,
        dateFormatStringAllowed,dateFormatAllowed);
    return null;
  }

  private MovUoaBbaVfaResponse movUoaBbaVfaResponse(MovUoaBbaVfa movUoaBbaVfa) {

    return MovUoaBbaVfaResponse.builder()
        .idPoliza(movUoaBbaVfa.getIdPoliza())
        .numeroPoliza(movUoaBbaVfa.getNumeroPoliza())
        .codigoUoAPacifico(movUoaBbaVfa.getCodigoUoAPacifico())
        .descripcionUoA(movUoaBbaVfa.getDescripcionUoA())
        .centroCosto(movUoaBbaVfa.getCentroCosto())
        .lineaNegocio(movUoaBbaVfa.getLineaNegocio())
        .productoPpto(movUoaBbaVfa.getProductoPpto())
        .canalDistribucion(movUoaBbaVfa.getCanalDistribucion())
        .subCanal(movUoaBbaVfa.getSubCanal())
        .fechaCreacion(movUoaBbaVfa.getFechaCreacion())
        .fechaActualizacion(movUoaBbaVfa.getFechaActualizacion())
        .periodo(movUoaBbaVfa.getPeriodo())
        .repartoTipo(movUoaBbaVfa.getRepartoTipo())
        .build();
  }
}
