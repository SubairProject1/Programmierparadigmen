/**
 *
 * Catalin: Implemented Building, BuildingGen, Office, OfficeGen and Test cases.
 * Andrei: Company, CompanyGen
 * Subair: Implemented Room, SideRoom and UtilRoom; Additionally helped with Test slightly.
 */
public class Test {
    public static void main(String[] args) {
        // Test Non-Generic Office
        System.out.println("Testing Non-Generic Office:");
        Office office = new Office(1, 50.0f); // Office with 50m^2 side rooms area

        // Create rooms
        UtilRoom room1 = new UtilRoom("Room A", 5.0f, 4.0f, 3.0f, 6.0f, 0.0f, 3);
        UtilRoom room2 = new UtilRoom("Room B", 6.0f, 5.0f, 3.0f, 0.0f, 8000.0f, 0);

        // Add rooms to the non-generic office
        office.addRoom(room1);
        office.addRoom(room2);

        // Display statistics for non-generic office
        System.out.println("Total area: " + office.calculateTotalArea() + " m^2");
        System.out.println("Average room area: " + office.calculateAverageRoomArea() + " m^2");
        System.out.println("Average window to room area ratio: " + office.calculateAverageWindowToRoomAreaRatio());
        System.out.println("Average storage volume: " + office.calculateAverageStorageVolume() + " m^3");
        System.out.println("Average workplaces: " + office.calculateAverageWorkplaces());
        System.out.println("Average light flux per area: " + office.calculateAverageLightFluxPerArea() + " lm/m^2");

        // Test Generic OfficeGen
        System.out.println("\nTesting Generic OfficeGen:");
        OfficeGen<Room> officeGen = new OfficeGen<>(2, 50.0f); // OfficeGen with 50m^2 side rooms area

        // Add rooms to the generic office
        officeGen.addRoom(room1);
        officeGen.addRoom(room2);

        // Display statistics for generic office
        System.out.println("Total area: " + officeGen.calculateTotalArea() + " m^2");
        System.out.println("Average room area: " + officeGen.calculateAverageRoomArea() + " m^2");
        System.out.println("Average window to room area ratio: " + officeGen.calculateAverageWindowToRoomAreaRatio());
        System.out.println("Average storage volume: " + officeGen.calculateAverageStorageVolume() + " m^3");
        System.out.println("Average workplaces: " + officeGen.calculateAverageWorkplaces());
        System.out.println("Average light flux per area: " + officeGen.calculateAverageLightFluxPerArea() + " lm/m^2");

        // Testing edge cases
        System.out.println("\nTesting Edge Cases:");

        // Non-Generic Building
        System.out.println("\nTesting Non-Generic Building:");
        Building building = new Building("Building A");

        // Create and add offices
        Office office1 = new Office(1, 50.0f);
        Office office2 = new Office(2, 75.0f);
        room1 = new UtilRoom("Room A", 5.0f, 4.0f, 3.0f, 6.0f, 0.0f, 3);
        room2 = new UtilRoom("Room B", 6.0f, 5.0f, 3.0f, 0.0f, 8000.0f, 0);

        office1.addRoom(room1);
        office2.addRoom(room2);

        building.addOffice(office1);
        building.addOffice(office2);

        // Display offices
        building.displayOffices();

        // Remove an office and display again
        building.removeOffice(1);
        System.out.println("\nAfter removing Office 1:");
        building.displayOffices();

        // Generic BuildingGen
        System.out.println("\nTesting Generic BuildingGen:");
        BuildingGen<OfficeGen<Room>> buildingGen = new BuildingGen<>("Building B");

        // Create and add generic offices
        OfficeGen<Room> officeGen1 = new OfficeGen<>(3, 60.0f);
        OfficeGen<Room> officeGen2 = new OfficeGen<>(4, 80.0f);

        officeGen1.addRoom(room1);
        officeGen2.addRoom(room2);

        buildingGen.addOffice(officeGen1);
        buildingGen.addOffice(officeGen2);

        // Display generic offices
        buildingGen.displayOffices();

        // Remove a generic office and display again
        buildingGen.removeOffice(3);
        System.out.println("\nAfter removing OfficeGen 3:");
        buildingGen.displayOffices();
        System.out.println();

        CompanyGen<BuildingGen<OfficeGen<Room>>> companyGen = new CompanyGen<>("Innovative Solutions");

        BuildingGen<OfficeGen<Room>> buildingGen1 = new BuildingGen<>("Building C");
        BuildingGen<OfficeGen<Room>> buildingGen2 = new BuildingGen<>("Building D");

        officeGen1.addRoom(room1);
        officeGen2.addRoom(room2);

        buildingGen1.addOffice(officeGen1);
        buildingGen2.addOffice(officeGen2);

        companyGen.addBuilding(buildingGen1);
        companyGen.addBuilding(buildingGen2);

        System.out.println(companyGen);

        companyGen.removeBuilding("Building C");
        System.out.println("\nAfter removing Building C:");
        System.out.println(companyGen);

        testEdgeCases();
    }

