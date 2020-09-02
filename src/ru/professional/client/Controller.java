package ru.professional.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    TextArea fldUsersText;
    @FXML
    TextArea fldClients;
    @FXML
    TextField fldClientText;
    @FXML
    Button btnSend;

    public void menuItemExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void btnSendAction(ActionEvent actionEvent) {
        Client.sendMessage(fldClientText.getText());
        fldClientText.clear();
    }


    public void menuItemConnectAction(ActionEvent actionEvent) {
        Client client = new Client(this);
        Thread thread = new Thread(client);
        thread.start();
    }
}
