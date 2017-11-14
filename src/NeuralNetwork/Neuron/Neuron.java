package NeuralNetwork.Neuron;

import NeuralNetwork.Connection;
import NeuralNetwork.MathUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Neuron implements Serializable {

    private String label;
    private IActivatorFunction activatorFunction;
    private List<Connection> ingoingConnections = new ArrayList<>();
    private double bias;

    public Neuron(String label) {
        this.label = label;
        this.bias = MathUtil.randomMinusOneToOne();
    }

    public abstract double computeOutput();

    public List<Connection> getIngoingConnections() {
        return ingoingConnections;
    }

    public void setIngoingConnections(List<Connection> ingoingConnections) {
        this.ingoingConnections = ingoingConnections;
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

    public String getLabel() {
        return label;
    }

    public IActivatorFunction getActivatorFunction() {
        return activatorFunction;
    }

    public void setActivatorFunction(IActivatorFunction activatorFunction) {
        this.activatorFunction = activatorFunction;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Neuron) {

            return bias == ((Neuron) obj).bias;
        }
        return false;
    }
}
