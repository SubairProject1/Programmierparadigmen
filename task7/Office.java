/**
 * This abstract class represents an office. An office can be heated by either a floor heating or a radiator heat pump.
 * For each heating type there are three Office sizes: small, medium and large.
 * */
@Responsible(groupMember = "Andrei", description = "Implementation of this abstract class and helped for every office type.")
public abstract class Office {

    HeatPump heatPump;
    /**
     * Default constructor for the Office class."
     */
    public Office(){}

    /**
     * Constructor for the Office class that initializes the heat pump.
     *
     * @param heatPump the heat pump to be assigned to the office
     */
    @Precondition(description = "heatPump != null")
    @Postcondition(description = "The heat pump is assigned to the office")
    public Office(HeatPump heatPump) {
        this.heatPump = heatPump;
    }

    /**
     * Checks if the office is heated (has a heat pump assigned to it).
     *
     * @return true if the office has a heat pump, false otherwise
     */
    @Postcondition(description = "Returns true if the office has a heat pump, false otherwise")
    public boolean isHeated() {
        return heatPump != null;
    }

    /**
     * Gets the power of the heat pump.
     *
     * @return the power of the heat pump as a String, or "Not Heated" if there is no heat pump
     */
    @Postcondition(description = "Returns the power of the heat pump as a String, or 'Not Heated' if there is no heat pump")
    public String getPower() {
        if(!isHeated()) return "Not Heated";
        return this.heatPump.getPower().toString();
    }

    /**
     * Gets the price of the heat pump.
     *
     * @return the price of the heat pump, or 0.0 if there is no heat pump
     */
    @Postcondition(description = "Returns the price of the heat pump, or 0.0 if there is no heat pump")
    public double getHeatPumpPrice() {
        if(!isHeated()) return 0.0d;
        return this.heatPump.getPrice();
    }

    /**
     * Abstract method to assign a heat pump to the office.
     *
     * @param heatPump the heat pump to be assigned
     * @return the assigned heat pump
     */
    @Postcondition(description = "Returns the heat pump that has been assigned, or null if there is no heat pump or if heatPump == null")
    public abstract HeatPump assignHeatPump(HeatPump heatPump);

    /**
     * Abstract method to assign a heat pump to the office, if none of the correct level are available.
     * Depends on the office type.
     *
     * @param heatPump the heat pump to be assigned
     * @return the assigned heat pump
     */
    public abstract HeatPump assignHeatPumpException(HeatPump heatPump);

    /**
     * Returns the current heat pump and removes it from the office.
     *
     * @return the current heat pump, or null if there is no heat pump
     */
    @Postcondition(description = "Returns the heat pump that has been removed, or null if there is no heat pump")
    public HeatPump returnHeatPump() {
        if(!isHeated()) return null;
        HeatPump toBeReturned = this.heatPump;
        this.heatPump = null;
        return toBeReturned;
    }

    /**
     * Abstract method to get the type of the office.
     *
     * @return the type of the office as a String
     */
    @Postcondition(description = "Returns the type of the office as a String")
    public abstract String getType();

    /**
     * Displays information about the office, including its type, heating status, power, and heat pump price.
     */
    @Postcondition(description = "Information about the office is displayed on the screen")
    public void showInfo() {
        System.out.println(getType() + ": " + (isHeated() ? "heated" : "not heated") + ", power: " +
                getPower() + ", heat pump price: " + getHeatPumpPrice() + "$");
    }
}
