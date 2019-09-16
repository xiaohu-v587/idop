package com.goodcol.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;

public class KplUtil {
	
	public static Logger log = Logger.getLogger(KplUtil.class);
	//大公司kpi计算参数标识
	private static String kpl_big_comp_standard = "major_company_kpi";
	//大公司kpi类型标识
	private static String kpl_big_comp_mgr_type = "1";
	//计算PA贷款年日均参数标识
	private static String kpl_padk = "pa_loan";
	//计算PA存款年日均参数标识
	private static String kpl_pack = "pa_deposit";
	//计算集团客户中成员参数标识
	private static String kpl_group_cust = "group_cust";
	//计算客户年收益参数标识
	private static String kpl_cust_income = "cust_income";
	//中小企业kpi计算参数标识
	private static String kpl_mid_sma_comp_standard = "minor_enterprise_kpi";
	//中小企业kpi类型标识
	private static String kpl_mid_sma_comp_type = "2";
	//金融机构kpi计算参数标识
	private static String kpl_fina_standard = "financial_institution_kpi";
	//金融机构kpi类型标识
	private static String kpl_fina_type = "4";
	//行政事业kpi类型标识
	private static String kpl_admi_type = "5";
	//保留三位小数
	private static DecimalFormat df = new DecimalFormat(".000");
	//营业收入指标参数
	private static String busi_inc_zb = "busine_income";
	//客户数（折）指标参数
	private static String cust_count_zb = "cust_num";
	//客户效能指标参数
	//private static String cust_effect_zb = "cust_effect";
	//产品效能指标参数
	//private static String pro_effect_zb = "cust_num";
	//客户日均存款指标参数
	private static String deposit_day_zb = "deposite";
	
	//跑批时间测试
	//private static String excuTime = "";
	
	/**
	 * kpl计算
	 * @throws Exception 
	 */
	public static void kplTask(String excuTime) throws Exception{
		//跑批前先删除属于此批次的数据
		delData(excuTime);
		try {
			kpl_big_comp(excuTime);
		} catch (Exception e) {
			log.error("大公司条线客户经理kpl计算异常", e);
			throw new Exception("大公司条线客户经理kpl计算异常",e);
		}
//		try {
//			kpl_mid_sma_comp(excuTime);
//		} catch (Exception e) {
//			log.error("中小企业客户经理kpl计算异常", e);
//			throw new Exception("中小企业客户经理kpl计算异常",e);
//		}
//		try {
//			kpl_fina(excuTime);
//		} catch (Exception e) {
//			log.error("金融机构客户经理kpl计算异常", e);
//			throw new Exception("金融机构客户经理kpl计算异常",e);
//		}
//		try {
//			kpl_admi(excuTime);
//		} catch (Exception e) {
//			log.error("行政事业客户经理kpl计算异常", e);
//			throw new Exception("行政事业客户经理kpl计算异常",e);
//		}
		
//		try {
//			kpl_acc(excuTime);
//		} catch (Exception e) {
//			log.error("账户经理kpl计算异常", e);
//		}
	}
	
