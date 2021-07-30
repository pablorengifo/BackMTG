package pe.com.pacifico.kuntur.business.threads;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.kuntur.repository.ProcesoJpaRepoository;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class HiloEjecucionTotalTest {

    @Mock
    private HiloEjecucionTotal mockHiloTotal = mock(HiloEjecucionTotal.class);

    @Mock
    private ProcesoJpaRepoository procesoJpaRepository = mock(ProcesoJpaRepoository.class);

    private int periodo = 202001;
    private int repartoTipo_R = 1;
    String ue = "";
    String un = "";

    @Test
    public void shouldBegin(){
        // When
        lenient().doNothing().when(mockHiloTotal).start();
        // Then
        HiloEjecucionTotal.begin(procesoJpaRepository, repartoTipo_R, periodo, ue, un);
    }
}
