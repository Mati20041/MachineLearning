package pl.mati.machinelearning.classifier.knn;

import pl.mati.machinelearning.data.Cell;
import pl.mati.machinelearning.data.DataRow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MajorityVotingMethod implements VotingMethod {
    @Override
    public Cell getBest(DataRow vector, List<DataRow> kBests) {
        Map<Cell, AtomicInteger> counter = new HashMap<>();
        for (DataRow row : kBests) {
            AtomicInteger atomicInteger = counter.get(row.getClassCell());
            if (atomicInteger == null) {
                atomicInteger = new AtomicInteger(0);
                counter.put(row.getClassCell(), atomicInteger);
            }
            atomicInteger.incrementAndGet();
        }
        int min = kBests.size() + 1;
        Cell aClass = null;
        for (Map.Entry<Cell, AtomicInteger> entry : counter.entrySet()) {
            int count = entry.getValue().get();
            if(count < min){
                min = count;
                aClass = entry.getKey();
            }
        }
        return aClass;
    }
}
