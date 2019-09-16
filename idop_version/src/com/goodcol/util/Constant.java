package com.goodcol.util;

public class Constant {
	
	//总行机构号
	//public static final String HEAD_OFFICE="99999";
	public static final String HEAD_OFFICE="99990000";
	//总行运管部
	public static final String HEAD_YGBOFFICE="99990001";
	//运营岗位运营资格证  字典表1235
	public static final String YY_CERT="0";
	//运营条线人员统计默认查询岗位
	public static final String POST_TELLER="740A6A7488613539E0531704650A64AB,730A6A7488613539E0531704650A64AB,720A6A7488613539E0531704650A64AB";

	
	
	/**
	 * 角色名称
	 */
	/*
	public static final String ROLENAME_XTGLY = "系统管理员";
	public static final String ROLENAME_YYJL = "营运经理";
	public static final String ROLENAME_GY = "柜员";
	public static final String ROLENAME_YWZX = "运维中心";
	*/
	//机构号长度
	public static final int ORGNUM_LENGTH=6;
	public static final String ROLENAME_FHYYGLBGLRY = "分行营运管理部管理人员";
	public static final String ROLENAME_ZHHZ = "支行行长";
	public static final String ROLENAME_YYJL = "营运经理";
	//gbase存储过程返回参数标识
	public static final String GBASE_RETURNCODE  = "T";
	
	public static final String CN_CUST_CNT_MY = "客户统计我行客户明细下载";
	public static final String CN_CUST_CNT_OTHER = "客户统计他行客户明细下载";
	public static final String CN_MY_CUST_MY = "我的客户我行客户明细下载";
	public static final String CN_MY_CUST_OTHER = "我的客户他行客户明细下载";
	public static final String CN_CUST_CLAIM_MY = "客户认领我行客户明细下载";
	public static final String CN_CUST_CLAIM_OTHER = "客户认领他行客户明细下载";
	public static final String CN_CUST_PA = "PA不为零客户明细下载";
	public static final String CN_CUST_VALID = "有效户明细下载";
	
	/**
	 * 客户统计我行客户明细查询SQL
	 */
	public static final String SQL_CUST_CNT_MY = " select si.orgname       as 机构名称, "
			+"        p.name as 客户名称, "
			+"        p.cust_no as 客户号, "
			+"        p1.name         as 五层分类, "
			+"        nvl(p.ck_curr_zcy,0) as 存款时点余额, "
			+"        nvl(p.pa_nrj_zcy,0) as 存款日均余额, "
			+"  	  nvl(p.bn_fic_nrj,0) as 表内理财日均, "
			+"        nvl(p.bw_fic_nrj,0) as 表外理财日均, "
//			+"        to_char((decode(nvl(p.loan_curr_zcy, 0), 0, '-', round(p.ck_curr_zcy / p.loan_curr_zcy, 4)))) as 存贷比时点, "
//			+"        to_char((decode(nvl(p.loan_nrj_zcy, 0), 0, '-', round(p.pa_nrj_zcy / p.loan_nrj_zcy, 4)))) as 存贷比日均, "
			+"        (nvl(p.int_inc, 0) + nvl(p.middle_income, 0) + nvl(p.splitting, 0)) as 营收, "
			+"        to_char(decode(p.is_dfx, 1, 20, 0) + decode(p.is_jscard, 1, 20, 0) + "
			+"                decode(p.is_dxt, 1, 20, 0) + decode(p.is_hdx, 1, 20, 0) + "
			+"                decode(p.is_bocnet, 1, 20, 0)) || '%' as 产品覆盖率 "
			+"   from pccm_cust_base_info p "
			+"   left join gcms_param_info p1 on p.five_level = p1.val and p1.jkey = 'clas_five' "
			+"   left join sys_org_info si on si.id = p.id "
			+"  where 1 = 1 "
//			+"    and p.cust_type = '02' "
			+"    and (nvl(p.int_inc, 0) + nvl(p.middle_income, 0) + nvl(p.splitting, 0) + nvl(p.pa_nrj_zcy, 0) + nvl(p.loan_nrj_zcy, 0) + nvl(p.bn_fic_nrj, 0) + nvl(p.bw_fic_nrj, 0)) <> 0 "
			+"    and p.id is not null ";
//			+"    and p.data_date = '"+AppUtils.findGNewDate()+"' ";
	
