package Controllers;

import Interfaces.Observer;
import Models.Message;
import Models.Status;
import Models.Type;
import Client.Connection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    public void printChat(){
        for (Message m : messages){
            System.out.println(m.toString());
        }
    }

    public void setMessages(ArrayList<Message> messages){
        this.messages = (ArrayList<Message>) messages.clone();
    }

    @FXML public void handleMouseClick(MouseEvent arg0) {
        System.out.println("clicked on " + MessagesListView.getSelectionModel().getSelectedItem());
        String selectedMessage = (String) MessagesListView.getSelectionModel().getSelectedItem();
        for (Message m : this.messages){
            int id = Integer.valueOf(selectedMessage.substring(selectedMessage.indexOf("(") +1, selectedMessage.indexOf(")")));
            System.out.println(id);
            if(m.getId() == id){
                this.selectedMessage = m;
            }
        }
    }

    public void handleDeleteMessageButton(){
        if (this.selectedMessage.getFromPort() == this.connection.getFromPort()){
            try {
                this.selectedMessage.setType(Type.MESSAGE_DELETE);
                this.connection.deleteMessage(this.selectedMessage);
                this.messages.remove(this.selectedMessage);
                this.updateMessageList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleSendMessageToUserButton() {
        String msg = this.MessageTextField.getText();
        Message message = new Message(Type.MESSAGE_SEND, Status.NOT_SENT, this.connection.getFromPort(), this.connection.getToPort(), msg);
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
    }

    @Override
    public void notifyUserConnected(int port) {

    }

    @Override
    public void notifyMessageDeletion(Message message){
        System.out.println("DELETING MESSAGE WOW SUCH WOW MUCH AWESOME");
        this.messages.remove(message);
        this.updateMessageList();
    }

    @Override
    public void notifyMessageReceived(Message message) {
        this.messages.add(message);
        this.printChat();
        updateMessageList();
    }

    public void updateMessageList(){
        Platform.runLater(
                () -> {
                    ObservableList<String> evt = FXCollections.observableArrayList(transformIntoText(this.messages));
                    MessagesListView.setItems(evt);
                }
        );
    }

    public ArrayList<String> transformIntoText(ArrayList<Message> messages){
        ArrayList<String> textedMessages = new ArrayList<>();
        for (Message m : messages){
            textedMessages.add(m.show());
        }
        return textedMessages;
    }

    @FXML private void initialize(){

        Platform.runLater(() ->{
            System.out.println("Iniciando a tela do chat");
            this.printChat();
            this.updateMessageList();
            MessageTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if(keyEvent.getCode().equals(KeyCode.ENTER)){
                        handleSendMessageToUserButton();
                    }
                }
            });
        });
    }

}
