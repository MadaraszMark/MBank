package application.presenter;

import application.controller.LoginController;
import application.controller.RegistrationController;
import javafx.stage.Stage;

public class BankPresenter {
	
	private LoginController loginController;

	public BankPresenter() {
        this.loginController = new LoginController();
    }
	
	public void startLogin(Stage primaryStage) {
        loginController.start(primaryStage);
    }
	


}
