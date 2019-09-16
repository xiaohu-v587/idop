package com.goodcol.util.execl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.Region;

import com.goodcol.core.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.Model;

/**
 * description:
 * 
 */
public class ExcelKitx {
	private final static int MAX_ROWS = 65536;//每页最大行数
	public final static int HEADER_ROW = 1;//默认的头部行是第一行
	public final static int MAX_SHEETS = 30;//最大可分页数 为 30页
	public final static int DEFAULT_CELL_WIDTH = 8000;

	/**
	 * 
	 * 自定义导出工具类核心生成工具
	 * @author cxy lpc 
	 * @category 20180319
	 * 
	 * @param wb
	 * 			  HSSFWorkbook 对象因多sheet问题需要外部传入，不传时，默认创建一个
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
	 * @param mergedColumns
	 *            列key(即 list<Map<String ,Ojbect>> 中 map的key)  //合并单元格元素，例如说合并那些列（字段名）
	 * @param headMergedColumns
	 *            列key(即 list<Map<String ,Ojbect>> 中 map的key)  //合并单元格分组元素，例如说依照于那个元素进行分组，比如说 以第一列为分组，合并1到3列分组内相同的值
	 * @param list
	 *            数据
	 * @return
	 */

	@SuppressWarnings( { "deprecation", "unchecked" })
	private static HSSFWorkbook export(HSSFWorkbook wb,String headName,String sheetName, int cellWidth, int headerRow, String[] headers, String[] columns,String [] mergedColumns,String [] headMergedColumns, List<Record> list, int columnNum,boolean hasMergedHead,boolean hasMerged,boolean hasHeaders,int sheetNum,Map<Integer,Integer> mycellWidth,boolean isCustomMerge,List<CellRangeAddress>  mergecells) {
		if (sheetName == null || sheetName.isEmpty()) {
			sheetName = "new sheet";
		}
		
		int len_t;
		List<Record> tmplist = new ArrayList<Record>();
		Map<String,Region> maps = new HashMap<String, Region>();
		List<Region> mergedList;
		Map<String,Region> headMergedMaps = new HashMap<String, Region>();
		
		
		
		try {
			if(wb == null ){
				wb = new HSSFWorkbook();
			}
			for (int k = 0; k < sheetNum; k++) {    
				tmplist.clear();
				len_t = list.size();
				if(len_t>MAX_ROWS){
					len_t = MAX_ROWS;
				}
				tmplist.addAll(list.subList(0, len_t));
				list.subList(0, len_t).clear();
				HSSFSheet sheet = null;
				if(sheetName.equals("new sheet")){
					sheet = wb.createSheet(sheetName+k);
				}else{
					sheet = wb.createSheet(sheetName);
				}
				HSSFRow row = null;
				HSSFCell cell = null;
				
				
				if(headName!=null){
					HSSFRow row0 = null;				
					HSSFFont headFont = wb.createFont();//创建字体
					headFont.setFontHeightInPoints((short) 22);//设置字体大小
					HSSFCellStyle centerstyle = wb.createCellStyle();//创建一个样式
					centerstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中
					centerstyle.setFont(headFont);//加入字体
					row0 = sheet.createRow(0);//创建第一行
					HSSFCell firstColumn = row0.createCell(0);//创建第一列
					firstColumn.setCellValue(headName);//第一列名称
					firstColumn.setCellStyle(centerstyle);
					row0.setHeight((short)500);//第一行设置行高
					sheet.addMergedRegion(new CellRangeAddress(0,0,0,headers.length));			
					if (hasHeaders) {
						row = sheet.createRow(1);
						if (headerRow <= 0) {
							headerRow = HEADER_ROW;
						}
						headerRow = Math.min(headerRow, MAX_ROWS);
						for (int h = 0, lenH = headers.length; h < lenH; h++) {
							Region region = new Region(0, (short) h, (short) headerRow - 1, (short) h);// 合并从第rowFrom行columnFrom列
							sheet.addMergedRegion(region);// 到rowTo行columnTo的区域
							// 得到所有区域
							sheet.getNumMergedRegions();
							if(mycellWidth != null && !mycellWidth.isEmpty()){
								if(mycellWidth.containsKey(h)){
									sheet.setColumnWidth(h, (int)mycellWidth.get(h));
								}else{
									sheet.setColumnWidth(h, cellWidth);
								}
							}else{
								sheet.setColumnWidth(h, cellWidth);
							}
							
							cell = row.createCell(h);
							cell.setCellValue(headers[h]);
						}
					}
				}else{
					
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
							
							if(mycellWidth != null && !mycellWidth.isEmpty()){
								if(mycellWidth.containsKey(h)){
									sheet.setColumnWidth(h, (int)mycellWidth.get(h));
								}else{
									sheet.setColumnWidth(h, cellWidth);
								}
							}else{
								setCellWidth(sheet, cellWidth, columnNum);
							}
							
							cell = row.createCell(h);
							cell.setCellValue(headers[h]);
						}
					}else{
						if(mycellWidth != null && !mycellWidth.isEmpty()){
							for (int i = 0; i < columnNum; i++) {
								
								if(mycellWidth.containsKey(i)){
									sheet.setColumnWidth(i, (int)mycellWidth.get(i));
								}else{
									sheet.setColumnWidth(i, cellWidth);
								}
							}
						}else{
							setCellWidth(sheet, cellWidth, columnNum);
						}
					}
				}

				if (tmplist == null || tmplist.size() == 0) {
					return wb;
				}
				//改造目标自动合并指定列相邻的相同的值
				//自定义合并列
				//将数组转为 map
				if(hasMerged){
					for (int i = 0; i < mergedColumns.length; i++) {
						maps.put(mergedColumns[i], null);
					}
				}
				if(hasMergedHead){
					for (int i = 0; i < headMergedColumns.length; i++) {
						headMergedMaps.put(headMergedColumns[i], null);
					}
				}
				
				mergedList = new ArrayList<Region>();
				
				int t = 0;
				boolean isHear = false;
				Region reg = null;
				for (int i = 0, len = tmplist.size(); i < len; i++) {
					if(i>1){
						t = i-1;
					}
					if(headName!=null){
						row = sheet.createRow(i + headerRow + 1);	
					}else{
					row = sheet.createRow(i + headerRow);
					}
					Object obj = tmplist.get(i);
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
					
					if(hasMerged){
						if (columns.length == 0) {// 未设置显示列，默认不合并
							
						}
						//判断分组是否是同一组
						if(hasMergedHead){
							for (int j = 0, lenJ = columns.length; j < lenJ; j++) {
								if(headMergedMaps.containsKey(columns[j])){
									if(i>0&&tmplist.get(t).get(columns[j]) != null&&tmplist.get(i).get(columns[j]) != null&&tmplist.get(t).get(columns[j]).equals(tmplist.get(i).get(columns[j]))){
										isHear = true;
									}else{
										isHear = false;
										break;
									}
								}
							}
						}else{
							isHear = true;
						}
						for (int j = 0, lenJ = columns.length; j < lenJ; j++) {
							if(maps.containsKey(columns[j])){
								if(isHear){
									if(i>0&&tmplist.get(t).get(columns[j]) != null&&tmplist.get(i).get(columns[j]) != null&&tmplist.get(t).get(columns[j]).equals(tmplist.get(i).get(columns[j]))){
										if(maps.get(columns[j]) == null){
											reg = new Region(t+ headerRow,(short)j,i+ headerRow,(short)j);// 合并从第rowFrom行columnFrom列
											maps.put(columns[j],reg);
										}else{
											maps.get(columns[j]).setRowTo(i+ headerRow);
										}
										if(i+1==len){
											if(maps.get(columns[j]) != null){
												mergedList.add(maps.get(columns[j]));
											}
											maps.put(columns[j],null);
										}
									}else{
										if(maps.get(columns[j]) != null){
											mergedList.add(maps.get(columns[j]));
										}
										maps.put(columns[j],null);
									}
								}else{
									if(maps.get(columns[j]) != null){
										mergedList.add(maps.get(columns[j]));
									}
									maps.put(columns[j],null);
								}
											
							}
						}
						
					}
					
				}
				if(mergedList.size()>0){
					for (Region region : mergedList) {
						sheet.addMergedRegion(region);// 到rowTo行columnTo的区域
					}
				}
				
				if(mergecells.size()>0){
					for (CellRangeAddress region : mergecells) {
						sheet.addMergedRegion(region);// 到rowTo行columnTo的区域
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
	public static HSSFWorkbook export(String headName ,String sheetName, int headerRow, String[] headers, String[] columns, List<Record> list, int cellWidth,Map<Integer,Integer> mycellWidth) {
		return export(null, headName, sheetName, headerRow, headers, columns, list, cellWidth,mycellWidth,new ArrayList<CellRangeAddress>());

	}
	
	
	/**
	 * 
	 * @param wb execl工作簿
	 * @param headName 头名称
	 * @param sheetName sheet 名称
	 * @param headerRow 头行
	 * @param headers 头数据
	 * @param columns 列头数据
	 * @param list 数据集
	 * @param cellWidth 列宽
	 * @return
	 */
	public static HSSFWorkbook export(HSSFWorkbook wb,String headName ,String sheetName, int headerRow, String[] headers, String[] columns, List<Record> list, int cellWidth,Map<Integer,Integer> mycellWidth,List<CellRangeAddress> mergecells) {
		boolean hasHeaders = false,isCustomMerge = false;
		int columnNum = 0;
		int sheetNum = 0;
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
		if(list != null && list.size()>0){
			sheetNum = list.size()%MAX_ROWS==0?list.size()/MAX_ROWS:(list.size()/MAX_ROWS)+1;
		}
		if(mergecells == null){
			isCustomMerge = true;
			mergecells = new ArrayList<CellRangeAddress>();
		}
		
		return export(wb,headName, sheetName, cellWidth, headerRow, headers, columns,new String[] {},new String[] {}, list, columnNum,false,false,hasHeaders,sheetNum,mycellWidth,isCustomMerge,mergecells);

	}
	
	/**
	 * 多数据集模式存储数据 20180316 cxy 
	 * 此处可能会出现异常请注意
	 * @param param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HSSFWorkbook export(List<Map<String,Object>> param) {
		
		//默认的数据传入对象
		String headName = null ;
		String sheetName = null; 
		int headerRow = 0; 
		String[] headers = null; 
		String[] columns = null; 
		List<Record> list = null; 
		Map<Integer,Integer> mycellWidth =null;
		int cellWidth = 0;
		int countparam = 0;
		List<CellRangeAddress>  mergecells = new ArrayList<CellRangeAddress>();
		
		HSSFWorkbook wb = new HSSFWorkbook();//因多SHEET页的话，workbook 对象需为同一个，所以提出到外部
		
		//根据数据集循环便利数据集进行数据组合
		if(param!=null && param.size()>0){
			for (Map<String,Object> map : param) {
				
				//此处是非必传参数项
				if(map.containsKey("headName")){
					headName = (String) map.get("headName");
				}
				if(map.containsKey("headerRow")){
					headerRow = (Integer) map.get("headerRow");
				}
				if(map.containsKey("mycellWidth")){
					mycellWidth = (Map<Integer, Integer>) map.get("mycellWidth");
				}
				if(map.containsKey("cellWidth")){
					cellWidth = (Integer) map.get("cellWidth");
				}else{
					cellWidth = 0;
				}
				
				//此处是必输项
				if(map.containsKey("headers")){
					headers = (String[]) map.get("headers");
				}else{
					countparam++;
				}
				if(map.containsKey("sheetName")){
					sheetName = (String) map.get("sheetName");
				}else{
					countparam++;
				}
				if(map.containsKey("columns")){
					columns =  (String[]) map.get("columns");
				}else{
					countparam++;
				}
				if(map.containsKey("list")){
					list = (List<Record>) map.get("list");
				}else{
					countparam++;
				}
				
				if(map.containsKey("mergecells")){
					mergecells = (List<CellRangeAddress>) map.get("mergecells");
				}else{
					//countparam++;
				}
				
				/**
				 * 如果出现参数不全的跳过循环
				 */
				
				if(countparam>0){
					countparam = 0;
					continue;
				}
				//因业务无需求，不需要使用单元格合并功能
				export(wb, headName, sheetName, headerRow, headers, columns, list, cellWidth,mycellWidth,mergecells);
			}
		}
		return wb;

	}
	
	
/*	*//**
	 * @param String
	 *            sheetName sheet名称
	 * @param int headerRow 设置头列占的行数
	 * @param String
	 *            [] headers 头列值
	 * @param String
	 *            [] columns 列key(即 list<Map<String ,Ojbect>> 中 map的key)
	 * @param String 
	 *            [] mergedColumns 列key(即 list<Map<String ,Ojbect>> 中 map的key) 设置自动合并相邻的单元格
	 * @param List
	 *            <Map<String, Object>> list 数据
	 * @param int cellWidth 设置单元格宽度
	 * @return
	 *//*
	public static HSSFWorkbook export(String headName,String sheetName, int headerRow, String[] headers, String[] columns,String [] mergedColumns, List<Record> list, int cellWidth) {
		boolean hasHeaders = false;
		boolean hasMerged = true;
		int columnNum = 0;
		int sheetNum = 0;
		if (headers != null && headers.length > 0) {
			hasHeaders = true;
			columnNum = headers.length;
		}
		if (columns == null) {
			columns = new String[] {};
		}
		if(mergedColumns == null){
			mergedColumns = new String[] {};
		}
		if(mergedColumns.length == 0){
			hasMerged = false;
		}
		columnNum = Math.max(columnNum, columns.length);
		if (cellWidth <= 0) {
			cellWidth = DEFAULT_CELL_WIDTH;
		}
		if(list != null && list.size()>0){
			sheetNum = list.size()%MAX_ROWS==0?list.size()/MAX_ROWS:(list.size()/MAX_ROWS)+1;
		}
		
		return export(null,headName,sheetName, cellWidth, headerRow, headers, columns,mergedColumns,new String[] {}, list, columnNum,false,hasMerged, hasHeaders,sheetNum,null);

	}*/
	
