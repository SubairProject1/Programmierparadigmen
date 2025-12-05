public class BuildingGen<T extends OfficeGen<?>> {
    private final String name;
    private Node<T> officesHead;

    public BuildingGen(String name) {
        this.name = name;
        this.officesHead = null;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Add an office to the building
     * @param office the office to add
     */
    public void addOffice(T office) {
        Node<T> newNode = new Node<>(office);
        newNode.next = officesHead;
        officesHead = newNode;
    }

    /**
     * Remove an office by number
     * @param officeNumber the number of the office to remove
     */
    public void removeOffice(int officeNumber) {
        Node<T> previous = null;
        Node<T> current = officesHead;
        while (current != null) {
            if (current.data.getOfficeNumber() == officeNumber) {
                if (previous == null) {
                    officesHead = current.next;
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
     * Display offices
     */
    public void displayOffices() {
        Node<T> current = officesHead;
        while (current != null) {
            T office = current.data;
            System.out.println(office);
            current = current.next;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Building: " + name + "\nOffices:\n");
        Node<T> current = officesHead;
        while (current != null) {
            result.append(current.data.toString()).append("\n");
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
