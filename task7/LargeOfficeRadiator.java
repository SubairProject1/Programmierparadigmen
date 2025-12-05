/**
 * This class represents a large office with a radiator.
 */
@Responsible(groupMember = "Catalin", description = "Implementation of the structure suggested by Subair")
public class LargeOfficeRadiator extends Office {

    /**
     * Constructor for the LargeOfficeRadiator class that initializes the heat pump.
     *
     * @param heatPump the heat pump to be assigned to the large office with radiator
     */
    @Precondition(description = "heatPump != null")
    @Postcondition(description = "The heat pump is assigned to the large office with radiator")
    LargeOfficeRadiator(HeatPump heatPump) {
        super(heatPump);
    }

    /**
     * Assigns a heat pump to the large office with radiator. Returns the assigned heat pump, or null if the
     * heatPump is null or is not assignable to this.
     *
     * @param heatPump the heat pump to be assigned
     * @return the assigned heat pump, or null if the office already has a heat pump or the heat pump is not assignable
     */
    @Override
    public HeatPump assignHeatPump(HeatPump heatPump) {
        if (this.heatPump != null) return null;
        if (heatPump.isAssignableToLargeOfficeRadiator()) {
            this.heatPump = heatPump;
            return heatPump;
        }
        return null;
    }

    /**
     * Return null. This method is not available for large offices.
     *
     * @param heatPump the heat pump to be assigned
     * @return null
     * */
    @Postcondition(description = "Returns null")
    @Override
    public HeatPump assignHeatPumpException(HeatPump heatPump) {
        return null;
    }

    /**
     * Gets the type of the office.
     *
     * @return the type of the office as a String
     */
    @Postcondition(description = "Returns the type of the office as a String")
    @Override
    public String getType() {
        return "Large Office with Radiator";
    }

}