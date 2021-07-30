package pe.com.pacifico.kuntur.business.impl;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import pe.com.pacifico.kuntur.business.ExcelService;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.anyInt;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;


/**
 * <b>Class</b>: ExcelServiceImplTest <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Pacifico Seguros - La Chakra <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     Junio 14, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */

@ExtendWith(MockitoExtension.class)
public class ExcelServiceImplTest {
  @Mock
  private ExcelService excelService = mock(ExcelService.class);
  @Mock
  private Row mockRow = mock(Row.class);

  @InjectMocks
  private ExcelServiceImpl excelServiceImpl;

  private SXSSFWorkbook mockWorkbook = new SXSSFWorkbook();
  private ByteArrayResource mockBAR = new ByteArrayResource(new ByteArrayOutputStream().toByteArray());
  private Sheet sheet = mockWorkbook.createSheet();
  private Row row = sheet.createRow(0);
  private Cell cell = row.createCell(0);
  private CellStyle cellStyle = mockWorkbook.createCellStyle();

  @Test
  public void crearLibro() {
    // Then
    assertNotNull(excelServiceImpl.crearLibro());
  }

  @Test
  public void crearCabecera() {
    ArrayList<Map<String,Object>> data = new ArrayList();
    Map<String,Object> map = new HashMap<>();
    map.put("Test", 123);
    data.add(map);
    List<Integer> list = new ArrayList();
    list.add(1);
    // Then
    assertEquals(1, excelServiceImpl.crearCabecera(sheet, 0, data, list, cellStyle));
  }

  @Test
  public void crearCabecera2() {
    ArrayList<Map<String,Object>> data = new ArrayList();
    List<String> extra = new ArrayList(Arrays.asList("1", "2"));
    Map<String,Object> map = new HashMap<>();
    map.put("Test", 123);
    data.add(map);
    List<Integer> list = new ArrayList();
    list.add(1);
    list.add(1);
    list.add(1);
    // Then
    assertEquals(1, excelServiceImpl.crearCabecera(sheet, 0, data, extra, list, cellStyle));
  }

  @Test
  public void crearCabecera3() {
    ArrayList<Map<String,Object>> data = new ArrayList();
    Map<String,Object> map1 = new HashMap<>();
    Map<String,Object> map2 = new HashMap<>();
    Map<String,Object> map3 = new HashMap<>();
    List<Integer> skip = new ArrayList<>(Arrays.asList(1));
    map1.put("Test", 123);
    map2.put("Skipper", 123);
    map3.put("Test2",123);
    data.add(map1);
    data.add(map2);
    data.add(map3);
    List<Integer> list = new ArrayList();
    list.add(1);
    list.add(1);
    // Then
    assertEquals(1, excelServiceImpl.crearCabecera(sheet, 0, data, list, cellStyle, skip));
  }

  @Test
  public void cabeceraEstilo() {
    // Then
    assertNotNull(excelServiceImpl.cabeceraEstilo(mockWorkbook));
  }

  @Test
  public void bordeEstilo() {
    // Then
    assertNotNull(excelServiceImpl.bordeEstilo(mockWorkbook));
  }

  @Test
  public void fechaEstilo() {
    // Then
    assertNotNull(excelServiceImpl.fechaEstilo(mockWorkbook));
  }

  @Test
  public void crearResource() {
    // Then
    assertNotNull(excelServiceImpl.crearResource(mockWorkbook));
  }

  @Test
  public void createCellString() {
    // When
    lenient().when(mockRow.createCell(anyInt())).thenReturn(cell);
    // Then
    assertNotNull(excelServiceImpl.createCell(row, 0, "", cellStyle));
  }

  @Test
  public void createCellDate() {
    // When
    lenient().when(mockRow.createCell(anyInt())).thenReturn(cell);
    // Then
    assertNotNull(excelServiceImpl.createCell(row, 0, new Date(), cellStyle));
  }

  @Test
  public void createCellDouble() {
    // When
    lenient().when(mockRow.createCell(anyInt())).thenReturn(cell);
    // Then
    assertNotNull(excelServiceImpl.createCell(row, 0, 1, cellStyle));
  }
}
