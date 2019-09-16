/**
 * 
 */
package com.goodcol.job;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * @author dinggang
 * 
 */
public class PccmProductBussinessJob extends OuartzJob {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		String incflg = "6";
		//记录数
		int record_size = 0;
		//跑批状态
		int run_status = 0;
		try {
			// 查询对公客户数据于CCMS客户比较（取得客户号相等的数据）
			List<Record> records = Db.use("default").find("select p1.*,p2.LOANEDCNYFMT,p2.LOANCASHCNCYFMT from PCCM_CUST_BASE_INFO p1,PCCM_P395 p2 where p1.CUST_NO=p2.CUSTOMERCODE");
			start_time = BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			if (records != null && !records.isEmpty()){
				for(int i=0;i<records.size();i++){
					// 查询数据
					Record record=records.get(i);
					String standardStr="code16";
					Map<String,Object> map=getFlagByStandard16(record,standardStr);
					if(map!=null){
						record_size++;
						Set<String> keys=map.keySet();
						Iterator<String> key=keys.iterator();
						while(key.hasNext()){
							String label_key=key.next();
							String label=map.get(label_key).toString();
							Db.use("default").update("insert into PCCM_LABEL_SEND(id,custno,custname,label_key,label)values(?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),record.getStr("cust_no"),record.getStr("name"),label_key,label});
						} 
					}
				}
			}
			end_time = BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			run_status = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Db.use("default").update(
					"insert into PCCM_QUARTZ_RUN(id,START_TIME,END_TIME,RECORD_SIZE,INCFLG,RUN_STATUS) values(?,?,?,?,?,?)",
					new Object[] { AppUtils.getStringSeq(), start_time,
							end_time, record_size, incflg, run_status });
		}

	}

	private Map<String, Object> getFlagByStandard16(Record record, String standardStr) {
		Map<String,Object> map=null;
		String br_no=record.getStr("br_no");
		List<Record> list=Db.use("default").find("select c.condition_val1,c.symbol1,c.condition_val2,c.symbol2,c.result_value,c.condition_flag from PCCM_STANDARD_INFO p,PCCM_STANDARD_CONDITION c where p.id=c.standard_id and p.standard_flag=? and (condition_flag='filed4' or condition_flag='filed15' or condition_flag='filed2' or condition_flag='loanedcnyfmt' or condition_flag='loancashcncyfmt')and (p.second_org_num=? or p.third_org_num=?)",new Object[]{standardStr,br_no,br_no});
		for(int j=0;j<list.size();j++){
			Record r=list.get(j);
			map=getLabelByInfo(record,r);
			if(map!=null){
				break;
			}
		}
		return map;
		
	}

	private Map<String, Object> getLabelByInfo(Record record, Record r) {
		Map<String,Object> map=new HashMap<String,Object>();
		String condition_flag=r.getStr("condition_flag");
		String filed4=record.getStr("filed4");//应收票据
		String filed15=record.getStr("filed15");//应付票据
		String filed2=record.getStr("filed2");//货币资金
		String loanedcnyfmt=record.getStr("loanedcnyfmt");//贷款额度币种
		String loancashcncyfmt=record.getStr("loancashcncyfmt");//贷款金额币种
		String label="";
		String label_key="";
		boolean flag=false;
		if("field4".equals(condition_flag)){
			label="";
			label_key="应收票据";
			double xc=0.0;
			flag=getFlagByStandard(r,xc);
		}else if("filed15".equals(condition_flag)){
			label="";
			label_key="应付票据";
			double xc=0.0;
			flag=getFlagByStandard(r,xc);
		}else if("field2".equals(condition_flag)){
			label="";
			label_key="货币资金";
			double xc=0.0;
			flag=getFlagByStandard(r,xc);
		}else if("loanedcnyfmt".equals(condition_flag)){
			label="";
			label_key="贷款额度币种";
			double xc=0.0;
			flag=getFlagByStandard(r,xc);
		}else if("loancashcncyfmt".equals(condition_flag)){
			label="";
			label_key="贷款金额币种";
			double xc=0.0;
			flag=getFlagByStandard(r,xc);
		}
		if(flag){
			map.put(label_key,label);
		}
		return map;
	}
	private boolean getFlagByStandard(Record r, double xc) {
		boolean flag=false;
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
		return flag;
	}

}
