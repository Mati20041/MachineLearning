package pl.mati.machinelearning.discretization;


import com.google.common.collect.Range;
import pl.mati.machinelearning.data.*;

import java.util.*;
import java.util.stream.Collectors;

public class EqualWidthDiscretization extends AbstractDiscretization {


    public EqualWidthDiscretization(int k) {
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
                .toArray();
        double size = (doubles[doubles.length - 1] - doubles[0]) / getK();
        List<Range<Double>> ranges = new ArrayList<>();
        for (int i = 0; i < getK(); i++) {
            Range<Double> r;
            if (i == getK() - 1) {
                r = Range.closed(doubles[0] + i * size, Double.POSITIVE_INFINITY);
            } else if (i == 0) {
                r = Range.closedOpen(Double.NEGATIVE_INFINITY,  doubles[0] + (i + 1) * size);
            } else {
                r = Range.closedOpen(doubles[0] + i * size, doubles[0] + (i + 1) * size);
            }
            ranges.add(r);
        }
        return ranges;
    }
}
