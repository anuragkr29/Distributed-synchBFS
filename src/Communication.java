import java.util.ArrayList;
import java.util.HashMap;

public class Communication {
    public static HashMap<Integer,Process> processUIDMap;
    private static int[] UIDs;

    public  Communication(HashMap<Integer,Process> h, int[] UIDs) {
        processUIDMap = h;
        this.UIDs = UIDs;
    }


    public synchronized static void sendMessage(Message m, ArrayList<Integer> neighbors){
        for (int n:neighbors) {
            Process neighbor = processUIDMap.get(UIDs[n]);
            send(m,neighbor);
        }
    }

    // polymorphic sendMessage for a single process communication
    public synchronized static void sendMessage(Message m, Integer neighborIndex){
            Process neighbor = processUIDMap.get(UIDs[neighborIndex]);
            send(m,neighbor);
    }

    private static void send(Message m , Process p){
//        System.out.println("putting message"+ m.getInUID() + " to neighbor : " + p.getUID());
        p.putMessage(m);
    }

}
