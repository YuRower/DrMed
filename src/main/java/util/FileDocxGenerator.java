package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import application.MainApp;
import exception.ApplicationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import model.Person;
import model.Report;
import model.manager.ClassesManager;
import model.manager.DialogManager;
import model.manager.VaccineManager;
import model.vaccine.VaccineEntity;
import processing.GenerateDocx;
import processing.LoadExcel;
import processing.XMLProcessing;
import processing.DAO.VaccinationTypeDAO;
import view.PersonOverviewController;

public class FileDocxGenerator {

	private final static Logger LOGGER = Logger.getLogger(FileDocxGenerator.class);
	private static final File XML_FILE = new File("xmlFile//vaccineInfo.xml");
	private static final int VACCINE_DIPHTERIA_GOOSEBERRY_TETANUS = 2;
	private static final int VACCINE_TUBERCULOSIS = 0;
	private Map<String, Object> mapVaccine = new HashMap<>();
	private ObservableList<VaccineEntity> vaccinePerson;
	private Map<String, Object> mapVaccine1 = new HashMap<>();

	public void generateDOCX(MainApp mainApp, TableView<Person> personTable,
			PersonOverviewController personOverviewController)
			throws IOException, ParseException, ApplicationException {

		LOGGER.debug("metod generateDOCX");
		if (LoadExcel.getOuter() == null) {
			DialogManager.selectPerson();

		} else {
			Report report = DialogManager.showOptionalDOCX();

			ObservableList<Person> listCurrentClass = LoadExcel.getOuter().get(ClassesManager.getCurrentIndex());
			LOGGER.debug("Current class " + listCurrentClass);

			Map<String, Object> map = new HashMap<>();
			int fileIndexUser = 0;// unique file index which added to file

			DirectoryChooser dirChooser = new DirectoryChooser();

			dirChooser.setTitle("Select a folder");

			String selectedDirPath = dirChooser.showDialog(mainApp.getPrimaryStage()).getAbsolutePath();

			File file = new File("docxFile//063-O.docx");
			File parentFile = file.getParentFile();
			LOGGER.debug("parentFile " + parentFile.getAbsolutePath() + "Load  ");
			LOGGER.debug("file " + file.getAbsolutePath() + "Load  ");

			String fileName;
			String extension;
			int pos = file.getName().lastIndexOf('.'); // get index of dot to divide name and extension
			if (pos == -1) {
				LOGGER.error(file.getName());
				throw new ApplicationException("file without extension");
			} else {
				fileName = file.getName().substring(0, pos);
				extension = file.getName().substring(pos, file.getName().length());
				LOGGER.debug(fileName + " -- " + extension);

			}

			if (report == Report.ONE) {
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
				selectedDirPath = makeDir(selectedDirPath);// create dir for saving user folder
				LOGGER.debug("new  Dir Path" + selectedDirPath);

				File userFile = createDocFile(
						selectedDirPath + "//" + fileName + selectedPerson.getLastName() + ++fileIndexUser + extension);

				copyFileUsingStream(file.getAbsoluteFile(), userFile);
				File userParentFile = userFile.getParentFile();
				fillDocxInfo(map, selectedPerson, userFile, userParentFile);
			} /*
				 * else if (report == Report.MANY) { LOGGER.info(report); for (Person persons :
				 * listCurrentClass) { selectedDirPath = makeDir(selectedDirPath);// create dir
				 * for saving user folder LOGGER.debug("new  Dir Path" + selectedDirPath); File
				 * userFile = createDocFile( selectedDirPath + "//" + fileName +
				 * persons.getLastName() + ++fileIndexUser + extension);
				 * copyFileUsingStream(file.getAbsoluteFile(), userFile); File userParentFile =
				 * userFile.getParentFile(); fillDocxInfo(map, persons, userFile,
				 * userParentFile); } }
				 */
		}
	}

