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
import pe.com.pacifico.kuntur.expose.request.MaSubcanalRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaSubcanal;
import pe.com.pacifico.kuntur.model.MovSubcanal;
import pe.com.pacifico.kuntur.repository.MaSubcanalJpaRepository;

/**
 * <b>Class</b>: MaSubcanalJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 16, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MaSubcanalJdbcRepository implements MaSubcanalJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  private static final String querySelect = "SELECT CodSubcanal, RepartoTipo, Nombre, EstaActivo, FechaCreacion, "
      + "FechaActualizacion FROM MA_SUBCANAL WHERE RepartoTipo = ?";
  private static final String querySelectOption = querySelect + " AND CodSubcanal = ? ";

  @Override
  public List<MaSubcanal> findAll(int repartoTipo) {
    try {
      log.info("Resultado MaSubcanal");
      return jdbcTemplate.query(querySelect, BeanPropertyRowMapper.newInstance(MaSubcanal.class), repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaSubcanal", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<MaSubcanal> findAllNotInMovSubcanal(int repartoTipo, int periodo) {
    try {
      log.info("Resultado Subcanal");
      String qry = "SELECT ma.CodSubcanal, ma.RepartoTipo, ma.Nombre, ma.EstaActivo, ma.FechaCreacion,"
          + " ma.FechaActualizacion"
          + " FROM MA_SUBCANAL ma LEFT JOIN MOV_SUBCANAL mo"
          + " ON ma.CodSubcanal = mo.CodSubcanal"
          + " AND mo.Periodo = ? AND mo.RepartoTipo = ?"
          + " WHERE mo.CodSubcanal is NULL AND ma.repartoTipo = ?";
      return jdbcTemplate.query(qry, BeanPropertyRowMapper.newInstance(MaSubcanal.class),
          periodo, repartoTipo, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en Subcanal", e);
    }
    return new ArrayList<>();
  }

  @Override
  public MaSubcanal findByCodSubcanal(int repartoTipo, String codigo) {
    try {
      return
          jdbcTemplate.queryForObject(querySelectOption, new Object[]{repartoTipo,codigo},
              BeanPropertyRowMapper.newInstance(MaSubcanal.class));
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en subcanales ", e);
      return null;
    }
  }

  @Override
  public int save(MaSubcanalRequest subcanal) {
    Pattern str = Pattern.compile("[A-Z0-9]{6}");
    if (!str.matcher(subcanal.getCodSubcanal()).matches()) {
      return 0;
    }
    return jdbcTemplate.update("insert into MA_SUBCANAL (CodSubcanal, RepartoTipo, Nombre, EstaActivo, FechaCreacion, "
            + "FechaActualizacion) "
            + " values (?,?,?,?,?,?)", subcanal.getCodSubcanal(), subcanal.getRepartoTipo(), subcanal.getNombre(),
        subcanal.isEstaActivo(), subcanal.getFechaCreacion(), subcanal.getFechaActualizacion());

  }

  @Override
  public int update(MaSubcanalRequest subcanal) {
    System.out.println(subcanal);
    return jdbcTemplate.update(
        new StringBuilder()
            .append(" update MA_SUBCANAL ")
            .append(" set Nombre = ? , EstaActivo = ? , FechaCreacion = ?,  FechaActualizacion = ?")
            .append(" where RepartoTipo = ? AND CodSubcanal = ?").toString(), subcanal.getNombre(), subcanal.isEstaActivo(),
        subcanal.getFechaCreacion(), subcanal.getFechaActualizacion(), subcanal.getRepartoTipo(),
        subcanal.getCodSubcanal());

  }

  @Override
  public boolean delete(int repartoTipo, String codigo) {
    try {
      String queryExistsInMov = "SELECT top 1 CodSubcanal FROM MOV_SUBCANAL"
          + " WHERE RepartoTipo = ? AND CodSubcanal = ? ";
      MovSubcanal ms = jdbcTemplate.queryForObject(queryExistsInMov, new Object[]{repartoTipo,codigo},
          BeanPropertyRowMapper.newInstance(MovSubcanal.class));
      log.info("NO ES NULL, NO SE ELIMINA");
      return false;
    } catch (EmptyResultDataAccessException ex) {
      log.info("ES VACIO, SE ELIMINA");
      String deleteQuery = "delete from MA_SUBCANAL where CodSubcanal = ? AND repartoTipo = ?" ;
      jdbcTemplate.update(deleteQuery, codigo, repartoTipo);
      return true;
    } catch (Exception e) {
      log.info("ERROR");
      return false;
    }
  }

  @Override
  public List<String> findAllCodSubcanal(int repartoTipo) {
    try {
      log.info("Resultado MaSubcanal");
      String query = "SELECT CodSubcanal FROM MA_SUBCANAL WHERE RepartoTipo = ?";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaSubcanal", e);
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
      List<MaSubcanal> lstCarga = new ArrayList<>();
      List<MaSubcanal> lstUpdate = new ArrayList<>();
      List<String> lstCodigos = findAllCodSubcanal(repartoTipo);
      Pattern str = Pattern.compile("[A-Z0-9]{6}");
      MaSubcanal subcanal;
      boolean codigoExiste = false;
      boolean patronDiferente = false;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoExiste = false;
        patronDiferente = false;
        subcanal = new MaSubcanal();
        numFila++;

        String codSubcanalActual = fila.get(0).getValue();

        // Valida patrón
        if (!str.matcher(codSubcanalActual).matches()) {
          patronDiferente = true;
        }

        String codSubcanalBD = lstCodigos.stream().filter(item -> codSubcanalActual.equals(item)).findAny().orElse(null);

        //VALIDA SI CODIGO YA EXISTE EN BD
        if (codSubcanalBD != null) {
          codigoExiste = true;
        }

        if (!patronDiferente) {
          subcanal.setCodSubcanal(codSubcanalActual);
          subcanal.setNombre(fila.get(1).getValue());
          if (codigoExiste)
          {
            lstUpdate.add(subcanal);
          } else
          {
            lstCarga.add(subcanal);
          }
          lstCodigos.removeIf(x -> x.equals(codSubcanalActual));
        } else {
          numErrores++;
          errores.add("fila: " + numFila + " con codigo: " + codSubcanalActual + " no presenta un patron de codigo valido");
        }
      }

      String sql = "INSERT INTO MA_SUBCANAL"
          + " (CodSubcanal, RepartoTipo, Nombre, EstaActivo, FechaCreacion, FechaActualizacion) "
          + "VALUES " + "(?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstCarga.get(i).getCodSubcanal());//CodSubcanal
            ps.setInt(2, repartoTipo);//RepartoTipo
            ps.setString(3, lstCarga.get(i).getNombre());//Nombre
            ps.setInt(4, 1);//EstaActivo
            ps.setString(5, fecha);//FechaCreacion
            ps.setString(6, fecha);//FechaActualizacion
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
      sql =  "update MA_SUBCANAL\n"
          + "set Nombre=?, FechaActualizacion=?\n"
          + "where RepartoTipo=? and CodSubcanal=?;\n";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstUpdate.get(i).getNombre());//Nombre
            ps.setString(2, fecha);//FechaActualizacion
            ps.setInt(3, repartoTipo);//RepartoTipo
            ps.setString(4, lstUpdate.get(i).getCodSubcanal());//CodCanal
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
    }  else {
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
