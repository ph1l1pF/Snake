package NeuralNetwork.visuals;

import NeuralNetwork.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

public class NetworkVisualization extends JFrame {


    private NeuralNetwork neuralNetwork;

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(Color.gray);
        final BasicStroke stroke = new BasicStroke(2.0f);
        g2.setStroke(stroke);

        Map<Neuron, Point> map = new HashMap<>();

        for (int i = 0; i < neuralNetwork.getNeurons().length; i++) {
            for (int k = 0; k < neuralNetwork.getNeurons()[i].length; k++) {

                int offset = 100;
                if (i == 0 || i == neuralNetwork.getNeurons().length - 1) {
                    offset = 200;
                }

                int x = i * (10 + 300) + 100;
                int y = k * (10 + 60) + offset;
                g2.draw(new Ellipse2D.Double(x, y, 10, 10));
                map.put(neuralNetwork.getNeurons()[i][k], new Point(x, y + 10));
            }
        }

        for (Connection con : neuralNetwork.getConnections()) {
            g2.drawLine(map.get(con.getStartNeuron()).x, map.get(con.getStartNeuron()).y,
                    map.get(con.getEndNeuron()).x, map.get(con.getEndNeuron()).y);
        }

    }

    public NetworkVisualization(NeuralNetwork network, String title) {
        super(title);
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        neuralNetwork = network;
        setVisible(true);
    }

    public static void main(String[] args) {
        Neuron in1 = new InputNeuron("left obstacle");
        Neuron in2 = new InputNeuron("right obstacle");
        Neuron in3 = new InputNeuron("front obstacle");

        Neuron[] hiddenNeurons = new Neuron[10];
        for (int i = 0; i < hiddenNeurons.length; i++) {
            hiddenNeurons[i] = new HiddenNeuron("hidden " + i);
        }

        Neuron outputNeuron = new OutputNeuron("output");

        Neuron[][] neurons = {{in1, in2, in3}, hiddenNeurons, {outputNeuron}};

        NeuralNetwork network = new NeuralNetwork(neurons);
        network.generateFullmesh();
        new NetworkVisualization(network, "bla");

        for (double d :
                network.computeOutputs(0, 0, 0))
            System.out.println(d);
    }
}
