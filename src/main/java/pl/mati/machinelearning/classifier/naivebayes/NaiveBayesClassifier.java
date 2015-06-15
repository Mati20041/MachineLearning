package pl.mati.machinelearning.classifier.naivebayes;

import pl.mati.machinelearning.classifier.Classifier;
import pl.mati.machinelearning.data.Cell;
import pl.mati.machinelearning.data.DataRow;
import pl.mati.machinelearning.data.DataSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NaiveBayesClassifier implements Classifier{
    private Map<Integer, BayesProbability> bayes;
    private Map<Cell, Double> classes;
    private int laplace = 0;

    public NaiveBayesClassifier() {
        bayes = new HashMap<>();
        classes = new HashMap<>();
    }
    public NaiveBayesClassifier(int laplace) {
        this();
        this.laplace = laplace;
    }

    private NaiveBayesClassifier(Map<Integer, BayesProbability> bayes, Map<Cell, Double> classes) {
        this.bayes = bayes;
        this.classes = classes;
    }

    @Override
    public void train(DataSet learningSet, DataSet validationSet) {
        NaiveBayesTrainer trainer = new NaiveBayesTrainer(laplace);
        this.bayes = trainer.createModel(learningSet);
        this.classes = trainer.getClassesProbability();
    }

    @Override
    public Cell classify(DataRow learningSet) {
        double probabilityMax = -1;
        Cell classified = null;
        for (Cell aClass : classes.keySet()) {
            double probability = calculateProbability(learningSet, aClass);
            if(probability > probabilityMax){
                probabilityMax = probability;
                classified = aClass;
            }
        }
        return classified;
    }

    private double calculateProbability(DataRow learningSet, Cell aClass) {
        int size = learningSet.getColumnMap().size();
        double probability = classes.get(aClass);
        for (int i = 0; i < size; i++) {
            Cell cell = learningSet.getCell(i);
            if(cell.equals(learningSet.getClassCell())) {
                continue;
            }
            probability *= bayes.get(i).calculate(cell.getValue(), aClass);
        }
        return probability;
    }

    @Override
    public void clear() {
        bayes.clear();
        classes.clear();
    }
    @Override
    public Object clone(){
        return new NaiveBayesClassifier(bayes, classes);
    }
}
