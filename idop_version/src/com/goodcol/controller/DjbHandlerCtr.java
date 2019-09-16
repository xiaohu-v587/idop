package com.goodcol.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.ajax.JSON;

import net.sf.json.JSONObject;

import com.goodcol.controller.dop.ResultPo;
import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.Constant;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.MessageSender;
import com.goodcol.util.ParamContainer;
import com.goodcol.util.YyglUtil;

import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.jfinal.anatation.RouteBind;
import com.goodcol.util.jfinal.render.excel.ExcelColumn;
import com.goodcol.util.jfinal.render.excel.ExcelRender;
import com.goodcol.util.log.Logger;
import com.goodcol.util.log.LoggerConsole;
import com.goodcol.util.zxgldbutil.JsonToMapTool;





@RouteBind(path = "/djbHandler")
@Before({ ManagerPowerInterceptor.class })
public class DjbHandlerCtr extends BaseCtl{
	/**
	 * 日志记录类.
	 */
	public LoggerConsole log = Logger.getLogger(); // 记录日志用
	
	//进入登记簿目录
	public void index(){
		String djbid = getPara("djbid");
		//根据登记簿ID,获取登记簿对应表名
		Record record = new Record();
		List<Record> re= Db.find("select ID,DJBID,DJBTAB,MENUID,CREATETIME,CREATEUSER from T_DJB_RESMENU_TAB"
				+ " where 1=1 and DJBID = '"+djbid+"'");
		if(re!=null&&re.size()>0){
			record = re.get(0);
			// 根据登记簿ID获取参数列表链表数据
			List<Record> jgpzMap = Db.find("select t.id as jgpzid,t.zdm,t.xsxs,t.kd,t.sfmhpp,t.sfxs,j.* from  t_djb_lbzs_jgpz t left join t_djb_jdsrysb j on t.djbid = j.djbid and t.zdm = j.ysm "+   
						"where 1=1 and t.DJBID = '"+record.getStr("djbid")+"' order by t.xsxs");
			// 根据登记簿ID获取查询条件链表数据
			List<Record> cxtjMap = Db.find("select t.id as cxtjid,t.bhqz,t.sffhbm,t.sfrq,t.sfmhpp,j.* from  t_djb_lbzs_cxtj t left join t_djb_jdsrysb j on t.djbid = j.djbid and t.bhqz = j.ysm "  
						+"where 1=1 and t.DJBID = '"+record.getStr("djbid")+"'  order by j.ysm");
			// 组合码表翻译数据
			Record dictMaps = new Record();
			//Map<String, Object> comMaps = new HashMap<String, Object>();
			if (jgpzMap.size() > 0) {
				for (Record map : jgpzMap) {
					if (map.getStr("ZDLX")!=null&&"1".equals(map.getStr("ZDLX"))) {
						dictMaps.set(map.getStr("YSM").toString(), map.getStr("MBLX"));
					}
				}
			}
			setAttr("jgpzdata",jgpzMap);
			setAttr("jgpztotal",total(jgpzMap));
			setAttr("cxtjdata",cxtjMap);
			setAttr("cxtjtotal",total(cxtjMap));
			setAttr("djbHandler",record);
			setAttr("dictMaps",dictMaps);
			setAttr("djbid",djbid);
		}
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿", "4", "登记簿目录");
		log.info(getLogPoint().exit("登记簿目录").toString());
		setAttr("orgname",getCurrentUser().get("org_name"));
		render("DjbHandlerindex.jsp");
	}
	
	//计算list条数
	private int total(List<Record> list) {
		return list == null ? 0 : list.size();
	}
	
