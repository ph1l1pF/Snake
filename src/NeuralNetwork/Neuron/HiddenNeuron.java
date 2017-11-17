package NeuralNetwork.Neuron;

public class HiddenNeuron extends Neuron {

    public HiddenNeuron(String label) {
        super(label);
        setActivatorFunction(new IdentityActivatorFunction());
    }

}
