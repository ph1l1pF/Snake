package Evolution.CrossOver;

import Evolution.Evolution;
import NeuralNetwork.NeuralNetwork;

public class ChromosomeCrossOver implements ICrossOver {

    @Override
    public NeuralNetwork crossOver(NeuralNetwork parent1, NeuralNetwork parent2) {

        if (parent1.equals(parent2)) {
            throw new IllegalArgumentException("Parents must not ne equal.");
        }


        System.out.println("parent 1");
        System.out.println(parent1);
        System.out.println("parent 2");
        System.out.println(parent2);

        NeuralNetwork child = Evolution.getInstance().generateRandomFullmeshNeuralNetwork();

        for (int i = 0; i < child.getNeurons().length; i++) {
            for (int k = 0; k < child.getNeurons()[i].length; k++) {

                for (int m = 0; m < child.getNeurons()[i][k].getIngoingConnections().size(); m++) {

                    if (m % 2 == 0) {
                        child.getNeurons()[i][k].getIngoingConnections().get(m).setWeight(parent1.getNeurons()[i][k].getIngoingConnections().get(m).getWeight());
                        child.getNeurons()[i][k].setBias(parent1.getNeurons()[i][k].getBias());
                    } else {
                        child.getNeurons()[i][k].getIngoingConnections().get(m).
                                setWeight(parent2.getNeurons()[i][k].getIngoingConnections().get(m).getWeight());
                        child.getNeurons()[i][k].setBias(parent2.getNeurons()[i][k].getBias());
                    }
                }
            }
        }
        return child;
    }
}
