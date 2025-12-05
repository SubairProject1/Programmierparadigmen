/**
 * This class represents a small office with a radiator.
 */
@Responsible(groupMember = "Subair", description = "Implmentation of this class and a guideline for every \"office-radiator\" combination.")
public class SmallOfficeRadiator extends Office {

    /**
     * Constructor for the SmallOfficeRadiator class that initializes the heat pump.
     *
     * @param heatPump the heat pump to be assigned to the small office with radiator
     */
    @Precondition(description = "heatPump != null")
    @Postcondition(description = "The heat pump is assigned to the small office with radiator")
    SmallOfficeRadiator(HeatPump heatPump) {
        super(heatPump);
    }

    /**
     * Assigns a heat pump to the small office with radiator. Returns the assigned heat pump, or null if the
     * heatPump is null or is not assignable to this.
     *
     * @param heatPump the heat pump to be assigned
     * @return the assigned heat pump, or null if the office already has a heat pump or the heat pump is not assignable
     */
    @Postcondition(description = "If the heat pump is not null and is assignable to this, it is assigned and returned. Otherwise, null is returned.")
    @Override
    public HeatPump assignHeatPump(HeatPump heatPump) {
        if(this.heatPump != null) return null;
        if(heatPump.isAssignableToSmallOfficeRadiator()) {
            this.heatPump = heatPump;
            return heatPump;
        }
        return null;
    }

    /**
     * Tries assigning a medium power heat pump to the small office with radiator.
     * To be called only if assignHeatPump with non-null argument returns null.
     *
     * @param heatPump the heat pump to be assigned
     * @return the assigned heat pump, or null if heatPump is null or is not assignable to this
     * */
    @Postcondition(description = "If the heat pump is not null and is assignable to this, it is assigned and returned. Otherwise, null is returned.")
    @Override
    public HeatPump assignHeatPumpException(HeatPump heatPump) {
        if(this.heatPump != null) return null;
        if(heatPump.isAssignableToMediumOfficeRadiator()) {
            this.heatPump = heatPump;
            return heatPump;
        }
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
        return "Small Office with Radiator";
    }
}
