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
import pe.com.pacifico.kuntur.business.AsignacionDriverCascadaService;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverCascadaRequest;
import pe.com.pacifico.kuntur.expose.response.AsignacionDriverCascadaResponse;
import pe.com.pacifico.kuntur.expose.response.MovDriverCentroResponse;
import pe.com.pacifico.kuntur.model.AsignacionDriverCascada;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.AsignacionDriverCascadaJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import pe.com.pacifico.kuntur.util.StringUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: AsignacionDriverCascadaServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 30, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AsignacionDriverCascadaServiceImpl implements AsignacionDriverCascadaService {

  @Value("${date-format-allowed.data-format}")
  private String dateFormatAllowed;

  @Value("${date-format-allowed.data-format-string}")
  private String dateFormatStringAllowed;

  private final AsignacionDriverCascadaJpaRepository asignacionDriverCascadaJpaRepository;

  @Override
  public Flux<AsignacionDriverCascadaResponse> getAsignaciones(int repartoTipo, int periodo) {
    return Flux.fromIterable(asignacionDriverCascadaJpaRepository.findAll(repartoTipo, periodo))
        .map(this::asignacionDriverCascadaResponse);
  }

  @Override
  public List<String>  fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo) {
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
        listExcelBody = ExcelUtil.getRowList_v2(filePath, dateFormatAllowed, dateFormatStringAllowed, 0);
        errores = asignacionDriverCascadaJpaRepository.saveExcelToBd(listExcelBody, periodo, repartoTipo);

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
  public Flux<MovDriverCentroResponse> getDetalleMovCentro(int repartoTipo, int periodo, String codDriver) {
    return null;
  }

  @Override
  public boolean deleteAsignacion(int repartoTipo, int periodo, String codigo) {
    return asignacionDriverCascadaJpaRepository.delete(repartoTipo, periodo, codigo);
  }

  @Override
  public Mono<AsignacionDriverCascada> registerDriverCascada(AsignacionDriverCascadaRequest request) {
    if (request != null) {
      asignacionDriverCascadaJpaRepository.save(request);
    }
    System.out.println("*******");
    System.out.println(request);

    AsignacionDriverCascada p = new AsignacionDriverCascada();
    return Mono.just(p);
  }

  private AsignacionDriverCascadaResponse asignacionDriverCascadaResponse(AsignacionDriverCascada asignacionDriverCascada) {
    return AsignacionDriverCascadaResponse.builder()
        .codCentro(asignacionDriverCascada.getCodCentro())
        .nombreCentro(asignacionDriverCascada.getNombreCentro())
        .codDriver(asignacionDriverCascada.getCodDriver())
        .nombreDriver(asignacionDriverCascada.getNombreDriver())
        .fechaActualizacion(asignacionDriverCascada.getFechaActualizacion())
        .fechaCreacion(asignacionDriverCascada.getFechaCreacion())
        .periodo(asignacionDriverCascada.getPeriodo())
        .repartoTipo(asignacionDriverCascada.getRepartoTipo())
        .build();
  }
}
