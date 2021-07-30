package pe.com.pacifico.kuntur.business.impl;

import com.pacifico.kuntur.core.exception.KunturHttpModelValidationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import pe.com.pacifico.kuntur.business.MaSubcanalService;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.request.MaSubcanalRequest;
import pe.com.pacifico.kuntur.expose.response.MaSubcanalResponse;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaSubcanal;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MaSubcanalJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import pe.com.pacifico.kuntur.util.StringUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MaSubcanalServiceImpl <br/>
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
public class MaSubcanalServiceImpl implements MaSubcanalService {

  private final MaSubcanalJpaRepository maSubcanalJpaRepository;

  /**
   * This method is used to get all subcanales.
   *
   * @return all subcanales.
   */
  public Flux<MaSubcanalResponse> getSubcanales(int repartoTipo) {

    return Flux.fromIterable(maSubcanalJpaRepository.findAll(repartoTipo))
        .map(this::getMaSubcanalResponseFromMaSubcanal);

  }

  @Override
  public Flux<MaSubcanalResponse> getSubcanalesNotInMov(int repartoTipo, int periodo) {
    return Flux.fromIterable(maSubcanalJpaRepository.findAllNotInMovSubcanal(repartoTipo, periodo))
        .map(this::getMaSubcanalResponseFromMaSubcanal);
  }

  /**
   * This method is used to get only one subcanal.
   *
   * @param codigo This is the first parameter to method.
   * @return one subcanal.
   */
  public Mono<MaSubcanalResponse> getSubcanal(int repartoTipo, String codigo) {
    return Mono.just(maSubcanalJpaRepository.findByCodSubcanal(repartoTipo, codigo))
        .map(this::getMaSubcanalResponseFromMaSubcanal);
  }

  private MaSubcanalResponse getMaSubcanalResponseFromMaSubcanal(MaSubcanal subcanal) {

    return MaSubcanalResponse.builder()
        .codSubcanal(subcanal.getCodSubcanal())
        .repartoTipo(subcanal.getRepartoTipo())
        .nombre(subcanal.getNombre())
        .estaActivo(subcanal.isEstaActivo())
        .fechaCreacion(subcanal.getFechaCreacion())
        .fechaActualizacion(subcanal.getFechaActualizacion())
        .build();
  }

  @Override
  public Mono<MaSubcanal> registerSubcanal(MaSubcanalRequest request) {

    if (request != null) {
      Calendar c = Calendar.getInstance();
      request.setEstaActivo(true);
      request.setFechaActualizacion(c.getTime());
      request.setFechaCreacion(c.getTime());
      maSubcanalJpaRepository.save(request);
    }
    System.out.println("*******");
    System.out.println(request);

    MaSubcanal ms = new MaSubcanal();
    return Mono.just(ms);

  }

  @Override
  public Mono<MaSubcanal> updateSubcanal(MaSubcanalRequest request) {

    MaSubcanal subcanal = maSubcanalJpaRepository.findByCodSubcanal(request.getRepartoTipo(), request.getCodSubcanal());
    System.out.println(subcanal);
    if (subcanal == null) {
      log.error("No existe registro en la tabla MA_SUBCANAL con CodSubcanal: {}", request.getCodSubcanal());
      return Mono.error(new KunturHttpModelValidationException("No existe registro con CodSubcanal: " + request.getCodSubcanal()));
    }
    Calendar c = Calendar.getInstance();
    request.setFechaActualizacion(c.getTime());
    maSubcanalJpaRepository.update(request);
    MaSubcanal ms = new MaSubcanal();
    return Mono.just(ms);

  }

  @Override
  public boolean deleteSubcanal(int repartoTipo, String codigo) {
    return maSubcanalJpaRepository.delete(repartoTipo, codigo);
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
        errores = maSubcanalJpaRepository.saveExcelToBD(listExcelBody, repartoTipo);
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
