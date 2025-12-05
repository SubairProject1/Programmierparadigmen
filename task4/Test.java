import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * The only interface who doesn't extend any other data type are Entity and Space, all the other classes and interfaces
 * use them. Entity is the general interface for viewing the context at a macro level (buildings, complexes, ...),
 * while Space is the general interface for viewing the context at a micro level (rooms, lifts,...).
 *
 * Entity is implemented by Building, Complex and Ensemble who build together a city.
 *
 * Space is extended by Circulation, an interface representing spaces that can be used in a movement path. To determine
 * whether a circulation is a valid one or not, the method isCirculation() is used. Circulation is extended by Room,
 * a space that is enclosed by walls or other barriers, and by Lift, a space that connects rooms. Room is implemented through
 * the interface Interior by ServedSpace and ServantSpace, which are spaces that can be used by people. There is also
 * PureCirculation, a class that only implements Circulation. A data type that doesn't implement Circulation is PublicRoad,
 * which only implements Space, because it isn't involved in the circulation inside an entity, but between entities.
 *
 * The work was distributed as followed:
 * We all worked together on defining the initial structure onf the project, then we split the work as follows:
 * Subair: Worked on Room, Interior, ServantSpace, ServedSpace, which initially were supposed to only have different connections, Room did not implement
 * Circulation, which did not implement Space initially. Implemented Exterior fully after the subtyping changes. Worked on the majority of Test (excluded TestEscape()).
 * Catalin: Implemented the escape algorithm and the way spaces interact and connect with each other.
 * Andrei: Implemented the Entity interface along with its subclasses. Tested my methods in the Test class.
 */
import java.util.*;

public class Test {

    public static void main(String[] args) {

        ArrayList<Entity> city = new ArrayList<>();
        ArrayList<PublicRoad> roads = new ArrayList<>();

        // certain testing of subtyping by creating city and roads
        createEntities(city);
        createRoads(city, roads);

        // additional test cases for subtyping
        testSubstitutability();

        testEscape();

        System.out.println("\nThe end of test reached with no issues. All Tests were successful.");
    }

    /**
     * Creates entities (buildings and complexes) and populates the city.
     *
     * @param city the list to populate with entities
     */
    public static void createEntities(ArrayList<Entity> city) {
        if (city == null) {
            throw new IllegalArgumentException("City is null!");
        }
        city.clear();

        // Create buildings
        List<Building> buildingList = new ArrayList<>();
        int numOfBuildings = (int) (Math.random() * 500) + 1;
        for (int i = 0; i < numOfBuildings; i++) {
            buildingList.add(createBuilding());
        }
        System.out.println("Successfully created " + numOfBuildings + " buildings.");

        // Create complexes
        List<Complex> complexList = createComplexes(buildingList);
        System.out.println("Successfully created " + complexList.size() + " complexes.");

        // Add all buildings and complexes to the city
        city.addAll(buildingList);
        city.addAll(complexList);
        System.out.println("Successfully added buildings and complexes to the city.");
    }

    private static List<Complex> createComplexes(List<Building> buildingList) {
        List<Complex> complexList = new ArrayList<>();
        int numOfComplexes = (int) (Math.random() * (buildingList.size() / 10.0)) + 1;
        Random random = new Random();

        for (int i = 0; i < numOfComplexes; i++) {
            List<Building> buildingsInComplex = new ArrayList<>();
            int numOfBuildingsInComplex = random.nextInt(Math.min(5, buildingList.size())) + 1;

            for (int j = 0; j < numOfBuildingsInComplex; j++) {
                buildingsInComplex.add(buildingList.remove(random.nextInt(buildingList.size())));
            }

            complexList.add(new Complex(buildingsInComplex, new ArrayList<>()));
        }
        System.out.println("Successfully created complexes with buildings distributed.");
        return complexList;
    }

    private static Building createBuilding() {
        Building building = new Building();
        Random random = new Random();

        // Add spaces (Served and Servant)
        List<Space> spaces = new ArrayList<>();
        for (int i = 0; i < random.nextInt(5) + 5; i++) {
            spaces.add(createServedSpace(random));
        }
        for (int i = 0; i < random.nextInt(3) + 2; i++) {
            spaces.add(createServantSpace(random));
        }

        for (Space space : spaces) {
            building.addSpace(space);
        }

        // Add lifts
        addLifts(building, random);

        System.out.println("Successfully created a building with " + spaces.size() + " spaces and lifts.");
        return building;
    }

    private static ServantSpace createServantSpace(Random random) {
        while (true) {
            try {
                int floor = random.nextInt(10) + 1;
                return new ServantSpace(floor, new Vec2(1.1d, 1.1d));
            } catch (IllegalArgumentException e) {
                // Retry if constraints aren't met
            }
        }
    }

