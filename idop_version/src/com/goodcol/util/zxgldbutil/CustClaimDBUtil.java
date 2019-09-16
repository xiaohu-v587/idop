package com.goodcol.util.zxgldbutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;

public class CustClaimDBUtil {
	
	/**
	 * 客户认领列表
	 */
	@SuppressWarnings("unchecked")
	public Page<Record> pageList(Map<String, Object> map){
		// 获取查询参数
		int pageNum = (Integer)map.get("pageNum");
		int pageSize = (Integer)map.get("pageSize");
		Map<String, Object> maps = findSql(map);
		Page<Record> records = Db.use("default").paginate(pageNum, pageSize, (String) maps.get("selectSql"), (String) maps.get("extrasql"), ((List<String>) maps.get("sqlStr")).toArray());
		return records;
	}
	
	public Map<String, Object> findSql(Map<String, Object> map) {
		// 获取当前用户信息
		String orgArr = (String)map.get("orgArr");
		//客户任务限期
		String flag	= (String)map.get("flag");
		// 获取页面输入查询条件
		String cust_no = (String)map.get("cust_no");
		String name = (String)map.get("name");	
		String clas_five = (String)map.get("clas_five");	
		String cust_stat	= (String)map.get("cust_stat");
		String cust_stat_flag	= (String)map.get("cust_stat_flag");
		String cust_type	= (String)map.get("cust_type");
		String befor_name	= (String)map.get("befor_name");
		
		String is_back	= (String)map.get("is_back");
		String userId	= (String)map.get("userId");
		String pageFlag	= (String)map.get("pageFlag");
		String areaArr	= (String)map.get("areaArr");
		String orgId	= (String)map.get("orgId");
		String orgSelArr	= (String)map.get("orgSelArr");
		String levelStr	= (String)map.get("levelStr");
		
		String area_code	= (String)map.get("area_code");
		String prov_code	= (String)map.get("prov_code");
		String date1 = Db.use("default").queryStr(" select val from gcms_param_info t where t.key='SECOND_WEEK_DAY' ");
		String date2 = Db.use("default").queryStr(" select val from gcms_param_info t where t.key='THIRD_WEEK_DAY' ");
		String selectSql = " select "
						+"       p.id, "
						+"       p.customercode cust_no, "
						+"       p.customername name, "
						+"       p.clas_five, "
						+"       p.custyp, "
						+"       p.dummy_cust_no, "
						+"       p.clas_potential, "
						+"       p.cussts,t.name admi_area, "
						+"       o.orgname resp_center_name, "
						+"       o.bancsid resp_center_no, "
						+"       p.relate_cust_name, "
						+"       u.name relate_mgr_name, "
						+"       u.phone relate_mgr_mobile, "
//						+"       case when c.claim_prop_all is not null then c.claim_prop_all||'%' else '0%' end claim_prop_all, "
//						+"       case when c.to_claim_prop is not null then c.to_claim_prop||'%' else '100%' end to_claim_prop, "
//						+"       replace(replace(c.claim_cust_mgr_name,',',';'),':','-') claim_cust_mgr_name, "
//						+"       c.claim_cust_mgr_id, "
//						+"       c.creat_name, "
						+"       p.befor_cust_mgr_id, "
						+"       p.befor_cust_mgr_name, "
						+"       p1.name clas_five_cn, "
						+"       p2.name cust_type_cn, "
						+"       p3.name cust_stat_cn ";
						/*+"       floor(now() - to_date(p.indate, 'yyyyMMdd')) stay_date, "*/
						/*+"       ceil(to_date(p.indate, 'yyyyMMdd') + 20 - now()) remain_date ";*/
		if (AppUtils.StringUtil(levelStr) != null) {
			if (!"3".equals(cust_stat)) {
				if ("2".equals(levelStr)) {
					selectSql+="       ,floor(sysdate - to_date(p.indate, 'yyyyMMdd')) stay_date, "
							+"       ceil(to_date(p.indate, 'yyyyMMdd') + nvl('"+date1+"',0) - sysdate) remain_date ";
				}else{
					selectSql+="       ,floor(sysdate - to_date(p.third_indate, 'yyyyMMdd')) stay_date, "
							+"       ceil(to_date(p.third_indate, 'yyyyMMdd') + nvl('"+date2+"',0) - sysdate) remain_date ";
				} 
			} else {
				selectSql+="       ,'0' as stay_date, "
						+"       '0' as remain_date ";
			}
		}
		//客户分配界面
		if ("1".equals(pageFlag)) {
			selectSql+="       ,floor(sysdate - to_date(p.indate, 'yyyyMMdd')) stay_date, "
					+"       ceil(to_date(p.indate, 'yyyyMMdd') + nvl('"+date1+"',0) - sysdate) remain_date ";
		}
						
		String extrasql = " from pccm_cust_pool p ";
//						+"  left join (select cust_pool_id, max(claim_time) claim_time,"
//						+"                    sum(to_number(nvl(claim_prop, '0'))) claim_prop_all, "
//						+"                    (100 - sum(to_number(nvl(claim_prop, '0')))) to_claim_prop, "
//						+"                    to_char(wm_concat(claim_cust_mgr_name)) claim_cust_mgr_name, "
//						+"                    to_char(wm_concat(claim_cust_mgr_id)) claim_cust_mgr_id, "
//						+"                    to_char(wm_concat(distinct(creat_name))) creat_name "
//						+"               from (select cust_pool_id,claim_time, "
//						+"                            claim_prop,claim_cust_mgr_id, "
//						+"                            claim_cust_mgr_name || ':' || claim_prop || '%' claim_cust_mgr_name, "
//						//+"                            creat_id, "
//						+"                            creat_name||creat_id creat_name "
//						+"                       from pccm_cust_claim "
//						+"                      where del_stat = '0' " ;
//						if ("1".equals(flag)) {
//							extrasql += " and incflg = '4'";
//						} else {
//							extrasql += " and incflg <> '4' ";
//						}
//						
//						extrasql += ") "
//						+"              where cust_pool_id in ( select id from  pccm_cust_pool where orgnum in "
//						+orgArr+ " and pa_not_zero <> '0')"
//						+"              group by cust_pool_id) c "
//						+"    on p.id = c.cust_pool_id "
				
//				extrasql+=	"  left join pccm_cust_org_are a on p.orgnum = a.org_id "
//						+"  left join PCCM_CUST_POOL_MONEY m on p.id = m.CUST_POOL_ID "
				extrasql +=	"  left join pccm_provinces_citys t on p.area_code = t.id "
						+"  left join sys_user_info u on p.cust_manager = u.id "
						+"  left join gcms_param_info p1 on p.clas_five = p1.val and p1.key = 'clas_five' "
						+"  left join gcms_param_info p2 on p.custyp = p2.val and p2.key = 'incflg' "
						+"  left join gcms_param_info p3 on p.cussts = p3.val and p3.key = 'cust_stat' "
						+"  left join sys_org_info o on o.bancsid = p.org_id ";
		StringBuffer whereSql = new StringBuffer(" where 1=1  ");
		//whereSql.append("and M.BUSI_INC <> 0");
		List<String> sqlStr = new ArrayList<String>();
		
		if ("1".equals(flag)) {
			whereSql.append(" and p.deptlevel='3' and p.customercode is not null and p.cust_type != '03' ");
			//默认查询条件
//			if (AppUtils.StringUtil(orgId) != null) {
//				 whereSql.append(" and p.orgnum in ( select id from sys_org_info where by5 like '%"+orgId+"%' or id ='"+orgId+"') ");
//			}
			//默认查询条件
			if (AppUtils.StringUtil(orgArr) != null) {
				whereSql.append(" and p.orgnum in " + orgArr);
			}
			
		}else if("2".equals(flag)){
			
			whereSql.append(" and p.customercode is null and p.area_code is not null");
			//默认查询条件
			if (AppUtils.StringUtil(areaArr) != null ) {
//				whereSql.append(" and ( ");
//				whereSql.append(" p.orgnum in " + orgArr);
//
//				if (AppUtils.StringUtil(orgArr) != null
//						&& AppUtils.StringUtil(areaArr) != null) {
//					whereSql.append(" or ");
//				}
//				if (AppUtils.StringUtil(areaArr) != null) {
//					whereSql.append(" p.area_code in " + areaArr);
//				}
//				whereSql.append(" ) ");
				whereSql.append(" and p.area_code in " + areaArr);
			}

			if (AppUtils.StringUtil(area_code) != null) {
				whereSql.append(" and t.id = ? ");
				sqlStr.add(area_code.trim());
			}
			if (AppUtils.StringUtil(prov_code) != null) {
				whereSql.append(" and t.parentid = ? ");
				sqlStr.add(prov_code.trim());
			}
		}
		if (AppUtils.StringUtil(cust_no) != null) {
			 if ("1".equals(flag)) {
					whereSql.append(" and p.customercode=? ");
				}else if("2".equals(flag)){
					whereSql.append(" and p.dummy_cust_no=? ");
				}
			 sqlStr.add(cust_no.trim());
		}
		if (AppUtils.StringUtil(orgSelArr) != null) {
			whereSql.append(" and p.orgnum in "+orgSelArr);
		}
		if (AppUtils.StringUtil(name) != null) {
			whereSql.append(" and p.customername like ? ");
			sqlStr.add("%" + name.trim() + "%");
		}
		if (AppUtils.StringUtil(clas_five) != null) {
			whereSql.append(" and p.clas_five= ? ");
			sqlStr.add(clas_five.trim());
		}
		if (AppUtils.StringUtil(befor_name) != null) {
			whereSql.append(" and p.befor_cust_mgr_name like ? ");
			sqlStr.add("%" + befor_name.trim() + "%");
		}
		
		if (AppUtils.StringUtil(cust_stat) != null) {
			whereSql.append(" and p.cussts= ? ");
			sqlStr.add(cust_stat.trim());
		}
		
		if ("1".equals(cust_stat_flag)) {
			whereSql.append(" and p.cussts in ('1','2') ");
		}
		
		if (AppUtils.StringUtil(cust_type) != null) {
			whereSql.append(" and p.custyp= ? ");
			sqlStr.add(cust_type.trim());
		}
		if (AppUtils.StringUtil(levelStr) != null) {
			 if ("2".equals(levelStr)) {
				 if (!"3".equals(cust_stat)) {
					 whereSql.append(" and p.third_indate is null ");
				 }
			} else {
				if (!"3".equals(cust_stat)) {
					whereSql.append(" and p.third_indate is not null ");
				}
			}
		}
		
		//是否可撤回
		if(AppUtils.StringUtil(is_back)!=null){
			
			if ("1".equals(is_back)) {
				whereSql.append( " and p.id in ");
			}else{
				whereSql.append( " and p.id not in ");
			}
			whereSql.append( "(select cust_pool_id from pccm_cust_claim c where c.del_stat = '0' ");
			whereSql.append( " and (sysdate - to_date(c.claim_time, 'yyyyMMddhh24miss'))<1 ");
			if (AppUtils.StringUtil(orgArr) != null&&"1".equals(flag)) {
				whereSql.append( " and cust_pool_id in (select id from pccm_cust_pool where orgnum in "+orgArr+" ) ");
			}
			if("1".equals(pageFlag)){
				whereSql.append(" and c.creat_id = '"+userId+"' ");
			}else{
				whereSql.append(" and c.claim_cust_mgr_id = '"+userId+"' ");
			}
			whereSql.append(" group by cust_pool_id)");
			
		}
		
		//排序
		whereSql.append(" order by p.cussts,p.indate,o.id ");
		extrasql += whereSql.toString();
		Map<String, Object> mapSql = new HashMap<String, Object>();
		mapSql.put("selectSql", selectSql);
		mapSql.put("extrasql", extrasql);
		mapSql.put("sqlStr", sqlStr);
		return mapSql;
	}
	
