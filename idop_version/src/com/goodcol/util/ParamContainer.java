 package com.goodcol.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据字典类
 * @author king
 */
public class ParamContainer {
	
	//缓存数据
	public static Map<String,List<Map<String,String>>> cacheValue = new HashMap<String,List<Map<String,String>>>();
	private static boolean flag = false;
	
	/**
	 * 获取缓存map
	 * @return Map<String,List<Map<String,String>>>
	 */
	public static Map<String,List<Map<String,String>>> getCacheValue(){
		return cacheValue;
	}
	
	/**
	 * 初始化数据字典缓存 
	 */
	public void init() {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSet keySet = null;
		try {
			if (flag == false) {
				flag = true;
				cacheValue.clear();

				String sql = "select distinct key from sys_param_info where status='1'";

				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					String key = rs.getString("key");

					pstmt = conn.prepareStatement("select key,val,remark from sys_param_info where status='1' and key = '" + key + "' order by sortnum");
					keySet = pstmt.executeQuery();
					List<Map<String,String>> list = new ArrayList<Map<String,String>>();
					while (keySet.next()) {
						Map<String,String> map = new HashMap<String, String>();
						map.put("dictID", keySet.getString("val"));
						map.put("dictName", keySet.getString("remark"));
						map.put("val", keySet.getString("val"));
						map.put("remark", keySet.getString("remark"));
						list.add(map);
					}
					cacheValue.put(key, list);
				}

			}			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			flag = false;
			close(rs);
			close(keySet);
			close(pstmt);
			close(conn);
		}
	}
	
	/**
	 * 获取数据字典名称
	 * @param dictTypeId
	 * @param value
	 * @return
	 */
	public static String getDictName(String dictTypeId ,String dictId){
		List<Map<String,String>> list = cacheValue.get(dictTypeId);
		String remark = null;
		if(list != null){
			for(Map<String,String> map : list){
				String val = map.get("dictID");
				if(val != null && val.equals(dictId)){
					remark = map.get("dictName");
					return remark;
				}				
			}
		}
			
		return remark;
	}
	
	
	/**
	 * 获取数据字典名称(针对val 为 key=val格式数据)
	 * @param dictTypeId
	 * @param value
	 * @return
	 */
	public static String getDictNameNew(String dictTypeId ,String dictId){
		List<Map<String,String>> list = cacheValue.get(dictTypeId);
		String remark = null;
		if(list != null){
			for(Map<String,String> map : list){
				String val = map.get("dictID");
				if(val.indexOf("=")!=-1){
					if(val != null && val.startsWith(dictId+"=")){
						String [] vals = val.split("=");
						String value = "";
						if(vals.length>1){
							value = vals[1];
						}
						return value;
					}
					
				}else{
					if(val != null && val.equals(dictId)){
						remark = map.get("dictName");
						return remark;
					}
				}
			}
		}
			
		return remark;
	}
	
	/**
	 * 获取数据字典list
	 * @param dictTypeId
	 * @return
	 */
	public static List<Map<String,String>> getDictList(String dictTypeId){
		List<Map<String,String>> list = cacheValue.get(dictTypeId);
		return list;
	}

	/**
	 * 建立数据库连接
	 * @return
	 */
	private static Connection getConnection(){  
        Connection conn=null;  
        try {  
            Class.forName(PropertiesContent.get("jdbc.driverClassName"));//找到oracle驱动器所在的类  
            String url=PropertiesContent.get("jdbc.url"); //URL地址  
            String username=PropertiesContent.get("jdbc.username");  
            String password=PropertiesContent.get("jdbc.password");  
            //System.out.println("url="+url+",username="+username+",password="+password);
            conn=DriverManager.getConnection(url, username, password);  
              
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return conn;  
    }
	
	/**
	 * 关闭
	 * @param pstmt
	 */
	private static void close(PreparedStatement pstmt){  
        if(pstmt !=null){  
            try {  
                pstmt.close();  
            } catch (SQLException e) {      
                e.printStackTrace();  
            }  
        }  
    }  
     
	/**
	 * 关闭
	 * @param rs
	 */
	private static void close(ResultSet rs){  
        if(rs !=null){  
            try {  
                rs.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
    }
    
    /**
     * 关闭
     * @param conn
     */
	private static void close(Connection conn){  
        if(conn !=null){  
            try {  
            	conn.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }  
    } 
}
