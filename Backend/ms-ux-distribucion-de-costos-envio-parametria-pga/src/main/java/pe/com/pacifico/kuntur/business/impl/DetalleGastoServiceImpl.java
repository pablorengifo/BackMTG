package pe.com.pacifico.kuntur.business.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.com.pacifico.kuntur.business.DetalleGastoService;
import pe.com.pacifico.kuntur.business.threads.CargarDetalleGastoHilo;
import pe.com.pacifico.kuntur.expose.response.DetalleGastoResponse;
import pe.com.pacifico.kuntur.model.DetalleGasto;
import pe.com.pacifico.kuntur.model.RequestFileRead;
import pe.com.pacifico.kuntur.repository.DetalleGastoJpaRepository;
import reactor.core.publisher.Flux;

/**
 * <b>Class</b>: DetalleGastoServiceImpl <br/>
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
public class DetalleGastoServiceImpl implements DetalleGastoService {

  private final DetalleGastoJpaRepository detalleGastoJpaRepository;

  @Value("${date-format-allowed.data-format}")
  private String dateFormatAllowed;

  @Value("${date-format-allowed.data-format-string}")
  private String dateFormatStringAllowed;

  @Override
  public Flux<DetalleGastoResponse> getDetallesGasto(int repartoTipo, int periodo) {
    return Flux.fromIterable(detalleGastoJpaRepository.findAll(repartoTipo, periodo))
        .map(this::detalleGastoResponse);
  }

  @Override
  public boolean generarDetalleGasto(int repartoTipo, int periodo) {
    CargarDetalleGastoHilo.generar = true;
    CargarDetalleGastoHilo.begin(repartoTipo,periodo,null,detalleGastoJpaRepository,
        dateFormatStringAllowed,dateFormatAllowed);
    return true;
  }

  @Override
  public List<String> fileRead(int repartoTipo, RequestFileRead requestFileRead, int periodo) {

    CargarDetalleGastoHilo.begin(repartoTipo,periodo,requestFileRead,detalleGastoJpaRepository,
        dateFormatStringAllowed,dateFormatAllowed);
    return null;
  }


  private DetalleGastoResponse detalleGastoResponse(DetalleGasto detalleGasto) {

    return DetalleGastoResponse.builder()
        .codCuentaContable(detalleGasto.getCodCuentaContable())
        .nombreCuentaContable(detalleGasto.getNombreCuentaContable())
        .codPartida(detalleGasto.getCodPartida())
        .nombrePartida(detalleGasto.getNombrePartida())
        .codCentro(detalleGasto.getCodCentro())
        .nombreCentro(detalleGasto.getNombreCentro())
        .fechaCreacion(detalleGasto.getFechaCreacion())
        .fechaActualizacion(detalleGasto.getFechaActualizacion())
        .tipoDocumentoCliente(detalleGasto.getTipoDocumentoCliente())
        .codDocumentoCliente(detalleGasto.getCodDocumentoCliente())
        .cliente(detalleGasto.getCliente())
        .periodo(detalleGasto.getPeriodo())
        .monto(detalleGasto.getMonto())
        .build();
  }
}
