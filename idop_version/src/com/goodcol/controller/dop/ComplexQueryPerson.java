package com.goodcol.controller.dop;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.goodcol.controller.BaseCtl;
import com.goodcol.controller.ParamCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * 重点报表展示
 * @author 张强强
 * @date 2018-11-19
 *
 */
@RouteBind( path = "/complexQueryPerson")
@Before({ManagerPowerInterceptor.class})
public class ComplexQueryPerson extends BaseCtl{

	// 记录日志用
	public static Logger log = Logger.getLogger(ComplexQueryPerson.class); 
	@Override
	//@Before(PermissionInterceptor.class)
	public void index() {
		// TODO Auto-generated method stub
		String key = getPara("key");
		String style = getPara("style");
		String  orgid = getCurrentUser().getStr("MAX_PERMI_ORGNUM");

		if(AppUtils.StringUtil(key)!= null){
			if(AppUtils.StringUtil(style)!= null && "0".equals(style)){
				orgid = key;
			}
		}
		setAttr("org", orgid);
		render("complexQuery_person.jsp");
	}




	/**
	 * 查询
	 */
	public void getWarnNameList(){
		String warnType = getPara("val");
		String index=getPara("index");
		//warning_code like 'GY902%'
		String sql="";
		if("2".equals(index) || StringUtils.isEmpty(index)){
			sql="select t.warning_name,t.warning_code from dop_warning_param  t where t.warning_type_code=(select val from dop_gangedmenu_info t where t.id = "+"'"+warnType+"') and (t.is_use is  null or t.is_use = '1') and t.WARNING_DIMENSION='1' order by t.warning_name asc";
		}else{
			sql="select t.warning_name,t.warning_code from dop_warning_param  t where t.warning_type_code=(select val from dop_gangedmenu_info t where t.id = "+"'"+warnType+"') " +
					"and (t.is_use is  null or t.is_use = '1') and t.warning_code like '%902%' and t.WARNING_DIMENSION='1' order by t.warning_name asc";
		}

		List<Record> list=Db.find(sql);
		setAttr("data",list);
		renderJson();
	}

