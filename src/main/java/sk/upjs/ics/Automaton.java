package sk.upjs.ics;

import java.util.ArrayList;
import java.util.List;

public class Automaton {

    public final static int NO_VALUE = Integer.MIN_VALUE;
    public final static int FINAL_STATE = Integer.MAX_VALUE;

    private int id = -1;
    private List<String> alphabet;
    private int[][] transitions;
    private String ownNonterminal;

    public Automaton(List<String> alphabet, int[][] transitions) {
        this.alphabet = alphabet;
        this.transitions = transitions;
    }

    public List<Integer> getIdxOfNonterminals() {
        List<Integer> idxOfNonterminals = new ArrayList<>();
        int i = 0;
        for (String s : alphabet) {
            if (s.startsWith("♥")) {
                idxOfNonterminals.add(i);
                i++;
            } else {
                i++;
            }
        }
        return idxOfNonterminals;
    }

    public int getIndexOf(char c) {
        int i = 0;
        for (String s : this.alphabet) {
            if (s.charAt(0) == c && s.length() == 1) {
                return i;
            } else {
                i++;
            }
        }
        return -1;
    }

    public int getIndexOf(String string) {
        int i = 0;
        for (String s : this.alphabet) {
            if (s.equals(string)) {
                return i;
            } else {
                i++;
            }
        }
        return -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(List<String> alphabet) {
        this.alphabet = alphabet;
    }

    public int[][] getTransitions() {
        return transitions;
    }

    public void setTransitions(int[][] transitions) {
        this.transitions = transitions;
    }

    public String getOwnNonterminal() {
        return ownNonterminal;
    }

    public void setOwnNonterminal(String ownNonterminal) {
        this.ownNonterminal = ownNonterminal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < transitions.length; i++) {
            sb.append("state: " + i);
            if (i == 0) {
                sb.append(" (inital)");
            } else if (i == 1) {
                sb.append(" (final)");
                sb.append("\n");
                sb.append("\n");
                continue;
            }
            sb.append("\n");

            for (int j = 0; j < transitions[0].length; j++) {
                if (transitions[i][j] != Automaton.NO_VALUE) {
                    sb.append(alphabet.get(j) + " -> " + transitions[i][j]);
                    sb.append("\n");
                }
            }
            if (i < transitions.length - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }
    
    public String toStringHtml() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < transitions.length; i++) {
            sb.append("state: " + i);
            if (i == 0) {
                sb.append(" (inital)");
            } else if (i == 1) {
                sb.append(" (final)");
                sb.append("\n");
                sb.append("\n");
                continue;
            }
            sb.append("\n");

            for (int j = 0; j < transitions[0].length; j++) {
                if (transitions[i][j] != Automaton.NO_VALUE) {
                    String symbol = alphabet.get(j);
                    if (symbol.startsWith("♥")) {
                        sb.append("<html>");
                        sb.append("<font color=\"red\">");
                        sb.append(symbol.substring(1, symbol.length()-1));
                        sb.append("</font>");;
                        sb.append(" -> ");
                        sb.append(transitions[i][j]);
                        sb.append("</html>");
                    } else {
                        sb.append(alphabet.get(j) + " -> " + transitions[i][j]);
                    }
                    
                    sb.append("\n");
                }
            }
            if (i < transitions.length - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

}
