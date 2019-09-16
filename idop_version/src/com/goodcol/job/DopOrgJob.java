package com.goodcol.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

public class DopOrgJob extends OuartzJob {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try{
			Db.use("gbase").update("CALL ap_idop.DROP_IDOP_SYNC_ORG_INFO(@a,@b)");
			Long maxRow =  Db.use("gbase").queryLong("SELECT COUNT(1) FROM SYS_ORG_INFO");
			//List<Record> rList =  Db.use("gbase").find("select * from sys_user_info");
			//String columns = "phone, school_name, remark, dele_flag, addr, cantellergrade, height, post_level, gender, user_type, idcard, health, postcode, cur_post, nation, flag, skill, email, tellergrade, emergentphone, residenaddress, mobile, origo, postid, user_level, birthday, is_teller, pay_level, posttime, namepy, study_level, id, wyid, podate, wage, name, birthaddress, political, marital_stat, jrworktime, user_no, dept_name, photo, stat, org_id, emergentlinker, resume, pwd, xqid, worktime, njrworktime";

			//String insertSql = "insert into sys_user_info("+columns+") values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			//String updateSql = "";
						
			//List<Record> recordList1 = new ArrayList<>();
			
			//List<Record> recordList2 = new ArrayList<>();
			int pageNumber = 1;
			int pageSize = 1000;
			long maxPage = maxRow%pageSize == 0 ? maxRow/pageSize : (maxRow/pageSize)+1;
			
			//不修改字段
			String [] filtercolumns = "".split(",");
			
			for (;pageNumber<=maxPage;pageNumber++) {
				
				Page<Record> page =  Db.use("gbase").paginate(pageNumber, pageSize, "SELECT *", "FROM SYS_ORG_INFO where 1=1 order by id ");
				
				for (Record r : page.getList()) {
				    Record user = Db.findById("sys_org_info",  r.get("id"));
					if(user != null){
						
						for (String filtercolumn : filtercolumns) {
							r.remove(filtercolumn);
						}
						
						Db.update("sys_org_info","id", r);
						//recordList1.add(r);
					}else{
						Db.save("sys_org_info","id", r);
						//recordList2.add(r);
					}
				}
			}
			
			
		
			/*if(recordList2.size() >0 ){
				Db.batch(insertSql, columns, recordList2, 10);
			}*/
			
			//Db.batchSave("sys_user_info", recordList1, 10);
			//Db.batchUpdate("sys_user_info", recordList2, 10);
			success();
		}catch(Exception e){
			error();
			e.printStackTrace();
		}
	}
}