	/**
	 * 列表查询
	 */
	public List<Record> custList(Map<String, Object> map) {
		Map<String, Object> maps = findSql(map);
		List<Record> reList = Db.use("default").find((String) maps.get("selectSql") + (String) maps.get("extrasql"), ((List<String>) maps.get("sqlStr")).toArray());
		return reList;

	}
	
	/**
	 * 客户数
	 */
	public String custListCount(Map<String, Object> map) {
		Map<String, Object> maps = findSql(map);
		Record record = Db.use("default").findFirst("select count(*) as total " + (String) maps.get("extrasql"), ((List<String>) maps.get("sqlStr")).toArray());
		return String.valueOf(record.getBigDecimal("total").intValue());
		
	}
	
	/**
	 * 客户认领保存
	 */
	public void claimSave(Map<String, Object> map){
		String user_id = (String)map.get("user_id");
		String user_name = (String)map.get("user_name");
		
		String customercode = (String)map.get("customercode");
		String id = (String)map.get("id");
		String pra = (String)map.get("pro");
		String cust_stat = null;
		// 
		if (AppUtils.StringUtil(id) != null && AppUtils.StringUtil(pra) != null) {
			int ablrProp = 0;
			String claimPropAll = claimPropAll(id);
			int prop = Integer.parseInt(pra);
			if ((100 - Integer.parseInt(claimPropAll)) >= prop) {
				ablrProp = prop;
			} else {
				ablrProp = (100 - Integer.parseInt(claimPropAll));
			}
			
			//客户经理已认领的客户比例
			String claimProp = claimProp(id, user_id);
			if("0".equals(claimProp)){
				//查询客户来源
				String incflg = Db.use("default").queryStr(" select incflg from pccm_cust_pool where id = ? ",new Object[]{id});
				//插入认领的数据
				Db.use("default").update(" insert into pccm_cust_claim(id,cust_no,claim_prop,claim_cust_mgr_id,"
						+"	claim_cust_mgr_name,claim_time,del_stat,cust_pool_id,incflg) values(?,?,?,?,?,to_char(sysdate,'yyyyMMddhh24miss'),'0',?,?) " ,
						new Object[] { AppUtils.getStringSeq(),customercode,ablrProp,user_id,user_name,id,incflg});
			}else{
			
				//修改认领的数据
				Db.use("default").update(" update pccm_cust_claim set claim_prop=? where claim_cust_mgr_id=? and cust_pool_id=? and del_stat='0' " ,
						new Object[] { ablrProp+Integer.parseInt(claimProp),user_id,id});
			}
			//修改客户状态
			
			cust_stat = claimStat(id, null);
			updateClaimStat(id, cust_stat);
			
		}
	}
	
