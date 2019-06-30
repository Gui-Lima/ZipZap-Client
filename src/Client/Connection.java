package Client;

import Models.Message;
import Models.Type;
import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.Socket;

public class Connection {
    static final int SERVER_ADDRESS = 9000;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private int toPort;
    private int fromPort;

    public String getToPort() {
        return String.valueOf(this.toPort);
    }

    public String getFromPort() {
        return String.valueOf(this.fromPort);
    }

    public void connectToServer() throws IOException {
        socket = new Socket("localhost", SERVER_ADDRESS);
    }

    public void disconnectFromServer() throws IOException {
        socket.close();
    }

    public void establishConnectionToUser(int port) throws IOException {
        input  = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());

        Message message = new Message("", String.valueOf(port), String.valueOf(socket.getPort()), Type.CONNECT);
        output.writeUTF(message.toString());



    }

    public void sendMessageToUser(Message message) throws IOException{
        output.writeUTF(message.toString());
    }

    public void finishConnectionToUser() throws IOException{
        input.close();
        output.close();
    }
}
