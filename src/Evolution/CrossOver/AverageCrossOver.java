package Evolution.CrossOver;

import Evolution.Evolution;
import NeuralNetwork.NeuralNetwork;

public class AverageCrossOver implements ICrossOver {
    @Override
    public NeuralNetwork crossOver(NeuralNetwork parent1, NeuralNetwork parent2) {
        NeuralNetwork childNet = Evolution.getInstance().generateRandomFullmeshNeuralNetwork();
        for (int i = 1; i < parent1.getNeurons().length; i++) {
            for (int k = 1; k < parent1.getNeurons()[i].length; k++) {
                for (int m = 0; m < parent1.getNeurons()[i][k].getIngoingConnections().size(); m++) {
                    double w1 = parent1.getNeurons()[i][k].getIngoingConnections().get(m).getWeight();
                    double w2 = parent2.getNeurons()[i][k].getIngoingConnections().get(m).getWeight();
                    childNet.getNeurons()[i][k].getIngoingConnections().get(m).setWeight((w1 + w2) / 2.0);
                }
            }
        }
        for (int i = 0; i < parent1.getNeurons().length; i++) {
            for (int k = 0; k < parent1.getNeurons()[i].length; k++) {
                childNet.getNeurons()[i][k].setBias((parent1.getNeurons()[i][k].getBias() + parent2.getNeurons()[i][k].getBias()) / 2);
            }
        }
        return childNet;
    }
}
