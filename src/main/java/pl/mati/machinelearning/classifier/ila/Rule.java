package pl.mati.machinelearning.classifier.ila;

import pl.mati.machinelearning.data.Cell;
import pl.mati.machinelearning.data.DataRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rule {
    private List<AtomicRule> ruleList;

    public Rule() {
        this.ruleList = new ArrayList<>(1);
    }

    public Rule(List<AtomicRule> ruleList) {
        this.ruleList = ruleList;
    }

    public List<AtomicRule> getRuleList() {
        return Collections.unmodifiableList(ruleList);
    }

    public boolean check(DataRow dataRow){
        for (AtomicRule atomicRule : ruleList) {
            Cell cell = dataRow.getCell(atomicRule.getIndex());
            if(!cell.getValue().equals(atomicRule.getEquals())){
                return false;
            }
        }
        return true;
    }

    public Rule and(Rule rule){
        List<AtomicRule> rules = new ArrayList<>(ruleList);
        rules.addAll(rule.getRuleList());
        return new Rule(rules);
    }
}
