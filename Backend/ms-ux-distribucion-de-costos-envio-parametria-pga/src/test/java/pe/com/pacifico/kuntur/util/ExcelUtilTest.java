package pe.com.pacifico.kuntur.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.FileCopyUtils;
import pe.com.pacifico.kuntur.business.impl.MovCentroServiceImpl;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.repository.MovCentroJpaRepository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExcelUtilTest {

  private String dateFormatAllowed = "58|31";

  private String dateFormatStringAllowed = "DD/MM/YYYY|m/d/yy|M/D/YY|MM/DD/YY|MM/DD/YYYY|YY-MM-DD|YYYY-MM-DD";

  //Given
  int repartoTipo=1;
  int periodo = 202104;

  @Mock
  private MovCentroJpaRepository jpaRepository;

  @Mock
  Cell mockCell = mock(Cell.class);

  @Mock
  SimpleDateFormat mockSimpleDateFormat = mock(SimpleDateFormat.class);

  @Mock
  CellStyle mockCellStyle = mock(CellStyle.class);

  @InjectMocks
  private MovCentroServiceImpl service;

  @InjectMocks
  private ExcelUtil excelUtil;

  @Test
  public void shouldReadFilev1() throws IOException, ValidateException  {
    // Given
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Prueba");
    Row firstRow = sheet.createRow(0);
    Row secondRow = sheet.createRow(1);
    Cell c = firstRow.createCell(0);
    c.setCellValue("header_0");
    c = firstRow.createCell(1);
    c.setCellValue("header_1");
    c = secondRow.createCell(0);
    c.setCellValue(1);
    c = secondRow.createCell(1);
    c.setCellValue("a");
    String filePath = Constant.TMP_PATH + File.separator + "Prueba";
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      workbook.write(bos);
    } finally {
      bos.close();
    }
    byte[] bytes = bos.toByteArray();
    File file = new File(filePath);
    FileCopyUtils.copy(bytes, file);
    Assertions.assertNotNull(ExcelUtil.getRowList(filePath, dateFormatAllowed, dateFormatStringAllowed, 0));
    FileUtil.removeTempFile("Prueba");
  }

  @Test
  public void shouldReadFilev2() throws ValidateException, IOException {
    // Given
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Prueba");
    Row firstRow = sheet.createRow(0);
    Row secondRow = sheet.createRow(1);
    Cell c = firstRow.createCell(0);
    c.setCellValue("header_0");
    c = firstRow.createCell(1);
    c.setCellValue("header_1");
    c = secondRow.createCell(0);
    c.setCellValue(1);
    c = secondRow.createCell(1);
    c.setCellValue("a");
    String filePath = Constant.TMP_PATH + File.separator + "Prueba";
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      workbook.write(bos);
    } finally {
      bos.close();
    }
    byte[] bytes = bos.toByteArray();
    File file = new File(filePath);
    FileCopyUtils.copy(bytes, file);
    //RequestFileRead requestFileRead = new RequestFileRead(java.util.Base64.getEncoder().encodeToString(bytes));
    Assertions.assertNotNull(ExcelUtil.getRowList_v2(filePath, dateFormatAllowed, dateFormatStringAllowed, 0));
    FileUtil.removeTempFile("Prueba");
  }

  @Test
  public void getCellValueString() {
    // When
    when(mockCell.getCellTypeEnum()).thenReturn(CellType.STRING);
    when(mockCell.getStringCellValue()).thenReturn("tester");
    // Then
    Assertions.assertEquals("tester", ExcelUtil.getCellValue(mockCell, dateFormatAllowed, dateFormatStringAllowed));
  }

  @Test
  public void getCellValueBoolean() {
    // When
    when(mockCell.getCellTypeEnum()).thenReturn(CellType.BOOLEAN);
    when(mockCell.getBooleanCellValue()).thenReturn(true);
    // Then
    Assertions.assertEquals("true", ExcelUtil.getCellValue(mockCell, dateFormatAllowed, dateFormatStringAllowed));
  }

  @Test
  public void getCellValueFormulaNumber() {
    // When
    when(mockCell.getCellTypeEnum()).thenReturn(CellType.FORMULA);
    when(mockCell.getCachedFormulaResultTypeEnum()).thenReturn(CellType.NUMERIC);
    lenient().when(mockCell.getNumericCellValue()).thenReturn(130.5d);
    // Then
    Assertions.assertEquals("130.5", ExcelUtil.getCellValue(mockCell, dateFormatAllowed, dateFormatStringAllowed));
  }

  @Test
  public void getCellValueFormulaString() {
    // When
    when(mockCell.getCellTypeEnum()).thenReturn(CellType.FORMULA);
    when(mockCell.getCachedFormulaResultTypeEnum()).thenReturn(CellType.STRING);
    lenient().when(mockCell.getStringCellValue()).thenReturn("texto");
    // Then
    Assertions.assertEquals("texto", ExcelUtil.getCellValue(mockCell, dateFormatAllowed, dateFormatStringAllowed));
  }

  @Test
  public void getCellValueBroke() {
    // When
    when(mockCell.getCellTypeEnum()).thenReturn(CellType.BLANK);
    // Then
    Assertions.assertNull(ExcelUtil.getCellValue(mockCell, dateFormatAllowed, dateFormatStringAllowed));
  }

  @Test
  public void getCellValueFormulaBoolean() {
    // When
    when(mockCell.getCellTypeEnum()).thenReturn(CellType.FORMULA);
    when(mockCell.getCachedFormulaResultTypeEnum()).thenReturn(CellType.BOOLEAN);
    when(mockCell.getBooleanCellValue()).thenReturn(true);
    // Then
    Assertions.assertEquals("true", ExcelUtil.getCellValue(mockCell, dateFormatAllowed, dateFormatStringAllowed));
  }

  @Test
  public void getCellValueFormulaDefault() {
    // When
    when(mockCell.getCellTypeEnum()).thenReturn(CellType.FORMULA);
    when(mockCell.getCachedFormulaResultTypeEnum()).thenReturn(CellType.BLANK);
    when(mockCell.getCellFormula()).thenReturn("");
    // Then
    Assertions.assertEquals("", ExcelUtil.getCellValue(mockCell, dateFormatAllowed, dateFormatStringAllowed));
  }

  @Test
  public void getCellValueNumber() {
    short s = 1;
    System.out.println(dateFormatAllowed);
    System.out.println(dateFormatStringAllowed);
    // When
    lenient().when(mockCell.getCellTypeEnum()).thenReturn(CellType.NUMERIC);
    lenient().when(mockCell.getNumericCellValue()).thenReturn(130.5d);
    lenient().when(mockCell.getCellStyle()).thenReturn(mockCellStyle);
    lenient().when(mockCellStyle.getDataFormat()).thenReturn(s);
    // Then
    Assertions.assertEquals("130.5", ExcelUtil.getCellValue(mockCell, dateFormatAllowed, dateFormatStringAllowed));
  }

  @Test
  public void getCellValueNumberDate() {
    Date date = new Date();
    String pattern = "yyyy-MM-dd HH:mm:ss.SSSZ";
    SimpleDateFormat dformat = new SimpleDateFormat(
        pattern, new Locale("en", "US"));
    short s = 1;
    System.out.println(dateFormatAllowed);
    System.out.println(dateFormatStringAllowed);
    // When
    lenient().when(mockCell.getCellTypeEnum()).thenReturn(CellType.NUMERIC);
    lenient().when(mockCell.getNumericCellValue()).thenReturn(43951d);
    lenient().when(mockCell.getCellStyle()).thenReturn(mockCellStyle);
    lenient().when(mockCellStyle.getDataFormat()).thenReturn(s);
    try (MockedStatic<DateUtil> theMock = Mockito.mockStatic(DateUtil.class)) {
      theMock.when(() -> DateUtil.isCellDateFormatted(any(Cell.class))).thenReturn(true);
      theMock.when(() -> DateUtil.getJavaDate(anyDouble())).thenReturn(date);
      // Then
      Assertions.assertEquals(dformat.format(date), ExcelUtil.getCellValue(mockCell, dateFormatAllowed, dateFormatStringAllowed));
    }
  }

}
