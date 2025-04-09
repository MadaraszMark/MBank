package application.controller;

import javax.persistence.PersistenceException;

import org.apache.commons.codec.digest.DigestUtils;

import application.dao.UserDao;
import application.dto.UserDto;
import application.util.SceneSwitcher;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistrationController {
	
	@FXML private VBox VBoxImage;
	@FXML private VBox Main;
	@FXML private ImageView imgRegistrationImage;
	@FXML private Label lblLastName;
	@FXML private TextField txtLastName;
	@FXML private Label lblFirstName;
    @FXML private TextField txtFirstName;
    @FXML private Label lblUserName;
    @FXML private TextField txtUserName;
    @FXML private Label lblEmail;
    @FXML private TextField txtEmail;
    @FXML private Label lblPhoneNumber;
    @FXML private TextField txtPhoneNumber;
    @FXML private Label lblPassword;
    @FXML private PasswordField pwsRegistrationPassword;
    @FXML private Button btnRegister;
    @FXML private Label lblMessage;
    @FXML private Button btnExit;
    @FXML private AnchorPane anchPaneRegistrationMain;
    
    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
        return password.matches(regex);
    }
    
    @FXML
    private void register(Event event) {
        lblMessage.setText("");
        lblMessage.getStyleClass().removeAll("success", "error");

        UserDto userDto = getUserDataFromScene();
        
        String passwordPlain = pwsRegistrationPassword.getText();

        if (!isValidPassword(passwordPlain)) {
            lblMessage.setText("A jelszónak legalább 8 karakteresnek kell lennie, tartalmaznia kell 1 nagybetűt és 1 számot.");
            lblMessage.getStyleClass().removeAll("success", "error");
            lblMessage.getStyleClass().add("error");
            return;
        }

        if (userDto == null) {
            lblMessage.setText("Minden mező kitöltése kötelező!");
            lblMessage.getStyleClass().add("error");
            return;
        }
        try {
            UserDao userDao = new UserDao();

            if (userDao.emailExists(userDto.getEmail())) {
                lblMessage.setText("Ez az e-mail cím már regisztrálva van!");
                lblMessage.getStyleClass().add("error");
                return;
            }

            if (userDao.phoneExists(userDto.getPhoneNumber())) {
                lblMessage.setText("Ez a telefonszám már foglalt!");
                lblMessage.getStyleClass().add("error");
                return;
            }

            userDao.saveWithAccount(userDto);
            lblMessage.setText("Sikeres regisztráció!");
            lblMessage.getStyleClass().add("success");
            try {
            	SceneSwitcher.switchSceneWithFade(anchPaneRegistrationMain, "/application/fxml/Login.fxml", "MBank - Bejelentkezés");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (PersistenceException e) {
            lblMessage.setText("A felhasználónév már létezik!");
            lblMessage.getStyleClass().add("error");
            e.printStackTrace();
        } catch (Exception e) {
            lblMessage.setText("Ismeretlen hiba történt.");
            lblMessage.getStyleClass().add("error");
            e.printStackTrace();
        }
    }
	
	private UserDto getUserDataFromScene() {
		String userName = txtUserName.getText();
        String password = DigestUtils.sha1Hex(pwsRegistrationPassword.getText()).trim();
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String email = txtEmail.getText();
        String phoneNumber = txtPhoneNumber.getText();
       
        UserDto userDto = null;
        if (userName.length() != 0 && password.length() != 0 && firstName.length() != 0 && lastName.length() != 0 &&
        		email.length() != 0 && phoneNumber.length() != 0) {
        	userDto = new UserDto(userName, firstName, lastName, password, email, phoneNumber);
        	userDto.setStatus("active");
        }
        return userDto;
	}
	
	@FXML
	private void goBackToLogin(Event event) {
	    SceneSwitcher.switchSceneWithFade(anchPaneRegistrationMain, "/application/fxml/Login.fxml", "MBank - Bejelentkezés");
	}
	
	@FXML private void exit(Event event) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }
}
