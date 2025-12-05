import java.util.ArrayList;

@Responsible(groupMember = "Catalin", description = "Implementation of the test cases")
public class Test {
    public static void main(String[] args) {
        System.out.println("### Test Case 1: Adding and Deleting Heat Pumps (Normal Case) ###");
        testAddAndDeleteHeatPumps();

        System.out.println("\n### Test Case 2: Adding Null Heat Pumps (Boundary Case) ###");
        testAddNullHeatPump();

        System.out.println("\n### Test Case 3: Deleting Non-Existent Heat Pumps (Invalid Case) ###");
        testDeleteNonExistentHeatPump();

        System.out.println("\n### Test Case 4: Assigning Heat Pumps to Offices ###");
        testAssignHeatPump();

        System.out.println("\n### Running DataExtractor ###");
        DataExtractor.main(null); // Reflectively analyze program annotations
    }

    /**
     * Test Case 1: Normal Case for adding and deleting heat pumps.
     */
    private static void testAddAndDeleteHeatPumps() {
        ArrayList<HeatPump> heatPumpList = new ArrayList<>();
        OfficeOperator officeOperator = new OfficeOperator(new ArrayList<>(), heatPumpList);

        HeatPump floorHeatingLow = new FloorHeating(500, HeatPump.Power.Low);
        HeatPump radiatorMedium = new Radiator(700, HeatPump.Power.Medium);

        // Add heat pumps
        officeOperator.addHeatPump(floorHeatingLow);
        officeOperator.addHeatPump(radiatorMedium);

        System.out.println("Added Heat Pumps:");
        officeOperator.showHeatPumps(); // Display current inventory

        // Delete a heat pump
        officeOperator.deleteHeatPump(floorHeatingLow);
        System.out.println("\nAfter Deleting Floor Heating (Low):");
        officeOperator.showHeatPumps();
    }

    /**
     * Test Case 2: Boundary Case for adding null heat pumps.
     */
    private static void testAddNullHeatPump() {
        ArrayList<HeatPump> heatPumpList = new ArrayList<>();
        OfficeOperator officeOperator = new OfficeOperator(new ArrayList<>(), heatPumpList);

        officeOperator.addHeatPump(null); // Attempt to add a null heat pump

        System.out.println("After Trying to Add Null Heat Pump:");
        officeOperator.showHeatPumps(); // Inventory should remain empty
    }

    /**
     * Test Case 3: Invalid Case for deleting non-existent heat pumps.
     */
    private static void testDeleteNonExistentHeatPump() {
        ArrayList<HeatPump> heatPumpList = new ArrayList<>();
        OfficeOperator officeOperator = new OfficeOperator(new ArrayList<>(), heatPumpList);

        HeatPump floorHeatingHigh = new FloorHeating(1200, HeatPump.Power.High);

        // Attempt to delete a heat pump that doesn't exist
        officeOperator.deleteHeatPump(floorHeatingHigh);

        System.out.println("After Trying to Delete Non-Existent Heat Pump:");
        officeOperator.showHeatPumps(); // Inventory should remain unchanged
    }

    /**
     * Test Case 4: Assign Heat Pumps to Offices.
     */
    private static void testAssignHeatPump() {
        HeatPump floorHeatingLow = new FloorHeating(500, HeatPump.Power.Low);
        HeatPump floorHeatingMedium = new FloorHeating(800, HeatPump.Power.Medium);
        HeatPump radiatorHigh = new Radiator(1200, HeatPump.Power.High);

        Office smallOfficeFloorHeating = new SmallOfficeFloorHeating(null);
        Office mediumOfficeFloorHeating = new MediumOfficeFloorHeating(null);
        Office largeOfficeRadiator = new LargeOfficeRadiator(null);

        System.out.println("Assigning Heat Pump to Small Office with Floor Heating:");
        System.out.println(assignHeatPumpTest(smallOfficeFloorHeating, floorHeatingLow));

        System.out.println("\nAssigning Heat Pump to Medium Office with Floor Heating:");
        System.out.println(assignHeatPumpTest(mediumOfficeFloorHeating, floorHeatingMedium));

        System.out.println("\nAssigning Heat Pump to Large Office with Radiator:");
        System.out.println(assignHeatPumpTest(largeOfficeRadiator, radiatorHigh));

        System.out.println("\nAttempting to Assign Incompatible Heat Pump to Large Office:");
        System.out.println(assignHeatPumpTest(largeOfficeRadiator, floorHeatingLow));
    }

    /**
     * Helper method to test assignHeatPump and return the result.
     */
    private static String assignHeatPumpTest(Office office, HeatPump heatPump) {
        if (office.assignHeatPump(heatPump) != null) {
            return "Successfully assigned " + heatPump.getClass().getSimpleName() +
                    " to " + office.getType();
        } else {
            return "Failed to assign " + heatPump.getClass().getSimpleName() +
                    " to " + office.getType();
        }
    }
}
