package com.goodcol.job;
import java.io.File;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

public class DelUploadFileJob extends OuartzJob{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		String incflg = "删除上传文件";
		//记录数
		int record_size = 0;
		//跑批状态
		int run_status = 0;
		try{
			start_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			String sql = "select * from SYS_ATTACHMENTS t where t.status='0' and  (t.uploadtype = '0')";
			List<Record> delFileList = Db.find(sql);
			//String uploadpath = PropertiesContent.get("upload_path");//上传文件地址
			for(Record r:delFileList){
				//String filename = r.getStr("filename");
				String id = r.getStr("id");
				String filepath = r.getStr("filepath");
				File file = new File(filepath.replace("/", "\\"));
				if(file.exists()){
					file.delete();
				}
				String updatesql = "update SYS_ATTACHMENTS set uploadtype='1' where id = "+"'"+id+"'"; // 更改数据状态是已删除状态
				Db.update(updatesql);
			}
			end_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			run_status=1;
			success();
		}catch(Exception e){
			error();
			run_status=2;
			e.printStackTrace();
		}finally{
			Db.use("default").update("insert into PCCM_QUARTZ_RUN(id,START_TIME,END_TIME,RECORD_SIZE,INCFLG,RUN_STATUS) values(?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),start_time,end_time,record_size,incflg,run_status});
		}
	}

}
