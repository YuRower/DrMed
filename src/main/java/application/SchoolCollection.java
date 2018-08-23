package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Classes;
import model.Person;
import processing.LoadExcel;
import processing.Status;
import processing.WriteExcel;
import util.DialogManager;
import view.BirthdayStatisticsController;
import view.PersonOverviewController;
import view.RootLayoutController;

public class SchoolCollection {
	private static ObservableList<Person> personData = FXCollections.observableArrayList();

	private final static Logger LOGGER = Logger.getLogger(SchoolCollection.class);
	MainApp app ;
	WriteExcel write;
	LoadExcel load;
	public static ObservableList<Person> getPersonData() {
		return personData;
	}
	List<Person> listPerson ;
	/*public void showBirthdayStatistics() {
		try {
			app= new MainApp();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/BirthdayStatistics.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			LOGGER.info("Load BirthdayStatistics.fxml");

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Birthday Statistics");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(app.getPrimaryStage());
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			dialogStage.getIcons().add(new Image("/images/calendar.png"));

			BirthdayStatisticsController controller = loader.getController();
			controller.setPersonData(personData);

			dialogStage.show();

		} catch (IOException e) {
			LOGGER.error(e);
		}
	}*/

	public void commonFactoryMethod(File file, Status marker) {
		try {
			LOGGER.info("File location " + file + "Status" + marker);
			
			if (marker == Status.SAVE) {
				write = new WriteExcel();
				write.writeToExcel(file);
				LOGGER.debug("Write file " + file + " to " + file.getAbsolutePath());

			} else if (marker == Status.LOAD) {
				load = new LoadExcel();
				listPerson = load.readBooksFromExcelFile(file,null);
				if (listPerson== null) {
					return;
				}
				LOGGER.debug("Read from file " + file + "  " + file.getAbsolutePath());
				personData.addAll(listPerson);
				LOGGER.info("added  list " + personData);
				
			} else {
				LOGGER.error("file not found");
				throw new RuntimeException();
			}
		} catch (Exception e) {
			LOGGER.error(e);
			e.printStackTrace();
			DialogManager.showErrorDialog("Error", "Could not load data from file:\n" + file.getPath());

		}
	}

	/*public void update(String str) {
		// TODO Auto-generated method stub
		
	}*/

	public List<Person> getListPerson() {
		return listPerson;
	}
	

	public ObservableList<Person> update(int sheet) throws IOException {
		ArrayList<Person> currClass = LoadExcel.outer.get(sheet);
		ObservableList<Person> personWrapper = FXCollections.observableArrayList(currClass);
		return personWrapper;
		 


	//	int index=Collections.indexOfSubList(LoadExcel.classList, Classes.classListData);
		// LOGGER.debug(index+ " " );
		


		//load.readBooksFromExcelFile(file,sheet);
	//	LOGGER.debug("update  file " + load.readBooksFromExcelFile(file,sheet));
		//List<Person> app1 = getListPerson();
		//LOGGER.debug( getListPerson());

	//	LOGGER.debug(LoadExcel.classList+ " " );
	//	LOGGER.debug(Classes.classListData+ " " );

		
	//	app.showPersonOverview();

		/*app= new MainApp();

		app.initRootLayout();
		app.showPersonOverview();*/
	    //PersonOverviewController person = new PersonOverviewController();


				 //   person.updateTable(customer);

		        
		        
	//	File file = RootLayoutController.currentFile;
	//	load = new LoadExcel();
		
		    
		         
		   
		    
	}
	
}
