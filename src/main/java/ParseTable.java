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
                            if (temp.containsKey(followSymbol)) {
                                throw new RuntimeException("Conflict in parse table on row " + nonTerminal + " and column " + followSymbol +
                                        " for rule " + rule + " with existing rule " + temp.get(followSymbol));
                            }
                            temp.put(followSymbol, rule);
                        }
                    } else if (grammar.getTerminals().contains(firstSymbol)) {
                        if (temp.containsKey(firstSymbol)) {
                            throw new RuntimeException("Conflict in parse table on row " + nonTerminal + " and column " + firstSymbol +
                                    " for rule " + rule + " with existing rule " + temp.get(firstSymbol));
                        }
                        temp.put(firstSymbol, rule);
                    } else {
                        for (String firstSymbolOfRule : firstSet.get(firstSymbol)) {
                            if (temp.containsKey(firstSymbolOfRule)) {
                                throw new RuntimeException("Conflict in parse table on row " + nonTerminal + " and column " + firstSymbolOfRule +
                                        " for rule " + rule + " with existing rule " + temp.get(firstSymbolOfRule));
                            }
                            temp.put(firstSymbolOfRule, rule);
                        }
                    }
                }
            }
            parseTable.put(nonTerminal, temp);
        }
        for (String terminal : grammar.getTerminals()) {
            Map<String, List<String>> temp = new HashMap<>();
            temp.put(terminal, List.of("pop"));
            parseTable.put(terminal, temp);
        }
        Map<String, List<String>> temp = new HashMap<>();
        temp.put("$", List.of("acc"));
        parseTable.put("$", temp);

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

    public List<String> getElement(String nonTerminal, String terminal) {

        List<String> result = parseTable.get(nonTerminal).get(terminal);
        if (result == null) {
            throw new RuntimeException("No rule found for " + nonTerminal + " -> " + terminal);

        }
        return result;
    }

    @Override
    public String toString() {
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
