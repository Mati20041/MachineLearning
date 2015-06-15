package pl.mati.machinelearning.classifier.ila;


import pl.mati.machinelearning.classifier.Classifier;
import pl.mati.machinelearning.data.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ILAClassifier implements Classifier {
    private List<RuleEntry> model;

    public ILAClassifier() {
        clear();
    }
    private ILAClassifier(List<RuleEntry> rules) {
        model = rules;
    }

    @Override
    public void train(DataSet learningSet, DataSet validationSet) {
        createRules(learningSet);
    }

    private void createRules(DataSet learningSet) {
        this.model = new ILATrainer().createModel(learningSet);
    }

    @Override
    public Cell classify(DataRow data) {
        for (RuleEntry ruleEntry : model) {
            if(ruleEntry.getRule().check(data)){
                return new Cell<>(new FieldInfo(FieldType.CLASS, Collections.emptySet()), ruleEntry.getThen());
            }
        }
        return null;
    }

    @Override
    public void clear() {
        model = new ArrayList<>();
    }

    public List<RuleEntry> getModel() {
        return Collections.unmodifiableList(model);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new ILAClassifier(new ArrayList<>(model));
    }
}
