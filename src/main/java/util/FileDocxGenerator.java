package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import application.MainApp;
import exception.ApplicationException;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import model.Person;
import model.Report;
import model.manager.ClassesManager;
import model.manager.DialogManager;
import processing.GenerateDocx;
import processing.LoadExcel;
import view.PersonOverviewController;

public class FileDocxGenerator {

	private final static Logger LOGGER = Logger.getLogger(FileDocxGenerator.class);

	public void generateDOCX(MainApp mainApp, TableView<Person> personTable,
			PersonOverviewController personOverviewController)
			throws IOException, ParseException, ApplicationException {
		LOGGER.debug("metod generateDOCX");
		ObservableList<Person> listCurrentClass = LoadExcel.getOuter().get(ClassesManager.getCurrentIndex());
		LOGGER.debug("Current class " + listCurrentClass);

		Map<String, Object> map = new HashMap<>();
		int fileIndexUser = 0;// unique file index which addede to file

		DirectoryChooser dirChooser = new DirectoryChooser();

		dirChooser.setTitle("Select a folder");

		String selectedDirPath = dirChooser.showDialog(mainApp.getPrimaryStage()).getAbsolutePath();

		// File downloadedFile = new File(selectedDirPath + "/" + downloadedFileName);

		/*
		 * FileChooser fileChooser = new FileChooser(); FileChooser.ExtensionFilter
		 * docFilter = new FileChooser.ExtensionFilter("DOC files (*.doc", "*.doc");
		 * FileChooser.ExtensionFilter docxFilter = new
		 * FileChooser.ExtensionFilter("DOCX files (*.docx", "*.docx");
		 * fileChooser.getExtensionFilters().add(docxFilter);
		 * fileChooser.getExtensionFilters().add(docFilter);
		 */

		File file = new File("docxFile//063-O.docx");// fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		File parentFile = file.getParentFile();
		LOGGER.debug("parentFile " + parentFile.getAbsolutePath() + "Load  ");
		LOGGER.debug("file " + file.getAbsolutePath() + "Load  ");

		if (file != null) {
			LOGGER.debug("Load " + file.getPath() + "Load doc ");
		}

		String fileName;
		String extension;// file.getName();
		int pos = file.getName().lastIndexOf('.'); // get index of dot to divide name and extension
		if (pos == -1) {
			LOGGER.error(file.getName());
			throw new ApplicationException("file without extension");
		} else {
			fileName = file.getName().substring(0, pos);
			extension = file.getName().substring(pos, file.getName().length());
			LOGGER.debug(fileName + " -- " + extension);

		}
		Report report = DialogManager.showOptionalDOCX();

		if (report == Report.ONE) {
			DialogManager.selectPerson();
			LOGGER.info(report);
			Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
			LOGGER.debug("selected Person from class " + listCurrentClass);

			if (selectedPerson != null) {
				boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
				if (okClicked) {
					personOverviewController.showPersonDetails(selectedPerson);
				}
			} else if (selectedPerson == null) {
				DialogManager.selectPerson();
				return;
			}
			LOGGER.info(fileName + selectedPerson.getLastName() + extension + " " + file.getPath());
			selectedDirPath = makeDir(selectedDirPath);//create dir for saving user folder
			LOGGER.debug("new  Dir Path" + selectedDirPath);

			File userFile = createDocFile(
					selectedDirPath + "//" + fileName + selectedPerson.getLastName() + ++fileIndexUser + extension);
			
			copyFileUsingStream(file.getAbsoluteFile(), userFile);
			File userParentFile = userFile.getParentFile();
			fillDocxInfo(map, selectedPerson, userFile, userParentFile);

		} else if (report == Report.MANY) {

			LOGGER.info(report);
			for (Person persons : listCurrentClass) {
				selectedDirPath = makeDir(selectedDirPath);//create dir for saving user folder
				LOGGER.debug("new  Dir Path" + selectedDirPath);
				File userFile = createDocFile(
						selectedDirPath + "//" + fileName + persons.getLastName() + ++fileIndexUser + extension);
				copyFileUsingStream(file.getAbsoluteFile(), userFile);
				File userParentFile = userFile.getParentFile();
				fillDocxInfo(map, persons, userFile, userParentFile);

			}
		}

	}

	public void fillDocxInfo(Map<String, Object> map, Person person, File file, File parentFile) {
		LOGGER.debug("Load  fillDocxInfo ");
		LOGGER.debug(file + "---------------- " + parentFile);

		String title[] = new String[] { "Name", "LastName", "Patronymic" ,"Street", "PostalCode", "Birthday",
				"PhoneNumber","testdata" };

		map.put(title[0], person.getFirstName());
		map.put(title[1], person.getLastName());
		map.put(title[2], person.getPatronymic());
		map.put(title[3], person.getStreet());
		//map.put(title[4], person.getPostalCode());
		map.put(title[6], person.getPhoneNumber());
		map.put(title[5], person.getBirthday());
		map.put(title[7], "");

		LOGGER.debug(map);
		boolean flag = GenerateDocx.generateAndSendDocx("\\" + file.getName(), map, parentFile.getAbsolutePath());
		LOGGER.debug("Status filling to file " + flag);
		LOGGER.debug("File have written succesefully");

	}

	public static File createDocFile(String fileName) {
		LOGGER.debug("method createDocFile");
		try {
			File file = new File(fileName);
			LOGGER.debug("file" + file);

			FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
			XWPFDocument doc = new XWPFDocument();
			doc.write(fos);
			fos.close();
			LOGGER.debug(file.getAbsolutePath());
			return file;
		} catch (Exception e) {
			LOGGER.error("an error ocured", e);

		}
		return null;

	}

	private static void copyFileUsingStream(File source, File dest) throws IOException {
		LOGGER.debug("method copyFileUsingStream");
		LOGGER.debug(source + "copy" + dest);

		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			LOGGER.debug(is);

			os = new FileOutputStream(dest);
			LOGGER.debug(is);

			byte[] buffer = new byte[1024];// check
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}

	public String makeDir(String selectedDirPath) {
		File file = new File(selectedDirPath+"//Directory1");
		if (!file.exists()) {
			if (file.mkdir()) {
				LOGGER.info("Directory is created!");
			} else {
				LOGGER.info("Failed to create directory!");
			}
		}
		return file.getAbsolutePath();
	}
}
