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
import pe.com.pacifico.kuntur.business.CuentaContableService;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.request.CuentaContableRequest;
import pe.com.pacifico.kuntur.expose.response.CuentaContableResponse;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.CuentaContable;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.CuentaContableJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import pe.com.pacifico.kuntur.util.StringUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: CuentaContableServiceImpl <br/>
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
public class CuentaContableServiceImpl implements CuentaContableService {

  private final CuentaContableJpaRepository cuentaContableJpaRepository;

  @Override
  public Flux<CuentaContableResponse> getCuentas(int repartoTipo) {
    return Flux.fromIterable(cuentaContableJpaRepository.findAll(repartoTipo))
        .map(this::getCuentaContableResponseFromCuentaContable);
  }

  @Override
  public Flux<CuentaContableResponse> getCuentasNotInMov(int repartoTipo, int periodo) {
    return Flux.fromIterable(cuentaContableJpaRepository.findAllNotInMovCuentaContable(repartoTipo, periodo))
        .map(this::getCuentaContableResponseFromCuentaContable);
  }

  @Override
  public Mono<CuentaContableResponse> getCuenta(int repartoTipo, String codigo) {
    return Mono.just(cuentaContableJpaRepository.findByCodCuentaContable(repartoTipo, codigo))
        .map(this::getCuentaContableResponseFromCuentaContable);
  }

  private CuentaContableResponse getCuentaContableResponseFromCuentaContable(CuentaContable cuenta) {

    return CuentaContableResponse.builder()
        .codCuentaContable(cuenta.getCodCuentaContable())
        .nombre(cuenta.getNombre())
        .estaActivo(cuenta.isEstaActivo())
        .fechaCreacion(cuenta.getFechaCreacion())
        .fechaActualizacion(cuenta.getFechaActualizacion())
        .niif17Atribuible(cuenta.getNiif17Atribuible())
        .niif17Clase(cuenta.getNiif17Clase())
        .niif17Tipo(cuenta.getNiif17Tipo())
        .repartoTipo(cuenta.getRepartoTipo())
        .tipoGasto(cuenta.isTipoGasto())
        .build();

  }

  @Override
  public Mono<CuentaContable> registerCuenta(CuentaContableRequest request) {

    int res = 0;
    if (request != null) {

      res = cuentaContableJpaRepository.save(request);
    }
    System.out.println("*******");
    System.out.println(request);

    CuentaContable c = new CuentaContable();
    if (res == 0) { return null; }
    return Mono.just(c);

  }

  @Override
  public Mono<CuentaContable> updateCuenta(CuentaContableRequest request) {

    CuentaContable cuenta = cuentaContableJpaRepository.findByCodCuentaContable(request.getRepartoTipo(), request.getCodCuentaContable());
    System.out.println(cuenta);
    if (cuenta == null) {
      log.error("No existe registro en la tabla MA_CUENTA_CONTABLE con CodCuentaContable: {}", request.getCodCuentaContable());
      return Mono.error(new KunturHttpModelValidationException(
          "No existe registro con CodCuentaContable: " + request.getCodCuentaContable()));
    }
    //LocalDateTime currentDateTime = LocalDateTime.now();
    cuentaContableJpaRepository.update(request);
    CuentaContable c = new CuentaContable();
    return Mono.just(c);

  }

  @Override
  public boolean deleteCuenta(int repartoTipo, String codigo) {
    return cuentaContableJpaRepository.delete(repartoTipo, codigo);
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
        errores = cuentaContableJpaRepository.saveExcelToBD(listExcelBody, repartoTipo);
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
  public Flux<CuentaContableResponse> getCuentasNiif(int repartoTipo) {
    return Flux.fromIterable(cuentaContableJpaRepository.findAllNiif(repartoTipo))
        .map(this::getCuentaContableResponseFromCuentaContable);
  }

  @Override
  public Flux<CuentaContableResponse> getCuentasNotNiif(int repartoTipo) {
    return Flux.fromIterable(cuentaContableJpaRepository.findAllNotNiifCuentaContable(repartoTipo))
        .map(this::getCuentaContableResponseFromCuentaContable);
  }

  @Override
  public Mono<CuentaContable> agregarCuentaNiif(CuentaContableRequest request) {
    if (request != null) {
      cuentaContableJpaRepository.registrarNiif(request);
    }
    System.out.println("*******");
    System.out.println(request);

    CuentaContable mc = new CuentaContable();
    return Mono.just(mc);
  }

  @Override
  public boolean removeCuentaNiif(int repartoTipo, String codigo) {
    return cuentaContableJpaRepository.quitarNiif(repartoTipo, codigo);
  }

  @Override
  public List<String> fileReadNiif(int repartoTipo, RequestFileRead requestFileRead) {
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
        errores = cuentaContableJpaRepository.registerNiifByExcel(listExcelBody, repartoTipo);
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
