/**
 * The FloorHeating class represents a type of heat pump, as it extends the HeatPump class.
 * It adds the ability to check if the floor heating system is assignable to a small, medium or large office with floor heating.
 */
public class FloorHeating extends HeatPump {
    /**
     * Constructor for the FloorHeating class.
     *
     * @param price The price of the floor heating system.
     * @param power The power level of the floor heating system.
     */
    @Postcondition(description = "this.price > 0 && this.power != null")
    public FloorHeating(double price, Power power) {
        super(price, power);
    }

    /**
     * Checks if the floor heating system is assignable to a small office with floor heating.
     *
     * @return true if the power level is Low, false otherwise.
     */
    @Postcondition(description = "Returns true if the power level is Low, false otherwise")
    public boolean isAssignableToSmallOfficeFloorHeating() {
        return getPower() == Power.Low;
    }

    /**
     * Checks if the floor heating system is assignable to a medium office with floor heating.
     *
     * @return true if the power level is Medium, false otherwise.
     */
    @Postcondition(description = "Returns true if the power level is Medium, false otherwise")
    public boolean isAssignableToMediumOfficeFloorHeating() {
        return getPower() == Power.Medium;
    }

    /**
     * Checks if the floor heating system is assignable to a large office with floor heating.
     *
     * @return true if the power level is High, false otherwise.
     */
    @Postcondition(description = "Returns true if the power level is High, false otherwise")
    public boolean isAssignableToLargeOfficeFloorHeating() {
        return getPower() == Power.High;
    }

    /**
     * Displays information about the floor heating system.
     */
    @Postcondition(description = "Information about the floor heating system is displayed on the screen")
    public void showInfo() {
        System.out.println("FloorHeating: price: " + getPrice() + "$, power: " + getPower().toString());
    }
}