	/**
	 * 客户申诉保存
	 */
	public void claimCompSave(Map<String, Object> map){
		String user_id = (String)map.get("user_id");
		String user_name = (String)map.get("user_name");
		
		String cust_claim_id = (String)map.get("cust_claim_id");
		String appeal_reas = (String)map.get("appeal_reas");
		// 
		if (AppUtils.StringUtil(cust_claim_id) != null) {
			Db.use("default").update(" insert into pccm_cust_appeal "
					+"(id,cust_pool_id,appeal_stat,appeal_reas,appeal_per_id,appeal_per_name,appeal_time) "
					+"values (?,?,?,?,?,?,to_char(sysdate,'yyyyMMdd'))" , 
					new Object[] { AppUtils.getStringSeq(),cust_claim_id,"1",appeal_reas,user_id,user_name });
		}
	}
	
	/**
	 * 详情查询
	 */
	public Record getDetail(String id){
		String sql = " select c.*,p.customercode||p.dummy_cust_no customercode,p.customername name,p.id "
				+" from pccm_cust_pool p "
				+" left join ( select cust_pool_id, "
				+" to_char(wm_concat(claim_prop)) claim_prop, "
				+" to_char(wm_concat(claim_cust_mgr_name)) claim_cust_mgr_name "
				+" from pccm_cust_claim where del_stat='0' "
				+" group by cust_pool_id) c "
				+" on c.cust_pool_id = p.id "
				+" where 1=1 ";
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();

		if (AppUtils.StringUtil(id) != null) {
			sb.append(" and p.id = ? ");
			listStr.add(id.trim());
		}
		List<Record> list = Db.use("default").find(sql + sb.toString(), listStr.toArray());
		Record re = null;
		if(list!=null&& list.size()>0){
			re = list.get(0);
		}
		return re;
	}
	