    public static void testEdgeCases() {
        // Edge Case 1: Empty Office
        System.out.println("\nEdge Case 1: Empty Office");
        Office emptyOffice = new Office(3, 20.0f);
        System.out.println("Total area: " + emptyOffice.calculateTotalArea() + " m^2");
        System.out.println("Average room area: " + emptyOffice.calculateAverageRoomArea() + " m^2");

        // Edge Case 2: Empty Generic OfficeGen
        System.out.println("\nEdge Case 2: Empty Generic OfficeGen");
        OfficeGen<Room> emptyOfficeGen = new OfficeGen<>(4, 20.0f);
        System.out.println("Total area: " + emptyOfficeGen.calculateTotalArea() + " m^2");
        System.out.println("Average room area: " + emptyOfficeGen.calculateAverageRoomArea() + " m^2");

        // Edge Case 3: Office with only side rooms (no usable rooms)
        System.out.println("\nEdge Case 3: Office with only Side Rooms");
        Office sideOnlyOffice = new Office(5, 100.0f);
        System.out.println("Total area: " + sideOnlyOffice.calculateTotalArea() + " m^2");
        System.out.println("Average room area: " + sideOnlyOffice.calculateAverageRoomArea() + " m^2");

        OfficeGen<Room> sideOnlyOfficeGen = new OfficeGen<>(6, 100.0f);
        System.out.println("Total area: " + sideOnlyOfficeGen.calculateTotalArea() + " m^2");
        System.out.println("Average room area: " + sideOnlyOfficeGen.calculateAverageRoomArea() + " m^2");

        // Edge Case 4: Rooms with invalid ratios
        System.out.println("\nEdge Case 4: Rooms with Invalid Ratios");
        UtilRoom invalidRoom = new UtilRoom("Room C", 5.0f, 5.0f, 3.0f, 0.0f, 0.0f, 0);

        Office invalidOffice = new Office(7, 10.0f);
        invalidOffice.addRoom(invalidRoom);

        System.out.println("Total area: " + invalidOffice.calculateTotalArea() + " m^2");
        System.out.println("Average room area: " + invalidOffice.calculateAverageRoomArea() + " m^2");

        OfficeGen<Room> invalidOfficeGen = new OfficeGen<>(8, 10.0f);
        invalidOfficeGen.addRoom(invalidRoom);

        System.out.println("Total area: " + invalidOfficeGen.calculateTotalArea() + " m^2");
        System.out.println("Average room area: " + invalidOfficeGen.calculateAverageRoomArea() + " m^2");

        // Edge Case 5: Switching between office and storage
        System.out.println("\nEdge Case 5: Switching Room Types");
        UtilRoom switchingRoom = new UtilRoom("Room D", 6.0f, 4.0f, 3.0f, 5.0f, 0.0f, 5);

        System.out.println("Initially an office with workplaces: " + switchingRoom.getNoOfWorkplaces());
        switchingRoom.turnIntoStorage();
        System.out.println("Now a storage room with volume: " + switchingRoom.getStorageVolume() + " m^3");

        switchingRoom.turnToOffice(10);
        System.out.println("Turned back into an office with workplaces: " + switchingRoom.getNoOfWorkplaces());

        // Edge Case 6: Empty Building
        System.out.println("\nEdge Case 6: Empty Building");
        Building emptyBuilding = new Building("Empty Building");
        System.out.println(emptyBuilding);

        // Edge Case 7: Building with no offices
        System.out.println("\nEdge Case 7: Building with only side rooms and no usable offices");
        Building buildingWithOnlySideRooms = new Building("Building With Only Side Rooms");
        System.out.println(buildingWithOnlySideRooms);

        // Edge Case 8: Empty BuildingGen
        System.out.println("\nEdge Case 8: Empty BuildingGen");
        BuildingGen<OfficeGen<Room>> emptyBuildingGen = new BuildingGen<>("Empty BuildingGen");
        System.out.println(emptyBuildingGen);

        // Edge Case 9: Empty Company
        System.out.println("\nEdge Case 9: Empty Company");
        Company emptyCompany = new Company("Empty Company");
        System.out.println(emptyCompany);

        // Edge Case 10: CompanyGen with no buildings
        System.out.println("\nEdge Case 10: CompanyGen with no buildings");
        CompanyGen<BuildingGen<OfficeGen<Room>>> emptyCompanyGen = new CompanyGen<>("Empty CompanyGen");
        System.out.println(emptyCompanyGen);

        // Edge Case 11: Company with only empty buildings
        System.out.println("\nEdge Case 11: Company with only empty buildings");
        Company companyWithEmptyBuildings = new Company("Company With Empty Buildings");
        Building emptyBuilding1 = new Building("Empty Building 1");
        Building emptyBuilding2 = new Building("Empty Building 2");
        companyWithEmptyBuildings.addBuilding(emptyBuilding1);
        companyWithEmptyBuildings.addBuilding(emptyBuilding2);
        System.out.println(companyWithEmptyBuildings);

        // Edge Case 12: CompanyGen with only empty BuildingGen
        System.out.println("\nEdge Case 12: CompanyGen with only empty BuildingGen");
        CompanyGen<BuildingGen<OfficeGen<Room>>> companyGenWithEmptyBuildings = new CompanyGen<>("CompanyGen With Empty Buildings");
        BuildingGen<OfficeGen<Room>> emptyBuildingGen1 = new BuildingGen<>("Empty BuildingGen 1");
        BuildingGen<OfficeGen<Room>> emptyBuildingGen2 = new BuildingGen<>("Empty BuildingGen 2");
        companyGenWithEmptyBuildings.addBuilding(emptyBuildingGen1);
        companyGenWithEmptyBuildings.addBuilding(emptyBuildingGen2);
        System.out.println(companyGenWithEmptyBuildings);
    }
}
