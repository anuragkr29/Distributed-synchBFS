import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

class Round {
    public static AtomicIntegerArray round ;
    private AtomicInteger threadCount ;

    public Round(int number) {
        round = new AtomicIntegerArray(number);
    }

    public  void nextRound(int number) {
        this.threadCount.set(number);
        for(int i=0; i<number;i++){
            round.set(i,100);
        }
    }

    public static int update(int index, int updatedValue) {
        return round.getAndSet(index,updatedValue);
    }

    public int getVal(int index){
        return round.get(index);
    }

}
