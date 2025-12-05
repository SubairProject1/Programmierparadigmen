public class UtilRoom implements Room {

    String name;
    private final float length;
    private final float width;
    private final float height;
    private final float windowArea;
    private final float luminescentLighting;
    private int noOfWorkplaces;

    private Boolean isWindowed;
    private Boolean isOffice;

    public UtilRoom(String name, float length, float width, float height, float windowArea, float luminescentLighting, int noOfWorkplaces) {
        this.name = name;
        this.length = length;
        this.width = width;
        this.height = height;
        this.windowArea = windowArea;
        this.luminescentLighting = luminescentLighting;
        this.isWindowed = windowArea > 0;
        this.isOffice = noOfWorkplaces > 0;
        this.noOfWorkplaces = noOfWorkplaces;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public float getLength() {
        return length;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getArea() {
        return Room.super.getArea();
    }

    public float getLuminescentLighting() {
        if(isWindowed) throw new UnsupportedOperationException("A windowed room does not support getLuminescentLighting()");
        return luminescentLighting;
    }

    public float getWindowArea() {
        if(!isWindowed) throw new UnsupportedOperationException("A non-windowed room does not support getWindowArea()");
        return windowArea;
    }

    public float getStorageVolume() {
        if(isOffice) throw new UnsupportedOperationException("An office room does not support getStorageVolume()");
        return getArea() * height;
    }

    public int getNoOfWorkplaces() {
        if(!isOffice) throw new UnsupportedOperationException("A storage room does not support getNoOfWorkplaces()");
        return noOfWorkplaces;
    }

    public Boolean isWindowed() {
        return isWindowed;
    }

    public Boolean isOffice() {
        return isOffice;
    }

    void turnToOffice(int noOfWorkplaces) {
        if(isOffice) throw new UnsupportedOperationException("It's already an office room");
        isOffice = true;
        this.noOfWorkplaces = noOfWorkplaces;
    }

    void turnIntoStorage() {
        if(!isOffice) throw new UnsupportedOperationException("It's already a storage room");
        isOffice = false;
        this.noOfWorkplaces = 0;
    }

    @Override
    public String toString() {
        return "UtilRoom{" +
                "name='" + name + '\'' +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", windowArea=" + windowArea +
                ", luminescentLighting=" + luminescentLighting +
                ", noOfWorkplaces=" + noOfWorkplaces +
                ", isWindowed=" + isWindowed +
                ", isOffice=" + isOffice +
                '}';
    }
}
