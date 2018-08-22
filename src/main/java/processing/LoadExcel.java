package processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
	int j = 0;
	static DateTimeFormatter VALIDATION = DateTimeFormatter.ofPattern("yyyy[-MM[-dd]]");

	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

	List<String> sheetName;
	public static ObservableList<Classes> classList = FXCollections.observableArrayList();
	public static ArrayList<ArrayList<Person>> outer ;//= new ArrayList<ArrayList<Person>>();
	

	ArrayList<Person> listPerson;
	public int getNumOfSheet() {
		return numOfSheet;
	}

	public List<Person> getListPerson() {
		return listPerson;
	}

	Classes cls;

	public List<Person> readBooksFromExcelFile(File excelFilePath, String sheet) throws IOException {
		 outer = new ArrayList<ArrayList<Person>>();
		listPerson = new ArrayList<Person>();
	//	stubPerson=new ArrayList<Person>();
		FileInputStream inputStream = new FileInputStream(excelFilePath);
		Sheet firstSheet;
		sheetName = new ArrayList<>();

		Workbook workbook = getWorkbook(inputStream, excelFilePath);
		LOGGER.info("name of sheet::" + sheet);

		numOfSheet = workbook.getNumberOfSheets();
		LOGGER.info("number of sheet::" + numOfSheet);
		for (int i = 0; i < numOfSheet; i++) {
			firstSheet = workbook.getSheetAt(i);
			String s = workbook.getSheetName(i);
			LOGGER.info("number of sheet::" + s);
			sheetName.add(s);

			Iterator<Row> iterator = firstSheet.iterator();

			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				if (nextRow.getRowNum() == 0) {
					LOGGER.info("second row");

				} else {
					Iterator<Cell> cellIterator = nextRow.cellIterator();
					Person person = new Person();

					while (cellIterator.hasNext()) {
						Cell nextCell = cellIterator.next();
						int columnIndex = nextCell.getColumnIndex();

						switch (columnIndex) {
						case 0:
							person.setFirstName((String) getCellValue(nextCell));
							break;
						case 1:
							person.setLastName((String) getCellValue(nextCell));
							break;
						case 2:
							person.setStreet((String) getCellValue(nextCell));
							break;

						case 3:
							person.setPostalCode((int) (double) getCellValue(nextCell));
							break;

						case 4:
							person.setCity((String) getCellValue(nextCell));
							break;

						case 5:

							DataFormatter dataFormatter = new DataFormatter();
							String cellStringValue = dataFormatter.formatCellValue(nextCell);
							// LOGGER.info("cellStringValue :" + cellStringValue);
							// LOGGER.debug(String.valueOf(formatter.format(cellStringValue)) + " string was
							// formatted ");
							// LocalDate.parse(cellStringValue, formatter);

							person.setBirthday(cellStringValue);
							break;
						}

					}

					listPerson.add(person);
				}

			}
			cls = new Classes(j++, sheetName.get(i), listPerson);
			classList.add(cls);
			outer.add(listPerson);
			listPerson = new ArrayList<Person>();

			/*
			 * Classes cls = new Classes(j++, sheetName.get(i), listPerson);
			 * classList.add(cls);
			 */
			/*
			 * LOGGER.info("Our class" + cls.toString()); LOGGER.info("Our class" +
			 * classList.toString());
			 */
			/*
			 * for (int i = 0; i < numOfSheet; i++) { LOGGER.info("name of :" +
			 * workbook.getSheetName(i)); String s = workbook.getSheetName(i);
			 * 
			 * //array = parseSting(s); sheetName.add(s); LOGGER.info("sheetName"
			 * +(sheetName));
			 * 
			 * LOGGER.info("load" + Arrays.toString(array)); /*if
			 * (listPerson.containsAll(classList)) { LOGGER.info("contains"); } else {
			 * LOGGER.info("create");
			 * 
			 * Classes cls = new Classes(i, s, listPerson);
			 * 
			 * classList.add(cls); LOGGER.info("Our class" + cls.toString());
			 * LOGGER.info("Our class" + classList.toString());
			 * 
			 * 
			 * }
			 */
		}
		LOGGER.info("outer" + outer.toString());

		workbook.close();
		inputStream.close();
		return outer.get(0);

	}

	/*
	 * private String[] parseSting(String s) {
	 * 
	 * Pattern p3 = Pattern.compile("\\-"); String[] words = p3.split(s);
	 * LOGGER.info("parseeeeeee" + Arrays.toString(words)); return words;
	 * 
	 * }
	 */
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
