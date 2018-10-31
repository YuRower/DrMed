package processing.DAO;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import model.Lang;
import model.VaccineTypeLocation;
import model.manager.LocaleManager;

public class VaccinationTypeDAO {
	private final static Logger LOGGER = Logger.getLogger(VaccinationTypeDAO.class);

	public static final String BUNDLES_FOLDER = "property.text";

	static ResourceBundle rb = ResourceBundle.getBundle(BUNDLES_FOLDER, LocaleManager.getCurrentLang().getLocale());

	public static ObservableList<VaccineTypeLocation> getVaccineList() {
		String tuberculosisResource = "/view/vaccination/Vaccination_against_tuberculosis.fxml";
		String poliomyelitsResource = "/view/vaccination/Vaccination_against_poliomyelitis.fxml";
		String diphtheriaResource = "/view/vaccination/Vaccination_against_diphtheria_pertussis_tetanus.fxml";
		String measlesResource = "/view/vaccination/Vaccination_against_measles.fxml";
		String rubellaResource = "/view/vaccination/Vaccination_against_rubella.fxml";
		String parotitisResource = "/view/vaccination/Vaccination_against_parotitis.fxml";
		String hepatitus_B_Resource = "/view/vaccination/Vaccination_against_hepatitis_B.fxml";
		String tubercilinTestResource = "/view/vaccination/Tuberculin_tests.fxml";

		String vaccineRubella = rb.getString("vaccineRubella");

		String vaccineTuberculosis = rb.getString("vaccineTuberculosis");

		String vaccinePoliomyelitis = rb.getString("vaccinePoliomyelitis");

		String vaccineDiphtheriaGooseberryTetanus = rb.getString("vaccineDiphtheriaGooseberryTetanus");

		String vaccineMeasles = rb.getString("vaccineMeasles");

		String vaccineMumps = rb.getString("vaccineMumps");
		String vaccineParotit = rb.getString("vaccineParotit");

		String vaccineHepatitisB = rb.getString("vaccineHepatitisB");
		String tuberculinTest = rb.getString("tuberculinTest");
//String vaccineRubella = rb.getString("vaccineRubella");

		VaccineTypeLocation tuberculosis = new VaccineTypeLocation(0, vaccineTuberculosis, tuberculosisResource);
		VaccineTypeLocation poliomyelits = new VaccineTypeLocation(1, vaccinePoliomyelitis, poliomyelitsResource);
		VaccineTypeLocation diphtheria = new VaccineTypeLocation(2, vaccineDiphtheriaGooseberryTetanus,
				diphtheriaResource);
		VaccineTypeLocation measles = new VaccineTypeLocation(3, vaccineMeasles, measlesResource);
		VaccineTypeLocation rubella = new VaccineTypeLocation(4, vaccineRubella, rubellaResource);
		VaccineTypeLocation parotitis = new VaccineTypeLocation(5, vaccineParotit, parotitisResource);
		VaccineTypeLocation hepatitusB = new VaccineTypeLocation(6, vaccineHepatitisB, hepatitus_B_Resource);
		VaccineTypeLocation tubercilinTest = new VaccineTypeLocation(7, tuberculinTest, tubercilinTestResource);

		ObservableList<VaccineTypeLocation> list = FXCollections.observableArrayList(tuberculosis, poliomyelits,
				diphtheria, measles, rubella, parotitis, hepatitusB, tubercilinTest);

		return list;
	}

}
