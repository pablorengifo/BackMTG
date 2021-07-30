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
import pe.com.pacifico.kuntur.business.MaCanalService;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.request.MaCanalRequest;
import pe.com.pacifico.kuntur.expose.response.MaCanalResponse;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaCanal;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MaCanalJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import pe.com.pacifico.kuntur.util.StringUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MaCanalServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 16, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MaCanalServiceImpl implements MaCanalService {

  private final MaCanalJpaRepository maCanalJpaRepository;

  /**
   * This method is used to get all canales.
   *
   * @return all canales.
   */
  public Flux<MaCanalResponse> getCanales(int repartoTipo) {

    return Flux.fromIterable(maCanalJpaRepository.findAll(repartoTipo))
        .map(this::getMaCanalResponseFromMaCanal);

  }

  /**
   * This method is used to get only one canal.
   *
   * @param codigo This is the first parameter to method.
   * @return one canal.
   */
  public Mono<MaCanalResponse> getCanal(int repartoTipo, String codigo) {
    return Mono.just(maCanalJpaRepository.findByCodCanal(repartoTipo, codigo))
        .map(this::getMaCanalResponseFromMaCanal);
  }

  private MaCanalResponse getMaCanalResponseFromMaCanal(MaCanal canal) {

    return MaCanalResponse.builder()
        .codCanal(canal.getCodCanal())
        .repartoTipo(canal.getRepartoTipo())
        .nombre(canal.getNombre())
        .estaActivo(canal.isEstaActivo())
        .fechaCreacion(canal.getFechaCreacion())
        .fechaActualizacion(canal.getFechaActualizacion())
        .build();
  }

  @Override
  public Mono<MaCanal> registerCanal(MaCanalRequest request) {

    if (request != null) {
      maCanalJpaRepository.save(request);
    }
    System.out.println("*******");
    System.out.println(request);

    MaCanal p = new MaCanal();
    return Mono.just(p);

  }

  @Override
  public Mono<MaCanal> updateCanal(MaCanalRequest request) {

    MaCanal canal = maCanalJpaRepository.findByCodCanal(request.getRepartoTipo(), request.getCodCanal());
    System.out.println(canal);
    if (canal == null) {
      log.error("No existe registro en la tabla MA_CANAL con CodCanal: {}", request.getCodCanal());
      return Mono.error(new KunturHttpModelValidationException("No existe registro con CodCanal: " + request.getCodCanal()));
    }
    maCanalJpaRepository.update(request);
    MaCanal p = new MaCanal();
    return Mono.just(p);

  }

  @Override
  public boolean deleteCanal(int repartoTipo, String codigo) {
    return maCanalJpaRepository.delete(repartoTipo, codigo);
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
        errores = maCanalJpaRepository.saveExcelToBD(listExcelBody, repartoTipo);
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
