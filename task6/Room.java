/**
 * Represents a room with basic properties like name, length, and width.
 * Provides a default method to calculate the area of the room.
 */
public interface Room {

    /**
     * Returns the name of the room.
     *
     * @return the name of the room
     */
    String getName();

    /**
     * Returns the length of the room in meters.
     *
     * @return the length of the room
     */
    float getLength();

    /**
     * Returns the width of the room in meters.
     *
     * @return the width of the room
     */
    float getWidth();

    /**
     * Calculates and returns the area of the room in square meters.
     *
     * @return the area of the room
     */
    default float getArea() {
        return getLength() * getWidth();
    }
}
