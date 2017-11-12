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
                for (int m = 0; m < neuralNetwork.getNeurons()[i][k].getIngoingConnections().size(); m++) {
                    g2.drawString(String.valueOf(neuralNetwork.getNeurons()[i][k].getIngoingConnections().get(m).getWeight()), x, y + m * 10);
                }
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
}
