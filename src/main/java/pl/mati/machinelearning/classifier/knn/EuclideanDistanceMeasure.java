package pl.mati.machinelearning.classifier.knn;

import pl.mati.machinelearning.data.Cell;
import pl.mati.machinelearning.data.DataRow;
import pl.mati.machinelearning.data.FieldType;

import java.util.List;

public class EuclideanDistanceMeasure extends AbstractDistanceMeasure {
    @Override
    public double calculateDistance(DataRow row1, DataRow row2) {
        List<Cell> cells1 = row1.getCells();
        List<Cell> cells2 = row2.getCells();
        double distance = 0;
        for (int i = 0; i < cells1.size(); i++) {
            Cell cell1 = cells1.get(i);
            Cell cell2 = cells2.get(i);
            if(cell1.getInfo().getFieldType() != FieldType.NUMBER ||
                    cell2.getInfo().getFieldType() != FieldType.NUMBER) {
                continue;
            }
            double v = ((Number) cell1.getValue()).doubleValue() - ((Number) cell2.getValue()).doubleValue();
            distance += v * v;
        }
        return Math.sqrt(distance);
    }
}
