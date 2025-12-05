public class Building {
    private final String name;
    private OfficeNode officesHead;

    public Building(String name) {
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
    public void addOffice(Office office) {
        OfficeNode newNode = new OfficeNode(office);
        newNode.next = officesHead;
        officesHead = newNode;
    }


    /**
     * Remove an office by number
     * @param officeNumber the number of the office to remove
     */
    public void removeOffice(int officeNumber) {
        OfficeNode previous = null;
        OfficeNode current = officesHead;
        while (current != null) {
            if (current.office.getOfficeNumber() == officeNumber) {
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
        OfficeNode current = officesHead;
        while (current != null) {
            Office office = current.office;
            System.out.print(office);
            current = current.next;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Building: " + name + "\nOffices:\n");
        OfficeNode current = officesHead;
        while (current != null) {
            result.append(current.office.toString()).append("\n");
            current = current.next;
        }
        return result.toString();
    }


    private static class OfficeNode {
        Office office;
        OfficeNode next;

        public OfficeNode(Office office) {
            this.office = office;
            this.next = null;
        }
    }
}
