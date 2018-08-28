package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import model.Classes;
import model.Lang;
import model.Person;
import processing.GenerateDocx;
import processing.LoadExcel;
import processing.Status;
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
		rg = new GenerateDocx();

		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter docFilter = new FileChooser.ExtensionFilter("DOC files (*.doc", "*.doc");
		FileChooser.ExtensionFilter docxFilter = new FileChooser.ExtensionFilter("DOCX files (*.docx", "*.docx");
		fileChooser.getExtensionFilters().add(docxFilter);

		fileChooser.getExtensionFilters().add(docFilter);

		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

		if (file != null) {
			LOGGER.debug("Load " + file.getPath() + "Load doc ");
		}
		ArrayList<Person> listGen = LoadExcel.getOuter().get(ClassesManager.getCurrentIndex());
		String title[] = new String[] { "FirstName", "LastName", "Street", "PostalCode", "City", "Birthday" };
		Map<String, Object> map = new HashMap<String, Object>();
		String fileName ; 
		String extension = null ;//  file.getName();
        int pos = file.getName().lastIndexOf(".");
        if (pos ==-1  ) {
        	fileName= file.getName();
        }else {
        	fileName = file.getName().substring(0, pos);
        	 extension =  file.getName().substring(pos, file.getName().length());
			LOGGER.debug(fileName +  " -- " + extension);

        }
  
        LOGGER.info(pos);
		for (Person person : listGen) {
		
			File temp =createDocFile(fileName+ person.getLastName()+extension,file.getPath());
		try {
				copyFileUsingStream(file,temp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			/*
			 * map.put(title[0], person.getFirstName()); map.put(title[1],
			 * person.getLastName()); map.put(title[2], person.getStreet());
			 * map.put(title[3], person.getPostalCode()); map.put(title[4],
			 * person.getCity()); map.put(title[5], person.getBirthday());
			 */

			/*
			 * LOGGER.debug(map ); boolean flag = rg.generateAndSendDocx(file.getPath(),
			 * map); LOGGER.debug("Load " + flag + "Load doc ");
			 */

		}

		/*
		 * LOGGER.debug(map ); boolean flag = rg.generateAndSendDocx(file.getPath(),
		 * map); LOGGER.debug("Load " + flag + "Load doc ");
		 */

		// schoolStorage.getPersonData();
		// LoadExcel.getOuter().get(ClassesManager.getCurrentIndex());

	}

	public static File createDocFile(String path,String fileName) {
		LOGGER.debug("Create");
		try {
			File file = new File(path);
			LOGGER.debug("Create" + path);

			FileOutputStream fos = new FileOutputStream(fileName);
			XWPFDocument doc = new XWPFDocument();
			doc.write(fos);
			fos.close();
			LOGGER.debug(file.getAbsolutePath());
			return file ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private static void copyFileUsingStream(File source, File dest) throws IOException {
		LOGGER.debug("copy");
		LOGGER.debug(source + "copy" +dest);

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
}