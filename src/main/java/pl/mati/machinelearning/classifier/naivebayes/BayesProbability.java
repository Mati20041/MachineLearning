package pl.mati.machinelearning.classifier.naivebayes;

import pl.mati.machinelearning.data.Cell;

import java.util.Map;

public class BayesProbability {
    private Map<Object, BayesValue> map;

    public BayesProbability(Map<Object, BayesValue> map) {
        this.map = map;
    }

    public double calculate(Object value, Cell aClass) {
        return map.get(aClass.getValue()).getConditionalProbability(value);
    }
}
