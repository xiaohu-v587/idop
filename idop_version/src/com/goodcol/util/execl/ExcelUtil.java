package com.goodcol.util.execl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;

import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.execl.ExcelUtil.XSSFDateUtil;
import com.goodcol.util.ext.render.excel.PoiModel;
import com.goodcol.util.jfinal.render.excel.ExcelColumn;

/**
 * Excel工具�?
 * @author cxy
 *
 */
public class ExcelUtil {
	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";
	
	public static final String EMPTY = "";
	public static final String POINT = ".";
	public static SimpleDateFormat sdf =   new SimpleDateFormat("yyyy/MM/dd");
	
	//工作簿对象
	private static HSSFWorkbook wb= new HSSFWorkbook();
	//工作表对象
	private static Sheet sheet=wb.createSheet();
	//列对应的字段名
	private static List<String> fields=new ArrayList<String>();
	
	/**
	 * 获得path的后�?��
	 * @param path
	 * @return
	 */
	public static String getPostfix(String path){
		if(path==null || EMPTY.equals(path.trim())){
			return EMPTY;
		}
		if(path.contains(POINT)){
			return path.substring(path.lastIndexOf(POINT)+1,path.length());
		}
		return EMPTY;
	}
	/**
	 * 单元格格�?
	 * @param hssfCell
	 * @return
	 */
	@SuppressWarnings({ "static-access", "deprecation" })
	public static String getHValue(HSSFCell hssfCell){
		 if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			 return String.valueOf(hssfCell.getBooleanCellValue());
		 } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			 String cellValue = "";
			 if(HSSFDateUtil.isCellDateFormatted(hssfCell)){				
				 Date date = HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue());
				 cellValue = sdf.format(date);
			 }else{
				 DecimalFormat df = new DecimalFormat("#.##");
				 cellValue = df.format(hssfCell.getNumericCellValue());
				 String strArr = cellValue.substring(cellValue.lastIndexOf(POINT)+1,cellValue.length());
				 if(strArr.equals("00")){
					 cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
				 }	
			 }
			 return cellValue;
		 } else {
			return String.valueOf(hssfCell.getStringCellValue());
		 }
	}
	/**
	 * 单元格格�?
	 * @param xssfCell
	 * @return
	 */
	public static String getXValue(XSSFCell xssfCell){
		 if (xssfCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			 return String.valueOf(xssfCell.getBooleanCellValue());
		 } else if (xssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			 String cellValue = "";
			 if(XSSFDateUtil.isCellDateFormatted(xssfCell)){
				 Date date = XSSFDateUtil.getJavaDate(xssfCell.getNumericCellValue());
				 cellValue = sdf.format(date);
			 }else{
				 DecimalFormat df = new DecimalFormat("#.##");
				 cellValue = df.format(xssfCell.getNumericCellValue());
				 String strArr = cellValue.substring(cellValue.lastIndexOf(POINT)+1,cellValue.length());
				 if(strArr.equals("00")){
					 cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
				 }	
			 }
			 return cellValue;
		 } else {
			return String.valueOf(xssfCell.getStringCellValue());
		 }
	}	
/**
 * 自定义xssf日期工具�?
 * @author lp
 *
 */
static class XSSFDateUtil extends DateUtil{
	protected static int absoluteDay(Calendar cal, boolean use1904windowing) {  
        return DateUtil.absoluteDay(cal, use1904windowing);  
    } 
}

	/**
	 * 获取工作对象 
	 * */
	public static HSSFWorkbook getWorkBook(List<ExcelColumn> columns,List<Record> data){
		inint();
		//写标题
		writeTitle(columns,0,null);
		//合并列
		mergeColumns();
		//合并行
		mergeRows();
		//写入行分隔符
		writeDelimiter();
		//冻结表头
		freezeTitle();
		//写数据
		writeData(data);
		return wb;
	}
	
	
	
