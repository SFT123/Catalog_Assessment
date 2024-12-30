import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShamirSecretSharing {

    public static void main(String[] args) {
        String filePath = "test2.json"; // Path to the JSON file
        // String filePath = "test1.json";

        try {
            String jsonInput = readFile(filePath);
            Map<Integer, Integer> points = parseJSON(jsonInput);

            int secret = findConstantTerm(points, 2);
            System.out.println("The secret constant (c) is: " + secret);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    public static String readFile(String filePath) throws IOException {
        StringBuilder jsonContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line).append("\n");
            }
        }
        return jsonContent.toString();
    }

    public static Map<Integer, Integer> parseJSON(String jsonInput) {
        Map<Integer, Integer> points = new HashMap<>();
        String[] lines = jsonInput.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.matches("\"\\d+\": \\{")) {
                String keyStr = line.split(":")[0].replace("\"", "").trim();
                int key = Integer.parseInt(keyStr);

                String baseLine = lines[++i].trim();
                String valueLine = lines[++i].trim();

                int base = Integer.parseInt(baseLine.split(":")[1].replace("\"", "").replace(",", "").trim());
                String encodedValue = valueLine.split(":")[1].replace("\"", "").replace(",", "").trim();

                int decodedValue = decodeValue(encodedValue, base);
                points.put(key, decodedValue);
            }
        }
        return points;
    }

    public static int decodeValue(String value, int base) {
        int result = 0;
        int length = value.length();
        for (int i = 0; i < length; i++) {
            int digit = value.charAt(length - 1 - i) - '0';
            result += digit * Math.pow(base, i);
        }
        return result;
    }

    public static int findConstantTerm(Map<Integer, Integer> points, int degree) {
        double constantTerm = 0.0;

        for (Map.Entry<Integer, Integer> p1 : points.entrySet()) {
            int x1 = p1.getKey();
            int y1 = p1.getValue();

            double product = y1;
            for (Map.Entry<Integer, Integer> p2 : points.entrySet()) {
                int x2 = p2.getKey();

                if (x1 != x2) {
                    product *= (0.0 - x2) / (x1 - x2);
                }
            }

            constantTerm += product;
        }
        return (int) Math.round(constantTerm);
    }
}
