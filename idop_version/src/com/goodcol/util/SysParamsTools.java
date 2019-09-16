package com.goodcol.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;


public class SysParamsTools {
	/**
	 * 码表查询
	 * @param key
	 * @param val
	 * @return
	 */
	public static List<Record> getParamInfos(String key,String val){
			String [] keys = (key==null?"":key).split(",");
			String [] vals = (val==null?"":val).split(",");
			StringBuffer sql = new StringBuffer();
			List<String> param = new ArrayList<String>();
			sql.append("select * from sys_param_info spi where 1=1 and spi.status='1' ");
			boolean isFirst = true;
			if(keys.length>0&&key!=null&&val==null){
				sql.append(" and spi.key in(");
				for (int i = 0; i < keys.length;i++) {
					if(isFirst)
						isFirst = false;
					else
						sql.append(",");
					sql.append("?");
					param.add(keys[i]);
				}
				sql.append(") ");
			}
			isFirst = true;
			if(val!=null&&keys.length==vals.length){
				sql.append(" and (  ");
				for (int i = 0; i < keys.length; i++) {
					if(isFirst)
						isFirst = false;
					else
						sql.append(" or ");
					sql.append("(spi.key = ? and spi.val = ?)");
					param.add(keys[i]);
					param.add(vals[i]);
				}
				sql.append(") ");
			}
			sql.append(" order by sortnum DESC");
			return Db.find(sql.toString(),param.toArray());
	}
	

	public static Map<String,List<Record>> getParamInfosToMap(String key,String val){
    	List<Record> list = getParamInfos(key,val);
    	Map<String,List<Record>> map = new HashMap<String,List<Record>>();
    	if(AppUtils.StringUtil(key)!=null){
    		String [] keys = key.split(",");
    		if(keys.length>0){
    			for (int i = 0; i < keys.length; i++) {
    				map.put(keys[i].toLowerCase(), new ArrayList<Record>());
				}
    			for (Record r : list) {
    				if(map.containsKey(r.getStr("key").toLowerCase())){
    					map.get(r.getStr("key").toLowerCase()).add(r);
    				}
        		}
    		}
    	}
    	return map;
    }
  
	

}
