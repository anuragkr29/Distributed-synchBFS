import java.util.concurrent.atomic.AtomicInteger;

class Round {
    private static AtomicInteger round = new AtomicInteger(0);

    public static int nextRound() {
        return round.incrementAndGet();
    }

    public int value() {
        return this.round.get();
    }

}
