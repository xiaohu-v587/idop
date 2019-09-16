package com.goodcol.controller.activiti;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.upload.UploadFile;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.activiti.ActivitiUtil;
import com.goodcol.util.activiti.BeanToRecordUtil;
import com.goodcol.util.ext.anatation.RouteBind;


/**
 * 流程管理控制器
 *
 * @author HenryYan
 */
@RouteBind(path = "/workflow")
@Before( { ManagerPowerInterceptor.class })
public class ActivitiController extends BaseCtl {

    protected Logger logger =  Logger.getLogger(ActivitiController.class);
    @Override
	public void index() {
    	render("index.jsp");
	}
   
    /**
     * 流程定义列表
     *
     * @return
     */
    public void processList() {
    	Record r;
    	int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize", 10);
		//数据存储集合
		List<Record> rList = new ArrayList<Record>();
		List<ProcessDefinition> processDefinitionList = null;
		String name = getPara("name");
		String key = getPara("key");
		String isNew = getPara("isNew");//是否查看最新的版本
    	RepositoryService repositoryService = ActivitiUtil.getProcessEngine().getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        if(AppUtils.StringUtil(name)!=null){
        	processDefinitionQuery.processDefinitionNameLike("%"+name+"%");
        }
        if(AppUtils.StringUtil(key)!=null){
        	processDefinitionQuery.processDefinitionKeyLike("%"+key+"%");
        }
        if(AppUtils.StringUtil(isNew)!=null&&isNew.equals("0")){
        	 processDefinitionQuery.orderByProcessDefinitionKey().asc().orderByProcessDefinitionVersion().desc();
        	 processDefinitionList = processDefinitionQuery.listPage(pageNum, pageSize);
        	 setAttr("total", processDefinitionQuery.count());
        }else{
        	 processDefinitionQuery.orderByProcessDefinitionVersion().asc();
        	 processDefinitionList = processDefinitionQuery.list();
        	 Map<String,ProcessDefinition> map=new LinkedHashMap<String,ProcessDefinition>();
     		 if(processDefinitionList!=null&&processDefinitionList.size()>0){
     			for(ProcessDefinition pd:processDefinitionList){
     				map.put(pd.getKey(), pd);
     			}
     		 }
     		 processDefinitionList=new ArrayList<ProcessDefinition>(map.values());
     		setAttr("total", 1);
        }
        
       
        //因与前台miniui对接需要json格式数据，但activiti实体对象无法进行序列化（实体中涉及Map,List,或其他类型对象）暂时采用手动组合方式进行转换为record进行json化
        BeanToRecordUtil bru = BeanToRecordUtil.getInstance();
        for (ProcessDefinition processDefinition : processDefinitionList) {
        	r = bru.toRecord(processDefinition);
        	String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            r.set("deploy_deploymentTime", deployment.getDeploymentTime());
        	rList.add(r);
        }
        setAttr("data", rList);
		
		renderJson();
    }
    
    /**
     * 发布流程
     */
    public void deployUpload(){
    	render("deployUpload.jsp");
    }
    
    /**
     *发布流程 目前流程不做重复流程限制
     */
    public void deploy() {
  		try {
  			// 判断有文件才进行上传
  			UploadFile file = getFile("Fdata");
  			String fileName = file.getOriginalFileName();
  			if (file != null) {
  				String newName = saveFile(file);
  				InputStream fileInputStream = new FileInputStream(new File(file.getSaveDirectory()+newName));
  	            String extension = FilenameUtils.getExtension(fileName);
  	            RepositoryService repositoryService = ActivitiUtil.getProcessEngine().getRepositoryService();
  	            if (extension.equals("zip") || extension.equals("bar")) {
  	            	ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
  	                repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
  	            } else {
  	                repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
  	            }
  			}
  			renderSuccessJsonMsg( "upload success");
  			
  		} catch (Exception e) {
  			logger.error("error on deploy process, because of file input stream", e);
  			renderFailJsonMsg("error on deploy process, because of file input stream");
  		}
    }
    
    
    /**
     * 删除部署的流程，级联删除流程实例
     *
     * @param deploymentId 流程部署ID
     */
    public void del() {
    	String uuids = getPara("ids");
    	if (uuids.contains(",")) 
		{
			String[] array = uuids.split(regex);
			if(array!=null && array.length>0)
			{
				for (String deploymentId : array) 
				{
					ActivitiUtil.deleteWFByManager(deploymentId);
				}
			}
		} else 
		{
			ActivitiUtil.deleteWFByManager(uuids);	
		}
    	renderSuccessJsonMsg("delete success");
    }
    
    
    
