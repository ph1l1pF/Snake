package NeuralNetwork;

public class Connection {

    private Neuron startNeuron;
    private Neuron endNeuron;
    private double weight;

    public Connection(Neuron startNeuron, Neuron endNeuron, double weight) {
        this.startNeuron = startNeuron;
        this.endNeuron = endNeuron;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "(" + startNeuron.toString() + ", " + endNeuron.toString() + ", " + weight + ")";
    }

    public Neuron getStartNeuron() {
        return startNeuron;
    }

    public Neuron getEndNeuron() {
        return endNeuron;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
