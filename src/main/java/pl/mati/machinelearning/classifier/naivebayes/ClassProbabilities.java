package pl.mati.machinelearning.classifier.naivebayes;

import pl.mati.machinelearning.data.Cell;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassProbabilities {
    private Map<Cell, Map<Integer, ColumnProbability>> columnProbabilityMap;
    private Map<Cell, AtomicInteger> classCounter;
    public ClassProbabilities() {
        columnProbabilityMap = new HashMap<>();
        classCounter = new HashMap<>();
    }

    public void add(Cell aClass, int column, Object cellValue, boolean discrete){
        increment(aClass);
        Map<Integer, ColumnProbability> colMap = columnProbabilityMap.get(aClass);
        if(colMap == null){
            colMap = new HashMap<>();
            columnProbabilityMap.put(aClass, colMap);
        }
        ColumnProbability columnProbability = colMap.get(column);
        if(columnProbability == null){
            columnProbability = discrete ? new DiscreteColumnProbability() : new ContinuousColumnProbability();
            colMap.put(column, columnProbability);
        }
        columnProbability.add(cellValue);
    }

    private void increment(Cell aClass) {
        AtomicInteger atomicInteger = classCounter.get(aClass);
        if(atomicInteger == null){
            atomicInteger = new AtomicInteger(0);
            classCounter.put(aClass, atomicInteger);
        }
        atomicInteger.incrementAndGet();
    }

    public BayesProbability getProbability(int column, int laplace){
        Map<Object, BayesValue> result = new HashMap<>();
        for (Map.Entry<Cell, Map<Integer, ColumnProbability>> entry : columnProbabilityMap.entrySet()) {
//            for (Map.Entry<Integer, ColumnProbability> entry2 : entry.getValue().entrySet()) {
                ColumnProbability columnProbability = entry.getValue().get(column);
                result.put(entry.getKey().getValue(), createConditionalProbability(columnProbability, laplace));
//            }
        }
        return new BayesProbability(result);
    }

    private BayesValue createConditionalProbability(ColumnProbability columnProbability, int laplace) {
        if(columnProbability instanceof DiscreteColumnProbability){
            return createDiscreteConditionalProbability((DiscreteColumnProbability) columnProbability, laplace);
        } else {
            return createContinuousConditionalProbability((ContinuousColumnProbability) columnProbability);
        }
    }

    private BayesValue createContinuousConditionalProbability(ContinuousColumnProbability columnProbability) {
        return new ContinuousBayesValue(columnProbability.getMean(), columnProbability.getStandardDerivation());
    }

    private BayesValue createDiscreteConditionalProbability(DiscreteColumnProbability column, int laplace) {
        Map<Object, Double> probabilities = new HashMap<>();
        column.addToAll(laplace);
        Set<Object> values =  column.getValues();
        int size = column.getSize();
        for (Object value : values) {
            int counts = column.getValueSize(value);
            probabilities.put(value, 1.*counts / size);
        }
        return new DiscreteBayesValue(probabilities, 1.*laplace/size);
    }
}
