package view;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import model.Person; 
import util.DateUtil;

public class BirthdayStatisticsController {

	@FXML
	private BarChart<String, Integer> barChart;

	@FXML
	private CategoryAxis xAxis;

	private ObservableList<String> monthNames = FXCollections.observableArrayList();

	private final static Logger LOGGER = Logger.getLogger(BirthdayStatisticsController.class);

	@FXML
	private void initialize() {
		LOGGER.info("Get an array with the English month names.");
		String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
		LOGGER.info(" Convert it to a list and add it to our ObservableList of months.");

		monthNames.addAll(Arrays.asList(months));

		xAxis.setCategories(monthNames);
	}

	
	/*public void setPersonData(List<Person> persons) {
		int[] monthCounter = new int[12];
		for (Person p : persons) {
			LOGGER.info(" PARSE birthday in a specific month. .");

			int month = DateUtil.parse(p.getBirthday()).getMonthValue() - 1;
			LOGGER.debug(" PARSE birthday " +month  );

			monthCounter[month]++;
		}

		XYChart.Series<String, Integer> series = new XYChart.Series<>();

		for (int i = 0; i < monthCounter.length; i++) {
			series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
		}
		LOGGER.debug(" number of month " +monthCounter  );
		barChart.getData().add(series);

	}*/
}