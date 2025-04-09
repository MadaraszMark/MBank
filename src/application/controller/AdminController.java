package application.controller;

import java.util.List;

import application.dao.ReportsDao;
import application.dao.UserDao;
import application.dto.ReportsDto;
import application.dto.UserDto;
import application.session.Session;
import application.util.SceneSwitcher;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AdminController {
	
	@FXML private VBox VBoxAdminSideMenu;
    @FXML private ImageView imgMBankImage;
    @FXML private Label lblMBankTitle;
    @FXML private Button btnCreateClient;
    @FXML private FontAwesomeIconView fontClientCreate;
    @FXML private Button btnClients;
    @FXML private FontAwesomeIconView fontClients;
    @FXML private Button btnDeposit;
    @FXML private FontAwesomeIconView fontAccounts;
    @FXML private Separator spSeparator;
    @FXML private Button btnLogout;
    @FXML private FontAwesomeIconView fontLogout;
    @FXML private VBox VBoxAdminMenu;
    @FXML private Label lblAdmin;
    @FXML private VBox VBoxCreateNewClient;
    @FXML private Label lblCreateNewClientTitle;
    @FXML private Label lblCreateNewClientLastName;
    @FXML private TextField txtCreateNewClientLastName;
    @FXML private Label lblCreateNewClientFirstName;
    @FXML private TextField txtCreateNewClientFirstName;
    @FXML private Label lblCreateNewClientUserName;
    @FXML private TextField txtCreateNewClientUserName;
    @FXML private Label lblCreateNewClientPassword;
    @FXML private PasswordField txtCreateNewClientPassword;
    @FXML private Label lblCreateNewClientEmail;
    @FXML private TextField txtCreateNewClientEmail;
    @FXML private Label lblCreateNewClientPhoneNumber;
    @FXML private TextField txtCreateNewClientPhoneNumber;
    @FXML private Label lblCreateNewClientResult;
    @FXML private Button btnCreateNewClient;
    @FXML private VBox VBoxCreateNewClientSeparator;
    @FXML private Separator sepSpearatorAccounts;
    @FXML private VBox VBoxAdminReports;
    @FXML private Label lblAdminReportsTitle;
    @FXML private ScrollPane spScrollPaneReportsList;
    
    private VBox vboxReportsContent = new VBox();
    
    @FXML
    private void initialize() {
        btnCreateNewClient.setText("Új ügyfél létrehozása");
        btnCreateNewClient.setOnAction(e -> createNewClient());
        loadReports();
    }

    private void createNewClient() {
        String userName = txtCreateNewClientUserName.getText().trim();
        String password = txtCreateNewClientPassword.getText();
        String firstName = txtCreateNewClientFirstName.getText().trim();
        String lastName = txtCreateNewClientLastName.getText().trim();
        String email = txtCreateNewClientEmail.getText().trim();
        String phone = txtCreateNewClientPhoneNumber.getText().trim();

        lblCreateNewClientResult.getStyleClass().removeAll("error", "success");

        if (userName.isEmpty() || password.isEmpty() || firstName.isEmpty() ||
            lastName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            lblCreateNewClientResult.setText("Minden mező kitöltése kötelező!");
            lblCreateNewClientResult.getStyleClass().add("error");
            lblCreateNewClientResult.setTextFill(Color.RED);
            return;
        }

        if (!isValidPassword(password)) {
            lblCreateNewClientResult.setText("A jelszónak legalább 8 karakteresnek kell lennie, tartalmaznia kell 1 nagybetűt és 1 számot.");
            lblCreateNewClientResult.getStyleClass().add("error");
            lblCreateNewClientResult.setTextFill(Color.RED);
            return;
        }

        try {
            UserDao userDao = new UserDao();

            if (userDao.emailExists(email)) {
                lblCreateNewClientResult.setText("Ez az e-mail cím már regisztrálva van!");
                lblCreateNewClientResult.getStyleClass().add("error");
                lblCreateNewClientResult.setTextFill(Color.RED);
                return;
            }

            if (userDao.phoneExists(phone)) {
                lblCreateNewClientResult.setText("Ez a telefonszám már foglalt!");
                lblCreateNewClientResult.getStyleClass().add("error");
                lblCreateNewClientResult.setTextFill(Color.RED);
                return;
            }

            UserDto newUser = new UserDto(
                userName,
                firstName,
                lastName,
                org.apache.commons.codec.digest.DigestUtils.sha1Hex(password).trim(),
                email,
                phone
            );
            newUser.setStatus("active");

            userDao.saveWithAccount(newUser);

            lblCreateNewClientResult.setText("Sikeres ügyfél létrehozás!");
            lblCreateNewClientResult.getStyleClass().add("success");
            lblCreateNewClientResult.setTextFill(Color.GREEN);

            clearClientForm();

        } catch (Exception e) {
            lblCreateNewClientResult.setText("Hiba történt az ügyfél mentése közben.");
            lblCreateNewClientResult.getStyleClass().add("error");
            lblCreateNewClientResult.setTextFill(Color.RED);
            e.printStackTrace();
        }
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$");
    }

    private void clearClientForm() {
        txtCreateNewClientUserName.clear();
        txtCreateNewClientPassword.clear();
        txtCreateNewClientFirstName.clear();
        txtCreateNewClientLastName.clear();
        txtCreateNewClientEmail.clear();
        txtCreateNewClientPhoneNumber.clear();
    }
    
    private void loadReports() {
        ReportsDao reportsDao = new ReportsDao();
        List<ReportsDto> reports = reportsDao.findAllReports();

        vboxReportsContent.getChildren().clear();
        vboxReportsContent.setSpacing(20);
        vboxReportsContent.setPadding(new Insets(20));

        for (ReportsDto report : reports) {
            UserDao userDao = new UserDao();
            UserDto user = userDao.findUserById(report.getUserId());

            String reportInfo = String.format(
                "👤 %s (%s %s)\n📩 %s\n🕒 %s\n📄 %s",
                user.getUserName(),
                user.getLastName(),
                user.getFirstName(),
                user.getEmail(),
                report.getSubmittedAt().toString().replace("T", " "),
                report.getMessage()
            );

            Label label = new Label(reportInfo);
            label.setWrapText(true);
            label.setMaxWidth(570);
            label.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-padding: 20;" +
                "-fx-text-fill: #1a1a1a;" +
                "-fx-background-color: #f5f5f5;" +
                "-fx-background-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0.3, 0, 2);"
            );

            VBox.setMargin(label, new Insets(10, 0, 10, 10));
            vboxReportsContent.getChildren().add(label);
        }

        spScrollPaneReportsList.setContent(vboxReportsContent);
    }
    
    @FXML
	private void handleClients() {
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/AdminClients.fxml"));
	        Parent root = loader.load();
	        Stage stage = (Stage) btnClients.getScene().getWindow();
	        Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource("/application/css/AdminClients.css").toExternalForm());
	        stage.setScene(scene);
	        stage.setTitle("MBank - Ügyfelek");
	        stage.show();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
    
    @FXML
	private void handleDeposit() {
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/AdminDeposit.fxml"));
	        Parent root = loader.load();
	        Stage stage = (Stage) btnClients.getScene().getWindow();
	        Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource("/application/css/AdminDeposit.css").toExternalForm());
	        stage.setScene(scene);
	        stage.setTitle("MBank - Befizetés");
	        stage.show();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
    
    @FXML
    private void handleLogout() {
        Session.loggedInUserName = null;
        SceneSwitcher.switchSceneWithFade(btnLogout, "/application/fxml/Login.fxml", "MBank - Bejelentkezés");
    }

}
