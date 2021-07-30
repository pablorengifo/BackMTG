package pe.com.pacifico.kuntur.business.threads;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.DetalleGastoJpaRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CargarDetalleGastoHiloTest {

  @Mock
  private CargarDetalleGastoHilo mockCargarDetalleGastoHilo = mock(CargarDetalleGastoHilo.class);

  @Mock
  private DetalleGastoJpaRepository detalleJpaRepository = mock(DetalleGastoJpaRepository.class);

  int repartoTipo = 1;
  int periodo = 202001;
  private String dfa = "58|31";
  private String dfsa = "DD/MM/YYYY|m/d/yy|M/D/YY|MM/DD/YY|MM/DD/YYYY|YY-MM-DD|YYYY-MM-DD";

  @Test
  public void shouldCleanHilo() throws IOException {
    // Given
    CargarDetalleGastoHilo.limpiarHilo();
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
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      workbook.write(bos);
    } finally {
      bos.close();
    }
    byte[] bytes = bos.toByteArray();
    RequestFileRead requestFileRead = new RequestFileRead(java.util.Base64.getEncoder().encodeToString(bytes));
    // When
    lenient().doNothing().when(mockCargarDetalleGastoHilo).start();
    // Then
    CargarDetalleGastoHilo.begin(repartoTipo, periodo, requestFileRead, detalleJpaRepository, dfsa, dfa);
  }
}
