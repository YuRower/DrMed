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
import model.vaccine.VaccineEntity;
import processing.DAO.SchoolDAO;
import view.LoginController;
import view.PersonEditDialogController;
import view.PersonOverviewController;
import view.RootLayoutController;
import view.VaccineController;
import view.tableEditpages.VaccineEditPageController;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class MainApp extends Application implements Observer {

	public static final String BUNDLES_FOLDER = "property.text";
	private Status status;
	private Stage primaryStage;

	PersonOverviewController personController;
	RootLayoutController rootController;
	LoginController loginController;
	VaccineController vaccineController;
	VaccineEditPageController vaccineEditController;

	private BorderPane rootLayout;
	private AnchorPane editPersonPage;
	private AnchorPane editTablePage;


	AnchorPane personOverview;
	AnchorPane loginPage;
	BorderPane tablePage;
	TableView<?> vaccineTable;

	private final static Logger LOGGER = Logger.getLogger(MainApp.class);
	/*static {
		new DOMConfigurator().doConfigure("log4j.xml", LogManager.getLoggerRepository());
	}*/

	public MainApp() {
	}

	@Override
	public void start(Stage primaryStage) {
		LOGGER.debug(" method Start");
		this.primaryStage = primaryStage;
		primaryStage.setMaximized(true);
		this.primaryStage.setTitle("MedApp");
		this.primaryStage.getIcons().add(new Image("/images/address_book_32.png"));
		Lang langRU = new Lang(0, "ru", "Русский", LocaleManager.RU_LOCALE);
		Lang langUK = new Lang(1, "uk", "Украинский", LocaleManager.UA_LOCALE);
		LOGGER.info("locale was added,default UK");
		LocaleManager.setCurrentLang(langUK);
		LOGGER.info("Current Lang " + langUK);
		authintication(false);

	}

	public void authintication(boolean authinticated) {
		LOGGER.debug(" method Authintication");
		LOGGER.debug("Authintication = " + authinticated);

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
			loginPage = loader.load();
			LOGGER.debug("Load " + loader.getLocation());
			Scene scene = new Scene(loginPage);
			primaryStage.setScene(scene);
			loginController = loader.getController();
			LOGGER.debug("Pass main object to loginController ");
			loginController.setMainApp(this);
			LOGGER.info("Show login page ");
			primaryStage.show();
		} catch (IOException ex) {
			LOGGER.error(ex);
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
			LOGGER.debug("Load " + loader.getLocation());
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			rootController = loader.getController();
			LOGGER.debug("Pass main object to rootController ");
			rootController.setMainApp(this);
			primaryStage.show();
			LOGGER.info("Show Root header ");

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
			LOGGER.info("method ShowPersonOverview");

			getPrimaryStage().setFullScreen(true);

			FXMLLoader loader = new FXMLLoader();
			loader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, locale));
			loader.setLocation(MainApp.class.getResource("/view/PersonOverview.fxml"));
			personOverview = loader.load();
			LOGGER.debug("Load " + loader.getLocation());
			rootLayout.setCenter(personOverview);
			personController = loader.getController();
			personController.addObserver(this);
			LOGGER.info(" Add observer for person controller ");
			LOGGER.debug("Pass main object to personController ");
			personController.setMainApp(this);

		} catch (IOException e) {
			LOGGER.error(e);
			e.printStackTrace();
		}
	}

	public void showVaccinationTables(Locale locale, String resource , Person person ) {
		try {
			LOGGER.info("method ShowVaccination Tables");
			LOGGER.info("Locale " + locale.toString() + "Resource " + resource);

			this.primaryStage.setTitle("MedApp");
			getPrimaryStage().setFullScreen(true);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/VaccinationTables.fxml"));
			loader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, locale));
			tablePage = loader.load();
			LOGGER.debug("Load " + loader.getLocation());
			rootLayout.setCenter(tablePage);

			FXMLLoader tableLoader = new FXMLLoader();
			tableLoader.setLocation(MainApp.class.getResource(resource));
			tableLoader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, locale));
			vaccineTable = tableLoader.load();
			LOGGER.debug("Load " + loader.getLocation());
			tablePage.setCenter(vaccineTable);
			vaccineController = loader.getController();
			LOGGER.debug("Pass main object to vaccineController ");
			vaccineController.setMainApp(this);
			vaccineController.setSelectedPerson(person);
		} catch (IOException ex) {
			ex.printStackTrace();
			LOGGER.error(ex);
		}
	}

	public boolean showPersonEditDialog(Person person) throws ParseException {
		try {
			LOGGER.info("method showPersonEditDialogl");

			getPrimaryStage().setFullScreen(true);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/PersonEditDialog.fxml"));
			editPersonPage = loader.load();
			LOGGER.debug("Load " + loader.getLocation());

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
	public boolean showTableVaccineEditDialog(VaccineEntity vaccine ,String resource) throws ParseException {
		try {
			LOGGER.info("method showTableVaccineEditDialog");

		//	getPrimaryStage().setFullScreen(true);

			FXMLLoader loader = new FXMLLoader();
			loader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, LocaleManager.UA_LOCALE));

			loader.setLocation(MainApp.class.getResource(resource));
			editTablePage = loader.load();
			LOGGER.debug("Load " + loader.getLocation());

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Person");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(editTablePage);
			dialogStage.setScene(scene);
			vaccineEditController = loader.getController();
			vaccineEditController.setDialogStage(dialogStage);
			LOGGER.debug("set vaccine " );

			vaccineEditController.setFisrtTwoTable(vaccine);
			//dialogStage.getIcons().add(new Image("/images/edit.png"));
			dialogStage.showAndWait();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public void update(Observable o, Object arg) {
		LOGGER.info("method update");

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
		LOGGER.info("CurrentLang " + lang);
		LOGGER.info("Locale " + current);
		LOGGER.info("init main controllers with new locale");
		initRootLayout(current);
		showPersonOverview(current);

	}

	public File getPersonFilePath() {
		LOGGER.info("method getPersonFilePath");

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
		LOGGER.info("method setPersonFilePath");

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
