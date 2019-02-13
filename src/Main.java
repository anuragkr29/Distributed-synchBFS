import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {

        Utils utility = new Utils();
        ReadFile fileReadObj = new ReadFile("input.dat");
        ArrayList<Integer>[] connectionMatrix = fileReadObj.getConnectionMatrix();
        int numberOfProcesses = fileReadObj.getNumberOfProcesses();
        final Semaphore available = new Semaphore(numberOfProcesses, true);
        int rootUID = fileReadObj.getRoot();
        int[] UIDs = fileReadObj.getUIDs();
        int numCores = Runtime.getRuntime().availableProcessors();
        Round r = new Round(numberOfProcesses);
        System.out.println("Number of Cores : " + numCores);
        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfProcesses);
        HashMap<Integer,Process> processMap = new HashMap<>(2*numberOfProcesses);
        // submit jobs to be executing by the pool
        for (int i = 0; i < numberOfProcesses; i++) {
            Process p = new Process(UIDs[i], false , utility.getNeighbors(i,connectionMatrix[i]),i);
            processMap.put(UIDs[i],p);
        }
        Communication channel = new Communication(processMap);
        for (int i = 0; i < numberOfProcesses; i++) {
            threadPool.submit(processMap.get(UIDs[i]));
        }
        System.out.println("Waiting for input");
        Scanner s = new Scanner(System.in);
        int inp = s.nextInt();
        while(true){
            if(inp == 3){
                r.nextRound(numberOfProcesses);
            }
            inp = s.nextInt();
        //threadPool.shutdown();
        // wait for the threads to finish if necessary
//        try {
//            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
    }
}
