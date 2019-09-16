package com.goodcol.util.activiti;

import java.util.List;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.ValidationError;


/**
 * bpmn文件的基本操作
 * 
 * @author puyu
 *
 */
public class ActivitiBpmnModel {

	/**
	 * bpmn文件转xml
	 * 转化正确返回xml字符串
	 * 转化错误返回"error"
	 */
	public static String BpmnModeltoXml(String processDefinitionKey) {
		RepositoryService repositoryService =ActivitiUtil.getProcessEngine().getRepositoryService();
		ProcessDefinition processDefinition =repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
		BpmnXMLConverter converter = new BpmnXMLConverter();
		byte[] bytes = converter.convertToXML(bpmnModel);
		String xmlContenxt=new String(bytes);
		ProcessValidatorFactory processValidatorFactory=new ProcessValidatorFactory();
		ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
		List<ValidationError> validate = defaultProcessValidator.validate(bpmnModel);
		if(validate.size()==0){
			return xmlContenxt;
		}else{
			System.out.println("bpmn转化失败");
			return "error";
		}
    }
	
	
	
}
