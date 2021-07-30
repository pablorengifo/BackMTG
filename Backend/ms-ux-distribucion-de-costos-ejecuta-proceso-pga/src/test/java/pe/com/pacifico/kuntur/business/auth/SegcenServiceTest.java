package pe.com.pacifico.kuntur.business.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SegcenServiceTest {

  String route = "";
  String bearer = "Bearer bear";

  @Mock
  private SegcenRequest request;

  @InjectMocks
  private SegcenService service;

  @Test
  public void shouldCheckAuthOKDate() {
    String route = "date";
    assertTrue(service.checkAuth(request, route, null));
  }

  @Test
  public void shouldFailRequest1() {
    // When
    when(request.obtainAuthRequest(bearer)).thenReturn(null);
    // Then
    assertFalse(service.checkAuth(request, route, bearer));
  }

  @Test
  public void shouldFailRequest2() {
    // When
    when(request.obtainAuthRequest(bearer)).thenReturn("");
    // Then
    assertFalse(service.checkAuth(request, route, bearer));
  }

  @Test
  public void shouldOKRequest() {
    // Given
    String monster = "{\"auditResponse\":{\"idTransaccion\":\"\",\"codigoRespuesta\":\"0\"," +
      "\"mensajeRespuesta\":\"Operación con éxito\"},\"objectResponse\":{\"Objetos\":" +
      "[{\"IdObjeto\":4286,\"IdAplicacion\":154,\"CodigoObjeto\":\"000001\",\"TipoObjeto\":\"01\"," +
      "\"NombreObjeto\":\"mnuMotor\",\"DescripcionAplicacion\":\"Adm Objetos del sistema\",\"Orden\":1," +
      "\"IdObjetoPadre\":0,\"AuditoriaCreacion\":\"t-rmarquina 05/05/2021 04:20:57 p.m.\",\"AuditoriaModificacion\":" +
      "null,\"NombreObjetoPadre\":\"\"},{\"IdObjeto\":4287,\"IdAplicacion\":154,\"CodigoObjeto\":\"000002\",\"TipoObjeto\"" +
      ":\"01\",\"NombreObjeto\":\"mnuInicio\",\"DescripcionAplicacion\":\"Menu Inicio\",\"Orden\":1,\"IdObjetoPadre\":4286," +
      "\"AuditoriaCreacion\":\"t-rmarquina 05/05/2021 04:23:43 p.m.\",\"AuditoriaModificacion\":null,\"NombreObjetoPadre\"" +
      ":\"mnuMotor\"},{\"IdObjeto\":4288,\"IdAplicacion\":154,\"CodigoObjeto\":\"000003\",\"TipoObjeto\":\"01\",\"" +
      "NombreObjeto\":\"mnuParametrizacion\",\"DescripcionAplicacion\":\"Menu Parametrizacion\",\"Orden\":2,\"IdObjetoPadre\"" +
      ":4286,\"AuditoriaCreacion\":\"t-rmarquina 13/05/2021 08:30:44 PM\",\"AuditoriaModificacion\":null,\"NombreObjetoPadre\"" +
      ":\"mnuMotor\"},{\"IdObjeto\":4289,\"IdAplicacion\":154,\"CodigoObjeto\":\"000004\",\"TipoObjeto\":\"01\"," +
      "\"NombreObjeto\":\"mnuAprovisionamiento\",\"DescripcionAplicacion\":\"Menu Aprovisionamiento\",\"Orden\":3," +
      "\"IdObjetoPadre\":4286,\"AuditoriaCreacion\":\"t-rmarquina 13/05/2021 08:30:44 PM\",\"AuditoriaModificacion\"" +
      ":null,\"NombreObjetoPadre\":\"mnuMotor\"},{\"IdObjeto\":4290,\"IdAplicacion\":154,\"CodigoObjeto\":\"000005\"" +
      ",\"TipoObjeto\":\"01\",\"NombreObjeto\":\"mnuAsignaciones\",\"DescripcionAplicacion\":\"Menu Asignaciones\"" +
      ",\"Orden\":4,\"IdObjetoPadre\":4286,\"AuditoriaCreacion\":\"t-rmarquina 13/05/2021 08:30:44 PM" +
      "\",\"AuditoriaModificacion\":null,\"NombreObjetoPadre\":\"mnuMotor\"},{\"IdObjeto\":4291,\"IdAplicacion\"" +
      ":154,\"CodigoObjeto\":\"000006\",\"TipoObjeto\":\"01\",\"NombreObjeto\":\"mnuProcesos\",\"DescripcionAplicacion\"" +
      ":\"Menu Procesos\",\"Orden\":5,\"IdObjetoPadre\":4286,\"AuditoriaCreacion\":\"t-rmarquina 13/05/2021 08:30:44 PM\"" +
      ",\"AuditoriaModificacion\":null,\"NombreObjetoPadre\":\"mnuMotor\"},{\"IdObjeto\":4292,\"IdAplicacion\":154,\"" +
      "CodigoObjeto\":\"000007\",\"TipoObjeto\":\"01\",\"NombreObjeto\":\"mnuReporting\",\"DescripcionAplicacion\":\"" +
      "Menu Reporting\",\"Orden\":6,\"IdObjetoPadre\":4286,\"AuditoriaCreacion\":\"t-rmarquina 13/05/2021 08:30:44 PM" +
      "\",\"AuditoriaModificacion\":null,\"NombreObjetoPadre\":\"mnuMotor\"}],\"Acciones\":[{\"IdAccion\":0,\"IdObjeto" +
      "\":4287,\"CodigoAccion\":\"A00001\",\"NombreAccion\":\"Acceder\",\"SecuenciaAccion\":1,\"Auditable\":false,\"" +
      "IdTipoAccion\":0,\"CodigoObjeto\":\"000002\",\"NombreObjeto\":\"mnuInicio\",\"Permitido\":true},{\"IdAccion\"" +
      ":0,\"IdObjeto\":4288,\"CodigoAccion\":\"A00001\",\"NombreAccion\":\"Acceder\",\"SecuenciaAccion\":1,\"Auditable" +
      "\":false,\"IdTipoAccion\":0,\"CodigoObjeto\":\"000003\",\"NombreObjeto\":\"mnuParametrizacion\",\"Permitido\"" +
      ":true},{\"IdAccion\":0,\"IdObjeto\":4289,\"CodigoAccion\":\"A00001\",\"NombreAccion\":\"Acceder\",\"SecuenciaAccion" +
      "\":1,\"Auditable\":false,\"IdTipoAccion\":0,\"CodigoObjeto\":\"000004\",\"NombreObjeto\":\"mnuAprovisionamiento\"" +
      ",\"Permitido\":true},{\"IdAccion\":0,\"IdObjeto\":4290,\"CodigoAccion\":\"A00001\",\"NombreAccion\":\"Acceder\",\"" +
      "SecuenciaAccion\":1,\"Auditable\":false,\"IdTipoAccion\":0,\"CodigoObjeto\":\"000005\",\"NombreObjeto\":\"" +
      "mnuAsignaciones\",\"Permitido\":true},{\"IdAccion\":0,\"IdObjeto\":4291,\"CodigoAccion\":\"A00001\",\"" +
      "NombreAccion\":\"Acceder\",\"SecuenciaAccion\":1,\"Auditable\":false,\"IdTipoAccion\":0,\"CodigoObjeto\":" +
      "\"000006\",\"NombreObjeto\":\"mnuProcesos\",\"Permitido\":true},{\"IdAccion\":0,\"IdObjeto\":4292,\"CodigoAccion\"" +
      ":\"A00001\",\"NombreAccion\":\"Acceder\",\"SecuenciaAccion\":1,\"Auditable\":false,\"IdTipoAccion\":0,\"CodigoObjeto\"" +
      ":\"000007\",\"NombreObjeto\":\"mnuReporting\",\"Permitido\":true},{\"IdAccion\":0,\"IdObjeto\":4287,\"CodigoAccion\":\"" +
      "A00002\",\"NombreAccion\":\"Editar\",\"SecuenciaAccion\":2,\"Auditable\":false,\"IdTipoAccion\":0,\"CodigoObjeto\":" +
      "\"000002\",\"NombreObjeto\":\"mnuInicio\",\"Permitido\":true},{\"IdAccion\":0,\"IdObjeto\":4288,\"CodigoAccion\":\"" +
      "A00002\",\"NombreAccion\":\"Editar\",\"SecuenciaAccion\":2,\"Auditable\":false,\"IdTipoAccion\":0,\"CodigoObjeto\":" +
      "\"000003\",\"NombreObjeto\":\"mnuParametrizacion\",\"Permitido\":true},{\"IdAccion\":0,\"IdObjeto\":4289,\"" +
      "CodigoAccion\":\"A00002\",\"NombreAccion\":\"Editar\",\"SecuenciaAccion\":2,\"Auditable\":false,\"IdTipoAccion" +
      "\":0,\"CodigoObjeto\":\"000004\",\"NombreObjeto\":\"mnuAprovisionamiento\",\"Permitido\":true},{\"IdAccion\":0," +
      "\"IdObjeto\":4290,\"CodigoAccion\":\"A00002\",\"NombreAccion\":\"Editar\",\"SecuenciaAccion\":2,\"Auditable\":false," +
      "\"IdTipoAccion\":0,\"CodigoObjeto\":\"000005\",\"NombreObjeto\":\"mnuAsignaciones\",\"Permitido\":true},{\"IdAccion\":0," +
      "\"IdObjeto\":4291,\"CodigoAccion\":\"A00002\",\"NombreAccion\":\"Editar\",\"SecuenciaAccion\":2,\"Auditable\":false,\"" +
      "IdTipoAccion\":0,\"CodigoObjeto\":\"000006\",\"NombreObjeto\":\"mnuProcesos\",\"Permitido\":true},{\"IdAccion\":0,\"" +
      "IdObjeto\":4292,\"CodigoAccion\":\"A00002\",\"NombreAccion\":\"Editar\",\"SecuenciaAccion\":2,\"Auditable\":false,\"" +
      "IdTipoAccion\":0,\"CodigoObjeto\":\"000007\",\"NombreObjeto\":\"mnuReporting\",\"Permitido\":true},{\"IdAccion\":0," +
      "\"IdObjeto\":4287,\"CodigoAccion\":\"A00003\",\"NombreAccion\":\"Actuals\",\"SecuenciaAccion\":3,\"Auditable\":false," +
      "\"IdTipoAccion\":0,\"CodigoObjeto\":\"000002\",\"NombreObjeto\":\"mnuInicio\",\"Permitido\":true}]}}";
    // When
    when(request.obtainAuthRequest(bearer)).thenReturn(monster);
    // Then
    assertTrue(service.checkAuth(request, route, bearer));
  }

}
