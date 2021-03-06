package view.vaccination;

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
import model.VaccineTypeLocation;
import model.manager.DialogManager;
import model.manager.VaccineManager;
import model.vaccine.VaccineEntity;
import processing.XMLProcessing;
import processing.DAO.VaccinationTypeDAO;
import util.AbstractResource;

public class VaccineTableController extends AbstractResource implements Initializable {
	private final static Logger LOGGER = Logger.getLogger(VaccineTableController.class);
	// private static final String XML_FILE = "xmlFile//vaccineInfo.xml";

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
	private TableColumn<VaccineEntity, String> reactionGeneralColumn;
	@FXML
	private TableColumn<VaccineEntity, String> reactionLocalColumn;
	@FXML
	private TableColumn<VaccineEntity, String> medicalContradicationColumn;
	@FXML
	private TableColumn<VaccineEntity, String> nameOfDrugColumn;

	private ObservableList<VaccineEntity> filteredList;
	private ObservableList<VaccineEntity> vaccinePerson;
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

				LOGGER.info(xmlFile.getCurrentVaccinePerson() + "getVaccineData");
				if (indexEntity >= 0) {

					LOGGER.info(xmlFile.getCurrentVaccinePerson() + "getVaccineData");
					VaccineEntity toDelete = filterConcreateVaccine(xmlFile.getCurrentVaccinePerson()).get(indexEntity);

					xmlFile.deleteVaccineFromXMLStrorage(toDelete);
					xmlFile.savePersonDataToFile(XML_FILE, selectedEntity);

				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("No Selection");
					alert.setHeaderText("No vaccine Selected");
					alert.setContentText("Please select a person in the table.");
					alert.showAndWait();
				}

				xmlFile.loadPersonDataFromFile(XML_FILE);
				vaccineTable.setItems(filterConcreateVaccine(xmlFile.getCurrentVaccinePerson()));
			}

		} else {
			DialogManager.deleteEditVaccine();
		}
	}

	@FXML
	private void handleDeleteVaccine() {

		int selectedIndex = vaccineTable.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {
			ButtonType checkUserInputOnDelete = DialogManager.wantToDelete();

			if (checkUserInputOnDelete == ButtonType.OK) {
				XMLProcessing xmlFile = new XMLProcessing();

				LOGGER.info("handleDelete");
				filteredList = filterConcreateVaccine(xmlFile.getCurrentVaccinePerson());
				LOGGER.info("deleteVaccine");
				LOGGER.info(selectedIndex);
				LOGGER.info(xmlFile.getCurrentVaccinePerson() + "getVaccineData");
				LOGGER.info("oooooooooooooooooooooooooo" + vaccineTable.getItems());

				VaccineEntity toDelete = filteredList.get(selectedIndex);
				LOGGER.info(xmlFile.getCurrentVaccinePerson() + "getVaccineData");
				filteredList = xmlFile.deleteVaccineFromXMLStrorage(toDelete);

				vaccineTable.getItems().remove(selectedIndex);

				xmlFile.loadPersonDataFromFile(XML_FILE);
				vaccineTable.setItems(filterConcreateVaccine(xmlFile.getCurrentVaccinePerson()));
			} else {
				LOGGER.info("Cancel");
				return;
			}
		} else {
			DialogManager.deleteEditVaccine();

		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		LOGGER.info("initialize//////////////////////////////////////////");

		XMLProcessing xmlFile = new XMLProcessing();
		xmlFile.loadPersonDataFromFile(XML_FILE);
		if (vaccineTable != null) {

			if (VaccineManager.getCurrentVaccine() == null) {// Should fix
				VaccineTypeLocation vc = VaccinationTypeDAO.getVaccineList().get(0);
				VaccineManager.setCurrentVaccine(vc);
				filteredList = filterConcreateVaccine(xmlFile.getCurrentVaccinePerson());
				LOGGER.info("getVaccineData<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + filteredList);

				vaccineTable.setItems(filteredList);
				LOGGER.info("null table-----------------------------------");

				typeOfVAccineColumn.setCellValueFactory(cellData -> cellData.getValue().typeVaccineProperty());
				ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty());
				dataColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

				dozeColumn.setCellValueFactory(cellData -> cellData.getValue().dozeProperty().asObject());
				reactionColumn.setCellValueFactory(cellData -> cellData.getValue().reactionProperty());
				seriesColumn.setCellValueFactory(cellData -> cellData.getValue().seriesProperty());
				medicalContradicationColumn
						.setCellValueFactory(cellData -> cellData.getValue().medicalContradicationProperty());

			} else {

				vaccineTable.setItems(filterConcreateVaccine(xmlFile.getCurrentVaccinePerson()));

				int indVaccine = VaccineManager.getCurrentVaccine().getIndex();
				if ((indVaccine >= 0) && (indVaccine < 1)) {// Should fix
					LOGGER.info("first table-------------------------------------------------------");

					typeOfVAccineColumn.setCellValueFactory(cellData -> cellData.getValue().typeVaccineProperty());
					ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty());
					dataColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
					dozeColumn.setCellValueFactory(cellData -> cellData.getValue().dozeProperty().asObject());
					reactionColumn.setCellValueFactory(cellData -> cellData.getValue().reactionProperty());
					seriesColumn.setCellValueFactory(cellData -> cellData.getValue().seriesProperty());
					medicalContradicationColumn
							.setCellValueFactory(cellData -> cellData.getValue().medicalContradicationProperty());

				} else if ((indVaccine >= 2) && (indVaccine <= 6)) {

					LOGGER.info("<><><><><><><><><><><><><>" + vaccineTable.getItems());
					LOGGER.info("second table--------------------------------------------------------");
					vaccineTable.setItems(filterConcreateVaccine(xmlFile.getCurrentVaccinePerson()));
					typeOfVAccineColumn.setCellValueFactory(cellData -> cellData.getValue().typeVaccineProperty());
					ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty());
					dataColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
					dozeColumn.setCellValueFactory(cellData -> cellData.getValue().dozeProperty().asObject());
					seriesColumn.setCellValueFactory(cellData -> cellData.getValue().seriesProperty());
					medicalContradicationColumn
							.setCellValueFactory(cellData -> cellData.getValue().medicalContradicationProperty());
					nameOfDrugColumn.setCellValueFactory(cellData -> cellData.getValue().nameOfDrugProperty());
					reactionLocalColumn.setCellValueFactory(cellData -> cellData.getValue().reactionLocaleProperty());
					reactionGeneralColumn
							.setCellValueFactory(cellData -> cellData.getValue().reactionGeneralProperty());

				} else {
					LOGGER.info(indVaccine);
					LOGGER.info("not found current table");

				}
			}

		}

	}

	private ObservableList<VaccineEntity> filterConcreateVaccine(ObservableList<VaccineEntity> currentVaccinePerson) {
		vaccinePerson = FXCollections.observableArrayList();

		if (VaccineManager.getCurrentVaccine() != null) {
			String strVaccine = VaccineManager.getCurrentVaccine().getName();
			for (VaccineEntity ve : currentVaccinePerson) {
				LOGGER.info(strVaccine + "  " + ve.getName());
				LOGGER.info(strVaccine.equals(ve.getName()));
				if (strVaccine.equals(ve.getName())) {
					vaccinePerson.add(ve);

				}
			}
		}

		return vaccinePerson;
	}

	public void setMainApp(MainApp mainApp) {
		main = mainApp;
	}

}