	/**
	 * 大公司客户经理kpl计算
	 */
	public static void kpl_big_comp(String excuTime){
		//设置参数
		int inc_para=0;				//营业收入权重
		int inc_score_max=0;		//营业收入分值上限
		int cust_num_para=0;		//客户数权重
		int cust_num_score_max=0;	//客户数分值上限
		int deduct_score_zb=0;		//风险问责扣分上限
		int stand_score=0;		//达标分值

		//读取大公司客户经理kpl计算参数
		List<Record> pralist = Db.use("default").find(" select name,val from gcms_param_info where key='kpl_big_comp_para' ");
		if(null!=pralist&&pralist.size()>0){
			for(int a = 0; a < pralist.size(); a++){
				switch ((String)pralist.get(a).get("name")) {
				case "营业收入权重":
					inc_para = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				case "营业收入分值上限":
					inc_score_max = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				case "客户数权重":
					cust_num_para = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				case "客户数分值上限":
					cust_num_score_max = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				case "风险问责扣分上限":
					deduct_score_zb = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				case "达标分值":
					stand_score = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				default:
					break;
				}
			}
		}else{
			log.error("未读取到大公司客户经理kpl计算参数！");return;
		}
		
		double big_comp_kpl=0;//大公司条线客户经理KPI
		double busi_inc_score=0;//营业收入
		double cust_num_score=0;//客户数
		double risk_acc_score=0;//风险问责
		
		//查询出各个大公司客户经理数据
		//使用客户经理号关联 查出相应客户经理数据
		int standTime = Integer.parseInt(getStandTime(excuTime));//执行月份的15号
		List<Record> custlist = null;
		String custmgrlistSql=" select cust_mgr_no, "
							+"       sum(cust_inc) cust_inc, "
							+"       sum(ck_nrj) ck_nrj, "
							+"       sum(loan_nrj) loan_nrj, "
							+"       c2.user_level, "
							+"       count(1) cn, "
							+"       cust_mgr_name "
							+"  from (select c.claim_cust_mgr_id cust_mgr_no, "
							+"               c.claim_cust_mgr_name cust_mgr_name, "
							+"               (ck_int_inc + loan_int_inc + int_inc + operating_inc_contrib + "
							+"               account_contrib_before_tax) * (to_number(c.claim_prop) / 100) cust_inc, "
							+"               (ck_nrj_cny + ck_nrj_wb) * (to_number(c.claim_prop) / 100) ck_nrj, "
							+"               (loan_nrj_cny + loan_nrj_wb) * "
							+"               (to_number(c.claim_prop) / 100) loan_nrj "
							+"          from pccm_cust_base_info b "
							+"           left join pccm.pccm_cust_pool m "
							+"             on m.customercode = b.cust_no "
							+"            and m.orgnum = b.id "
							+"           left join pccm.pccm_cust_claim c "
							+"             on c.cust_pool_id = m.id "
							+"         where c.del_stat = '0' "
							+"           and to_number(c.claim_time) <= ? "
							+"        union all "
							+"        select c.claim_cust_mgr_id cust_mgr_no, "
							+"               c.claim_cust_mgr_name cust_mgr_name, "
							+"               p.loan_bal_day * (to_number(c.claim_prop) / 100) ck_nrj, "
							+"               p.in_bal_day * (to_number(c.claim_prop) / 100) loan_nrj, "
							+"               (p.net_rate + p.out_rate_income + p.splitting_rate_income + "
							+"               p.middle_income + p.splitting_income) * "
							+"               (to_number(c.claim_prop) / 100) cust_inc "
							+"          from pccm_cust_pa p "
							+"           left join pccm.pccm_cust_pool m "
							+"             on m.customercode = p.cust_no "
							+"            and m.orgnum = b.id "
							+"           left join pccm.pccm_cust_claim c "
							+"             on c.cust_pool_id = m.id "
							+"         where c.del_stat = '0' "
							+"           and to_number(c.claim_time) < = ?) c1 "
							+"  left join sys_user_info c2 "
							+"    on c1.cust_mgr_no = c2.user_no "
							+"  left join gcms_role_apply c3 "
							+"    on c2.user_no = c3.user_id "
							+"  left join gcms_param_info c4 "
							+"    on c4.id = c3.role_id "
							+" where c4.val = '1' "
							+"   and c3.apply_status = '1' "
							+" group by cust_mgr_no, cust_mgr_name, c2.user_level ";
		List<Record> custmgrlist = Db.use("default").find(custmgrlistSql,new Object[]{standTime,standTime});
		if(null!=custmgrlist&&custmgrlist.size()>0){
			String cust_mgr_no = null;//客户经理号
			String cust_mgr_name = null;//客户经理号
			//String cust_mgr_type = null;//客户经理类型
			String cust_mgr_level = null;//客户经理职级
			double cust_inc_mgr=0;//客户经理营收
			double ck_nrj_mgr=0;	//客户经理存款日均
			double loan_nrj_mgr=0;	//客户经理贷款日均
			double cust_num_mgr=0;	//客户经理实际维护客户成员数
			
			double cust_memb_num=0;	//客户经理实际维护集团客户成员数
			
			double cust_num_ck=0;	//存款客户数
			double cust_num_loan=0;	//贷款客户数
			double cust_num_comp=0;	//集团客户数
			double cust_num_kpi=0;//客户经理KPI计算客户数
			
			double cust_inc_target=0;//营收指标
			double cust_num_target=0;	//客户数指标
			for(int i = 0; i < custmgrlist.size(); i++){
				cust_mgr_no = custmgrlist.get(i).getStr("cust_mgr_no");
				cust_mgr_name = custmgrlist.get(i).getStr("cust_mgr_name");
				cust_inc_mgr = custmgrlist.get(i).getBigDecimal("cust_inc").doubleValue();
				ck_nrj_mgr = custmgrlist.get(i).getBigDecimal("ck_nrj").doubleValue();
				loan_nrj_mgr = custmgrlist.get(i).getBigDecimal("loan_nrj").doubleValue();
				cust_num_mgr = custmgrlist.get(i).getBigDecimal("cn").doubleValue();
				cust_mgr_level = custmgrlist.get(i).getStr("user_level");
				if(AppUtils.StringUtil(cust_mgr_level)==null){
					log.error(cust_mgr_no+cust_mgr_name+"未读到客户经理级别！");break;
				}
				//根据客户经理职级，营业收入达标值，客户数达标值
				cust_inc_target = getTarget(busi_inc_zb, kpl_big_comp_mgr_type, cust_mgr_level);
				cust_num_target = getTarget(cust_count_zb, kpl_big_comp_mgr_type, cust_mgr_level);;
				
				//计算KPI（营收）=70×客户经理营收/客户经理职级达标值，上限126分。
				busi_inc_score = Double.parseDouble(df.format(inc_para*cust_inc_mgr/cust_inc_target));
				busi_inc_score = busi_inc_score>inc_score_max?inc_score_max:busi_inc_score;
				//xxxx;
				//根据客户经理号 查询相应客户数据 处理 
				custlist =  Db.use("default").find("select cust_mgr_no, cust_inc*(to_number(claim_prop) / 100) cust_inc, ck_nrj, loan_nrj,clas_five, claim_prop,cust_name,cust_no,inc_type,group_flag "
						+" from (select c.claim_cust_mgr_id cust_mgr_no, (ck_int_inc + loan_int_inc + int_inc + operating_inc_contrib + "
						+" account_contrib_before_tax) cust_inc, (ck_nrj_cny + ck_nrj_wb) ck_nrj,p.clas_five, "
						+" (loan_nrj_cny + loan_nrj_wb) loan_nrj, c.claim_prop,name cust_name,b.cust_no, '1' inc_type,group_flag "
						+" from pccm_cust_base_info b left join pccm_cust_claim c on b.cust_no = c.cust_no "
						+" left join pccm_cust_pool p on b.cust_no = p.customercode "
						+" where c.del_stat = '0' and to_number(c.claim_time)<=? "
						+" union all  "
						+" select c.claim_cust_mgr_id cust_mgr_no, p.loan_bal_day ck_nrj, p.in_bal_day loan_nrj,o.clas_five, "
						+" (p.net_rate + p.out_rate_income + p.splitting_rate_income + "
						+" p.middle_income + p.splitting_income) cust_inc, c.claim_prop,cust_name,p.cust_no, '2' inc_type,null as group_flag"
						+" from pccm_cust_pa p left join pccm_cust_claim c on c.cust_no = p.cust_no left join pccm_cust_pool o on p.cust_no = o.customercode "
						+" where c.del_stat = '0' and to_number(c.claim_time)<=?) c1 where cust_mgr_no=? ",new Object[]{standTime,standTime,cust_mgr_no});
				List<Map<String, Object>>  maplist=null;
				if(null!=custlist&&custlist.size()>0){
					String custName=null;//客户名称
					String custNo=null;//客户号
					String clasFive=null;//五层分类
					String groupFlag=null;//集团客户标识	
					String claimPro=null;//认领占比
					String inctype=null;//1宽表数据 2 pa数据
					double custInc=0;//客户营收
					double ck_nrj=0;	//客户存款日均
					double loan_nrj=0;	//客户贷款日均
					double custNumCk=0;	//存款客户数
					double custNumLoan=0;	//贷款客户数
					double custMembNum=0;	//集团客户数
					double custNumMan=0;//KPI计算客户数
					maplist= new ArrayList<Map<String, Object>>();
					Map<String, Object> map = null;
					for(int n=0;n<custlist.size();n++){
						custName = custlist.get(n).getStr("cust_name");
						inctype = custlist.get(n).getStr("inc_type");
						claimPro = custlist.get(n).getStr("claim_prop");
						clasFive = custlist.get(n).getStr("clas_five");
						custNo = custlist.get(n).getStr("cust_no");
						groupFlag = custlist.get(n).getStr("group_flag");
						custInc = custlist.get(n).getBigDecimal("cust_inc").doubleValue();
						ck_nrj = custlist.get(n).getBigDecimal("ck_nrj").doubleValue();
						loan_nrj = custlist.get(n).getBigDecimal("loan_nrj").doubleValue();
						
						if("1".equals(inctype)){//宽表数据
							//集团客户折算客户数
							custMembNum = "1".equals(groupFlag)?(Double.parseDouble(claimPro)/100):0;
						} else if("2".equals(inctype)){
							//计算贷款客户数
							custNumLoan = getDouVal(kpl_big_comp_standard, kpl_padk, loan_nrj)*(Double.parseDouble(claimPro)/100);
							//计算存款客户数
							custNumCk = getDouVal(kpl_big_comp_standard, kpl_pack, ck_nrj)*(Double.parseDouble(claimPro)/100);
						}
						
						//判断认领比例是否是100%  
						if("100".equals(claimPro)){
							//客户客户数折算=贷款客户数+存款客户数+集团客户数
							custNumMan = BigDecimal.valueOf(custNumLoan).
									add(BigDecimal.valueOf(custNumCk)).
									add(BigDecimal.valueOf(custMembNum)).doubleValue();
							//客户经理计算贷款客户数
							cust_num_loan = BigDecimal.valueOf(cust_num_loan).
									add(BigDecimal.valueOf(custNumLoan)).doubleValue();
							//客户经理计算存款客户数
							cust_num_ck = BigDecimal.valueOf(cust_num_ck).
									add(BigDecimal.valueOf(custNumCk)).doubleValue();
						}else{
							//贷款客户数存款客户数 选择孰高值进行计算
							if(custNumLoan>=custNumCk){
								//客户客户数折算=贷款客户数+集团客户数
								custNumMan = BigDecimal.valueOf(custNumLoan).
										add(BigDecimal.valueOf(custMembNum)).doubleValue();
								//客户经理计算贷款客户数
								cust_num_loan = BigDecimal.valueOf(cust_num_loan).
										add(BigDecimal.valueOf(custNumLoan)).doubleValue();
							}else{
								//客户客户数折算=存款客户数+集团客户数
								custNumMan = BigDecimal.valueOf(custNumCk).
										add(BigDecimal.valueOf(custMembNum)).doubleValue();
								//客户经理计算存款客户数
								cust_num_ck = BigDecimal.valueOf(cust_num_ck).
										add(BigDecimal.valueOf(custNumCk)).doubleValue();
							}
						}
						
						//客户经理计算集团客户数
						cust_memb_num = BigDecimal.valueOf(cust_memb_num).
								add(BigDecimal.valueOf(custMembNum)).doubleValue();
						
						//插入kpi客户贡献详情表
						Db.use("default").update(" insert into pccm_kpi_cust_info (id, period, cust_no, cust_name, claim_prop, "
							+" busi_inc,cust_num, clas_five, create_time, cust_mgr_no, cust_mgr_name) "
							+" values (?, ?, ?, ?, ?, ?, ?,  ?, to_char(sysdate,'yyyyMMddhh24miss'), ?, ?)", 
							new Object[] { AppUtils.getStringSeq(),excuTime,custNo,custName,claimPro,custInc,custNumMan,
									clasFive,cust_mgr_no,cust_mgr_name });
					
						map = new HashMap<String, Object>();
						map.put("custNumMan", custNumMan);
						map.put("cust_inc", custInc);
						map.put("custNo", custNo);
						maplist.add(map);
					}
				}
				
				//计算集团客户数
				cust_num_comp = getDouVal(kpl_big_comp_standard, kpl_group_cust, cust_memb_num);
				
				//客户经理客户数=贷款客户数+存款客户数+集团客户数
				cust_num_kpi = BigDecimal.valueOf(cust_num_loan).add(BigDecimal.valueOf(cust_num_ck)).add(BigDecimal.valueOf(cust_num_comp)).doubleValue();
				//KPI（客户数）=30×客户经理客户数/客户经理职级达标值，上限54分。
				cust_num_score = Double.parseDouble(df.format(cust_num_para*cust_num_kpi/cust_num_target));
				cust_num_score = cust_num_score>cust_num_score_max?cust_num_score_max:cust_num_score;
				
				//风险问责 当月记录查询
				risk_acc_score = Db.use("default").queryBigDecimal("select nvl(sum(deduction),0) from pccm_cust_puni where del_stat='0' " +
						"and cust_id=? ", new Object[] {cust_mgr_no}).doubleValue();
			    //近两年内，客户经理风险问责扣分
				double risk_acc_two_years = Db.use("default").queryBigDecimal("select nvl(sum(deduction),0) from pccm_cust_puni " +
						"where del_stat='0' and create_time>(sysdate-interval '2' year) and cust_id=? ",
						new Object[] {cust_mgr_no}).doubleValue();
				if(risk_acc_two_years<deduct_score_zb){
					//大公司客户经理价值创造=营业收入+客户数+风险问责
					big_comp_kpl = BigDecimal.valueOf(busi_inc_score)
							.add(BigDecimal.valueOf(cust_num_score))
							.subtract(BigDecimal.valueOf(risk_acc_score)).doubleValue();
				}else{
					//近两年内，客户经理风险问责扣分达到30分的，KPI得分＜达标分值100。
					if(big_comp_kpl>=stand_score){
						big_comp_kpl = stand_score-1;
					}
				}
				//将计算的数据按照客户经理号，日期插入客户经理kpi表中
				Db.use("default").update(" insert into pccm_kpi_info(id,period,cust_mgr_no,kpi,create_time,"
						+" cust_num_mgr,cust_kpi_num,cust_busi_inc,ck_nrj_mgr,loan_nrj_mgr) "
						+" values(?,?,?,?,sysdate,?,?,?,?,?) " ,
						new Object[] { AppUtils.getStringSeq(),excuTime,cust_mgr_no,big_comp_kpl,cust_num_mgr,
								cust_num_kpi,cust_inc_mgr,ck_nrj_mgr,loan_nrj_mgr });
				
				//计算客户对客户经理的KPI贡献值
				if(null!=maplist&&maplist.size()>0){
					String custNo=null;//客户号
					double custNum=0;//KPI计算客户数
					double custKpi=0;//KPI贡献值
					double cust_inc=0;//KPI营收
					double inc_kpi=0;//KPI营收
					for(int c=0;c<maplist.size();c++){
						custNo = (String) maplist.get(c).get("custNo");
						custNum = (Double)maplist.get(c).get("custNumMan");
						cust_inc = (Double)maplist.get(c).get("cust_inc");
						if(0!=cust_num_kpi&&0!=cust_memb_num){
							custKpi = Double.parseDouble(df.format(custNum/cust_memb_num*cust_num_score));
						}
						if(0!=cust_inc_mgr){
							inc_kpi = Double.parseDouble(df.format(cust_inc/cust_inc_mgr*busi_inc_score));
						}
						Db.use("default").update(" update pccm_kpi_cust_info set kpi=?,busi_inc_kpi=?,cust_num_kpi=? where cust_no=? and period=? and cust_mgr_no=? " ,
								new Object[] { BigDecimal.valueOf(custKpi)
								.add(BigDecimal.valueOf(inc_kpi)).doubleValue(),inc_kpi,custKpi,custNo,excuTime,cust_mgr_no});
					}
				}
			}
			
			
		}
	}
	
