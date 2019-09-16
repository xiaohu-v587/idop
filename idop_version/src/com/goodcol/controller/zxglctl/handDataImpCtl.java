package com.goodcol.controller.zxglctl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import net.sf.json.JSONArray;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.upload.UploadFile;
import com.goodcol.server.zxglserver.CustAppealServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;

/**
 * 客户申诉
 * 
 * @author start
 * 
 */
@RouteBind(path = "/handDataImp")
public class handDataImpCtl extends BaseCtl {
	
	public static Logger log = Logger.getLogger(handDataImpCtl.class);
	CustAppealServer custAppealServer = new CustAppealServer();
	
	/**
	 * 主页面初始化
	 */
	public void index() {
		render("upload.jsp");
	}
	
	/**
	 * 上传excel
	 */
	public void importData(){
 		HSSFWorkbook hb = null;
 		InputStream is = null;
 		HSSFRow row = null;
 		Set<Map<String, Object>> entitySet = new HashSet<Map<String, Object>>();
 		Map<String, Object> map = null;
 		StringBuffer msg = new StringBuffer();
 		String msgStr = null;
 		try {
 			UploadFile uploadFile=this.getFile("upload_file");
  			// 判断有文件才进行上传
  			if(uploadFile!=null){
  				File file=uploadFile.getFile();
  				//文件名
  				String fileName = file.getName();
  				//尾缀名
  				String suffix = fileName.substring(fileName.lastIndexOf("."));
  				if(!".xls".equals(suffix)){
  					renderFailJsonMsg("请上传尾缀是xls的Excel文件！");
  					return;
  				}
  				is = new FileInputStream(file);
  				hb = new HSSFWorkbook(is);
  				HSSFSheet sheet = hb.getSheetAt(0);
  				int totalrows = sheet.getLastRowNum()+1;
  				if(totalrows<2){
  					renderFailJsonMsg("Excel无数据！");
  					return;
  				}
  				for(int i=0;i<sheet.getLastRowNum()+1;i++){
  					map = new HashMap<String, Object>();
  					msgStr = "第"+(i+1)+"数据：<br/>" ;
  					//创建一个行对象
  					row = sheet.getRow(i);
  					//读取行内容
  					String custNo = row.getCell(0).getStringCellValue().trim();
  					String custName = row.getCell(1).getStringCellValue().trim();
  					
  					//校验数据
  					if(AppUtils.StringUtil(custNo) != null){
  						map.put("custNo", custNo);
  					}else{
  						msg.append("客户号不能为空！<br/>") ;
  					}
  					if(AppUtils.StringUtil(custName) != null){
  						map.put("custName", custName);
  					}else{
  						msg.append("客名称不能为空！<br/>") ;
  					}
  					
//  					//校验数据是否已经存在
//  					if(1==1){//存在则提示
//  						msg.append("此客户信息已分配！<br/>") ;
//  					}else{
//  						entitySet.add(map);
//  					}
  					
  					
  					
  					if(AppUtils.StringUtil(msg.toString()) != null){
  						renderSuccessJsonMsg(msgStr+msg.toString());
  						return;
  					}else{
  						//遍历entitySet 数据入库
  						for(int j=0;j<entitySet.size();j++){
  							
  						}
  	  					
  					}
  				}
  				renderSuccessJsonMsg( "上传成功！");
  			}else{
  				renderFailJsonMsg("请先上传文件！");
  				return;
  			}
  			
  		} catch (Exception e) {
  			renderFailJsonMsg("上传失败！");
  		}
	}
	
	
}
