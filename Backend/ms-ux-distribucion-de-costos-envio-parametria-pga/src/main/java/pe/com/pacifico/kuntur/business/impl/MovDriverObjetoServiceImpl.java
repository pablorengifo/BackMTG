package pe.com.pacifico.kuntur.business.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.com.pacifico.kuntur.business.MovDriverObjetoService;
import pe.com.pacifico.kuntur.business.threads.CargarDriverObjetoHilo;
import pe.com.pacifico.kuntur.expose.response.MaDriverResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverObjetoResponse;
import pe.com.pacifico.kuntur.model.MaDriver;
import pe.com.pacifico.kuntur.model.MovDriverObjeto;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MovDriverObjetoJpaRepository;
import reactor.core.publisher.Flux;




/**
 * <b>Class</b>: MovDriverObjetoServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 28, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MovDriverObjetoServiceImpl implements MovDriverObjetoService {

  private final MovDriverObjetoJpaRepository movDriverObjetoJpaRepository;
  @Value("${date-format-allowed.data-format}")
  private String dateFormatAllowed;
  @Value("${date-format-allowed.data-format-string}")
  private String dateFormatStringAllowed;

  @Override
  public Flux<MaDriverResponse> getMaDriverObjeto(int repartoTipo, int periodo) {
    return Flux.fromIterable(movDriverObjetoJpaRepository.findAll(repartoTipo, periodo))
        .map(this::getResponseFromMaDriver);
  }

  @Override
  public Flux<MovDriverObjetoResponse> getObjetos(int repartoTipo, int periodo, String codDriver) {
    List<MovDriverObjeto> movDriverObjetoList = movDriverObjetoJpaRepository.findAllByPeriod(repartoTipo, periodo, codDriver);
    /*System.out.println("************* Mov driver objeto **************");
    System.out.println("*** Array Size: " + movDriverObjetoList.size());
    System.out.println(movDriverObjetoList.toString());
    System.out.println("***********************************");*/
    return Flux.fromIterable(movDriverObjetoList)
        .map(this::getMovDriverObjetoResponseFromMovDriverObjeto);
  }

  @Override
  public Flux<MovDriverObjetoResponse> getObjetos(int repartoTipo, int periodo) {
    List<MovDriverObjeto> movDriverObjetoList = movDriverObjetoJpaRepository.findAllByPeriod(repartoTipo, periodo);
    return Flux.fromIterable(movDriverObjetoList)
        .map(this::getMovDriverObjetoResponseFromMovDriverObjeto);
  }

  @Override
  public boolean deleteMovDriverObjeto(int repartoTipo, int periodo, String codigo) {
    return movDriverObjetoJpaRepository.delete(repartoTipo, periodo, codigo);
  }

  private MovDriverObjetoResponse getMovDriverObjetoResponseFromMovDriverObjeto(MovDriverObjeto objeto) {
    System.out.println("Nombre producto : " + objeto.getNombreProducto());
    return MovDriverObjetoResponse.builder()
        .codDriverObjeto(objeto.getCodDriverObjeto())
        .nombreDriver(objeto.getNombreDriver())
        .repartoTipo(objeto.getRepartoTipo())
        .periodo(objeto.getPeriodo())
        .codProducto(objeto.getCodProducto())
        .codSubcanal(objeto.getCodSubcanal())
        .fechaCreacion(objeto.getFechaCreacion())
        .fechaActualizacion(objeto.getFechaActualizacion())
        .porcentaje(objeto.getPorcentaje())
        .nombreDriver(objeto.getNombreDriver())
        .nombreProducto(objeto.getNombreProducto())
        .nombreSubcanal(objeto.getNombreSubcanal())
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

  @Override
  public List<String> fileRead(int repartoTipo, int periodo, RequestFileRead requestFileRead) {
    CargarDriverObjetoHilo.begin(repartoTipo,periodo,requestFileRead,
        movDriverObjetoJpaRepository,dateFormatStringAllowed,dateFormatAllowed);
    return null;
  }

}
