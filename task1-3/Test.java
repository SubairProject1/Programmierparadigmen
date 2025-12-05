import java.util.ArrayList;

/**
 * AB1:
 * Together, we read and understood the assignment and decided which classes and interfaces to use for modelling the given problem
 * Working mostly together, we all contributed to all the classes, with Andrei focusing more on the Building.java Inerface
 * and its implementations (classes), Subair with how the different events in Building are handled in Simulation and how the results are printed and Catalin
 * on how the key facts are calculated in Simulation.
 *
 * AB2:
 * Catalin: Scenarios parametrisieren, damit  neue Scenarios einfach hinzugefügt werden können (schon gemacht)
 * Catalin: Datenbank mit verschiedenen Materiallien, alle in mehreren Kategorien, minimal, high quality, ecological + weitere Szenarien
 * Catalin: Parsing Klasse für den JSON Datenbank
 * Catalin: Revitalisierung (neue, modernere Materiallien, geringere CO2 Emissionen)
 *
 * Andrei: Katastrophen kommen mit unterschiedlichen Wahrscheinlichkeiten und unterschiedlichen Schwierigkeitsgrade vor. Die Wahrscheinlichkeit, dass eine Katastrophe überhaupt kommt ist vom Ground Type abhängig. Der verursachte Schaden variiert auch je nach dem Schwierigkeitsgrad und Resistence der benutzen Materialien.
 * Andrei: Unerwartete Kosten können beim Rohbau, Renovierung und Revitalisierung auftreten. Die Wahrscheinlichkeit dafür beziehungsweise der maximale Anteil an zusätzliche Kosten ist abhängig vom Ground Type.
 * Andrei: Bei dem Rohbau können unerwartete Ereignisse die Kosten erhöhen.
 * Andrei: Anzahl der Szenarien erhöhen.
 *
 * Subair: Bodenversiegelung wird berücksichtigt, kann einen Einfluss auf Kosten, CO2-Ausstoß und frühzeitigen Abbruch bei unvorhersehbaren Ereignissen.
 * Subair: Bauweise (Einfamilienhaus, verdichtet, temporäres Zuhause für Flüchtlinge/Einwanderer, Einzelperson)
 * Subair: unterschiedliche BewohnerInnen erhöhen Risiko für Konflikt, welches einen Einfluss auf die Zufriedenheit der EinwohnerInnen haben könnte und im schlimmsten Fall zum Umzug führen könnte. Es kann auch zu Schäden kommen (Spraying, Müll nicht sorgfältig entsorgt etc.)
 * Subair: Bodenform (flach, uneben, hügelig?, moorig?, schroff?), Bodentyp (Sand, Schluff, Lehm, Ton) können wiederum Einflüsse auf unterschiedliche Faktoren haben (z.B. Bodenversiegelung)
 *
 * AB3:
 * Catalin: Implemented testing functionality in Test.java
 * Catalin: Implemented, using functional programming, the possibility of extending a buildings lifespan instead of demolishing it, turning it into a historical building.
 * Catalin: Added GOOD and BAD comments for the object oriented parts
 * Catalin: Added explanatory comments on why certain programming styles were used
 * Catalin: Added comments regarding design by contract
 *
 * Subair: Analysed MathUtils, PrintSimulation, Material, MaterialJsonParser, Ground (STYLE, Design by Contract)
 * Subair: Forced preconditions into some methods.
 * Subair: Added GOOD and BAD comments for the procedural parts
 * Subair: Implemented a method that simulates an ongoing change to ground once it's created with the help of concurrent programming
 *
 * Andrei: probability for catastrophe and the costs are dependent on the ground zusammensetzung
 * Andrei: Helped the other with writing Style related comments
 */

public class Test {

    private static final int numberOfSimulations = 10;
    private static final int buildingsPerSimulation = 5;
    private static ArrayList<Ground> grounds = new ArrayList<>(10);

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
                simulation.simulate();
                if(simulation.isSuccessful()) {
                    System.out.println("Simulation successful");
                } else {
                    System.out.println("Simulation failed");
                }
            }
        }
    }
}