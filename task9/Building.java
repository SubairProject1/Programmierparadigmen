import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents a Building along with its structure and behavior for different types of sustainability scenarios.
 * Each implementation of Building defines a different building model with specific material quantity, costs and CO2 Emissions.
 *
 */
public class Building {

    final private BuildingType buildingType;

    /**
     * BAD: The BuildingType enum is tightly coupled with the Building class, making it less flexible and harder to extend.
     * This approach lacks dynamic binding, making it difficult to add new building types without modifying the Building class.
     * A better solution would be to use a class hierarchy with a common interface or abstract class for different building types.
     */
    private enum BuildingType {
        DefaultHouse,
        OneFamilyHouse,
        Duplex,
        Triplex,
        CommunalApartment,
        TemporaryShelter,
        NursingHome;
    }

    private Resident[] residents;
    private String scenarioName;
    private int averageLifespan;
    private int averageRenovationInterval;
    private double constructionCost;
    private double CO2EmissionsPerYear;
    private double constructionCO2Emissions;
    private double costsPerYear;
    private double renovationCost;
    private double materialQuantity;

    private Material material;

    private Ground ground;
    private int initResidentSatisfaction;

    private int lifeSpan;
    private List<Integer> renovationSchedule = new ArrayList<>();
    private boolean isStanding;

    private double totalCost;
    private double totalCO2Emission;
    private double totalWaste;
    private double residentSatisfaction;

    public Building (Scenario scenario) {

        material = MaterialsJsonParser.getRandomMaterial(MaterialsJsonParser.parseMaterials(),
                scenario.getMaxMaterialPrice(), scenario.getMinMaterialEcologicalCoefficient(),
                scenario.getMinMaterialQuality());

        scenarioName = scenario.getName();
        averageLifespan = scenario.getAverageLifespan();
        averageRenovationInterval = scenario.getAverageRenovationInterval();
        constructionCost = material.getCost();
        CO2EmissionsPerYear = material.calculateCO2EmissionPerYear();
        constructionCO2Emissions = material.constructionCO2Emission();
        costsPerYear = scenario.getCostsPerYear();
        renovationCost = scenario.getRenovationCost();
        materialQuantity = scenario.getMaterialQuantity();

        initResidentSatisfaction = (int) (scenario.getMedianInitialResidentSatisfaction() + Math.random() * 2);
        residentSatisfaction = initResidentSatisfaction;
        lifeSpan = MathUtils.gaussian(averageLifespan, 6);
        totalCost = 0;
        totalCO2Emission = 0;
        isStanding = true;

        int toAdd = MathUtils.gaussian(averageRenovationInterval, 1);
        while (toAdd < lifeSpan) {
            renovationSchedule.add(toAdd);
            toAdd += MathUtils.gaussian(averageRenovationInterval, 1);
        }
        buildingType = getRandomBuildingType();
        createResidents();
    }

    /**
     * Constructs Building and adds scenario-specific costs and CO2 emissions to the total.
     * Building is standing after calling of this method.
     */
    public void roughConstruction() {
        isStanding = true;
        totalCost += constructionCost;
        totalCO2Emission += constructionCO2Emissions;

        //totalCosts increased only if unexpected costs occur.
        totalCost += Math.random() * ground.costFactor() * unexpectedCostFactor() * constructionCost;
    }

    /**
     * Revitalizes the building by replacing half of the material with a new better one.
     * Adds costs and CO2 emissions to the total.
     * Increases resident satisfaction to the initial level.
     *
     * STYLE: Functional Programming
     * This part demonstrates functional style by using lambdas and higher-order functions to retrieve and apply a new material.
     * Integration is achieved by applying the updated values directly to the object's properties once calculated.
     */
    public void revitalize() {
        // Supplier to fetch a new material with higher standards
        Supplier<Optional<Material>> newMaterialSupplier = () -> Optional.ofNullable(
                MaterialsJsonParser.getRandomMaterial(
                        MaterialsJsonParser.parseMaterials(),
                        material.getCost() * 1.20,
                        material.getEcologicalCoefficient() * 1.05,
                        material.getQuality() * 1.05
                )
        );

        // Attempt to replace material if a new one is found
        newMaterialSupplier.get().ifPresent(newMaterial -> {
            // Update material
            material = newMaterial;

            // Add half of the new material cost to total cost, including potential unexpected costs
            double addedCost = material.getCost() / 2;
            totalCost += addedCost;
            totalCost += addedCost * Math.random() * ground.costFactor() * unexpectedCostFactor();

            // Update CO2 emissions per year and total CO2 emissions based on new material
            CO2EmissionsPerYear += material.calculateCO2EmissionPerYear();
            totalCO2Emission += material.constructionCO2Emission()/2;

            // Reset resident satisfaction to the initial level
            residentSatisfaction = initResidentSatisfaction;
        });
    }

