package com.water.supplier.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
public class SysUserDao extends BaseJdbcDao {
	
	public List<JSONObject> findUsers(JSONObject params) {
		StringBuffer sql = new StringBuffer();
        List<Object> sqlArgs = new ArrayList<Object>();
        sql.append(" select user_id,user_code,user_name,role_code,tel,openId from sys_user user ");
        sql.append(" where user.user_status = '1' ");
        if(StringUtils.isNotBlank(params.optString("openId"))) {
        	sql.append(" and user.openId = ? ");
        	sqlArgs.add(params.optString("openId"));
        }
        if(StringUtils.isNotBlank(params.optString("password"))) {
        	sql.append(" and user.password = ? ");
        	sqlArgs.add(params.optString("password"));
        }
        if(StringUtils.isNotBlank(params.optString("user_code"))) {
        	sql.append(" and user.user_code = ? ");
        	sqlArgs.add(params.optString("user_code"));
        }
        if(StringUtils.isNotBlank(params.optString("shop_id"))) {
        	sql.append(" and user.shop_id = ? ");
        	sqlArgs.add(params.optString("shop_id"));
        }
        List<JSONObject> userList = super.queryForJsonList(sql.toString(), sqlArgs.toArray());
		return userList;
		
	}

	public int updateUser(String user_id, String openId) {
		StringBuffer sql = new StringBuffer();
        List<Object> sqlArgs = new ArrayList<Object>();
        sql.append(" update sys_user set openId = ?, update_time = now() where user_id = ? ");
        sqlArgs.add(openId);
        sqlArgs.add(user_id);
		int n = super.getJdbcTemplate().update(sql.toString(), sqlArgs.toArray());
		return n;
		
	}
}
