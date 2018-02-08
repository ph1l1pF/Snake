package EvolutionTests;

import Evolution.CrossOver.ChromosomeCrossOver;
import Evolution.Evolution;
import Evolution.Mutation.MutationImpl;
import NeuralNetwork.NeuralNetwork;
import NeuralNetwork.Neuron.HiddenNeuron;
import NeuralNetwork.Neuron.InputNeuron;
import NeuralNetwork.Neuron.Neuron;
import NeuralNetwork.Neuron.OutputNeuron;
import NeuralNetwork.visuals.NetworkVisualization;
import org.junit.Before;
import org.junit.Test;

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
    public void mutate() {
        NeuralNetwork network = Evolution.getInstance().generateRandomFullmeshNeuralNetwork();

        new NetworkVisualization(network, "before");
        new NetworkVisualization(new MutationImpl().mutate(network), "after");

        while (true) {
        }
    }

    @Test
    public void crossOver() {
        NeuralNetwork net1 = Evolution.getInstance().generateRandomFullmeshNeuralNetwork();
        NeuralNetwork net2 = Evolution.getInstance().generateRandomFullmeshNeuralNetwork();

        NeuralNetwork child = new ChromosomeCrossOver().crossOver(net1, net2);

        System.out.println(net1.toString() + "\n ----------------");
        System.out.println(net2.toString() + "\n ----------------");
        System.out.println(child.toString() + "\n ----------------");
    }

}