    /**
     * Renovates the building according to the renovation plan generated in the constructor
     * of each Building. Removes renovation from the schedule and adds costs and waste to the total.
     * Increments resident satisfaction to near the initial level.
     */
    public void renovate() {
        renovationSchedule.remove(0);
        totalCost += renovationCost;
        totalCost += Math.random() * ground.costFactor() * unexpectedCostFactor() * renovationCost;
        totalWaste += materialQuantity/2;
        residentSatisfaction +=
                1 + (Math.random() * (initResidentSatisfaction - residentSatisfaction - 1.5));
    }

    /**
     * Calculates the cost factor due to unforeseen events such as planning issues or problems during construction.
     * The factor is a multiplier to the base cost, reflecting the increased costs due to these events.
     *
     * Event type thresholds:
     *  - Planning Issue: 5% chance of a factor up to 1.30
     *  - Construction Delay: 4% chance of a factor up to 1.25
     *  - Material Shortage: 6% chance of a factor up to 1.35
     *  - Labor Strike: 3% chance of a factor up to 1.20
     *  - Permit Delay: 2% chance of a factor up to 1.15
     *
     * @return Cost factor for the total cost, based on the occurrence of unexpected events. If no event occurs, returns 1.0.
     */
    private double unexpectedCostFactor() {
        double rand = Math.random();
        double costFactor = 1.0;

        if (rand < 0.05) {
            costFactor = 1.30;
        } else if (rand < 0.09) {
            costFactor = 1.25;
        } else if (rand < 0.15) {
            costFactor = 1.35;
        } else if (rand < 0.18) {
            costFactor = 1.20;
        } else if (rand < 0.20) {
            costFactor = 1.15;
        }

        return costFactor;
    }

    /**
     * Renovates the building according to the renovation plan generated in the constructor
     * of each Building. Removes renovation from the schedule and adds costs and waste to the total.
     * Increments resident satisfaction to near the initial level.
     *
     * Preconditions:
     * - The building must be standing.
     * - The renovation schedule must not be empty.
     *
     * Postconditions:
     * - The first renovation in the schedule is removed.
     * - The total cost is increased by the renovation cost and any unexpected costs.
     * - The total waste is increased by half the material quantity.
     * - The resident satisfaction is incremented.
     */
    public void renovate(int damageReceived, int year) {
        if (!renovationSchedule.isEmpty() && renovationSchedule.get(0) - year <= 5) {
            renovate();
            return;
        }

        totalCost += ((double) damageReceived / 100) * renovationCost;
        totalCost +=  Math.random() * ground.costFactor() * unexpectedCostFactor() * ((double) damageReceived / 100) * renovationCost;
        totalWaste += ((double) damageReceived / 100)* materialQuantity/2;
        residentSatisfaction += 1 + (Math.random() * (initResidentSatisfaction - residentSatisfaction - 1.5));
        renovationSchedule = generateRenovationSchedule(year);
    }

