package pl.mati.machinelearning.validator;


public class Rate {
    private double TP;
    private double TN;
    private double FP;
    private double FN;
    private int count;

    public Rate(double TP, double TN, double FP, double FN) {
        this.TP = TP;
        this.TN = TN;
        this.FP = FP;
        this.FN = FN;
        this.count = (int) Math.ceil(TP + TN + FP + FN);
    }
    public Rate() {
        this.TP = 0;
        this.TN = 0;
        this.FP = 0;
        this.FN = 0;
        this.count = 0;
    }

    public void falsePositive() {
        falsePositive(1);
    }

    public void falseNegative() {
        falseNegative(1);
    }

    public void truePositive() {
        truePositive(1);
    }

    public void trueNegative() {
        trueNegative(1);
    }

    public void falsePositive(int i) {
        FP += i;
        count += i;
    }

    public void falseNegative(int i) {
        FN += i;
        count += i;
    }

    public void truePositive(int i) {
        TP += i;
        count += i;
    }

    public void trueNegative(int i) {
        TN += i;
        count += i;
    }

    public double getFalsePositive() {
        return FP;
    }

    public double getTruePositive() {
        return TP;
    }

    public double getFalseNegative() {
        return FN;
    }

    public double getTrueNegative() {
        return TN;
    }

    public int getCount() {
        return count;
    }

    public double precision() {
        return TP != 0 ? TP / ( TP + FP) : 0;
    }

    public double recall() {
        return TP != 0 ? TP / (TP + FN) : 0;
    }

    public double accuracy() {
        double v = TP + TN;
        return v != 0 ?  v / (TP + TN + FP + FN) : 0;
    }

    public double fScore() {
        double v = precision() * recall();
        return v != 0 ? (2.0 * v) / (precision() + recall()) : 0;
    }

    public void clear() {
        TP = 0;
        FP = 0;
        TN = 0;
        FN = 0;
        count = 0;
    }

}
