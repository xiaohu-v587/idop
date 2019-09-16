package com.goodcol.util.execl;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.goodcol.core.plugin.activerecord.Record;
import com.jfinal.render.Render;

/**
 * 该类是扩展或者说重新定义jfinalexecl导出工具
 * <br/>
 * 1.实现传入配置化的参数定义合并头部参数
 * <br/>
 * 2.实现合并单元格(纵向)
 * <br/>
 * 3.自定义参数集传入 20180316 加入，用于不同sheet页不同的数据集
 * <br/>
 * 4.生成的数据如果大于 2M 使用 gzip 模式进行输出
 * <br/>
 * 5.允许自定义合并单元格，合并对象由外部传入
 * 
 * 自定义扩展类
 * @author cxy
 * @category 20180319
 * @version 1.3
 */
public class ExcelRenderx extends Render {

	public HttpServletRequest request; //http请求对象
	public HttpServletResponse response; //http响应对象

	private String[] columns; //列名指定用哪些查询出来的字段填充excel文件
	private String[][] columnsMul;
	private String[][] columnsMulNum;
	
	private String[] mergedColumns;//需要合并的列字段
	private String[] headMergedColumns;//需要合并的头
	
	
	private Map<Integer,Integer> mycellWidth;
	
	private String[] headers;//excel文件的每列的名称,顺序与填充字段的顺序保持一致
	private long maxByte = 2*1024*1024;//设置默认的gzip使用大小
	private String fileName = "defaultFilename";//如果不传默认设置文件名
	private List<Map<String,Object>> param;//自定义参数集
	private boolean isBatch = false;//是否是自定义参数集传入
	
	private String headName;//头部名称

	private String sheetName = "sheet";//默认的sheet页名称对象

	private List<Record> data;//传入的数据项
	
	private List<CellRangeAddress> mergecells = new ArrayList<CellRangeAddress>();//自定义合并对象

	/**
	 * 多数据集多sheet模式
	 * @param fileName //文件名
	 * @param param //参数及
	 * @param response //响应对象
	 */
	public ExcelRenderx(String fileName,List<Map<String,Object>> param, HttpServletResponse response) {
		this.fileName = fileName;
		this.response = response;
		this.param = param;
		this.isBatch = true;
	}
	
	
	public ExcelRenderx(String fileName, String[] headers, List<Record> data, HttpServletResponse response) {
		this.fileName = fileName;
		this.headers = headers;
		this.response = response;
		this.data = data;
	}
	
	public ExcelRenderx(String fileName, String[] headers, String[] columns, List<Record> data, HttpServletResponse response) {
		this.fileName = fileName;
		this.headers = headers;
		this.columns = columns;
		this.data = data;
		this.response = response;
	}
	
	public ExcelRenderx(String fileName, String[] headers, String[] columns,Map<Integer,Integer> mycellWidth, List<Record> data, HttpServletResponse response) {
		this.fileName = fileName;
		this.headers = headers;
		this.columns = columns;
		this.data = data;
		this.response = response;
		this.mycellWidth = mycellWidth;
	}
	
	public ExcelRenderx(String headName,String fileName, String[] headers, String[] columns, List<Record> data, HttpServletResponse response) {
		this.fileName = fileName;
		this.headers = headers;
		this.columns = columns;
		this.data = data;
		this.response = response;
		this.headName = headName;
	}
	
	public ExcelRenderx(String headName,String fileName, String[] headers, String[] columns,Map<Integer,Integer> mycellWidth, List<Record> data, HttpServletResponse response) {
		this.fileName = fileName;
		this.headers = headers;
		this.columns = columns;
		this.data = data;
		this.response = response;
		this.headName = headName;
		this.mycellWidth = mycellWidth;
	}
	
