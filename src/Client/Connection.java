package Client;

import Models.Message;
import Models.Status;
import Models.Type;
import java.io.*;
import java.net.Socket;

public class Connection {
    static final int SERVER_ADDRESS = 9000;
    private Socket socket;
    private DataOutputStream output;
    private int toPort;
    private int fromPort;

    public int getToPort() {
        return this.toPort;
    }

    public int getFromPort() {
        return this.fromPort;
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
        Message message = new Message(Type.CONNECT, Status.NOT_SENT, this.fromPort, this.toPort, "connecting");
        output.writeUTF(message.toString());
    }

    public void sendMessageToUser(Message message) throws IOException{
        output.writeUTF(message.toString());
    }

    public void finishConnectionToUser() throws IOException{
        output.close();
    }
}