	/**
	 * @param String
	 *            sheetName sheet名称
	 * @param int headerRow 设置头列占的行数
	 * @param String
	 *            [] headers 头列值
	 * @param String
	 *            [] columns 列key(即 list<Map<String ,Ojbect>> 中 map的key)
	 * @param String 
	 *            [] mergedColumns 列key(即 list<Map<String ,Ojbect>> 中 map的key) 设置自动合并相邻的单元格
	 * @param List
	 *            <Map<String, Object>> list 数据
	 * @param int cellWidth 设置单元格宽度
	 * @return
	 */
	public static HSSFWorkbook export(String headName,String sheetName, int headerRow, String[] headers, String[] columns,String [] mergedColumns,String [] headMergedColumns, List<Record> list, int cellWidth,Map<Integer,Integer> mycellWidth,List<CellRangeAddress>  mergecells) {
		boolean hasHeaders = false;
		boolean hasMerged = true;
		boolean hasMergedHead = true;
		boolean isCustomMerge = false;
		int columnNum = 0;
		int sheetNum = 1;
		if (headers != null && headers.length > 0) {
			hasHeaders = true;
			columnNum = headers.length;
		}
		if (columns == null) {
			columns = new String[] {};
		}
		if(mergedColumns == null){
			mergedColumns = new String[] {};
		}
		if(mergedColumns.length == 0){
			hasMerged = false;
		}
		if(headMergedColumns == null){
			headMergedColumns = new String[] {};
		}
		if(headMergedColumns.length == 0){
			hasMergedHead = false;
		}
		columnNum = Math.max(columnNum, columns.length);
		if (cellWidth <= 0) {
			cellWidth = DEFAULT_CELL_WIDTH;
		}
		if(list != null && list.size()>0){
			sheetNum = list.size()%MAX_ROWS==0?list.size()/MAX_ROWS:(list.size()/MAX_ROWS)+1;
		}
		if(mergecells == null){
			isCustomMerge = true;
			mergecells = new ArrayList<CellRangeAddress>();
		}
		
		return export(null,headName, sheetName, cellWidth, headerRow, headers, columns,mergedColumns,headMergedColumns, list, columnNum,hasMergedHead,hasMerged, hasHeaders,sheetNum,mycellWidth,isCustomMerge, mergecells);

	}
	
