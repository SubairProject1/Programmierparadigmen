public class RCounter implements Approvable<RCounter, Path<RCounter>> { //extends Counter???

    int count = 0;
    private Path<RCounter> path;

    @Override
    public Path<RCounter> approved(RCounter rCounter) {
        this.count += 1000;
        rCounter.count++;

        return path;
    }

    @Override
    public void approve(RCounter rCounter, Path<RCounter> rCounters) {
        this.path = rCounters;
    }

    @Override
    public String toString() {
        return Integer.toString(count);
    }
}
