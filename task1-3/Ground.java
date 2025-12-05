/**
 * AUTHOR: Subair, Andrei
 * STYLE: Concurrent Programming (a bit Object-Oriented Programming)
 *
 * Represents the ground with various types and shapes.
 * This class models the composition of the ground in terms of different materials
 * and shapes, with each ground type having a specific percentage of each material
 * and shape. The percentages for materials and shapes add up to 100%.
 *
 * GOOD: This class has high cohesion because it encapsulates all properties and behaviors related to the ground in one place.
 * It models the ground's composition and its types, ensuring that all related data and methods are grouped together.
 * A less cohesive variant would split the materials and shapes into separate classes, making it harder to manage and understand the ground's overall composition.
 */
public class Ground implements Runnable {

    private GroundType type;

    // Relative amounts of different ground materials must add up to 100%
    private double sandPercentage;
    private double siltPercentage;
    private double clayPercentage;
    private double loamPercentage;

    // Relative amounts of different ground shapes must add up to 100%
    private double flatPercentage;
    private double unevenPercentage;
    private double hillyPercentage;
    private double marshyPercentage;
    private double rockyPercentage;

    Boolean running;

    /**
     * Enum representing various types of ground, each with specific percentages
     * of different materials and shapes.
     */
    public enum GroundType {
        Beach(0.08, 65, 35, 0, 0, 20, 80, 0, 0, 0),
        Forest(0.07, 10, 20, 30, 40, 50, 10, 30, 10, 0),
        Plains(0.04, 50, 25, 15, 10, 70, 10, 10, 5, 5),
        Land(0.05, 30, 40, 20, 10, 60, 20, 10, 5, 5),
        City(0.06, 5, 15, 20, 60, 90, 5, 2, 1, 2),
        Mountain(0.09, 10, 10, 40, 40, 20, 20, 50, 5, 5);

        private final double catastropheProbability;
        private final double sand;
        private final double silt;
        private final double clay;
        private final double loam;
        private final double flat;
        private final double uneven;
        private final double hilly;
        private final double marshy;
        private final double rocky;

        /**
         * Constructor for GroundType enum.
         *
         * @param catastropheProbability Probability of catastrophe
         * @param sand Percentage of sand
         * @param silt Percentage of silt
         * @param clay Percentage of clay
         * @param loam Percentage of loam
         * @param flat Percentage of flat ground
         * @param uneven Percentage of uneven ground
         * @param hilly Percentage of hilly ground
         * @param marshy Percentage of marshy ground
         * @param rocky Percentage of rocky ground
         */
        GroundType(double catastropheProbability, double sand, double silt, double clay, double loam, double flat, double uneven, double hilly, double marshy, double rocky) {
            this.catastropheProbability = catastropheProbability;
            this.sand = sand;
            this.silt = silt;
            this.clay = clay;
            this.loam = loam;
            this.flat = flat;
            this.uneven = uneven;
            this.hilly = hilly;
            this.marshy = marshy;
            this.rocky = rocky;
        }

        public double getCatastropheProbability() { return catastropheProbability; }

        private double getSand() { return sand; }
        private double getSilt() { return silt; }
        private double getClay() { return clay; }
        private double getLoam() { return loam; }
        private double getFlat() { return flat; }
        private double getUneven() { return uneven; }
        private double getHilly() { return hilly; }
        private double getMarshy() { return marshy; }
        private double getRocky() { return rocky; }
    }

    /**
     * Constructs a Ground object based on a predefined GroundType.
     *
     * @param type The type of ground (e.g., Beach, Forest, etc.)
     * Precondition: The type parameter must be a valid GroundType.
     * Postcondition: The Ground object is initialized with the specified type, and its material and shape percentages are set accordingly.
     */
    public Ground(GroundType type) {
        this.type = type;
        this.running = true;
        this.sandPercentage = type.getSand();
        this.siltPercentage = type.getSilt();
        this.clayPercentage = type.getClay();
        this.loamPercentage = type.getLoam();
        this.flatPercentage = type.getFlat();
        this.unevenPercentage = type.getUneven();
        this.hillyPercentage = type.getHilly();
        this.marshyPercentage = type.getMarshy();
        this.rockyPercentage = type.getRocky();
        new Thread(this).start();
    }

    /**
     * Gets the GroundType of this Ground object.
     *
     * @return the type of the ground.
     */
    public GroundType getType() { return type; }

    // Getters for each ground type and shape percentage
    public double getSandPercentage() { return sandPercentage; }
    public double getSiltPercentage() { return siltPercentage; }
    public double getClayPercentage() { return clayPercentage; }
    public double getLoamPercentage() { return loamPercentage; }
    public double getFlatPercentage() { return flatPercentage; }
    public double getUnevenPercentage() { return unevenPercentage; }
    public double getHillyPercentage() { return hillyPercentage; }
    public double getMarshyPercentage() { return marshyPercentage; }
    public double getRockyPercentage() { return rockyPercentage; }

