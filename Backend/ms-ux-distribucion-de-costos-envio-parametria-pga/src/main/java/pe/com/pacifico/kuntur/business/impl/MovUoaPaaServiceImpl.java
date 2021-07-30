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
import pe.com.pacifico.kuntur.business.MovUoaPaaService;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.request.MovUoaPaaRequest;
import pe.com.pacifico.kuntur.expose.response.MovUoaPaaResponse;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovUoaPaa;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MovUoaPaaJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import pe.com.pacifico.kuntur.util.StringUtil;
import reactor.core.publisher.Flux;


/**
 * <b>Class</b>: MovUoaPaaServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 12, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MovUoaPaaServiceImpl implements MovUoaPaaService {

  private final MovUoaPaaJpaRepository movUoaPaaJpaRepository;

  @Override
  public Flux<MovUoaPaaResponse> getAllMovUoaPaaList(int repartoTipo, int periodo) {
    return Flux.fromIterable(movUoaPaaJpaRepository.findAllMov(repartoTipo, periodo))
        .map(this::getResponseFromMovUoaPaa);
  }

  @Override
  public Flux<MovUoaPaaResponse> getMovUoaPaaList(int repartoTipo, int periodo, String codProducto) {
    return Flux.fromIterable(movUoaPaaJpaRepository.findAllMovByCodProducto(repartoTipo, periodo, codProducto))
        .map(this::getResponseFromMovUoaPaa);
  }

  @Override
  public boolean deleteMovUoaPaaList(int repartoTipo, int periodo, String codProducto) {
    return movUoaPaaJpaRepository.delete(repartoTipo,periodo, codProducto);
  }

  @Override
  public int registerMovUoaPaaList(List<MovUoaPaaRequest> movUoaPaaRequestList) {
    return movUoaPaaJpaRepository.save(movUoaPaaRequestList);
  }

  @Override
  public int updateMovUoaPaaList(List<MovUoaPaaRequest> movUoaPaaRequestList) {
    /*MovDriverCentro movDriverCentro = driverCentroJpaRepository.findByCodDriver(request.getCodDriver());
    System.out.println(movDriverCentro);
    if (movDriverCentro == null) {
      log.error("No existe registro en la tabla MOV_DRIVER_CENTRO con CodCentro: {}", request.getCodDriver());
      return Mono.error(new KunturHttpModelValidationException("No existe registro con CodCentro: " + request.getCodDriver()));
    }*/
    //LocalDateTime currentDateTime = LocalDateTime.now();
    return movUoaPaaJpaRepository.update(movUoaPaaRequestList);
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
        errores = movUoaPaaJpaRepository.saveExcelToBD(listExcelBody, repartoTipo, periodo);
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

  private MovUoaPaaResponse getResponseFromMovUoaPaa(MovUoaPaa movUoaPaa) {
    return MovUoaPaaResponse.builder()
        .codProducto(movUoaPaa.getCodProducto())
        .nombreProducto(movUoaPaa.getNombreProducto())
        .unidadCuenta(movUoaPaa.getUnidadCuenta())
        .fechaActualizacion(movUoaPaa.getFechaActualizacion())
        .fechaCreacion(movUoaPaa.getFechaCreacion())
        .periodo(movUoaPaa.getPeriodo())
        .repartoTipo(movUoaPaa.getRepartoTipo())
        .porcentaje(movUoaPaa.getPorcentaje())
        .build();
  }
}