	public ExcelRenderx(String fileName, String[] headers, String[] columns,String[] mergedColumns, List<Record> data, HttpServletResponse response) {
		this.fileName = fileName;
		this.headers = headers;
		this.columns = columns;
		this.data = data;
		this.response = response;
		this.mergedColumns = mergedColumns;
	}
	public ExcelRenderx(String fileName, String[] headers, String[] columns,String[] mergedColumns,String[] headMergedColumns, List<Record> data, HttpServletResponse response) {
		this.fileName = fileName;
		this.headers = headers;
		this.columns = columns;
		this.data = data;
		this.response = response;
		this.mergedColumns = mergedColumns;
		this.headMergedColumns = headMergedColumns;
	}
	
	
	
	public ExcelRenderx(String fileName, String[] headers, String[][] columnsMul,String[][] columnsMulNum, List<Record> data, HttpServletResponse response) {
		this.fileName = fileName;
		this.headers = headers;
		this.columnsMul = columnsMul;
		this.data = data;
		this.response = response;
		this.columnsMulNum = columnsMulNum;
	}
	

	public ExcelRenderx(String headName,String fileName, String[] headers, String[] columns,String[] mergedColumns,String[] headMergedColumns,String[][] columnsMul,String[][] columnsMulNum,Map<Integer,Integer> mycellWidth, List<Record> data, HttpServletResponse response) {
		this.headName = headName;
		this.fileName = fileName;
		this.headers = headers;
		this.columns = columns;
		this.data = data;
		this.response = response;
		this.mergedColumns = mergedColumns;
		this.headMergedColumns = headMergedColumns;
		this.columnsMul = columnsMul;
		this.columnsMulNum = columnsMulNum;
	}

	

	public void setMaxByte(long maxByte){
		this.maxByte = maxByte;
	}
	public Long getMaxByte(){
		return this.maxByte;
	}
	
	public List<CellRangeAddress> getMergecells() {
		return mergecells;
	}


	public void setMergecells(List<CellRangeAddress> mergecells) {
		this.mergecells = mergecells;
	}


	@Override
	public void render() {
		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
		response.setContentType("application/msexcel;charset=utf-8");// 定义输出类型
		ByteArrayOutputStream os = null;
		OutputStream out = null;
		GZIPOutputStream gzout = null;
		try {
			out = response.getOutputStream();// 取得输出流
			os = new  ByteArrayOutputStream();
			HSSFWorkbook wb = null;
			if(this.isBatch){
				wb = ExcelKitx.export(param);
			}else{
				wb = ExcelKitx.export(headName,sheetName, 0, headers, columns, mergedColumns,headMergedColumns, data, 0,mycellWidth,mergecells);
			}
			wb.write(os);
			byte [] data = os.toByteArray();
			if(os.size()>maxByte){
				response.setHeader("Content-disposition", "attachment; filename=" + fileName+".gzip");// 设定输出文件头
				gzout = new GZIPOutputStream(out);
				gzout.write(data);
			}else{
				out.write(data);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				os.flush();
				os.close();
				if(gzout!=null){
					gzout.flush();
					gzout.close();
				}
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 该函数调用ExcelKitx的export函数中会对表格的标题进行列合并
	 */
	public void renderMulRow() {
		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
		response.setContentType("application/msexcel;charset=utf-8");// 定义输出类型
		ByteArrayOutputStream os = null;
		OutputStream out = null;
		GZIPOutputStream gzout = null;
		try {
			out = response.getOutputStream();// 取得输出流
			os = new  ByteArrayOutputStream();
			HSSFWorkbook wb = ExcelKitx.export(headers, columnsMul, columnsMulNum, data, 0);
			wb.write(os);
			byte [] data = os.toByteArray();
			if(os.size()>maxByte){
				response.setHeader("Content-disposition", "attachment; filename=" + fileName+".gzip");// 设定输出文件头
				gzout = new GZIPOutputStream(out);
				gzout.write(data);
			}else{
				out.write(data);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				os.flush();
				os.close();
				if(gzout!=null){
					gzout.flush();
					gzout.close();
				}
			} catch (Exception e) {
			}
		}
	}
	
}
