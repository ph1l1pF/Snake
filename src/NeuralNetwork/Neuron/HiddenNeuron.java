package NeuralNetwork.Neuron;

import NeuralNetwork.Connection;

public class HiddenNeuron extends Neuron {

    public double computeOutput() {
        double sum = 0;
        for (Connection inCon : getIngoingConnections()) {
            Neuron in = inCon.getStartNeuron();
            if (in.computeOutput() > in.getBias()) {
                sum += in.computeOutput() * inCon.getWeight();
            }
        }
        return getActivatorFunction().activate(sum);
    }

    public HiddenNeuron(String label) {
        super(label);
        setActivatorFunction(new IdentityActivatorFunction());
    }

}
