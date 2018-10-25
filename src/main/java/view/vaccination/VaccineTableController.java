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

public class VaccineTableController {
	private final static Logger LOGGER = Logger.getLogger(VaccineTableController.class);
	private static final String XML_FILE = "xmlFile//vaccineInfo.xml";

		@FXML
	    private TableView<VaccineEntity> vaccineTable;
	    @FXML
	    private TableColumn<VaccineEntity, String> typeOfVAccineColumn;
	    @FXML
	    private TableColumn<VaccineEntity, String> ageColumn;
	    @FXML
	    private TableColumn<VaccineEntity, String> dataColumn;
	    @FXML
	    private TableColumn<VaccineEntity, Double> dozeColumn;
	    @FXML
	    private TableColumn<VaccineEntity, String> reactionColumn;
	    @FXML
	    private TableColumn<VaccineEntity, String> seriesColumn;
	    @FXML
	    private TableColumn<VaccineEntity, String> medicalContradicationColumn;
	    


	    public VaccineTableController() {
	    	LOGGER.info("VaccineTableController");
	    }

	   
	    @FXML
	    public void initialize() {
	     	XMLProcessing xmlFile = new XMLProcessing();
			xmlFile.loadPersonDataFromFile(new File(XML_FILE));
			LOGGER.info(xmlFile.getVaccineData()+"getVaccineData");

			
	    	vaccineTable.setItems(xmlFile.getVaccineData());
	    	LOGGER.info("initialize//////////////////////////////////////////");
	    	typeOfVAccineColumn.setCellValueFactory(cellData -> cellData.getValue().typeVaccineProperty());
	    	ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty());
	    	dataColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
	    	
	    	dozeColumn.setCellValueFactory(cellData -> cellData.getValue().dozeProperty().asObject());
	    	reactionColumn.setCellValueFactory(cellData -> cellData.getValue().reactionProperty());
	    	seriesColumn.setCellValueFactory(cellData -> cellData.getValue().seriesProperty());
	    	medicalContradicationColumn.setCellValueFactory(cellData -> cellData.getValue().medicalContradicationProperty());

	     

	    }

	  
	  /*  public void setMainApp(VaccineController mainApp) {
	        this.mainApp = mainApp;

	        vaccineTable.setItems(mainApp.getVaccineData());
	    }*/
	}

