/**
 * This class represents a medium office with a floor heating.
 */
@Responsible(groupMember = "Catalin", description = "Implementation of the structure suggested by Subair")
public class MediumOfficeFloorHeating extends Office {

    /**
     * Constructor for the MediumOfficeFloorHeating class that initializes the heat pump.
     *
     * @param heatPump the heat pump to be assigned to the medium office with floor heating
     */
    @Precondition(description = "heatPump != null")
    @Postcondition(description = "The heat pump is assigned to the medium office with floor heating")
    MediumOfficeFloorHeating(HeatPump heatPump) {
        super(heatPump);
    }

    /**
     * Assigns a heat pump to the medium office with floor heating. Returns the assigned heat pump, or null if the
     * heatPump is null or is not assignable to this.
     *
     * @param heatPump the heat pump to be assigned
     * @return the assigned heat pump, or null if the office already has a heat pump or the heat pump is not assignable
     */
    @Postcondition(description = "If the heat pump is not null and is assignable to this, it is assigned and returned. Otherwise, null is returned.")
    @Override
    public HeatPump assignHeatPump(HeatPump heatPump) {
        if (this.heatPump != null) return null;
        if (heatPump.isAssignableToMediumOfficeFloorHeating()) {
            this.heatPump = heatPump;
            return heatPump;
        }
        return null;
    }

    /**
     * Tries Assigning a high power heat pump to the medium office with floor heating.
     * To be called only if assignHeatPump with non-null argument returns null.
     *
     * @param heatPump the heat pump to be assigned
     * @return the assigned heat pump, or null if heatPump is null or is not assignable to this
     * */
    @Postcondition(description = "If the heat pump is not null and is assignable to this, it is assigned and returned. Otherwise, null is returned.")
    @Override
    public HeatPump assignHeatPumpException(HeatPump heatPump) {
        if (this.heatPump != null) return null;
        if (heatPump.isAssignableToLargeOfficeFloorHeating()) {
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
        return "Medium Office with Floor Heating";
    }

}