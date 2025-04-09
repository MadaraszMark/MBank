package application.controller;

import java.io.IOException;
import java.util.Set;

import application.dao.AccountDao;
import application.dao.TransactionDao;
import application.dao.UserDao;
import application.dto.AccountsDto;
import application.dto.UserDto;
import application.session.Session;
import application.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AccountsController {
	
	@FXML private Button btnDashboard;
    @FXML private Button btnTransactions;
    @FXML private Button btnAccounts;
    @FXML private Button btnProfile;
    @FXML private Button btnReport;
    @FXML private Button btnLogout;
    @FXML private PieChart chartPieChartAccountStats;
    @FXML private Label lblAccountsNumberResult;
    @FXML private Label lblAccountsTypeResult;
    @FXML private Label lblAccountsMoneyResult;
    @FXML private Label lblAccountsStatusResult;
    @FXML private Label lblAccountsCreditCardNumberResult;
    @FXML private Label lblAccountsRegisteredDateResult;
    
    @FXML
    private void initialize() {
        loadStats();
        loadAccountInfo();
    }

    public void loadStats() {
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
            System.out.println("Felhasználó nem található.");
            return;
        }
        AccountsDto account = accountDao.findAccountByUserId(user.getId());
        if (account == null) {
            System.out.println("Számla nem található.");
            return;
        }

        Long accountId = account.getId();
        double income = transactionDao.getMonthlyIncome(accountId);
        double expense = transactionDao.getMonthlyExpense(accountId);

        PieChart.Data incomeData = new PieChart.Data("Bevétel: " + (int) income + " HUF", income);
        PieChart.Data expenseData = new PieChart.Data("Kiadás: " + (int) expense + " HUF", expense);

        chartPieChartAccountStats.getData().setAll(incomeData, expenseData);
        chartPieChartAccountStats.setPrefSize(650, 650);
        chartPieChartAccountStats.setMinSize(650, 650);
        chartPieChartAccountStats.setMaxSize(650, 650);
        chartPieChartAccountStats.setLegendVisible(true);
        chartPieChartAccountStats.setLabelsVisible(true);
        chartPieChartAccountStats.setStartAngle(90);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                incomeData.getNode().setStyle("-fx-pie-color: #00695c;");
                expenseData.getNode().setStyle("-fx-pie-color: #2f80ed;");

                Set<Node> legendItems = chartPieChartAccountStats.lookupAll(".chart-legend-item");
                for (Node item : legendItems) {
                    if (item instanceof Label) {
                        Label label = (Label) item;
                        String text = label.getText();
                        Node symbol = label.getGraphic();
                        if (text != null && symbol != null) {
                            if (text.contains("Bevétel")) {
                                symbol.setStyle("-fx-background-color: #00695c;");
                            } else if (text.contains("Kiadás")) {
                                symbol.setStyle("-fx-background-color: #2f80ed;");
                            }
                        }
                    }
                }
            }
        });
    }
    
    public void loadAccountInfo() {
        String userName = Session.loggedInUserName;
        if (userName == null) {
            System.out.println("Nincs bejelentkezett felhasználó.");
            return;
        }

        UserDao userDao = new UserDao();
        AccountDao accountDao = new AccountDao();

        UserDto user = userDao.findByUserName(userName);
        if (user == null) {
            System.out.println("Felhasználó nem található.");
            return;
        }

        AccountsDto account = accountDao.findAccountByUserId(user.getId());
        if (account == null) {
            System.out.println("Számla nem található.");
            return;
        }
        lblAccountsNumberResult.setText(user.getUserName());
        lblAccountsTypeResult.setText(account.getAccountTypeText());
        lblAccountsMoneyResult.setText(String.format("%.0f %s", account.getBalance(), account.getCurrency()));
        lblAccountsStatusResult.setText(account.getStatusText());
        lblAccountsCreditCardNumberResult.setText(account.getCardNumber());
        lblAccountsRegisteredDateResult.setText(account.getCreatedAt().toLocalDate().toString());
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
