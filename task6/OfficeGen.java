public class OfficeGen<T extends Room> {
    private final int officeNumber;
    private final float sideRoomsArea;
    private Node<T> roomsHead;

    public OfficeGen(int officeNumber, float sideRoomsArea) {
        this.officeNumber = officeNumber;
        this.sideRoomsArea = sideRoomsArea;
        this.roomsHead = null;
    }

    public int getOfficeNumber() {
        return officeNumber;
    }

    public float getSideRoomsArea() {
        return sideRoomsArea;
    }

    /**
     * Add a room to the office
     * @param room the room to add
     */
    public void addRoom(T room) {
        Node<T> newNode = new Node<>(room);
        newNode.next = roomsHead;
        roomsHead = newNode;
    }

    /**
     * Remove a room by name
     * @param roomName the name of the room to remove
     */
    public void removeRoom(String roomName) {
        Node<T> previous = null;
        Node<T> current = roomsHead;
        while (current != null) {
            if (current.data.getName().equals(roomName)) {
                if (previous == null) {
                    roomsHead = current.next; // Remove the head
                } else {
                    previous.next = current.next;
                }
                return; // Room removed
            }
            previous = current;
            current = current.next;
        }
    }

    /**
     * Get a room by name
     * @param roomName the name of the room to get
     * @return the room with the given name, or null if not found
     */
    public T getRoom(String roomName) {
        Node<T> current = roomsHead;
        while (current != null) {
            if (current.data.getName().equals(roomName)) {
                return current.data; // Room found
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Get the total number of rooms
     * @return the total number of rooms
     */
    public float calculateTotalArea() {
        float total = sideRoomsArea;
        Node<T> current = roomsHead;
        while (current != null) {
            total += current.data.getArea();
            current = current.next;
        }
        return total;
    }

    /**
     * Calculate the average area of all rooms
     * @return the average area of all rooms
     */
    public float calculateAverageRoomArea() {
        float total = 0;
        int count = 0;
        Node<T> current = roomsHead;
        while (current != null) {
            total += current.data.getArea();
            count++;
            current = current.next;
        }
        return count == 0 ? 0 : total / count;
    }

    /**
     * Calculate the average area of windowed rooms
     * @return the average area of windowed rooms
     */
    public float calculateAverageWindowedRoomArea() {
        float total = 0;
        int count = 0;
        Node<T> current = roomsHead;
        while (current != null) {
            if (current.data instanceof UtilRoom && ((UtilRoom) current.data).isWindowed()) {
                total += current.data.getArea();
                count++;
            }
            current = current.next;
        }
        return count == 0 ? 0 : total / count;
    }

    /**
     * Calculate the average area of non-windowed rooms
     * @return the average area of non-windowed rooms
     */
    public float calculateAverageNonWindowedRoomArea() {
        float total = 0;
        int count = 0;
        Node<T> current = roomsHead;
        while (current != null) {
            if (current.data instanceof UtilRoom && !((UtilRoom) current.data).isWindowed()) {
                total += current.data.getArea();
                count++;
            }
            current = current.next;
        }
        return count == 0 ? 0 : total / count;
    }

    /**
     * Calculate the average storage volume for storage rooms
     * @return the average storage volume for storage rooms
     */
    public float calculateAverageStorageVolume() {
        float totalVolume = 0;
        int count = 0;
        Node<T> current = roomsHead;
        while (current != null) {
            if (current.data instanceof UtilRoom) {
                UtilRoom utilRoom = (UtilRoom) current.data;
                if (!utilRoom.isOffice()) {
                    totalVolume += utilRoom.getStorageVolume();
                    count++;
                }
            }
            current = current.next;
        }
        return count == 0 ? 0 : totalVolume / count;
    }

    /**
     * Calculate the average number of workplaces for office rooms
     * @return the average number of workplaces for office rooms
     */
    public float calculateAverageWorkplaces() {
        int totalWorkplaces = 0;
        int count = 0;
        Node<T> current = roomsHead;
        while (current != null) {
            if (current.data instanceof UtilRoom) {
                UtilRoom utilRoom = (UtilRoom) current.data;
                if (utilRoom.isOffice()) {
                    totalWorkplaces += utilRoom.getNoOfWorkplaces();
                    count++;
                }
            }
            current = current.next;
        }
        return count == 0 ? 0 : (float) totalWorkplaces / count;
    }

    /**
     * Calculate the average window to room area ratio
     * @return the average window to room area ratio
     */
    public float calculateAverageWindowToRoomAreaRatio() {
        float totalRatio = 0;
        int count = 0;
        Node<T> current = roomsHead;
        while (current != null) {
            if (current.data instanceof UtilRoom) {
                UtilRoom utilRoom = (UtilRoom) current.data;
                if (utilRoom.isWindowed()) {
                    float ratio = utilRoom.getWindowArea() / utilRoom.getArea();
                    totalRatio += ratio;
                    count++;
                }
            }
            current = current.next;
        }
        return count == 0 ? 0 : totalRatio / count;
    }

    /**
     * Calculate the average light flux per area for non-windowed rooms
     * @return the average light flux per area for non-windowed rooms
     */
    public float calculateAverageLightFluxPerArea() {
        float totalFluxPerArea = 0;
        int count = 0;
        Node<T> current = roomsHead;
        while (current != null) {
            if (current.data instanceof UtilRoom) {
                UtilRoom utilRoom = (UtilRoom) current.data;
                if (!utilRoom.isWindowed()) {
                    float fluxPerArea = utilRoom.getLuminescentLighting() / utilRoom.getArea();
                    totalFluxPerArea += fluxPerArea;
                    count++;
                }
            }
            current = current.next;
        }
        return count == 0 ? 0 : totalFluxPerArea / count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OfficeGen{officeNumber=").append(officeNumber);
        sb.append(", sideRoomsArea=").append(sideRoomsArea);
        sb.append(", rooms=[");
        Node<T> current = roomsHead;
        while (current != null) {
            sb.append(current.data);
            current = current.next;
            if (current != null) {
                sb.append(", ");
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    /**
     * A node in the linked list
     * @param <E> the type of the data
     */
    private static class Node<E> {
        E data;
        Node<E> next;

        public Node(E data) {
            this.data = data;
            this.next = null;
        }
    }
}
