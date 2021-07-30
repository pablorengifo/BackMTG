package pe.com.pacifico.kuntur.business.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SegcenRequestTest {

  @InjectMocks
  private SegcenRequest request;

  String bearer = "Bearer bear";

  @Test
  public void shouldObtainAuthRequest() {
    // Then
    Assertions.assertNotNull(request.obtainAuthRequest(bearer));
  }
}
