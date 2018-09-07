package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;

import application.MainApp;
import application.SchoolCollection;
import javafx.beans.property.ObjectProperty;
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
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import model.Classes;
import model.Lang;
import model.Person;
import processing.GenerateDocx;
import processing.LoadExcel;
import processing.Report;
import processing.Status;
import util.ClassesManager;
import util.DialogManager;
import util.LocaleManager;
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
	GenerateDocx rg;
	@FXML
	public ComboBox<Classes> comboClass;

	public ComboBox<Classes> getComboClass() {
		return comboClass;
	}

	private static ObservableList<Classes> comboClasslist;

	public static ObservableList<Classes> getComboClasslist() {
		return comboClasslist;
	}

	private MainApp mainApp;
	SchoolCollection schoolStorage = new SchoolCollection() ;

	@FXML
	private Label labelCount;
	private ResourceBundle resourceBundle;

	private static final String RU_CODE = "ru";
	private static final String UA_CODE = "uk";
	private final static Logger LOGGER = Logger.getLogger(PersonOverviewController.class);
	ObservableList<Person> updatedClass = null;

	public PersonOverviewController() {

	}

	@FXML

	public void generDOCX() throws IOException, ParseException {

		rg = new GenerateDocx();
		int someIndex = 0;
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter docFilter = new FileChooser.ExtensionFilter("DOC files (*.doc", "*.doc");
		FileChooser.ExtensionFilter docxFilter = new FileChooser.ExtensionFilter("DOCX files (*.docx", "*.docx");
		fileChooser.getExtensionFilters().add(docxFilter);

		fileChooser.getExtensionFilters().add(docFilter);

		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		File parentFile = file.getParentFile();
		
		if (file != null) {
			LOGGER.debug("Load " + file.getPath() + "Load doc ");
		}
		ObservableList<Person> listGen = LoadExcel.getOuter().get(ClassesManager.getCurrentIndex());
		Map<String, Object> map = new HashMap<String, Object>();
		String fileName;
		String extension = null;// file.getName();
		int pos = file.getName().lastIndexOf(".");
		if (pos == -1) {
			fileName = file.getName();
		} else {
			fileName = file.getName().substring(0, pos);
			extension = file.getName().substring(pos, file.getName().length());
			LOGGER.debug(fileName + " -- " + extension);

		}
		Report report = DialogManager.showOptionalDOCX();

		if (report == Report.ONE) {
			DialogManager.selectPerson(" 1 ", " select person");
			LOGGER.info(report);
			Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
			if (selectedPerson != null) {
				boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
				if (okClicked) {
					showPersonDetails(selectedPerson);
				}
			} else if (selectedPerson == null) {
				DialogManager.selectPerson(" 1 ", " please select person");
				return;
			}
			LOGGER.info(fileName + selectedPerson.getLastName() + extension + " " + file.getPath());
			File temp = createDocFile(fileName + selectedPerson.getLastName() + ++someIndex + extension);
			copyFileUsingStream(file, temp);
			sentInfo(map, selectedPerson, file,parentFile);

		} else if (report == Report.MANY) {

			LOGGER.info(report);
			for (Person person : listGen) {

				File temp = createDocFile(fileName + person.getLastName() + ++someIndex + extension);
				copyFileUsingStream(file, temp);

				sentInfo(map, person, file,parentFile);

			}
		}

	}

	public void sentInfo(Map map, Person person, File file,File parentFile) {
		LOGGER.debug("Load  sentInfo ");
		LOGGER.debug("Load  sentInfo "+ parentFile);

		String title[] = new String[] { "FirstName", "LastName", "Street", "PostalCode", "City", "Birthday" };

		map.put(title[0], person.getFirstName());
		map.put(title[1], person.getLastName());
		map.put(title[2], person.getStreet());
		map.put(title[3], person.getPostalCode());
		map.put(title[4], person.getCity());
		map.put(title[5], person.getBirthday());

		LOGGER.debug(map);
		boolean flag = rg.generateAndSendDocx("\\"+file.getName(), map,parentFile.getAbsolutePath());
		LOGGER.debug("Load " + flag + "Load doc ");
		LOGGER.debug("Load  sentInfo succesefully");

	}

	public static File createDocFile(String fileName) {
		LOGGER.debug("Create");
		try {

			File file = new File(fileName);
			LOGGER.debug("file" + file);

			FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
			XWPFDocument doc = new XWPFDocument();
			doc.write(fos);
			fos.close();
			LOGGER.debug(file.getAbsolutePath());
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private static void copyFileUsingStream(File source, File dest) throws IOException {
		LOGGER.debug("copy");
		LOGGER.debug(source + "copy" + dest);

		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
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
	 private void setupClearButtonField(CustomTextField customTextField) {
	        try {
	            Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class, ObjectProperty.class);
	            m.setAccessible(true);
	            m.invoke(null, customTextField, customTextField.rightProperty());
	        }catch (Exception e){
	            e.printStackTrace();
	        }
	    }

	private void fillLangComboBox() {
		Lang langRU = new Lang(0, RU_CODE, resourceBundle.getString("ru"), LocaleManager.RU_LOCALE);
		Lang langUK = new Lang(1, UA_CODE, resourceBundle.getString("uk"), LocaleManager.UA_LOCALE);

		comboLocales.getItems().add(langRU);
		comboLocales.getItems().add(langUK);
		LOGGER.info("fill locale into box ");

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
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	//	LOGGER.info(LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()));
		personTable.setItems(LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()));
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
		LOGGER.info("handleDeletePerson");
		LOGGER.info(selectedIndex);

		if (selectedIndex >= 0) {
	        personTable.getItems().remove(selectedIndex);
			LOGGER.info(LoadExcel.getOuter());

			//personTable.getItems().remove(LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()).get(selectedIndex));
			LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()).remove(selectedIndex);
			LOGGER.info(LoadExcel.getOuter());
			updateCountLabel();

		//	SchoolCollection.getPersonData().remove(selectedIndex);
			
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
				LOGGER.info("error");
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

				Classes selectedClass = (Classes) comboClass.getSelectionModel().getSelectedItem();
				int index = selectedClass.getIndex();
				LOGGER.info(index + "////sortedData///////////");

				ClassesManager.setCurrentClass(selectedClass);
				ClassesManager.setCurrentIndex(index);

				String str = String.valueOf(selectedClass);
				LOGGER.info(str + "////sortedData///////////");
				//schoolStorage = new SchoolCollection();
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
				LOGGER.info("combo Locales listener");

				Lang selectedLang = (Lang) comboLocales.getSelectionModel().getSelectedItem();

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
		LOGGER.info(LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()).size());
		labelCount.setText(resourceBundle.getString("count") + ": " + LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()).size());
	}

	@FXML
	private void handleNewPerson() throws ParseException {
		Person tempPerson = new Person();
		boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
		LOGGER.debug(okClicked + "okClicked");

		if (okClicked) {
			SchoolCollection.getPersonData().add(tempPerson);
			LOGGER.debug(tempPerson + "tempPerson");

			LoadExcel.getOuter().get(ClassesManager.getCurrentIndex()).add(tempPerson);
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