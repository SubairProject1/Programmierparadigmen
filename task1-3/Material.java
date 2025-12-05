/**
 * AUTHOR: Catalin
 * STYLE: Object-Oriented Programming
 * The Material class exemplifies object-oriented programming by encapsulating all relevant properties and methods for a material,
 * such as quality, ecologicalCoefficient, and cost, along with related behaviors (e.g., calculating CO2 emissions).
 * This cohesive design follows encapsulation principles, making it easy to understand, test, and reuse.
 *
 * This class represents a material with its quality, ecological coefficient and cost.
 *
 * GOOD: This class has high cohesion because it encapsulates all properties and behaviors related to a material.
 * Each method and attribute is directly related to the concept of a material, making it easy to understand and maintain.
 * A less cohesive variant would mix unrelated responsibilities, such as including methods for managing inventory or orders.
 */
public class Material {


    private double quality;
    private double ecologicalCoefficient;
    private double cost;

    /**
     * Constructor for the Material class
     *
     * @param quality              the quality of the material
     * @param ecologicalCoefficient the ecological coefficient of the material
     * @param cost                 the cost of the material
     *
     * Precondition: Quality, ecologicalCoefficient, and cost are non-negative
     * Postcondition: The material object is initialized with the given values
     */
    public Material(double quality, double ecologicalCoefficient, double cost) {
        assert quality >= 0 : "Quality must be non-negative";
        assert cost >= 0 : "Cost must be non-negative";

        this.quality = quality;
        this.ecologicalCoefficient = ecologicalCoefficient;
        this.cost = cost;
    }

    /**
     * Returns the cost of the material.
     *
     * @return the cost of the material.
     */
    public double getCost() {
        return cost;
    }

    /**
     * Returns the ecological coefficient of the material.
     *
     * @return the ecological coefficient of the material.
     */
    public double getEcologicalCoefficient() {
        return ecologicalCoefficient;
    }

    /**
     * Returns the quality of the material.
     *
     * @return the quality of the material.
     */
    public double getQuality() {
        return quality;
    }

    /**
     * Calculates the CO2 emission per year based on the ecological coefficient.
     *
     * @return the CO2 emission per year.
     */
    public double calculateCO2EmissionPerYear() {
        return 10 * (10 - ecologicalCoefficient);
    }

    /**
     * Calculates the CO2 emission during the construction phase based on the ecological coefficient.
     *
     * @return the CO2 emission during construction.
     */
    public double constructionCO2Emission() {
        return 70 * (10 - ecologicalCoefficient);
    }

    @Override
    public String toString() {
        return "Material{" +
                "quality=" + quality +
                ", ecologicalCoefficient=" + ecologicalCoefficient +
                ", cost=" + cost +
                '}';
    }
}