	/**
	 * 查询数据最新入池时间
	 */
	public String findNewDate(Map<String, Object> map){
		String sql ="select max(maxDate) maxDate "
				+"  from (select case "
				+"                 when third_indate is null then "
				+"                  indate "
				+"                 else "
				+"                  third_indate "
				+"               end as maxDate "
				+"          from pccm_cust_pool p where 1=1 ";
		String orgArr = (String)map.get("orgArr");
		if (AppUtils.StringUtil(orgArr) != null) {
			sql += " and p.orgnum in "+orgArr;
		}
		String flag	= (String)map.get("flag");
		if ("1".equals(flag)) {
			sql += " and p.customercode is not null ";
		}else if("2".equals(flag)){
			sql += " and p.customercode is null ";
		}
				
		sql+=" ) ";
		String result =Db.use("default").queryStr(sql) ;
		return result;
	}
	
	/**
	 * 查询可待认领最大值
	 */
	public String claimManyCheck(String ids){
		String sql = "select (100 - nvl(max(claim_prop_all),0)) claim_prop_max "
				+"  from pccm_cust_pool p "
				+"  left join (select cust_pool_id, "
				+"                    sum(to_number(nvl(claim_prop, '0'))) claim_prop_all "
				+"               from pccm_cust_claim "
				+"              where del_stat = '0' "
				+"              	and cust_pool_id in "+ids 
				+"              group by cust_pool_id) c "
				+"    on p.id = c.cust_pool_id ";
		String result =String.valueOf(Db.use("default").queryBigDecimal(sql)) ;
		return result;
	}
	
