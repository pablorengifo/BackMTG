package pe.com.pacifico.kuntur.repository.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pe.com.pacifico.kuntur.expose.request.MovProductoRequest;
import pe.com.pacifico.kuntur.expose.response.MovProductoResponse;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovProducto;
import pe.com.pacifico.kuntur.repository.MovProductoJpaRepository;

/**
 * <b>Class</b>: MovProductoJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Pablo Rengifo <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 7, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MovProductoJdbcRepository implements MovProductoJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<MovProductoResponse> findAll(int repartoTipo, int periodo) {
    try {
      log.info("Resultado MovProducto");
      String queryAllWithNombre = ""
          + "SELECT mo.CodProducto, mo.CodLinea, mo.Periodo,"
          + " mo.FechaActualizacion, mo.FechaCreacion,"
          + " mo.RepartoTipo, ma.Nombre, ml.Nombre as 'NombreLinea'"
          + " FROM MOV_PRODUCTO mo"
          + " JOIN MA_PRODUCTO ma ON mo.CodProducto = ma.CodProducto"
          + " AND mo.RepartoTipo = ma.RepartoTipo"
          + " JOIN MA_LINEA ml ON mo.CodLinea = ml.CodLinea"
          + " AND mo.RepartoTipo = ml.RepartoTipo"
          + " where mo.RepartoTipo = ? AND mo.Periodo = ?";
      return jdbcTemplate.query(queryAllWithNombre, BeanPropertyRowMapper.newInstance(MovProductoResponse.class), repartoTipo, periodo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovProducto", e);
    }
    return new ArrayList<>();
  }

  @Override
  public int save(MovProductoRequest producto) {
    int result = 0;
    try {
      result = jdbcTemplate.update("insert into MOV_PRODUCTO "
              + "(CodProducto, Periodo, RepartoTipo, "
              + "FechaCreacion, FechaActualizacion, CodLinea) "
              + " values (?,?,?,?,?,?)", producto.getCodProducto(), producto.getPeriodo(),
          producto.getRepartoTipo(), producto.getFechaCreacion(), producto.getFechaActualizacion(),
          producto.getCodLinea());
    } catch (DuplicateKeyException e) {
      result = -1;
      log.warn(e.toString());
    } catch (Exception e) {
      log.warn(e.toString());
      result = -2;
    }
    log.info("Result save = " + result);
    return result;
  }

  @Override
  public boolean delete(int repartoTipo, int periodo, String codigo) {
    try {
      String deleteQuery = "delete from MOV_PRODUCTO where repartoTipo = ? AND periodo = ? AND CodProducto = ?";
      jdbcTemplate.update(deleteQuery, repartoTipo, periodo, codigo);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public List<String> findAllCodLinea(int repartoTipo) {
    try {
      log.info("Resultado MaLinea");
      String query = "SELECT CodLinea FROM MA_LINEA WHERE RepartoTipo = ?";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaLinea", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> findAllCodProducto(int repartoTipo) {
    try {
      log.info("Resultado MaProducto");
      String query = "SELECT CodProducto FROM MA_PRODUCTO WHERE RepartoTipo = ?";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaProducto", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo, int periodo) {
    List<String> errores = new ArrayList<>();
    int numErrores = 0;
    log.info("CANTIDAD FILAS EXCEL (CON HEADERS): " + excel.size());
    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().toLowerCase().equals("codigo")
        && excel.get(0).get(3).getValue().toLowerCase().equals("codigo padre"))
    {
      System.out.println("REMOVER HEADERS PARA CARGAR INFO");
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      System.out.println("PARAMETROS CARGA");
      List<MovProducto> lstCarga = new ArrayList<>();
      List<String> lstCodigos = findAllCodProducto(repartoTipo);
      List<String> lstCodigosLinea = findAllCodLinea(repartoTipo);
      MovProducto movProducto;
      boolean codigoExiste = false;
      boolean codigoLineaExiste = false;
      int numFila = 0;
      System.out.println("VALIDACIONES DE FILAS A CARGAR");
      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoExiste = false;
        codigoLineaExiste = false;
        movProducto = new MovProducto();
        numFila++;

        String codProductoActual = fila.get(0).getValue();

        String codPartidaBD = lstCodigos.stream().filter(item -> codProductoActual.equals(item)).findAny().orElse(null);

        String codLineaActual = fila.get(3).getValue();

        String codLineaBD = lstCodigosLinea.stream().filter(item -> codLineaActual.equals(item)).findAny().orElse(null);

        //VALIDA QUE CODIGO SI EXISTA EN BD
        if (codPartidaBD != null) {
          codigoExiste = true;
        }

        //VALIDA QUE CODIGO LINEA SI EXISTA EN BD
        if (codLineaBD != null) {
          codigoLineaExiste = true;
        }

        if (codigoExiste && codigoLineaExiste) {
          movProducto.setCodProducto(codProductoActual);
          movProducto.setCodLinea(codLineaActual);
          lstCarga.add(movProducto);
          //lstCodigos.removeIf(x -> x.equals(codPartidaActual));
        } else {
          numErrores++;
          if (!codigoExiste)
          {
            errores.add("FILA: " + numFila + " con codigo: " + codProductoActual
                + " presenta un codigo que no existe en Catalogo de productos");
          }
          if (!codigoLineaExiste)
          {
            errores.add("FILA: " + numFila + " con codigo: " + codProductoActual
                + " presenta un codigo de linea: " + codLineaActual + " que no existe en Catalogo de lineas");
          }
        }
      }
      if (lstCarga.size() > 0) {
        String deleteQuery = "DELETE FROM MOV_PRODUCTO WHERE RepartoTipo = ? AND Periodo = ?";
        jdbcTemplate.update(deleteQuery, repartoTipo, periodo);
      }

      String sql = "INSERT INTO MOV_PRODUCTO"
          + " (CodProducto, Periodo, RepartoTipo,"
          + " FechaCreacion, FechaActualizacion, CodLinea)"
          + " VALUES " + "(?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstCarga.get(i).getCodProducto());//CodProducto
            ps.setInt(2,periodo);//Periodo
            ps.setInt(3, repartoTipo);//RepartoTipo
            ps.setString(4, fecha);//FechaCreacion
            ps.setString(5, fecha);//FechaActualizacion
            ps.setString(6, lstCarga.get(i).getCodLinea());//CodLinea
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
