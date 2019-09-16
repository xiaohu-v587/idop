package com.goodcol.controller;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

import com.goodcol.controller.dop.WarningCtl;
import com.goodcol.core.log.Logger;
import com.goodcol.util.AppUtils;

import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;


@RouteBind(path="exportSearch")
public class ExportSearchCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(WarningCtl.class);
	@Override
	public void index() {
		// TODO Auto-generated method stub
     render("index.jsp");
	}
 
	//列表数据查询
	public void getTableList(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
	
	
		String ehrCode = getCurrentUser().getStr("USER_NO");
		//机构查询
		String sql;
		sql = "call ap_idop.DPRO_DOWNLOAD_JOB_DISPLAY('"+ehrCode+"',@a,@b)";
		List<Record> list=Db.use("gbase").find(sql);
		List<Record> list1 = new ArrayList<Record>();
		int data=0;//判断此页数据从哪个值开始
		int page = 0;
		if(list.size()%pageSize==0){
			page = list.size()/pageSize;
		}else{
			page = list.size()/pageSize+1;
		}
		
		data =pageSize*(pageNum-1);
			if(pageSize>list.size()){
				list1.addAll(list);
			}else{
				if(pageNum==page&&list.size()%pageSize!=0){//最后一页，且数据量小于pageSize
					for(int i=0;i<list.size()%pageSize;i++){
						list1.add(list.get(data+i));
					}
				}else{//
					for(int i=0;i<pageSize;i++){
						list1.add(list.get(data+i));
					}
				}
				
			}

		setAttr("data",list1);
		setAttr("total",list.size());
		//setAttr("total",list.getTotalRow());
		// 打印日志
		log.info("getList--列表信息:" + list);
		renderJson();
	}
	
	//创建任务 
	public void createTask(){
	/*	 前台需提供参数详解如下：
		    * ehrCode: EHR号，请传入用户登录时使用的7位员工号
		    * SysID: 系统ID，关联ddp_para下的FTP_DEFINE；固定传入ap_idop.ftp_define中sys_name = 'idop'的sys_id；
		    * UserFlg: 用户定义的标志，用于区分一个系统id中的不同用户；固定为idop
		    * JobExplain: 任务说明；即下载附言，如前台不向用户开放附言结构，可传空串
		    * SqlTableId: 查询表Id；即ap_idop.idop_download_tableconfig的table_id，与前台用户选择相关
		    * SqlBeginDate: 查询时间段开始时间；yyyymmdd，与前台用户选择相关
		    * SqlEndDate: 查询时间段结束时间；yyyymmdd，与前台用户选择相关
		    * SqlBranchNo: 查询数据机构；9位机构号，与前台用户选择相关
		    * PrioLevelOther: 对方过来的优先级 0-9；固定为5
		    * IsHaveDWFile: 是否有下传文件 Y/N；固定为Y
		    * IsCompress: 传输给对方的文件是否压缩 Y/N；如前台不向用户开放选择接口，建议直接固定为Y，压缩格式默认为.gz*/
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String org = getPara("orgid");//机构
		String module = getPara("module");//模块
		String table_id = getPara("table_id");//table_id
		String begindate = getPara("begindate");//开始时间
		String enddate = getPara("enddate");//结束时间
		try {
			begindate=sdf.format(sdf1.parse(begindate));
			enddate=sdf.format(sdf1.parse(enddate));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String ehrCode = getCurrentUser().getStr("USER_NO");
		
		//String sysid=getCurrentUser().getStr("id");
		
		//String getSysName="select sys_name ";
		
		String sql;
		String flag="true";
		List<Record> list=new ArrayList<Record>();
		try {
			sql="call ap_idop.DPRO_DOWNLOAD_QUERY_JOB('"+ehrCode+"', 7, 'idop', 'test_idop', '"+table_id+"', '"+begindate+"', '"+enddate+"', '"+org+"', 5, 'Y', 'Y', @code, @msg)";
		    Db.use("gbase").update(sql);
		} catch (Exception e) {
			flag="false";
			log.info("getList--列表信息:" + list);
			// TODO: handle exception
		}
		setAttr("flag",flag);
		renderJson(flag);
	}
	//查询表名
	public void getTableName(){
		String val=getPara("val");
		/*String conditional=getPara("conditional");
		 and cond_type= '"+conditional+"'*/
		String sql="";
		if(AppUtils.StringUtil(val) != null){
			 sql="select table_id ,table_name,table_desc from ap_idop.idop_download_tableconfig t where 1=1 and  module= '"+val+"' order by table_id";
		}else{
			sql="select table_id ,table_name,table_desc from ap_idop.idop_download_tableconfig t ";
		}
		List<Record> rList = Db.use("gbase").find(sql);
		// 赋值
		JSONArray array = JSONArray.fromObject(rList);
		String jsonStr = "";
		for (int i = 0; i < array.size(); i++) {
			String columns = array.getJSONObject(i).getString("columns");
			if (i == 0) {
				jsonStr = columns;
			} else if (i > 0) {
				jsonStr = columns + "," + jsonStr;
			}
		}
		jsonStr = "[ " + jsonStr + " ]";
		renderJson(jsonStr);
      log.info("getList--r):" + rList);
	}
	
	//获取业务模块
	public void getDict(){
		String key = getPara("key");
		List<Record> list = Db.find("select val,remark from SYS_PARAM_INFO where key = ? order by sortnum", key);
		JSONArray array = JSONArray.fromObject(list);
		String jsonStr = "";
		for (int i = 0; i < array.size(); i++) {
			String columns = array.getJSONObject(i).getString("columns");
			if (i == 0) {
				jsonStr = columns;
			} else if (i > 0) {
				jsonStr = columns + "," + jsonStr;
			}
		}
		jsonStr = "[ " + jsonStr + " ]";
		renderJson(jsonStr);
	}
	
	//ftp下载文件
	@SuppressWarnings("static-access")
	public void  exportList() throws Exception{
		String pkid=getPara("pkid");
		String sql="";
		sql="select download_file_name  from ap_idop.idop_download_job_list t where pkid= ? ";
		String downloadfilename=Db.use("gbase").findFirst(sql,pkid).getStr("download_file_name");
		 Record re=Db.findFirst("select remark from sys_param_info where key='con_ftp' ");
		String conftp=re.getStr("remark");
		
		/*Ftp f=new Ftp();
		FtpUtil ftp=new FtpUtil();
		f.setIpAddr("22.200.142.106");
		f.setUsername("ftphdsn");
		f.setPwd("Ftphdsn_123");
		f.setPath("/hdsn/idop/");
		ftp.connectFtp(f);
		boolean flag=ftp.startDown(f, "C:/test2/", "/hdsn/idop/",downloadfilename);
		setAttr("flag",flag);*/
		setAttr("dlfn",downloadfilename);
		setAttr("conftp",conftp);
		renderJson();
		
	}

}
