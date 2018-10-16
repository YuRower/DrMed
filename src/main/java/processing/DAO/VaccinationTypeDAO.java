package processing.DAO;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import model.VaccineTypeLocation;

public class VaccinationTypeDAO {
	private final static Logger LOGGER = Logger.getLogger(VaccinationTypeDAO.class);

	public static ObservableList<VaccineTypeLocation> getVaccineList() {
		String tuberculosisResource = "/view/vaccination/Vaccination_against_tuberculosis.fxml";

		String poliomyelitsResource = "/view/vaccination/Vaccination_against_poliomyelitis.fxml";

		String diphtheriaResource = "/view/vaccination/Vaccination_against_diphtheria_pertussis_tetanus.fxml";

		String measlesResource = "/view/vaccination/Vaccination_against_measles.fxml";

		String rubellaResource = "/view/vaccination/Vaccination_against_rubella.fxml";

		String parotitisResource = "/view/vaccination/Tuberculin_tests.fxml";

		String hepatitus_B_Resource = "/view/vaccination/Vaccination_against_hepatitis_B.fxml";
		String tubercilinTestResource = "/view/vaccination/Tuberculin_tests.fxml";
		
		VaccineTypeLocation tuberculosis = new VaccineTypeLocation(0, "Щеплення проти туберкульозу",tuberculosisResource);
		VaccineTypeLocation poliomyelits = new VaccineTypeLocation(1, "Щеплення проти поліомієліту",poliomyelitsResource);
		VaccineTypeLocation diphtheria = new VaccineTypeLocation(2, "Щеплення проти дифтерії, кашлюку, правцю",diphtheriaResource);
		VaccineTypeLocation measles = new VaccineTypeLocation(3, "Щеплення проти кору",measlesResource);
		VaccineTypeLocation rubella = new VaccineTypeLocation(4, "Щеплення проти краснухи",rubellaResource);
		VaccineTypeLocation parotitis = new VaccineTypeLocation(5, "Щеплення проти паротиту",parotitisResource);
		VaccineTypeLocation hepatitusB = new VaccineTypeLocation(6, "Щеплення проти гепатиту В",hepatitus_B_Resource);
		VaccineTypeLocation tubercilinTest = new VaccineTypeLocation(7, "Туберкулінові проби",tubercilinTestResource);

		ObservableList<VaccineTypeLocation> list = FXCollections.observableArrayList(tuberculosis, poliomyelits, diphtheria, measles, rubella, parotitis,
						hepatitusB, tubercilinTest);

		return list;
	}

/*	public static ObservableMap<VaccinationType, String> getMapResourceVaccine() {
		String tuberculosisResource = "/view/vaccination/Vaccination_against_tuberculosis.fxml";

		String poliomyelitsResource = "/view/vaccination/Vaccination_against_poliomyelitis.fxml";

		String diphtheriaResource = "/view/vaccination/Vaccination_against_diphtheria_pertussis_tetanus.fxml";

		String measlesResource = "/view/vaccination/Vaccination_against_measles.fxml";

		String rubellaResource = "/view/vaccination/Vaccination_against_rubella.fxml";

		String parotitisResource = "/view/vaccination/Tuberculin_tests.fxml";

		String hepatitus_B_Resource = "/view/vaccination/Vaccination_against_hepatitis_B.fxml";
		String tubercilinTestResource = "/view/vaccination/Tuberculin_tests.fxml";

		ObservableMap<VaccinationType, String> map = FXCollections.observableHashMap();
		map.put(getVaccineList().get(0), tuberculosisResource);
		map.put(getVaccineList().get(1), poliomyelitsResource);
		map.put(getVaccineList().get(2), diphtheriaResource);
		map.put(getVaccineList().get(3), measlesResource);
		map.put(getVaccineList().get(4), rubellaResource);
		map.put(getVaccineList().get(5), parotitisResource);
		map.put(getVaccineList().get(6), hepatitus_B_Resource);
		map.put(getVaccineList().get(7), tubercilinTestResource);
		for (Map.Entry<VaccinationType, String> testMap : map.entrySet()) {
			LOGGER.info(testMap.getKey() + " = " + testMap.getValue());

		}

		return map;
	}*/

}
