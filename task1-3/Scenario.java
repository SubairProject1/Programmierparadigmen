/**
 * AUTHOR: Catalin
 * STYLE: Object-Oriented Programming
 * The Scenario class follows object-oriented principles by encapsulating all parameters related to a building scenario
 * —such as lifespan, renovation intervals, and costs—within a single entity. This design keeps related data and access
 * methods organized, allowing easy instantiation and management of different scenarios.
 *
 * Represents a scenario with various parameters affecting building and maintenance.
 * This class models different scenarios by providing a structure to encapsulate parameters like
 * lifespan, renovation interval, costs, and material properties.
 */
public class Scenario {

    private String name;
    private int averageLifespan;
    private int averageRenovationInterval;
    private double costsPerYear;
    private double renovationCost;
    private double materialQuantity;
    private double medianInitialResidentSatisfaction;
    private double maxMaterialPrice;
    private double minMaterialEcologicalCoefficient;
    private double minMaterialQuality;

    /**
     * Constructor for the Scenario class
     * @param name The name of the scenario
     * @param averageLifespan The average lifespan of buildings in this scenario
     * @param averageRenovationInterval The average interval between renovations
     * @param costsPerYear The costs per year for maintenance
     * @param renovationCost The cost of renovation
     * @param materialQuantity The quantity of material needed for renovation
     * @param medianInitialResidentSatisfaction The median initial resident satisfaction
     * @param maxMaterialPrice The maximum price for materials
     * @param minMaterialEcologicalCoefficient The minimum ecological coefficient for materials
     * @param minMaterialQuality The minimum quality for materials
     *
     * Precondition: The name is not null and not empty, averageLifespan > 0, averageRenovationInterval > 0,
     * costsPerYear >= 0, renovationCost >= 0, materialQuantity >= 0, medianInitialResidentSatisfaction >= 0,
     * maxMaterialPrice >= 0, minMaterialEcologicalCoefficient >= 0, minMaterialQuality >= 0.
     *
     * Postcondition: Scenario object is initialized with provided values.
     */
    public Scenario(String name, int averageLifespan, int averageRenovationInterval, double costsPerYear,
                    double renovationCost, double materialQuantity, double medianInitialResidentSatisfaction,
                    double maxMaterialPrice, double minMaterialEcologicalCoefficient, double minMaterialQuality) {

        if(name == null || name.isEmpty()) { throw new IllegalArgumentException("The name is either null or empty."); }
        this.name = name;
        this.averageLifespan = averageLifespan;
        this.averageRenovationInterval = averageRenovationInterval;
        this.costsPerYear = costsPerYear;
        this.renovationCost = renovationCost;
        this.materialQuantity = materialQuantity;
        this.medianInitialResidentSatisfaction = medianInitialResidentSatisfaction;
        this.maxMaterialPrice = maxMaterialPrice;
        this.minMaterialEcologicalCoefficient = minMaterialEcologicalCoefficient;
        this.minMaterialQuality = minMaterialQuality;
    }

    // Getters for each variable in Scenario
    /**
     * Returns the name of the scenario.
     * @return the name of the scenario.
     */
    public String getName() { return name; }

    /**
     * Returns the average lifespan of buildings in this scenario.
     * @return the average lifespan of buildings in this scenario.
     */
    public int getAverageLifespan() { return averageLifespan; }

    /**
     * Returns the average renovation interval.
     * @return the average renovation interval.
     */
    public int getAverageRenovationInterval() { return averageRenovationInterval; }

    /**
     * Returns the costs per year for maintenance.
     * @return the costs per year for maintenance.
     */
    public double getCostsPerYear() { return costsPerYear; }

    /**
     * Returns the cost of renovation.
     * @return the cost of renovation.
     */
    public double getRenovationCost() { return renovationCost; }

    /**
     * Returns the quantity of material needed for renovation.
     * @return the quantity of material needed for renovation.
     */
    public double getMaterialQuantity() { return materialQuantity; }

    /**
     * Returns the median initial resident satisfaction.
     * @return the median initial resident satisfaction.
     */
    public double getMedianInitialResidentSatisfaction() { return medianInitialResidentSatisfaction; }

    /**
     * Returns the maximum price for materials.
     * @return the maximum price for materials.
     */
    public double getMaxMaterialPrice() { return maxMaterialPrice; }

    /**
     * Returns the minimum ecological coefficient for materials.
     * @return the minimum ecological coefficient for materials.
     */
    public double getMinMaterialEcologicalCoefficient() { return minMaterialEcologicalCoefficient; }

    /**
     * Returns the minimum quality for materials.
     * @return the minimum quality for materials.
     */
    public double getMinMaterialQuality() { return minMaterialQuality; }
}
