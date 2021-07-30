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
import pe.com.pacifico.kuntur.expose.request.MaPartidaRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaPartida;
import pe.com.pacifico.kuntur.model.MovPartida;
import pe.com.pacifico.kuntur.repository.MaPartidaJpaRepository;

/**
 * <b>Class</b>: MaPartidaJdbcRepository <br/>
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
public class MaPartidaJdbcRepository implements MaPartidaJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  private static final String queryCoverage = "SELECT CodPartida, Nombre, FechaActualizacion, FechaCreacion, "
      + "GrupoGasto, RepartoTipo, TipoGasto FROM MA_PARTIDA";
  private static final String querySelect = "SELECT CodPartida, Nombre, FechaActualizacion, FechaCreacion, "
      + "GrupoGasto, RepartoTipo, TipoGasto FROM MA_PARTIDA WHERE RepartoTipo = ?";
  private static final String querySelectOption = querySelect + " AND codPartida = ? ";

  @Override
  public List<MaPartida> findAll(int repartoTipo) {
    try {
      log.info("Resultado MaPartida");
      return jdbcTemplate.query(querySelect, BeanPropertyRowMapper.newInstance(MaPartida.class), repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaPartida", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<MaPartida> findAllNotInMovPartida(int repartoTipo, int periodo) {
    try {
      log.info("Resultado Partida");
      String qry = "SELECT ma.codPartida, ma.nombre, ma.fechaCreacion, ma.fechaActualizacion, ma.grupoGasto,"
          + " ma.repartoTipo, ma.tipoGasto"
          + " FROM MA_PARTIDA ma LEFT JOIN MOV_PARTIDA mo"
          + " ON ma.CodPartida = mo.CodPartida"
          + " AND mo.Periodo = ? AND mo.RepartoTipo = ?"
          + " WHERE mo.CodPartida is NULL AND ma.repartoTipo = ?";
      return jdbcTemplate.query(qry, BeanPropertyRowMapper.newInstance(MaPartida.class),
          periodo, repartoTipo, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en Partida", e);
    }
    return new ArrayList<>();
  }

  @Override
  public MaPartida findByCodPartida(int repartoTipo, String codigo) {
    try {
      return
          jdbcTemplate.queryForObject(querySelectOption, new Object[]{repartoTipo,codigo},
              BeanPropertyRowMapper.newInstance(MaPartida.class));
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en partidas ", e);
      return null;
    }
  }

  @Override
  public int save(MaPartidaRequest partida) {
    Pattern str = Pattern.compile("[A-Z0-9]{4,5}");
    if (!str.matcher(partida.getCodPartida()).matches()) {
      return 0;
    }
    return jdbcTemplate.update("insert into MA_PARTIDA (CodPartida, Nombre, FechaActualizacion, FechaCreacion, "
            + "GrupoGasto, RepartoTipo, TipoGasto) "
            + " values (?,?,?,?,?,?,?)", partida.getCodPartida(), partida.getNombre(), partida.getFechaActualizacion(),
        partida.getFechaCreacion(), partida.getGrupoGasto(), partida.getRepartoTipo(), partida.isTipoGasto());

  }

  @Override
  public int update(MaPartidaRequest partida) {
    System.out.println(partida);
    return jdbcTemplate.update(
        new StringBuilder()
            .append(" update MA_PARTIDA ")
            .append(" set Nombre = ? , FechaActualizacion = ?, FechaCreacion = ?, GrupoGasto = ?, TipoGasto = ?")
            .append(" where repartoTipo = ? AND codPartida = ?").toString(), partida.getNombre(), partida.getFechaActualizacion(),
        partida.getFechaCreacion(), partida.getGrupoGasto(), partida.isTipoGasto(), partida.getRepartoTipo(),
        partida.getCodPartida());

  }

  @Override
  public boolean delete(int repartoTipo, String codigo) {
    try {
      String queryExistsInMov = "SELECT top 1 CodPartida FROM MOV_PARTIDA"
          + " WHERE RepartoTipo = ? AND CodPartida = ? ";
      MovPartida mp = jdbcTemplate.queryForObject(queryExistsInMov, new Object[]{repartoTipo,codigo},
          BeanPropertyRowMapper.newInstance(MovPartida.class));
      log.info("NO ES NULL, NO SE ELIMINA");
      return false;
    } catch (EmptyResultDataAccessException ex) {
      log.info("ES VACIO, SE ELIMINA");
      String deleteQuery = "delete from MA_PARTIDA where CodPartida = ? AND repartoTipo = ?" ;
      jdbcTemplate.update(deleteQuery, codigo, repartoTipo);
      return true;
    } catch (Exception e) {
      log.info("ERROR");
      return false;
    }
  }

  @Override
  public List<String> findAllCodPartida(int repartoTipo) {
    try {
      log.info("Resultado MaPartida");
      String query = "SELECT CodPartida FROM MA_PARTIDA WHERE RepartoTipo = ?";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaPartida", e);
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
        && excel.get(0).get(1).getValue().toLowerCase().equals("nombre")
        && excel.get(0).get(2).getValue().toLowerCase().equals("grupo gasto")
        && excel.get(0).get(3).getValue().toLowerCase().equals("tipo gasto"))
    {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      List<MaPartida> lstCarga = new ArrayList<>();
      List<String> lstCodigos = findAllCodPartida(repartoTipo);
      List<MaPartida> lstUpdate = new ArrayList<>();
      MaPartida partida;
      Pattern str = Pattern.compile("[A-Z0-9]{4,5}");
      boolean patronDiferente = false;
      boolean codigoExiste = false;
      int numFila = 0;

      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        patronDiferente = false;
        codigoExiste = false;
        partida = new MaPartida();
        numFila++;

        String codPartidaActual = fila.get(0).getValue();

        //VALIDA PATRON
        if (!str.matcher(codPartidaActual).matches()) {
          patronDiferente = true;
        }

        String codPartidaBD = lstCodigos.stream().filter(item -> codPartidaActual.equals(item)).findAny().orElse(null);

        //VALIDA SI CODIGO YA EXISTE EN BD
        if (codPartidaBD != null) {
          codigoExiste = true;
        }

        if (!patronDiferente) {
          int error = 0;
          partida.setCodPartida(codPartidaActual);
          partida.setNombre(fila.get(1).getValue());

          partida.setGrupoGasto(fila.get(2).getValue());
          if (fila.get(2).getValue().equalsIgnoreCase("GT"))
          {
            partida.setGrupoGasto("GT");
          } else if (fila.get(2).getValue().equalsIgnoreCase("GM")) {
            partida.setGrupoGasto("GM");
          } else if (fila.get(2).getValue().equalsIgnoreCase("GP")) {
            partida.setGrupoGasto("GP");
          } else {
            errores.add("fila: " + numFila + " con codigo: " + codPartidaActual
                + " El GrupoGasto solo puede tomar valores de GM, GP o GT");
            error++;
          }

          String tipo = fila.get(3).getValue().split("\\.")[0]; //para obtener el numero
          if (tipo.equalsIgnoreCase("1"))
          {
            partida.setTipoGasto(true);
          } else if (tipo.equalsIgnoreCase("0")) {
            partida.setTipoGasto(false);
          } else {
            errores.add("fila: " + numFila + " con codigo: " + codPartidaActual
                + " El Tipo gasto solo puede tomar valores de 1 o 0");
            error++;
          }

          if (error > 0) { numErrores++; }
          if (error == 0) {
            if (codigoExiste)
            {
              lstUpdate.add(partida);
            } else
            {
              lstCarga.add(partida);
            }
          }
          lstCodigos.removeIf(x -> x.equals(codPartidaActual));
        } else {
          numErrores++;
          errores.add("fila: " + numFila + " con codigo: " + codPartidaActual + " no presenta un patron de codigo valido");
        }
      }

      String sql = "INSERT INTO MA_PARTIDA"
          + " (codPartida, nombre, fechaCreacion, fechaActualizacion, grupoGasto, tipoGasto, repartoTipo) "
          + "VALUES " + "(?,?,?,?,?,?,?)";

      try {
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstCarga.get(i).getCodPartida());//CodPartida
            ps.setString(2, lstCarga.get(i).getNombre());//Nombre
            ps.setString(3, fecha);//FechaCreacion
            ps.setString(4, fecha);//FechaActualizacion
            ps.setString(5, lstCarga.get(i).getGrupoGasto());//GrupoGasto
            ps.setInt(6, (lstCarga.get(i).isTipoGasto()) ? 1 : 0);//TipoGasto
            ps.setInt(7, repartoTipo);//RepartoTipo
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

      //Luego se actualizan los centros
      sql = "update MA_PARTIDA\n"
          + "set Nombre=?,GrupoGasto=?,TipoGasto=?, FechaActualizacion=?\n"
          + "where CodPartida=? and RepartoTipo=?";
      try {
        log.info("llego aqui 4");
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {

            ps.setString(1, lstUpdate.get(i).getNombre());//Nombre
            ps.setString(2, lstUpdate.get(i).getGrupoGasto());//GrupoGasto
            ps.setInt(3, (lstUpdate.get(i).isTipoGasto()) ? 1 : 0);//TipoGasto
            ps.setString(4, fecha);//FechaActualizacion
            ps.setString(5, lstUpdate.get(i).getCodPartida());//CodPartida
            ps.setInt(6, repartoTipo);//RepartoTipo
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
