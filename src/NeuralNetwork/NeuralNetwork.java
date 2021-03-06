package NeuralNetwork;


import NeuralNetwork.Neuron.HiddenNeuron;
import NeuralNetwork.Neuron.InputNeuron;
import NeuralNetwork.Neuron.Neuron;
import NeuralNetwork.Neuron.OutputNeuron;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork implements Serializable {

    private Neuron[][] neurons;
    private List<Connection> connections = new ArrayList<>();

    public NeuralNetwork(Neuron[][] neurons) {
        this.neurons = neurons;
    }

    public Neuron[][] getNeurons() {
        return neurons;
    }

    public void setNeurons(Neuron[][] neurons) {
        this.neurons = neurons;
    }

    public double[] computeOutputs(double... inputValues) {

        for (int i = 0; i < neurons[0].length; i++) {
            neurons[0][i].setValue(inputValues[i]);
        }

        double[] output = new double[neurons[neurons.length - 1].length];

        for (int i = 1; i < neurons.length; i++) {
            for (int k = 0; k < neurons[i].length; k++) {
                Neuron currentNeuron = neurons[i][k];
                for (Connection connection : currentNeuron.getIngoingConnections()) {
                    Neuron neuronIngoing = connection.getStartNeuron();

                    if (neuronIngoing.getValue() > neuronIngoing.getBias()) {
                        currentNeuron.setValue(currentNeuron.getValue() +
                                neuronIngoing.getValue() * connection.getWeight());
                    }
                }

                if (i == neurons.length - 1) {
                    if (currentNeuron.getValue() > currentNeuron.getBias()) {
                        output[k] = currentNeuron.getValue();
                    }
                }
            }
        }

        for (int i = 0; i < neurons.length; i++) {
            for (int k = 0; k < neurons[i].length; k++) {
                neurons[i][k].setValue(0);
            }
        }

        return output;
    }

    public void generateFullmesh() {

        for (int i = 1; i < neurons.length; i++) {
            for (int k = 0; k < neurons[i].length; k++) {
                for (int l = 0; l < neurons[i - 1].length; l++) {
                    Connection con = new Connection(neurons[i - 1][l], neurons[i][k]);
                    neurons[i][k].getIngoingConnections().add(con);
                    connections.add(con);
                }
            }
        }
    }

    public String toStringConnections() {
        String result = "";
        for (int i = 1; i < neurons.length; i++) {
            for (int k = 0; k < neurons[i].length; k++) {
                for (Connection connection : neurons[i][k].getIngoingConnections()) {
                    result += connection.toString() + "\n";
                }
            }
        }
        return result;
    }

    public String toStringBiases() {
        String result = "\n\n Neurons with biases: \n";

        for (int i = 0; i < neurons.length; i++) {
            for (int k = 0; k < neurons[i].length; k++) {
                result += neurons[i][k].toString() + ": " + neurons[i][k].getBias() + "\n";
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return toStringConnections() + toStringBiases();


    }

    public NeuralNetwork deepCopy() {
        NeuralNetwork copy = new NeuralNetwork(new Neuron[getNeurons().length][getNeurons()[0].length]);
        for (int i = 0; i < neurons.length; i++) {
            copy.neurons[i] = new Neuron[neurons[i].length];
        }

        for (int i = 0; i < getNeurons().length; i++) {
            for (int k = 0; k < getNeurons()[i].length; k++) {
                Neuron originalNeuron = getNeurons()[i][k];
                if (originalNeuron instanceof InputNeuron) {
                    copy.getNeurons()[i][k] = new InputNeuron(originalNeuron.getLabel());
                } else if (originalNeuron instanceof HiddenNeuron) {
                    copy.getNeurons()[i][k] = new HiddenNeuron(originalNeuron.getLabel());
                } else if (originalNeuron instanceof OutputNeuron) {
                    copy.getNeurons()[i][k] = new OutputNeuron(originalNeuron.getLabel());
                }
                copy.getNeurons()[i][k].setBias(originalNeuron.getBias());


            }
        }

        for (Connection connection : connections) {
            Point start = null, end = null;
            Connection currentCon = null;
            for (int i = 0; i < getNeurons().length; i++) {
                for (int k = 0; k < getNeurons()[i].length; k++) {
                    if (connection.getStartNeuron().equals(getNeurons()[i][k])) {
                        start = new Point(i, k);
                    } else if (connection.getEndNeuron().equals(getNeurons()[i][k])) {
                        end = new Point(i, k);
                        currentCon = connection;
                    }
                }
            }
            Connection copyCon = null;
            try {
                copyCon = new Connection(copy.neurons[start.x][start.y], copy.neurons[end.x][end.y], currentCon.getWeight());
            } catch (NullPointerException e) {
                System.out.println();
            }
            copy.neurons[end.x][end.y].addIngoingConnection(copyCon);
            copy.connections.add(copyCon);
        }

        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NeuralNetwork) {
            return equalNeurons((NeuralNetwork) obj) && equalConnections((NeuralNetwork) obj);
        }

        return false;
    }

    public boolean equalNeurons(NeuralNetwork otherNet) {
        boolean equal = true;
        for (int i = 0; i < getNeurons().length; i++) {
            for (int k = 0; k < getNeurons()[i].length; k++) {
                if (!neurons[i][k].equals(otherNet.getNeurons()[i][k])) {
                    equal = false;
                }

            }

        }
        return equal;
    }

    public boolean equalConnections(NeuralNetwork otherNet) {
        boolean equal = true;
        for (int i = 0; i < getNeurons().length; i++) {
            for (int k = 0; k < getNeurons()[i].length; k++) {

                for (int m = 0; m < neurons[i][k].getIngoingConnections().size(); m++) {
                    if (neurons[i][k].getIngoingConnections().get(m).getWeight() !=
                            otherNet.getNeurons()[i][k].getIngoingConnections().get(m).getWeight()) {
                        equal = false;
                    }
                }

            }

        }
        return equal;
    }

    public List<Connection> getConnections() {
        return connections;
    }
}
