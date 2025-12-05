import java.util.*;

public class Escape implements Iterable<Space> {

    Space space;
    ArrayList<Space> escapePath;

    public Escape(Space space) {
        this.space = space;
        escapePath = new ArrayList<>();
        findEscapePath();
    }

    /**
     * Find the escape path
     */
    public void findEscapePath() {
        Circulation startRoom = (Circulation) space;
        Queue<ArrayList<Circulation>> q = new LinkedList<>();
        Set<Circulation> visited = new HashSet<>();
        q.add(new ArrayList<>(Collections.singletonList(startRoom)));
        visited.add(startRoom);
        while(!q.isEmpty()) {
            List<Circulation> path = q.poll();
            Circulation current = path.getLast();

            if(current.getConnectedSpaces() == null) {
                continue;
            }

            if (current.isExit()) {
                escapePath.addAll(path);
                return;
            }

            for (Space neighbor : current.getConnectedSpaces()) {
                if(neighbor instanceof Room neighboorRoom && !(neighbor instanceof Lift)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighboorRoom);
                        ArrayList<Circulation> newPath = new ArrayList<>(path);
                        newPath.add(neighboorRoom);
                        q.add(newPath);
                    }
                }
            }
        }
    }

    /**
     * @return the space
     */
    Space space() {
        return space;
    }

    /**
     * @return the length of the escape path
     */
    public int length() {
        return escapePath.size();
    }

    @Override
    public Iterator<Space> iterator() {
        return new EscapePathIterator(escapePath);
    }
}
