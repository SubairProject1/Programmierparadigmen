import java.util.ArrayList;
import java.util.Set;

/**
 * Represents a served space designed for longer-term occupancy,
 * such as living rooms, bedrooms, or offices. These spaces must
 * meet certain quality standards such as sufficient natural and artificial
 * lighting, ventilation, heating, minimum heights, movement areas,
 * and secondary escape routes.
 */
public class ServedSpace implements Interior {

    private int floor;
    private Boolean isConnectedToLift;
    private double naturalLighting;
    private double ventilation;
    private double heating;
    private double minimumHeight;
    private Vec2 movementArea;
    private Vec2 windowSize;

    private Boolean emergencyArisen; // not fixed

    private ArrayList<Space> connectedSpaces;

    /**
     * Constructs a ServedSpace with specified quality parameters.
     *
     * @param floor the floor number where the space is located
     * @param isConnectedToLift indicates if the space is connected to a lift
     * @param naturalLighting the level of natural lighting
     * @param ventilation the level of ventilation
     * @param heating the level of heating
     * @param minimumHeight the minimum height of the space
     * @param movementArea the movement area dimensions
     * @param windowSize the window size dimensions
     * @throws IllegalArgumentException if quality requirements are not met
     */
    public ServedSpace(int floor, Boolean isConnectedToLift, double naturalLighting, double ventilation, double heating, double minimumHeight, Vec2 movementArea, Vec2 windowSize) {
        // naturalLighting >= 5.0
        // ventilation >= 3.0
        // heating >= 2.0
        // minimumHeight >= 2.5
        // movementArea > 10.0
        // windowSize > 1.1
        if(naturalLighting < 5.0d || ventilation < 3.0d || heating < 2.0d || minimumHeight < 2.5d || movementArea.x() * movementArea.y() < 10.0d || windowSize.x() * windowSize.y() < 1.1d)
            throw new IllegalArgumentException("Quality requirements not met.");

        this.floor = floor;
        this.isConnectedToLift = isConnectedToLift;
        this.naturalLighting = naturalLighting;
        this.ventilation = ventilation;
        this.heating = heating;
        this.minimumHeight = minimumHeight;
        this.movementArea = movementArea;
        this.windowSize = windowSize;
        emergencyArisen = false;
        connectedSpaces = new ArrayList<>();
    }

    /**
     * Checks if the space is connected to a lift.
     *
     * @return true if the space is connected to a lift, false otherwise
     */
    @Override
    public Boolean isConnectedToLift() {
        return isConnectedToLift;
    }

    /**
     * Gets the spaces connected to this served space.
     *
     * @return a list of connected spaces
     */
    @Override
    public ArrayList<Space> getConnectedSpaces() {
        return connectedSpaces;
    }

    /**
     * Adds a space to the list of connected spaces.
     *
     * @param space the space to connect
     */
    @Override
    public void addConnectedSpace(Space space) {
        connectedSpaces.add(space);
    }

    /**
     * Checks if the space functions as circulation.
     *
     * @return true if the space functions as circulation, false otherwise
     */
    @Override
    public boolean isCirculation() {
        // connectedSpaces.size() > 1
        return connectedSpaces.size() > 1;
    }

    /**
     * Gets the floor number where the space is located.
     *
     * @return the floor number
     */
    @Override
    public int getFloor() {
        return floor;
    }

    /**
     * Gets the associated entity of the space.
     *
     * @return the associated entity (currently returns null)
     */
    @Override
    public Entity entity() {
        return null;
    }

    /**
     * Provides an escape route if an emergency has arisen.
     *
     * @return an Escape object representing the escape path, or null if no emergency
     */
    @Override
    public Escape escape() {
        if(emergencyArisen) {
            return new Escape(this);
        }
        return null;
    }

    /**
     * Removes the space and returns an empty set of connected spaces.
     *
     * @return an empty set of spaces
     */
    @Override
    public Set<Space> remove() {
        return Set.of();
    }

    /**
     * Calculates the alternative escape route area.
     *
     * @return the area of the window size
     */
    @Override
    public double alternativeEscape() {
        // windowSize
        return windowSize.x() * windowSize.y();
    }

    /**
     * Checks if the space serves as an exit.
     *
     * @return true if the space is an exit, false otherwise
     */
    @Override
    public boolean isExit() {
        if(connectedSpaces == null)
            return false;
        for(Space space : connectedSpaces) {
            if(space instanceof PublicRoad) {
                return true;
            }
        }
        return false;
    }
}