	/**
	 * 中小企业客户经理kpl计算
	 */
	public static void kpl_mid_sma_comp(String excuTime){		
		//设置参数
		int inc_para=0;				//营业收入权重
		int inc_score_max=0;		//营业收入分值上限
		int cust_num_para=0;		//客户数权重
		int cust_num_score_max=0;	//客户数分值上限
		int deduct_score_zb=0;		//风险问责扣分上限
		int stand_score=0;		//达标分值

		//读取中小企业客户经理kpl计算参数
		List<Record> pralist = Db.use("default").find(" select name,val from gcms_param_info where key='kpl_mid_sma_para' ");
		if(null!=pralist&&pralist.size()>0){
			for(int a = 0; a < pralist.size(); a++){
				switch ((String)pralist.get(a).get("name")) {
				case "营业收入权重":
					inc_para = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				case "营业收入分值上限":
					inc_score_max = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				case "客户数权重":
					cust_num_para = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				case "客户数分值上限":
					cust_num_score_max = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				case "风险问责扣分上限":
					deduct_score_zb = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				case "达标分值":
					stand_score = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				default:
					break;
				}
			}
		}else{
			log.error("未读取到中小企业客户经理kpl计算参数！");return;
		}
		
		double kpl_mid_sma_comp=0;//中小企业客户经理KPI
		double busi_inc_score=0;//营业收入
		double cust_num_score=0;//客户数
		double risk_acc_score=0;//风险问责
		
		//查询出各个中小企业客户经理数据
		//使用客户经理号关联 查出相应客户经理数据
		int standTime = Integer.parseInt(getStandTime(excuTime));//执行月份的15号
		List<Record> custlist = null;
		String custmgrlistSql=" select cust_mgr_no, "
				+"       sum(cust_inc) cust_inc, "
				+"       sum(ck_nrj) ck_nrj, "
				+"       sum(loan_nrj) loan_nrj, "
				+"       c2.user_level, "
				+"       count(1) cn, "
				+"       cust_mgr_name "
				+"  from (select c.claim_cust_mgr_id cust_mgr_no, "
				+"               cust_mgr_name, "
				+"               (ck_int_inc + loan_int_inc + int_inc + operating_inc_contrib + "
				+"               account_contrib_before_tax) * (to_number(c.claim_prop) / 100) cust_inc, "
				+"               (ck_nrj_cny + ck_nrj_wb) * (to_number(c.claim_prop) / 100) ck_nrj, "
				+"               (loan_nrj_cny + loan_nrj_wb) * "
				+"               (to_number(c.claim_prop) / 100) loan_nrj "
				+"          from pccm_cust_base_info b "
				+"          left join pccm_cust_claim c "
				+"            on c.cust_no = b.cust_no "
				+"         where c.del_stat = '0' "
				+"           and to_number(c.claim_time) <= ? "
				+"        union all "
				+"        select c.claim_cust_mgr_id cust_mgr_no, "
				+"               c.claim_cust_mgr_name cust_mgr_name, "
				+"               p.loan_bal_day * (to_number(c.claim_prop) / 100) ck_nrj, "
				+"               p.in_bal_day * (to_number(c.claim_prop) / 100) loan_nrj, "
				+"               (p.net_rate + p.out_rate_income + p.splitting_rate_income + "
				+"               p.middle_income + p.splitting_income) * "
				+"               (to_number(c.claim_prop) / 100) cust_inc "
				+"          from pccm_cust_pa p "
				+"          left join pccm_cust_claim c "
				+"            on c.cust_no = p.cust_no "
				+"         where c.del_stat = '0' "
				+"           and to_number(c.claim_time) < = ?) c1 "
				+"  left join sys_user_info c2 "
				+"    on c1.cust_mgr_no = c2.user_no "
				+"  left join gcms_role_apply c3 "
				+"    on c2.user_no = c3.user_id "
				+"  left join gcms_param_info c4 "
				+"    on c4.id = c3.role_id "
				+" where c4.val = '2' "
				+"   and c3.apply_status = '1' "
				+" group by cust_mgr_no, cust_mgr_name, c2.user_level ";
		List<Record> custmgrlist = Db.use("default").find(custmgrlistSql,new Object[]{standTime,standTime});
		if(null!=custmgrlist&&custmgrlist.size()>0){
			String cust_mgr_no = null;//客户经理号
			String cust_mgr_name = null;//客户经理号
			//String cust_mgr_type = null;//客户经理类型
			String cust_mgr_level = null;//客户经理职级
			double cust_inc_mgr=0;//客户经理营收
			double ck_nrj_mgr=0;	//客户经理存款日均
			double loan_nrj_mgr=0;	//客户经理贷款日均
			double cust_num_mgr=0;	//客户经理实际维护客户成员数
			
			//double cust_memb_num=0;	//客户经理实际维护集团客户成员数
			
			double sxcust_num=0;	//授信客户数
			double cust_num_ck=0;	//存款客户数
			double cust_num_loan=0;	//贷款客户数
			double cust_num_comp=0;	//担保公司客户数
			double cust_num_kpi=0;//客户经理KPI计算客户数
			
			double cust_inc_target=0;//营收指标
			double cust_num_target=0;	//客户数指标
			for(int i = 0; i < custmgrlist.size(); i++){
				cust_mgr_no = custmgrlist.get(i).getStr("cust_mgr_no");
				cust_mgr_name = custmgrlist.get(i).getStr("cust_mgr_name");
				cust_inc_mgr = custmgrlist.get(i).getBigDecimal("cust_inc").doubleValue();
				ck_nrj_mgr = custmgrlist.get(i).getBigDecimal("ck_nrj").doubleValue();
				loan_nrj_mgr = custmgrlist.get(i).getBigDecimal("loan_nrj").doubleValue();
				cust_num_mgr = custmgrlist.get(i).getBigDecimal("cn").doubleValue();
				cust_mgr_level = custmgrlist.get(i).getStr("user_level");
				//根据客户经理职级，营业收入达标值，客户数达标值
				cust_inc_target = getTarget(busi_inc_zb, kpl_mid_sma_comp_type, cust_mgr_level);
				cust_num_target = getTarget(cust_count_zb, kpl_mid_sma_comp_type, cust_mgr_level);
				
				//计算KPI（营收）=60×客户经理营收/客户经理职级达标值，上限108分。
				busi_inc_score = Double.parseDouble(df.format(inc_para*cust_inc_mgr/cust_inc_target));
				busi_inc_score = busi_inc_score>inc_score_max?inc_score_max:busi_inc_score;
				//xxxxx;
				//根据客户经理号 查询相应客户数据 处理 
				custlist =  Db.use("default").find("select cust_mgr_no, cust_inc*(to_number(claim_prop) / 100) cust_inc, clas_five,ck_nrj, loan_nrj, claim_prop,cust_name,cust_no, inc_type,is_sxcust "
						+" from (select c.claim_cust_mgr_id cust_mgr_no, (ck_int_inc + loan_int_inc + int_inc + operating_inc_contrib + "
						+" account_contrib_before_tax) cust_inc, (ck_nrj_cny + ck_nrj_wb) ck_nrj, p.clas_five, "
						+" (loan_nrj_cny + loan_nrj_wb) loan_nrj, c.claim_prop,name cust_name,b.cust_no, '1' inc_type,is_sxcust "
						+" from pccm_cust_base_info b left join pccm_cust_claim c on b.cust_no = c.cust_no "
						+" left join pccm_cust_pool p on b.cust_no = p.customercode "
						+" where c.del_stat = '0' and to_number(c.claim_time)<=? "
						+" union all  "
						+" select c.claim_cust_mgr_id cust_mgr_no, p.loan_bal_day ck_nrj, p.in_bal_day loan_nrj,o.clas_five, "
						+" (p.net_rate + p.out_rate_income + p.splitting_rate_income + "
						+" p.middle_income + p.splitting_income) cust_inc, c.claim_prop,cust_name,p.cust_no, '2' inc_type,null as is_sxcust"
						+" from pccm_cust_pa p left join pccm_cust_claim c on c.cust_no = p.cust_no left join pccm_cust_pool o on p.cust_no = o.customercode"
						+" where c.del_stat = '0' and to_number(c.claim_time)<?) c1 where cust_mgr_no=? ",new Object[]{standTime,standTime,cust_mgr_no});
				List<Map<String, Object>>  maplist=null;
				if(null!=custlist&&custlist.size()>0){
					String custName=null;//客户名称
					String custNo=null;//客户号
					String clasFive=null;//五层分类
					String claimPro=null;//认领占比
					String is_sxcust=null;//是否授信客户
					String inctype=null;//1宽表数据 2 pa数据
					double custInc=0;//客户营收
					double ck_nrj=0;	//客户存款日均
					double loan_nrj=0;	//客户贷款日均
					double custNumCk=0;	//存款客户数
					double custNumLoan=0;	//贷款客户数
					double custSxcNum=0;	//授信客户数
					double custNumMan=0;//KPI计算客户数
					maplist= new ArrayList<Map<String, Object>>();
					Map<String, Object> map = null;
					for(int n=0;n<custlist.size();n++){
						custName = custlist.get(n).getStr("cust_name");
						custNo = custlist.get(n).getStr("cust_no");
						inctype = custlist.get(n).getStr("inctype");
						clasFive = custlist.get(n).getStr("clas_five");
						is_sxcust = custlist.get(n).getStr("is_sxcust");
						claimPro = custlist.get(n).getStr("claim_prop");
						custInc = custlist.get(n).getBigDecimal("cust_inc").doubleValue();
						ck_nrj = custlist.get(n).getBigDecimal("ck_nrj").doubleValue();
						loan_nrj = custlist.get(n).getBigDecimal("loan_nrj").doubleValue();
						
						if("1".equals(inctype)){//宽表数据
							//授信客户折算客户数
							custSxcNum = "1".equals(is_sxcust)?(Double.parseDouble(claimPro)/100):0;
						} else if("2".equals(inctype)){
							//计算贷款客户数
							custNumLoan = getDouVal(kpl_mid_sma_comp_standard, kpl_padk, loan_nrj)*(Double.parseDouble(claimPro)/100);
							//计算存款客户数
							custNumCk = getDouVal(kpl_mid_sma_comp_standard, kpl_pack, ck_nrj)*(Double.parseDouble(claimPro)/100);
						}
						
						sxcust_num = BigDecimal.valueOf(sxcust_num).
								add(BigDecimal.valueOf(custSxcNum)).doubleValue();
						
						//客户经理如分层认领客户，按分层以后的存贷款数进行计算；
						//授信客户符合①、②条件，选择孰高值进行计算；
						/***-担保公司客户符合②、③条件，选择孰高值进行计算。*******暂时没有条件判断是否是担保公司******/
						//判断认领比例是否是100%  
						if("100".equals(claimPro)){
							//客户客户数折算=贷款客户数+存款客户数+授信客户数
							custNumMan = BigDecimal.valueOf(custNumLoan).
									add(BigDecimal.valueOf(custNumCk)).doubleValue();
							//客户经理计算贷款客户数
							cust_num_loan = BigDecimal.valueOf(cust_num_loan).
									add(BigDecimal.valueOf(custNumLoan)).doubleValue();
							//客户经理计算存款客户数
							cust_num_ck = BigDecimal.valueOf(cust_num_ck).
									add(BigDecimal.valueOf(custNumCk)).doubleValue();
						}else{
							//贷款客户数存款客户数 选择孰高值进行计算
							if(custNumLoan>=custNumCk){
								//客户客户数折算=贷款客户数
								custNumMan = custNumLoan;
								//客户经理计算贷款客户数
								cust_num_loan = BigDecimal.valueOf(cust_num_loan).
										add(BigDecimal.valueOf(custNumLoan)).doubleValue();
							}else{
								//客户客户数折算=存款客户数
								custNumMan = custNumCk;
								//客户经理计算存款客户数
								cust_num_ck = BigDecimal.valueOf(cust_num_ck).
										add(BigDecimal.valueOf(custNumCk)).doubleValue();
							}
						}
						
						
						//插入kpi客户贡献详情表
						Db.use("default").update(" insert into pccm_kpi_cust_info (id, period, cust_no, cust_name, claim_prop, "
							+" busi_inc,cust_num, clas_five, create_time, cust_mgr_no, cust_mgr_name) "
							+" values (?, ?, ?, ?, ?, ?, ?,  ?, to_char(sysdate,'yyyyMMddhh24miss'), ?, ?)", 
							new Object[] { AppUtils.getStringSeq(),excuTime,custNo,custName,claimPro,custInc,custNumMan,
									clasFive,cust_mgr_no,cust_mgr_name });
					
						map = new HashMap<String, Object>();
						map.put("custNumMan", custNumMan);
						map.put("cust_inc", custInc);
						map.put("custNo", custNo);
						maplist.add(map);
					}
				}
				
				//计算担保公司：户数=取整（年末担保的授信客户数/3）
				sxcust_num = Math.floor(sxcust_num/3);
				
				//客户经理客户数=贷款客户数+存款客户数+集团客户数
				cust_num_kpi = BigDecimal.valueOf(cust_num_loan).add(BigDecimal.valueOf(cust_num_ck)).add(BigDecimal.valueOf(cust_num_comp)).doubleValue();
				//客户数：权重40%，上限180%；KPI（客户数）=40×客户经理客户数/客户经理职级达标值，上限72分。
				cust_num_score = Double.parseDouble(df.format(cust_num_para*cust_num_kpi/cust_num_target));
				cust_num_score = cust_num_score>cust_num_score_max?cust_num_score_max:cust_num_score;
				
				//风险问责 当月记录查询
				risk_acc_score = Db.use("default").queryBigDecimal("select nvl(sum(deduction),0) from pccm_cust_puni where del_stat='0' " +
						"and cust_id=? ", new Object[] {cust_mgr_no}).doubleValue();
			    //近两年内，客户经理风险问责扣分
				double risk_acc_two_years = Db.use("default").queryBigDecimal("select nvl(sum(deduction),0) from pccm_cust_puni " +
						"where del_stat='0' and create_time>(sysdate-interval '2' year) and cust_id=? ",
						new Object[] {cust_mgr_no}).doubleValue();
				if(risk_acc_two_years<deduct_score_zb){
					//中小企业客户经理价值创造=营业收入+客户数+风险问责
					kpl_mid_sma_comp = BigDecimal.valueOf(busi_inc_score)
							.add(BigDecimal.valueOf(cust_num_score))
							.subtract(BigDecimal.valueOf(risk_acc_score)).doubleValue();
				}else{
					//近两年内，客户经理风险问责扣分达到30分的，KPI得分＜达标分值100。
					if(kpl_mid_sma_comp>=stand_score){
						kpl_mid_sma_comp = stand_score-1;
					}
				}
				//将计算的数据按照客户经理号，日期插入客户经理kpi表中
				Db.use("default").update(" insert into pccm_kpi_info(id,period,cust_mgr_no,kpi,create_time,"
						+" cust_num_mgr,cust_kpi_num,cust_busi_inc,ck_nrj_mgr,loan_nrj_mgr) "
						+" values(?,?,?,?,sysdate,?,?,?,?,?) " ,
						new Object[] { AppUtils.getStringSeq(),excuTime,cust_mgr_no,kpl_mid_sma_comp,cust_num_mgr,
								cust_num_kpi,cust_inc_mgr,ck_nrj_mgr,loan_nrj_mgr });
				
				//计算客户对客户经理的KPI贡献值
				if(null!=maplist&&maplist.size()>0){
					String custNo=null;//客户号
					double custNum=0;//KPI计算客户数
					double custKpi=0;//KPI贡献值
					double cust_inc=0;//KPI营收
					double inc_kpi=0;//KPI营收
					for(int c=0;c<maplist.size();c++){
						custNo = (String) maplist.get(c).get("custNo");
						custNum = (Double)maplist.get(c).get("custNumMan");
						cust_inc = (Double)maplist.get(c).get("cust_inc");
						if(0!=cust_num_kpi){
							custKpi = Double.parseDouble(df.format(custNum/cust_num_kpi*cust_num_score));
						}
						if(0!=cust_inc_mgr){
							inc_kpi = Double.parseDouble(df.format(cust_inc/cust_inc_mgr*busi_inc_score));
						}
						Db.use("default").update(" update pccm_kpi_cust_info set kpi=?,busi_inc_kpi=?,cust_num_kpi=? where cust_no=? and period=? and cust_mgr_no=? " ,
								new Object[] { BigDecimal.valueOf(custKpi)
								.add(BigDecimal.valueOf(inc_kpi)).doubleValue(),inc_kpi,custKpi,custNo,excuTime,cust_mgr_no});
					}
				}
			}
			
			
		}
	}
	
