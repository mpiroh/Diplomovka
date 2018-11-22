package sk.upjs.ics;

import java.util.Map;

public class State {
    private Map<Character, State> transitions;
    private boolean finalState;
}
