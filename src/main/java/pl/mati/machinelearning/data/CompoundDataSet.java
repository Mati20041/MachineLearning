package pl.mati.machinelearning.data;

import java.util.ArrayList;
import java.util.List;

public class CompoundDataSet {
    private DataSet originalSet;
    private DataSet learningSet;
    private DataSet validationSet;
    private DataSet crossValidationSet;
    private int validationFolds;
    private int crossValidationFolds;

    public CompoundDataSet(DataSet inputSet) {
        this.originalSet = inputSet;
        this.learningSet = inputSet;
        this.validationFolds = 0;
        this.crossValidationFolds = 0;
    }

    public void setCrossValidationFolds(int folds, int step) {
        assert folds > 0 && step > 0 && step <= folds;
        this.crossValidationFolds = folds;
        List<DataRow> rows = originalSet.getRows();
        List<DataRow> learningSet = new ArrayList<>(rows.size() - rows.size() / folds);
        List<DataRow> crossValidationSet = new ArrayList<>(rows.size() / folds);
        for (int i = 0; i < rows.size(); i++) {
            if (step == 1) {
                crossValidationSet.add(rows.get(i));
                step = folds;
            } else {
                learningSet.add(rows.get(i));
                --step;
            }
        }
        this.learningSet = new DataSet(originalSet.getColumnMap(), originalSet.getInfoMap(), learningSet);
        this.crossValidationSet = new DataSet(originalSet.getColumnMap(), originalSet.getInfoMap(), crossValidationSet);
        int validationFoldsTemp = this.validationFolds;
        this.validationFolds = 0;
        setValidationFolds(validationFoldsTemp);
    }

    public void setValidationFolds(int folds) {
        if (validationFolds != 0) {
            this.learningSet = this.learningSet.addDataSet(this.validationSet);
        }
        if(folds == 0){
            return;
        }
        this.validationFolds = folds;
        List<DataRow> rows = originalSet.getRows();
        List<DataRow> learningSet = new ArrayList<>(rows.size() - rows.size() / folds);
        List<DataRow> validationSet = new ArrayList<>(rows.size() / folds);
        for (int i = 0; i < rows.size(); i++) {
            if (i != 0 && i % folds == 0) {
                validationSet.add(rows.get(i));
            } else {
                learningSet.add(rows.get(i));
            }
        }
        this.learningSet = new DataSet(originalSet.getColumnMap(), originalSet.getInfoMap(), learningSet);
        this.validationSet = new DataSet(originalSet.getColumnMap(), originalSet.getInfoMap(), validationSet);
    }

    public DataSet getLearningSet() {
        return learningSet;
    }

    public DataSet getValidationSet() {
        return validationSet;
    }

    public DataSet getCrossValidationSet() {
        return crossValidationSet;
    }

    public int getValidationFolds() {
        return validationFolds;
    }

    public int getCrossValidationFolds() {
        return crossValidationFolds;
    }
}