	/**
	 * 查询已认领的时间
	 */
	public String claimDate(String pool_id,String mgr_id,String distr_id){
		String sql = "select min(round(sysdate - to_date(claim_time, 'yyyyMMddhh24miss'), 3)) claim_date "
				+"  from pccm_cust_claim "
				+" where del_stat = '0' ";
		if (AppUtils.StringUtil(mgr_id) != null ) {
			sql+="   and claim_cust_mgr_id = '"+mgr_id+"' ";
		}
		if (AppUtils.StringUtil(distr_id) != null ) {
			sql+="   and creat_id = '"+distr_id+"' ";
		}
		sql +="   and cust_pool_id = ? ";
		String result = String.valueOf(Db.use("default").queryBigDecimal(sql,new Object[]{pool_id}));
		return result;
	}
	
	/**
	 * 查询当前认领状态
	 */
	public String claimStat(String pool_id,String mgr_id){
		String cust_stat = null;
		String str="select sum(to_number(nvl(claim_prop, '0'))) claim_prop_all "
				+"  from pccm_cust_claim "
				+" where del_stat = '0' ";
		if (AppUtils.StringUtil(mgr_id) != null ) {
			str+="   and claim_cust_mgr_id = '"+mgr_id+"' ";
		}
		if (AppUtils.StringUtil(pool_id) != null ) {
			str+="   and cust_pool_id = '"+pool_id+"' ";
		};
		str+=" group by cust_pool_id ";
		//
		String claim_prop_all = String.valueOf(Db.use("default").queryBigDecimal(str));
		if(Integer.parseInt(claim_prop_all)==100){
			cust_stat="3";
		}else if(Integer.parseInt(claim_prop_all)<100&&Integer.parseInt(claim_prop_all)>0){
			cust_stat="2";
		}else{
			cust_stat="1";
		}
		return cust_stat;
	}
	
	/**
	 * 查询客户当前被认领比例
	 */
	public String claimPropAll(String pool_id){
		String str="select sum(to_number(nvl(claim_prop, '0'))) claim_prop_all "
				+"  from pccm_cust_claim "
				+" where del_stat = '0' "
				+"   and cust_pool_id = ? "
				+" group by cust_pool_id ";
		//
		String claim_prop_all = String.valueOf(Db.use("default").queryBigDecimal(str,new Object[] {pool_id}));
		if (AppUtils.StringUtil(claim_prop_all) == null ) {
			claim_prop_all="0";
		}
		return claim_prop_all;
	}
	
