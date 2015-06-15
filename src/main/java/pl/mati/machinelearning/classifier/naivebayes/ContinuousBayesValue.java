package pl.mati.machinelearning.classifier.naivebayes;

public class ContinuousBayesValue implements BayesValue<Double> {
    private static final double SQRT = Math.sqrt(2 * Math.PI);
    private final double mean;
    private final double variance;

    public ContinuousBayesValue(double mean, double variance) {
        this.mean = mean;
        this.variance = variance;
    }

    @Override
    public double getConditionalProbability(Double value) {
        double meanDistance = value - mean;
        return 1/(variance * SQRT)*Math.exp(-(meanDistance * meanDistance)/(2*variance*variance));
    }

    public double getMean() {
        return mean;
    }

    public double getVariance() {
        return variance;
    }
}
