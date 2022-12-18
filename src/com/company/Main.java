package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        try {
            Grammar grammar = new Grammar("C:\\Users\\cenan\\Desktop\\Lftc_team\\src\\g1.txt");
            grammar.printTerminals();
            grammar.printNonTerminals();
            grammar.printProductions();
            grammar.printStartSymbol();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
