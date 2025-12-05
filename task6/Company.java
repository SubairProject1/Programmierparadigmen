public class Company {
    private final String name;
    private BuildingNode buildingsHead;

    public Company(String name) {
        this.name = name;
        this.buildingsHead = null;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Add a building to the company
     * @param building the building to add
     */
    public void addBuilding(Building building) {
        BuildingNode newNode = new BuildingNode(building);
        newNode.next = buildingsHead;
        buildingsHead = newNode;
    }

    /**
     * Remove a building by name
     * @param buildingName the name of the building to remove
     */
    public void removeBuilding(String buildingName) {
        BuildingNode previous = null;
        BuildingNode current = buildingsHead;
        while (current != null) {
            if (current.building.getName().equals(buildingName)) {
                if (previous == null) {
                    buildingsHead = current.next;
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
     * Display buildings
     */
    public void displayBuildings() {
        BuildingNode current = buildingsHead;
        while (current != null) {
            Building building = current.building;
            System.out.println(building);
            current = current.next;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Company: " + name + "\nBuildings:\n");
        BuildingNode current = buildingsHead;
        while (current != null) {
            result.append(current.building.toString()).append("\n");
            current = current.next;
        }
        return result.toString();
    }

    private static class BuildingNode {
        Building building;
        BuildingNode next;

        public BuildingNode(Building building) {
            this.building = building;
            this.next = null;
        }
    }
}
