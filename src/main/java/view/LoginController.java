package view;

import org.apache.log4j.Logger;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.MainApp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import util.DialogManager;
import util.LoginManager;

public class LoginController {
	MainApp main ;

	 @FXML private JFXTextField user;
	  @FXML private JFXPasswordField password;
	  @FXML private Button confirmButton;
		private final static Logger LOGGER = Logger.getLogger(LoginController.class);

	  public void initialize() {}
	  
	  
	  @FXML
	  public void initManager() {
		  LoginManager loginManager = new LoginManager();
		  
		/*  confirmButton.setOnAction(new EventHandler<ActionEvent>() {
	      @Override public void handle(ActionEvent event) {*/
	        String sessionID = authorize();
	        LOGGER.info(sessionID);
	        if (sessionID != null) {
	          loginManager.authenticated(sessionID,main);
	        }else {
	        	DialogManager.incorrectPassword("Incorrect Password or Login" ,"Please enter correct data" );
	        }
	   //  }
	   // });
	  }
	  public void setMainApp(MainApp mainApp) {
			this.main = mainApp;
		}
	  
	  /**
	   * Check authorization credentials.
	   * 
	   * If accepted, return a sessionID for the authorized session
	   * otherwise, return null.
	   */   
	  private String authorize() {
	        LOGGER.info(user.getText() + " " + password.getText());

		  
	    return 
	      "admin".equals(user.getText()) && "1234".equals(password.getText()) 
	            ? generateSessionID() 
	            : null;
	  }
	  
	  private static int sessionID = 0;

	  private String generateSessionID() {
	    sessionID++;
	    return "xyzzy - session " + sessionID;
	  }
	}


