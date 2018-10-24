package view.vaccination;

import java.io.File;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.vaccine.VaccineEntity;
import processing.XMLProcessing;
import view.VaccineController;

public class VaccineTableController {
	private final static Logger LOGGER = Logger.getLogger(VaccineTableController.class);
	private static final String XML_FILE = "xmlFile//vaccineInfo.xml";

		@FXML
	    private TableView<VaccineEntity> vaccineTable;
	    @FXML
	    private TableColumn<VaccineEntity, String> firstNameColumn;
	    @FXML
	    private TableColumn<VaccineEntity, String> lastNameColumn;

	    private VaccineController mainApp;

	    public VaccineTableController() {
	    	LOGGER.info("VaccineTableController");
	   
			
			//LOGGER.info(xmlFile.getVaccineData().get(0).getMedicalContradication()+" getVaccineData");

	    }

	   
	    
	    public void init() {
	     	XMLProcessing xmlFile = new XMLProcessing();
			xmlFile.loadPersonDataFromFile(new File(XML_FILE));
			LOGGER.info(xmlFile.getVaccineData()+"getVaccineData");
	    	ObservableList<VaccineEntity> list = xmlFile.getVaccineData();
			LOGGER.info(list+"getVaccineData");

	    	vaccineTable.setItems(list);
	    	LOGGER.info("initialize//////////////////////////////////////////");
	    	
	    	
	        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().typeVaccineProperty());
	        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

	    }

	  
	  /*  public void setMainApp(VaccineController mainApp) {
	        this.mainApp = mainApp;

	        vaccineTable.setItems(mainApp.getVaccineData());
	    }*/
	}

