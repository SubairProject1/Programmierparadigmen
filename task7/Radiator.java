public class Radiator extends HeatPump {
    /**
     * Constructor for the Radiator class.
     *
     * @param price The price of the radiator.
     * @param power The power level of the radiator.
     */
    @Postcondition(description = "this.price > 0 && this.power != null")
    public Radiator(double price, Power power) {
        super(price, power);
    }

    /**
     * Checks if the radiator is assignable to a small office with a radiator.
     *
     * @return true if the power level is Low, false otherwise.
     */
    @Postcondition(description = "Returns true if the power level is Low, false otherwise")
    public boolean isAssignableToSmallOfficeRadiator() {
        return getPower() == Power.Low;
    }

    /**
     * Checks if the radiator is assignable to a medium office with a radiator.
     *
     * @return true if the power level is Low, false otherwise.
     */
    @Postcondition(description = "Returns true if the power level is Low, false otherwise")
    public boolean isAssignableToMediumOfficeRadiator() {
        return getPower() == Power.Low;
    }

    /**
     * Checks if the radiator is assignable to a large office with a radiator.
     *
     * @return true if the power level is Low, false otherwise.
     */
    @Postcondition(description = "Returns true if the power level is Low, false otherwise")
    public boolean isAssignableToLargeOfficeRadiator() {
        return getPower() == Power.Low;
    }

    /**
     * Displays information about the radiator.
     */
    @Postcondition(description = "Information about the radiator is displayed on the screen")
    public void showInfo() {
        System.out.println("Radiator: price: " + getPrice() + "$, power: " + getPower().toString());
    }
}
