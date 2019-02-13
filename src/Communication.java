import java.util.ArrayList;

public class Communication {
    private static ArrayList<Message>[] sharedMessage;

    public Communication(int n) {
        this.sharedMessage = new ArrayList[n];
    }

    public ArrayList<Message> getMessage(int index){
        return this.sharedMessage[index];
    }

    public void sendMessage(Message m, int index){
        if(index >= sharedMessage.length){
            this.sharedMessage[index] = new ArrayList<Message>();
        }else{
            this.sharedMessage[index].add(m);
        }

    }
}
