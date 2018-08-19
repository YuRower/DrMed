package view;

import java.io.IOException;
import java.text.ParseException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.Observable;

import org.apache.log4j.Logger;
import org.controlsfx.control.textfield.CustomTextField;

import application.MainApp;
import application.SchoolCollection;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import model.Classes;
import model.Person;
import processing.LoadExcel;

import util.ClassesManager;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PersonOverviewController extends Observable {
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
	private CustomTextField txtSearch;

	@FXML
	public ComboBox<Classes> comboClass;

	private MainApp mainApp;
	SchoolCollection schoolStorage;

	@FXML
	private Label labelCount;
	private final static Logger LOGGER = Logger.getLogger(PersonOverviewController.class);

	public PersonOverviewController() {

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

	@FXML
	private void initialize() {
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
		showPersonDetails(null);
		personTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
		initListeners();
		// fillComboBox();

	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

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
				// comboClass.getSelectionModel().select(0);

				Classes selectedClass = (Classes) comboClass.getSelectionModel().getSelectedItem();
				ClassesManager.setCurrentClass(selectedClass);
				String str = String.valueOf(selectedClass);
				LOGGER.info(str + "////sortedData///////////");
				schoolStorage = new SchoolCollection ();
				try {
					schoolStorage.update(str);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				LOGGER.info(selectedClass + "////sortedData///////////");

				// setChanged();
				// notifyObservers(selectedClass);
			}
		});

	}

	private void fillComboBox() {
		ObservableList<Classes> liist = LoadExcel.classList;
		LOGGER.debug(liist + "(states.toString(");

		comboClass.getItems().addAll(liist);

		if (ClassesManager.getCurrentClass() == null) {

			comboClass.getSelectionModel().select(0);
		} else {
			comboClass.getSelectionModel().select(ClassesManager.getCurrentClass().getSchoolClass());
		}
	}

	private void updateCountLabel() {
		labelCount.setText("count" + ": " + SchoolCollection.getPersonData().size());
		// fillComboBox();
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
}