package pl.mati.machinelearning.classifier.ila;

import java.util.ArrayList;
import java.util.List;

public class RuleBuilder {
    public List<RuleEntry> rules;

    public RuleBuilder(List<RuleEntry> rules) {
        this.rules = rules;
    }

    public RuleBuilder(){
        rules = new ArrayList<>();
    }

    public RuleIfBuilder ifIndex(int index){
        return new RuleIfBuilder(index, new ArrayList<>(), rules);
    }

    public List<RuleEntry> build() {
        return rules;
    }
}
