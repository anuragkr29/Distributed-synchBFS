/**
 * @author Anurag Kumar
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {

        Utils utility = new Utils();
        ReadFile fileReadObj = new ReadFile("input.dat");
        ArrayList<Integer>[] connectionMatrix = fileReadObj.getConnectionMatrix();
        int numberOfProcesses = fileReadObj.getNumberOfProcesses();
        int rootUID = fileReadObj.getRoot();
        int[] UIDs = fileReadObj.getUIDs();
        int numCores = Runtime.getRuntime().availableProcessors();
        Round r = new Round(numberOfProcesses);
        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfProcesses);
        HashMap<Integer,Process> processMap = new HashMap<>(2*numberOfProcesses);
        // submit jobs to be executing by the pool
        for (int i = 0; i < numberOfProcesses; i++) {
            Process p = new Process(UIDs[i], false , utility.getNeighbors(i,connectionMatrix[i]),i, rootUID);
            processMap.put(UIDs[i],p);
        }
        Communication channel = new Communication(processMap, UIDs);
        for (int i = 0; i < numberOfProcesses; i++) {
                threadPool.submit(processMap.get(UIDs[i]));
        }
        Scanner s = new Scanner(System.in);
        int round = 0;
        Process root = processMap.get(rootUID);
        while (!root.isProcessCompleted()) {
            try {
                if (Round.threadCount.get() == 0) {
                    round++;
                    if (round==1){
                        Message m = new Message();
                        m.setRoot(true);
                        m.setRoundNum(1);
                        m.setLevel(1);
                        root.putMessage(m);
                    }
                    Thread.currentThread().sleep(1000);
                    r.nextRound(numberOfProcesses,round);
                    System.out.println("Started round : " + (round));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            inp = s.nextInt();
        }
        r.setStopAllThreads(true);
        System.out.println("All rounds finishied . Closing Thread pool");
        threadPool.shutdown();
//wait for the threads to finish if necessary
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            System.out.println("Thread pool closed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
