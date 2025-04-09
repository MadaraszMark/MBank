package application.controller;

import java.util.List;

import application.dao.AccountDao;
import application.dao.UserDao;
import application.dto.AccountsDto;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminClientsController {
	
	 	@FXML private VBox VBoxAdminClientsSideMenu;
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
	    @FXML private VBox VBoxAdminClientsList;
	    @FXML private Label lblAdminClientsTitle;
	    @FXML private ScrollPane spScrollPaneAdminClientsList;
	    @FXML private VBox VBoxScrollPaneAdminClientList;
	    
	    @FXML
	    private void initialize() {
	    	loadClientsToScrollPane();
	    }
	    
	    private void loadClientsToScrollPane() {
	        VBoxScrollPaneAdminClientList.getChildren().clear();
	        UserDao userDao = new UserDao();
	        AccountDao accountDao = new AccountDao();

	        List<UserDto> users = userDao.findAllUsers();

	        for (UserDto user : users) {
	            if ("admin".equalsIgnoreCase(user.getUserName())) {
	                continue;
	            }

	            AccountsDto account = accountDao.findAccountByUserId(user.getId());

	            String userInfo = String.format(
	                "\uD83D\uDC64 %s (%s %s)\n📧 %s\n📞 %s\n💳 %s\n💰 %.0f %s",
	                user.getUserName(),
	                user.getLastName(),
	                user.getFirstName(),
	                user.getEmail(),
	                user.getPhoneNumber(),
	                account != null ? account.getAccountNumber() : "Nincs számla",
	                account != null ? account.getBalance() : 0.0,
	                account != null ? account.getCurrency() : ""
	            );

	            Label label = new Label(userInfo);
	            label.setMaxWidth(1000);
	            label.setWrapText(true);
	            label.setStyle(
	                "-fx-font-size: 18px;" +
	                "-fx-padding: 20;" +
	                "-fx-text-fill: #1a1a1a;" +
	                "-fx-background-color: #f5f5f5;" +
	                "-fx-background-radius: 12;" +
	                "-fx-pref-width: 1000px;" +
	                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0.3, 0, 2);"
	            );

	            VBox.setMargin(label, new Insets(20, 0, 20, 30));
	            VBoxScrollPaneAdminClientList.getChildren().add(label);
	        }
	    }

	
	@FXML
    private void handleLogout() {
        Session.loggedInUserName = null;
        SceneSwitcher.switchSceneWithFade(btnLogout, "/application/fxml/Login.fxml", "MBank - Bejelentkezés");
    }
	
	@FXML
	private void handleAdmin() {
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/Admin.fxml"));
	        Parent root = loader.load();
	        Stage stage = (Stage) btnClients.getScene().getWindow();
	        Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource("/application/css/Admin.css").toExternalForm());
	        stage.setScene(scene);
	        stage.setTitle("MBank - Admin");
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

}
