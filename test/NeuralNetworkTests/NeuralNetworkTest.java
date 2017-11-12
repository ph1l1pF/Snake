package NeuralNetworkTests;

import Evolution.CrossOver.ChromosomeCrossOver;
import Evolution.Evolution;
import NeuralNetwork.*;
import NeuralNetwork.Neuron.HiddenNeuron;
import NeuralNetwork.Neuron.InputNeuron;
import NeuralNetwork.Neuron.Neuron;
import NeuralNetwork.Neuron.OutputNeuron;
import NeuralNetwork.visuals.NetworkVisualization;
import org.junit.Assert;
import org.junit.Test;

public class NeuralNetworkTest {

    @Test
    public void setUp() throws Exception {
    }

    @Test
    public void getNeurons() throws Exception {
    }

    @Test
    public void setNeurons() throws Exception {
    }

    @Test
    public void computeOutputs() throws Exception {
        Neuron n = new InputNeuron("in");
        Neuron nOut = new OutputNeuron("out");
        Neuron[] hiddens = new Neuron[10];
        for(int i = 0;i<hiddens.length;i++){
            hiddens[i] = new HiddenNeuron("");
        }
        NeuralNetwork network = new NeuralNetwork(new Neuron[][]{{n}, hiddens,{nOut}});

        network.generateFullmesh();
        double output = network.computeOutputs(0)[0];

        System.out.println(output);
    }

    @Test
    public void generateFullmesh() throws Exception {
    }

    @Test
    public void deepCopy() {
        NeuralNetwork network = Evolution.getInstance().generateRandomFullmeshNeuralNetwork();
        new NetworkVisualization(network, "original");
        new NetworkVisualization(network.deepCopy(), "copy");

        while (true) {
        }
    }

    @Test
    public void getConnections() throws Exception {
    }

    @Test
    public void visualize() {
        NeuralNetwork network = Evolution.getInstance().generateRandomFullmeshNeuralNetwork();
        new NetworkVisualization(network, "net");

        while (true) {

        }
    }

    @Test
    public void equals() {
        NeuralNetwork net = Evolution.getInstance().generateRandomFullmeshNeuralNetwork();
        NeuralNetwork net2 = net.deepCopy();

        Assert.assertTrue(net.equals(net2));

        net2 = Evolution.getInstance().generateRandomFullmeshNeuralNetwork();

        Assert.assertFalse(net.equals(net2));

        NeuralNetwork childNet = new ChromosomeCrossOver().crossOver(net, net2);

        Assert.assertFalse(net.equals(childNet));
        Assert.assertFalse(net2.equals(childNet));
    }
}
