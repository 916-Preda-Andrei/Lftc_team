import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

            Grammar grammar = new Grammar("src/main/resources/g1.txt");
            grammar.printTerminals();
            grammar.printNonTerminals();
            grammar.printProductions();
            grammar.printStartSymbol();
            Parser parser = new Parser("src/main/resources/g3.txt");
            parser.printFirstSet();
            parser.printFollowSet();
            parser.printParseTable();

    }
}
