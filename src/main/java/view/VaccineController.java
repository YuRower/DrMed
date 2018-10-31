package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import application.MainApp;
import exception.ApplicationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import model.Person;
import model.VaccineTypeLocation;
import model.manager.DialogManager;
import model.manager.LocaleManager;
import model.manager.VaccineManager;
import model.vaccine.VaccineEntity;

import processing.XMLProcessing;
import processing.DAO.VaccinationTypeDAO;
import util.AbstractResource;
import util.FileDocxGenerator;

public class VaccineController  extends AbstractResource implements Initializable  {
	XMLProcessing xmlFile = new XMLProcessing();

	MainApp main;
	@FXML
	private Label selectedPersonLabel;
	@FXML
	public ComboBox<VaccineTypeLocation> comboVaccine;
	private final static Logger LOGGER = Logger.getLogger(VaccineController.class);
	private ObservableList<VaccineEntity> vaccineData = FXCollections.observableArrayList();

	String currentTable;
	Locale currentLocale;

	/**
	 * @return the vaccineData
	 */
	public ObservableList<VaccineEntity> getVaccineData() {
		return vaccineData;
	}

	public void setMainApp(MainApp mainApp, String currentTable, Locale currentLocale) {
		this.main = mainApp;
		this.currentTable = currentTable;
		this.currentLocale = currentLocale;
	}

	Person currentPerson;


	/**
	 * @return the currentPerson
	 */
	public Person getCurrentPerson() {
		return currentPerson;
	}

	/**
	 * @param currentPerson the currentPerson to set
	 */
	public void setCurrentPerson(Person currentPerson) {
		this.currentPerson = currentPerson;
	}

	private void fillVaccineComboBox() {
		ObservableList<VaccineTypeLocation> list = VaccinationTypeDAO.getVaccineList();

		LOGGER.info("List of vaccine =  " + list);

		comboVaccine.getItems().addAll(list);
		LOGGER.info("fill vaccine  into box ");

		if (VaccineManager.getCurrentVaccine() == null) {

			comboVaccine.getSelectionModel().select(0);
		} else {
			comboVaccine.getSelectionModel().select(VaccineManager.getCurrentVaccine().getIndex());
		}
	}



	@FXML
	private void handleAdd() throws ParseException {
		VaccineTypeLocation optionOfVaccine = comboVaccine.getSelectionModel().getSelectedItem();

		VaccineEntity newVaccine = new VaccineEntity(optionOfVaccine.getName());
		LOGGER.info(newVaccine);

		newVaccine.setId(currentPerson.getId());
		LOGGER.info(newVaccine.getId());

		LOGGER.info("handleAdd");
		String resource = getResource();
		LOGGER.info(resource);
		boolean result = main.showTableVaccineEditDialog(newVaccine, resource);
		LOGGER.info(result);
		if (result) {
			LOGGER.debug(newVaccine + "newVaccine");
			xmlFile.savePersonDataToFile(XML_FILE, newVaccine);
			main.showVaccinationTables(currentLocale, currentTable, currentPerson);

		}
	}

	@FXML
	private void moveBack() {
		LOGGER.info("moveBack");
		main.showPersonOverview(LocaleManager.getCurrentLang().getLocale());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		LOGGER.info("////initialize///////////" + arg1);
		fillVaccineComboBox();
		initListeners();
	}

	private void initListeners() {
		comboVaccine.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				LOGGER.info("combo vaccine setOnAction");
				VaccineTypeLocation optionOfVaccine = comboVaccine.getSelectionModel().getSelectedItem();
				VaccineManager.setCurrentVaccine(optionOfVaccine);
				LOGGER.info("combo vaccine is " + VaccineManager.getCurrentVaccine());
				main.showVaccinationTables(LocaleManager.getCurrentLang().getLocale(), VaccineManager.getCurrentVaccine().getResource(),
						currentPerson);

			}
		});

	}

	public void setSelectedPerson(Person person) {

		LOGGER.info("setSelectedPerson  " + person);

		if (person == null) {
			this.selectedPersonLabel.setText("");
		} else {

			this.selectedPersonLabel.setText(person.getFirstName());

			this.currentPerson = person;
		}

	}

}
