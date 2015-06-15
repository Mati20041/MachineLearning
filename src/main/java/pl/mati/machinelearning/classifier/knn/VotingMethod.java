package pl.mati.machinelearning.classifier.knn;

import pl.mati.machinelearning.data.Cell;
import pl.mati.machinelearning.data.DataRow;

import java.util.Comparator;
import java.util.List;

public interface VotingMethod {
    public Cell getBest(DataRow vector, List<DataRow> kBests);
}
