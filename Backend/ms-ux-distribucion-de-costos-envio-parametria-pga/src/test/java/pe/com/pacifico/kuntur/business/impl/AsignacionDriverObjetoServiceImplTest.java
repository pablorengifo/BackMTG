package pe.com.pacifico.kuntur.business.impl;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import pe.com.pacifico.kuntur.exception.ValidateException;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverCascadaRequest;
import pe.com.pacifico.kuntur.expose.request.AsignacionDriverObjetoRequest;
import pe.com.pacifico.kuntur.expose.response.AsignacionDriverCascadaResponse;
import pe.com.pacifico.kuntur.expose.response.AsignacionDriverObjetoResponse;
import pe.com.pacifico.kuntur.model.*;
import pe.com.pacifico.kuntur.repository.AsignacionDriverCascadaJpaRepository;
import pe.com.pacifico.kuntur.repository.AsignacionDriverObjetoJpaRepository;
import pe.com.pacifico.kuntur.util.Constant;
import pe.com.pacifico.kuntur.util.ExcelUtil;
import pe.com.pacifico.kuntur.util.FileUtil;
import pe.com.pacifico.kuntur.util.StringUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AsignacionDriverObjetoServiceImplTest {

  @Mock
  private AsignacionDriverObjetoJpaRepository jpaRepository;

  @InjectMocks
  private AsignacionDriverObjetoServiceImpl service;

  //Given
  int repartoTipo=1;
  int periodo = 202105;

  RequestFileRead requestFileRead = new RequestFileRead("prueba");
  AsignacionDriverObjeto asignacion = new AsignacionDriverObjeto();
  AsignacionDriverObjeto asignacion1 = new AsignacionDriverObjeto();
  AsignacionDriverObjeto asignacion2 = new AsignacionDriverObjeto();
  AsignacionDriverObjeto asignacion3 = new AsignacionDriverObjeto();
  AsignacionDriverObjetoRequest request = new AsignacionDriverObjetoRequest();
  String codigo="any";
  String grupoGasto="any";

  @Test
  public void shouldGetAsignaciones() {
    //When
    when(jpaRepository.findAll(repartoTipo, periodo))
        .thenReturn(Arrays.asList(asignacion1,asignacion2,asignacion3));

    //Then
    Flux<AsignacionDriverObjetoResponse> flux= service.getAsignaciones(repartoTipo, periodo);
    StepVerifier.create(flux.log()).expectNextCount(3).verifyComplete();
  }

  @Test
  public void shouldRegisterAsignacion() {
    //When
    //Then
    service.registerAsignacion(request).subscribe(Assertions::assertNotNull);
  }

  @Test
  public void shouldDeleteAsignacion() {
    //When
    when(jpaRepository.delete(repartoTipo, periodo, codigo, grupoGasto)).thenReturn(true);

    //Then
    assertThat(service.deleteAsignacion(repartoTipo,periodo, codigo, grupoGasto), Is.is(true));
  }

  @Test
  public void shouldReadAFile() {
    //When
    //Then
    Assertions.assertNotNull(service.fileRead(repartoTipo,requestFileRead, periodo));
  }

}
