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
import pe.com.pacifico.kuntur.business.MaProductoService;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.request.MaProductoRequest;
import pe.com.pacifico.kuntur.expose.response.MaProductoResponse;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaProducto;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MaProductoJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import pe.com.pacifico.kuntur.util.StringUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MaProductoServiceImpl <br/>
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
public class MaProductoServiceImpl implements MaProductoService {
  private final MaProductoJpaRepository maProductoJpaRepository;

  /**
   * This method is used to get all cuentas.
   *
   * @return all cuentas.
   */

  public Flux<MaProductoResponse> getProductos(int repartoTipo) {

    return Flux.fromIterable(maProductoJpaRepository.findAll(repartoTipo))
        .map(this::getMaProductoResponseFromMaProducto);

  }

  @Override
  public Flux<MaProductoResponse> getProductosNotInMovProducto(int repartoTipo, int periodo) {
    List<MaProducto> a = maProductoJpaRepository.findAllNotInMovProducto(repartoTipo, periodo);
    System.out.println("************* Prueba **************");
    System.out.println("*** Array Size: " + a.size());
    System.out.println(a.toString());
    System.out.println("***********************************");
    return Flux.fromIterable(a)
        .map(this::getMaProductoResponseFromMaProducto);
  }

  /**
   * This method is used to get only one cuenta.
   *
   * @param codigo This is the first parameter to method.
   * @return one cuenta.
   */
  public Mono<MaProductoResponse> getProducto(int repartoTipo, String codigo) {
    return Mono.just(maProductoJpaRepository.findByCodProducto(repartoTipo, codigo))
        .map(this::getMaProductoResponseFromMaProducto);
  }

  private MaProductoResponse getMaProductoResponseFromMaProducto(MaProducto producto) {

    return MaProductoResponse.builder()
        .codProducto(producto.getCodProducto())
        .nombre(producto.getNombre())
        .fechaCreacion(producto.getFechaCreacion())
        .fechaActualizacion(producto.getFechaActualizacion())
        .repartoTipo(producto.getRepartoTipo())
        .estaActivo(producto.isEstaActivo())
        .build();

  }

  @Override
  public Mono<MaProducto> registerProducto(MaProductoRequest request) {

    if (request != null) {
      Calendar c = Calendar.getInstance();
      request.setEstaActivo(true);
      request.setFechaActualizacion(c.getTime());
      request.setFechaCreacion(c.getTime());
      maProductoJpaRepository.save(request);
    }
    System.out.println("*******");
    System.out.println(request);

    MaProducto p = new MaProducto();
    return Mono.just(p);

  }

  @Override
  public Mono<MaProducto> updateProducto(MaProductoRequest request) {

    MaProducto producto = maProductoJpaRepository.findByCodProducto(request.getRepartoTipo(), request.getCodProducto());
    System.out.println(producto);
    if (producto == null) {
      log.error("No existe registro en la tabla MA_LINEA con CodProducto: {}", request.getCodProducto());
      return Mono.error(new KunturHttpModelValidationException("No existe registro con CodProducto: " + request.getCodProducto()));
    }
    Calendar c = Calendar.getInstance();
    request.setFechaActualizacion(c.getTime());
    maProductoJpaRepository.update(request);
    MaProducto p = new MaProducto();
    return Mono.just(p);

  }

  @Override
  public boolean deleteProducto(int repartoTipo, String codigo) {
    return maProductoJpaRepository.delete(repartoTipo, codigo);
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
        errores = maProductoJpaRepository.saveExcelToBD(listExcelBody, repartoTipo);
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
