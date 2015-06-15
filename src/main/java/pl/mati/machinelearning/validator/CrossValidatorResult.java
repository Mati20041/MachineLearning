package pl.mati.machinelearning.validator;


import pl.mati.machinelearning.classifier.Classifier;

import java.util.*;

public class CrossValidatorResult {
    private double precision = 0;
    private double recall=0;
    private double accuracy=0;
    private double fScore=0;
    private List<PartialCrossValidatorResult> partialResults;
    private Map<String, Rate> averageRates;
    private Classifier bestClassifier;


    public CrossValidatorResult(List<PartialCrossValidatorResult> results, Classifier bestClassifier) {
        this.partialResults = results;
        this.bestClassifier = bestClassifier;
        calculateAverageRates(results);
        calculateFields(results);
    }

    private void calculateFields(List<PartialCrossValidatorResult> results) {
        long count = 0;
        for (PartialCrossValidatorResult result : results) {
            precision += result.getPrecision();
            recall += result.getRecall();
            accuracy += result.getAccuracy();
            fScore += result.getFScore();
            ++count;
        }
        precision /= count;
        recall /= count;
        accuracy /= count;
        fScore /= count;

    }

    private void calculateAverageRates(List<PartialCrossValidatorResult> results) {
        averageRates = new HashMap<>();
        Set<String> strings = results.get(0).getRates().keySet();
        for (String string : strings) {
            double avgTP = 0, avgTN = 0, avgFP = 0, avgFN = 0;
            for (PartialCrossValidatorResult result : results) {
                Rate rate = result.getRates().get(string);
                avgTP += rate.getTruePositive();
                avgTN += rate.getTrueNegative();
                avgFP += rate.getFalsePositive();
                avgFN += rate.getFalseNegative();
            }
            avgFN /= results.size();
            avgTN /= results.size();
            avgFP /= results.size();
            avgTP /= results.size();
            averageRates.put(string, new Rate(avgTP, avgTN, avgFP, avgFN));
        }
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

    public double getfScore() {
        return fScore;
    }

    public List<PartialCrossValidatorResult> getPartialResults() {
        return Collections.unmodifiableList(partialResults);
    }

    public Map<String, Rate> getAverageRates() {
        return Collections.unmodifiableMap(averageRates);
    }

    @Override
    public String toString() {
        return "CrossValidatorResult{" +
                "precision=" + precision +
                ", recall=" + recall +
                ", accuracy=" + accuracy +
                ", fScore=" + fScore +
                '}';
    }

    public Classifier getBestClassifier() {
        return bestClassifier;
    }
}
