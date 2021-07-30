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
import pe.com.pacifico.kuntur.expose.request.MaLineaRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaLinea;
import pe.com.pacifico.kuntur.model.MovProducto;
import pe.com.pacifico.kuntur.repository.MaLineaJpaRepository;


/**
 * <b>Class</b>: MaLineaJdbcRepository <br/>
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
@SuppressWarnings("checkstyle:LineLength")
@Repository
@RequiredArgsConstructor
@Slf4j
public class MaLineaJdbcRepository implements MaLineaJpaRepository {
  private final JdbcTemplate jdbcTemplate;

  private static final String querySelect = "SELECT CodLinea, Nombre, FechaActualizacion, FechaCreacion, EstaActivo, "
      + "RepartoTipo FROM MA_LINEA WHERE RepartoTipo = ?";
  private static final String querySelectOption = querySelect + " AND codLinea = ? ";

  @Override
  public List<MaLinea> findAll(int repartoTipo) {
    try {
      log.info("Resultado MaLinea");
      return jdbcTemplate.query(querySelect, BeanPropertyRowMapper.newInstance(MaLinea.class), repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaLinea", e);
    }
    return new ArrayList<>();
  }

  @Override
  public MaLinea findByCodLinea(int repartoTipo, String codigo) {
    try {
      return
          jdbcTemplate.queryForObject(querySelectOption, new Object[]{repartoTipo,codigo},
              BeanPropertyRowMapper.newInstance(MaLinea.class));
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en lineas ", e);
      return null;
    }
  }

  @Override
  public int save(MaLineaRequest linea) {
    Pattern str = Pattern.compile("[A-Z]{3,4}|[A-Z]{3,4}[0-9]{4}");
    if (!str.matcher(linea.getCodLinea()).matches() && !linea.getCodLinea().equals("SIN LINEA")) {
      return 0;
    }
    return jdbcTemplate.update("insert into MA_LINEA (CodLinea, Nombre, FechaActualizacion, FechaCreacion,  RepartoTipo, EstaActivo) "
            + " values (?,?,?,?,?,?)", linea.getCodLinea(), linea.getNombre(), linea.getFechaActualizacion(),
        linea.getFechaCreacion(), linea.getRepartoTipo(), linea.isEstaActivo());

  }

  @Override
  public int update(MaLineaRequest linea) {
    System.out.println(linea);
    return jdbcTemplate.update(
        new StringBuilder()
            .append(" update MA_LINEA ")
            .append(" set Nombre = ? , FechaActualizacion = ?, FechaCreacion = ?, EstaActivo = ?")
            .append(" where repartoTipo = ? AND codLinea = ?").toString(), linea.getNombre(), linea.getFechaActualizacion(),
        linea.getFechaCreacion(), linea.isEstaActivo(), linea.getRepartoTipo(),
        linea.getCodLinea());

  }

  @Override
  public boolean delete(int repartoTipo, String codigo) {
    try {
      String queryExistsInMovProducto = "SELECT top 1 CodLinea FROM MOV_PRODUCTO"
          + " WHERE RepartoTipo = ? AND CodLinea = ? ";
      MovProducto mp = jdbcTemplate.queryForObject(queryExistsInMovProducto, new Object[]{repartoTipo,codigo},
          BeanPropertyRowMapper.newInstance(MovProducto.class));
      log.info("NO ES NULL, NO SE ELIMINA");
      return false;
    } catch (EmptyResultDataAccessException ex) {
      log.info("ES VACIO, SE ELIMINA");
      String deleteQuery = "delete from MA_LINEA where CodLinea = ? AND RepartoTipo = ?" ;
      jdbcTemplate.update(deleteQuery, codigo, repartoTipo);
      return true;
    } catch (Exception e) {
      log.info("ERROR");
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
  public List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo) {
    int numErrores = 0;
    List<String> errores = new ArrayList<>();
    log.info("CANTIDAD FILAS EXCEL (CON HEADERS): " + excel.size());
    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().toLowerCase().equals("codigo")
        && excel.get(0).get(1).getValue().toLowerCase().equals("nombre"))
    {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      Pattern str = Pattern.compile("[A-Z]{3,4}|[A-Z]{3,4}[0-9]{4}");
      List<MaLinea> lstCarga = new ArrayList<>();
      List<MaLinea> lstUpdate = new ArrayList<>();
      List<String> lstCodigos = findAllCodLinea(repartoTipo);
      MaLinea linea;
      boolean codigoExiste = false;
      boolean patronDiferente = false;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoExiste = false;
        patronDiferente = false;
        linea = new MaLinea();
        numFila++;

        String codLineaActual = fila.get(0).getValue();

        String codLineaBD = lstCodigos.stream().filter(item -> codLineaActual.equals(item)).findAny().orElse(null);

        //VALIDA PATRON
        if (!str.matcher(codLineaActual).matches() && !codLineaActual.equals("SIN LINEA")) {
          patronDiferente = true;
        }
        //VALIDA SI CODIGO YA EXISTE EN BD
        if (codLineaBD != null) {
          codigoExiste = true;
        }

        if (!patronDiferente) {
          linea.setCodLinea(codLineaActual);
          linea.setNombre(fila.get(1).getValue());
          if (codigoExiste)
          {
            lstUpdate.add(linea);
          } else
          {
            lstCarga.add(linea);
          }
          lstCodigos.removeIf(x -> x.equals(codLineaActual));
        } else {
          numErrores++;
          errores.add("fila: " + numFila + " con codigo: " + codLineaActual + " no presenta un patron de codigo valido");
        }
      }

      String sql = "INSERT INTO MA_LINEA"
          + " (codLinea, nombre, fechaCreacion, fechaActualizacion, EstaActivo, repartoTipo) "
          + "VALUES " + "(?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstCarga.get(i).getCodLinea());//CodLinea
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
      sql =  "update MA_LINEA\n"
          + "set Nombre=?, FechaActualizacion=?\n"
          + "where RepartoTipo=? and codLinea=?";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstUpdate.get(i).getNombre());//Nombre
            ps.setString(2, fecha);//FechaActualizacion
            ps.setInt(3, repartoTipo);//RepartoTipo
            ps.setString(4, lstUpdate.get(i).getCodLinea());//CodCanal
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
