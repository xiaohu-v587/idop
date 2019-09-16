package com.goodcol.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.RedisUtil;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.plugin.quartz.QuartzSchedule;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

/**
 * 定时任务管理
 *
 * @author puyu
 */
@RouteBind(path = "/schedule")
@Before( { ManagerPowerInterceptor.class })
public class ScheduleCtl extends BaseCtl{

    protected Logger log =  Logger.getLogger(ScheduleCtl.class);
    
    @Override
	public void index() {
    	render("index.jsp");
	}
    
    /**
     * 显示所有的定时任务
     */
    public void list(){
		int pageNumber = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String rwmc=getPara("rwmc");
		StringBuffer sb = new StringBuffer();
		List<String> listStr = new ArrayList<String>();
		if (AppUtils.StringUtil(rwmc) != null) {
			sb.append(" and rwmc like ? ");
			listStr.add("%" + rwmc.trim() + "%");
		}
		String sql = "select * ";
		String extrasql = " from SYS_SCHEDULE_TASK where (rwzt='1' or rwzt='0') " + sb.toString()
				+ " order by zhxgsj desc";
		Page<Record> r = Db.paginate(pageNumber, pageSize, sql, extrasql,
				listStr.toArray());
		List<Record> list = r.getList();
		if (list.size() < 1) {
			Page<Record> r1 = Db.paginate(1, 10, sql, extrasql,
					listStr.toArray());
			list = r1.getList();
		}
		setAttr("data", list);
		setAttr("total", r.getTotalRow());
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"), "定时任务管理", "3", "定时任务管理-查询");
		renderJson();
    	
    }
    
    /**
     * 新增，修改页面
     */
    public void form(){
    	render("scheduleTaskForm.jsp");
    }
    
    /**
     * 选择类名页面
     */
    public void jobSelector(){
    	render("jobSelector.jsp");
    }
    
    /**
     * 获取任务编码和任务类名
     */
    public void getAllTaskClasses(){
    	 List<Class<?>> classes = getClasses("com.goodcol.job");
    	 List<Record> list=new ArrayList<Record>();
    	 int i=0;
         for (Class clas :classes) {
        	Record record=new Record();
        	 String rwlm=clas.getName();
        	 String[] rwbmarry=rwlm.split("\\.");
        	 String rwbm=rwbmarry[rwbmarry.length-1];
             record.set("rwbm", rwbm);
             record.set("rwlm", rwlm);
             list.add(i, record);
             i++;
         }
         setAttr("data", list);
         renderJson(); 
    }
    
    
    public void showCronTabWizard(){
    	render("showCronTab.jsp");
    }
    
