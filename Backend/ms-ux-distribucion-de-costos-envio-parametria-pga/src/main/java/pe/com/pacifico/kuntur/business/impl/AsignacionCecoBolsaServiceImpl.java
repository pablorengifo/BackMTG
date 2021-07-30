package pe.com.pacifico.kuntur.business.impl;

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
import pe.com.pacifico.kuntur.business.AsignacionCecoBolsaService;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.response.AsignacionCecoBolsaResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverCentroResponse;
import pe.com.pacifico.kuntur.model.AsignacionCecoBolsa;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovDriverCentro;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.AsignacionCecoBolsaJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import pe.com.pacifico.kuntur.util.StringUtil;
import reactor.core.publisher.Flux;


/**
 * <b>Class</b>: AsignacionCecoBolsaServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 21, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AsignacionCecoBolsaServiceImpl implements AsignacionCecoBolsaService {

  private final AsignacionCecoBolsaJpaRepository asignacionCecoBolsaJpaRepository;

  @Value("${date-format-allowed.data-format}")
  private String dateFormatAllowed;

  @Value("${date-format-allowed.data-format-string}")
  private String dateFormatStringAllowed;

  @Override
  public Flux<AsignacionCecoBolsaResponse> getAsignaciones(int repartoTipo, int periodo) {
    return Flux.fromIterable(asignacionCecoBolsaJpaRepository.findAll(repartoTipo, periodo))
        .map(this::asignacionCecoBolsaResponse);
  }

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
        errores = asignacionCecoBolsaJpaRepository.saveExcelToBd(listExcelBody,periodo,repartoTipo);

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

  @Override
  public Flux<MovDriverCentroResponse> getMovDriversCentro(int repartoTipo, int periodo, String codDriverCentro) {
    return Flux.fromIterable(asignacionCecoBolsaJpaRepository.findAllMov(repartoTipo,periodo,codDriverCentro))
        .map(this::getResponseFromMovDriverCentro);
  }

  private AsignacionCecoBolsaResponse asignacionCecoBolsaResponse(AsignacionCecoBolsa asignacionCecoBolsa)
  {
    return AsignacionCecoBolsaResponse.builder()
        .codCentro(asignacionCecoBolsa.getCodCentro())
        .codCuentaContable(asignacionCecoBolsa.getCodCuentaContable())
        .codDriver(asignacionCecoBolsa.getCodDriver())
        .codPartida(asignacionCecoBolsa.getCodPartida())
        .nombreCentro(asignacionCecoBolsa.getNombreCentro())
        .nombreCuentaContable(asignacionCecoBolsa.getNombreCuentaContable())
        .nombreDriver(asignacionCecoBolsa.getNombreDriver())
        .nombrePartida(asignacionCecoBolsa.getNombrePartida())
        .build();
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
}
