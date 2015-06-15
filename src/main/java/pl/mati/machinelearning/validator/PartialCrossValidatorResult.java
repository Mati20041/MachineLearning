package pl.mati.machinelearning.validator;

import java.util.Collections;
import java.util.Map;

public class PartialCrossValidatorResult {
    private double precision;
    private double recall;
    private double accuracy;
    private double fScore;
    private Map<String, Rate> rates;

    public PartialCrossValidatorResult(Map<String, Rate> rates) {
        this.rates = rates;
        calculateMeasurements(rates);
    }

    private void calculateMeasurements(Map<String, Rate> rates) {
        long count = 0;
        for (Map.Entry<String, Rate> entry : rates.entrySet()) {
            precision += entry.getValue().precision() * entry.getValue().getCount();
            recall += entry.getValue().recall() * entry.getValue().getCount();
            accuracy += entry.getValue().accuracy() * entry.getValue().getCount();
            fScore += entry.getValue().fScore() * entry.getValue().getCount();
            count += entry.getValue().getCount();
        }
        precision /= count;
        recall /= count;
        accuracy /= count;
        fScore /= count;

    }

    public Map<String, Rate> getRates() {
        return Collections.unmodifiableMap(rates);
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getFScore() {
        return fScore;
    }
}
