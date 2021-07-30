package pe.com.pacifico.kuntur.repository.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pe.com.pacifico.kuntur.expose.request.MaProductoRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaProducto;
import pe.com.pacifico.kuntur.model.MovProducto;
import pe.com.pacifico.kuntur.repository.MaProductoJpaRepository;


/**
 * <b>Class</b>: MaProductoJdbcRepository <br/>
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
public class MaProductoJdbcRepository implements MaProductoJpaRepository {
  private final JdbcTemplate jdbcTemplate;

  private static final String querySelect = "SELECT CodProducto, Nombre, FechaActualizacion, "
      + "FechaCreacion, EstaActivo, RepartoTipo FROM MA_PRODUCTO WHERE RepartoTipo = ?";
  private static final String querySelectOption = querySelect + " AND codProducto = ? ";

  @Override
  public List<MaProducto> findAll(int repartoTipo) {
    try {
      log.info("Resultado MaProducto");
      return jdbcTemplate.query(querySelect, BeanPropertyRowMapper.newInstance(MaProducto.class), repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaProducto", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<MaProducto> findAllNotInMovProducto(int repartoTipo, int periodo) {
    try {
      log.info("Resultado Producto");
      String qry = "SELECT ma.codProducto, ma.nombre, "
          + "ma.fechaCreacion, ma.fechaActualizacion, "
          + "ma.repartoTipo, ma.EstaActivo "
          + " FROM MA_PRODUCTO ma LEFT JOIN MOV_PRODUCTO mo"
          + " ON ma.CodProducto = mo.CodProducto"
          + " AND mo.Periodo = ? AND mo.RepartoTipo = ?"
          + " WHERE mo.CodProducto is NULL AND ma.repartoTipo = ?";
      System.out.println("-> Query: " + qry);
      System.out.println("Reparto tipo: " + repartoTipo);
      System.out.println("Periodo: " + periodo);
      List<MaProducto> ma = jdbcTemplate.query(qry, BeanPropertyRowMapper.newInstance(MaProducto.class),
          periodo, repartoTipo, repartoTipo);
      System.out.println("Ma size: " + ma.size());
      return ma;
    } catch (Exception e) { // EmptyResultDataAccessException e
      System.out.println("Exception: " + e);
      log.warn("Resultado vacío en Producto", e);
    }
    System.out.println("Returning new array list...");
    return new ArrayList<>();
  }

  @Override
  public MaProducto findByCodProducto(int repartoTipo, String codigo) {
    try {
      return
          jdbcTemplate.queryForObject(querySelectOption, new Object[]{repartoTipo,codigo},
              BeanPropertyRowMapper.newInstance(MaProducto.class));
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en productos ", e);
      return null;
    }
  }

  @Override
  public int save(MaProductoRequest producto) {
    Pattern str = Pattern.compile("[A-Z0-9]{4,10}");
    if (!str.matcher(producto.getCodProducto()).matches()) {
      return 0;
    }
    return jdbcTemplate.update("insert into MA_PRODUCTO "
            + "(CodProducto, Nombre, FechaActualizacion, FechaCreacion,  RepartoTipo, EstaActivo) "
            + " values (?,?,?,?,?,?)", producto.getCodProducto(), producto.getNombre(), producto.getFechaActualizacion(),
        producto.getFechaCreacion(), producto.getRepartoTipo(), producto.isEstaActivo());

  }

  @Override
  public int update(MaProductoRequest producto) {
    System.out.println(producto);
    return jdbcTemplate.update(
        new StringBuilder()
            .append(" update MA_PRODUCTO ")
            .append(" set Nombre = ? , FechaActualizacion = ?, FechaCreacion = ?, EstaActivo = ?")
            .append(" where repartoTipo = ? AND codProducto = ?").toString(), producto.getNombre(), producto.getFechaActualizacion(),
        producto.getFechaCreacion(), producto.isEstaActivo(), producto.getRepartoTipo(),
        producto.getCodProducto());

  }

  @Override
  public boolean delete(int repartoTipo, String codigo) {
    try {
      String queryExistsInMov = "SELECT top 1 CodProducto FROM MOV_PRODUCTO"
          + " WHERE RepartoTipo = ? AND CodProducto = ? ";
      MovProducto mp = jdbcTemplate.queryForObject(queryExistsInMov, new Object[]{repartoTipo,codigo},
          BeanPropertyRowMapper.newInstance(MovProducto.class));
      log.info("NO ES NULL, NO SE ELIMINA");
      return false;
    } catch (EmptyResultDataAccessException ex) {
      log.info("ES VACIO, SE ELIMINA");
      String deleteQuery = "delete from MA_PRODUCTO where CodProducto = ? AND repartoTipo = ?" ;
      jdbcTemplate.update(deleteQuery, codigo, repartoTipo);
      return true;
    } catch (Exception e) {
      log.info("ERROR");
      return false;
    }
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
  public List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo) {
    List<String> errores = new ArrayList<>();
    int numErrores = 0;
    log.info("CANTIDAD FILAS EXCEL (CON HEADERS): " + excel.size());
    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().toLowerCase().equals("codigo")
        && excel.get(0).get(1).getValue().toLowerCase().equals("nombre"))
    {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      List<MaProducto> lstCarga = new ArrayList<>();
      List<MaProducto> lstUpdate = new ArrayList<>();
      List<String> lstCodigos = findAllCodProducto(repartoTipo);
      MaProducto producto;
      Pattern str = Pattern.compile("[A-Z0-9]{4,10}");
      boolean patronDiferente = false;
      boolean codigoExiste = false;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        patronDiferente = false;
        codigoExiste = false;
        producto = new MaProducto();
        numFila++;

        String codProductoActual = fila.get(0).getValue();

        //VALIDA PATRON
        if (!str.matcher(codProductoActual).matches()) {
          patronDiferente = true;
        }

        String codProductoBD = lstCodigos.stream().filter(item -> codProductoActual.equals(item)).findAny().orElse(null);

        //VALIDA SI CODIGO YA EXISTE EN BD
        if (codProductoBD != null) {
          codigoExiste = true;
        }
        //System.out.println("Num fila: " + numFila + " patrondiferente: " + patronDiferente + " codigoExiste: " + codigoExiste);
        if (!patronDiferente) {
          producto.setCodProducto(codProductoActual);
          producto.setNombre(fila.get(1).getValue());
          if (codigoExiste)
          {
            lstUpdate.add(producto);
          } else
          {
            lstCarga.add(producto);
          }
          lstCodigos.removeIf(x -> x.equals(codProductoActual));
        } else {
          numErrores++;
          errores.add("fila: " + numFila + " con codigo: " + codProductoActual + " no presenta un patron de codigo valido");
        }
      }

      String sql = "INSERT INTO MA_PRODUCTO"
          + " (codProducto, nombre, fechaCreacion, fechaActualizacion, EstaActivo, repartoTipo) "
          + "VALUES " + "(?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstCarga.get(i).getCodProducto());//CodProducto
            ps.setString(2, lstCarga.get(i).getNombre());//Nombre
            ps.setString(3, fecha);//FechaCreacion
            ps.setString(4, fecha);//FechaActualizacion
            ps.setInt(5, 1);//EstaActivo
            ps.setInt(6, repartoTipo);//RepartoTipo
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
      //Luego actualiza
      sql =  "update MA_PRODUCTO\n"
          + "set Nombre=?, FechaActualizacion=?\n"
          + "where RepartoTipo=? and CodProducto=?;\n";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstUpdate.get(i).getNombre());//Nombre
            ps.setString(2, fecha);//FechaActualizacion
            ps.setInt(3, repartoTipo);//RepartoTipo
            ps.setString(4, lstUpdate.get(i).getCodProducto());//CodCanal
          }

          @Override
          public int getBatchSize() {
            return lstUpdate.size();
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
