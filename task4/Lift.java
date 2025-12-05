import java.util.ArrayList;
import java.util.Set;

/**
 * Represents a lift within a building, which acts as both a room and a circulation space.
 * The lift connects multiple rooms across different floors, ensuring efficient vertical movement.
 *
 * The lift validates that it connects at least two rooms on different floors at the time of its creation.
 * If this condition is not met, an IllegalArgumentException is thrown.
 *
 * Implements the Room and Circulation interfaces to ensure compatibility with room and circulation-specific operations.
 */
public class Lift implements Room, Circulation {

    private ArrayList<Room> connectedRooms;

    /**
     * Constructs a Lift object connecting the specified rooms.
     *
     * @param connectedRooms the rooms that the lift connects
     * @throws IllegalArgumentException if less than two rooms are connected or if all rooms are on the same floor
     */
    public Lift(ArrayList<Room> connectedRooms) {
        if (connectedRooms.size() < 2) {
            throw new IllegalArgumentException("A lift must connect at least two rooms.");
        }
        this.connectedRooms = connectedRooms;

        if (!validateFloors()) {
            throw new IllegalArgumentException("A lift must connect rooms on different floors.");
        }
    }

    /**
     * Validates that the connected rooms are on different floors.
     *
     * @return true if the connected rooms are on different floors
     */
    private boolean validateFloors() {
        int initialFloor = connectedRooms.get(0).getFloor();
        for (Room room : connectedRooms) {
            if (room.getFloor() != initialFloor) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<Space> getConnectedSpaces() {
        return new ArrayList<Space>(connectedRooms);
    }

    @Override
    public void addConnectedSpace(Space space) {
        connectedRooms.add((Room) space);
    }

    @Override
    public boolean isCirculation() {
        return true;
    }

    @Override
    public int getFloor() {
        throw new UnsupportedOperationException("A lift does not have a single floor.");
    }

    @Override
    public Entity entity() {
        return null;
    }

    @Override
    public Escape escape() {
        return null;
    }

    @Override
    public Set<Space> remove() {
        return Set.of();
    }
    @Override
    public boolean isExit() {
        return false;
    }
}

