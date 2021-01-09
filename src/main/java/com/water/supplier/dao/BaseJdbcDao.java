package com.water.supplier.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;


public class BaseJdbcDao {
    
    /** JSON数据行映射器 */
    private static final JsonRowMapper JSON_ROW_MAPPER = new JsonRowMapper();
    
    /** 启动时间 */
    private static Date startTime;

    /** JDBC调用模板 */
    private JdbcTemplate jdbcTemplate;


    /**
     * <B>方法名称：</B>初始化JDBC调用模板<BR>
     * <B>概要说明：</B><BR>
     * 
     * @param dataSource 数据源
     */
    @Autowired
    public void initJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        if (startTime == null) {
            startTime = getCurrentTime();
        }
    }


    /**
     * <B>取得：</B>JDBC调用模板<BR>
     * 
     * @return JdbcTemplate JDBC调用模板
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * <B>取得：</B>启动时间<BR>
     * 
     * @return Date 启动时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 
     * <B>方法名称：</B>获取数据库的当前时间<BR>
     * <B>概要说明：</B><BR>
     * 
     * @return Date 当前时间
     */
    public Date getCurrentTime() {
        return this.getJdbcTemplate().queryForObject("SELECT NOW() FROM DUAL", Date.class);
    }

    /**
     * <B>方法名称：</B>查询JSON列表<BR>
     * <B>概要说明：</B><BR>
     * 
     * @param sql SQL语句
     * @param args 参数
     * @return List<JSONObject> JSON列表
     */
    public List<JSONObject> queryForJsonList(String sql, Object... args) {
        return this.jdbcTemplate.query(sql, JSON_ROW_MAPPER, args);
    }
    
    /**
     * 
     * <B>方法名称：</B><BR>
     * <B>概要说明：</B><BR>
     * @param sql sql 语句
     * @param args 参数
     * @return string 列表
     */
    public List<String> queryForStringList(String sql, Object... args) {
        return this.jdbcTemplate.queryForList(sql, String.class, args);
    }

    /**
     * <B>方法名称：</B>查询JSON数据<BR>
     * <B>概要说明：</B><BR>
     * 
     * @param sql SQL语句
     * @param args 参数
     * @return JSONObject JSON数据
     */
    public JSONObject queryForJsonObject(String sql, Object... args) {
        List<JSONObject> jsonList = queryForJsonList(sql, args);
        if (jsonList == null || jsonList.size() < 1) {
            return null;
        }
        return jsonList.get(0);
    }

    /**
     * <B>方法名称：</B>查询文本<BR>
     * <B>概要说明：</B><BR>
     * 
     * @param sql SQL语句
     * @param args 参数
     * @return String 文本
     */
    public String queryForString(String sql, Object... args) {
        List<String> dataList = this.jdbcTemplate.queryForList(sql, args, String.class);
        if (dataList == null || dataList.size() < 1) {
            return null;
        }
        return dataList.get(0);
    }

    /**
     * <B>方法名称：</B>用于 in 通配符(?) 的拼接<BR>
     * <B>概要说明：</B>字段 in(?,?,?,?,?)<BR>
     * 
     * @param sql sql
     * @param sqlArgs 参数容器
     * @param params 参数的个数
     */
    public void appendSqlIn(StringBuffer sql, List<Object> sqlArgs, String[] params) {
        if (params != null && params.length > 0) {
            sql.append(" (");
            for (int i = 0; i < params.length; i++) {
                if (i == 0) {
                    sql.append("?");
                } else {
                    sql.append(",?");
                }
                sqlArgs.add(params[i]);
            }
            sql.append(") ");
        }
    }
    
    
    /**
     * <B>方法名称：</B>适应SQL列名<BR>
     * <B>概要说明：</B><BR>
     * 
     * @param c 原列名
     * @return String 调整后列名
     */
    public static String c(String c) {
        if (StringUtils.isBlank(c)) {
            return null;
        }
        return c.trim().toUpperCase();
    }

    /**
     * <B>方法名称：</B>适应SQL参数<BR>
     * <B>概要说明：</B>防止SQL注入问题<BR>
     * 
     * @param v 参数
     * @return String 调整后参数
     */
    public static String v(String v) {
        if (StringUtils.isBlank(v)) {
            return null;
        }
        return v.trim().replaceAll("'", "''");
    }


    /**
     * <B>方法名称：</B>批量新增數據方法<BR>
     * <B>概要说明：</B><BR>
     * 
     * @param tableName 数据库表名称
     * @param list 插入数据集合
     */
    protected void insertBatch(String tableName, final List<LinkedHashMap<String, Object>> list) {

        if (list.size() <= 0) {
            return;
        }

        LinkedHashMap<String, Object> linkedHashMap = list.get(0);

        StringBuffer sql = new StringBuffer();
        sql.append(" INSERT INTO ");
        sql.append(tableName + " ( ");

        final String[] keyset = (String[]) linkedHashMap.keySet().toArray(new String[linkedHashMap.size()]);

        for (int i = 0; i < linkedHashMap.size(); i++) {
            sql.append(keyset[i] + ",");
        }

        sql.delete(sql.length() - 1, sql.length());

        sql.append(" ) VALUES ( ");
        for (int i = 0; i < linkedHashMap.size(); i++) {
            sql.append("?,");
        }

        sql.delete(sql.length() - 1, sql.length());
        sql.append(" ) ");

        this.getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                LinkedHashMap<String, Object> map = list.get(i);
                Object[] valueset = map.values().toArray(new Object[map.size()]);
                int len = map.keySet().size();
                for (int j = 0; j < len; j++) {
                    ps.setObject(j + 1, valueset[j]);
                }
            }

            public int getBatchSize() {
                return list.size();
            }
        });
    }

}
