package application.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import application.dao.AccountDao;
import application.dao.TransactionDao;
import application.dao.UserDao;
import application.dto.AccountsDto;
import application.dto.TransactionsDto;
import application.dto.UserDto;
import application.session.Session;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ClientMenuController {
	
	@FXML private BorderPane borderPaneMain;
	@FXML private BorderPane borderPaneCenter;
	@FXML private VBox VBoxSideMenu;
	@FXML private ImageView imgMBankImage;
	@FXML private Label lblMBankTitle;
	@FXML private Button btnDashboard;
	@FXML private Button btnTransactions;
	@FXML private Button btnAccounts;
	@FXML private Separator spSeparator;
	@FXML private Button btnProfile;
	@FXML private Button btnLogout;
	@FXML private VBox VBoxReportMenu;
	@FXML private Label ReportTitle;
	@FXML private Label Report;
	@FXML private Button btnReport;
	@FXML private HBox HBoxHeader;
	@FXML private Label lblWelcomeMessage;
	@FXML private Label lblWelcomeMessageName;
	@FXML private Label lblWelcomeDate;
	@FXML private AnchorPane anchPaneAccounts;
	@FXML private VBox VBoxAccountsChecking;
	@FXML private Label lblCheckingAccountAllMoney;
	@FXML private Label lblCheckingAccountNumber;
	@FXML private Label lblCheckingAccountTitle;
	@FXML private AnchorPane anchPaneAccountSummary;
	@FXML private Label AccountSummaryIncome;
	@FXML private Label AccountSummaryExpenses;
	@FXML private Label lblAccountSummaryIncomeResult;
	@FXML private Label lblAccountSummaryExpensesResult;
	@FXML private Label lblAccountSummaryThisMonth;
	@FXML private TextField txtPayeeAdressTransaction;
	@FXML private TextField txtAmountTransaction;
	@FXML private Label lblResultTransaction;
	@FXML private TextField txtMessageTransaction;
	// Tranzakció 1
	@FXML private Label lblLatesTransactionsDate1;
	@FXML private Label lblLatesTransactionsFromName1;
	@FXML private Label lblLatesTransactionsToName1;
	@FXML private Label lblLatesTransactionsMoney1;
	@FXML private FontAwesomeIconView fontAwesomeIconArrowLeft1;
	@FXML private FontAwesomeIconView fontAwesomeIconArrowRight1;
	@FXML private FontAwesomeIconView fontAwesomeIconBell1;
	@FXML private Separator sepSeparatorTransactions1;
	// Tranzakció 2
	@FXML private Label lblLatesTransactionsDate2;
	@FXML private Label lblLatesTransactionsFromName2;
	@FXML private Label lblLatesTransactionsToName2;
	@FXML private Label lblLatesTransactionsMoney2;
	@FXML private FontAwesomeIconView fontAwesomeIconArrowLeft2;
	@FXML private FontAwesomeIconView fontAwesomeIconArrowRight2;
	@FXML private FontAwesomeIconView fontAwesomeIconBell2;
	@FXML private Separator sepSeparatorTransactions2;
	// Tranzakció 3
	@FXML private Label lblLatesTransactionsDate3;
	@FXML private Label lblLatesTransactionsFromName3;
	@FXML private Label lblLatesTransactionsToName3;
	@FXML private Label lblLatesTransactionsMoney3;
	@FXML private FontAwesomeIconView fontAwesomeIconArrowLeft3;
	@FXML private FontAwesomeIconView fontAwesomeIconArrowRight3;
	@FXML private FontAwesomeIconView fontAwesomeIconBell3;
	@FXML private Separator sepSeparatorTransactions3;
	// Tranzakció 4
	@FXML private Label lblLatesTransactionsDate4;
	@FXML private Label lblLatesTransactionsFromName4;
	@FXML private Label lblLatesTransactionsToName4;
	@FXML private Label lblLatesTransactionsMoney4;
	@FXML private FontAwesomeIconView fontAwesomeIconArrowLeft4;
	@FXML private FontAwesomeIconView fontAwesomeIconArrowRight4;
	@FXML private FontAwesomeIconView fontAwesomeIconBell4;
	@FXML private Separator sepSeparatorTransactions4;
	@FXML private AnchorPane anchPaneLatesTransaction1;
	@FXML private AnchorPane anchPaneLatesTransaction2;
	@FXML private AnchorPane anchPaneLatesTransaction3;
	@FXML private AnchorPane anchPaneLatesTransaction4;

	@FXML
	private void initialize() {
	    startClock();
	    getWelcomeMessageName();
	    loadAccountData();
	    setCurrentMontSummaryTitle();
	    loadMonthlySummary();
	    String userName = Session.loggedInUserName;
	    if (userName != null) {
	        UserDao userDao = new UserDao();
	        UserDto user = userDao.findByUserName(userName);
	        if (user != null) {
	            AccountDao accountDao = new AccountDao();
	            AccountsDto account = accountDao.findAccountByUserId(user.getId());
	            if (account != null) {
	                loadLatestTransactions(account.getId());
	            }
	        }
	    }
	}
	
	private void loadAccountData() {
	    String userName = Session.loggedInUserName;
	    if (userName != null) {
	        UserDao userDao = new UserDao();
	        UserDto user = userDao.findByUserName(userName);

	        if (user != null) {
	            AccountDao accountDao = new AccountDao();
	            AccountsDto account = accountDao.findAccountByUserId(user.getId());
	            if (account != null) {
	            	lblCheckingAccountNumber.setText(maskCardNumberFancy(account.getCardNumber()));
	                lblCheckingAccountAllMoney.setText(account.getBalance() + " " + account.getCurrency());
	            } else {
	                lblCheckingAccountNumber.setText("Nincs számla");
	                lblCheckingAccountAllMoney.setText("0 HUF");
	            }
	        }
	    }
	}

	private void startClock() {
	    Timeline timeline = new Timeline(
	        new KeyFrame(Duration.ZERO, e -> updateTime()),
	        new KeyFrame(Duration.seconds(60))
	    );
	    timeline.setCycleCount(Timeline.INDEFINITE);
	    timeline.play();
	}

	private void updateTime() {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MMMM dd. - HH:mm");
	    String formattedDate = LocalDateTime.now().format(formatter);
	    lblWelcomeDate.setText(formattedDate);
	}
	
	private void getWelcomeMessageName() {
	    String userName = Session.loggedInUserName;

	    if (userName != null) {
	        UserDao userDao = new UserDao();
	        String firstName = userDao.getFirstNameByUsername(userName);
	        lblWelcomeMessageName.setText(firstName + "!");
	    } else {
	        lblWelcomeMessageName.setText("Vendég!");
	    }
	}
	
	private String maskCardNumberFancy(String cardNumber) {
	    if (cardNumber == null || cardNumber.length() < 4) return "****";

	    String visible = cardNumber.substring(cardNumber.length() - 4);
	    return "•••• •••• •••• " + visible;
	}
	
	private void setCurrentMontSummaryTitle() {
		DateTimeFormatter formatted = DateTimeFormatter.ofPattern("yyyy. MMMM", new Locale("hu"));
		String currentMonth = LocalDate.now().format(formatted);
		lblAccountSummaryThisMonth.setText(currentMonth);
	}
	
	private void loadMonthlySummary() {
	    String userName = Session.loggedInUserName;

	    if (userName != null) {
	        UserDao userDao = new UserDao();
	        AccountDao accountDao = new AccountDao();
	        TransactionDao transactionDao = new TransactionDao();

	        UserDto user = userDao.findByUserName(userName);

	        if (user != null) {
	            AccountsDto account = accountDao.findAccountByUserId(user.getId());

	            if (account != null) {
	                double income = transactionDao.getMonthlyIncome(account.getId());
	                double expense = transactionDao.getMonthlyExpense(account.getId());

	                lblAccountSummaryIncomeResult.setText(String.format("+ %.2f HUF", income));
	                lblAccountSummaryExpensesResult.setText(String.format("- %.2f HUF", expense));
	            }
	        }
	    }
	}
	
	@FXML
	private void sendTransaction() {
	    lblResultTransaction.setText("");
	    lblResultTransaction.getStyleClass().removeAll("success", "error");

	    String payeeAccountNumber = txtPayeeAdressTransaction.getText();
	    String amountText = txtAmountTransaction.getText();
	    String message = txtMessageTransaction.getText();

	    if (payeeAccountNumber.isEmpty() || amountText.isEmpty()) {
	        lblResultTransaction.setText("Hiányzó mezők!");
	        lblResultTransaction.getStyleClass().add("error");
	        return;
	    }

	    try {
	        double amount = Double.parseDouble(amountText);
	        if (amount <= 0) {
	            lblResultTransaction.setText("Az összegnek pozitívnak kell lennie!");
	            lblResultTransaction.getStyleClass().add("error");
	            return;
	        }

	        UserDao userDao = new UserDao();
	        AccountDao accountDao = new AccountDao();
	        UserDto senderUser = userDao.findByUserName(Session.loggedInUserName);
	        AccountsDto senderAccount = accountDao.findAccountByUserId(senderUser.getId());
	        AccountsDto receiverAccount = accountDao.findByAccountNumber(payeeAccountNumber);

	        if (receiverAccount == null) {
	            lblResultTransaction.setText("A megadott számlaszám nem létezik.");
	            lblResultTransaction.getStyleClass().add("error");
	            return;
	        }

	        if (senderAccount.getId().equals(receiverAccount.getId())) {
	            lblResultTransaction.setText("Nem utalhatsz saját magadnak!");
	            lblResultTransaction.getStyleClass().add("error");
	            return;
	        }

	        if (senderAccount.getBalance() < amount) {
	            lblResultTransaction.setText("Nincs elég fedezet a tranzakcióhoz.");
	            lblResultTransaction.getStyleClass().add("error");
	            return;
	        }

	        TransactionDao transactionDao = new TransactionDao();
	        transactionDao.transferAmount(senderAccount.getId(), receiverAccount.getId(), amount, message);

	        loadAccountData();
	        loadMonthlySummary();

	        lblResultTransaction.setText("Sikeres átutalás!");
	        Long accountId = getCurrentAccountId();
	        if (accountId != null) {
	            loadLatestTransactions(accountId);
	        }
	        lblResultTransaction.getStyleClass().add("success");

	    } catch (NumberFormatException e) {
	        lblResultTransaction.setText("Érvénytelen összeg formátum.");
	        lblResultTransaction.getStyleClass().add("error");
	    } catch (Exception e) {
	        lblResultTransaction.setText("Ismeretlen hiba történt.");
	        lblResultTransaction.getStyleClass().add("error");
	        e.printStackTrace();
	    }
	}
	
	private String getUsernameByAccountId(Long accountId) {
	    AccountDao accountDao = new AccountDao();
	    AccountsDto account = accountDao.findAccountById(accountId);
	    if (account != null) {
	        UserDao userDao = new UserDao();
	        UserDto user = userDao.findUserById(account.getUserId());
	        return user != null ? user.getUserName() : "ismeretlen";
	    }
	    return "ismeretlen";
	}

	private void loadLatestTransactions(Long accountId) {
	    anchPaneLatesTransaction1.setVisible(false);
	    anchPaneLatesTransaction1.setManaged(false);
	    anchPaneLatesTransaction2.setVisible(false);
	    anchPaneLatesTransaction2.setManaged(false);
	    anchPaneLatesTransaction3.setVisible(false);
	    anchPaneLatesTransaction3.setManaged(false);
	    anchPaneLatesTransaction4.setVisible(false);
	    anchPaneLatesTransaction4.setManaged(false);

	    TransactionDao transactionDao = new TransactionDao();
	    List<TransactionsDto> transactions = transactionDao.getLatestTransactionsForAccount(accountId, 4);

	    for (int i = 0; i < transactions.size(); i++) {
	        TransactionsDto t = transactions.get(i);

	        String date = t.getTimeStamp().toLocalDate().toString();
	        String from = "@" + getUsernameByAccountId(t.getFromAccount());
	        String to = "@" + getUsernameByAccountId(t.getToAccount());
	        String amountText = String.format("%.0f HUF", t.getAmount());
	        String message = (t.getMessage() != null && !t.getMessage().isEmpty()) ? t.getMessage() : "Nincs megjegyzés";
	        boolean isOutgoing = t.getFromAccount().equals(accountId);

	        Tooltip tooltip = new Tooltip(message);

	        switch (i) {
	            case 0 : {
	                anchPaneLatesTransaction1.setVisible(true);
	                anchPaneLatesTransaction1.setManaged(true);

	                lblLatesTransactionsDate1.setText(date);
	                lblLatesTransactionsFromName1.setText(from);
	                lblLatesTransactionsToName1.setText(to);
	                lblLatesTransactionsMoney1.setText(amountText);
	                fontAwesomeIconArrowLeft1.setFill(isOutgoing ? Color.CRIMSON : Color.web("#90a4ae"));
	                fontAwesomeIconArrowRight1.setFill(isOutgoing ? Color.web("#90a4ae") : Color.LIMEGREEN);
	                Tooltip.install(fontAwesomeIconBell1, tooltip);
	                break;
	            }
	            case 1 : {
	                anchPaneLatesTransaction2.setVisible(true);
	                anchPaneLatesTransaction2.setManaged(true);

	                lblLatesTransactionsDate2.setText(date);
	                lblLatesTransactionsFromName2.setText(from);
	                lblLatesTransactionsToName2.setText(to);
	                lblLatesTransactionsMoney2.setText(amountText);
	                fontAwesomeIconArrowLeft2.setFill(isOutgoing ? Color.CRIMSON : Color.web("#90a4ae"));
	                fontAwesomeIconArrowRight2.setFill(isOutgoing ? Color.web("#90a4ae") : Color.LIMEGREEN);
	                Tooltip.install(fontAwesomeIconBell2, tooltip);
	                break;
	            }
	            case 2 : {
	                anchPaneLatesTransaction3.setVisible(true);
	                anchPaneLatesTransaction3.setManaged(true);

	                lblLatesTransactionsDate3.setText(date);
	                lblLatesTransactionsFromName3.setText(from);
	                lblLatesTransactionsToName3.setText(to);
	                lblLatesTransactionsMoney3.setText(amountText);
	                fontAwesomeIconArrowLeft3.setFill(isOutgoing ? Color.CRIMSON : Color.web("#90a4ae"));
	                fontAwesomeIconArrowRight3.setFill(isOutgoing ? Color.web("#90a4ae") : Color.LIMEGREEN);
	                Tooltip.install(fontAwesomeIconBell3, tooltip);
	                break;
	            }
	            case 3 : {
	                anchPaneLatesTransaction4.setVisible(true);
	                anchPaneLatesTransaction4.setManaged(true);

	                lblLatesTransactionsDate4.setText(date);
	                lblLatesTransactionsFromName4.setText(from);
	                lblLatesTransactionsToName4.setText(to);
	                lblLatesTransactionsMoney4.setText(amountText);
	                fontAwesomeIconArrowLeft4.setFill(isOutgoing ? Color.CRIMSON : Color.web("#90a4ae"));
	                fontAwesomeIconArrowRight4.setFill(isOutgoing ? Color.web("#90a4ae") : Color.LIMEGREEN);
	                Tooltip.install(fontAwesomeIconBell4, tooltip);
	                break;
	            }
	        }
	    }
	}

	private Long getCurrentAccountId() {
	    String userName = Session.loggedInUserName;
	    if (userName != null) {
	        UserDao userDao = new UserDao();
	        AccountDao accountDao = new AccountDao();
	        UserDto user = userDao.findByUserName(userName);
	        if (user != null) {
	            AccountsDto account = accountDao.findAccountByUserId(user.getId());
	            if (account != null) {
	                return account.getId();
	            }
	        }
	    }
	    return null;
	}

	@FXML
	private void handleLogout() {
	    try {
	        javafx.scene.Node currentRoot = borderPaneMain;
	        javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(Duration.millis(400), currentRoot);
	        fadeOut.setFromValue(1.0);
	        fadeOut.setToValue(0.0);
	        fadeOut.setOnFinished(event -> {
	            try {
	                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/application/fxml/Login.fxml"));
	                javafx.scene.Parent loginRoot = loader.load();
	                
	                javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(Duration.millis(400), loginRoot);
	                fadeIn.setFromValue(0.0);
	                fadeIn.setToValue(1.0);

	                javafx.stage.Stage stage = (javafx.stage.Stage) currentRoot.getScene().getWindow();
	                stage.setScene(new javafx.scene.Scene(loginRoot));
	                fadeIn.play();

	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        });
	        fadeOut.play();
	        Session.loggedInUserName = null;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
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
