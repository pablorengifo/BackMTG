package pe.com.pacifico.kuntur.business.threads;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.DetalleGastoJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import pe.com.pacifico.kuntur.util.StringUtil;


/**
 * <b>Class</b>: HiloPrueba <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 6, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class CargarDetalleGastoHilo extends Thread {

  public static Thread hilo = null;
  public static boolean generar = false;
  public static boolean enEjecucion = false;
  public static boolean terminoEjecucion = false;
  public static List<String> errores;
  public static int periodo;
  public static int repartoTipo;
  public static RequestFileRead requestFileRead;
  public static DetalleGastoJpaRepository detalleGastoJpaRepository;
  private static String dateFormatStringAllowed;
  private static String dateFormatAllowed;

  /**
   * Ejecucion del hilo.
   */
  public void run() {
    if (generar)
    {
      errores = generarDetalleGasto();
      generar = false;
    } else
    {
      errores = cargarArchivo();
    }
    CargarDetalleGastoHilo.enEjecucion = false;
    CargarDetalleGastoHilo.terminoEjecucion = true;
  }

  /**
   * Inicio del hilo.
   */
  public static void begin(int repartoTipo, int periodo, RequestFileRead requestFileRead,
                           DetalleGastoJpaRepository detalleGastoJpaRepository, String dfsa, String dfa) {

    CargarDetalleGastoHilo.repartoTipo = repartoTipo;
    CargarDetalleGastoHilo.periodo = periodo;
    CargarDetalleGastoHilo.requestFileRead = requestFileRead;
    CargarDetalleGastoHilo.detalleGastoJpaRepository = detalleGastoJpaRepository;
    CargarDetalleGastoHilo.dateFormatAllowed = dfa;
    CargarDetalleGastoHilo.dateFormatStringAllowed = dfsa;

    Thread hilo = new CargarDetalleGastoHilo();
    enEjecucion = true;
    hilo.start();
  }

  /**
   * Limpiar el hilo.
   */
  public static void limpiarHilo() {
    hilo = null;
    enEjecucion = false;
    terminoEjecucion = false;
    errores = null;
  }

  /**
   * Generar detalle.
   */
  private List<String> generarDetalleGasto() {
    return detalleGastoJpaRepository.generarDetalleGasto(repartoTipo,periodo);
  }

  /**
   * Cargar Archivo.
   */
  private List<String> cargarArchivo() {
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
        listExcelBody = ExcelUtil.getRowList_v2(filePath, dateFormatAllowed, dateFormatStringAllowed,0);
        if (repartoTipo == 1) {
          errores = detalleGastoJpaRepository.saveExcelToBdReal(listExcelBody,periodo);
        } else {
          errores = detalleGastoJpaRepository.saveExcelToBdPresupuesto(listExcelBody,periodo);
        }
        log.info("Leer archivo");
        log.info("Borrar archivo");
        FileUtil.removeTempFile(clearName);
        log.info("Fin de proceso");

      } catch (ValidateException | IOException ex) {
        log.error("Error genérico al crear archivo temporal", ex);
        errores.add("Error genérico al crear archivo temporal: " + ex.getMessage());
      } catch (Exception ex) {
        log.error("Error", ex);
        errores.add("Error: " + ex.getMessage());
      }
    }
    return errores;
  }


}
