package sk.upjs.ics;

import java.util.List;
import java.util.Stack;

public class IDPDA {

    private String input;
    private int[] state = new int[2];  // [0]-automaton id; [1]-state
    private Stack<Integer[]> stack = new Stack<>();

    private List<Automaton> automata;

    public IDPDA(List<Automaton> automata) {
        state[0] = 0;
        state[1] = 0;
        this.automata = automata;
    }

    public boolean simulate(String input) {
        this.input = input;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            Automaton automaton = getAutomatonById(state[0]);
            int[][] transitions = automaton.getTransitions();

            boolean finalStateApplied = true;
            while (finalStateApplied) {
                if (transitions[state[1]][0] == Automaton.FINAL_STATE) {
                    if (stack.isEmpty()) {
                        break;
                    }
                    Integer[] stackValue = stack.pop();
                    state[0] = stackValue[0];
                    state[1] = stackValue[1];
                    automaton = getAutomatonById(state[0]);
                    transitions = automaton.getTransitions();
                } else {
                    finalStateApplied = false;
                }
            }

            boolean transitionApplied; // nonterminal transition
            while (true) {
                transitionApplied = false;
                List<Integer> idxOfNonterminals = automaton.getIdxOfNonterminals();
                for (int idx : idxOfNonterminals) {
                    if (transitions[state[1]][idx] != Automaton.NO_VALUE) {
                        Integer[] stackValue = new Integer[2];
                        stackValue[0] = state[0];
                        stackValue[1] = transitions[state[1]][idx];
                        stack.push(stackValue);
                        
                        state[0] = getIdOfAutomatonByNonterminal(automaton.getAlphabet().get(idx));
                        state[1] = 0;
                        automaton = getAutomatonById(state[0]);
                        transitions = automaton.getTransitions();
                        
                        transitionApplied = true;
                        break;
                    }
                }
                if (transitionApplied == false) {
                    break;
                }
            }

            automaton = getAutomatonById(state[0]);
            transitions = automaton.getTransitions();

            if (automaton.getAlphabet().contains(String.valueOf(c))) {
                int idx = automaton.getIndexOf(c);
                int nextState = transitions[state[1]][idx];
                if (nextState != Automaton.NO_VALUE) {
                    state[1] = nextState;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        Automaton automaton = getAutomatonById(state[0]);
        int[][] transitions = automaton.getTransitions();
        if (transitions[state[1]][0] == Automaton.FINAL_STATE && automaton.getId() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Automaton getAutomatonById(int id) {
        for (Automaton automaton : automata) {
            if (automaton.getId() == id) {
                return automaton;
            }
        }
        return null;
    }

    public int getIdOfAutomatonByNonterminal(String nonTerminal) {
        for (Automaton a : automata) {
            if (a.getOwnNonterminal().equals(nonTerminal)) {
                return a.getId();
            }
        }
        return -1;
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

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public List<Automaton> getAutomata() {
        return automata;
    }

}
