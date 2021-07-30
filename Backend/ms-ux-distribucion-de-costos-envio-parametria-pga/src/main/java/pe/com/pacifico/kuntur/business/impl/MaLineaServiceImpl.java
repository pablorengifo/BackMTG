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
import pe.com.pacifico.kuntur.business.MaLineaService;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.request.MaLineaRequest;
import pe.com.pacifico.kuntur.expose.response.MaLineaResponse;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaLinea;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MaLineaJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import pe.com.pacifico.kuntur.util.StringUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;




/**
 * <b>Class</b>: MaLineaServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 7, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class MaLineaServiceImpl implements MaLineaService {
  private final MaLineaJpaRepository maLineaJpaRepository;

  /**
   * This method is used to get all cuentas.
   *
   * @return all cuentas.
   */

  public Flux<MaLineaResponse> getLineas(int repartoTipo) {

    return Flux.fromIterable(maLineaJpaRepository.findAll(repartoTipo))
        .map(this::getMaLineaResponseFromMaLinea);

  }

  /**
   * This method is used to get only one cuenta.
   *
   * @param codigo This is the first parameter to method.
   * @return one cuenta.
   */
  public Mono<MaLineaResponse> getLinea(int repartoTipo, String codigo) {
    return Mono.just(maLineaJpaRepository.findByCodLinea(repartoTipo, codigo))
        .map(this::getMaLineaResponseFromMaLinea);
  }

  private MaLineaResponse getMaLineaResponseFromMaLinea(MaLinea linea) {

    return MaLineaResponse.builder()
        .codLinea(linea.getCodLinea())
        .nombre(linea.getNombre())
        .fechaCreacion(linea.getFechaCreacion())
        .fechaActualizacion(linea.getFechaActualizacion())
        .repartoTipo(linea.getRepartoTipo())
        .estaActivo(linea.isEstaActivo())
        .build();

  }

  @Override
  public Mono<MaLinea> registerLinea(MaLineaRequest request) {

    if (request != null) {
      maLineaJpaRepository.save(request);
    }
    System.out.println("*******");
    System.out.println(request);

    MaLinea p = new MaLinea();
    return Mono.just(p);

  }

  @Override
  public Mono<MaLinea> updateLinea(MaLineaRequest request) {

    MaLinea linea = maLineaJpaRepository.findByCodLinea(request.getRepartoTipo(), request.getCodLinea());
    System.out.println(linea);
    if (linea == null) {
      log.error("No existe registro en la tabla MA_LINEA con CodLinea: {}", request.getCodLinea());
      return Mono.error(new KunturHttpModelValidationException("No existe registro con CodLinea: " + request.getCodLinea()));
    }
    maLineaJpaRepository.update(request);
    MaLinea p = new MaLinea();
    return Mono.just(p);

  }

  @Override
  public boolean deleteLinea(int repartoTipo, String codigo) {
    return maLineaJpaRepository.delete(repartoTipo, codigo);
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
        errores = maLineaJpaRepository.saveExcelToBD(listExcelBody, repartoTipo);
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
