package com.anosi.asset.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/***
 * 主要用于读取excel，导出excel大部分情况下使用easypoi
 * 
 * @author jinyao
 *
 */
public class ExcelUtil {

	/**
	 * 读取excel
	 * 
	 * @param file
	 * @param sheetIndex
	 *            sheet的序号，从0开始,如果为-1,代表遍历全部sheet
	 *            
	 * @return 返回Guava的table类型
	 * 
	 * 可以看用例  {@link com.anosi.asset.test.TestExcel#testReadExcel()}  
	 */
	public static Table<Integer, String, Object> readExcel(File file, Integer sheetIndex) throws IOException {
		// 使用Guava的table来存放读取excel的数据
		// 三个泛型分别为行(int)，列(string)，值(object)
		Table<Integer, String, Object> excelTable = HashBasedTable.create();

		Workbook workbook = null;
		// 针对03和07版本的区别
		try {
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} catch (OfficeXmlFileException e) {
			workbook = new XSSFWorkbook(new FileInputStream(file));
		}

		if(sheetIndex==-1){
			//遍历所有sheet
			for (Sheet sheet : workbook) {
				readSheet(sheet, excelTable, workbook);
			}
		}else{
			readSheet(workbook.getSheetAt(sheetIndex), excelTable, workbook);
		}
		return excelTable;
	}

	private static void readSheet(Sheet sheet, Table<Integer, String, Object> excelTable, Workbook workbook) {
		try {
			// 标题行
			Row titles = sheet.getRow(0);
			// 循环每一行
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				// 循环每行的每个单元格
				for (int j = 0; j <= titles.getLastCellNum(); j++) {
					Cell cell = row.getCell(j);
					if (cell != null) {
						Object cellValue = readCellValue(cell);
						excelTable.put(i, String.valueOf(readCellValue(titles.getCell(j))), cellValue);
					}
				}
			} 
		} finally {
			if(workbook!=null){
				try {
					workbook.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	

	/***
	 * 根据cell内容的格式，读取cell的值
	 * 
	 * @param cell
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static Object readCellValue(Cell cell) {
		if (cell.getCellTypeEnum() == CellType.BLANK) {
			return new String();
		} else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
			return cell.getBooleanCellValue();
		} else if (cell.getCellTypeEnum() == CellType.ERROR) {
			return cell.getErrorCellValue();
		} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
			// 日期
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				return DateFormatUtil.getFormateDate(cell.getDateCellValue());
			}
			return cell.getNumericCellValue();
		} else if (cell.getCellTypeEnum() == CellType.STRING) {
			return cell.getStringCellValue();
		} else {
			return cell.getStringCellValue();
		}
	}

}
