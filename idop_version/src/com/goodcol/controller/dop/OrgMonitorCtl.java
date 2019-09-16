package com.goodcol.controller.dop;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.goodcol.controller.BaseCtl;
import com.goodcol.controller.dop.CpcCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;

/**
 * 机构监测
 *
 */
@RouteBind(path = "/orgMonitor")
@Before({ManagerPowerInterceptor.class})
public class OrgMonitorCtl extends BaseCtl{
	
	// 记录日志用
	public static Logger log = Logger.getLogger(CpcCtl.class); 
	
	
	/**
	 * 加载主页面
	 */
	@Override
//	@Before(PermissionInterceptor.class)
	public void index() {
		render("index.jsp");
	}
	
	/**
	 * 跳转至监测说明页面
	 */
//	@Before(PermissionInterceptor.class)
	public void toView() {
		render("form1.jsp");
	}
	
	/**
	 * 跳转至监测明细页面
	 */
//	@Before(PermissionInterceptor.class)
	public void toDetail() {
		
		String monitor_name = getPara("monitor_name"); //监测条件
		String monitor_date = getPara("monitor_date"); //监测时间
		setAttr("monitor_name", monitor_name);
		setAttr("monitor_date", monitor_date);
		render("form2.jsp");
	}
	
	
	//监测条件
	public void getMonitorName(){
		String sql = "select id,monitor_name from dop_monitor_config c where c.monitor_type='0' and c.monitor_name is not null";
		List<Record> list = Db.find(sql);
		setAttr("data",list);
		renderJson();
	}
		

