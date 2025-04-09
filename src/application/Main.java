//********** Madarász Márk - 2025 **********\\

package application;

import application.presenter.BankPresenter;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	//Itt indul az alkalmazás
	@Override
	public void start(Stage primaryStage) throws Exception {
		BankPresenter presenter = new BankPresenter();
		presenter.startLogin(primaryStage);
		
	}

	
}
