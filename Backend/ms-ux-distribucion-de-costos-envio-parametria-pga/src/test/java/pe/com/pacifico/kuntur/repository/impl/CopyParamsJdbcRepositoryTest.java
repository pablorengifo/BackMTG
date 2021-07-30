package pe.com.pacifico.kuntur.repository.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CopyParamsJdbcRepositoryTest {
  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private CopyParamsJdbcRepository jdbcRepository;

  //Given
  int repartoTipo=1;
  int periodo1 = 202104;
  int periodo2 = 202106;

  @Test
  public void shouldCopyMovCuentaContable(){
    when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenReturn(1);
    when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenReturn(1);

    assertTrue(jdbcRepository.copyMovCuentaContable(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldNotCopyMovCuentaContable(){
    lenient().when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenThrow(NullPointerException.class);
    lenient().when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenThrow(NullPointerException.class);

    assertFalse(jdbcRepository.copyMovCuentaContable(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldCopyMovPartida(){
    when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenReturn(1);
    when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenReturn(1);

    assertTrue(jdbcRepository.copyMovPartida(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldNotCopyMovPartida(){
    lenient().when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenThrow(NullPointerException.class);
    lenient().when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenThrow(NullPointerException.class);

    assertFalse(jdbcRepository.copyMovPartida(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldCopyMovCentro(){
    when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenReturn(1);
    when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenReturn(1);

    assertTrue(jdbcRepository.copyMovCentro(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldNotCopyMovCentro(){
    lenient().when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenThrow(NullPointerException.class);
    lenient().when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenThrow(NullPointerException.class);

    assertFalse(jdbcRepository.copyMovCentro(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldCopyMovProducto(){
    when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenReturn(1);
    when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenReturn(1);

    assertTrue(jdbcRepository.copyMovProducto(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldNotCopyMovProducto(){
    lenient().when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenThrow(NullPointerException.class);
    lenient().when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenThrow(NullPointerException.class);

    assertFalse(jdbcRepository.copyMovProducto(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldCopyMovSubcanal(){
    when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenReturn(1);
    when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenReturn(1);

    assertTrue(jdbcRepository.copyMovSubcanal(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldNotCopyMovSubcanal(){
    lenient().when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenThrow(NullPointerException.class);
    lenient().when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenThrow(NullPointerException.class);

    assertFalse(jdbcRepository.copyMovSubcanal(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldCopyMovDriverCentro(){
    when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenReturn(1);
    when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenReturn(1);

    assertTrue(jdbcRepository.copyMovDriverCentro(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldNotCopyMovDriverCentro(){
    lenient().when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenThrow(NullPointerException.class);
    lenient().when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenThrow(NullPointerException.class);

    assertFalse(jdbcRepository.copyMovDriverCentro(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldCopyMovDriverObjeto(){
    when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenReturn(1);
    when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenReturn(1);

    assertTrue(jdbcRepository.copyMovDriverObjeto(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldNotCopyMovDriverObjeto(){
    lenient().when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenThrow(NullPointerException.class);
    lenient().when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenThrow(NullPointerException.class);

    assertFalse(jdbcRepository.copyMovDriverObjeto(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldCopyAsignacionCecoBolsa(){
    when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenReturn(1);
    when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenReturn(1);

    assertTrue(jdbcRepository.copyAsignacionCecoBolsa(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldNotCopyAsignacionCecoBolsa(){
    lenient().when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenThrow(NullPointerException.class);
    lenient().when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenThrow(NullPointerException.class);

    assertFalse(jdbcRepository.copyAsignacionCecoBolsa(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldCopyAsignacionDriverCascada(){
    when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenReturn(1);
    when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenReturn(1);

    assertTrue(jdbcRepository.copyAsignacionDriverCascada(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldNotCopyAsignacionDriverCascada(){
    lenient().when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenThrow(NullPointerException.class);
    lenient().when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenThrow(NullPointerException.class);

    assertFalse(jdbcRepository.copyAsignacionDriverCascada(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldCopyAsignacionDriverObjeto(){
    when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenReturn(1);
    when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenReturn(1);

    assertTrue(jdbcRepository.copyAsignacionDriverObjeto(repartoTipo,periodo1,periodo2));
  }

  @Test
  public void shouldNotCopyAsignacionDriverObjeto(){
    lenient().when( jdbcTemplate.update(anyString(),anyInt(),anyInt())).thenThrow(NullPointerException.class);
    lenient().when(jdbcTemplate.update(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), anyInt(), anyInt()))
        .thenThrow(NullPointerException.class);

    assertFalse(jdbcRepository.copyAsignacionDriverObjeto(repartoTipo,periodo1,periodo2));
  }

}
