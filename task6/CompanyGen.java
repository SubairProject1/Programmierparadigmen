public class CompanyGen<T extends BuildingGen<?>> {
    private final String name;
    private Node<T> buildingsHead;

    public CompanyGen(String name) {
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
    public void addBuilding(T building) {
        Node<T> newNode = new Node<>(building);
        newNode.next = buildingsHead;
        buildingsHead = newNode;
    }

    /**
     * Remove a building by name
     * @param buildingName the name of the building to remove
     */
    public void removeBuilding(String buildingName) {
        Node<T> previous = null;
        Node<T> current = buildingsHead;
        while (current != null) {
            if (current.data.getName().equals(buildingName)) {
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
        Node<T> current = buildingsHead;
        while (current != null) {
            T building = current.data;
            System.out.println(building);
            current = current.next;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Company: " + name + "\nBuildings:\n");
        Node<T> current = buildingsHead;
        while (current != null) {
            result.append(current.data.toString());
            current = current.next;
        }
        return result.toString();
    }


    private static class Node<E> {
        E data;
        Node<E> next;

        public Node(E data) {
            this.data = data;
            this.next = null;
        }
    }
}