	//获取登记簿目录数据
	public void getDjbmc(){
		// 获取查询条件
		// 获取当前用户信息
		String orgId = getCurrentUser().getStr("ORG_ID");
		//根据当前用户机构获取所属分行机构
    	Record orgRecord = Db.findFirst(" select t.orgnum,t.by2,t.by5  from sys_org_info t where t.orgnum = '"+orgId+"' and t.stat='1'   ");
    	String org = null;
    	String by2 = orgRecord.getStr("by2");
    	String str ="";
    	if(orgRecord != null){
    		if("0".equals(by2)){
    			org = Constant.HEAD_OFFICE;
    		}else if("1".equals(by2)){
    			org = orgRecord.getStr("orgnum");
    		}else{
    			org = orgRecord.getStr("orgnum");
    		}
    		str = orgRecord.getStr("by5");
    	}
    	String params="";
    	if(StringUtils.isNotEmpty(str)){
    		String[] strs = str.split(",");
        	for(int i=0; i<strs.length;i++){
        		params+="'"+strs[i]+"',";
        	}
        	params=params.substring(1, params.length()-2);
    	}
		String sjfw=getCurrentUser().getStr("MAX_PERMI_ORGNUM");//数据范围,不能取用户自己所在的机构，而要按照用户的数据范围、角色级别 来查， 数据范围优先

    	
		String djbmc = getPara("djbmc");//登记簿名称
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		//按照规则，上级机构可以看到下面所属机构，此时需要查询出适用机构的所有上级机构
		String by5 = Db.findFirst("select by5 from sys_org_info where id = '"+sjfw+"'").getStr("by5");
		String par = "";
		if(StringUtils.isNotEmpty(by5)){
			String[] str1 = by5.split(",");
			
			for (int i=0;i<str1.length;i++){
				par+="'"+str1[i]+"',";
			}
			par = par.substring(0,par.length()-1);
		}else{
			par = "''";
		}
		
		// 查询语句
		String selectSql = " select t.*,(select orgname from sys_org_info tt where tt.id=t.orgid) as orgname ,(select orgname from sys_org_info tt where tt.id=t.cjjg) as org_name   ";
		String extraSql = " from T_DJB_RESMENU_TAB g  left join t_djb_jcxx t on t.id = g.djbid  ";
				//and jgbh in (select id from sys_org_info where id = '"+sjfw+"'  or by5 like '%"+sjfw+"%' ) "
		StringBuffer whereSql = new StringBuffer("where t.id is not null   ");
		if(!"000000000".equals(orgId)){
			whereSql.append(" and ( t.orgid in ("+par+")  "
					+ " or  '"+orgId+"' = t.orgid or t.orgid in (select id from sys_org_info where id = '"+sjfw+"'  or by5 like '%"+sjfw+"%' )) ");
		}		
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if (AppUtils.StringUtil(djbmc) != null) {
			whereSql.append(" and DJBMC like ? ");
			sqlStr.add("%"+djbmc+"%");
		}
		// 排序
		whereSql.append(" order by CJRQ DESC ");
		extraSql += whereSql.toString();
		// 查询
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql, extraSql, sqlStr.toArray());
		// 赋值
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 打印日志s
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿目录数据", "4", "登记簿目录-获取所有数据");
		log.info(getLogPoint().exit("登记簿目录-获取所有数据").toString());
		renderJson();
		
	}
	
	
	//获取登记簿查询条件
	public void queryCxtj(){
		//查询配置的条件
		String djbid = getPara("djbid");
		List<Record> cxtj = Db.find("select t.djbid, t.bhqz, t.sffhbm, t.sfrq, t.sfmhpp,t.isstarttime,t.isendtime,p.* "
					+ " from T_DJB_LBZS_CXTJ t left join t_djb_jdsrysb p on t.djbid = p.djbid and  t.bhqz=p.ysm " 
					+" where t.djbid ='"+djbid+"'");
		setAttr("cxtj",cxtj);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "获取登记簿查询条件", "4", "获取登记簿查询条件");
		log.info(getLogPoint().exit("获取登记表查询条件").toString());
		renderJson();
	}

	
	/**
	 * 查询表头实体
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void queryHeaders() {
		String djbid = getPara("djbid");
		Map<String,Object>[] result = null;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Record> zdlist = Db.find("select t.ysm,t.yshy,t.zdlx,t.cdxz,t.zdpx,t.mblx from T_DJB_JDSRYSB t where 1=1 and t.djbid='"+djbid+"' order by t.zdpx");
		addHeader1(list);
		//拼接配置的字段作为列
		Map<String,Object> singleHeaderData = null;
		for(int i=0;i<zdlist.size();i++){
			String field = zdlist.get(i).get("ysm");
			if("1".equals(zdlist.get(i).getStr("zdlx"))){
				singleHeaderData = new HashMap<String,Object>();
				singleHeaderData.put("header",zdlist.get(i).get("yshy"));
				singleHeaderData.put("field",(field+"s").toString());
				singleHeaderData.put("headerAlign","center");
				singleHeaderData.put("align","center");
				singleHeaderData.put("allowSort",true);
			}else if("4".equals(zdlist.get(i).getStr("zdlx"))){
				singleHeaderData = new HashMap<String,Object>();
				singleHeaderData.put("header",zdlist.get(i).get("yshy"));
				singleHeaderData.put("field",(field+"s").toString());
				singleHeaderData.put("headerAlign","center");
				singleHeaderData.put("dateFormat","yyyy-MM-dd HH:mm:ss");
				singleHeaderData.put("align","center");
				singleHeaderData.put("allowSort",true);
			}else{
				singleHeaderData = new HashMap<String,Object>();
				singleHeaderData.put("header",zdlist.get(i).get("yshy"));
				singleHeaderData.put("field",zdlist.get(i).get("ysm").toString());
				singleHeaderData.put("headerAlign","center");
				singleHeaderData.put("align","center");
				singleHeaderData.put("allowSort",true);
			}
			list.add(singleHeaderData);
		}
		//addHeader2(list);
		result = list.toArray(new HashMap[list.size()]);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "获取登记簿表头数据", "4", "获取登记簿表头数据");
		log.info(getLogPoint().exit("获取登记簿表头数据").toString());
		renderJson(result);
	}
	
	/**
	 * 查询表头实体
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void queryHeaders1() {
		String djbid = getPara("djbid");
		Map<String,Object>[] result = null;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Record> zdlist = Db.find("select t.ysm,t.yshy,t.zdlx,t.cdxz,t.zdpx,t.mblx from T_DJB_JDSRYSB t where 1=1 and t.djbid='"+djbid+"' order by t.zdpx");
		String value = Db.findFirst("select t.sfpz from t_djb_jcxx t where 1=1 and t.id='"+djbid+"'").getStr("sfpz");
		

		addHeader1(list);
		addHeader3(list);//机构
		addHeader4(list);//登记时间
		addHeader8(list);//登记人
		if(!"0".equals(value)){
			addHeader2(list);//状态
			addHeader6(list);//复核人
		}
		addHeader5(list);//最后修改时间
		addHeader7(list);//修改人
		
		//拼接配置的字段作为列
		Map<String,Object> singleHeaderData = null;
		for(int i=0;i<zdlist.size();i++){
			String field = zdlist.get(i).get("ysm");
			if("1".equals(zdlist.get(i).getStr("zdlx"))){
				singleHeaderData = new HashMap<String,Object>();
				singleHeaderData.put("header",zdlist.get(i).get("yshy"));
				singleHeaderData.put("field",(field+"s").toString());
				singleHeaderData.put("headerAlign","center");
				singleHeaderData.put("align","center");
				singleHeaderData.put("allowSort",true);
			}else if("4".equals(zdlist.get(i).getStr("zdlx"))){
				singleHeaderData = new HashMap<String,Object>();
				singleHeaderData.put("header",zdlist.get(i).get("yshy"));
				singleHeaderData.put("field",(field+"s").toString());
				singleHeaderData.put("headerAlign","center");
				singleHeaderData.put("dateFormat","yyyy-MM-dd HH:mm:ss");
				singleHeaderData.put("align","center");
				singleHeaderData.put("allowSort",true);
			}else{
				singleHeaderData = new HashMap<String,Object>();
				singleHeaderData.put("header",zdlist.get(i).get("yshy"));
				singleHeaderData.put("field",zdlist.get(i).get("ysm").toString());
				singleHeaderData.put("headerAlign","center");
				singleHeaderData.put("align","center");
				singleHeaderData.put("allowSort",true);
			}
			list.add(singleHeaderData);
		}
		
		
		result = list.toArray(new HashMap[list.size()]);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "获取登记簿表头数据", "4", "获取登记簿表头数据");
		log.info(getLogPoint().exit("获取登记簿表头数据").toString());
		renderJson(result);
	}
	
	//拼接固定列
	private void addHeader1(List<Map<String, Object>> list) {
		Map<String,Object> singleHeaderData1 = new HashMap<String,Object>();
		singleHeaderData1.put("header","序号");
		singleHeaderData1.put("type","indexcolumn");
		singleHeaderData1.put("headerAlign","center");
		singleHeaderData1.put("align","center");
		singleHeaderData1.put("width","20");
		list.add(singleHeaderData1);
		Map<String,Object> singleHeaderData2 = new HashMap<String,Object>();
		singleHeaderData2.put("header","id");
		singleHeaderData2.put("field","id");
		singleHeaderData2.put("visible",false);
		singleHeaderData2.put("headerAlign","center");
		singleHeaderData2.put("align","center");
		singleHeaderData2.put("width","100");
		singleHeaderData2.put("allowSort",true);
		list.add(singleHeaderData2);
	}
	
	//拼接机构名称
		private void addHeader3(List<Map<String, Object>> list) {
			Map<String,Object> singleHeaderData2 = new HashMap<String,Object>();
			singleHeaderData2.put("header","机构名称");
			singleHeaderData2.put("field","orgname");
			singleHeaderData2.put("visible",true);
			singleHeaderData2.put("headerAlign","center");
			singleHeaderData2.put("align","center");
			singleHeaderData2.put("width","100");
			singleHeaderData2.put("allowSort",true);
			list.add(singleHeaderData2);
		}
	
	//拼接固定列
	private void addHeader2(List<Map<String, Object>> list) {
		Map<String,Object> singleHeaderData3 = new HashMap<String,Object>();
		singleHeaderData3.put("header","状态");
		singleHeaderData3.put("field","status");
		singleHeaderData3.put("visible",true);
		singleHeaderData3.put("headerAlign","center");
		singleHeaderData3.put("align","center");
		singleHeaderData3.put("width","100");
		singleHeaderData3.put("renderer","statusRender");
		singleHeaderData3.put("allowSort",true);
		list.add(singleHeaderData3);
	}
	//拼接固定列
		private void addHeader4(List<Map<String, Object>> list) {
			Map<String,Object> singleHeaderData4 = new HashMap<String,Object>();
			singleHeaderData4.put("header","登记时间");
			singleHeaderData4.put("field","cjsjs");
			singleHeaderData4.put("visible",true);
			singleHeaderData4.put("headerAlign","center");
			singleHeaderData4.put("align","center");
			singleHeaderData4.put("width","100");
			singleHeaderData4.put("dateFormat","yyyy-MM-dd HH:mm:ss");
			//singleHeaderData4.put("renderer","");
			singleHeaderData4.put("allowSort",true);
			list.add(singleHeaderData4);
		}
		//拼接固定列
				private void addHeader5(List<Map<String, Object>> list) {
					Map<String,Object> singleHeaderData5 = new HashMap<String,Object>();
					singleHeaderData5.put("header","最后修改时间");
					singleHeaderData5.put("field","updatesjs");
					singleHeaderData5.put("visible",true);
					singleHeaderData5.put("headerAlign","center");
					singleHeaderData5.put("align","center");
					singleHeaderData5.put("width","100");
					singleHeaderData5.put("dateFormat","yyyy-MM-dd HH:mm:ss");

					//singleHeaderData5.put("renderer","");
					singleHeaderData5.put("allowSort",true);
					list.add(singleHeaderData5);
				}
				//拼接固定列
				private void addHeader6(List<Map<String, Object>> list) {
					Map<String,Object> singleHeaderData6 = new HashMap<String,Object>();
					singleHeaderData6.put("header","复核人");
					singleHeaderData6.put("field","checkname");
					singleHeaderData6.put("visible",true);
					singleHeaderData6.put("headerAlign","center");
					singleHeaderData6.put("align","center");
					singleHeaderData6.put("width","100");
					//singleHeaderData6.put("renderer","");
					singleHeaderData6.put("allowSort",true);
					list.add(singleHeaderData6);
				}
				
				//拼接固定列
				private void addHeader7(List<Map<String, Object>> list) {
					Map<String,Object> singleHeaderData7 = new HashMap<String,Object>();
					singleHeaderData7.put("header","修改人");
					singleHeaderData7.put("field","updatepersonname");
					singleHeaderData7.put("visible",true);
					singleHeaderData7.put("headerAlign","center");
					singleHeaderData7.put("align","center");
					singleHeaderData7.put("width","100");
					//singleHeaderData7.put("renderer","");
					singleHeaderData7.put("allowSort",true);
					list.add(singleHeaderData7);
				}
				//拼接固定列
				private void addHeader8(List<Map<String, Object>> list) {
					Map<String,Object> singleHeaderData8 = new HashMap<String,Object>();
					singleHeaderData8.put("header","登记人");
					singleHeaderData8.put("field","registname");
					singleHeaderData8.put("visible",true);
					singleHeaderData8.put("headerAlign","center");
					singleHeaderData8.put("align","center");
					singleHeaderData8.put("width","100");
					//singleHeaderData7.put("renderer","");
					singleHeaderData8.put("allowSort",true);
					list.add(singleHeaderData8);
				}
	
	/*获取登记簿数据
	 * 1.先根据djbid获取登记簿表名
	 * 2.再根据表名来查询数据
	 */
	public void queryList(){
		// 获取查询条件
		String djbid = getPara("djbid");
		//当前登录人所在机构
		String org = getCurrentUser().getStr("org_id");
		//根据当前登录人所在机构等级获取对应数据
		Record orgRecord = Db.findFirst(" select by2,orgnum from sys_org_info  where stat = '1' and orgnum = '"+org+"'");
		if(orgRecord != null){
			if(orgRecord.getStr("by2") != null){
				String by2 = orgRecord.getStr("by2");
				if("0".equals(by2)){
					org = Constant.HEAD_OFFICE;
				}else if("1".equals(by2)){
					org = orgRecord.getStr("orgnum");
				}else{
					org = orgRecord.getStr("orgnum");
				}
			}
		}
		StringBuffer sbf = new StringBuffer("");
		String flag = getPara("flag");
		String sz = getPara("sz");
		String jgcxtj = getPara("jgcxtj");
		//拼接所选机构
		String orgarr = null;
		//所有查询条件
		String[] arr=null;
		//机构查询条件
		String[] jgarr=null;
		if(sz!=null){
			arr = sz.split(",");
		}
		if(jgcxtj != null && !"".equals(jgcxtj)){
			jgarr = jgcxtj.split(",");
			for(int i=0;i<jgarr.length;i++){
				if(!" ".equals(jgarr[i])){
					sbf.append(""+jgarr[i]+",");
				}
			}
			if(sbf.length() >0){
				orgarr = sbf.substring(0, sbf.length()-1).toString();
			}
		}
		
		String djbname = null;
		//获取查询条件字段
		List<Record> cxtj = Db.find("select t.djbid, t.bhqz, t.sffhbm, t.sfrq, t.sfmhpp,p.* "
				+ " from T_DJB_LBZS_CXTJ t left join t_djb_jdsrysb p on t.djbid = p.djbid and  t.bhqz=p.ysm " 
				+" where t.djbid ='"+djbid+"'");
		int num = total(cxtj);
		Record re = new Record();
		for(int i=0;i<num;i++){
			if("1".equals(cxtj.get(i).getStr("sffhbm"))){
				re.set("cxtj"+i, cxtj.get(i).getStr("bhqz"));
				re.set("sfmhpp"+i, cxtj.get(i).getStr("sfmhpp"));
			}else if("2".equals(cxtj.get(i).getStr("sffhbm"))){
				re.set("cxtj"+i, cxtj.get(i).getStr("bhqz"));
				re.set("sfmhpp"+i, cxtj.get(i).getStr("sfmhpp"));
			}else if("3".equals(cxtj.get(i).getStr("sffhbm"))){
				re.set("cxtj"+i, cxtj.get(i).getStr("bhqz"));
				re.set("sfmhpp"+i, cxtj.get(i).getStr("sfmhpp"));
			}else{
				re.set("cxtj"+i, cxtj.get(i).getStr("ysm"));
				re.set("sfmhpp"+i, cxtj.get(i).getStr("sfmhpp"));
			}
		}
		//获取登记簿名称
		Record formRecord = Db.findFirst("select t.djbid,t.djbtab from T_DJB_RESMENU_TAB t where t.djbid= '"+djbid+"'");
		if(formRecord != null){
			djbname = formRecord.getStr("djbtab");
		}
				
		int pageNum = 0;
		int pageSize = 0;
		String downflag = getAttr("download");
		if("down".equals(downflag)){
			pageSize = 999999;
			pageNum = 1;
		}else{
			pageNum = getParaToInt("pageIndex") + 1;
			pageSize = getParaToInt("pageSize", 10);
		}
		
		//
		List<Record> zdlxList = Db.find("select * from  t_djb_jdsrysb p where 1=1  and  p.djbid='"+djbid+"' ");
		// 查询语句List
		String forech =null;
		StringBuffer selectSql = new StringBuffer(" select t.*,(select orgname from sys_org_info where stat=1 and  orgnum = t.jgbh  ) orgname,to_date(cjsj,'yyyy-mm-dd hh24:mi:ss') as cjsjs,to_date(updatesj,'yyyy-mm-dd hh24:mi:ss') as updatesjs ");
		if(zdlxList != null){
			for(Record r1 : zdlxList){
				if("1".equals(r1.getStr("zdlx"))||"12".equals(r1.getStr("zdlx"))){
					selectSql.append(" ,(select remark from sys_param_info where key ='"+r1.getStr("mblx")+"' and status = 1 and val = t."+r1.getStr("ysm")+") "+r1.getStr("ysm")+"s");
				}else if("4".equals(r1.getStr("zdlx"))){
					selectSql.append(" ,to_date("+r1.getStr("ysm")+", 'yyyy-mm-dd hh24:mi:ss') as "+r1.getStr("ysm")+"s");
				}
			}
		}
		String extraSql = " from "+djbname;
				
		StringBuffer whereSql = new StringBuffer(" t where 1 = 1  ");
		List<String> sqlStr = new ArrayList<String>();
		Record user = getCurrentUser();
		String sjfw=getCurrentUser().getStr("MAX_PERMI_ORGNUM");//数据范围,不能取用户自己所在的机构，而要按照用户的数据范围、角色级别 来查， 数据范围优先
		String userOrg = user.getStr("MAX_PERMI_ORGNUM");
		// 查询条件
		for(int j=0;j<=num;j++){
			if(sz!=null&&re.getStr("cxtj"+j)!=null&&arr[j]!=null&&!" ".equals(arr[j])){
				//查询条件不为空
				if("jgcxtj".equals(re.getStr("cxtj"+j))){
					if(arr[j] != null){
						sbf.append("'"+arr[j]+"',");
					}
				}else if(re.getStr("sfmhpp"+j)!=null&&"1".equals(re.getStr("sfmhpp"+j))){
					whereSql.append(" and "+re.getStr("cxtj"+j)+" like '%"+arr[j]+"%' ");
				}else if("startTime".equals(re.getStr("cxtj"+j))){
					whereSql.append(" and substr(cjsj,0,10) >= ? ");
						sqlStr.add(arr[j].substring(0,10));
				}else if("endTime".equals(re.getStr("cxtj"+j))){
					whereSql.append(" and substr(cjsj,0,10) <= ? ");
					sqlStr.add(arr[j].substring(0,10));
				}else{
					String cs = arr[j];
					if(cs.contains("00:00:00")){
						String datestr = cs.substring(0, 10);
						boolean flags = DateTimeUtil.isDate(datestr);
						if(flags==true){
							whereSql.append(" and "+re.getStr("cxtj"+j)+" like '%"+datestr+"%' ");
						}
					}else{
						whereSql.append(" and "+re.getStr("cxtj"+j)+" = ? ");
						sqlStr.add(arr[j]);
					}
					
					
				}
			}
			
			
				if(!"".equals(orgarr)&&orgarr != null){
					
					whereSql.append(" and jgbh in (select id from sys_org_info where id = '"+orgarr+"'  or by5 like '%"+orgarr+"%' ) ");
				}else{
					whereSql.append(" and jgbh in (select id from sys_org_info where id = '"+sjfw+"'  or by5 like '%"+sjfw+"%' ) ");

				}
		}
		
		/*if("1".equals(flag)){
			whereSql.append(" and status = ? ");
			sqlStr.add("1");
		}*/
		String userno = getCurrentUser().getStr("USER_NO");
		
		// 排序
		extraSql += whereSql.toString();
		// 查询
		Page<Record> r = PaginateX(pageNum, pageSize, selectSql.toString(),forech, extraSql,sqlStr.toArray());
		// 赋值
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 打印日志
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(userno, "登记簿", "3", "登记簿数据-查询");
		log.info(getLogPoint().exit("登记簿数据-查询").toString());
		// 返回json数据
		renderJson();
	}
	
	

	//判断当前用户角色是否是这个登记簿对应流程的最开始角色，只有最开始角色才能申请，是返回flag=1，不是返回flag=0
	public void checkUserPower(){
		//当前操作人角色
		String nowRole = getCurrentUser().getStr("ROLEID");
		String djbid = getPara("djbid");
		//先判断是否是不需要配置流程的登记簿，如果是，直接进入，如果不是，则进行角色判断
		Record sfpzRe = Db.findFirst("select t.sfpz from t_djb_jcxx t where t.id ='"+djbid+"' ");
		if(sfpzRe != null && "1".equals(sfpzRe.getStr("sfpz"))){
			List<Record> roleList = Db.find("select t.current_operator from t_djb_lcpz t  "
					+ " where t.djbid='"+djbid+"' and t.flag = '1' and instr(t.current_operator,'"+nowRole+"')>0 ");
			if(roleList != null && roleList.size() > 0){
				setAttr("flag","1");
			}else{
				setAttr("flag","1");
			}
		}else{
			setAttr("flag","1");
		}
		renderJson();
	}
	
	public void checkUserOrg(){
		String orgid = getPara("orgid");//适用机构
		String pageType = getPara("pageType");//适用机构
		String selforgid = getCurrentUser().getStr("org_id");//当前登录人机构号
		//String sjfw=getCurrentUser().getStr("MAX_PERMI_ORGNUM");//数据范围,不能取用户自己所在的机构，而要按照用户的数据范围、角色级别 来查， 数据范围优先
		String maxid = Db.findFirst("select t.by5 from sys_org_info t where t.id = '"+selforgid+"'").getStr("by5");
		//适用机构存在于by5或者是当前登录人机构就是适用机构时
		//如果当前机构是适用机构下属机构，那么by5中包含适用机构
		
		if("edit".equals(pageType)){
			if(selforgid.equals(orgid)){
				setAttr("flag","1");
			}else{
				setAttr("flag","0");
			}
		}else{
			if((StringUtils.isNoneEmpty(maxid)&&maxid.indexOf(orgid)>-1)||(selforgid.equals(orgid))){
				setAttr("flag","1");
			}else{
				setAttr("flag","0");
			}
		}
		renderJson();
	}
	
	//进入单个登记簿申请界面，查询登记簿信息并在界面上拼接
	public void list(){
		String djbid = getPara("djbid");
		Record sfpzRe = Db.findFirst("select t.sfpz from t_djb_jcxx t where t.id ='"+djbid+"' ");
		if(sfpzRe != null){
			if("0".equals(sfpzRe.getStr("sfpz"))){
				setAttr("sfpz","0");
			}else{
				setAttr("sfpz","1");
			}
		}
		setAttr("djbid",djbid);
		render("TDjbHandlerList.jsp");
	}
	
	//进入单个登记簿查看界面，查询登记簿信息并在界面上拼接
	public void list1(){
		setAttr("djbid",getPara("djbid"));
		setAttr("orgid",getPara("orgid"));
		setAttr("useflag",getPara("useflag"));
		setAttr("sfpz",getPara("sfpz"));
		String username = getCurrentUser().getStr("name");
		if("系统管理员".equals(username)){
			setAttr("level","1");
		}else{
			setAttr("level","0");
		}
		
		render("TDjbHandlerList1.jsp");
	}
	
	//登记簿表单新增展示页
	public void addDetail(){
		String djbid = getPara("djbid");
		String bd = getPara("bd");
		String username = getCurrentUser().getStr("NAME");
		String id = AppUtils.getStringSeq();
		setAttr("username",username);
		setAttr("djbid",djbid);
		setAttr("id",id);
		setAttr("bd",bd);
		render("TDjbHandlerForm.jsp");
	}
	
	public void addDetail1(){
		String djbid = getPara("djbid");
		//获取字段,除了申请人和审批人员的字段
		List<Record> zdList = Db.find("select t.djbid,t.ysm,t.yshy,t.zdlx,t.mblx,t.cdxz,t.zdpx,t.zdsm,t.sfym from T_DJB_JDSRYSB t where t.djbid='"+djbid+"'  order by t.zdpx ");
		setAttr("zdlist",zdList);
		setAttr("zdlisttotal",total(zdList));
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "获取所有字段", "3", "获取所有字段");
		log.info(getLogPoint().exit("获取所有字段").toString());
		renderJson();
	}
	
	//登记簿表单编辑展示页
	public void editDetail(){
		String djbid = getPara("djbid");
		String id = getPara("id");
		String pageType = getPara("pageType");
		String status = getPara("status");
		Record fjidRe = Db.findFirst(" select fjid from t_djb_apply where sjid = '"+id+"' and status = 2 ");
		if(fjidRe != null){
			String fjid = fjidRe.getStr("fjid");
			setAttr("fjid",fjid);
		}
		String username = getCurrentUser().getStr("NAME");
		setAttr("username",username);
		
		setAttr("djbid",djbid);
		setAttr("id",id);
		setAttr("pageType",pageType);
		setAttr("status",status);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿表单编辑展示页", "3", "登记簿表单编辑展示页");
		log.info(getLogPoint().exit("登记簿表单编辑展示页").toString());
		render("editDetail.jsp");
	}
	
	//登记簿表单编辑展示页
	public void editDetail1(){
		String djbid = getPara("djbid");
		String id = getPara("id");
		String pageType = getPara("pageType");
		setAttr("djbid",djbid);
		setAttr("id",id);
		setAttr("pageType",pageType);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿表单编辑展示页", "3", "登记簿表单编辑展示页");
		log.info(getLogPoint().exit("登记簿表单编辑展示页").toString());
		render("editDetail1.jsp");
	}
	
	public void approves(){
		String djbid = getPara("djbid");
		String id = getPara("id");
		setAttr("djbid",djbid);
		setAttr("id",id);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿表单提交展示页", "3", "登记簿表单提交展示页");
		log.info(getLogPoint().exit("登记簿表单提交展示页").toString());
		render("approve.jsp");
	}
	
	public void getApprover(){
		List<Record> lr = YyglUtil.getUserListByRoleForDjbApply(getCurrentUser().getStr("ROLEID"), getCurrentUser().getStr("ORGNUM"),getCurrentUser().getStr("ROLE_LEVEL"),getPara("djbid"));
		setAttr("nextAppData", lr);
		renderJson();
	}
	
	/**
	 * 编辑页面反显数据
	 * @return
	 */
	public void getDatileData(){
		//登记簿id
		String djbid = getPara("djbid");
		//信息id
		String id = getPara("id");
		//获取登记簿名称
		String djbname = null;
		Record formRecord = Db.findFirst("select t.djbid,t.djbtab from T_DJB_RESMENU_TAB t where t.djbid= '"+djbid+"'");
		if(formRecord != null){
			djbname = formRecord.getStr("djbtab");
		}
		Record data = new Record();
		if(djbname != null){
			data = Db.findFirst(" select t.*,(select results from t_djb_apply where sjid = t.id and status = 2) as results  from "+djbname + " t where t.id = '"+id+"' ");
		}else{
			data = null;
		}
		setAttr("data",data);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "编辑界面反显数据", "3", "编辑界面反显数据");
		log.info(getLogPoint().exit("编辑界面反显数据").toString());
		renderJson();
	}
	
	
	/**
	 * 新增业务数据保存方法.
	 * @param vo 业务数据
	 */
	public void save() {
		log.info("save");
		//当前操作人用户号
		String userNo = getCurrentUser().getStr("USER_NO");
		String userName = getCurrentUser().getStr("NAME");
		String jStr = getPara("data");
		String bd = getPara("bd");
		jStr = jStr.substring(1, jStr.length()-1);
		Map<String, Object> map = JsonToMapTool.convertJsonToMap(jStr);
		String djbid = map.get("djbid").toString();
		String id = map.get("id").toString();
		String org = getCurrentUser().getStr("org_id");
		//获取登记簿名称
		String djbname = null;
		Record formRecord = Db.findFirst("select t.djbid,t.djbtab from T_DJB_RESMENU_TAB t where t.djbid= '"+djbid+"'");
		if(formRecord != null){
			djbname = formRecord.getStr("djbtab");
		}
		List<Record> zdList = Db.find("select t.djbid,t.ysm,t.yshy,t.zdlx,t.mblx,t.cdxz,t.zdpx,t.zdsm from T_DJB_JDSRYSB t where t.djbid='"+djbid+"'");
		Record jcxxRecord = Db.findFirst("select t.SFPZ from t_djb_jcxx t where t.id= '"+djbid+"'");
		String sfpz = "";
		if(jcxxRecord != null){
			sfpz = jcxxRecord.getStr("sfpz");
		}
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Record r_log = new Record();
		r_log.set("id", id);
		r_log.set("jgbh",org);
		r_log.set("cjr",userNo);
		r_log.set("status",2);

		for(Record r : zdList){
			r_log.set(r.getStr("ysm"), map.get(r.getStr("ysm")).toString());
		}
		String pageType = getPara("pageType");
		if(pageType != null &&  ("edit").equals(pageType)){
			r_log.set("updatesj", sdf.format(new Date()));
			r_log.set("updatepersonid", userNo);
			r_log.set("updatepersonname", userName);
			Record re = Db.findFirst("select * from "+ djbname +" where id = '"+id+"'");
			if(StringUtils.isNotEmpty(re.getStr("cjsj"))&&(re.getStr("cjsj").substring(0, 10).equals(sdf.format(new Date()).substring(0,10)))){
				Db.update(djbname, r_log);
				//Db.save(djbname, r_log);
			}else{
				r_log.set("cjsj", re.getStr("cjsj"));
				r_log.set("registid",userNo);
				r_log.set("registname",userName);
				String ids = AppUtils.getStringSeq();
				r_log.set("id", ids);
				setAttr("id",ids);
				Db.save(djbname, r_log);
			}
		}else{
			if(StringUtils.isNoneEmpty(bd)){
				r_log.set("cjsj", bd);
			}else{
				r_log.set("cjsj", sdf.format(new Date()));
			}
			r_log.set("registid",userNo);
			r_log.set("registname",userName);
			Db.save(djbname, r_log);
			
		}
		if("1".equals(sfpz)){
			setAttr("sfpz",sfpz);
			saveApproves();
		}
		Db.update(" update t_djb_jcxx set flag = 1 where id='"+djbid+"'");
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿—保存", "4", "登记簿-保存");
		log.info(getLogPoint().exit("登记簿-数据保存").toString());
		renderJson();
	}
	
	
	/**
	 * 
	 * @param vo 新增时直接提交业务数据
	 */
	public void saveApproves() {
		log.info("save");
		JSONObject json = getJsonObj("data");
		String djbid = json.getString("djbid");
		String sfpz = getAttr("sfpz");
		String id ="";
		if("1".equals(sfpz)){
			id = getAttr("id");
		}else{
			id = json.getString("id");
		}
		if(StringUtils.isEmpty(id)){
			id = json.getString("id");
		}
		//当前操作人用户号
		String userNo = getCurrentUser().getStr("USER_NO");
		//当前操作人姓名
		Record userRecord = Db.findFirst(" select * from sys_user_info t where t.stat=0 and (t.dele_flag = 0 or t.dele_flag  is null) and t.user_no='"+userNo+"' ");
		
		String username = userRecord.getStr("name");
		//当前操作人角色
		String nowRole = getCurrentUser().getStr("ROLEID");
		//下一审批人
		//String nextApprover = json.getString("nextApprover");
		//下一审批人信息
		//Record re = Db.findFirst(" select name,role_id,mobile  from sys_user_info where stat = 0 and dele_flag = 0 and user_no = '"+nextApprover+"' ");
		//下一审批人角色对应字段
		List<Record> zdmcRe = Db.find(" select zdmc from t_djb_lcpz t where t.djbid = '"+djbid+"'  and instr(t.current_operator,'"+nowRole+"')>0  ");
		String zdmc = "";
		if(zdmcRe != null && zdmcRe.size() > 0){
			zdmc = zdmcRe.get(0).getStr("zdmc");
		}
		//状态  0.已提交  1.通过
		String status = "0";
		String org = getCurrentUser().getStr("org_id");
		//获取登记簿名称
		String djbname = null;
		Record formRecord = Db.findFirst("select t.djbid,t.djbtab from T_DJB_RESMENU_TAB t where t.djbid= '"+djbid+"'");
		if(formRecord != null){
			djbname = formRecord.getStr("djbtab");
		}
		List<Record> zdList = Db.find("select t.djbid,t.ysm,t.yshy,t.zdlx,t.mblx,t.cdxz,t.zdpx,t.zdsm from T_DJB_JDSRYSB t where t.djbid='"+djbid+"'");
		
		//登记簿信息保存
		Record r_log = new Record();
		r_log.set("id", id);
		r_log.set("status",status);
		for(Record r : zdList){
			r_log.set(r.getStr("ysm"), json.getString(r.getStr("ysm")));
		}
		Db.update(djbname, r_log);
		
		
		Record djbnames = Db.findFirst(" select djbmc from  t_djb_jcxx where id='"+djbid+"'");
		String name = "";
		if(djbnames != null){
			name = djbnames.getStr("djbmc");
		}
		//流程流水保存
		Db.update("delete from t_djb_apply t where t.sjid = '"+id+"'");
		Db.update("insert into t_djb_apply(id,djbid,sjid,ordernum,approver,nowrole,orgnum,status,apptime,nextapprover,nextapprovername,nextzdmc) values "
				+ "(?,?,?,?,?,?,?,?,?,?,?,?) ", new Object[]{AppUtils.getStringSeq(),djbid,id,1,userNo,nowRole,org,1,DateTimeUtil.getTime(),"","",zdmc});
		Db.update(" delete from t_djb_apply where sjid = '"+id+"' and status = 2 ");
		//将申请人信息存入
		Record sqrRecord = Db.findFirst("select t.ysm,t.yshy from t_djb_jdsrysb t where t.zdlx='7' and t.djbid = '"+djbid+"'");
		
		/*if(re != null && re.getStr("mobile") != null){
			MessageSender.sendMessage(re.getStr("mobile").trim(), YyglUtil.APPLY_MESSAGE_13+"(登记簿名称："+name+")");
		}*/
		if(sqrRecord != null){
			//登记簿信息保存
			Record r_logs = new Record();
			r_logs.set("id", id);
			//r_logs.set(sqrRecord.getStr("ysm"),username);
			Db.update(djbname, r_logs);
		}
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿流水—保存", "4", "登记簿-保存");
		log.info(getLogPoint().exit("登记簿流水-数据保存").toString());
		renderJson();
	}
	/**
	 * 删除选中的数据.
	 * @param ids 选中的数据列表，逗号分隔
	 */
	public void delete() {
		String id = getPara("ids");
		String djbid = getPara("djbid");
		//获取登记簿名称
		String djbname = null;
		String flag = "0";
		Record formRecord = Db.findFirst("select t.djbid,t.djbtab from T_DJB_RESMENU_TAB t where t.djbid= '"+djbid+"'");
		if(formRecord != null){
			djbname = formRecord.getStr("djbtab");
		}
		String[] arr = id.split(",");
		for(String ar : arr){
			int count = Db.update(" delete from "+djbname + " where id = '"+ar+"'");
			if(count >0){
				flag = "1";
			}
		}
		setAttr("flag",flag);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿—删除", "6", "登记簿-删除");
		log.info(getLogPoint().exit("登记簿-数据删除").toString());
		renderJson();
	}
	
	//未配置流程的登记簿确认数据
	public void queRen(){
		//当前操作人用户号
		String userNo = getCurrentUser().getStr("USER_NO");
		//当前操作人姓名
		Record userRecord = Db.findFirst(" select * from sys_user_info t where t.stat=0 and t.dele_flag = 0 and t.user_no='"+userNo+"' ");
		String username = userRecord.getStr("name");
		String id = getPara("id");
		String djbid = getPara("djbid");
		//获取登记簿名称
		String djbname = null;
		Record formRecord = Db.findFirst("select t.djbid,t.djbtab from T_DJB_RESMENU_TAB t where t.djbid= '"+djbid+"'");
		if(formRecord != null){
			djbname = formRecord.getStr("djbtab");
		}
		int flag = 0;
		flag = Db.update(" update "+djbname+" set status='1' where id = '"+id+"' ");
		//将申请人信息存入
		Record sqrRecord = Db.findFirst("select t.ysm,t.yshy from t_djb_jdsrysb t where t.zdlx='7' and t.djbid = '"+djbid+"'");
				
		if(sqrRecord != null){
			//登记簿信息保存
			Record r_logs = new Record();
			r_logs.set("id", id);
			r_logs.set(sqrRecord.getStr("ysm"),username);
			Db.update(djbname, r_logs);
		}
		setAttr("flag",flag);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿—数据确认", "4", "登记簿-数据确认");
		log.info(getLogPoint().exit("登记簿-数据确认").toString());
		renderJson();
	}
	
	
	//导出
	public void download(){
		setAttr("download","down");
		queryList();
		/*// 获取查询条件
		String djbid = getPara("djbid");
		//当前登录人所在机构
		String org = getCurrentUser().getStr("org_id");
		//根据当前登录人所在机构等级获取对应数据
		Record orgRecord = Db.findFirst(" select by2,orgnum from sys_org_info  where stat = '1' and orgnum = '"+org+"'");
		if(orgRecord != null){
			if(orgRecord.getStr("by2") != null){
				String by2 = orgRecord.getStr("by2");
				if("0".equals(by2)){
					org = Constant.HEAD_OFFICE;
				}else if("1".equals(by2)){
					org = orgRecord.getStr("orgnum");
				}
			}
		}
		StringBuffer sbf = new StringBuffer("");
		String flag = getPara("flag");
		String sz = getPara("sz");
		String jgcxtj = getPara("jgcxtj");
		//拼接所选机构
		String orgarr = null;
		//所有查询条件
		String[] arr=null;
		//机构查询条件
		String[] jgarr=null;
		if(sz!=null){
			arr = sz.split(",");
		}
		if(jgcxtj != null && !"".equals(jgcxtj)){
			jgarr = jgcxtj.split(",");
			for(int i=0;i<jgarr.length;i++){
				if(!" ".equals(jgarr[i])){
					sbf.append("'"+jgarr[i]+"',");
				}
			}
			if(sbf.length() >0){
				orgarr = sbf.substring(0, sbf.length()-1).toString();
			}
		}
		
		String djbname = null;
		//获取查询条件字段
		List<Record> cxtj = Db.find("select t.djbid, t.bhqz, t.sffhbm, t.sfrq, t.sfmhpp,p.* "
				+ " from T_DJB_LBZS_CXTJ t left join t_djb_jdsrysb p on t.djbid = p.djbid and  t.bhqz=p.ysm " 
				+" where t.djbid ='"+djbid+"'");
		int num = total(cxtj);
		Record re = new Record();
		for(int i=0;i<num;i++){
			if("1".equals(cxtj.get(i).getStr("sffhbm"))){
				re.set("cxtj"+i, cxtj.get(i).getStr("bhqz"));
				re.set("sfmhpp"+i, cxtj.get(i).getStr("sfmhpp"));
			}else{
				re.set("cxtj"+i, cxtj.get(i).getStr("ysm"));
				re.set("sfmhpp"+i, cxtj.get(i).getStr("sfmhpp"));
			}
		}
		//获取登记簿名称
		Record formRecord = Db.findFirst("select t.djbid,t.djbtab from T_DJB_RESMENU_TAB t where t.djbid= '"+djbid+"'");
		if(formRecord != null){
			djbname = formRecord.getStr("djbtab");
		}
				
		//
		List<Record> zdlxList = Db.find("select * from  t_djb_jdsrysb p where 1=1  and  p.djbid='"+djbid+"' ");
		// 查询语句List
		StringBuffer selectSql = new StringBuffer(" select t.*,(select orgname from sys_org_info where stat=1 and  orgnum = t.jgbh  ) orgname ");
		if(zdlxList != null){
			for(Record r1 : zdlxList){
				if("1".equals(r1.getStr("zdlx"))){
					selectSql.append(" ,(select remark from sys_param_info where key ='"+r1.getStr("mblx")+"' and status = 1 and val = t."+r1.getStr("ysm")+") "+r1.getStr("ysm")+"s");
				}else if("4".equals(r1.getStr("zdlx"))){
					selectSql.append(" ,to_date("+r1.getStr("ysm")+", 'yyyy-mm-dd hh24:mi:ss') as "+r1.getStr("ysm")+"s");
				}
			}
		}
		String extraSql = " from "+djbname;
				
		StringBuffer whereSql = new StringBuffer(" t where 1 = 1  ");
		List<String> sqlStr = new ArrayList<String>();
		
		int jg = 0;
		// 查询条件
		
		for(int j=0;j<=num;j++){
			if(sz!=null&&re.getStr("CXTJ"+j)!=null&&arr[j]!=null&&!" ".equals(arr[j])){
				//查询条件不为空
				if("jgcxtj".equals(re.getStr("CXTJ"+j))){
					if(arr[j] != null){
						sbf.append("'"+arr[j]+"',");
					}
				}else if(re.getStr("SFMHPP"+j)!=null&&"1".equals(re.getStr("SFMHPP"+j))){
					whereSql.append(" and "+re.getStr("CXTJ"+j)+" like '%"+arr[j]+"%' ");
				}else{
					whereSql.append(" and "+re.getStr("CXTJ"+j)+" = ? ");
					sqlStr.add(arr[j]);
				}
			}
			if(j == num-1){
				if(!"".equals(orgarr)&&orgarr != null){
					whereSql.append(" and jgbh in ("+orgarr+") ");
					jg = 1;
				}
			}
		}
		
		if("1".equals(flag)){
//			whereSql.append(" and status = ? ");
//			sqlStr.add("1");
		}
		
		Record djbnames = Db.findFirst(" select djbmc from  t_djb_jcxx where id='"+djbid+"'");
		String name = "";
		if(djbnames != null){
			name = djbnames.getStr("djbmc");
		}
		
		//根据机构号来查询，默认查询当前登录人所管辖的机构
		if(jg == 0){
			whereSql.append(" and jgbh in ( select orgnum  from sys_org_info where stat = 1 start with orgnum = '"+org+"' connect by prior  orgnum = upid ) ");
		}
		// 排序
		extraSql += whereSql.toString();
		// 查询
		List<Record> r = Db.find(selectSql.toString()+extraSql,sqlStr.toArray());*/
		String djbid = getPara("djbid");
		Record djbnames = Db.findFirst(" select djbmc from  t_djb_jcxx where id='"+djbid+"'");
		String name = "";
		if(djbnames != null){
			name = djbnames.getStr("djbmc");
		}
		List<Record> r=getAttr("data");
		List<Record> zdlist = Db.find("select t.ysm,t.yshy,t.zdlx,t.cdxz,t.zdpx,t.mblx from T_DJB_JDSRYSB t where 1=1 and t.djbid='"+djbid+"' order by t.zdpx");
		List<ExcelColumn> column=new ArrayList<ExcelColumn>();
		ExcelColumn ec =new ExcelColumn();
		ec.setTitle(name);
		ec.setField("");
		ec.setWidth((zdlist.size()+1)*4000);
		List<ExcelColumn> child=new ArrayList<ExcelColumn>();
		for(int i=0;i<zdlist.size();i++){
			ExcelColumn e =new ExcelColumn();
			e.setTitle(zdlist.get(i).getStr("yshy"));
			e.setWidth(4000);
			if("1".equals(zdlist.get(i).getStr("zdlx"))){
				e.setField((zdlist.get(i).getStr("ysm")+"s"));
			}else{
				e.setField(zdlist.get(i).getStr("ysm"));
			}
			child.add(e);
		}
		ExcelColumn e1 =new ExcelColumn();
		e1.setTitle("机构名称");
		e1.setField("ORGNAME");
		e1.setWidth(4000);
		child.add(e1);
		ec.setChildren(child);
		column.add(ec);
		String fileName = "";
		try {
		    fileName = new String(("登记簿"+System.currentTimeMillis()+".xls").getBytes("GB2312"),"ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}
		ExcelRender er =new ExcelRender(fileName, column, r, getResponse());
		//er = new ExcelRenderx(fileName, column, headers, list, getResponse());
		//er.render(); 
		er.renderMergre();
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿数据".toString(), "7","登记簿数据-下载");
		log.info(getLogPoint().exit("登记簿数据-下载列表").toString());
		renderNull();

	}
	
	
	public void download2(){
		setAttr("download","down");
		queryList();
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = getPara("execlcolumns").split(",");
		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = getPara("execlheaders").split(",");
		String fileName = "";
		try {
			fileName = new String((getPara("execlfilename")+System.currentTimeMillis()+".xls").getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}
		List<Record> list = getAttr("data");
		// 转换成excel
		ExcelRenderx er = new ExcelRenderx(fileName, columns, headers, list, getResponse());
		System.out.println("111111");
		er.render(); 
		// 打印日志   
		log.info("download--list:" + list);
		renderNull();
	}

	
	/**
	 * 根据当前登录用户的角色获取对应维护的审批角色信息
	 */
	public void getNextOperatorList() {
		List<Record> lr = YyglUtil.getUserListByRoleForDjbApply(getCurrentUser().getStr("ROLEID"), getCurrentUser().getStr("ORGNUM"),getCurrentUser().getStr("ROLE_LEVEL"),getPara("djbid"));
		setAttr("nextAppData", lr);
		renderJson();
	}
	
	/**
	 * 
	 * @param vo 业务数据
	 */
	public void saveApprove() {
		log.info("save");
		JSONObject json = getJsonObj("data");
		String djbid = json.getString("djbid");
		String id = json.getString("id");
		//当前操作人用户号
		String userNo = getCurrentUser().getStr("USER_NO");
		//当前操作人姓名
		Record userRecord = Db.findFirst(" select * from sys_user_info t where t.stat=0 and (t.dele_flag = 0 or t.dele_flag  is null) and t.user_no='"+userNo+"' ");
		
		String username = userRecord.getStr("name");
		//当前操作人角色
		String nowRole = getCurrentUser().getStr("ROLEID");
		//下一审批人
		String nextApprover = json.getString("nextApprover");
		//下一审批人信息
		Record re = Db.findFirst(" select name,role_id,mobile  from sys_user_info where stat = 0 and dele_flag = 0 and user_no = '"+nextApprover+"' ");
		//下一审批人角色对应字段
		List<Record> zdmcRe = Db.find(" select zdmc from t_djb_lcpz t where t.djbid = '"+djbid+"'  and instr(t.current_operator,'"+nowRole+"')>0  ");
		String zdmc = "";
		if(zdmcRe != null && zdmcRe.size() > 0){
			zdmc = zdmcRe.get(0).getStr("zdmc");
		}
		//状态  0.已提交  1.通过
		String status = "0";
		String org = getCurrentUser().getStr("org_id");
		//获取登记簿名称
		String djbname = null;
		Record formRecord = Db.findFirst("select t.djbid,t.djbtab from T_DJB_RESMENU_TAB t where t.djbid= '"+djbid+"'");
		if(formRecord != null){
			djbname = formRecord.getStr("djbtab");
		}
		List<Record> zdList = Db.find("select t.djbid,t.ysm,t.yshy,t.zdlx,t.mblx,t.cdxz,t.zdpx,t.zdsm from T_DJB_JDSRYSB t where t.djbid='"+djbid+"'");
		
		//登记簿信息保存
		Record r_log = new Record();
		r_log.set("id", id);
		r_log.set("status",status);
		for(Record r : zdList){
			r_log.set(r.getStr("ysm"), json.getString(r.getStr("ysm")));
		}
		Db.update(djbname, r_log);
		
		
		Record djbnames = Db.findFirst(" select djbmc from  t_djb_jcxx where id='"+djbid+"'");
		String name = "";
		if(djbnames != null){
			name = djbnames.getStr("djbmc");
		}
		//流程流水保存
		
		Db.update("insert into t_djb_apply(id,djbid,sjid,ordernum,approver,nowrole,orgnum,status,apptime,nextapprover,nextapprovername,nextzdmc) values "
				+ "(?,?,?,?,?,?,?,?,?,?,?,?) ", new Object[]{AppUtils.getStringSeq(),djbid,id,1,userNo,nowRole,org,1,DateTimeUtil.getTime(),"","",zdmc});
		Db.update(" delete from t_djb_apply where sjid = '"+id+"' and status = 2 ");
		//将申请人信息存入
		Record sqrRecord = Db.findFirst("select t.ysm,t.yshy from t_djb_jdsrysb t where t.zdlx='7' and t.djbid = '"+djbid+"'");
		
		if(re != null && re.getStr("mobile") != null){
			MessageSender.sendMessage(re.getStr("mobile").trim(), YyglUtil.APPLY_MESSAGE_13+"(登记簿名称："+name+")");
		}
		if(sqrRecord != null){
			//登记簿信息保存
			Record r_logs = new Record();
			r_logs.set("id", id);
			r_logs.set(sqrRecord.getStr("ysm"),username);
			Db.update(djbname, r_logs);
		}
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿流水—保存", "4", "登记簿-保存");
		log.info(getLogPoint().exit("登记簿流水-数据保存").toString());
		renderJson();
	}
	
	public void lookup(){
		String sjid = getPara("sjid");
		setAttr("sjid",sjid);
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "登记簿表单提交展示页", "3", "登记簿表单提交展示页");
		log.info(getLogPoint().exit("登记簿表单提交展示页").toString());
		render("djbApprove.jsp");
	}
	
	public void getApproverjl(){
		String sjid = getPara("sjid");
	    
		StringBuffer whereSql = new StringBuffer();
		List<String> sqlStr = new ArrayList<String>();
		
		String selectSql = " select t.id,(select name from sys_user_info  where stat = 0 and user_no = t.approver ) approver,"
				+ " t.ordernum,(select orgname from sys_org_info where stat =1 and orgnum = t.orgnum ) orgnum,t.apptime,"
				+ "(select remark from sys_param_info where key = '537' and status ='1' and val =t.status ) status,p.name  ";
		
		String fromSql = " from t_djb_apply t left join sys_role_info p  on t.nowrole = p.id where t.sjid = '"+sjid+"' ";
		
		whereSql.append(" order by to_number(t.ordernum) ");
		
		fromSql += whereSql.toString();
		
		// 获取分页的索引和每页显示的条数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
				
		// 从数据库查询指定条数的用户记录
		Page<Record> r = Db.paginate(pageNum,pageSize,selectSql,fromSql,sqlStr.toArray() );
		
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("datas", r.getList()); // 此处的datas对应index.html中datagrid1的dataField属性值
		setAttr("total", r.getTotalRow());
		
		/**
		 * 记录操作日志
		 */
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "员工考核打分", "3", "审批流程-查询");
		// 返回json数据
		renderJson();
		
	}
	
	
	public void testValidate(){
        RPCServiceClient serviceClient=null;
		try {
			serviceClient = new RPCServiceClient();
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        Options options = serviceClient.getOptions();
        //ResourceBundle resourceBundle = ResourceBundle.getBundle("config/env/app");
//        String endPoint="http://22.200.129.201:9080/ehr/services/password?wsdl";
        String endPoint=ParamContainer.getDictName("validatePassword", "validatePassword");
        EndpointReference targetEPR = new EndpointReference(endPoint);
        options.setTo(targetEPR);
        
        String pwd = getPara("sfym");
        String user = getPara("ysm");
//        String str = encodePwd(pwd);//此处注释，以前是传输密文，现在传输明斿
//        QName qname = new QName("http://pwd.ws.neusoft.com", "validate");
        QName qname = new QName(ParamContainer.getDictName("ws", "ws"), "validate");
        Object[] inputArgs = new Object[] { user, pwd };
        Class[] returnTypes = new Class[] {ResultPo.class};
        Object[] response=null;
        try {
			response = serviceClient.invokeBlocking(qname, inputArgs, returnTypes);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        ResultPo result = (ResultPo) response[0];
        //String result=null;
        // display results
        if (result == null) {
            System.out.println("PasswordService didn't initialize!");
        } else {
            if (result.isSuccess()) {
                System.out.println("密码验证通过＿ + result.isSuccess()");
                setAttr("flag",true);
            }
            else {
                System.out.println("错误＿ + result.getMsg()");
                setAttr("flag",false);
            }
        }
        renderJson();
    }
	
}
