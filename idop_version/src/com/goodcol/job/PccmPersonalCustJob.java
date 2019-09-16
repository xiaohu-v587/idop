/**
 * 
 */
package com.goodcol.job;

import java.math.BigDecimal;
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
public class PccmPersonalCustJob extends OuartzJob {
	private static Logger log=Logger.getLogger(PccmPersonalCustJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		//数据源
		String incflg = "1";
		//记录数
		int record_size = 0;
		//跑批状态
		int run_status = 0;
		//入池日期
		String indate=BolusDate.getDateTime("yyyyMMdd");
		try{
			start_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			// 查询个金数据
			List<Record> records=Db.use("default").find("select r.CUSTNO,r.FAM_INCOMEMON,r.TOTAL_INCOMEMON,r.HIBNK from PCCM_T_DSTCIF_STAT_CPB c,PCCM_CCTPER_RL r where c.CIFNO=r.CUSTNO");
			if (records != null && !records.isEmpty()) {
				for (int i = 0; i < records.size(); i++) {
					Record record = records.get(i);
					// 查询借款人或家庭收入标准6
					String standardStr="code6";
					boolean flag=getFlagByStandard6(record,standardStr);
					// 标准6达标
					if (flag) {
						Record r=Db.use("default").findFirst("select * from PCCM_ELOAN_LIMIT where CUST_COMPANY_NO=?",new Object[]{record.getStr("custno")});
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
									Db.use("default").update("insert into PCCM_CUST_POOL(ID,INCFLG,INDATE,CUSTOMERCODE,CUSTOMERNAME,ORG_ID) values(?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),incflg,indate,record.getStr("custno"),kunbiao.getStr("name"),kunbiao.getStr("br_no")});
									record_size++;
									// 数据修补行内及企查查
									// 籍贯打标
									// 短信推送至所属客户经理
								}
							} else {
								Db.use("default").update("insert into PCCM_CUST_POOL(ID,INCFLG,INDATE,CUSTOMERCODE,CUSTOMERNAME,ORG_ID) values(?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),incflg,indate,record.getStr("custno"),kunbiao.getStr("name"),kunbiao.getStr("br_no")});
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
			end_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			run_status=1;
		}catch(Exception e){
			e.printStackTrace();
			log.error("个人客户跑批时在"+BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss")+"发生异常:"+e.getMessage());
		}finally{
			Db.use("default").update("insert into pccm_quartz_run(id,start_time,end_time,record_size,incflg,run_status,indate) values(?,?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),start_time,end_time,record_size,incflg,run_status,indate});
		}
		
	}
	private boolean getFlagByStandard6(Record record, String standardStr) {
		boolean flag=false;
		//家庭月收入
		BigDecimal fam_incomemon=record.getBigDecimal("fam_incomemon");
		//借款人月收入
		BigDecimal total_incomemon=record.getBigDecimal("total_incomemon");
		//客户所属网点
		String hibnk=record.getStr("hibnk");
		List<Record> records=Db.use("default").find("select * from sys_org_info where bancsid=?",new Object[]{hibnk});
		if(records!=null&&!records.isEmpty()){
			for(int i=0;i<records.size();i++){
				String orgnum=records.get(i).getStr("orgnum");
				if(flag){
					break;
				}
				List<Record> list=Db.use("default").find("select c.condition_val1,c.symbol1,c.condition_val2,c.symbol2,c.result_value,c.condition_flag from PCCM_STANDARD_INFO p,PCCM_STANDARD_CONDITION c where p.id=c.standard_id and p.standard_flag=? and (c.condition_flag='borrow_income' or c.condition_flag='family_income') and (p.second_org_num=? or p.third_org_num=? or p.hibnk=?)",new Object[]{standardStr,orgnum,orgnum,orgnum});
				if(list!=null&&!list.isEmpty()){
					for(int j=0;j<list.size();j++){
						flag=getFlagByInfo(list.get(j),fam_incomemon,total_incomemon);
						if(flag){
							break;
						}
					}
				}
			}
		}
		return flag;
	}
	private Record getFlagByCl(String custno, String cust_company_no, String cust_company_name) {
		//查询宽表即存量客户
		Record kunbiao=Db.use("default").findFirst("select * from PCCM_CUST_BASE_INFO where cust_no=? and name=?",new Object[]{cust_company_no,cust_company_name});
		return kunbiao;
	}
	/**
	 * 判断标准2是否是价值客户
	 * @param kunbiao
	 * @return
	 */
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
		//客户所属网点
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

	private boolean getFlagByInfo(Record r, BigDecimal fam_incomemon,
			BigDecimal total_incomemon) {
		boolean flag=false;
		String condition_flag=r.getStr("condition_flag");
		double xc=0;
		if("family_income".equals(condition_flag)){
			if(fam_incomemon!=null){
				xc=fam_incomemon.doubleValue();
			}
			flag=getFlagByStandard(r,xc);
		}else if("borrow_income".equals(condition_flag)){
			if(total_incomemon!=null){
				xc=total_incomemon.doubleValue();
			}
			flag=getFlagByStandard(r,xc);
		}
		return flag;
	}
	
	private boolean getFlagByStandard(Record r, double xc) {
		boolean flag=false;
		if(r!=null){
			String symbol1=r.getStr("symbol1");
			if(symbol1.equals(">=")){
				flag=xc>=Double.parseDouble(r.getStr("condition_val1"));
			}else if(symbol1.equals("<=")){
				flag=xc<=Double.parseDouble(r.getStr("condition_val1"));
			}else if(symbol1.equals(">")){
				flag=xc>Double.parseDouble(r.getStr("condition_val1"));
			}else if(symbol1.equals("<")){
				flag=xc<Double.parseDouble(r.getStr("condition_val1"));
			}else if(symbol1.equals("=")){
				flag=xc==Double.parseDouble(r.getStr("condition_val1"));
			}
			String symbol2=r.getStr("symbol2");
			String condition_val2=r.getStr("condition_val2");
			if(AppUtils.StringUtil(symbol2)!=null){
				if(AppUtils.StringUtil(condition_val2)!=null){
					if(symbol2.equals(">=")){
						flag=flag&&xc>=Double.parseDouble(condition_val2);
					}else if(symbol1.equals("<=")){
						flag=flag&&xc<=Double.parseDouble(condition_val2);
					}else if(symbol1.equals(">")){
						flag=flag&&xc>Double.parseDouble(condition_val2);
					}else if(symbol1.equals("<")){
						flag=flag&&xc<Double.parseDouble(condition_val2);
					}else if(symbol1.equals("=")){
						flag=flag&&xc==Double.parseDouble(condition_val2);
					}
				}
			}
		}
		return flag;
	}

}
