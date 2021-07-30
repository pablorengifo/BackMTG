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
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovUoaBbaVfa;
import pe.com.pacifico.kuntur.repository.MovUoaBbaVfaJpaRepository;

/**
 * <b>Class</b>: MovUoaBbaVfaJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     July 14, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MovUoaBbaVfaJdbcRepository implements MovUoaBbaVfaJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<MovUoaBbaVfa> findAll(int repartoTipo, int periodo) {
    try {
      log.info("Resultado MovUoaBbaVfa");
      String queryAllWithNombre = "SELECT Periodo, RepartoTipo, IdPoliza, NumeroPoliza, CodigoUoAPacifico,\n"
          + "DescripcionUoA, CentroCosto, LineaNegocio, ProductoPPTO, CanalDistribucion,\n"
          + "SubCanal, FechaCreacion, FechaActualizacion\n"
          + "FROM MOV_UOA_BBA_VFA\n"
          + "WHERE RepartoTipo = ? AND Periodo = ?";
      return jdbcTemplate.query(queryAllWithNombre, BeanPropertyRowMapper.newInstance(MovUoaBbaVfa.class), repartoTipo, periodo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovUoaBbaVfa", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> findAllCodProducto(int repartoTipo, int periodo) {
    try {
      log.info("Resultado MovProducto en MovUoaBbaVfa");
      String query = "SELECT CodProducto FROM MOV_PRODUCTO WHERE RepartoTipo = ? and Periodo = ?";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo, periodo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovProducto", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> findAllCodSubcanal(int repartoTipo, int periodo) {
    try {
      log.info("Resultado MovSubcanal en MovUoaBbaVfa");
      String query = "SELECT CodSubcanal FROM MOV_SUBCANAL WHERE RepartoTipo = ? and Periodo = ?";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo, periodo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovSubcanal", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> saveExcelToBd(List<List<CellData>> excel, int repartoTipo, int periodo) {
    int numErrores = 0;
    List<String> errores = new ArrayList<>();
    log.info("CANTIDAD FILAS EXCEL (CON HEADERS): " + excel.size());
    //VALIDACION HEADERS
    if (excel.get(0).get(2).getValue().equalsIgnoreCase("IdPoliza")
        && excel.get(0).get(5).getValue().equalsIgnoreCase("NumeroPoliza")
        && excel.get(0).get(6).getValue().equalsIgnoreCase("CodigoUoAPacifico")
        && excel.get(0).get(7).getValue().equalsIgnoreCase("DescripcionUoA")
        && excel.get(0).get(15).getValue().equalsIgnoreCase("CentroCosto")
        && excel.get(0).get(16).getValue().equalsIgnoreCase("LineaNegocio")
        && excel.get(0).get(17).getValue().equalsIgnoreCase("ProductoPPTO")
        && excel.get(0).get(18).getValue().equalsIgnoreCase("CanalDistribucion")
        && excel.get(0).get(19).getValue().equalsIgnoreCase("SubCanal")) {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      List<MovUoaBbaVfa> lstCarga = new ArrayList<>();
      List<String> lstCodigosProducto = findAllCodProducto(repartoTipo, periodo);
      List<String> lstCodigosSubCanal = findAllCodSubcanal(repartoTipo, periodo);
      MovUoaBbaVfa movUoaBbaVfa;
      boolean codigoProductoExiste = false;
      boolean codigoSubcanalExiste = false;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoProductoExiste = false;
        codigoSubcanalExiste = false;
        movUoaBbaVfa = new MovUoaBbaVfa();
        numFila++;

        String codProductoActual = fila.get(17).getValue();

        String codProductoBD = lstCodigosProducto.stream().filter(item -> codProductoActual.equals(item)).findAny().orElse(null);

        String codSubcanalActual = fila.get(19).getValue();

        String codSubcanalBD = lstCodigosSubCanal.stream().filter(item -> codSubcanalActual.equals(item)).findAny().orElse(null);

        //VALIDA QUE CODIGO PRODUCTO SI EXISTA EN BD
        if (codProductoBD != null) {
          codigoProductoExiste = true;
        }

        //VALIDA QUE CODIGO SUBCANAL SI EXISTA EN BD
        if (codSubcanalBD != null) {
          codigoSubcanalExiste = true;
        }

        if (codigoProductoExiste && codigoSubcanalExiste) {
          movUoaBbaVfa.setProductoPpto(codProductoActual);
          movUoaBbaVfa.setSubCanal(codSubcanalActual);
          lstCarga.add(movUoaBbaVfa);
          //lstCodigos.removeIf(x -> x.equals(codSubcanalActual));
        } else {
          numErrores++;
          if (!codigoProductoExiste)
          {
            errores.add("FILA: " + numFila + " con codigo: " + codProductoActual
                + " presenta un Codigo Producto que no existe en el Periodo");
          }
          if (!codigoSubcanalExiste)
          {
            errores.add("FILA: " + numFila + " con codigo: " + codSubcanalActual
                + " presenta un Codigo Subcanal que no existe en el Periodo");
          }
        }
      }
      if (lstCarga.size() > 0) {
        String deleteQuery = "DELETE FROM MOV_UOA_BBA_VFA WHERE RepartoTipo = ? AND Periodo = ?";
        jdbcTemplate.update(deleteQuery, repartoTipo, periodo);
      }

      String sql = "INSERT INTO MOV_UOA_BBA_VFA\n"
          + "(Periodo, RepartoTipo, IdPoliza, NumeroPoliza, CodigoUoAPacifico,\n"
          + "DescripcionUoA, CentroCosto, LineaNegocio, ProductoPPTO, CanalDistribucion,\n"
          + "SubCanal, FechaCreacion, FechaActualizacion)\n"
          + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setInt(1, periodo);//Periodo
            ps.setInt(2, repartoTipo);//RepartoTipo
            ps.setString(3, lstCarga.get(i).getIdPoliza());//IdPoliza
            ps.setString(4, lstCarga.get(i).getNumeroPoliza());//NumeroPoliza
            ps.setString(5, lstCarga.get(i).getCodigoUoAPacifico());//CodigoUoAPacifico
            ps.setString(6, lstCarga.get(i).getDescripcionUoA());//DescripcionUoA
            ps.setString(7, lstCarga.get(i).getCentroCosto());//CentroCosto
            ps.setString(8, lstCarga.get(i).getLineaNegocio());//LineaNegocio
            ps.setString(9, lstCarga.get(i).getProductoPpto());//ProductoPPTO
            ps.setString(10, lstCarga.get(i).getCanalDistribucion());//CanalDistribucion
            ps.setString(11, lstCarga.get(i).getSubCanal());//SubCanal
            ps.setString(12, fecha);//FechaCreacion
            ps.setString(13, fecha);//FechaActualizacion
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
    } else {
      errores.add("Se tiene un error de formato de cabeceras");
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

}
