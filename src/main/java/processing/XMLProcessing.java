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
	private static int vaccineToUser = -1;

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
		LOGGER.info("loadPersonDataFromFile"+ vaccineToUser);

		try {
			JAXBContext context = JAXBContext.newInstance(VaccineWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			// Чтение XML из файла и демаршализация.
			VaccineWrapper wrapper = (VaccineWrapper) um.unmarshal(file);

			vaccineData.clear();
			for (VaccineEntity ve : wrapper.getListVaccines()) {
				if (ve.getId() == vaccineToUser) {
					vaccineData.add(ve);
					LOGGER.info("add-------------------------------------------------------------------------");

				}
			}
			LOGGER.info(wrapper.getListVaccines());

			// vaccineData.addAll(wrapper.getListVaccines());

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
	public void savePersonDataToFile(File xmlFile, VaccineEntity vaccine) {

		try {
			JAXBContext context = JAXBContext.newInstance(VaccineWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			LOGGER.info(vaccineData);

			loadPersonDataFromFile(xmlFile);
			LOGGER.info(vaccineData);

			// Обёртываем наши данные об адресатах.
			VaccineWrapper wrapper = new VaccineWrapper();
			wrapper.setListVaccines(vaccineData);
			wrapper.getListVaccines().add(vaccine);

			LOGGER.info(vaccineData);

			// Маршаллируем и сохраняем XML в файл.
			m.marshal(wrapper, xmlFile);

		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + xmlFile);

			alert.showAndWait();
			e.printStackTrace();
		}
	}

	public void setCurrentUser(int id) {
		LOGGER.info("loadPersonDataFromFile"+ id);

		this.vaccineToUser = id;
		LOGGER.info("loadPersonDataFromFile"+ this.vaccineToUser);

	}
}
