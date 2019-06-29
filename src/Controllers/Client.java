package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import Client.Connection;
import javafx.scene.control.TextField;

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

    public void handleStartButton(){
        if(StartChatButton.getOpacity() == 1.0){
            String port = PortTextField.getText();
            if (connectToUser(Integer.valueOf(port))){
                Chat chat = new Chat();
                chat.display();
            }

        }
    }

    private boolean connectToUser(int port){
        try {
            return connection.establishConnectionToUser(port);
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
