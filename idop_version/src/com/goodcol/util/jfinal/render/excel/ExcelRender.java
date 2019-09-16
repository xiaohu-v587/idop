 package com.goodcol.util.jfinal.render.excel;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.execl.ExcelUtil;
import com.goodcol.util.ext.render.excel.ExcelKit;
import com.jfinal.render.Render;

public class ExcelRender extends Render {

	public HttpServletRequest request;
	public HttpServletResponse response;

	private String[] columns;
	private String[] headers;
	private List<ExcelColumn> list;
	private String fileName = "defaultFilename";

	private String sheetName = "sheet1";

	private List<Record> data;

	public ExcelRender(String fileName, String[] headers, List<Record> data, HttpServletResponse response) {
		this.fileName = fileName;
		this.headers = headers;
		this.response = response;
		this.data = data;
	}
	public ExcelRender(String fileName, String[] headers, String[] columns, List<Record> data, HttpServletResponse response) {
		this.fileName = fileName;
		this.headers = headers;
		this.columns = columns;
		this.data = data;
		this.response = response;
	}
	public ExcelRender(String fileName, List<ExcelColumn> list, List<Record> data, HttpServletResponse response) {
		this.fileName = fileName;
		this.list= list;
		this.data = data;
		this.response = response;
	}
	@Override
	public void render() {
		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
		response.setContentType("application/msexcel;charset=utf-8");// 定义输出类型
		OutputStream os = null;
		try {
			os = response.getOutputStream();// 取得输出流
			HSSFWorkbook wb = ExcelKit.export(sheetName, 0, headers, columns, data, 0);
			wb.write(os);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				os.flush();
				os.close();
			} catch (Exception e) {
			}
		}
	}
	//导出Excel 单元格格式问题 
	public void renderExcel(){
		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
		response.setContentType("application/msexcel;charset=utf-8");// 定义输出类型
		OutputStream os = null;
		try {
			os = response.getOutputStream();// 取得输出流
			HSSFWorkbook wb = ExcelKit.download(sheetName, 0, headers, columns, data, 0);
			wb.write(os);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				os.flush();
				os.close();
			} catch (Exception e) {
			}
		}
	}
	
		//导出Excel 和并列  
		public void renderMergre(){
			response.reset();// 清空输出流
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
			response.setContentType("application/msexcel;charset=utf-8");// 定义输出类型
			OutputStream os = null;
			try {
				os = response.getOutputStream();// 取得输出流
				HSSFWorkbook wb = ExcelUtil.getWorkBook(list,data);
				wb.write(os);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				try {
					os.flush();
					os.close();
				} catch (Exception e) {
				}
			}
		}
		
		
		//导出Excel 和并数据行
				public void renderMergreData(String[] title,Map<String,List<Map<String,String>>> maps,int[] mergeIdex ){
					response.reset();// 清空输出流
					response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
					response.setContentType("application/msexcel;charset=utf-8");// 定义输出类型
					OutputStream os = null;
					try {
						os = response.getOutputStream();// 取得输出流
						HSSFWorkbook wb = ExcelUtil.getWorkBookData(list,data,title,maps,mergeIdex);
						wb.write(os);
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
						try {
							os.flush();
							os.close();
						} catch (Exception e) {
						}
					}
				}
		
		
		
}