	/**
	 * 查询判断所选择机构是否是同级别
	 * 
	 */
	public void judgeCondition() {
		// 获取查询条件
		String orgid = getPara("orgid");

		String[] strs = orgid.split(",");
		Set<String> set = new HashSet<String>();
		for(String str:strs){
			// 查询语句
			Record r= Db.findFirst("select t.by2 from sys_org_info t where t.id="+"'"+str+"'");
			set.add(r.getStr("by2"));
		}
		if(set.size()>1){
			// 赋值
			setAttr("data", false);
		}else{
			setAttr("data", true);
		}
		// 返回json数据
		renderJson();
	}
	/**
	 * 生成前台header
	 */
	public void queryHeader(){
		List<Record> headers = new ArrayList<>();
		String orgid = getPara("orgid");
		String userid = getPara("userid");
		String compareValue = getPara("comparevalue");
		String yjstr = getPara("yjstr");
		String zbstr = getPara("zbstr");
		String zhstr = "";
		if(StringUtils.isNotEmpty(yjstr)&&StringUtils.isNotEmpty(zbstr)){
			zhstr = yjstr+","+zbstr;
		}else if(StringUtils.isNotEmpty(yjstr)&&StringUtils.isEmpty(zbstr)){
			zhstr = yjstr;
		}else{
			zhstr = zbstr;
		}
		Map<String,Object>[] result = null;
		Map<String,Object> singleheader = new HashMap<String,Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		//此时为多人员
		List<Record> rsList = new ArrayList<Record>();
		String[] strs = zhstr.split(",");
		String[] orgarr = orgid.split(",");
		singleheader.put("header", "机构名称");
		singleheader.put("field", "deptno");
		singleheader.put("visible", "true");
		singleheader.put("headerAlign", "center");
		singleheader.put("align", "center");
		singleheader.put("width", "100");
		list.add(singleheader);
		Map<String,Object> singleheader3 = new HashMap<String,Object>();
		String[] userarr = userid.split(",");
		singleheader.put("header", "人员名称");
		singleheader.put("field", "userno");
		singleheader.put("visible", "true");
		singleheader.put("headerAlign", "center");
		singleheader.put("align", "center");
		singleheader.put("width", "100");
		list.add(singleheader3);
		Map<String,Object> singleheader1 = new HashMap<String,Object>();
		singleheader1.put("header", "开始时间");
		singleheader1.put("field", "begindate");
		singleheader1.put("visible", "true");
		singleheader1.put("headerAlign", "center");
		singleheader1.put("align", "center");
		singleheader1.put("width", "100");
		list.add(singleheader1);
		Map<String,Object> singleheader2 = new HashMap<String,Object>();
		singleheader2.put("header", "结束时间");
		singleheader2.put("field", "enddate");
		singleheader2.put("visible", "true");
		singleheader2.put("headerAlign", "center");
		singleheader2.put("align", "center");
		singleheader2.put("width", "100");
		list.add(singleheader2);
		for(int j=0;j<strs.length;j++){
			//根据前台传回来的组合查询参数中指标和预警的名称拼出加载列
			Map<String,Object> singheader = new HashMap<String,Object>();
			singheader.put("header", "名称");
			singheader.put("field", "indexname"+"_"+j);
			singheader.put("visible", "true");
			singheader.put("headerAlign", "center");
			singheader.put("align", "center");
			singheader.put("width", "100");
			list.add(singheader);
			Map<String,Object> singheader1 = new HashMap<String,Object>();
			singheader1.put("header", "基准值");
			singheader1.put("field", "value0"+"_"+j);
			singheader1.put("visible", "true");
			singheader1.put("headerAlign", "center");
			singheader1.put("align", "center");
			singheader1.put("width", "100");
			list.add(singheader1);
			if(compareValue.contains("1")){
				Map<String,Object> singheader2 = new HashMap<String,Object>();
				singheader2.put("header", "上日");
				singheader2.put("field", "value1"+"_"+j);
				singheader2.put("visible", "true");
				singheader2.put("headerAlign", "center");
				singheader2.put("align", "center");
				singheader2.put("width", "100");
				list.add(singheader2);
			}
			if(compareValue.contains("2")){
				Map<String,Object> singheader2 = new HashMap<String,Object>();
				singheader2.put("header", "上月末");
				singheader2.put("field", "value2"+"_"+j);
				singheader2.put("visible", "true");
				singheader2.put("headerAlign", "center");
				singheader2.put("align", "center");
				singheader2.put("width", "100");
				list.add(singheader2);
			}
			if(compareValue.contains("3")){
				Map<String,Object> singheader3 = new HashMap<String,Object>();
				singheader3.put("header", "上年末");
				singheader3.put("field", "value3"+"_"+j);
				singheader3.put("visible", "true");
				singheader3.put("headerAlign", "center");
				singheader3.put("align", "center");
				singheader3.put("width", "100");
				list.add(singheader3);
			}
			if(compareValue.contains("4")){
				Map<String,Object> singheader4 = new HashMap<String,Object>();
				singheader4.put("header", "同比");
				singheader4.put("field", "value4"+"_"+j);
				singheader4.put("visible", "true");
				singheader4.put("headerAlign", "center");
				singheader4.put("align", "center");
				singheader4.put("width", "100");
				list.add(singheader4);
			}
		}
		//setAttr("headers", headers);
		//setAttr("data", rsList);
		/*for(int i=0;i<strs.length;i++){


			Record result = Db.findFirst(sql);

			headers.add(new Record().set("text", r.getStr("REMARK")).set("field", "a"+r.getStr("VAL")));
		}
		setAttr("data", result);*/
		result = list.toArray(new HashMap[list.size()]);
		renderJson(result);

	}
	/**
	 * 查询所有相对数据
	 * 
	 */
	public void queryData() {
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
		//String orgnum = getCurrentUser().getStr("ORG_ID");begindate comparevalue enddate	orgid yjstr	zbstr
		// 获取查询条件  已使用
		String orgid = getPara("orgid");
		String userid = getPara("followed_teller");
		String begindate = getPara("begindate");
		String enddate = getPara("enddate");
		String compareValue = getPara("comparevalue");
		String yjstr = getPara("yjstr");//预警编号
		String zbstr = getPara("zbstr");//指标编号
		String type  = getPara("type","0");

		String zhstr = "";
		if(StringUtils.isNotEmpty(yjstr)&&StringUtils.isNotEmpty(zbstr)){
			zhstr = yjstr+","+zbstr;
		}else if(StringUtils.isNotEmpty(yjstr)&&StringUtils.isEmpty(zbstr)){
			zhstr = yjstr;
		}else{
			zhstr = zbstr;
		}
		StringBuffer compares = new StringBuffer();
		boolean isFir = true;
		for(int i=1;i<=4;i++){
			if(isFir){
				isFir = false;
			}else{ 
				compares.append(",");
			}
			if(compareValue.indexOf(""+i) != -1){
				compares.append("1");
			}else{
				compares.append("0");
			}
		}


		zbstr = AppUtils.getStrByDistinct(zbstr);
		yjstr = AppUtils.getStrByDistinct(yjstr);

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		try {
			begindate = sdf.format(sdf1.parse(begindate));
			enddate = sdf.format(sdf1.parse(enddate));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		//暂未使用
		String parenttype = getPara("parenttype");
		String mark_dimension = getPara("mark_dimension");
		String ywtype = getPara("ywtype");
		String sub_busi_code = getPara("sub_busi_code");
		String mark_code = getPara("mark_code");
		String warning_type = getPara("warning_type");
		String warn_wd = getPara("warn_wd");
		String warn_name = getPara("warn_name");

		if(userid.contains(",")){//此时为多人员
			List<Record> rsList = new ArrayList<Record>();

			
			String sql;
			sql = "call ap_idop.DPRO_GROUP_QRY_SINGLE_PERSON('"+userid+"','"+begindate+"','"+enddate+"','"+zbstr+"','"+yjstr+"','"+compares.toString()+"','"+userNo+"',@a,@b)";
			Db.use("gbase").find(sql);


			String selectstr = "SELECT t.deptname,t.userno,t.module_name,t.ywlx,t.indexcode,t.indexname,nvl((case when unit ='%' then concat (round(t.value0*100,2),'%') else t.value0  end ),'--') as value0,"+
					"nvl((case when unit ='%' then concat (round(t.value1*100,2),'%') else t.value1  end ),'--') as value1," +
					"nvl((case when unit ='%' then concat (round(t.value2*100,2),'%') else t.value2  end ),'--') as value2," +
					"nvl((case when unit ='%' then concat (round(t.value3*100,2),'%') else t.value3  end ),'--') as value3," +
					"nvl((case when unit ='%' then concat (round(t.value4*100,2),'%') else t.value4  end ),'--') as value4,"+
					"nvl(t.value5,'--') as value5,nvl(t.value6,'--') as value6,nvl(t.value7,'--') as value7,nvl(t.value8,'--') as value8,"+
					"nvl(t.value9,'--') as value9,nvl(t.value10,'--') as value10,nvl(t.value11,'--') as value11,nvl(t.value12,'--') as value12,t.flag,t.czy FROM ap_idop.dop_group_query_person t left join dop_mark_param dmp on t.indexcode=dmp.mark_code  where t.czy = ? order by t.module_name";
			List<Record> rList = Db.use("gbase").find(selectstr,userNo);
			//List<Record> rList = Db.use("gbase").find("select * from ap_idop.dop_group_query where czy = ? order by module_name",userNo);


			

			//查询人员中文
			String [] userids = userid.split(regex);
			List<String> inSqlList = new ArrayList<>();
			StringBuffer inSqlBuffer = new StringBuffer();
			isFir = true;
			for (String val : userids) {
				if(isFir){
					isFir = false;
				}else{
					inSqlBuffer.append(",");
				}
				inSqlBuffer.append("?");
				inSqlList.add(val);
			}
			List<Record> userList =  Db.find("select sui.*,doi.orgname,doi.orgnum from sys_user_info sui , sys_org_info doi where sui.org_id=doi.orgnum and user_no in ("+inSqlBuffer.toString()+")",inSqlList.toArray());


			Record userr = new Record();
			for (Record user : userList) {
				userr.set(user.getStr("user_no"), user.get("name"));
				userr.set(user.getStr("orgnum"), user.get("orgname"));
			}


			/*
				 	1.给每一行数据打上标签进行数据归类，但是每一行数据都有多个标签时如何处理？
			 */

			//1.按照业务模块进行第一层数据归纳
			//业务模块-业务类型-名称-人员
			Map<String,Map<String,Map<String,Map<String,List<Record>>>>> modelMap = new HashMap<>();
			String deptname,userno,module_name,ywlx,indexname;


			for (Record r : rList) {
				deptname = r.getStr("deptname");	
				userno = r.getStr("userno");
				module_name = r.getStr("module_name");
				ywlx = r.getStr("ywlx");
				indexname = r.getStr("indexname");
				//初始化业务模型参数
				reset(modelMap, userno, module_name, ywlx, indexname, r);
			}

			List<Record> headers = new ArrayList<>();
			headers.add(new Record().set("header", "机构名称").set("field", "deptname").set("visible", "true").set("headerAlign", "center").set("align", "center").set("width", "100"));
			headers.add(new Record().set("header", "人员名称").set("field", "username").set("visible", "true").set("headerAlign", "center").set("align", "center").set("width", "100"));
			headers.add(new Record().set("header", "开始时间").set("field", "begindate").set("visible", "true").set("headerAlign", "center").set("align", "center").set("width", "100"));
			headers.add(new Record().set("header", "结束时间").set("field", "enddate").set("visible", "true").set("headerAlign", "center").set("align", "center").set("width", "100"));
			Set<String> onekeys = modelMap.keySet();

			Map<String,Record> dataMap = new HashMap<>();
			Record one = new Record();
			Record two = new Record();
			int cloumnIndex = 0;

			//组合比较类型
			String [] compareValues = compareValue.split(regex);
			inSqlList = new ArrayList<>();
			inSqlBuffer = new StringBuffer();
			isFir = true;
			for (String val : compareValues) {
				if(isFir){
					isFir = false;
				}else{
					inSqlBuffer.append(",");
				}
				inSqlBuffer.append("?");
				inSqlList.add(val);
			}

			//根据字典表拼装field 和  text
			List<Record> paramList = Db.find("select * from sys_param_info where key = 'COMPAREVALUE' and val in ("+inSqlBuffer.toString()+") ",inSqlList.toArray());

			String[] fileds = new String[paramList.size()];
			String[] texts = new String[paramList.size()];
			for (int i = 0; i < paramList.size(); i++) {
				fileds[i] = "value"+paramList.get(i).getStr("val");
				texts[i] = paramList.get(i).getStr("remark")+paramList.get(i).getStr("name");
			}

			for (String onekey : onekeys) {//业务模型
				one = new Record()
				.set("header", onekey)
				.set("field", "")
				.set("headerAlign", "center")
				.set("align", "center");
				List<Record> twoColumns = new ArrayList<>();
				Set<String> twokeys = modelMap.get(onekey).keySet();
				for (String twokey : twokeys) {//业务类型
					two = new Record()
					.set("header", twokey)
					.set("field", "")
					.set("headerAlign", "center")
					.set("align", "center");
					List<Record> threeColumns = new ArrayList<>();
					Set<String> threekeys = modelMap.get(onekey).get(twokey).keySet(); 
					for (String threekey : threekeys) {//组合参数

						Map<String,List<Record>> usermap = modelMap.get(onekey).get(twokey).get(threekey);
						Set<String> usermapkeys = usermap.keySet();
						cloumnIndex++;
						//类型名称
						threeColumns.add(new Record()
						.set("header",threekey )
						.set("field","name-"+cloumnIndex)
						.set("headerAlign", "center")
						.set("align", "center")
						.set("width", "100"));
						//比较值
						for (int i=0;i<fileds.length;i++) {
							threeColumns.add(new Record()
							.set("header", texts[i])
							.set("field", fileds[i]+"-"+cloumnIndex)
							.set("headerAlign", "center")
							.set("align", "center")
							.set("width", "100")
									);
						}
						//按照人员组装数据行
						for (String usermapkey : usermapkeys) {
							List<Record> tLsit = usermap.get(usermapkey);

							if(!dataMap.containsKey(usermapkey)){
								dataMap.put(usermapkey, new Record());
								dataMap.get(usermapkey).set("userno",usermapkey);
								dataMap.get(usermapkey).set("username",userr.get(usermapkey));//进行人员中文转换
								dataMap.get(usermapkey).set("deptname",usermap.get(usermapkey).get(0).get("deptname"));
								dataMap.get(usermapkey).set("begindate",begindate);
								dataMap.get(usermapkey).set("enddate",enddate);
							}
						
							
							//				    			for (Record record : tLsit) {
							//				    				
							//		    						dataMap.get(deptmapkey).set("name-"+cloumnIndex, record.get("value0"));
							//				    				for (int i=0;i<fileds.length;i++) {
							//				    					dataMap.get(deptmapkey).set(fileds[i]+"-"+cloumnIndex, record.get(fileds[i]));
							//				    				}
							//								}
							for (Record record : tLsit) {

								dataMap.get(usermapkey).set("name-"+cloumnIndex, record.get("value0"));
								for (int i=0;i<fileds.length;i++) {
									dataMap.get(usermapkey).set(fileds[i]+"-"+cloumnIndex, record.get(fileds[i]));
								}
							}
						}
					}
					two.set("columns", threeColumns);
					twoColumns.add(two);
				}
				one.set("columns", twoColumns);
				headers.add(one);

			}

			Set<String> keys = dataMap.keySet();
			for (String key : keys) {
				rsList.add(dataMap.get(key));
			}
			//对数据进行转列	
			/*
			 	1.人员作为KEY
			 	2.按照业务模块进行第一层数据归纳
			 	3.按照业务类型进行第二层数据归纳
			 	4.按照动态列进行数据填充
			 */
			// 赋值
			setAttr("data", rsList);
			setAttr("headers", headers);
			log.info("getList--r):" + rsList);
		}else{//单人员
			//List<Record> orgList =  Db.find("select * from sys_org_info where orgnum  = '"+orgid+"'");
			List<Record> userList =  Db.find("select sui.*,doi.orgname,doi.orgnum from sys_user_info sui , sys_org_info doi where sui.org_id=doi.orgnum and user_no  = '"+userid+"'");
			

			String sql;
			sql = "call ap_idop.DPRO_GROUP_QRY_SINGLE_PERSON('"+userid+"','"+begindate+"','"+enddate+"','"+zbstr+"','"+yjstr+"','"+compares.toString()+"','"+userNo+"',@a,@b)";
			Db.use("gbase").find(sql);

			List<Record> rList = Db.use("gbase").find("select deptname,userno,module_name,ywlx,indexcode,indexname,"
					+"(case when unit ='%' then concat (round(value0*100,2),'%') else value0  end ) value0," +
					"(case when unit ='%' then concat (round(value1*100,2),'%') else value1  end ) value1," +
					"(case when unit ='%' then concat (round(value2*100,2),'%') else value2  end ) value2," +
					"(case when unit ='%' then concat (round(value3*100,2),'%') else value3  end ) value3," +
					"(case when unit ='%' then concat (round(value4*100,2),'%') else value4  end ) value4," +
					"value5,value6,value7,value8,value9,value10,value11,value12,flag,czy from ap_idop.dop_group_query_person dki left join dop_mark_param dmp on dki.indexcode=dmp.mark_code where czy = ? order by userno,module_name,ywlx desc",userNo);


			// 赋值
			Record userr = new Record();
			for (Record user : userList) {
				userr.set(user.getStr("user_no"), user.get("name"));
				//userr.set(user.getStr("orgnum"), user.get("orgname"));

			}

			for (Record record : rList) {
				record.set("userno",userr.get(record.getStr("userno")));
				//record.set("deptno",userr.get(record.getStr("deptno")));
			}



			// 赋值

			setAttr("data", rList);
			log.info("getList--r):" + rList);
		}

		// 打印日志

		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "查询报表", "9", "组合-查询_人员维度");
		log.info("组合-查询_人员维度");
		// 返回json数据
		renderJson();
	}

