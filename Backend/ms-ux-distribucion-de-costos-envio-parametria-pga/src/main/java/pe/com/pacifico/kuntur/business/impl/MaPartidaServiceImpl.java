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
import pe.com.pacifico.kuntur.business.MaPartidaService;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.request.MaPartidaRequest;
import pe.com.pacifico.kuntur.expose.response.MaPartidaResponse;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaPartida;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.MaPartidaJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import pe.com.pacifico.kuntur.util.StringUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



/**
 * <b>Class</b>: MaPartidaServiceImpl <br/>
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
public class MaPartidaServiceImpl implements MaPartidaService {

  private final MaPartidaJpaRepository maPartidaJpaRepository;

  /**
   * This method is used to get all cuentas.
   *
   * @return all cuentas.
   */
  public Flux<MaPartidaResponse> getPartidas(int repartoTipo) {

    return Flux.fromIterable(maPartidaJpaRepository.findAll(repartoTipo))
        .map(this::getMaPartidaResponseFromMaPartida);

  }

  @Override
  public Flux<MaPartidaResponse> getPartidasNotInMov(int repartoTipo, int periodo) {
    return Flux.fromIterable(maPartidaJpaRepository.findAllNotInMovPartida(repartoTipo, periodo))
        .map(this::getMaPartidaResponseFromMaPartida);
  }

  /**
   * This method is used to get only one cuenta.
   *
   * @param codigo This is the first parameter to method.
   * @return one cuenta.
   */
  public Mono<MaPartidaResponse> getPartida(int repartoTipo, String codigo) {
    return Mono.just(maPartidaJpaRepository.findByCodPartida(repartoTipo, codigo))
        .map(this::getMaPartidaResponseFromMaPartida);
  }

  /**
   * This method converts a MaPartida into a response.
   * @param partida codigo This is the first parameter to method.
   * @return one response
   */
  public MaPartidaResponse getMaPartidaResponseFromMaPartida(MaPartida partida) {

    return MaPartidaResponse.builder()
        .codPartida(partida.getCodPartida())
        .nombre(partida.getNombre())
        .fechaCreacion(partida.getFechaCreacion())
        .fechaActualizacion(partida.getFechaActualizacion())
        .grupoGasto(partida.getGrupoGasto())
        .tipoGasto(partida.isTipoGasto())
        .repartoTipo(partida.getRepartoTipo())
        .build();

  }

  @Override
  public Mono<MaPartida> registerPartida(MaPartidaRequest request) {

    if (request != null) {
      maPartidaJpaRepository.save(request);
    }
    System.out.println("*******");
    System.out.println(request);

    MaPartida p = new MaPartida();
    return Mono.just(p);

  }

  @Override
  public Mono<MaPartida> updatePartida(MaPartidaRequest request) {

    MaPartida partida = maPartidaJpaRepository.findByCodPartida(request.getRepartoTipo(), request.getCodPartida());
    System.out.println(partida);
    if (partida == null) {
      log.error("No existe registro en la tabla MA_PARTIDA con CodCuentaContable: {}", request.getCodPartida());
      return Mono.error(new KunturHttpModelValidationException("No existe registro con CodCuentaContable: " + request.getCodPartida()));
    }
    //LocalDateTime currentDateTime = LocalDateTime.now();
    maPartidaJpaRepository.update(request);
    MaPartida p = new MaPartida();
    return Mono.just(p);

  }

  @Override
  public boolean deletePartida(int repartoTipo, String codigo) {
    return maPartidaJpaRepository.delete(repartoTipo, codigo);
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
        errores = maPartidaJpaRepository.saveExcelToBD(listExcelBody, repartoTipo);
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
