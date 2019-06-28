package Client;

import jdk.internal.util.xml.impl.Input;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection {
    static final int SERVER_ADDRESS = 9000;
    private Socket socket;
    private InputStream input;
    private DataOutputStream output;

    public void connectToServer() throws IOException {
        socket = new Socket("localhost", SERVER_ADDRESS);
    }

    public void disconnectFromServer() throws IOException{
        socket.close();
    }

    public void stablishConnectionToUser(int port) throws IOException {
        input  = socket.getInputStream();
        output = new DataOutputStream(socket.getOutputStream());
        String message = String.valueOf(port);
        output.writeUTF(message);
    }

    public void sendMessageToUsaer() throws IOException{

    }

    public void finishConnectionToUser() throws IOException{
        
    }
}
