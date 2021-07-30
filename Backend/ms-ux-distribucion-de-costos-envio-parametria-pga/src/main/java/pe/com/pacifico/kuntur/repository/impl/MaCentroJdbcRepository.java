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
import pe.com.pacifico.kuntur.expose.request.MaCentroRequest;
import pe.com.pacifico.kuntur.model.CellData;
import pe.com.pacifico.kuntur.model.MaCentro;
import pe.com.pacifico.kuntur.model.MovCentro;
import pe.com.pacifico.kuntur.repository.MaCentroJpaRepository;

/**
 * <b>Class</b>: MaCentroJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     April 12, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MaCentroJdbcRepository implements MaCentroJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  private static final String queryCoverage = "SELECT CodCentro, Nombre, CodCentroPadre, EstaActivo, FechaCreacion, "
      + "FechaActualizacion, GrupoCeco, Niif17Atribuible, Niif17Clase, Niif17Tipo, Nivel, RepartoTipo, Tipo, TipoCeco, "
      + "TipoGasto FROM MA_CENTRO";
  private static final String queryCoverageOption = queryCoverage + " where RepartoTipo=? and CodCentro=?";
  private static final String queryCoverageOption2 = queryCoverage + " where RepartoTipo=";

  @Override
  public List<MaCentro> findAll(int repartoTipo) {
    try {
      String strRepartoRipo = String.valueOf(repartoTipo);
      log.info("Resultado MaCentro");
      return jdbcTemplate.query(queryCoverageOption2 + strRepartoRipo, BeanPropertyRowMapper.newInstance(MaCentro.class));
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaCentro", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<MaCentro> findAllNotInMovCentro(int repartoTipo, int periodo) {
    try {
      log.info("Resultado MaCentro");
      return jdbcTemplate.query(
          new StringBuilder()
              .append("SELECT ma.CodCentro, ma.Nombre, ma.CodCentroPadre, ma.EstaActivo, ma.FechaCreacion, "
                  + "ma.fechaActualizacion, ma.GrupoCeco, ma.Niif17Atribuible, ma.Niif17Clase, ma.Niif17Tipo, ma.Nivel, "
                  + "ma.RepartoTipo, ma.Tipo, ma.TipoCeco, ma.TipoGasto "
                  + "FROM MA_CENTRO ma")
              .append(" LEFT JOIN MOV_CENTRO mo")
              .append(" ON ma.CodCentro = mo.CodCentro AND mo.Periodo = ")
              .append(periodo)
              .append(" WHERE mo.CodCentro is NULL and ma.RepartoTipo=")
              .append(repartoTipo).toString(),
          BeanPropertyRowMapper.newInstance(MaCentro.class));
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en MaCentro", e);
    }
    return new ArrayList<>();
  }

  @Override
  public MaCentro findByCodCentro(int repartoTipo, String codigo) {
    try {
      return
          jdbcTemplate.queryForObject(queryCoverageOption, new Object[]{repartoTipo,codigo},
              BeanPropertyRowMapper.newInstance(MaCentro.class));
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en centros ", e);
      return null;
    }
  }

  @Override
  public int save(MaCentroRequest centro) {
    Pattern str = Pattern.compile("[0-9]{2}.[A-Z0-9]{2}.[A-Z0-9]{2}");
    if (!str.matcher(centro.getCodCentro()).matches()) {
      return 0;
    }
    centro.setFechaCreacion(new Date());
    centro.setFechaActualizacion(new Date());
    return jdbcTemplate.update("insert into MA_CENTRO (CodCentro, Nombre, CodCentroPadre, EstaActivo, FechaCreacion, "
            + "FechaActualizacion, GrupoCeco, Niif17Atribuible, Niif17Clase, Niif17Tipo, Nivel, RepartoTipo, Tipo, TipoCeco, "
            + "TipoGasto)"
            + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", centro.getCodCentro(), centro.getNombre(), centro.getCodCentroPadre(),
          centro.isEstaActivo(), centro.getFechaCreacion(), centro.getFechaActualizacion(), centro.getGrupoCeco(),
          centro.getNiif17Atribuible(), centro.getNiif17Clase(), centro.getNiif17Tipo(), centro.getNivel(), centro.getRepartoTipo(),
          centro.getTipo(), centro.getTipoCeco(), centro.isTipoGasto());

  }

  @Override
  public int update(MaCentroRequest centro) {
    centro.setFechaActualizacion(new Date());
    System.out.println(centro);
    return jdbcTemplate.update(
        new StringBuilder()
            .append(" update MA_CENTRO ")
            .append(" set Nombre = ? , CodCentroPadre = ?, EstaActivo = ? , FechaCreacion = ? , FechaActualizacion = ? , "
                + "GrupoCeco = ? , Niif17Atribuible = ? , Niif17Clase = ? , Niif17Tipo = ? , Nivel = ? , RepartoTipo = ? , "
                + "Tipo = ? , TipoCeco = ? , TipoGasto = ?")
            .append(" where CodCentro = ?").toString(), centro.getNombre(), centro.getCodCentroPadre(),
        centro.isEstaActivo(), centro.getFechaCreacion(), centro.getFechaActualizacion(), centro.getGrupoCeco(),
        centro.getNiif17Atribuible(), centro.getNiif17Clase(), centro.getNiif17Tipo(), centro.getNivel(), centro.getRepartoTipo(),
        centro.getTipo(), centro.getTipoCeco(), centro.isTipoGasto(), centro.getCodCentro());

  }

  @Override
  public boolean delete(int repartoTipo, String codigo) {
    try {
      String queryExistsInMov = "SELECT top 1 CodCentro FROM MOV_CENTRO where CodCentro = ? and RepartoTipo = ?";
      MovCentro mc = jdbcTemplate.queryForObject(queryExistsInMov, new Object[]{codigo,repartoTipo},
          BeanPropertyRowMapper.newInstance(MovCentro.class));
      log.info("NO ES NULL, NO SE ELIMINA");
      return false;
    } catch (EmptyResultDataAccessException ex) {
      log.info("ES VACIO, SE ELIMINA");
      String deleteQuery = "delete from MA_CENTRO where CodCentro = ? and RepartoTipo = ?";
      jdbcTemplate.update(deleteQuery, codigo,repartoTipo);
      return true;
    } catch (Exception e) {
      log.info("ERROR");
      return false;
    }
  }

  @Override
  public List<String> findAllCodCentro(int repartoTipo) {
    try {
      log.info("Resultado Centro");
      String query = "SELECT CodCentro FROM MA_CENTRO WHERE RepartoTipo = ?";
      return jdbcTemplate.queryForList(query, String.class, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en CuentaContable", e);
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
        && excel.get(0).get(1).getValue().toLowerCase().equals("nombre")
        && excel.get(0).get(2).getValue().toLowerCase().equals("tipo")
        && excel.get(0).get(3).getValue().toLowerCase().equals("nivel")
        && excel.get(0).get(4).getValue().toLowerCase().equals("centro padre")
        && excel.get(0).get(5).getValue().toLowerCase().equals("tipo gasto")
        && excel.get(0).get(6).getValue().toLowerCase().equals("niif17 atribuible")
        && excel.get(0).get(7).getValue().toLowerCase().equals("niif17 tipo")
        && excel.get(0).get(8).getValue().toLowerCase().equals("niif17 clase")
        && excel.get(0).get(9).getValue().toLowerCase().equals("grupo ceco")
        && excel.get(0).get(10).getValue().toLowerCase().equals("tipo ceco"))
    {
      //REMOVER HEADERS PARA CARGAR INFO
      excel.remove(0);

      //PARAMETROS CARGA
      List<MaCentro> lstCarga = new ArrayList<>();
      List<MaCentro> lstUpdate = new ArrayList<>();
      List<String> lstCodigos = findAllCodCentro(repartoTipo);
      MaCentro centro;

      Pattern str = Pattern.compile("[0-9]{2}.[A-Z0-9]{2}.[A-Z0-9]{2}");
      boolean patronDiferente = false;
      boolean codigoExiste = false;
      int numFila = 0;


      //VALIDACIONES DE FILAS A CARGAR
      for (List<CellData> fila : excel) {
        patronDiferente = false;
        codigoExiste = false;
        centro = new MaCentro();
        numFila++;

        String codigoCentroActual = fila.get(0).getValue();

        //VALIDA PATRON
        if (!str.matcher(codigoCentroActual).matches()) {
          patronDiferente = true;
        }

        String codCentroBD = lstCodigos.stream().filter(item -> codigoCentroActual.equals(item)).findAny().orElse(null);

        //VALIDA SI CODIGO YA EXISTE EN BD
        if (codCentroBD != null) {
          codigoExiste = true;
        }

        if (!patronDiferente) {
          try {
            int error = 0;
            centro.setCodCentro(codigoCentroActual);
            centro.setNombre(fila.get(1).getValue());
            centro.setTipo(fila.get(2).getValue());
            String[] parts = fila.get(3).getValue().split("\\."); //para obtener el numero
            centro.setNivel(Integer.parseInt(parts[0]));
            centro.setCodCentroPadre(fila.get(4).getValue().equalsIgnoreCase("") ? "-" : fila.get(4).getValue());
            String tipo = fila.get(5).getValue().split("\\.")[0]; //para obtener el numero
            if (tipo.equalsIgnoreCase("1"))
            {
              centro.setTipoGasto(true);
            } else if (tipo.equalsIgnoreCase("0")) {
              centro.setTipoGasto(false);
            } else {
              errores.add("fila: " + numFila + " con codigo: " + codigoCentroActual
                  + " El Tipo gasto solo puede tomar valores de 1 o 0");
              error++;
            }
            if (fila.get(6).getValue().equalsIgnoreCase("SI"))
            {
              centro.setNiif17Atribuible("SI");
            } else if (fila.get(6).getValue().equalsIgnoreCase("NO")) {
              centro.setNiif17Atribuible("NO");
            } else {
              errores.add("fila: " + numFila + " con codigo: " + codigoCentroActual
                  + " El Niif17Atribuible solo puede tomar valores de SI o NO");
              error++;
            }

            if (fila.get(7).getValue().equalsIgnoreCase("GM"))
            {
              centro.setNiif17Tipo("GM");
            } else if (fila.get(7).getValue().equalsIgnoreCase("GA")) {
              centro.setNiif17Tipo("GA");
            } else {
              errores.add("fila: " + numFila + " con codigo: " + codigoCentroActual
                  + " El Niif17Tipo solo puede tomar valores de GM o GA");
              error++;
            }

            if (fila.get(8).getValue().equalsIgnoreCase("FI"))
            {
              centro.setNiif17Clase("FI");
            } else if (fila.get(8).getValue().equalsIgnoreCase("VA")) {
              centro.setNiif17Clase("VA");
            } else {
              errores.add("fila: " + numFila + " con codigo: " + codigoCentroActual
                  + " El Niif17Clase solo puede tomar valores de FI o NO");
              error++;
            }
            centro.setGrupoCeco(fila.get(9).getValue().equalsIgnoreCase("") ? "-" : fila.get(9).getValue());
            centro.setTipoCeco(fila.get(10).getValue().equalsIgnoreCase("") ? "-" : fila.get(10).getValue());

            if (error > 0) { numErrores++; }
            if (error == 0) {
              if (codigoExiste)
              {
                lstUpdate.add(centro);
              } else
              {
                lstCarga.add(centro);
              }
            }
          } catch (Exception e) {
            log.info(e.toString());
            errores.add("fila: " + numFila + " con codigo: " + codigoCentroActual
                + " Presenta un error en los datos");
            numErrores++;
          }
          //log.info("llego aqui 1");
          lstCodigos.removeIf(x -> x.equals(codigoCentroActual));
          //log.info("llego aqui 2");
        } else {
          numErrores++;
          errores.add("fila: " + numFila + " con codigo: " + codigoCentroActual + " no presenta un patron de codigo valido");
        }
      }

      String sql = "insert into MA_CENTRO "
          + "(CodCentro,Nombre,Tipo,Nivel,CodCentroPadre,TipoGasto,Niif17Atribuible,Niif17Tipo,Niif17Clase,GrupoCeco,"
          + "TipoCeco,RepartoTipo,EstaActivo, FechaCreacion,FechaActualizacion) "
          + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      log.info("llego aqui SQL");

      //Primero se añaden los centros
      try {
        log.info("llego aqui 3");
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstCarga.get(i).getCodCentro());//CodCuentaContable
            ps.setString(2, lstCarga.get(i).getNombre());//Nombre
            ps.setString(3, lstCarga.get(i).getTipo());//Tipo
            ps.setInt(4, lstCarga.get(i).getNivel()); //Nivel
            ps.setString(5, lstCarga.get(i).getCodCentroPadre());
            ps.setInt(6, (lstCarga.get(i).isTipoGasto()) ? 1 : 0);//TipoGasto
            ps.setString(7, lstCarga.get(i).getNiif17Atribuible());//Niif17Atribuible
            ps.setString(8, lstCarga.get(i).getNiif17Tipo());//Niif17Tipo
            ps.setString(9, lstCarga.get(i).getNiif17Clase());//Niif17Clase
            ps.setString(10, lstCarga.get(i).getGrupoCeco());//grupo CECO
            ps.setString(11, lstCarga.get(i).getTipoCeco());//tipo CECO
            ps.setInt(12, repartoTipo);//RepartoTipo
            ps.setInt(13, lstCarga.get(i).isEstaActivo() ? 1 : 0);//EstaActivo
            ps.setString(14, fecha);//FechaCreacion
            ps.setString(15, fecha);//FechaActualizacion
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
      sql = "update MA_CENTRO\n"
          + "set Nombre=?, Tipo= ?, Nivel=?,CodCentroPadre=?,\n"
          + "TipoGasto=?, Niif17Atribuible=?, Niif17Tipo=?, Niif17Clase = ?,\n"
          + "GrupoCeco = ?, TipoCeco = ?, FechaActualizacion = ?\n"
          + "where CodCentro=? and RepartoTipo=?";
      try {
        log.info("llego aqui 4");
        Date date = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(date);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, lstUpdate.get(i).getNombre());//Nombre
            ps.setString(2, lstUpdate.get(i).getTipo());//Tipo
            ps.setInt(3, lstUpdate.get(i).getNivel()); //Nivel
            ps.setString(4, lstUpdate.get(i).getCodCentroPadre());
            ps.setInt(5, (lstUpdate.get(i).isTipoGasto()) ? 1 : 0);//TipoGasto
            ps.setString(6, lstUpdate.get(i).getNiif17Atribuible());//Niif17Atribuible
            ps.setString(7, lstUpdate.get(i).getNiif17Tipo());//Niif17Tipo
            ps.setString(8, lstUpdate.get(i).getNiif17Clase());//Niif17Clase
            ps.setString(9, lstUpdate.get(i).getGrupoCeco());//grupo CECO
            ps.setString(10, lstUpdate.get(i).getTipoCeco());//tipo CECO
            ps.setString(11, fecha);//FechaActualizacion
            ps.setString(12, lstUpdate.get(i).getCodCentro());//CodCuentaContable
            ps.setInt(13, repartoTipo);//RepartoTipo
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
