package pe.com.pacifico.kuntur.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import pe.com.pacifico.kuntur.model.AsignacionCecoBolsa;
import pe.com.pacifico.kuntur.model.EjecucionProceso;
import pe.com.pacifico.kuntur.repository.ProcesoJpaRepoository;


/**
 * <b>Class</b>: ProcesoJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 6, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class ProcesoJdbcRepository implements ProcesoJpaRepoository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public Integer fasesCompletadas(int repartoTipo, int periodo) {
    String sql = "select count(1) as cuenta from MOV_EJECUCION\n"
            + "where RepartoTipo=? and periodo=? and FechaFin is not NULL;";
    try {
      return jdbcTemplate.queryForObject(sql,Integer.class,repartoTipo,periodo);

    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return 0;
  }

  @Override
  public int procesoCerrado(int repartoTipo, int periodo) {
    String sql = "select Estado\n"
            + "from MOV_CIERRE_PROCESO\n"
            + "where periodo = ?\n"
            + "and RepartoTipo = ?";
    try {
      return jdbcTemplate.queryForObject(sql,Integer.class,periodo,repartoTipo);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return 0;
  }

  /*@Override
  public boolean faseEnEjecucion(int repartoTipo, int periodo) {
    String sql = "select count(*) as cuenta from MOV_EJECUCION\n"
            + "where RepartoTipo=? and Periodo=? and FechaFin is NULL";
    try {
      int cuenta = jdbcTemplate.queryForObject(sql,Integer.class,repartoTipo,periodo);
      if (cuenta == 0) {
        return false;
      } else {
        return true;
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return true;
  }

  @Override
  public boolean faseEnEjecucion(int repartoTipo, int periodo) {
    String sql = "select count(*) as cuenta from MOV_EJECUCION\n"
            + "where RepartoTipo=? and Periodo=? and FechaFin is NULL";
    try {
      int cuenta = jdbcTemplate.queryForObject(sql,Integer.class,repartoTipo,periodo);
      if (cuenta == 0) {
        return false;
      } else {
        return true;
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return true;
  }*/

  // Fase 2 *****************
  @Override
  public int maxNivelCascada(int periodo, int repartoTipo) {
    int periodoF = repartoTipo == 1 ? periodo : periodo / 100 * 100;
    String sql = "SELECT MAX(B.NIVEL) NIVEL\n"
            + "       FROM MOV_CENTRO A\n"
            + "       JOIN MA_CENTRO B\n"
            + "       ON A.CodCentro=B.CodCentro\n"
            + "       AND A.PERIODO= ?\n"
            + "       AND A.RepartoTipo= ?\n"
            + "       AND B.TIPO IN ('STAFF','SOPORTE')";
    try {
      return jdbcTemplate.queryForObject(sql,Integer.class,periodoF, repartoTipo);

    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return 0;
  }


  @Override
  public int iniciarFase2(int periodo, int repartoTipo) {
    int periodoF = repartoTipo == 1 ? periodo : periodo / 100 * 100;
    // a) Primer query
    String query1 = "INSERT INTO FASE_2(Iteracion,CodCuentaContableInicial,CodPartidaInicial,\n"
            + "\t\t\t\t\t  CodCentroInicial,CodCentroOrigen,CodDocumentoClienteInicial, CodDriver,CodCentroDestino,\n"
            + "\t\t\t\t\t  GrupoGasto,Monto)\n"
            + "                SELECT 0 ITERACION,\n"
            + "                       A.CodCuentaContableOrigen CodCuentaContableInicial,\n"
            + "                       A.CodPartidaOrigen CodPartidaInicial,\n"
            + "                       A.CodCentroDestino CodCentroInicial,\n"
            + "                       A.CodCentroDestino CodCentroOrigen,\n"
            + "                       A.CodDocumentoClienteOrigen CodDocumentoClienteInicial,\n"
            + "                       A.CodDriver,\n"
            + "                       A.CodCentroDestino,\n"
            + "                       A.GrupoGasto,\n"
            + "                       SUM(A.MONTO) MONTO\n"
            + "                  FROM FASE_1 A\n"
            + "                  JOIN MA_CENTRO B ON B.CodCentro=A.CodCentroDestino AND B.RepartoTipo = ?\n"
            + "                 GROUP BY CodCuentaContableOrigen,\n"
            + "                          CodPartidaOrigen,\n"
            + "                          CodDriver,\n"
            + "                          CodCentroDestino,\n"
            + "                          CodDocumentoClienteOrigen,\n"
            + "                          GrupoGasto";
    //  b) Segundo query
    // Nota: Periodo de segundo y tercer query es periodoF:
    //		 int periodoF = repartoTipo == 1 ? periodo : periodo / 100 * 100
    String query2 = "INSERT INTO TMP_ENTIDAD_ORIGEN_DRIVER(CodEntidadOrigen, CodDriver)\n"
            + "\tSELECT CodCentro,CodDriver\n"
            + "    FROM CENTRO_DRIVER\n"
            + "    WHERE RepartoTipo = ? AND PERIODO = ?";
    // c) Tercer query
    String query3 = "INSERT INTO TMP_MOV_DRIVER(CodDriver, CodEntidadDestino, Porcentaje)\n"
            + "    SELECT CodDriverCentro,CodCentroDestino,Porcentaje\n"
            + "    FROM MOV_DRIVER_CENTRO\n"
            + "    WHERE RepartoTipo = ? AND Periodo = ?";
    try {
      jdbcTemplate.update(query1, repartoTipo);
      jdbcTemplate.update(query2, repartoTipo, periodoF);
      jdbcTemplate.update(query3, repartoTipo, periodoF);
      return 1;
    } catch (Exception e) {
      log.warn(e.toString());
    }
    return -1;
  }



  @Override
  public int insertarDistribucionCascadaPorNivel(int iteracion, int periodo, int repartoTipo, double precision) {
    String sql = "INSERT INTO FASE_2(ITERACION,CodCuentaContableInicial,CodPartidaInicial,\n"
            + "\t\t\t\t\tCodCentroInicial,CodCentroOrigen,CodDocumentoClienteInicial, CodDriver,CodCentroDestino,\n"
            + "\t\t\t\t\tGrupoGasto,Monto)\n"
            + "                SELECT ? ITERACION,\n"
            + "                       A.CodCuentaContableInicial,\n"
            + "                       A.CodPartidaInicial,\n"
            + "                       A.CodCentroInicial,\n"
            + "                       A.CodCentroDestino CodCentroOrigen,\n"
            + "                       A.CodDocumentoClienteInicial,\n"
            + "                       C.CodDriver,\n"
            + "                       D.CodEntidadDestino CodCentroDestino,\n"
            + "                       A.GrupoGasto,\n"
            + "                       SUM(A.MONTO*D.PORCENTAJE/100.00) MONTO\n"
            + "                  FROM FASE_2 A\n"
            + "                  JOIN MA_CENTRO B\n"
            + "                    ON B.NIVEL= ?\n"
            + "                   AND B.CodCentro=A.CodCentroDestino\n"
            + "                   AND B.RepartoTipo = ?\n"
            + "                  JOIN TMP_ENTIDAD_ORIGEN_DRIVER C\n"
            + "                    ON C.CodEntidadOrigen=A.CodCentroDestino\n"
            + "                  JOIN TMP_MOV_DRIVER D\n"
            + "                    ON D.CodDriver = C.CodDriver\n"
            + "                 GROUP BY A.CodCuentaContableInicial,\n"
            + "                          A.CodPartidaInicial,\n"
            + "                          A.CodCentroInicial,\n"
            + "                          A.CodCentroDestino,\n"
            + "                          A.CodDocumentoClienteInicial,\n"
            + "                          C.CodDriver,\n"
            + "                          D.CodEntidadDestino,\n"
            + "                          A.GrupoGasto\n"
            + "                 HAVING ABS(SUM(A.MONTO*D.PORCENTAJE/100.00))>= ? ";
    try {
      log.info("ITERACION NRO. " + iteracion);
      return jdbcTemplate.update(sql, iteracion, iteracion, repartoTipo, precision);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return 0;
  }

  @Override
  public int calcularFase2F(int periodo, int repartoTipo) {
    String sql = "INSERT INTO FASE_2_F(CodCuentaContableInicial,CodPartidaInicial,\n"
            + "\t\tCodCentroInicial,CodCentroDestino,CodDocumentoClienteInicial,GrupoGasto,Monto)\n"
            + "    SELECT A.CodCuentaContableInicial,\n"
            + "\t\tA.CodPartidaInicial,\n"
            + "        A.CodCentroInicial,\n"
            + "        A.CodCentroDestino,\n"
            + "        A.CodDocumentoClienteInicial,\n"
            + "        A.GrupoGasto,\n"
            + "        SUM(A.MONTO) Monto\n"
            + "    FROM FASE_2 A\n"
            + "\t\tJOIN MA_CENTRO B\n"
            + "        ON A.CodCentroDestino=B.CodCentro\n"
            + "        AND B.TIPO NOT IN ('STAFF','SOPORTE')\n"
            + "        AND B.RepartoTipo = ?\n"
            + "    GROUP BY CodCuentaContableInicial,CodPartidaInicial,CodCentroInicial,CodCentroDestino,"
            + "             CodDocumentoClienteInicial,GrupoGasto ";
    try {
      return jdbcTemplate.update(sql, repartoTipo);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return 0;
  }

  // **********************
  @Override
  public List<String> listarCodigosObjetos(int repartoTipo, int periodo) {
    int periodoF = repartoTipo == 1 ? periodo : periodo / 100 * 100;
    String sql = "select movc.CodCentro\n"
            + "from MOV_CENTRO movc\n"
            + "inner join MA_CENTRO mac on movc.CodCentro=mac.CodCentro AND mac.RepartoTipo = ?\n"
            + "where movc.Periodo=? and movc.RepartoTipo=? and mac.tipo NOT IN ('STAFF','SOPORTE')\n"
            + "order by movc.CodCentro";
    try {
      return jdbcTemplate.queryForList(sql,String.class,repartoTipo, periodoF,repartoTipo);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
    return new ArrayList<>();
  }

  @Override
  public int iniciarFase3(int periodo, int repartoTipo) {
    int periodoF = repartoTipo == 1 ? periodo : periodo / 100 * 100;
    String query1 = "INSERT INTO TMP_OBJETO_DRIVER (CodCentro,GrupoGasto,CodDriver)\n"
            + "SELECT CodCentro,GrupoGasto,CodDriver\n"
            + "FROM CENTRO_DRIVER_OBJETO\n"
            + "WHERE RepartoTipo = ? AND Periodo = ?";
    String query2 = "INSERT INTO TMP_MOV_DRIVER_OBJETO (CodDriver, CodProducto, CodSubcanal, Porcentaje)\n"
            + "SELECT CodDriverObjeto,CodProducto,CodSubcanal,Porcentaje\n"
            + "FROM MOV_DRIVER_OBJETO\n"
            + "WHERE RepartoTipo = ? AND Periodo = ?";
    try {
      jdbcTemplate.update(query1, repartoTipo, periodoF);
      jdbcTemplate.update(query2, repartoTipo, periodoF);
      return 1;
    } catch (Exception e) {
      log.warn(e.toString());
      return -1;
    }
  }

  @Override
  public int insertarDistribucionesDeObjetosDeCosto(int repartoTipo, int periodo,String codigo) {
    String sql = "INSERT INTO FASE_3 (CodCuentaContableInicial, CodPartidaInicial, CodCentroInicial, CodCentroDestino, \n"
            + "CodDocumentoClienteInicial, CodDriver, CodProducto, CodSubcanal, GrupoGasto, Monto)\n"
            + "SELECT A.CodCuentaContableInicial,A.CodPartidaInicial,A.CodCentroInicial,A.CodCentroDestino,\n"
            + "A.CodDocumentoClienteInicial,B.CodDriver,C.CodProducto,C.CodSubcanal,A.GrupoGasto,A.Monto*C.Porcentaje/100.00\n"
            + "FROM FASE_2_F A \n"
            + "inner join TMP_OBJETO_DRIVER B on B.CodCentro=A.CodCentroDestino and B.GrupoGasto=A.GrupoGasto\n"
            + "inner join TMP_MOV_DRIVER_OBJETO C on B.CodDriver=C.CodDriver\n"
            + "inner join MA_CENTRO D ON D.CodCentro = A.CodCentroDestino AND D.RepartoTipo = ?\n"
            + "where A.CodCentroDestino = ?\n"
            + "and ABS(A.Monto*C.Porcentaje/100.00)>= 0.0001";
    try {
      return jdbcTemplate.update(sql,repartoTipo,codigo);
    } catch (Exception e) {
      log.warn(e.toString());
    }
    return -1;
  }

  @Override
  public int verificarFasesEnEjecucion(int repartoTipo, int periodo) {
    try {
      String query = "select Fase FROM MOV_EJECUCION\n"
              + "WHERE FechaIni IS NOT NULL\n"
              + "AND FechaFin IS NULL\n"
              + "AND Periodo = ? AND RepartoTipo = ?";
      int c = jdbcTemplate.queryForObject(query, Integer.class, periodo, repartoTipo);
      return c;
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  public boolean verificarDetalleGasto(int repartoTipo, int periodo) {
    String tabla = "CUENTA_PARTIDA_CENTRO_";
    if (repartoTipo == 1) { tabla = tabla + "R"; }
    if (repartoTipo == 2) { tabla = tabla + "P"; }
    try {
      String query = "SELECT COUNT(1) c\n"
              + "FROM " + tabla + "\n"
              + "WHERE Periodo = ?";
      int c = jdbcTemplate.queryForObject(
              query, new Object[] { periodo }, Integer.class);
      return c > 0;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public List<String> obtenerDriverFase1ConError(int repartoTipo, int periodo) {
    int periodoF = repartoTipo == 1 ? periodo : periodo / 100 * 100;
    String tabla = "CUENTA_PARTIDA_CENTRO_";
    if (repartoTipo == 1) { tabla = tabla + "R"; }
    if (repartoTipo == 2) { tabla = tabla + "P"; }
    try {
      String query = "SELECT distinct CONCAT('Cuenta Contable: ',A.CodCuentaContable,'; Partida: ',A.CodPartida, \n"
              + "'; Centro: ',A.CodCentro,'.')"
              + "FROM " + tabla + " A\n"
              + "JOIN MA_CENTRO B ON A.CodCentro = B.CodCentro \n"
              + "AND B.RepartoTipo = ? \n"
              + "AND B.Tipo IN ('BOLSA','OFICINA')\n"
              + "LEFT JOIN CUENTA_PARTIDA_CENTRO_DRIVER_CENTRO C ON C.RepartoTipo = ? \n"
              + "AND C.Periodo = ?\n"
              + "AND A.CodCuentaContable = C.CodCuentaContable \n"
              + "AND A.CodPartida = C.CodPartida\n"
              + "AND A.CodCentro = C.CodCentro\n"
              + "WHERE A.Periodo = ? \n"
              + "AND C.CodDriver IS NULL\n";
      List<String> lstDriversConError = jdbcTemplate.queryForList(
              query, String.class, repartoTipo, repartoTipo, periodoF, periodoF);
      return lstDriversConError;
    } catch (EmptyResultDataAccessException e) {
      log.info("Sin Errores en Asignaciones Fase 1");
      return new ArrayList<>();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public int borrarAsignaciones(int periodo, int fase, int repartoTipo) {
    String deleteQuery = "DELETE FROM MOV_EJECUCION\n"
            + "WHERE Periodo = ? AND Fase >= ? AND RepartoTipo = ?";
    try {
      return jdbcTemplate.update(deleteQuery, periodo, fase, repartoTipo);
    } catch (Exception e) {
      log.error(e.toString());
    }
    return -1;
  }

  @Override
  public void borrarDistribucionesFase1() {
    String truncateQuery1 = "TRUNCATE TABLE FASE_1";
    String truncateQuery2 = "TRUNCATE TABLE TMP_BOLSA_DRIVER";
    String truncateQuery3 = "TRUNCATE TABLE TMP_MOV_DRIVER";
    try {
      jdbcTemplate.execute(truncateQuery1);
      jdbcTemplate.execute(truncateQuery2);
      jdbcTemplate.execute(truncateQuery3);
    } catch (Exception e) {
      log.error(e.toString());
    }
  }

  @Override
  public void borrarDistribucionesFase2() {
    String truncateQuery1 = "TRUNCATE TABLE FASE_2";
    String truncateQuery2 = "TRUNCATE TABLE FASE_2_F";
    String truncateQuery3 = "TRUNCATE TABLE TMP_ENTIDAD_ORIGEN_DRIVER";
    String truncateQuery4 = "TRUNCATE TABLE TMP_MOV_DRIVER";
    try {
      jdbcTemplate.execute(truncateQuery1);
      jdbcTemplate.execute(truncateQuery2);
      jdbcTemplate.execute(truncateQuery3);
      jdbcTemplate.execute(truncateQuery4);
    } catch (Exception e) {
      log.error(e.toString());
    }
  }

  @Override
  public void borrarDistribucionesFase3() {
    String truncateQuery1 = "TRUNCATE TABLE FASE_3";
    String truncateQuery2 = "TRUNCATE TABLE TMP_OBJETO_DRIVER";
    String truncateQuery3 = "TRUNCATE TABLE TMP_MOV_DRIVER_OBJETO";
    try {
      jdbcTemplate.execute(truncateQuery1);
      jdbcTemplate.execute(truncateQuery2);
      jdbcTemplate.execute(truncateQuery3);
    } catch (Exception e) {
      log.error(e.toString());
    }
  }

  @Override
  public int insertarEjecucionIni(int periodo, int fase, int repartoTipo, String userEmail, String userNames) {
    Date fecha = Calendar.getInstance().getTime();
    String query = "INSERT INTO MOV_EJECUCION (Periodo, Fase, RepartoTipo, FechaIni, CorreoEjecutor, NombreEjecutor) "
            + " VALUES (?,?,?,?, ?, ?)";
    try {
      return jdbcTemplate.update(query, periodo, fase, repartoTipo, fecha, userEmail, userNames);
    } catch (Exception e) {
      log.warn(e.toString());
    }
    return -1;
  }

  @Override
  public int iniciarFase1(int periodo, int repartoTipo) {
    int periodoF = repartoTipo == 1 ? periodo : periodo / 100 * 100;
    String query1 = "INSERT INTO TMP_BOLSA_DRIVER (CodCuentaContable, CodPartida, CodCentro, CodDriver)\n"
            + "SELECT CodCuentaContable,CodPartida,CodCentro,CodDriver\n"
            + "FROM CUENTA_PARTIDA_CENTRO_DRIVER_CENTRO\n"
            + "WHERE RepartoTipo = ? AND Periodo = ?";
    String query2 = "INSERT INTO TMP_MOV_DRIVER (CodDriver, CodEntidadDestino, Porcentaje)\n"
            + "SELECT CodDriverCentro,CodCentroDestino,Porcentaje\n"
            + "FROM MOV_DRIVER_CENTRO\n"
            + "WHERE RepartoTipo = ? AND Periodo = ?";
    try {
      jdbcTemplate.update(query1, repartoTipo, periodoF);
      return jdbcTemplate.update(query2, repartoTipo, periodoF);
    } catch (Exception e) {
      e.printStackTrace();
      log.warn(e.toString());
    }
    return -1;
  }

  @Override
  public int insertarDistribucionBolsas(int periodo, int repartoTipo) {
    String tabla = "CUENTA_PARTIDA_CENTRO_";
    if (repartoTipo == 1) { tabla = tabla + "R"; }
    if (repartoTipo == 2) { tabla = tabla + "P"; }
    String query = "INSERT INTO FASE_1\n"
            + "(CodCuentaContableOrigen,CodPartidaOrigen,CodCentroOrigen,CodDriver,CodCentroDestino,GrupoGasto,\n"
            + "CodDocumentoClienteOrigen,Monto)\n"
            + "SELECT A.CodCuentaContable,\n"
            + "A.CodPartida,\n"
            + "A.CodCentro,\n"
            + "ISNULL(B.CodDriver,'N/A'),\n"
            + "ISNULL(C.CodEntidadDestino,A.CodCentro),\n"
            + "D.GrupoGasto,\n"
            + "A.CodDocumentoCliente,\n"
            + "A.Monto*ISNULL(C.Porcentaje/100.0,1)\n"
            + "FROM " + tabla + " A\n"
            + "LEFT JOIN TMP_BOLSA_DRIVER B ON A.CodCuentaContable = B.CodCuentaContable\n"
            + "AND A.CodPartida = B.CodPartida\n"
            + "AND A.CodCentro = B.CodCentro\n"
            + "LEFT JOIN TMP_MOV_DRIVER C ON C.CodDriver = B.CodDriver\n"
            + "LEFT JOIN MA_PARTIDA D ON A.CodPartida = D.CodPartida\n"
            + "AND D.RepartoTipo = ?\n"
            + "WHERE A.Periodo = ?";
    try {
      jdbcTemplate.update(query, repartoTipo, periodo);
      return 1;
    } catch (Exception e) {
      log.warn(e.toString());
    }
    return -1;
  }

  @Override
  public List<String> obtenerDriverFase3ConError(int repartoTipo, int periodo) {
    int periodoF = repartoTipo == 1 ? periodo : periodo / 100 * 100;
    try {
      String query = "SELECT \n"
              + "(CASE WHEN T.Estado = 0 THEN\n"
              + "    CONCAT('Centro: ',T.CodCentro,'; Driver: ',T.DRIVER_CODIGO,'; Grupo de gasto:',T.GRUPO_GASTO, \n"
              + "\t' - No tiene driver asignado.') END) as respuesta\n"
              + "from(\n"
              + "\tSELECT A.CodCentro, \n"
              + "\t(case when C.CodDriver is null then 'N/A' else C.CodDriver end) DRIVER_CODIGO,\n"
              + "\t(case when F.GrupoGasto is null then 'N/A' else F.GrupoGasto end) GRUPO_GASTO,\n"
              + "\tMAX(CASE WHEN C.CodDriver IS NULL THEN 0 ELSE 1 END) ESTADO \n"
              + "\tFROM MOV_CENTRO A \n"
              + "\tJOIN MA_CENTRO B \n"
              + "\tON B.TIPO IN ('LINEA','CANAL','FICTICIO','SALUD','PROYECTO') \n"
              + "\tAND A.CodCentro=B.CodCentro \n"
              + "\tJOIN MA_GRUPO_GASTO F \n"
              + "\tON 1=1 \n"
              + "\tLEFT JOIN CENTRO_DRIVER_OBJETO C \n"
              + "\tON C.RepartoTipo=A.RepartoTipo \n"
              + "\tAND C.PERIODO=A.PERIODO \n"
              + "\tAND C.CodCentro=A.CodCentro \n"
              + "\tAND C.GrupoGasto = F.GrupoGasto \n"
              + "\tWHERE A.RepartoTipo= ? AND A.PERIODO= ?\n"
              + "\tGROUP BY A.CodCentro,F.GrupoGasto,\n"
              + "\t(case when C.CodDriver is null then 'N/A' else C.CodDriver end),\n"
              + "\t(case when F.GrupoGasto is null then 'N/A' else F.GrupoGasto end)\n"
              + ") T\n"
              + "WHERE T.Estado !=1\n"
              + "ORDER BY T.CodCentro";
      List<String> lstDriversConError = jdbcTemplate.queryForList(
              query, String.class, repartoTipo, periodoF);
      return lstDriversConError;
    } catch (EmptyResultDataAccessException e) {
      log.info("Sin Errores en Asignaciones Fase 3");
      return new ArrayList<>();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<String> obtenerDriverFase2ConError(int repartoTipo, int periodo) {
    int periodoF = repartoTipo == 1 ? periodo : periodo / 100 * 100;
    try {
      String query = "SELECT (CASE WHEN T.Estado = 0 THEN \n"
              + "CONCAT('Centro: ',T.CodCentro,'; Driver: ',T.CodDriver,' - No tiene driver asignado.')\n"
              + "            WHEN T.Estado = 2 THEN\n"
              + "CONCAT('Centro: ',T.CodCentro,'; Driver: ',T.CodDriver,' - Asigna a niveles inferiores.')\n"
              + "            END)\n"
              + "FROM (\n"
              + "SELECT A.CodCentro,\n"
              + "    ISNULL(C.CodDriver,'N/A') CodDriver,\n"
              + "    MAX(CASE WHEN C.CodDriver IS NULL THEN 0\n"
              + "            WHEN B.Nivel<E.Nivel THEN 1\n"
              + "            ELSE 2 END) Estado\n"
              + "FROM MOV_CENTRO A\n"
              + "JOIN MA_CENTRO B ON B.Tipo IN ('STAFF','SOPORTE') \n"
              + "AND A.CodCentro = B.CodCentro AND B.RepartoTipo = ?\n"
              + "LEFT JOIN CENTRO_DRIVER C ON C.RepartoTipo=?\n"
              + "AND C.Periodo = ?\n"
              + "AND C.CodCentro = A.CodCentro\n"
              + "LEFT JOIN MOV_DRIVER_CENTRO D ON D.RepartoTipo=?\n"
              + "AND D.Periodo = ? \n"
              + "AND D.CodDriverCentro = C.CodDriver\n"
              + "LEFT JOIN MA_CENTRO E ON D.CodCentroDestino = E.CodCentro AND E.RepartoTipo = ?\n"
              + "WHERE A.RepartoTipo = ? AND A.Periodo = ?\n"
              + "GROUP BY A.CodCentro,C.CodDriver) T\n"
              + "WHERE T.Estado !=1\n"
              + "ORDER BY T.CodCentro";
      List<String> lstDriversConError = jdbcTemplate.queryForList(
              query, String.class, repartoTipo, repartoTipo, periodoF, repartoTipo, periodoF,
              repartoTipo, repartoTipo, periodoF);
      return lstDriversConError;
    } catch (EmptyResultDataAccessException e) {
      log.info("Sin Errores en Asignaciones Fase 2");
      return new ArrayList<>();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public int insertarEjecucionFin(int periodo, int fase, int repartoTipo) {
    Date fecha = Calendar.getInstance().getTime();
    String query = "UPDATE MOV_EJECUCION\n"
            + "SET FechaFin=?\n"
            + "WHERE RepartoTipo=? AND Periodo=? AND Fase=?";
    try {
      return jdbcTemplate.update(query, fecha, repartoTipo, periodo, fase);
    } catch (Exception e) {
      log.warn(e.toString());
    }
    return -1;
  }

  @Override
  public int terminarProcesosIndefinidos() {
    String query = "update MOV_CIERRE_PROCESO\n"
            + "set Estado=0\n"
            + "where fechaFin is null and fechaini < GETDATE()-0.25 and Estado=1";
    return jdbcTemplate.update(query);
  }

  @Override
  public int obtenerEstadoProceso(int repartoTipo, int periodo) {
    try {
      String query = "SELECT Estado estado\n"
              + "FROM MOV_CIERRE_PROCESO\n"
              + "WHERE Periodo = ? AND RepartoTipo = ?";
      return jdbcTemplate.queryForObject(query, Integer.class, periodo, repartoTipo);
    } catch (EmptyResultDataAccessException e) {
      return -1;
    } catch (Exception e) {
      log.error(e.toString());
      return -2;
    }
  }

  @Override
  public boolean verificarProcesosCompletos(int repartoTipo, int periodo) {
    try {
      String query = "select COUNT(1) c FROM MOV_EJECUCION\n"
              + "WHERE FechaFin IS NOT NULL\n"
              + "AND Periodo = ? AND RepartoTipo = ?";
      int c = jdbcTemplate.queryForObject(query, Integer.class, periodo, repartoTipo);
      return c == 3;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public List<String> verificarLineasNiifAsignadas(int repartoTipo, int periodo) {
    int periodoF = repartoTipo == 1 ? periodo : periodo / 100 * 100;
    String query = "SELECT \n"
            + "    (CASE WHEN T.Estado = 0 THEN\n"
            + "        CONCAT('Linea: ',T.CodLinea,'; Porcentaje Atribuible: ',T.ATRIBUIBLE,\n"
            + "\t\t'; Porcentaje No Atribuible::',T.NO_ATRIBUIBLE, \n"
            + "    ' - No tiene porcentajes asignados.') END) as respuesta\n"
            + "    from(\n"
            + "\t\tSELECT A.CodLinea, \n"
            + "\t\t(case when C.PorcentajeAtribuible is null then 'N/A' else 'ASIGNADO' end) ATRIBUIBLE,\n"
            + "\t\t(case when C.PorcentajeNoAtribuible is null then 'N/A' else 'ASIGNADO' end) NO_ATRIBUIBLE,\n"
            + "\t\tMAX(CASE WHEN C.PorcentajeAtribuible IS NULL THEN 0 ELSE 1 END) ESTADO \n"
            + "\t\tfrom MOV_PRODUCTO A \n"
            + "\t\tJOIN MA_LINEA B \n"
            + "\t\tON B.CodLinea=A.CodLinea \n"
            + "\t\tand A.RepartoTipo=B.RepartoTipo\n"
            + "\t\tLEFT JOIN LINEA_NIIF C \n"
            + "\t\tON C.CodLinea=A.CodLinea\n"
            + "\t\tAND C.PERIODO=A.PERIODO \n"
            + "\t\tAND C.RepartoTipo=A.RepartoTipo \n"
            + "\t\tAND C.Periodo=A.Periodo\n"
            + "\t\tWHERE A.RepartoTipo= ? AND A.PERIODO= ?\n"
            + "\t\tGROUP BY A.CodLinea,\n"
            + "\t\t(case when C.PorcentajeAtribuible is null then 'N/A' else 'ASIGNADO' end),\n"
            + "\t\t(case when C.PorcentajeNoAtribuible is null then 'N/A' else 'ASIGNADO' end)\n"
            + "    ) T\n"
            + "    WHERE T.Estado !=1\n"
            + "    ORDER BY T.CodLinea";
    try
    {
      List<String> lstLineasConError = jdbcTemplate.queryForList(
              query, String.class, repartoTipo, periodoF);
      return lstLineasConError;
    } catch (EmptyResultDataAccessException e) {
      log.info("Sin Errores en Lineas Niif");
      return new ArrayList<>();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public boolean existeInformacionReporteTabla(int repartoTipo, int periodo, int nroReporte) {
    try {
      String query = "SELECT COUNT(1) c\n"
              + "FROM MOV_EJECUCION_REPORTE\n"
              + "WHERE Periodo = ? AND RepartoTipo = ? AND NroReporte = ?";
      int c = jdbcTemplate.queryForObject(query, Integer.class, periodo, repartoTipo, nroReporte);
      return c != 0;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public int insertarGeneracionReporte(int repartoTipo, int periodo, int nroReporte) {
    String deleteQuery = "DELETE FROM MOV_EJECUCION_REPORTE\n"
            + "WHERE Periodo = ? AND RepartoTipo = ? AND NroReporte = ?";
    String insertQuery = "INSERT INTO MOV_EJECUCION_REPORTE (Periodo, RepartoTipo, NroReporte, Fecha)\n"
            + "VALUES(?,?,?,?)";
    try {
      jdbcTemplate.update(deleteQuery, periodo, repartoTipo, nroReporte);
      Date fecha = Calendar.getInstance().getTime();
      return jdbcTemplate.update(insertQuery, periodo, repartoTipo, nroReporte, fecha);
    } catch (Exception e) {
      log.error(e.toString());
      e.printStackTrace();
    }
    return -1;
  }

  @Override
  public int generarReporteBolsasOficinas(int repartoTipo, int periodo) {
    String tabla = "REPORTE_BOLSA_OFICINA_";
    String tablaDetalle = "CUENTA_PARTIDA_CENTRO_";
    if (repartoTipo == 1) {
      tabla = tabla + "R_HST";
      tablaDetalle = tablaDetalle + "R";
    }
    if (repartoTipo == 2) {
      tabla = tabla + "P_HST";
      tablaDetalle = tablaDetalle + "P";
    }
    //TRUNCAR PARTICION??
    //POR AHORA DELETE
    String deleteQuery = "DELETE FROM " + tabla + "\n"
            + "WHERE Periodo = ?";
    String query = "INSERT INTO " + tabla + "\n"
            + "(Periodo, CodCuentaContableOrigen, CuentaContableOrigenNombre,\n"
            + "CodPartidaOrigen, PartidaOrigenNombre, CodCentroOrigen, CentroOrigenNombre, CentroOrigenNivel,\n"
            + "CentroOrigenTipo, CodCentroDestino, CentroDestinoNombre, CentroDestinoNivel, CentroDestinoTipo,\n"
            + "TipoDocumentoClienteOrigen, CodDocumentoClienteOrigen, ClienteOrigen,\n"
            + "Monto, CodDriver, DriverNombre, Asignacion)\n"
            + "SELECT distinct ?,\n"
            + "A.CodCuentaContableOrigen,\n"
            + "C.Nombre,\n"
            + "A.CodPartidaOrigen,\n"
            + "D.Nombre,\n"
            + "A.CodCentroOrigen,\n"
            + "E.Nombre,\n"
            + "E.Nivel,\n"
            + "E.Tipo,\n"
            + "A.CodCentroDestino,\n"
            + "B.Nombre,\n"
            + "B.Nivel,\n"
            + "B.Tipo,\n"
            + "G.TipoDocumentoCliente,\n"
            + "A.CodDocumentoClienteOrigen,\n"
            + "G.Cliente,\n"
            + "A.Monto,\n"
            + "A.CodDriver,\n"
            + "ISNULL(F.Nombre,'N/A'),\n"
            + "CASE WHEN A.CodDriver='N/A' THEN 'SEMBRADO'\n"
            + "    ELSE 'BOLSA'\n"
            + "END ASIGNACION\n"
            + "FROM FASE_1 A\n"
            + "JOIN MA_CENTRO B ON B.CodCentro = A.CodCentroDestino AND B.RepartoTipo = ?\n"
            + "JOIN MA_CUENTA_CONTABLE C ON C.CodCuentaContable = A.CodCuentaContableOrigen AND C.RepartoTipo = ?\n"
            + "JOIN MA_PARTIDA D ON D.CodPartida = A.CodPartidaOrigen AND D.RepartoTipo = ?\n"
            + "JOIN MA_CENTRO E ON E.CodCentro = A.CodCentroOrigen AND E.RepartoTipo = ?\n"
            + "LEFT JOIN " + tablaDetalle + " G On G.CodDocumentoCliente = A.CodDocumentoClienteOrigen AND \n"
            + "A.CodCentroOrigen=G.CodCentro and A.CodPartidaOrigen=G.CodPartida and A.CodCuentaContableOrigen = G.CodCuentaContable\n"
            + "LEFT JOIN MA_DRIVER F ON F.CodDriver = A.CodDriver AND F.CodDriverTipo = 'CECO' AND F.RepartoTipo = ?";
    try {
      jdbcTemplate.update(deleteQuery, periodo);
      jdbcTemplate.update(query, periodo, repartoTipo, repartoTipo, repartoTipo, repartoTipo,repartoTipo);
      insertarGeneracionReporte(repartoTipo, periodo, 1);
      return 1;
    } catch (Exception e) {
      log.warn(e.toString());
      return 0;
    }
  }

  @Override
  public int generarReporteCascada(int repartoTipo, int periodo) {
    String tabla = "REPORTE_CASCADA_";
    String tablaDetalle = "CUENTA_PARTIDA_CENTRO_";
    if (repartoTipo == 1) {
      tabla = tabla + "R_HST";
      tablaDetalle = tablaDetalle + "R";
    }
    if (repartoTipo == 2) {
      tabla = tabla + "P_HST";
      tablaDetalle = tablaDetalle + "P";
    }
    //TRUNCAR PARTICION??
    //POR AHORA DELETE
    String deleteQuery = "DELETE FROM " + tabla + "\n"
            + "WHERE Periodo = ?";
    String query = "INSERT INTO " + tabla + "\n"
            + "(Periodo, Iteracion, CodCuentaContableInicial, CuentaContableInicialNombre, CodPartidaInicial,\n"
            + "PartidaInicialNombre, CodCentroInicial, CentroInicialNombre, CentroInicialNivel, CentroInicialTipo,\n"
            + "CodCentroOrigen, CentroOrigenNombre, CentroOrigenNivel, CentroOrigenTipo, CodCentroDestino,\n"
            + "CentroDestinoNombre, CentroDestinoNivel, CentroDestinoTipo,\n"
            + "TipoDocumentoClienteInicial, CodDocumentoClienteInicial, ClienteInicial,\n"
            + " Monto, CodDriver, DriverNombre)\n"
            + "SELECT distinct ?,\n"
            + "A.Iteracion,\n"
            + "A.CodCuentaContableInicial,\n"
            + "C.Nombre,\n"
            + "A.CodPartidaInicial,\n"
            + "D.Nombre,\n"
            + "A.CodCentroInicial,\n"
            + "E.Nombre,\n"
            + "E.Nivel,\n"
            + "E.Tipo,\n"
            + "A.CodCentroOrigen,\n"
            + "COALESCE(F.Nombre,'N/A'),\n"
            + "COALESCE(F.Nivel,-1),\n"
            + "COALESCE(F.Tipo,'N/A'),\n"
            + "A.CodCentroDestino,\n"
            + "B.Nombre,\n"
            + "B.Nivel,\n"
            + "B.Tipo,\n"
            + "H.TipoDocumentoCliente,\n"
            + "A.CodDocumentoClienteInicial,\n"
            + "H.Cliente,\n"
            + "A.Monto,\n"
            + "A.CodDriver,\n"
            + "G.Nombre\n"
            + "FROM FASE_2 A\n"
            + "JOIN MA_CENTRO B ON B.CodCentro = A.CodCentroDestino AND B.RepartoTipo = ?\n"
            + "JOIN MA_CUENTA_CONTABLE C ON C.CodCuentaContable = A.CodCuentaContableInicial AND C.RepartoTipo = ?\n"
            + "JOIN MA_PARTIDA D ON D.CodPartida = A.CodPartidaInicial AND D.RepartoTipo = ?\n"
            + "JOIN MA_CENTRO E ON E.CodCentro = A.CodCentroInicial AND E.RepartoTipo = ?\n"
            + "LEFT JOIN MA_CENTRO F ON F.CodCentro = A.CodCentroOrigen AND F.RepartoTipo = ?\n"
            + "LEFT JOIN " + tablaDetalle + " H On H.CodDocumentoCliente = A.CodDocumentoClienteInicial AND \n"
            + "A.CodCentroInicial=H.CodCentro and A.CodPartidaInicial=H.CodPartida and A.CodCuentaContableInicial = H.CodCuentaContable\n"
            + "LEFT JOIN MA_DRIVER G ON G.CodDriver = A.CodDriver AND G.CodDriverTipo = 'CECO' AND G.RepartoTipo = ?";
    try {
      jdbcTemplate.update(deleteQuery, periodo);
      jdbcTemplate.update(query, periodo, repartoTipo, repartoTipo, repartoTipo, repartoTipo, repartoTipo, repartoTipo);
      insertarGeneracionReporte(repartoTipo, periodo, 2);
      return 1;
    } catch (Exception e) {
      log.warn(e.toString());
      return 0;
    }
  }

  @Override
  public int actualizarTipoNegocio(int periodo, String tabla, int rowsUpdateTotal) {
    String query = "UPDATE TOP(500000) " + tabla + "\n"
            + "SET TipoNegocio =\n"
            + "CASE\n"
            + "WHEN CodProducto in\n"
            + "('AMED004','AMED005','AMED006','AMED007','AMED008',\n"
            + "'AMED010','AMED011','AMED013','AMED014','AMED015','AMED016','VEHI001',\n"
            + "'VEHI003','VEHI005','VEHI006','VEHI007','VEHI008',\n"
            + "'VEHI009','VEHI010','VEHI011','VEHI012','VEHI014','VEHI018','VEHI019',\n"
            + "'SOAT001','SOAT007','LPER004','LPER005',\n"
            + "'LPER006','LPER007','LPER008','LPER009','LPER010',\n"
            + "'LPER011','LPER012','LPER013','LPER014','LPER015',\n"
            + "'LPER016','LPER017','LPER018','LPER019','VIND001',\n"
            + "'VIND002','VIND003','VIND004','SEPE001','VCRD001',\n"
            + "'RVIT001','RVIT002','RVIT003') THEN 'PERSONA'\n"
            + "WHEN CodProducto in\n"
            + "('AMED001','AMED002','AMED003','AMED009','AMED012',\n"
            + "'VEHI002','VEHI004','VEHI013','SOAT002','SOAT003','SOAT004','SOAT005','SOAT009','LCOM001',\n"
            + "'LCOM002','LCOM003','LCOM004','LCOM005','LCOM006',\n"
            + "'LCOM007','LCOM008','LCOM009','LCOM010','LCOM011',\n"
            + "'LCOM012','LCOM013','LCOM014','LPER001','LPER002',\n"
            + "'LPER003','VGRP002','VGRP003','AFP001','AFP002',\n"
            + "'AFP003') THEN 'EMPRESA'\n"
            + "WHEN CodProducto in ('VGRP001') and\n"
            + "CodCanal in('GNC','ALZ')\n"
            + "THEN 'PERSONA'\n"
            + "WHEN CodProducto in ('VGRP001') and\n"
            + "CodCanal not in('GNC','ALZ')\n"
            + "THEN 'EMPRESA'\n"
            + "END\n"
            + "WHERE Periodo = ?\n"
            + "AND TipoNegocio is NULL";
    try {
      for (int i = 0; i < rowsUpdateTotal; i = i + 500000) {
        log.info("UPDATING TIPO NEGOCIO - ROWS " + (i + 1) + " - " + (i + 500000) + " OF " + rowsUpdateTotal);
        jdbcTemplate.update(query, periodo);
      }
      //return jdbcTemplate.update(query, periodo);
      return 1;
    } catch (Exception e) {
      log.warn(e.toString());
    }
    return -1;
  }

  @Override
  public int actualizarAsignacion(int repartoTipo, int periodo, String tabla, int rowsUpdateTotal) {
    String tablaDetalleGasto = "CUENTA_PARTIDA_CENTRO_";
    if (repartoTipo == 1) { tablaDetalleGasto = tablaDetalleGasto + "R"; }
    if (repartoTipo == 2) { tablaDetalleGasto = tablaDetalleGasto + "P"; }
    String query = "UPDATE TOP(500000) V4\n"
            + "SET Asignacion = (\n"
            + "SELECT  V3.Asignacion  FROM \n"
            + "(SELECT V2.CodCuentaContable,V2.CodPartida, V2.Asignacion, V2.Monto FROM \n"
            + "(SELECT CodCuentaContable,CodPartida, MAX(Monto) Maximo FROM \n"
            + "(SELECT CodCuentaContable,CodPartida,\n"
            + "CASE WHEN SUBSTRING(CodCentro,1,CHARINDEX('.',CodCentro,CHARINDEX('.',CodCentro,1)+1)-1) = '60.99' THEN 'DRIVER'\n"
            + "ELSE 'SEMBRADO' END Asignacion, ABS(SUM(Monto)) Monto\n"
            + "FROM " + tablaDetalleGasto + "\n"
            + "WHERE Periodo = ?\n"
            + "GROUP BY CodCuentaContable,CodPartida,\n"
            + "CASE WHEN SUBSTRING(CodCentro,1,CHARINDEX('.',CodCentro,CHARINDEX('.',CodCentro,1)+1)-1) = '60.99' THEN 'DRIVER'\n"
            + "ELSE 'SEMBRADO' END\n"
            + ") v\n"
            + "GROUP BY CodCuentaContable,CodPartida) V1\n"
            + "JOIN \n"
            + "(SELECT CodCuentaContable,CodPartida,\n"
            + "CASE WHEN SUBSTRING(CodCentro,1,CHARINDEX('.',CodCentro,CHARINDEX('.',CodCentro,1)+1)-1) = '60.99' THEN 'DRIVER'\n"
            + "ELSE 'SEMBRADO' END Asignacion, ABS(SUM(Monto)) Monto\n"
            + "FROM " + tablaDetalleGasto + "\n"
            + "WHERE Periodo = ?\n"
            + "GROUP BY CodCuentaContable,CodPartida,\n"
            + "CASE WHEN SUBSTRING(CodCentro,1,CHARINDEX('.',CodCentro,CHARINDEX('.',CodCentro,1)+1)-1) = '60.99' THEN 'DRIVER'\n"
            + "ELSE 'SEMBRADO' END) V2 ON V1.CodCuentaContable=V2.CodCuentaContable AND V1.CodPartida=V2.CodPartida\n"
            + "WHERE V1.Maximo=V2.Monto) V3 \n"
            + "WHERE V3.CodCuentaContable = V4.CodCuentaContableInicial\n"
            + "AND V3.CodPartida=V4.CodPartidaInicial)\n"
            + "FROM " + tabla + " V4\n"
            + "WHERE Periodo = ?\n"
            + "AND Asignacion is NULL";
    try {
      for (int i = 0; i < rowsUpdateTotal; i = i + 500000) {
        log.info("UPDATING ASIGNACION - ROWS " + (i + 1) + " - " + (i + 500000) + " OF " + rowsUpdateTotal);
        jdbcTemplate.update(query, periodo, periodo, periodo);
      }
      //return jdbcTemplate.update(query, periodo, periodo, periodo);
      return 1;
    } catch (Exception e) {
      log.warn(e.toString());
    }
    return -1;
  }

  @Override
  public int actualizarNiif17Atribuible(int periodo, String tabla, int rowsUpdateTotal) {
    String query = "UPDATE TOP(500000) A \n"
            + "SET A.ResultadoNiif17Atribuible = \n"
            + "(CASE WHEN A.CtaContableNiif17Atribuible = 'ATRIBUIBLE' \n"
            + "THEN \n"
            + "   (CASE WHEN A.CcInicialNiif17Atribuible = 'ATRIBUIBLE' \n"
            + "   THEN \n"
            + "(CASE WHEN A.CcDestinoNiif17Atribuible = 'ATRIBUIBLE' \n"
            + " THEN 'ATRIBUIBLE' \n"
            + "ELSE 'NO ATRIBUIBLE' \n"
            + "       END) \n"
            + "   ELSE 'NO ATRIBUIBLE' \n"
            + "   END) \n"
            + "ELSE 'NO ATRIBUIBLE' \n"
            + "END)\n"
            + "FROM " + tabla + " A\n"
            + "WHERE Periodo = ?\n"
            + "AND ResultadoNiif17Atribuible is NULL";
    try {
      for (int i = 0; i < rowsUpdateTotal; i = i + 500000) {
        log.info("UPDATING NIIF17 ATRIBUIBLE - ROWS " + (i + 1) + " - " + (i + 500000) + " OF " + rowsUpdateTotal);
        jdbcTemplate.update(query, periodo);
      }
      //return jdbcTemplate.update(query, periodo);
      return 1;
    } catch (Exception e) {
      log.warn(e.toString());
    }
    return -1;
  }

  @Override
  public int generarReporteObjetos(int repartoTipo, int periodo) {
    String tabla = "REPORTE_OBJETO_";
    String tablaDetalle = "CUENTA_PARTIDA_CENTRO_";
    if (repartoTipo == 1) {
      tabla = tabla + "R_HST";
      tablaDetalle = tablaDetalle + "R";
    }
    if (repartoTipo == 2) {
      tabla = tabla + "P_HST";
      tablaDetalle = tablaDetalle + "P";
    }
    int periodoF = repartoTipo == 1 ? periodo : periodo / 100 * 100;
    //TRUNCAR PARTICION??
    //POR AHORA DELETE
    String deleteQuery = "DELETE FROM " + tabla + "\n"
            + "WHERE Periodo = ?";
    String rowsQueryNoNiif = "SELECT COUNT(1) FROM FASE_3 A\n"
                            + "join MA_CUENTA_CONTABLE B on A.CodCuentaContableInicial=B.CodCuentaContable\n"
                            + "where B.CuentaNiif=0";
    String rowsQueryNiif = "SELECT COUNT(1) FROM FASE_3 A\n"
                          + "join MA_CUENTA_CONTABLE B on A.CodCuentaContableInicial=B.CodCuentaContable\n"
                          + "where B.CuentaNiif=1";
    String rowsDeleteQuery = "SELECT COUNT(1) FROM " + tabla + "\n"
            + "WHERE Periodo = ?";
    String queryNoNiif = getInsertReporteObjetoNoNiif(tabla,tablaDetalle);
    String queryNiifAtribuible = getInsertReporteObjetoNiifAtribuible(tabla,tablaDetalle);
    String queryNiifNoAtribuible = getInsertReporteObjetoNiifNoAtribuible(tabla,tablaDetalle);

    try {
      log.info("CIERRE PROCESO - REPORTES OBJETOS - INICIO DELETES.");
      int rowsDeleteTotal = jdbcTemplate.queryForObject(rowsDeleteQuery,Integer.class,periodo);
      String delete = "DELETE TOP(500000) FROM " + tabla + "\n"
              + "WHERE Periodo = ?";
      for (int i = 0; i < rowsDeleteTotal; i = i + 500000) {
        jdbcTemplate.update(delete, periodo);
        log.info("DELETING ROWS " + (i + 1) + " - " + (i + 500000) + " OF " + rowsDeleteTotal);
      }
      log.info("CIERRE PROCESO - REPORTES OBJETOS - FIN DELETES.");
      log.info("CIERRE PROCESO - REPORTES OBJETOS - INICIO INSERTS.");
      log.info("CIERRE PROCESO - REPORTES OBJETOS - INSERTAR CUENTAS NO NIIF");
      int rowsInsertTotalNoNiif = jdbcTemplate.queryForObject(rowsQueryNoNiif,Integer.class);
      for (int i = 0; i < rowsInsertTotalNoNiif; i = i + 500000) {
        String offset = "\nORDER BY 1 OFFSET " + i + " ROWS FETCH NEXT 500000 ROWS ONLY";
        log.info("INSERTING ROWS " + (i + 1) + " - " + (i + 500000) + " OF " + rowsInsertTotalNoNiif);
        jdbcTemplate.update(queryNoNiif + offset, periodo, repartoTipo, repartoTipo, repartoTipo, repartoTipo, repartoTipo, repartoTipo,
                repartoTipo, periodoF, repartoTipo, repartoTipo, periodoF, repartoTipo, repartoTipo,periodoF,repartoTipo);
      }

      log.info("CIERRE PROCESO - REPORTES OBJETOS - INSERTAR CUENTAS NIIF ATRIBUIBLES");
      int rowsInsertTotalNiif = jdbcTemplate.queryForObject(rowsQueryNiif,Integer.class);
      for (int i = 0; i < rowsInsertTotalNiif; i = i + 500000) {
        String offset = "\nORDER BY 1 OFFSET " + i + " ROWS FETCH NEXT 500000 ROWS ONLY";
        log.info("INSERTING ROWS " + (i + 1) + " - " + (i + 500000) + " OF " + rowsInsertTotalNiif);
        jdbcTemplate.update(queryNiifAtribuible + offset, periodo, repartoTipo, repartoTipo, repartoTipo, repartoTipo,
                repartoTipo, repartoTipo,
                repartoTipo, periodoF, repartoTipo, repartoTipo, periodoF, repartoTipo, repartoTipo,periodoF,repartoTipo,
                periodoF,repartoTipo);
      }
      log.info("CIERRE PROCESO - REPORTES OBJETOS - INSERTAR CUENTAS NIIF NO ATRIBUIBLES");
      for (int i = 0; i < rowsInsertTotalNiif; i = i + 500000) {
        String offset = "\nORDER BY 1 OFFSET " + i + " ROWS FETCH NEXT 500000 ROWS ONLY";
        log.info("INSERTING ROWS " + (i + 1) + " - " + (i + 500000) + " OF " + rowsInsertTotalNiif);
        jdbcTemplate.update(queryNiifNoAtribuible + offset, periodo, repartoTipo, repartoTipo, repartoTipo, repartoTipo,
                repartoTipo, repartoTipo,
                repartoTipo, periodoF, repartoTipo, repartoTipo, periodoF, repartoTipo, repartoTipo,periodoF,repartoTipo,
                periodoF,repartoTipo);
      }
      log.info("CIERRE PROCESO - REPORTES OBJETOS - FIN INSERTS.");
      int rowsInsertTotal = rowsInsertTotalNoNiif + 2 *  rowsInsertTotalNiif;
      //UPDATES
      log.info("CIERRE PROCESO - REPORTES OBJETOS - INICIO UPDATE TIPO NEGOCIO.");
      actualizarTipoNegocio(periodo, tabla, rowsInsertTotal);
      log.info("CIERRE PROCESO - REPORTES OBJETOS - FIN UPDATE TIPO NEGOCIO.");
      log.info("CIERRE PROCESO - REPORTES OBJETOS - INICIO UPDATE NIIF17 ATRIBUIBLE.");
      actualizarNiif17Atribuible(periodo, tabla, rowsInsertTotal);
      log.info("CIERRE PROCESO - REPORTES OBJETOS - FIN UPDATE NIIF17 ATRIBUIBLE.");
      log.info("CIERRE PROCESO - REPORTES OBJETOS - INICIO UPDATE ASIGNACION.");
      actualizarAsignacion(repartoTipo, periodo, tabla, rowsInsertTotal);
      log.info("CIERRE PROCESO - REPORTES OBJETOS - FIN UPDATE ASIGNACION.");
      log.info("CIERRE PROCESO - REPORTES OBJETOS - INICIO UPDATE QUITAR MONTOS 0.");
      delete = "DELETE  FROM " + tabla + "\n"
              + "WHERE Periodo = ? AND monto=0";
      jdbcTemplate.update(delete, periodo);
      log.info("CIERRE PROCESO - REPORTES OBJETOS - FIN UPDATE ASIGNACION.");
      insertarGeneracionReporte(repartoTipo, periodo, 3);
      return 1;
    } catch (Exception e) {
      log.warn(e.toString());
      return 0;
    }
  }

  @Override
  public int insertarCierreProceso(int repartoTipo, int periodo, int estado) {
    String insertQuery = "INSERT INTO MOV_CIERRE_PROCESO (Periodo, RepartoTipo, Estado,FechaIni)\n"
            + "VALUES(?,?,?,GETDATE())";
    try {
      return jdbcTemplate.update(insertQuery, periodo, repartoTipo, estado);
    } catch (Exception e) {
      log.error(e.toString());
    }
    return -1;
  }

  @Override
  public int updateCierreProceso(int repartoTipo, int periodo, int estado) {
    Date fecha = Calendar.getInstance().getTime();
    String updateQuery;
    if (estado == 1) {
      updateQuery = "UPDATE MOV_CIERRE_PROCESO\n"
        + "SET Estado = ?, FechaIni=GETDATE()\n"
        + "WHERE Periodo = ? AND RepartoTipo = ?";
    } else if (estado == 2)
    {
      updateQuery = "UPDATE MOV_CIERRE_PROCESO\n"
              + "SET Estado = ?, FechaFin=GETDATE()\n"
              + "WHERE Periodo = ? AND RepartoTipo = ?";
    } else
    {
      updateQuery = "UPDATE MOV_CIERRE_PROCESO\n"
              + "SET Estado = ?\n"
              + "WHERE Periodo = ? AND RepartoTipo = ?";
    }
    try {
      return jdbcTemplate.update(updateQuery, estado, periodo, repartoTipo);
    } catch (Exception e) {
      log.error(e.toString());
    }
    return -1;
  }

  @Override
  public EjecucionProceso obtenerFechaEjecucion(int repartoTipo, int periodo, int fase) {
    try {
      String query = "select FechaFin, CorreoEjecutor, NombreEjecutor from MOV_EJECUCION\n"
              + "where FechaFin IS NOT NULL AND Periodo = ? AND RepartoTipo = ? and fase =?";
      //Date fecha = jdbcTemplate.queryForObject(query, Date.class, periodo, repartoTipo, fase);
      return jdbcTemplate.queryForObject(query, new Object[]{periodo, repartoTipo, fase}, (rs, rowNum) ->
              new EjecucionProceso(
                      rs.getDate("FechaFin").getTime(),
                      rs.getString("CorreoEjecutor"),
                      rs.getString("NombreEjecutor")
              ));
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public int cleanCierreProceso(int repartoTipo, int periodo) {
    String insertQuery = "DELETE FROM MOV_CIERRE_PROCESO WHERE PERIODO = ? AND REPARTOTIPO = ?";
    try {
      return jdbcTemplate.update(insertQuery, periodo, repartoTipo);
    } catch (Exception e) {
      log.error(e.toString());
    }
    return -1;
  }

  @Override
  public List<AsignacionCecoBolsa> getAsignacionesFase1Val3(int repartoTipo, int periodo) {
    String periodoStr = repartoTipo == 1 ? String.valueOf(periodo) : String.valueOf(periodo / 100) + "%" ;
    try {
      String querySelect = "SELECT distinct A.CodCuentaContable,\n"
              + "D.Nombre 'NombreCuentaContable',\n"
              + "A.CodPartida,\n"
              + "E.Nombre 'NombrePartida', \n"
              + "A.CodCentro,\n"
              + "B.Nombre 'NombreCentro',\n"
              + "ISNULL(C.CodDriver,'-') 'CodDriver',\n"
              + "ISNULL(F.Nombre,'-') 'NombreDriver'\n"
              + "FROM CUENTA_PARTIDA_CENTRO_R  A\n"
              + "JOIN MA_CENTRO B ON A.CodCentro = B.CodCentro \n"
              + "AND B.RepartoTipo = ? \n"
              + "AND B.Tipo IN ('BOLSA','OFICINA')\n"
              + "LEFT JOIN CUENTA_PARTIDA_CENTRO_DRIVER_CENTRO C ON C.RepartoTipo = ? \n"
              + "AND C.Periodo = ?\n"
              + "AND A.CodCuentaContable = C.CodCuentaContable \n"
              + "AND A.CodPartida = C.CodPartida\n"
              + "AND A.CodCentro = C.CodCentro\n"
              + "JOIN MA_CUENTA_CONTABLE D ON A.CodCuentaContable = D.CodCuentaContable\n"
              + "AND D.RepartoTipo = ? \n"
              + "JOIN MA_PARTIDA E ON A.CodPartida = E.CodPartida\n"
              + "AND E.RepartoTipo = ?\n"
              + "LEFT JOIN MA_DRIVER F ON C.CodDriver = F.CodDriver\n"
              + "AND F.RepartoTipo = ?\n"
              + "WHERE A.Periodo = ? \n"
              + "ORDER BY A.CodCuentaContable, A.CodPartida, A.CodCentro";

      return jdbcTemplate.query(querySelect, BeanPropertyRowMapper.newInstance(AsignacionCecoBolsa.class),
              repartoTipo, repartoTipo, periodoStr, repartoTipo, repartoTipo, repartoTipo, periodoStr);
    } catch (EmptyResultDataAccessException e) {
      log.warn("Resultado vacío en Asignacion Ceco bolsa ", e);
    }
    return new ArrayList<>();
  }

  private String getInsertReporteObjetoNoNiif(String tabla, String tablaDetalle) {
    return "INSERT INTO   " + tabla + "  \n"
            + "             (Periodo, CodCuentaContableInicial, CuentaContableInicialNombre, CodPartidaInicial, \n"
            + "             PartidaInicialNombre, CodCentroInicial, CentroInicialNombre, CentroInicialNivel, CentroInicialTipo,\n"
            + "             CentroInicialGrupo, TipoDocumentoClienteInicial, CodDocumentoClienteInicial, ClienteInicial,\n"
            + "             CodProducto, ProductoNombre, CodLinea, LineaNombre, CodSubcanal,\n"
            + "             SubcanalNombre, CodCanal, CanalNombre, CodCentroDestino, CentroDestinoNombre, CentroDestinoNivel,\n"
            + "             CentroDestinoTipo, GrupoGasto, CodDriver, DriverNombre, Tipo, TipoGasto, Monto,\n"
            + "             CtaContableNiif17Atribuible, CcInicialNiif17Atribuible, CcDestinoNiif17Atribuible, ResultadoNiif17Tipo,\n"
            + "             asiento,TipoReferenciaDocumento,DocumentoContabilizado,Referencia,FechaContable,OrigenNegocio,Moneda,\n"
            + "              MontoMonedaOrigen,TipoMovimiento)\n"
            + "SELECT       ?, \n"
            + "             A.CodCuentaContableInicial,\n"
            + "             G.Nombre,\n"
            + "             A.CodPartidaInicial,\n"
            + "             H.Nombre,\n"
            + "             A.CodCentroInicial,\n"
            + "             I.Nombre,\n"
            + "             I.Nivel,\n"
            + "             I.Tipo,\n"
            + "             I.GrupoCeco,\n"
            + "             (select distinct TipoDocumentoCliente from   " + tablaDetalle + "   \n"
            + "             where CodDocumentoCliente = A.CodDocumentoClienteInicial) as documentoCliente,\n"
            + "             A.CodDocumentoClienteInicial,\n"
            + "             (select distinct Cliente from   " + tablaDetalle + "   \n"
            + "             where CodDocumentoCliente = A.CodDocumentoClienteInicial) as cliente,\n"
            + "             A.CodProducto,\n"
            + "             B.Nombre,\n"
            + "             COALESCE(J2.CodLinea,'N/A'),\n"
            + "             COALESCE(J2.Nombre,'N/A'),\n"
            + "             A.CodSubcanal,\n"
            + "             C.Nombre as nombreC,\n"
            + "             COALESCE(K2.CodCanal,'N/A'),\n"
            + "             COALESCE(K2.Nombre,'N/A'),\n"
            + "             A.CodCentroDestino,\n"
            + "             L.Nombre,\n"
            + "             L.Nivel,\n"
            + "             L.Tipo,\n"
            + "             A.GrupoGasto,\n"
            + "             A.CodDriver,\n"
            + "             M.Nombre,\n"
            + "             CASE WHEN A.CodCentroInicial = A.CodCentroDestino THEN 'PROPIO' ELSE 'ASIGNADO' END,\n"
            + "             CASE WHEN I.TipoGasto = 0 THEN 'INDIRECTO' ELSE 'DIRECTO' END,\n"
            + "             A.Monto,\n"
            + "             (CASE WHEN G.Niif17Atribuible = 'SI' THEN 'ATRIBUIBLE' ELSE 'NO ATRIBUIBLE' END),\n"
            + "             (CASE WHEN I.Niif17Atribuible = 'SI' THEN 'ATRIBUIBLE' ELSE 'NO ATRIBUIBLE' END),\n"
            + "             (CASE WHEN L.Niif17Atribuible = 'SI' THEN 'ATRIBUIBLE' ELSE 'NO ATRIBUIBLE' END),\n"
            + "             (CASE WHEN A.CodCentroInicial = '60.21.03' \n"
            + "             THEN 'ADQUISICION' \n"
            + "             ELSE (CASE WHEN G.Niif17Tipo = 'GA' THEN 'ADQUISICION' ELSE 'MANTENIMIENTO' END) \n"
            + "             END),\n"
            + "             X.Asiento,\n"
            + "             X.TipoReferenciaDocumento,\n"
            + "             X.DocumentoContabilizado,\n"
            + "             X.Referencia,\n"
            + "             X.FechaContable,\n"
            + "             X.OrigenNegocio,\n"
            + "             X.Moneda,\n"
            + "             X.MontoMonedaOrigen,\n"
            + "             X.TipoMovimiento\n"
            + "             FROM FASE_3 A\n"
            + "             JOIN MA_PRODUCTO B ON B.CodProducto = A.CodProducto AND B.RepartoTipo = ?\n"
            + "             JOIN MA_SUBCANAL C ON C.CodSubcanal = A.CodSubcanal AND C.RepartoTipo = ?\n"
            + "             JOIN MA_GRUPO_GASTO E ON E.GrupoGasto = A.GrupoGasto\n"
            + "             JOIN MA_CUENTA_CONTABLE G ON A.CodCuentaContableInicial = G.CodCuentaContable AND G.RepartoTipo = ?\n"
            + "               AND G.CuentaNiif=0\n"
            + "             LEFT JOIN MA_PARTIDA H ON A.CodPartidaInicial = H.CodPartida AND H.RepartoTipo = ?\n"
            + "             LEFT JOIN MA_CENTRO I ON A.CodCentroInicial = I.CodCentro AND I.RepartoTipo = ?\n"
            + "             LEFT JOIN MA_CENTRO L ON A.CodCentroDestino = L.CodCentro AND L.RepartoTipo = ?\n"
            + "             LEFT JOIN MA_DRIVER M ON A.CodDriver = M.CodDriver AND M.RepartoTipo = ?\n"
            + "             LEFT JOIN MOV_PRODUCTO J1 ON J1.Periodo = ? AND J1.RepartoTipo = ? AND J1.CodProducto = A.CodProducto\n"
            + "             LEFT JOIN MA_LINEA J2 ON J2.CodLinea = J1.CodLinea AND J2.RepartoTipo = ?\n"
            + "             LEFT JOIN MOV_SUBCANAL K1 ON K1.Periodo = ? AND K1.RepartoTipo = ? AND K1.CodSubcanal = A.CodSubcanal\n"
            + "             LEFT JOIN MA_CANAL K2 ON K2.CodCanal = K1.CodCanal AND K2.RepartoTipo = ?\n"
            + "             LEFT JOIN (\n"
            + "             select codCuentaContable, CodPartida,CodCentro,CodDocumentoCliente,\n"
            + "             max(Asiento) as asiento, max(TipoReferenciaDocumento) as TipoReferenciaDocumento, \n"
            + "             max(DocumentoContabilizado) as DocumentoContabilizado, max(Referencia) as Referencia,\n"
            + "             max(FechaContable) as FechaContable, max(OrigenNegocio) as OrigenNegocio, max(Moneda) as Moneda, \n"
            + "             sum(MontoMonedaOrigen) as MontoMonedaOrigen, max(TipoMovimiento) as TipoMovimiento\n"
            + "             from EXACTUS\n"
            + "             where Periodo=? and RepartoTipo=?\n"
            + "             group by codCuentaContable, CodPartida,CodCentro,CodDocumentoCliente\n"
            + "             ) as X\n"
            + "             ON A.CodCentroInicial = X.CodCentro AND A.CodPartidaInicial = X.CodPartida\n"
            + "             AND A.CodCuentaContableInicial = X.CodCuentaContable AND A.CodDocumentoClienteInicial = X.CodDocumentoCliente";
  }

  private String getInsertReporteObjetoNiifAtribuible(String tabla, String tablaDetalle) {
    return "INSERT INTO   " + tabla + "  \n"
            + "             (Periodo, CodCuentaContableInicial, CuentaContableInicialNombre, CodPartidaInicial, \n"
            + "             PartidaInicialNombre, CodCentroInicial, CentroInicialNombre, CentroInicialNivel, CentroInicialTipo,\n"
            + "             CentroInicialGrupo, TipoDocumentoClienteInicial, CodDocumentoClienteInicial, ClienteInicial,\n"
            + "             CodProducto, ProductoNombre, CodLinea, LineaNombre, CodSubcanal,\n"
            + "             SubcanalNombre, CodCanal, CanalNombre, CodCentroDestino, CentroDestinoNombre, CentroDestinoNivel,\n"
            + "             CentroDestinoTipo, GrupoGasto, CodDriver, DriverNombre, Tipo, TipoGasto, Monto,\n"
            + "             CtaContableNiif17Atribuible, CcInicialNiif17Atribuible, CcDestinoNiif17Atribuible, ResultadoNiif17Tipo,\n"
            + "             asiento,TipoReferenciaDocumento,DocumentoContabilizado,Referencia,FechaContable,OrigenNegocio,Moneda,\n"
            + "              MontoMonedaOrigen,TipoMovimiento)\n"
            + "SELECT       ?, \n"
            + "             A.CodCuentaContableInicial,\n"
            + "             G.Nombre,\n"
            + "             A.CodPartidaInicial,\n"
            + "             H.Nombre,\n"
            + "             A.CodCentroInicial,\n"
            + "             I.Nombre,\n"
            + "             I.Nivel,\n"
            + "             I.Tipo,\n"
            + "             I.GrupoCeco,\n"
            + "             (select distinct TipoDocumentoCliente from   " + tablaDetalle + "   \n"
            + "             where CodDocumentoCliente = A.CodDocumentoClienteInicial) as documentoCliente,\n"
            + "             A.CodDocumentoClienteInicial,\n"
            + "             (select distinct Cliente from   " + tablaDetalle + "   \n"
            + "             where CodDocumentoCliente = A.CodDocumentoClienteInicial) as cliente,\n"
            + "             A.CodProducto,\n"
            + "             B.Nombre,\n"
            + "             COALESCE(J2.CodLinea,'N/A'),\n"
            + "             COALESCE(J2.Nombre,'N/A'),\n"
            + "             A.CodSubcanal,\n"
            + "             C.Nombre as nombreC,\n"
            + "             COALESCE(K2.CodCanal,'N/A'),\n"
            + "             COALESCE(K2.Nombre,'N/A'),\n"
            + "             A.CodCentroDestino,\n"
            + "             L.Nombre,\n"
            + "             L.Nivel,\n"
            + "             L.Tipo,\n"
            + "             A.GrupoGasto,\n"
            + "             A.CodDriver,\n"
            + "             M.Nombre,\n"
            + "             CASE WHEN A.CodCentroInicial = A.CodCentroDestino THEN 'PROPIO' ELSE 'ASIGNADO' END,\n"
            + "             CASE WHEN I.TipoGasto = 0 THEN 'INDIRECTO' ELSE 'DIRECTO' END,\n"
            + "             A.Monto * S.PorcentajeAtribuible / 100,\n"
            + "             'ATRIBUIBLE',\n"
            + "             (CASE WHEN I.Niif17Atribuible = 'SI' THEN 'ATRIBUIBLE' ELSE 'NO ATRIBUIBLE' END),\n"
            + "             (CASE WHEN L.Niif17Atribuible = 'SI' THEN 'ATRIBUIBLE' ELSE 'NO ATRIBUIBLE' END),\n"
            + "             (CASE WHEN A.CodCentroInicial = '60.21.03' \n"
            + "             THEN 'ADQUISICION' \n"
            + "             ELSE (CASE WHEN G.Niif17Tipo = 'GA' THEN 'ADQUISICION' ELSE 'MANTENIMIENTO' END) \n"
            + "             END),\n"
            + "             X.Asiento,\n"
            + "             X.TipoReferenciaDocumento,\n"
            + "             X.DocumentoContabilizado,\n"
            + "             X.Referencia,\n"
            + "             X.FechaContable,\n"
            + "             X.OrigenNegocio,\n"
            + "             X.Moneda,\n"
            + "             X.MontoMonedaOrigen,\n"
            + "             X.TipoMovimiento\n"
            + "             FROM FASE_3 A\n"
            + "             JOIN MA_PRODUCTO B ON B.CodProducto = A.CodProducto AND B.RepartoTipo = ?\n"
            + "             JOIN MA_SUBCANAL C ON C.CodSubcanal = A.CodSubcanal AND C.RepartoTipo = ?\n"
            + "             JOIN MA_GRUPO_GASTO E ON E.GrupoGasto = A.GrupoGasto\n"
            + "             JOIN MA_CUENTA_CONTABLE G ON A.CodCuentaContableInicial = G.CodCuentaContable AND G.RepartoTipo = ?\n"
            + "               AND G.CuentaNiif=1\n"
            + "             LEFT JOIN MA_PARTIDA H ON A.CodPartidaInicial = H.CodPartida AND H.RepartoTipo = ?\n"
            + "             LEFT JOIN MA_CENTRO I ON A.CodCentroInicial = I.CodCentro AND I.RepartoTipo = ?\n"
            + "             LEFT JOIN MA_CENTRO L ON A.CodCentroDestino = L.CodCentro AND L.RepartoTipo = ?\n"
            + "             LEFT JOIN MA_DRIVER M ON A.CodDriver = M.CodDriver AND M.RepartoTipo = ?\n"
            + "             LEFT JOIN MOV_PRODUCTO J1 ON J1.Periodo = ? AND J1.RepartoTipo = ? AND J1.CodProducto = A.CodProducto\n"
            + "             LEFT JOIN MA_LINEA J2 ON J2.CodLinea = J1.CodLinea AND J2.RepartoTipo = ?\n"
            + "             LEFT JOIN MOV_SUBCANAL K1 ON K1.Periodo = ? AND K1.RepartoTipo = ? AND K1.CodSubcanal = A.CodSubcanal\n"
            + "             LEFT JOIN MA_CANAL K2 ON K2.CodCanal = K1.CodCanal AND K2.RepartoTipo = ?\n"
            + "             LEFT JOIN LINEA_NIIF S ON S.CodLinea = J1.codLinea AND S.Periodo=? AND S.RepartoTipo = ?\n"
            + "             LEFT JOIN (\n"
            + "             select codCuentaContable, CodPartida,CodCentro,CodDocumentoCliente,\n"
            + "             max(Asiento) as asiento, max(TipoReferenciaDocumento) as TipoReferenciaDocumento, \n"
            + "             max(DocumentoContabilizado) as DocumentoContabilizado, max(Referencia) as Referencia,\n"
            + "             max(FechaContable) as FechaContable, max(OrigenNegocio) as OrigenNegocio, max(Moneda) as Moneda, \n"
            + "             sum(MontoMonedaOrigen) as MontoMonedaOrigen, max(TipoMovimiento) as TipoMovimiento\n"
            + "             from EXACTUS\n"
            + "             where Periodo=? and RepartoTipo=?\n"
            + "             group by codCuentaContable, CodPartida,CodCentro,CodDocumentoCliente\n"
            + "             ) as X\n"
            + "             ON A.CodCentroInicial = X.CodCentro AND A.CodPartidaInicial = X.CodPartida\n"
            + "             AND A.CodCuentaContableInicial = X.CodCuentaContable AND A.CodDocumentoClienteInicial = X.CodDocumentoCliente";
  }

  private String getInsertReporteObjetoNiifNoAtribuible(String tabla, String tablaDetalle) {
    return "INSERT INTO   " + tabla + "  \n"
            + "             (Periodo, CodCuentaContableInicial, CuentaContableInicialNombre, CodPartidaInicial, \n"
            + "             PartidaInicialNombre, CodCentroInicial, CentroInicialNombre, CentroInicialNivel, CentroInicialTipo,\n"
            + "             CentroInicialGrupo, TipoDocumentoClienteInicial, CodDocumentoClienteInicial, ClienteInicial,\n"
            + "             CodProducto, ProductoNombre, CodLinea, LineaNombre, CodSubcanal,\n"
            + "             SubcanalNombre, CodCanal, CanalNombre, CodCentroDestino, CentroDestinoNombre, CentroDestinoNivel,\n"
            + "             CentroDestinoTipo, GrupoGasto, CodDriver, DriverNombre, Tipo, TipoGasto, Monto,\n"
            + "             CtaContableNiif17Atribuible, CcInicialNiif17Atribuible, CcDestinoNiif17Atribuible, ResultadoNiif17Tipo,\n"
            + "             asiento,TipoReferenciaDocumento,DocumentoContabilizado,Referencia,FechaContable,OrigenNegocio,Moneda,\n"
            + "              MontoMonedaOrigen,TipoMovimiento)\n"
            + "SELECT       ?, \n"
            + "             A.CodCuentaContableInicial,\n"
            + "             G.Nombre,\n"
            + "             A.CodPartidaInicial,\n"
            + "             H.Nombre,\n"
            + "             A.CodCentroInicial,\n"
            + "             I.Nombre,\n"
            + "             I.Nivel,\n"
            + "             I.Tipo,\n"
            + "             I.GrupoCeco,\n"
            + "             (select distinct TipoDocumentoCliente from   " + tablaDetalle + "   \n"
            + "             where CodDocumentoCliente = A.CodDocumentoClienteInicial) as documentoCliente,\n"
            + "             A.CodDocumentoClienteInicial,\n"
            + "             (select distinct Cliente from   " + tablaDetalle + "   \n"
            + "             where CodDocumentoCliente = A.CodDocumentoClienteInicial) as cliente,\n"
            + "             A.CodProducto,\n"
            + "             B.Nombre,\n"
            + "             COALESCE(J2.CodLinea,'N/A'),\n"
            + "             COALESCE(J2.Nombre,'N/A'),\n"
            + "             A.CodSubcanal,\n"
            + "             C.Nombre as nombreC,\n"
            + "             COALESCE(K2.CodCanal,'N/A'),\n"
            + "             COALESCE(K2.Nombre,'N/A'),\n"
            + "             A.CodCentroDestino,\n"
            + "             L.Nombre,\n"
            + "             L.Nivel,\n"
            + "             L.Tipo,\n"
            + "             A.GrupoGasto,\n"
            + "             A.CodDriver,\n"
            + "             M.Nombre,\n"
            + "             CASE WHEN A.CodCentroInicial = A.CodCentroDestino THEN 'PROPIO' ELSE 'ASIGNADO' END,\n"
            + "             CASE WHEN I.TipoGasto = 0 THEN 'INDIRECTO' ELSE 'DIRECTO' END,\n"
            + "             A.Monto * S.PorcentajeNoAtribuible / 100,\n"
            + "             'NO ATRIBUIBLE',\n"
            + "             (CASE WHEN I.Niif17Atribuible = 'SI' THEN 'ATRIBUIBLE' ELSE 'NO ATRIBUIBLE' END),\n"
            + "             (CASE WHEN L.Niif17Atribuible = 'SI' THEN 'ATRIBUIBLE' ELSE 'NO ATRIBUIBLE' END),\n"
            + "             (CASE WHEN A.CodCentroInicial = '60.21.03' \n"
            + "             THEN 'ADQUISICION' \n"
            + "             ELSE (CASE WHEN G.Niif17Tipo = 'GA' THEN 'ADQUISICION' ELSE 'MANTENIMIENTO' END) \n"
            + "             END),\n"
            + "             X.Asiento,\n"
            + "             X.TipoReferenciaDocumento,\n"
            + "             X.DocumentoContabilizado,\n"
            + "             X.Referencia,\n"
            + "             X.FechaContable,\n"
            + "             X.OrigenNegocio,\n"
            + "             X.Moneda,\n"
            + "             X.MontoMonedaOrigen,\n"
            + "             X.TipoMovimiento\n"
            + "             FROM FASE_3 A\n"
            + "             JOIN MA_PRODUCTO B ON B.CodProducto = A.CodProducto AND B.RepartoTipo = ?\n"
            + "             JOIN MA_SUBCANAL C ON C.CodSubcanal = A.CodSubcanal AND C.RepartoTipo = ?\n"
            + "             JOIN MA_GRUPO_GASTO E ON E.GrupoGasto = A.GrupoGasto\n"
            + "             JOIN MA_CUENTA_CONTABLE G ON A.CodCuentaContableInicial = G.CodCuentaContable AND G.RepartoTipo = ?\n"
            + "               AND G.CuentaNiif=1\n"
            + "             LEFT JOIN MA_PARTIDA H ON A.CodPartidaInicial = H.CodPartida AND H.RepartoTipo = ?\n"
            + "             LEFT JOIN MA_CENTRO I ON A.CodCentroInicial = I.CodCentro AND I.RepartoTipo = ?\n"
            + "             LEFT JOIN MA_CENTRO L ON A.CodCentroDestino = L.CodCentro AND L.RepartoTipo = ?\n"
            + "             LEFT JOIN MA_DRIVER M ON A.CodDriver = M.CodDriver AND M.RepartoTipo = ?\n"
            + "             LEFT JOIN MOV_PRODUCTO J1 ON J1.Periodo = ? AND J1.RepartoTipo = ? AND J1.CodProducto = A.CodProducto\n"
            + "             LEFT JOIN MA_LINEA J2 ON J2.CodLinea = J1.CodLinea AND J2.RepartoTipo = ?\n"
            + "             LEFT JOIN MOV_SUBCANAL K1 ON K1.Periodo = ? AND K1.RepartoTipo = ? AND K1.CodSubcanal = A.CodSubcanal\n"
            + "             LEFT JOIN MA_CANAL K2 ON K2.CodCanal = K1.CodCanal AND K2.RepartoTipo = ?\n"
            + "             LEFT JOIN LINEA_NIIF S ON S.CodLinea = J1.codLinea AND S.Periodo=? AND S.RepartoTipo = ?\n"
            + "             LEFT JOIN (\n"
            + "             select codCuentaContable, CodPartida,CodCentro,CodDocumentoCliente,\n"
            + "             max(Asiento) as asiento, max(TipoReferenciaDocumento) as TipoReferenciaDocumento, \n"
            + "             max(DocumentoContabilizado) as DocumentoContabilizado, max(Referencia) as Referencia,\n"
            + "             max(FechaContable) as FechaContable, max(OrigenNegocio) as OrigenNegocio, max(Moneda) as Moneda, \n"
            + "             sum(MontoMonedaOrigen) as MontoMonedaOrigen, max(TipoMovimiento) as TipoMovimiento\n"
            + "             from EXACTUS\n"
            + "             where Periodo=? and RepartoTipo=?\n"
            + "             group by codCuentaContable, CodPartida,CodCentro,CodDocumentoCliente\n"
            + "             ) as X\n"
            + "             ON A.CodCentroInicial = X.CodCentro AND A.CodPartidaInicial = X.CodPartida\n"
            + "             AND A.CodCuentaContableInicial = X.CodCuentaContable AND A.CodDocumentoClienteInicial = X.CodDocumentoCliente";
  }

}
