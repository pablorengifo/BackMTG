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
import pe.com.pacifico.kuntur.business.MovCuentaContableService;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.request.MovCuentaContableRequest;
import pe.com.pacifico.kuntur.expose.response.MovCuentaContableResponse;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovCuentaContable;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MovCuentaContableJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import pe.com.pacifico.kuntur.util.StringUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;




/**
 * <b>Class</b>: MovCuentaContableServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     March 31, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MovCuentaContableServiceImpl implements MovCuentaContableService {

  private final MovCuentaContableJpaRepository movCuentaContableJpaRepository;

  @Override
  public Flux<MovCuentaContableResponse> getCuentas(int repartoTipo, int periodo) {
    return Flux.fromIterable(movCuentaContableJpaRepository.findAll(repartoTipo, periodo))
        .map(this::getMovCuentaContableResponseFromMovCuentaContable);
  }

  @Override
  public MovCuentaContableResponse getMovCuentaContableResponseFromMovCuentaContable(MovCuentaContable cuenta) {

    return MovCuentaContableResponse.builder()
        .codCuentaContable(cuenta.getCodCuentaContable())
        .nombre(cuenta.getNombre())
        .fechaCreacion(cuenta.getFechaCreacion())
        .fechaActualizacion(cuenta.getFechaActualizacion())
        .periodo(cuenta.getPeriodo())
        .repartoTipo(cuenta.getRepartoTipo())
        .saldo(cuenta.getSaldo())
        .build();

  }

  @Override
  public Mono<MovCuentaContable> registerCuenta(MovCuentaContableRequest request) {

    if (request != null) {
      movCuentaContableJpaRepository.save(request);
    }
    System.out.println("*******");
    System.out.println(request);

    MovCuentaContable mc = new MovCuentaContable();
    return Mono.just(mc);

  }

  @Override
  public boolean deleteCuenta(int repartoTipo, int periodo, String codigo) {
    return movCuentaContableJpaRepository.delete(repartoTipo, periodo, codigo);
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
        errores = movCuentaContableJpaRepository.saveExcelToBD(listExcelBody, repartoTipo, periodo);
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
