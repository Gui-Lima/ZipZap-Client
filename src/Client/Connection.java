package Client;

import Models.Message;
import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.Socket;

public class Connection {
    static final int SERVER_ADDRESS = 9000;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public void connectToServer() throws IOException {
        socket = new Socket("localhost", SERVER_ADDRESS);
    }

    public void disconnectFromServer() throws IOException{
        socket.close();
    }

    public boolean stablishConnectionToUser(int port) throws IOException {
        input  = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        String message = String.valueOf(port);
        output.writeUTF(message);
        String result = input.readUTF();
        System.out.println(result);
        if(result.equals(String.valueOf(port))){
            return true;
        }
        return false;
    }

    public void sendMessageToUser(Message messsage) throws IOException{
        //output.writeUTF(message.toString());
    }

    public void finishConnectionToUser() throws IOException{
        input.close();
        output.close();
    }
}
