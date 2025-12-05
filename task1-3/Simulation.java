import java.util.*;
import static java.lang.Double.isNaN;

/**
 * AUTHOR: Catalin, Andrei, Subair
 * STYLE: Object-Oriented Programming
 *
 * This class represent a year-by-year Simulation for the lifecycle of a group of buildings from a certain scenario.
 * It simulates occurrences of natural disasters, renovations and the passing of time and their effects on cost, waste,
 * emissions and resident satisfaction levels. The class calculates for every simulation the key facts and the sustainability
 * function, i.e. the evaluation of the simulation.
 */
public class Simulation {

    private static final int numberOfSimulations = 10;
    private static final int buildingsPerSimulation = 5;

    private int currentYear;
    private double sumOfAverageSatisfactions;
    private List<Building> buildings;
    private List<Double> costPerDecade;
    private List<Double> sumOfAverageSatisfactionsPerDecade;
    private static ArrayList<Ground> grounds = new ArrayList<>();
    private static ArrayList<String> resultList = new ArrayList<>();
    private Ground updatedGround;

    private boolean isSuccessful = true;

    public static final Scenario modularScenario = new Scenario("Modular Scenario",
            75, 15, 20000, 50000,
            140, 7, 750000, 3,
            4);


    public static final Scenario minimalScenario = new Scenario( "Minimal Scenario",
            50, 20,
            15000, 100000, 150,
            6, 300000, 0,0);
    public static final Scenario ecologicalScenario = new Scenario( "Ecological Scenario",
            50, 20,
            15000, 200000, 150,
            7, 750000, 7,0);

    public static final Scenario highQualityScenario = new Scenario("High Quality Scenario",
            100, 25, 15000, 400000, 200,
            9, 1500000, 0, 8);

    public static final Scenario highTechScenario = new Scenario("High Tech Scenario",
            50, 20,
            15000, 200000, 150,
            8, 1200000, 5,5);


    public static final Scenario CatastropheSafeScenario = new Scenario("Catastrophe Safe Scenario",
            100, 25, 10000, 400000, 200,
            8, 2000000, 0, 9);


    /**
     * Constructs a new Simulation instance with the given buildings and ground.
     *
     * STYLE: Object-Oriented Programming
     *
     * @param buildings Collection of buildings to include in the simulation.
     * @param ground The ground type for the buildings.
     *
     * Precondition: buildings collection and ground must not be null.
     * Postcondition: Simulation object is initialized with given buildings and ground.
     */
    public Simulation(Collection<Building> buildings, Ground ground) {

        currentYear = 0;
        this.buildings = new ArrayList<>(buildings);
        costPerDecade = new ArrayList<>();
        sumOfAverageSatisfactions = 0;
        sumOfAverageSatisfactionsPerDecade = new ArrayList<>();
        for (Building building : this.buildings) building.setGround(ground);
        this.updatedGround = ground;
    }

