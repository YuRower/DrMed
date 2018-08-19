package view;

import java.io.File;

import org.apache.log4j.Logger;

import application.MainApp;
import application.SchoolCollection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import processing.Status;

public class RootLayoutController {

	private MainApp mainApp;
	private Status status;
	public static File currentFile;
	private final static Logger LOGGER = Logger.getLogger(RootLayoutController.class);
	SchoolCollection schoolStorage = new SchoolCollection();

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void handleNew() {
		SchoolCollection.getPersonData().clear();
		 mainApp.setPersonFilePath(null);
	}

	@FXML
	private void handleOpen() {
		handleNew() ;
		FileChooser fileChooser = new FileChooser();
		LOGGER.info("Set extension filter XLS , XLSX files ");

		FileChooser.ExtensionFilter xlsxFilter = new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx");
		FileChooser.ExtensionFilter xlsFilter = new FileChooser.ExtensionFilter("XLS files (*.xls)", "*.xls");
		fileChooser.getExtensionFilters().add(xlsxFilter);
		fileChooser.getExtensionFilters().add(xlsFilter);

		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

		if (file != null) {
			LOGGER.debug("Load " + file.getPath() + "\nStatus " + status);
			currentFile=file;
			status = Status.LOAD;
			schoolStorage.commonFactoryMethod(file, status);
		}
	}


	@FXML
	private void handleSave() {
		File personFile = mainApp.getPersonFilePath();
LOGGER.debug("HANDLE SAVE " + personFile);
		if (personFile != null) {
			status = Status.SAVE;
			LOGGER.debug("Load " + personFile.getPath() + "\nStatus " + status);
			schoolStorage.commonFactoryMethod(personFile, status);
		} else {
			handleSaveAs();
		}
	}

	@FXML
	private void handleSaveAs() {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter xlsxFilter = new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx");
		FileChooser.ExtensionFilter xlsFilter = new FileChooser.ExtensionFilter("XLS files (*.xls)", "*.xls");
		fileChooser.getExtensionFilters().add(xlsxFilter);
		fileChooser.getExtensionFilters().add(xlsFilter);
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
		LOGGER.debug("Load " + file.getPath());
		if (file != null) {
			if (!file.getPath().endsWith(".xlsx")) {
				file = new File(file.getPath());

			} else if (!file.getPath().endsWith(".xls")) {
				file = new File(file.getPath());
			} else {
				LOGGER.debug(file.getPath() + " file not found ");

			}
			status = Status.SAVE;
			LOGGER.debug("Load " + file.getPath() + "\nStatus " + status);

			schoolStorage.commonFactoryMethod(file, status);
		}
	}

	@FXML
	private void handleAbout() {
		LOGGER.info("About ");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("MedicalApp");
		alert.setHeaderText("About");
		alert.setContentText("Copy Rigth");
		alert.showAndWait();
	}

	@FXML
	private void handleExit() {
		LOGGER.debug("Info ");
		System.exit(0);
	}

	@FXML
	private void handleShowBirthdayStatistics() {
		LOGGER.info("Show Birthday Statistics ");
		schoolStorage.showBirthdayStatistics();
	}
}