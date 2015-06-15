package pl.mati.machinelearning.classifier.naivebayes;

public interface BayesValue<T> {
    double getConditionalProbability(T value);
}
