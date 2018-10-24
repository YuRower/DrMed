package processing.DAO;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import application.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Person;
import model.Status;
import model.manager.DialogManager;
import processing.LoadExcel;
import processing.WriteExcel;

public class SchoolDAO {

	private static ObservableList<Person> personData = FXCollections.observableArrayList();

	private final static Logger LOGGER = Logger.getLogger(SchoolDAO.class);
	MainApp app;
	WriteExcel write;
	LoadExcel load;
	private static ObservableList<Person> currClass;
	private static ObservableList<Person> personWrapper;

	public static ObservableList<Person> getPersonData() {
		return personData;
	}

	private List<Person> listPerson;

	public void factoryStatusFile(File file, Status marker) {
		try {
			LOGGER.debug("File location " + file + "Status" + marker);

			if (marker == Status.SAVE) {
				write = new WriteExcel();
				write.writeToExcel(file);
				LOGGER.debug("Write file " + file + " to " + file.getAbsolutePath());

			} else if (marker == Status.LOAD) {
				load = new LoadExcel();
				listPerson = load.readBooksFromExcelFile(file, null);
				if (listPerson == null) {
					return;
				}
				LOGGER.debug("Read from file " + file + "  " + file.getAbsolutePath());
				personData.addAll(listPerson);
				LOGGER.info("added  list " + personData);

			} else {
				LOGGER.error("file not found");
				throw new RuntimeException();
			}
		} catch (Exception e) {
			LOGGER.error(e);
			e.printStackTrace();
			DialogManager.showErrorDialog("Error", "Could not load data from file:\n" + file.getPath());

		}
	}

	public List<Person> getListPerson() {
		return listPerson;
	}

	public ObservableList<Person> update(int indexClass) throws IOException {

		LOGGER.info("update class ");

		currClass = LoadExcel.getOuter().get(indexClass);
		LOGGER.info(currClass);
		personWrapper = FXCollections.observableArrayList(currClass);

		return personWrapper;

	}

	public void delete(int index) {
		LOGGER.info(personWrapper);
		personWrapper.remove(index);
		LOGGER.info(personWrapper);
	}

	public void clearWrapperList() {
		if (currClass == null) {
			LOGGER.info("null");

			LOGGER.info(currClass);
			LOGGER.info(personWrapper);
		} else {
			LOGGER.info("not null");

			currClass.clear();
			personWrapper.clear();
		}

	}
}
