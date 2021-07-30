package pe.com.pacifico.kuntur.repository.impl;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pe.com.pacifico.kuntur.model.FiltroLinea;
import pe.com.pacifico.kuntur.model.Reporte1BolsasOficinas;
import pe.com.pacifico.kuntur.model.Reporte2Cascada;
import pe.com.pacifico.kuntur.model.Reporte3Objetos;
import pe.com.pacifico.kuntur.model.Reporte4CascadaCentros;
import pe.com.pacifico.kuntur.model.Reporte5LineaCanal;
import pe.com.pacifico.kuntur.model.Reporte6Control1;
import pe.com.pacifico.kuntur.model.Reporte7Agile1Centros;
import pe.com.pacifico.kuntur.model.Reporte8Niff;
import pe.com.pacifico.kuntur.repository.ReportsJpaRepository;

/**
 * <b>Class</b>: ReportsJdbcRepository <br/>
 * <b>Copyright</b>: 2021 Pacifico Seguros - La Chakra <br/>.
 *
 * @author 2021  Management Solutions <br/>
 * <u>Service Provider</u>: Soliciones Digitales <br/>
 * <u>Developed by</u>: Management Solutions <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     May 10, 2021 Creación de Clase.
 *   </li>
 * </ul>
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class ReportsJdbcRepository implements ReportsJpaRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  private final NamedParameterJdbcTemplate plantilla;

  @Override
  public List<Map<String, Object>> obtenerReporte1BolsasOficinas(int repartoTipo, int periodo) {

    String sufixTabla = repartoTipo == 1 ? "R" : "P";

    String sql = String.format("SELECT Periodo,CodCuentaContableOrigen,CuentaContableOrigenNombre,\n"
            + "CodPartidaOrigen,PartidaOrigenNombre,CodCentroOrigen,CentroOrigenNombre,CentroOrigenNivel,\n"
            + "CentroOrigenTipo,CodCentroDestino,CentroDestinoNombre,\n"
            + "CentroDestinoNivel,CentroDestinoTipo,\n"
            + "TipoDocumentoClienteOrigen,CodDocumentoClienteOrigen,ClienteOrigen,\n"
            + "Monto,CodDriver,DriverNombre,Asignacion\n"
            + "FROM REPORTE_BOLSA_OFICINA_%s_HST\n"
            + "WHERE PERIODO = %d", sufixTabla, periodo);

    System.out.println("Executing " + sql);

    return plantilla.queryForList(sql, (MapSqlParameterSource) null);
    //return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Reporte1BolsasOficinas.class), periodo);
  }

  @Override
  public List<Map<String, Object>> obtenerReporte1Vacio() {

    String sql = String.format("SELECT NULL Periodo, NULL CodCuentaContableOrigen, NULL CuentaContableOrigenNombre,\n"
            + "NULL CodPartidaOrigen, NULL PartidaOrigenNombre, NULL CodCentroOrigen, NULL CentroOrigenNombre, NULL CentroOrigenNivel,\n"
            + "NULL CentroOrigenTipo,NULL CodCentroDestino,NULL CentroDestinoNombre,\n"
            + "NULL TipoDocumentoClienteOrigen, NULL CodDocumentoClienteOrigen, NULL ClienteOrigen,\n"
            + "NULL CentroDestinoNivel, NULL CentroDestinoTipo, NULL Monto, NULL CodDriver, NULL DriverNombre, NULL Asignacion");

    return plantilla.queryForList(sql, (MapSqlParameterSource) null);
  }

  @Override
  public List<Reporte1BolsasOficinas> obtenerReporte1BolsasOficinasLimited(int repartoTipo, int periodo) {

    String sufixTabla = repartoTipo == 1 ? "R" : "P";

    String sql = String.format("SELECT TOP (1000) Periodo,CodCuentaContableOrigen,CuentaContableOrigenNombre,\n"
            + "CodPartidaOrigen,PartidaOrigenNombre,CodCentroOrigen,CentroOrigenNombre,CentroOrigenNivel,\n"
            + "CentroOrigenTipo,CodCentroDestino,CentroDestinoNombre,\n"
            + "CentroDestinoNivel,CentroDestinoTipo,TipoDocumentoClienteOrigen,CodDocumentoClienteOrigen,ClienteOrigen,\n"
            + "Monto,CodDriver,DriverNombre,Asignacion\n"
            + "FROM REPORTE_BOLSA_OFICINA_%s_HST\n"
            + "WHERE PERIODO = ?", sufixTabla);

    System.out.println("Executing " + sql);

    return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Reporte1BolsasOficinas.class), periodo);
  }

  @Override
  public List<Reporte2Cascada> obtenerReporte2Cascada(int repartoTipo, int periodo) {

    String sufixTabla = repartoTipo == 1 ? "R" : "P";

    String sql = String.format("SELECT TOP (1000) CentroDestinoNivel\n"
            + "      ,CentroDestinoNombre\n"
            + "      ,CentroDestinoTipo\n"
            + "      ,CentroInicialNivel\n"
            + "      ,CentroInicialNombre\n"
            + "      ,CentroInicialTipo\n"
            + "      ,CentroOrigenNivel\n"
            + "      ,CentroOrigenNombre\n"
            + "      ,CentroOrigenTipo\n"
            + "      ,CodCentroDestino\n"
            + "      ,CodCentroInicial\n"
            + "      ,CodCentroOrigen\n"
            + "      ,CodCuentaContableInicial\n"
            + "      ,CodDriver\n"
            + "      ,CodPartidaInicial\n"
            + "      ,CuentaContableInicialNombre\n"
            + "      ,TipoDocumentoClienteInicial\n"
            + "      ,CodDocumentoClienteInicial\n"
            + "      ,ClienteInicial\n"
            + "      ,DriverNombre\n"
            + "      ,Iteracion\n"
            + "      ,Monto\n"
            + "      ,PartidaInicialNombre\n"
            + "      ,Periodo\n"
            + "  FROM REPORTE_CASCADA_%s_HST"
            + "  WHERE periodo = ?", sufixTabla);

    return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Reporte2Cascada.class), periodo);
  }

  @Override
  public List<Reporte3Objetos> obtenerReporte3Objetos(int repartoTipo, int periodo) {

    String sufixTabla = repartoTipo == 1 ? "R" : "P";

    String sql = String.format("SELECT TOP (1000) Asignacion\n"
            + "      ,CanalNombre\n"
            + "      ,CcDestinoNiif17Atribuible\n"
            + "      ,CcInicialNiif17Atribuible\n"
            + "      ,CentroDestinoNivel\n"
            + "      ,CentroDestinoNombre\n"
            + "      ,CentroDestinoTipo\n"
            + "      ,CentroInicialGrupo\n"
            + "      ,CentroInicialNivel\n"
            + "      ,CentroInicialNombre\n"
            + "      ,CentroInicialTipo\n"
            + "      ,CodCanal\n"
            + "      ,CodCentroDestino\n"
            + "      ,CodCentroInicial\n"
            + "      ,CodCuentaContableInicial\n"
            + "      ,CodDriver\n"
            + "      ,CodLinea\n"
            + "      ,CodPartidaInicial\n"
            + "      ,CodProducto\n"
            + "      ,CodSubcanal\n"
            + "      ,CtaContableNiif17Atribuible\n"
            + "      ,CuentaContableInicialNombre\n"
            + "      ,TipoDocumentoClienteInicial\n"
            + "      ,CodDocumentoClienteInicial\n"
            + "      ,ClienteInicial\n"
            + "      ,DriverNombre\n"
            + "      ,GrupoGasto\n"
            + "      ,LineaNombre\n"
            + "      ,Monto\n"
            + "      ,PartidaInicialNombre\n"
            + "      ,Periodo\n"
            + "      ,ProductoNombre\n"
            + "      ,ResultadoNiif17Atribuible\n"
            + "      ,ResultadoNiif17Tipo\n"
            + "      ,SubcanalNombre\n"
            + "      ,Tipo\n"
            + "      ,TipoGasto\n"
            + "      ,TipoNegocio\n"
            + "      ,Asiento\n"
            + "      ,TipoReferenciaDocumento\n"
            + "      ,DocumentoContabilizado\n"
            + "      ,Referencia\n"
            + "      ,FechaContable\n"
            + "      ,OrigenNegocio\n"
            + "      ,Moneda\n"
            + "      ,MontoMonedaOrigen\n"
            + "      ,TipoMovimiento\n"
            + "  FROM REPORTE_OBJETO_%s_HST"
            + "  WHERE periodo = ?", sufixTabla);

    return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Reporte3Objetos.class), periodo);
  }

  @Override
  public List<Map<String, Object>> obtenerReporte4CascadaCentros(int repartoTipo, int periodo) {

    String sufixTabla = repartoTipo == 1 ? "R" : "P";

    String sql = String.format("SELECT Periodo,\n"
            + "                       ITERACION,\n"
            + "                       CodCentroDestino,\n"
            + "                       CentroDestinoNombre,\n"
            + "                       CentroDestinoNivel,\n"
            + "                       CentroDestinoTipo,\n"
            + "                       CodCentroOrigen,\n"
            + "                       CentroOrigenNombre,\n"
            + "                       CentroOrigenNivel,\n"
            + "                       CentroOrigenTipo,\n"
            + "                       CASE WHEN CodCentroOrigen=CodCentroDestino THEN 'PROPIO'\n"
            + "                            ELSE 'ASIGNADO'\n"
            + "                       END TIPO,\n"
            + "                       SUM(MONTO) MONTO\n"
            + "                  FROM REPORTE_CASCADA_%s_HST WHERE Periodo = %d\n"
            + "                 GROUP BY Periodo, ITERACION,\n"
            + "                       CodCentroDestino,\n"
            + "                       CentroDestinoNombre,\n"
            + "                       CentroDestinoNivel,\n"
            + "                       CentroDestinoTipo,\n"
            + "                       CodCentroOrigen,\n"
            + "                       CentroOrigenNombre,\n"
            + "                       CentroOrigenNivel,\n"
            + "                       CentroOrigenTipo,\n"
            + "                       CASE WHEN CodCentroOrigen=CodCentroDestino THEN 'PROPIO'\n"
            + "                            ELSE 'ASIGNADO'\n"
            + "                       END ", sufixTabla, periodo);

    return plantilla.queryForList(sql, (MapSqlParameterSource) null);
  }

  @Override
  public List<Reporte4CascadaCentros> obtenerReporte4CascadaCentrosLimited(int repartoTipo, int periodo) {

    String sufixTabla = repartoTipo == 1 ? "R" : "P";

    String sql = String.format("SELECT TOP (1000) Periodo,\n"
            + "                       ITERACION,\n"
            + "                       CodCentroDestino,\n"
            + "                       CentroDestinoNombre,\n"
            + "                       CentroDestinoNivel,\n"
            + "                       CentroDestinoTipo,\n"
            + "                       CodCentroOrigen,\n"
            + "                       CentroOrigenNombre,\n"
            + "                       CentroOrigenNivel,\n"
            + "                       CentroOrigenTipo,\n"
            + "                       CASE WHEN CodCentroOrigen=CodCentroDestino THEN 'PROPIO'\n"
            + "                            ELSE 'ASIGNADO'\n"
            + "                       END TIPO,\n"
            + "                       SUM(MONTO) MONTO\n"
            + "                  FROM REPORTE_CASCADA_%s_HST WHERE Periodo = %d\n"
            + "                 GROUP BY Periodo, ITERACION,\n"
            + "                       CodCentroDestino,\n"
            + "                       CentroDestinoNombre,\n"
            + "                       CentroDestinoNivel,\n"
            + "                       CentroDestinoTipo,\n"
            + "                       CodCentroOrigen,\n"
            + "                       CentroOrigenNombre,\n"
            + "                       CentroOrigenNivel,\n"
            + "                       CentroOrigenTipo,\n"
            + "                       CASE WHEN CodCentroOrigen=CodCentroDestino THEN 'PROPIO'\n"
            + "                            ELSE 'ASIGNADO'\n"
            + "                       END ", sufixTabla, periodo);

    return jdbcTemplate.query(sql,BeanPropertyRowMapper.newInstance(Reporte4CascadaCentros.class));
  }

  @Override
  public List<Map<String, Object>> obtenerReporte4Vacio() {
    String sql = String.format("SELECT NULL Periodo,\n"
            + "                       NULL ITERACION,\n"
            + "                       NULL CodCentroDestino,\n"
            + "                       NULL CentroDestinoNombre,\n"
            + "                       NULL CentroDestinoNivel,\n"
            + "                       NULL CentroDestinoTipo,\n"
            + "                       NULL CodCentroOrigen,\n"
            + "                       NULL CentroOrigenNombre,\n"
            + "                       NULL CentroOrigenNivel,\n"
            + "                       NULL CentroOrigenTipo,\n"
            + "                       NULL TIPO,\n"
            + "                       NULL  MONTO\n");

    return plantilla.queryForList(sql, (MapSqlParameterSource) null);
  }

  @Override
  public List<Map<String, Object>> obtenerReporte5LineaCanal(int repartoTipo, int periodo,
                                                             List<String> lstFiltrosDimension, List<String> lstFiltrosLinea,
                                                             List<String> lstFiltrosCanal) {
    String queryStr = String.format("SELECT %d Periodo, ", periodo);
    if (lstFiltrosDimension.contains("CUENTA CONTABLE INICIAL")) {
      queryStr += "A.CodCuentaContableInicial,A.CuentaContableInicialNombre,\n       ";
    }
    if (lstFiltrosDimension.contains("PARTIDA INICIAL")) {
      queryStr += "A.CodPartidaInicial,A.PartidaInicialNombre,\n       ";
    }
    if (lstFiltrosDimension.contains("CENTRO INICIAL")) {
      queryStr += "A.CodCentroInicial,A.CentroInicialNombre,\n       ";
    }
    String sufixTabla = repartoTipo == 1 ? "R" : "P";
    queryStr += "B.TipoGasto,\n       ";
    queryStr += String.format("A.CodLinea,A.LineaNombre,\n"
                    + "       A.CodCanal,A.CanalNombre,\n"
                    + "       SUM(A.Monto) Monto\n"
                    + "  FROM REPORTE_OBJETO_%s_HST A \n"
                    + "  JOIN MA_CENTRO B ON A.CodCentroInicial=B.CodCentro AND B.RepartoTipo = %d\n"
                    + " WHERE Periodo = %d\n",
            sufixTabla, repartoTipo, periodo);
    if (lstFiltrosLinea.isEmpty()) {
      queryStr += String.format("   AND 1=0\n");
    } else {
      queryStr += String.format("   AND A.CodLinea IN ('%s'", lstFiltrosLinea);
      queryStr = lstFiltrosLinea.stream().map((item) -> String.format(",'%s'", item)).reduce(queryStr, String::concat);
      queryStr += ")\n";
    }
    if (lstFiltrosCanal.isEmpty()) {
      queryStr += String.format("   AND 1=0\n");
    } else {
      queryStr += String.format("   AND A.CodCanal IN ('%s'", lstFiltrosCanal);
      queryStr = lstFiltrosCanal.stream().map((item) -> String.format(",'%s'", item)).reduce(queryStr, String::concat);
      queryStr += ")\n";
    }
    queryStr += " GROUP BY A.CodLinea,A.LineaNombre,\n"
            + "       A.CodCanal,A.CanalNombre";
    if (lstFiltrosDimension.contains("CUENTA CONTABLE INICIAL")) {
      queryStr += ",\n       A.CodCuentaContableInicial,A.CuentaContableInicialNombre";
    }
    if (lstFiltrosDimension.contains("PARTIDA INICIAL")) {
      queryStr += ",\n       A.CodPartidaInicial,A.PartidaInicialNombre";
    }
    if (lstFiltrosDimension.contains("CENTRO INICIAL")) {
      queryStr += ",\n       A.CodCentroInicial,A.CentroInicialNombre";
    }
    queryStr += ",\n       B.TipoGasto";

    System.out.println("Query #5 : " + queryStr);

    return plantilla.queryForList(queryStr, (MapSqlParameterSource) null);
  }

  @Override
  public List<Reporte5LineaCanal> obtenerReporte5LineaCanalLimited(int repartoTipo, int periodo,
                                                                   List<String> lstFiltrosDimension, List<String> lstFiltrosLinea,
                                                                   List<String> lstFiltrosCanal)
  {
    String queryStr = String.format("SELECT TOP (1000) %d Periodo, ", periodo);
    if (lstFiltrosDimension.contains("CUENTA CONTABLE INICIAL")) {
      queryStr += "A.CodCuentaContableInicial,A.CuentaContableInicialNombre,\n       ";
    }
    if (lstFiltrosDimension.contains("PARTIDA INICIAL")) {
      queryStr += "A.CodPartidaInicial,A.PartidaInicialNombre,\n       ";
    }
    if (lstFiltrosDimension.contains("CENTRO INICIAL")) {
      queryStr += "A.CodCentroInicial,A.CentroInicialNombre,\n       ";
    }
    String sufixTabla = repartoTipo == 1 ? "R" : "P";
    queryStr += "B.TipoGasto,\n       ";
    queryStr += String.format("A.CodLinea,A.LineaNombre,\n"
                    + "       A.CodCanal,A.CanalNombre,\n"
                    + "       SUM(A.Monto) Monto\n"
                    + "  FROM REPORTE_OBJETO_%s_HST A \n"
                    + "  JOIN MA_CENTRO B ON A.CodCentroInicial=B.CodCentro AND B.RepartoTipo = %d\n"
                    + " WHERE Periodo = %d\n",
            sufixTabla, repartoTipo, periodo);
    if (lstFiltrosLinea.isEmpty()) {
      queryStr += String.format("   AND 1=0\n");
    } else {
      queryStr += String.format("   AND A.CodLinea IN ('%s'", lstFiltrosLinea);
      queryStr = lstFiltrosLinea.stream().map((item) -> String.format(",'%s'", item)).reduce(queryStr, String::concat);
      queryStr += ")\n";
    }
    if (lstFiltrosCanal.isEmpty()) {
      queryStr += String.format("   AND 1=0\n");
    } else {
      queryStr += String.format("   AND A.CodCanal IN ('%s'", lstFiltrosCanal);
      queryStr = lstFiltrosCanal.stream().map((item) -> String.format(",'%s'", item)).reduce(queryStr, String::concat);
      queryStr += ")\n";
    }
    queryStr += " GROUP BY A.CodLinea,A.LineaNombre,\n"
            + "       A.CodCanal,A.CanalNombre";
    if (lstFiltrosDimension.contains("CUENTA CONTABLE INICIAL")) {
      queryStr += ",\n       A.CodCuentaContableInicial,A.CuentaContableInicialNombre";
    }
    if (lstFiltrosDimension.contains("PARTIDA INICIAL")) {
      queryStr += ",\n       A.CodPartidaInicial,A.PartidaInicialNombre";
    }
    if (lstFiltrosDimension.contains("CENTRO INICIAL")) {
      queryStr += ",\n       A.CodCentroInicial,A.CentroInicialNombre";
    }
    queryStr += ",\n       B.TipoGasto";

    System.out.println("Query #5 : " + queryStr);

    return jdbcTemplate.query(queryStr,BeanPropertyRowMapper.newInstance(Reporte5LineaCanal.class));
  }

  @Override
  public List<Map<String, Object>> obtenerReporte5Vacio(List<String> lstFiltrosDimension, List<String> lstFiltrosLinea,
                                                        List<String> lstFiltrosCanal) {
    String queryStr = "SELECT NULL PERIODO, ";
    if (lstFiltrosDimension.contains("CUENTA CONTABLE INICIAL")) {
      queryStr += " NULL CodCuentaContableInicial, NULL CuentaContableInicialNombre,\n       ";
    }
    if (lstFiltrosDimension.contains("PARTIDA INICIAL")) {
      queryStr += " NULL CodPartidaInicial, NULL PartidaInicialNombre,\n       ";
    }
    if (lstFiltrosDimension.contains("CENTRO INICIAL")) {
      queryStr += " NULL CodCentroInicial, NULL CentroInicialNombre,\n       ";
    }

    queryStr += " NULL TipoGasto,\n       ";

    queryStr += String.format(" NULL CodLinea, NULL LineaNombre,\n"
            + "        NULL CodCanal, NULL CanalNombre,\n"
            + "       NULL Monto\n");

    return plantilla.queryForList(queryStr, (MapSqlParameterSource) null);
  }

  @Override
  public List<Map<String, Object>> obtenerReporte6A(int repartoTipo, int periodo) {
    String sufixTabla = repartoTipo == 1 ? "R" : "P";

    System.out.println("*) JDBC");

    String queryStr = String.format(""
                    + "SELECT %d Periodo, A1.CodCentro,\n"
                    + "       A1.CentroNombre,\n"
                    + "       A1.CentroTipo,\n"
                    + "       ISNULL(A2.Monto,0) MontoTablaInput,\n"
                    + "       ISNULL(A3.Monto,0) MontoTablaBolsas,\n"
                    + "       ISNULL(A4.Monto,0) MontoTablaCascadaF,\n"
                    + "       ISNULL(A5.Monto,0) MontoTablaObjetos\n"
                    + "  FROM (SELECT A.CodCentro,\n"
                    + "               B.Nombre CentroNombre,\n"
                    + "               B.Tipo CentroTipo\n"
                    + "          FROM MOV_CENTRO A\n"
                    + "          LEFT JOIN MA_CENTRO B\n"
                    + "            ON A.CodCentro = B.CodCentro\n"
                    + "            AND B.RepartoTipo = %d"
                    + "         WHERE A.Iteracion=-2\n"
                    + "         GROUP BY A.CodCentro,B.Nombre,B.Tipo) A1\n"
                    + "  LEFT JOIN (SELECT A.CodCentro,\n"
                    + "                    SUM(Monto) Monto\n"
                    + "               FROM CUENTA_PARTIDA_CENTRO_%s A\n"
                    + "              WHERE A.Periodo=%d\n"
                    + "              GROUP BY A.CodCentro) A2\n"
                    + "         ON A1.CodCentro=A2.CodCentro\n"
                    + "  LEFT JOIN (SELECT CodCentroDestino CodCentro,\n"
                    + "                    SUM(Monto) Monto\n"
                    + "               FROM REPORTE_BOLSA_OFICINA_%s_HST \n"
                    + "               WHERE Periodo = %d \n"
                    + "              GROUP BY CodCentroDestino) A3\n"
                    + "         ON A1.CodCentro=A3.CodCentro\n"
                    + "  LEFT JOIN (SELECT CodCentroDestino CodCentro,\n"
                    + "                    SUM(Monto) Monto\n"
                    + "                FROM REPORTE_CASCADA_%s_HST \n"
                    + "               WHERE Periodo = %d"
                    + "               AND CentroDestinoTipo NOT IN ('STAFF','SOPORTE')\n"
                    + "               GROUP BY CodCentroDestino) A4\n"
                    + "         ON A1.CodCentro=A4.CodCentro\n"
                    + "  LEFT JOIN (SELECT CodCentroDestino CodCentro,\n"
                    + "                    SUM(Monto) Monto\n"
                    + "               FROM REPORTE_OBJETO_%s_HST\n"
                    + "               WHERE Periodo = %d\n"
                    + "              GROUP BY CodCentroDestino) A5\n"
                    + "         ON A1.CodCentro=A5.CodCentro", periodo,
            repartoTipo, sufixTabla, periodo, sufixTabla, periodo, sufixTabla, periodo, sufixTabla, periodo);

    System.out.println("Query #6A : " + queryStr);

    return plantilla.queryForList(queryStr, (MapSqlParameterSource) null);
  }

  @Override
  public List<Reporte6Control1> obtenerReporte6ALimited(int repartoTipo, int periodo) {
    String sufixTabla = repartoTipo == 1 ? "R" : "P";

    System.out.println("*) JDBC");

    String queryStr = String.format(""
                    + "SELECT TOP (1000) %d Periodo, A1.CodCentro,\n"
                    + "       A1.CentroNombre,\n"
                    + "       A1.CentroTipo,\n"
                    + "       ISNULL(A2.Monto,0) MontoTablaInput,\n"
                    + "       ISNULL(A3.Monto,0) MontoTablaBolsas,\n"
                    + "       ISNULL(A4.Monto,0) MontoTablaCascadaF,\n"
                    + "       ISNULL(A5.Monto,0) MontoTablaObjetos\n"
                    + "  FROM (SELECT A.CodCentro,\n"
                    + "               B.Nombre CentroNombre,\n"
                    + "               B.Tipo CentroTipo\n"
                    + "          FROM MOV_CENTRO A\n"
                    + "          LEFT JOIN MA_CENTRO B\n"
                    + "            ON A.CodCentro = B.CodCentro\n"
                    + "            AND B.RepartoTipo = %d"
                    + "         WHERE A.Iteracion=-2\n"
                    + "         GROUP BY A.CodCentro,B.Nombre,B.Tipo) A1\n"
                    + "  LEFT JOIN (SELECT A.CodCentro,\n"
                    + "                    SUM(Monto) Monto\n"
                    + "               FROM CUENTA_PARTIDA_CENTRO_%s A\n"
                    + "              WHERE A.Periodo=%d\n"
                    + "              GROUP BY A.CodCentro) A2\n"
                    + "         ON A1.CodCentro=A2.CodCentro\n"
                    + "  LEFT JOIN (SELECT CodCentroDestino CodCentro,\n"
                    + "                    SUM(Monto) Monto\n"
                    + "               FROM REPORTE_BOLSA_OFICINA_%s_HST \n"
                    + "               WHERE Periodo = %d \n"
                    + "              GROUP BY CodCentroDestino) A3\n"
                    + "         ON A1.CodCentro=A3.CodCentro\n"
                    + "  LEFT JOIN (SELECT CodCentroDestino CodCentro,\n"
                    + "                    SUM(Monto) Monto\n"
                    + "                FROM REPORTE_CASCADA_%s_HST \n"
                    + "               WHERE Periodo = %d"
                    + "               AND CentroDestinoTipo NOT IN ('STAFF','SOPORTE')\n"
                    + "               GROUP BY CodCentroDestino) A4\n"
                    + "         ON A1.CodCentro=A4.CodCentro\n"
                    + "  LEFT JOIN (SELECT CodCentroDestino CodCentro,\n"
                    + "                    SUM(Monto) Monto\n"
                    + "               FROM REPORTE_OBJETO_%s_HST\n"
                    + "               WHERE Periodo = %d\n"
                    + "              GROUP BY CodCentroDestino) A5\n"
                    + "         ON A1.CodCentro=A5.CodCentro", periodo,
            repartoTipo, sufixTabla, periodo, sufixTabla, periodo, sufixTabla, periodo, sufixTabla, periodo);

    System.out.println("Query #6A : " + queryStr);

    return jdbcTemplate.query(queryStr,BeanPropertyRowMapper.newInstance(Reporte6Control1.class));
  }

  @Override
  public List<Map<String, Object>> obtenerReporte6B(int repartoTipo, int periodo) {
    String sufixTabla = repartoTipo == 1 ? "R" : "P";

    String queryStr = String.format(""
                    + "SELECT %d Periodo, A.CodCuentaContableInicial CodCuentaContable,\n"
                    + "       A.CuentaContableInicialNombre CuentaContableNombre,\n"
                    + "       A.CodPartidaInicial CodPartida,\n"
                    + "       A.PartidaInicialNombre PartidaNombre,\n"
                    + "       B.Monto MontoTablaInput,\n"
                    + "       A.Monto MontoTablaObjetos,\n"
                    + "       B.CntCentro CntCentrosTablaInput,\n"
                    + "       A.CntCentro CntCentrosTablaObjetos\n"
                    + "  FROM (SELECT CodCuentaContableInicial,\n"
                    + "               CuentaContableInicialNombre,\n"
                    + "               CodPartidaInicial,\n"
                    + "               PartidaInicialNombre,\n"
                    + "               COUNT(DISTINCT CodCentroInicial) CntCentro,\n"
                    + "               SUM(Monto) Monto\n"
                    + "          FROM REPORTE_OBJETO_%s_HST "
                    + "           WHERE PERIODO = %d \n"
                    + "         GROUP BY CodCuentaContableInicial,\n"
                    + "               CuentaContableInicialNombre,\n"
                    + "               CodPartidaInicial,\n"
                    + "               PartidaInicialNombre) A\n"
                    + "  LEFT JOIN (SELECT CodCuentaContable,\n"
                    + "                    CodPartida,\n"
                    + "                    COUNT(1) CntCentro,\n"
                    + "                    SUM(MONTO) Monto\n"
                    + "               FROM CUENTA_PARTIDA_CENTRO_%s\n"
                    + "              WHERE Periodo=%d \n"
                    + "              GROUP BY CodPartida,\n"
                    + "                    CodCuentaContable) B\n"
                    + "    ON A.CodCuentaContableInicial=B.CodCuentaContable\n"
                    + "   AND A.CodPartidaInicial=B.CodPartida", periodo,
            sufixTabla, periodo, sufixTabla, periodo);

    System.out.println("Query #6B : " + queryStr);

    return plantilla.queryForList(queryStr, (MapSqlParameterSource) null);
  }

  @Override
  public List<Map<String, Object>> obtenerReporte6AVacio() {
    String queryStr = String.format(""
            + "SELECT NULL Periodo,\n"
            + "       NULL CodCentro,\n"
            + "       NULL CentroNombre,\n"
            + "       NULL CentroTipo,\n"
            + "       NULL MontoTablaInput,\n"
            + "       NULL MontoTablaBolsas,\n"
            + "       NULL MontoTablaCascadaF,\n"
            + "       NULL MontoTablaObjetos\n");


    return plantilla.queryForList(queryStr, (MapSqlParameterSource) null);
  }

  @Override
  public List<Map<String, Object>> obtenerReporte6BVacio() {
    String queryStr = String.format(""
            + "SELECT NULL Periodo,"
            + "       NULL CodCuentaContable,\n"
            + "       NULL CuentaContableNombre,\n"
            + "       NULL CodPartida,\n"
            + "       NULL PartidaNombre,\n"
            + "       NULL MontoTablaInput,\n"
            + "       NULL MontoTablaObjetos,\n"
            + "       NULL CntCentrosTablaInput,\n"
            + "       NULL CntCentrosTablaObjetos\n");

    return plantilla.queryForList(queryStr, (MapSqlParameterSource) null);
  }

  @Override
  public List<Map<String, Object>> obtenerReporte7A(int repartoTipo, int periodo) {
    String sufixTabla = repartoTipo == 1 ? "R" : "P";

    String queryStr = String.format(""
                    + "SELECT %d Periodo, Iteracion,\n"
                    + "CodCentroDestino,\n"
                    + "CentroDestinoNombre,\n"
                    + "B.GrupoCeco CentroDestinoGrupo,\n"
                    + "CentroDestinoNivel,\n"
                    + "CentroDestinoTipo,\n"
                    + "CodCentroOrigen,\n"
                    + "CentroOrigenNombre,\n"
                    + "A.GrupoCeco CentroOrigenGrupo,\n"
                    + "CentroOrigenNivel,\n"
                    + "CentroOrigenTipo,\n"
                    + "A.CodCentroPadre CodCentroPadreOrigen,\n"
                    + "A.CentroPadreNombre CentroPadreOrigenNombre,\n"
                    + "B.CodCentroPadre CodCentroPadreDestino,\n"
                    + "B.CentroPadreNombre CentroPadreDestinoNombre,\n"
                    + "CASE WHEN CodCentroOrigen=CodCentroDestino THEN 'PROPIO'\n"
                    + "ELSE 'ASIGNADO' END Tipo,\n"
                    + "CASE WHEN A.TipoGasto = 0 THEN 'INDIRECTO'\n"
                    + "ELSE 'DIRECTO' END TipoGasto,\n"
                    + "SUM(Monto) Monto\n"
                    + "FROM REPORTE_CASCADA_%s_HST R\n"
                    + "LEFT JOIN (SELECT a.CodCentro, a.Nombre, a.GrupoCeco, a.TipoGasto, a.CodCentroPadre,  b.Nombre CentroPadreNombre "
                    + "           FROM MA_CENTRO A LEFT "
                    + "           JOIN MA_CENTRO B ON A.CodCentroPadre = B.CodCentro AND B.RepartoTipo = %d "
                    + "           WHERE A.RepartoTipo = %d) A "
                    + "           ON A.CodCentro = R.CodCentroOrigen\n"
                    + "LEFT JOIN (SELECT a.CodCentro, a.Nombre, a.GrupoCeco, a.CodCentroPadre, b.Nombre CentroPadreNombre "
                    + "           FROM MA_CENTRO A LEFT JOIN MA_CENTRO B "
                    + "           ON A.CodCentroPadre = B.CodCentro AND B.RepartoTipo = %d "
                    + "           WHERE A.RepartoTipo = %d) B "
                    + "           ON A.CodCentro = R.CodCentroDestino\n"
                    + "WHERE R.Periodo = %d\n"
                    + "GROUP BY Iteracion,\n"
                    + "CodCentroDestino,\n"
                    + "CentroDestinoNombre,\n"
                    + "B.GrupoCeco,\n"
                    + "CentroDestinoNivel,\n"
                    + "CentroDestinoTipo,\n"
                    + "CodCentroOrigen,\n"
                    + "CentroOrigenNombre,\n"
                    + "A.GrupoCeco,\n"
                    + "CentroOrigenNivel,\n"
                    + "CentroOrigenTipo,\n"
                    + "A.CodCentroPadre,\n"
                    + "A.CentroPadreNombre,\n"
                    + "B.CodCentroPadre,\n"
                    + "B.CentroPadreNombre,\n"
                    + "CASE WHEN CodCentroOrigen=CodCentroDestino THEN 'PROPIO'\n"
                    + "ELSE 'ASIGNADO'\n"
                    + "END,\n"
                    + "CASE WHEN A.TipoGasto = 0 THEN 'INDIRECTO'\n"
                    + "ELSE 'DIRECTO' END", periodo,
            sufixTabla, repartoTipo, repartoTipo, repartoTipo, repartoTipo, periodo);

    System.out.println("Query #7A : " + queryStr);

    return plantilla.queryForList(queryStr, (MapSqlParameterSource) null);
  }

  @Override
  public List<Reporte7Agile1Centros> obtenerReporte7ALimited(int repartoTipo, int periodo) {
    String sufixTabla = repartoTipo == 1 ? "R" : "P";

    String queryStr = String.format(""
                    + "SELECT TOP (1000) %d Periodo, Iteracion,\n"
                    + "CodCentroDestino,\n"
                    + "CentroDestinoNombre,\n"
                    + "B.GrupoCeco CentroDestinoGrupo,\n"
                    + "CentroDestinoNivel,\n"
                    + "CentroDestinoTipo,\n"
                    + "CodCentroOrigen,\n"
                    + "CentroOrigenNombre,\n"
                    + "A.GrupoCeco CentroOrigenGrupo,\n"
                    + "CentroOrigenNivel,\n"
                    + "CentroOrigenTipo,\n"
                    + "A.CodCentroPadre CodCentroPadreOrigen,\n"
                    + "A.CentroPadreNombre CentroPadreOrigenNombre,\n"
                    + "B.CodCentroPadre CodCentroPadreDestino,\n"
                    + "B.CentroPadreNombre CentroPadreDestinoNombre,\n"
                    + "CASE WHEN CodCentroOrigen=CodCentroDestino THEN 'PROPIO'\n"
                    + "ELSE 'ASIGNADO' END Tipo,\n"
                    + "CASE WHEN A.TipoGasto = 0 THEN 'INDIRECTO'\n"
                    + "ELSE 'DIRECTO' END TipoGasto,\n"
                    + "SUM(Monto) Monto\n"
                    + "FROM REPORTE_CASCADA_%s_HST R\n"
                    + "LEFT JOIN (SELECT a.CodCentro, a.Nombre, a.GrupoCeco, a.TipoGasto, a.CodCentroPadre,  b.Nombre CentroPadreNombre "
                    + "           FROM MA_CENTRO A LEFT "
                    + "           JOIN MA_CENTRO B ON A.CodCentroPadre = B.CodCentro AND B.RepartoTipo = %d "
                    + "           WHERE A.RepartoTipo = %d) A "
                    + "           ON A.CodCentro = R.CodCentroOrigen\n"
                    + "LEFT JOIN (SELECT a.CodCentro, a.Nombre, a.GrupoCeco, a.CodCentroPadre, b.Nombre CentroPadreNombre "
                    + "           FROM MA_CENTRO A LEFT JOIN MA_CENTRO B "
                    + "           ON A.CodCentroPadre = B.CodCentro AND B.RepartoTipo = %d "
                    + "           WHERE A.RepartoTipo = %d) B "
                    + "           ON A.CodCentro = R.CodCentroDestino\n"
                    + "WHERE R.Periodo = %d\n"
                    + "GROUP BY Iteracion,\n"
                    + "CodCentroDestino,\n"
                    + "CentroDestinoNombre,\n"
                    + "B.GrupoCeco,\n"
                    + "CentroDestinoNivel,\n"
                    + "CentroDestinoTipo,\n"
                    + "CodCentroOrigen,\n"
                    + "CentroOrigenNombre,\n"
                    + "A.GrupoCeco,\n"
                    + "CentroOrigenNivel,\n"
                    + "CentroOrigenTipo,\n"
                    + "A.CodCentroPadre,\n"
                    + "A.CentroPadreNombre,\n"
                    + "B.CodCentroPadre,\n"
                    + "B.CentroPadreNombre,\n"
                    + "CASE WHEN CodCentroOrigen=CodCentroDestino THEN 'PROPIO'\n"
                    + "ELSE 'ASIGNADO'\n"
                    + "END,\n"
                    + "CASE WHEN A.TipoGasto = 0 THEN 'INDIRECTO'\n"
                    + "ELSE 'DIRECTO' END", periodo,
            sufixTabla, repartoTipo, repartoTipo, repartoTipo, repartoTipo, periodo);

    System.out.println("Query #7A : " + queryStr);

    return jdbcTemplate.query(queryStr,BeanPropertyRowMapper.newInstance(Reporte7Agile1Centros.class));
  }

  @Override
  public List<Map<String, Object>> obtenerReporte7B(int repartoTipo, int periodo) {
    String sufixTabla = repartoTipo == 1 ? "R" : "P";

    String queryStr = String.format("SELECT %d Periodo, A.CodCentroInicial,"
            + "                       A.CentroInicialNombre, "
            + "                       C.CodCentroPadre CodCentroPadreInicial,\n"
            + "                       C.CentroPadreNombre NombreCentroPadreInicial,\n"
            + "                       B.TipoGasto,\n"
            + "                       A.CodLinea,"
            + "                       A.LineaNombre,\n"
            + "                       A.CodCanal,"
            + "                       A.CanalNombre,\n"
            + "                       SUM(A.Monto) Monto\n"
            + "FROM REPORTE_OBJETO_%s_HST A\n"
            + "JOIN MA_CENTRO B ON A.CodCentroInicial=B.CodCentro AND B.RepartoTipo = %d\n"
            + "LEFT JOIN (SELECT a.CodCentro, a.Nombre, a.GrupoCeco, a.TipoGasto,"
            + "            a.CodCentroPadre,  b.Nombre CentroPadreNombre "
            + "           FROM MA_CENTRO A LEFT JOIN MA_CENTRO B ON A.CodCentroPadre = B.CodCentro AND B.RepartoTipo = %d "
            + "           WHERE A.RepartoTipo = %d) C "
            + "ON   C.CodCentro=A.CodCentroInicial\n"
            + "WHERE Periodo = %d\n"
            + "GROUP BY A.CodLinea,A.LineaNombre,\n"
            + "A.CodCanal,A.CanalNombre,\n"
            + "A.CodCentroInicial,A.CentroInicialNombre,\n"
            + "C.CodCentroPadre,\n"
            + "C.CentroPadreNombre,\n"
            + "B.TipoGasto\n", periodo, sufixTabla, repartoTipo, repartoTipo, repartoTipo, periodo);

    System.out.println("Query #7B : " + queryStr);

    return plantilla.queryForList(queryStr, (MapSqlParameterSource) null);
  }

  @Override
  public List<Map<String, Object>> obtenerReporte7AVacio() {
    String queryStr = String.format(""
            + "SELECT NULL Periodo, NULL Iteracion,\n"
            + "NULL CodCentroDestino,\n"
            + "NULL CentroDestinoNombre,\n"
            + "NULL  CentroDestinoGrupo,\n"
            + "NULL CentroDestinoNivel,\n"
            + "NULL CentroDestinoTipo,\n"
            + "NULL CodCentroOrigen,\n"
            + "NULL CentroOrigenNombre,\n"
            + "NULL CentroOrigenGrupo,\n"
            + "NULL CentroOrigenNivel,\n"
            + "NULL CentroOrigenTipo,\n"
            + "NULL CodCentroPadreOrigen,\n"
            + "NULL CentroPadreOrigenNombre,\n"
            + "NULL CodCentroPadreDestino,\n"
            + "NULL CentroPadreDestinoNombre,\n"
            + "NULL Tipo,\n"
            + "NULL TipoGasto,\n"
            + "NULL Monto\n");
    return plantilla.queryForList(queryStr, (MapSqlParameterSource) null);
  }

  @Override
  public List<Map<String, Object>> obtenerReporte7BVacio() {
    String queryStr = String.format("SELECT NULL Periodo, NULL CodCentroInicial,"
            + "                       NULL CentroInicialNombre, "
            + "                       NULL CodCentroPadreInicial,\n"
            + "                       NULL NombreCentroPadreInicial,\n"
            + "                       NULL TipoGasto,\n"
            + "                       NULL CodLinea,"
            + "                       NULL LineaNombre,\n"
            + "                       NULL CodCanal,"
            + "                       NULL CanalNombre,\n"
            + "                       NULL Monto\n");

    return plantilla.queryForList(queryStr, (MapSqlParameterSource) null);
  }

  @Override
  public List<Map<String, Object>> obtenerReporte8(int repartoTipo, int periodo) {
    String sufixTabla = repartoTipo == 1 ? "R" : "P";

    String queryStr = String.format(""
            + "SELECT %d Periodo, A.CodCuentaContableInicial, \n"
            + "A.CuentaContableInicialNombre, \n"
            + "A.CodLinea, \n"
            + "A.LineaNombre, \n"
            + "A.CodProducto, \n"
            + "A.ProductoNombre, \n"
            + "A.CodCanal, \n"
            + "A.CanalNombre, \n"
            + "A.CodSubcanal,\n"
            + "A.SubcanalNombre, \n"
            + "A.ResultadoNiif17Atribuible, \n"
            + "A.ResultadoNiif17Tipo, \n"
            + "(CASE WHEN B.Niif17Clase = 'FI' THEN 'FIJO' ELSE 'VARIABLE' END) Niif17Clase, \n"
            + "SUM(A.Monto) Monto \n"
            + "FROM REPORTE_OBJETO_%s_HST A "
            + "       JOIN MA_CUENTA_CONTABLE B "
            + "       ON A.CodCuentaContableInicial = B.CodCuentaContable AND B.RepartoTipo = %d\n"
            + "WHERE A.Periodo = %d AND B.RepartoTipo = %d\n"
            + "GROUP BY A.CodCuentaContableInicial, A.CuentaContableInicialNombre, A.CodLinea, A.LineaNombre, \n"
            + "A.CodProducto, A.ProductoNombre, A.CodCanal, A.CanalNombre, A.CodSubcanal, \n"
            + "A.SubcanalNombre, A.ResultadoNiif17Atribuible, A.ResultadoNiif17Tipo, B.Niif17Clase \n", periodo,
            sufixTabla, repartoTipo, periodo, repartoTipo);

    System.out.println("Query #8 : " + queryStr);

    return plantilla.queryForList(queryStr, (MapSqlParameterSource) null);
  }

  @Override
  public List<Reporte8Niff> obtenerReporte8Limited(int repartoTipo, int periodo) {
    String sufixTabla = repartoTipo == 1 ? "R" : "P";

    String queryStr = String.format(""
                    + "SELECT TOP (1000) %d Periodo, A.CodCuentaContableInicial, \n"
                    + "A.CuentaContableInicialNombre, \n"
                    + "A.CodLinea, \n"
                    + "A.LineaNombre, \n"
                    + "A.CodProducto, \n"
                    + "A.ProductoNombre, \n"
                    + "A.CodCanal, \n"
                    + "A.CanalNombre, \n"
                    + "A.CodSubcanal,\n"
                    + "A.SubcanalNombre, \n"
                    + "A.ResultadoNiif17Atribuible, \n"
                    + "A.ResultadoNiif17Tipo, \n"
                    + "(CASE WHEN B.Niif17Clase = 'FI' THEN 'FIJO' ELSE 'VARIABLE' END) Niif17Clase, \n"
                    + "SUM(A.Monto) Monto \n"
                    + "FROM REPORTE_OBJETO_%s_HST A "
                    + "       JOIN MA_CUENTA_CONTABLE B "
                    + "       ON A.CodCuentaContableInicial = B.CodCuentaContable AND B.RepartoTipo = %d\n"
                    + "WHERE A.Periodo = %d AND B.RepartoTipo = %d\n"
                    + "GROUP BY A.CodCuentaContableInicial, A.CuentaContableInicialNombre, A.CodLinea, A.LineaNombre, \n"
                    + "A.CodProducto, A.ProductoNombre, A.CodCanal, A.CanalNombre, A.CodSubcanal, \n"
                    + "A.SubcanalNombre, A.ResultadoNiif17Atribuible, A.ResultadoNiif17Tipo, B.Niif17Clase \n", periodo,
            sufixTabla, repartoTipo, periodo, repartoTipo);

    System.out.println("Query #8 : " + queryStr);

    return jdbcTemplate.query(queryStr,BeanPropertyRowMapper.newInstance(Reporte8Niff.class));
  }

  @Override
  public List<Map<String, Object>> obtenerReporte8Vacio() {
    String queryStr = String.format(""
            + "SELECT NULL Periodo, NULL CodCuentaContableInicial, \n"
            + "NULL CuentaContableInicialNombre, \n"
            + "NULL CodLinea, \n"
            + "NULL LineaNombre, \n"
            + "NULL CodProducto, \n"
            + "NULL ProductoNombre, \n"
            + "NULL CodCanal, \n"
            + "NULL CanalNombre, \n"
            + "NULL CodSubcanal,\n"
            + "NULL SubcanalNombre, \n"
            + "NULL ResultadoNiif17Atribuible, \n"
            + "NULL ResultadoNiif17Tipo, \n"
            + "NULL Niif17Clase, \n"
            + "NULL Monto \n");

    return plantilla.queryForList(queryStr, (MapSqlParameterSource) null);
  }

  @Override
  public List<FiltroLinea> obtenerFiltroCodLineas(int repartoTipo, int periodo) {

    if (repartoTipo == 2) {
      periodo = (periodo / 100) * 100;
    }

    String sql = "SELECT DISTINCT CodLinea\n"
            + "FROM MOV_PRODUCTO\n"
            + "WHERE Periodo = ?\n"
            + "AND RepartoTipo = ?";

    return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(FiltroLinea.class), periodo, repartoTipo);
  }

  @Override
  public boolean validarRepo5(int repartoTipo, int periodo,
                              List<String> lineas, List<String> canales) {
    String sufixTabla = repartoTipo == 1 ? "R_HST" : "P_HST";
    String queryStr = "select COUNT(1) from REPORTE_OBJETO_" + sufixTabla;
    queryStr += "  where Periodo = ?";
    if (lineas.isEmpty()) {
      queryStr += " AND 1=0\n";
    } else {
      queryStr += String.format(" AND CodLinea IN ('%s'", lineas);
      queryStr = lineas.stream().map((item) -> String.format(",'%s'", item)).reduce(queryStr, String::concat);
      queryStr += ")\n";
    }
    if (canales.isEmpty()) {
      queryStr += "   AND 1=0\n";
    } else {
      queryStr += String.format("   AND CodCanal IN ('%s'", canales);
      queryStr = canales.stream().map((item) -> String.format(",'%s'", item)).reduce(queryStr, String::concat);
      queryStr += ")\n";
    }

    int respuesta;
    try
    {
      respuesta = jdbcTemplate.queryForObject(queryStr,int.class,periodo);
    } catch (Exception e)
    {
      respuesta = 0;
    }
    return respuesta > 0;
  }
}
