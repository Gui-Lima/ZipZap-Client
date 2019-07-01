package Client;

import Interfaces.Observer;
import Models.Message;
import Models.Status;
import Models.Type;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {
    static final int SERVER_ADDRESS = 9000;
    private Socket socket;
    private DataOutputStream output;
    private int toPort;
    private int fromPort;
    private ArrayList<Observer> listeners = new ArrayList<Observer>();


    public int getToPort() {
        return this.toPort;
    }

    public int getFromPort() {
        return this.fromPort;
    }

    public void deleteMessage(Message message) throws IOException {
        this.output.writeUTF(message.toString());
    }

    public void connectToServer() throws IOException {
        socket = new Socket("localhost", SERVER_ADDRESS);
        this.fromPort = socket.getLocalPort();
        this.output = new DataOutputStream(socket.getOutputStream());
        Connection_Receiver receiver = new Connection_Receiver(socket, this);
        new Thread(receiver).start();
    }

    public void disconnectFromServer() throws IOException {
        socket.close();
    }

    public void establishConnectionToUser(int port) throws IOException {
        output = new DataOutputStream(socket.getOutputStream());
        this.toPort = port;
        Message message = new Message(Type.CONNECT_TO, Status.NOT_SENT, this.fromPort, this.toPort, "connecting");
        output.writeUTF(message.toString());
        notifySomethingHappened(message);
    }

    public void sendMessageToUser(Message message) throws IOException{
        output.writeUTF(message.toString());
    }

    public void finishConnectionToUser() throws IOException{
        output.close();
    }

    public void addListener(Observer observer) {
        listeners.add(observer);
    }

    void notifySomethingHappened(Message message) {
        System.out.println("Something happened!");
        for(Observer observer : listeners) {
            if(message.getType() == Type.CONNECT_TO) {
                System.out.println(" It was a connection tryout");
                observer.notifyConnectionEstablished(message.getToPort());
            }
            else if(message.getType()== Type.RECEIVE_CONNECTION) {
                System.out.println(" It was a Connection tryout reciever");
                observer.notifyUserConnected(message.getFromPort());
            }
            else if(message.getType() == Type.MESSAGE_SEND) {
                System.out.println(" It was a Message to" + observer.toString());
                observer.notifyMessageReceived(message);
            }
            else if(message.getType() == Type.MESSAGE_DELETE){
                System.out.println("It was a deletion, wow ");
                observer.notifyMessageDeletion(message);
            }
        }
    }

}
