package util;

import static util.AbstractResource.FIRST_TWO_TABLES;
import static util.AbstractResource.FROM_THREE_TO_SIX_TABLES;
import static util.AbstractResource.LAST_TABLE;

import java.io.File;

import org.apache.log4j.Logger;

import model.manager.VaccineManager;
import view.vaccination.VaccineTableController;

public abstract class AbstractResource {
	protected static final String FIRST_TWO_TABLES = "/view/tableEditpages/edit1_2Table.fxml";
	protected static final String FROM_THREE_TO_SIX_TABLES = "/view/tableEditpages/edit3_6Table.fxml";
	protected static final String LAST_TABLE = "/view/tableEditpages/edit7Table.fxml";
	protected static final File XML_FILE = new File("xmlFile//vaccineInfo.xml");
	private final static Logger LOGGER = Logger.getLogger(AbstractResource.class);

	public String getResource() {
		LOGGER.info("method getResource" + VaccineManager.getCurrentVaccine());
		String resource = null;
		if (VaccineManager.getCurrentVaccine() == null) {
			resource = FIRST_TWO_TABLES;
		} else {
			int indVaccine = VaccineManager.getCurrentVaccine().getIndex();
			if ((indVaccine >= 0) && (indVaccine <= 1)) {
				LOGGER.info(indVaccine);
				resource = FIRST_TWO_TABLES;
			} else if ((indVaccine >= 2) && (indVaccine <= 6)) {
				LOGGER.info(indVaccine);
				resource = FROM_THREE_TO_SIX_TABLES;
			} else if (indVaccine == 7) {
				LOGGER.info(indVaccine);
				resource = LAST_TABLE;
			} else {
				LOGGER.info("not found current table");

			}
		}
		return resource;

	}
}
