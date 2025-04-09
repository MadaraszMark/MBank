package application.controller;


import org.apache.commons.codec.digest.DigestUtils;

import application.dao.UserDao;
import application.dto.UserDto;
import application.session.Session;
import application.util.SceneSwitcher;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController {
	
	@FXML private VBox VBoxMainImage;
	@FXML private ImageView imgView;
	@FXML private VBox VBoxTitle;
	@FXML private Label lblTitle;
	@FXML private VBox VBoxMain;
	@FXML private Label lblUserName;
	@FXML private TextField txtUserName;
	@FXML private Label lblPassword;
	@FXML private PasswordField pwdPassword;
	@FXML private Label lblMessage;
	@FXML private Button btnLogin;
	@FXML private Button btnExit;
	@FXML private Button btnRegistration;
	@FXML private AnchorPane anchPaneLoginMain;
	
	public void start(Stage primaryStage) {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("/application/fxml/Login.fxml"));
			Scene scene = new Scene(root, 891, 593);
			scene.getStylesheets().add(getClass().getResource("/application/css/Login.css").toExternalForm());
			primaryStage.setTitle("MBank - Bejelentkezés");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void initialize() {
	    btnLogin.setDefaultButton(true);
	}
	
	@FXML
	private void login(Event event) {
	    UserDao userDao = new UserDao();
	    lblMessage.setText("");
	    lblMessage.setTextFill(Color.BLACK);

	    String userName = txtUserName.getText();
	    String passwordSha1 = DigestUtils.sha1Hex(pwdPassword.getText());

	    if (userName.isEmpty() || pwdPassword.getText().isEmpty()) {
	        lblMessage.setText("Hiányzó adatok!");
	        lblMessage.setTextFill(Color.RED);
	        return;
	    }
	    try {
	        Long result = userDao.validateUserByUserNameAndPassword(userName, passwordSha1);
	        UserDto user = userDao.findByUserName(userName);

	        if (result != null && result > 0 && user != null) {
	            Session.loggedInUserName = user.getUserName();

	            userDao.logLoginAttempt(user.getId(), true);

	            lblMessage.setText("Sikeres belépés!");
	            lblMessage.setTextFill(Color.GREEN);

	            if ("admin".equalsIgnoreCase(user.getUserName())) {
	                // Admin felület
	                SceneSwitcher.switchSceneWithFade(anchPaneLoginMain, "/application/fxml/Admin.fxml", "MBank - Admin");
	            } else {
	                // Felhasználói felület
	                SceneSwitcher.switchSceneWithFade(anchPaneLoginMain, "/application/fxml/ClientMenu.fxml", "MBank - Kezdőlap");
	            }

	        } else {
	            if (user != null) {
	                userDao.logLoginAttempt(user.getId(), false);
	            } else {
	                userDao.logLoginAttempt(null, false);
	            }

	            lblMessage.setText("Hibás felhasználónév/jelszó!");
	            lblMessage.setTextFill(Color.RED);
	        }

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        lblMessage.setText("Ismeretlen hiba történt.");
	        lblMessage.setTextFill(Color.RED);
	    }
	}
	
	@FXML
	private void registration(Event event) {
	    try {
	    	SceneSwitcher.switchSceneWithFade(anchPaneLoginMain, "/application/fxml/Registration.fxml", "MBank - Regisztráció");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@FXML private void exit(Event event) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }
}

