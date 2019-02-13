import java.util.ArrayList;
import java.util.HashMap;

public class Communication {
    public static HashMap<Integer,Process> processUIDMap;

    public Communication(HashMap<Integer,Process> h) {
        processUIDMap = h;
    }


    public void sendMessage(Message m, int UID, int[] neighbors){
        for (int n:neighbors) {
            Process neighbor = processUIDMap.get(UID);

        }

    }

}
