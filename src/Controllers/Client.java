package Controllers;

import Interfaces.Observer;
import Models.Message;
import Models.Status;
import Models.Type;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import Client.Connection;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Client implements Observer {
    @FXML
    Button ConnectButton;
    @FXML
    Label StatusLabel;
    @FXML
    Button StartChatButton;
    @FXML
    TextField PortTextField;
    @FXML
    Label PortNumber;
    @FXML
    ListView LatestMessagesListView;

    private HashMap<Integer, ArrayList<Message>> chatStatus = new HashMap<>();
    private ArrayList<String> eventos = new ArrayList<>();
    static final String pathToChat = "../Resources/Chat.fxml";
    private Connection connection;
    private boolean chatOpen = false;


    private void connectToServer() throws IOException {
        connection = new Connection();
        connection.addListener(this);
        connection.connectToServer();
    }

    private void disconnectToServer() throws IOException {
        connection.disconnectFromServer();
    }

    public void handleConnectButton(){
        if (ConnectButton.getText().equals("Connect")){
            try{
                connectToServer();
                StatusLabel.setText("Connected!");
                ConnectButton.setText("Disconnect");
                StartChatButton.setOpacity(1.0);

            } catch (Exception e){
                e.printStackTrace();
                StatusLabel.setText("Disconnected");
                ConnectButton.setText("Connect");
                StartChatButton.setOpacity(0.4);
            }
        }
        else if(ConnectButton.getText().equals("Disconnect")){
            try{
                disconnectToServer();
                StatusLabel.setText("Disconnected");
                ConnectButton.setText("Connect");
                StartChatButton.setOpacity(0.4);
            }catch (Exception e){
                e.printStackTrace();
                StatusLabel.setText("Connected");
                ConnectButton.setText("Disconnect");
                StartChatButton.setOpacity(1.0);
            }
        }
    }

    public void setPortNumberLabel() {
         PortNumber.setText(String.valueOf(connection.getFromPort()));
    }

    public void portAndConnect() {
        handleConnectButton();
        setPortNumberLabel();
    }

    public void handleStartButton() {
        try {
            if (StartChatButton.getOpacity() == 1.0) {
                String port = PortTextField.getText();
                if (connectToUser(Integer.valueOf(port))) {
                    this.createChat(Integer.valueOf(port));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean connectToUser(int port){
        try {
            connection.establishConnectionToUser(port);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void printmap(){
        for(Integer i : this.chatStatus.keySet()){
            System.out.println(i);
            for (Message m : this.chatStatus.get(i)){
                System.out.println(m);
            }
        }
    }

    public void updateMapRemove(Message message, int port){
        if(!chatStatus.containsKey(port)){

        }
        else{
            Message m = new Message(message);
            m.setText("MENSAGEM APAGADA");
            this.chatStatus.get(port).set(this.findInList(message, this.connection.getToPort()), m);
        }
    }

    public void updateMapAdd(Message message, int port){
        if(!chatStatus.containsKey(port)){
            ArrayList<Message> messages = new ArrayList<>();
            messages.add(message);
            this.chatStatus.put(port, messages);
        }
        else{
            ArrayList<Message> messages = this.chatStatus.get(port);
            messages.add(message);
            this.chatStatus.replace(port, messages);
        }
    }

    private void createChat (int port) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(pathToChat));
        Parent root = (Parent)fxmlLoader.load();
        Chat chat = fxmlLoader.<Chat>getController();
        chat.setConnection(this.connection);
        chat.setParent(this);
        if(this.chatStatus.containsKey(this.connection.getToPort())){
            chat.setMessages(this.chatStatus.get(connection.getToPort()));
        }
        this.chatOpen = true;
        this.updateMessageListStatus(port);
        connection.addListener(chat);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setOnHiding( event -> {this.chatOpen = false;} );
        stage.setScene(scene);
        stage.show();
    }

    private void updateMessageListStatus(int port) {
        ArrayList<Message> tradedMessagesWithUser = this.chatStatus.get(port);
        if(tradedMessagesWithUser != null){
            for (Message m : tradedMessagesWithUser){
                if(m.getFromPort() == port){
                    this.sendStatusUpdateMessage(m);
                }
            }
        }
    }

    private int findInList(Message message, int port){
        int i =0;
        if(this.chatStatus.containsKey(port)){
            for(Message m: this.chatStatus.get(port)){
                if(m.equals(message)){
                    return i;
                }
                i++;
            }
        }
        return 0;
    }

    @Override
    public void notifyConnectionEstablished(int port) {
        this.eventos.add("You are connected to: " + port );
        ObservableList<String> evt = FXCollections.observableArrayList(this.eventos);
        LatestMessagesListView.setItems(evt);
    }

    @Override
    public void notifyUserConnected(int port) {
        Platform.runLater(
                () -> {
                    this.eventos.add("This User Connected to you: " + port );
                    ObservableList<String> evt = FXCollections.observableArrayList(this.eventos);
                    LatestMessagesListView.setItems(evt);
                }
        );
    }

    @Override
    public void notifyMessageDeletion(Message message){
        Message m = new Message(message);
        m.setText("MENSAGEM APAGADA");
        this.chatStatus.get(message.getFromPort()).set(this.findInList(message, this.connection.getToPort()), m);
    }

    @Override
    public void notifyStatusUpdate(Message message) {
        System.out.println("Received a status update ");
        this.printmap();
        int index;
        if(message.getStatus() == Status.DOUBLE_CHECK_A){
            index = message.getFromPort();
        }
        else{
            index = message.getToPort();
        }

        int i =0;
        boolean found = false;
        if(this.chatStatus.containsKey(index)){
            for (Message m : this.chatStatus.get(index)){
                if (m.getId() == message.getId()){
                    found = true;
                    break;
                }
                i++;
            }
            if (found){
                this.chatStatus.get(index).set(i, message);
            }
        }
        this.printmap();
    }

    @Override
    public void notifyMessageReceived(Message message) {
        System.out.println ("The message was " + message);
        System.out.println("I'm adding it to the message queue of the client");
        if(this.chatOpen) this.sendStatusUpdateMessage(message);
        if(!chatStatus.containsKey(message.getFromPort())){
            System.out.println("Creating a new Queue since it's the first message to the client from the user");
            ArrayList<Message> messages = new ArrayList<>();
            messages.add(message);
            this.chatStatus.put(message.getFromPort(), messages);
        }
        else{
            System.out.println("Appending to the existing messages:");
            ArrayList<Message> messages = this.chatStatus.get(message.getFromPort());
            messages.add(message);
            this.chatStatus.replace(message.getFromPort(), messages);
        }
    }

    private void sendStatusUpdateMessage(Message message) {
        try {
            Message m = new Message(message);
            m.setStatus(Status.DOUBLE_CHECK_A);
            m.setType(Type.STATUS_UPDATE);
            this.connection.sendMessageToUser(m);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
