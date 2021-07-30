package pe.com.pacifico.kuntur.business.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SegcenInterceptorTest {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private SegcenService service;

  @InjectMocks
  private SegcenInterceptor segcenInterceptor;

  @Test
  public void shouldPreHandleOptions() {
    // When
    when(request.getMethod()).thenReturn("options");
    // Then
    assertTrue(segcenInterceptor.preHandle(request, response, new Object()));
  }

  @Test
  public void shouldPreHandleBearerError() {
    // When
    when(request.getMethod()).thenReturn("put");
    when(request.getHeader("Authorization")).thenReturn("test");
    // Then
    assertFalse(segcenInterceptor.preHandle(request, response, new Object()));
  }

  @Test
  public void shouldPreHandleBearerNull() {
    // When
    when(request.getMethod()).thenReturn("put");
    when(request.getHeader("Authorization")).thenReturn(null);
    // Then
    assertFalse(segcenInterceptor.preHandle(request, response, new Object()));
  }

  @Test
  public void shouldPreHandleAuthError() {
    // When
    when(request.getMethod()).thenReturn("put");
    when(request.getHeader("Authorization")).thenReturn("Bearer test");
    when(request.getServletPath()).thenReturn("/test");
    when(service.checkAuth(any(), anyString(), anyString())).thenReturn(false);
    // Then
    assertFalse(segcenInterceptor.preHandle(request, response, new Object()));
  }

  @Test
  public void shouldPreHandleAuthException() {
    // When
    when(request.getMethod()).thenReturn("put");
    when(request.getHeader("Authorization")).thenReturn("Bearer test");
    when(request.getServletPath()).thenReturn("/test");
    when(service.checkAuth(any(), anyString(), anyString())).thenReturn(false);
    //when(response.sendError(anyInt())).thenThrow(MockitoException.class);
    try {
      doThrow(new IOException()).when(response).sendError(anyInt());
    } catch (Exception e) {
      e.printStackTrace();
    }
    // Then
    System.out.println("Doing then...");
    assertFalse(segcenInterceptor.preHandle(request, response, new Object()));
  }


  @Test
  public void shouldPreHandleOK() {
    // When
    when(request.getMethod()).thenReturn("put");
    when(request.getHeader("Authorization")).thenReturn("Bearer test");
    when(request.getServletPath()).thenReturn("/test");
    when(service.checkAuth(any(), anyString(), anyString())).thenReturn(true);
    // Then
    assertTrue(segcenInterceptor.preHandle(request, response, new Object()));
  }
}
