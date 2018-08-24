package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import model.Person;
import processing.LoadExcel;
import processing.Status;
import processing.WriteExcel;
import util.DialogManager;

public class SchoolCollection {
	private static ObservableList<Person> personData = FXCollections.observableArrayList();

	private final static Logger LOGGER = Logger.getLogger(SchoolCollection.class);
	MainApp app;
	WriteExcel write;
	LoadExcel load;
	private  ArrayList<Person> currClass ;
	private  ObservableList<Person> personWrapper ;
	public static ObservableList<Person> getPersonData() {
		return personData;
	}

	private List<Person> listPerson;
	/*
	 * public void showBirthdayStatistics() { try { app= new MainApp(); FXMLLoader
	 * loader = new FXMLLoader();
	 * loader.setLocation(MainApp.class.getResource("/view/BirthdayStatistics.fxml")
	 * ); AnchorPane page = (AnchorPane) loader.load();
	 * LOGGER.info("Load BirthdayStatistics.fxml");
	 * 
	 * Stage dialogStage = new Stage(); dialogStage.setTitle("Birthday Statistics");
	 * dialogStage.initModality(Modality.WINDOW_MODAL);
	 * dialogStage.initOwner(app.getPrimaryStage()); Scene scene = new Scene(page);
	 * dialogStage.setScene(scene);
	 * 
	 * dialogStage.getIcons().add(new Image("/images/calendar.png"));
	 * 
	 * BirthdayStatisticsController controller = loader.getController();
	 * controller.setPersonData(personData);
	 * 
	 * dialogStage.show();
	 * 
	 * } catch (IOException e) { LOGGER.error(e); } }
	 */

	public void commonFactoryMethod(File file, Status marker) {
		try {
			LOGGER.info("File location " + file + "Status" + marker);

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

	public ObservableList<Person> update(int index) throws IOException {
	    currClass = LoadExcel.getOuter().get(index);
		personWrapper = FXCollections.observableArrayList(currClass);
		return personWrapper;

	}
	
	/*public  void calledNewClass() {
		LOGGER.error(currClass== null);
		LOGGER.error(personWrapper == null);
		if (!(currClass == null && personWrapper == null )) {
		currClass.clear();
		personWrapper.clear();
		}
		LOGGER.error(currClass);
		LOGGER.error(personWrapper);


	}*/

}
