/**
 * 
 */
package com.goodcol.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.server.zxglserver.UserServer;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * @author dinggang
 * 
 */
public class PccmCustPoolJob extends OuartzJob {
	public static Logger log = Logger.getLogger(PccmCustPoolJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("根据参数化的停留标准时间，月初先入分行，时间截止后入支行池，进入中心支行池未按时认领完的，"
				+ "平均随机挂至中心支行领导名下开始---"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("根据参数化的停留标准时间，月初先入分行，时间截止后入支行池，进入中心支行池未按时认领完的，"
				+ "平均随机挂至中心支行领导名下开始---"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		try {
			UserServer userServer = new UserServer();
			// 分行超期的入支行池
			userServer.updateThirdPool();
			// 在三级池超期未认领的客户
			List<Record> list = userServer.findCustByOver();
			// 查找中心支行领导
			List<Record> records = userServer.findSubLeaders();
			if (list != null && !list.isEmpty()) {
				if (records != null && !records.isEmpty()) {
					int count = list.size();
					int size = records.size();
					// 当三级池超期未认领的客户数小于等于中心支行领导数
					if (count <= size) {
						//遍历在三级池超期未认领的客户
						for (int i = 0; i < list.size(); i++) {
							Record record = list.get(i);
							// 取中心支行领导,该客户推送给中心支行领导
							Record leader = records.get(i);
							userServer.saveCustClaim(record, leader);
						}
					} else {// 当三级池超期未认领的客户数大于中心支行领导数
						int part = 0;
						if (count % size == 0) {
							part = count / size;
						} else {
							part = count / size + 1;
						}
						// 依次平均推送给中心支行领导
						for (int i = 0; i < part; i++) {
							List<Record> custs = null;
							if (i == 0) {
								custs = list.subList(0, size);
								for (int j = 0; j < custs.size(); j++) {
									Record record = custs.get(j);
									Record leader = records.get(j); // 取中心支行领导,该客户推送给中心支行领导
									userServer.saveCustClaim(record, leader);
								}
							} else {
								custs = list.subList(i * size, count);
								if (custs.size() > size) {
									custs = list.subList(i * size, i * size
											+ size);
									for (int j = 0; j < custs.size(); j++) {
										Record record = custs.get(j);
										Record leader = records.get(j); // 取中心支行领导,该客户推送给中心支行领导
										userServer
												.saveCustClaim(record, leader);
									}
								} else {
									for (int j = 0; j < custs.size(); j++) {
										Record record = custs.get(j);
										Record leader = records.get(j);// 取中心支行领导,该客户推送给中心支行领导
										userServer
												.saveCustClaim(record, leader);
									}
								}
							}
						}
					}
				} else {
					System.out.println("-------查找中心支行领导的记录为空");
					log.warn("查找中心支行领导的记录为空");
				}
			} else {
				System.out.println("-------查询到三级池超期未认领的客户记录为空");
				log.warn("查询到三级池超期未认领的客户记录为空");
			}
			success();
		} catch (Exception e) {
			System.out
					.println("------根据参数化的停留标准时间，月初先入分行，时间截止后入支行池，进入中心支行池未按时认领完的，平均随机挂至中心支行领导名下发生异常:"
							+ e.toString());
			error();
			e.printStackTrace();
			log.error("根据参数化的停留标准时间，月初先入分行，时间截止后入支行池，进入中心支行池未按时认领完的，平均随机挂至中心支行领导名下发生异常:"
					+ e.toString());
		}
		log.warn("根据参数化的停留标准时间，月初先入分行，时间截止后入支行池，进入中心支行池未按时认领完的，"
				+ "平均随机挂至中心支行领导名下结束---"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("根据参数化的停留标准时间，月初先入分行，时间截止后入支行池，进入中心支行池未按时认领完的，"
				+ "平均随机挂至中心支行领导名下结束---"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
	}
}
