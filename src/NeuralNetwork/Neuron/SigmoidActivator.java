package NeuralNetwork.Neuron;

public class SigmoidActivator implements IActivatorFunction {
    @Override
    public double activate(double input) {
        double e = Math.exp(input);
        return e / (1.0 + e);
    }
}
