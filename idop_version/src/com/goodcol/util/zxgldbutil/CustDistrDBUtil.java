package com.goodcol.util.zxgldbutil;

import java.util.Map;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.util.AppUtils;

public class CustDistrDBUtil {
	
	CustClaimDBUtil claimDbUtil = new CustClaimDBUtil();
	/**
	 * 客户分配保存
	 */
	public void distrSave(Map<String, Object> map){
		
		String user_id = (String)map.get("user_id");
		String user_name = (String)map.get("user_name");
		
		String id = (String)map.get("id");
		String customercode = (String)map.get("customercode");
		String mgr_ids = (String)map.get("mgr_ids");
		String mgr_names = (String)map.get("mgr_names");
		String claim_props = (String)map.get("claim_props");
		
		String[] idArr = mgr_ids.split(",");
		String[] nameArr = mgr_names.split(",");
		String[] propArr = claim_props.split(",");
		//已经认领的客户比例
		String claimPropAll=null;
		//查询客户经理已认领比例
		String claimProp=null;
		//客户状态
		String cussts= "1";
		//此次分配的比例
		//int nowProp=0;
		if(idArr!=null&&idArr.length>0){
			//已经认领的客户比例
			claimPropAll = claimDbUtil.claimPropAll(id);
			for(int i=0;i<idArr.length;i++){
				//nowProp += Integer.parseInt(propArr[i]);
				
				int ablrProp = 0;
				int prop = Integer.parseInt(propArr[i]);
				if ((100 - Integer.parseInt(claimPropAll)) >= prop) {
					ablrProp = prop;
				} else {
					ablrProp = (100 - Integer.parseInt(claimPropAll));
				}
				
				//客户经理已认领的客户比例
				claimProp =claimDbUtil.claimProp(id, idArr[i]);
				if("0".equals(claimProp)){
					
					//查询客户来源
					String incflg = Db.use("default").queryStr(" select incflg from pccm_cust_pool where id = ? ",new Object[]{id});
					//插入认领的数据
					Db.use("default").update(" insert into pccm_cust_claim(id,cust_no,claim_prop,claim_cust_mgr_id,"
							+"claim_cust_mgr_name,claim_time,del_stat,cust_pool_id,creat_id,creat_name,incflg) values(?,?,?,?,?,to_char(sysdate,'yyyyMMdd'),'0',?,?,?,?) " ,
							new Object[] { AppUtils.getStringSeq(),customercode,propArr[i],idArr[i],nameArr[i],id,user_id,user_name,incflg});
				}else{
					//修改认领的数据
					Db.use("default").update(" update pccm_cust_claim set claim_prop=?,creat_id=?,creat_name=? where claim_cust_mgr_id=? and cust_pool_id=? and del_stat='0' " ,
							new Object[] { ablrProp+Integer.parseInt(claimProp),user_id,user_name,idArr[i],id});
				}
				if((Integer.parseInt(claimPropAll) + ablrProp) ==100){
					cussts="3";
				} else if((Integer.parseInt(claimPropAll) + ablrProp)<100
						&&(Integer.parseInt(claimPropAll)+ablrProp)>0){
					cussts="2";
				}
				//修改客户状态
				Db.use("default").update("update pccm_cust_pool set cussts=?,all_claim_time=to_char(sysdate,'yyyyMMdd') where id=? " , new Object[] { cussts,id });
			}
		}
	}
	
}
