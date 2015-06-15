package pl.mati.machinelearning.classifier.knn;

import pl.mati.machinelearning.data.DataRow;

public class DistanceToRow implements Comparable<DistanceToRow>{
    private final double distance;
    private final DataRow row;

    public DistanceToRow(double distance, DataRow row) {
        this.distance = distance;
        this.row = row;
    }

    @Override
    public int compareTo(DistanceToRow o) {
        return (int) Math.signum(distance - o.distance);
    }

    public double getDistance() {
        return distance;
    }

    public DataRow getRow() {
        return row;
    }
}
