package pl.mati.machinelearning.data;

import java.util.Collections;
import java.util.List;

public class DataCol {
    private String name;
    private List<Cell> cells;
    private FieldInfo fieldInfo;

    public DataCol(String name, FieldInfo fieldInfo, List<Cell> cells) {
        this.name = name;
        this.cells = cells;
        this.fieldInfo = fieldInfo;
    }

    public Cell getCell(int row){
        return cells.get(row);
    }

    public String getName() {
        return name;
    }

    public int size(){
        return cells.size();
    }

    public List<Cell> list(){
        return Collections.unmodifiableList(cells);
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }
}
