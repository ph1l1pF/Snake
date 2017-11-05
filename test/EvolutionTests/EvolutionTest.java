package EvolutionTests;

import Evolution.CrossOver.ChromosomeCrossOver;
import Evolution.Evolution;
import NeuralNetwork.*;
import NeuralNetwork.visuals.NetworkVisualization;
import org.junit.Before;
import org.testng.annotations.Test;

public class EvolutionTest {
    @Before
    public void setUp() throws Exception {
    }

    private NeuralNetwork createNetwork() {
        Neuron n = new InputNeuron("in");
        Neuron nOut = new OutputNeuron("out");
        Neuron[] hiddens = new Neuron[10];
        for (int i = 0; i < hiddens.length; i++) {
            hiddens[i] = new HiddenNeuron("hidden " + i);
        }
        NeuralNetwork network = new NeuralNetwork(new Neuron[][]{{n}, hiddens, {nOut}});

        network.generateFullmesh();
        return network;
    }

    @Test
    public void crossOver() {
        NeuralNetwork net1 = Evolution.getInstance().generateRandomFullmeshNeuralNetwork();
        NeuralNetwork net2 = Evolution.getInstance().generateRandomFullmeshNeuralNetwork();

        NeuralNetwork child = new ChromosomeCrossOver().crossOver(net1, net2);
        new NetworkVisualization(net1, "net1");
        new NetworkVisualization(net2, "net2");
        new NetworkVisualization(child, "child");
    }

}