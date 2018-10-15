package view;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import application.MainApp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import model.Lang;
import model.VaccinationType;
import processing.DAO.VaccinationTypeDAO;
import util.LocaleManager;
import util.VaccineManager;

public class VaccineController implements Initializable {
	MainApp main;
	@FXML
	public ComboBox<VaccinationType> comboVaccine;
	private final static Logger LOGGER = Logger.getLogger(VaccineController.class);

	public void setMainApp(MainApp mainApp) {
		this.main = mainApp;
	}

	private void fillVaccineComboBox() {
		ObservableList<VaccinationType> list = VaccinationTypeDAO.getVaccineList();
		//Інші імунобіологічні препарати

		LOGGER.info("List of vaccine =  "+ list);

		comboVaccine.getItems().addAll(list);
		LOGGER.info("fill vaccine  into box ");
		if (VaccineManager.getCurrentVaccine() == null) {

			comboVaccine.getSelectionModel().select(0);
		} else {
			comboVaccine.getSelectionModel().select(VaccineManager.getCurrentVaccine().getIndex());
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		LOGGER.info("////initialize///////////" + arg1);

		fillVaccineComboBox();		
	}
}
