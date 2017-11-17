package Evolution.Mutation;

import NeuralNetwork.Connection;
import NeuralNetwork.NeuralNetwork;
import NeuralNetwork.MathUtil;

import java.util.Random;

public class MutationImpl implements IMutation {


    /**
     * The probability that a connection will be changed during mutation
     */
    private final double PROBABILITY_CONNECTION_AFFECTED = 0.2;


    @Override
    public NeuralNetwork mutate(NeuralNetwork newNetwork) {
        //NeuralNetwork newNetwork = network.deepCopy();

        //System.out.println("mutation anfang ");
        //System.out.println(newNetwork.toStringConnections());

        Random random = new Random();

        for (int i = 0; i < newNetwork.getNeurons().length; i++) {
            for (int k = 0; k < newNetwork.getNeurons()[i].length; k++) {
                for (Connection connection : newNetwork.getNeurons()[i][k].getIngoingConnections()) {
                    if (random.nextDouble() <= PROBABILITY_CONNECTION_AFFECTED) {
                        double manipulationValue = MathUtil.randomMinusOneToOne();
                        if (random.nextBoolean()) {
                            manipulationValue /= 1.0;
                        }
                        connection.setWeight(connection.getWeight() * manipulationValue);
                    }
                }
            }
        }

        //System.out.println("mutation ende ");
        //System.out.println(newNetwork.toStringConnections());

        return newNetwork;
    }
}
