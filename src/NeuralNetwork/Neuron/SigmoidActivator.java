package NeuralNetwork.Neuron;

import java.io.Serializable;

public class SigmoidActivator implements IActivatorFunction, Serializable {
    @Override
    public double activate(double input) {
        double e = Math.exp(input);
        return e / (1.0 + e);
    }
}
