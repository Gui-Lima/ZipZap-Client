package Controllers;

import Models.Message;
import Models.Type;
import Client.Connection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class Chat{
    @FXML
    Button SendMessageToUserButton;
    @FXML
    TextField MessageTextField;

    private Connection connection;

public void handleSendMessageToUserButton() {
        String msg = this.MessageTextField.getText();
        Message message = new Message(msg, this.connection.getToPort(), this.connection.getFromPort(), Type.MESSAGE);
        try {
            this.connection.sendMessageToUser(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


}
