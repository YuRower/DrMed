package util;

import java.io.IOException;
import java.util.logging.*;

import org.apache.log4j.Logger;

import application.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import view.LoginController;

/** Manages control flow for logins */
public class LoginManager {
	private Scene scene;
	private final static Logger LOGGER = Logger.getLogger(LoginController.class);

	public LoginManager(Scene scene) {
		this.scene = scene;
	}

	public LoginManager() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Callback method invoked to notify that a user has been authenticated. Will
	 * show the main application screen.
	 */
	public void authenticated(String sessionID, MainApp main) {
        LOGGER.info("authenticated");
        main.authintication(true);

	}

	/**
	 * Callback method invoked to notify that a user has logged out of the main
	 * application. Will show the login application screen.
	 */
	public void logout() {
		//showLoginScreen();
	}

}