    /**
     * Generates a renovation schedule for the building based on the average renovation interval.
     *
     * @param startYear The year to start generating the schedule from
     *
     * STYLE: Functional Programming
     * It exemplifies functional programming by using Streams to generate a renovation schedule,
     * focusing on the computation result rather than control flow, aligning with declarative design.
     * This functional part of the code generates a renovation schedule using Java Streams.
     * This code is referentially transparent, as the renovation schedule is created without altering object state until returned.
     * Integration with the main class happens through an immutable list, which maintains referential transparency.
     *
     * Preconditions: The start year must be a positive integer.
     * Postconditions: The renovation schedule is generated based on the average renovation interval and returned as an immutable list.
     */
    private List<Integer> generateRenovationSchedule(int startYear) {
        return IntStream.iterate(startYear + MathUtils.gaussian(averageRenovationInterval, 1),
                        year -> year < lifeSpan,
                        year -> year + MathUtils.gaussian(averageRenovationInterval, 1))
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * Calculates a damageDegree for a Building, depending on the mean of the damage, which also calculated from the degree of the Catastrophe and the quality of the Material used in Building.
     * If the damageDegree exceeds 85%, the Building is demolished. Else, it's renovated (by renovate(damageDegree, year)).
     *
     * @param year The current year
     * @param mean The mean of the damage
     * Preconditions: The building must be standing.
     *             The mean must be a positive integer.
     *             The year must be a positive integer.
     *
     *  Postconditions: If the damage degree is greater than or equal to 85%, the building is demolished.
     *             Otherwise, the building is renovated based on the damage degree and year.
     */
    public void damage(int mean, int year) {
        int damageDegree = MathUtils.gaussian(mean, 10);

        if(damageDegree >= 85) {
            demolition();
        } else{
            renovate(damageDegree, year);
        }
    }

    /**
     * Demolishes the building with a 5% chance to transform it into a historical building.
     *
     * STYLE: Functional Programming
     * This method demonstrates functional programming by using Optional to handle probabilistic behavior concisely,
     * avoiding explicit conditions and focusing on a clean, expressive outcome.
     */
    public void demolishWithChanceToTransformToHistoricalBuilding() {
        Optional<Integer> updatedLifeSpan = Optional.of(lifeSpan)
                .filter(lifespan -> material.getQuality() > 9)
                .flatMap(lifespan -> applyLifetimeExtension());

        // Update lifespan or demolish
        updatedLifeSpan.ifPresentOrElse(
                newLifespan -> {
                    this.lifeSpan = newLifespan;
                },
                this::demolition
        );
    }

    /**
     * Applies a 10% chance to double the building's lifetime.
     * Returns an Optional containing the new lifespan if successful, or an empty Optional otherwise.
     *
     * STYLE: Functional Programming
     * is functional because it encapsulates lifespan doubling logic as a pure, side-effect-free function,
     * using Optional to manage the probabilistic result transparently.
     * Uses a functional style to encapsulate a probabilistic lifespan extension in a self-contained,
     * referentially transparent function.
     *
     * Preconditions: The lifespan must be a positive integer.
     * Postconditions: If the probability check succeeds, the lifespan is doubled and returned in an Optional.
     */
    private Optional<Integer> applyLifetimeExtension() {
        return Optional.of(new Random().nextDouble())
                .filter(probability -> probability < 0.05)
                .map(probability -> lifeSpan * 2);
    }

    /**
     * Demolishes the building at the end of its lifespan or after a natural catastrophe. Therefore, it's no longer
     * standing and increases the building's total waste.
     */
    public void demolition() {
        isStanding = false;
        totalWaste += materialQuantity;
    }

    /**
     * Adds CO2 emissions and costs to the total. Decreases resident satisfaction by 5 percent.
     * Preconditions: The building must be standing.
     * Invariant: The resident satisfaction is between 0 and 10.
     * Postconditions: The total CO2 emissions and costs are increased by the yearly values.
     */
    public void yearlyMaintenance() {
        totalCO2Emission += CO2EmissionsPerYear;
        totalCost += costsPerYear;

        residentSatisfaction *= 0.95;
    }

    /**
     * Creates and initializes the residents based on the building type.
     *
     * For different building types, it generates a varying number of residents
     * and assigns their life stages accordingly.
     *
     * - **OneFamilyHouse**: 2 to 6 residents.
     *   - The first 2 residents are always MiddleAgedAdults.
     *   - Any additional residents are children.
     *   - If both initial parents are foreign, the children are forced to have foreign roots.
     *
     * - **Duplex**: 4 to 12 residents.
     *   - The first 4 residents are always MiddleAgedAdults.
     *   - Residents 0-1 and 2-3 are treated as two separate family units.
     *   - If both initial parents in a unit are foreign, their children are forced to have foreign roots.
     *
     * - **Triplex**: 6 to 18 residents.
     *   - The first 6 residents are always MiddleAgedAdults.
     *   - Residents 0-1, 2-3, and 4-5 are treated as three separate family units.
     *   - If both initial parents in a unit are foreign, their children are forced to have foreign roots.
     *
     * - **CommunalApartment**: 10 to 20 residents.
     *   - All residents are set to be adults with various life stages.
     *
     * - **NursingHome**: 10 to 30 residents.
     *   - All residents are set to the Elderly life stage.
     *
     * - **TemporaryShelter**: 10 to 30 residents.
     *   - All residents are set with non-native status.
     *
     * - **DefaultHouse**: 1 resident.
     *   - The single resident is set to be an adult with any life stage.
     *
     *   BAD: The createResidents method uses a large switch statement to handle different building types, leading to code duplication and reduced maintainability.
     *   BAD: This approach lacks dynamic binding, making it difficult to add new building types without modifying this method.
     *   BAD: A better solution would be to use polymorphism, where each building type has its own implementation of the createResidents method.
     */
    private void createResidents() {

        int noOfResidents;

        switch (buildingType) {
            case OneFamilyHouse:

                noOfResidents = 2 + (int) (Math.random() * 5);
                residents = new Resident[noOfResidents];

                for (int i = 0; i < 2; i++) {
                    residents[i] = new Resident((new Random()).nextBoolean(), Resident.LifeStage.MiddleAgedAdult);
                }

                boolean onlyForeignParents = !residents[0].isNative() && !residents[1].isNative();

                for (int i = 2; i < noOfResidents; i++) {
                    residents[i] = new Resident();
                    residents[i].setAnyChild();
                    if(onlyForeignParents) residents[i].forceForeignRoots();
                }
                break;

            case Duplex:

                noOfResidents = 4 + (int) (Math.random() * 9);
                residents = new Resident[noOfResidents];

                for (int i = 0; i < 4; i++) {
                    residents[i] = new Resident((new Random()).nextBoolean(), Resident.LifeStage.MiddleAgedAdult);
                }

                boolean onlyForeignParents1 = !residents[0].isNative() && !residents[1].isNative();
                boolean onlyForeignParents2 = !residents[2].isNative() && !residents[3].isNative();

                for (int i = 4; i < noOfResidents; i++) {
                    residents[i] = new Resident();
                    residents[i].setAnyChild();
                    if(onlyForeignParents1 && i <= (noOfResidents + 4)/2) residents[i].forceForeignRoots();
                    if(onlyForeignParents2 && i > (noOfResidents + 4)/2) residents[i].forceForeignRoots();
                }
                break;

            case Triplex:

                noOfResidents = 6 + (int) (Math.random() * 13);
                residents = new Resident[noOfResidents];

                for (int i = 0; i < 6; i++) {
                    residents[i] = new Resident((new Random()).nextBoolean(), Resident.LifeStage.MiddleAgedAdult);
                }

                boolean onlyForeignParentsA = !residents[0].isNative() && !residents[1].isNative();
                boolean onlyForeignParentsB = !residents[2].isNative() && !residents[3].isNative();
                boolean onlyForeignParentsC = !residents[4].isNative() && !residents[5].isNative();

                for (int i = 6; i < noOfResidents; i++) {
                    residents[i] = new Resident();
                    residents[i].setAnyChild();
                    if(onlyForeignParentsA && i <= (noOfResidents + 12)/3) residents[i].forceForeignRoots();
                    if(onlyForeignParentsB && (noOfResidents + 12)/3 < i && i <= 2 * (noOfResidents + 3)/3)
                        residents[i].forceForeignRoots();
                    if(onlyForeignParentsC && i > 2 * (noOfResidents + 3)/3) residents[i].forceForeignRoots();
                }
                break;

            case CommunalApartment:

                noOfResidents = 10 + (int) (Math.random() * 11);
                residents = new Resident[noOfResidents];

                for (int i = 0; i < noOfResidents; i++) {
                    residents[i] = new Resident();
                    residents[i].setAnyAdult();
                }
                break;

            case NursingHome:

                noOfResidents = (10 + (int) (Math.random() * 21));
                residents = new Resident[noOfResidents];

                for (int i = 0; i < noOfResidents; i++) {
                    residents[i] = new Resident(Resident.LifeStage.Elderly);
                }
                break;

            case TemporaryShelter:

                noOfResidents = (10 + (int) (Math.random() * 21));
                residents = new Resident[noOfResidents];

                for (int i = 0; i < noOfResidents; i++) {
                    residents[i] = new Resident(false);
                }
                break;

            default:

                residents = new Resident[1];
                residents[0] = new Resident();
                residents[0].setAnyAdult();
                break;
        }
    }

    /**
     * Sets the surrounding ground of this building.
     *
     * @param ground The ground type surrounding the building.
     * Preconditions: The ground must not be null.
     */
    public void setGround(Ground ground) {
        this.ground = ground;
    }

    /**
     * Reduces the resident satisfaction by a specified amount.
     *
     * Ensures satisfaction doesn't drop below 0.
     * Example: Conflicts like misbehaving teens reduce satisfaction.
     *
     * @param amount The amount to reduce satisfaction by.
     *  Preconditions: The amount must be a positive double.
     *               Invariant: The resident satisfaction must be between 0 and 100.
     *  Postconditions: The resident satisfaction is reduced by the specified amount.
     */
    public void reduceSatisfaction(double amount) {
        residentSatisfaction -= amount;
        if (residentSatisfaction < 0) {
            residentSatisfaction = 0; // making sure it's not below 0
        }
    }

    /**
     * Returns true if the building is standing at the moment, false otherwise.
     *
     * @return true if the building is standing, false otherwise
     */
    public boolean isStanding() {
        return isStanding;
    }

    /**
     * Returns true if a renovation is scheduled for the year, false otherwise.
     *
     * @param year The year to check for renovations.
     * @return true if a renovation happens in the year, false otherwise
     */
    public boolean isRenovationYear(int year) {
        return renovationSchedule.contains(year);
    }

    /**
     * Returns true if the building is demolished the year, false otherwise.
     *
     * @param year The year to check for demolition.
     * @return true if the building is demolished in the year, false otherwise
     */
    public boolean isDemolationYear(int year) {
        return year == lifeSpan;
    }

    /**
     * Returns the total cost of this until now.
     *
     * @return total cost of the building over time
     */
    public double getTotalCost() {
        return totalCost;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    /**
     * Returns the total CO2 emissions of this until now.
     *
     * @return amount of CO2 emissions over time (in tonnes)
     */
    public double getTotalCO2Emissions() {
        return totalCO2Emission;
    }

    /**
     * Returns the total amount of waste of the building until now.
     *
     * @return total amount of waste over time (in tonnes)
     */
    public double getTotalWaste() {
        return totalWaste;
    }

    /**
     * Returns the current resident satisfaction.
     *
     * @return resident satisfaction at the moment
     */
    public double getResidentSatisfaction() {
        return residentSatisfaction;
    }

    /**
     * Returns the lifespan of the building. I.e. how many years the building will stand at most.
     *
     * @return lifespan of the building
     */
    public int getLifeSpan() {
        return lifeSpan;
    }

    /**
     * Returns a random building type for this building.
     *
     * @return random building type
     */
    public BuildingType getRandomBuildingType() {
        return BuildingType.values()[(int) (new Random().nextDouble() * BuildingType.values().length)];
    }

    /**
     * Returns the amount of residents that live in this building.
     *
     * @return the amount of residents that live in this building
     */
    public int getNoOfResidents() {
        return residents.length;
    }

    /**
     * Returns his building's type.
     *
     * @return this building's type
     */
    // public BuildingType getBuildingType() { return buildingType; }

    /**
     * Returns the residents of this building.
     *
     * @return residents
     */
    public Resident[] getResidents() {
        return residents;
    }

    /**
     * Returns the surrounding ground of this building.
     *
     * @return the surrounding ground of this building
     */
    public Ground getGround() {
        return ground;
    }

    /**
     * Returns the material of this building.
     *
     * @return the material of this building
     */
    public Material getMaterial() {
        return material;
    }
}
