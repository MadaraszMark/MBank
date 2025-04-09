package application.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

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
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProfileController {

    @FXML private Label lblProfileUserNameResult;
    @FXML private Label lblProfileFirstNameResult;
    @FXML private Label lblProfileLastNameResult;
    @FXML private Label lblProfileEmailResult;
    @FXML private Label lblProfilePhoneNumberResult;
    @FXML private Label lblProfileRegisteredDateResult;
    @FXML private PasswordField pwdProfilePasswordChange;
    @FXML private TextField txtProfileEmailChange;
    @FXML private Label lblProfilePasswordResult;
    @FXML private Label lblProfileEmailChangeResult;
    @FXML private Button btnProfilePasswordChange;
    @FXML private Button btnProfileEmailChange;
    @FXML private Button btnDashboard;
    @FXML private Button btnTransactions;
    @FXML private Button btnAccounts;
    @FXML private Button btnProfile;
    @FXML private Button btnLogout;
    @FXML private Button btnReport;
    @FXML private Separator sepSpearatorProfile;

    @FXML
    private void initialize() {
        loadProfileData();
    }

    private void loadProfileData() {
        String username = Session.loggedInUserName;
        if (username != null) {
            UserDao userDao = new UserDao();
            UserDto user = userDao.findByUserName(username);

            if (user != null) {
                lblProfileUserNameResult.setText(user.getUserName());
                lblProfileFirstNameResult.setText(user.getFirstName());
                lblProfileLastNameResult.setText(user.getLastName());
                lblProfileEmailResult.setText(user.getEmail());
                lblProfilePhoneNumberResult.setText(user.getPhoneNumber());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                lblProfileRegisteredDateResult.setText(user.getCreatedAt().format(formatter));
            }
        }
    }

    @FXML
    private void handleLogout() {
        Session.loggedInUserName = null;
        SceneSwitcher.switchSceneWithFade(btnLogout, "/application/fxml/Login.fxml", "MBank - Bejelentkezés");
    }
    
    @FXML
    private void handleMainMenu(Event event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/ClientMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnProfile.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/application/css/ClientMenu.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("MBank - Kezdőlap");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*\\d.*");
    }
    
    @FXML
	private void handleAccounts() {
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/Accounts.fxml"));
	        Parent root = loader.load();
	        Stage stage = (Stage) btnProfile.getScene().getWindow();
	        Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource("/application/css/Accounts.css").toExternalForm());
	        stage.setScene(scene);
	        stage.setTitle("MBank - Számlák");
	        stage.show();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
    
    @FXML
    private void handlePasswordChange() {
        String newPassword = pwdProfilePasswordChange.getText().trim();

        if (!isValidPassword(newPassword)) {
            lblProfilePasswordResult.setText("A jelszónak min. 8 karakterből kell állnia, 1 nagybetű és 1 szám.");
            lblProfilePasswordResult.setStyle("-fx-text-fill: #d32f2f;");
            return;
        }

        UserDao userDao = new UserDao();
        UserDto user = userDao.findByUserName(Session.loggedInUserName);

        if (user != null) {
            String hashedPassword = DigestUtils.sha1Hex(newPassword);
            user.setPassword(hashedPassword);
            userDao.update(user);

            lblProfilePasswordResult.setStyle("-fx-text-fill: #2e7d32;");
            lblProfilePasswordResult.setText("Sikeres jelszóváltoztatás!");
            pwdProfilePasswordChange.clear();
        } else {
            lblProfilePasswordResult.setText("Hiba történt.");
            lblProfilePasswordResult.setStyle("-fx-text-fill: #d32f2f;");
        }
    }
    
    @FXML
    private void handleEmailChange() {
        String newEmail = txtProfileEmailChange.getText().trim();

        if (newEmail.isEmpty() || !newEmail.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
            lblProfileEmailChangeResult.setText("Érvénytelen email formátum.");
            lblProfileEmailChangeResult.setStyle("-fx-text-fill: #d32f2f;");
            return;
        }

        UserDao userDao = new UserDao();

        if (userDao.emailExists(newEmail)) {
            lblProfileEmailChangeResult.setText("Ez az email már használatban van.");
            lblProfileEmailChangeResult.setStyle("-fx-text-fill: #d32f2f;");
            return;
        }

        UserDto user = userDao.findByUserName(Session.loggedInUserName);
        if (user != null) {
            user.setEmail(newEmail);
            userDao.update(user);
            lblProfileEmailChangeResult.setText("Sikeres email módosítás!");
            lblProfileEmailChangeResult.setStyle("-fx-text-fill: #2e7d32;");
            txtProfileEmailChange.clear();
            lblProfileEmailResult.setText(newEmail);
        } else {
            lblProfileEmailChangeResult.setText("Hiba történt.");
        }
    }
    
    @FXML
	private void handleTransactions() {
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/Transactions.fxml"));
	        Parent root = loader.load();
	        Stage stage = (Stage) btnProfile.getScene().getWindow();
	        Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource("/application/css/Transactions.css").toExternalForm());
	        stage.setScene(scene);
	        stage.setTitle("MBank - Tranzakciók");
	        stage.show();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
    
    @FXML
	private void openReportWindow() {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/Reports.fxml"));
	        Parent root = loader.load();

	        Stage stage = new Stage();
	        stage.setTitle("Hibajelentés");
	        stage.setScene(new Scene(root));
	        stage.initModality(Modality.WINDOW_MODAL);
	        stage.initOwner(btnReport.getScene().getWindow());
	        stage.setResizable(false);
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


}

