package pl.mati.machinelearning.classifier.knn;


import pl.mati.machinelearning.classifier.Classifier;
import pl.mati.machinelearning.data.Cell;
import pl.mati.machinelearning.data.DataRow;
import pl.mati.machinelearning.data.DataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KNearestNeighboursClassifier implements Classifier {

    private DataSet learningSet;
    private int k;
    private VotingMethod votingMethod;
    private DistanceMeasure distanceMeasure;

    public KNearestNeighboursClassifier(int k, VotingMethod votingMethod, DistanceMeasure distanceMeasure) {
        this.k = k;
        this.votingMethod = votingMethod;
        this.distanceMeasure = distanceMeasure;
    }

    @Override
    public void train(DataSet learningSet, DataSet validationSet) {
        this.learningSet = learningSet;
    }

    @Override
    public Cell classify(DataRow vector) {
        assert distanceMeasure != null;
        List<DataRow> kBests = getKBests(vector);
        Cell best = votingMethod.getBest(vector, kBests);
        return best;
    }

    private List<DataRow> getKBests(DataRow vector) {
        distanceMeasure.setOriginVector(vector);
        List<DataRow> list = learningSet.getRows()
                .stream()
                .map(r -> new DistanceToRow(distanceMeasure.calculateDistance(vector, r), r))
                .sorted()
                .limit(k)
                .map(DistanceToRow::getRow)
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public void clear() {

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
