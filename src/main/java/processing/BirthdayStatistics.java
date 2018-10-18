package processing;

import java.io.IOException;

import org.apache.log4j.Logger;

import application.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Classes;
import view.BirthdayStatisticsController;

public class BirthdayStatistics {
	private final static Logger LOGGER = Logger.getLogger(BirthdayStatistics.class);
	MainApp app;
	public void showBirthdayStatistics() {
		try {
			LOGGER.info("method showBirthdayStatistics");

			app = new MainApp();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/BirthdayStatistics.fxml"));
			AnchorPane page = loader.load();
			LOGGER.info("Load " + loader.getLocation());

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Birthday Statistics");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(app.getPrimaryStage());
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			dialogStage.getIcons().add(new Image("/images/calendar.png"));

			BirthdayStatisticsController controller = loader.getController();
			LOGGER.info("list with current data classes" + Classes.classListData);
			controller.setPersonData(Classes.classListData);
			dialogStage.show();

		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
}
