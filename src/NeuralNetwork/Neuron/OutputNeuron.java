package NeuralNetwork.Neuron;

public class OutputNeuron extends Neuron {


    public OutputNeuron(String label) {
        super(label);
        setActivatorFunction(new IdentityActivatorFunction());
    }

}
