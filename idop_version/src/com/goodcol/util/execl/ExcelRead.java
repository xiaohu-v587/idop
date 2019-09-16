package com.goodcol.util.execl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.goodcol.core.plugin.activerecord.Record;


/**
 * 读取Excel
 * 
 * @author cxy
 *
 */
public class ExcelRead {
	private int inintRow = 0; // sheet中初始行
	private boolean isSetInitRow = false;
	private int totalRows; // sheet中行数
	private int totalCells; // 每一行单元格数
	private boolean isAutoReadSheet = true; // 是否默认读取所有的sheet页
	private boolean isReadSheetList = false; // 是否默认读取所有的sheet页数据格式

	private int maxSheetNum = 1;// 设置默认读取的最大sheet
	private String[] initHeadCells;// 头行的每列按照key进行存放,如果设置该值时， 设置 totalCells 无效
	private Map<String,String> headName = null;
	private boolean isAutoHeads = false; //启用设置英文字段头部设置。例如  英文头关联字段
	private String autoHeadKey = "DATA_COL_"; //启用设置英文字段头部设置。例如  英文头关联字段
	
	
	private String [] heads = null; 
	
	
	public List<String> readExcelTitle(File file) throws Exception {
		if (file == null || ExcelUtil.EMPTY.equals(file.getName().trim())) {
			return null;
		} else {
			String postfix = ExcelUtil.getPostfix(file.getName());
			if (!ExcelUtil.EMPTY.equals(postfix)) {
				if (ExcelUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXlsTitle(file);
				} else if (ExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					return readXlsxTitle(file);
				} else {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * 获取2010版的Excel的表头
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> readXlsTitle(File file) throws Exception {
		List<String> list = new ArrayList<String>();
		// IO流读取文件
		InputStream input = null;
		HSSFWorkbook wb = null;
		String headkey = "";
		try {
			input = new FileInputStream(file);
			// 创建文档
			wb = new HSSFWorkbook(input);
			if (isAutoReadSheet) {
				maxSheetNum = wb.getNumberOfSheets();
			}
			// 读取sheet(页)
			for (int numSheet = 0; numSheet < maxSheetNum; numSheet++) {
				HSSFSheet hssfSheet = wb.getSheetAt(numSheet);

				if (hssfSheet == null) {
					continue;
				}
				// 读取头
				HSSFRow hssfHeader = hssfSheet.getRow(inintRow);
				if (hssfHeader == null) {
					return list;
				}
				// 已启用按照自定义头获取值
				if (this.initHeadCells != null && this.initHeadCells.length > 0) {
					totalCells = initHeadCells.length;
				}

				// 如果未设置默认读取的列数，按照execl中的头列进行设定
				if (totalCells == 0) {
					totalCells = hssfHeader.getLastCellNum();
				}

				// 读取Row,从第二行
				for (int rowNum = inintRow; rowNum < 1; rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow != null) {
						// totalCells = hssfRow.getLastCellNum();
						// 读取列
						for (short c = 0; c < totalCells; c++) {
							headkey = ExcelUtil.getHValue(hssfHeader.getCell(c)).trim();
							list.add(headkey);
						}
					}
				}
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取2003-2007版的Excel的表头
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> readXlsxTitle(File file) throws Exception {
		List<String> list = new ArrayList<String>();
		// IO流读取
		InputStream input = null;
		XSSFWorkbook wb = null;
		String headkey = "";
		try {
			input = new FileInputStream(file);
			// 创建文档
			wb = new XSSFWorkbook(input);
			if (isAutoReadSheet) {
				maxSheetNum = wb.getNumberOfSheets();
			}
			// 读取sheet
			for (int numSheet = 0; numSheet < maxSheetNum; numSheet++) {
				XSSFSheet xssfSheet = wb.getSheetAt(numSheet);

				if (xssfSheet == null) {
					continue;
				}

				// 读取头行
				XSSFRow xssfHeader = xssfSheet.getRow(inintRow);
				if (xssfHeader == null) {
					return list;
				}

				// 如果未设置默认读取的列数，按照execl中的头列进行设定
				if (totalCells == 0) {
					totalCells = xssfHeader.getLastCellNum();
				}

				// 读取Row,从第二行开始
				for (int rowNum = 0; rowNum < 1; rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					if (xssfRow != null) {
						// 读取列，从第一列开始
						for (int c = 0; c < totalCells; c++) {
							headkey = ExcelUtil.getXValue(xssfHeader.getCell(c)).trim();
							list.add(headkey);
						}
					}
				}
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * read the Excel .xlsx,.xls
	 * 
	 * @param file
	 *            jsp中的上传文件
	 * @return
	 * @throws IOException
	 */

	public List<Record> readExcel(File file) throws IOException {
		if (file == null || ExcelUtil.EMPTY.equals(file.getName().trim())) {
			return null;
		} else {
			String postfix = ExcelUtil.getPostfix(file.getName());
			if (!ExcelUtil.EMPTY.equals(postfix)) {
				if (ExcelUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(file);
				} else if (ExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					return readXlsx(file);
				} else {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * read the Excel 2010 .xlsx
	 * 
	 * @param file
	 * @param beanclazz
	 * @param titleExist
	 * @return
	 * @throws IOException
	 */
	public List<Record> readXlsx(File file) {
		List<Record> list = new ArrayList<Record>();
		List<Record> rowList = null;
		// IO流读取
		InputStream input = null;
		XSSFWorkbook wb = null;
		Record sheet = null;
		Record row = null;
		boolean isinit = false;
		String headkey = "";
		setHeads(heads);

		try {
			input = new FileInputStream(file);
			// 创建文档
			wb = new XSSFWorkbook(input);
			if (isAutoReadSheet) {
				maxSheetNum = wb.getNumberOfSheets();
			}
			// 读取sheet
			for (int numSheet = 0; numSheet < maxSheetNum; numSheet++) {
				XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
				if (isReadSheetList) {
					sheet = new Record();
					rowList = new ArrayList<Record>();

				}
				if (xssfSheet == null) {
					continue;
				}

				// 读取头行
				XSSFRow xssfHeader = xssfSheet.getRow(inintRow);
				if (xssfHeader == null) {
					return list;
				}

				// 已启用按照自定义头获取值
				if (this.initHeadCells != null && this.initHeadCells.length > 0) {
					totalCells = initHeadCells.length;
					isinit = true;
				}

				// 如果未设置默认读取的列数，按照execl中的头列进行设定
				if (totalCells == 0) {
					totalCells = xssfHeader.getLastCellNum();
				}
				heads = new String[totalCells];

				totalRows = xssfSheet.getLastRowNum();
				boolean isfir = true;
				int rowNum = isAutoHeads ? inintRow : inintRow + 1;
				
				// 读取Row,从第二行开始
				for (; rowNum < totalRows + 1; rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					if (xssfRow != null) {
						row = new Record();
						// totalCells = xssfRow.getLastCellNum();
						
						// 读取列，从第一列开始
						for (int c = 0; c < totalCells; c++) {
							XSSFCell cell = xssfRow.getCell(c);
							if (isinit) {
								headkey = initHeadCells[c].trim();
							} else if(headName != null){
								////如果头部转换设置不为空，强制按照此进行设置
								headkey = headName.containsKey(headkey)?headName.get(headkey):headkey;
							}else if(isAutoHeads){
								headkey  = autoHeadKey+(c+1);
							}else{
								headkey = ExcelUtil.getXValue(xssfHeader.getCell(c)).trim();
							}
							if(headkey == null || headkey == ""){
								continue;
							}
							
							if(isfir){
								heads[c] = headkey;
							}
							
							if (cell == null) {
								row.set(headkey, ExcelUtil.EMPTY);
								continue;
							}
							row.set(headkey, ExcelUtil.getXValue(cell).trim());
						}
						
						if(isfir){
							isfir = false;
						}
						if (isReadSheetList) {
							rowList.add(row);
						} else {
							list.add(row);
						}
					}
				}
				if (isReadSheetList) {
					sheet.set("key", xssfSheet.getSheetName());
					sheet.set("value", rowList);
					list.add(sheet);
				}
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	/**
	 * read the Excel 2003-2007 .xls
	 * 
	 * @param file
	 * @param beanclazz
	 * @param titleExist
	 * @return
	 * @throws IOException
	 */
	public List<Record> readXls(File file) {
		List<Record> list = new ArrayList<Record>();
		List<Record> rowList = null;
		// IO流读取文�?
		InputStream input = null;
		HSSFWorkbook wb = null;
		Record row = null;
		Record sheet = null;
		boolean isinit = false;
		String headkey = "";
		try {
			input = new FileInputStream(file);
			// 创建文档
			wb = new HSSFWorkbook(input);
			if (isAutoReadSheet) {
				maxSheetNum = wb.getNumberOfSheets();
			}
			// 读取sheet（页）
			for (int numSheet = 0; numSheet < maxSheetNum; numSheet++) {
				HSSFSheet hssfSheet = wb.getSheetAt(numSheet);

				if (isReadSheetList) {
					sheet = new Record();
					rowList = new ArrayList<Record>();

				}
				if (hssfSheet == null) {
					continue;
				}
				// 读取头
				HSSFRow hssfHeader = hssfSheet.getRow(inintRow);
				if (hssfHeader == null) {
					return list;
				}
				// 已启用按照自定义头获取值
				if (this.initHeadCells != null && this.initHeadCells.length > 0) {
					totalCells = initHeadCells.length;
					isinit = true;
				}

				// 如果未设置默认读取的列数，按照execl中的头列进行设定
				if (totalCells == 0) {
					totalCells = hssfHeader.getLastCellNum();
				}

				totalRows = hssfSheet.getLastRowNum();
				int rowNum = isAutoHeads ? inintRow : inintRow + 1;
				// 读取Row,从第二行
				for (; rowNum < totalRows + 1; rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow != null) {
						row = new Record();
						// totalCells = hssfRow.getLastCellNum();
						// 读取列
						for (short c = 0; c < totalCells; c++) {
							HSSFCell cell = hssfRow.getCell(c);
							
							if (isinit) {
								headkey = initHeadCells[c].trim();
							} else if(headName != null){
								////如果头部转换设置不为空，强制按照此进行设置
								headkey = headName.containsKey(headkey)?headName.get(headkey):headkey;
							}else if(isAutoHeads){
								headkey  = autoHeadKey+(c+1);
							}else{
								headkey = ExcelUtil.getHValue(hssfHeader.getCell(c)).trim();
							}
							
							
							if(headkey == null || headkey == ""){
								continue;
							}
							
							if (cell == null) {
								row.set(headkey, ExcelUtil.EMPTY);
								continue;
							}
							row.set(headkey, ExcelUtil.getHValue(cell).trim());
						}
						if (isReadSheetList) {
							rowList.add(row);
						} else {
							list.add(row);
						}

					}
				}
				if (isReadSheetList) {
					sheet.set("key", hssfSheet.getSheetName());
					sheet.set("value", rowList);
					list.add(sheet);
				}
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public List<String[]> readExcelToCol(File file) throws Exception {
		if (file == null || ExcelUtil.EMPTY.equals(file.getName().trim())) {
			return null;
		} else {
			String postfix = ExcelUtil.getPostfix(file.getName());
			if (!ExcelUtil.EMPTY.equals(postfix)) {
				if (ExcelUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXlsToCol(file);
				} else if (ExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					return readXlsxToCol(file);
				} else {
					return null;
				}
			}
		}
		return null;
	}
	
	
	//按照列读取数据反馈list数组
	
	/**
	 * read the Excel 2010 .xlsx
	 * 
	 * @param file
	 * @param beanclazz
	 * @param titleExist
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public List<String []> readXlsToCol(File file) {
		List<String []> list = new ArrayList<String []>();
		// IO流读取文�?
		InputStream input = null;
		HSSFWorkbook wb = null;
		String [] row = null;
		try {
			input = new FileInputStream(file);
			// 创建文档
			wb = new HSSFWorkbook(input);
			// 读取sheet（页）
			for (int numSheet = 0; numSheet < maxSheetNum; numSheet++) {
				HSSFSheet hssfSheet = wb.getSheetAt(numSheet);

				if (hssfSheet == null) {
					continue;
				}
				// 读取头
				HSSFRow hssfHeader = hssfSheet.getRow(inintRow);
				if (hssfHeader == null) {
					return list;
				}

				// 如果未设置默认读取的列数，按照execl中的头列进行设定
				if (totalCells == 0) {
					totalCells = hssfHeader.getLastCellNum();
				}

				totalRows = hssfSheet.getLastRowNum();
				
				// 读取Row,从第二行
				for (int rowNum = inintRow; rowNum < totalRows + 1; rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow != null) {
						row = new String[totalCells];
						// totalCells = hssfRow.getLastCellNum();
						// 读取列
						for (short c = 0; c < totalCells; c++) {
							HSSFCell cell = hssfRow.getCell(c);
							if (cell == null) {
								row[c]= ExcelUtil.EMPTY;
								continue;
							}
							row[c] = ExcelUtil.getHValue(cell).trim();
						}
						list.add(row);

					}
				}
				
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * read the Excel 2010 .xlsx
	 * 
	 * @param file
	 * @param beanclazz
	 * @param titleExist
	 * @return
	 * @throws IOException
	 */
	public List<String []> readXlsxToCol(File file) {
		List<String []> list = new ArrayList<String []>();
		// IO流读取文�?
		InputStream input = null;
		XSSFWorkbook wb = null;
		String [] row = null;
		try {
			input = new FileInputStream(file);
			// 创建文档
			wb = new XSSFWorkbook(input);
			// 读取sheet（页）
			for (int numSheet = 0; numSheet < maxSheetNum; numSheet++) {
				XSSFSheet hssfSheet = wb.getSheetAt(numSheet);

				if (hssfSheet == null) {
					continue;
				}
				// 读取头
				XSSFRow hssfHeader = hssfSheet.getRow(inintRow);
				if (hssfHeader == null) {
					return list;
				}

				// 如果未设置默认读取的列数，按照execl中的头列进行设定
				if (totalCells == 0) {
					totalCells = hssfHeader.getLastCellNum();
				}

				totalRows = hssfSheet.getLastRowNum();
				
				// 读取Row,从第二行
				for (int rowNum = inintRow; rowNum < totalRows + 1; rowNum++) {
					XSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow != null) {
						row = new String[totalCells];
						// totalCells = hssfRow.getLastCellNum();
						// 读取列
						for (short c = 0; c < totalCells; c++) {
							XSSFCell cell = hssfRow.getCell(c);
							if (cell == null) {
								row[c]= ExcelUtil.EMPTY;
								continue;
							}
							row[c] = ExcelUtil.getXValue(cell).trim();
						}
						list.add(row);

					}
				}
				
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	

	public int getMaxSheetNum() {
		return maxSheetNum;
	}

	public void setMaxSheetNum(int maxSheetNum) {
		this.maxSheetNum = maxSheetNum;
	}

	public boolean isAutoReadSheet() {
		return isAutoReadSheet;
	}

	public void setAutoReadSheet(boolean isAutoReadSheet) {
		this.isAutoReadSheet = isAutoReadSheet;
	}

	public String[] getInitHeadCells() {
		return initHeadCells;
	}

	public void setInitHeadCells(String[] initHeadCells) {
		this.initHeadCells = initHeadCells;
	}

	public int getInintRow() {
		return inintRow;
	}

	public int getTotalCells() {
		return totalCells;
	}

	public void setTotalCells(int totalCells) {
		this.totalCells = totalCells;
	}

	public void setInintRow(int inintRow) {
		this.inintRow = inintRow;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public boolean isReadSheetList() {
		return isReadSheetList;
	}

	public void setReadSheetList(boolean isReadSheetList) {
		this.isReadSheetList = isReadSheetList;
	}
	
	public Map<String, String> getHeadName() {
		return headName;
	}

	public void setHeadName(Map<String, String> headName) {
		this.headName = headName;
	}

	public boolean isAutoHeads() {
		return isAutoHeads;
	}

	public void setAutoHeads(boolean isAutoHeads) {
		this.isAutoHeads = isAutoHeads;
	}

	public String[] getHeads() {
		return heads;
	}

	public void setHeads(String[] heads) {
		this.heads = heads;
	}

	public String getAutoHeadKey() {
		return autoHeadKey;
	}

	public void setAutoHeadKey(String autoHeadKey) {
		this.autoHeadKey = autoHeadKey;
	}

}
