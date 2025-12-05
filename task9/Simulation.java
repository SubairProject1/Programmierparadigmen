import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * The Simulation class simulates the movements of people on the path network of a building.
 * The number of people and the name of a path network are passed to the Simulation process via command-line arguments.
 * The specified number of people are then placed randomly on positions in the path network.
 *
 * When all people have reached the meeting point or when a person has reached the maximum number of 64 wait steps
 * (calls to sleep), the simulation prints the count of wait steps, the number of movements, and the position pairs
 * (the fields they are on) for all people to the file test.out, then terminates all threads.
 * It uses Thread.interrupt() to interrupt a thread. It prints the positions of the fields
 * (e.g., the X and Y coordinates in a plane) as well as the counters, then terminates the thread.
 * The displayed path network may not exceed 80 fields in any direction (North/South, East/West).
 * A random person is selected as the lead person, and after the lead person waits (calls sleep),
 * the fields are written line by line to the file test.out.
 * For fields where the left foot is located, the character "L" is printed; for fields where the right foot is located,
 * the character "R" is printed; and for fields that are access points to the meeting point, the character "S" is printed.
 */
public class Simulation {

    private final char[][] grid;
    private char[][] directionsGrid;
    private int[][] distanceToMeetingPoint;
    private final List<Person> people = Collections.synchronizedList(new ArrayList<>());
    private final BlockingQueue<PersonData> pipeline;
    private final List<Thread> personThreads = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Test");
        String gridName = args[0];
        int numPeople = Integer.parseInt(args[1]);

