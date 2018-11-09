package view;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.ParseException;
import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import application.MainApp;
import exception.ApplicationException;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;

import model.Classes;
import model.Lang;
import model.Person;
import model.manager.ClassesManager;
import model.manager.DialogManager;
import model.manager.LocaleManager;
import processing.LoadExcel;
import processing.DAO.SchoolDAO;
import processing.DAO.VaccinationTypeDAO;
import util.FileDocxGenerator;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PersonOverviewController extends Observable implements Initializable {
	@FXML
	private TableView<Person> personTable;
	@FXML
	private TableColumn<Person, String> firstNameColumn;
	@FXML
	private TableColumn<Person, String> lastNameColumn;

	@FXML
	private Label firstNameLabel;
	@FXML
	private Label lastNameLabel;
	@FXML
	private Label patronymicLabel;
	@FXML
	private Label streetLabel;
	@FXML
	private Label postalCodeLabel;
	@FXML
	private Label phoneNumberLabel;
	@FXML
	private Label birthdayLabel;

	@FXML
	public ComboBox<Lang> comboLocales;

	@FXML
	private CustomTextField txtSearch;
	@FXML
	public ComboBox<Classes> comboClass;

	public ComboBox<Classes> getComboClass() {
		return comboClass;
	}

	private static ObservableList<Classes> comboClasslist;

	public static ObservableList<Classes> getComboClasslist() {
		return comboClasslist;
	}

	private static Locale locale = LocaleManager.getCurrentLang().getLocale();
	static ResourceBundle rb = ResourceBundle.getBundle(LocaleManager.BUNDLES_FOLDER, locale);

	private MainApp mainApp;

	@FXML
	private Label labelCount;

	private ResourceBundle resourceBundle;

	private static final String RU_CODE = "ru";
	private static final String UA_CODE = "uk";
	private final static Logger LOGGER = Logger.getLogger(PersonOverviewController.class);
	ObservableList<Person> updatedClass = null;

	@FXML
	public void generDOCX() throws IOException, ParseException, ApplicationException {
		LOGGER.debug("generDOCX");
		FileDocxGenerator doc = new FileDocxGenerator();
		doc.generateDOCX(mainApp, personTable, this);
	}

	@FXML
	public void sortPerson() {
		FilteredList<Person> filteredData = new FilteredList<>(
				LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()), p -> true);

		txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(person -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				if (person.getFirstName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				} else if (person.getLastName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				return false;
			});
		});

		SortedList<Person> sortedData = new SortedList<>(filteredData);
		LOGGER.info(sortedData + "////sortedData///////////");
		sortedData.comparatorProperty().bind(personTable.comparatorProperty());
		personTable.setItems(sortedData);

	}

	private void setupClearButtonField(CustomTextField customTextField) {
		try {
			Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class,
					ObjectProperty.class);
			m.setAccessible(true);
			m.invoke(null, customTextField, customTextField.rightProperty());
		} catch (Exception e) {
			LOGGER.error(e.getStackTrace());
		}
	}

	private void fillLangComboBox() {
		Lang langRU = new Lang(0, RU_CODE, resourceBundle.getString("ru"), LocaleManager.RU_LOCALE);
		Lang langUK = new Lang(1, UA_CODE, resourceBundle.getString("uk"), LocaleManager.UA_LOCALE);

		comboLocales.getItems().add(langRU);
		comboLocales.getItems().add(langUK);
		LOGGER.info("fill locale into box ");
		updateCountLabel();
		if (LocaleManager.getCurrentLang() == null) {

			comboLocales.getSelectionModel().select(0);
		} else {
			comboLocales.getSelectionModel().select(LocaleManager.getCurrentLang().getIndex());
		}
	}

	class LanguageListCell extends ListCell<Locale> {
		@Override
		protected void updateItem(Locale item, boolean empty) {
			super.updateItem(item, empty);
			if (item != null) {
				setText(item.getDisplayLanguage());
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.resourceBundle = resources;
		LOGGER.info("////initialize///////////" + resources.getLocale().getLanguage());

		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
		showPersonDetails(null);
		personTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
		setupClearButtonField(txtSearch);

		initListeners();
		fillLangComboBox();
		fillComboBox();
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		if (LoadExcel.getOuter() != null) {
			LOGGER.info(LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()));
			personTable.setItems(LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()));
		}
	}

	public void showPersonDetails(Person person) {
		if (person != null) {
			firstNameLabel.setText(person.getFirstName());
			lastNameLabel.setText(person.getLastName());
			patronymicLabel.setText(person.getPatronymic());
			streetLabel.setText(person.getStreet());
			postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
			phoneNumberLabel.setText(person.getPhoneNumber());
			birthdayLabel.setText((person.getBirthday()));
		} else {
			firstNameLabel.setText("");
			lastNameLabel.setText("");
			patronymicLabel.setText("");
			streetLabel.setText("");
			postalCodeLabel.setText("");
			phoneNumberLabel.setText("");
			birthdayLabel.setText("");
		}
	}

	private void initListeners() {
		SchoolDAO schoolStorage = new SchoolDAO();

		SchoolDAO.getPersonData().addListener(new ListChangeListener<Person>() {
			@Override
			public void onChanged(Change<? extends Person> c) {
				updateCountLabel();
				fillComboBox();
			}
		});

		personTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {
					try {
						handleEditPerson();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

			}
		});
		comboClass.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				LOGGER.info("comboClass.setOnAction");

				Classes selectedClass = comboClass.getSelectionModel().getSelectedItem();
				if (selectedClass != null) {
					int index = selectedClass.getIndex();

					ClassesManager.setCurrentClass(selectedClass);
					ClassesManager.setCurrentIndex(index);

					String str = String.valueOf(selectedClass);
					LOGGER.info(str + "////selectedClass///////////");
					try {
						updatedClass = schoolStorage.update(ClassesManager.getCurrentIndex());
					} catch (IOException e) {
						LOGGER.error(e.fillInStackTrace());
					}
					personTable.setItems(updatedClass);
					updateCountLabel();
				} else {
					LOGGER.info("////selectedClass is null ///////");

				}
			}

		});

		comboLocales.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				LOGGER.info("combo Locales listener");
				Lang selectedLang = comboLocales.getSelectionModel().getSelectedItem();
				LocaleManager.setCurrentLang(selectedLang);
				LOGGER.info("selected lang " + selectedLang);
				setChanged();
				notifyObservers(selectedLang);
			}
		});

	}

	public void clearCombobox() {
		comboClass.getItems().removeAll(comboClass.getItems());
	}

	private void fillComboBox() {
		clearCombobox();
		comboClasslist = LoadExcel.getClassList();
		LOGGER.debug(comboClasslist + "boxOfClasses");
		comboClass.getItems().addAll(comboClasslist);
		if (ClassesManager.getCurrentClass() == null) {
			comboClass.getSelectionModel().select(0);
		} else {
			comboClass.getSelectionModel().select(ClassesManager.getCurrentClass().getIndex());
		}
	}

	private void updateCountLabel() {
		if (LoadExcel.getOuter() != null) {
			if (!LoadExcel.getOuter().isEmpty()) {
				LOGGER.info(LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()).size());
				labelCount.setText(resourceBundle.getString("count") + ": "
						+ LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()).size());
			} else {
				labelCount.setText(resourceBundle.getString("count") + ": " + 0);
			}
		} else {
			LOGGER.info("null");

		}
	}

	@FXML
	private void handleNewPerson() throws ParseException {
		if (LoadExcel.getOuter() != null) {

			Person tempPerson = new Person();
			boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
			LOGGER.debug(okClicked + "okClicked");
			if (okClicked) {
				LOGGER.debug(tempPerson + "tempPerson");
				LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()).add(tempPerson);
				personTable.setItems(LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()));
				updateCountLabel();

			}
		} else {
			DialogManager.addThroughLoad();
		}
	}

	@FXML
	private void showTables() {
		Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
		if (selectedPerson == null) {
			DialogManager.selectPerson();
		} else {
			String resource = VaccinationTypeDAO.getVaccineList().get(0).getResource();
			LOGGER.debug(resource);
			mainApp.showVaccinationTables(LocaleManager.getCurrentLang().getLocale(), resource, selectedPerson);
			LOGGER.debug("show Tables()");
		}
	}

	@FXML
	private void handleDeletePerson() throws IOException {
	
		int selectedIndex = personTable.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {

			ButtonType checkUserInputOnDelete = DialogManager.wantToDelete();
			if (checkUserInputOnDelete == ButtonType.OK) {
				SchoolDAO schoolStorage = new SchoolDAO();
				updatedClass = schoolStorage.update(ClassesManager.getCurrentIndex());
				personTable.setItems(updatedClass);
				LOGGER.info("handleDeletePerson");
				LOGGER.info(selectedIndex);
				LOGGER.info(LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()).size());

				personTable.getItems().remove(selectedIndex);
				LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()).remove(selectedIndex);
				updateCountLabel();

			} else {
				LOGGER.info("Cancel");
			}
		} else {
			DialogManager.deleteEditPerson();
			
		}
	}

	@FXML
	private void handleEditPerson() throws ParseException {
		Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {
			boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
			if (okClicked) {
				showPersonDetails(selectedPerson);
			}
		} else {
			DialogManager.deleteEditPerson();
		}
	}
}