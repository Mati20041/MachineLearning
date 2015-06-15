package pl.mati.machinelearning.classifier.knn;

import pl.mati.machinelearning.data.DataRow;

import java.util.Comparator;

public interface DistanceMeasure extends Comparator<DataRow> {
    public double calculateDistance(DataRow row1, DataRow row2);
    public void setOriginVector(DataRow vector);
}
