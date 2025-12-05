import java.util.ArrayList;
import java.util.Collection;

/**
 * This class manages the operations of an office, including adding and removing heat pumps, and various other operations related to heat pumps and office units.
 */
@Responsible(groupMember = "Subair", description = "Subair created the initial structure and provided it to Catalin, so he could work on showHeatPumps() and showOffices().")
@Invariant(description = "officeList != null && heatPumpList != null")
public class OfficeOperator {

    private ArrayList<Office> officeList;
    private ArrayList<HeatPump> heatPumpList;

    /**
     * Constructs an OfficeOperator.
     */
    @Postcondition(description = "officeList != null && heatPumpList != null")
    public OfficeOperator() {
        this.officeList = new ArrayList<>();
        this.heatPumpList = new ArrayList<>();
    }

    /**
     * Constructs an OfficeOperator with the given lists of offices and heat pumps.
     *
     * @param officeList the list of offices
     * @param heatPumpList the list of heat pumps
     */
    @Precondition(description = "officeList != null && heatPumpList != null")
    @Postcondition(description = "this.officeList != null && this.heatPumpList != null")
    public OfficeOperator(Collection<Office> officeList, Collection<HeatPump> heatPumpList) {
        this.officeList = new ArrayList<>(officeList);
        this.heatPumpList = new ArrayList<>(heatPumpList);
    }

    /**
     * Adds a new heat pump to the inventory list, if it is not null.
     * Otherwise, the method does nothing.
     *
     * @param heatPump the heat pump to be added
     */
    @Precondition(description = "heatPump != null")
    @Postcondition(description = "heatPumpList.contains(heatPump)")
    void addHeatPump(HeatPump heatPump) {
        if (heatPump == null) return;
        heatPumpList.add(heatPump);
    }

    /**
     * Deletes a defective heat pump from the inventory list, if it is not null and exists in the list.
     * Otherwise, the method does nothing.
     *
     * @param heatPump the heat pump to be deleted
     */
    @Precondition(description = "heatPump != null && heatPumpList.contains(heatPump)")
    @Postcondition(description = "!heatPumpList.contains(heatPump)")
    void deleteHeatPump(HeatPump heatPump) {
        if (heatPump == null || !heatPumpList.contains(heatPump)) return;
        heatPumpList.remove(heatPump);
    }

    /**
     * Assigns a suitable heat pump to an office unit and removes it from the inventory list.
     * Returns the assigned heat pump, or null if no suitable heat pump is available or if the office is null.
     *
     * @param office the office unit to which the heat pump is to be assigned
     * @return the assigned heat pump, or null
     */
    @Precondition(description = "office != null")
    @Postcondition(description = "office.getHeatPump() != null")
    HeatPump assignHeatPump(Office office) {
        if (office == null) return null;
        for (HeatPump heatPump : heatPumpList) {
            if (office.assignHeatPump(heatPump) != null) {
                heatPumpList.remove(heatPump);
                return heatPump;
            }
        }
        // no fitting heat pump was found,
        // therefore it gets a heat pump power with a single higher level (if there are any)
        for (HeatPump heatPump : heatPumpList) {
            if (office.assignHeatPumpException(heatPump) != null) {
                heatPumpList.remove(heatPump);
                return heatPump;
            }
        }

        return null;
    }

    /**
     * Removes the assignment of a heat pump from an office unit and adds it back to the inventory list, if the office is not null.
     * Otherwise, the method does nothing.
     *
     * @param office the office unit from which the heat pump is to be removed
     */
    @Precondition(description = "office != null && office.isHeated()")
    @Postcondition(description = "heatPumpList.contains(office.returnHeatPump())")
    void returnHeatPump(Office office) {
        if (office == null) return;
        if (office.isHeated()) heatPumpList.add(office.returnHeatPump());
    }

    /**
     * Displays the total price of all heat pumps in the inventory list on the screen.
     */
    @Postcondition(description = "totalPrice displayed on screen")
    void priceAvailable() {
        double totalPrice = 0.0d;
        for (HeatPump heatPump : heatPumpList) totalPrice += heatPump.getPrice();
        System.out.print(totalPrice);
    }

    /**
     * Displays the total price of all heat pumps installed in the office units on the screen.
     */
    @Postcondition(description = "totalPrice displayed on screen")
    void priceInstalled() {
        double totalPrice = 0.0d;
        for (Office office : officeList) {
            if (office.isHeated()) totalPrice += office.getHeatPumpPrice();
        }
        System.out.print(totalPrice);
    }

    /**
     * Displays all heat pumps in the inventory list with all information on the screen.
     */
    @Postcondition(description = "heatPump information displayed on screen")
    void showHeatPumps() {
        for (HeatPump heatPump : heatPumpList) {
            heatPump.showInfo();
        }
    }

    /**
     * Displays all office units with all information on the screen, including whether and which heat pump is installed in an office unit.
     */
    @Postcondition(description = "office information displayed on screen")
    void showOffices() {
        for (Office office : officeList) office.showInfo();
    }
}