        Simulation simulation = new Simulation(gridName, numPeople);
        simulation.startSimulation();
    }

    /**
     * Generates a grid with directional instructions to find the shortest path
     * to the meeting points in the network of paths.
     */
    public void generateDirectionsGrid() {
        distanceToMeetingPoint = new int[grid.length][grid[0].length];
        directionsGrid = new char[grid.length][grid[0].length];
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};
        Queue<Position> q = new LinkedList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'S') {
                    distanceToMeetingPoint[i][j] = 0;
                    q.add(new Position(i, j));
                }
            }
        }
        while (!q.isEmpty()) {
            Position currentPosition = q.poll();
            for (int i = 0; i < 4; i++) {
                int newX = currentPosition.getX() + dx[i];
                int newY = currentPosition.getY() + dy[i];
                if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid[0].length &&
                        grid[newX][newY] == 'x' &&
                        (distanceToMeetingPoint[newX][newY] == 0 ||
                                distanceToMeetingPoint[newX][newY] >
                                        distanceToMeetingPoint[currentPosition.getX()][currentPosition.getY()] + 1)) {
                    distanceToMeetingPoint[newX][newY] = distanceToMeetingPoint[currentPosition.getX()][currentPosition.getY()] + 1;
                    q.add(new Position(newX, newY));
                }
            }
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 'x') {
                    for (int k = 0; k < 4; k++) {
                        int newX = i + dx[k];
                        int newY = j + dy[k];
                        if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid[0].length &&
                                distanceToMeetingPoint[newX][newY] < distanceToMeetingPoint[i][j] &&
                                grid[newX][newY] != ' ') {
                            if (dx[k] == 0 && dy[k] == 1) {
                                directionsGrid[i][j] = '>';
                            } else if (dx[k] == 1 && dy[k] == 0) {
                                directionsGrid[i][j] = 'v';
                            } else if (dx[k] == 0 && dy[k] == -1) {
                                directionsGrid[i][j] = '<';
                            } else if (dx[k] == -1 && dy[k] == 0) {
                                directionsGrid[i][j] = '^';
                            }
                        }
                    }
                } else if (grid[i][j] == 'S') {
                    directionsGrid[i][j] = 'S';
                } else {
                    directionsGrid[i][j] = ' ';
                }
            }
        }
    }

    /**
     * Calculates the distance from a given position to the meeting point.
     *
     * @param pos The position to check
     * @return The distance to the meeting point
     */
    private int distanceToMeetingPoint(Position pos) {
        return distanceToMeetingPoint[pos.getX()][pos.getY()];
    }

    /**
     * Checks if a given position is free, i.e., no other person occupies this position.
     *
     * @param pos The position to check
     * @return true if the position is free, false otherwise
     */
    private boolean isPositionFree(Position pos) {
        synchronized (people) {
            for (Person person : people) {
                if (isValidPosition(pos) && grid[pos.getX()][pos.getY()] == 'x' &&
                        (person.getCurrentLeft().equals(pos) || person.getCurrentRight().equals(pos))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Generates the next position pairs for a person based on their current position
     * and the distances to the meeting point. It either returns a valid nextPair
     * or null if it couldn't find a next free pair because the positions were already used by someone else.
     * Valid field is assumed meaning a person can't be on an island without being able to reach Sammelpunkt.
     *
     * @param person The person for whom the next positions are to be generated
     * @return The next position pairs or null if no valid pairs are found
     */
    public PositionPair generateNextPositionPairs(Person person) {
        PositionPair currentPositionPair = person.getPositionPair();
        int minimumSumOfDistances = distanceToMeetingPoint(currentPositionPair.getLeft()) + distanceToMeetingPoint(currentPositionPair.getRight());
        Position leftFoot = currentPositionPair.getLeft();
        Position rightFoot = currentPositionPair.getRight();
        PositionPair nextPair = null;

        int[] dx = {-1, 0, 1, 0, -1, 1, 1, -1};
        int[] dy = {0, 1, 0, -1, 1, 1, -1, -1};

        for (int i = 0; i < 8; i++) {
            Position newLeft = new Position(leftFoot.getX() + dx[i], leftFoot.getY() + dy[i]);
            for (int j = 0; j < 8; j++) {
                Position newRight = new Position(rightFoot.getX() + dx[j], rightFoot.getY() + dy[j]);
                if (newLeft.equals(rightFoot) || newRight.equals(leftFoot) || newLeft.equals(newRight) ||
                        !isPositionFree(newLeft) || !isPositionFree(newRight)) {
                    continue;
                }
                if (isValidPosition(newLeft) && isValidPosition(newRight)) {
                    if (newRight.isAdjacent(newLeft)) {
                        if (distanceToMeetingPoint(newLeft) + distanceToMeetingPoint(newRight) < minimumSumOfDistances) {
                            minimumSumOfDistances = distanceToMeetingPoint(newLeft) + distanceToMeetingPoint(newRight);
                            nextPair = new PositionPair(newLeft, newRight);
                        }
                    }
                }
            }
        }
        return nextPair;
    }

    /**
     * Constructs a simulation using the grid name and number of persons specified as command-line arguments.
     *
     * @param gridName    The name of the grid file
     * @param nrOfPeople The number of persons to simulate
     */
    public Simulation(String gridName, int nrOfPeople) {
        this.pipeline = Constants.pipeline;
        this.grid = Constants.gridsMap.get(gridName);
        for(int i = 0; i < nrOfPeople; i++) {
            PositionPair positionPair = generateRandomPositionPair();
            people.add(new Person(positionPair, this, i == 0));
        }
        generateDirectionsGrid();
    }

    /**
     * Starts the simulation by creating a thread for each person.
     */
    public void startSimulation() {
        System.out.println("Starting simulation...");

        // Start threads for all people
        synchronized (people) {
            for (Person person : people) {
                Thread personThread = new Thread(person);
                synchronized (personThreads) {
                    personThreads.add(personThread); // Synchronize access to personThreads
                }
                personThread.start();
                System.out.println("Started thread for person: " + person.getId());
            }
        }

        // Wait for all threads to finish
        synchronized (personThreads) {
            for (Thread personThread : new ArrayList<>(personThreads)) {
                try {
                    personThread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }


    /**
     * Generates a random position pair for a person. The position pair is generated
     * such that both positions are valid and free, and they are adjacent to each other.
     *
     * @return A valid random position pair.
     */
    private PositionPair generateRandomPositionPair() {
        int gridRows = grid.length;
        int gridCols = grid[0].length;

        Position position1, position2;
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};
        int attempts = 0;

        do {
            // Randomly choose the first position
            int x1 = (int) (Math.random() * gridRows);
            int y1 = (int) (Math.random() * gridCols);
            position1 = new Position(x1, y1);

            // Randomly pick a valid direction for the second position
            int direction = (int) (Math.random() * 4);
            int x2 = x1 + dx[direction];
            int y2 = y1 + dy[direction];
            position2 = new Position(x2, y2);

            attempts++;
            if (attempts > 1000) {
                throw new IllegalStateException("Unable to find valid random position pair after 1000 attempts.");
            }
        } while (!isValidPosition(position1) || !isValidPosition(position2) ||
                !isPositionFree(position1) || !isPositionFree(position2));

        System.out.println("Generated random position pair: " + position1 + " and " + position2);
        return new PositionPair(position1, position2);
    }


    /**
     * Checks if a position is valid within the grid, i.e., within bounds and on a walkable path ('x' or 'S').
     *
     * @param pos  The position to check
     * @return true if the position is valid, false otherwise
     */
    private boolean isValidPosition(Position pos) {
        return pos.getX() >= 0 && pos.getX() < this.grid.length && pos.getY() >= 0 && pos.getY() < this.grid[0].length &&
                (this.grid[pos.getX()][pos.getY()] == 'x' || this.grid[pos.getX()][pos.getY()] == 'S');
    }

    /**
     * Checks if a position is an access point to the meeting point.
     *
     * @param pos The position to check
     * @return true if the position is an access point, false otherwise
     */
    public boolean isSammelpunkt(Position pos) {
        return grid[pos.getX()][pos.getY()] == 'S';
    }

    /**
     * Notifies the Sammelpunkt and terminates the thread for the person who reached
     * the Sammelpunkt. This method sends the data of the person who reached the
     * Sammelpunkt to the Sammelpunkt and terminates the person's thread. If all people
     * have reached the Sammelpunkt, it sends an END_MARKER to the Sammelpunkt.
     *
     * @param person The person who reached the Sammelpunkt
     */
    public void notifySammelpunkt(Person person) {
        PersonData data = new PersonData(person.getId(), person.getMoveCounter(), person.getWaitCounter());
        try {
            pipeline.put(data); // Add to the pipeline
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        synchronized (people) {
            person.terminate();
            people.remove(person);
        }

        // Send END_MARKER if all people have been processed
        if (people.isEmpty()) {
            try {
                pipeline.put(Sammelpunkt.END_MARKER);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void printToFile() {
        System.out.println("Printing to file...");
        try {
            File file = new File("test.out");
            FileWriter writer = new FileWriter(file, true);
            writer.write(this.toString());
            writer.write("Leader at: " + people.getFirst().getPositionPair());
            writer.write("\n----------------------------------\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        char[][] gridWithPeople = new char[grid.length][grid[0].length];
        for(Person person : people) {
            gridWithPeople[person.getCurrentLeft().getX()][person.getCurrentLeft().getY()] = 'L';
            gridWithPeople[person.getCurrentRight().getX()][person.getCurrentRight().getY()] = 'R';
        }
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                if(gridWithPeople[i][j] != 'L' && gridWithPeople[i][j] != 'R') {
                    gridWithPeople[i][j] = directionsGrid[i][j];
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                sb.append(gridWithPeople[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