	/**
	 * 账户经理kpl计算
	 */
	public static void kpl_acc(String excuTime){
//		账户经理价值创造（KPI）=客户效能维度+产品效能维度		
//		客户效能维度=账户经理实际客户效能/相应层级客户效能达标值×100
//		产品效能维度=账户经理实际产品效能/相应层级产品效能达标值×100
//		KPI得分= K1（权重）×客户效能维度+K2（权重）×产品效能维度。其中，K1客户效能维度权重为40%（上限72分），K2产品效能维度权重为60%（上限108分）。KPI得分上限不高于180分
//		备注：
//		客户效能=新开有效账户×0.2+潜力客户×0.5+优质客户×1+高端客户×1
//		产品效能=(对公短信通数+电子回单箱数+单位结算卡数+企业网银开办数)/账户经理维护客户数+日均公司存款数×0.001
		
		double kpl_acc=0;//中小企业客户经理KPI
		double cust_effec_dim=0;//客户效能维度
		double prod_effec_dim=0;//产品效能维度
		double cust_effec=0;//客户效能
		double prod_effec=0;//产品效能
		//使用客户经理号关联 查出相应客户经理数据
		String sql = " select c1.cust_mgr_no,"
				+"        c1.cust_mgr_name,"
				+"        sum(c1.ck_nrj) ck_nrj,"
				+"        sum(c1.cust_inc) cust_inc,"
				+"        c2.user_level,"
				+"        sum(bw_fic_nrj) bw_fic_nrj,"
				+"        sum(jscard_num) jscard_num,"
				+"        sum(dxt_num) dxt_num,"
				+"        sum(is_hdx) is_hdx,"
				+"        sum(is_bocnet) is_bocnet,"
				+"        count(1) cn"
				+"   from (select c.claim_cust_mgr_id cust_mgr_no,"
				+"                c.claim_cust_mgr_name cust_mgr_name,"
				+"                (ck_nrj_cny + ck_nrj_wb) * (to_number(claim_prop) / 100) ck_nrj,"
				+"                (ck_int_inc + loan_int_inc + int_inc + operating_inc_contrib +"
				+"                account_contrib_before_tax) * (to_number(c.claim_prop) / 100) cust_inc,"
				+"                bw_fic_nrj,"
				+"                jscard_num,"
				+"                dxt_num,"
				+"                to_number(nvl(is_hdx, 0)) is_hdx,"
				+"                to_number(nvl(is_bocnet, 0)) is_bocnet"
				+"           from pccm_cust_base_info b"
				+"          inner join pccm_cust_pool m"
				+"             on m.customercode = b.cust_no"
				+"            and m.orgnum = b.id"
				+"          inner join pccm_cust_claim c"
				+"             on c.cust_pool_id = m.id"
				+"          where c.del_stat = '0'"
				+"            and to_number(c.claim_time) < 2016) c1"
				+"   left join sys_user_info c2"
				+"     on c1.cust_mgr_no = c2.user_no"
				+"   left join gcms_role_apply c3"
				+"     on c2.user_no = c3.user_id"
				+"   left join gcms_param_info c4"
				+"     on c4.id = c3.role_id"
				+"  where c4.val = 3"
				+"    and c3.apply_status = '1'"
				+"  group by cust_mgr_no, cust_mgr_name, c2.user_level";
		List<Record> list = Db.use("default").find(sql);
		if(null!=list&&list.size()>0){
			String cust_mgr_no = "";//客户经理号
			String cust_mgr_name = "";//客户经理号
			double new_open_acc_num = 0;	//新开有效账户
			double cust_pot_num = 0;	//潜力客户
			double cust_high_qual_num = 0;	//优质客户
			double cust_high_end_num = 0;	//高端客户
			
			int public_msg_num =0;	//对公短信通数
			int elec_retu_box_num=0;//电子回单箱数
			int unit_sett_card_num=0;//单位结算卡数
			int comp_net_silver = 0;//企业网银开办数
			double ck_nrj=0;	//日均公司存款数
			double bw_fic_nrj=0;	//表外理财日均余额
			int mag_cust_num = 0;//账户经理维护客户数
			for(int i = 0; i <= list.size(); i++){
				cust_mgr_no = list.get(i).getStr("cust_mgr_no");
				cust_mgr_name = list.get(i).getStr("cust_mgr_name");
				public_msg_num = list.get(i).getInt("dxt_num");
				elec_retu_box_num = list.get(i).getInt("is_hdx");
				unit_sett_card_num = list.get(i).getInt("jscard_num");
				comp_net_silver = list.get(i).getInt("is_bocnet");
				ck_nrj = list.get(i).getDouble("ck_nrj");
				bw_fic_nrj = list.get(i).getDouble("bw_fic_nrj");
				
				
				//客户效能=新开有效账户×0.2+潜力客户×0.5+优质客户×1+高端客户×1
				cust_effec = new_open_acc_num*0.2+cust_pot_num*0.5+cust_high_qual_num*1+cust_high_end_num*1;
				//(对公短信通数+电子回单箱数+单位结算卡数+企业网银开办数)/账户经理维护客户数+日均公司存款数×0.001
				prod_effec=(public_msg_num+elec_retu_box_num+unit_sett_card_num+comp_net_silver)/mag_cust_num 
						+ (ck_nrj*0.001>cust_effec_dim*0.85?ck_nrj*0.001:cust_effec_dim*0.85);
				
				
				//客户效能维度=账户经理实际客户效能/相应层级客户效能达标值×100 客户效能维度权重为40%（上限72分）
				
				//产品效能维度=账户经理实际产品效能/相应层级产品效能达标值×100 K2产品效能维度权重为60%（上限108分）。KPI得分上限不高于180分
				
				//账户经理价值创造（KPI）=客户效能维度+产品效能维度		
				
				kpl_acc = BigDecimal.valueOf(cust_effec_dim)
						.add(BigDecimal.valueOf(prod_effec_dim)).doubleValue();
				//将计算的数据按照客户经理号，日期插入kpi详情表中
				
				//根据客户经理号 查询相应客户数据 处理 插入kpi客户详情表
				//客户名称	四层分类	客户效能值	对公短信	电子回单箱	单位结算卡	网银	日均存款	KPI值
			
			}
		}

	}
	
