import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        ReadFile fileReadObj = new ReadFile("input.dat");
        ArrayList<Integer>[] connectionMatrix = fileReadObj.getConnectionMatrix();
        int numberOfProcesses = fileReadObj.getNumberOfProcesses();
        int rootUID = fileReadObj.getRoot();
        int[] UIDs = fileReadObj.getUIDs();
        int numCores = Runtime.getRuntime().availableProcessors();
        Communication channel = new Communication(numberOfProcesses);
        System.out.println("Number of Cores : " + numCores);
        ExecutorService threadPool = Executors.newFixedThreadPool(numCores);
        //ArrayList
        // submit jobs to be executing by the pool
        for (int i = 0; i < numberOfProcesses; i++) {
            threadPool.submit(new Process(UIDs[i], false , connectionMatrix[i],i,channel));
        }
        threadPool.shutdown();
        // wait for the threads to finish if necessary
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
