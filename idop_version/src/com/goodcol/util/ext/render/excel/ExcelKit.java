 package com.goodcol.util.ext.render.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import com.goodcol.core.plugin.activerecord.Model;
import com.goodcol.core.plugin.activerecord.Record;

/**
 * description:
 * 
 */
public class ExcelKit {
	private final static int MAX_ROWS = 65536;
	public final static int HEADER_ROW = 1;
	public final static int DEFAULT_CELL_WIDTH = 8000;

	/**
	 * 
	 * @param sheetName
	 *            sheet名称
	 * @param cellWidth
	 *            设置单元格宽度
	 * @param headerRow
	 *            设置头列占的行数
	 * @param headers
	 *            头列值
	 * @param columns
	 *            列key(即 list<Map<String ,Ojbect>> 中 map的key)
	 * @param list
	 *            数据
	 * @return
	 */

	@SuppressWarnings( { "deprecation", "unchecked" })
	private static HSSFWorkbook export(String sheetName, int cellWidth, int headerRow, String[] headers, String[] columns, List<Record> list, int columnNum, boolean hasHeaders) {
		if (sheetName == null || sheetName.isEmpty()) {
			sheetName = "new sheet";
		}
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook();
			
			int number = 1;
			if(list.size()>65535&&list.size()%65535>0){
				number = (list.size()/65535)+1;
			}
			List<Record> datas = new ArrayList<Record>();
			datas.addAll(list);
			for(int ii=0;ii<number;ii++){
				list.clear();
				int count = 65535*(ii+1);
				if(count>datas.size()){
					count = datas.size();
				}
				for(int j=65535*ii;j<count;j++){
					list.add(datas.get(j));
				}				
				HSSFSheet sheet = wb.createSheet("sheet"+(ii+1));
				HSSFRow row = null;
				HSSFCell cell = null;
				setCellWidth(sheet, cellWidth, columnNum);
				if (hasHeaders) {
					row = sheet.createRow(0);
					if (headerRow <= 0) {
						headerRow = HEADER_ROW;
					}
					headerRow = Math.min(headerRow, MAX_ROWS);
					for (int h = 0, lenH = headers.length; h < lenH; h++) {
						Region region = new Region(0, (short) h, (short) headerRow - 1, (short) h);// 合并从第rowFrom行columnFrom列
						sheet.addMergedRegion(region);// 到rowTo行columnTo的区域
						// 得到所有区域
						sheet.getNumMergedRegions();
						sheet.setColumnWidth(h, cellWidth);
						cell = row.createCell(h);
						cell.setCellValue(headers[h]);
					}
				}

				if (list == null || list.size() == 0) {
					return wb;
				}
				for (int i = 0, len = list.size(); i < len; i++) {
					row = sheet.createRow(i + headerRow);
					Object obj = list.get(i);
					if (obj == null) {
						continue;
					}
					if (obj instanceof Map) {

						Map<String, Object> map = (Map<String, Object>) obj;
						if (columns.length == 0) {// 未设置显示列，默认全部
							int columnIndex = 0;
							for (String key : headers) {
								cell = row.createCell(columnIndex);
								
								if (map.get(key)==null )
									cell.setCellValue("");
								else
									cell.setCellValue(map.get(key) + "");
								
								columnIndex++;
							}
						} else {
							for (int j = 0, lenJ = columns.length; j < lenJ; j++) {
								cell = row.createCell(j);
								cell.setCellValue(map.get(columns[j]) + "");
							}
						}
					} else if (obj instanceof Model) {
						Model<?> model = (Model<?>) obj;
						Set<Entry<String, Object>> entries = model.getAttrsEntrySet();
						if (columns.length == 0) {// 未设置显示列，默认全部
							int columnIndex = 0;
							for (Entry<String, Object> entry : entries) {
								cell = row.createCell(columnIndex);
								
								if (entry.getValue()==null )
									cell.setCellValue("");
								else
									cell.setCellValue(entry.getValue() + "");
								columnIndex++;
							}
						} else {
							for (int j = 0, lenJ = columns.length; j < lenJ; j++) {
								cell = row.createCell(j);
								if (model.get(columns[j]) ==null )
									cell.setCellValue("");
								else
									cell.setCellValue(model.get(columns[j])  + "");
								
							}
						}

					} else if (obj instanceof Record) {
						Record record = (Record) obj;
						Map<String, Object> map = record.getColumns();
						if (columns.length == 0) {// 未设置显示列，默认全部
							record.getColumns();
							int columnIndex = 0;
							for (String key : headers) {
								cell = row.createCell(columnIndex);
								if (record.get(key) == null) {
									cell.setCellValue(" ");
								} else {
									cell.setCellValue(record.get(key) + "");
								}
								columnIndex++;
							}
						} else {
							for (int j = 0, lenJ = columns.length; j < lenJ; j++) {
								cell = row.createCell(j);
								if (map.get(columns[j]) == null) {
									cell.setCellValue(" ");
								} else {
									cell.setCellValue(map.get(columns[j]) + "");
								}
								
							}
						}

					}
				}
			}
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return wb;
	}

