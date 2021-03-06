package processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	static int numOfSheet;
	int j = 0;
	static DateTimeFormatter VALIDATION = DateTimeFormatter.ofPattern("yyyy[-MM[-dd]]");

	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

	private static List<String> sheetName;
	private static ObservableList<Classes> classList = FXCollections.observableArrayList();

	public static ObservableList<Classes> getClassList() {
		return classList;
	}

	public static List<String> getSheetName() {
		return sheetName;
	}

	private static ObservableList<ObservableList<Person>> outer;

	public static ObservableList<ObservableList<Person>> getOuter() {
		LOGGER.info(outer);
		return outer;
	}

	private ObservableList<Person> listPerson;

	private Classes cls;
	private static int counterId;

	public List<Person> readBooksFromExcelFile(File excelFilePath, String sheet) throws IOException {
		outer = FXCollections.observableArrayList();
		listPerson = FXCollections.observableArrayList();
		FileInputStream inputStream = new FileInputStream(excelFilePath);
		Sheet firstSheet;
		sheetName = new ArrayList<>();

		Workbook workbook = getWorkbook(inputStream, excelFilePath);
		LOGGER.info("name of sheet:" + sheet);

		numOfSheet = workbook.getNumberOfSheets();
		LOGGER.info("number of sheet:" + numOfSheet);
		for (int i = 0; i < numOfSheet; i++) {
			firstSheet = workbook.getSheetAt(i);
			String s = workbook.getSheetName(i);
			LOGGER.info("current num of sheet" + s);
			sheetName.add(s);

			Iterator<Row> iterator = firstSheet.iterator();

			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				if (nextRow.getRowNum() == 0) {
					LOGGER.info("first row");
				} else {
					Iterator<Cell> cellIterator = nextRow.cellIterator();
					Person person = new Person();
					person.setId(++counterId);

					while (cellIterator.hasNext()) {
						Cell nextCell = cellIterator.next();
						int columnIndex = nextCell.getColumnIndex();

						switch (columnIndex) {
						case 0:
							person.setLastName((String) getCellValue(nextCell));
							break;
						case 1:
							person.setFirstName((String) getCellValue(nextCell));
							break;
						case 2:
							person.setPatronymic((String) getCellValue(nextCell));
							break;
						case 3:
							person.setStreet((String) getCellValue(nextCell));
							break;
						case 4:
							Object postalCode = getCellValue(nextCell);
							if (postalCode == null) {
								break;
							} else {
								person.setPostalCode((int) ((double) postalCode));
							}
							break;
						case 5:
							DataFormatter dataFormatter = new DataFormatter();
							String cellStringValue = dataFormatter.formatCellValue(nextCell);
							person.setBirthday(cellStringValue);
							break;
						case 6:
							person.setPhoneNumber((String) getCellValue(nextCell));
							break;
						}

					}

					listPerson.add(person);
				}

			}
			cls = new Classes(j++, sheetName.get(i), listPerson);
			classList.add(cls);
			outer.add(listPerson);
			listPerson = FXCollections.observableArrayList();
		}
		workbook.close();
		inputStream.close();
		LOGGER.info("-----------------------------" + counterId + "----------------------");
		return outer.get(0);

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
			LOGGER.info("xlsx");
			workbook = new XSSFWorkbook(inputStream);
		} else if (excelFilePath.toString().endsWith("xls")) {
			LOGGER.info("xls");
			workbook = new HSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException("The specified file is not Excel file");
		}

		return workbook;

	}

}
