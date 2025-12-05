/**
 * AUTHOR: Subair
 * STYLE: Object-Oriented Programming
 * This is object-oriented programming because it defines a Resident class with encapsulated attributes (isNative and lifeStage)
 * and provides methods that represent the behaviors of a resident. It models a resident as an object, grouping data and
 * related functionality together, allowing for easy instantiation, encapsulation, and extension through the use of class instances.
 *
 * GOOD: The Resident class demonstrates good object-oriented programming by maintaining high cohesion,
 * encapsulating all attributes and behaviors related to a resident within a single class.
 * It also ensures weak coupling, as it is self-contained and independent of other classes,
 * making it modular, easy to understand, and flexible for future modifications.
 * dding unrelated responsibilities, directly accessing other classes, embedding complex logic, hardcoding dependencies,
 * or managing lifecycle events within the Resident class would reduce its cohesion and increase coupling, making it harder to maintain and test.
 *
 * Represents a resident with life stage and nationality.
 * However, a resident death is not simulated to simplify the simulation.
 */
public class Resident {

    /**
     * Enum representing the different life stages of a resident.
     */
    public enum LifeStage {

        Infant,
        Teen,
        YoungAdult,
        MiddleAgedAdult,
        Elderly
    }

    private Boolean isNative;
    private LifeStage lifeStage;

    /**
     * Constructs a Resident with specified native status and life stage.
     *
     * Precondition: isNative must not be null.
     * Postcondition: Resident is initialized with the given native status and life stage.
     *
     * @param isNative the native status of the resident
     * @param lifeStage the life stage of the resident
     */
    public Resident(Boolean isNative, LifeStage lifeStage) {
        this.isNative = isNative;
        this.lifeStage = lifeStage;
    }

    /**
     * Constructs a Resident with random native status and life stage.
     *
     * Postcondition: Resident is initialized with random native status and life stage.
     */
    public Resident() {
        this(Math.random() < 0.5,
                LifeStage.values()[(int) (Math.random() * LifeStage.values().length)]);
    }

    /**
     * Constructs a Resident with specified native status and random life stage.
     *
     * Precondition: isNative must not be null.
     * Postcondition: Resident is initialized with the given native status and random life stage.
     */
    public Resident(Boolean isNative) {
        this(isNative,
                LifeStage.values()[(int) (Math.random() * LifeStage.values().length)]);
    }

    /**
     * Constructs a Resident with random native status and specified life stage.
     *
     * Postcondition: Resident is initialized with random native status and given life stage.
     */
    public Resident(LifeStage lifeStage) {
        this(Math.random() < 0.5, lifeStage);
    }

    /**
     * Returns whether the resident is native.
     *
     * @return true if the resident is native, false otherwise
     */
    public boolean isNative() {
        return isNative;
    }

    /**
     * Sets the life stage of this resident to a random child stage (Infant or Teen).
     *
     * Postcondition: lifeStage is set to either Infant or Teen.
     */
    public void setAnyChild() {
        this.lifeStage = (Math.random() < 0.5 ? LifeStage.Infant : LifeStage.Teen);
    }

    /**
     * Sets the life stage of this resident to a random adult stage (YoungAdult, MiddleAgedAdult, or Elderly).
     *
     * Postcondition: lifeStage is set to YoungAdult, MiddleAgedAdult, or Elderly.
     */
    public void setAnyAdult() {
        this.lifeStage = LifeStage.values()[2 + (int) (Math.random() * 3)];
    }

    /**
     * Forces the resident to have foreign roots, making them non-native.
     *
     * Postcondition: isNative is set to false.
     */
    public void forceForeignRoots() {
        isNative = false;
    }

    /**
     * Returns the life stage of the resident.
     *
     * @return life stage of the resident
     */
    public LifeStage getLifeStage() {
        return lifeStage;
    }
}