    /**
     * 读取资源，通过部署ID
     *
     * @param processDefinitionId 流程定义
     * @param resourceType        资源类型(xml|image)
     * @throws Exception
     */
    public void loadByDeployment() throws Exception {
    	String resourceType = getPara("resourcetype");
    	String processDefinitionId = getPara("processdefinitionid");
    	RepositoryService repositoryService = ActivitiUtil.getProcessEngine().getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        String resourceName = "";
        if (resourceType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resourceType.equals("xml")) {
            resourceName = processDefinition.getResourceName();
        }
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            getResponse().getOutputStream().write(b, 0, len);
        }
        renderNull();
    }

    
    public void convertToModel() {
    	String processDefinitionId = getPara("processdefinitionid");
    	try {
			RepositoryService repositoryService = ActivitiUtil.getProcessEngine().getRepositoryService();
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
			InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),processDefinition.getResourceName());
			XMLInputFactory xif = XMLInputFactory.newInstance();
			InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
			XMLStreamReader xtr = xif.createXMLStreamReader(in);
			BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

			BpmnJsonConverter converter = new BpmnJsonConverter();
			com.fasterxml.jackson.databind.node.ObjectNode modelNode = converter.convertToJson(bpmnModel);
			Model modelData = repositoryService.newModel();
			modelData.setKey(processDefinition.getKey());
			modelData.setName(processDefinition.getResourceName());
			modelData.setCategory(processDefinition.getDeploymentId());

			ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
			modelData.setMetaInfo(modelObjectNode.toString());

			repositoryService.saveModel(modelData);

			repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
			 renderSuccessJsonMsg("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error on convert to model ", e);
  			renderFailJsonMsg("error on convert to model");
		}

       
    }
    
    
    public void updateState() {
    	String state = getPara("state");
    	String processDefinitionId = getPara("processdefinitionid");
    	String msg = "";
    	RepositoryService repositoryService = ActivitiUtil.getProcessEngine().getRepositoryService();
		if (state.equals("active")) {
			msg = "已激活ID为[" + processDefinitionId + "]的流程定义。";
			repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
		} else if (state.equals("suspend")) {
			repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
			msg = "已挂起ID为[" + processDefinitionId + "]的流程定义。";
		}
		renderSuccessJsonMsg(msg);
    }
    
    
    
    /**
     * 待办任务--Portlet
     */
    public void todoList(HttpSession session) throws Exception {
        Record user = getCurrentUser();
        List<Record> result = new ArrayList<Record>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        // 已经签收的任务
        List<Task> todoList = ActivitiUtil.getProcessEngine().getTaskService().createTaskQuery().taskAssignee(user.getStr("USER_NO")).active().list();
        for (Task task : todoList) {
            String processDefinitionId = task.getProcessDefinitionId();
            ProcessDefinition processDefinition = getProcessDefinition(processDefinitionId);

            Record singleTask = packageTaskInfo(sdf, task, processDefinition);
            singleTask.set("status", "todo");
            result.add(singleTask);
        }

        // 等待签收的任务
        List<Task> toClaimList =ActivitiUtil.getProcessEngine().getTaskService().createTaskQuery().taskCandidateUser(user.getStr("USER_NO")).active().list();
        for (Task task : toClaimList) {
            String processDefinitionId = task.getProcessDefinitionId();
            ProcessDefinition processDefinition = getProcessDefinition(processDefinitionId);

            Record singleTask = packageTaskInfo(sdf, task, processDefinition);
            singleTask.set("status", "claim");
            result.add(singleTask);
        }
        setAttr("data", result);
		setAttr("total", result.size());
		renderJson();
    }
    

    private Record packageTaskInfo(SimpleDateFormat sdf, Task task, ProcessDefinition processDefinition) {
        Record r = new Record();
        r.set("id", task.getId());
        r.set("name", task.getName());
        r.set("createtime", sdf.format(task.getCreateTime()));
        r.set("pdname", processDefinition.getName());
        r.set("pdversion", processDefinition.getVersion());
        r.set("pid", task.getProcessInstanceId());
        return r;
    }

    private ProcessDefinition getProcessDefinition(String processDefinitionId) {
        return ActivitiUtil.getProcessEngine().getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
    }
    
    
    /**
     * 读取带跟踪的图片
     */
    public void readResource() throws Exception {
    	String processDefinitionId = getPara("processDefinitionId");
    	String executionId = getPara("executionId");
        InputStream imageStream = ActivitiUtil.tracePhoto(processDefinitionId, executionId) ;
        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        OutputStream out = getResponse().getOutputStream();
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
        	out.write(b, 0, len);
        }
        renderNull();
    }

    
    
   /**
    * 重新部署流程,开发中
    */
    public void deployFromClasspath() throws Exception {
    	try {
  			// 判断有文件才进行上传
  			UploadFile file = getFile("Fdata");
  			String fileName = file.getOriginalFileName();
  			
  			if (file != null) {
  				InputStream fileInputStream = new FileInputStream(new File(file.getSaveDirectory()+file.getFileName()));
  	            String extension = FilenameUtils.getExtension(fileName);
  	            RepositoryService repositoryService = ActivitiUtil.getProcessEngine().getRepositoryService();
  	            if (extension.equals("zip") || extension.equals("bar")) {
  	            	ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
  	                repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
  	            } else {
  	                repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
  	            }
  			}
  			renderSuccessJsonMsg( "upload success");
  			
  		} catch (Exception e) {
  			logger.error("error on deploy process, because of file input stream", e);
  			renderFailJsonMsg("error on deploy process, because of file input stream");
  		}
    }
}