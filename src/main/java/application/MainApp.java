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
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Lang;
import model.Person;
import model.Status;
import model.manager.LocaleManager;
import processing.DAO.SchoolDAO;
import view.LoginController;
import view.PersonEditDialogController;
import view.PersonOverviewController;
import view.RootLayoutController;
import view.VaccineController;

import org.apache.log4j.Logger;

public class MainApp extends Application implements Observer {

	public static final String BUNDLES_FOLDER = "property.text";
	private Status status;
	private Stage primaryStage;
	
	PersonOverviewController personController;
	RootLayoutController rootController;
	LoginController loginController;
	VaccineController vaccineController;

	private BorderPane rootLayout;
	private AnchorPane editPersonPage;
	
	AnchorPane personOverview;
	AnchorPane loginPage;
	BorderPane tablePage;
	TableView<?> vaccineTable;


	private final static Logger LOGGER = Logger.getLogger(MainApp.class);

	public MainApp() {
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setMaximized(true);
		this.primaryStage.setTitle("MedApp");
		this.primaryStage.getIcons().add(new Image("/images/address_book_32.png"));
		Lang langRU = new Lang(0, "ru", "Русский", LocaleManager.RU_LOCALE);
		Lang langUK = new Lang(1, "uk", "Украинский", LocaleManager.UA_LOCALE);
		LocaleManager.setCurrentLang(langUK);
		LOGGER.info("Current Lang " + langUK);
		authintication(false);

	}

	public void authintication(boolean authinticated) {
		if (authinticated) {
			initRootLayout(LocaleManager.UA_LOCALE);
			showPersonOverview(LocaleManager.UA_LOCALE);
		} else {
			initLoginController(LocaleManager.UA_LOCALE);
		}
	}

	private void initLoginController(Locale locale) {
		try {
			LOGGER.info("Init Login Controller");
			this.primaryStage.setTitle("MedApp");

			getPrimaryStage().setFullScreen(true);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/UserAuthentication.fxml"));
			loader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, locale));
			loginPage =  loader.load();
			LOGGER.info("Load loginPage.fxml");
			Scene scene = new Scene(loginPage);
			primaryStage.setScene(scene);
			loginController = loader.getController();
			loginController.setMainApp(this);
			primaryStage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void initRootLayout(Locale locale) {
		SchoolDAO schoolStorage = new SchoolDAO();

		try {
			LOGGER.info("init Root Layout");
			getPrimaryStage().setFullScreen(true);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/RootLayout.fxml"));

			loader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, locale));
			rootLayout = loader.load();

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
			schoolStorage.factoryStatusFile(file, status);
		}
	}

	public void showPersonOverview(Locale locale) {
		try {
			LOGGER.info("Show Person Overview");

			getPrimaryStage().setFullScreen(true);

			FXMLLoader loader = new FXMLLoader();
			loader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, locale));
			loader.setLocation(MainApp.class.getResource("/view/PersonOverview.fxml"));

			personOverview =  loader.load();

			LOGGER.info("Load PersonOverview.fxml");

			rootLayout.setCenter(personOverview);

			personController = loader.getController();
			personController.addObserver(this);
			LOGGER.info(" add observer for person controller");

			personController.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e);
		}
	}
	
	public void showVaccinationTables(Locale locale,String resource) {
		try {
			LOGGER.info("Show Vaccination Tables");
			this.primaryStage.setTitle("MedApp");

			getPrimaryStage().setFullScreen(true);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/VaccinationTables.fxml"));
			loader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, locale));
			tablePage = loader.load();
			LOGGER.info("Load VaccinationTables.fxml");
			rootLayout.setCenter(tablePage);
			
			/*SHOULD REFACTOR*/
			FXMLLoader tableLoader = new FXMLLoader();
			tableLoader.setLocation(MainApp.class.getResource(resource));
			tableLoader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, locale));
			vaccineTable = tableLoader.load();
			tablePage.setCenter(vaccineTable);
	
			
			vaccineController = loader.getController();
			vaccineController.setMainApp(this);
						
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	public boolean showPersonEditDialog(Person person) throws ParseException {
		try {
			LOGGER.info("show Person Edit Dialogl");

			getPrimaryStage().setFullScreen(true);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/PersonEditDialog.fxml"));
			editPersonPage =  loader.load();
			LOGGER.info("Load PersonEditDialog.fxml");

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Person");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(editPersonPage);
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
	
	@Override
	public void update(Observable o, Object arg) {
		Locale current = null;

		Lang lang = (Lang) arg;
		LOGGER.info("index  " + lang.getIndex());

		if (lang.getIndex() == 0) {
			current = LocaleManager.RU_LOCALE;
			LocaleManager.setCurrentLang(lang);

		} else if (lang.getIndex() == 1) {
			current = LocaleManager.UA_LOCALE;
			LocaleManager.setCurrentLang(lang);

		}
		LOGGER.info("setCurrentLang " + lang);
		LOGGER.info("Locale " + current);

		initRootLayout(current);
		showPersonOverview(current);

	}

	public File getPersonFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);
		LOGGER.debug(" File Path = " + filePath);

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
			LOGGER.debug("set File Path = " + file.getPath());
			primaryStage.setTitle("AddressApp - " + file.getName());
		} else {
			prefs.remove("filePath");
			primaryStage.setTitle("AddressApp");
		}
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public BorderPane getRootLayout() {
		return rootLayout;
	}
	
	public static void main(String[] args) {
		LOGGER.info("////////////// Start//////////////// ");
		launch(args);
	}
}
