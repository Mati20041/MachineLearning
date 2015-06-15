package pl.mati.machinelearning.discretization;


import com.google.common.collect.Range;
import pl.mati.machinelearning.data.*;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractDiscretization implements Discretization {
    private int k;

    public AbstractDiscretization(int k) {
        this.k = k;
    }

    @Override
    public DataSet discretizeAll(DataSet input) {
        for (Map.Entry<Integer, FieldInfo> entry : input.getInfoMap().entrySet()) {
            if(entry.getValue().getFieldType() == FieldType.NUMBER){
                input = discretizeColumn(input, entry.getKey());
            }
        }
        return input;
    }

    @Override
    public DataSet discretizeColumn(DataSet input, int col) {
        List<Range<Double>> ranges = getRanges(input, col);
        return discretizeColumn(input, col, ranges);
    }

    @Override
    public DataSet discretizeColumn(DataSet input, int col, List<Range<Double>> ranges) {
        Set<String> allowedValues = createAllowedValues(ranges.size());
        FieldInfo fieldInfo = new FieldInfo(FieldType.DISCRETED, allowedValues);
        List<DataRow> collect = input.getRows().stream()
                .map(row -> new DataRow(row.getColumnMap(), discretize(row.getCells(), col, ranges, fieldInfo)))
                .collect(Collectors.toList());
        HashMap<Integer, FieldInfo> infoMap = new HashMap<>(input.getInfoMap());
        infoMap.put(col, fieldInfo);
        return new DataSet(input.getColumnMap(), infoMap, collect);
    }

    @Override
    public DataRow discretizeRow(DataRow row, int col, List<Range<Double>> ranges) {
        Set<String> allowedValues = createAllowedValues(ranges.size());
        FieldInfo fieldInfo = new FieldInfo(FieldType.DISCRETED, allowedValues);
        return new DataRow(row.getColumnMap(), discretize(row.getCells(), col, ranges, fieldInfo));
    }

    private Set<String> createAllowedValues(int size) {
        Set<String> allowedValues = new HashSet<>(size);
        for (int i = 0; i < size; i++) {
            allowedValues.add(i + "");
        }
        return allowedValues;
    }

    private List<Cell> discretize(List<Cell> cells, int col, List<Range<Double>> ranges, FieldInfo fieldInfo) {
        ArrayList<Cell> c = new ArrayList<>(cells);
        Cell cell = c.get(col);
        int val = 0;
        for (int i = 0; i < ranges.size(); i++) {
            if (ranges.get(i).contains((Double) cell.getValue())) {
                val = i;
                break;
            }
        }
        c.set(col, new Cell<>(fieldInfo, val + ""));
        return c;
    }

    protected int getK() {
        return k;
    }
}
