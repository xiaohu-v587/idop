package com.goodcol.util.zxgldbutil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.goodcol.util.AppUtils;

public class ImportData {
public static void main(String[] args) throws Exception {
		
		BufferedWriter userWriter = new BufferedWriter(new FileWriter(new File("E:\\zx\\updateUserInfo0817_new.sql")));
		BufferedWriter userIDWriter = new BufferedWriter(new FileWriter(new File("E:\\zx\\insertUserIDInfo0817_new.sql")));
		BufferedWriter userRoleWriter = new BufferedWriter(new FileWriter(new File("E:\\zx\\insertUserRoleInfo0817_new.sql")));
		
		FileInputStream input = new FileInputStream(new File("e:\\zx\\user20180817-1.csv"));
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input, "gbk"));
		String line = null;
		if(null != bufferedReader){
			bufferedReader.readLine();
			bufferedReader.readLine();
			int num = 1;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println("num: " + num);
				String[] sts = line.split(",");
				String user_id = sts[0];
				System.out.println("user_id: " + user_id);
				//判断用户是否是盐都或者是丁卯桥下的用户
				//boolean flag = findUser(user_id);
				//if(flag){
					//String org_id = sts[3];
					String role_name = sts[1];
					String user_sql = "update sys_user_info set role_id = (select a.id from sys_role_info a"
							+ " where a.name = '"+role_name+"' and a.role_dele_flag = '1') "
							+ " where id = '"+user_id+"';\n";
					userWriter.write(user_sql);
					userWriter.flush();
					
					String id = AppUtils.getStringSeq();
					String id_sql = "delete from gcms_role_apply where user_id = '"+user_id+"';\n"
							+ "insert into gcms_role_apply(id, user_id, role_id) " 
							+ "values('"+id+"', '"+user_id+"', (select a.id from sys_role_info a " 
							+ "where a.name = '"+role_name+"' and a.role_dele_flag = '1'));\n";
					userIDWriter.write(id_sql);
					userIDWriter.flush();
					
					String role_sql = "delete from sys_user_role where user_id = '"+user_id+"' " 
							+ "and role_id = (select a.id from sys_role_info a where a.name = '"
							+ role_name + "' and a.role_dele_flag = '1');\n"
							+ "insert into sys_user_role(user_id, role_id) " 
							+ "values('"+user_id+"', (select a.id from sys_role_info a " 
							+ "where a.name = '"+role_name+"' and a.role_dele_flag = '1'));\n";
					userRoleWriter.write(role_sql);
					userRoleWriter.flush();
				//}
				
				if(num % 2000 == 0){
					userWriter.write("commit;\n");
					userWriter.flush();
					
					userIDWriter.write("commit;\n");
					userIDWriter.flush();
					
					userRoleWriter.write("commit;\n");
					userRoleWriter.flush();
				}
				num++;
			}
		}
		bufferedReader.close();
		input.close();
		userWriter.write("commit;\n");
		userWriter.flush();
		userWriter.close();
		
		userIDWriter.write("commit;\n");
		userIDWriter.flush();
		userIDWriter.close();
		
		userRoleWriter.write("commit;\n");
		userRoleWriter.flush();
		userRoleWriter.close();
	}
	
	public static boolean findUser(String userId) throws Exception {
		boolean flag = false;
		String url = "jdbc:oracle:thin:@22.200.142.46:1521:pccm";
		String driverName = "oracle.jdbc.driver.OracleDriver";
		String user = "pccm";
		String password = "pccm_2016";
		
		Class.forName(driverName);
		Connection conn = DriverManager.getConnection(url, user, password);
		String sql = "select soi.orgnum from sys_org_info soi where soi.by2 = '3' "
				+ "start with soi.id = (select org_id from sys_user_info where user_no = ? ) "
				+ "connect by prior soi.upid = soi.id";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, userId);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			String orgnum = rs.getString("orgnum");
			if("005013000".equals(orgnum)){
				//盐都
				flag = true;
			}else if("008008000".equals(orgnum)){
				//丁卯桥
				flag = true;
			}else{
				flag = false;
			}
		}
		rs.close();
		stmt.close();
		conn.close();
		return flag;
	}

}
