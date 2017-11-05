package NeuralNetwork;

public class HiddenNeuron extends Neuron {

    public double computeOutput() {
        double sum = 0;
        for (Connection inCon : getIngoingConnections()) {
            Neuron in = inCon.getStartNeuron();
            if (in.computeOutput() > in.getBias()) {
                sum += in.computeOutput() * inCon.getWeight();
            }
        }
        return manipulate(sum);
    }

    public HiddenNeuron(String label) {
        super(label);
    }

    public double manipulate(double value) {
        // e^x/(1+e^x)
        double e = Math.exp(value);
        return e / (1 + e);
    }
}
