package pl.mati.machinelearning.discretization;


import com.google.common.collect.Range;
import pl.mati.machinelearning.data.DataRow;
import pl.mati.machinelearning.data.DataSet;

import java.util.List;

public interface Discretization {
    public DataSet discretizeAll(DataSet input);
    public DataSet discretizeColumn(DataSet input, int col);
    public DataSet discretizeColumn(DataSet input, int col, List<Range<Double>> ranges);
    public DataRow discretizeRow(DataRow row, int col, List<Range<Double>> ranges);
    public List<Range<Double>> getRanges(DataSet input, int col);
}