	public boolean reset(Map<String,Map<String,Map<String,Map<String,List<Record>>>>> modelMap,String userno,String module_name,String ywlx,String indexname,Record r){
		boolean flag = false;
		if(modelMap.containsKey(module_name)){
			if(modelMap.get(module_name).containsKey(ywlx)){
				if(modelMap.get(module_name).get(ywlx).containsKey(indexname)){
					if(modelMap.get(module_name).get(ywlx).get(indexname).containsKey(userno)){
						modelMap.get(module_name).get(ywlx).get(indexname).get(userno).add(r);
					}else{
						modelMap.get(module_name).get(ywlx).get(indexname).put(userno, new ArrayList<Record>());
						modelMap.get(module_name).get(ywlx).get(indexname).get(userno).add(r);
					}
				}else{
					modelMap.get(module_name).get(ywlx).put(indexname, new HashMap<String,List<Record>>());
					flag = true;
				}
			}else{
				modelMap.get(module_name).put(ywlx, new HashMap<String,Map<String,List<Record>>>());
				flag = true;
			}
		}else{
			modelMap.put(module_name, new HashMap<String,Map<String,Map<String,List<Record>>>>());
			flag = true;
		}
		if(flag){
			return reset(modelMap, userno, module_name, ywlx, indexname, r);
		}else{
			return flag;
		}

	}

