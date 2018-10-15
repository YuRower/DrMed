package util;

import java.util.Optional;

import org.apache.log4j.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import model.Report;
import javafx.scene.control.ButtonType;

public class DialogManager {
	private final static Logger LOGGER = Logger.getLogger(DialogManager.class);

	public static void showErrorDialog(String title, String text) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setContentText(text);
		alert.setHeaderText("");
		alert.showAndWait();
	}

	public static void showInfoDialog(String title, String text) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(text);
		alert.setHeaderText("");
		alert.showAndWait();
	}

	public static Report showOptionalDOCX() {
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
			LOGGER.info("cancel");
		}
		return null;

	}

	public static ButtonType wantToDelete() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Look, a Confirmation Dialog");
		alert.setContentText("Are you ok with this?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			return ButtonType.OK;
		} else {
			LOGGER.info("cancel");
			return ButtonType.CANCEL;
		}

	}

	public static void selectPerson(String title, String text) {
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
}
