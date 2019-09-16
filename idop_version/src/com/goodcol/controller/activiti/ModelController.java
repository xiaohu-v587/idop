package com.goodcol.controller.activiti;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.activiti.ActivitiUtil;
import com.goodcol.util.activiti.BeanToRecordUtil;
import com.goodcol.util.ext.anatation.RouteBind;


/**
 * 流程模型控制器
 *
 * @author HenryYan
 */
@RouteBind(path = "/model")
@Before( { ManagerPowerInterceptor.class })
public class ModelController extends BaseCtl {

    protected Logger logger =  Logger.getLogger(ModelController.class);
    @Override
	public void index() {
    	render("index.jsp");
	}
   
    /**
     * 模型列表
     *
     * @return
     */
    public void modelList() {
    	Record r;
    	int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize", 10);
		//数据存储集合
		List<Record> rList = new ArrayList<Record>();
		/*
	     * 保存对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
	     */
    	RepositoryService repositoryService = ActivitiUtil.getProcessEngine().getRepositoryService();
    	ModelQuery modelQuery = repositoryService.createModelQuery();
        List<Model> modelQueryList = modelQuery.listPage(pageNum, pageSize);
        //因与前台miniui对接需要json格式数据，但activiti实体对象无法进行序列化（实体中涉及Map,List,或其他类型对象）暂时采用手动组合方式进行转换为record进行json化
        BeanToRecordUtil bru = BeanToRecordUtil.getInstance();
        for (Model model : modelQueryList) {
        	r = bru.toRecord(model);
        	rList.add(r);
        }
        setAttr("data", rList);
		setAttr("total", modelQuery.count());
		renderJson();
    }
    
    /**
     * 创建模型索引页
     */
    public void createModelTemplate(){
    	render("createModelTemplate.jsp");
    }
    
    
    /**
     * 创建模型
     */
    public void create() {
        String name = getPara("name");
        String key = getPara("key");
        String description = getPara("description");
    	try {
        	RepositoryService repositoryService = ActivitiUtil.getProcessEngine().getRepositoryService();
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            description = StringUtils.defaultString(description);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(name);
            modelData.setKey(StringUtils.defaultString(key));

            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
            renderSuccessJsonMsg( "success");
        } catch (Exception e) {
            logger.error("创建模型失败：", e);
            renderFailJsonMsg("创建模型失败");
        }
    }
    
    
    /**
     * 根据Model部署流程
     */
    public void deploy() {
    	String modelId = getPara("modelId");
    	try {
//    		RepositoryService repositoryService = ActivitiUtil.getProcessEngine().getRepositoryService();
//            Model model = repositoryService.getModel(modelId);
////            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(model.getId()));
//            byte[] bpmnBytes = null;
//
////            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
////            bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
//            
//            bpmnBytes = repositoryService.getModelEditorSource(model.getId());
//            
//            String processName = model.getName() + ".bpmn20.xml";
//            Deployment deployment = repositoryService.createDeployment().name("测试").addString(processName, new String(bpmnBytes)).deploy();
//            
//            
////    		RepositoryService repositoryService = ActivitiUtil.getProcessEngine().getRepositoryService();
////    		Model model = repositoryService.getModel(modelId);
////    		byte[] bytes = repositoryService.getModelEditorSource(model.getId());
////    		Deployment deployment = repositoryService.createDeployment().name("")
//            
//            renderSuccessJsonMsg( "部署成功，部署ID=" + deployment.getId());
            
            
            
            // 暂时使用一个部署，从本地部署
            RepositoryService repositoryService = ActivitiUtil.getProcessEngine().getRepositoryService();
            File file = new File("F:/processDesignFile/test-AAA.bpmn");
            FileInputStream in = new FileInputStream(file);
            Deployment deployment =  repositoryService.createDeployment().name("测试AAA")
            					.addInputStream("test-AAA.bpmn", in)
            					.deploy();
            renderSuccessJsonMsg( "部署成功，部署ID=" + deployment.getId());
        } catch (Exception e) {
            logger.error("根据模型部署流程失败：modelId={"+modelId+"}", e);
            renderFailJsonMsg("根据模型部署流程失败：modelId={"+modelId+"}");
        }
    }
    
   
    /**
     * 导出model对象为指定类型
     *
     * @param modelId 模型ID
     * @param type    导出文件类型(bpmn\json)
     */
    public void export() {
    	
    	String modelId = getPara("modelId");
        String type = getPara("type");
        try {
        	RepositoryService repositoryService = ActivitiUtil.getProcessEngine().getRepositoryService();
            Model modelData = repositoryService.getModel(modelId);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            byte[] modelEditorSource = repositoryService.getModelEditorSource(modelData.getId());

            JsonNode editorNode = new ObjectMapper().readTree(modelEditorSource);
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);

            // 处理异常
            if (bpmnModel.getMainProcess() == null) {
                return;
            }
            String filename = "";
            byte[] exportBytes = null;

            String mainProcessId = bpmnModel.getMainProcess().getId();

            if (type.equals("bpmn")) {

                BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
                exportBytes = xmlConverter.convertToXML(bpmnModel);

                filename = mainProcessId + ".bpmn20.xml";
            } else if (type.equals("json")) {

                exportBytes = modelEditorSource;
                filename = mainProcessId + ".json";

            }
            OutputStream out = getResponse().getOutputStream();
            
            ByteArrayInputStream in = new ByteArrayInputStream(exportBytes);
            IOUtils.copy(in, out);
            getResponse().setHeader("Content-Disposition", "attachment; filename=" + filename);
            getResponse().flushBuffer();
        } catch (Exception e) {
            logger.error("导出model的xml文件失败：modelId={"+modelId+"}, type={"+type+"}", e);
        }
        renderNull();
    }
    

    /**
     * 删除模型
     *
     */
    public void del() {
    	String modelId = getPara("modelId");
    	String uuids = getPara("ids");
    	if (uuids.contains(",")) 
		{
			String[] array = uuids.split(regex);
			if(array!=null && array.length>0)
			{
				for (String id : array) 
				{
					ActivitiUtil.getProcessEngine().getRepositoryService().deleteModel(id);
				}
			}
		} else 
		{
			ActivitiUtil.getProcessEngine().getRepositoryService().deleteModel(uuids);
		}
    	renderSuccessJsonMsg("delete success");
    }

    

}