	/**
	 * 客户统计他行客户明细查询SQL
	 */
	public static final String SQL_CUST_CNT_OTHER = " select t.name  as 所属区域, "
			+"        p.customername as 客户名称, "
			+"        p.dummy_cust_no as 虚拟客户号, "
			+"        p.relate_cust_name as 关联客户名称, "
			+"        u.name as 关联客户经理, "
			+"        u.phone as 关联客户经理联系方式 "
			+"   from pccm_cust_pool p "
			+"   left join sys_user_info u "
			+"     on p.cust_manager = u.id "
			+"   left join pccm_provinces_citys t "
			+"     on p.area_code = t.id "
			+"  where 1 = 1 "
			+"    and p.incflg <> '4' "
			+"    and p.area_code is not null ";
	
	/**
	 * 我的客户我行客户明细下载SQL
	 */
	public static final String SQL_MY_CUST_MY (String user_no,String custType){
		String sql = null;
		if(AppUtils.StringUtil(user_no) != null){
			sql=" select o.orgname as 责任中心名称, "
				+"        p.name 客户名称, "
				+"        p.cust_no 客户号, "
				+"        p1.name 五层分类 , "
				+"        nvl(p.ck_curr_zcy, 0) * (to_number(c.claim_prop) / 100) as 存款时点余额, "
				+"        nvl(p.pa_nrj_zcy, 0) * (to_number(c.claim_prop) / 100) as 存款日均余额, "
				+"  	  nvl(p.bn_fic_nrj,0)* (to_number(c.claim_prop) / 100) as 表内理财日均, "
				+"        nvl(p.bw_fic_nrj,0)* (to_number(c.claim_prop) / 100) as 表外理财日均, "
//				+"        to_char((decode(nvl(p.loan_curr_zcy, 0), 0, '-', round(p.ck_curr_zcy / p.loan_curr_zcy, 4)))) as 存贷比时点, "
//				+"        to_char((decode(nvl(p.loan_nrj_zcy, 0), 0, '-', round(p.pa_nrj_zcy / p.loan_nrj_zcy, 4)))) as 存贷比日均,      "
				+"        (nvl(p.int_inc, 0) + nvl(p.middle_income, 0) + nvl(p.splitting, 0)) * "
				+"        (to_number(c.claim_prop) / 100) as 营收, "
				+"        to_char(decode(p.is_dfx, 1, 20, 0) + decode(p.is_jscard, 1, 20, 0) + "
				+"                decode(p.is_dxt, 1, 20, 0) + decode(p.is_hdx, 1, 20, 0) + "
				+"                decode(p.is_bocnet, 1, 20, 0)) || '%' as 产品覆盖率 "
				+" from pccm_cust_claim c "
				+" inner join pccm_cust_base_info p on p.id||p.cust_no = c.cust_pool_id  "
				+" left join gcms_param_info p1 on p.five_level = p1.val and p1.jkey = 'clas_five' "
				+" left join sys_org_info o on o.bancsid = p.br_no "
				+" where c.incflg='4' "
				+"  	and c.del_stat = '0' "
				+"    and (nvl(p.int_inc, 0) + nvl(p.middle_income, 0) + nvl(p.splitting, 0) + nvl(p.pa_nrj_zcy, 0) + nvl(p.loan_nrj_zcy, 0) + nvl(p.bn_fic_nrj, 0) + nvl(p.bw_fic_nrj, 0)) <> 0 "
				+"    and p.id is not null "
//				+"    and p.cust_type ='"+custType+"' "
				+"    and p.data_date = '"+AppUtils.findGNewDate()+"' "
				+"    and c.claim_cust_mgr_id = '"+user_no+"' ";
			if(AppUtils.StringUtil(custType) != null){
				if("N".equals(custType)){
					sql += "    and p.cust_type is null ";
				}else{
					sql += "    and p.cust_type = '"+custType+"' ";
				}
			}
		}
		return sql;
		
	};
	