	/**
	 * 设置单元格宽度
	 * 
	 * @param sheet
	 * @param cellWidth
	 * @param columnNum
	 */
	private static void setCellWidth(HSSFSheet sheet, int cellWidth, int columnNum) {
		for (int i = 0; i < columnNum; i++) {
			sheet.setColumnWidth(i, cellWidth);
		}

	}

	/**
	 * @param String
	 *            sheetName sheet名称
	 * @param int headerRow 设置头列占的行数
	 * @param String
	 *            [] headers 头列值
	 * @param String
	 *            [] columns 列key(即 list<Map<String ,Ojbect>> 中 map的key)
	 * @param List
	 *            <Map<String, Object>> list 数据
	 * @param int cellWidth 设置单元格宽度
	 * @return
	 */
	public static HSSFWorkbook export(String sheetName, int headerRow, String[] headers, String[] columns, List<Record> list, int cellWidth) {
		boolean hasHeaders = false;
		int columnNum = 0;
		if (headers != null && headers.length > 0) {
			hasHeaders = true;
			columnNum = headers.length;
		}
		if (columns == null) {
			columns = new String[] {};
		}
		columnNum = Math.max(columnNum, columns.length);
		if (cellWidth <= 0) {
			cellWidth = DEFAULT_CELL_WIDTH;
		}
		return export(sheetName, cellWidth, headerRow, headers, columns, list, columnNum, hasHeaders);

	}
	
	/**
	 * 
	 * @param sheetName
	 *            sheet名称
	 * @param cellWidth
	 *            设置单元格宽度
	 * @param headerRow
	 *            设置头列占的行数
	 * @param headers
	 *            头列值
	 * @param columns
	 *            列key(即 list<Map<String ,Ojbect>> 中 map的key)
	 * @param list
	 *            数据
	 * @return
	 */

