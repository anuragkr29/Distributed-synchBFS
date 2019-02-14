import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

class Process implements Runnable{

    private int UID;
    private int rootUID;
    private int parentIndex;
    private boolean marked;
    private ArrayList<Integer> neighbors;
    private ArrayList<Integer> children;
    private int process_index;
    private BlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);
    private int level;
    private boolean leaf = false;
    private int count = 0;
    private boolean processCompleted = false;
    private int doneCount = 0;

    public Process(int UID, boolean is_marked, ArrayList<Integer> neighbors, int process_index, int rootUID) {
        this.UID = UID;
        this.marked = is_marked;
        this.neighbors = neighbors;
        this.children = new ArrayList<>();
        this.process_index = process_index;
        this.rootUID = rootUID;
        this.parentIndex = rootUID;
    }
    public void putMessage(Message m){
        queue.add(m);
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

    public boolean isProcessCompleted() {
        return processCompleted;
    }

    public void setProcess_index(int process_index) {
        this.process_index = process_index;
    }

    public void checkAndSetChild(Message m){
        if(m.isParent())
        {
            this.children.add(m.getProcess_index());
            this.queue.remove(m);
        }
    }

    public int processQueue(boolean isMyRound){
        // for parent selection
        int maxUIDIndex = -1;
        Message maxUIDMsg = new Message(false,-1,true,-1,Integer.MAX_VALUE);
        ArrayList<Integer> nonParent = new ArrayList<Integer>();
        try {
            while(!this.queue.isEmpty())
            {
                Message m = this.queue.take();
                if(m.isSearch() && isMyRound){
                    nonParent.add(m.getProcess_index());
                    maxUIDMsg = this.compareParent(maxUIDMsg,m);
                }
                else if(m.isSearch() && !isMyRound  && this.marked){
                    Message rejectMsg = new Message();
                    rejectMsg.setReject(true);
                    rejectMsg.setInUID(this.UID);
                    Communication.sendMessage(rejectMsg,m.getProcess_index());
                }
                else if(m.isParent() && !m.isRoot()){
                    this.children.add(m.getProcess_index());
                }
                else if(m.isReject()){
                    count++;
//                    System.out.println("Reject msg from : " + m.getInUID() + " to me : " + this.UID);
                    if(count == this.neighbors.size())
                    {
                        this.leaf = true;
                        this.sendDone(this.parentIndex);
                    }
                }
                else if(m.isRoot()){
                    return -1;
                }
                if(m.isDone()){
                    this.doneCount++;
                    if(this.doneCount == this.children.size())
                    {
                        if(this.UID == rootUID)
                        {
                            System.out.println("queue data for : " + this.UID + " count : " + this.doneCount + " child : " + this.children.size());
                            Thread.currentThread().sleep(500);
                            this.processCompleted = true;
                            System.out.println("Process completed for : " + this.UID);
                        }
                        else
                        {
                            this.sendDone(this.parentIndex);
                            Thread.currentThread().sleep(500);
                            this.processCompleted = true;
                            System.out.println("Process completed for : " + this.UID);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        maxUIDIndex = maxUIDMsg.getProcess_index();
        if(maxUIDIndex != -1){
            this.parentIndex = maxUIDIndex;
            nonParent.remove(nonParent.indexOf(maxUIDIndex));
//            System.out.println(" UID : " + this.UID + " Non Parent : " + nonParent);
            this.sendReject(nonParent);
        }
        return maxUIDIndex;
    }

    public Message compareParent(Message a , Message b){
        int level_a = a.getLevel();
        int level_b = b.getLevel();
        if(level_a < level_b){
            return a;
        }
        else if(level_a > level_b){
            return b;
        }
        else{
            int max =  Math.max(a.getProcess_index(), b.getProcess_index());
            if (max == a.getProcess_index())
                return a;
            else
                return b;
        }
    }

    public void sendReject(ArrayList<Integer> nonParent){
        Message rejectMsg = new Message();
        rejectMsg.setReject(true);
        rejectMsg.setInUID(this.UID);
        Communication.sendMessage(rejectMsg,nonParent);
    }

    public void sendDone(Integer recipient){
        Message rejectMsg = new Message();
        rejectMsg.setDone(true);
        rejectMsg.setInUID(this.UID);
        Communication.sendMessage(rejectMsg,recipient);
        if(this.children.size() == 0)
        {
            this.processCompleted = true;
            System.out.println("Process completed for : " + this.UID);
        }
    }
    public int selectParent(){
        int maxUIDIndex = -1;
        int maxUID = -1;
        List<Integer> nonParent = new ArrayList<Integer>();
        Message m=null;
        while (!this.queue.isEmpty()){
            try {
                m = this.queue.take();
                if(m.isSearch())
                {
                    int inUID = m.getInUID();
                    nonParent.add(m.getProcess_index());
                    if(inUID > maxUID)
                    {
                        maxUID = inUID;
                        maxUIDIndex = m.getProcess_index();
                    }

                }
                if(!m.isSearch() && !m.isRoot()){
                    this.checkAndSetChild(m);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        if(maxUID != -1)
        {
            this.parentIndex = maxUIDIndex;
            nonParent.remove(nonParent.indexOf(maxUID));
            for (int process_index:nonParent) {
                if(process_index != -1){
                    Message rejectMsg = new Message();
                    rejectMsg.setReject(true);
                    Communication.sendMessage(rejectMsg,process_index);
                }
            }
        }
        return maxUIDIndex;
    }
    @Override
    public void run(){
        Thread.currentThread().setName("" + process_index);
        int parentUID = 0;
        while(true)
        {
            if(Round.round.get(this.process_index) == 100)
            {

//                System.out.println("My UID is : " + UID);
//                System.out.println("My Neighbours : " + neighbors);
//                System.out.println("Current Thread name : " + Thread.currentThread().getName());
//                System.out.println("Array before : " + Round.round.get(this.process_index));
                int globalRoundNum = Round.getGlobalRoundNumber();
                try {
//                    System.out.println("My UID is : " + UID + " in round : " + globalRoundNum);
                    if(!this.queue.isEmpty())
                    {
                        Message m = this.queue.peek();

//                        if(!m.isSearch() && !m.isRoot()){
//                            this.checkAndSetChild(m);
//                        }
                        if(globalRoundNum != m.getRoundNum())
                            this.processQueue(false);

                        if(globalRoundNum == m.getRoundNum() && !this.marked)
                        {
                            this.level = globalRoundNum;
//                            System.out.println("Sending msgs to my neighbors : " + UID + "  Neighbors : "  + this.neighbors);

                            parentUID = this.processQueue(true);
                            if(parentUID != -1)
                            {
                                //this.neighbors.remove(this.neighbors.indexOf(parentUID));
//                                System.out.println("updated my neighbors : " + UID + "  Neighbors : "  + this.neighbors);
                                Message toSend = new Message(true,this.UID,false,this.process_index, this.level);
                                toSend.setRoundNum(0);
//                                System.out.println("sending message to parent " + parentUID + " my UID : " + this.UID );
                                Communication.sendMessage(toSend,parentUID);
//                                System.out.println("Message to  parent sent from UID : " + this.UID );
                            }

//                            if(m.isRoot())
//                            {
//                                this.queue.remove(m);
//                                Message toSend = new Message(false,this.UID,true);
//                                Thread.sleep(1000);
//                                toSend.setRoundNum(globalRoundNum + 1);
//                                Communication.sendMessage(toSend,this.neighbors);
//                                break;
//                            }
//                                m = this.queue.take();
                                Message toSend = new Message(false,this.UID,true,this.process_index,this.level);
                                toSend.setRoundNum(globalRoundNum + 1);
                                Thread.sleep(1000);
//                                System.out.println("sending Messages to neighbors  " + this.neighbors + "from UID : " + this.UID );
                                Communication.sendMessage(toSend,this.neighbors);
                                this.marked = true;
//                                if (parentUID != -1)
//                                    this.neighbors.add(parentUID);
//                                System.out.println("Messages to neighbors  " + this.UID );

                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                Round.update(this.process_index,0);
//                System.out.println("Array after : " + Round.round.get(this.process_index));
            }

            if(Round.getStopAllThreads()){
                System.out.println("Shutting down UID : " + UID + " with children : " + this.children + " with parent : " + this.parentIndex +
                        " is leaf : " + this.leaf + " Neighbor size : " + this.neighbors.size() + " reject count : " + count);
//                System.out.println("My children : " + this.children);
//                System.out.println("My Parent : " + this.parentIndex);
                break;
            }
        }

    }
}