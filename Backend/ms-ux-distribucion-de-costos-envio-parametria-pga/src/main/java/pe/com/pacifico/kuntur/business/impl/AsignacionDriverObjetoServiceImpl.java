package pe.com.pacifico.kuntur.business.impl;

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
import pe.com.pacifico.kuntur.business.AsignacionDriverObjetoService;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverObjetoRequest;
import pe.com.pacifico.kuntur.expose.response.AsignacionDriverObjetoResponse;
import pe.com.pacifico.kuntur.model.AsignacionDriverObjeto;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.AsignacionDriverObjetoJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import pe.com.pacifico.kuntur.util.StringUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: AsignacionDriverObjetoServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 04, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AsignacionDriverObjetoServiceImpl implements AsignacionDriverObjetoService {

  private final AsignacionDriverObjetoJpaRepository asignacionDriverObjetoJpaRepository;

  @Override
  public Flux<AsignacionDriverObjetoResponse> getAsignaciones(int repartoTipo, int periodo) {
    return Flux.fromIterable(asignacionDriverObjetoJpaRepository.findAll(repartoTipo, periodo))
        .map(this::asignacionDriverObjetoResponse);
  }

  @Override
  public Mono<AsignacionDriverObjeto> registerAsignacion(AsignacionDriverObjetoRequest request) {
    if (request != null) {
      Calendar c = Calendar.getInstance();
      request.setFechaCreacion(c.getTime());
      request.setFechaActualizacion(c.getTime());
      asignacionDriverObjetoJpaRepository.save(request);
    }
    AsignacionDriverObjeto p = new AsignacionDriverObjeto();
    return Mono.just(p);
  }

  @Override
  public boolean deleteAsignacion(int repartoTipo, int periodo, String codigo, String grupoGasto) {
    return asignacionDriverObjetoJpaRepository.delete(repartoTipo, periodo, codigo, grupoGasto);
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
        listExcelBody = ExcelUtil.getRowList(filePath, dateFormatAllowed, dateFormatStringAllowed, 0);
        errores = asignacionDriverObjetoJpaRepository.saveExcelToBd(listExcelBody, periodo, repartoTipo);

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

  private AsignacionDriverObjetoResponse asignacionDriverObjetoResponse(AsignacionDriverObjeto asignacionDriverObjeto) {
    return AsignacionDriverObjetoResponse.builder()
        .codCentro(asignacionDriverObjeto.getCodCentro())
        .codDriver(asignacionDriverObjeto.getCodDriver())
        .nombreCentro(asignacionDriverObjeto.getNombreCentro())
        .nombreDriver(asignacionDriverObjeto.getNombreDriver())
        .grupoGasto(asignacionDriverObjeto.getGrupoGasto())
        .repartoTipo(asignacionDriverObjeto.getRepartoTipo())
        .periodo(asignacionDriverObjeto.getPeriodo())
        .build();
  }

}
