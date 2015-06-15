package pl.mati.machinelearning.classifier.ila;

public class RuleEntry {
    private final Rule rule;
    private final String then;

    public RuleEntry(Rule rule, String then) {
        this.rule = rule;
        this.then = then;
    }

    public Rule getRule() {
        return rule;
    }

    public String getThen() {
        return then;
    }
}
