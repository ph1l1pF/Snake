package NeuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Neuron {

    private String label;

    public void setIngoingConnections(List<Connection> ingoingConnections) {
        this.ingoingConnections = ingoingConnections;
    }

    private List<Connection> ingoingConnections = new ArrayList<>();

    private double bias;

    public Neuron(String label) {
        this.label = label;
        this.bias = new Random().nextDouble();
    }


    public abstract double computeOutput();


    public List<Connection> getIngoingConnections() {
        return ingoingConnections;
    }

    public void addIngoingConnection(Connection connection) {
        ingoingConnections.add(connection);
    }

    @Override
    public String toString() {
        return "Neuron " + label;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }
}
