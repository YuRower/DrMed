package model.manager;

import java.io.File;
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
	private static Lang locale = LocaleManager.getCurrentLang();

	public static void checkCinfirmationLang(Lang locale) {
		if (locale.getIndex() == 0) {
			currentLang = LocaleManager.RU_LOCALE;

		} else if (locale.getIndex() == 1) {
			currentLang = LocaleManager.UA_LOCALE;

		}
	}

	public static void showErrorDialogOnLoadFile(File file) {
		checkCinfirmationLang(locale);
		ResourceBundle rb = ResourceBundle.getBundle(BUNDLES_FOLDER, currentLang);
		String title = rb.getString("error");
		String text = rb.getString("noLoadFile");
		LOGGER.info("method showErrorDialog with " + title + " " + text);
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setContentText(text + file.getPath());
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
		checkCinfirmationLang(locale);
		 ResourceBundle rb = ResourceBundle.getBundle(BUNDLES_FOLDER, currentLang);

		String title = rb.getString("openFile");
		String textHeader = rb.getString("openFileFolder");
		String setContentText = rb.getString("openFileContext");

		LOGGER.info(" method showOptionalDOCX");
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(textHeader);
		alert.setContentText(setContentText);
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
		checkCinfirmationLang(locale);
		 ResourceBundle rb = ResourceBundle.getBundle(BUNDLES_FOLDER, currentLang);

		String confirmationDialog = rb.getString("confirmationDialogonDelete");
		String title = rb.getString("deleteRow");

		alert.setTitle(confirmationDialog);
		//alert.setHeaderText("Look, a Confirmation Dialog");
		alert.setContentText(title);
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
		checkCinfirmationLang(locale);
		 ResourceBundle rb = ResourceBundle.getBundle(BUNDLES_FOLDER, currentLang);
		String title = rb.getString("selectPerson");
		String text = rb.getString("selectPerson");

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
	}

	public static void incorrectPassword() {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		checkCinfirmationLang(locale);
		 ResourceBundle rb = ResourceBundle.getBundle(BUNDLES_FOLDER, currentLang);
		String text = rb.getString("inccorectPasswordorLogin");
		String title = rb.getString("error");
		String headerText = rb.getString("errorAuthentication");

		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(text);

		alert.showAndWait();
	}

	public static void fileSuccessfully() {
		LOGGER.info("method showInfoDialog with " + "file successfully written" + " " );
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		checkCinfirmationLang(locale);
		 ResourceBundle rb = ResourceBundle.getBundle(BUNDLES_FOLDER, currentLang);
		String text = rb.getString("fileWritten");
		String title = rb.getString("titleFileWritten");
		String headerText = rb.getString("saveFileHeaderText");
		
		alert.setTitle(title);
		alert.setContentText(text);
		alert.setHeaderText(headerText);
		alert.showAndWait();
		
	}
}
