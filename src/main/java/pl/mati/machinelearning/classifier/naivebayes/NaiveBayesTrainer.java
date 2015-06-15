package pl.mati.machinelearning.classifier.naivebayes;

import pl.mati.machinelearning.data.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NaiveBayesTrainer {
    private final int laplace;
    private ClassProbabilities p = new ClassProbabilities();
    private Map<Cell, AtomicInteger> classCounter = new HashMap<>();
    private int size;

    public NaiveBayesTrainer(int laplace) {
        this.laplace = laplace;
    }

    public Map<Integer, BayesProbability> createModel(DataSet learningSet) {
        size = learningSet.size();
        List<DataRow> rows = learningSet.getRows();
        for (DataRow row : rows) {
            splitData(row);
        }
        Map<Integer, BayesProbability> model = new HashMap<>();
        for (int i = 0; i < learningSet.colSize() - 1; i++) {
            model.put(i, createBayesProbability(i));
        }
        return model;
    }

    private BayesProbability createBayesProbability(int column) {
        return p.getProbability(column, laplace);
    }

    private void splitData(DataRow row) {
        List<Cell> cells = row.getCells();
        Cell aClass = cells.get(cells.size() - 1);
        addClass(aClass);
        for (int i = 0; i < cells.size() - 1; i++) {
            split(i, cells.get(i), aClass);
        }
    }

    private void addClass(Cell aClass) {
        AtomicInteger atomicInteger = classCounter.get(aClass);
        if (atomicInteger == null) {
            atomicInteger = new AtomicInteger(0);
            classCounter.put(aClass, atomicInteger);
        }
        atomicInteger.incrementAndGet();
    }

    private void split(int i, Cell cell, Cell aClass) {
        if (cell.getInfo().getFieldType() == FieldType.NUMBER) {
            p.add(aClass, i, cell.getValue(), false);
//            mContinous.get(aClass).get(i).add((Number) cell.getValue());
        } else {
            p.add(aClass, i, cell.getValue(), true);
//            Map<Object, Integer> objectIntegerMap = mDiscrete.get(aClass);
//            objectIntegerMap.put(i, objectIntegerMap.get(i) + 1);
        }
    }

    public Map<Cell, Double> getClassesProbability() {
        Map<Cell, Double> prob = new HashMap<>();
        for (Map.Entry<Cell, AtomicInteger> entry : classCounter.entrySet()) {
            prob.put(entry.getKey(), (entry.getValue().doubleValue() + laplace) / (size + laplace));
        }
        return prob;
    }
}
