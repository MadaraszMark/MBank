package application.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import application.dao.AccountDao;
import application.dao.TransactionDao;
import application.dao.UserDao;
import application.dto.AccountsDto;
import application.dto.TransactionsDto;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TransactionsController {
	
	@FXML private Button btnDashboard;
    @FXML private Button btnTransactions;
    @FXML private Button btnAccounts;
    @FXML private Button btnProfile;
    @FXML private Button btnLogout;
    @FXML private Button btnReport;
    @FXML private VBox VBoxTransactionsScrollPane;
    @FXML private Label lblTransactionsTitle;
    @FXML private AnchorPane anchPaneLatesTransaction1;
    @FXML private ScrollPane scScrollPaneTransactions;
    
    @FXML
    private void initialize() {
    	loadTransactions();
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
	
	@FXML
    private void handleLogout() {
        Session.loggedInUserName = null;
        SceneSwitcher.switchSceneWithFade(btnLogout, "/application/fxml/Login.fxml", "MBank - Bejelentkezés");
    }
	
	@FXML
	private void handleProfile() {
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/Profile.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnProfile.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/application/css/Profile.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("MBank - Profil");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
	
	private void loadTransactions() {
	    String userName = Session.loggedInUserName;
	    if (userName == null) {
	        System.out.println("Nincs bejelentkezett felhasználó.");
	        return;
	    }
	    UserDao userDao = new UserDao();
	    AccountDao accountDao = new AccountDao();
	    TransactionDao transactionDao = new TransactionDao();

	    UserDto user = userDao.findByUserName(userName);
	    if (user == null) {
	        System.out.println("Nem található felhasználó ezzel a névvel: " + userName);
	        return;
	    }

	    AccountsDto account = accountDao.findAccountByUserId(user.getId());
	    if (account == null) {
	        System.out.println("Nincs számla az adott userhez.");
	        return;
	    }

	    Long myAccountId = account.getId();
	    List<TransactionsDto> transactions = transactionDao.getAllByAccountId(myAccountId);

	    VBoxTransactionsScrollPane.getChildren().clear();

	    for (TransactionsDto t : transactions) {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/TransactionsItem.fxml"));
	            AnchorPane pane = loader.load();

	            TransactionsItemController controller = loader.getController();

	            boolean isIncoming = t.getToAccount().equals(myAccountId);

	            String fromName = "Ismeretlen";
	            String toName = "Ismeretlen";

	            AccountsDto from = accountDao.findAccountById(t.getFromAccount());
	            AccountsDto to = accountDao.findAccountById(t.getToAccount());

	            if (from != null) {
	                UserDto fromUser = userDao.findUserById(from.getUserId());
	                if (fromUser != null) fromName = fromUser.getUserName();
	            }

	            if (to != null) {
	                UserDto toUser = userDao.findUserById(to.getUserId());
	                if (toUser != null) toName = toUser.getUserName();
	            }

	            String formattedDate = t.getTimeStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	            String formattedAmount = String.format("%.0f %s", t.getAmount(), t.getCurrency());

	            controller.setTransactionData(formattedDate, "@" + fromName, "@" + toName, formattedAmount, isIncoming, t.getMessage());
	            VBoxTransactionsScrollPane.getChildren().add(pane);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
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
