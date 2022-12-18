import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        try {
            Grammar grammar = new Grammar("src/main/resources/g2.txt");
            grammar.printTerminals();
            grammar.printNonTerminals();
            grammar.printProductions();
            grammar.printStartSymbol();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
