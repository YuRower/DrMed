package processing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.vaccine.VaccineEntity;
import model.wrapper.VaccineWrapper;

public class XMLProcessing {
	private final static Logger LOGGER = Logger.getLogger(XMLProcessing.class);
    private ObservableList<VaccineEntity> vaccineData = FXCollections.observableArrayList();
	

	/**
	 * @return the vaccineData
	 */
	public ObservableList<VaccineEntity> getVaccineData() {
		return vaccineData;
	}

	/**
	 * Загружает информацию об адресатах из указанного файла. Текущая информация об
	 * адресатах будет заменена.
	 * 
	 * @param file
	 */
	public void loadPersonDataFromFile(File file) {
		LOGGER.info("loadPersonDataFromFile");
		try {
			JAXBContext context = JAXBContext.newInstance(VaccineWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			// Чтение XML из файла и демаршализация.
			VaccineWrapper wrapper = (VaccineWrapper) um.unmarshal(file);

			vaccineData.clear();
			vaccineData.addAll(wrapper.getListVaccines());

		

		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
			e.printStackTrace();

		}
	}

	/**
	 * Сохраняет текущую информацию об адресатах в указанном файле.
	 * 
	 * @param xmlFile
	 */
	public void savePersonDataToFile(String xmlFile ,VaccineEntity vaccine) {
		LOGGER.info("savePersonDataToFile");

		try {
			JAXBContext context = JAXBContext.newInstance(VaccineWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Обёртываем наши данные об адресатах.
			VaccineWrapper wrapper = new VaccineWrapper();
			wrapper.setListVaccines(vaccineData);
			wrapper.getListVaccines().add(new VaccineEntity());

			LOGGER.info(vaccineData);

			// Маршаллируем и сохраняем XML в файл.
			m.marshal(wrapper, new File (xmlFile));

			
		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + xmlFile);

			alert.showAndWait();
			e.printStackTrace();
		}
	}
}
