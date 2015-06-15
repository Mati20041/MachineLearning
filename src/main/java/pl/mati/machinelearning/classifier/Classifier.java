package pl.mati.machinelearning.classifier;

import pl.mati.machinelearning.data.Cell;
import pl.mati.machinelearning.data.DataRow;
import pl.mati.machinelearning.data.DataSet;

public interface Classifier extends Cloneable{
    public void train(DataSet learningSet, DataSet validationSet);
    public Cell classify(DataRow learningSet);
    public Object clone() throws CloneNotSupportedException;
    public void clear();
}
