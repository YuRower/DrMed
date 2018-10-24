package view.tableEditpages;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.vaccine.VaccineEntity;
import util.DateUtil;


public class VaccineEditPageController {
	private final static Logger LOGGER = Logger.getLogger(VaccineEditPageController.class);
	Locale currentLocale = Locale.getDefault();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
	@FXML
	private TextField chipViewField;
	@FXML
	private TextField ageField;
	@FXML
	private TextField seriesField;
	@FXML
	private TextField dozeField;
	@FXML
	private TextField responseLocalvaccineField;
	@FXML
	private TextField dateField;
	@FXML
	private TextField medicalСontraindicationsField;
	@FXML
	private TextField nameOfDrugField;

	private Stage dialogStage;
	private VaccineEntity vaccine;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
		LOGGER.info(" fxml file has been loaded ");

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;

		this.dialogStage.getIcons().add(new Image("/images/edit.png"));
	}

	public void setFisrtTwoTable(VaccineEntity vaccine) throws ParseException {
		LOGGER.info(" setFisrtTwoTable "+ vaccine);

		this.vaccine = vaccine;
		LOGGER.info(" setFisrtTwoTable "+ vaccine);

		chipViewField.setText(vaccine.getTypeVaccine());
		ageField.setText(vaccine.getAge());
		seriesField.setText(vaccine.getSeries());
		dozeField.setText(Double.toString(vaccine.getDoze()));
		medicalСontraindicationsField.setText(vaccine.getMedicalContradication());
		dateField.setText(String.valueOf(DateUtil.parse(String.valueOf(vaccine.getDate()))));
		LOGGER.debug(String.valueOf(DateUtil.parse(String.valueOf(vaccine.getDate()))));
		dateField.setPromptText("dd.mm.yyyy");
		LOGGER.info("Info about vaccine"+ vaccine);
	}

	public void setThreeToSixTable(VaccineEntity vaccine) {
		LOGGER.info(" setThreeToSixTable ");

		try {
			setFisrtTwoTable(vaccine);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nameOfDrugField.setText(vaccine.getNameOfDrug());
		LOGGER.info("Info about vaccine"+ vaccine);

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() throws ParseException {
		if (isInputValid()) {
			LOGGER.info(" Table edit info ");

			vaccine.setTypeVaccine(chipViewField.getText());
			vaccine.setSeries(seriesField.getText());
			vaccine.setAge(ageField.getText());
			vaccine.setDoze(Double.parseDouble(dozeField.getText()));
			vaccine.setMedicalContradication(medicalСontraindicationsField.getText());
			vaccine.setDate(DateUtil.format(LocalDate.parse(dateField.getText())));
			LOGGER.info(" Table edit info " + DateUtil.format(LocalDate.parse(dateField.getText())));
			LOGGER.info(" Table edit info " +vaccine);

			okClicked = true;
			dialogStage.close();
		}
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private boolean isInputValid() throws ParseException {
		LOGGER.info(" Validation user input ");

		String errorMessage = "";

		if (chipViewField.getText() == null || chipViewField.getText().length() == 0) {
			errorMessage += "No valid chipViewField!\n";
		}
		if (ageField.getText() == null || ageField.getText().length() == 0) {
			errorMessage += "No valid ageField!\n";
		}
		if (seriesField.getText() == null || seriesField.getText().length() == 0) {
			errorMessage += "No valid seriesField!\n";
		}

		if (dozeField.getText() == null || dozeField.getText().length() == 0) {
			errorMessage += "No valid dozeField!\n";
		} else {
			LOGGER.info(" try to parse the dozeField  into an int. ");

			try {
				Double.parseDouble(dozeField.getText());
			} catch (NumberFormatException e) {
				errorMessage += "No valid dozeField (must be an double)!\n";
			}
		}

		if (medicalСontraindicationsField.getText() == null || medicalСontraindicationsField.getText().length() == 0) {
			errorMessage += "No valid medicalСontraindicationsField!\n";
		}

		if (dateField.getText() == null || dateField.getText().length() == 0) {
			errorMessage += "No valid birthday!\n";
		} else {
			if (!DateUtil.validDate(dateField.getText())) {
				errorMessage += "No valid dateField. Use the format dd.mm.yyyy!\n";
			}
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {

			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}
}
