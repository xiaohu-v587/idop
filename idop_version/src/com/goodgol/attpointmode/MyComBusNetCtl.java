package com.goodgol.attpointmode;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.goodcol.controller.BaseCtl;
import com.goodcol.controller.ParamCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.tx.Tx;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.PermissionInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ext.anatation.RouteBind;
/*
 * x
 * @author 刘亚南
 * @date 2018-11-21
 */
@RouteBind(path = "/mycombusnet")
@Before({ManagerPowerInterceptor.class})
public class MyComBusNetCtl extends BaseCtl {
	
	
	// 记录日志用
	public static Logger log = Logger.getLogger(ParamCtl.class); 
	/**
	 * 加载主页面
	 */
	@Override
	@Before(PermissionInterceptor.class)
	public void index() {
		render("AddComBusNet.jsp");
	}
   //数据加载
	public void getList(){
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
		String orgnum = getCurrentUser().getStr("ORG_ID");//后续修改
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		// 查询语句
		
		String selectSql = " select id, networkname as netname , webaddress as webadd ";
		String extraSql = " from dop_my_combusnet  t  ";
		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1 ");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if (AppUtils.StringUtil(userNo) != null) {
			whereSql.append(" AND  t.follower = ? ");
			sqlStr.add(userNo);
		}
		if (AppUtils.StringUtil(orgnum) != null) {
			whereSql.append("  and t.org = ? ");
			sqlStr.add(orgnum);
		}
	
		
		// 排序
		whereSql.append(" ORDER BY t.change_time ");
		extraSql += whereSql.toString();
		// 查询
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql,extraSql, sqlStr.toArray());
		// 赋值
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 打印日志
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "基本算法管理", "3", "基本算法管理-查询");
		log.info("基本算法管理-查询");
		// 返回json数据
		renderJson();
	}
	
	public void toindex() {
		String orgid = getPara("orgnum");
		String warnkey = "1";
		String warning_status = "1";
		String indentify_status = "1";
		String check_stat = "1";
		Record re = new Record();
		String key = getPara("key");
		String style = getPara("style");
		String userno = null;
		if(AppUtils.StringUtil(key)!= null){
			if(AppUtils.StringUtil(style)!= null && "0".equals(style)){
				orgid = key;
			}else if(AppUtils.StringUtil(style)!= null && "1".equals(style)){
				userno = key;
			}
		}
		re.set("orgid", orgid);
		re.set("teller_no", userno);
		re.set("is_key_warning", warnkey);
		re.set("warning_status", warning_status);
		re.set("indentify_status", indentify_status);
		re.set("check_stat", check_stat);
		setAttr("datas", re.toJson());
		setAttr("flag","1");
		render("index.jsp");
	}
	
	
	//新增保存算法
	@Before(Tx.class)
	public void save(){
		//获取前台数据
		//主表主键
		String id = AppUtils.getStringSeq();
		//被关注机构
		//String followed_org = getPara("followed_org");
		//被关注人
		//String followed_teller = getPara("followed_teller");
		//业务模块
		//String busi_module = getPara("busi_module");
		//关注者机构号
		String org = getCurrentUser().getStr("ORG_ID");
		//关注人
		String follower = getCurrentUser().getStr("USER_NO");
		//关注类别
		//String follow_type = "";
		//网站名称
		String networkname=getPara("networkname");
		//网址
		String webaddress=getPara("webaddress");
		//时间转化
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		//当前时间
		String change_time = simpleFormat.format(new Date());
		int flag=0;
		
		//插入数据前先判断是否存在该数据
		String str="select count(*) sum from dop_my_combusnet k where k.org=? and  k.follower=?  and k.networkname =? and webaddress =? order by follower desc";
		 List<Record> list = Db.find(str,new Object[]{org,follower,networkname,webaddress});
		 String ss=list.get(0).getBigDecimal("sum").toString();
		if("0".equals(ss)){
			//保存信息
			 flag = Db.update("	INSERT INTO dop_my_combusnet (id, networkname,org,follower,change_time,webaddress)  " +
					" VALUES(?,?,?,?,?,?)",
					new Object[] {id, networkname,org,follower,change_time,webaddress});
		}
		
		// 记录日志
	    setAttr("flag", flag);
		log.info("基本算法管理-新增");
		renderJson();
	}

	
	//删除算法
	@Before(Tx.class)
	public void delete(){
		String id = getPara("key");
		//根据id删除算法表数据
		if(AppUtils.StringUtil(id) != null){
			String[] ids = id.split(",");
			for(String everId : ids){
				Db.update(" DELETE FROM dop_my_combusnet T WHERE T.ID='"+everId+"' ");
				setAttr("record", 1);
			}
		}else{
			setAttr("record", 0);
		}
		renderJson();
	}
	
	
	   //获取常用网址
	   @SuppressWarnings("unchecked")
	   public void getComBusNetData(){
		   String sql="select * from sys_param_info t  where key='dop_combusnet' order by val desc";
		   List<Record> list = Db.find(sql);
		   setAttr("data",list);
			renderJson();
	   }
	   
	   //统计某个菜单近期点击次数
	   @SuppressWarnings("unchecked")
	   public void gettotalclick(){
		   String id=getPara("ids");
		   //获取菜单名称
		   String searchid="select name from sys_menu_info where id=?";
		   List<Record> cdname=Db.find(searchid,new Object[]{id});
		   String name=cdname.get(0).getStr("name"); 
		   String url=getPara("url");
		   String userno = getCurrentUser().getStr("id");
		   String org = getCurrentUser().getStr("ORG_ID");
		   Record r= Db.findFirst("select t.by2 from sys_org_info t where t.id="+"'"+org+"'");
		   String by2=r.getStr("by2");
		   
		   String roleid = getCurrentUser().getStr("ROLE_ID");
		   if(roleid==null){
			   roleid="k";
		   }
		   int flag=0;
		   //时间转化
		   SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			//当前时间
		   String recentdate = simpleFormat.format(new Date());
		   
		   String sql="select sum from dop_common_funct where id=? and org=? and follower=? and role_id=? and recent_date>=to_char(sysdate-30,'yyyyMMddHH24:MI:SS') ";
		   List<Record> list=Db.find(sql,new Object[]{id,org,userno,roleid});
		   if("4".equals(by2)){//只有网点的人才具有点击统计效果
			   if(list !=null && list.size()>0){
				   int sum=Integer.parseInt(list.get(0).getBigDecimal("sum").toString());
				      sum++;
				   String update=" update dop_common_funct set sum="+sum +" where  id=? and org=? and follower=? and role_id=? and recent_date>=to_char(sysdate-30,'yyyyMMddHH24:MI:SS') ";  
				   flag=Db.update(update, new Object[]{id,org,userno,roleid});
				   
			   }else{
				 int sum=1;
				  flag = Db.update(" INSERT INTO dop_common_funct (id,name, org,follower, recent_date,sum,role_id,url)  " +
							" VALUES(?,?,?,?,?,?,?,?)",
							new Object[] {id,name, org,userno, recentdate, sum,roleid,url});
			   }
		   }
		
		  setAttr("flag",flag);
		  renderJson();
	   }
	   
	   //获取常用功能
	   @SuppressWarnings("unchecked")
	   public void  getComFunctionData(){
		   String userno = getCurrentUser().getStr("id");
		   String org = getCurrentUser().getStr("ORG_ID");
		   String roleid = getCurrentUser().getStr("ROLE_ID");
		   if(roleid==null){
			   roleid="k";
		   }
		   String sql="select * from (select * from dop_common_funct where  org=? and follower=? and role_id=? and recent_date>=to_char(sysdate-30,'yyyyMMddHH24:MI:SS' )order by sum desc) where  rownum<=7 order by rownum asc";
		   List<Record> list = Db.find(sql,new Object[]{org,userno,roleid});
		   setAttr("data",list);
		   renderJson();
	   }
	   
		
		//获取预警信息
		public void getWarnSignalData(){

			// 获取当前用户所属机构
			String orgnum = getCurrentUser().getStr("USER_NO");
			String org = getCurrentUser().getStr("ORG_ID");
			if(AppUtils.StringUtil(org)==null){
				org = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
			}
			//String orgnum = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
			//获取当前机构级别
			Record orgRe = Db.findFirst("  select by2 from sys_org_info  where orgnum ='"+org+"'  and  stat ='1'");
			String orgLevel = orgRe.getStr("by2");
			List<Record> list = null;
			
			Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			String time =format.format(date);
			String startTime = time.substring(0,8)+"000000";
			
			int sum = 0;
			//网点的只查当前网点的
			if("4".equals(orgLevel)){
				list = Db.find(" select t.deptno,(select orgname from sys_org_info where orgnum = t.deptno) as orgname,count(1) as total from dop_warning_info t left join dop_warning_param d on t.warning_code = d.warning_code   where t.deptlevel = '"+orgLevel+"' "+
						" and t.warning_status = '1'  and  (d.is_use is  null or d.is_use = '1') and t.indentify_status='1' and t.last_check_stat = '1'  and t.last_approval_stat = '1' and  "+
						" d.is_key_warning='1'  and t.deptno = '"+orgnum+"'   and t.create_time >='"+startTime+"'  group by  deptno order by total desc ");
			}/*else{
				list = Db.find(" select t.deptno,(select orgname from sys_org_info where orgnum = t.deptno) as orgname,count(1) as total from dop_warning_info t left join dop_warning_param d on t.warning_code = d.warning_code   where t.deptlevel = '"+orgLevel+"' "+
						" and t.warning_status = '1' and  (d.is_use is  null or d.is_use = '1')  and t.indentify_status='1' and t.last_check_stat = '1'  and t.last_approval_stat = '1' and  "+
						" d.is_key_warning='1'  and t.deptno in (select orgnum from sys_org_info where upid = '"+orgnum+"' and stat = '1')  and t.create_time >=to_char(sysdate-1,'yyyyMMddHH24:MI:SS') group by deptno");
			}*/
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					sum +=Integer.valueOf(list.get(i).get("total").toString()); 
				}
			}
			String key = "dop_morenum";
			String val = "01";
			String remark = Db.queryStr("select remark from SYS_PARAM_INFO where key = ? and val = ? ",
					new Object[] { key, val });
			setAttr("disnum",remark);
			setAttr("data",list);
			setAttr("sum",sum);
			
			
			renderJson();
		
		}
		
		//查询我的流程的数据
		@SuppressWarnings("unchecked")
		public void getWarningInfo(){
			Record user = getCurrentUser();
			BigDecimal indentWarning = new BigDecimal(0);
			BigDecimal checkWarning = new BigDecimal(0);
			BigDecimal approvalWarning = new BigDecimal(0);
			BigDecimal searchCheckWarning = new BigDecimal(0);
			
			String selectSql = " select count(1) ";		
			String sjfw= user.getStr("MAX_PERMI_ORGNUM");
			String roleLevel = user.getStr("ROLE_LEVEL");
			String org = getCurrentUser().getStr("ORG_ID");
			String orgid=user.getStr("orgnum");
			String userid = getCurrentUser().getStr("USER_NO");
			if(AppUtils.StringUtil(org)!=null){
				sjfw=org;
			}
		
			//判断权限机构级别是否为省行 
			String org_by2 = AppUtils.getOrgLevel(sjfw);
			if("1".equals(org_by2)){
				//待认定  都是省行认定
				String sql = "select count(*)  from dop_warning_info i left join dop_warning_param p on p.warning_code=i.warning_code left join (" +
						"select * from dop_gangedmenu_info k where k.id in ('351627F908C7461A9B8F82EB1BDEE750','35915F0EA9DC4B3DBFFD0C267579EA4C','4349615CE0A5433F9E23BADB7100D990'," +
						"'54AA6F8373B94F3AB115548D3867C6F7','679BE1CBC3C94B0689ECA17D7480A44D','8BDA5EE49374486CB87E7832EC8536F4','9DD1BF61F7F3419CAF5EB76AAD8BFDAA','9ECC18D14F7F4CB9812582C342F70936'," +
						"'CAAC03E702B84EB792D4CFBA924CB1247','CE1A1A8E9026486BB11124CAE2457D5A','DF39639549A449E8B1974D3B497165BB')) g on g.val=p.warning_type_code " +
						"where 1 = 1 " +
						" and i.indentify_status ='0' and  (p.is_use is  null or p.is_use = '1') ";
				
				indentWarning = Db.queryBigDecimal(sql);
			}
			String extrasql = " from dop_warning_info i left join dop_warning_param p on p.warning_code=i.warning_code " +
					" left join dop_gangedmenu_info g on g.val=p.warning_type_code  ";
			
			if("1".equals(roleLevel)){
				String whereSql2 = " where  (i.last_check_stat='0'  )  and  (p.is_use is  null or p.is_use = '1') and " +
						"( lvl_3_branch_no ='001001000') ";
				//待核查
				checkWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql2);
			}
			else if("4".equals(roleLevel)){
				String whereSql2 = " where  (i.last_check_stat='0'  )  and  (p.is_use is  null or p.is_use = '1') and (select case checker_level  "+
						" when '4' then lvl_4_branch_no end checker_level from dop_warning_info i2 where i2.id=i.id)='"+sjfw+"' ";
			//待核查
			checkWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql2);
			
			String whereSql4 = " where  (i.search_check_status='0' or i.search_check_status='2' )  and  (p.is_use is  null or p.is_use = '1') and  "+
					" (select check_org from dop_search_check c where c.check_flownum=i.flownum ) ='"+sjfw+"' ";
				//待查复
				searchCheckWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql4);
			
			}else if("2".equals(roleLevel)){
				
				String whereSql4 = " where  (i.search_check_status='0' or i.search_check_status='2' )  and  (p.is_use is  null or p.is_use = '1') and  "+
					" (select check_org from dop_search_check c where c.check_flownum=i.flownum ) in ('"+sjfw+"','"+orgid+"')";
				//待查复
				searchCheckWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql4);
			
			}
			
			else
			{
				
				String whereSql2 = " where  (i.last_check_stat='0'  ) and  (p.is_use is  null or p.is_use = '1') and (select case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no  when '2' then lvl_3_branch_no "+
							" when '3' then lvl_4_branch_no end checker_level from dop_warning_info i2 where i2.id=i.id)='"+sjfw+"' ";
				//待核查
				checkWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql2);
				
				String whereSql4 = " where  (i.search_check_status='0' or i.search_check_status='2' )  and  (p.is_use is  null or p.is_use = '1') and   "+
						" (select check_org from dop_search_check c where c.check_flownum=i.flownum ) in ('"+sjfw+"','"+orgid+"')";
				//待查复
				searchCheckWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql4);
			}
			String whereSql3 = "where p.is_confirm='1' and  (p.is_use is  null or p.is_use = '1') and i.last_approval_stat='0'  and " +
				"i.last_approver_org ='"+sjfw+"' ";
					//		" i.LAST_CHECKER_ORG in (select id from sys_org_info start with orgnum = '"+user.getStr("orgnum")+"' connect by prior orgnum=upid)" ;
			//待审批
			approvalWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql3);
			
			String sqlx = "select by5 from sys_org_info tt where  tt.id = '"+org+"'";
			String liststr = Db.findFirst(sqlx).getStr("by5");
			String str = "";
			if(StringUtils.isNotEmpty(liststr)){
				String[] strs =  liststr.split(",");
				for(String s:strs){
					str += "'"+s+"',";
				}
				str = str.substring(0,str.length()-1);
			}else{
				str = "000000000";
			}
			String sql ="select * from T_DJB_JCXX t where (t.orgid in ("+str+") or t.orgid = "+org+")  and sfpz='1'";
			List<Record> list = Db.find(sql);
			String id ="";
			String querySql = "";
			String djbname ="";
			String queryDataSql = "";
			int count = 0;
			List<Record> listone = Db.find(sql);
			for (int i = 0;i<list.size();i++){
				id = list.get(i).getStr("id");
					queryDataSql = "select * from t_djb_apply  t where t.djbid = '"+id+"' and t.status='1' and t.orgnum = '"+org+"' and approver !='"+userid+"'" ;
					listone = Db.find(queryDataSql);
					count+=listone.size();
			}
			//待认定(界面：warning)
			setAttr("indentWarning",indentWarning.intValue());
			//核查(界面：manager)
			setAttr("checkWarning",checkWarning.intValue());
			//审批(界面：manager) 
			setAttr("approvalWarning",approvalWarning.intValue());
			//待查复(界面：warning)
			setAttr("searchCheckWarning",searchCheckWarning.intValue());
			
			setAttr("djbdfh",count);
			renderJson();
		}
		
		//获取我的关注的数据
		public void getMyFocus(){
			String userno = getCurrentUser().getStr("id");
//			String roleLvl = getCurrentUser().getStr("role_level");
			String  maxPermiOrgnum =  getCurrentUser().getStr("MAX_PERMI_ORGNUM");
			String remarks=Db.findFirst("select remark from SYS_PARAM_INFO t where t.key='NearDate' and t.status='1' ").get("remark");
			int date=Integer.parseInt(remarks);
			
			//先查出关注的预警
			String selectSql = "select followed_org, max(follower) follower, busi_module, sub_busi_code, mark_code, follow_type, followed_teller from (select case when t.followed_org is null then '"+ maxPermiOrgnum +"' else t.followed_org end followed_org, t.follower, t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller from dop_my_follow t where (follower = ?  and t.follow_type = '0') " + "or (t.assigned_type = '3' and t.follow_type = '0'))" +
					"  group by followed_org,busi_module,sub_busi_code,mark_code,follow_type,followed_teller order by followed_org,follow_type desc ";
			List<Record> lr = Db.find(selectSql,userno);//0代表预警
			String org = null;
			Map<String,String> orgMap = new HashMap<>();
			List<Record> dataList = new ArrayList<>();
			if(lr != null && lr.size() > 0){
				for(Record re : lr){
					//机构中模块的数据
				
						org = re.getStr("followed_org");
						orgMap.put(org, "org_id");
					String followed_teller = re.getStr("followed_teller");
					String sub_busi_code = re.getStr("sub_busi_code");
					String mark_code=re.getStr("mark_code");
					String followed_org = re.getStr("followed_org");
					String busi_module = re.getStr("busi_module");
					
					String extraSql = " from dop_warning_info i left join dop_warning_param p on" +
							" p.warning_code=i.warning_code where i.deptno in (select id from sys_org_info where id = ? or by5 like ?) and p.busi_module=?  " +
							"and  (p.is_use is  null or p.is_use = '1')  and p.warning_type_code= ?  and p.warning_code= ? and i.create_time >= to_char(" +
							"sysdate-'"+date+"','yyyyMMddHH24:MI:SS') ";
					String wheresql = " ";
					if(followed_teller != null){
						wheresql = " and  i.teller_no = '"+followed_teller+"' ";
					}
					List<Record> warning = Db.find("select distinct (select orgname from sys_org_info where orgnum = ? and stat=1) orgname, " +
							" 	(select remark from sys_param_info where key = 'dop_ywtype' and val=p.busi_module) name, " +
							//" 	(select remark from DOP_GANGEDMENU_INFO where  val= p.warning_type_code) ywzlname, " +
							" 	(select warning_name from dop_warning_param where warning_code=p.warning_code ) warncodename, " +//预警名称
							"   (select count(i.is_question)" + extraSql + wheresql +"  and i.warning_status='1' and is_question = '1' ) isquestioncount ,"+
							" 	(select d.name from sys_user_info d where d.user_no = i.teller_no ) username,(select count(1) " + extraSql + wheresql +"  and " +
							"  i.warning_status='1' ) warningcount " +extraSql ,followed_org,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code);
					if(warning != null && warning.size() > 0){
						if(followed_teller != null){
							warning.get(0).set("orgid", re.getStr("followed_org"));
							warning.get(0).set("date", date);
							warning.get(0).set("busi_module", busi_module);
							warning.get(0).set("mark_code", mark_code);
							warning.get(0).set("userno", followed_teller);
						}else{
							warning.get(0).set("orgid", re.getStr("followed_org"));
							warning.get(0).set("date", date);
							warning.get(0).set("busi_module", busi_module);
							warning.get(0).set("mark_code", mark_code);
						}
						dataList.add(warning.get(0));
					}
					org=followed_org;
					
				}
			}
			String pftjSql = "select followed_org, max(follower) follower, busi_module, sub_busi_code, mark_code, follow_type, followed_teller from (select case when t.followed_org is null then '"+ maxPermiOrgnum +"' else t.followed_org end followed_org, t.follower, t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller from dop_my_follow t where (follower = ?  and t.follow_type = '1') " + "or (t.assigned_type = '3' and t.follow_type = '1'))" +
					"  group by followed_org,busi_module,sub_busi_code,mark_code,follow_type,followed_teller order by followed_org,follow_type desc ";
			List<Record> pflr = Db.find(pftjSql,userno);//1代表评分
			if(pflr != null && pflr.size() > 0){
				//拼接完结束
				String busimodule=" ";
				for (Record record : pflr) {
					String pforg = record.getStr("followed_org");
					//if(!orgMap.containsKey(pforg)){
						String pfSql = " from dop_org_score_detail d left join sys_org_info o on o.id=d.deptno where deptno=?  and data_month >= to_char(add_months(sysdate,-2),'yyyyMM') and indexname='ZF' and module=? order by d.data_month desc,grow_rate desc ";
						List<Record> scoreTempList = Db.find("select  d.data_month,d.deptno,o.orgname,d.score,case lag(score,1)over(order by data_month) when 0 then 0 else score/lag(score,1)over(order by data_month)-1 end grow_rate "
								+pfSql,pforg,record.get("busi_module"));
						busimodule=record.get("busi_module");
						if(scoreTempList != null && scoreTempList.size() > 0){
							Record lastMonthRecord = scoreTempList.get(0);//上个月得分
							Record r = appendScoreStr(lastMonthRecord,busimodule);
							if (r != null) {
								r.set("orgid", pforg);
								dataList.add(r);
							}
						}
					//}
				}
			}
			String key = "dop_morenum";
			String val = "01";
			String remark = Db.queryStr("select remark from SYS_PARAM_INFO where key = ? and val = ? ",
					new Object[] { key, val });
			setAttr("disnum",Integer.parseInt(remark)-1);
			setAttr("data", dataList);
			renderJson();
		}
		private Record appendScoreStr(Record lastMonthRecord,String busi_module){
			
			//获取业务模块名称
			String ywmkname=Db.findFirst("  select remark from sys_param_info where key='dop_ywtype'  and val = ? ", busi_module).getStr("remark");
			Record record = new Record();
			String lastMonth = lastMonthRecord.getStr("data_month").substring(4);
			//将个位数月份0去掉，01月>>1月
			if(lastMonth.substring(0, 1).equals("0")){
				lastMonth = lastMonth.substring(1);
			}
			BigDecimal growRate = lastMonthRecord.getBigDecimal("grow_rate");
			String growFlag = "";
			if(growRate !=null){
			 try{
				if(growRate.compareTo(new BigDecimal(0))==-1){
					growRate = growRate.multiply(new BigDecimal(-1));
					growFlag = "下降";
				}else{
					growFlag = "增长";
				}
				growRate = growRate.multiply(new BigDecimal(100));
				growRate = growRate.setScale(2,BigDecimal.ROUND_HALF_UP);//保留两位小数，四舍五入
				record.set("orgname", lastMonthRecord.getStr("orgname")).set("ywmkname", ywmkname).set("lastMonth", lastMonth).set("score", lastMonthRecord.getBigDecimal("score").toString()).set("growFlag", growFlag).set("growRate", growRate);
		 	 }catch(Exception e){
				System.out.println("WarningSearchCtl.getMyFocus()>>>>>>>>>>> growRate空指针异常，原因：未获得上月数据");
				e.printStackTrace();
			 }
			}
			return record;
			
		}
}
