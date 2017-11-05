package EvolutionTests;

import Evolution.Evolution;
import NeuralNetwork.*;
import org.junit.Assert;
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


        NeuralNetwork netChild = Evolution.getInstance().crossOver(net1, net2);
        boolean equal = true;
        for (int i = 1; i < net1.getNeurons().length; i++) {
            for (int k = 1; k < net1.getNeurons()[i].length; k++) {
                for (int m = 0; m < net1.getNeurons()[i][k].getIngoingConnections().size(); m++) {
                    double wchild = netChild.getNeurons()[i][k].getIngoingConnections().get(m).getWeight();
                    double w1 = net1.getNeurons()[i][k].getIngoingConnections().get(m).getWeight();
                    double w2 = net2.getNeurons()[i][k].getIngoingConnections().get(m).getWeight();

                    if (wchild != (w1 + w2) / 2.0) {
                        equal = false;
                    }
                }
            }
        }

        Assert.assertTrue(equal);
    }

}