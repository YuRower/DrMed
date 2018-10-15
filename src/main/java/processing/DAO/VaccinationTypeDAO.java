package processing.DAO;

import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import model.VaccinationType;

public class VaccinationTypeDAO {
	public static ObservableList<VaccinationType> getVaccineList() {
		VaccinationType tuberculosis = new VaccinationType(0, "Щеплення проти туберкульозу");
		VaccinationType poliomyelits = new VaccinationType(1, "Щеплення проти поліомієліту");
		VaccinationType diphtheria = new VaccinationType(2, "Щеплення проти дифтерії, кашлюку, правцю");
		VaccinationType measles = new VaccinationType(3, "Щеплення проти кору");
		VaccinationType rubella = new VaccinationType(4, "Щеплення проти краснухи");
		VaccinationType parotitis = new VaccinationType(5, "Щеплення проти паротиту");
		VaccinationType hepatitusB = new VaccinationType(6, "Щеплення проти гепатиту В");
		VaccinationType tubercilinTest = new VaccinationType(7, "Туберкулінові проби");

		ObservableList<VaccinationType> list //
				= FXCollections.observableArrayList(tuberculosis, poliomyelits, diphtheria, measles, rubella, parotitis,
						hepatitusB, tubercilinTest);

		return list;
	}

	public static ObservableMap getMapResourceVaccine() {
		String tuberculosisResource = "/view/vaccination/Vaccination_against_tuberculosis.fxml";

		String poliomyelits = "/view/vaccination/Vaccination_against_poliomyelitis.fxml";

		String diphtheria = "/view/vaccination/Vaccination_against_diphtheria_pertussis_tetanus.fxml";

		String measles = "/view/vaccination/Vaccination_against_measles.fxml";

		String rubella = "/view/vaccination/Vaccination_against_rubella.fxml";

		String parotitis = "/view/vaccination/Tuberculin_tests.fxml";
		String hepatitusB = "/view/vaccination/Vaccination_against_hepatitis_B.fxml";
		String tubercilinTest = "/view/vaccination/Tuberculin_tests.fxml";

		ObservableMap<VaccinationType, String> map = FXCollections.observableHashMap();

		return map;
	}

}
