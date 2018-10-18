package view.vaccination;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.vaccine.VaccineEntity;
import view.VaccineController;

public class VaccineTableController {
	private final static Logger LOGGER = Logger.getLogger(VaccineTableController.class);
    private ObservableList<VaccineEntity> vaccineData = FXCollections.observableArrayList();

		@FXML
	    private TableView<VaccineEntity> vaccineTable;
	    @FXML
	    private TableColumn<VaccineEntity, String> firstNameColumn;
	    @FXML
	    private TableColumn<VaccineEntity, String> lastNameColumn;

	    private VaccineController mainApp;

	    public VaccineTableController() {
	    	LOGGER.info("VaccineTableController");
	    }

	   
	    @FXML
	    private void initialize() {
	    	vaccineData.add(new VaccineEntity(1,"test","test"));
			vaccineData.add(new VaccineEntity(2,"test1","test1"));
	    	vaccineTable.setItems(vaccineData);
	    	LOGGER.info("initialize//////////////////////////////////////////");
	    	
	        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeVaccine());
	        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());

	    }

	  
	    public void setMainApp(VaccineController mainApp) {
	        this.mainApp = mainApp;

	        vaccineTable.setItems(mainApp.getVaccineData());
	    }
	}