	@SuppressWarnings( { "deprecation", "unchecked", "static-access" })
	private static HSSFWorkbook download(String sheetName, int cellWidth, int headerRow, String[] headers, String[] columns, List<Record> list, int columnNum, boolean hasHeaders) {
		
		if (sheetName == null || sheetName.isEmpty()) {
			sheetName = "new sheet";
		}
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet(sheetName);
			HSSFCellStyle headerstyle= wb.createCellStyle();//头部样式对象
//			HSSFFont font = wb.createFont();//字体对象
//			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//			headerstyle.setFont(font);
			headerstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
			headerstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
			HSSFCellStyle contextstyle= wb.createCellStyle();//内容样式对象
			contextstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
			contextstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
			HSSFRow row = null;
			HSSFCell cell = null;
			setCellWidth(sheet, cellWidth, columnNum);
			if (hasHeaders) {
				row = sheet.createRow(0);
				if (headerRow <= 0) {
					headerRow = HEADER_ROW;
				}
				headerRow = Math.min(headerRow, MAX_ROWS);
				for (int h = 0, lenH = headers.length; h < lenH; h++) {
					Region region = new Region(0, (short) h, (short) headerRow - 1, (short) h);// 合并从第rowFrom行columnFrom列
					sheet.addMergedRegion(region);// 到rowTo行columnTo的区域
					// 得到所有区域
					sheet.getNumMergedRegions();
					sheet.setColumnWidth(h, cellWidth);
					cell = row.createCell(h);
					cell.setCellStyle(headerstyle);
					cell.setCellValue(headers[h]);
				}
			}
			Boolean isNum=false;//是否为数值型
			Boolean isInteger=false;//是否为整数
			Boolean isPercent=false;//是否为百分数
			
			if (list == null || list.size() == 0) {
				return wb;
			}
			for (int i = 0, len = list.size(); i < len; i++) {
				row = sheet.createRow(i + headerRow);
				Object obj = list.get(i);
				if (obj == null) {
					continue;
				}
				if (obj instanceof Map) {

					Map<String, Object> map = (Map<String, Object>) obj;
					if (columns.length == 0) {// 未设置显示列，默认全部
						int columnIndex = 0;
						for (String key : headers) {
							cell = row.createCell(columnIndex);
							
							if (map.get(key)==null )
								cell.setCellValue("");
							else
								cell.setCellValue(map.get(key) + "");
							
							columnIndex++;
						}
					} else {
						for (int j = 0, lenJ = columns.length; j < lenJ; j++) {
							cell = row.createCell(j);
							cell.setCellValue(map.get(columns[j]) + "");
						}
					}
				} else if (obj instanceof Model) {
					Model<?> model = (Model<?>) obj;
					Set<Entry<String, Object>> entries = model.getAttrsEntrySet();
					if (columns.length == 0) {// 未设置显示列，默认全部
						int columnIndex = 0;
						for (Entry<String, Object> entry : entries) {
							cell = row.createCell(columnIndex);
							
							if (entry.getValue()==null )
								cell.setCellValue("");
							else
								cell.setCellValue(entry.getValue() + "");
							columnIndex++;
						}
					} else {
						for (int j = 0, lenJ = columns.length; j < lenJ; j++) {
							cell = row.createCell(j);
							if (model.get(columns[j]) ==null )
								cell.setCellValue("");
							else
								cell.setCellValue(model.get(columns[j])  + "");
							
						}
					}

				} else if (obj instanceof Record) {
					Record record = (Record) obj;
					Map<String, Object> map = record.getColumns();
					if (columns.length == 0) {// 未设置显示列，默认全部
						record.getColumns();
						int columnIndex = 0;
						for (String key : headers) {
							cell = row.createCell(columnIndex);
							if (record.get(key) == null) {
								cell.setCellValue(" ");
							} else {
								String data=record.get(key).toString();
								//判断是否为数值型
								isNum=data.matches("^(-?\\d+)(\\.\\d+)?$");
								//是否为整数
								isInteger=data.matches("^[-\\+]?[\\d]*$");
								//是否为百分数
								isPercent=data.contains("%");
								if(isNum&&!isPercent){
									HSSFDataFormat df= wb.createDataFormat();
									if(isInteger){
										contextstyle.setDataFormat(df.getBuiltinFormat("#,#0"));
									}else{
										contextstyle.setDataFormat(df.getBuiltinFormat("#,##0.00"));//小数保留两位小数
									}
									cell.setCellStyle(contextstyle);
									cell.setCellValue(Double.parseDouble(data));
								}else{
									cell.setCellStyle(contextstyle);
									cell.setCellValue(data);
								}
							}
							columnIndex++;
						}
					} else {
						for (int j = 0, lenJ = columns.length; j < lenJ; j++) {
							cell = row.createCell(j);
							if (map.get(columns[j]) == null) {
								cell.setCellValue(" ");
							} else {
								String data=map.get(columns[j]).toString();
								//判断是否为数值型
								isNum=data.matches("^(-?\\d+)(\\.\\d+)?$");
								//是否为整数
								isInteger=data.matches("^[-\\+]?[\\d]*$");
								//是否为百分数
								isPercent=data.contains("%");
								if(isNum&&!isPercent){
									HSSFDataFormat df= wb.createDataFormat();
									if(isInteger){
										contextstyle.setDataFormat(df.getBuiltinFormat("#,#0"));
									}else{
										contextstyle.setDataFormat(df.getBuiltinFormat("#,##0.00"));//小数保留两位小数
									}
									cell.setCellStyle(contextstyle);
									cell.setCellValue(Double.parseDouble(data));
								}else{
									cell.setCellStyle(contextstyle);
									cell.setCellValue(data);
								}
								//cell.setCellValue(data);
							}
							
						}
					}

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return wb;
	}
	
	public static HSSFWorkbook download(String sheetName, int headerRow, String[] headers, String[] columns, List<Record> list, int cellWidth) {
		boolean hasHeaders = false;
		int columnNum = 0;
		if (headers != null && headers.length > 0) {
			hasHeaders = true;
			columnNum = headers.length;
		}
		if (columns == null) {
			columns = new String[] {};
		}
		columnNum = Math.max(columnNum, columns.length);
		if (cellWidth <= 0) {
			cellWidth = DEFAULT_CELL_WIDTH;
		}
		return download(sheetName, cellWidth, headerRow, headers, columns, list, columnNum, hasHeaders);

	}
}