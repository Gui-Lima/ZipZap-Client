package Controllers;

import Interfaces.Observer;
import Models.Message;
import Models.Status;
import Models.Type;
import Client.Connection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.HashMap;


public class Chat implements Observer {
    @FXML
    Button SendMessageToUserButton;
    @FXML
    TextField MessageTextField;
    @FXML
    ListView MessagesListView;

    private Connection connection;
    private ArrayList<Message> messages = new ArrayList<>();


    public void setMessages(ArrayList<Message> messages){
        this.messages = messages;
    }

    public void handleSendMessageToUserButton() {
        String msg = this.MessageTextField.getText();
        Message message = new Message(Type.MESSAGE, Status.NOT_SENT,
                this.connection.getFromPort(), this.connection.getToPort(), msg);
        try {
            this.connection.sendMessageToUser(message);
            this.messages.add(message);
            this.updateMessageList();
            MessageTextField.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void notifyConnectionEstablished(int port) {
        //System.out.println("algo mudou");
    }

    @Override
    public void notifyUserConnected(int port) {

    }

    @Override
    public void notifyMessageReceived(Message message) {
        this.messages.add(message);
    }

    public void updateMessageList(){
        ObservableList<Message> evt = FXCollections.observableArrayList(this.messages);
        MessagesListView.setItems(evt);
    }

    public void initialize(){
        this.updateMessageList();
    }
}
