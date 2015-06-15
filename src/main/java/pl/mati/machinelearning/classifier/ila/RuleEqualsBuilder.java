package pl.mati.machinelearning.classifier.ila;

import java.util.List;

public class RuleEqualsBuilder {
    List<AtomicRule> atomicRules;
    List<RuleEntry> oldRules;


    public RuleEqualsBuilder(List<AtomicRule> atomicRules, List<RuleEntry> oldRules) {
        this.atomicRules = atomicRules;
        this.oldRules = oldRules;
    }

    public RuleBuilder then (String className){
        oldRules.add(new RuleEntry(new Rule(atomicRules),className ));
        return new RuleBuilder(oldRules);
    }
    public RuleIfBuilder and (int index){
        return new RuleIfBuilder(index, atomicRules, oldRules);
    }
}
