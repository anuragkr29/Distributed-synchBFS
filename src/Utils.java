import java.util.ArrayList;

public class Utils {

    public ArrayList<Integer> getNeighbors(int process_index, ArrayList<Integer> connectionMatrix){
        ArrayList<Integer> temp = new ArrayList<>();
        for(int i=0 ; i<connectionMatrix.size();i++){
            if(connectionMatrix.get(i) == 1 && i != process_index ){
                temp.add(i);
            }
        }
        return temp;
    }

}
