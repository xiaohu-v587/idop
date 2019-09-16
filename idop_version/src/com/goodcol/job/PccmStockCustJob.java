/**
 * 
 */
package com.goodcol.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class PccmStockCustJob extends OuartzJob{
	private static Logger log=Logger.getLogger(PccmStockCustJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		//-----开始时间-----
		String start_time="";
		//-----结束时间-----
		String end_time="";
		String incflg="4";
		//记录数
		int record_size=0;
		//跑批状态
		int run_status=0;
		//入池日期
		String indate=BolusDate.getDateTime("yyyyMMdd");
		try{
			start_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			//查询公司存量客户数据
			List<Record>records=Db.use("default").find("select * from pccm_cust_base_info");
			if(records!=null&&!records.isEmpty()){
				for(int i=0;i<records.size();i++){
					Record record=records.get(i);
					//存款+理财标准8
					String standardStr="code8";
					List<Map<String,Object>> infos=getFlagByStandard(standardStr,record);
					if(infos!=null&&!infos.isEmpty()){
						//存款利息收入
						BigDecimal ck_int_inc=record.getBigDecimal("ck_int_inc");
						//贷款利息收入
						BigDecimal loan_int_inc=record.getBigDecimal("loan_int_inc");
						//净利息收入
						BigDecimal int_inc=record.getBigDecimal("int_inc");
						//利息收入
						double interest_inc=0;
						if(ck_int_inc!=null){
							interest_inc=ck_int_inc.doubleValue();
							if(loan_int_inc!=null){
								interest_inc=interest_inc+loan_int_inc.doubleValue();
							}
							if(int_inc!=null){
								interest_inc=interest_inc+int_inc.doubleValue();
							}
						}
						//营业收入贡献
						BigDecimal operating_inc_contrib=record.getBigDecimal("operating_inc_contrib");
						//税费前利润贡献
						BigDecimal account_contrib_before_tax=record.getBigDecimal("account_contrib_before_tax");
						//各核算码项下中收贡献
						BigDecimal mis_contrib=record.getBigDecimal("mis_contrib");
						double non_interest_inc=0;
						if(operating_inc_contrib!=null){
							non_interest_inc=operating_inc_contrib.doubleValue();
							if(account_contrib_before_tax!=null){
								non_interest_inc=non_interest_inc+account_contrib_before_tax.doubleValue();
							}
							if(mis_contrib!=null){
								non_interest_inc=non_interest_inc+mis_contrib.doubleValue();
							}
						}
						for(int j=0;j<infos.size();j++){
							Map<String,Object> map=infos.get(j);
							String label=map.get("label").toString();
							Db.use("default").update("insert into pccm_cust_pool(id,incflg,indate,customercode,clas_five,customername,org_id) values(?,?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),incflg,indate,record.getStr("cust_no"),label,record.getStr("name"),record.getStr("br_no")});
						}
						Db.use("default").update("insert into pccm_cust_pool_money(id,customercode,loaned,loanedcnyfmt,loancashcncyfmt,valpap,othercap,riskqut,customer_stat,incomcash,incomday,incompoint,loancash,busi_inc,loanscash,loansday,loanspoint,interest_inc,non_interest_inc,data_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
								,new Object[]{AppUtils.getStringSeq(),record.getStr("customercode"),record.getBigDecimal("loaned"),record.getStr("loanedcnyfmt"),record.getStr("loancashcncyfmt"),record.getStr("valpap"),record.getStr("othercap"),record.getBigDecimal("riskqut"),record.getStr("customer_stat"),record.getBigDecimal("incomcash"),record.getBigDecimal("incomday"),record.getBigDecimal("incompoint"),record.getBigDecimal("loancash"),record.getBigDecimal("busi_inc"),record.getBigDecimal("loanscash"),record.getBigDecimal("loansday"),record.getBigDecimal("loanspoint"),interest_inc,non_interest_inc,BolusDate.getDateTime("yyyyMMdd")});
						record_size++;
					}
				}
			}
			end_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			run_status=1;
		}catch(Exception e){
			e.printStackTrace();
			log.error("存量客户跑批时在"+BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss")+"发生异常:"+e.getMessage());
		}finally{
			Db.use("default").update("insert into pccm_quartz_run(id,start_time,end_time,record_size,incflg,run_status,indate) values(?,?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),start_time,end_time,record_size,incflg,run_status,indate});
		}
		
	}

	private List<Map<String,Object>> getFlagByStandard(String standardStr, Record record) {
		List<Map<String,Object>>infos=new ArrayList<Map<String,Object>>();
		double xc=0;
		//人民币存款日均
		BigDecimal ck_nrj_cny=record.getBigDecimal("ck_nrj_cny");
		//表外理财总年日均
		BigDecimal bw_fic_nrj=record.getBigDecimal("bw_fic_nrj");
		if(ck_nrj_cny!=null){
			xc=xc+ck_nrj_cny.doubleValue();
			if(bw_fic_nrj!=null){
				xc=xc+bw_fic_nrj.doubleValue();
			}
		}
		//表内理财年日均
//		double bn_fic_nrj=record.getBigDecimal("bn_fic_nrj").doubleValue();
		//客户所属网点
		String br_no=record.getStr("br_no");
		List<Record> records=Db.use("default").find("select orgnum from sys_org_info where bancsid=?",new Object[]{br_no});
		if(records!=null&&!records.isEmpty()){
			for(int i=0;i<records.size();i++){
				Map<String,Object> map=new HashMap<String,Object>();
				String label="";
				String orgnum=records.get(i).getStr("orgnum");
				String by2=records.get(i).getStr("by2");
				List<Record> list=null;
				if("1".equals(by2)){
					list=Db.use("default").find("select c.condition_val1,c.symbol1,c.condition_val2,c.symbol2,c.result_value,c.condition_flag from PCCM_STANDARD_INFO p,PCCM_STANDARD_CONDITION c where p.id=c.standard_id and p.standard_flag like ? and (c.condition_flag='rjcklc8-critical' or c.condition_flag='rjcklc8-absolute') and p.hibnk=?",new Object[]{"%"+standardStr+"%",orgnum});
				}else if("2".equals(by2)){
					list=Db.use("default").find("select c.condition_val1,c.symbol1,c.condition_val2,c.symbol2,c.result_value,c.condition_flag from PCCM_STANDARD_INFO p,PCCM_STANDARD_CONDITION c where p.id=c.standard_id and p.standard_flag like ? and (c.condition_flag='rjcklc8-critical' or c.condition_flag='rjcklc8-absolute') and p.second_org_num=?",new Object[]{"%"+standardStr+"%",orgnum});
				}else if("3".equals(by2)){
					list=Db.use("default").find("select c.condition_val1,c.symbol1,c.condition_val2,c.symbol2,c.result_value,c.condition_flag from PCCM_STANDARD_INFO p,PCCM_STANDARD_CONDITION c where p.id=c.standard_id and p.standard_flag like ? and (c.condition_flag='rjcklc8-critical' or c.condition_flag='rjcklc8-absolute') and p.third_org_num=?",new Object[]{"%"+standardStr+"%",orgnum});
				}
				if(list!=null&&!list.isEmpty()){
					for(int j=0;j<list.size();j++){
						Record r=list.get(j);
						boolean flag=getFlagByStandard(r, xc);
						if(flag){
							label=r.getStr("result_value");
							break;
						}
					}
				}
				map.put("label",label);
				map.put("by2",by2);
				infos.add(map);
			}
		}
		return infos;
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