    /**
     * Runs the Simulation and returns the simulation results.
     *
     * STYLE: Procedural Programming
     * This method is procedural because it follows a sequence of actions, directly manipulating data structures and
     * controlling logic with loops and conditions, rather than encapsulating behavior within objects.
     * It emphasizes operational flow over object interactions.
     *
     * @return the simulation result after running the simulation.
     *
     * Postcondition: Simulation results are calculated and returned as a String.
     */
    public String simulate() {

        isSuccessful = true;

        for(Building building: buildings) {
            building.roughConstruction();
        }

        while(isAnyBuildingStanding()) {

            Boolean[] conflicts = occuredConflicts();
            for (int i = 0; i < conflicts.length; i++) {
                if (conflicts[i]) {
                    for (Building building : buildings) {
                        building.reduceSatisfaction(0.25);
                    }
                }
            }

            /**
             * Check if Catastrophe has Occurred
             *
             * BAD: The method `hasCatastropheOccurred` directly accesses the `GroundType` of the first building's ground.
             * This creates tight coupling between the `Simulation` and `Ground` classes.
             * A better solution would be to delegate the catastrophe probability check to the `Ground` class itself.
             */
            if(hasCatastropheOccurred()) {
                double rand = Math.random();
                int degree;

                if (rand < 0.8) degree = 1; // 80% probability
                else if (rand < 0.97) degree = 2; // 17% probability
                else degree = 3; // 3% probability

                // Calculate damage based on degree and Material used for each Building.
                // Middle Quality between material with worst and with best quality = 6.25 = (3 + 9.5) / 2;

                double midq = (3 + 9.5) / 2;
                switch (degree){
                    case 1: for(Building building: buildings) {
                            building.damage((int) (16 * (midq/building.getMaterial().getQuality())), currentYear);
                            }
                            break;

                    case 2: for(Building building: buildings) {
                            building.damage((int) (49 * (midq/building.getMaterial().getQuality())), currentYear);
                            }
                            break;

                    case 3: for(Building building: buildings) {
                            building.damage((int) (82 * (midq/building.getMaterial().getQuality())), currentYear);
                            }
                            break;
                }

            }

            double averageSatisfactionThisYear = 0;
            for(Building building: buildings) {

                if(!building.isStanding()) {
                    continue;
                }

                if(building.isDemolationYear(currentYear)) {
                    building.demolishWithChanceToTransformToHistoricalBuilding();
                    continue;
                }

                building.yearlyMaintenance();

                if(building.isRenovationYear(currentYear)) {
                    int random = (int) (Math.random() * 100);
                    if(random > 20)
                        building.renovate();
                    else
                        building.revitalize();
                }

                averageSatisfactionThisYear += building.getResidentSatisfaction();
            }

            sumOfAverageSatisfactions += computeAverageSatisfactionYear();


            if(currentYear % 10 == 9) {

                // cost related
                if(currentYear == 9) {
                    costPerDecade.add(getTotalCost());
                } else {
                    double costUntilLastDecade = 0;
                    for(int decade = 0; decade < currentYear/10; decade++) {
                        costUntilLastDecade += costPerDecade.get(decade);
                    }
                    costPerDecade.add(getTotalCost() - costUntilLastDecade);
                }

                // satisfaction related
                if(currentYear == 9) {
                    sumOfAverageSatisfactionsPerDecade.add(sumOfAverageSatisfactions);
                } else {
                    double sumOfAverageSatisfactionsUntilLastDecade = 0;
                    for(int decade = 0; decade < currentYear/10; decade++) {
                        sumOfAverageSatisfactionsUntilLastDecade += sumOfAverageSatisfactionsPerDecade.get(decade);
                    }
                    if(!isNaN(sumOfAverageSatisfactions - sumOfAverageSatisfactionsUntilLastDecade)) {
                        sumOfAverageSatisfactionsPerDecade.
                                add(sumOfAverageSatisfactions - sumOfAverageSatisfactionsUntilLastDecade);
                    }

                }
            }
            currentYear++;
        }

        // ground does no longer change after simulation ends
        this.updatedGround.stop();

        for(double cost: costPerDecade) {
            if(Double.isNaN(cost) || cost < 0) {
                isSuccessful = false;
            }
        }

        for(double satisfaction: sumOfAverageSatisfactionsPerDecade) {
            if(Double.isNaN(satisfaction) || satisfaction < 0) {
                isSuccessful = false;
            }
        }

        return simulationResult();
    }

    /**
     * Returns true if the simulation was successful, false otherwise.
     *
     * @return true if the simulation was successful, false otherwise
     */
    public boolean isSuccessful() {
        return isSuccessful;
    }

    /**
     * Returns the sum of total costs of the buildings.
     *
     * @return sum of total costs of the buildings
     */
    private double getTotalCost() {
        double totalCost = 0;
        for(Building building: buildings) {
            totalCost += building.getTotalCost();
        }
        return totalCost;
    }

    /**
     * Returns rue if there are still buildings standing, false otherwise.
     *
     * @return true if there are still buildings standing, false otherwise
     */
    private boolean isAnyBuildingStanding() {
        for(Building building: buildings) {
            if(building.isStanding())
                return true;
        }
        return false;
    }

    String getScenarioName() {
        return buildings.get(0).getScenarioName();
    }

    /**
     * Returns the average financial expenditure per resident per year.
     *
     * @return average financial expenditure per resident per year (KEY FACT 1)
     */
    private double keyFact1 () {
        double totalCost = 0;
        int totalLifeSpan = 0;
        int noOfResidents = 0;
        for(Building building: buildings) {
            totalCost += building.getTotalCost();
            totalLifeSpan += building.getLifeSpan();
            noOfResidents += building.getNoOfResidents();
        }

        return totalCost / (totalLifeSpan * noOfResidents);
    }

    /**
     * Returns the distribution of the average financial expenditure per resident and year over decades.
     *
     * @return distribution of the average financial expenditure per resident and year over decades (KEY FACT 2)
     */
    private List<Double> keyFact2 () {
        List<Double> keyFact2 = new ArrayList<>();

        int noOfResidents = 0;
        for(Building building: buildings) {
            noOfResidents += building.getNoOfResidents();
        }

        for(Double cost : costPerDecade) {
            keyFact2.add(cost / (10 * noOfResidents));
        }
        return keyFact2;
    }

