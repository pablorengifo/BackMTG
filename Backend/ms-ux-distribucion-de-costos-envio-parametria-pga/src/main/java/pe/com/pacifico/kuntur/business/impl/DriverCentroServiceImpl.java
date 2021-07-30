package pe.com.pacifico.kuntur.business.impl;

import com.pacifico.kuntur.core.exception.KunturHttpModelValidationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.com.pacifico.kuntur.business.DriverCentroService;
import pe.com.pacifico.kuntur.business.threads.CargarDriverCentroHilo;
import pe.com.pacifico.kuntur.expose.request.DriverCentroRequest;
import pe.com.pacifico.kuntur.expose.response.MaDriverResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverCentroResponse;
import pe.com.pacifico.kuntur.model.MaDriver;
import pe.com.pacifico.kuntur.model.MovDriverCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.DriverCentroJpaRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;




/**
 * <b>Class</b>: DriverCentroServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
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
public class DriverCentroServiceImpl implements DriverCentroService {

  private final DriverCentroJpaRepository driverCentroJpaRepository;

  @Override
  public Flux<MaDriverResponse> getMaDriverCentro(int repartoTipo, int periodo) {
    return Flux.fromIterable(driverCentroJpaRepository.findAll(repartoTipo,periodo))
        .map(this::getResponseFromMaDriver);
  }

  @Override
  public Flux<MovDriverCentroResponse> getAllMovDriverCentro(int repartoTipo, int periodo) {
    return Flux.fromIterable(driverCentroJpaRepository.findAllMov(repartoTipo,periodo))
        .map(this::getResponseFromMovDriverCentro);
  }

  @Override
  public Flux<MovDriverCentroResponse> getMovDriversCentro(int repartoTipo, int periodo, String codDriverCentro) {
    return Flux.fromIterable(driverCentroJpaRepository.findAllMov(repartoTipo,periodo,codDriverCentro))
        .map(this::getResponseFromMovDriverCentro);
  }

  @Override
  public boolean deleteMovDriverCentro(int repartoTipo, int periodo, String codigo) {
    return driverCentroJpaRepository.delete(repartoTipo,periodo, codigo);
  }

  @Override
  public int registerDriverCentro(DriverCentroRequest request) {
    return  driverCentroJpaRepository.save(request);
  }

  @Override
  public Mono<MovDriverCentro> updateDriverCentro(DriverCentroRequest request) {
    MovDriverCentro movDriverCentro = driverCentroJpaRepository.findByCodDriver(request.getCodDriver());
    System.out.println(movDriverCentro);
    if (movDriverCentro == null) {
      log.error("No existe registro en la tabla MOV_DRIVER_CENTRO con CodCentro: {}", request.getCodDriver());
      return Mono.error(new KunturHttpModelValidationException("No existe registro con CodCentro: " + request.getCodDriver()));
    }
    //LocalDateTime currentDateTime = LocalDateTime.now();
    driverCentroJpaRepository.update(request);
    MovDriverCentro md = new MovDriverCentro();
    return Mono.just(md);
  }

  @Value("${date-format-allowed.data-format}")
  private String dateFormatAllowed;

  @Value("${date-format-allowed.data-format-string}")
  private String dateFormatStringAllowed;

  @Override
  public List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo) {
    CargarDriverCentroHilo.begin(repartoTipo,periodo,requestFileRead,driverCentroJpaRepository,dateFormatStringAllowed,dateFormatAllowed);
    return null;
  }

  private MovDriverCentroResponse getResponseFromMovDriverCentro(MovDriverCentro movDriverCentro) {
    return MovDriverCentroResponse.builder()
        .codDriverCentro(movDriverCentro.getCodDriverCentro())
        .nombreDriver(movDriverCentro.getNombreDriver())
        .codCentroDestino(movDriverCentro.getCodCentroDestino())
        .nombreCentro(movDriverCentro.getNombreCentro())
        .fechaActualizacion(movDriverCentro.getFechaActualizacion())
        .fechaCreacion(movDriverCentro.getFechaCreacion())
        .periodo(movDriverCentro.getPeriodo())
        .repartoTipo(movDriverCentro.getRepartoTipo())
        .porcentaje(movDriverCentro.getPorcentaje())
        .build();
  }

  private MaDriverResponse getResponseFromMaDriver(MaDriver maDriver) {
    return MaDriverResponse.builder()
        .codDriver(maDriver.getCodDriver())
        .nombre((maDriver.getNombre()))
        .codDriverTipo(maDriver.getCodDriverTipo())
        .fechaActualizacion(maDriver.getFechaActualizacion())
        .fechaCreacion(maDriver.getFechaCreacion())
        .repartoTipo(maDriver.getRepartoTipo())
        .build();
  }
}