    /**
     * Calculates the weighted probability of a catastrophe based on the ground composition.
     * The probability is influenced by the type of ground and the percentages of different materials and shapes.
     *
     * Precondition: The ground composition percentages must be normalized to add up to 100%.
     * Postcondition: Returns the weighted probability of a catastrophe based on the ground composition.
     *
     * @return the weighted probability of a catastrophe based on the ground composition.
     */
    public double getWeightedCatastropheProbability(){

        double weightedProbability = this.getType().getCatastropheProbability() * (
                sandPercentage * 0.005 +
                siltPercentage * 0.004 +
                clayPercentage * 0.006 +
                loamPercentage * 0.003 +
                flatPercentage * 0.005 +
                unevenPercentage * 0.01 +
                hillyPercentage * 0.012 +
                marshyPercentage * 0.015 +
                rockyPercentage * 0.02);

        return weightedProbability;

    }

    @Override
    public void run() {
        while(running) {
            updateGround();
            try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt();  }
        }
    }

    /**
     * A ground can change over the years due to unexpected events such as the weather.
     * This method updates the ground based on various events with realistic probabilities.
     *
     * Precondition: The method is called regularly to simulate environmental changes.
     * Postcondition: The ground composition is updated.
     */
    private void updateGround() {
        double eventChance = Math.random();

        if (eventChance < 0.1) {
            rainEvent();
        } else if (eventChance < 0.15) {
            windEvent();
        } else if (eventChance < 0.17) {
            riseWaterLevelEvent();
        } else if (eventChance < 0.18) {
            erosionEvent();
        }

        normalizePercentages();
    }

    /**
     * Simulates the effect of rain on the ground, affecting sand, silt, clay, and loam percentages.
     */
    private synchronized void rainEvent() {
        sandPercentage -= 0.1;
        siltPercentage += 0.05;
        clayPercentage += 0.04;
        loamPercentage -= 0.01;
    }

    /**
     * Simulates the effect of wind on the ground, affecting sand and silt percentages.
     */
    private synchronized void windEvent() {
        sandPercentage += 0.08;
        siltPercentage -= 0.08;
    }

    /**
     * Simulates the effect of rising water levels on the ground, affecting marshy and flat percentages.
     */
    private synchronized void riseWaterLevelEvent() {
        marshyPercentage += 0.05;
        flatPercentage -= 0.05;
    }

    /**
     * Simulates the effect of erosion on the ground, affecting uneven and hilly percentages.
     */
    private synchronized void erosionEvent() {
        unevenPercentage += 0.05;
        hillyPercentage -= 0.05;
    }

    /**
     * Normalizes the percentages of ground materials and shapes to ensure they add up to 100%.
     *
     * This method adjusts the percentages of sand, silt, clay, and loam to collectively sum to 100%.
     * Similarly, it adjusts the percentages of flat, uneven, hilly, marshy, and rocky ground shapes
     * to collectively sum to 100%.
     *
     * STYLE: Concurrent Programming
     * This method recalculates and normalizes percentages to ensure they sum to 100%. It uses synchronized to prevent
     * inconsistent state if multiple threads attempt normalization simultaneously.
     *
     * Precondition: The ground material and shape percentages may not sum to 100% due to previous updates.
     * Postcondition: The ground material percentages and shape percentages will each sum to 100%.
     */
    private synchronized void normalizePercentages() {
        double totalMaterials = sandPercentage + siltPercentage + clayPercentage + loamPercentage;
        double totalShapes = flatPercentage + unevenPercentage + hillyPercentage + marshyPercentage + rockyPercentage;

        sandPercentage = (sandPercentage / totalMaterials) * 100;
        siltPercentage = (siltPercentage / totalMaterials) * 100;
        clayPercentage = (clayPercentage / totalMaterials) * 100;
        loamPercentage = (loamPercentage / totalMaterials) * 100;

        flatPercentage = (flatPercentage / totalShapes) * 100;
        unevenPercentage = (unevenPercentage / totalShapes) * 100;
        hillyPercentage = (hillyPercentage / totalShapes) * 100;
        marshyPercentage = (marshyPercentage / totalShapes) * 100;
        rockyPercentage = (rockyPercentage / totalShapes) * 100;
    }

    /**
     * Calculates the cost factor based on the composition of the ground.
     * Rocky ground increases the cost factor more than other types.
     *
     * STYLE: Concurrent Programming
     * This method calculates a cost factor based on the ground composition and shape, using synchronized to ensure that changes
     * in ground composition percentages don’t interfere with the calculation, providing thread-safe access to shared data.
     *
     * Precondition: The percentages of ground materials and shapes must be normalized to add up to 100%.
     * Postcondition: Returns a cost factor based on the difficulty of building on different types of ground.
     *
     * @return the cost factor for construction costs
     */
    public double costFactor() {
        synchronized (this) {

            double factor = 1.0;

            factor += rockyPercentage * 0.02;
            factor += unevenPercentage * 0.01;
            factor += hillyPercentage * 0.012;
            factor += marshyPercentage * 0.015;

            factor += sandPercentage * 0.005;
            factor += siltPercentage * 0.004;
            factor += clayPercentage * 0.006;
            factor += loamPercentage * 0.003;


            return factor;
        }
    }

    /**
     * Stops the ground updates by setting the running flag to false.
     *
     * Precondition: The ground updates are currently running.
     * Postcondition: The ground updates are stopped.
     */
    public void stop() {
        synchronized (this) {
            this.running = false;
        }
    }
}