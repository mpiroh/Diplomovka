package sk.upjs.ics;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        /*Convertor convertor = new Convertor();
        String regExp = "a♥A♥♠c";
        int[][] automaton = convertor.toAutomaton(regExp).getTransitions();
        for (int i = 0; i < automaton.length; i++) {
            for (int j = 0; j < automaton[0].length; j++) {
                System.out.print(automaton[i][j] + " ");
            }
            System.out.println();
        }*/
        
        Map<String, List<String>> map = new LinkedHashMap<>();
        List<String> toList1 = new ArrayList<>();
        List<String> toList2 = new ArrayList<>();
        List<String> toList3 = new ArrayList<>();
        List<String> toList4 = new ArrayList<>();
        List<String> toList5 = new ArrayList<>();
        
        toList1.add("♥A♥ab♥C♥♥D♥");
        map.put("♥S♥", toList1);
        System.out.println(map);
        
        toList2.add("♥E♥a");
        map.put("♥A♥", toList2);
        System.out.println(map);
        
        toList3.add("c♥C♥");
        toList3.add("d");
        map.put("♥C♥", toList3);
        System.out.println(map);
        
        toList4.add("d");
        map.put("♥D♥", toList4);
        System.out.println(map);
        
        toList5.add("x");
        map.put("♥E♥", toList5);
        System.out.println(map);
        
        /*toList1.add("♥A♥cd");
        map.put("♥S♥", toList1);
        System.out.println(map);
        
        toList2.add("a♥A♥");
        toList2.add("b");
        map.put("♥A♥", toList2);
        System.out.println(map);*/
        
        Grammar grammar = new Grammar();
        grammar.setRules(map);
        
        Convertor convertor = new Convertor();
        IDPDA idpda = convertor.toIDPDA(grammar);
        System.out.println(idpda.simulate("xaabcccccdd"));
    }
}