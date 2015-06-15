package pl.mati.machinelearning.classifier.naivebayes;

import java.util.ArrayList;
import java.util.Collection;

public class ContinuousColumnProbability extends ColumnProbability {
    private Collection<Number> collection = new ArrayList<>();

    @Override
    protected void doAdd(Object value) {
        collection.add((Number) value);
    }

    public double getMean() {
        return collection.stream().mapToDouble(Number::doubleValue).average().getAsDouble();
    }

    public double getStandardDerivation() {
        double mean = getMean();
        double sum = collection.stream()
                .mapToDouble(x -> Math.pow(x.doubleValue() - mean, 2))
                .sum();
        return Math.sqrt(sum / (collection.size()) );
    }
}
