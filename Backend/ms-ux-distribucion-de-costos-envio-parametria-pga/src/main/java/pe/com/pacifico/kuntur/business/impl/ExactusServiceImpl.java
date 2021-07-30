package pe.com.pacifico.kuntur.business.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.com.pacifico.kuntur.business.ExactusService;
import pe.com.pacifico.kuntur.business.threads.CargarExactusHilo;
import pe.com.pacifico.kuntur.expose.response.ExactusResponse;
import pe.com.pacifico.kuntur.model.Exactus;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.ExactusJpaRepository;
import reactor.core.publisher.Flux;




/**
 * <b>Class</b>: ExactusServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 21, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ExactusServiceImpl implements ExactusService {

  private final ExactusJpaRepository exactusJpaRepository;

  @Value("${date-format-allowed.data-format}")
  private String dateFormatAllowed;

  @Value("${date-format-allowed.data-format-string}")
  private String dateFormatStringAllowed;

  @Override
  public Flux<ExactusResponse> getExactus(int repartoTipo, int periodo) {
    return Flux.fromIterable(exactusJpaRepository.findAll(repartoTipo, periodo))
        .map(this::exactusResponse);
  }

  @Override
  public List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo) {

    CargarExactusHilo.begin(repartoTipo,periodo,requestFileRead,exactusJpaRepository,
        dateFormatStringAllowed,dateFormatAllowed);
    return null;
  }

  private ExactusResponse exactusResponse(Exactus exactus) {

    return ExactusResponse.builder()
        .codCuentaContable(exactus.getCodCuentaContable())
        .nombreCuentaContable(exactus.getNombreCuentaContable())
        .codPartida(exactus.getCodPartida())
        .nombrePartida(exactus.getNombrePartida())
        .codCentro(exactus.getCodCentro())
        .nombreCentro(exactus.getNombreCentro())
        .tipoDocumentoCliente(exactus.getTipoDocumentoCliente())
        .codDocumentoCliente(exactus.getCodDocumentoCliente())
        .cliente(exactus.getCliente())
        .monto(exactus.getMonto())
        .repartoTipo(exactus.getRepartoTipo())
        .periodo(exactus.getPeriodo())
        .asiento(exactus.getAsiento())
        .tipoReferenciaDocumento(exactus.getTipoReferenciaDocumento())
        .documentoContabilizado(exactus.getDocumentoContabilizado())
        .referencia(exactus.getReferencia())
        .fechaContable(exactus.getFechaContable())
        .origenNegocio(exactus.getOrigenNegocio())
        .moneda(exactus.getMoneda())
        .montoMonedaOrigen(exactus.getMontoMonedaOrigen())
        .tipoMovimiento(exactus.getTipoMovimiento())
        .fechaCreacion(exactus.getFechaCreacion())
        .build();
  }

}
