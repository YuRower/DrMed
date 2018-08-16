package util;

import javafx.scene.control.Alert;

public class DialogManager {
	   public static void showErrorDialog(String title, String text){
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle(title);
	        alert.setContentText(text);
	        alert.setHeaderText("");
	        alert.showAndWait();
	    }


}
