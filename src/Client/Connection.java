package Client;

import Models.Message;
import Models.Type;
import java.io.*;
import java.net.Socket;

public class Connection {
    static final int SERVER_ADDRESS = 9000;
    private Socket socket;
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
        this.output = new DataOutputStream(socket.getOutputStream());
        Connection_Receiver receiver = new Connection_Receiver(socket);
        new Thread(receiver).start();
    }

    public void disconnectFromServer() throws IOException {
        socket.close();
    }

    public void establishConnectionToUser(int port) throws IOException {
        this.toPort = port;
        output = new DataOutputStream(socket.getOutputStream());
        Message message = new Message("aaa", String.valueOf(port), String.valueOf(socket.getPort()), Type.CONNECT);
        output.writeUTF(message.toString());
    }

    public void sendMessageToUser(Message message) throws IOException{
        output.writeUTF(message.toString());
    }

    public void finishConnectionToUser() throws IOException{
        output.close();
    }
}
