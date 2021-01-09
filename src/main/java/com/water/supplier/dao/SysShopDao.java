package com.water.supplier.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
public class SysShopDao extends BaseJdbcDao {

	/**
	 * 查询所有门店列表
	 * @param json
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<JSONObject> queryShopList(JSONObject params, int start, int limit){
		StringBuffer sql = new StringBuffer();
        List<Object> sqlArgs = new ArrayList<Object>();
        sql.append(" select * from sys_shop order by shop_id ");
        super.appendPageSql(sql, start, limit);
        return super.queryForJsonList(sql.toString(), sqlArgs.toArray());
		
	}
	
	/**
	 * 新增门店地址
	 * @param json
	 * @return
	 */
	public int insertShop(JSONObject params) {
		StringBuffer sql = new StringBuffer();
		List<Object> sqlArgs = new ArrayList<Object>();
		sql.append(" insert into sys_shop (shop_code,shop_source,shop_name,shop_address,shop_leader,shop_tel,shop_status,create_by,create_time,update_by,update_time) ");
		sql.append(" values (?,?,?,?,?,?,?,?,now(),?,now()) ");
		
		sqlArgs.add(params.optString("shop_code"));
		sqlArgs.add(params.optString("shop_source"));
		sqlArgs.add(params.optString("shop_name"));
		sqlArgs.add(params.optString("shop_address"));
		sqlArgs.add(params.optString("shop_leader"));
		sqlArgs.add(params.optString("shop_tel"));
		sqlArgs.add(params.optString("shop_status"));
		sqlArgs.add(params.optString("create_by"));
		sqlArgs.add(params.optString("update_by"));
		return super.getJdbcTemplate().update(sql.toString(), sqlArgs.toArray());
		
	}

}
