package pe.com.pacifico.kuntur.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.com.pacifico.kuntur.business.CopyParamsService;
import pe.com.pacifico.kuntur.repository.CopyParamsJpaRepository;

/**
 * <b>Class</b>: CopyParamsServiceImpl <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 14, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CopyParamsServiceImpl implements CopyParamsService {

  private final CopyParamsJpaRepository copyParamsJpaRepository;

  @Override
  public boolean copyParams(int repartoTipo, int periodoOri, int periodoDest) {

    log.info("INICIO COPIA PARAMETROS PERIODO " + periodoOri + " A " + periodoDest);
    boolean sinError;
    //Copia Mov
    //Parametrizacion
    //- MovCuentas
    log.info("INICIO COPIA CUENTAS CONTABLES");
    sinError = copyParamsJpaRepository.copyMovCuentaContable(repartoTipo, periodoOri, periodoDest);
    if (sinError) {
      log.info("FIN COPIA CUENTAS CONTABLES");
    } else {
      log.info("ERROR AL COPIAR CUENTAS CONTABLES");
    }
    //- MovPartidas
    log.info("INICIO COPIA PARTIDAS");
    sinError = copyParamsJpaRepository.copyMovPartida(repartoTipo, periodoOri, periodoDest);
    if (sinError) {
      log.info("FIN COPIA PARTIDAS");
    } else {
      log.info("ERROR AL COPIAR PARTIDAS");
    }
    //- MovCentros
    log.info("INICIO COPIA CENTROS");
    sinError = copyParamsJpaRepository.copyMovCentro(repartoTipo, periodoOri, periodoDest);
    if (sinError) {
      log.info("FIN COPIA CENTROS");
    } else {
      log.info("ERROR AL COPIAR CENTROS");
    }
    //- MovProductos
    log.info("INICIO COPIA PRODUCTOS");
    sinError = copyParamsJpaRepository.copyMovProducto(repartoTipo, periodoOri, periodoDest);
    if (sinError) {
      log.info("FIN COPIA PRODUCTOS");
    } else {
      log.info("ERROR AL COPIAR PRODUCTOS");
    }
    //- MovSubcanales
    log.info("INICIO COPIA SUBCANALES");
    sinError = copyParamsJpaRepository.copyMovSubcanal(repartoTipo, periodoOri, periodoDest);
    if (sinError) {
      log.info("FIN COPIA SUBCANALES");
    } else {
      log.info("ERROR AL COPIAR SUBCANALES");
    }
    //Aprovisionamiento
    //- Drivers centro
    log.info("INICIO COPIA DRIVERS CENTROS");
    sinError = copyParamsJpaRepository.copyMovDriverCentro(repartoTipo, periodoOri, periodoDest);
    if (sinError) {
      log.info("FIN COPIA DRIVERS CENTROS");
    } else {
      log.info("ERROR AL COPIAR DRIVERS CENTROS");
    }
    //- Drivers objeto
    log.info("INICIO COPIA DRIVERS OBJETOS");
    sinError = copyParamsJpaRepository.copyMovDriverObjeto(repartoTipo, periodoOri, periodoDest);
    if (sinError) {
      log.info("FIN COPIA DRIVERS OBJETOS");
    } else {
      log.info("ERROR AL COPIAR DRIVERS OBJETOS");
    }
    //Asignaciones
    //- Fase 1 - Bolsas
    log.info("INICIO COPIA ASIGNACIONES CECO BOLSAS");
    sinError = copyParamsJpaRepository.copyAsignacionCecoBolsa(repartoTipo, periodoOri, periodoDest);
    if (sinError) {
      log.info("FIN COPIA ASIGNACIONES CECO BOLSAS");
    } else {
      log.info("ERROR AL COPIAR ASIGNACIONES CECO BOLSAS");
    }
    //- Fase 2 - Cascada
    log.info("INICIO COPIA ASIGNACIONES DRIVER CASCADA");
    sinError = copyParamsJpaRepository.copyAsignacionDriverCascada(repartoTipo, periodoOri, periodoDest);
    if (sinError) {
      log.info("FIN COPIA ASIGNACIONES DRIVER CASCADA");
    } else {
      log.info("ERROR AL COPIAR ASIGNACIONES DRIVER CASCADA");
    }
    //- Fase 3 - Objetos
    log.info("INICIO COPIA ASIGNACIONES DRIVER OBJETO");
    sinError = copyParamsJpaRepository.copyAsignacionDriverObjeto(repartoTipo, periodoOri, periodoDest);
    if (sinError) {
      log.info("FIN COPIA ASIGNACIONES DRIVER OBJETO");
    } else {
      log.info("ERROR AL COPIAR ASIGNACIONES DRIVER OBJETO");
    }
    log.info("FIN COPIA PARAMETROS PERIODO " + periodoOri + " A " + periodoDest);
    return true;
  }
}
