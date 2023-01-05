import java.io.IOException;
import java.util.*;

public class Parser {
    private final Grammar grammar;
    private final Map<String, Set<String>> firstSet;
    private final Map<String, Set<String>> followSet;
    private static final Stack<List<String>> rules = new Stack<>();
    private ParseTable parseTable;


    public Parser(String fileName) throws IOException {
        this.grammar = new Grammar(fileName);
        this.firstSet = new HashMap<>();
        this.followSet = new HashMap<>();
        generateSets();
        this.parseTable = new ParseTable(grammar, firstSet, followSet);
    }

    private void generateSets() {
        generateFirstSet();
        generateFollowSet();
    }

    private void generateFirstSet() {
        for (String nonTerminal : grammar.getNonTerminals()) {
            firstSet.put(nonTerminal, this.firstOf(nonTerminal));
        }
    }

    private Set<String> firstOf(String nonTerminal) {
        if (firstSet.containsKey(nonTerminal))
            return firstSet.get(nonTerminal);
        Set<String> temp = new HashSet<>();
        Set<String> terminals = grammar.getTerminals();
        for (Map.Entry<String, List<List<String>>> production : grammar.getProductionsForNonterminal(nonTerminal).entrySet())
            for (List<String> rule : production.getValue()) {
                String firstSymbol = rule.get(0);
                if (firstSymbol.equals("ε"))
                    temp.add("ε");
                else if (terminals.contains(firstSymbol))
                    temp.add(firstSymbol);
                else
                    temp.addAll(firstOf(firstSymbol));
            }
        return temp;
    }

    private void generateFollowSet() {
        for (String nonTerminal : grammar.getNonTerminals()) {
            followSet.put(nonTerminal, this.followOf(nonTerminal, nonTerminal));
        }
    }

    private Set<String> followOf(String nonTerminal, String initialNonTerminal) {
        if (followSet.containsKey(nonTerminal))
            return followSet.get(nonTerminal);
        Set<String> temp = new HashSet<>();
        Set<String> terminals = grammar.getTerminals();

        if (nonTerminal.equals(grammar.getStartSymbol()))
            temp.add("$");

        for (Map.Entry<String, List<List<String>>> production : grammar.getProductionsContainingNonterminal(nonTerminal).entrySet()) {
            String productionStart = production.getKey();
            for (List<String> rule : production.getValue()) {
                if (rule.contains(nonTerminal)) {
                    int indexNonTerminal = rule.indexOf(nonTerminal);
                    temp.addAll(followOperation(nonTerminal, temp, terminals, productionStart, rule, indexNonTerminal, initialNonTerminal));
                    List<String> sublist = rule.subList(indexNonTerminal + 1, rule.size());
                    if (sublist.contains(nonTerminal))
                        temp.addAll(followOperation(nonTerminal, temp, terminals, productionStart, rule, indexNonTerminal + 1 + sublist.indexOf(nonTerminal), initialNonTerminal));

                }
            }
        }

        return temp;
    }

    private Set<String> followOperation(String nonTerminal, Set<String> temp, Set<String> terminals, String productionStart, List<String> rule, int indexNonTerminal, String initialNonTerminal) {
        if (indexNonTerminal == rule.size() - 1) {
            if (productionStart.equals(nonTerminal))
                return temp;
            if (!initialNonTerminal.equals(productionStart)) {
                temp.addAll(followOf(productionStart, initialNonTerminal));
            }
        } else {
            String nextSymbol = rule.get(indexNonTerminal + 1);
            if (terminals.contains(nextSymbol))
                temp.add(nextSymbol);
            else {
                if (!initialNonTerminal.equals(nextSymbol)) {
                    Set<String> fists = new HashSet<>(firstSet.get(nextSymbol));
                    if (fists.contains("ε")) {
                        temp.addAll(followOf(nextSymbol, initialNonTerminal));
                        fists.remove("ε");
                    }
                    temp.addAll(fists);
                }
            }
        }
        return temp;
    }


    public Map<String, Set<String>> getFirstSet() {
        return firstSet;
    }

    public Map<String, Set<String>> getFollowSet() {
        return followSet;
    }

    //print parser table
    public void printParseTable() {
        parseTable.printParseTable();
    }


    public void printFirstSet() {
        System.out.println("First set:");
        for (Map.Entry<String, Set<String>> entry : firstSet.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    public void printFollowSet() {
        System.out.println("Follow set:");
        for (Map.Entry<String, Set<String>> entry : followSet.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    public ParseTable getParseTable() {
        return parseTable;
    }
}