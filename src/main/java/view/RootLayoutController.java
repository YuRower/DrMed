package view;

import java.io.File;

import org.apache.log4j.Logger;

import application.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import model.Status;
import processing.Statistics;
import processing.LoadExcel;
import processing.DAO.SchoolDAO;

public class RootLayoutController {

	private MainApp mainApp;
	private Status status;
	public static File currentFile;
	private final static Logger LOGGER = Logger.getLogger(RootLayoutController.class);
	SchoolDAO schoolStorage = new SchoolDAO();

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void handleNew() {
		LOGGER.info(LoadExcel.getSheetName() + " " + LoadExcel.getClassList() + " " + LoadExcel.getOuter() + " "
				+ SchoolDAO.getPersonData() + " " + PersonOverviewController.getComboClasslist());
		LOGGER.info(LoadExcel.getSheetName() != null);
		LOGGER.info(LoadExcel.getClassList().isEmpty());
		LOGGER.info(LoadExcel.getOuter() != null);
		LOGGER.info(SchoolDAO.getPersonData().isEmpty());
		

		LOGGER.info(PersonOverviewController.getComboClasslist() != null);

		if (!((LoadExcel.getSheetName() == null) && (LoadExcel.getClassList().isEmpty())
				&& (LoadExcel.getOuter() == null) && (SchoolDAO.getPersonData().isEmpty()))) {
			LOGGER.info("ddelete");
			schoolStorage.clearWrapperList();
			LoadExcel.getClassList().clear();
			LoadExcel.getOuter().clear();
			LoadExcel.getSheetName().clear();
			SchoolDAO.getPersonData().clear();
			PersonOverviewController.getComboClasslist().clear();

		}
		LOGGER.info(LoadExcel.getSheetName() + " " + LoadExcel.getClassList() + " " + LoadExcel.getOuter() + " "
				+ SchoolDAO.getPersonData() + " " + PersonOverviewController.getComboClasslist());
		mainApp.setPersonFilePath(null);
	}

	@FXML
	private void handleOpen() {
		handleNew();
		FileChooser fileChooser = new FileChooser();
		LOGGER.info("Set extension filter XLS , XLSX files ");
		FileChooser.ExtensionFilter xlsxFilter = new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx");
		FileChooser.ExtensionFilter xlsFilter = new FileChooser.ExtensionFilter("XLS files (*.xls)", "*.xls");
		fileChooser.getExtensionFilters().add(xlsxFilter);
		fileChooser.getExtensionFilters().add(xlsFilter);

		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

		if (file != null) {
			LOGGER.debug("Load " + file.getPath() + "\nStatus " + status);
			currentFile = file;
			status = Status.LOAD;
			schoolStorage.factoryStatusFile(file, status);
		}
	}

	@FXML
	private void handleSave() {
		File personFile = mainApp.getPersonFilePath();
		LOGGER.debug("HANDLE SAVE " + personFile);
		if (personFile != null) {
			status = Status.SAVE;
			LOGGER.debug("Load " + personFile.getPath() + "\nStatus " + status);
			schoolStorage.factoryStatusFile(personFile, status);
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

			schoolStorage.factoryStatusFile(file, status);
		}
	}

	@FXML
	private void handleAbout() {
		LOGGER.info("About ");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("MedicalApp");
		alert.setHeaderText("About");
		alert.setContentText("add Copy Rigth and email feedback");
		alert.showAndWait();
	}

	@FXML
	private void handleExit() {
		LOGGER.debug("handleExit ");
		System.exit(0);
	}

	@FXML
	private void handleLogOut() {
		LOGGER.debug("handleLogOut ");
		this.mainApp.authintication(false);
	}

	@FXML
	private void handleShowBirthdayStatistics() {
		LOGGER.info("Show Birthday Statistics ");
		new Statistics().showBirthdayStatistics();
	}
	@FXML
	private void handleShowVaccineStatistics() {
		LOGGER.info("Show Birthday Statistics ");
		new Statistics().showVaccineStatistics();
	}
}