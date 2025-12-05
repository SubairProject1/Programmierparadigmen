import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AUTHOR: Catalin
 * STYLE: Procedural Programming
 * The methods of this class use  procedural programming because it organizes code into a sequence of procedures (methods) that operate on data,
 * focusing on performing actions (like parsing and filtering) rather than on representing data as objects with behaviors.
 *
 * Auxiliary class for parsing JSON strings containing materials data.
 *
 * GOOD: The MaterialsJsonParser class has weak coupling with other classes
 * because it only deals with parsing JSON data and does not depend on other classes.
 * The MaterialsJsonParser class exemplifies good procedural programming by focusing on task-specific, sequential data
 * handling with minimal dependencies, making it modular, easy to modify, and flexible for future extensions.
 */
public class MaterialsJsonParser {

    private static final String MATERIALS_FILE_PATH = "materials.json";

    /**
     * Parses the materials data from the JSON file and returns a list of Material instances.
     *
     * @return a list of Material instances parsed from the JSON file.
     *
     * Precondition: The JSON file at the specified path exists and is accessible.
     * Postcondition: A list of Material instances is returned, each populated with data from the JSON file.
     */
    public static List<Material> parseMaterials() {
        List<Material> materials = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(MATERIALS_FILE_PATH))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            // Convert to string and remove outer brackets to get the inner content
            String content = jsonContent.toString().trim();
            if (content.startsWith("[") && content.endsWith("]")) {
                content = content.substring(1, content.length() - 1); // Remove [ and ]
            }

            int i = 0;
            while (i < content.length()) {
                int start = content.indexOf('{', i);
                int end = content.indexOf('}', start);

                if (start == -1 || end == -1) {
                    break;
                }
                String materialObject = content.substring(start + 1, end);

                String[] fields = materialObject.split(",");
                double quality = 0;
                double ecologicalCoefficient = 0;
                double cost = 0;

                for (String field : fields) {
                    String[] keyValue = field.split(":");
                    String key = keyValue[0].trim().replace("\"", ""); // Remove quotes from key
                    double value = Double.parseDouble(keyValue[1].trim());

                    switch (key) {
                        case "quality":
                            quality = value;
                            break;
                        case "ecologicalCoefficient":
                            ecologicalCoefficient = value;
                            break;
                        case "cost":
                            cost = value;
                            break;
                    }
                }

                materials.add(new Material(quality, ecologicalCoefficient, cost));
                i = end + 1;
            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        return materials;
    }

    /**
     * Chooses a random material from the provided list that falls within the specified price and ecological coefficient intervals.
     *
     * @param materials a list of materials to choose from
     * @param minQuality the minimum quality of the material
     * @param maxPrice the maximum price of the material
     * @param minEcologicalCoefficient the minimum ecological coefficient of the material
     * @return a random material from the list that meets the specified criteria, or null if no such material exists
     *
     * Precondition: The materials list is not null, minQuality, maxPrice, and minEcologicalCoefficient are non-negative.
     * Postcondition: A random material that meets the specified criteria is returned, or null if no such material exists.
     *
     * GOOD: The getRandomMaterial method uses dynamic binding when accessing Material properties.
     * This could allow for future subclasses of Material that might override
     * getQuality, getCost, or getEcologicalCoefficient methods.
     * Without dynamic binding, each material property access would need explicit type-checks,
     * making the code more complex and less maintainable.
     */
    public static Material getRandomMaterial(List<Material> materials, double maxPrice, double minEcologicalCoefficient, double minQuality) {
        if(materials == null) { throw new IllegalArgumentException("The materials list is empty."); }
        if(minQuality < 0 || maxPrice < 0 || minEcologicalCoefficient < 0) { throw new IllegalArgumentException("The minQuality, maxPrice and minEcologicalCoefficient are negative."); }
        List<Material> possibleMaterials = new ArrayList<>();
        for (Material material : materials) {
            if (material.getQuality() >= minQuality && material.getCost() <= maxPrice && material.getEcologicalCoefficient() >= minEcologicalCoefficient) {
                possibleMaterials.add(material);
            }
        }
        if (possibleMaterials.isEmpty()) { return null; }
        int randomIndex = (int) (Math.random() * possibleMaterials.size());
        return possibleMaterials.get(randomIndex);
    }
}
