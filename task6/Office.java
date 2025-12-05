public class Office {
    private final int officeNumber;
    private final float sideRoomsArea;
    private RoomNode roomsHead;

    public Office(int officeNumber, float sideRoomsArea) {
        this.officeNumber = officeNumber;
        this.sideRoomsArea = sideRoomsArea;
        this.roomsHead = null;
    }

    /**
     * @return the officeNumber
     */
    public int getOfficeNumber() {
        return officeNumber;
    }

    /**
     * @return the sideRoomsArea
     */
    public float getSideRoomsArea() {
        return sideRoomsArea;
    }

    /**
     * Add a room to the office
     * @param room the room to add
     */
    public void addRoom(Room room) {
        RoomNode newNode = new RoomNode(room);
        newNode.next = roomsHead;
        roomsHead = newNode;
    }

    /**
     * Remove a room by name
     * @param roomName the name of the room to remove
     */
    public void removeRoom(String roomName) {
        RoomNode previous = null;
        RoomNode current = roomsHead;
        while (current != null) {
            if (current.room.getName().equals(roomName)) {
                if (previous == null) {
                    roomsHead = current.next;
                } else {
                    previous.next = current.next;
                }
                return;
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
    public Room getRoom(String roomName) {
        RoomNode current = roomsHead;
        while (current != null) {
            if (current.room.getName().equals(roomName)) {
                return current.room; // Room found
            }
            current = current.next;
        }
        return null; // Room not found
    }

    /**
     * Calculate the total area of the office (usable + side rooms)
     * @return the total area of the office
     */
    public float calculateTotalArea() {
        float total = sideRoomsArea;
        RoomNode current = roomsHead;
        while (current != null) {
            total += current.room.getArea();
            current = current.next;
        }
        return total;
    }

    /**
     * Calculate the average area of all usable rooms
     * @return the average area of all usable rooms
     */
    public float calculateAverageRoomArea() {
        float total = 0;
        int count = 0;
        RoomNode current = roomsHead;
        while (current != null) {
            total += current.room.getArea();
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
        RoomNode current = roomsHead;
        while (current != null) {
            if (current.room instanceof UtilRoom && ((UtilRoom) current.room).isWindowed()) {
                total += current.room.getArea();
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
        RoomNode current = roomsHead;
        while (current != null) {
            if (current.room instanceof UtilRoom && !((UtilRoom) current.room).isWindowed()) {
                total += current.room.getArea();
                count++;
            }
            current = current.next;
        }
        return count == 0 ? 0 : total / count;
    }

    /**
     * Calculate the average storage volume per storage room
     * @return the average storage volume per storage room
     */
    public float calculateAverageStorageVolume() {
        float totalVolume = 0;
        int count = 0;
        RoomNode current = roomsHead;
        while (current != null) {
            if (current.room instanceof UtilRoom) {
                UtilRoom utilRoom = (UtilRoom) current.room;
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
     * Calculate the average number of workplaces per office room
     * @return the average number of workplaces per office room
     */
    public float calculateAverageWorkplaces() {
        int totalWorkplaces = 0;
        int count = 0;
        RoomNode current = roomsHead;
        while (current != null) {
            if (current.room instanceof UtilRoom) {
                UtilRoom utilRoom = (UtilRoom) current.room;
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
     * Calculate the average window-to-room area ratio
     * @return the average window-to-room area ratio
     */
    public float calculateAverageWindowToRoomAreaRatio() {
        float totalRatio = 0;
        int count = 0;
        RoomNode current = roomsHead;
        while (current != null) {
            if (current.room instanceof UtilRoom) {
                UtilRoom utilRoom = (UtilRoom) current.room;
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
        RoomNode current = roomsHead;
        while (current != null) {
            if (current.room instanceof UtilRoom) {
                UtilRoom utilRoom = (UtilRoom) current.room;
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
        sb.append("Office ").append(officeNumber).append(":\n");
        RoomNode current = roomsHead;
        while (current != null) {
            sb.append(current.room).append("\n");
            current = current.next;
        }
        return sb.toString();
    }

    /**
     * A private class to represent a node in the linked list of rooms
     */
    private static class RoomNode {
        Room room;
        RoomNode next;

        public RoomNode(Room room) {
            this.room = room;
            this.next = null;
        }
    }
}
