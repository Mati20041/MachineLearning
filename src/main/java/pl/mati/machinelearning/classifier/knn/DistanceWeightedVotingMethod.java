package pl.mati.machinelearning.classifier.knn;

import com.google.common.util.concurrent.AtomicDouble;
import pl.mati.machinelearning.data.Cell;
import pl.mati.machinelearning.data.DataRow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DistanceWeightedVotingMethod implements VotingMethod {
    private DistanceMeasure measure;

    public DistanceWeightedVotingMethod(DistanceMeasure measure) {
        this.measure = measure;
    }

    @Override
    public Cell getBest(DataRow vector, List<DataRow> kBests) {
        Map<Cell, AtomicDouble> counter = new HashMap<>();
        double distSum = 0;
        for (DataRow row : kBests) {
            distSum += Math.exp(-measure.calculateDistance(vector, row));
        }
        for (DataRow row : kBests) {
            AtomicDouble atomicDouble = counter.get(row.getClassCell());
            if (atomicDouble == null) {
                atomicDouble = new AtomicDouble(0);
                counter.put(row.getClassCell(), atomicDouble);
            }
            atomicDouble.addAndGet(calculateWeight(vector, row, distSum));
        }
        double max = -1;
        Cell aClass = null;
        for (Map.Entry<Cell, AtomicDouble> entry : counter.entrySet()) {
            double weight = entry.getValue().get();
            if (weight > max) {
                max = weight;
                aClass = entry.getKey();
            }
        }
        return aClass;
    }

    private double calculateWeight(DataRow vector, DataRow dataRow, double distSum) {
        double d = measure.calculateDistance(vector, dataRow);
        return Math.exp(-d) / distSum;
    }
}
