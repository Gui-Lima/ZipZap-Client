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
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;


public class Chat implements Observer {
    @FXML
    Button SendMessageToUserButton;
    @FXML
    TextField MessageTextField;
    @FXML
    ListView MessagesListView;
    @FXML
    Button DeleteMessageButton;

    private Connection connection;
    private Message selectedMessage;
    private ArrayList<Message> messages = new ArrayList<>();
    private Message deletedMessage;

    public void printChat(){
        for (Message m : messages){
            System.out.println(m.toString());
        }
    }

    public void setMessages(ArrayList<Message> messages){
        this.messages = messages;
    }

    @FXML public void handleMouseClick(MouseEvent arg0) {
        System.out.println("clicked on " + MessagesListView.getSelectionModel().getSelectedItem());
        this.selectedMessage = (Message) MessagesListView.getSelectionModel().getSelectedItem();
    }

    public void handleDeleteMessageButton(){
        try {
            String text = this.selectedMessage.getText();
            int fromPort = this.selectedMessage.getFromPort();
            int toPort = this.selectedMessage.getToPort();
            Status status = this.selectedMessage.getStatus();
            Message message = new Message(Type.MESSAGE_DELETE, status, fromPort, toPort, text);
            this.connection.deleteMessage(message);
            this.removeFromList(message);
            this.updateMessageList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSendMessageToUserButton() {
        String msg = this.MessageTextField.getText();
        Message message = new Message(Type.MESSAGE_SEND, Status.NOT_SENT,
                this.connection.getFromPort(), this.connection.getToPort(), msg);
        try {
            this.messages.add(message);
            this.updateMessageList();
            this.connection.sendMessageToUser(message);
            MessageTextField.clear();
            this.printChat();
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

    public void removeFromList(Message message){
        int i = 0;
        for (Message m : this.messages){
            if(message.getText().equals(m.getText())){
                break;
            }
            i++;
        }
        this.messages.remove(i);
    }

    @Override
    public void notifyMessageDeletion(Message message){
        System.out.println("DELETING MESSAGE WOW SUCH WOW MUCH AWESOME");
        this.removeFromList(message);
        this.updateMessageList();
    }

    @Override
    public void notifyMessageReceived(Message message) {
        this.printChat();
        this.messages.add(message);
        updateMessageList();
    }

    public void updateMessageList(){
        ObservableList<Message> evt = FXCollections.observableArrayList(this.messages);
        MessagesListView.setItems(evt);
    }

    @FXML private void initialize(){

        Platform.runLater(() ->{
            System.out.println("Iniciando a tela do chat");
            this.printChat();
            this.updateMessageList();
        });
    }
}
