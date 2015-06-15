package pl.mati.machinelearning.classifier.ila;

public class RuleResult {
    private Rule rule;
    private int count;

    public RuleResult(Rule rule, int count) {
        this.rule = rule;
        this.count = count;
    }

    public Rule getRule() {
        return rule;
    }

    public int getCount() {
        return count;
    }
}
