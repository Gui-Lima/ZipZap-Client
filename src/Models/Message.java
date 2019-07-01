package Models;

public class Message {

    private Type type;
    private Status status;
    private int fromPort;
    private int toPort;
    private String text;

    public Message(Type type, Status status, int fromPort, int toPort, String text) {
        this.type = type;
        this.status = status;
        this.fromPort = fromPort;
        this.toPort = toPort;
        this.text = text;
    }

    public Message(String encrypt) {
        String[] arr = encrypt.split("<=>");

        this.type = Type.valueOf(arr[0]);
        this.status = Status.valueOf(arr[1]);
        this.fromPort = Integer.parseInt(arr[2]);
        this.toPort = Integer.parseInt(arr[3]);
        this.text = arr[4];
    }

    @Override
    public String toString() {
        StringBuilder message = new StringBuilder();
        message.append(this.type.toString()).append("<=>")
                .append(this.status.toString()).append("<=>")
                .append(this.fromPort).append("<=>")
                .append(this.toPort).append("<=>")
                .append(this.text);
        return message.toString();
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Message){
            if(((Message) object).fromPort == this.fromPort && ((Message) object).toPort == this.toPort && ((Message) object).text.equals(this.text)){
                return true;
            }
        }
        return false;
    }

    public void setText(String str) {
        this.text = str;
    }

    public void setToPort(int port) {
        this.toPort = port;
    }

    public void setFromPort(int port) {
        this.fromPort = port;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getText() {
        return this.text;
    }

    public int getToPort() {
        return this.toPort;
    }

    public int getFromPort() {
        return this.fromPort;
    }

    public Type getType() {
        return this.type;
    }

    public Status getStatus() {
        return this.status;
    }
}