    /**
     * 开启，停止监控
     * @param sfjk 是否监控
     * @param id 任务id
     * 
     */
    public void updatejk(){
    	String id=getPara("id");
    	String sfjk=getPara("sfjk");
    	String sql="update SYS_SCHEDULE_TASK set sfjk=? where id=?";
    	Db.update(sql,new Object[]{sfjk,id});
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"),
				"定时任务管理管理", "5",
				"开启或关闭监控");
		}
    
    /**
     * 开启，停止定时任务
     * @param rwbm 任务编码
     * @param id 任务id
     * @param rwlm 任务类名
     * @param rwqdsj 任务启动时间
     */
    public void restartTask(){
    	String id=getPara("id");
    	String rwzt=getPara("rwzt");
    	String rwlm=getPara("rwlm");
    	String rwqdsj=getPara("rwqdsj");
    	String rwbm=getPara("rwbm");
    	String sql="update SYS_SCHEDULE_TASK set rwzt=? where id=?";
    	Db.update(sql,new Object[]{rwzt,id});
    	if(rwzt.equals("0")){
			QuartzSchedule.addJob(rwlm,rwbm, rwqdsj);
		}else{
			QuartzSchedule.removeJob(rwlm,rwbm);
		}
		LoggerUtil.getIntanceof().saveLogger(
				getCurrentUser().getStr("USER_NO"),
				"定时任务管理", "5",
				"开启或关闭");
    }
    
    /**
     * 删除定时任务
     *  @param rwbm 任务编码
     */
    public void delete(){
    	String id=getPara("id");
    	Record record=Db.findFirst("select * from SYS_SCHEDULE_TASK where id=?",id);
    	Db.delete("SYS_SCHEDULE_TASK", record);
    	// 记录操作日志
    	LoggerUtil.getIntanceof().saveLogger(
    		getCurrentUser().getStr("USER_NO"),
    		"定时任务管理", "6",
    		"删除定时任务");
    }
    
    /**
     * 增加定时任务
     * @param rwbm 任务编码
     * @param rwlm 任务类名
     * @param rwmc 任务名称
     * @param rwqdsj 任务启动时间
     * @param sfjk 是否监控
     */
    public void  save() {
    	String rwlm=getPara("rwlm");
    	String rwbm=getPara("rwbm");
    	String rwmc=getPara("rwmc");
    	String rwqdsj=getPara("rwqdsj");
    	String sfjk=getPara("sfjk");
    	String bz=getPara("bz");
    	String rwzt=getPara("rwzt");
    	Record record=new Record();
    	record.set("id",AppUtils.getStringSeq() ).set("RWBM", rwbm).set("RWMC", rwmc)
    	.set("RWQDSJ", rwqdsj).set("RWLM", rwlm).set("RWZT", rwzt).set("BZ", bz)
    	.set("CJR", getCurrentUser().getStr("USER_NO")).set("CJSJ", DateTimeUtil.getTime())
    	.set("ZHXGR", getCurrentUser().getStr("USER_NO")).set("ZHXGSJ", DateTimeUtil.getTime())
    	.set("SFJK", sfjk).set("jq","1");
    	Db.save("SYS_SCHEDULE_TASK", record);
    	QuartzSchedule.addJob(rwlm,rwmc,rwqdsj);
    	LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "定时任务管理", "4", "定时任务-新增");
    	renderNull();
	}
    
    
    /**
     * 修改定时任务
     * @param id 任务id
     * @param rwbm 任务编码
     * @param rwlm 任务类名
     * @param rwmc 任务名称
     * @param rwqdsj 任务启动时间
     */
    public void  update() {
    	String id=getPara("id");
    	String rwlm=getPara("rwlm");
    	String rwbm=getPara("rwbm");
    	String rwmc=getPara("rwmc");
    	String rwqdsj=getPara("rwqdsj");
    	String sfjk=getPara("sfjk");
    	String bz=getPara("bz");
    	Db.update("update SYS_SCHEDULE_TASK set rwbm=?,rwmc=?,rwqdsj=?,rwlm=?,"
    			+ "bz=?,ZHXGR=?,ZHXGSJ=?,sfjk=?  where id = ? ", 
    			new Object[] {rwbm,rwmc, rwqdsj,rwlm,bz,
    					getCurrentUser().getStr("USER_NO"),DateTimeUtil.getTime(),sfjk,
    					id});
    	LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "定时任务管理", "5", "定时任务-修改");
    	renderNull();
	}
    
    
    /**
     * 新增任务时判断该任务是否已经添加
     * @param rwlm 任务类名
     */
    public void checkrwlm(){
    	String rwlm=getPara("rwlm");
    	List<Record> r=Db.find("select * from SYS_SCHEDULE_TASK where rwlm=?",rwlm);
    	if(r.size()!=0){
    		setAttr("count", "0");
    	}
    	renderJson();
    }
    
    
    /**
     * 从包package中获取所有的Class
     * @param pack
     * @return
     */
    public static List<Class<?>> getClasses(String packageName){
        List<Class<?>> classes = new ArrayList<Class<?>>();
        boolean recursive = true;
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()){
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                    break;
                } else if ("jar".equals(protocol)){
                    JarFile jar;
                    try {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.charAt(0) == '/') {
                                name = name.substring(1);
                            }
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                if (idx != -1) {
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                if ((idx != -1) || recursive){
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                      }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } 
                }
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        return classes;
    }
    
    /**
     * 以文件的形式来获取包下的所有Class
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes){
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirFiles = dir.listFiles(new FileFilter() {
              public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
              }
            });
        for (File file : dirFiles) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
                                      file.getAbsolutePath(),
                                      recursive,
                                      classes);
            }
            else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    
    public void jqTask(){
    	String id=getPara("id");
    	String jq=getPara("jq");
    	if(RedisUtil.isRedisOpen()){
    		String sql="update SYS_SCHEDULE_TASK set jq=? where id=?";
        	Db.update(sql,new Object[]{jq,id});
        	setAttr("hckg", "open");
    	}else{
    		setAttr("hckg", "close");
    	}
    	renderJson();
    }
    
}

