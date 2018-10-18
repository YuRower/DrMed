package model.manager;

import org.apache.log4j.Logger;
import application.MainApp;
import javafx.scene.*;
import view.LoginController;

public class LoginManager {
	private Scene scene;
	private final static Logger LOGGER = Logger.getLogger(LoginController.class);

	public LoginManager(Scene scene) {
		this.scene = scene;
	}

	public LoginManager() {

	}

	public void authenticated(String sessionID, MainApp main) {
		LOGGER.info("method authenticated curren session number " + sessionID);
		main.authintication(true);
	}

}