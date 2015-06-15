package pl.mati.machinelearning.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DataSet {
    public static final DataSet EMPTY = new DataSet(Collections.emptyMap(), Collections.emptyMap(), Collections.emptyList());
    private BiMap<String, Integer> columnMap;
    private Map<Integer, FieldInfo> infoMap;
    private List<DataRow> rows;

    public DataSet(Map<String, Integer> columnMap, Map<Integer, FieldInfo> infoMap, List<DataRow> rows) {
        this.columnMap = HashBiMap.create(columnMap);
        this.infoMap = infoMap;
        this.rows = new ArrayList<>(new HashSet<>(rows));
    }

    public DataRow getRow(int row){
        return rows.get(row);
    }

    public List<DataRow> getRows() {
        return Collections.unmodifiableList(rows);
    }

    public DataCol getCol(int col){
        return createColumnFrom(rows, col);
    }

    private DataCol createColumnFrom(List<DataRow> data, final int col) {
        List<Cell> cells = data.stream().map(d -> d.getCell(col)).collect(Collectors.toList());
        return new DataCol(columnMap.inverse().get(col), infoMap.get(col), cells);
    }

    public Cell getCell(int row, int col){
        return rows.get(row).getCell(col);
    }
    public int getIndexOfColumn(String colName){
        return columnMap.get(colName);
    }
    public DataCol getCol(String colName){
        return getCol(getIndexOfColumn(colName));
    }

    public DataSet filter(Predicate<DataSet> filter){
        return null;
    }

    public int size() {
        return rows.size();
    }
    public int colSize() {
        return columnMap.size();
    }

    public DataSet addDataSet(DataSet dataSet){
        List<DataRow> rows = new ArrayList<>(this.rows);
        rows.addAll(dataSet.getRows());
        return new DataSet(columnMap, infoMap, rows);
    }

    public String getColumnName(int col){
        return columnMap.inverse().get(col);
    }

    public Map<String, Integer> getColumnMap() {
        return Collections.unmodifiableMap(columnMap);
    }

    public Map<Integer, FieldInfo> getInfoMap() {
        return Collections.unmodifiableMap(infoMap);
    }
}
