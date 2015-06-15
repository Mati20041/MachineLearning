package pl.mati.machinelearning.classifier.naivebayes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class DiscreteColumnProbability extends ColumnProbability{
    private Map<Object, AtomicInteger> counter = new HashMap<>();
    private int laplace = 0;

    @Override
    protected void doAdd(Object value) {
        AtomicInteger atomicInteger = counter.get(value);
        if (atomicInteger == null) {
            atomicInteger = new AtomicInteger(0);
            counter.put(value, atomicInteger);
        }
        atomicInteger.incrementAndGet();
    }

    public Set<Object> getValues() {
        return counter.keySet();
    }

    public int getValueSize(Object value) {
        return counter.get(value).get();
    }

    public void addToAll(int laplace) {
        if(laplace != 0){
            for (AtomicInteger atomicInteger : counter.values()) {
                atomicInteger.addAndGet(laplace);
                setSize(getSize() + laplace);
            }
        }
    }

    public int getLaplace() {
        return laplace;
    }
}
