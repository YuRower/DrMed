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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;

public class DialogManager {
	private final static Logger LOGGER = Logger.getLogger(DialogManager.class);
	private static Locale currentLang = null;
	private static Lang locale = LocaleManager.getCurrentLang();

	// static Image image = new Image("/images/logo.jpg");
	// static ImageView imageView = new ImageView(image);
	public static void checkCinfirmationLang(Lang locale) {
		if (locale.getIndex() == 0) {
			currentLang = LocaleManager.RU_LOCALE;

		} else if (locale.getIndex() == 1) {
			currentLang = LocaleManager.UA_LOCALE;

		}
	}

	public static void showErrorDialogOnLoadFile(File file) {
		checkCinfirmationLang(locale);
		ResourceBundle rb = ResourceBundle.getBundle(LocaleManager.BUNDLES_FOLDER, currentLang);
		String title = rb.getString("error");
		String text = rb.getString("noLoadFile");
		LOGGER.info("method showErrorDialogOnLoadFile with " + title + " " + text);
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
		ResourceBundle rb = ResourceBundle.getBundle(LocaleManager.BUNDLES_FOLDER, currentLang);

		String title = rb.getString("openFile");
		String textHeader = rb.getString("openFileFolder");
		String setContentText = rb.getString("openFileContext");
		String one = rb.getString("firstDecision");
		String two = rb.getString("secondDecision");
		String cancel = rb.getString("cancel");

		LOGGER.info(" method showOptionalDOCX");
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(textHeader);
		alert.setContentText(setContentText);
		ButtonType buttonTypeOne = new ButtonType(one);
		ButtonType buttonTypeTwo = new ButtonType(two);
		ButtonType buttonTypeCancel = new ButtonType(cancel, ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			return Report.ONE;
		} else if (result.get() == buttonTypeTwo) {
			return Report.MANY;
		} else {
			return Report.CANCEL;
		}

	}

	public static ButtonType wantToDelete() {
		LOGGER.info("method wantToDelete  ");
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		checkCinfirmationLang(locale);
		ResourceBundle rb = ResourceBundle.getBundle(LocaleManager.BUNDLES_FOLDER, currentLang);

		String confirmationDialog = rb.getString("confirmationDialogonDelete");
		String title = rb.getString("deleteRow");

		alert.setTitle(confirmationDialog);
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
		ResourceBundle rb = ResourceBundle.getBundle(LocaleManager.BUNDLES_FOLDER, currentLang);
		String title = rb.getString("error");
		String text = rb.getString("selectPerson");
		String headerText = rb.getString("selectPersonHeader");

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(text);
		alert.showAndWait();
	}

	public static void incorrectPassword() {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		checkCinfirmationLang(locale);
		ResourceBundle rb = ResourceBundle.getBundle(LocaleManager.BUNDLES_FOLDER, currentLang);
		String text = rb.getString("inccorectPasswordorLogin");
		String title = rb.getString("error");
		String headerText = rb.getString("errorAuthentication");

		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(text);

		alert.showAndWait();
	}

	public static void noDatatoSave() {
		LOGGER.info("method noDatatoSave with ");
		Alert alert = new Alert(Alert.AlertType.WARNING);
		checkCinfirmationLang(locale);
		ResourceBundle rb = ResourceBundle.getBundle(LocaleManager.BUNDLES_FOLDER, currentLang);
		String text = rb.getString("NoDatatoSave");
		String title = rb.getString("NoDatatoSave");
		alert.setTitle(title);
		alert.setContentText(text);
		alert.showAndWait();
	}

	public static void fileSuccessfully() {
		LOGGER.info("method showInfoDialog with " + "file successfully written" + " ");
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		checkCinfirmationLang(locale);
		ResourceBundle rb = ResourceBundle.getBundle(LocaleManager.BUNDLES_FOLDER, currentLang);
		String text = rb.getString("fileWritten");
		String title = rb.getString("titleFileWritten");
		String headerText = rb.getString("saveFileHeaderText");

		alert.setTitle(title);
		alert.setContentText(text);
		alert.setHeaderText(headerText);
		alert.showAndWait();

	}

	public static void addThroughLoad() {
		LOGGER.info("method addThroughLoad");
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		checkCinfirmationLang(locale);
		ResourceBundle rb = ResourceBundle.getBundle(LocaleManager.BUNDLES_FOLDER, currentLang);
		String text = rb.getString("addition");
		String title = rb.getString("additionThrough");
		String headerText = rb.getString("additionThroughLoad");

		alert.setTitle(title);
		alert.setContentText(text);
		alert.setHeaderText(headerText);
		alert.showAndWait();
	}

	public static void deleteEditPerson() {
		LOGGER.info("method deleteEditPerson");
		checkCinfirmationLang(locale);
		ResourceBundle rb = ResourceBundle.getBundle(LocaleManager.BUNDLES_FOLDER, currentLang);
		String noSelection = rb.getString("noSelection");
		String recordTable = rb.getString("recordTable");
		String pleaseSelect = rb.getString("pleaseSelect");
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(noSelection);
		alert.setHeaderText(recordTable);
		alert.setContentText(pleaseSelect);
		alert.showAndWait();
	}

	public static void deleteEditVaccine() {
		LOGGER.info("method deleteEditVaccine");
		checkCinfirmationLang(locale);
		ResourceBundle rb = ResourceBundle.getBundle(LocaleManager.BUNDLES_FOLDER, currentLang);
		String noSelectionVaccine = rb.getString("noSelectionVaccine");
		String recordTableVeccine = rb.getString("recordTableVeccine");
		String pleaseSelectVaccine = rb.getString("pleaseSelectVaccine");
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(noSelectionVaccine);
		alert.setHeaderText(recordTableVeccine);
		alert.setContentText(pleaseSelectVaccine);
		alert.showAndWait();
	}
}