	/**
	 * 查询关键指标名称下拉列表
	 */
	public void getMarkParamList(){
		String sub_busi_code =getPara("sub_busi_code");
		String mark_dimension="1";
		if(AppUtils.StringUtil(sub_busi_code)!=null ){
			Record sub=Db.findFirst("select id,type,remark,val from DOP_GANGEDMENU_INFO where id=?",new Object[]{sub_busi_code});//获取业务类型中文
			sub_busi_code=sub.get("val").toString();
		}
		String sql="select mark_code , mark_name from dop_mark_param  ";


		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1  ");
		List<String> sqlStr = new ArrayList<String>();  
		// 查询条件
		if (AppUtils.StringUtil(sub_busi_code) != null) {
			whereSql.append(" AND  sub_busi_code = ? ");
			sqlStr.add(sub_busi_code);

		}
		if(AppUtils.StringUtil(mark_dimension) != null){
			whereSql.append(" AND  mark_dimension = ? ");
			sqlStr.add(mark_dimension);
		}

		whereSql.append("  and is_key_mark = ? order by mark_code");
		sqlStr.add("1");

		sql+=whereSql.toString();

		List<Record>list=Db.find(sql,sqlStr.toArray());
		setAttr("data",list);
		renderJson();
	}

	/**
	 * 查询关键指标名称下拉列表
	 */
	public void getMarkParam(){
		String sub_busi_code =getPara("val");
		String mark_dimension=getPara("mark_dimension");
		if(AppUtils.StringUtil(sub_busi_code)!=null ){
			Record sub=Db.findFirst("select id,type,remark,val from DOP_GANGEDMENU_INFO where id=?",new Object[]{sub_busi_code});//获取业务类型中文
			sub_busi_code=sub.get("val").toString();
		}
		String sql="select mark_code , mark_name from dop_mark_param  ";


		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1  ");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if (AppUtils.StringUtil(sub_busi_code) != null) {
			whereSql.append(" AND  sub_busi_code = ? ");
			sqlStr.add(sub_busi_code);

		}
		if(AppUtils.StringUtil(mark_dimension) != null){
			whereSql.append(" AND  mark_dimension = ? ");
			sqlStr.add(mark_dimension);
		}

