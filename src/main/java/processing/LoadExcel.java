package processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.Person;

public class LoadExcel {

	public List<Person> readBooksFromExcelFile(File excelFilePath) throws IOException {
		List<Person> listPerson = new ArrayList<>();
		FileInputStream inputStream = new FileInputStream(excelFilePath);

		Workbook workbook = getWorkbook(inputStream, excelFilePath);// new HSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator();

		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			if (nextRow.getRowNum() == 0) {
				System.out.println("firstROw");
			} else {
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				Person personList = new Person();

				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();
					int columnIndex = nextCell.getColumnIndex();

					switch (columnIndex) {
					case 0:
						personList.setFirstName((String) getCellValue(nextCell));
						break;
					case 1:
						personList.setLastName((String) getCellValue(nextCell));
						break;
					case 2:
						personList.setStreet((String) getCellValue(nextCell));

						break;

					case 3:
						personList.setPostalCode((int) (double) getCellValue(nextCell));

						break;

					case 4:
						personList.setCity((String) getCellValue(nextCell));

						break;

					case 5:

						DataFormatter dataFormatter = new DataFormatter();
						String cellStringValue = dataFormatter.formatCellValue(nextCell);
						personList.setBirthday(cellStringValue);

						break;
					}

				}
				listPerson.add(personList);
			}

			workbook.close();
			inputStream.close();
		}
		return listPerson;
	}

	private Object getCellValue(Cell cell) {
		CellType cellType = cell.getCellTypeEnum();
		switch (cellType) {
		case _NONE:
			return cell;
		case BOOLEAN:
			return cell.getBooleanCellValue();

		case NUMERIC:
			return cell.getNumericCellValue();

		case STRING:
			return cell.getStringCellValue();

		case ERROR:
			return "!";

		}

		return null;
	}

	public Workbook getWorkbook(FileInputStream inputStream, File excelFilePath) throws IOException {
		Workbook workbook = null;

		if (excelFilePath.toString().endsWith("xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		} else if (excelFilePath.toString().endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException("The specified file is not Excel file");
		}

		return workbook;

	}
}
