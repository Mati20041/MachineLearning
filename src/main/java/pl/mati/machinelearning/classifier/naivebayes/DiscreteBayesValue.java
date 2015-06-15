package pl.mati.machinelearning.classifier.naivebayes;

import java.util.Map;

public class DiscreteBayesValue implements BayesValue<Object> {
    private Map<Object, Double> probabilities;
    private double missingProbabilityValue;

    public DiscreteBayesValue(Map<Object, Double> probabilities, double missingProbabilityValue) {
        this.probabilities = probabilities;
        this.missingProbabilityValue = missingProbabilityValue;
    }

    @Override
    public double getConditionalProbability(Object value) {
        return probabilities.containsKey(value) ? probabilities.get(value) : missingProbabilityValue;
    }
}
