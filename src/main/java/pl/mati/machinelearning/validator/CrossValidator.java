package pl.mati.machinelearning.validator;


import pl.mati.machinelearning.classifier.Classifier;
import pl.mati.machinelearning.data.*;

import java.util.*;

public class CrossValidator {
    private int folds;

    public CrossValidator(int folds) {
        this.folds = folds;
    }
    public CrossValidator() {
        this(5);
    }

    public CrossValidatorResult crossValidate(Classifier classifier, CompoundDataSet dataSet){
        List<PartialCrossValidatorResult> results = new ArrayList<>(folds);
        DataSet learningSet = dataSet.getLearningSet();
        int colSize = learningSet.getInfoMap().size();
        FieldInfo fieldInfo = learningSet.getInfoMap().get(colSize - 1);
        Set<String> allowedValues = fieldInfo.getAllowedValues();
        double highestfScore = -4;
        Classifier bestClassifier = null;
        for (int i = 1; i <= folds; i++) {
            classifier.clear();
            dataSet.setCrossValidationFolds(folds, i);
            classifier.train(dataSet.getLearningSet(), dataSet.getValidationSet());
            PartialCrossValidatorResult validate = validate(classifier, dataSet.getCrossValidationSet(), allowedValues);
            if(validate.getFScore() != Double.NaN && validate.getFScore() > highestfScore){
                highestfScore = validate.getFScore();
                try {
                    bestClassifier = (Classifier) classifier.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            results.add(validate);
        }
        return new CrossValidatorResult(results, bestClassifier);
    }

    private PartialCrossValidatorResult validate(Classifier classifier, DataSet crossValidationSet, Set<String> allowedValues) {
        Map<String, Rate> rates = createRates(allowedValues);
        for (DataRow dataRow : crossValidationSet.getRows()) {
            Cell classify = classifier.classify(dataRow);
            if(classify == null){
                for (Map.Entry<String, Rate> entry : rates.entrySet()) {
                    if(entry.getKey().equals(dataRow.getClassCell().getValue())){
                        entry.getValue().falseNegative();
                    } else {
                        entry.getValue().trueNegative();
                    }
                }
            } else if(dataRow.getClassCell().getValue().equals(classify.getValue())){
                for (Map.Entry<String, Rate> entry : rates.entrySet()) {
                    if(entry.getKey().equals(classify.getValue())){
                        entry.getValue().truePositive();
                    } else {
                        entry.getValue().trueNegative();
                    }
                }
            } else {
                for (Map.Entry<String, Rate> entry : rates.entrySet()) {
                    if(entry.getKey().equals(classify.getValue())){
                        entry.getValue().falsePositive();
                    } else if(entry.getKey().equals(dataRow.getClassCell().getValue())){
                        entry.getValue().falseNegative();
                    } else {
                        entry.getValue().trueNegative();
                    }
                }
            }
        }
        return new PartialCrossValidatorResult(rates);
    }

    private Map<String, Rate> createRates(Set<String> allowedValues) {
        Map<String, Rate> map = new HashMap<>();
        for (String allowedValue : allowedValues) {
            map.put(allowedValue, new Rate());
        }
        return map;
    }

    public int getFolds() {
        return folds;
    }

    public void setFolds(int folds) {
        this.folds = folds;
    }

}
