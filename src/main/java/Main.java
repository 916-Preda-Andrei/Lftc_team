import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        Grammar grammar = new Grammar("src/main/resources/g1.txt");
        grammar.printTerminals();
        grammar.printNonTerminals();
        grammar.printProductions();
        grammar.printStartSymbol();
        Parser parser = new Parser("src/main/resources/g1.txt");
        parser.printFirstSet();
        parser.printFollowSet();
        parser.printParseTable();

        ParseOutput parseOutput = new ParseOutput(parser.getParseTable());
        parseOutput.printToFile("src/main/resources/parser_output.txt");

        checkParseSequence("src/main/resources/g1.txt", "src/main/resources/seq.txt", "src/main/resources/out1.txt");
    }

    private static void checkParseSequence(String grammarFile, String sequenceFile, String outputFile) throws IOException {
        System.out.println("Checking our sequence...");
        List<String> sequence = obtainSequence(sequenceFile);
        Parser parser = new Parser(grammarFile);
        List<Pair<String, List<String>>> result = parser.parse(sequence);
        System.out.println("Sequence accepted!");

        writeToFile(result, outputFile);


    }

    private static void writeToFile(List<Pair<String, List<String>>> result, String outputFile) throws IOException {
        FileWriter myWriter = new FileWriter(outputFile);

        for (Pair<String, List<String>> item : result) {
            myWriter.write(item.getKey() + " -> ");
            for (String rhsElement : item.getValue())
                myWriter.write(rhsElement + " ");
            myWriter.write("\n");
        }
        myWriter.close();
    }

    private static List<String> obtainSequence(String sequenceFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(sequenceFile));
        return List.of(reader.readLine().split(" "));
    }
}
