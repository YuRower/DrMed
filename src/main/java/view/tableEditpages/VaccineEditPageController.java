package view.tableEditpages;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Lang;
import model.manager.LocaleManager;
import model.vaccine.VaccineEntity;
import util.DateUtil;


public class VaccineEditPageController {
	private final static Logger LOGGER = Logger.getLogger(VaccineEditPageController.class);
	Locale currentLocale = Locale.getDefault();
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
	public static final String BUNDLES_FOLDER = "property.text";
	private static Locale locale = LocaleManager.getCurrentLang().getLocale();

	
	@FXML
	private void initialize() {
		LOGGER.info(" fxml file has been loaded ");

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;

		this.dialogStage.getIcons().add(new Image("/images/edit.png"));
	}

	public void setFisrtTable(VaccineEntity vaccine) throws ParseException {
		LOGGER.info(" setFisrtTwoTable "+ vaccine);

		this.vaccine = vaccine;
		LOGGER.info(" setFisrtTwoTable "+ vaccine);

		chipViewField.setText(vaccine.getTypeVaccine());
		ageField.setText(vaccine.getAge());
		seriesField.setText(vaccine.getSeries());
		dozeField.setText(Double.toString(vaccine.getDoze()));
		medicalСontraindicationsField.setText(vaccine.getMedicalContradication());
		responseLocalvaccineField.setText(vaccine.getReaction());
		LOGGER.info("Info about vaccine"+ vaccine.getReaction());

		dateField.setText(String.valueOf(vaccine.getDate()));
		dateField.setPromptText("dd.mm.yyyy");
		LOGGER.info("Info about vaccine"+ vaccine);
	}
	
	public void setThirdTable(VaccineEntity vaccine) throws ParseException {
		LOGGER.info(" setFisrtTwoTable "+ vaccine);

		this.vaccine = vaccine;
		LOGGER.info(" setFisrtTwoTable "+ vaccine);

		chipViewField.setText(vaccine.getTypeVaccine());
		ageField.setText(vaccine.getAge());
		seriesField.setText(vaccine.getSeries());
		dozeField.setText(Double.toString(vaccine.getDoze()));
		nameOfDrugField.setText(vaccine.getNameOfDrug());


		medicalСontraindicationsField.setText(vaccine.getMedicalContradication());
		responseLocalvaccineField.setText(vaccine.getReaction());
		LOGGER.info("Info about vaccine"+ vaccine.getReaction());

		dateField.setText(String.valueOf(vaccine.getDate()));
		dateField.setPromptText("dd.mm.yyyy");
		LOGGER.info("Info about vaccine"+ vaccine);
	}
	


	public void setThreeToSixTable(VaccineEntity vaccine) {
		LOGGER.info(" setThreeToSixTable ");

		try {
			setFisrtTable(vaccine);
		} catch (ParseException e) {
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
 			vaccine.setReaction(responseLocalvaccineField.getText());
			LOGGER.info("Info about vaccine"+ responseLocalvaccineField.getText());

			vaccine.setDate(DateUtil.format(DateUtil.parse(dateField.getText())));
			LOGGER.info(" Table edit info " + DateUtil.format(DateUtil.parse(dateField.getText())));
			LOGGER.info(" Table edit info " +vaccine);

			okClicked = true;
			dialogStage.close();
		}
	}
	@FXML
	private void handleOkTable3_6() throws ParseException {
		if (isInputValid()) {
			LOGGER.info(" handleOkTable3_6 ");

			vaccine.setTypeVaccine(chipViewField.getText());
			vaccine.setSeries(seriesField.getText());
			vaccine.setAge(ageField.getText());
			vaccine.setDoze(Double.parseDouble(dozeField.getText()));
			vaccine.setMedicalContradication(medicalСontraindicationsField.getText());
			vaccine.setNameOfDrug(nameOfDrugField.getText());
 			vaccine.setReaction(responseLocalvaccineField.getText());
			LOGGER.info("Info about vaccine"+ responseLocalvaccineField.getText());

			vaccine.setDate(DateUtil.format(DateUtil.parse(dateField.getText())));
			LOGGER.info(" Table edit info " + DateUtil.format(DateUtil.parse(dateField.getText())));
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
		ResourceBundle rb = ResourceBundle.getBundle(BUNDLES_FOLDER, locale);
		String typeVaccineProp = rb.getString("chipViewField");
		String seriesFieldProp = rb.getString("seriesField");
		String dozeFieldProp = rb.getString("dozeField");
		String medicalContraindicationsFieldProp = rb.getString("medicalContraindicationsField");
		String birthdayFieldProp = rb.getString("birthdayField");
		String responseLocalvaccineFieldProp = rb.getString("responseLocalvaccineField");
		String ageFieldProp = rb.getString("ageField");
		String noValidDozeFieldProp = rb.getString("noValidDozeField");
		String dateFieldProp = rb.getString("dateField");
		String noValiedFields = rb.getString("noValiedFields");
		String enterCorrectFields = rb.getString("enterCorrectFields");


		
		
		LOGGER.info(" Validation user input ");

		String errorMessage = "";

		if (chipViewField.getText() == null || chipViewField.getText().length() == 0) {
			errorMessage += typeVaccineProp+"\n";
		}
		if (ageField.getText() == null || ageField.getText().length() == 0) {
			errorMessage += ageFieldProp+"\n";
		}
		if (seriesField.getText() == null || seriesField.getText().length() == 0) {
			errorMessage += seriesFieldProp+"\n";
		}

		if (dozeField.getText() == null || dozeField.getText().length() == 0) {
			errorMessage += dozeFieldProp+"\n";
		} else {
			LOGGER.info(" try to parse the dozeField  into an int. ");

			try {
				Double.parseDouble(dozeField.getText());
			} catch (NumberFormatException e) {
				errorMessage += noValidDozeFieldProp+"\n";
			}
		}

		if (medicalСontraindicationsField.getText() == null || medicalСontraindicationsField.getText().length() == 0) {
			errorMessage += medicalContraindicationsFieldProp+"\n";
		}
		if (responseLocalvaccineField.getText() == null || responseLocalvaccineField.getText().length() == 0) {
			errorMessage +=responseLocalvaccineFieldProp+ "\n";
		}
		
		if (dateField.getText() == null || dateField.getText().length() == 0) {
			errorMessage += birthdayFieldProp+"!\n";
		} else {
			if (!DateUtil.validDate(dateField.getText())) {
				errorMessage += dateFieldProp+"\n";
			}
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {

			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle(noValiedFields);
			alert.setHeaderText(enterCorrectFields);
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}
}
