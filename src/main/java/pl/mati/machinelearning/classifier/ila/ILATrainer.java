package pl.mati.machinelearning.classifier.ila;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import pl.mati.machinelearning.data.DataRow;
import pl.mati.machinelearning.data.DataSet;
import pl.mati.machinelearning.data.FieldInfo;
import pl.mati.machinelearning.data.FieldType;

import java.util.*;
import java.util.stream.Collectors;

public class ILATrainer {
    public Multimap<Object, DataRow> rowsForClass = ArrayListMultimap.create();
    public Multimap<Object, DataRow> rowsForClassOrigin;
    private DataSet learningSet;
    int cols = 0;

    public List<RuleEntry> createModel(DataSet learningSet) {
        this.learningSet = learningSet;
        this.cols = learningSet.getInfoMap().size() - 1;
        FieldInfo classFieldInfo = learningSet.getInfoMap().values().stream().filter(f -> f.getFieldType() == FieldType.CLASS).findFirst().get();
        separateClasses(learningSet);
        List<RuleEntry> rules = new ArrayList<>();
        while (!isEmpty(rowsForClass)){
            Object classType = null;
            for (String s : classFieldInfo.getAllowedValues()) {
                if(rowsForClass.get(s).size() > 0){
                    classType = s;
                    break;
                }
            }
            if(classType == null){
                throw new RuntimeException("class type null");
            }
            Rule r = findShortestRule(classType);
            removeRows(r);
            rules.add(new RuleEntry(r, classType.toString()));
        }

        return rules;
    }

    private void removeRows(Rule r) {
        List<Map.Entry<Object, DataRow>> toDelete = rowsForClass.entries().parallelStream()
                .filter(entry -> r.check(entry.getValue()))
                .collect(Collectors.toList());
        for (Map.Entry<Object, DataRow> entry : toDelete) {
            rowsForClass.remove(entry.getKey(), entry.getValue());
        }

    }

    private Rule findShortestRule(Object classType) {
        int i = 1;
        Rule r = null;
        while((r = findRule(classType, i)) == null){
            ++i;
            if(i > cols){
                throw new RuntimeException("cannot create rule");
            }
        }
        return r;
    }

    private Rule findRule(Object classType, int i) {
        List<RuleResult> results =  getResults(classType, i , new Rule(Collections.emptyList()));
        if(results.isEmpty()){
            return null;
        } else {
            return results.stream().max((o1,o2) -> o1.getCount() - o2.getCount()).get().getRule();
        }
    }

    private List<RuleResult> getResults(Object classType, int i, Rule rule) {
        List<RuleResult> results = new ArrayList<>();
        if(i == 0){
            int count = countRowsForClass(classType, rule);
            if(count > 0){
                return Collections.singletonList(new RuleResult(rule, count));
            } else {
                return Collections.emptyList();
            }
        } else {
            for (int j = 0; j < cols; j++) {
                for (String value : learningSet.getInfoMap().get(j).getAllowedValues()) {
                    results.addAll(getResults(classType, i - 1, rule.and(new Rule(Collections.singletonList(new AtomicRule(j, value))))));
                }
            }
        }
        return results;
    }

    private int countRowsForClass(Object classType, Rule rule) {
        int count = 0;
        for (Map.Entry<Object, DataRow> entry : rowsForClassOrigin.entries()) {
            if(entry.getKey().equals(classType)){
                continue;
            } else {
                if(rule.check(entry.getValue())){
                    return -1;
                }
            }
        }
        for (DataRow dataRow : rowsForClass.get(classType)) {
            if(rule.check(dataRow)){
                ++count;
            }
        }
        return count;
    }

    private boolean isEmpty(Multimap<Object, DataRow> rowsForClass) {

        return rowsForClass.isEmpty();
    }


    private void separateClasses(DataSet learningSet) {
        for (DataRow dataRow : learningSet.getRows()) {
            rowsForClass.put(dataRow.getClassCell().getValue(), dataRow);
        }
        this.rowsForClassOrigin = ArrayListMultimap.create(rowsForClass);
    }
}
