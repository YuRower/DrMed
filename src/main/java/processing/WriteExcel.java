package processing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.log4j.Logger;

import application.MainApp;
import application.SchoolCollection;
import model.Person;
import util.Style;

public class WriteExcel {
	private final static Logger LOGGER = Logger.getLogger(WriteExcel.class);

	public void writeToExcel(File excelFilePath) throws IOException {
		Workbook workbook = getWorkbook(excelFilePath);
		for (int i = 0; i < LoadExcel.numOfSheet; i++) {
			Sheet sheet = workbook.createSheet();
			workbook.setSheetName(i, LoadExcel.sheetName.get(i));
			LOGGER.info("number of sheet::" + LoadExcel.sheetName.get(i));

		
		List<Person> list = LoadExcel.outer.get( i);
		LOGGER.info("check returned list " + list + " & acctPrefix=" + SchoolCollection.getPersonData());


		int rownum = 0;
		Cell cell;
		Row row;
		//
		CellStyle style = Style.createStyleForTitle(workbook);

		row = sheet.createRow(rownum);

		// EmpNo
		cell = row.createCell(0, CellType.STRING);
		cell.setCellValue("First Name");
		cell.setCellStyle(style);
		// EmpName
		cell = row.createCell(1, CellType.STRING);
		cell.setCellValue("Last Name");
		cell.setCellStyle(style);
		// Salary
		cell = row.createCell(2, CellType.STRING);
		cell.setCellValue("Street");
		cell.setCellStyle(style);
		// Grade
		cell = row.createCell(3, CellType.STRING);
		cell.setCellValue("Postal Code");
		cell.setCellStyle(style);
		// Bonus
		cell = row.createCell(4, CellType.STRING);
		cell.setCellValue("City");
		cell.setCellStyle(style);

		cell = row.createCell(5);
		cell.setCellValue("Birthday");
		cell.setCellStyle(style);

		for (Person emp : list) {
			rownum++;
			row = sheet.createRow(rownum);

			cell = row.createCell(0, CellType.STRING);
			cell.setCellValue(emp.getFirstName());
			cell = row.createCell(1, CellType.STRING);
			cell.setCellValue(emp.getLastName());
			cell = row.createCell(2, CellType.STRING);
			cell.setCellValue(emp.getStreet());
			cell = row.createCell(3, CellType.NUMERIC);
			cell.setCellValue(emp.getPostalCode());
			cell = row.createCell(4, CellType.STRING);
			cell.setCellValue(emp.getCity());

			CellStyle cellStyle = workbook.createCellStyle();
			CreationHelper createHelper = workbook.getCreationHelper();
			cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy;@"));
			cell = row.createCell(5);
			cell.setCellValue(emp.getBirthday());
			cell.setCellStyle(cellStyle);
		}
		}
	//	File file = new File("C:/demo1/person123.xlsx");
		excelFilePath.getParentFile().mkdirs();

		FileOutputStream outFile = new FileOutputStream(excelFilePath);
		workbook.write(outFile);
		LOGGER.info("Created file: " + excelFilePath.getAbsolutePath());

	}

	public Workbook getWorkbook (File excelFilePath) throws IOException {
		Workbook workbook = null;

		if (excelFilePath.toString().endsWith("xlsx")) {
			workbook = new XSSFWorkbook();
		} else if (excelFilePath.toString().endsWith("xls")) {
			workbook = new HSSFWorkbook();
		} else {
			throw new IllegalArgumentException("The specified file is not Excel file");
		}

		return workbook;

	}

}
