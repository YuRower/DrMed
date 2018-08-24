package view;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.controlsfx.control.textfield.CustomTextField;

import application.MainApp;
import application.SchoolCollection;
import javafx.collections.FXCollections;
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
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import model.Classes;
import model.Lang;
import model.Person;
import processing.LoadExcel;
import processing.ReportGenerator;

import util.ClassesManager;
import util.LocaleManager;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
	private Label streetLabel;
	@FXML
	private Label postalCodeLabel;
	@FXML
	private Label cityLabel;
	@FXML
	private Label birthdayLabel;

	@FXML
	public ComboBox comboLocales;

	@FXML
	private CustomTextField txtSearch;
	ReportGenerator rg;
	@FXML
	public ComboBox<Classes>  comboClass;
	public ComboBox<Classes> getComboClass() {
		return comboClass;
	}

	private static ObservableList<Classes> comboClasslist ;
	public static ObservableList<Classes> getComboClasslist() {
		return comboClasslist;
	}

	private MainApp mainApp;
	SchoolCollection schoolStorage;

	@FXML
	private Label labelCount;
	private ResourceBundle resourceBundle;

	private static final String RU_CODE = "ru";
	private static final String UA_CODE = "uk";
	private final static Logger LOGGER = Logger.getLogger(PersonOverviewController.class);

	public PersonOverviewController() {

	}

	@FXML

	public void generDOCX() {
		rg = new ReportGenerator();
		Map<String, String> map = new HashMap<String, String>();
		map.put("Key", "test");
		map.put("Key1", "test2");

		rg.generateAndSendDocx("063-O.docx", map);

	}

	@FXML
	public void sortPerson() {
		FilteredList<Person> filteredData = new FilteredList<>(SchoolCollection.getPersonData(), p -> true);

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

	private void fillLangComboBox() {
		Lang langRU = new Lang(0, RU_CODE, resourceBundle.getString("ru"), LocaleManager.RU_LOCALE);
		Lang langUK = new Lang(1, UA_CODE, resourceBundle.getString("uk"), LocaleManager.UA_LOCALE);

		comboLocales.getItems().add(langRU);
		comboLocales.getItems().add(langUK);
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
		LOGGER.info("////initialize///////////"+resources);

		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
		showPersonDetails(null);
		personTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
		initListeners();
		fillLangComboBox();
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		LOGGER.info(SchoolCollection.getPersonData());
		personTable.setItems(SchoolCollection.getPersonData());
	}

	private void showPersonDetails(Person person) {
		if (person != null) {
			firstNameLabel.setText(person.getFirstName());
			lastNameLabel.setText(person.getLastName());
			streetLabel.setText(person.getStreet());
			postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
			cityLabel.setText(person.getCity());
			birthdayLabel.setText((person.getBirthday()));
		} else {
			firstNameLabel.setText("");
			lastNameLabel.setText("");
			streetLabel.setText("");
			postalCodeLabel.setText("");
			cityLabel.setText("");
			birthdayLabel.setText("");
		}
	}

	@FXML
	private void handleDeletePerson() {
		int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			personTable.getItems().remove(selectedIndex);
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Person Selected");
			alert.setContentText("Please select a person in the table.");
			alert.showAndWait();
		}
	}

	private void initListeners() {

		SchoolCollection.getPersonData().addListener(new ListChangeListener<Person>() {
			@Override
			public void onChanged(Change<? extends Person> c) {
				updateCountLabel();
				fillComboBox();
				// fillLangComboBox();
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
				Classes selectedClass = (Classes) comboClass.getSelectionModel().getSelectedItem();
				int index = selectedClass.getIndex();
				LOGGER.info(index + "////sortedData///////////");

				ClassesManager.setCurrentClass(selectedClass);
				ClassesManager.setCurrentIndex(index);

				String str = String.valueOf(selectedClass);
				LOGGER.info(str + "////sortedData///////////");
				schoolStorage = new SchoolCollection();
				ObservableList<Person> updatedClass = null;
				try {
					updatedClass = schoolStorage.update(ClassesManager.getCurrentIndex());
				} catch (IOException e) {
					e.printStackTrace();
				}

				personTable.setItems(updatedClass);
				updateCountLabel();
			}
		});
		
		 comboLocales.setOnAction(new EventHandler<ActionEvent>() {
	            @Override

	            public void handle(ActionEvent event) {
					LOGGER.info( "////clicl box///////////");

	                Lang selectedLang = (Lang) comboLocales.getSelectionModel().getSelectedItem();

	                LocaleManager.setCurrentLang(selectedLang);

	                setChanged();
	                notifyObservers(selectedLang);
	            }
	        });
		 

	}

	public  void clearCombobox() {
		comboClass.getItems().removeAll(comboClass.getItems());// = new ComboBox();
	}


	private void fillComboBox() {
		clearCombobox();
		 comboClasslist = LoadExcel.getClassList();
		LOGGER.debug(comboClasslist + "(states.toString(");

		comboClass.getItems().addAll(comboClasslist);

		if (ClassesManager.getCurrentClass() == null) {

			comboClass.getSelectionModel().select(0);
		} else {
			comboClass.getSelectionModel().select(ClassesManager.getCurrentClass().getIndex());
		}
	}

	private void updateCountLabel() {
		labelCount.setText(resourceBundle.getString("count") + ": " + SchoolCollection.getPersonData().size());
	}

	@FXML
	private void handleNewPerson() throws ParseException {
		Person tempPerson = new Person();
		boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
		if (okClicked) {
			SchoolCollection.getPersonData().add(tempPerson);
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
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Person Selected");
			alert.setContentText("Please select a person in the table.");

			alert.showAndWait();
		}
	}

	public void updateTable(Classes customer) {
		LOGGER.debug(customer + "1 ");
		// List<Person> list = customer.getClassListData();
		// LOGGER.debug(list + "1 ");
		// initialize();
		LOGGER.debug(personTable + "1 ");
		/*
		 * personTable.getSelectionModel().selectedItemProperty()
		 * .addListener((observable, oldValue, newValue) ->
		 * showPersonDetails(newValue));
		 */
		LOGGER.debug(personTable + "1 ");
		personTable = new TableView<Person>();
		LOGGER.debug(personTable + "1 ");
		// initListeners();
		LOGGER.debug(personTable + "1 ");

		// personTable.setItems(list);

	}
}