    /**
     * Returns the average per resident per year through the building CO2 release caused by the production process.
     *
     * @return average per resident per year through the building CO2 release caused by the production process (KEY FACT 3)
     */
    private double keyFact3() {
        double totalCO2 = 0;
        int totalLifeSpan = 0;
        int noOfResidents = 0;
        for(Building building: buildings) {
            totalCO2 += building.getTotalCO2Emissions();
            totalLifeSpan += building.getLifeSpan();
            noOfResidents += building.getNoOfResidents();
        }
        return totalCO2 / (totalLifeSpan * noOfResidents);
    }

    /**
     * Returns the average per resident per year through the building amount of non-recycled waste in tonnes.
     *
     * @return average per resident per year through the building amount of non-recycled waste in tonnes (KEY FACT 4)
     */
    private double keyFact4() {
        double totalWaste = 0;
        int totalLifeSpan = 0;
        int noOfResidents = 0;
        for(Building building: buildings) {
            totalWaste += building.getTotalWaste();
            totalLifeSpan += building.getLifeSpan();
            noOfResidents += building.getNoOfResidents();
        }
        return totalWaste / (totalLifeSpan * noOfResidents);
    }

    /**
     * Returns an index for the average satisfaction with housing quality per decade.
     *
     * @return an index for the average satisfaction with housing quality per decade (KEY FACT 5).
     */
    private List<Double> keyFact5() {
        List<Double> keyFact5 = new ArrayList<>();
        for(Double satisfactionPerDecade: sumOfAverageSatisfactionsPerDecade) {
            keyFact5.add(satisfactionPerDecade/10);
        }
        return keyFact5;
    }

    /**
     * Returns the average satisfaction of all residents in the current year.
     *
     * @return average satisfaction of all residents in the current year.
     */
    private double computeAverageSatisfactionYear() {
        int noOfBuildingsStanding = 0;
        double sumOfSatisfactions = 0;
        for(Building building: buildings) {
            if(building.isStanding()) {
                noOfBuildingsStanding++;
                sumOfSatisfactions += building.getResidentSatisfaction();
            }
        }
        return sumOfSatisfactions / noOfBuildingsStanding;
    }

    /**
     * Determines if a catastrophe has occurred based on the type of ground the first building is on.
     *
     * Ground type thresholds:
     * - City: 6% chance of catastrophe
     * - Plains: 4% chance of catastrophe
     * - Forest: 7% chance of catastrophe
     * - Beach: 8% chance of catastrophe
     * - Mountain: 9% chance of catastrophe
     * - Land or no GroundType assigned: 5% chance of catastrophe
     *
     * @return Boolean indicating whether a catastrophe has occurred (true) or not (false).
     */
    private Boolean hasCatastropheOccurred() {
        Ground g = this.buildings.get(0).getGround();

        double rand = Math.random();
        return rand < g.getWeightedCatastropheProbability();
    }

    /**
     * Evaluates the likelihood of conflicts occurring among residents based on various factors.
     *
     * Causes of conflict can include:
     * - Significant age gaps among residents, leading to generational misunderstandings.
     * - A large number of teenagers, whose developmental phase might lead to activities such as property damage.
     * - Misbehaving or crying children, which can cause frustration among other residents.
     * - Imbalanced ratios of native and non-native residents, potentially leading to cultural clashes.
     *
     * These factors contribute to an overall risk of conflict, represented as a probability.
     *
     * @return an array of Booleans indicating which types of conflicts have occurred this year.
     *
     * STYLE: Procedural Programming
     */
    private Boolean[] occuredConflicts() {

        Boolean[] conflicts = new Boolean[4];
        Arrays.fill(conflicts, false);

        int numInfants = 0;
        int numTeens = 0;
        int numYoungAdults = 0;
        int numMiddleAgedAdults = 0;
        int numElderly = 0;

        int numNatives = 0;

        for (Building building : buildings) {
            Resident[] residents = building.getResidents();
            for (Resident resident : residents) {
                Resident.LifeStage lifeStage = resident.getLifeStage();
                switch (lifeStage) {
                    case Infant -> numInfants++;
                    case Teen -> numTeens++;
                    case YoungAdult -> numYoungAdults++;
                    case MiddleAgedAdult -> numMiddleAgedAdults++;
                    case Elderly -> numElderly++;
                }

                if (resident.isNative()) numNatives++;
            }
        }

        int totalResidents = numInfants + numTeens + numYoungAdults + numMiddleAgedAdults + numElderly;

        // Age gap conflicts
        double childrenRatio = (double) (numTeens + numInfants) / totalResidents;
        if (Math.random() <= Math.abs(childrenRatio - 0.5) / 2 && childrenRatio != 0 && childrenRatio != 1.0) {
            conflicts[0] = true;
        }

        // Teen behavior conflicts
        double teensRatio = (double) numTeens / totalResidents;
        if (Math.random() <= teensRatio / 4) conflicts[1] = true;

        // Infant behavior conflicts
        double infantsRatio = (double) numInfants / totalResidents;
        if (Math.random() <= infantsRatio / 3) conflicts[2] = true;

        // Cultural clashes
        double nativeRatio = (double) numNatives / totalResidents;
        if (Math.random() <= Math.abs(nativeRatio - 0.5) / 2 && nativeRatio != 0 && nativeRatio != 1.0) {
            conflicts[3] = true;
        }

        return conflicts;
    }

