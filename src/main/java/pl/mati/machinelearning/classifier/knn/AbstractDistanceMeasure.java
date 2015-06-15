package pl.mati.machinelearning.classifier.knn;

import pl.mati.machinelearning.data.DataRow;


public abstract class AbstractDistanceMeasure implements DistanceMeasure {
    private DataRow origin;

    @Override
    public abstract double calculateDistance(DataRow row1, DataRow row2);

    @Override
    public void setOriginVector(DataRow vector) {
        this.origin = vector;
    }

    @Override
    public int compare(DataRow o1, DataRow o2) {
        double v1 = calculateDistance(origin, o2);
        double v2 = calculateDistance(origin, o1);

        int signum = (int) Math.signum(v2 - v1);
        int signum2 = (int) Math.signum(v1 - v2);
        if(signum != -signum2){
            System.out.println();
        }
        return signum;
    }
}
