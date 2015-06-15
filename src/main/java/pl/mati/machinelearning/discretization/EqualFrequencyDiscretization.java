package pl.mati.machinelearning.discretization;


import com.google.common.collect.Range;
import pl.mati.machinelearning.data.*;

import java.util.*;
import java.util.stream.Collectors;

public class EqualFrequencyDiscretization extends AbstractDiscretization {

    public EqualFrequencyDiscretization(int k) {
        super(k);
    }

    @Override
    public List<Range<Double>> getRanges(DataSet input, int col) {
        DataCol column = input.getCol(col);
        assert column.getFieldInfo().getFieldType() == FieldType.NUMBER;

        double[] doubles = column.list().parallelStream()
                .mapToDouble(c -> ((Double) c.getValue()))
                .sequential()
                .sorted()
                .parallel()
                .toArray();
        int size = doubles.length / getK();
        List<Range<Double>> ranges = new ArrayList<>();
        for (int i = 0; i < getK(); i++) {
            Range<Double> r;
            if(i == 0){
                r = Range.closedOpen(Double.NEGATIVE_INFINITY, doubles[Math.min((i + 1) * size, doubles.length -1)]);
            } else if (i == getK() - 1) {
                r = Range.closed(doubles[i * size], Double.POSITIVE_INFINITY);
            } else {
                r = Range.closedOpen(doubles[i * size], doubles[Math.min((i + 1) * size, doubles.length - 1)]);
            }
            ranges.add(r);
        }
        return ranges;
    }
}
