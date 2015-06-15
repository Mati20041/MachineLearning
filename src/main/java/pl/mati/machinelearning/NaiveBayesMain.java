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

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NaiveBayesMain {
    private static final int[] FOLDS = {2, 3, 5, 10};
    private static final int[] KS = {0, 5, 10, 15, 30};
    private static final String[] FILES = {"iris.txt", "wine.txt", "glass.txt"};
    private static final boolean[] EQUAL_WIDTH = {false, true};

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(8);
        String[][][][] outputs = new String[FILES.length][EQUAL_WIDTH.length][KS.length][FOLDS.length];

        for (int i = 0; i < FILES.length; ++i) {
            String file = FILES[i];
            for (int j = 0; j < EQUAL_WIDTH.length; ++j) {
                boolean equalWidth = EQUAL_WIDTH[j];
                for (int n = 0; n < FOLDS.length; ++n) {
                    for (int m = 0; m < KS.length; ++m) {
                        int k = KS[m];
                        int folds = FOLDS[n];
                        final int finalI = i;
                        final int finalJ = j;
                        final int finalN = n;
                        final int finalM = m;
                        service.submit(() -> {
                            StringBuilder builder = new StringBuilder();
                            builder.append("---------------------");
                            builder.append(String.format("Plik: %s , k: %d, foldy: %d, %s\n\n", file, k, folds, equalWidth ? "EqualWidthDiscretization" : "EqualFrequencyDiscretization"));
                            try {
                                JsonDataSetLoader jsonLoader = new JsonDataSetLoader();
                                DataSet s = jsonLoader.loadData(new File(file));
                                long start = System.currentTimeMillis();
                                builder.append(test(folds, k, equalWidth, s));
                                builder.append(String.format("%d;", System.currentTimeMillis() - start));
//                                builder.append(String.format("Czas: %dms\n", System.currentTimeMillis() - start));
                            } catch (Exception e) {
                                builder.append("Fail: ").append(e.getMessage());
                                e.printStackTrace();
                            }
                            String output = builder.toString();
                            outputs[finalI][finalJ][finalM][finalN] = output;
                            System.out.println(output);
                        });
                    }
                }
            }
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.DAYS);
        PrintWriter printWriter = new PrintWriter(new BufferedOutputStream(new FileOutputStream("out3.txt")));
        for (int i = 0; i < FILES.length; ++i) {
            for (int j = 0; j < EQUAL_WIDTH.length; ++j) {
                for (int m = 0; m < KS.length; ++m) {
                for (int n = 0; n < FOLDS.length; ++n) {
                        printWriter.append(outputs[i][j][m][n]).append("\n");
                    }
                }
            }
        }
        printWriter.close();

    }

    public static String test(int folds, int k, boolean equalWidth, DataSet s) throws FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        NaiveBayesClassifier classifier = new NaiveBayesClassifier(1);
        if (k != 0) {
            Discretization discretization = equalWidth ? new EqualWidthDiscretization(k) : new EqualFrequencyDiscretization(k);
            Map<String, List<Range<Double>>> rangesPerColumn = new LinkedHashMap<>();
            for (Map.Entry<Integer, FieldInfo> entry : s.getInfoMap().entrySet()) {
                if (entry.getValue().getFieldType() == FieldType.NUMBER) {
                    List<Range<Double>> ranges = discretization.getRanges(s, entry.getKey());
                    rangesPerColumn.put(s.getColumnName(entry.getKey()), ranges);
                    s = discretization.discretizeColumn(s, entry.getKey());
                }
            }
//            stringBuilder.append(printRanges(rangesPerColumn));
        }
        classifier.train(s, null);

        CompoundDataSet compoundDataSet = new CompoundDataSet(s);
        CrossValidator validator = new CrossValidator(folds);
        CrossValidatorResult result = validator.crossValidate(classifier, compoundDataSet);
//        stringBuilder.append(printModel(((ILAClassifier)result.getBestClassifier()).getModel(), s));
//        System.out.println(result);
        stringBuilder.append(folds).append(";").append(k).append(";");
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
                if (i == size - 1) {
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
//        builder.append(String.format("%30s","")).append(String.format("%10s","precision")).append(String.format("%10s","recall")).append(String.format("%10s","accuracy")).append(String.format("%10s","FScore")).append("\n");
//        for (Map.Entry<String, Rate> entry : result.getAverageRates().entrySet()) {
//            builder.append(String.format("%28s: ",entry.getKey())).append(String.format("%10.4f", entry.getValue().precision())).append(String.format("%10.4f",entry.getValue().recall())).append(String.format("%10.4f",entry.getValue().accuracy())).append(String.format("%10.4f",entry.getValue().fScore())).append("\n");
//        }
//        builder.append(String.format("%28s: ","avg")).append(String.format("%10.4f", result.getPrecision())).append(String.format("%10.4f",result.getRecall())).append(String.format("%10.4f",result.getAccuracy())).append(String.format("%10.4f",result.getfScore())).append("\n");
        builder.append(String.format("%.4f;", result.getPrecision())).append(String.format("%.4f;", result.getRecall())).append(String.format("%.4f;", result.getAccuracy())).append(String.format("%.4f;", result.getfScore()));
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
