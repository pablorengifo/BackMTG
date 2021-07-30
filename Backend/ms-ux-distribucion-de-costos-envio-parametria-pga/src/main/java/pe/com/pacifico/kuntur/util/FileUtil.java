package pe.com.pacifico.kuntur.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.extern.slf4j.Slf4j;

/**
 * <b>Class</b>: FileUtil <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Pacifico Seguros - La Chakra <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     March 23, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Slf4j
public class FileUtil {
  /**
   * remove Temp File.
   *
   * @param fileName the file
   */
  public static void removeTempFile(String fileName) {
    Path path = Paths.get(Constant.TMP_PATH.concat(File.separator).concat(fileName));
    try {
      Files.deleteIfExists(path);
    } catch (IOException ex) {
      log.error(ex.getMessage(), ex.getCause());
    }
  }
}
