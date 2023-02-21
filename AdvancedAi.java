import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;

public class AdvancedAi {
    private static final String MODEL_PATH = "models/en-doccat.bin";

    public static void main(String[] args) throws IOException {
        // Load the OpenNLP document categorizer model
        InputStream modelIn = Files.newInputStream(Paths.get(MODEL_PATH));
        DoccatModel model = new DoccatModel(modelIn);
        DocumentCategorizerME categorizer = new DocumentCategorizerME(model);
        Tokenizer tokenizer = SimpleTokenizer.INSTANCE;

        // Load the response properties file
        Properties responses = new Properties();
        InputStream responsesIn = AdvancedAI.class.getClassLoader().getResourceAsStream("responses.properties");
        responses.load(responsesIn);

        // Start the conversation
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hi, I'm an AI. What's your name?");

        String name = scanner.nextLine();
        System.out.println("Nice to meet you, " + name + "! How can I help you today?");

        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }

            // Use OpenNLP to categorize the input
            String[] tokens = tokenizer.tokenize(input);
            double[] outcomes = categorizer.categorize(tokens);
            String category = categorizer.getBestCategory(outcomes);

            // Use the category to select a response
            String response = responses.getProperty(category);
            if (response != null) {
                System.out.println(response);
            } else {
                System.out.println("I'm sorry, I don't know how to respond to that.");
            }
        }
    }
}
