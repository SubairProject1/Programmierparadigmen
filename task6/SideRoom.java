/**
 * Represents a side room with specific dimensions.
 * Implements the Room interface to provide properties like name and only area.
 */
public class SideRoom implements Room {

    private final String name;
    private final float area;

    /**
     * Constructs a SideRoom object with the specified name, length, and width.
     *
     * @param name the name of the side room
     * @param length the length of the side room in meters
     * @param width the width of the side room in meters
     */
    public SideRoom(String name, float length, float width) {
        this.name = name;
        this.area = length * width;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getLength() {
        throw new UnsupportedOperationException("A side room does not support getLength()");
    }

    @Override
    public float getWidth() {
        throw new UnsupportedOperationException("A side room does not support getWidth()");
    }

    @Override
    public float getArea() {
        return this.area;
    }

    @Override
    public String toString() {
        return "SideRoom{" +
                "name='" + name + '\'' +
                ", area=" + area + "m^2" +
                '}';
    }
}
