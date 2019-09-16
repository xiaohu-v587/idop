package com.goodcol.controller.activiti;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collections;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.bpmn.diagram.Bpmn20NamespaceContext;
import org.activiti.engine.repository.Model;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.kit.JsonKit;
import com.goodcol.util.activiti.ActivitiUtil;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * 流程管理控制器
 *
 * @author puyu
 */
@RouteBind(path = "/actividesig")
@Before( { ManagerPowerInterceptor.class })
public class ActiviDesigController extends BaseCtl{

	
    protected Logger logger =  Logger.getLogger(ActiviDesigController.class);
    @Override
	public void index() {
    	render("index.html");
	}
    
	public void saveProcessDef() throws Exception {

		// 流程数据
		String processDescriptor = getPara("processDescriptor");
		String processName = getPara("processName");
		String processKey = getPara("processId");
		String processVariables = getPara("processVariables");

		//String json = convertXmlToJson(processDescriptor);
		String json = processDescriptor;

		RepositoryService repositoryService = ActivitiUtil.getProcessEngine().getRepositoryService();
		Model model1 = repositoryService.newModel();
		model1.setKey(processKey);
		model1.setName(processName);
		repositoryService.saveModel(model1);
		repositoryService.addModelEditorSource(model1.getId(), json.getBytes());
		setAttr("result", "success");
		renderJson();
	}
    
	
    public static String convertXmlToJson(String xml) {
    	 XmlMapper xmlMapper = new XmlMapper();
         ObjectMapper objectMapper = new ObjectMapper();

        StringWriter w = new StringWriter();
        try {
            JsonParser jp = xmlMapper.getFactory().createParser(xml);
            JsonGenerator jg = objectMapper.getFactory().createGenerator(w);
            while (jp.nextToken() != null) {
                jg.copyCurrentEvent(jp);
            }
            jp.close();
            jg.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return w.toString();
    }
    
    public static void main(String[] args) throws Exception {
    	File file = new File("F:\\processDesignFile\\test.txt");
    	StringBuffer sb = new StringBuffer();
    	BufferedReader br = new BufferedReader(new FileReader(file));
    	String str = null;
    	 while((str = br.readLine()) != null){
    		sb.append(str);
    	}
    	 System.out.println(sb.toString());
    	ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(sb.toString());
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
        InputStream inputStream = processDiagramGenerator.generateDiagram(bpmnModel,
                "png",
                Collections.<String>emptyList(), Collections.<String>emptyList(),
                "WenQuanYi Micro Hei", "WenQuanYi Micro Hei","",
                null, 1.0);
//        InputStream inputStream = processDiagramGenerator.generatePngDiagram(bpmnModel);
        File file1 =new File("F:/processDesignFile/aaaaaaa.png");
        FileUtils.copyInputStreamToFile(inputStream, file1);
	}
    
    
}
