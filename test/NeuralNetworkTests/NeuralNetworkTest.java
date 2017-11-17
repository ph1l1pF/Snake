package NeuralNetworkTests;

import Evolution.CrossOver.ChromosomeCrossOver;
import Evolution.Evolution;
import NeuralNetwork.NeuralNetwork;
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

        /*NeuralNetwork network = Evolution.getInstance().generateRandomFullmeshNeuralNetwork();
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("net.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(network);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data");
        } catch (IOException i) {
            i.printStackTrace();
        }

        NeuralNetwork net = null;
        try {
            FileInputStream fileIn = new FileInputStream("net.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            net = (NeuralNetwork) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            return;
        }*/

        NeuralNetwork net = Evolution.getInstance().generateRandomFullmeshNeuralNetwork();

        System.out.println(net);

        double output = net.computeOutputs(-1, 0, 0, -1, 1.0 / 20)[0];

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
