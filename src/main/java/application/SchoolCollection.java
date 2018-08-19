package application;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Classes;
import model.Person;
import processing.LoadExcel;
import processing.Status;
import processing.WriteExcel;
import util.DialogManager;
import view.BirthdayStatisticsController;
import view.RootLayoutController;

public class SchoolCollection {
	private static ObservableList<Person> personData = FXCollections.observableArrayList();

	private final static Logger LOGGER = Logger.getLogger(SchoolCollection.class);
	MainApp app; // new MainApp();
	WriteExcel write;
	LoadExcel load;
	public static ObservableList<Person> getPersonData() {
		return personData;
	}

	public void showBirthdayStatistics() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/BirthdayStatistics.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			LOGGER.info("Load BirthdayStatistics.fxml");

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Birthday Statistics");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(app.getPrimaryStage());
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			dialogStage.getIcons().add(new Image("/images/calendar.png"));

			BirthdayStatisticsController controller = loader.getController();
			controller.setPersonData(personData);

			dialogStage.show();

		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	public void commonFactoryMethod(File file, Status marker) {
		try {
			LOGGER.info("File location " + file + "Status" + marker);
			
			if (marker == Status.SAVE) {
				write = new WriteExcel();
				write.writeToExcel(file);
				LOGGER.debug("Write file " + file + " to " + file.getAbsolutePath());

			} else if (marker == Status.LOAD) {
				load = new LoadExcel();
				List<Person> listPerson = load.readBooksFromExcelFile(file,null);
				LOGGER.debug("Read from file " + file + "  " + file.getAbsolutePath());
				personData.addAll(listPerson);
			//	Classes cls = new Classes(0, "A", personData);
				LOGGER.info("added  list " + personData);
			} else {
				LOGGER.error("file not found");
				throw new RuntimeException();
			}
		} catch (Exception e) {
			LOGGER.error(e);
			DialogManager.showErrorDialog("Error", "Could not load data from file:\n" + file.getPath());

		}
	}

	/*public void update(String str) {
		// TODO Auto-generated method stub
		
	}*/

	public void update(String sheet) throws IOException {
		File file = RootLayoutController.currentFile;
		load = new LoadExcel();

	LOGGER.debug("update  file " + file + "  " + file.getAbsolutePath() + "sheet " + sheet);

		load.readBooksFromExcelFile(file,sheet);

	}
}
