package NeuralNetwork.Neuron;

public class InputNeuron extends Neuron {

    private double input;

    public InputNeuron(String label) {
        super(label);
    }

    @Override
    public double computeOutput() {
        if (input > getBias()) {
            return input;
        } else {
            return 0;
        }
    }

    public void setInput(double input) {
        this.input = input;
    }

}
