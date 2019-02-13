import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

class Process implements Runnable{

    private int UID;
    private boolean marked;
    private ArrayList<Integer> neighbors;
    private int process_index;
    private Communication channel;
    private BlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);

    public Process(int UID, boolean is_marked, ArrayList<Integer> neighbors, int process_index, Communication channel) {
        this.UID = UID;
        this.marked = is_marked;
        this.neighbors = new ArrayList<>(neighbors);
        this.process_index = process_index;
        this.channel = channel;
    }

    public boolean is_marked() {
        return marked;
    }

    public void set_marked(boolean is_marked) {
        this.marked = is_marked;
    }

    public ArrayList<Integer> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<Integer> neighbors) {
        this.neighbors = neighbors;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public int getProcess_index() {
        return process_index;
    }

    public void setProcess_index(int process_index) {
        this.process_index = process_index;
    }
    @Override
    public void run(){
            Thread.currentThread().setName("" + process_index);
            System.out.println("My UID is : " + UID);
            System.out.println("Current Thread name : " + Thread.currentThread().getName());

    }
}