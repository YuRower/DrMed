package processing;

import java.io.File;

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
	private static ObservableList<VaccineEntity> currentVaccinePerson = FXCollections.observableArrayList();
	private static ObservableList<VaccineEntity> allVaccinesPersons = FXCollections.observableArrayList();

	public static ObservableList<VaccineEntity> getAllVaccinesPersons() {
		return allVaccinesPersons;
	}

	private static int vaccineToUser = -1;
	private static final File XML_FILE = new File("xmlFile//vaccineInfo.xml");

	public ObservableList<VaccineEntity> getCurrentVaccinePerson() {
		return currentVaccinePerson;
	}

	public ObservableList<VaccineEntity> deleteVaccineFromXMLStrorage(VaccineEntity toDelete) {
		LOGGER.info("method deleteVaccineFromXMLStrorage for" + toDelete);

		try {
			JAXBContext context = JAXBContext.newInstance(VaccineWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			VaccineWrapper wrapper = (VaccineWrapper) um.unmarshal(XML_FILE);
			wrapper.getListVaccines().remove(toDelete);
			allVaccinesPersons.remove(toDelete);

			LOGGER.info("deleteVaccineFromXMLStrorage" + wrapper.getListVaccines());
			LOGGER.info("deleteVaccineFromXMLStrorage" + allVaccinesPersons);
			allVaccinesPersons = updateXMLfile();

		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + XML_FILE.getPath());
			alert.showAndWait();
			LOGGER.info(e.getMessage());
		}
		return allVaccinesPersons;
	}

	public ObservableList<VaccineEntity> updateXMLfile() {
		LOGGER.info("updateXMLfile");
		try {
			JAXBContext context = JAXBContext.newInstance(VaccineWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			VaccineWrapper wrapper = new VaccineWrapper();
			wrapper.setListVaccines(allVaccinesPersons);
			m.marshal(wrapper, XML_FILE);
		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + XML_FILE);
			alert.showAndWait();
			LOGGER.info(e.getMessage());
		}

		return allVaccinesPersons;
	}

	public void loadPersonDataFromFile(File file) {
		LOGGER.info("method loadPersonDataFromFile" + vaccineToUser);

		try {
			JAXBContext context = JAXBContext.newInstance(VaccineWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			// Чтение XML из файла и демаршализация.
			VaccineWrapper wrapper = (VaccineWrapper) um.unmarshal(file);
			allVaccinesPersons.clear();
			allVaccinesPersons.addAll(wrapper.getListVaccines());
			currentVaccinePerson.clear();
			for (VaccineEntity ve : wrapper.getListVaccines()) {
				if (ve.getId() == vaccineToUser) {
					currentVaccinePerson.add(ve);
					LOGGER.info(ve);

				}
			}
			LOGGER.info(wrapper.getListVaccines());
		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
			LOGGER.info(e.getMessage());
		}
	}

	public void savePersonDataToFile(File xmlFile, VaccineEntity vaccine) {

		try {
			JAXBContext context = JAXBContext.newInstance(VaccineWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			LOGGER.info(currentVaccinePerson);

			loadPersonDataFromFile(xmlFile);

			LOGGER.info(currentVaccinePerson);
			VaccineWrapper wrapper = new VaccineWrapper();
			wrapper.setListVaccines(allVaccinesPersons);
			wrapper.getListVaccines().add(vaccine);

			LOGGER.info(currentVaccinePerson);
			LOGGER.info(allVaccinesPersons);

			m.marshal(wrapper, xmlFile);

		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + xmlFile);

			alert.showAndWait();
			LOGGER.info(e.getMessage());
		}
	}

	public void setCurrentUser(int id) {
		LOGGER.info("setCurrentUser" + id);
		vaccineToUser = id;
	}
}
