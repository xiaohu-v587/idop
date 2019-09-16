package com.goodcol.util;

/**
 * 
 * @author ftp连接常量
 *
 */
public class Ftp {
    private String ipAddr;//ip 地址
    private Integer port;//端口号
    private String username;//用户名
    private String pwd;//密码
    private String path;//路径
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
    
   
}
