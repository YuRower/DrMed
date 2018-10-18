package view;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import application.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import model.Classes;
import model.Lang;
import model.Person;
import model.VaccineTypeLocation;
import model.manager.ClassesManager;
import model.manager.LocaleManager;
import model.manager.VaccineManager;
import model.vaccine.VaccineEntity;
import processing.DAO.VaccinationTypeDAO;
import view.vaccination.VaccineTableController;

public class VaccineController implements Initializable {
	MainApp main;
	@FXML
	public ComboBox<VaccineTypeLocation> comboVaccine;
	private final static Logger LOGGER = Logger.getLogger(VaccineController.class);
    private ObservableList<VaccineEntity> vaccineData = FXCollections.observableArrayList();

	/**
	 * @return the vaccineData
	 */
	public ObservableList<VaccineEntity> getVaccineData() {
		return vaccineData;
	}

	public void setMainApp(MainApp mainApp) {
		this.main = mainApp;
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
	private void moveBack() {
		LOGGER.info("moveBack");
		main.showPersonOverview(LocaleManager.UA_LOCALE);
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
				LOGGER.info("combo vaccine is "+VaccineManager.getCurrentVaccine() );
				main.showVaccinationTables(LocaleManager.UA_LOCALE, VaccineManager.getCurrentVaccine().getResource());

			}
		});

	}
}