	/**
	 * 我的客户他行客户明细下载SQL
	 */
	public static final String SQL_MY_CUST_OTHER (String user_no){
		String sql = null;
		if(AppUtils.StringUtil(user_no) != null){
			sql=" select t.name             as 所属区域, "
				+"        p.customername 客户名称, "
				+"        p.dummy_cust_no 虚拟客户号, "
				+"        p.relate_cust_name 关联客户名称, "
				+"        u.name             关联客户经理, "
				+"        u.phone            关联客户经理联系方式 "
				+" from pccm_cust_claim c "
				+"  inner join pccm_cust_pool p on p.id = c.cust_pool_id "
				+"  left join sys_user_info u on p.cust_manager = u.id "
				+"  left join pccm_provinces_citys t on p.area_code = t.id "
				+"  where 1 = 1 "
				+"    and p.incflg <> '4' "
				+"    and p.area_code is not null "
				+"    and c.del_stat = '0' "
				+"    and c.claim_cust_mgr_id = '"+user_no+"' ";;
		}
		return sql;
	};
	
	/**
	 * 客户认领我行客户明细下载SQL
	 */
	public static final String SQL_CUST_CLAIM_MY = " select o.bancsid 责任中心号, "
			+"        o.orgname 责任中心名称, "
			+"        p.customercode 客户号, "
			+"        p.customername 客户名称, "
			+"        p1.name 五层分类, "
			+"        decode(p.cussts,'3','-',ceil(to_date(p.indate, 'yyyyMMdd') + nvl('10', 0) - now())) 剩余认领时间天, "
			+"        p3.name 客户状态, "
			+"        to_char(nvl(c.claim_prop_all,0)||'%') 已认领比例, "
			+"        to_char(nvl(c.to_claim_prop,100)||'%') 待认领比例, "
			+"        p.befor_cust_mgr_name 上任客户经理 "
			+"   from ap_pccm.pccm_cust_pool p "
//			+"   left join (select a.cust_pool_id, "
//			+"                    sum(to_number(nvl(a.claim_prop, '0'))) claim_prop_all, "
//			+"                    (100 - sum(to_number(nvl(a.claim_prop, '0')))) to_claim_prop "
//			+"               from (select m.cust_pool_id, "
//			+"                            m.claim_prop "
//			+"                       from ap_pccm.pccm_cust_claim m "
//			+"                      where m.del_stat = '0' "
//			+"                        and m.incflg = '4') a "
//			+"              group by a.cust_pool_id) c on p.id = c.cust_pool_id "
			+"   left join pccm_cust_pool_claim c on p.id = c.cust_pool_id and c.incflg = '4' "
			+"   left join ap_pccm.gcms_param_info p1 on p.clas_five = p1.val and p1.jkey = 'clas_five' "
			+"   left join ap_pccm.gcms_param_info p2 on p.custyp = p2.val and p2.jkey = 'incflg' "
			+"   left join ap_pccm.gcms_param_info p3 on p.cussts = p3.val and p3.jkey = 'cust_stat' "
			+"   left join ap_pccm.sys_org_info o on o.bancsid = p.org_id "
			+"  where 1 = 1 "
			+"    and p.deptlevel = '3' "
			+"    and p.customercode is not null ";
//			+"    and p.cust_type != '03' ";
//			+"    and p.third_indate is null ";
	
