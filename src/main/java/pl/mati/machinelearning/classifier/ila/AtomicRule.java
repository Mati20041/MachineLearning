package pl.mati.machinelearning.classifier.ila;

public class AtomicRule {
    private int index;
    private String equals;

    public AtomicRule(int index, String equals) {
        this.index = index;
        this.equals = equals;
    }

    public int getIndex() {
        return index;
    }

    public String getEquals() {
        return equals;
    }
}
