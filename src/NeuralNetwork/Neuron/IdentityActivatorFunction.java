package NeuralNetwork.Neuron;

import java.io.Serializable;

public class IdentityActivatorFunction implements IActivatorFunction, Serializable {
    @Override
    public double activate(double input) {
        return input;
    }
}