	//监测详情
	public void getList(){
		
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");

		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		// 获取查询条件
		String monitor_name=getPara("monitor_name");//监测条件名称
		String date=getPara("monitor_date");//监测时间
		
		if(AppUtils.StringUtil(date)!=null){
			date=date.substring(0, 10).replace("-", "");
		}


		String sql="select c.id,c.monitor_name,c.monitor_rate,c.monitor_type,c.monitor_level,c.monitor_method,c.monitor_state,c.createtime ," +
				"d.BUSINESS_TYPE,d.MARK_TYPE_CODE,i.remark,d.VALUE,d.QUESTION " +
				"from dop_monitor_config c,dop_monitor_config_detail d ,sys_param_info i where i.key='compare_operator' and c.id=d.CONFIGID and i.val=d.compare_relate and c.id=?";
		
		Record sub=Db.findFirst(sql,monitor_name);
		String jcpl=sub.getStr("monitor_rate");
		String jccj = sub.getStr("monitor_level");
		Record remark = Db.findFirst("select remark from SYS_PARAM_INFO where key = 'monitor_rate' and val=? ", jcpl);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf1= null;
		Calendar cal = Calendar.getInstance();
		String endDate = "";
		try {
			switch(jcpl) {
			case "0":
				sdf1=new SimpleDateFormat("yyyy-MM-dd");
				date=sdf1.format(sdf.parse(date));
				break;
			case "1":
				sdf1=new SimpleDateFormat("yyyy-MM-dd");
				cal.setTime(sdf.parse(date));
				int interval = cal.get(Calendar.DAY_OF_WEEK);
				if (interval == 1) {
					cal.add(Calendar.DATE, -6);
				} else {
					cal.add(Calendar.DATE, -interval+2);
				}
				date = sdf1.format(cal.getTime());
				cal.add(Calendar.DATE, 6);
				endDate = sdf1.format(cal.getTime());
				date=date+"至"+endDate;
				break;
			case "3":
				sdf1=new SimpleDateFormat("yyyy-MM-dd");
				cal.setTime(sdf.parse(date));
				int mo = cal.get(Calendar.MONTH);
				if ((mo+1)%3 == 2) {
					cal.add(Calendar.MONTH, -1);
				} else if ((mo+1)%3 == 0) {
					cal.add(Calendar.MONTH, -2);
				}
				cal.set(Calendar.DAY_OF_MONTH, 1);
				date = sdf1.format(cal.getTime());
				cal.add(Calendar.MONTH, 3);
				cal.add(Calendar.DATE, -1);
				endDate = sdf1.format(cal.getTime());
				date=date+"至"+endDate;
				break;
			case "2":
				sdf1=new SimpleDateFormat("yyyy-MM");
				date=sdf1.format(sdf.parse(date));
				break;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<Record> chartsList;
		StringBuffer selectSql = new StringBuffer();
		List<String> list = new ArrayList<>();
		String sum="";
		
		selectSql.append("select indexcode,sum(value) value  from ap_idop.dop_comb_detection t where czy=? and data_flag='1' group by indexcode order by value  desc ");

		chartsList = Db.use("gbase").find( selectSql.toString(), userNo);
		
		for (Record r : chartsList) {
			String indexcode=r.getStr("indexcode");
			Record record=Db.findFirst("select warning_code,warning_name from dop_warning_param where warning_code=?",indexcode);
			if(record!=null){
				 list.add(record.getStr("warning_code"));					
			}
		}
		
		String extraSql = " from (select lvl_2_branch_name branch_name, " ;
		for (int i = 0; i < list.size(); i++) {
			String code=list.get(i);
			String Sql="case indexcode when '"+code+"' then sum(value) else '0' end  "+code+",";
			extraSql +=Sql;
			sum+=code;
			if(i!=list.size()-1){
			sum+="+";
			}			 
		}		
		String sqls="select *";
		String gridSql="";
		
		StringBuffer whereSql = new StringBuffer("1".equals(jccj)?"lvl_4_branch_name org_name,i.bancsid from ap_idop.dop_comb_detection d ,ap_idop.sys_org_info i WHERE czy=? AND data_flag='1' and d.lvl_4_branch_no=i.orgnum group by branch_name,org_name,indexcode,value,bancsid ) t WHERE 1 = 1 group by branch_name,org_name,bancsid ":
			"lvl_3_branch_name org_name,i.bancsid from ap_idop.dop_comb_detection d ,ap_idop.sys_org_info i WHERE czy=? AND data_flag='1' and d.lvl_3_branch_no=i.orgnum group by branch_name,org_name,indexcode,value,bancsid ) t WHERE 1 = 1 group by branch_name,org_name,bancsid ");
		//表格查询语句
		gridSql = "from (select t.branch_name,'"+date+"' monitor_date,'"+remark.getStr("remark")+"' monitor_rate,t.org_name,sum("+sum+") count,bancsid org_no" ;
		whereSql.append(" order by count desc ) tt");
				
		List<String> sqlStr = new ArrayList<String>();
		
		// 排序
		extraSql += whereSql.toString();
		sqlStr.add(userNo);
		// 查询
		Page<Record> r = Db.use("gbase").paginate(pageNum, pageSize, sqls,gridSql+extraSql, sqlStr.toArray());
		// 赋值
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 打印日志
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		// 记录日志
//		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "监测参数配置", "3", "监测参数配置-查询");
//		log.info("监测参数配置-查询");
		// 返回json数据
		renderJson();

	}
	

	
		//根据查询条件查询某地区的基本信息
		public void getGegendData(){
			
			// 获取当前用户信息
			String userNo = getCurrentUser().getStr("USER_NO");
			// 获取查询条件
			String monitor_name=getPara("monitor_name");//监测条件名称
			String date=getPara("monitor_date");//监测时间
			if(AppUtils.StringUtil(date)!=null){
				date=date.substring(0, 10).replace("-", "");
			}
			String sql=" select * from dop_monitor_config where id=?";

			String whereSql1="select c.id,c.monitor_name,c.monitor_rate,c.monitor_type,c.monitor_level,c.monitor_method,c.monitor_state,c.createtime ," +
					"d.BUSINESS_TYPE,d.MARK_TYPE_CODE,i.remark,d.VALUE,d.QUESTION " +
					"from dop_monitor_config c,dop_monitor_config_detail d ,sys_param_info i where i.key='compare_operator' and c.id=d.CONFIGID and i.val=d.compare_relate and c.id=? and d.business_type='0' ";

			String whereSql2="select c.id,c.monitor_name,c.monitor_rate,c.monitor_type,c.monitor_level,c.monitor_method,c.monitor_state,c.createtime ," +
					"d.BUSINESS_TYPE,d.MARK_TYPE_CODE,i.remark,d.VALUE,d.QUESTION " +
					"from dop_monitor_config c,dop_monitor_config_detail d ,sys_param_info i where i.key='compare_operator' and c.id=d.CONFIGID and i.val=d.compare_relate and c.id=? and d.business_type='1' ";

			Record sub=Db.findFirst(sql,monitor_name);
			List<Record> records1=Db.find(whereSql1, monitor_name);
			List<Record> records2=Db.find(whereSql2, monitor_name);
			String[] a=new String[30]; 
			String b="";
			String c="";	
			for(int i=0;i<records1.size();i++){
				Record record=records1.get(i);
				String business_type=record.getStr("BUSINESS_TYPE");	
				String mark_type_code=record.getStr("MARK_TYPE_CODE");
				String compare_relate=record.getStr("remark");
				String value=record.getStr("VALUE");
				String question=record.getStr("QUESTION");
			
				b=business_type+"|"+mark_type_code+"|"+compare_relate+"|"+value+"|"+question+"|";
				a[i]=b;
			}
			
			for(int i=0;i<records2.size();i++){
				Record record=records2.get(i);
				String business_type=record.getStr("BUSINESS_TYPE");	
				String mark_type_code=record.getStr("MARK_TYPE_CODE");
				String compare_relate=record.getStr("remark");
				String value=record.getStr("VALUE");
			
				c=business_type+"|"+mark_type_code+"|"+compare_relate+"|"+value+"|";
				a[i+records1.size()]=c;
			}

			for(int j=(records2.size()+records1.size());j<a.length;j++){
				a[j]="";
			}
			
			String jcpl=sub.getStr("monitor_rate");
			String jclx=sub.getStr("monitor_type");
			String jccj=sub.getStr("monitor_level");
			
			String validSql = " CALL ap_idop.DPRO_COMB_DETECTION('"+ date + "','" + jcpl + "','" + jclx +"','" +jccj+ "','1','1'," +
					"'"+userNo+"','"+a[0]+"','"+a[1]+"','"+a[2]+"','"+a[3]+"','"+a[4]+"','"+a[5]+"','"+a[6]+"','"+a[7]+"','"+a[8]+"','"+a[9]+"','"+a[10]+"','"+a[11]+"','"+a[12]+"','"+a[13]+"','"+a[14]+"','"+a[15]+"','"+a[16]+"','"+a[17]+"','"+a[18]+"','"+a[19]+"','"+a[20]+"','"+a[21]+"','"+a[22]+"','"+a[23]+"','"+a[24]+"','"+a[25]+"','"+a[26]+"','"+a[27]+"','"+a[28]+"','"+a[29]+"',@a,@b)";
//			System.out.println(validSql);
//			System.out.println("end");
			
			Db.use("gbase").find(validSql);
		
			List<Record> chartsList1;
			List<Record> chartsList2;
			List<Record> chartsList4;
			List<Record> chartsList5;
			//legend
			List<String> legendList1 = new ArrayList<>();
			//series
			List<Record> seriesList1 = new ArrayList<>();
			//xAxis 
			List<String> xAxisList1 = new ArrayList<>();
			StringBuffer selectSql1 = new StringBuffer();
			
			selectSql1.append("select indexcode,sum(value) value  from ap_idop.dop_comb_detection t where czy=? and data_flag='1' group by indexcode order by value  desc ");
			chartsList1 = Db.use("gbase").find( selectSql1.toString(), userNo);
			
			if(chartsList1.isEmpty()){
				
				String text="无符合当期监测条件数据";
				Map<String,String> map  = new HashMap<String,String>();
				map.put("text", text);
				renderJson(map);
				
			}else{
				
				//柱状图1
				Record serie1 ;
				List<String> values1 = new ArrayList<>();
				values1 = new ArrayList<>();
				serie1 = new Record();
				
				for(int i=0;i<chartsList1.size();i++){
					Record r=chartsList1.get(i);
					String indexcode=r.getStr("indexcode");
					Record remark=Db.findFirst("select warning_name from dop_warning_param where warning_code=?",indexcode);
					if(remark!=null){
						xAxisList1.add("预警"+(i+1));
						String value=r.getDouble("value")+"";
						values1.add(value.substring(0,value.indexOf('.')));
					}
					
				}
				legendList1.add("数量分布");
				serie1.set("type", "bar");
				serie1.set("data", values1);
				seriesList1.add(serie1);
				
				setAttr("xAxis1", xAxisList1);
				setAttr("legend1", legendList1);
				setAttr("series1", seriesList1);
				setAttr("chartsList1", chartsList1);
				
				
				//legend
				List<String> legendList2 = new ArrayList<>();
				//series
				List<Record> seriesList2 = new ArrayList<>();
				//xAxis 
				List<String> xAxisList2 = new ArrayList<>();				
				StringBuffer selectSql2 = new StringBuffer();
				
				selectSql2.append("1".equals(jccj)?"select lvl_2_branch_name,count(lvl_2_branch_name) value from (select lvl_2_branch_name,lvl_4_branch_name from ap_idop.dop_comb_detection t where czy=? and data_flag='1' group by lvl_2_branch_name,lvl_4_branch_name order by lvl_2_branch_name) t group by lvl_2_branch_name order by value":
					"select lvl_2_branch_name,count(lvl_2_branch_name) value from (select lvl_2_branch_name,lvl_3_branch_name from ap_idop.dop_comb_detection t where czy=? and data_flag='1' group by lvl_2_branch_name,lvl_3_branch_name order by lvl_2_branch_name) t group by lvl_2_branch_name order by value");
				
				chartsList2 = Db.use("gbase").find( selectSql2.toString(), userNo);
				//饼图
				for (Record r1 : chartsList2) {
					seriesList2.add(new Record().set("name", r1.getStr("lvl_2_branch_name")).set("value", r1.getLong("value")+""));
					
					legendList2.add(r1.getStr("lvl_2_branch_name"));	
				}

				setAttr("xAxis2", xAxisList2);
				setAttr("legend2", legendList2);
				setAttr("series2", seriesList2);
				setAttr("chartsList2", chartsList2);
				

				//legend
				List<String> legendList4 = new ArrayList<>();
				//series
				List<Record> seriesList4 = new ArrayList<>();
				//xAxis 
				List<String> xAxisList4 = new ArrayList<>();
				String selectSql4 = new String();
				List<String> list = new ArrayList<>();
				String sum="";
				String s="";
				for (Record r : chartsList1) {
					String indexcode=r.getStr("indexcode");
					
					Record record4=Db.findFirst("select warning_code,warning_name from dop_warning_param where warning_code=?",indexcode);
					if(record4!=null){					
						list.add(record4.getStr("warning_code"));						
					}							
				}
				String ssql1="1".equals(jccj)?"select lvl_4_branch_no branch_no,":"select lvl_3_branch_no branch_no,";
				for (int i = 0; i < list.size(); i++) {
					String code=list.get(i);
					String Sql="case indexcode when '"+code+"' then sum(value) else '0' end  "+code+",";
					ssql1+=Sql;
					sum+=code;
					s+="sum("+code+") "+code;
					if(i!=list.size()-1){
					sum+="+";
					s+=",";
					}			 
				}	
				String fsql1="1".equals(jccj)?"lvl_4_branch_name branch_name from ap_idop.dop_comb_detection  WHERE czy=? AND data_flag='1' group by branch_no,branch_name,indexcode,value":
					"lvl_3_branch_name branch_name from ap_idop.dop_comb_detection  WHERE czy=? AND data_flag='1' group by branch_no,branch_name,indexcode,value";
				ssql1+=fsql1;
				
				selectSql4="select t.branch_no,t.branch_name,"+s+",sum("+sum+") count  from ("+ssql1+") t WHERE 1 = 1 group by branch_no,branch_name order by count desc limit 0,10";				
				chartsList4 = Db.use("gbase").find( selectSql4.toString(), userNo);
	
				for (int i = 0; i < list.size(); i++) {
					List<String> values4 = new ArrayList<String>();
					Record  serie4 = new Record();
					String indexcode=list.get(i);
					Record re4=Db.findFirst("select warning_code,warning_name from dop_warning_param where warning_code=?",indexcode);
					if(!legendList4.contains(re4.getStr("warning_name"))){
						legendList4.add(re4.getStr("warning_name"));
					}
					for (Record r1 : chartsList4) {
						
						String value=r1.getDouble(indexcode)+"";
						values4.add(value.substring(0,value.indexOf('.')));
						
						if(!xAxisList4.contains(r1.getStr("branch_name"))){						
							xAxisList4.add(r1.getStr("branch_name"));
						}
					}
					serie4.set("name", re4.getStr("warning_name"));
					serie4.set("type", "bar");
					serie4.set("data", values4);
					seriesList4.add(serie4);
				}
				
				setAttr("chartsList4", chartsList4);			
				setAttr("xAxis4", xAxisList4);
				setAttr("legend4", legendList4);
				setAttr("series4", seriesList4);
				
				
				
				//legend
				List<String> legendList5 = new ArrayList<>();
				//series
				List<Record> seriesList5 = new ArrayList<>();
				List<String> xaixs5 = new ArrayList<>();				
				StringBuffer selectSql5 = new StringBuffer();			
				
				selectSql5.append("1".equals(jccj)?"select t.data_flag,count(*) value from (select lvl_4_branch_name,data_flag from ap_idop.dop_comb_detection  where czy=? group by data_flag, lvl_4_branch_name) t group by t.data_flag order by t.data_flag desc ":
					"select t.data_flag,count(*) value from (select lvl_3_branch_name,data_flag from ap_idop.dop_comb_detection  where czy=? group by data_flag, lvl_3_branch_name) t group by t.data_flag order by t.data_flag desc ");
				chartsList5 = Db.use("gbase").find( selectSql5.toString(), userNo);
				//折线图
				Record r5;
				List<String> values5;
				r5 = new Record();
				values5 = new ArrayList<>();
				for (Record r : chartsList5) {
									
					values5.add(r.getLong("value")+"");
					
					if("3".equals(r.getStr("data_flag"))){
						xaixs5.add("上上期");
					}
					if("2".equals(r.getStr("data_flag"))){
						xaixs5.add("上期");
					}
					if("1".equals(r.getStr("data_flag"))){
						xaixs5.add("本期");
					}					
				}
				r5.set("type", "line");
				r5.set("data",values5);
				seriesList5.add(r5);
				// 赋值
				setAttr("xAxis5", xaixs5);
				setAttr("legend5", legendList5);
				setAttr("series5", seriesList5);
				setAttr("chartsList5", chartsList5);
				
				// 记录日志
				log.info("");
				renderJson();				
				
			}
						
		}
		
		//监测详情
		public void getOrg(){
			
			int pageNum = getParaToInt("pageIndex") + 1;
			int pageSize = getParaToInt("pageSize", 10);
			// 获取当前用户信息
			String userNo = getCurrentUser().getStr("USER_NO");
			String monitor_name=getPara("monitor_name");
			String jccj = Db.findFirst("select monitor_level from dop_monitor_config where id = ?", monitor_name).getStr("monitor_level");
			List<Record> chartsList;
			StringBuffer selectSql = new StringBuffer();
			List<String> list = new ArrayList<>();
			String sum="";
			
			selectSql.append("select indexcode,sum(value) value  from ap_idop.dop_comb_detection t where czy=? and data_flag='1' group by indexcode order by value  desc ");

			chartsList = Db.use("gbase").find( selectSql.toString(), userNo);
			
			for (Record r : chartsList) {
				String indexcode=r.getStr("indexcode");
				Record record=Db.findFirst("select warning_code,warning_name from dop_warning_param where warning_code=?",indexcode);
				if(record!=null){
					 list.add(record.getStr("warning_code"));					
				}
			}
			
			String ssql1 = "1".equals(jccj) ? " select lvl_2_branch_name branch_name,lvl_4_branch_name org_name, " :  " select lvl_2_branch_name branch_name,lvl_3_branch_name org_name, ";
			for (int i = 0; i < list.size(); i++) {
				String code=list.get(i);
				String Sql="case indexcode when '"+code+"' then sum(value) else '0' end  "+code+",";
				ssql1 +=Sql;
				sum+=code;
				if(i!=list.size()-1){
				sum+="+";
				}			 
			}
			String fsql1= "1".equals(jccj) ? "data_flag,i.bancsid  from ap_idop.dop_comb_detection d,ap_idop.sys_org_info i WHERE czy=? and d.lvl_4_branch_no=i.orgnum group by branch_name,org_name,indexcode,value,data_flag,bancsid" : 
				"data_flag,i.bancsid  from ap_idop.dop_comb_detection d,ap_idop.sys_org_info i WHERE czy=? and d.lvl_3_branch_no=i.orgnum group by branch_name,org_name,indexcode,value,data_flag,bancsid";
			ssql1+=fsql1;
			String sql2="";
			String sql3="";
			String extraSql="";
			StringBuffer whereSql = new StringBuffer("");
			
			sql2="select t.branch_name,t.org_name,t.bancsid, " +
				 "case data_flag when '1' then sum("+sum+") else '0' end flag1," +
				 "case data_flag when '2' then sum("+sum+") else '0' end flag2," +
				 "case data_flag when '3' then sum("+sum+") else '0' end flag3 from (" +ssql1+
				 " ) t group by t.branch_name,t.org_name,t.data_flag,t.bancsid";
			sql3="select tt.branch_name,tt.org_name,tt.bancsid org_no,sum(tt.flag1) flag1,sum(tt.flag2) flag2,sum(tt.flag3) flag3 from (" +sql2+
				 ") tt group by tt.branch_name,tt.org_name,tt.bancsid ";
			extraSql ="from ("+sql3+")ttt where 1=1 and ttt.flag1>0 and ttt.flag2>0 and ttt.flag3>0";
			whereSql.append(" order by flag1 desc ");
			
			// 查询语句
			String gridSql = "select *  " ;
			List<String> sqlStr = new ArrayList<String>();
			
			// 排序		
			extraSql += whereSql.toString();
			sqlStr.add(userNo);
			// 查询
			Page<Record> r = Db.use("gbase").paginate(pageNum, pageSize, gridSql,extraSql, sqlStr.toArray());
				
			// 赋值
			setAttr("data", r.getList());
			setAttr("total", r.getTotalRow());
			// 打印日志
			log.info("getList--r.getList():" + r.getList());
			log.info("getList--r.getTotalRow():" + r.getTotalRow());
			// 返回json数据
			renderJson();

		}
		
		
		/**
		 * 查询指标名称下拉列表
		 */
		public void getMarkParamList(){
			
			String sub_busi_code=getPara("sub_busi_code");
			String monitor_type=getPara("monitor_type");
			
			if(AppUtils.StringUtil(sub_busi_code)!=null ){
				Record sub=Db.findFirst("select id,type,remark,val from DOP_GANGEDMENU_INFO where id=?",new Object[]{sub_busi_code});//获取业务类型
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
			if(AppUtils.StringUtil(monitor_type) != null){
				if (monitor_type.equals("1")) {//1表示监测类型为人员监测，仅查询人员维度指标
					whereSql.append(" AND  mark_dimension = '1' ");
				} else {//其他情况（机构监测、业务监测）时，人员和机构维度都查
					whereSql.append(" AND  mark_dimension in ('0','1') ");
				}
			}
			
			whereSql.append("  and is_key_mark = ? ");
			sqlStr.add("1");
			
			whereSql.append("  and total_type is not null  ");
			whereSql.append("  order by mark_code ");
			sql+=whereSql.toString();

			List<Record>list=Db.find(sql,sqlStr.toArray());
			setAttr("data",list);
			renderJson();
		}
		
		/**
		 * 预警名称下拉列表
		 */
		public void getWarnNameList(){
			String warnType = getPara("val");
			String monitorType = getPara("monitor_type");
			String sql="";
			if (AppUtils.StringUtil(monitorType) != null) {
				if ("1".equals(monitorType)) {
					sql="select t.warning_name,t.warning_code from dop_warning_param  t where t.warning_type_code=(select val from dop_gangedmenu_info t where t.id = "+"'"+warnType+"') and (t.is_use is  null or t.is_use = '1') and t.WARNING_DIMENSION='1' order by t.warning_name asc";
				} else {
					sql="select t.warning_name,t.warning_code from dop_warning_param  t where t.warning_type_code=(select val from dop_gangedmenu_info t where t.id = "+"'"+warnType+"') and (t.is_use is  null or t.is_use = '1') order by t.warning_name asc";
				}
			}
			List<Record> list=Db.find(sql);
			setAttr("data",list);
			renderJson();
		}
		

		public void getDetail(){
			// 获取当前用户信息
			String userNo = getCurrentUser().getStr("USER_NO");
			int pageNum = getParaToInt("pageIndex") + 1;
			int pageSize = getParaToInt("pageSize", 10);
			
			String sql="select c.id,c.monitor_name,c.monitor_rate,c.monitor_type,c.monitor_level,c.monitor_method,c.monitor_state,c.createtime ," +
					"d.BUSINESS_TYPE,d.MARK_TYPE_CODE,i.remark,d.VALUE,d.QUESTION " +
					"from dop_monitor_config c,dop_monitor_config_detail d ,sys_param_info i where i.key='compare_operator' and c.id=d.CONFIGID and i.val=d.compare_relate and c.id=?";
			
			String monitor_name=getPara("monitor_name");
			String date=getPara("monitor_date");
			
			date=date.substring(0, 10).replace("-", "");
			
			Record sub=Db.findFirst(sql,monitor_name);			
			String jcpl=sub.getStr("monitor_rate");			
			String jccj = sub.getStr("monitor_level");
			Record remark = Db.findFirst("select remark from SYS_PARAM_INFO where key = 'monitor_rate' and val=? ", jcpl);
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdf1= null;
			Calendar cal = Calendar.getInstance();
			String endDate = "";
			try {
				switch(jcpl) {
				case "0":
					sdf1=new SimpleDateFormat("yyyy-MM-dd");
					date=sdf1.format(sdf.parse(date));
					break;
				case "1":
					sdf1=new SimpleDateFormat("yyyy-MM-dd");
					cal.setTime(sdf.parse(date));
					int interval = cal.get(Calendar.DAY_OF_WEEK);
					if (interval == 1) {
						cal.add(Calendar.DATE, -6);
					} else {
						cal.add(Calendar.DATE, -interval+2);
					}
					date = sdf1.format(cal.getTime());
					cal.add(Calendar.DATE, 6);
					endDate = sdf1.format(cal.getTime());
					date=date+"至"+endDate;
					break;
				case "3":
					sdf1=new SimpleDateFormat("yyyy-MM-dd");
					cal.setTime(sdf.parse(date));
					int mo = cal.get(Calendar.MONTH);
					if ((mo+1)%3 == 2) {
						cal.add(Calendar.MONTH, -1);
					} else if ((mo+1)%3 == 0) {
						cal.add(Calendar.MONTH, -2);
					}
					cal.set(Calendar.DAY_OF_MONTH, 1);
					date = sdf1.format(cal.getTime());
					cal.add(Calendar.MONTH, 3);
					cal.add(Calendar.DATE, -1);
					endDate = sdf1.format(cal.getTime());
					date=date+"至"+endDate;
					break;
				case "2":
					sdf1=new SimpleDateFormat("yyyy-MM");
					date=sdf1.format(sdf.parse(date));
					break;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			queryField();
			
			List<Record> headers =getAttr("headers");
			String fieldstr = "";
			String headerstr="";
			for(int i=0;i<headers.size();i++){
				fieldstr+="case indexcode when '"+ headers.get(i).getStr("field")+"' then sum(value) else '0' end "+headers.get(i).getStr("header")+",";
				headerstr+="sum(t."+headers.get(i).getStr("header")+") "+headers.get(i).getStr("field")+",";
			}
			
			List<Record> chartsList;
			StringBuffer selectSql = new StringBuffer();
			List<String> list = new ArrayList<>();
			String sum="";
			
			selectSql.append("select indexcode,sum(value) value  from ap_idop.dop_comb_detection t where czy=? and data_flag='1' group by indexcode order by value  desc ");

			chartsList = Db.use("gbase").find( selectSql.toString(), userNo);
			
			for (Record r : chartsList) {
				String indexcode=r.getStr("indexcode");
				Record record=Db.findFirst("select warning_code,warning_name from dop_warning_param where warning_code=?",indexcode);
				if(record!=null){
					 list.add(record.getStr("warning_code"));					
				}
			}
			
			String extraSql = " from (select lvl_2_branch_name branch_name, " ;
			for (int i = 0; i < list.size(); i++) {
				String code=list.get(i);
				String Sql="case indexcode when '"+code+"' then sum(value) else '0' end  "+code+",";
				extraSql +=Sql;
				sum+=code;
				if(i!=list.size()-1){
				sum+="+";
				}			 
			}		
			String sqls="select *";
			String gridSql="";
			StringBuffer whereSql = new StringBuffer(fieldstr+("1".equals(jccj)?"lvl_4_branch_name org_name,i.bancsid from ap_idop.dop_comb_detection d,ap_idop.sys_org_info i WHERE czy=? AND data_flag='1' and d.lvl_4_branch_no=i.orgnum group by branch_name,org_name,indexcode,value,bancsid ) t WHERE 1 = 1 group by branch_name,org_name ,bancsid":
				"lvl_3_branch_name org_name,i.bancsid from ap_idop.dop_comb_detection d,ap_idop.sys_org_info i WHERE czy=? AND data_flag='1' and d.lvl_3_branch_no=i.orgnum group by branch_name,org_name,indexcode,value,bancsid ) t WHERE 1 = 1 group by branch_name,org_name ,bancsid"));

			//表格查询语句	
			gridSql = " from (select t.branch_name,'"+date+"' monitor_date,'"+remark.getStr("remark")+"' monitor_rate,"+headerstr+"t.org_name,sum("+sum+") count,t.bancsid org_no" ;
			whereSql.append(" order by count desc ) tt ");
					
			List<String> sqlStr = new ArrayList<String>();
			
			// 排序
			
			extraSql += whereSql.toString();
			sqlStr.add(userNo);
			// 查询
			Page<Record> r = Db.use("gbase").paginate(pageNum, pageSize, sqls,gridSql+extraSql,sqlStr.toArray());
			// 赋值
			setAttr("data", r.getList());
			setAttr("total", r.getTotalRow());
			renderJson();
		}
		
		//根据主键，获取主键对应监测详细数据
		public void getDetailById(){
			String id = getPara("key");
			
			/* 获取id对应监测信息*/
			String sql = "select id, monitor_name, monitor_rate, monitor_type, monitor_level, monitor_method, monitor_state from dop_monitor_config where id=?";
			//获取主表数据
			Record re = Db.findFirst(sql, id);	
			
			//获取条件数据
			String sql1 = "select busi_module biz_module_target, value num_target, compare_relate operator_target, sub_busi_code biz_type, mark_type_code target from dop_monitor_config_detail where configid = ? and business_type = '1'";
			String sql2 = "select busi_module biz_module_warning, value num_warning, compare_relate operator_warning, sub_busi_code warning_type, mark_type_code warning_name, question from dop_monitor_config_detail where configid = ? and business_type = '0'";
			List<Record> tList = Db.find(sql1, re.get("id"));	
			List<Record> wList = Db.find(sql2, re.get("id"));
			setAttr("record", re);
			setAttr("tList", tList);
			setAttr("wList", wList);
			setAttr("tSize", tList.size());
			setAttr("wSize", wList.size());
			// 记录日志
			log.info("监测说明");
			renderJson();
		}
		

		
		public void queryField(){
			String monitor_name = getPara("monitor_name");
			
			// 获取查询条件
			List<Record> yjSql=Db.find("select dwp.warning_code,dwp.warning_name from dop_warning_param  dwp where dwp.warning_code in (select dmcd.mark_type_code from dop_monitor_config_detail dmcd where dmcd.configid=?)",monitor_name);
			List<Record> zbSql=Db.find("select dmp.mark_code,dmp.mark_name from dop_mark_param dmp where dmp.mark_code in (select dmcd.mark_type_code from dop_monitor_config_detail dmcd where dmcd.configid=?)",monitor_name);
			
			List<Record> headers = new ArrayList<>();
			for (Record record : yjSql) {
				headers.add(new Record().set("header", record.get("warning_name")).set("field",record.get("warning_code")).set("headerAlign", "center").set("align", "center").set("visible", "true"));
			}
			for (Record record : zbSql) {
				headers.add(new Record().set("header", record.get("mark_name")).set("field",record.get("mark_code")).set("headerAlign", "center").set("align", "center").set("visible", "true"));
			}
			
			setAttr("headers",headers);	
			renderJson(headers);
		}
		
		//下载
		public void download(){
			
			getDetail();
			List<Record> list = getAttr("data");
			
			queryField();
			List<Record> headers =getAttr("headers");
			// 指定用哪些查询出来的字段填充excel文件
			String[] eheaders = new String[headers.size()+6];//{"create_time","indicator_code","lvl_4_branch_name","indicator_value"};//getPara("execlheaders").split(",");
			// excel文件的每列的名称,顺序与填充字段的顺序保持一致
			String[] columns = new String[headers.size()+6];//{"预警时间","一级明细名称","机构名称","值"};//getPara("execlcolumns").split(",");;
			eheaders[0]="branch_name";
			columns[0]="分行名称";
			eheaders[1]="org_no";
			columns[1]="机构号";
			eheaders[2]="org_name";
			columns[2]="机构名称";
			eheaders[3]="monitor_date";
			columns[3]="监测时间";
			eheaders[4]="monitor_rate";
			columns[4]="监测频率";
			eheaders[5]="count";
			columns[5]="数量";
			
			for(int i=0;i<headers.size();i++){
				columns[i+6]=headers.get(i).getStr("header");
				eheaders[i+6]=headers.get(i).getStr("field");

			}	
			String fileName = "";
			try {
				fileName = new String(("机构监测详细.xls").getBytes("GB2312"), "ISO_8859_1");
			} catch (UnsupportedEncodingException e) {
				log.error("orgMonitorCtl-download", e);
			}
			// 转换成excel
			ExcelRender er = new ExcelRender(fileName, columns, eheaders, list, getResponse());
			er.render();
			renderNull();
			// 打印日志
		}
}