	/**
	 * 客户认领我行客户明细下载SQL
	 */
	public static final String SQL_CUST_CLAIM_MY( String org,String orgArr){
		String sql = null;
		if(AppUtils.StringUtil(org) != null){
			sql=" select t.bancsid 责任中心号, t.orgname 责任中心名称, t.customercode 客户号, t.customername 客户名称, "
					+"  t.five_name 五层分类,t.havedate 剩余认领时间天, t.type_name 客户状态, "
					+"  sum(t.claim_prop)||'%' 已认领比例, (100 - sum(t.claim_prop))||'%' 待认领比例, t.befor_cust_mgr_name 上任客户经理 "
					+" from ("
					+" select o.bancsid, o.orgname, p.customercode, p.customername, p1.name five_name, "
					+" ceil(to_date(p.indate, 'yyyyMMdd') + nvl('10', 0) - now()) havedate, p3.name type_name, "
					+" p.befor_cust_mgr_name,to_number(nvl(c.claim_prop, '0')) as claim_prop"
					+" from ap_pccm.pccm_cust_pool p "
					+"   left join ap_pccm.pccm_cust_claim c on p.id = c.cust_pool_id and c.del_stat = '0' and c.incflg = '4' "
					+"   left join ap_pccm.gcms_param_info p1 on p.clas_five = p1.val  and p1.jkey = 'clas_five' "
					+"   left join ap_pccm.gcms_param_info p2 on p.custyp = p2.val  and p2.jkey = 'incflg' "
					+"   left join ap_pccm.gcms_param_info p3 on p.cussts = p3.val  and p3.jkey = 'cust_stat' "
					+"   left join ap_pccm.sys_org_info o on o.bancsid = p.org_id"
					+" where 1 = 1  "
					+" and p.deptlevel = '3'  "
					+" and p.customercode is not null  "
					+" and p.cust_type != '03'  "
					+" and p.cussts in('3')  ";
			if(AppUtils.StringUtil(org) != null){
				sql += " and p.orgnum in "+orgArr;
			}
			sql += " ) t"
				+" group by t.bancsid, t.orgname, t.customercode, t.customername,t.five_name,t.havedate, t.type_name, t.befor_cust_mgr_name";;
		}
		return sql;
	}
	
	/**
	 * 客户认领他行客户明细下载SQL
	 */
	public static final String SQL_CUST_CLAIM_OTHER = " select p.dummy_cust_no 虚拟客户号, "
			+"        p.customername 客户名称, "
			+"        t.name 行政区域, "
			+"        p.relate_cust_name 关联客户名称, "
			+"        u.name 关联客户经理, "
			+"        u.phone 关联客户经理联系方式, "
			+"        ceil(to_date(p.indate, 'yyyyMMdd') + nvl('10', 0) - now()) 剩余认领时间天, "
			+"        p3.name 客户状态, "
			+"        to_char(nvl(c.claim_prop_all,0)||'%') 已认领比例, "
			+"        to_char(nvl(c.to_claim_prop,100)||'%') 待认领比例,   "
			+"        p.befor_cust_mgr_name 上任客户经理 "
			+"   from pccm_cust_pool p "
//			+"   left join (select cust_pool_id, "
//			+"                    sum(to_number(nvl(claim_prop, '0'))) claim_prop_all, "
//			+"                    (100 - sum(to_number(nvl(claim_prop, '0')))) to_claim_prop "
//			+"               from (select cust_pool_id, "
//			+"                            claim_prop "
//			+"                       from pccm_cust_claim "
//			+"                      where del_stat = '0' "
//			+"                        and incflg <> '4') a "
//			+"              group by cust_pool_id) c on p.id = c.cust_pool_id "
			+"   left join pccm_cust_pool_claim c on p.id = c.cust_pool_id and c.incflg <> '4' "
			+"   left join pccm_provinces_citys t on p.area_code = t.id "
			+"   left join sys_user_info u on p.cust_manager = u.id "
			+"   left join gcms_param_info p3 on p.cussts = p3.val and p3.jkey = 'cust_stat' "
			+"   left join sys_org_info o on o.bancsid = p.org_id "
			+"  where 1 = 1 "
			+"    and p.customercode is null "
			+"    and p.area_code is not null ";
//			+"    and p.third_indate is null ";
	
