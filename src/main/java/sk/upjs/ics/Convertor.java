package sk.upjs.ics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Convertor {
    public Map<String, String> getMapOfRegExp(Grammar grammar) {
        Map<String, String> map = new LinkedHashMap<>();

        for (String c : grammar.getRules().keySet()) {
            List<String> list = grammar.getRules().get(c);
            StringBuilder regExp = new StringBuilder();
            for (String s : list) {
                regExp.append(s);
                regExp.append("♠");  // special symbol for '+' operator
            }
            regExp.setLength(regExp.length() - 1);
            map.put(c, regExp.toString());
        }

        return map;
    }

    public Automaton toAutomaton(String regExp) {
        List<String> alphabet = getAlphabet(regExp);
        //System.out.println(alphabet);
        
        int[][] transitions = new int[2][alphabet.size()];  // 0th state = inital state, 1st state = final state
        transitions = fillRow(transitions, 0, Automaton.NO_VALUE);
        transitions = fillRow(transitions, 1, Automaton.FINAL_STATE);
        
        int maxState = 1;
        int curState = 0;
        boolean inNonTerminal = false;
        String nonTerminal = "";

        for (int i = 0; i < regExp.length(); i++) {
            char c = regExp.charAt(i);
            if (inNonTerminal) {
                if (c == '♥') {  // end of nonTerminal
                    inNonTerminal = false;
                    nonTerminal = nonTerminal + c;
                    if ((i + 1 < regExp.length() && regExp.charAt(i + 1) == '♠') || i + 1 >= regExp.length()) {
                        transitions[curState][getIndexOf(nonTerminal, alphabet)] = 1; //prechod na nonTerminal do finalState
                        curState = 0;
                        nonTerminal = "";
                    } else {
                        maxState++;
                        transitions = addRow(transitions, alphabet.size());
                        transitions = fillRow(transitions, maxState, Automaton.NO_VALUE);
                        transitions[curState][getIndexOf(nonTerminal, alphabet)] = maxState; //prechod na nonTerminal do maxState
                        curState = maxState;
                        nonTerminal = "";
                    }
                } else {
                    nonTerminal = nonTerminal + c;
                }
            } else {
                if (c == '♥') {  // begin of nonterminal symbol
                    inNonTerminal = true;
                    nonTerminal = nonTerminal + c;
                } else if (c == '♠') {  // '+' operator
                    // do nothing
                } else {  // terminal symbol
                    if (i + 1 < regExp.length() && regExp.charAt(i + 1) == '♠' || i + 1 >= regExp.length()) {
                        transitions[curState][getIndexOf(c, alphabet)] = 1; //prechod na c do finalState
                        curState = 0;
                    } else {
                        maxState++;
                        transitions = addRow(transitions, alphabet.size());
                        transitions = fillRow(transitions, maxState, Automaton.NO_VALUE);
                        transitions[curState][getIndexOf(c, alphabet)] = maxState; //prechod na c do maxState
                        curState = maxState;
                    }
                }
            }
        }
        
        Automaton automaton = new Automaton(alphabet, transitions);
        return automaton;
    }

    public int[][] fillRow(int[][] automaton, int rowIndex, int value) {  // fills selected row with NO_VALUE
        for (int i = 0; i < automaton[rowIndex].length; i++) {
            automaton[rowIndex][i] = value;
        }
        return automaton;
    }
    
    public int[][] addRow(int[][] automaton, int width) {
        int[][] newAutomaton = new int[automaton.length + 1][];
        System.arraycopy(automaton, 0, newAutomaton, 0, automaton.length);
        newAutomaton[newAutomaton.length - 1] = new int[width];
        return newAutomaton;
    }

    public List<String> getAlphabet(String regExp) {
        List<String> alphabet = new ArrayList<>();
        boolean inNonterminal = false;
        String nonTerminal = "";
        for (int i = 0; i < regExp.length(); i++) {
            char c = regExp.charAt(i);
            
            if (c == '♠') {
                continue;
            }
            
            if (inNonterminal) {
                if (c == '♥') {
                    nonTerminal = nonTerminal + c;
                    inNonterminal = false;
                    if (!alphabet.contains(nonTerminal)) {
                        alphabet.add(nonTerminal);
                    }
                    nonTerminal = "";
                } else {
                    nonTerminal = nonTerminal + c;
                }
            } else {
                if (c == '♥') {
                    inNonterminal = true;
                    nonTerminal = nonTerminal + c;
                } else {
                    if (!alphabet.contains(String.valueOf(regExp.charAt(i)))) {
                        alphabet.add(String.valueOf(regExp.charAt(i)));
                    }
                }
            }
        }
        return alphabet;
    }
    
    public int getIndexOf(char c, List<String> alphabet) {
        int i = 0;
        for (String s : alphabet) {
            if (s.charAt(0) == c && s.length() == 1) {
                return i;
            } else {
                i++;
            }
        }
        return -1;
    }
    
    public int getIndexOf(String string, List<String> alphabet) {
        int i = 0;
        for (String s : alphabet) {
            if (s.equals(string)) {
                return i;
            } else {
                i++;
            }
        }
        return -1;
    }
    
    public List<Automaton> toAutomata(Grammar grammar) {
        Map<String, String> regExpMap = getMapOfRegExp(grammar);
        
        List<Automaton> automata = new ArrayList<>();
        int id = 0;
        for (String s : regExpMap.keySet()) {
            String regExp = regExpMap.get(s);
            Automaton automaton = toAutomaton(regExp);
            automaton.setOwnNonterminal(s);
            automaton.setId(id);
            id++;
            
            automata.add(automaton);
        }
        
        return automata;
    }
    
    public IDPDA toIDPDA(Grammar grammar) {
        Map<String, String> regExpMap = getMapOfRegExp(grammar);
        
        List<Automaton> automata = new ArrayList<>();
        int id = 0;
        for (String s : regExpMap.keySet()) {
            String regExp = regExpMap.get(s);
            Automaton automaton = toAutomaton(regExp);
            automaton.setOwnNonterminal(s);
            automaton.setId(id);
            id++;
            
            automata.add(automaton);
        }
        for (Automaton a : automata) {
            System.out.println("CHAR: " + a.getOwnNonterminal());
        }
        
        return new IDPDA(automata);
    }
    
}
