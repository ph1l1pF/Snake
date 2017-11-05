package NeuralNetwork;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NeuralNetwork implements Cloneable {

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

    public double[] computeOutputs(int... inputValues) {

        for (int i = 0; i < neurons[0].length; i++) {
            ((InputNeuron) neurons[0][i]).setInput(inputValues[i]);
        }

        double[] output = new double[neurons[neurons.length - 1].length];

        for (int i = 0; i < neurons[neurons.length - 1].length; i++) {
            output[i] = neurons[neurons.length - 1][i].computeOutput();
        }

        return output;
    }

    public void generateFullmesh() {

        for (int i = neurons.length - 1; i > 0; i--) {
            List<Point> lstTuplesVisited = new ArrayList<>();
            for (int j = 0; j < neurons[i].length; j++) {
                for (int k = 0; k < neurons[i - 1].length; k++) {

                    if (lstTuplesVisited.contains(new
                            Point(j, k))) {
                        continue;
                    } else {
                        lstTuplesVisited.add(new Point(k, j));
                    }


                    Connection con = new Connection(neurons[i - 1][k], neurons[i][j], new Random().nextDouble());
                    neurons[i][j].addIngoingConnection(con);
                    connections.add(con);

                }
            }
        }
    }

    public List<Connection> getConnections() {
        return connections;
    }
}
