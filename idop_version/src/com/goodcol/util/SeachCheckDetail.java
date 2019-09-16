package com.goodcol.util;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.date.BolusDate;

public class SeachCheckDetail {
   
//插入明细
	public static void getSeachCheckDetail(String flownum,String cxcfstatus){
		//插入明细
		String id=AppUtils.getStringSeq();
		String sqltr="select * from dop_search_check where check_flownum= '"+flownum+"'";
		Record records=Db.findFirst(sqltr);
		if(records !=null){
			String status=cxcfstatus;
			String check_date=BolusDate.getDate();
			String createtime=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			String check_name=records.getStr("check_name");
			String check_org=records.getStr("check_org");
			String search_date=BolusDate.getDate();			
			String search_name=records.getStr("search_name");
			String search_org=records.getStr("search_org");
			String remarks=records.getStr("check_remark");
			String back=records.getStr("remarks");
			if("4".equals(status)){
				Db.update(" insert into dop_searchCheck_detail( id,check_flownum ,action,search_date,search_name, search_org,createtime)values(?,?,?,?,?,?,?)"
						,new Object[]{id,flownum,status,search_date,search_name, search_org,createtime});//插入明系
	
			}else if("2".equals(status)||"3".equals(status)){
				Db.update(" insert into dop_searchCheck_detail( id,check_flownum ,action,search_date,search_name, search_org,remarks,createtime)values(?,?,?,?,?,?,?,?)"
						,new Object[]{id,flownum,status,search_date,search_name, search_org,back,createtime});//插入明系
			}else{
				Db.update(" insert into dop_searchCheck_detail( id,check_flownum ,check_date,action,check_name, check_org,search_date,search_name, search_org,remarks,createtime)values(?,?,?,?,?,?,?,?,?,?,?)"
						,new Object[]{id,flownum,check_date,status,check_name,check_org,search_date,search_name, search_org,remarks,createtime});//插入明系
			}
		}
	}
}
