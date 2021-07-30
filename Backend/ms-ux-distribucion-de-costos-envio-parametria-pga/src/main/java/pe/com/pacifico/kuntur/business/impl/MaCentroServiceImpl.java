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
import pe.com.pacifico.kuntur.business.MaCentroService;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.request.MaCentroRequest;
import pe.com.pacifico.kuntur.expose.response.MaCentroResponse;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MaCentroJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import pe.com.pacifico.kuntur.util.StringUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MaCentroServiceImpl <br/>
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
public class MaCentroServiceImpl implements MaCentroService {

  private final MaCentroJpaRepository maCentroJpaRepository;

  /**
   * This method is used to get all cuentas.
   * @param repartoTipo int
   * @return all cuentas.
   */

  public Flux<MaCentroResponse> getCentros(int repartoTipo) {

    return Flux.fromIterable(maCentroJpaRepository.findAll(repartoTipo))
        .map(this::getMaCentroResponseFromMaCentro);

  }

  @Override
  public Flux<MaCentroResponse> getCentrosNotInMov(int repartoTipo, int periodo) {
    return Flux.fromIterable(maCentroJpaRepository.findAllNotInMovCentro(repartoTipo,periodo))
        .map(this::getMaCentroResponseFromMaCentro);
  }

  /**
   * This method is used to get only one cuenta.
   *
   *
   * @param repartoTipo integer
   * @param codigo This is the first parameter to method.
   * @return one cuenta.
   */
  public Mono<MaCentroResponse> getCentro(int repartoTipo, String codigo) {
    return Mono.just(maCentroJpaRepository.findByCodCentro(repartoTipo,codigo))
        .map(this::getMaCentroResponseFromMaCentro);
  }

  private MaCentroResponse getMaCentroResponseFromMaCentro(MaCentro centro) {

    return MaCentroResponse.builder()
        .codCentro(centro.getCodCentro())
        .nombre(centro.getNombre())
        .codCentroPadre(centro.getCodCentroPadre())
        .estaActivo(centro.isEstaActivo())
        .fechaCreacion(centro.getFechaCreacion())
        .fechaActualizacion(centro.getFechaActualizacion())
        .grupoCeco(centro.getGrupoCeco())
        .niif17Atribuible(centro.getNiif17Atribuible())
        .niif17Clase(centro.getNiif17Clase())
        .niif17Tipo(centro.getNiif17Tipo())
        .nivel(centro.getNivel())
        .repartoTipo(centro.getRepartoTipo())
        .tipo(centro.getTipo())
        .tipoCeco(centro.getTipoCeco())
        .tipoGasto(centro.isTipoGasto())
        .build();

  }

  @Override
  public Mono<MaCentro> registerCentro(MaCentroRequest request) {
    int res = 0 ;
    if (request != null) {
      res = maCentroJpaRepository.save(request);
    }
    System.out.println("*******");
    System.out.println(request);

    MaCentro mc = new MaCentro();
    if (res == 0) { return null; }
    return Mono.just(mc);

  }

  @Override
  public Mono<MaCentro> updateCentro(MaCentroRequest request) {

    MaCentro centro = maCentroJpaRepository.findByCodCentro(request.getRepartoTipo(), request.getCodCentro());
    System.out.println(centro);
    if (centro == null) {
      log.error("No existe registro en la tabla MA_CENTRO con CodCentro: {}", request.getCodCentro());
      return Mono.error(new KunturHttpModelValidationException("No existe registro con CodCentro: " + request.getCodCentro()));
    }
    //LocalDateTime currentDateTime = LocalDateTime.now();
    maCentroJpaRepository.update(request);
    MaCentro mc = new MaCentro();
    return Mono.just(mc);

  }

  @Override
  public boolean deleteCentro(int repartoTipo, String codigo) {
    return maCentroJpaRepository.delete(repartoTipo,codigo);
  }

  @Value("${date-format-allowed.data-format}")
  private String dateFormatAllowed;

  @Value("${date-format-allowed.data-format-string}")
  private String dateFormatStringAllowed;

  @Override
  public List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead) {
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
        errores = maCentroJpaRepository.saveExcelToBD(listExcelBody, repartoTipo);
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
