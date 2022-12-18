import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Grammar {
    Set<String> terminals;
    Set<String> nonTerminals;
    Map<String, List<List<String>>> productions;
    String startSymbol;

    public Grammar(String fileName) throws IOException {
        terminals = new HashSet<>();
        nonTerminals = new HashSet<>();
        productions = new HashMap<>();

        readFromFile(fileName);
    }

    private void readFromFile(String fileName) throws IOException {
        //read terminals, nonTerminals, productions and startSymbol from file
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        nonTerminals.addAll(List.of(reader.readLine().split(" ")));
        terminals.addAll(List.of(reader.readLine().split(" ")));
        startSymbol = reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("->");
            List<String> lhs = List.of(parts[0].trim().split(" "));
            if (lhs.size() > 1) {
                throw new RuntimeException("Not CFG!");
            }
            String nonTerminal = lhs.get(0);
            List<String> rhs = List.of(parts[1].trim().split(" "));
            if (productions.containsKey(nonTerminal)) {
                List<List<String>> value = productions.get(nonTerminal);
                value.add(rhs);
                productions.put(nonTerminal, value);
            } else {
                productions.put(nonTerminal, new ArrayList<>(Collections.singleton(rhs)));
            }

        }
    }

    void printTerminals() {
        System.out.println("Terminals: " + terminals);
    }

    void printNonTerminals() {
        System.out.println("NonTerminals: " + nonTerminals);
    }

    void printProductions() {
        System.out.println("Productions: " + productions);
    }

    void printStartSymbol() {
        System.out.println("StartSymbol: " + startSymbol);
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public Map<String, List<List<String>>> getProductions() {
        return productions;
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public Map<String, List<List<String>>> getProductionsForNonterminal(String nonTerminal) {
        Map<String, List<List<String>>> result = new HashMap<>();
        for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
            if (entry.getKey().equals(nonTerminal)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    public Map<String, List<List<String>>> getProductionsContainingNonterminal(String nonTerminal) {
        Map<String, List<List<String>>> result = new HashMap<>();
        for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
            for (List<String> rhs : entry.getValue()) {
                if (rhs.contains(nonTerminal)) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }
}