	public void fillDocxInfo(Map<String, Object> map, Person person, File file, File parentFile) {
		LOGGER.debug("Load  fillDocxInfo ");
		XMLProcessing loadVaccine = new XMLProcessing();
		loadVaccine.loadPersonDataFromFile(XML_FILE);

		LOGGER.debug(file + "---------------- " + parentFile);

		String[] pupilInfo = new String[] { "Name", "LastName", "Patronymic", "Street", "PostalCode", "Birthday",
				"PhoneNumber", "testdata" };
		map.put(pupilInfo[0], person.getFirstName());
		map.put(pupilInfo[1], person.getLastName());
		map.put(pupilInfo[2], person.getPatronymic());
		map.put(pupilInfo[3], person.getStreet());
		// map.put(title[4], person.getPostalCode());
		map.put(pupilInfo[6], person.getPhoneNumber());
		map.put(pupilInfo[5], person.getBirthday());
		map.put(pupilInfo[7], "");
		LOGGER.debug(map);
		boolean flag = GenerateDocx.generateAndSendDocx("\\" + file.getName(), map, parentFile.getAbsolutePath(),
				false);
		LOGGER.debug("Status filling to file " + flag);

		String[] vaccineInfoTable1 = new String[] { "Age", "Date", "Doze", "Series", "Reaction", "MedContra",
				"TypeVaccine" };
		Iterator<VaccineEntity> list = filterConcreateVaccine(
				VaccinationTypeDAO.getVaccineList().get(VACCINE_TUBERCULOSIS).getName(),
				XMLProcessing.getAllVaccinesPersons()).iterator();
		LOGGER.debug(list);

		int i = 0;
		while (list.hasNext()) {
			VaccineEntity vaccine = list.next();
			if (vaccine.getId() == person.getId()) {
				mapVaccine.put(vaccineInfoTable1[0].concat(String.valueOf(i)), vaccine.getAge());
				mapVaccine.put(vaccineInfoTable1[1].concat(String.valueOf(i)), vaccine.getDate());
				mapVaccine.put(vaccineInfoTable1[2].concat(String.valueOf(i)), String.valueOf(vaccine.getDoze()));
				mapVaccine.put(vaccineInfoTable1[3].concat(String.valueOf(i)), vaccine.getSeries());
				mapVaccine.put(vaccineInfoTable1[4].concat(String.valueOf(i)), vaccine.getReaction());
				mapVaccine.put(vaccineInfoTable1[5].concat(String.valueOf(i)), vaccine.getMedicalContradication());
				mapVaccine.put(vaccineInfoTable1[6].concat(String.valueOf(i)), vaccine.getTypeVaccine());
				++i;
			}
		}
		LOGGER.debug(mapVaccine.toString());
		mapVaccine.toString();
		boolean flag1 = GenerateDocx.generateAndSendDocx("\\" + file.getName(), mapVaccine,
				parentFile.getAbsolutePath(), false);
		LOGGER.debug("Status filling to file " + flag1);

		String[] vaccineInfoTable3_6 = new String[] { "AgeDGT", "DateDGT", "DozeDGT", "SeriesDGT", "ReactionGeneralDGT",
				"ReactionLocalDGT", "MedContraDGT", "TypeVaccineDGT", "nameOfDrugDGT" };
		Iterator<VaccineEntity> list1 = filterConcreateVaccine(
				VaccinationTypeDAO.getVaccineList().get(VACCINE_DIPHTERIA_GOOSEBERRY_TETANUS).getName(),
				XMLProcessing.getAllVaccinesPersons()).iterator();
		int j = 0;
		while (list1.hasNext()) {
			VaccineEntity vaccine = list1.next();
			if (vaccine.getId() == person.getId()) {
				mapVaccine1.put(vaccineInfoTable3_6[0].concat(String.valueOf(j)), vaccine.getAge());
				mapVaccine1.put(vaccineInfoTable3_6[1].concat(String.valueOf(j)), vaccine.getDate());
				mapVaccine1.put(vaccineInfoTable3_6[2].concat(String.valueOf(j)), String.valueOf(vaccine.getDoze()));
				mapVaccine1.put(vaccineInfoTable3_6[3].concat(String.valueOf(j)), vaccine.getSeries());
				mapVaccine1.put(vaccineInfoTable3_6[4].concat(String.valueOf(j)), vaccine.getReactionGeneral());
				mapVaccine1.put(vaccineInfoTable3_6[5].concat(String.valueOf(j)), vaccine.getLocalReaction());
				mapVaccine1.put(vaccineInfoTable3_6[6].concat(String.valueOf(j)), vaccine.getMedicalContradication());
				mapVaccine1.put(vaccineInfoTable3_6[7].concat(String.valueOf(j)), vaccine.getTypeVaccine());
				mapVaccine1.put(vaccineInfoTable3_6[8].concat(String.valueOf(j)), vaccine.getNameOfDrug());

				++j;
			}
		}
		LOGGER.debug(mapVaccine1.toString());
		mapVaccine1.toString();
		boolean flag2 = GenerateDocx.generateAndSendDocx("\\" + file.getName(), mapVaccine1,
				parentFile.getAbsolutePath(), true);
		LOGGER.debug("Status filling to file " + flag2);
		LOGGER.debug("File have written succesefully");
	}

	private ObservableList<VaccineEntity> filterConcreateVaccine(String name,
			ObservableList<VaccineEntity> currentVaccinePerson) {
		vaccinePerson = FXCollections.observableArrayList();
		for (VaccineEntity ve : currentVaccinePerson) {
			LOGGER.info(ve.getName());
			if (name.equals(ve.getName())) {
				LOGGER.debug(ve);
				vaccinePerson.add(ve);
			}
		}
		return vaccinePerson;
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
		try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
			byte[] buffer = new byte[1024];// check
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		}
	}

	public String makeDir(String selectedDirPath) {
		File file = new File(selectedDirPath + "//Directory1");
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
