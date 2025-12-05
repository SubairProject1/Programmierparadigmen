/**
 * NO subtype relationship between AdminSet and ApSet:
 *       AdminSet not a subtype of ApSet, because AdminSet has stricter constraints on T (must implemment Admin<X, T>) and
 *           contains extra methods (extend, shorten).
 *       ApSet not a subtype of AdminSet, because ApSet would inherit the stricter constraint on T and the extra methods, which we don't want or need.
 *
 *  NO subtype relationship between Counter and RCounter:
 *       Counter not a subtype of RCounter, because Counter would have to have the fixed type parameters of RCounter, which we don't want, because Counter should
 *           be flexible and work with any type.
 *       RCounter not a subtype of Counter, because RCounter would have to inherit the generic type parameters of Counter, which we don't want, because RCounter
 *          should have fixed type parameters and not be flexible.
 *
 *   The work was distributed as followed:
 *
 *   We all worked together on discussing the classes and their signatures along with their type parameters and eventual subtype relationships,
 *   then we split the work as follows:
 *
 *      Subair: Implemented the iterators in AdminSet and ApSet, helped with the javaDoc comments in Admin, Approvable, AdminSet,
 *      ApprovableSet, ApSet, Counter, Exterior, Interior; wrote Test cases.
 **
 *      Andrei: Implemented the Interior, Exterior, Counter and RCounter classes. Helped with javadoc Comments in Path.
 *
 *      Catalin: Implemented other classes and interfaces and also the data structures that are used in them (maps, sets, lists);
 *      wrote test cases
 *
 */
public class Test {
    public static void main(String[] args) {
        // Step 1: Create the specified objects

        // ApSet Objects
        ApSet<Counter<String>, Counter<String>, String> apSetCounterString = new ApSet<>();
        ApSet<Counter<Integer>, Counter<Integer>, Integer> apSetCounterInteger = new ApSet<>();
        ApSet<Counter<Path<String>>, Counter<Path<String>>, Path<String>> apSetCounterPath = new ApSet<>();
        ApSet<RCounter, RCounter, Path<RCounter>> apSetRCounter = new ApSet<>();
        ApSet<Space<String>, String, Path<Space<String>>> apSetSpace = new ApSet<>();
        ApSet<Interior<String>, String, Path<Space<String>>> apSetInterior = new ApSet<>();
        ApSet<Exterior<String>, String, Path<Space<String>>> apSetExterior = new ApSet<>();

        // AdminSet Objects
        AdminSet<RCounter, RCounter, Path<RCounter>> adminSetRCounter = new AdminSet<>();
        AdminSet<Space<String>, String, Path<Space<String>>> adminSetSpace = new AdminSet<>();
        AdminSet<Interior<String>, String, Path<Space<String>>> adminSetInterior = new AdminSet<>();
        AdminSet<Exterior<String>, String, Path<Space<String>>> adminSetExterior = new AdminSet<>();

        // Step 2: Fill containers with entries

        // Counter and RCounter Objects
        Counter<String> counter1 = new Counter<>("Counter1");
        Counter<String> counter2 = new Counter<>("Counter2");
        Counter<Integer> counter3 = new Counter<>(1);
        Counter<Integer> counter4 = new Counter<>(2);
        Counter<Path<String>> counter5 = new Counter<>(new Path<>());
        Counter<Path<String>> counter6 = new Counter<>(new Path<>());
        RCounter rCounter1 = new RCounter();
        RCounter rCounter2 = new RCounter();

        apSetCounterString.add(counter1);
        apSetCounterString.add(counter2);
        apSetCounterInteger.add(counter3);
        apSetCounterInteger.add(counter4);
        apSetCounterPath.add(counter5);
        apSetCounterPath.add(counter6);
        apSetRCounter.add(rCounter1);
        apSetRCounter.add(rCounter2);

        // Space, Interior, and Exterior Objects
        Space<String> hallway = new Space<>("Hallway");
        Space<String> garden = new Space<>("Garden");
        Interior<String> livingRoom = new Interior<>("Living Room", 30.0);
        Interior<String> kitchen = new Interior<>("Kitchen", 20.0);
        Exterior<String> patio = new Exterior<>("Patio", false);
        Exterior<String> backyard = new Exterior<>("Backyard", true);

        apSetSpace.add(hallway);
        apSetSpace.add(garden);
        apSetInterior.add(livingRoom);
        apSetInterior.add(kitchen);
        apSetExterior.add(patio);
        apSetExterior.add(backyard);

        adminSetSpace.add(hallway);
        adminSetSpace.add(garden);
        adminSetInterior.add(livingRoom);
        adminSetInterior.add(kitchen);
        adminSetExterior.add(patio);
        adminSetExterior.add(backyard);

        // Step 3: Transfer objects from b (Interior) and c (Exterior) to a (Space)
        System.out.println("\nTransferring Entries from AdminSet<Interior> and AdminSet<Exterior> to AdminSet<Space>...");
        adminSetInterior.iteratorAll().forEachRemaining(interior -> {
            System.out.println("Transferring: " + interior + " (Area: " + interior.area() + " m²)");
            adminSetSpace.add(interior);
        });

        adminSetExterior.iteratorAll().forEachRemaining(exterior -> {
            System.out.println("Transferring: " + exterior + " (Public: " + exterior.isPublic() + ")");
            adminSetSpace.add(exterior);
        });

        System.out.println("\nThere are no subtype relationships between AdminSet and ApSet, nor between Counter and RCounter.");

        // Step 4: Test deletion and re-adding of entries
        System.out.println("\nTesting Add and Remove:");
        System.out.println("Before Removal:");
        adminSetSpace.iteratorAll().forEachRemaining(System.out::println);

        adminSetSpace.remove(hallway);

        System.out.println("\nAfter Removal:");
        adminSetSpace.iteratorAll().forEachRemaining(System.out::println);

        adminSetSpace.add(hallway);

        System.out.println("\nAfter Re-Adding:");
        adminSetSpace.iteratorAll().forEachRemaining(System.out::println);
    }
}


