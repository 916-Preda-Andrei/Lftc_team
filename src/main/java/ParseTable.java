import java.util.*;

// create parse table for LL(1) grammar
public class ParseTable {
    private Map<String, Map<String, List<String>>> parseTable;
    private Grammar grammar;
    private Map<String, Set<String>> firstSet;
    private Map<String, Set<String>> followSet;

    public ParseTable(Grammar grammar, Map<String, Set<String>> firstSet, Map<String, Set<String>> followSet) {
        this.grammar = grammar;
        this.firstSet = firstSet;
        this.followSet = followSet;
        this.parseTable = new HashMap<>();
        createParseTable();
    }

    private void createParseTable() {
       for (String nonTerminal : grammar.getNonTerminals()) {
                Map<String, List<String>> temp = new HashMap<>();
                for (Map.Entry<String, List<List<String>>> production : grammar.getProductionsForNonterminal(nonTerminal).entrySet()) {
                    for (List<String> rule : production.getValue()) {
                        String firstSymbol = rule.get(0);
                        if (firstSymbol.equals("ε")) {
                            for (String followSymbol : followSet.get(nonTerminal)) {
                                temp.put(followSymbol, rule);
                            }
                        } else if (grammar.getTerminals().contains(firstSymbol)) {
                            temp.put(firstSymbol, rule);
                        } else {
                            for (String firstSymbolOfRule : firstSet.get(firstSymbol)) {
                                temp.put(firstSymbolOfRule, rule);
                            }
                        }
                    }
                }
                parseTable.put(nonTerminal, temp);
            }
    }

    public Map<String, Map<String, List<String>>> getParseTable() {
        return parseTable;
    }

    public void printParseTable() {
        for (Map.Entry<String, Map<String, List<String>>> entry : parseTable.entrySet()) {
            System.out.println(entry.getKey() + ":");
            for (Map.Entry<String, List<String>> rule : entry.getValue().entrySet()) {
                System.out.println("\t" + rule.getKey() + " -> " + rule.getValue());
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Map<String, List<String>>> entry : parseTable.entrySet()) {
            result.append(entry.getKey()).append(":\n");
            for (Map.Entry<String, List<String>> rule : entry.getValue().entrySet()) {
                result.append("\t").append(rule.getKey()).append(" -> ").append(rule.getValue()).append("\n");
            }
        }
        return result.toString();
    }
}