	/**
	 * 金融机构客户经理kpl计算
	 */
	public static void kpl_fina(String excuTime){
		//设置参数
		int inc_para=0;				//营业收入权重
		int inc_score_max=0;		//营业收入分值上限
		int cust_num_para=0;		//客户数权重
		int cust_num_score_max=0;	//客户数分值上限

		//读取大公司客户经理kpl计算参数
		List<Record> pralist = Db.use("default").find(" select name,val from gcms_param_info where key='kpl_final_para' ");
		if(null!=pralist&&pralist.size()>0){
			for(int a = 0; a < pralist.size(); a++){
				switch ((String)pralist.get(a).get("name")) {
				case "营业收入权重":
					inc_para = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				case "营业收入分值上限":
					inc_score_max = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				case "客户数权重":
					cust_num_para = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				case "客户数分值上限":
					cust_num_score_max = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				default:
					break;
				}
			}
		}else{
			log.error("未读取到金融机构客户经理kpl计算参数！");return;
		}
		
		double kpl_fina=0;//金融客户经理KPI
		double busi_inc_score=0;//营业收入
		double cust_num_score=0;//客户数
		
		//使用客户经理号关联 查出相应客户经理数据
		int standTime = Integer.parseInt(getStandTime(excuTime));//执行月份的15号
		List<Record> custlist = null;
		String custmgrlistSql = "select cust_mgr_no, "
							+"       sum(cust_inc) cust_inc, "
							+"       sum(ck_nrj) ck_nrj, "
							+"       sum(loan_nrj) loan_nrj, "
							+"       c2.user_level, "
							+"       count(1) cn, "
							+"       cust_mgr_name "
							+"  from (select cust_mgr_no, "
							+"               cust_mgr_name, "
							+"               (ck_int_inc + loan_int_inc + int_inc + operating_inc_contrib + "
							+"               account_contrib_before_tax)* (to_number(c.claim_prop) / 100) cust_inc, "
							+"               (ck_nrj_cny + ck_nrj_wb)* (to_number(c.claim_prop) / 100) ck_nrj, "
							+"               (loan_nrj_cny + loan_nrj_wb)* (to_number(c.claim_prop) / 100) loan_nrj "
							+"          from pccm_cust_base_info b "
							+"          left join pccm_cust_claim c "
							+"            on b.cust_no = c.cust_no "
							+"         where c.del_stat = '0' "
							+"           and to_number(c.claim_time) <= ?) c1 "
							+"  left join sys_user_info c2 "
							+"    on c1.cust_mgr_no = c2.user_no "
							+"  left join gcms_role_apply c3 "
							+"    on c2.user_no = c3.user_id "
							+"  left join gcms_param_info c4 "
							+"    on c4.id = c3.role_id "
							+" where c4.val = '2' "
							+"   and c3.apply_status = '1' "
							+" group by cust_mgr_no, cust_mgr_name, c2.user_level ";
		List<Record> custmgrlist = Db.use("default").find(custmgrlistSql,new Object[]{standTime});
		if(null!=custmgrlist&&custmgrlist.size()>0){
			String cust_mgr_no = null;//客户经理号
			String cust_mgr_name = null;//客户经理姓名
			String cust_mgr_level = null;//客户经理职级
			double cust_inc_mgr=0;	//客户经理营收
			double cust_num_mgr=0;	//客户经理实际维护客户成员数
			
			double cust_num_inc=0;	//客户经理KPI计算客户数
			
			double cust_inc_target=0;	//营收指标
			double cust_num_target=0;	//客户数指标
			for(int i = 0; i < custmgrlist.size(); i++){
				cust_mgr_no = custmgrlist.get(i).getStr("cust_mgr_no");
				cust_mgr_name = custmgrlist.get(i).getStr("cust_mgr_name");
				cust_inc_mgr = custmgrlist.get(i).getBigDecimal("cust_inc").doubleValue();
				cust_num_mgr = custmgrlist.get(i).getBigDecimal("cn").doubleValue();
				cust_mgr_level = custmgrlist.get(i).getStr("user_level");
				//根据客户经理职级，营业收入达标值，客户数达标值
				cust_inc_target = getTarget(busi_inc_zb, kpl_fina_type, cust_mgr_level);
				cust_num_target = getTarget(cust_count_zb, kpl_fina_type, cust_mgr_level);;
				
				//计算KPI（营收）=70×客户经理营收/客户经理职级达标值，上限126分。
				busi_inc_score = Double.parseDouble(df.format(inc_para*cust_inc_mgr/cust_inc_target));
				busi_inc_score = busi_inc_score>inc_score_max?inc_score_max:busi_inc_score;
				
				//根据客户经理号 查询相应客户数据 处理 
				custlist =  Db.use("default").find("select cust_mgr_no,clas_five, cust_inc*(to_number(claim_prop) / 100) cust_inc, claim_prop, cust_name, cust_no "
								+"  from (select c.claim_cust_mgr_id cust_mgr_no,p.clas_five, "
								+"               (ck_int_inc + loan_int_inc + int_inc + operating_inc_contrib + "
								+"               account_contrib_before_tax)* (to_number(c.claim_prop) / 100) cust_inc, "
								+"               c.claim_prop, "
								+"               name cust_name, "
								+"               b.cust_no "
								+"          from pccm_cust_base_info b "
								+"          left join pccm_cust_claim c "
								+"            on b.cust_no = c.cust_no "
								+" left join pccm_cust_pool p on b.cust_no = p.customercode "
								+"         where c.del_stat = '0' "
								+"           and to_number(c.claim_time) <= ?) c1 "
								+" where cust_mgr_no = ? ",new Object[]{standTime,cust_mgr_no});
				List<Map<String, Object>>  maplist=null;
				if(null!=custlist&&custlist.size()>0){
					String custName=null;//客户名称
					String custNo=null;//客户号
					String clasFive=null;//五层分类
					String claimPro=null;//认领占比
					double custInc=0;//客户营收
					double custIncNum=0;//KPI计算客户数
					maplist= new ArrayList<Map<String, Object>>();
					Map<String, Object> map = null;
					for(int n=0;n<custlist.size();n++){
						custName = custlist.get(n).getStr("cust_name");
						claimPro = custlist.get(n).getStr("claim_prop");
						clasFive = custlist.get(n).getStr("clas_five");
						custNo = custlist.get(n).getStr("cust_no");
						custInc = custlist.get(n).getBigDecimal("cust_inc").doubleValue();
						
						//根据年收益计算客户数
						custIncNum = getDouVal(kpl_fina_standard, kpl_cust_income, custInc);
						
						//客户经理计算集团客户数
						cust_num_inc = BigDecimal.valueOf(cust_num_inc).
								add(BigDecimal.valueOf(custIncNum)).doubleValue();
						
						//插入kpi客户贡献详情表
						Db.use("default").update(" insert into pccm_kpi_cust_info (id, period, cust_no, cust_name, claim_prop, "
							+" busi_inc,cust_num, clas_five, create_time, cust_mgr_no, cust_mgr_name) "
							+" values (?, ?, ?, ?, ?, ?, ?, ?, to_char(sysdate,'yyyyMMddhh24miss'), ?, ?)", 
							new Object[] { AppUtils.getStringSeq(),excuTime,custNo,custName,claimPro,custInc,cust_num_inc,
									clasFive,cust_mgr_no,cust_mgr_name });
						map = new HashMap<String, Object>();
						map.put("custIncNum", custIncNum);
						map.put("cust_inc", custInc);
						map.put("custNo", custNo);
						maplist.add(map);
					}
				}
				
				
				//KPI（客户数）=30×客户经理客户数/客户经理职级达标值，上限54分。
				cust_num_score = Double.parseDouble(df.format(cust_num_para*cust_num_inc/cust_num_target));
				cust_num_score = cust_num_score>cust_num_score_max?cust_num_score_max:cust_num_score;
				
				//金融客户经理价值创造=营业收入+客户数
				kpl_fina = BigDecimal.valueOf(busi_inc_score)
						.add(BigDecimal.valueOf(cust_num_score)).doubleValue();
				//将计算的数据按照客户经理号，日期插入客户经理kpi表中
				Db.use("default").update(" insert into pccm_kpi_info(id,period,cust_mgr_no,kpi,create_time,"
						+" cust_num_mgr,cust_kpi_num,cust_busi_inc,ck_nrj_mgr,loan_nrj_mgr) "
						+" values(?,?,?,?,sysdate,?,?,?,?,?) " ,
						new Object[] { AppUtils.getStringSeq(),excuTime,cust_mgr_no,kpl_fina,cust_num_mgr,
								cust_num_inc,cust_inc_mgr,null,null });
				
				//计算客户对客户经理的KPI贡献值
				if(null!=maplist&&maplist.size()>0){
					String custNo=null;//客户号
					double custIncNum=0;//KPI计算客户数
					double custKpi=0;//KPI贡献值
					double cust_inc=0;//KPI营收
					double inc_kpi=0;//KPI营收
					for(int c=0;c<maplist.size();c++){
						custNo = (String) maplist.get(c).get("custNo");
						custIncNum = (Double)maplist.get(c).get("custIncNum");
						cust_inc = (Double)maplist.get(c).get("cust_inc");
						if(0!=cust_num_inc){
							custKpi = Double.parseDouble(df.format(custIncNum/cust_num_inc*cust_num_score));
						}
						if(0!=cust_inc_mgr){
							inc_kpi = Double.parseDouble(df.format(cust_inc/cust_inc_mgr*busi_inc_score));
						}
						Db.use("default").update(" update pccm_kpi_cust_info set kpi=?,busi_inc_kpi=?,cust_num_kpi=? where cust_no=? and period=? and cust_mgr_no=? " ,
								new Object[] { BigDecimal.valueOf(custKpi)
								.add(BigDecimal.valueOf(inc_kpi)).doubleValue(),inc_kpi,custKpi,custNo,excuTime,cust_mgr_no});
					}
				}
			}
			
			
		}
	}
	
