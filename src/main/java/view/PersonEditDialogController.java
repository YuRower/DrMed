package view;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Person;
import model.manager.LocaleManager;
import util.DateUtil;

public class PersonEditDialogController {
	private final static Logger LOGGER = Logger.getLogger(PersonEditDialogController.class);
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField patronymicField;
	@FXML
	private TextField streetField;
	@FXML
	private TextField postalCodeField;
	@FXML
	private TextField birthdayField;
	@FXML
	private TextField phoneNumberField;
	
	private Stage dialogStage;
	private Person person;
	private boolean okClicked = false;
//	public static final String BUNDLES_FOLDER = "property.text";
	private static Locale locale = LocaleManager.getCurrentLang().getLocale();

	@FXML
	private void initialize() {
		LOGGER.info(" fxml file has been loaded ");

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;

		this.dialogStage.getIcons().add(new Image("/images/edit.png"));
	}

	public void setPerson(Person person) throws ParseException {
		this.person = person;
		firstNameField.setText(person.getFirstName());
		lastNameField.setText(person.getLastName());
		patronymicField.setText(person.getPatronymic());
		streetField.setText(person.getStreet());
		postalCodeField.setText(Integer.toString(person.getPostalCode()));
		birthdayField.setText(String.valueOf(person.getBirthday()));
		phoneNumberField.setText(person.getPhoneNumber());
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() throws ParseException {
		if (isInputValid()) {
			LOGGER.info(" User edit info ");

			person.setFirstName(firstNameField.getText());
			person.setLastName(lastNameField.getText());
			person.setPatronymic(patronymicField.getText());
			person.setStreet(streetField.getText());
			person.setPostalCode(Integer.parseInt(postalCodeField.getText()));
			person.setBirthday(DateUtil.format(DateUtil.parse(birthdayField.getText())));
			person.setPhoneNumber(phoneNumberField.getText());

			okClicked = true;
			dialogStage.close();
		}
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private boolean isInputValid() throws ParseException {
		ResourceBundle rb = ResourceBundle.getBundle(LocaleManager.BUNDLES_FOLDER, locale);
		String firstNameProp = rb.getString("firstNameField");
		String lastNameProp = rb.getString("lastNameField");
		String patronymicProp = rb.getString("patronymicProp");
		String streetProp = rb.getString("streetProp");
		String birthdayProp = rb.getString("birthdayProp");
		String postalCodeProp = rb.getString("postalCodeProp");
		//String phoneNumberProp = rb.getString("phoneNumberField");
		String dateField = rb.getString("dateField");
		String noValiedFields = rb.getString("noValiedFields");
		String enterCorrectFields = rb.getString("enterCorrectFields");
	

		LOGGER.info(" Validation user input ");

		String errorMessage = "";

		if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
			errorMessage += firstNameProp+"\n";
		}
		if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
			errorMessage += lastNameProp+"\n";
		}
		if (patronymicField.getText() == null || patronymicField.getText().length() == 0) {
			errorMessage += patronymicProp+"\n";
		}
		if (streetField.getText() == null || streetField.getText().length() == 0) {
			errorMessage += streetProp+"\n";
		}

		if (postalCodeField.getText() == null || postalCodeField.getText().length() == 0) {
			errorMessage += postalCodeProp+"\n";
		} else {
			LOGGER.info(" try to parse the postal code into an int. ");

			try {
				Integer.parseInt(postalCodeField.getText());
			} catch (NumberFormatException e) {
				errorMessage += "No valid postal code (must be an integer)!\n";
			}
		}


		if (birthdayField.getText() == null || birthdayField.getText().length() == 0) {
			errorMessage += birthdayProp+"\n";
		}
		/*if (phoneNumberField.getText() == null || phoneNumberField.getText().length() == 0) {
			errorMessage += phoneNumberProp+"\n";
		
		}*/ else {
			if (!DateUtil.validDate(birthdayField.getText())) {
				errorMessage += dateField+"\n";
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