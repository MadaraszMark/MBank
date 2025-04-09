package application.controller;

import application.dao.AccountDao;
import application.dao.UserDao;
import application.dto.AccountsDto;
import application.dto.UserDto;
import application.session.Session;
import application.util.SceneSwitcher;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDepositController {

    @FXML private VBox VBoxAdminDepositSideMenu;
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
    @FXML private AnchorPane anpAnchorPaneAdminDeposit;
    @FXML private HBox HBoxAdminDepositSearch;
    @FXML private Label lblAdminDepositSearchTitle;
    @FXML private TextField txtAdminDepostSearchByNumber;
    @FXML private Button btnAdminDepositSearchByNumber;
    @FXML private FontAwesomeIconView fontFontAwesomeIconViewSearch;
    @FXML private VBox VBoxAdminDepositAmount;
    @FXML private Label lblAdminDepositSearchResult;
    @FXML private Label lblAdminDepositTitle;
    @FXML private Label lblAdminDepositAmount;
    @FXML private TextField txtAdminDepositAmount;
    @FXML private Button btnAdminDeposit;
    @FXML private Label lblAdminDepositResult;
    @FXML private FontAwesomeIconView fontFontAwesomeIconViewMoney;

    private AccountsDto foundAccount;

    @FXML
    private void initialize() {
        btnAdminDepositSearchByNumber.setOnAction(event -> searchByAccountNumber());
        btnAdminDeposit.setOnAction(event -> depositToAccount());
    }

    private void searchByAccountNumber() {
        String accountNumber = txtAdminDepostSearchByNumber.getText().trim();
        lblAdminDepositSearchResult.setText("");
        lblAdminDepositResult.setText("");
        lblAdminDepositResult.getStyleClass().removeAll("error", "success");

        if (accountNumber.isEmpty()) {
            lblAdminDepositResult.setText("Add meg a keresett bankszámlaszámot!");
            lblAdminDepositResult.getStyleClass().add("error");
            return;
        }

        AccountDao accountDao = new AccountDao();
        UserDao userDao = new UserDao();

        foundAccount = accountDao.findByAccountNumber(accountNumber);

        if (foundAccount == null) {
            lblAdminDepositSearchResult.setText("");
            lblAdminDepositResult.setText("Nincs ilyen bankszámla.");
            lblAdminDepositResult.getStyleClass().add("error");
        } else {
            UserDto user = userDao.findUserById(foundAccount.getUserId());
            if (user != null) {
                String info = String.format(
                    "👤 %s (%s %s)\n📧 %s\n📱 %s\n💳 Számlaszám: %s\n💰 Egyenleg: %.2f %s",
                    user.getUserName(),
                    user.getLastName(),
                    user.getFirstName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    foundAccount.getCardNumber(),
                    foundAccount.getBalance(),
                    foundAccount.getCurrency()
                );
                lblAdminDepositSearchResult.setText(info);
            }
        }
    }

    private void depositToAccount() {
        lblAdminDepositResult.setText("");
        lblAdminDepositResult.getStyleClass().removeAll("error", "success");

        if (foundAccount == null) {
            lblAdminDepositResult.setText("Előbb keress rá egy számlára!");
            lblAdminDepositResult.getStyleClass().add("error");
            return;
        }

        String amountText = txtAdminDepositAmount.getText().trim();
        if (amountText.isEmpty()) {
            lblAdminDepositResult.setText("Add meg a befizetni kívánt összeget!");
            lblAdminDepositResult.getStyleClass().add("error");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                lblAdminDepositResult.setText("A befizetett összegnek pozitívnak kell lennie.");
                lblAdminDepositResult.getStyleClass().add("error");
                return;
            }

            AccountDao accountDao = new AccountDao();
            double newBalance = foundAccount.getBalance() + amount;
            accountDao.updateBalance(foundAccount.getId(), newBalance);

            foundAccount.setBalance(newBalance);
            txtAdminDepositAmount.clear();

            lblAdminDepositResult.setText("Sikeres befizetés! ");
            lblAdminDepositResult.getStyleClass().add("success");

        } catch (NumberFormatException e) {
            lblAdminDepositResult.setText("Érvénytelen összeg.");
            lblAdminDepositResult.getStyleClass().add("error");
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
}
