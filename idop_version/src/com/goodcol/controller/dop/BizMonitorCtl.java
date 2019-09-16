package com.goodcol.controller.dop;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;

@RouteBind(path = "/bizMonitor")
@Before({ManagerPowerInterceptor.class})
public class BizMonitorCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(BizMonitorCtl.class);
	
	@Override
	public void index() {
		render("index.jsp");
	}
	
	/**
	 * 获取监测名称下拉列表数据
	 */
	public void getMonitorList() {
		String sql = "select id, monitor_name from dop_monitor_config where monitor_name is not null and monitor_type = '2'";
		List<Record> list = Db.find(sql);
		setAttr("data", list);
		renderJson();
	}
	
	public void getData() {
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
		String date = getPara("date").substring(0, 10).replace("-", "");//监测时间
		String id = getPara("id");
		Record r = Db.findById("dop_monitor_config", id);
		String sql = "select t.id,                                                          "+
			"	       business_type || '|' || mark_type_code || '|' || remark || '|' ||    "+
			"	       value || '|' || (case business_type                                  "+
			"	         when '0' then                                                      "+
			"	          question || '|'                                                   "+
			"	         when '1' then                                                      "+
			"	          '|'                                                               "+
			"	       end) c                                                               "+
			"	  from dop_monitor_config_detail t                                          "+
			"	  join sys_param_info p                                                     "+
			"	    on t.compare_relate = p.val                                             "+
			"	 where p.key = 'compare_operator'                                           "+
			"	   and t.configid = ?";
		List<Record> rs = Db.find(sql, id);
		Object[] paras = new Object[37];
		paras[0] = date;
		paras[1] = r.getStr("monitor_rate");
		paras[2] = r.getStr("monitor_type");
		paras[3] = r.getStr("monitor_level");
		paras[4] = r.getStr("monitor_method");
		paras[5] = r.getStr("monitor_state");
		paras[6] = userNo;
		for (int i = 7; i <= 36; i++) {
			if (i-7>=rs.size()) {
				paras[i] = "";
			} else {
				paras[i] = rs.get(i-7).getStr("c");
			}
		}
		paras[3] = "";
		Db.use("gbase").find("CALL ap_idop.DPRO_COMB_DETECTION_BUSINESS('"+ paras[0] + "','" + paras[1] + "','" + paras[2] +"','" +paras[3]+ "','"+paras[4]+"','"+paras[5]+"','"+paras[6]+"','"+paras[7]+"','"+paras[8]+"','"+paras[9]+"','"+paras[10]+"','"+paras[11]+"','"+paras[12]+"','"+paras[13]+"','"+paras[14]+"','"+paras[15]+"','"+paras[16]+"','"+paras[17]+"','"+paras[18]+"','"+paras[19]+"','"+paras[20]+"','"+paras[21]+"','"+paras[22]+"','"+paras[23]+"','"+paras[24]+"','"+paras[25]+"','"+paras[26]+"','"+paras[27]+"','"+paras[28]+"','"+paras[29]+"','"+paras[30]+"','"+paras[31]+"','"+paras[32]+"','"+paras[33]+"','"+paras[34]+"','"+paras[35]+"','"+paras[36]+"',@a,@b)");
		List<Record> rList = Db.use("gbase").find("select indexcode, sum(value) amount from ap_idop.dop_comb_detection_business where czy = ? and data_flag = 1 group by indexcode order by amount desc", userNo);
		if(rList.isEmpty()){
			
			String text="无符合当期监测条件数据";
			Map<String,String> map  = new HashMap<String,String>();
			map.put("text", text);
			renderJson(map);
			
		}else{
		
		
		List<Record> rList1 = Db.use("gbase").find("select lvl_2_branch_name, sum(value) amount from ap_idop.dop_comb_detection_business where czy = ? and data_flag = 1 group by lvl_2_branch_name order by amount", userNo);
		List<String> items = new ArrayList<String>();
		List<String> items1 = new ArrayList<String>();
		List<Double> vals = new ArrayList<Double>();
		List<Record> vals1 = new ArrayList<Record>();
		List<Record> vals2 = new ArrayList<Record>();
		for(int i=0;i<rList.size();i++){
			Record rd=rList.get(i);
			Record findFirst = Db.findFirst("select warning_name from dop_warning_param where warning_code = ?", rd.getStr("indexcode"));
			if (findFirst != null) {
				String warnName = findFirst.getStr("warning_name");
				items.add("预警"+(i+1));
				vals.add(rd.getDouble("amount"));
				Record temp = new Record();
				temp.set("name", warnName).set("value", rd.getDouble("amount"));
				vals2.add(temp);
			}
			
		}
		for (Record rd : rList1) {
			items1.add(rd.getStr("lvl_2_branch_name"));
			Record temp = new Record();
			temp.set("name", rd.get("lvl_2_branch_name")).set("value", rd.getDouble("amount"));
			vals1.add(temp);
		}
		setAttr("items", items);
		setAttr("items1", items1);
		setAttr("vals", vals);
		setAttr("vals1", vals1);
		setAttr("vals2", vals2);
		renderJson();
		}
	}
	
	public void getList() {
		String userNo = getCurrentUser().getStr("USER_NO");
		String id = getPara("id");
		String date = getPara("date").substring(0, 10);
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		Record r = Db.findFirst("select remark mr from sys_param_info where key = 'monitor_rate' and val = (select monitor_rate from dop_monitor_config where id = ?)", id);
		String monitorRate = r.getStr("mr");
		SimpleDateFormat sdf1= null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String endDate = "";
		try {
			switch(monitorRate) {
			case "日":
				break;
			case "周":
				cal.setTime(sdf.parse(date));
				int interval = cal.get(Calendar.DAY_OF_WEEK);
				if (interval == 1) {
					cal.add(Calendar.DATE, -6);
				} else {
					cal.add(Calendar.DATE, -interval+2);
				}
				date = sdf.format(cal.getTime());
				cal.add(Calendar.DATE, 6);
				endDate = sdf.format(cal.getTime());
				date=date+"至"+endDate;
				break;
			case "季":
				cal.setTime(sdf.parse(date));
				int mo = cal.get(Calendar.MONTH);
				if ((mo+1)%3 == 2) {
					cal.add(Calendar.MONTH, -1);
				} else if ((mo+1)%3 == 0) {
					cal.add(Calendar.MONTH, -2);
				}
				cal.set(Calendar.DAY_OF_MONTH, 1);
				date = sdf.format(cal.getTime());
				cal.add(Calendar.MONTH, 3);
				cal.add(Calendar.DATE, -1);
				endDate = sdf.format(cal.getTime());
				date=date+"至"+endDate;
				break;
			case "月":
				sdf1=new SimpleDateFormat("yyyy-MM");
				date=sdf1.format(sdf.parse(date));
				break;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String selectSql = "select module, sum(value) warnnum, '"+monitorRate+"' md , indexcode, '"+ date + "' d ";
		String extraSql = "from ap_idop.dop_comb_detection_business where czy = ? and data_flag = 1 and (indexcode like '__901%' or indexcode like '__902%') group by indexcode, module order by indexcode";
		List<String> paras = new ArrayList<String>();
		paras.add(userNo);
		Page<Record> page = Db.use("gbase").paginate(pageNum, pageSize, selectSql, extraSql, paras.toArray());
		for(Record rd : page.getList()) {
			Record rr = Db.findFirst("select t.remark module, p.warning_name from dop_warning_param p left join sys_param_info t on p.busi_module = t.val where warning_code = ? and t.key = 'dop_ywtype'", rd.getStr("indexcode"));
			rd.set("warnname", rr.getStr("warning_name"));
			rd.set("module", rr.getStr("module"));
		}
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}

	//下载
	public void download(){
		getList();
		List<Record> list = getAttr("data");
		// 指定用哪些查询出来的字段填充excel文件
		String[] eheaders = new String[6];//{"create_time","indicator_code","lvl_4_branch_name","indicator_value"};//getPara("execlheaders").split(",");
		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = new String[6];//{"预警时间","一级明细名称","机构名称","值"};//getPara("execlcolumns").split(",");;
		eheaders[0]="module";
		columns[0]="业务模块";
		eheaders[1]="warnname";
		columns[1]="预警名称";
		eheaders[2]="d";
		columns[2]="监测时间";
		eheaders[3]="md";
		columns[3]="监测频率";
		eheaders[4]="warnnum";
		columns[4]="预警数量";
		eheaders[5]="warnnum";
		columns[5]="问题数量";
		String fileName = "";
		try {
			fileName = new String(("监测详细.xls").getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("PersonMonitorCtl-download", e);
		}
		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, eheaders, list, getResponse());
		er.render();
		renderNull();
		// 打印日志
	}
}