	/**
	 * 查询客户经理当前认领比例
	 */
	public String claimProp(String pool_id,String mgr_id){
		String str="select claim_prop  "
				+"  from pccm_cust_claim "
				+" where del_stat = '0' "
				+"   and claim_cust_mgr_id = ? "
				+"   and cust_pool_id = ? ";
		String claim_prop = Db.use("default").queryStr(str,new Object[] { mgr_id,pool_id });
		if (AppUtils.StringUtil(claim_prop) == null ) {
			claim_prop="0";
		}
		return claim_prop;
	}
	
	/**
	 * 修改认领状态
	 */
	public void updateClaimStat(String pool_id,String cust_stat){
		String upsql="3".equals(cust_stat)?
				" update pccm_cust_pool set cussts=?,all_claim_time=to_char(sysdate,'yyyyMMddhh24miss') where id=? "
				:" update pccm_cust_pool set cussts=? where id=? ";
		Db.use("default").update(upsql , new Object[] { cust_stat,pool_id});
	}
	
	/**
	 * 撤回操作
	 */
	public void claimBack(String pool_id,String mgr_id,String distr_id){
		String cust_stat=null;
		double claimPro = 0;
		String claimProp = "0";
		String claimPropAll=claimPropAll(pool_id);
		if(AppUtils.StringUtil(mgr_id) != null){
			claimProp = claimProp(pool_id, mgr_id);
			claimPro = Integer.parseInt(claimPropAll)-Integer.parseInt(claimProp);
		}
		String distrProp = "0";
		if(AppUtils.StringUtil(distr_id) != null){
			distrProp = distrProp(pool_id, distr_id);
			claimPro = Integer.parseInt(claimPropAll)-Integer.parseInt(distrProp);
		}
		
		if(claimPro<100&&claimPro>0){
			cust_stat="2";
		}else{
			cust_stat="1";
		}
		updateClaimStat(pool_id, cust_stat);
		delclaim(pool_id, mgr_id,distr_id);
	}
	
	/**
	 * 查询客户经理当前分配比例
	 */
	public String distrProp(String pool_id,String distr_id){
		String str="select to_char(sum(claim_prop))  "
				+"  from pccm_cust_claim "
				+" where del_stat = '0' "
				+"   and creat_id = ? "
				+"   and cust_pool_id = ? ";
		String claim_prop = Db.use("default").queryStr(str,new Object[] { distr_id,pool_id });
		if (AppUtils.StringUtil(claim_prop) == null ) {
			claim_prop="0";
		}
		return claim_prop;
	}
	
	/**
	 * 删除操作
	 */
	public void delclaim(String pool_id,String mgr_id,String distr_id){
		if (AppUtils.StringUtil(mgr_id) != null ) {
			Db.use("default").update(" update pccm_cust_claim set del_stat='1',del_time=to_char(sysdate,'yyyyMMddhh24miss') where cust_pool_id = ? and claim_cust_mgr_id=?", new Object[] {pool_id,mgr_id});
		}else if (AppUtils.StringUtil(distr_id) != null ) {
			Db.use("default").update(" update pccm_cust_claim set del_stat='1',del_time=to_char(sysdate,'yyyyMMddhh24miss') where cust_pool_id = ? and creat_id=?", new Object[] {pool_id,distr_id});
		}
	}
	
	/**
	 * 行政区域列表查询
	 */
	public List<Record> areaList(String pid) {
		String sql=" select id ,name from pccm_provinces_citys where 1=1 ";
		if (AppUtils.StringUtil(pid) != null) {
			sql += " and parentid = '"+pid+"' ";
		} else {
			sql += " and arealevel ='2' ";
		}
		sql += " order by id desc ";
		List<Record> reList = Db.use("default").find(sql);
		return reList;

	}
	
	/**
	 * 认领详情
	 **/
	public List<Record> findClaimList(String poolid){
		List<Record> list = null;
		if (AppUtils.StringUtil(poolid) != null) {
			String sql=" select id ,claim_cust_mgr_id,claim_cust_mgr_name,claim_prop,claim_prop||'%' as claim_prop_str, "
					+" cust_pool_id, creat_name||creat_id creat_name from pccm_cust_claim where 1=1 and del_stat='0' and cust_pool_id = ? ";
			list = Db.use("default").find(sql,new Object[]{poolid});
		} 
		return list;
	}
	
}
