package com.goodcol.util;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;
import com.goodcol.core.log.Logger;
import com.mchange.v2.c3p0.ComboPooledDataSource;



public class DBUtil {
	public static Logger log = Logger.getLogger(DBUtil.class);
	private static String url;
	private static String driver;
	private static String user;
	private static String pwd;
	private static ComboPooledDataSource dataSource = null;
	
	
    static{
    	
    	Properties prop = new Properties();
	  try {
		  prop.load( DBUtil.class.getClassLoader().getResourceAsStream("config.properties"));
	      driver = prop.getProperty("jdbc.driverClassName");
	      url = prop.getProperty("jdbc.url");
	      user = prop.getProperty("jdbc.username");
	      pwd = prop.getProperty("jdbc.password");
	      
	      dataSource = new ComboPooledDataSource();
	      dataSource.setJdbcUrl(url);
	      dataSource.setDriverClass(driver);
          dataSource.setUser(user);
          dataSource.setPassword(pwd);
          dataSource.setInitialPoolSize(3);//初始化时获取三个连接
          dataSource.setMaxPoolSize(6);//最大连接数
          dataSource.setMaxIdleTime(60);//最大空闲时间60s
      
	  } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (PropertyVetoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    }
	
	
    public static Connection getCon() throws Exception{
	
			try {
				return dataSource.getConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException();
			}
	
	
    }
    
    
    
	 
}
