package model.manager;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import model.Lang;
import model.Report;
import javafx.scene.control.ButtonType;

public class DialogManager {
	private final static Logger LOGGER = Logger.getLogger(DialogManager.class);
	public static final String BUNDLES_FOLDER = "property.text";
	private static Locale currentLang = null;

	public static void checkCinfirmationLang(Lang locale) {
		if (locale.getIndex() == 0) {
			currentLang = LocaleManager.RU_LOCALE;

		} else if (locale.getIndex() == 1) {
			currentLang = LocaleManager.UA_LOCALE;

		}
	}

	public static void showErrorDialog(String title, String text) {
		LOGGER.info("method showErrorDialog with " + title + " " + text);
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setContentText(text);
		alert.setHeaderText("");
		alert.showAndWait();
	}

	public static void showInfoDialog(String title, String text) {
		LOGGER.info("method showInfoDialog with " + title + " " + text);
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(text);
		alert.setHeaderText("");
		alert.showAndWait();
	}

	public static Report showOptionalDOCX() {
		LOGGER.info(" method showOptionalDOCX");

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog with Custom Actions");
		alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
		alert.setContentText("Choose your option.\nSelect report for whole class or specific people");
		ButtonType buttonTypeOne = new ButtonType("One");
		ButtonType buttonTypeTwo = new ButtonType("Two");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			return Report.ONE;
		} else if (result.get() == buttonTypeTwo) {
			return Report.MANY;
		} else {
			LOGGER.info("Cancel");
		}
		return null;

	}

	public static ButtonType wantToDelete() {
		LOGGER.info("method wantToDelete  ");
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		Lang locale = LocaleManager.getCurrentLang();
		checkCinfirmationLang(locale);
		ResourceBundle rb = ResourceBundle.getBundle(BUNDLES_FOLDER, currentLang);
		String confirmationDialog = rb.getString("confirmationDialog");
		alert.setTitle(confirmationDialog);
		alert.setHeaderText("Look, a Confirmation Dialog");
		alert.setContentText("Are you ok with this?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			return ButtonType.OK;
		} else {
			LOGGER.info("Cancel");
			return ButtonType.CANCEL;
		}

	}

	public static void selectPerson() {
		
		LOGGER.info("method selectPerson ");
		Lang locale = LocaleManager.getCurrentLang();
		checkCinfirmationLang(locale);
		ResourceBundle rb = ResourceBundle.getBundle(BUNDLES_FOLDER, currentLang);
		String title = rb.getString("confirmationDialog");
		String text = rb.getString("confirmationDialog");

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
	}

	public static void incorrectPassword(String title, String text) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText("Look, a Warning Dialog");
		alert.setContentText(text);

		alert.showAndWait();
	}

	public static void fileSuccessfully() {
		LOGGER.info("method showInfoDialog with " + "file successfully written" + " " );
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("file successfully written");
		alert.setContentText("file successfully written");
		alert.setHeaderText("");
		alert.showAndWait();
		
	}
}