	/**
	 * 
	 * @param headers
	 *            列key(即 list<Map<String ,Ojbect>> 中 map的key)
	 * @param columns 表格标题列值
	 * @param columnsnum 标题合并单元格信息
	 * @param list
	 *            数据
	 * @return
	 */

	@SuppressWarnings({ "deprecation"})
	private static HSSFWorkbook export(String[] headers, String[][] columns,
			String[][] columnsnum, List<Record> list, int columnNum, boolean hasMergedHead,
			boolean hasMerged, boolean hasHeaders, int sheetNum) {
		
		HSSFWorkbook wb = null;
		int len_t;
		List<Record> tmplist = new ArrayList<Record>();

		try {
			wb = new HSSFWorkbook();
			
			/**
			 * 设置单元格样式
			 */
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);	//垂直居中
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);		//水平居中
			
			if(sheetNum == 0) {
				HSSFSheet sheet = wb.createSheet();
				return wb;
			}
			
			for (int k = 0; k < sheetNum; k++) {
				tmplist.clear();
				len_t = list.size();
				if (len_t > MAX_ROWS) {
					len_t = MAX_ROWS;
				}
				tmplist.addAll(list.subList(0, len_t));
				list.subList(0, len_t).clear();
				HSSFSheet sheet = wb.createSheet();
				
				//默认设置前20列的列宽
				for(int i=0; i<20;i++) {
					sheet.setColumnWidth(i, DEFAULT_CELL_WIDTH);
				}
				
				HSSFRow row = null;
				HSSFCell cell = null;
				
				//遍历标题数组，并创建标题行信息
				for(int i=0; i<columns.length; i++) {
					row = sheet.createRow(i);
					for(int j=0; j<columns[i].length; j++) {
						cell = row.createCell(j);
						cell.setCellValue(columns[i][j]);
						cell.setCellStyle(cellStyle);
					}
				}
				
				//遍历标题列合并信息数组，将指定的单元格进行合并
				for(int i=0; i<columnsnum.length; i++) {
					for(int j=0; j<columnsnum[i].length; j++) {
						String[] tmp = columnsnum[i][j].split(",");
						Integer startRow = Integer.parseInt(tmp[0]);
						Integer endRow = Integer.parseInt(tmp[1]);
						Integer startCol = Integer.parseInt(tmp[2]);
						Integer endCol = Integer.parseInt(tmp[3]);
						sheet.addMergedRegion(new CellRangeAddress(startRow,endRow,startCol,endCol));
					}
				}
				
				int count=0;
				
				//遍历数据列表，将数据插入表格
				for(Record record :  tmplist) {
					row = sheet.createRow(columns.length+count);
					Map<String, Object> map = record.getColumns();
					for (int j = 0, lenJ = headers.length; j < lenJ; j++) {
						cell = row.createCell(j);
						if (map.get(headers[j]) == null) {
							cell.setCellValue(" ");
						} else {
							cell.setCellValue(map.get(headers[j]) + "");
						}
						cell.setCellStyle(cellStyle);
					}
					count++;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return wb;
	}
	
	/**
	 * 
	 * @param headers
	 *            列key(即 list<Map<String ,Ojbect>> 中 map的key)
	 * @param columns 表格标题列值
	 * @param columnsnum 标题合并单元格信息
	 * @param list
	 *            数据
	 * @return
	 */
	public static HSSFWorkbook export(String[] headers, String[][] columns,
			String[][] columnsnum,
			List<Record> list, int cellWidth) {
		boolean hasHeaders = false;
		boolean hasMerged = true;
		boolean hasMergedHead = true;
		int columnNum = 0;
		int sheetNum = 0;
		if (headers != null && headers.length > 0) {
			hasHeaders = true;
			columnNum = headers.length;
		}
		if (columns == null) {
			columns = new String[][] {};
		}
		columnNum = Math.max(columnNum, columns.length);
		if (cellWidth <= 0) {
			cellWidth = DEFAULT_CELL_WIDTH;
		}
		if (list != null && list.size() > 0) {
			sheetNum = list.size() % MAX_ROWS == 0 ? list.size() / MAX_ROWS
					: (list.size() / MAX_ROWS) + 1;
		}

		return export(headers,columns, columnsnum, list, columnNum,
				hasMergedHead, hasMerged, hasHeaders, sheetNum);

	}
}
