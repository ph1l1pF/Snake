package NeuralNetworkTests;

import NeuralNetwork.*;
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
    public void getConnections() throws Exception {
    }

}