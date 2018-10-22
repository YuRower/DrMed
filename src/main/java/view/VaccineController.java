package view;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
	private static final String FIRST_TWO_TABLES = "/view/tableEditpages/edit1_2Table.fxml";
	private static final String FROM_THREE_TO_SIX_TABLES = "/view/tableEditpages/edit3_6Table.fxml";
	private static final String LAST_TABLE = "/view/tableEditpages/edit7Table.fxml";

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

	public String getResource() {
		LOGGER.info("method getResource"+VaccineManager.getCurrentVaccine());
		String resource = null;
		if (VaccineManager.getCurrentVaccine() == null) {
			resource = FIRST_TWO_TABLES;
		} else {
			int indVaccine = VaccineManager.getCurrentVaccine().getIndex();
			if ((indVaccine >= 0) && (indVaccine <= 1)) {
				LOGGER.info(indVaccine);
				resource = FIRST_TWO_TABLES;
			} else if ((indVaccine >= 2) && (indVaccine <= 6)) {
				LOGGER.info(indVaccine);
				resource = FROM_THREE_TO_SIX_TABLES;
			} else if (indVaccine == 7) {
				LOGGER.info(indVaccine);
				resource = LAST_TABLE;
			} else {
				LOGGER.info("not found current table");

			}
		}
		return resource;

	}

	@FXML
	private void handleAdd() {
		LOGGER.info("handleAdd");
		String resource = getResource();
		LOGGER.info(resource);

		try {
			boolean result = main.showTableVaccineEditDialog(resource);
			LOGGER.info(result);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void handleUpdate() {
		LOGGER.info("handleUpdate");
		String resource = getResource();
		LOGGER.info(resource);

		try {
			boolean result = main.showTableVaccineEditDialog(resource);
			LOGGER.info(result);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void handleDelete() {
		LOGGER.info("handleDelete");
		String resource = getResource();
		LOGGER.info(resource);

		try {
			boolean result = main.showTableVaccineEditDialog(resource);
			LOGGER.info(result);
		} catch (ParseException e) {
			e.printStackTrace();
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
				LOGGER.info("combo vaccine is " + VaccineManager.getCurrentVaccine());
				main.showVaccinationTables(LocaleManager.UA_LOCALE, VaccineManager.getCurrentVaccine().getResource());

			}
		});

	}

}