	/**
	 * 行政事业客户经理kpl计算
	 */
	public static void kpl_admi(String excuTime){
		//行政事业客户经理价值创造=客户拓展维度+效能提升维度
		//其中：客户拓展维度=实际维护客户数×修正系数/相应层级维护客户数达标值
		//效能提升维度=实际维护客户日均存款×修正系数/相应层级维护客户日均存款达标值	
		//设置参数
		int ck_day_para=0;				//营业收入权重
		int cust_num_para=0;		//客户数权重

		//读取大公司客户经理kpl计算参数
		List<Record> pralist = Db.use("default").find(" select name,val from gcms_param_info where key='kpl_admi_para' ");
		if(null!=pralist&&pralist.size()>0){
			for(int a = 0; a < pralist.size(); a++){
				switch ((String)pralist.get(a).get("name")) {
				case "日均存款权重":
					ck_day_para = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				case "客户数权重":
					cust_num_para = Integer.parseInt((String) pralist.get(a).get("val"));
					continue;
				default:
					break;
				}
			}
		}else{
			log.error("未读取到行政客户经理kpl计算参数！");return;
		}
		
		double kpl_admi=0;//金融机构客户经理KPI
		double cust_exte_dim=0;//客户拓展维度
		double effec_prom_dim=0;//效能提升维度
		
		//使用客户经理号关联 查出相应客户经理数据
		int standTime = Integer.parseInt(getStandTime(excuTime));//执行月份的15号
		List<Record> custlist = null;
		List<Record> custmgrlist = Db.use("default").find(" select c1.cust_mgr_no, sum(ck_nrj) ck_nrj, c2.user_level, count(1) cn, "
				+" c1.cust_mgr_name from (select c.claim_cust_mgr_id cust_mgr_no, cust_mgr_name, (ck_nrj_cny + ck_nrj_wb)* (to_number(claim_prop) / 100) ck_nrj "
				+" from pccm_cust_base_info "
				+" b left join pccm_cust_claim c on b.cust_no = c.cust_no "
				+" where c.del_stat = '0' and to_number(c.claim_time)<?  "
				+" ) c1 "
				+" left join sys_user_info c2 on c1.cust_mgr_no = c2.user_no "
				+" left join gcms_role_apply c3 on c2.user_no = c3.user_id "
				+" left join gcms_param_info c4 on c4.id = c3.role_id "
				+" where c4.val = 5 and c3.apply_status='1' "
				+" group by c1.cust_mgr_no, c1.cust_mgr_name, c2.user_level ",new Object[]{standTime});
		if(null!=custmgrlist&&custmgrlist.size()>0){
			String cust_mgr_no = null;//客户经理号
			String cust_mgr_name = null;//客户经理名称
			String cust_mgr_level = null;//客户经理级别
			double fact_ser_cust_num=0;//实际维护客户数
			double ck_nrj_mgr=0;	//客户经理存款日均
			double fact_ser_cust_target=0;//相应层级维护客户数达标值
			double depo_day_target=0;	//相应层级维护客户日均存款达标值
			for(int i = 0; i < custmgrlist.size(); i++){
				cust_mgr_no = custmgrlist.get(i).getStr("cust_mgr_no");
				cust_mgr_name = custmgrlist.get(i).getStr("cust_mgr_name");
				ck_nrj_mgr = custmgrlist.get(i).getBigDecimal("ck_nrj").doubleValue();
				fact_ser_cust_num = custmgrlist.get(i).getBigDecimal("cn").doubleValue();
				cust_mgr_level = custmgrlist.get(i).getStr("user_level");
				
				//根据客户经理号查出客户经理职级，营业收入达标值，客户数达标值							
				//根据客户经理职级，营业收入达标值，客户数达标值
				fact_ser_cust_target = getTarget(cust_count_zb, kpl_admi_type, cust_mgr_level);
				depo_day_target = getTarget(deposit_day_zb, kpl_admi_type, cust_mgr_level);
				
				//客户拓展维度=实际维护客户数×修正系数/相应层级维护客户数达标值
				cust_exte_dim = fact_ser_cust_num*cust_num_para/fact_ser_cust_target;
				//效能提升维度=实际维护客户日均存款×修正系数/相应层级维护客户日均存款达标值
				effec_prom_dim = fact_ser_cust_num*ck_day_para/depo_day_target;
				//行政事业客户经理价值创造=客户拓展维度+效能提升维度
				kpl_admi = BigDecimal.valueOf(cust_exte_dim).add(BigDecimal.valueOf(effec_prom_dim)).doubleValue();
				//将计算的数据按照客户经理号，日期插入kpi详情表中
				Db.use("default").update(" insert into pccm_kpi_info(id,period,cust_mgr_no,kpi,create_time,"
						+" cust_num_mgr,cust_kpi_num,cust_busi_inc,ck_nrj_mgr,loan_nrj_mgr) "
						+" values(?,?,?,?,sysdate,?,?,?,?,?) " ,
						new Object[] { AppUtils.getStringSeq(),excuTime,cust_mgr_no,Double.parseDouble(df.format(kpl_admi)),null,
								null,null,null,null });
				//根据客户经理号 查询相应客户数据 处理 插入kpi客户详情表
				custlist =  Db.use("default").find(" select cust_mgr_no, ck_nrj, claim_prop, cust_name, cust_no "
						+" from (select c.claim_cust_mgr_id cust_mgr_no, (ck_nrj_cny + ck_nrj_wb)* (to_number(claim_prop) / 100) ck_nrj, "
						+"  c.claim_prop, name cust_name, b.cust_no "
						+" from pccm_cust_base_info b left join pccm_cust_claim c on b.cust_no = c.cust_no"
						+" where c.del_stat = '0' and to_number(c.claim_time)<? ) c1 where cust_mgr_no=? ",new Object[]{standTime,cust_mgr_no});
				if(null!=custlist&&custlist.size()>0){
					String custName=null;//客户名称
					String custNo=null;//客户号
					String claimPro=null;//认领占比
					double ck_nrj=0;//日均存款
					double cust_exte_kpi=0;	//客户拓展kpi
					double effec_prom_kpi=0;	//效能提升kpi
					double kpi=0;//KPI计算客户数
					for(int n=0;n<custlist.size();n++){
						custName = custlist.get(n).getStr("cust_name");
						claimPro = custlist.get(n).getStr("claim_prop");
						custNo = custlist.get(n).getStr("cust_no");
						ck_nrj = custlist.get(n).getBigDecimal("ck_nrj").doubleValue();
						
						//客户拓展kpi
						cust_exte_kpi = fact_ser_cust_num==0?0:Double.parseDouble(df.format((Double.parseDouble(claimPro)/100)/fact_ser_cust_num*cust_exte_dim));
						//效能提升kpi
						effec_prom_kpi = ck_nrj_mgr==0?0:Double.parseDouble(df.format(ck_nrj/ck_nrj_mgr*effec_prom_dim));
						
						kpi = BigDecimal.valueOf(cust_exte_kpi).add(BigDecimal.valueOf(effec_prom_kpi)).doubleValue();
						//插入kpi客户贡献详情表
						Db.use("default").update(" insert into pccm_kpi_cust_info (id, period, cust_no, cust_name, claim_prop, "
							+" kpi,depo_day_true,cust_num_true,cust_expa_kpi,effi_prom_kpi, create_time, cust_mgr_no, cust_mgr_name) "
							+" values (?, ?, ?, ?, ?, ?,?, ?,?, ?, to_char(sysdate,'yyyyMMddhh24miss'), ?, ?)", 
							new Object[] { AppUtils.getStringSeq(),excuTime,custNo,custName,claimPro,kpi,ck_nrj,Double.parseDouble(claimPro)/100,cust_exte_kpi,
									effec_prom_kpi,cust_mgr_no,cust_mgr_name });
					}
				}
			}
		}
		
	}
	
