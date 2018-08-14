package view;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Person;
import util.DateUtil;

public class PersonEditDialogController {
	private final static Logger LOGGER = Logger.getLogger(PersonEditDialogController.class);
	Locale currentLocale = Locale.getDefault();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField streetField;
	@FXML
	private TextField postalCodeField;
	@FXML
	private TextField cityField;
	@FXML
	private TextField birthdayField;

	private Stage dialogStage;
	private Person person;
	private boolean okClicked = false;

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
		streetField.setText(person.getStreet());
		postalCodeField.setText(Integer.toString(person.getPostalCode()));
		cityField.setText(person.getCity());
		birthdayField.setText(String.valueOf(DateUtil.parse(String.valueOf(person.getBirthday()))));
		LOGGER.debug(String.valueOf(DateUtil.parse(String.valueOf(person.getBirthday()))));

		birthdayField.setPromptText("dd.mm.yyyy");
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
			person.setStreet(streetField.getText());
			person.setPostalCode(Integer.parseInt(postalCodeField.getText()));
			person.setCity(cityField.getText());
			person.setBirthday(DateUtil.format(LocalDate.parse(birthdayField.getText())));
			LOGGER.info(" User edit info " + DateUtil.format(LocalDate.parse(birthdayField.getText())));

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

		if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
			errorMessage += "No valid first name!\n";
		}
		if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
			errorMessage += "No valid last name!\n";
		}
		if (streetField.getText() == null || streetField.getText().length() == 0) {
			errorMessage += "No valid street!\n";
		}

		if (postalCodeField.getText() == null || postalCodeField.getText().length() == 0) {
			errorMessage += "No valid postal code!\n";
		} else {
			LOGGER.info(" try to parse the postal code into an int. ");

			try {
				Integer.parseInt(postalCodeField.getText());
			} catch (NumberFormatException e) {
				errorMessage += "No valid postal code (must be an integer)!\n";
			}
		}

		if (cityField.getText() == null || cityField.getText().length() == 0) {
			errorMessage += "No valid city!\n";
		}

		if (birthdayField.getText() == null || birthdayField.getText().length() == 0) {
			errorMessage += "No valid birthday!\n";
		} else {
			if (!DateUtil.validDate(birthdayField.getText())) {
				errorMessage += "No valid birthday. Use the format dd.mm.yyyy!\n";
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