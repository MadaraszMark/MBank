package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.paint.Color;

public class TransactionsItemController {

    @FXML private Label lblTransactionsDate;
    @FXML private Label lblTransactionsToName;
    @FXML private Label lblTransactionsFromName;
    @FXML private Label lblTransactionsMoney;
    @FXML private FontAwesomeIconView fontAwesomeIconArrowLeft;
    @FXML private FontAwesomeIconView fontAwesomeIconArrowRight;
    @FXML private FontAwesomeIconView fontAwesomeIconBell;

    public void setTransactionData(String date, String from, String to, String amount, boolean isIncoming, String message) {
        lblTransactionsDate.setText(date);
        lblTransactionsFromName.setText(from);
        lblTransactionsToName.setText(to);
        lblTransactionsMoney.setText(amount);
        fontAwesomeIconArrowLeft.setVisible(true);
        fontAwesomeIconArrowLeft.setManaged(true);
        fontAwesomeIconArrowRight.setVisible(true);
        fontAwesomeIconArrowRight.setManaged(true);

        if (isIncoming) {
            fontAwesomeIconArrowRight.setFill(Color.web("#4caf50"));
            fontAwesomeIconArrowLeft.setFill(Color.web("#B0B0B0"));
        } else {
            fontAwesomeIconArrowLeft.setFill(Color.web("#f44336"));
            fontAwesomeIconArrowRight.setFill(Color.web("#B0B0B0"));
        }
        String tooltipText = (message != null && !message.trim().isEmpty())
                ? message
                : "Nincs üzenet ehhez a tranzakcióhoz.";
        javafx.scene.control.Tooltip.install(fontAwesomeIconBell, new javafx.scene.control.Tooltip(tooltipText));

        fontAwesomeIconBell.setVisible(true);
        fontAwesomeIconBell.setManaged(true);
    }

}