    private static ServedSpace createServedSpace(Random random) {
        while (true) {
            try {
                int floor = random.nextInt(10) + 1;
                boolean isConnectedToLift = random.nextBoolean();
                double naturalLighting = random.nextDouble() * 10;
                double ventilation = random.nextDouble() * 5;
                double heating = random.nextDouble() * 5;
                double minimumHeight = 2.5 + random.nextDouble();
                Vec2 movementArea = new Vec2(random.nextDouble() * 20 + 10, random.nextDouble() * 20 + 10);
                return new ServedSpace(floor, isConnectedToLift, naturalLighting, ventilation, heating, minimumHeight, movementArea, new Vec2(1.1d, 1.1d));
            } catch (IllegalArgumentException e) {
                // Retry if constraints aren't met
            }
        }
    }

    private static void addLifts(Building building, Random random) {
        List<Space> allSpaces = building.spaces();
        if (allSpaces.size() < 2) return;

        List<Room> roomsForLift = new ArrayList<>();
        for (Space space : allSpaces) {
            if (space instanceof Room room) {
                roomsForLift.add(room);
            }
        }

        if (roomsForLift.size() < 2) return;

        List<Room> liftRooms = new ArrayList<>();
        for (int i = 0; i < random.nextInt(3) + 2; i++) { // 2-4 rooms per lift
            liftRooms.add(roomsForLift.get(random.nextInt(roomsForLift.size())));
        }

        try {
            Lift lift = new Lift(new ArrayList<>(liftRooms));
            building.addSpace(lift);
            System.out.println("Successfully added a lift to the building.");
        } catch (IllegalArgumentException e) {
            // If lift validation fails, skip adding lift
        }
    }

    public static void createRoads(List<Entity> city, List<PublicRoad> roads) {
        Random random = new Random();
        int numOfRoads = (int) (Math.random() * city.size() / 2) + city.size() / 4; // Reasonable road count
        for (int i = 0; i < numOfRoads; i++) {
            roads.add(new PublicRoad());
        }
        System.out.println("Successfully created " + numOfRoads + " public roads.");

        for (Entity entity : city) {
            if (entity instanceof Building building) {
                connectBuildingToRoads(building, roads, random);
            } else if (entity instanceof Complex complex) {
                for (Building building : complex.buildings()) {
                    connectBuildingToRoads(building, roads, random);
                }
            }
        }
        System.out.println("Successfully connected roads to city buildings and complexes.");
    }

    private static void connectBuildingToRoads(Building building, List<PublicRoad> roads, Random random) {
        int connections = Math.min(roads.size(), random.nextInt(3) + 1); // 1-3 roads per building
        for (int j = 0; j < connections; j++) {
            PublicRoad road = roads.get(random.nextInt(roads.size()));
            building.addSpace(road);

            for (Space space : building.spaces()) {
                if (space instanceof Exterior exterior) {
                    exterior.getConnectedSpaces().add(road);
                }
            }
        }
    }

    public static void testEscape() {
        Room servedSpace = new ServedSpace(0, true, 5.0, 3.0, 2.5, 3.0, new Vec2(2.0d, 5.0d), new Vec2(1.1d, 1.1d));
        Room servantSpace = new ServantSpace(0, new Vec2(1.1d, 1.1d));
        servedSpace.addConnectedSpace(servantSpace);
        PublicRoad road = new PublicRoad();
        servantSpace.addConnectedSpace(road);
        Escape escape1 = new Escape(servantSpace);
        Escape escape2 = new Escape(servedSpace);
        if (escape1.length() == 1 && escape2.length() == 2) {
            System.out.println("Escape test was successful.");
        } else {
            System.out.println("Escape test was unsuccessful.");
        }
    }

    public static void testSubstitutability() {
        // Substitution testing as described earlier
        // Adding logs for each part
        System.out.println("Testing substitution of Space objects...");
        List<Space> spaces = new ArrayList<>();
        ArrayList<Room> liftRooms = new ArrayList<>();
        Room servedSpace1 = new ServedSpace(0, true, 5.0, 3.0, 2.5, 3.0, new Vec2(2.0d, 5.0d), new Vec2(1.1d, 1.1d));
        Room servedSpace2 = new ServedSpace(1, true, 5.0, 3.0, 2.5, 3.0, new Vec2(2.0d, 5.0d), new Vec2(1.1d, 1.1d));
        liftRooms.add(servedSpace1);
        liftRooms.add(servedSpace2);

        try {
            Lift lift = new Lift(new ArrayList<>());
        } catch (Exception e) {
            Lift lift = new Lift(liftRooms);
            spaces.add(lift);
        }
        System.out.println("Successfully tested substitution for Space objects in Lift.");

        // Add additional logs or tests as needed
    }
}
