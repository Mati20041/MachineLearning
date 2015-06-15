package pl.mati.machinelearning.data;

import org.apache.commons.math3.ml.neuralnet.MapUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DataRow {
    private Map<String, Integer> columnMap;
    private List<Cell> cells;
    private Cell classCell;

    public DataRow(Map<String, Integer> columnMap, List<Cell> cells) {
        this.columnMap = columnMap;
        this.cells = cells;
        this.classCell = cells.stream().filter(c -> c.getInfo().getFieldType() == FieldType.CLASS).findAny().get();
    }

    public Cell getCell(int col){
        return cells.get(col);
    }
    public Cell getCell(String colName){
        return cells.get(getColumnIndex(colName));
    }

    public int getColumnIndex(String colName){
        return columnMap.get(colName);
    }

    public int size(){
        return cells.size();
    }

    public List<Cell> list(){
        return Collections.unmodifiableList(cells);
    }

    public List<Cell> getCells(){
        return Collections.unmodifiableList(cells);
    }

    public Map<String, Integer> getColumnMap() {
        return columnMap;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DataRow){
            for (int i = 0; i < cells.size(); i++) {
                if(!cells.get(i).equals(((DataRow) obj).cells.get(i))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Cell getClassCell() {
        return classCell;
    }
}
