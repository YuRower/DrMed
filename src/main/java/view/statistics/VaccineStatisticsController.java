package view.statistics;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import model.Person;
import model.VaccineTypeLocation;
import model.vaccine.VaccineEntity;
import processing.DAO.VaccinationTypeDAO;
import util.DateUtil;

public class VaccineStatisticsController {
	@FXML
	private CategoryAxis xAxis = new CategoryAxis();;
	@FXML
	private NumberAxis yAxis = new NumberAxis();;
	@FXML
	private BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);

	private ObservableList<String> vaccineNames = FXCollections.observableArrayList();

	private final static Logger LOGGER = Logger.getLogger(BirthdayStatisticsController.class);

	@FXML
	private void initialize() {
		ObservableList<VaccineTypeLocation> list = VaccinationTypeDAO.getVaccineList();

		LOGGER.info("Get an array with the vaccine names.");
		LOGGER.info(" Convert it to a list and add it to our ObservableList of months.");
		Iterator<VaccineTypeLocation> iter = list.iterator();
		while (iter.hasNext()) {
			VaccineTypeLocation vacc = iter.next();
			vaccineNames.add(vacc.getName());
			LOGGER.info(vaccineNames);
		}
		xAxis.setCategories(vaccineNames);
	}

	public void setVaccineData(ObservableList<VaccineEntity> vaccineList) {
		xAxis.setLabel("Vaccine");
		yAxis.setLabel("Pupils");
		int[] vaccineCounter = new int[8];
		LOGGER.info(vaccineList+"-----------------------------------------");
		for (VaccineEntity v : vaccineList) {
		//for (int j = 0; j < vaccineList.size(); j++) {
			LOGGER.info(" PARSE birthday in a specific month. .");

			int vaccine = v.getId();
			LOGGER.debug(" PARSE birthday " + vaccine);
			vaccineCounter[vaccine]++;
		}
		XYChart.Series<String, Number> series = new XYChart.Series<>();

		for (int i = 0; i < vaccineCounter.length; i++) {
			series.getData().add(new XYChart.Data<String, Number>(vaccineNames.get(i), vaccineCounter[i]));
		}

		LOGGER.debug(" number of month " + vaccineCounter);
		barChart.getData().add(series);

		barChart.setTitle("Birthday Statistics");

	}
}