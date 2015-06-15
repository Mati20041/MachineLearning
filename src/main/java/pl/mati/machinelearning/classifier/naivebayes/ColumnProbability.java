package pl.mati.machinelearning.classifier.naivebayes;

public abstract class ColumnProbability {
    private int size;

    public ColumnProbability() {
        this.size = 0;
    }


    public void add(Object value){
        ++size;
        doAdd(value);
    }

    protected abstract void doAdd(Object value);

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
