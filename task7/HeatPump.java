/**
 * This abstract class represents a heat pump, which can be used to heat an office in a CO2-neutral way.
 * The heat pump has a price and a power level, which can be low, medium, or high.
 * There are two types of heat pumps: floor heating and radiator.
 * */
@Responsible(groupMember = "Andrei", description = "Implementation of this abstract class and helped for every heat pump type.")
public abstract class HeatPump {

    private final double price;
    private final Power power;

    /**
     * Enum to represent the power level of the heat pump.
     */
    public enum Power {
        Low,
        Medium,
        High;

        @Override
        public String toString() {
            return switch(this) {
                case Low -> "Low";
                case Medium -> "Medium";
                case High -> "High";
            };
        }
    }

    /**
     * Constructor for the HeatPump class.
     * Constructs a heat pump with the given price and power level if the price is >0 and Power is not null.
     * Otherwise, price is set to 1 and power to Low.
     *
     * @param price The price of the heat pump.
     * @param power The power level of the heat pump.
     */
    @Postcondition(description = "this.price > 0 && this.power != null && price is in ('Low', 'Medium', 'High')")
    public HeatPump(double price, Power power) {

        if (price <= 0 || power == null) {
            this.price = 1;
            this.power = Power.Low;
            return;
        }

        this.price = price;
        this.power = power;
    }

    /**
     * Gets the price of the heat pump.
     *
     * @return The price of the heat pump.
     */
    @Postcondition(description = "Returns this.price")
    public double getPrice() {
        return this.price;
    }

    /**
     * Gets the power level of the heat pump.
     *
     * @return The power level of the heat pump.
     */
    @Postcondition(description = "Returns this.power")
    public Power getPower() {
        return this.power;
    }

    /**
     * Checks if the heat pump is assignable to a small office with floor heating.
     *
     * @return false, as this method should be overridden by subclasses.
     */
    @Postcondition(description = "Returns false (method should be overridden by subclasses)")
    public boolean isAssignableToSmallOfficeFloorHeating() { return false; }

    /**
     * Checks if the heat pump is assignable to a medium office with floor heating.
     *
     * @return false, as this method should be overridden by subclasses.
     */
    @Postcondition(description = "Returns false (method should be overridden by subclasses)")
    public boolean isAssignableToMediumOfficeFloorHeating() { return false; }

    /**
     * Checks if the heat pump is assignable to a large office with floor heating.
     *
     * @return false, as this method should be overridden by subclasses.
     */
    @Postcondition(description = "Returns false (method should be overridden by subclasses)")
    public boolean isAssignableToLargeOfficeFloorHeating() { return false; }

    /**
     * Checks if the heat pump is assignable to a small office with a radiator.
     *
     * @return false, as this method should be overridden by subclasses.
     */
    @Postcondition(description = "Returns false (method should be overridden by subclasses)")
    public boolean isAssignableToSmallOfficeRadiator() { return false; }

    /**
     * Checks if the heat pump is assignable to a medium office with a radiator.
     *
     * @return false, as this method should be overridden by subclasses.
     */
    @Postcondition(description = "Returns false (method should be overridden by subclasses)")
    public boolean isAssignableToMediumOfficeRadiator() { return false; }

    /**
     * Checks if the heat pump is assignable to a large office with a radiator.
     *
     * @return false, as this method should be overridden by subclasses.
     */
    @Postcondition(description = "Returns false (method should be overridden by subclasses)")
    public boolean isAssignableToLargeOfficeRadiator() { return false; }

    /**
     * Abstract method to show information about the heat pump.
     * This method should be implemented by subclasses.
     */
    @Postcondition(description = "Method should be implemented by subclasses")
    public abstract void showInfo();
}
