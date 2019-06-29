package Models;

public class Message {

    public Type type;
    private String fromPort;
    private String toPort;
    private String text;

    public Message(String text, String toPort, String fromPort, Type type) {
        this.type = type;
        this.fromPort = fromPort;
        this.toPort = toPort;
        this.text = text;
    }

    public Message(String encrypt) {
        String[] arr = encrypt.split("||");

        this.type = Type.valueOf(arr[0]);
        this.fromPort = arr[1];
        this.toPort = arr[2];
        this.text = arr[3];
    }

    public String toString() {
        StringBuilder message = new StringBuilder();
        message.append(this.type.toString()).append("||")
                .append(this.fromPort).append("||")
                .append(this.toPort).append("||")
                .append(this.text);
        return message.toString();
    }

    public void setText(String str) {
        this.text = str;
    }

    public void setToPort(int port) {
        this.toPort = Integer.toString(port);
    }

    public void setFromPort(int port) {
        this.fromPort = Integer.toString(port);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getText() {
        return this.text;
    }

    public int getToPort() {
        return Integer.parseInt(this.toPort);
    }

    public int getFromPort() {
        return Integer.parseInt(this.fromPort);
    }

    public Type getType() {
        return this.type;
    }
}
