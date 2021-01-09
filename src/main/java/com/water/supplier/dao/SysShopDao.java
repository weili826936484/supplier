package com.water.supplier.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
public class SysShopDao extends BaseJdbcDao {

	public List<JSONObject> queryShopList(JSONObject json, int start, int limit){
		StringBuffer sql = new StringBuffer();
        List<Object> sqlArgs = new ArrayList<Object>();
        sql.append(" select * from sys_shop ");
        return super.queryForJsonList(sql.toString(), sqlArgs.toArray());
		
	}
}
