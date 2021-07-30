package pe.com.pacifico.kuntur.business.impl;

import com.pacifico.kuntur.core.exception.KunturHttpModelValidationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import pe.com.pacifico.kuntur.business.MovCentroService;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.request.MovCentroRequest;
import pe.com.pacifico.kuntur.expose.response.MovCentroResponse;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MovCentroJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import pe.com.pacifico.kuntur.util.StringUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MovCentroServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 12, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MovCentroServiceImpl implements MovCentroService {

  private final MovCentroJpaRepository movCentroJpaRepository;

  /**
   * This method is used to get all Centros.
   *
   * @return all Centros.
   */

  public Flux<MovCentroResponse> getCentros(int repartoTipo, int periodo) {

    return Flux.fromIterable(movCentroJpaRepository.findAll(repartoTipo,periodo))
        .map(this::getMovCentroResponseFromMovCentro);

  }

  /**
   * This method is used to get only one Centro.
   *
   * @param codigo This is the first parameter to method.
   * @return one Centro.
   */
  public Mono<MovCentroResponse> getCentro(String codigo) {
    return Mono.just(movCentroJpaRepository.findByCodCentro(codigo))
        .map(this::getMovCentroResponseFromMovCentro);
  }

  /**
   * This method converts a MovCentro into a response.
   * @param centro This is the first parameter to method.
   * @return one response
   */
  public MovCentroResponse getMovCentroResponseFromMovCentro(MovCentro centro) {

    return MovCentroResponse.builder()
        .codCentro(centro.getCodCentro())
        .nombre(centro.getNombre())
        .tipo(centro.getTipo())
        .codCentroOrigen(centro.getCodCentroOrigen())
        .codCuentaContableOrigen(centro.getCodCuentaContableOrigen())
        .codEntidadOrigen(centro.getCodEntidadOrigen())
        .codPartidaOrigen(centro.getCodPartidaOrigen())
        .fechaCreacion(centro.getFechaCreacion())
        .fechaActualizacion(centro.getFechaActualizacion())
        .grupoGasto(centro.getGrupoGasto())
        .iteracion(centro.getIteracion())
        .periodo(centro.getPeriodo())
        .repartoTipo(centro.getRepartoTipo())
        .saldo(centro.getSaldo())
        .build();

  }

  @Override
  public int registerCentro(MovCentroRequest request) {
    return  movCentroJpaRepository.save(request);
  }

  @Override
  public Mono<MovCentro> updateCentro(MovCentroRequest request) {

    MovCentro centro = movCentroJpaRepository.findByCodCentro(request.getCodCentro());
    System.out.println(centro);
    if (centro == null) {
      log.error("No existe registro en la tabla MOV_CENTRO con CodCentro: {}", request.getCodCentro());
      return Mono.error(new KunturHttpModelValidationException("No existe registro con CodCentro: " + request.getCodCentro()));
    }
    //LocalDateTime currentDateTime = LocalDateTime.now();
    movCentroJpaRepository.update(request);
    MovCentro mp = new MovCentro();
    return Mono.just(mp);

  }

  @Override
  public boolean deleteCentro(int repartoTipo, int periodo, String codigo) {

    return movCentroJpaRepository.delete(repartoTipo,periodo, codigo);
  }

  @Value("${date-format-allowed.data-format}")
  private String dateFormatAllowed;

  @Value("${date-format-allowed.data-format-string}")
  private String dateFormatStringAllowed;

  @Override
  public List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo) {
    List<String> errores = new ArrayList<>();
    if (requestFileRead.getEncodeByte() != null) {
      log.info("Generar Archivo tmp");
      String fileName = StringUtil.convertCurrentDateToText();
      String clearName = StringUtil.removeDiacritics(fileName);
      String filePath = Constant.TMP_PATH + File.separator + clearName;
      log.info(String.format("Ruta archivo tmp: %s", filePath));
      File file = new File(filePath);
      log.info("Decodificando cadena Base64");
      byte[] bytes = Base64.getDecoder().decode(requestFileRead.getEncodeByte());
      InputStream is = new ByteArrayInputStream(bytes);
      List<List<CellData>> listExcelBody;
      try {
        String mimeType = URLConnection.guessContentTypeFromStream(is);
        log.info("Crear archivo");
        FileCopyUtils.copy(bytes, file);
        listExcelBody = ExcelUtil.getRowList(filePath, dateFormatAllowed, dateFormatStringAllowed,0);
        errores = movCentroJpaRepository.saveExcelToBD(listExcelBody, repartoTipo, periodo);
      } catch (ValidateException | IOException ex) {
        log.error("Error genérico al crear archivo temporal", ex);
        errores.add("Error genérico al crear archivo temporal: " + ex.getMessage());
        return errores;
      }
      log.info("Leer archivo");
      log.info("Borrar archivo");
      FileUtil.removeTempFile(clearName);
      log.info("Fin de proceso");
    }
    return errores;
  }

}