    /**
     *
     * @return a sustainability score based off the key facts (the larger, the better)
     *
     * STYLE: Procedural Programming
     */
    private double getSustainability() {

        double keyFact2avg = 0.0d;
        List<Double> keyFact2List = keyFact2();
        for(double value : keyFact2List) {
            keyFact2avg += value;
        }
        keyFact2avg /= keyFact2List.size();

        double keyFact5avg = 0.0d;
        List<Double> keyFact5List = keyFact5();
        for(double value : keyFact5List) {
            keyFact5avg += value;
        }
        keyFact5avg /= keyFact2List.size();

        return 0.25d * 1/keyFact1() + 0.15d * 1/keyFact2avg + 0.30d * 1/keyFact3() + 0.15d * 1/keyFact4() + 0.15d * 1/keyFact5avg;
    }

    public static void main(String[] args) {

        /**
         * City: 35% – Major population hubs where most people live.
         * Plains: 25% – These vast open areas support large settlements and agriculture.
         * Land: 20% – General land areas that are quite suitable for large communities.
         * Forest: 10% – Not as dense but still support smaller communities.
         * Beach: 5% – Attractive but limited by space.
         * Mountain: 5% – Least likely due to harsh terrain.
         */
        for (int i = 0; i < numberOfSimulations; i++) {
            double rand = Math.random();
            if(rand < 0.35d) grounds.add(i, new Ground(Ground.GroundType.City));
            else if(0.35d <= rand && rand < 0.6d) grounds.add(i, new Ground(Ground.GroundType.Plains));
            else if(0.6d <= rand && rand < 0.8d) grounds.add(i, new Ground(Ground.GroundType.Land));
            else if(0.8d <= rand && rand < 0.9d) grounds.add(i, new Ground(Ground.GroundType.Forest));
            else if(0.9d <= rand && rand < 0.95d) grounds.add(i, new Ground(Ground.GroundType.Beach));
            else if(0.95d <= rand) grounds.add(i, new Ground(Ground.GroundType.Mountain));
        }

        Scenario[] scenarios = {Simulation.minimalScenario, Simulation.ecologicalScenario, Simulation.highQualityScenario,
                Simulation.modularScenario, Simulation.CatastropheSafeScenario, Simulation.highTechScenario};

        for(Scenario scenario: scenarios) {
            for(int simulationNr = 0; simulationNr < numberOfSimulations; simulationNr ++) {

                ArrayList<Building> buildings = new ArrayList<>();

                for(int i = 0; i < buildingsPerSimulation; i ++) {
                    buildings.add(new Building(scenario));
                }

                Simulation simulation = new Simulation(buildings, grounds.get(simulationNr));
                resultList.add(simulation.simulate());
            }

            PrintSimulation.printAllSimulationResult(resultList);
            PrintSimulation.printSimulationClosestToAverage(resultList);
            resultList = new ArrayList<>();
        }
    }

    /**
     * Returns a whole String with all the values of sustainability and key factors including a description.
     * In simple terms, it's the result of the simulation.
     *
     * @return the simulation result along with the key facts and sustainability
     */
    private String simulationResult() {
        String result = "SimulationResult for " + getScenarioName() + ":\n" + "sustainability: " + getSustainability();
        result += "\naverage financial expenditure per resident per year: " + keyFact1();
        result += "\ndistribution of the average financial expenditure per resident and year over decades: " + keyFact2();
        result += "\naverage per resident per year through the building CO2 release caused by the production process: " + keyFact3();
        result += "\naverage per resident per year through the building amount of non-recycled waste in tonnes: " + keyFact4();
        result += "\nan index for the average satisfaction with housing quality per decade: " + keyFact5() + "\n";
        return result;
    }
}