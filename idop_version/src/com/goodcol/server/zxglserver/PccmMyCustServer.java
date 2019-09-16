package com.goodcol.server.zxglserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.zxgldbutil.PccmMyCustDBUtil;

public class PccmMyCustServer {

	PccmMyCustDBUtil dbUtil = new PccmMyCustDBUtil();
	
	/**
	 * 汇总列表查询
	 */
	public List<Record> mainList(Map<String, Object> map) {
		return dbUtil.mainList(map);
	}
	
	/**
	 * 分类明细页面
	 */
	public Page<Record> detailPage(Map<String, Object> map){
		return dbUtil.detailPage(map);
	}
	
	/**
	 * 汇总列表查询
	 */
	public List<Record> detailList(Map<String, Object> map) {
		return dbUtil.detailList(map);
	}
	
	public void transave(Map<String, Object> map){
		String claimids=(String)map.get("claimids");
		String clas=(String)map.get("clas");
		String mgrNames=(String)map.get("mgrNames");
		String mgrIds=(String)map.get("mgrIds");
		String claimProps=(String)map.get("claimProps");
		
		//String userName = (String)map.get("user_name");
		String userId = (String)map.get("user_id");
		String[] clasArr = null;
		String[] claimArr = null;
		String[] mgrNameArr = null;
		String[] mgrIdArr = null;
		String[] claimPropArr = null;
		List<Record> reList = null;
		
		Map<String, Object> para = null;
		//判断是否勾选明细，如果勾选明细，就根据明细提交
		if (AppUtils.StringUtil(claimids) != null) {
			claimArr = claimids.split(",");
			mgrNameArr = mgrNames.split(",");
			mgrIdArr = mgrIds.split(",");
			claimPropArr = claimProps.split(",");
			for(int i=0;i<claimArr.length;i++){
				Record re = dbUtil.getClaim(claimArr[i]);
				for(int j=0;j<mgrIdArr.length;j++){
					this.save(re, mgrIdArr[j], mgrNameArr[j],claimPropArr[j]);
				}
			}
		}else{
			//未勾选明细根据类别移交
//			if (AppUtils.StringUtil(clas) != null) {
//				clasArr = clas.split(",");
//				for(int i=0;i<clasArr.length;i++){
//					para = new HashMap<String, Object>();
//					para.put("clas_five", clasArr[i]);
//					para.put("userId", userId);
//					reList = dbUtil.detailList(para);
//					for(int j=0;j<reList.size();j++){
//						Record re = reList.get(i);
//						this.save(re, mgrId, mgrName);
//					}
//				}
//			}
		}
	}
	
	public void save(Record re,String mgrId,String mgrName,String toProp){
		String poolId = re.getStr("cust_pool_id");//客户号
		String cust_no = re.getStr("cust_no");//客户号
		String claim_id = re.getStr("id");//认领Id
		String claim_prop = re.getStr("claim_prop");//认领比例
		String claim_cust_mgr_id = re.getStr("claim_cust_mgr_id");//认领比例
		
		//判断选中的人是否包含自己
		if(!claim_cust_mgr_id.equals(mgrId)){
			int prop = 0;
			//客户剩余认领比例与所填的认领比例比较
			if(Integer.parseInt(claim_prop)-Integer.parseInt(toProp)>=0){
				//客户移交后剩余的比例
				prop = Integer.parseInt(claim_prop)-Integer.parseInt(toProp);
			}else{
				toProp = claim_prop;
			}
			
			
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("cust_no", cust_no);
			para.put("claim_id", claim_id);
			para.put("mgrId", mgrId);
			para.put("mgrName", mgrName);
			para.put("claim_prop", prop);
			
			//选中的客户经理原先是否认领此客户
			int to_mgr_pro = dbUtil.getProByIds(mgrId, poolId);
			para.put("to_mgr_pro", to_mgr_pro);
			para.put("poolId", poolId);
			para.put("toProp", to_mgr_pro+Integer.parseInt(toProp));
			dbUtil.transfersave(para);
		}
		
	}
	
	/**
	 * 五层分类列表查询
	 */
	public List<Record> clasFiveList(String userId) {
		return dbUtil.clasFiveList(userId);
	}
	
	/**
	 * 客户分类列表查询
	 */
	public List<Record> custClasList(String userId) {
		return dbUtil.custClasList(userId);
	}
	
	/**
	 * 我的客户详情列表
	 */
	public Page<Record> myCustPage(Map<String, Object> map){
		return dbUtil.myCustPage(map);
	}
	
	/**
	 * 产品覆盖率查询
	 */
	public Record custdetail(String pool_id) {
		return dbUtil.custdetail(pool_id);
	}
	/**
	 * 五层分类说明
	 */
	public List<Record> clasFiveText() {
		return dbUtil.clasFiveText();
	}
	/**
	 * 汇总列表查询
	 */
	public List<Record> myCustList(Map<String, Object> map) {
		return dbUtil.myCustList(map);
	}
	
	/**
	 * 查询数据最新入池时间
	 */
	public String findNewDate(String userId){
		return dbUtil.findNewDate(userId);
	}
	
	/**
	 * 用户当前组织成员下拉框数据
	 */
	public Page<Record> getOrgUser(Map<String, Object> map){
		return dbUtil.getOrgUser(map);
	}
	
	/**
	 * 查询gbase宽表
	 */
	public Record getBaseInfo(String orgNo,String custNo){
		return dbUtil.getBaseInfo(orgNo, custNo);
	}
	
	/**
	 * gbase查询产品覆盖率
	 */
	public Record custdetail(String cust_no,String orgnum) {
		return dbUtil.custdetail(cust_no, orgnum);
	}
}
