package pe.com.pacifico.kuntur.business;

import java.util.Date;

import java.util.List;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.CellStyle;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import org.springframework.core.io.ByteArrayResource;

/**
 * <b>Class</b>: ExcelService <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     June 3, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
public interface ExcelService {

  public SXSSFWorkbook crearLibro();

  public int crearCabecera(Sheet sh, int rowNum, List<Map<String, Object>> data, List<Integer> widths, CellStyle style);

  public int crearCabecera(Sheet sh, int rowNum, List<Map<String, Object>> data, List<String> extra, List<Integer> widths, CellStyle style);

  public int crearCabecera(Sheet sh, int rowNum, List<Map<String, Object>> data, List<Integer> widths,
                           CellStyle style, List<Integer> exclusion);

  public CellStyle cabeceraEstilo(SXSSFWorkbook wb);

  public CellStyle bordeEstilo(SXSSFWorkbook wb);

  public CellStyle fechaEstilo(SXSSFWorkbook wb);

  public ByteArrayResource crearResource(SXSSFWorkbook wb);

  public Cell createCell(Row row, int colNum, String value, CellStyle style);

  public Cell createCell(Row row, int colNum, Date value, CellStyle style);

  public Cell createCell(Row row, int colNum, double value, CellStyle style);

}
