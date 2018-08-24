package application;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Lang;
import model.Person;
import processing.Status;
import util.LocaleManager;
import view.PersonEditDialogController;
import view.PersonOverviewController;
import view.RootLayoutController;

import org.apache.log4j.Logger;

public class MainApp extends Application implements Observer {

	private static final String FXML_MAIN = "/view/PersonOverview.fxml";
	public static final String BUNDLES_FOLDER = "property.text";
	private Status status;
	private FXMLLoader fxmlLoader;

	private Stage primaryStage;

	public BorderPane getRootLayout() {
		return rootLayout;
	}

	PersonOverviewController personController;
	RootLayoutController rootController;
	private BorderPane rootLayout;
	private AnchorPane page;
	AnchorPane personOverview;
	SchoolCollection schoolStorage = new SchoolCollection();
	private final static Logger LOGGER = Logger.getLogger(MainApp.class);

	public MainApp() {

	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setMaximized(true);
		this.primaryStage.setTitle("AddressApp");
		this.primaryStage.getIcons().add(new Image("/images/address_book_32.png"));
		Lang langRU = new Lang(0, "ru", "Русский", LocaleManager.RU_LOCALE);
		Lang langUK = new Lang(1, "uk", "Украинский", LocaleManager.UA_LOCALE);
		LocaleManager.setCurrentLang(langUK);
		LOGGER.info("setCurrentLang(langRU");
		initRootLayout(LocaleManager.UA_LOCALE);
		showPersonOverview(LocaleManager.UA_LOCALE);
	}

	public void initRootLayout(Locale locale) {
		try {
			getPrimaryStage().setFullScreen(true);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/RootLayout.fxml"));

			loader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, locale));
			rootLayout = (BorderPane) loader.load();

			LOGGER.info("Load RootLayout.fxml");
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			rootController = loader.getController();
			rootController.setMainApp(this);
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

	public void showPersonOverview(Locale locale) {
		try {
			getPrimaryStage().setFullScreen(true);

			FXMLLoader loader = new FXMLLoader();
			loader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, locale));
			loader.setLocation(MainApp.class.getResource("/view/PersonOverview.fxml"));

			personOverview = (AnchorPane) loader.load();

			LOGGER.info("Load PersonOverview.fxml");

			rootLayout.setCenter(personOverview);

			personController = loader.getController();
			personController.addObserver(this);
			LOGGER.info("////////////// addobserver//////////////// ");

			personController.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e);
		}
	}

	public boolean showPersonEditDialog(Person person) throws ParseException {
		try {
			getPrimaryStage().setFullScreen(true);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/PersonEditDialog.fxml"));
			page = (AnchorPane) loader.load();
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

	@Override
	public void update(Observable o, Object arg) {
		Locale current = null;

		Lang lang = (Lang) arg;
		if (lang.getIndex() == 0) {
			current = LocaleManager.UA_LOCALE;

		} else if (lang.getIndex() == 1) {
			current = LocaleManager.RU_LOCALE;
		}
		initRootLayout(current);
		showPersonOverview(current);


	}
}
