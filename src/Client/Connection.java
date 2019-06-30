package Client;

import Models.Message;
import Models.Type;
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
        this.fromPort = socket.getLocalPort();
    }

    public void disconnectFromServer() throws IOException {
        socket.close();
    }

    public void establishConnectionToUser(int port) throws IOException {
        this.toPort = port;
        input  = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        Message message = new Message("connecting", String.valueOf(port), String.valueOf(socket.getPort()), Type.CONNECT);
        output.writeUTF(message.toString());
    }

    public void sendMessageToUser(Message message) throws IOException{
        output.writeUTF(message.toString());
    }

    public void recieveMessageFromUser() throws IOException {
        System.out.println(input.readUTF());
    }

    public void finishConnectionToUser() throws IOException{
        input.close();
        output.close();
    }
}