	/**
	 * 每次跑批前先删除此批次的数据
	 * @return
	 */
	public static void delData(String excuTime){
		//删除客户KPI贡献详情表的数据
		Db.use("default").update("delete from pccm_kpi_cust_info where period=? ", new Object[]{excuTime});
		//删除客户经理KPI表的数据
		Db.use("default").update("delete from pccm_kpi_info where period=? ", new Object[]{excuTime});
	}
	
	/**
	 * 获取客户经理营业收入达标值，客户数达标值
	 * @return
	 */
	public static double getTarget(String zb_flag,String cust_mgr_type,String cust_mgr_level){
		String val = Db.use("default").queryStr( " select d.zbmc_val from pccm_kpi_param g " +
				" left join pccm_zb_detail d on g.id = d.kpi_id where d.zb_flag = ? " +
				" and g.khjl_type=? and g.zj=? ",new Object[]{zb_flag,cust_mgr_type,cust_mgr_level});
		if(AppUtils.StringUtil(val)==null){
			log.error("未读取到客户经理级别是"+cust_mgr_level+"，客户经理类型是"+cust_mgr_type+"，指标是"+zb_flag+""+"的达标值！");
		}
		return Double.parseDouble(val);
	}

	/**
	 * 根据参数读取值
	 * @return
	 */
	public static double getDouVal(String standard_flag,String condition_flag,double param){
		double dou=0;
		double v1=0;
		double v2=0;
		String s1=null;
		String s2=null;
		List<Record> praList =  Db.use("default").find("select c.symbol1, c.condition_val1, c.symbol2, c.condition_val2, " +
				" c.result_value from pccm_standard_condition c left join pccm_standard_info i on c.standard_id = i.id " +
				" where i.standard_flag = ? and c.condition_flag = ? ",new Object[]{standard_flag,condition_flag});
		if(null!= praList&&praList.size()>0){
			for(int p=0;p<praList.size();p++){
				Record pra = praList.get(p);
				s1= pra.getStr("symbol1");
				s2= pra.getStr("symbol2");
				if(AppUtils.StringUtil(s1) != null && AppUtils.StringUtil(s2)!=null){
					v1= Double.parseDouble(pra.getStr("condition_val1"));
					v2= Double.parseDouble(pra.getStr("condition_val2"));
					if(compare(s1, v1, param)&&compare(s2, v2, param)){
						dou= Double.parseDouble(pra.getStr("result_value"));
					}
				}else if(AppUtils.StringUtil(s1) != null && AppUtils.StringUtil(s2)==null){
					v1= Double.parseDouble(pra.getStr("condition_val1"));
					if(compare(s1, v1, param)){
						dou= Double.parseDouble(pra.getStr("result_value"));
					}
				}else if(AppUtils.StringUtil(s1) == null && AppUtils.StringUtil(s2)!=null){
					v2= Double.parseDouble(pra.getStr("condition_val2"));
					if(compare(s2, v2, param)){
						dou= Double.parseDouble(pra.getStr("result_value"));
					}
				}
			}
		}
		return dou;
	}
	
	/**
	 *参数比较
	 * @return
	 */
	public static boolean compare(String symbol,double val,double para){
		boolean flag = false;
		switch (symbol) {
			case ">=":
				flag = para>=val;
				break;
			case ">":
				flag = para>val;
				break;
			case "<=":
				flag = para<=val;
				break;
			case "<":
				flag = para<val;
				break;
			default:
				break;
		}
		return flag;
	}
	
	/**
	 *取执行月份的15号
	 * @return
	 */
	public static String getStandTime(String excuTime){
		String standTime = null;
		if(excuTime.length()>6){
			standTime = excuTime.substring(0,6)+"15";
		}else{
			standTime = excuTime+"15";
		}
		return standTime;
	}
}
