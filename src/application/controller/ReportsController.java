package application.controller;

import application.dao.ReportsDao;
import application.dao.UserDao;
import application.dto.ReportsDto;
import application.dto.UserDto;
import application.session.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ReportsController {

    @FXML private TextArea txtReportsField;
    @FXML private Button btnReportsSend;

    @FXML
    private void initialize() {
        btnReportsSend.setOnAction(e -> sendReport());
    }

    private void sendReport() {
        String message = txtReportsField.getText().trim();

        if (message.isEmpty()) {
            showAlert("Hiba", "A mező nem lehet üres.");
            return;
        }

        try {
            String loggedInUsername = Session.loggedInUserName;
            UserDao userDao = new UserDao();
            UserDto user = userDao.findByUserName(loggedInUsername);

            if (user == null) {
                showAlert("Hiba", "Nem található felhasználó.");
                return;
            }

            ReportsDto report = new ReportsDto();
            report.setUserId(user.getId());
            report.setMessage(message);
            report.setSubmittedAt(java.time.LocalDateTime.now());

            ReportsDao dao = new ReportsDao();
            dao.saveReport(report);

            Stage stage = (Stage) btnReportsSend.getScene().getWindow();
            stage.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Hiba", "Hiba történt a mentés során.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}


