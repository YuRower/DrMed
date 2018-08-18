package processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Classes;
import model.Person;

public class LoadExcel {
	private final static Logger LOGGER = Logger.getLogger(LoadExcel.class);
	int numOfSheet = 0;
	String[] array;
	public static ObservableList<Classes> classList = FXCollections.observableArrayList();// = new ArrayList<>();


	public int getNumOfSheet() {
		return numOfSheet;
	}

	public List<Person> readBooksFromExcelFile(File excelFilePath) throws IOException {
		List<Person> listPerson = new ArrayList<>();
		FileInputStream inputStream = new FileInputStream(excelFilePath);

		Workbook workbook = getWorkbook(inputStream, excelFilePath);
		Sheet firstSheet = workbook.getSheetAt(0);
		numOfSheet = workbook.getNumberOfSheets();

		LOGGER.info("number of sheet::" + workbook.getNumberOfSheets());
		Iterator<Row> iterator = firstSheet.iterator();

		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			if (nextRow.getRowNum() == 0) {
				LOGGER.info("second row");

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
		for (int i = 0; i < numOfSheet; i++) {
			LOGGER.info("name of :" + workbook.getSheetName(i));
			String s = workbook.getSheetName(i);
			array = parseSting(s);
			LOGGER.info("load" + Arrays.toString(array));

			Classes cls = new Classes(Integer.parseInt(array[0]), array[1], listPerson);
			classList.add(cls);
			LOGGER.info("Our class" + cls.toString());
			LOGGER.info("Our class" + classList.toString());


		}
		LOGGER.info(listPerson.toString());

		return listPerson;

	}

	private String[] parseSting(String s) {
		Pattern p3 = Pattern.compile("\\-");
		String[] words = p3.split(s);
		LOGGER.info("parseeeeeee" + Arrays.toString(words));
		return words;

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
