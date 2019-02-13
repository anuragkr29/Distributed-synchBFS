import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

class Round {
    public static AtomicIntegerArray round ;
    public static AtomicInteger threadCount= new AtomicInteger(0) ;
    private static AtomicBoolean stopAllThreads = new AtomicBoolean(false);

    public void setStopAllThreads(boolean state) {
        this.stopAllThreads.set(state);
    }

    public static boolean getStopAllThreads(){
        return stopAllThreads.get();
    }

    public Round(int number) {
        round = new AtomicIntegerArray(number);
    }

    public  void nextRound(int number) {
        threadCount.set(number);
        for(int i=0; i<number;i++){
            round.set(i,100);
        }
    }

    public static int update(int index, int updatedValue) {
        threadCount.decrementAndGet();
        return round.getAndSet(index,updatedValue);
    }

    public int getVal(int index){
        return round.get(index);
    }

    public int getThreadCount(){
        return threadCount.get();
    }

}
