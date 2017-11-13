package NeuralNetwork;

import NeuralNetwork.Neuron.Neuron;

import java.util.Random;

public class Connection {

    private Neuron startNeuron;
    private Neuron endNeuron;
    private double weight;

    public Connection(Neuron startNeuron, Neuron endNeuron, double weight) {
        this.startNeuron = startNeuron;
        this.endNeuron = endNeuron;
        this.weight = weight;
    }

    public Connection(Neuron startNeuron, Neuron endNeuron) {
        this(startNeuron, endNeuron, randomWeight());
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Connection) {
            Connection otherCon = (Connection) obj;
            return startNeuron.equals(otherCon.startNeuron) && endNeuron.equals(otherCon.endNeuron) && weight == otherCon.getWeight();
        }
        return false;
    }

    public static double randomWeight() {
        Random random = new Random();
        double d = random.nextDouble();
        if (random.nextBoolean()) {
            d *= -1;
        }
        return d;
    }
}
