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
import javafx.stage.FileChooser;
import model.Person;
import model.Report;
import model.manager.ClassesManager;
import model.manager.DialogManager;
import processing.GenerateDocx;
import processing.LoadExcel;
import view.PersonOverviewController;

public class FileDocxGenerator {
	GenerateDocx rg;

	private final static Logger LOGGER = Logger.getLogger(FileDocxGenerator.class);

	public void generateDOCX(MainApp mainApp, TableView<Person> personTable, PersonOverviewController personOverviewController) throws IOException, ParseException, ApplicationException {
		LOGGER.debug("generDOCX");

		rg = new GenerateDocx();
		int someIndex = 0;
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter docFilter = new FileChooser.ExtensionFilter("DOC files (*.doc", "*.doc");
		FileChooser.ExtensionFilter docxFilter = new FileChooser.ExtensionFilter("DOCX files (*.docx", "*.docx");
		fileChooser.getExtensionFilters().add(docxFilter);

		fileChooser.getExtensionFilters().add(docFilter);

		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		File parentFile = file.getParentFile();

		if (file != null) {
			LOGGER.debug("Load " + file.getPath() + "Load doc ");
		}
		ObservableList<Person> listGen = LoadExcel.getOuter().get(ClassesManager.getCurrentIndex());
		Map<String, Object> map = new HashMap<>();
		String fileName;
		String extension;// file.getName();
		int pos = file.getName().lastIndexOf('.'); //get index of dot to divide name and extension
		if (pos == -1) {
			LOGGER.error(file.getName());

			throw new ApplicationException("file withot extension");
		} else {
			fileName = file.getName().substring(0, pos);
			extension = file.getName().substring(pos, file.getName().length());
			LOGGER.debug(fileName + " -- " + extension);

		}
		Report report = DialogManager.showOptionalDOCX();

		if (report == Report.ONE) {
			DialogManager.selectPerson(" 1 ", " select person");//should fix
			LOGGER.info(report);
			Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
			if (selectedPerson != null) {
				boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
				if (okClicked) {
					personOverviewController.showPersonDetails(selectedPerson);
				}
			} else if (selectedPerson == null) {
				DialogManager.selectPerson(" 1 ", " please select person");
				return;
			}
			LOGGER.info(fileName + selectedPerson.getLastName() + extension + " " + file.getPath());
			File temp = createDocFile(fileName + selectedPerson.getLastName() + ++someIndex + extension);
			copyFileUsingStream(file, temp);
			fillDocxInfo(map, selectedPerson, file, parentFile);

		} else if (report == Report.MANY) {

			LOGGER.info(report);
			for (Person person : listGen) {

				File temp = createDocFile(fileName + person.getLastName() + ++someIndex + extension);
				copyFileUsingStream(file, temp);

				fillDocxInfo(map, person, file, parentFile);

			}
		}

	}

	public void fillDocxInfo(Map map, Person person, File file, File parentFile) {
		LOGGER.debug("Load  sentInfo ");
		LOGGER.debug("Load  sentInfo " + parentFile);

		String title[] = new String[] { "FirstName", "LastName", "Street", "PostalCode", "City", "Birthday" };

		map.put(title[0], person.getFirstName());
		map.put(title[1], person.getLastName());
		map.put(title[2], person.getStreet());
		map.put(title[3], person.getPostalCode());
		map.put(title[4], person.getCity());
		map.put(title[5], person.getBirthday());

		LOGGER.debug(map);
		boolean flag = rg.generateAndSendDocx("\\" + file.getName(), map, parentFile.getAbsolutePath());
		LOGGER.debug("Load " + flag + "Load doc ");
		LOGGER.debug("Load  sentInfo succesefully");

	}

	public static File createDocFile(String fileName) {
		LOGGER.debug("createDocFile");
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
			LOGGER.error("an error ocured" , e);

		}
		return null;

	}

	private static void copyFileUsingStream(File source, File dest) throws IOException {
		LOGGER.debug("copyFileUsingStream");
		LOGGER.debug(source + "copy" + dest);

		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];//check
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}
}
