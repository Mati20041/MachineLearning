package pl.mati.machinelearning;


import com.google.common.collect.Range;
import pl.mati.machinelearning.classifier.ila.AtomicRule;
import pl.mati.machinelearning.classifier.ila.ILAClassifier;
import pl.mati.machinelearning.classifier.ila.RuleEntry;
import pl.mati.machinelearning.classifier.naivebayes.NaiveBayesClassifier;
import pl.mati.machinelearning.data.*;
import pl.mati.machinelearning.discretization.Discretization;
import pl.mati.machinelearning.discretization.EqualFrequencyDiscretization;
import pl.mati.machinelearning.discretization.EqualWidthDiscretization;
import pl.mati.machinelearning.validator.CrossValidator;
import pl.mati.machinelearning.validator.CrossValidatorResult;
import pl.mati.machinelearning.validator.Rate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleNaiveBayesMain {
    public static void main(String[] args) throws FileNotFoundException {
        NaiveBayesClassifier classifier = new NaiveBayesClassifier(0);
        StringBuilder stringBuilder = new StringBuilder();
        JsonDataSetLoader jsonLoader = new JsonDataSetLoader();
        DataSet s = jsonLoader.loadData(new File("weather.txt"));
        Discretization discretization =  new EqualFrequencyDiscretization(10);
        Map<String, List<Range<Double>>> rangesPerColumn = new LinkedHashMap<>();
        for (Map.Entry<Integer, FieldInfo> entry : s.getInfoMap().entrySet()) {
            if(entry.getValue().getFieldType() == FieldType.NUMBER){
                List<Range<Double>> ranges = discretization.getRanges(s, entry.getKey());
                rangesPerColumn.put(s.getColumnName(entry.getKey()), ranges);
                s = discretization.discretizeColumn(s, entry.getKey());
            }
        }
        stringBuilder.append(printRanges(rangesPerColumn));
        classifier.train(s, null);
        System.out.println("end");
//        System.out.println(printModel(classifier.getModel(), s));
    }


    public static String test(int folds, String file, int k, boolean equalWidth) throws FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        ILAClassifier classifier = new ILAClassifier();
        JsonDataSetLoader jsonLoader = new JsonDataSetLoader();
        DataSet s = jsonLoader.loadData(new File(file));
        Discretization discretization = equalWidth ? new EqualWidthDiscretization(k) : new EqualFrequencyDiscretization(k);
        Map<String, List<Range<Double>>> rangesPerColumn = new LinkedHashMap<>();
        for (Map.Entry<Integer, FieldInfo> entry : s.getInfoMap().entrySet()) {
            if(entry.getValue().getFieldType() == FieldType.NUMBER){
                List<Range<Double>> ranges = discretization.getRanges(s, entry.getKey());
                rangesPerColumn.put(s.getColumnName(entry.getKey()), ranges);
                s = discretization.discretizeColumn(s, entry.getKey());
            }
        }
        stringBuilder.append(printRanges(rangesPerColumn));
        classifier.train(s, null);
        stringBuilder.append(printModel(classifier.getModel(), s));

        CompoundDataSet compoundDataSet = new CompoundDataSet(s);
        CrossValidator validator = new CrossValidator(folds);
        CrossValidatorResult result = validator.crossValidate(classifier, compoundDataSet);
        result.getBestClassifier();
        stringBuilder.append(printResult(result));
        return stringBuilder.toString();
    }

    private static String printRanges(Map<String, List<Range<Double>>> rangesPerColumn) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, List<Range<Double>>> entry : rangesPerColumn.entrySet()) {
            builder.append(String.format("[%20s]: ", entry.getKey()));
            int size = entry.getValue().size();
            for (int i = 0; i < size; i++) {
                Range<Double> doubleRange = entry.getValue().get(i);
                if(i == size - 1){
                    builder.append(String.format("[ %6.2f, %6.2f] = %3d ", doubleRange.lowerEndpoint(), doubleRange.upperEndpoint(), i));
                } else {
                    builder.append(String.format("[ %6.2f, %6.2f) = %3d ", doubleRange.lowerEndpoint(), doubleRange.upperEndpoint(), i));
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private static String printResult(CrossValidatorResult result) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%30s","")).append(String.format("%10s","precision")).append(String.format("%10s","recall")).append(String.format("%10s","accuracy")).append(String.format("%10s","FScore")).append("\n");
        for (Map.Entry<String, Rate> entry : result.getAverageRates().entrySet()) {
            builder.append(String.format("%28s: ",entry.getKey())).append(String.format("%10.4f", entry.getValue().precision())).append(String.format("%10.4f",entry.getValue().recall())).append(String.format("%10.4f",entry.getValue().accuracy())).append(String.format("%10.4f",entry.getValue().fScore())).append("\n");
        }
        builder.append(String.format("%28s: ","avg")).append(String.format("%10.4f", result.getPrecision())).append(String.format("%10.4f",result.getRecall())).append(String.format("%10.4f",result.getAccuracy())).append(String.format("%10.4f",result.getfScore())).append("\n");
        return builder.toString();
    }

    private static String printModel(List<RuleEntry> model, DataSet set) {
        StringBuilder builder = new StringBuilder();
        for (RuleEntry ruleEntry : model) {
            builder.append("if ");
            for (AtomicRule atomicRule : ruleEntry.getRule().getRuleList()) {
                builder.append("[").append(set.getColumnName(atomicRule.getIndex())).append("] == ").append(atomicRule.getEquals()).append(" ");
                builder.append("and ");
            }
            builder.delete(builder.length() - 4, builder.length());
            builder.append(" then [ ").append(ruleEntry.getThen()).append(" ]\n");
        }
        builder.append("Liczba reguł: ").append(model.size()).append("\n");
        builder.append("Liczba atomowych reguł: ").append(model.stream().mapToLong(r -> r.getRule().getRuleList().size()).sum()).append("\n");
        return builder.toString();
    }
}
