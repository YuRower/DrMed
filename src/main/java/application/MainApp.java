package application;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Person;
import processing.LoadExcel;
import processing.Status;
import processing.WriteExcel;
import view.BirthdayStatisticsController;
import view.PersonEditDialogController;
import view.PersonOverviewController;
import view.RootLayoutController;

import org.apache.log4j.Logger;

public class MainApp extends Application {
	private Status status;
	private Stage primaryStage;
	private BorderPane rootLayout;
	SchoolCollection schoolStorage = new SchoolCollection();
	private final static Logger LOGGER = Logger.getLogger(MainApp.class);

	public MainApp() {

	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("AddressApp");
		this.primaryStage.getIcons().add(new Image("/images/address_book_32.png"));
		initRootLayout();
		showPersonOverview();
	}

	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			LOGGER.info("Load RootLayout.fxml");
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			RootLayoutController controller = loader.getController();

			controller.setMainApp(this);
			primaryStage.show();
		} catch (IOException e) {
			LOGGER.error(e);
		}

		File file = getPersonFilePath();
		if (file != null) {
			status = Status.LOAD;
			LOGGER.debug("Load " + file.getPath() + "\nStatus " + status);
			schoolStorage.commonFactoryMethod(file, status);
		}
	}

	public void showPersonOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/PersonOverview.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			LOGGER.info("Load PersonOverview.fxml");

			rootLayout.setCenter(personOverview);

			PersonOverviewController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e);
		}
	}

	public boolean showPersonEditDialog(Person person) throws ParseException {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/PersonEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			LOGGER.info("Load PersonEditDialog.fxml");

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Person");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			PersonEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setPerson(person);

			dialogStage.getIcons().add(new Image("/images/edit.png"));

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			LOGGER.error(e);
			return false;
		}
	}

	public File getPersonFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);
		LOGGER.debug("get Person File Path//////////// " + filePath);

		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	public void setPersonFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());
			LOGGER.debug("set Person File Path /////////////////////" + file.getPath());
			primaryStage.setTitle("AddressApp - " + file.getName());
		} else {
			prefs.remove("filePath");
			primaryStage.setTitle("AddressApp");
		}
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		LOGGER.info("////////////// Start//////////////// ");
		launch(args);
	}
}
