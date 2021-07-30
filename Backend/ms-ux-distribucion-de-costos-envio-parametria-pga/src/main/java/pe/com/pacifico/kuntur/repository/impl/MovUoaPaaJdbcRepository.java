package pe.com.pacifico.kuntur.repository.impl;

import static java.lang.Math.round;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pe.com.pacifico.kuntur.expose.request.MovUoaPaaRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MovUoaPaa;
import pe.com.pacifico.kuntur.repository.MovUoaPaaJpaRepository;

/**
 * <b>Class</b>: MovUoaPaaJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 23, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MovUoaPaaJdbcRepository implements MovUoaPaaJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<MovUoaPaa> findAllMov(int repartoTipo, int periodo) {
    try {
      String query = "SELECT A.CodProducto, B.Nombre as 'nombreProducto', A.UnidadCuenta, A.Porcentaje\n"
          + "FROM MOV_UOA_PAA A\n"
          + "JOIN MA_PRODUCTO B ON A.CodProducto = B.CodProducto AND A.RepartoTipo = B.RepartoTipo\n"
          + "WHERE A.Periodo = ?\n"
          + "AND A.RepartoTipo = ?\n"
          + "ORDER BY A.CodProducto, A.UnidadCuenta";

      return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(MovUoaPaa.class), periodo, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovUoaPaa", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<MovUoaPaa> findAllMovByCodProducto(int repartoTipo, int periodo, String codProducto) {
    try {
      String query = "SELECT A.UnidadCuenta, A.Porcentaje\n"
          + "FROM MOV_UOA_PAA A\n"
          + "WHERE A.Periodo = ? \n"
          + "AND A.RepartoTipo = ? \n"
          + "AND A.CodProducto = ?";

      return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(MovUoaPaa.class), periodo, repartoTipo, codProducto);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovUoaPaa", e);
    }
    return new ArrayList<>();
  }

  @Override
  public int save(List<MovUoaPaaRequest> movUoaPaaRequestList) {
    int result = 0;
    Date date = new Date();
    try {
      for (MovUoaPaaRequest uar : movUoaPaaRequestList) {
        String queryMov = "INSERT INTO MOV_UOA_PAA(Periodo, RepartoTipo, CodProducto, UnidadCuenta, Porcentaje,\n"
            + "FechaCreacion, FechaActualizacion)\n"
            + "VALUES (?,?,?,?,?,?,?)";
        result = jdbcTemplate.update(queryMov, uar.getPeriodo(), uar.getRepartoTipo(), uar.getCodProducto(),
            uar.getUnidadCuenta(), uar.getPorcentaje(), date, date);
      }
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
  public int update(List<MovUoaPaaRequest> movUoaPaaRequestList) {
    int result = 0;
    try {
      if (movUoaPaaRequestList.size() > 0) {
        delete(movUoaPaaRequestList.get(0).getRepartoTipo(), movUoaPaaRequestList.get(0).getPeriodo(),
            movUoaPaaRequestList.get(0).getCodProducto());
      }
      result = save(movUoaPaaRequestList);
    } catch (DuplicateKeyException e) {
      result = -1;
      log.warn(e.toString());
    } catch (Exception e) {
      log.warn(e.toString());
      result = -2;
    }
    return result;
  }

  @Override
  public boolean delete(int repartoTipo, int periodo, String codigo) {
    try {
      String deleteQuery = "delete from MOV_UOA_PAA\n"
          + "where CodProducto = ? and Periodo = ? and RepartoTipo = ?";
      log.info(codigo + " con " + periodo + " con " + repartoTipo);
      jdbcTemplate.update(deleteQuery, codigo, periodo, repartoTipo);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public List<String> saveExcelToBD(List<List<CellData>> excel, int repartoTipo, int periodo) {
    List<String> errores = new ArrayList<>();
    log.info("CANTIDAD FILAS (CON HEADERS): " + excel.size());
    int numErrores = 0;

    //VALIDACION HEADERS
    if (excel.get(0).get(0).getValue().equalsIgnoreCase("CODIGO PRODUCTO")
        && excel.get(0).get(2).getValue().equalsIgnoreCase("UNIDAD DE CUENTA")
        && excel.get(0).get(3).getValue().equalsIgnoreCase("PORCENTAJE")) {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      List<MovUoaPaa> lstCarga = new ArrayList<>();

      List<String> lstCodigosProducto = findAllCodProducto(repartoTipo, periodo);

      List<String> lstCodigosAgregar = new ArrayList<>();
      MovUoaPaa movUoaPaa;
      boolean codigoExiste;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        codigoExiste = false;
        movUoaPaa = new MovUoaPaa();
        numFila++;

        String codProductoActual = fila.get(0).getValue();

        String codProductoBD = lstCodigosProducto.stream().filter(item -> codProductoActual.equals(item)).findAny().orElse(null);
        String nuevo = lstCodigosAgregar.stream().filter(item -> codProductoActual.equals(item)).findAny().orElse(null);

        //VALIDA QUE AMBOS CODIGOS SI EXISTAN EN BD
        if (codProductoBD != null) {
          codigoExiste = true;

          //Esto identifica si el codigo es nuevo
          if (nuevo == null) {
            lstCodigosAgregar.add(codProductoActual);
          }
        }

        if (codigoExiste) {
          movUoaPaa.setCodProducto(codProductoActual);
          movUoaPaa.setUnidadCuenta(fila.get(2).getValue());
          double porcentaje = Double.parseDouble(fila.get(3).getValue());
          movUoaPaa.setPorcentaje(porcentaje);
          lstCarga.add(movUoaPaa);
        } else {
          numErrores++;
          if (codProductoBD == null) {
            errores.add("FILA: " + numFila + " con codigo de producto " + codProductoActual + " no existe en el periodo");
          }
        }
      }

      //Se valida que el porcentaje sea 100 en las sumatorias de unidades a subir
      for (String codNuevo : lstCodigosAgregar) {
        double porcentajeAcumulado = 0;
        int filas = 0;
        for (MovUoaPaa i : lstCarga) {
          if (i.getCodProducto().equals(codNuevo)) {
            porcentajeAcumulado += i.getPorcentaje();
            filas ++;
          }
        }

        //Si no suma 100 se borra de la lista
        if (round(porcentajeAcumulado) != 100) {
          lstCarga.removeIf(i -> i.getCodProducto().equals(codNuevo));
          numErrores += filas;
          errores.add("Unidades de Cuenta para el producto con codigo " + codNuevo
              + " - ERROR: Porcentaje no suma 100, suma: " + round(porcentajeAcumulado * 1000) / 1000);
        }
      }

      //Se limpian las antiguas unidades de cuenta
      Set<String> set = new HashSet<>(lstCarga.size());
      List<MovUoaPaa> lstDelete =
          lstCarga.stream().filter(p -> set.add(p.getCodProducto())).collect(Collectors.toList());

      if (lstDelete.size() > 0) {
        String deleteQuery = "DELETE FROM MOV_UOA_PAA \n"
            + "WHERE RepartoTipo = ? AND Periodo = ? and CodProducto = ?";
        try {
          jdbcTemplate.batchUpdate(deleteQuery, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setString(3, lstDelete.get(i).getCodProducto());//CodProducto
              ps.setInt(2, periodo);//Periodo
              ps.setInt(1, repartoTipo);//RepartoTipo
            }

            @Override
            public int getBatchSize() {
              return lstDelete.size();
            }
          });
        } catch (Exception e) {
          log.error(e.toString());
        }
      }

      //se agregan las unidades de cuenta
      String sql = "INSERT INTO MOV_UOA_PAA(Periodo, RepartoTipo, CodProducto, UnidadCuenta, Porcentaje,\n"
          + "FechaCreacion, FechaActualizacion)\n"
          + "VALUES (?,?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        System.out.println("Ejecutando inserts...");
        if (lstCarga.size() > 0) {
          jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setInt(1, periodo);//Periodo
              ps.setInt(2, repartoTipo);//Reparto Tipo
              ps.setString(3, lstCarga.get(i).getCodProducto());//CodProducto
              ps.setString(4, lstCarga.get(i).getUnidadCuenta());//UnidadCuenta
              ps.setDouble(5, lstCarga.get(i).getPorcentaje());//Porcentaje
              ps.setString(6, fecha);//FechaCreacion
              ps.setString(7, fecha);//FechaActualizacion
            }

            @Override
            public int getBatchSize() {
              return lstCarga.size();
            }
          });
        }
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
          + " de Unidades de Cuenta PAA no se subieron por los siguientes errores: ");
      for (String fila : errores) {
        log.info(fila);
      }
      return errores;
    } else { return null; }
  }

  @Override
  public List<String> findAllCodProducto(int repartoTipo, int periodo) {
    try {
      log.info("Resultado MovProducto en MovUoaPaa");
      String query = "SELECT CodProducto FROM MOV_PRODUCTO WHERE RepartoTipo = ? and Periodo = ?";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo, periodo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MovProducto", e);
    }
    return new ArrayList<>();
  }

}
