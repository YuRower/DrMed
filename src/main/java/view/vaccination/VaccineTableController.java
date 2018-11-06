package view.vaccination;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import application.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import model.Person;
import model.manager.ClassesManager;
import model.manager.DialogManager;
import model.vaccine.VaccineEntity;
import processing.LoadExcel;
import processing.XMLProcessing;
import util.AbstractResource;

public class VaccineTableController extends AbstractResource implements Initializable {
	private final static Logger LOGGER = Logger.getLogger(VaccineTableController.class);
	//private static final String XML_FILE = "xmlFile//vaccineInfo.xml";

	@FXML
	private TableView<VaccineEntity> vaccineTable;
	@FXML
	private TableColumn<VaccineEntity, String> typeOfVAccineColumn;
	@FXML
	private TableColumn<VaccineEntity, String> ageColumn;
	@FXML
	private TableColumn<VaccineEntity, String> dataColumn;
	@FXML
	private TableColumn<VaccineEntity, Double> dozeColumn;
	@FXML
	private TableColumn<VaccineEntity, String> reactionColumn;
	@FXML
	private TableColumn<VaccineEntity, String> seriesColumn;
	@FXML
	private TableColumn<VaccineEntity, String> medicalContradicationColumn;
	private Locale currentLocale;

	private String currentTable;

	private Person currentPerson;
	private static MainApp main;

	public VaccineTableController() {
		LOGGER.info("VaccineTableController");

	}

	@FXML
	private void handleEditVaccine() throws ParseException {
		LOGGER.info("handleEditVaccine");
		VaccineEntity selectedEntity = vaccineTable.getSelectionModel().getSelectedItem();
		int indexEntity = vaccineTable.getSelectionModel().getSelectedIndex();
		String resource = getResource();
		LOGGER.info(resource);
		if (selectedEntity != null) {
			boolean okClicked = main.showTableVaccineEditDialog(selectedEntity, resource);
			LOGGER.info("new +" + selectedEntity);
			if (okClicked) {
				XMLProcessing xmlFile = new XMLProcessing();
				LOGGER.info("handleDeleteVaccine");

				LOGGER.info(xmlFile.getCurrentVaccinePerson() + "getVaccineData");
				if (indexEntity >= 0) {
					LOGGER.info(xmlFile.getCurrentVaccinePerson() + "getVaccineData");
					xmlFile.deleteVaccineFromXMLStrorage(indexEntity);
					xmlFile.savePersonDataToFile(XML_FILE, selectedEntity);

				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("No Selection");
					alert.setHeaderText("No vaccine Selected");
					alert.setContentText("Please select a person in the table.");
					alert.showAndWait();
				}

				xmlFile.loadPersonDataFromFile(XML_FILE);
				vaccineTable.setItems(xmlFile.getCurrentVaccinePerson());
			}

		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No Selection");
			alert.setHeaderText("No Person Selected");
			alert.setContentText("Please select a person in the table.");

			alert.showAndWait();
		}
	}

	@FXML
	private void handleDeleteVaccine() {
		ButtonType checkUserInputOnDelete = DialogManager.wantToDelete();
		if (checkUserInputOnDelete == ButtonType.OK) {
			LOGGER.info("handleDelete");
			LOGGER.info(vaccineTable);
			int selectedIndex = vaccineTable.getSelectionModel().getSelectedIndex();
			LOGGER.info("deleteVaccine");
			LOGGER.info(selectedIndex);
			XMLProcessing xmlFile = new XMLProcessing();
			LOGGER.info(xmlFile.getCurrentVaccinePerson() + "getVaccineData");
			if (selectedIndex >= 0) {
				vaccineTable.getItems().remove(selectedIndex);
				LOGGER.info(xmlFile.getCurrentVaccinePerson() + "getVaccineData");
				xmlFile.deleteVaccineFromXMLStrorage(selectedIndex);
				vaccineTable.setItems(xmlFile.getCurrentVaccinePerson());
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("No Selection");
				alert.setHeaderText("No vaccine Selected");
				alert.setContentText("Please select a person in the table.");
				alert.showAndWait();
			}

		} else {
			LOGGER.info("Cancel");
			return;
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		XMLProcessing xmlFile = new XMLProcessing();
		xmlFile.loadPersonDataFromFile(XML_FILE);
		LOGGER.info(xmlFile.getCurrentVaccinePerson() + "getVaccineData");
		if (vaccineTable != null){
		vaccineTable.setItems(xmlFile.getCurrentVaccinePerson());

		LOGGER.info("initialize//////////////////////////////////////////");
		typeOfVAccineColumn.setCellValueFactory(cellData -> cellData.getValue().typeVaccineProperty());
		ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty());
		dataColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

		dozeColumn.setCellValueFactory(cellData -> cellData.getValue().dozeProperty().asObject());
		reactionColumn.setCellValueFactory(cellData -> cellData.getValue().reactionProperty());
		seriesColumn.setCellValueFactory(cellData -> cellData.getValue().seriesProperty());
		medicalContradicationColumn
				.setCellValueFactory(cellData -> cellData.getValue().medicalContradicationProperty());
		
		}
	}

	public void setMainApp(MainApp mainApp, Locale locale, String currentTable, Person currentPerson) {
		LOGGER.info(main);
		main = mainApp;
		this.currentLocale = locale;
		this.currentTable = currentTable;
		this.currentPerson = currentPerson;
		LOGGER.info(main);
	}

}
