import java.net.Socket;
import java.util.ArrayList;

public class Message {
    public ArrayList<Socket> getSockets() {
        return sockets;
    }

    private ArrayList<String> message=new ArrayList<>();
    private ArrayList<Socket> sockets=new ArrayList<>();
    public synchronized void add(String src){
        message.add(src);
    }
    public synchronized void addSocket(Socket socket){
        sockets.add(socket);
    }
    public ArrayList<String> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<String> message) {
        this.message = message;
    }
    public void reset(){
        message.clear();
    }
}
