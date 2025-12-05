import java.lang.reflect.*;
import java.util.*;

@BelongsToProgram(
        description = "Extracts runtime metadata such as responsibilities, methods, and invariants for the program",
        components = {
                "HeatPump",
                "FloorHeating",
                "Radiator",
                "Office",
                "OfficeOperator",
                "LargeOfficeFloorHeating",
                "LargeOfficeRadiator",
                "MediumOfficeFloorHeating",
                "MediumOfficeRadiator",
                "SmallOfficeFloorHeating",
                "SmallOfficeRadiator",
                "Responsible",
                "Precondition",
                "Postcondition",
                "Invariant",
                "BelongsToProgram"
        }
)
@Responsible(groupMember = "Catalin", description = "Catalin implemented the structure and the first three key points, " +
        "while Subair was responsible for the last three points.")
public class DataExtractor {

    public static void main(String[] args) {
        List<Class<?>> classes = getAnnotatedClasses(DataExtractor.class);
        extractData(classes);
        findResponsibles(classes);
    }

    /**
     * Retrieves all classes listed in the @BelongsToProgram annotation dynamically.
     */
    public static List<Class<?>> getAnnotatedClasses(Class<?> annotatedClass) {
        List<Class<?>> classList = new ArrayList<>();
        if (annotatedClass.isAnnotationPresent(BelongsToProgram.class)) {
            BelongsToProgram annotation = annotatedClass.getAnnotation(BelongsToProgram.class);
            for (String className : annotation.components()) {
                try {
                    Class<?> clazz = Class.forName(className);
                    classList.add(clazz);
                } catch (ClassNotFoundException e) {
                    System.err.println("Class not found: " + className);
                }
            }
        }
        return classList;
    }

    /**
     * Extracts runtime metadata, including class-level annotations, methods, and constructors.
     * If a constructor has no @Responsible, it uses the class-level @Responsible annotation.
     */
    public static void extractData(List<Class<?>> classes) {
        System.out.println("### Runtime Data Extraction ###\n");

        for (Class<?> clazz : classes) {
            System.out.println("Class: " + clazz.getSimpleName());

            Responsible classResponsible = clazz.getAnnotation(Responsible.class);
            if (classResponsible != null) {
                System.out.println(" - Responsible: " + classResponsible.groupMember());
                System.out.println(" - Description: " + classResponsible.description());
            }

            if (clazz.isAnnotationPresent(Invariant.class)) {
                Invariant invariant = clazz.getAnnotation(Invariant.class);
                System.out.println(" - Invariant: " + invariant.description());
            }

            System.out.println("\nMethods:");
            for (Method method : clazz.getDeclaredMethods()) {
                System.out.println(" - Method: " + method.getName());

                if (method.isAnnotationPresent(Responsible.class)) {
                    Responsible methodResponsible = method.getAnnotation(Responsible.class);
                    System.out.println("   Responsible: " + methodResponsible.groupMember());
                    System.out.println("   Description: " + methodResponsible.description());
                }

                if (method.isAnnotationPresent(Precondition.class)) {
                    System.out.println("   Precondition: " + method.getAnnotation(Precondition.class).description());
                }
                if (method.isAnnotationPresent(Postcondition.class)) {
                    System.out.println("   Postcondition: " + method.getAnnotation(Postcondition.class).description());
                }
            }

            System.out.println("\nConstructors:");
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                System.out.print(" - Constructor: " + constructor.getName());

                // Use class-level Responsible annotation if no constructor-level annotation
                if (classResponsible != null) {
                    System.out.println(" | Responsible: " + classResponsible.groupMember());
                } else {
                    System.out.println(" | No Responsible " +
                            "annotation found.");
                }
            }

            System.out.println("\n---------------------------\n");
        }
    }

    /**
     * Finds and displays responsibilities:
     * 4th Point: Count classes/interfaces/annotations.
     * 5th Point: Count methods and constructors.
     * 6th Point: Count assertions in classes, methods, and constructors.
     */
    public static void findResponsibles(List<Class<?>> classes) {
        System.out.println("### Responsibility Summary ###");

        // 4th Point: Count classes/interfaces/annotations
        HashMap<String, Integer> responsibleClasses = new HashMap<>();
        for (Class<?> clazz : classes) {
            Responsible classResponsible = clazz.getAnnotation(Responsible.class);
            if (classResponsible != null) {
                responsibleClasses.put(classResponsible.groupMember(),
                        responsibleClasses.getOrDefault(classResponsible.groupMember(), 0) + 1);
            }
        }

        responsibleClasses.forEach((key, value) ->
                System.out.println(key + " is responsible for " + value + " class(es), interface(s), or annotation(s)."));

        System.out.println();

        // 5th Point: Count methods and constructors
        HashMap<String, Integer> responsibleMethods = new HashMap<>();
        for (Class<?> clazz : classes) {
            Responsible classResponsible = clazz.getAnnotation(Responsible.class);
            if (classResponsible != null) {
                int count = clazz.getDeclaredMethods().length + clazz.getDeclaredConstructors().length;
                responsibleMethods.put(classResponsible.groupMember(),
                        responsibleMethods.getOrDefault(classResponsible.groupMember(), 0) + count);
            }
        }

        responsibleMethods.forEach((key, value) ->
                System.out.println(key + " is responsible for " + value + " method(s) and constructor(s)."));

        System.out.println();

        // 6th Point: Count assertions
        HashMap<String, Integer> responsibleAssertions = new HashMap<>();
        for (Class<?> clazz : classes) {
            Responsible classResponsible = clazz.getAnnotation(Responsible.class);
            if (classResponsible != null) {
                int count = 0;
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Precondition.class) || method.isAnnotationPresent(Postcondition.class)) {
                        count++;
                    }
                }
                responsibleAssertions.put(classResponsible.groupMember(),
                        responsibleAssertions.getOrDefault(classResponsible.groupMember(), 0) + count);
            }
        }

        responsibleAssertions.forEach((key, value) ->
                System.out.println(key + " is responsible for " + value + " assertion(s)."));
    }
}