		whereSql.append("  and is_key_mark = ? ");
		sqlStr.add("1");

		sql+=whereSql.toString();

		List<Record>list=Db.find(sql,sqlStr.toArray());
		setAttr("data",list);
		renderJson();
	}

	/**
	 * 跳转报表页面
	 */
	public void queryPage(){
		String str = getPara("str");
		// 赋值
		setAttr("data", str);
		if(str.equals("gywdyytp")||str.equals("jgwdyytp")||str.equals("gywdtp")||str.equals("jgwdtp")){
			render("threeParamPage.jsp");
		}else{
			render("twoParamPage.jsp");
		}
	}

	private final static int[] areacode={1601,1637,1833,2078,2274,2302,2433,2594,2787,3106,3212,3472,3635,3722,3730,3858,4027,4086,4390,4558,4684,4925,5249,5590};
	private final static String[] letters={"a","b","c","d","e","f","g","h","j","k","l","m","n","o","p","q","r","s","t","w","x","y","z"};

	public  void getAllFirstLetter(){
		String _str = "";
		String str = getPara("bbname");
		if(str==null ||str.trim().length()==0){
			_str = "";
		}
		for(int i=0;i<str.length();i++){
			_str =_str+getFirstLetter(str.substring(i,i+1));
		}
		setAttr("data", _str);
		renderJson();
	}
	public  String getFirstLetter(String str){
		str = conversionstr(str,"GB2312","ISO8859-1");
		if(str.length()>1){
			int li_sectorCode = (int)str.charAt(0);//区码
			int li_positionCode = (int)str.charAt(1);//位码
			li_sectorCode = li_sectorCode-160;
			li_positionCode = li_positionCode-160;
			int li_secposcode = li_sectorCode*100+li_positionCode;//汉子区位码
			if(li_secposcode>1600&&li_secposcode<5590){
				for (int i=0;i<23;i++){
					if(li_secposcode>=areacode[i] && li_secposcode<areacode[i+1]){
						str = letters[i];
						break;
					}
				}
			}
			else{
				str = conversionstr(str,"ISO8859-1","GB2312");
				str = str.substring(0,1);
			}
		}
		return str;

	}
	public  String conversionstr(String str,String charsetName,String toCharSetName){
		try{
			str = new String(str.getBytes(charsetName),toCharSetName);
		}catch(UnsupportedEncodingException e){
			System.out.println(e.getMessage()+"字符串转码异常");
		}
		return str;

	}
	/**
	 * 导出人员Excel表格
	 */
	//@SuppressWarnings("unchecked")
	//@Before(PermissionInterceptor.class)
	public void exportRy(){
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
		// 获取查询条件  已使用
		String orgid = getPara("orgid");
		String userid=getPara("followed_teller");
		String begindate = getPara("begindate");
		String enddate = getPara("enddate");
		String compareValue = getPara("comparevalue");

		String yjstr = getPara("yjstr");//预警编号
		String zbstr = getPara("zbstr");//指标编号
		String type  = getPara("type","0");
		String zhstr = "";
		String sql;
		if(StringUtils.isNotEmpty(yjstr)&&StringUtils.isNotEmpty(zbstr)){
			zhstr = yjstr+","+zbstr;
		}else if(StringUtils.isNotEmpty(yjstr)&&StringUtils.isEmpty(zbstr)){
			zhstr = yjstr;
		}else{
			zhstr = zbstr;
		}
		StringBuffer compares = new StringBuffer();
		boolean isFir = true;
		for(int i=1;i<=4;i++){
			if(isFir){
				isFir = false;
			}else{ 
				compares.append(",");
			}
			if(compareValue.indexOf(""+i) != -1){
				compares.append("1");
			}else{
				compares.append("0");
			}
		}
		zbstr = AppUtils.getStrByDistinct(zbstr);
		yjstr = AppUtils.getStrByDistinct(yjstr);

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		try {
			begindate = sdf.format(sdf1.parse(begindate));
			enddate = sdf.format(sdf1.parse(enddate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(userid.contains(",")){//多维度
			List<Record> rsList = new ArrayList<Record>();
			/**********导出二级维度的数据***********/

			log.info("单机构查询导出二级维度机构数据存储过程 ap_idop.DPRO_GROUP_QRY_SINGLE_PERSON 调用开始:"+DateTimeUtil.getTime());
			sql = "call ap_idop.DPRO_GROUP_QRY_SINGLE_PERSON('"+userid+"','"+begindate+"','"+enddate+"','"+zbstr+"','"+yjstr+"','"+compares.toString()+"','"+userNo+"',@a,@b)";
			Db.use("gbase").update(sql);
			System.out.println("二级维度机构数据计算SQL：" + sql);
			log.info("单机构查询导出二级维度机构数据存储过程 ap_idop.DPRO_GROUP_QRY_SINGLE_PERSON  调用结束:"+ DateTimeUtil.getTime()); 


			//查询
			List<Record> SList = Db.use("gbase").find("select * from ap_idop.dop_group_query_person where czy = ? order by module_name",userNo);
			for(Record SsList :SList){
				if(SsList.get("value0") ==null){
					SsList.set("value0","--");
				}
				if(SsList.get("value1") ==null){
					SsList.set("value1","--");
				}
				if(SsList.get("value2") ==null){
					SsList.set("value2","--");
				}
				if(SsList.get("value3") ==null){
					SsList.set("value3","--");
				}
				if(SsList.get("value4") ==null){
					SsList.set("value4","--");
				}
			}
			//查询人员中文
			String [] userids = userid.split(regex);
			List<String> inSqlList = new ArrayList<>();
			StringBuffer inSqlBuffer = new StringBuffer();
			isFir = true;
			for (String val : userids) {
				if(isFir){
					isFir = false;
				}else{
					inSqlBuffer.append(",");
				}
				inSqlBuffer.append("?");
				inSqlList.add(val);
			}
			//			List<Record> orgList =  Db.find("select * from sys_org_info where orgnum in ("+inSqlBuffer.toString()+")",inSqlList.toArray());
			List<Record> userList =  Db.find("select * from sys_user_info where user_no in ("+inSqlBuffer.toString()+")",inSqlList.toArray());
			Record userr = new Record();
			for (Record user : userList) {
				userr.set(user.getStr("user_no"), user.get("name"));
			}

			//1.按照业务模块进行第一层数据归纳
			//业务模块-业务类型-名称-人员
			Map<String,Map<String,Map<String,Map<String,List<Record>>>>> modelMap = new HashMap<>();
			String deptname,userno,module_name,ywlx,indexname;


			for (Record r : SList) {
				deptname = r.getStr("deptname");	
				userno = r.getStr("userno");	
				module_name = r.getStr("module_name");
				ywlx = r.getStr("ywlx");
				indexname = r.getStr("indexname");
				//初始化业务模型参数
				reset(modelMap, userno, module_name, ywlx, indexname, r);
			}

			List<Record> headers = new ArrayList<>();
			headers.add(new Record().set("header", "机构名称").set("field", "deptname").set("visible", "true").set("headerAlign", "center").set("align", "center").set("width", "100"));
			headers.add(new Record().set("header", "人员名称").set("field", "username").set("visible", "true").set("headerAlign", "center").set("align", "center").set("width", "100"));
			headers.add(new Record().set("header", "开始时间").set("field", "begindate").set("visible", "true").set("headerAlign", "center").set("align", "center").set("width", "100"));
			headers.add(new Record().set("header", "结束时间").set("field", "enddate").set("visible", "true").set("headerAlign", "center").set("align", "center").set("width", "100"));
			Set<String> onekeys = modelMap.keySet();
			Set<String> twokeys=null;
			Map<String,Record> dataMap = new HashMap<>();
			Record one = new Record();
			Record two = new Record();
			int cloumnIndex = 0;

			//组合比较类型
			String [] compareValues = compareValue.split(regex);
			inSqlList = new ArrayList<>();
			inSqlBuffer = new StringBuffer();
			isFir = true;
			for (String val : compareValues) {
				if(isFir){
					isFir = false;
				}else{
					inSqlBuffer.append(",");
				}
				inSqlBuffer.append("?");
				inSqlList.add(val);
			}

			//根据字典表拼装field 和  text
			List<Record> paramList = Db.find("select * from sys_param_info where key = 'COMPAREVALUE' and val in ("+inSqlBuffer.toString()+") ",inSqlList.toArray());
			List<Record> threeColumns = new ArrayList<>();
			String[] fileds = new String[paramList.size()];
			String[] texts = new String[paramList.size()];
			for (int i = 0; i < paramList.size(); i++) {
				fileds[i] = "value"+paramList.get(i).getStr("val");
				texts[i] = paramList.get(i).getStr("remark")+paramList.get(i).getStr("name");
			}

			for (String onekey : onekeys) {//业务模型
				List<Record> twoColumns = new ArrayList<>();
				twokeys= modelMap.get(onekey).keySet();
				for (String twokey : twokeys) {//业务类型

					Set<String> threekeys = modelMap.get(onekey).get(twokey).keySet(); 
					for (String threekey : threekeys) {//组合参数	
						Map<String,List<Record>> usermap = modelMap.get(onekey).get(twokey).get(threekey);
						Set<String> usermapkeys = usermap.keySet();
						cloumnIndex++;
						//类型名称
						threeColumns.add(new Record()
						.set("header",threekey )
						.set("field","name-"+cloumnIndex)
						.set("headerAlign", "center")
						.set("align", "center")
						.set("width", "100"));
						//比较值
						for (int i=0;i<fileds.length;i++) {
							threeColumns.add(new Record()
							.set("header", texts[i])
							.set("field", fileds[i]+"-"+cloumnIndex)
							.set("headerAlign", "center")
							.set("align", "center")
							.set("width", "100")
									);
						}
						//按照机构组装数据行
						for (String usermapkey : usermapkeys) {
							List<Record> tLsit = usermap.get(usermapkey);

							if(!dataMap.containsKey(usermapkey)){
								dataMap.put(usermapkey, new Record());
								dataMap.get(usermapkey).set("userno",usermapkey);
								dataMap.get(usermapkey).set("username",userr.get(usermapkey));//进行人员中文转换
								dataMap.get(usermapkey).set("deptname",usermap.get(usermapkey).get(0).get("deptname"));
								dataMap.get(usermapkey).set("begindate",begindate);
								dataMap.get(usermapkey).set("enddate",enddate);
							}

							for (Record record : tLsit) {

								dataMap.get(usermapkey).set("name-"+cloumnIndex, record.get("value0"));
								for (int i=0;i<fileds.length;i++) {
									dataMap.get(usermapkey).set(fileds[i]+"-"+cloumnIndex, record.get(fileds[i]));
								}
							}
						}
					}
					two.set("columns", threeColumns);
					headers.add(two);
				}		
			}

			Set<String> keys = dataMap.keySet();
			for (String key : keys) {
				rsList.add(dataMap.get(key));
			}

			log.info("getList--r):" + rsList);
			exportMxsExcel(userNo,rsList,threeColumns,onekeys,twokeys);

		}else{//单维度
			/**********导出一级维度的数据***********/


			log.info("单机构查询导出一级维度机构数据存储过程 ap_idop.DPRO_GROUP_QRY_SINGLE_PERSON 调用开始:"+DateTimeUtil.getTime());
			sql = "call ap_idop.DPRO_GROUP_QRY_SINGLE_PERSON('"+userid+"','"+begindate+"','"+enddate+"','"+zbstr+"','"+yjstr+"','"+compares.toString()+"','"+userNo+"',@a,@b)";
			Db.use("gbase").update(sql);
			System.out.println("一 级维度机构数据计算SQL：" + sql);
			log.info("单机构查询导出一级维度机构数据存储过程 ap_idop.DPRO_GROUP_QRY_SINGLE_PERSON  调用结束:"
					+ DateTimeUtil.getTime()); 
			//exportMxfExcel(userNo,compareValue);
			exportExcel(userid,userNo,compareValue);
		}
		renderNull();
	}
	//导出多维度 ()
	public void exportMxsExcel(String userNo,List<Record> rsList,List<Record> threeColumns,Set<String> onekeys,Set<String> twokeys){


		//获取要合并表头内要合并列的名称
		String onekey=null;
		String twokey=null;
		int lens=threeColumns.size();
		for(String okey:onekeys){
			System.out.println(okey);
			onekey=okey;
		}
		for(String tkey:twokeys){
			System.out.println(tkey);
			twokey=tkey;
		}
		//需要合并的表头
		String[][] arr=new String[3][]; 
		String[] onek=new String[lens+4];
		String[] twok=new String[lens+4];
		onek[0]="机构名称";
		onek[1]="EHR号";
		onek[2]="人员名称";
		onek[3]="开始时间";
		onek[4]="结束时间";
		twok[0]="机构名称";
		twok[1]="EHR号";
		twok[2]="人员名称";
		twok[3]="开始时间";
		twok[4]="结束时间";
		for(int le=5;le<lens+3;le++){
			onek[le]=onekey;
			twok[le]=twokey;
		}
		arr[0]=onek;
		arr[1]=twok;


		//最后一行的数据
		int len=threeColumns.size()/2;
		String [] headers1 = new String[threeColumns.size()+5];
		String [] columns1 = new String[threeColumns.size()+5];
		headers1[0]="deptname";
		columns1[0]="机构名称";
		headers1[1]="userno";
		columns1[1]="EHR号";
		headers1[2]="username";
		columns1[2]="人员名称";
		headers1[3]="begindate";
		columns1[3]="开始时间";
		headers1[4]="enddate";
		columns1[4]="结束时间";
		for(int i=0;i<threeColumns.size();i++){
			headers1[i+5]=threeColumns.get(i).getStr("field");
			columns1[i+5]=threeColumns.get(i).getStr("header");
		}
		arr[2]=columns1;
		//设置 单元格 合并位置起始行，结束行，起始列，结束列
		String [] oneMulNum  = {"0,2,0,0","0,2,1,1","0,2,2,2","0,2,3,3","0,2,4,4","0,0,5,"+(threeColumns.size()+4),"1,1,5,"+(threeColumns.size()+4)};
		String [][] columnsMulNum  = new String[1][];
		columnsMulNum[0] = oneMulNum;

		// 转换成excel
		ExcelRenderx er = null;
		String fileName = "";
		try {
			fileName = new String("组合查询-人员维度数据展现.xls".getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}
		er = new ExcelRenderx(fileName, headers1, arr,columnsMulNum, rsList, getResponse());
		er.renderMulRow(); 

		//打印日志
		log.info("getList--r):" + rsList);

	}
	//单维度
	public void exportExcel(String userid, String userNo, String compareValue){
		//查询
		List<Record> rlist = Db.use("gbase").find("select deptname,userno,module_name,ywlx,indexname ,( case when flag='0' then '指标' else '预警' end ) flags" +
				", value0,value1,value2,value3 from ap_idop.dop_group_query_person where czy = ?",userNo);

		//人员查询
		String user=Db.queryStr("select name from sys_user_info where user_no= ?", new Object[] { userid });
		//数据处理 
		for(Record SsList :rlist){
			if(SsList.get("value0") ==null){
				SsList.set("value0","--");
			}
			if(SsList.get("value1") ==null){
				SsList.set("value1","--");
			}
			if(SsList.get("value2") ==null){
				SsList.set("value2","--");
			}
			if(SsList.get("value3") ==null){
				SsList.set("value3","--");
			}
			if(SsList.get("value4") ==null){
				SsList.set("value4","--");
			}
			SsList.set("username", user);
		}


		/*****************动态生成表头数据*********************/
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> singleheader = new HashMap<String,Object>();
		singleheader.put("header", "机构名称");
		singleheader.put("field", "deptname");
		singleheader.put("visible", "true");
		singleheader.put("headerAlign", "center");
		singleheader.put("align", "center");
		singleheader.put("width", "100");
		list.add(singleheader);
		Map<String,Object> singleheader01 = new HashMap<String,Object>();
		singleheader01.put("header", "EHR号");
		singleheader01.put("field", "userno");
		singleheader01.put("visible", "true");
		singleheader01.put("headerAlign", "center");
		singleheader01.put("align", "center");
		singleheader01.put("width", "100");
		list.add(singleheader01);
		Map<String,Object> singleheader0 = new HashMap<String,Object>();
		singleheader0.put("header", "人员名称");
		singleheader0.put("field", "username");
		singleheader0.put("visible", "true");
		singleheader0.put("headerAlign", "center");
		singleheader0.put("align", "center");
		singleheader0.put("width", "100");
		list.add(singleheader0);
		Map<String,Object> singleheader1 = new HashMap<String,Object>();
		singleheader1.put("header", "业务模块");
		singleheader1.put("field", "module_name");
		singleheader1.put("visible", "true");
		singleheader1.put("headerAlign", "center");
		singleheader1.put("align", "center");
		singleheader1.put("width", "100");
		list.add(singleheader1);
		Map<String,Object> singleheader2 = new HashMap<String,Object>();
		singleheader2.put("header", "业务类型");
		singleheader2.put("field", "ywlx");
		singleheader2.put("visible", "true");
		singleheader2.put("headerAlign", "center");
		singleheader2.put("align", "center");
		singleheader2.put("width", "100");
		list.add(singleheader2);
		//根据前台传回来的组合查询参数中指标和预警的名称拼出加载列
		Map<String,Object> singheader = new HashMap<String,Object>();
		singheader.put("header", "名称");
		singheader.put("field", "indexname");
		singheader.put("visible", "true");
		singheader.put("headerAlign", "center");
		singheader.put("align", "center");
		singheader.put("width", "100");
		list.add(singheader);
		Map<String,Object> singheaderss = new HashMap<String,Object>();
		singheaderss.put("header", "类型");
		singheaderss.put("field", "flags");
		singheaderss.put("visible", "true");
		singheaderss.put("headerAlign", "center");
		singheaderss.put("align", "center");
		singheaderss.put("width", "100");
		list.add(singheaderss);
		Map<String,Object> singheader1 = new HashMap<String,Object>();
		singheader1.put("header", "基准值");
		singheader1.put("field", "value0");
		singheader1.put("visible", "true");
		singheader1.put("headerAlign", "center");
		singheader1.put("align", "center");
		singheader1.put("width", "100");
		list.add(singheader1);
		if(compareValue.contains("1")){
			Map<String,Object> singheader2 = new HashMap<String,Object>();
			singheader2.put("header", "上日比较值");
			singheader2.put("field", "value1");
			singheader2.put("visible", "true");
			singheader2.put("headerAlign", "center");
			singheader2.put("align", "center");
			singheader2.put("width", "100");
			list.add(singheader2);
		}
		if(compareValue.contains("2")){
			Map<String,Object> singheader2 = new HashMap<String,Object>();
			singheader2.put("header", "上月末比较值");
			singheader2.put("field", "value2");
			singheader2.put("visible", "true");
			singheader2.put("headerAlign", "center");
			singheader2.put("align", "center");
			singheader2.put("width", "100");
			list.add(singheader2);
		}
		if(compareValue.contains("3")){
			Map<String,Object> singheader3 = new HashMap<String,Object>();
			singheader3.put("header", "上年末比较值");
			singheader3.put("field", "value3");
			singheader3.put("visible", "true");
			singheader3.put("headerAlign", "center");
			singheader3.put("align", "center");
			singheader3.put("width", "100");
			list.add(singheader3);
		}
		/***************************end**************************/

		/**************************对应的数据与表头一致*********************************/
		String[] headers= new String[list.size()];
		String[] columns= new String[list.size()];
		for(int j=0;j<list.size();j++){
			headers[j]=list.get(j).get("field").toString();
			columns[j]=list.get(j).get("header").toString();
		}
		//头部两行需要合并
		String[] oneRowArr=new String[list.size()];
		oneRowArr[0]="机构名称";
		oneRowArr[1]="人员编码";
		oneRowArr[2]="人员名称";
		oneRowArr[3]="指标/预警";
		oneRowArr[4]="指标/预警";
		oneRowArr[5]="指标/预警";	
		for(int k=6;k<list.size();k++){
			oneRowArr[k]=columns[k];
		}
		//二维数组2行头部
		String [][] columns2 = new String[2][];
		columns2[0] = oneRowArr;
		columns2[1] = columns;


		//设置 单元格 合并位置起始行，结束行，起始列，结束列
		/*****************合并表头*********************/
		String [] oneMulNum  =new String[list.size()-2];// {"0,1,0,0","0,0,1,"+len,"0,0,"+(len+1)+","+headList.size()};
		oneMulNum[0]="0,1,0,0";
		oneMulNum[1]="0,1,1,1";
		oneMulNum[2]="0,1,2,2";
		oneMulNum[3]="0,0,3,5";
		for(int kk=4;kk<list.size()-2;kk++){
			oneMulNum[kk]="0,1,"+(kk+2)+","+(kk+2);
		}
		/**************要合并的单元格****************/
		//String[] mergedColumns={""};//合并单元格元素，例如说合并那些列（字段名）
		//String[] headMergedColumns={" "};//合并单元格分组元素，例如说依照于那个元素进行分组，比如说 以第一列为分组，合并1到3列分组内相同的值

		String [][] columnsMulNum  = new String[1][];
		columnsMulNum[0] = oneMulNum;
		// 转换成excel
		ExcelRenderx er = null;
		String fileName = "";
		try {
			fileName = new String("组合查询-人员维度数据展现.xls".getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}
		er = new ExcelRenderx(fileName,headers,columns2,columnsMulNum,rlist,getResponse());
		//er = new ExcelRenderx(null, fileName, headers, null, mergedColumns, headMergedColumns, columns2, columnsMulNum, null, rlist, getResponse());
		er.renderMulRow(); 
		//打印日志
		log.info("getList--r):" + rlist);

		//查询结束删除本次查询数据
		// Db.use("gbase").update("delete ap_idop.dop_group_query where  czy = ? ",userNo);
		renderNull();
	}

}

