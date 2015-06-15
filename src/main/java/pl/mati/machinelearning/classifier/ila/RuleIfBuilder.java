package pl.mati.machinelearning.classifier.ila;

import java.util.List;

public class RuleIfBuilder {
    private int index;
    private List<AtomicRule> atomicRules;
    private List<RuleEntry> rules;

    public RuleIfBuilder(int index, List<AtomicRule> atomicRules, List<RuleEntry> rules) {
        this.index = index;
        this.atomicRules = atomicRules;
        this.rules = rules;
    }

    public RuleEqualsBuilder equals(String value){
        atomicRules.add(new AtomicRule(index, value));
        return new RuleEqualsBuilder(atomicRules, rules);
    }
}
