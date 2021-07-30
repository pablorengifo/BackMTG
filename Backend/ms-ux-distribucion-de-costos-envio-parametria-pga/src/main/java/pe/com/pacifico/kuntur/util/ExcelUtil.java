package pe.com.pacifico.kuntur.util;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.HttpStatus;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.model.CellData;


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
public class ExcelUtil {
  /**
   * get Row List.
   *
   * @param filepath the file
   * @return the response entity
   */
  public static List<List<CellData>> getRowList(String filepath, String dateFormatAllowed, String dateFormatStringAllowed, int sheetNum)
      throws ValidateException {
    List<List<CellData>> body = new ArrayList<>();
    try (FileInputStream file = new FileInputStream(new File(filepath))) {
      try (Workbook worBook = WorkbookFactory.create(file)) {
        Sheet sheet = worBook.getSheetAt(sheetNum);
        Iterator<Row> rowIterator = sheet.iterator();
        Row row;
        List<CellData> rowHeader = new ArrayList<>();
        while (rowIterator.hasNext()) {
          row = rowIterator.next();
          Iterator<Cell> cellIterator = row.cellIterator();
          List<CellData> rowBody = new ArrayList<>();
          while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String cellValue = getCellValue(cell, dateFormatAllowed, dateFormatStringAllowed);
            if (cellValue != null) {
              if (row.getRowNum() == 0) {
                rowHeader
                    .add(new CellData(row.getRowNum(), cell.getColumnIndex(),
                        cellValue, cell.getCellStyle().getDataFormatString()));
              } else {
                rowBody
                    .add(new CellData(row.getRowNum(), cell.getColumnIndex(),
                        cellValue, cell.getCellStyle().getDataFormatString()));
              }
            }
          }
          if (!rowHeader.isEmpty() && row.getRowNum() == 0) {
            body.add(rowHeader);
          }
          if (!rowBody.isEmpty()) {
            body.add(rowBody);
          }
        }
      }
    } catch (Exception ex) {
      log.error("Error reading the file", ex);
      throw new ValidateException("Error reading the file", ex, "read_storage_error",
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return body;
  }

  /**
   * get Row List v2.
   *
   * @param filepath the file
   * @return the response entity
   */
  public static List<List<CellData>> getRowList_v2(String filepath, String dateFormatAllowed, String dateFormatStringAllowed,int sheetNum)
      throws ValidateException {
    List<List<CellData>> body = new ArrayList<>();
    try (FileInputStream file = new FileInputStream(new File(filepath))) {
      try (Workbook worBook = WorkbookFactory.create(file)) {
        Sheet sheet = worBook.getSheetAt(sheetNum);
        Iterator<Row> rowIterator = sheet.iterator();
        Row row;
        List<CellData> rowHeader = new ArrayList<>();
        while (rowIterator.hasNext()) {
          row = rowIterator.next();
          Iterator<Cell> cellIterator = row.cellIterator();
          List<CellData> rowBody = new ArrayList<>();

          boolean isRowNull = true;

          while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String cellValue = getCellValue(cell, dateFormatAllowed, dateFormatStringAllowed);

            if (cellValue != null) {
              isRowNull = false;
            }
            if (row.getRowNum() == 0) {
              rowHeader
                  .add(new CellData(row.getRowNum(), cell.getColumnIndex(),
                      cellValue, cell.getCellStyle().getDataFormatString()));
            } else {
              rowBody
                  .add(new CellData(row.getRowNum(), cell.getColumnIndex(),
                      cellValue, cell.getCellStyle().getDataFormatString()));
            }
          }
          if (!rowHeader.isEmpty() && row.getRowNum() == 0 && !isRowNull) {
            body.add(rowHeader);
          }
          if (!rowBody.isEmpty() && !isRowNull) {
            body.add(rowBody);
          }
        }
      }
    } catch (Exception ex) {
      log.error("Error reading the file", ex);
      throw new ValidateException("Error reading the file", ex, "read_storage_error",
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return body;
  }

  /**
   * Gets cell value.
   *
   * @param cell                    the cell
   * @param dateFormatAllowed       the date format allowed
   * @param dateFormatStringAllowed the date format string allowed
   * @return the cell value
   */
  public static String getCellValue(final Cell cell, String dateFormatAllowed, String dateFormatStringAllowed) {
    String cellValue = null;
    CellType ct = cell.getCellTypeEnum();
    switch (ct) {
      case STRING:
        cellValue = cell.getStringCellValue().trim();
        break;
      case NUMERIC:
        if (DateUtil.isCellDateFormatted(cell) || dateFormatAllowed(cell, dateFormatAllowed, dateFormatStringAllowed)) {
          Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
          System.out.println("Cell numeric value: " + cell.getNumericCellValue());
          System.out.println(date.toString());
          String pattern = "yyyy-MM-dd HH:mm:ss.SSSZ";
          SimpleDateFormat dformat = new SimpleDateFormat(
              pattern, new Locale("en", "US"));
          cellValue = dformat.format(date);
        } else {
          BigDecimal result = new BigDecimal(String.valueOf(cell.getNumericCellValue()).trim());
          cellValue = result.toPlainString();
        }
        break;
      case BOOLEAN:
        cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
        break;
      case FORMULA:
        CellType cacheCellType = cell.getCachedFormulaResultTypeEnum();
      {
        switch (cacheCellType) {
          case STRING:
            cellValue = cell.getStringCellValue().trim();
            break;
          case NUMERIC:
            BigDecimal result = new BigDecimal(String.valueOf(cell.getNumericCellValue()).trim());
            cellValue = result.toPlainString();
            break;
          case BOOLEAN:
            cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
            break;
          default:
            cellValue = cell.getCellFormula().trim();
        }
      }
        break;
      default:
        cellValue = null;
    }
    return cellValue;
  }

  /**
   * Date format allowed boolean.
   *
   * @param cell                    the cell
   * @param dateFormatAllowed       the date format allowed
   * @param dateFormatStringAllowed the date format string allowed
   * @return the boolean
   */
  public static boolean dateFormatAllowed(final Cell cell, String dateFormatAllowed, String dateFormatStringAllowed) {
    String[] dateFormatAllowedArray = dateFormatAllowed.split(Constant.VERTICAL_BAR);
    String[] dateFormatStringAllowedArray = dateFormatStringAllowed.split(Constant.VERTICAL_BAR);
    boolean noneMatchDateFormat = Arrays.asList(dateFormatAllowedArray).stream()
        .noneMatch(s -> s.equalsIgnoreCase(String.valueOf(cell.getCellStyle().getDataFormat())));
    boolean noneMatchDateFormatString = Arrays.asList(dateFormatStringAllowedArray).stream()
        .noneMatch(s -> s.equalsIgnoreCase(cell.getCellStyle().getDataFormatString()));
    return !(noneMatchDateFormat && noneMatchDateFormatString);
  }
}

