package Controllers;

import Models.Message;
import Models.Type;
import Client.Connection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.annotation.Resource;
import java.awt.*;
import java.io.IOException;


public class Chat{
    @FXML
    Button SendMessageToUserButton;
    @FXML
    TextField MessageTextField;

    static final String pathToChat = "../Resources/Chat.fxml";
    private Connection connection;

    public Chat(Connection connection) {
        this.connection = connection;
    }

    public void display(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pathToChat));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setTitle("Chat");
            stage.setScene(new Scene(root));
            stage.show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void handleSendMessageToUserButton() {
        String msg = MessageTextField.getText();
        Message message = new Message(msg, this.connection.getToPort(), this.connection.getFromPort(), Type.MESSAGE);
        try {
            this.connection.sendMessageToUser(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
