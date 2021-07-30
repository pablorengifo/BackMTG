package pe.com.pacifico.kuntur.repository.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pe.com.pacifico.kuntur.expose.request.AsignacionLineaNiifRequest;
import pe.com.pacifico.kuntur.model.AsignacionLineaNiif;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.repository.AsignacionLineaNiifJpaRepository;

/**
 * <b>Class</b>: AsignacionLineaNiifJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soluciones Digitales <br/>
 * <u>Developed by</u>: prueba <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 22, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class AsignacionLineaNiifJdbcRepository implements AsignacionLineaNiifJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<AsignacionLineaNiif> findAll(int repartoTipo, int periodo) {
    String periodoStr = repartoTipo == 1 ? String.valueOf(periodo) : periodo / 100 + "%" ;
    String query = "select distinct A.CodLinea,B.Nombre as NombreLinea,\n"
        + "COALESCE(C.PorcentajeAtribuible,0) PorcentajeAtribuible,\n"
        + "COALESCE(C.PorcentajeNoAtribuible,0) PorcentajeNoAtribuible\n"
        + "from MOV_PRODUCTO A\n"
        + "join MA_LINEA B on B.CodLinea=A.CodLinea and A.RepartoTipo=B.RepartoTipo\n"
        + "left join LINEA_NIIF C on C.CodLinea=A.CodLinea and C.RepartoTipo=A.RepartoTipo and C.Periodo=A.Periodo\n"
        + "where A.Periodo = ? and A.RepartoTipo=?";
    try {
      return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(AsignacionLineaNiif.class),
          periodoStr, repartoTipo);
    } catch (EmptyResultDataAccessException e)
    {
      log.warn("Resultado vacío en Asignacion Linea Niif ", e);
    }
    return new ArrayList<>();
  }

  @Override
  public boolean delete(int repartoTipo, int periodo, String codLinea) {
    try {
      String deleteQuery = "delete from LINEA_NIIF where Periodo=? and RepartoTipo=? and CodLinea=?";
      jdbcTemplate.update(deleteQuery, periodo, repartoTipo, codLinea);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public int save(AsignacionLineaNiifRequest req) {
    System.out.println("Save: ");
    System.out.println("Request: " + req.toString());
    if (req.getPorcentajeAtribuible() + req.getPorcentajeNoAtribuible() == 100)
    {
      Date date = new Date();
      String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);

      return jdbcTemplate.update("insert into LINEA_NIIF (CodLinea,RepartoTipo,Periodo,PorcentajeAtribuible,\n"
              + "PorcentajeNoAtribuible,FechaCreacion,FechaActualizacion)\n"
              + "values (?,?,?,?,?,?,?)", req.getCodLinea(),req.getRepartoTipo(),req.getPeriodo(),
          req.getPorcentajeAtribuible(), req.getPorcentajeNoAtribuible(), fecha, fecha);
    }
    return -1;
  }

  @Override
  public List<String> saveExcelToBd(List<List<CellData>> excel, int periodo, int repartoTipo) {
    List<String> errores = new ArrayList<>();
    int numErrores = 0;
    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("Codigo")
        && excel.get(0).get(2).getValue().equalsIgnoreCase("Atribuible")
        && excel.get(0).get(3).getValue().equalsIgnoreCase("No atribuible")) {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      List<String> lstCodLineas = findAllLineasInMov(repartoTipo, periodo);

      //System.out.println("Sizes: " + lstCodDriver.size() + " " + lstCodCentro.size() + " " + lstMovCentro.size());

      List<AsignacionLineaNiif> lstCarga = new ArrayList();

      boolean codigoExiste;
      boolean sumaCien;
      boolean valoresNumericos;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoExiste = false;
        sumaCien = false;
        valoresNumericos = true;
        double porcentajeAtribuibleActual = 0;
        double porcentajeNoAtribuibleActual = 0;
        numFila++;
        String[] atribuible = fila.get(2).getValue().split("%");
        String[] noAtribuible = fila.get(3).getValue().split("%");

        String codLineaActual = fila.get(0).getValue();
        try {
          porcentajeAtribuibleActual = Double.parseDouble(atribuible[0]);
          porcentajeNoAtribuibleActual = Double.parseDouble(noAtribuible[0]);
          if (porcentajeAtribuibleActual + porcentajeNoAtribuibleActual == 100) { sumaCien = true; }
        } catch (Exception e) {
          valoresNumericos = false;
        }


        String codLineaBD = lstCodLineas.stream().filter(codLineaActual::equals).findAny().orElse(null);
        if (codLineaBD != null) {
          codigoExiste = true;
        }

        if (codigoExiste && valoresNumericos && sumaCien) {
          AsignacionLineaNiif lineaNiif = new AsignacionLineaNiif();
          lineaNiif.setCodLinea(codLineaActual);
          lineaNiif.setPorcentajeAtribuible(porcentajeAtribuibleActual);
          lineaNiif.setPorcentajeNoAtribuible(porcentajeNoAtribuibleActual);
          lstCarga.add(lineaNiif);
        } else {
          numErrores++;
          String error = "FILA: " + numFila + " - ERROR: " + "CODIGO LINEA '" + codLineaActual + "'";
          if (!codigoExiste) {
            error = error + " no existe en Periodo. ";
          }
          if (!valoresNumericos) {
            error = error + " Presenta porcentajes con valores no numericos.  ";
          }
          if (!sumaCien) {
            error = error + " Los porcentajes no suman 100%.";
          }
          errores.add(error);
        }
      }

      System.out.println("Lst carga size: " + lstCarga.size());

      if (lstCarga.size() > 0) {
        // Limpiar lo viejo
        String deleteQuery = "delete from LINEA_NIIF where RepartoTipo = ? and Periodo = ?";
        try {
          jdbcTemplate.update(deleteQuery, repartoTipo, periodo);
        } catch (Exception e) {
          log.error(e.toString());
        }

        // Subir lo nuevo
        String sql = "insert into LINEA_NIIF (CodLinea,RepartoTipo,Periodo,PorcentajeAtribuible,\n"
            + "PorcentajeNoAtribuible,FechaCreacion,FechaActualizacion)\n"
            + "values (?,?,?,?,?,?,?)";

        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);

        try {
          jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setString(1, lstCarga.get(i).getCodLinea());//getCodCentro
              ps.setInt(2, repartoTipo);//Reparto Tipo
              ps.setInt(3, periodo);//Periodo
              ps.setDouble(4, lstCarga.get(i).getPorcentajeAtribuible()); //Porcentaje
              ps.setDouble(5, lstCarga.get(i).getPorcentajeNoAtribuible()); //Porcentaje
              ps.setString(6, fecha);//Fecha
              ps.setString(7, fecha);//Fecha
            }

            @Override
            public int getBatchSize() {
              return lstCarga.size();
            }
          });
          log.info("SE SUBIERON " + lstCarga.size() + " DE " + excel.size() + " FILAS.");
        } catch (Exception e) {
          log.error(e.toString());
          numErrores = excel.size();
          errores.add("Se tiene un error al registrar el excel. Verifique que los datos sean correctos");
        }
      }
    } else {
      errores.add("Se tiene un error de formato de cabeceras");
      log.info(errores.get(0));
      return errores;
    }
    if (errores.size() > 0) {
      errores.add(0,numErrores + " filas de " + excel.size()
          + " no se subieron por los siguientes errores: ");
      for (String fila : errores) {
        log.info(fila);
      }
      return errores;
    } else { return null; }
  }

  private List<String> findAllLineasInMov(int repartoTipo, int periodo) {
    String periodoStr = repartoTipo == 1 ? String.valueOf(periodo) : periodo / 100 + "%" ;
    try {
      log.info("Resultado DriverCentro");
      String query = "select distinct CodLinea from MOV_PRODUCTO\n"
          + "where Periodo = ? and RepartoTipo=?";
      return jdbcTemplate.queryForList(query, String.class, periodoStr,repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaCentro", e);
    }
    return new ArrayList<>();
  }
}
