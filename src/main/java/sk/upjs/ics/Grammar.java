package sk.upjs.ics;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Grammar {
    private Map<String, List<String>> rules = new LinkedHashMap<>();
    private char startSymbol;
    
    public Grammar() {
    }
    
    public Grammar(char startSymbol) {
        this.startSymbol = startSymbol;
    }
    
    public void addRule(String from, String to) {
        List<String> toList = rules.get(from);
        if (toList == null) {
            toList = new ArrayList<>();
        }
        toList.add(to);
        rules.put(from, toList);
    }

    public Map<String, List<String>> getRules() {
        return rules;
    }

    public void setRules(Map<String, List<String>> rules) {
        this.rules = rules;
    }

    public char getStartSymbol() {
        return startSymbol;
    }

    public void setStartSymbol(char startSymbol) {
        this.startSymbol = startSymbol;
    }
    
}
