/**
 * 
 */
package com.goodcol.job;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * @author dinggang
 * 
 */
public class PccmSupplyChainJob extends OuartzJob {
	private static Logger log=Logger.getLogger(PccmSupplyChainJob.class);
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		String incflg = "2";
		// 记录数
		int record_size = 0;
		// 跑批状态
		int run_status = 0;
		//入池日期
		String indate=BolusDate.getDateTime("yyyyMMdd");
		try {
			start_time = BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			// 当前日期
			Date end = new Date();
			String nowDate = sdf.format(end);
			// 求前多少天到当前日期的CTIS数据（例如:30天）
			Calendar c = Calendar.getInstance();
			c.setTime(end);
			c.add(Calendar.DATE, -30);
			Date start = c.getTime();
			String beforeDate = sdf.format(start);
			List<Record> records = Db
					.use("default")
					.find("select customer_no,tran_brch_no,count(customer_no) as jybs,sum(tran_amount) as jyje from PCCM_TXN_TIF_BAS where TRAN_DATE>=? and TRAN_DATE<=? group by customer_no,tran_brch_no",
							new Object[] { beforeDate, nowDate });
			if (records != null && !records.isEmpty()) {
				for (int i = 0; i < records.size(); i++) {
					// 查询交易笔数/金额(标准1)
					Record record = records.get(i);
					// 查询标准
					String standardStr = "code1";
					boolean flag = getFlagByStandard1(standardStr, record);
					// 标准1达标
					if (flag) {
						Record r=Db.use("default").findFirst("select * from PCCM_ELOAN_LIMIT where CUST_COMPANY_NO=?",new Object[]{record.getStr("customer_no")});
						if(r!=null){
							//客户号
							String cust_company_no=r.getStr("cust_company_no");
							//客户名称
							String cust_company_name=r.getStr("cust_company_name");
							// 与存量客户交互，查询是否是存量
							Record kunbiao=getFlagByCl(record.getStr("custno"),cust_company_no,cust_company_name);
							if (kunbiao!=null) {
								flag=getFlagByStandard2(kunbiao);
								if(flag){//不符合标准2
									Db.use("default").update("insert into PCCM_CUST_POOL(ID,INCFLG,INDATE,CUSTOMERCODE,CUSTOMERNAME,ORG_ID) values(?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),incflg,BolusDate.getDateTime("yyyyMMdd"),record.getStr("customer_no"),kunbiao.getStr("name"),kunbiao.getStr("br_no")});
									record_size++;
									// 数据修补行内及企查查
									// 籍贯打标
									// 短信推送至所属客户经理
								}
							}else{
								Db.use("default").update("insert into PCCM_CUST_POOL(ID,INCFLG,INDATE,CUSTOMERCODE,CUSTOMERNAME,ORG_ID) values(?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),incflg,BolusDate.getDateTime("yyyyMMdd"),record.getStr("customer_no"),"",record.getStr("tran_brch_no")});
								record_size++;
								// 数据修补企查查
								// 籍贯达标
								// 短信推送至二级分行客户池
								// 推送至物理网点
							}
						}
					}
				}
			}
			end_time = BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			run_status = 1;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("供应链客户跑批时在"+BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss")+"发生异常:"+e.getMessage());
		} finally {
			Db.use("default").update("insert into pccm_quartz_run(id,start_time,end_time,record_size,incflg,run_status,indate) values(?,?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),start_time,end_time,record_size,incflg,run_status,indate});
		}

	}

	private boolean getFlagByStandard2(Record kunbiao) {
		boolean flag=false;
		double xc=0;
		//人民币活期存款日均余额
		BigDecimal ck_hqnrj_cny=kunbiao.getBigDecimal("ck_hqnrj_cny");
		//人民币定期存款日均余额
		BigDecimal ck_dqnrj_cny=kunbiao.getBigDecimal("ck_dqnrj_cny");
		//表内理财余额
		BigDecimal bn_fic_curr=kunbiao.getBigDecimal("bn_fic_curr");
		if(ck_hqnrj_cny!=null){
			xc=xc+ck_hqnrj_cny.doubleValue();
			if(ck_dqnrj_cny!=null){
				xc=xc+ck_dqnrj_cny.doubleValue();
			}
			if(bn_fic_curr!=null){
				xc=xc+bn_fic_curr.doubleValue();
			}
		}
		// 根据获取数据判断是否符合标准2
		String standardStr="code2";
		String br_no=kunbiao.getStr("br_no");
		String is_sxcust=kunbiao.getStr("is_sxcust");
		List<Record> records=Db.use("default").find("select * from sys_org_info where bancsid=?",new Object[]{br_no});
		if(records!=null&&!records.isEmpty()){
			for(int i=0;i<records.size();i++){
				String orgnum=records.get(i).getStr("orgnum");
				if(flag){
					break;
				}
				List<Record> list=Db.use("default").find("select c.condition_val1,c.symbol1,c.condition_val2,c.symbol2,c.result_value,c.condition_flag from PCCM_STANDARD_INFO p,PCCM_STANDARD_CONDITION c where p.id=c.standard_id and p.standard_flag=? and c.condition_flag='cklc' and (p.second_org_num=? or p.third_org_num=? or p.hibnk=?)",new Object[]{standardStr,orgnum,orgnum,orgnum});
				if(list!=null&&!list.isEmpty()){
					for(int j=0;j<list.size();j++){
						flag=getFlagByStandard(list.get(j), xc);
						if(!(flag&&"1".equals(is_sxcust))){
							flag=true;
							break;
						}
					}
				}
			}
		}
		return flag;
	}

	private Record getFlagByCl(String str, String cust_company_no,
			String cust_company_name) {
		//查询宽表即存量客户
		Record kunbiao=Db.use("default").findFirst("select * from PCCM_CUST_BASE_INFO where cust_no=? and name=?",new Object[]{cust_company_no,cust_company_name});
		return kunbiao;
	}

	private boolean getFlagByStandard1(String standardStr, Record record) {
		boolean flag = false;
		double jybs = record.getBigDecimal("jybs").doubleValue();
		double jyje = record.getBigDecimal("jyje").doubleValue();
		String tran_brch_no=record.getStr("tran_brch_no");
		List<Record> records=Db.use("default").find("select * from sys_org_info where bancsid=?",new Object[]{tran_brch_no});
		if(records!=null&&!records.isEmpty()){
			for(int i=0;i<records.size();i++){
				String orgnum=records.get(i).getStr("orgnum");
				if(flag){
					break;
				}
				List<Record> list=Db.use("default").find("select c.condition_val1,c.symbol1,c.condition_val2,c.symbol2,c.result_value,c.condition_flag from PCCM_STANDARD_INFO p,PCCM_STANDARD_CONDITION c where p.id=c.standard_id and p.standard_flag=? and (c.condition_flag='jybs' or c.condition_flag='jyje') and (p.second_org_num=? or p.third_org_num=? or p.hibnk=?)",
						new Object[] { standardStr,orgnum,orgnum,orgnum});
				if(list!=null&&!list.isEmpty()){
					for(int j=0;j<list.size();j++){
						Record r = list.get(j);
						flag = getFlagByInfo(r, jybs, jyje);
						if(flag){
							break;
						}
					}
				}
			}
		}
		return flag;
	}

	private boolean getFlagByInfo(Record r, double jybs, double jyje) {
		boolean flag = false;
		String condition_flag = r.getStr("condition_flag");
		if ("jybs".equals(condition_flag)) {
			flag = getFlagByStandard(r, jybs);
		} else if ("jyje".equals(condition_flag)) {
			flag = getFlagByStandard(r, jyje);
		}
		return flag;
	}

	private boolean getFlagByStandard(Record r, double xc) {
		boolean flag = false;
		if(r!=null){
			String symbol1 = r.getStr("symbol1");
			if (symbol1.equals(">=")) {
				flag = xc >= Double.parseDouble(r.getStr("condition_val1"));
			} else if (symbol1.equals("<=")) {
				flag = xc <= Double.parseDouble(r.getStr("condition_val1"));
			} else if (symbol1.equals(">")) {
				flag = xc > Double.parseDouble(r.getStr("condition_val1"));
			} else if (symbol1.equals("<")) {
				flag = xc < Double.parseDouble(r.getStr("condition_val1"));
			} else if (symbol1.equals("=")) {
				flag = xc == Double.parseDouble(r.getStr("condition_val1"));
			}
			String symbol2 = r.getStr("symbol2");
			String condition_val2 = r.getStr("condition_val2");
			if (AppUtils.StringUtil(symbol2) != null) {
				if (AppUtils.StringUtil(condition_val2) != null) {
					if (symbol2.equals(">=")) {
						flag = flag && xc >= Double.parseDouble(condition_val2);
					} else if (symbol1.equals("<=")) {
						flag = flag && xc <= Double.parseDouble(condition_val2);
					} else if (symbol1.equals(">")) {
						flag = flag && xc > Double.parseDouble(condition_val2);
					} else if (symbol1.equals("<")) {
						flag = flag && xc < Double.parseDouble(condition_val2);
					} else if (symbol1.equals("=")) {
						flag = flag && xc == Double.parseDouble(condition_val2);
					}
				}
			}
		}
		return flag;
	}

}
