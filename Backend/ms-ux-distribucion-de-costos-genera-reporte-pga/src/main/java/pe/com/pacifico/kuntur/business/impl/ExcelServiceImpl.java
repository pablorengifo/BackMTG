package pe.com.pacifico.kuntur.business.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.ooxml.POIXMLProperties.CoreProperties;
import org.apache.poi.ooxml.POIXMLProperties.ExtendedProperties;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import pe.com.pacifico.kuntur.business.ExcelService;

/**
 * <b>Class</b>: ExcelServiceImpl <br/>
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
@Service
public class ExcelServiceImpl implements ExcelService {
  private Logger logger = LoggerFactory.getLogger(ExcelServiceImpl.class);

  @Autowired
  public ExcelServiceImpl() {
  }

  @Override
  public ByteArrayResource crearResource(SXSSFWorkbook wb) {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      wb.write(bos);
      bos.close();
      wb.dispose();
      return new ByteArrayResource(bos.toByteArray());
    } catch (IOException ex) {
      logger.info(ex.getMessage());
      return null;
    }
  }

  @Override
  public int crearCabecera(Sheet sh, int rowNum, List<Map<String, Object>> data, List<Integer> widths, CellStyle cabeceraEstilo) {
    Map<String, Object> fila = data.get(0);
    Row row = sh.createRow(rowNum);
    int colNum = 0;
    for (Entry<String, Object> campo : fila.entrySet()) {
      sh.setColumnWidth(colNum, widths.get(colNum));
      Cell cell = row.createCell(colNum++);
      cell.setCellValue(campo.getKey());
      cell.setCellStyle(cabeceraEstilo);
    }
    return 1;
  }

  @Override
  public int crearCabecera(Sheet sh, int rowNum, List<Map<String, Object>> data, List<String> extra,
                           List<Integer> widths, CellStyle cabeceraEstilo) {
    Map<String, Object> fila = data.get(0);
    Row row = sh.createRow(rowNum);
    int colNum = 0;
    for (Entry<String, Object> campo : fila.entrySet()) {
      Cell cell = row.createCell(colNum);
      sh.setColumnWidth(colNum, widths.get(colNum));
      cell.setCellValue(campo.getKey());
      cell.setCellStyle(cabeceraEstilo);
      colNum++;
    }
    for (String ex : extra) {
      //sh.setColumnWidth(colNum, widths.get(colNum));
      Cell cell = row.createCell(colNum);
      sh.setColumnWidth(colNum, widths.get(colNum));
      cell.setCellValue(ex);
      cell.setCellStyle(cabeceraEstilo);
      colNum++;
    }
    return 1;
  }

  @Override
  public int crearCabecera(Sheet sh, int rowNum, List<Map<String, Object>> data,
                           List<Integer> widths, CellStyle cabeceraEstilo, List<Integer> exclusion) {
    Map<String, Object> fila = data.get(0);
    Row row = sh.createRow(rowNum);
    int colNum = 0;
    int reduction = 0;
    for (Entry<String, Object> campo : fila.entrySet()) {
      if (!exclusion.contains(colNum)) {
        Cell cell = row.createCell(colNum - reduction);
        sh.setColumnWidth(colNum - reduction, widths.get(colNum - reduction));
        cell.setCellValue(campo.getKey());
        cell.setCellStyle(cabeceraEstilo);
      } else {
        reduction++;
      }
      colNum++;
    }
    return 1;
  }

  @Override
  public SXSSFWorkbook crearLibro() {
    SXSSFWorkbook wb = new SXSSFWorkbook(-1);
    CoreProperties coreProperties = wb.getXSSFWorkbook().getProperties().getCoreProperties();
    coreProperties.setCreator("Administrator");

    ExtendedProperties extendedProperties = wb.getXSSFWorkbook().getProperties().getExtendedProperties();
    extendedProperties.getUnderlyingProperties().setApplication("Encuestas Planeamiento");
    extendedProperties.getUnderlyingProperties().setCompany("Pacífico Seguros");
    return wb;
  }

  @Override
  public CellStyle cabeceraEstilo(SXSSFWorkbook wb) {
    // Create a Font for styling header cells
    Font headerFont = wb.createFont();
    headerFont.setBold(true);
    headerFont.setFontHeightInPoints((short) 12);
    headerFont.setColor(IndexedColors.WHITE.getIndex());

    // Create a CellStyle with the font
    XSSFCellStyle estilo = (XSSFCellStyle) wb.createCellStyle();
    estilo.setFont(headerFont);
    estilo.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 170, 230), null));
    estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    estilo.setBorderTop(BorderStyle.THIN);
    estilo.setTopBorderColor(new XSSFColor(new java.awt.Color(0, 153, 204), null));
    estilo.setBorderBottom(BorderStyle.THIN);
    estilo.setBottomBorderColor(new XSSFColor(new java.awt.Color(0, 153, 204), null));
    estilo.setBorderLeft(BorderStyle.THIN);
    estilo.setLeftBorderColor(new XSSFColor(new java.awt.Color(0, 153, 204), null));
    estilo.setBorderRight(BorderStyle.THIN);
    estilo.setRightBorderColor(new XSSFColor(new java.awt.Color(0, 153, 204), null));
    return estilo;
  }

  @Override
  public CellStyle bordeEstilo(SXSSFWorkbook wb) {
    XSSFCellStyle estilo = (XSSFCellStyle) wb.createCellStyle();
    estilo.setBorderTop(BorderStyle.THIN);
    estilo.setTopBorderColor(new XSSFColor(new java.awt.Color(0, 153, 204), null));
    estilo.setBorderBottom(BorderStyle.THIN);
    estilo.setBottomBorderColor(new XSSFColor(new java.awt.Color(0, 153, 204), null));
    estilo.setBorderLeft(BorderStyle.THIN);
    estilo.setLeftBorderColor(new XSSFColor(new java.awt.Color(0, 153, 204), null));
    estilo.setBorderRight(BorderStyle.THIN);
    estilo.setRightBorderColor(new XSSFColor(new java.awt.Color(0, 153, 204), null));
    return estilo;
  }

  @Override
  public CellStyle fechaEstilo(SXSSFWorkbook wb) {
    XSSFCellStyle estilo = (XSSFCellStyle) wb.createCellStyle();
    estilo.setBorderTop(BorderStyle.THIN);
    estilo.setTopBorderColor(new XSSFColor(new java.awt.Color(0, 153, 204), null));
    estilo.setBorderBottom(BorderStyle.THIN);
    estilo.setBottomBorderColor(new XSSFColor(new java.awt.Color(0, 153, 204), null));
    estilo.setBorderLeft(BorderStyle.THIN);
    estilo.setLeftBorderColor(new XSSFColor(new java.awt.Color(0, 153, 204), null));
    estilo.setBorderRight(BorderStyle.THIN);
    estilo.setRightBorderColor(new XSSFColor(new java.awt.Color(0, 153, 204), null));
    estilo.setDataFormat((short) 14);
    return estilo;
  }

  @Override
  public Cell createCell(Row row, int colNum, String value, CellStyle style) {
    Cell cell = row.createCell(colNum);
    cell.setCellValue(value);
    cell.setCellStyle(style);
    return cell;
  }

  @Override
  public Cell createCell(Row row, int colNum, Date value, CellStyle style) {
    Cell cell = row.createCell(colNum);
    cell.setCellValue(value);
    cell.setCellStyle(style);
    return cell;
  }

  @Override
  public Cell createCell(Row row, int colNum, double value, CellStyle style) {
    Cell cell = row.createCell(colNum);
    cell.setCellValue(value);
    cell.setCellStyle(style);
    return cell;
  }
}