/*
 * 初始化对象 防止多次调用发生错误
 * 
 * */
	private static void inint(){
		wb=new HSSFWorkbook();
		sheet=wb.createSheet();
		fields=new ArrayList<String>();
	}
	
	//获取子节点的个数
		private static int getSubNodesCount(List<ExcelColumn> list){
			int count=0;
			if(list!=null&&list.size()>0){
				//有子节点
				count=list.size();
				Iterator<ExcelColumn>it=list.iterator();
				while (it.hasNext()) {
					int a=getSubNodesCount(it.next().getChildren());
					count += a>0 ? a-1 :a;
				}
			}
			return count;
		}

//写入标题 列
	private static void writeTitle(List<ExcelColumn> columns,int rowIndex,String title) {
//		JSONArray array= new JSONArray();
//		array = array.fromObject(columns);
		HSSFCellStyle style=wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		Iterator<ExcelColumn> it = columns.iterator();
		Row row=sheet.getRow(rowIndex)==null ?sheet.createRow(rowIndex) :sheet.getRow(rowIndex);
		Row lastRow= rowIndex==0? null:sheet.getRow(rowIndex-1);
		int colIndex=-1;
		//遍历当前行 获取行最多的单元格 的个数  因为如果之前有单元格是空 对列索引造成问题
		int currentMaxColumns=0;
		for (int i = 0; i < rowIndex; i++) {
			Row rr=sheet.getRow(i);
			if(rr.getPhysicalNumberOfCells()>currentMaxColumns){
				currentMaxColumns=rr.getPhysicalNumberOfCells();
			}
		}
		//查找上一级列的位置
		if(lastRow!=null&&AppUtils.StringUtil(title)!=null){
			for (int i = 0; i < currentMaxColumns; i++) {
				if(lastRow.getCell(i)!=null&&AppUtils.StringUtil(lastRow.getCell(i).getStringCellValue())!=null&&lastRow.getCell(i).getStringCellValue().equals(title)){
					colIndex=i;
					break;
				}
			}
		}
		colIndex=colIndex==-1?0:colIndex;
		Cell cell;
		int count=0;
		while (it.hasNext()) {
		   ExcelColumn ec = it.next();
			   count=getSubNodesCount(ec.getChildren());
			   boolean isLeaf=false;
			  if(count==0){ //没有子节点 就是最终的叶子节点
				  count=1;
				  isLeaf=true;
			  }else{
				  isLeaf=false;
			  }
			  
			  for (int i = 0; i < count; i++) {
				cell=row.createCell(colIndex++);
				if(isLeaf){
					if(ec.getWidth()!=0){
						//把对应的field 放进List
						fields.add(ec.getField());
						sheet.setColumnWidth(colIndex-1,ec.getWidth());
					}
				}
				cell.setCellStyle(style);
				cell.setCellValue(ec.getTitle());
				if(i==count-1){
					//最后一个父节点 如果有子节点 递归写入子节点
					if(ec.getChildren()!=null&&ec.getChildren().size()>0){
						writeTitle(ec.getChildren(),rowIndex+1,ec.getTitle());
					}
				}
			}
		}
	}
	
	//合并列
		private static void mergeColumns(){
			//行数
			int rowsCount=sheet.getPhysicalNumberOfRows();
			//列数
			int colsCount=sheet.getRow(0).getPhysicalNumberOfCells();
			Row row=null;
			Cell cell1=null;
			Cell cell2=null;
			int colSpan=0;
			for (int i = 0; i < rowsCount; i++) {
				row=sheet.getRow(i);
				//重置
				colSpan=0;
				row=sheet.getRow(i);
				for (int j = 0; j <colsCount; j++) {
					cell1=row.getCell(j);
					cell2=row.getCell(j+1);
					
					if(cell1==null){//如果当前单元格为空 跳过 查找下一个单元格
						if(j==colsCount-1){
							break;
						}else{
							continue;
						}
					}
					if(cell2==null){//说明当前已经到最后一个单元格
						if(colSpan>=1){//大于一 合并
							sheet.addMergedRegion(new CellRangeAddress(i, i,j-colSpan,j));
							break;
						}
					}
					if(cell1!=null &&cell2!=null){
						//如果内容相同
						if(cell1.getStringCellValue().equals(cell2.getStringCellValue())){
							colSpan++;
						}else{
							if(colSpan>=1){
								sheet.addMergedRegion(new CellRangeAddress(i,i,j-colSpan,j));
								colSpan=0;
								continue;
							}
						}
					}
				}
			}
		}
		
		//合并行
		private static void mergeRows(){
			//行数
			int rowsCount=sheet.getPhysicalNumberOfRows();
			//列数
			int colsCount=sheet.getRow(0).getPhysicalNumberOfCells();
			Row row=null;
			Cell cell=null;
			int rowSpan=0;
			for (int i = 0; i < colsCount; i++) {
				rowSpan=0;
				for (int j = rowsCount-1; j >-1; j--) {
					row=sheet.getRow(j);
					cell=row.getCell(i);
					if(cell!=null&&j==rowsCount-1){
						break;
					}else if(cell!=null&&j!=rowsCount-1){
						//和并列
						sheet.addMergedRegion(new CellRangeAddress(rowsCount-rowSpan-1,rowsCount-1,i,i));
						break;
					}else{
						//行合并数+1
						rowSpan++;
					}
				}
			}
		}
		
		//行分隔
		private static void writeDelimiter() {
			//行数
			int rowsCount=sheet.getPhysicalNumberOfRows();
			//列数
			int colsCount=sheet.getRow(0).getPhysicalNumberOfCells();
			Cell cell;
			CellStyle cs= wb.createCellStyle();
			Row row=sheet.createRow(rowsCount);
			for (int i = 0; i < colsCount; i++) {
				cell=row.createCell(i);
				cs.setBorderTop(HSSFCellStyle.BORDER_MEDIUM_DASHED);
				cell.setCellStyle(cs);
			}
			
		}
		/*
		 * 冻结表头
		 * */
		public static void freezeTitle(){
			int rowsCount=sheet.getPhysicalNumberOfRows();
			sheet.createFreezePane(0, rowsCount-1);
		}
		
		@SuppressWarnings("static-access")
		private static void writeData(List<Record>data){
			Boolean isNum=false;//是否为数值型
			Boolean isInteger=false;//是否为整数
			Boolean isPercent=false;//是否为百分数
			//行数
			int rowsCount=sheet.getPhysicalNumberOfRows();
			//列数
			int colsCount=sheet.getRow(0).getPhysicalNumberOfCells();
			Row row;
			Cell cell;
			Record record= null;
			CellStyle cs=wb.createCellStyle();
//			cs.setBorderTop(HSSFCellStyle.BORDER_MEDIUM_DASHED);
//			cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
//			cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
			
			CellStyle ct=wb.createCellStyle();
//			ct.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM_DASHED);
//			ct.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
//			ct.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
			if(data!=null &&data.size()>0){
			for (int i = 0; i < data.size(); i++) {
				row=sheet.createRow(i+rowsCount-1);
					record=data.get(i);
				for (int j = 0; j < colsCount; j++) {
					cell=row.createCell(j);
					String value=record.get(fields.get(j))==null ? "": record.get(fields.get(j)).toString();
					if(AppUtils.StringUtil(value)==null){
						cell.setCellValue("");
					}else{
					//判断是否为数值型
					isNum=value.matches("^(-?\\d+)(\\.\\d+)?$");
					//是否为整数
					isInteger=value.matches("^[-\\+]?[\\d]*$");
					//是否为百分数
					isPercent=value.contains("%");
					if(isNum&&!isPercent){
						HSSFDataFormat df= wb.createDataFormat();
						if(isInteger){
							cs.setDataFormat(df.getBuiltinFormat("#,#0"));
							ct.setDataFormat(df.getBuiltinFormat("#,#0"));
						}else{
							cs.setDataFormat(df.getBuiltinFormat("#,##0.00"));//小数保留两位小数
							ct.setDataFormat(df.getBuiltinFormat("#,##0.00"));//小数保留两位小数
						}
						cell.setCellValue(Double.parseDouble(value));
					}else{
						cell.setCellValue(value);
					}
					if(j==0){
						cell.setCellStyle(cs);
					}
					if(j==data.size()-1){
						cell.setCellStyle(ct);
					}
				}
					}
			}
		 }
		}
		
		/**
		 * 获取工作对象 
		 * */
		public static HSSFWorkbook getWorkBookData(List<ExcelColumn> columns,List<Record> data,String[] title,Map<String,List<Map<String,String>>> maps,int[] mergeIdex ){
			inint();
			/*	//写标题
			writeTitle(columns,0,null);
			//合并列
			mergeColumns();
			//合并行
			mergeRows();
			//写入行分隔符
			writeDelimiter();
			//冻结表头
			freezeTitle();
			//写数据
			writeData(data);
		*/
			//数据合并 BY  20180508
			
			
			
			return mergeRowsData(title,maps,mergeIdex);
			
		}
		
		/**
		 * @function 合并行--数据
		 * @param title excel的列
		 * @param maps 所有sheet保存在外层Map中，每个sheet的数据保存在List中，每行保存在内层Map中
		 * @param mergeIdex 需要合并的列的索引
		 */
		private static HSSFWorkbook mergeRowsData(String[] title,Map<String,List<Map<String,String>>> maps,int[] mergeIndex ){
			  if (title.length==0){  
		            return null;  
		        }  
		        /*初始化excel模板*/  
		        //Workbook workbook = new XSSFWorkbook(); 
		       
			   HSSFWorkbook workbook=new HSSFWorkbook();
		        Sheet sheet_n = null;  
		        int n = 0;  
		        /*循环sheet页*/  
		        for(Map.Entry<String, List<Map<String/*对应title的值*/, String>>> entry : maps.entrySet()){  
		            /*实例化sheet对象并且设置sheet名称，book对象*/  
		        	try {  
		        		sheet_n = workbook.createSheet();  
		                workbook.setSheetName(n, entry.getKey());  
		                workbook.setSelectedTab(0);  
		            }catch (Exception e){  
		                e.printStackTrace();  
		            }  
		            /*初始化head，填值标题行（第一行）*/  
		            Row row0 = sheet_n.createRow(0);  
		            for(int i = 0; i<title.length; i++){  
		                /*创建单元格，指定类型*/  
		                Cell cell_1 = row0.createCell(i, Cell.CELL_TYPE_STRING);  
		                cell_1.setCellValue(title[i]);  
		            }  
		            /*得到当前sheet下的数据集合*/  
		            List<Map<String/*对应title的值*/, String>> list = entry.getValue();  
		            /*遍历该数据集合*/  
		            List<PoiModel> poiModels = new ArrayList();  
		            if(null!=workbook){  
		                Iterator iterator = list.iterator();  
		                int index = 1;/*这里1是从excel的第二行开始，第一行已经塞入标题了*/  
		                while (iterator.hasNext()){  
		                    Row row = sheet_n.createRow(index);  
		                    /*取得当前这行的map，该map中以key，value的形式存着这一行值*/  
		                    Map<String, String> map = (Map<String, String>)iterator.next();  
		                    /*循环列数，给当前行塞值*/  
		                    for(int i = 0; i<title.length; i++){  
		                        String old = "";  
		                        /*old存的是上一行统一位置的单元的值，第一行是最上一行了，所以从第二行开始记*/  
		                        if(index > 1){  
		                            old = poiModels.get(i)==null?"":poiModels.get(i).getContent();  
		                        }  
		                        /*循环需要合并的列*/  
		                        for(int j = 0; j < mergeIndex.length; j++){  
		                            if(index == 1){  
		                                /*记录第一行的开始行和开始列*/  
		                                PoiModel poiModel = new PoiModel();  
		                                poiModel.setOldContent(map.get(title[i]));  
		                                poiModel.setContent(map.get(title[i]));  
		                                poiModel.setRowIndex(1);  
		                                poiModel.setCellIndex(i);  
		                                poiModels.add(poiModel);  
		                                break;  
		                            }else if(i > 0 && mergeIndex[j] == i){/*这边i>0也是因为第一列已经是最前一列了，只能从第二列开始*/  
		                                /*当前同一列的内容与上一行同一列不同时，把那以上的合并, 或者在当前元素一样的情况下，前一列的元素并不一样，这种情况也合并*/  
		                                /*如果不需要考虑当前行与上一行内容相同，但是它们的前一列内容不一样则不合并的情况，把下面条件中||poiModels.get(i).getContent().equals(map.get(title[i])) && !poiModels.get(i - 1).getOldContent().equals(map.get(title[i-1]))去掉就行*/  
		                                if(!poiModels.get(i).getContent().equals(map.get(title[i])) || poiModels.get(i).getContent().equals(map.get(title[i])) && !poiModels.get(i - 1).getOldContent().equals(map.get(title[i-1]))){  
		                                    /*当前行的当前列与上一行的当前列的内容不一致时，则把当前行以上的合并*/  
		                                    CellRangeAddress cra=new CellRangeAddress(poiModels.get(i).getRowIndex()/*从第二行开始*/, index - 1/*到第几行*/, poiModels.get(i).getCellIndex()/*从某一列开始*/, poiModels.get(i).getCellIndex()/*到第几列*/);  
		                                    //在sheet里增加合并单元格  
		                                    sheet_n.addMergedRegion(cra);  
		                                    /*重新记录该列的内容为当前内容，行标记改为当前行标记，列标记则为当前列*/  
		                                    poiModels.get(i).setContent(map.get(title[i]));  
		                                    poiModels.get(i).setRowIndex(index);  
		                                    poiModels.get(i).setCellIndex(i);  
		                                }  
		                            }  
		                            /*处理第一列的情况*/  
		                            if(mergeIndex[j] == i && i == 0 && !poiModels.get(i).getContent().equals(map.get(title[i]))){  
		                                /*当前行的当前列与上一行的当前列的内容不一致时，则把当前行以上的合并*/  
		                                CellRangeAddress cra=new CellRangeAddress(poiModels.get(i).getRowIndex()/*从第二行开始*/, index - 1/*到第几行*/, poiModels.get(i).getCellIndex()/*从某一列开始*/, poiModels.get(i).getCellIndex()/*到第几列*/);  
		                                //在sheet里增加合并单元格  
		                                sheet_n.addMergedRegion(cra);  
		                                /*重新记录该列的内容为当前内容，行标记改为当前行标记*/  
		                                poiModels.get(i).setContent(map.get(title[i]));  
		                                poiModels.get(i).setRowIndex(index);  
		                                poiModels.get(i).setCellIndex(i);  
		                            }  
		  
		                            /*最后一行没有后续的行与之比较，所有当到最后一行时则直接合并对应列的相同内容*/  
		                            if(mergeIndex[j] == i && index == list.size()){  
		                                CellRangeAddress cra=new CellRangeAddress(poiModels.get(i).getRowIndex()/*从第二行开始*/, index/*到第几行*/, poiModels.get(i).getCellIndex()/*从某一列开始*/, poiModels.get(i).getCellIndex()/*到第几列*/);  
		                                //在sheet里增加合并单元格  
		                                sheet_n.addMergedRegion(cra);  
		                            }  
		                        }  
		                        Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);  
		                        cell.setCellValue(map.get(title[i]));  
		                        /*在每一个单元格处理完成后，把这个单元格内容设置为old内容*/  
		                        poiModels.get(i).setOldContent(old);  
		                    }  
		                    index++;  
		                }  
		            }  
		            n++; 
		        } 
		        //System.out.println("该文档sheet数量："+n); 
		        
		        /*生成临时文件*/  
		     /*   FileOutputStream out = null;  
		        String localPath = null;  
		        File tempFile = null;  
		        String fileName = String.valueOf(new Date().getTime()/1000);  
		        try {  
		            tempFile = File.createTempFile(fileName, ".xls");  
		            localPath = tempFile.getAbsolutePath();  
		            System.out.println("*********************************="+localPath);
		            out = new FileOutputStream(localPath);  
		          //  wb.write(out);  
		        }catch (IOException e){  
		            e.printStackTrace();  
		        }finally {  
		            try {  
		                out.flush();  
		                out.close();  
		            }catch (IOException e){  
		                e.printStackTrace();  
		            }  
		        }  
		        */
		        return workbook;  
		}
}