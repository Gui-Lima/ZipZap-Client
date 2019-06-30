package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import Client.Connection;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Client {
    @FXML
    Button ConnectButton;
    @FXML
    Label StatusLabel;
    @FXML
    Button StartChatButton;
    @FXML
    TextField PortTextField;
    @FXML
    Label numeroPorta;

    private Connection connection;

    private void connectToServer() throws IOException {
        connection = new Connection();
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
    public void changePort() {
    }

    public void handleStartButton() {
        try {
            if (StartChatButton.getOpacity() == 1.0) {
                String port = PortTextField.getText();
                if (connectToUser(Integer.valueOf(port))) {
                    this.createChat();
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

    private void createChat () throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Resources/Chat.fxml"));

        Parent root = (Parent)fxmlLoader.load();
        Chat chat = fxmlLoader.<Chat>getController();
        chat.setConnection(this.connection);
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