	/**
	 * PA不为零客户明细下载SQL
	 */
	public static final String SQL_CUST_PA = " select soi.orgname 责任中心名称, "
			+"        p.name as 客户名称, "
			+"        p.cust_no as 客户号, "
			+"        p.account_contrib_before_tax as 税费前利润 "
			+"   from pccm_cust_base_info p "
			+"   left join sys_org_info soi on soi.id = p.id "
			+"  where 1=1 "
			+"    and pa_not_zero = '1' "
//			+"    and p.cust_type = '02' "
//			+"    and (nvl(p.int_inc, 0) + nvl(p.middle_income, 0) + nvl(p.splitting, 0) + nvl(p.pa_nrj_zcy, 0) + nvl(p.loan_nrj_zcy, 0) + nvl(p.bn_fic_nrj, 0) + nvl(p.bw_fic_nrj, 0)) <> 0 "
			+"    and p.id is not null ";
	
	/**
	 * 有效户客户明细下载SQL
	 */
	public static final String SQL_CUST_VALID = " select si.orgname 责任中心名称, "
			+"		p.name as 客户名称, "
			+"		p.cust_no as 客户号, "
			+"		case p.valid_cust when '1' then '是' else '否' end 是否有效户, "
			+"		p.pa_nrj_zcy as 存款日均, "
			+" 	    p.bn_fic_nrj as 表内理财日均, "
			+" 	    p.account_contrib_before_tax as 税费前利润 "
			+"   from pccm_cust_base_info p "
			+"   left join sys_org_info si on p.id = si.id "
			+"  where 1=1 "
			+"    and pa_not_zero = '1' "
//			+"    and p.cust_type = '02' "
//			+"    and (nvl(p.int_inc, 0) + nvl(p.middle_income, 0) + nvl(p.splitting, 0) + nvl(p.pa_nrj_zcy, 0) + nvl(p.loan_nrj_zcy, 0) + nvl(p.bn_fic_nrj, 0) + nvl(p.bw_fic_nrj, 0)) <> 0 "
			+"    and p.id is not null ";
	
	/**
	 *表头
	 */
	public static final String HEAD_CUST_CNT_MY = "机构名称,客户名称,客户号,五层分类,存款时点余额,存款日均余额,表内理财日均,表外理财日均,营收,产品覆盖率";
	public static final String HEAD_CUST_CNT_OTHER = "所属区域,客户名称,虚拟客户号,关联客户名称,关联客户经理,关联客户经理联系方式";
	public static final String HEAD_MY_CUST_MY = "责任中心名称,客户名称,客户号,五层分类,存款时点余额,存款日均余额,表内理财日均,表外理财日均,营收,产品覆盖率";
	public static final String HEAD_MY_CUST_OTHER = "所属区域,客户名称,虚拟客户号,关联客户名称,关联客户经理,关联客户经理联系方式";
	public static final String HEAD_CUST_CLAIM_MY = "责任中心号,责任中心名称,客户号,客户名称,五层分类,剩余认领时间天,客户状态,已认领比例,待认领比例,上任客户经理";
	public static final String HEAD_CUST_CLAIM_OTHER = "虚拟客户号,客户名称,行政区域,关联客户名称,关联客户经理,关联客户经理联系方式,剩余认领时间天,客户状态,已认领比例,待认领比例,上任客户经理";
	public static final String HEAD_CUST_PA = "责任中心名称,客户号,客户名称,税费前利润";
	public static final String HEAD_CUST_VALID = "责任中心名称,客户名称,客户号,是否有效户,存款日均,表内理财日均,税费前利润";
			
	public static final String ROLE_SYSMANAGE_ZH = "";
	public static final String ROLE_SYSMANAGE_FH = "";
}
